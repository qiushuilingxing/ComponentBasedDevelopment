package com.xiaoling.vuandroidsdk.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import com.xiaoling.vuandroidsdk.constant.SDKConstant;

/**
 * Created by xiaoling on 2017/3/30.
 * function: 工具类
 */

public class Utils {



    /**
     * 检查是否可以自动播放
     * @param context
     * @param setting
     * @return
     */
    public static boolean checkCanAutoPlay(Context context,SDKConstant.AutoPlaySetting setting) {
        boolean result = false;
        switch (setting) {
            case AUTO_PLAY_ONLY_WIFI:
                result = true;
                break;
            case AUTO_PLAY_3G_4G_WIFI:
                result = true;
                break;
            case AUTO_PLAY_NEVER:
                result = false;
                break;
        }
        return result;
    }

    /**
     * 获取view的可见比例
     * @param view
     * @return
     */
    public static int getVisiblePercent(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);

        int visibleArea = rect.width() * rect.height();
        int viewArea = view.getWidth() * view.getHeight();
        int visiblePercent = (int) (visibleArea * 1f / viewArea * 100);

        return visiblePercent;

    }
}
