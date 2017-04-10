package com.xiaoling.vuandroidsdk.okhttp.exception;

/**
 * 定义自己的异常类。用于已知异常处， 方便快速定位异常出现的位置和原因
 * Created by xiaoling on 2017/3/26.
 */

public class OkHttpException extends Exception {

    private String code;
    private String msg;

    /**
     * 网络异常
     */
    public static final String CODE_NET_ERRO = "net erro";

    /**
     * json解析异常
     */
    public static final String CODE_JSON_ERRO = "json erro";

    /**
     * 未知异常
     */
    public static final String CODE_OTHER_ERRO = "other erro";

    /**
     * 文件读写异常
     */
    public static final String CODE_IO_ERRO = "io erro";

    public OkHttpException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
