package com.xiaoling.vuandroidsdk.okhttp.listner;

/**
 * 提供定义网络请求返回结果行为的listner
 * 提供javabean
 * 提供下载文件路径
 * Created by xiaoling on 2017/3/26.
 */

public class DisposeDataHandle {
    /**
     * 网络请求结果回调监听
     */
    public DisposeDataListner mListner;

    /**
     * 含code,message,response的完整实体类.即服务器返回的完整json格式
     */
    public Class<?> mClazz;

    /**
     * 要下载的文件名
     */
    public String mFilePath;

    /**
     * 只有网络请求结果回调监听
     * @param mListner
     */
    public DisposeDataHandle(DisposeDataListner mListner) {

        this.mListner = mListner;
    }

    /**
     * 提供解析json的javabean
     * 提供网络请求结果回调监听
     * @param mListner
     * @param mClazz
     */
    public DisposeDataHandle(Class<?> mClazz, DisposeDataListner mListner) {

        this.mListner = mListner;
        this.mClazz = mClazz;
    }

    /**
     * 提供文件下载的路径
     * 提供网络请求结果回调监听
     * @param mFilePath
     * @param mListner
     */
    public DisposeDataHandle(String mFilePath,DisposeDataListner mListner) {

        this.mListner = mListner;
        this.mFilePath = mFilePath;
    }
}
