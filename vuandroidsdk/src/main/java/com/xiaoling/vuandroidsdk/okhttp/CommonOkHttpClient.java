package com.xiaoling.vuandroidsdk.okhttp;


import com.xiaoling.vuandroidsdk.okhttp.https.HttpsUtils;
import com.xiaoling.vuandroidsdk.okhttp.listner.DisposeDataHandle;
import com.xiaoling.vuandroidsdk.okhttp.listner.DisposeDataListner;
import com.xiaoling.vuandroidsdk.okhttp.request.CommonRequest;
import com.xiaoling.vuandroidsdk.okhttp.request.RequestParams;
import com.xiaoling.vuandroidsdk.okhttp.response.FileCallBack;
import com.xiaoling.vuandroidsdk.okhttp.response.JsonCallBack;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * okHttp请求
 * Created by xiaoling on 2017/3/26.
 */

public class CommonOkHttpClient {
    public static OkHttpClient mOkHttpClient;
    //超时时间
    public static final int TIME_OUT = 30;

    static {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        //设置超时范围
        clientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        clientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);

        /**
         *  为所有请求添加请求头，看个人需求
         */
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("User-Agent", "Imooc-Mobile") // 标明发送本次请求的客户端
                        .build();
                return chain.proceed(request);
            }
        });

        //允许重定向
        clientBuilder.followRedirects(true);

        //支持https请求 信任所有证书
        clientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        clientBuilder.sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager());//ssl设置

        //构建okhttpClient
        mOkHttpClient = clientBuilder.build();
    }

    public static OkHttpClient getMOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 参数最全的get请求
     * @param url 请求网址 如:https://www.baidu.com/   其实可以再封装
     * @param params 请求参数
     * @param headers 头信息
     * @param mClazz javabean 服务器返回的完整json. 如含code,message,reponse
     * @param listner 请求结果回调监听
     * @return 返回Call对象用于在Activity的onDestroy方法. 场景: 请求未完成时用户退出Activity - onDestroy中销毁Call释放资源. 避免虽然退出页面仍请求网络占用内存
     */
    public static Call get(String url, RequestParams params, RequestParams headers, Class<?> mClazz, DisposeDataListner listner) {

        Request request = CommonRequest.createGetRequest(url, params, headers);
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new JsonCallBack(new DisposeDataHandle(mClazz,listner)));
        return call;
    }

    public static Call get(String url, RequestParams params, RequestParams headers, DisposeDataListner listner) {

        return get(url, params, headers, null, listner);
    }

    public static Call get(String url, RequestParams params, Class<?> mClazz, DisposeDataListner listner) {

        return get(url, params, null, mClazz, listner);
    }

    public static Call get(String url, RequestParams params, DisposeDataListner listner) {

        return get(url, params, null, null, listner);
    }

    /**
     * 最全的post请求
     * @param url
     * @param params
     * @param headers
     * @param mClazz
     * @param listner
     * @return
     */
    public static Call post(String url, RequestParams params, RequestParams headers, Class<?> mClazz, DisposeDataListner listner) {

        Request request = CommonRequest.createPostRequest(url, params, headers);
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new JsonCallBack(new DisposeDataHandle(mClazz,listner)));
        return call;
    }

    /**
     * 下载文件,用的get请求,文件解析
     * @param url
     * @param path
     * @param listner
     * @return
     */
    public static Call downLoadFile(String url, String path, DisposeDataListner listner) {
        Request request = CommonRequest.createGetRequest(url, null);
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new FileCallBack(new DisposeDataHandle(path,listner)));
        return call;
    }

    /**
     * 上传文件
     * @param url
     * @param params
     * @param listner
     * @return
     */
    public static Call upLoadFile(String url, RequestParams params, DisposeDataListner listner) {
        Request request = CommonRequest.createMutiPartRequst(url, params);
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new JsonCallBack(new DisposeDataHandle(listner)));
        return call;
    }
}
