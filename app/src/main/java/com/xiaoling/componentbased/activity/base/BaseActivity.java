package com.xiaoling.componentbased.activity.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * @Created by xiaoling on 2017/3/24.
 * @function:
 */
public class BaseActivity extends AppCompatActivity {
    public String TAG;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        TAG = getComponentName().getShortClassName();
        Log.d(TAG, "componentName:" +TAG + "getClassName:" + getClass().getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
