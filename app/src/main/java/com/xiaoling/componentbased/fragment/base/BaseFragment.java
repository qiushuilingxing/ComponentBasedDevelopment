package com.xiaoling.componentbased.fragment.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * @Created by xiaoling on 2017/3/24.
 * @function:
 */
public class BaseFragment extends Fragment {
    public Context mContext; //fragment用到的上下文

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
