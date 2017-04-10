package com.xiaoling.vuandroidsdk.okhttp.listner;

/**
 * 定义JSON格式数据请求,或者文件下载请求场景中，请求结果返回后的行为
 * Created by xiaoling on 2017/3/26.
 */

public interface DisposeDataListner {

    void onFaile(Object object);

    void onSuccess(Object object);
}
