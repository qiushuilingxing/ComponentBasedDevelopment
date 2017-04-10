package com.xiaoling.componentbased.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.xiaoling.componentbased.R;

/**
 * Created by xiaoling on 2017/3/27.
 * function: 图片下载工具类. 工具类对外提供的方法一般用static修饰
 */

public class ImageLoaderManager {

    public static ImageLoaderManager mInstance;
    public static ImageLoader imageLoader;

    //设置ImageLoader参数常亮
    public static final int THRED_POOL = 4;//允许线程池中最多有加载4张图片的线程
    public static final int PRIORITY = 2;//优先级,获取系统定义的优先级-此优先级= 图片加载的优先级. 因为app中文本优先级最高,图片不需要那么高优先级
    public static final int CONNECTION_TIME_OUT = 5 * 1000; //连接超时
    public static final int READ_TIME_OUT = 30 * 1000;//读超时
    public static final int MEMORY_CACH_SIZE = 2 * 1024 * 1024;//允许占用的内存为2兆,可以不用设置,内存不够时,弱引用缓存设置会自动首先清空内存中的图片
    public static final int DISK_CACHE_SIZE = 5 * 1024 * 1024;//允许占用的硬盘空间为50兆

    private ImageLoaderManager(Context context) {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .threadPoolSize(THRED_POOL)
                .threadPriority(Thread.NORM_PRIORITY - PRIORITY)
                .denyCacheImageMultipleSizesInMemory()
                //.memoryCache(new UsingFreqLimitedMemoryCache(MEMORY_CACHE_SIZE))
                .memoryCache(new WeakMemoryCache())//使用弱引用内存缓存, 当内存不足时就会首先清空内存中的图片缓存
                .diskCacheSize(DISK_CACHE_SIZE)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(getDefaultOptions())
                .imageDownloader(new BaseImageDownloader(context, CONNECTION_TIME_OUT, READ_TIME_OUT))
                .writeDebugLogs()
                .build();


        ImageLoader.getInstance().init(config);
        imageLoader = ImageLoader.getInstance();
    }

    /**
     * 定义默认的图片显示option,可设置图片的缓存策略，编解码方式等，非常重要
     *
     * @return
     */
    private DisplayImageOptions getDefaultOptions() {

        DisplayImageOptions options = new
                DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.default_user_avatar)
                .showImageOnFail(R.mipmap.default_user_avatar)
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中, 重要，否则图片不会缓存到内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中, 重要，否则图片不会缓存到硬盘中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .decodingOptions(new BitmapFactory.Options())//设置图片的解码配置
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .build();
        return options;
    }

    public static ImageLoaderManager getInstance(Context context) {

        if (mInstance == null) {
            synchronized (mInstance.getClass()) {
                if (mInstance == null) {
                    mInstance = new ImageLoaderManager(context);
                }
            }
        }

        return mInstance;
    }

    /**
     * 显示图片
     *
     * @param url              图片地址
     * @param imageView        显示图片的控件
     * @param options          图片显示策略
     * @param listener         图片加载监听
     * @param progressListener 图片下载进度监听
     */
    public static void displayImage(String url, ImageView imageView, DisplayImageOptions options
            , ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {

        if (imageLoader != null) {
            imageLoader.displayImage(url, imageView, options, listener, progressListener);
        }
    }

    public static void displayImage(String url, ImageView imageView, ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {

        displayImage(url, imageView, null, listener, progressListener);
    }

    public static void displayImage(String url, ImageView imageView, ImageLoadingProgressListener progressListener) {

        displayImage(url, imageView, null, progressListener);
    }

    public static void displayImage(String url, ImageView imageView) {
        displayImage(url, imageView, null);
    }
}

