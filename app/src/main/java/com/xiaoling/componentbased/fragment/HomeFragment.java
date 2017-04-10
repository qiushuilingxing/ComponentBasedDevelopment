package com.xiaoling.componentbased.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoling.componentbased.R;
import com.xiaoling.componentbased.databinding.FragmentHomeLayoutBinding;
import com.xiaoling.componentbased.fragment.base.BaseFragment;
import com.xiaoling.vuandroidsdk.okhttp.CommonOkHttpClient;
import com.xiaoling.vuandroidsdk.okhttp.exception.OkHttpException;
import com.xiaoling.vuandroidsdk.okhttp.listner.DisposeDataListner;

import static android.content.ContentValues.TAG;

/**
 * @Created by xiaoling on 2017/3/24.
 * @function:
 */
public class HomeFragment extends BaseFragment {
    public View view;
    FragmentHomeLayoutBinding binding;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_home_layout, container, false);
        initData();
        return binding.getRoot();
    }

    private void initData() {
        binding.testHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testOkhttp();

            }
        });
    }

    String content = null;
    private void testOkhttp() {

        String url = "http://imooc.com/api/config/check_update.php";
        CommonOkHttpClient.get(url, null, new DisposeDataListner() {
            @Override
            public void onFaile(Object object) {
                if (object instanceof OkHttpException) {
                    OkHttpException okHttpException = (OkHttpException) object;
                    okHttpException.getCode();
                    Log.d(TAG, "网络请求失败:" + okHttpException.getCode() + ",自定义失败信息:" + okHttpException.getMsg());
                } else {
                    Exception exception= (Exception) object;
                    Log.d(TAG, "网络请求失败, 系统Exception"+exception.getMessage());
                }
            }

            @Override
            public void onSuccess(Object object) {
                Log.d(TAG, "网络请求成功"+object);
                content = object.toString();
                binding.testHome.setText(content);
            }
        });
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
