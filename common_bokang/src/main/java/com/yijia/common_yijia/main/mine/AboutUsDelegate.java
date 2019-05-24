package com.yijia.common_yijia.main.mine;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yijia.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.widget.HeadLayout;
import com.example.yijia.ui.TextViewUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *
 * 关于我们
 */
public class AboutUsDelegate extends LatteDelegate implements HeadLayout.OnClickHeadReturn {
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

    AppCompatTextView tvTitle, tvSave;
    RelativeLayout rl;
    @Override
    public Object setLayout() {
        return R.layout.delegate_aboutus;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        //初始化头布局
        initHead(rootView);
        //获取版本号
        setcode();
    }

    //初始化头布局
    private void initHead(View rootView) {
        rl = rootView.findViewById(R.id.tv_back);
        rl.setOnClickListener(v -> getSupportDelegate().pop());
        tvTitle = rootView.findViewById(R.id.tv_title);
        tvSave = rootView.findViewById(R.id.tv_save);
        tvSave.setVisibility(View.INVISIBLE);
        TextViewUtils.AppCompatTextViewSetText(tvTitle,"关于我们");
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

    @Override
    public void onClickHeadReturn() {
        getSupportDelegate().pop();
    }
}
