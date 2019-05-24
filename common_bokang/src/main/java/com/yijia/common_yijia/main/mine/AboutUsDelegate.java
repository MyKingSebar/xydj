package com.yijia.common_yijia.main.mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.widget.HeadLayout;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.TextViewUtils;
import com.example.yijia.util.PackageUtils;
import com.google.gson.Gson;
import com.yijia.common_yijia.bean.Status;
import com.yijia.common_yijia.bean.VersionCheckBean;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
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
    String downloadUrl = null;
    String versionName = null;
    long versionCode = 0;

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
        TextViewUtils.AppCompatTextViewSetText(tvTitle, "关于我们");
    }

    //获取版本号
    private void setcode() {
        versionCode = PackageUtils.getAppVersionCode(getContext());
        versionName = PackageUtils.getAppVersionName(getContext());
        getIsParentsConfirm();
    }

    private void getIsParentsConfirm() {
        RxRestClient.builder()
                .url("version/check")
                .params("versionName", versionName)
                .params("versionCode", versionCode)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(Latte.getApplicationContext()) {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        VersionCheckBean bean = gson.fromJson(response, VersionCheckBean.class);
                        if(bean.getStatus()== Status.SUCCESS){
                            if (bean.getData().getHasNewVersion() == 1) {
                                update(bean.getData().getUpdateUrl());
                            }
                        }

                    }

                    @Override
                    public void onFail(Throwable e) {

                    }
                });
    }

    private void update(String url) {
        AllenVersionChecker
                .getInstance()
                .downloadOnly(
                        UIData.create().setDownloadUrl(url)
                )
                .executeMission(getContext());
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

