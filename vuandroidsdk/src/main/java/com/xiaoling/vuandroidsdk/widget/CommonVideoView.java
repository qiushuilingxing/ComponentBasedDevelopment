package com.xiaoling.vuandroidsdk.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xiaoling.vuandroidsdk.R;
import com.xiaoling.vuandroidsdk.databinding.XadsdkVideoPlayerBinding;

import java.io.FileDescriptor;
import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Created by xiaoling on 2017/3/29.
 * function:
 */

public class CommonVideoView extends RelativeLayout implements View.OnClickListener
        , MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener
        , MediaPlayer.OnCompletionListener,MediaPlayer.OnInfoListener,MediaPlayer.OnSeekCompleteListener
        , TextureView.SurfaceTextureListener {

    XadsdkVideoPlayerBinding binding;

    private ViewGroup mParentConainer;//容纳此视频view的容器
    private Context mContext;

    //Constant 常量
    public static final int TIME_MSG = 0x01;
    public static final int TIME_INAVL = 1 * 1000;//消息发送时间间隔

    //播放状态
    public static final int STATE_ERRO = -1;//播放错误
    public static final int STATE_IDLE = 0;//视频空闲
    public static final int STATE_PAUSE = 1;//暂停
    public static final int STATE_PLAYING = 2;//正在播放
    public static final int LAOD_TOTAL_COUNT = 3;//加载重连次数。当加载失败时最多允许重连3次，增强用户体验
    public int mCurrentCount;//当前重连次数
    public int playerState = STATE_IDLE;//默认视频处于空闲状态

    //视频加载数据
    public String url;//视频加载地址
    public boolean isMute; //是否静音
    public int mScreenWigth;//播放器宽度
    public int mScreenHeight;//播放器高度

    //status状态保护
    public boolean isRealPause;//是否真的暂停了
    public boolean isComplete;//是否完成
    private boolean mPlaying;//是否正在播放


    //播放相关
    private AudioManager mAudioManager;//音量控制
    private Surface mSurFace;//显示帧数据的控件
    private MediaPlayer mMediaPlayer;//视频播放类
    private AdVideoPlayerListner mListner;//view中各种控件点击事件的回调及其他回调
    private ScreenEventReceiver mScreenReceiver;//锁屏事件监听

    //ceshi
    FileDescriptor fd;


    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_MSG:
                    //通知服务器视频的最新播放进度
                    if (isPlaying()) {
                        mListner.onBufferUpdate(getCurrentPosition());
                        sendEmptyMessageDelayed(TIME_MSG, TIME_INAVL);
                    }
                    break;
            }
        }
    };


    public CommonVideoView(Context context, ViewGroup parentContainer) {
        this(context, null,0);
        mParentConainer =parentContainer;
    }

    public CommonVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    public void setDataResourse(String url) {
        this.url = url;
    }

    public void setFd(FileDescriptor fd) {
        this.fd = fd;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.xadsdk_video_player, this, true);
        binding.videoTextureView.setSurfaceTextureListener(this);
        setAllClickListner();
    }

    /**
     * 所有的点击事件在此设置
     */
    private void setAllClickListner() {

    }

    /**
     * 判断视频是否正在播放
     *
     * @return
     */
    private boolean isPlaying() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            return true;
        }
        return false;
    }

    /**
     * 获取media当前播放位置
     *
     * @return
     */
    private int getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }


    /**
     * 创建MediaPlayer并实例化
     *
     * @return
     */
    private MediaPlayer createMedia() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.reset();
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        setPlayerState(STATE_IDLE);
        if (mSurFace != null && mSurFace.isValid()) {
            mMediaPlayer.setSurface(mSurFace);
        } else {
            stop();
        }
        //TODO 添加meida初始化配置
        return mMediaPlayer;
    }



    /**
     * 来回切换页面时,根据在屏幕中的位置,判断视频是否可以播放.如果超过一半的视频控件显示在屏幕内,则可以播放
     */
    private void decideCanPlay() {


    }

    /**
     * 加载MediaPlayer
     */
    public void load() {
        if (getPlayerState() != STATE_IDLE) {
            return;
        }
        showLoadingView();
        try {
            createMedia();
            setPlayerState(STATE_IDLE);
            if (!TextUtils.isEmpty(url)) {
                mMediaPlayer.setDataSource(url);
            }
            if (fd != null) {
                mMediaPlayer.setDataSource(fd);
            }

//            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            stop();
        }
    }



    /**
     * 视频开始播放
     */
    private void resume() {
        if (getPlayerState() != STATE_PAUSE) {//非暂停状态，那么就是可能正在播放，或者失败
            return;
        }
        if (mMediaPlayer != null) {
            isRealPause = false;
            isComplete = false;
            mPlaying = true;
            setPlayerState(STATE_PLAYING);
            if (!isPlaying()) {
                mMediaPlayer.start();
                mHandler.sendEmptyMessage(TIME_MSG);
                showPauseOrPlayView(true);
            } else {
                showPauseOrPlayView(false);
            }
        }
    }

    /**
     * 视频暂停
     */
    private void pause() {
        if (getPlayerState() != STATE_PLAYING) {
            return;
        }
        setPlayerState(STATE_PAUSE);
        if (isPlaying()) {
            mMediaPlayer.pause();
            if (!mPlaying) {
                mMediaPlayer.seekTo(0);//若是无法播放状态中，暂停就把进度置为0
            }
        }
        mHandler.removeCallbacksAndMessages(null);
        showPauseOrPlayView(true);
    }

    /**
     * 视频在大屏幕时暂停
     */
    private void pauseInFullScreen() {

    }

    /**
     * 视屏停止播放
     */
    private void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();//重置视频至未初始化状态
            mMediaPlayer.setOnSeekCompleteListener(null);//播放进度条的拖动监听置空
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        mHandler.removeCallbacksAndMessages(null);//停止发送视频进度的消息

        //尝试重连
        if (mCurrentCount < LAOD_TOTAL_COUNT) {
            mCurrentCount++;
            load();
        } else {
            setPlayerState(STATE_PAUSE);
            showPauseOrPlayView(true);
            if (mListner != null) {
                mListner.onVideoLoadfail();
            }
        }
    }

    /**
     * 跳到指定地方暂停
     */
    private void seekAndPause() {

    }

    /**
     * 跳到指定地方播放
     */
    private void seekAndResume() {

    }

    /**
     * 控件显示隐藏状态变化,是View的方法
     *
     * @param changedView
     * @param visibility  value = {View.VISIBLE, View.INVISIBLE, View.GONE}
     */
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }

    /**
     * 如果触摸事件到了视频控件这里,就自己处理事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    /**
     * 点击事件监听,可以没有,因为有binding
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

    }

    /**
     * 视频是否准备就绪监听
     *
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer = mp;
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnBufferingUpdateListener(this);//开始缓冲
            mCurrentCount = 0; //重连次数重置
            if (mListner != null) {
                mListner.onVideoLoadSuccess();
            }

            //如果满足条件（有网，视频在屏幕中显示处的区域超过本身的一半）则自动播放，否则暂停
//            if (Utils.checkCanAutoPlay(getContext(), AdParameters.getCurrentSetting())
//                    && Utils.getVisiblePercent(mParentConainer) > SDKConstant.VIDEO_SCREEN_PERCENT) {
                resume();
//            } else {
//                pause();
//            }
        }
    }

    /**
     * 判断是否可以播放
     * @return
     */
    private boolean checkCanPlay() {
        return false;
    }

    /**
     * 视频错误监听
     *
     * @param mp
     * @param what
     * @param extra
     * @return true 表示自己处理异常事件
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mMediaPlayer = mp;
        setPlayerState(STATE_ERRO);
        stop();//重置mediaplayer并尝试重连
        showPauseOrPlayView(true);
        return true;
    }

    /**
     * 视频缓冲监听
     *
     * @param mp
     * @param percent
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        mMediaPlayer = mp;
    }

    /**
     * 视频播放完成监听
     *
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        mMediaPlayer = mp;
        playBack();
        isRealPause = true;
        isComplete = true;
        if (mListner != null) {
            mListner.onVideoLoadComplete();
        }
    }

    /**
     * 从头播放
     */
    private void playBack() {
        Log.d(TAG,"do playBack");
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnSeekCompleteListener(this);
            mMediaPlayer.seekTo(0);
            mMediaPlayer.pause();
            setPlayerState(STATE_PAUSE);
            showPauseOrPlayView(true);
        }
        mHandler.removeCallbacksAndMessages(null);
    }


    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "onInfo()");
        return false;
    }

    /**
     * TextureView可用监听
     *
     * @param surface
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurFace = new Surface(surface);
        createMedia();
        mMediaPlayer.setSurface(mSurFace);
        load();
    }

    /**
     * TextureView尺寸发生变化监听
     *
     * @param surface
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    /**
     * TextureView销毁监听
     *
     * @param surface
     * @return
     */
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    /**
     * TextureView更新监听
     *
     * @param surface
     */
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }


    /**
     * 对view中控件,及其他必要事件的监听
     */
    public interface AdVideoPlayerListner {
        void onClickFullScreen();

        void onClickPlay();

        void onClickVideo();

        void onClickBackBtn();

        void onVideoLoadSuccess();

        void onVideoLoadfail();

        void onVideoLoadComplete();

        void onBufferUpdate(int position); //获取视频当前播放位置
    }

    public void setmListner(AdVideoPlayerListner mListner) {
        this.mListner = mListner;
    }

    private class ScreenEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Intent.ACTION_USER_PRESENT://用户在使用手机
                    if (playerState == STATE_PAUSE) {//视频暂停
                        if (isRealPause) {//用户手动点的暂停
                            pause();
                        } else {
                            decideCanPlay();//不是用户引起的暂停,可能是视频被回收了,尝试重连
                        }
                    }
                    break;
                case Intent.ACTION_SCREEN_OFF://锁屏了,如果视频是播放的,那就暂停掉
                    if (playerState == STATE_PLAYING) {
                        pause();
                    }
                    break;
            }

        }
    }

    /**
     * 注册广播
     */
    private void registerScreenEventReceiver() {
        if (mScreenReceiver == null) {
            mScreenReceiver = new ScreenEventReceiver();
            IntentFilter filter = new IntentFilter();
            //广播只监听这两个事件
            filter.addAction(Intent.ACTION_USER_PRESENT);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            getContext().registerReceiver(mScreenReceiver, filter);
        }
    }

    /**
     * 取消注册广播
     */
    private void unRegisterScreenEventReceiver() {
        if (mScreenReceiver != null) {
            getContext().unregisterReceiver(mScreenReceiver);
        }
    }

    /**
     * 显示正在加载的页面
     */
    private void showLoadingView() {
        AnimationDrawable anim = (AnimationDrawable) binding.videoLoadingImg.getBackground();
        anim.start();
    }

    /**
     * 显示暂停或者播放画面
     * @param isPauseView true：显示暂停界面（按钮未播放按钮）  false: 显示播放界面（按钮为暂停按钮）
     */
    private void showPauseOrPlayView(boolean isPauseView) {
        binding.videoErroImg.setVisibility(View.GONE);
        if (!isPauseView) {
           binding.videoLoadingImg.clearAnimation();//正在播放视频时停止加载动画
        }
        binding.videoLoadingImg.setVisibility(isPauseView ? View.VISIBLE : View.GONE);
        binding.videoPlayImg.setVisibility(isPauseView ? View.GONE : View.VISIBLE);
    }

    /**
     * 显示加载失败页面
     */
    private void showFailView() {
        binding.videoErroImg.setVisibility(View.VISIBLE);
    }

    /**
     * 获取播放状态
     * @return
     */
    public int getPlayerState() {
        return playerState;
    }

    /**
     * 设置播放状态
     * @param playerState
     */
    public void setPlayerState(int playerState) {
        this.playerState = playerState;
    }

    public boolean isRealPause() {
        return isRealPause;
    }

    public void setRealPause(boolean realPause) {
        isRealPause = realPause;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean mute) {
        isMute = mute;
    }
}
