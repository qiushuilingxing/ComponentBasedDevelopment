package com.xiaoling.vuandroidsdk.okhttp.request;

import java.io.File;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * 提供get, post,fileDownLoad, fileUpLoad等Request
 * Created by xiaoling on 2017/3/26.
 */

public class CommonRequest {

    public static final MediaType FILE_TYPE = MediaType.parse("application/octet-stream");

    public static Request createGetRequest(String url, RequestParams params, RequestParams headerParam) {

        //拼接url和参数
        StringBuilder sBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            Set<Map.Entry<String, String>> entries = params.urlParams.entrySet();
            for (Map.Entry<String, String> entry:entries) {
                sBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        String urlBuilder = sBuilder.substring(0, sBuilder.length() - 1);

        //拼接headers
        Headers.Builder hBuilder = new Headers.Builder();
        if (headerParam != null) {
            Set<Map.Entry<String, String>> entries = headerParam.urlParams.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                hBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        Headers headers = hBuilder.build();

        return new Request.Builder()
                .url(urlBuilder)
                .headers(headers)
                .get()
                .build();
    }

    public static Request createGetRequest(String url, RequestParams params) {
        return createGetRequest(url, params, null);
    }

    /**
     * post请求
     * @param url
     * @param params
     * @param headerParams
     * @return
     */
    public static Request createPostRequest(String url, RequestParams params, RequestParams headerParams) {

        //构建RequestBody
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (params != null) {
            Set<Map.Entry<String, String>> entries = params.urlParams.entrySet();
            for (Map.Entry<String, String> entry : entries ) {
                bodyBuilder.add(entry.getKey(), entry.getValue());
            }
            
        }
        FormBody body = bodyBuilder.build();

        //处理头信息
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headerParams != null) {

            Set<Map.Entry<String, String>> entries = headerParams.urlParams.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                headerBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        Headers headers = headerBuilder.build();

        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }

    public static Request createPostRequset(String url, RequestParams params) {
        return createGetRequest(url, params, null);
    }

    /**
     * 文件上传, 已经定义了头信息, 根据具体项目可修改
     * @param url
     * @param params
     * @return
     */
    public static Request createMutiPartRequst(String url, RequestParams params) {

        MultipartBody.Builder multiBuilder = new MultipartBody.Builder();
        multiBuilder.setType(MultipartBody.FORM);

        if (params != null) {

            Set<Map.Entry<String, Object>> entries = params.fileParams.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof File) {
                    multiBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\"")
                            , RequestBody.create(FILE_TYPE, (File) value));
                } else {
                    multiBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\"")
                            , RequestBody.create(null, (String) value));
                }
            }
        }

        MultipartBody multipartBody = multiBuilder.build();

        return new Request.Builder()
                .url(url)
                .post(multipartBody)
                .build();
    }
}
