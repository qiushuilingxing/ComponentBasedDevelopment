package com.xiaoling.componentbased.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.xiaoling.componentbased.R;
import com.xiaoling.componentbased.databinding.ActivityMainBinding;
import com.xiaoling.componentbased.fragment.HomeFragment;
import com.xiaoling.componentbased.fragment.MessageFragment;
import com.xiaoling.componentbased.fragment.MineFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private FragmentManager fm;

    Fragment mHomeFragment;
    Fragment mMessageFragment;
    Fragment mMineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        initData();
    }

    private void initData() {
        fm = getSupportFragmentManager();
        setInitialFragment();
        setOnClickListner();
    }


    /**
     * 设置默认fragment
     */
    private void setInitialFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        mHomeFragment = new HomeFragment();
        ft.replace(R.id.contentRl,mHomeFragment, "home");
        setTabHighLight(binding.homeTabImg,R.mipmap.comui_tab_home_selected);
        ft.commit();
    }

    /**
     * 点击事件
     */
    private void setOnClickListner() {


        binding.homeRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了home", Toast.LENGTH_LONG).show();
                FragmentTransaction ft = fm.beginTransaction();
                hideOtherFragment(ft,mMessageFragment,mMineFragment);

                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    ft.add(R.id.contentRl,mHomeFragment, "home");
                } else {
                    ft.show(mHomeFragment);
                }
                ft.commit();
                setTabHighLight(binding.homeTabImg,R.mipmap.comui_tab_home_selected);
            }
        });
        binding.messageRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction ft = fm.beginTransaction();
                hideOtherFragment(ft,mHomeFragment,mMineFragment);
                if (mMessageFragment == null) {
                    mMessageFragment = new MessageFragment();
                    ft.add(R.id.contentRl,mMessageFragment, "message");
                } else {
                    ft.show(mMessageFragment);
                }
                ft.commit();
                setTabHighLight(binding.messageTabImg,R.mipmap.comui_tab_message_selected);
            }
        });
        binding.mineRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fm.beginTransaction();
                hideOtherFragment(ft,mHomeFragment,mMessageFragment);
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                    ft.add(R.id.contentRl,mMineFragment, "mine");
                }else {
                    ft.show(mMineFragment);
                }
                ft.commit();
                setTabHighLight(binding.mineTabImg,R.mipmap.comui_tab_person_selected);
            }
        });



    }

    /**
     * 隐藏其他fragment
     */
    private void hideOtherFragment(FragmentTransaction ft, Fragment hideOne, Fragment hideTwo) {
        if (hideOne != null) {
            ft.hide(hideOne);
        }
        if (hideTwo != null) {
            ft.hide(hideTwo);
        }
    }

    /**
     * 所有的tab背景变为默认， 指定图片高亮
     * @param view 高亮的图片
     * @param resourceId 高亮图片的资源id
     */
    private void setTabHighLight(ImageView view, int resourceId) {
        binding.homeTabImg.setBackgroundResource(R.mipmap.comui_tab_home);
        binding.messageTabImg.setBackgroundResource(R.mipmap.comui_tab_message);
        binding.mineTabImg.setBackgroundResource(R.mipmap.comui_tab_person);

        view.setBackgroundResource(resourceId);
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
