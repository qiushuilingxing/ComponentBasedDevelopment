package com.xiaoling.componentbased.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by xiaoling on 2017/4/4.
 * function:
 */

public class Utils {
    public static void showToast(Context context,String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
