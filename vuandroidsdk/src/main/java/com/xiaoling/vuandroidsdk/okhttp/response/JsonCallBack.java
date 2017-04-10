package com.xiaoling.vuandroidsdk.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.xiaoling.vuandroidsdk.okhttp.exception.OkHttpException;
import com.xiaoling.vuandroidsdk.okhttp.listner.DisposeCookieListner;
import com.xiaoling.vuandroidsdk.okhttp.listner.DisposeDataHandle;
import com.xiaoling.vuandroidsdk.okhttp.listner.DisposeDataListner;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * 把json结果发送到ui线程
 * Created by xiaoling on 2017/3/26.
 */

public class JsonCallBack implements Callback {

    //把json结果发送到ui线程
    Handler mDeliveryHandler;
    DisposeDataListner mListner;
    Class<?> mClazz;

    public JsonCallBack(DisposeDataHandle handle) {
        mListner = handle.mListner;
        mClazz = handle.mClazz;
        mDeliveryHandler = new Handler(Looper.getMainLooper());//获取主线程
    }

    @Override

    public void onFailure(Call call, final IOException e) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListner.onFaile(new OkHttpException(OkHttpException.CODE_NET_ERRO,e.getMessage()));
            }
        });

    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {

        final String result = response.body().string();
        final ArrayList<String> cookiesList = handleCookies(response.headers());

        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);

                //如果是头信息的回调
                if (mListner instanceof DisposeCookieListner) {
                    ((DisposeCookieListner) mListner).onCookies(cookiesList);
                }
            }
        });
    }

    /**
     * 获取头信息,并处理成list
     * @param headers
     * @return
     */
    private ArrayList<String> handleCookies(Headers headers) {
        ArrayList<String> cookies = new ArrayList<>();
        for (int i = 0; i < headers.size(); i++) {
            String name = headers.name(i);
            String value = headers.value(i);
            if (name.equalsIgnoreCase(DisposeCookieListner.COOKIE_STORE)) {
                cookies.add(headers.value(i));
            }
        }
        return cookies;
    }

    /**
     * 处理json，返回Object， 可以qiang'hzuan强转为Class<?>类
     * am response
     *
     * @return
     */
    private void handleResponse(String result) {

        if (TextUtils.isEmpty(result)) {
            mListner.onFaile(new OkHttpException(OkHttpException.CODE_NET_ERRO,"response.body为空"));
            return ;
        }

        if (mClazz == null) {
            mListner.onSuccess(result);
            return;
        }

        try {
            Object obj = JSONObject.parseObject(result, mClazz);
            if (obj == null) {
                mListner.onFaile(new OkHttpException(OkHttpException.CODE_JSON_ERRO, "解析json得到的实体类为空"));
            } else {
                mListner.onSuccess(obj);
            }
        } catch (Exception e) {
            mListner.onFaile(new OkHttpException(OkHttpException.CODE_OTHER_ERRO,"未知异常"+e.getMessage()));
        }

    }

}
