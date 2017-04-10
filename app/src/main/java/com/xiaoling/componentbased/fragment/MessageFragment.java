package com.xiaoling.componentbased.fragment;

import android.content.res.AssetFileDescriptor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoling.componentbased.R;
import com.xiaoling.componentbased.databinding.FragmentMessageLayoutBinding;
import com.xiaoling.componentbased.fragment.base.BaseFragment;
import com.xiaoling.componentbased.utils.Utils;
import com.xiaoling.vuandroidsdk.widget.CommonVideoView;

import java.io.IOException;


/**
 * @Created by xiaoling on 2017/3/24.
 * @function:
 */
public class MessageFragment extends BaseFragment {
    FragmentMessageLayoutBinding binding;

    public View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_message_layout, container, false);
//        view = inflater.inflate(R.layout.fragment_message_layout, container, false);
        initData();
        return binding.getRoot();
    }

    private void initData() {
        CommonVideoView videoView = new CommonVideoView(getActivity(),binding.testVideo);
        try {
            AssetFileDescriptor fd = getContext().getAssets().openFd("cat.mp4");
            videoView.setFd(fd.getFileDescriptor());

        } catch (IOException e) {
            e.printStackTrace();
        }
//        videoView.setDataResourse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");

        binding.testVideo.addView(videoView);
        videoView.setmListner(new CommonVideoView.AdVideoPlayerListner() {
            @Override
            public void onClickFullScreen() {

            }

            @Override
            public void onClickPlay() {

            }

            @Override
            public void onClickVideo() {

            }

            @Override
            public void onClickBackBtn() {

            }

            @Override
            public void onVideoLoadSuccess() {

            }

            @Override
            public void onVideoLoadfail() {
                Utils.showToast(getContext(),"视频加载失败");
            }

            @Override
            public void onVideoLoadComplete() {

            }

            @Override
            public void onBufferUpdate(int position) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
