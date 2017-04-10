package com.xiaoling.componentbased.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoling.componentbased.R;
import com.xiaoling.componentbased.fragment.base.BaseFragment;


/**
 * @Created by xiaoling on 2017/3/24.
 * @function:
 */
public class MineFragment extends BaseFragment {
    private View view ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine_layout,container,false);
        return view;
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
