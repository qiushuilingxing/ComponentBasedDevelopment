package com.xiaoling.vuandroidsdk.okhttp.listner;

/**
 * 定义文件下载场景中,网络请求结果返回后的行为
 * 继承父类DisposeDataListner，本接口除了有onProgress显示进度的行为， 还有成功和失败结果处理的行为
 * Created by xiaoling on 2017/3/26.
 */

public interface DisposeDownLoadLisnter extends DisposeDataListner {
    void onProgress(int progress);
}
