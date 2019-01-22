package com.yijia.common_yijia.main.mine;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.widget.HeadLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *
 * 关于我们
 */
public class AboutUsDelegate extends LatteDelegate {
    @BindView(R2.id.head_layout)
    HeadLayout headLayout;
    @BindView(R2.id.yijialog)
    ImageView yijialog;
    @BindView(R2.id.yijiabanben)
    TextView yijiabanben;
    @BindView(R2.id.newversionimg)
    ViewStub newversionimg;
    @BindView(R2.id.newversionimglayot)
    LinearLayout newversionimglayot;
    @BindView(R2.id.function_introduction)
    TextView functionIntroduction;

    @Override
    public Object setLayout() {
        return R.layout.delegate_aboutus;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        //获取版本号
        setcode();
    }
    //获取版本号
    private void setcode() {
        PackageManager manager = _mActivity.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(_mActivity.getPackageName(), 0);
            code = info.versionCode;
            yijiabanben.setText("壹家 "+code);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R2.id.newversionimglayot, R2.id.function_introduction})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.newversionimglayot) {
            //todo 版本更新
        } else if (i == R.id.function_introduction) {
            //todo 功能介绍
        }
    }
}
