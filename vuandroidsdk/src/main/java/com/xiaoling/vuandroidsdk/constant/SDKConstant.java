package com.xiaoling.vuandroidsdk.constant;

/**
 * Created by xiaoling on 2017/3/30.
 * function:
 */

public class SDKConstant {
    //自动播放阈值
    public static int VIDEO_SCREEN_PERCENT = 50;

    public static float VIDEO_HEIGHT_PERCENT = 9 / 16.0f;

    /**
     * 自动播放设置,再设置界面用得到
     */
    public enum AutoPlaySetting{
        AUTO_PLAY_ONLY_WIFI,
        AUTO_PLAY_3G_4G_WIFI,
        AUTO_PLAY_NEVER
    }
}
