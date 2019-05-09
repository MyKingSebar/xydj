package com.yijia.common_yijia.main.robot.callsetting;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.TextViewUtils;
import com.example.yijia.ui.dialog.JDialogUtil;
import com.example.yijia.util.log.LatteLogger;
import com.yijia.common_yijia.database.YjDatabaseManager;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RobotCallSettingAddFriendDelegate extends LatteDelegate {

    AppCompatTextView tvTitle = null;
    AppCompatTextView tvSave = null;
    RelativeLayout tvBack = null;
    AppCompatEditText et_name=null;
    AppCompatEditText et_phone=null;
    private String token = null;

    @Override
    public Object setLayout() {
        return R.layout.delegete_robot_callsetting_addfriend;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        initView(rootView);
    }

    private void initView(View rootView) {
        tvTitle = rootView.findViewById(R.id.tv_title);
        tvSave = rootView.findViewById(R.id.tv_save);
        tvBack = rootView.findViewById(R.id.tv_back);
        et_name = rootView.findViewById(R.id.et_name);
        et_phone = rootView.findViewById(R.id.et_phone);
        TextViewUtils.AppCompatTextViewSetText(tvTitle,"添加通讯录");
        TextViewUtils.AppCompatTextViewSetText(tvSave,"确定");
        tvBack.setOnClickListener(v-> getSupportDelegate().pop());
        tvSave.setOnClickListener(v->{
            String name=et_name.getText().toString();
            if(TextUtils.isEmpty(name)){
                showToast("请输入要添加人的用户名");
                return;
            }
            String phone=et_phone.getText().toString();
            if(TextUtils.isEmpty(phone)){
                showToast("请输入要添加人的手机号");
                return;
            }
            addFriend(name,phone);
        });
    }

    public void addFriend(String name,String phone) {
        JDialogUtil.INSTANCE.showRxDialogShapeLoading(getContext());
        String url = "call/insert_call";
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("callNickname", name)
                .params("callPhone", phone)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.json("insert_call", response);
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            JDialogUtil.INSTANCE.dismiss();
                            showToast("添加成功");
                            getSupportDelegate().pop();
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            JDialogUtil.INSTANCE.dismiss();
                        }

                    }

                    @Override
                    public void onFail(Throwable e) {
                        JDialogUtil.INSTANCE.dismiss();
                    }
                });
    }

}
