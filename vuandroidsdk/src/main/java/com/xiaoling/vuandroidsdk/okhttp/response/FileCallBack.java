package com.xiaoling.vuandroidsdk.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.xiaoling.vuandroidsdk.okhttp.exception.OkHttpException;
import com.xiaoling.vuandroidsdk.okhttp.listner.DisposeDataHandle;
import com.xiaoling.vuandroidsdk.okhttp.listner.DisposeDownLoadLisnter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 把文件下载的结果推送给UI线程
 * Created by xiaoling on 2017/3/26.
 */

public class FileCallBack implements Callback {

    public static final int PROGRESS_MSG = 0;
    private Handler mDeliveryHandle;
    private DisposeDownLoadLisnter mListner;
    private String mPath;

    private int mProgress;//文件下载进度

    public FileCallBack(DisposeDataHandle handle) {

        mListner = (DisposeDownLoadLisnter) handle.mListner;
        mPath = handle.mFilePath;
        mDeliveryHandle = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PROGRESS_MSG:
                        mListner.onProgress(mProgress);//把下载进度回调出去
                        break;
                }
            }
        };
    }

    @Override

    public void onFailure(Call call, final IOException e) {

        mDeliveryHandle.post(new Runnable() {
            @Override
            public void run() {
                mListner.onFaile(new OkHttpException(OkHttpException.CODE_NET_ERRO,e.getMessage()));
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {

        mDeliveryHandle.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(response);
            }
        });
    }

    private void handleResponse(Response response) {

        if (response == null) {
            mListner.onFaile(new OkHttpException(OkHttpException.CODE_IO_ERRO,"response为null"));
            return;
        }

        File file;
        InputStream inputStream = null;
        FileOutputStream fos = null;
        int length;
        byte[] buffer = new byte[2048];
        long currentLength = 0;
        long sumLength = 0;

        try {
            file = new File(mPath);
            fos = new FileOutputStream(file);
            inputStream = response.body().byteStream();
            sumLength = response.body().contentLength();

            while ((length = inputStream.read()) != -1) {
                fos.write(buffer,0,length);
                currentLength += length;
                mProgress = (int) ((currentLength * 1f / sumLength) * 100);
                mDeliveryHandle.obtainMessage(PROGRESS_MSG, mProgress).sendToTarget();
            }
            fos.flush();

            if (file == null) {
                mListner.onFaile(new OkHttpException(OkHttpException.CODE_IO_ERRO, "下载路径对应文件为null"));
            } else {
                mListner.onSuccess(file);
            }
        } catch (Exception e) {
            mListner.onFaile(new OkHttpException(OkHttpException.CODE_IO_ERRO,"io异常"));
            e.printStackTrace();
        }finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                mListner.onFaile(new OkHttpException(OkHttpException.CODE_IO_ERRO,"io异常"));
            }
        }


    }
}
