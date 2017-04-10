package com.xiaoling.vuandroidsdk.okhttp.listner;

import java.util.ArrayList;

/**
 * 处理网络请求结果中返回的头信息
 * Created by xiaoling on 2017/3/26.
 */

public interface DisposeCookieListner extends DisposeDataListner {
    public static final String COOKIE_STORE = "Set-Cookie"; //具体开发中这是由服务器定义的,比如微联定义这个字段为:req

    void onCookies(ArrayList<String> cookies);
}
