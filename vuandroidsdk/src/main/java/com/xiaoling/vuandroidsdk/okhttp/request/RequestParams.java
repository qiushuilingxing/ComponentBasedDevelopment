package com.xiaoling.vuandroidsdk.okhttp.request;

import android.text.TextUtils;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static android.R.attr.value;

/**
 * Created by xiaoling on 2017/3/26.
 */

public class RequestParams {

    //请求json格式数据场景用的参数
    public Map<String, String> urlParams = new HashMap<>();

    //文件上传用的
    public Map<String, Object> fileParams = new HashMap<>();


    public void put(String key, String value) {

        urlParams.put(key, value);
    }

    public void put(String key) throws FileNotFoundException {

        if (!TextUtils.isEmpty(key)) {
            fileParams.put(key, value);
        }
    }

    public RequestParams(Map<String, String> map) {

        if (map != null) {
            Set<Map.Entry<String, String>> entries = map.entrySet();
            for (Map.Entry<String, String> entry:entries) {
                put(entry.getKey(),entry.getValue());
            }
        }
    }

    public RequestParams(final String key, final String value) {

        this(new HashMap<String, String>(){
            {
                put(key, value);
            }
        });
    }

    public RequestParams() {

        this(null);
    }

    /**
     * 判断是否有参数
     * @return
     */
    public boolean hasParams() {
        if (urlParams.size() > 0 || fileParams.size() > 0) {
            return true;
        } else {
            return false;
        }
    }


}
