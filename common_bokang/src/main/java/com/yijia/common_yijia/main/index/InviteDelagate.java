package com.yijia.common_yijia.main.index;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.commcon_xfyun.Lat;
import com.example.commcon_xfyun.LatCallbackInterface;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.dialog.JDialogUtil;
import com.example.yijia.util.callback.CallbackManager;
import com.example.yijia.util.callback.CallbackType;
import com.example.yijia.util.callback.IGlobalCallback;
import com.example.yijia.util.log.LatteLogger;
import com.google.gson.Gson;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.index.friendcircle.choosefriend.LetterchoosefriendDelegate;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class InviteDelagate extends LatteDelegate {
    @BindView(R2.id.et_text)
    AppCompatEditText etText;
    @BindView(R2.id.et_title)
    AppCompatEditText etTitle;
    @BindView(R2.id.tv_recipients)
    AppCompatTextView tv_recipients;

    private long friendId = 0;



    @OnClick(R2.id.tv_back)
    void back() {
        getSupportDelegate().pop();
    }




    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }




    private final static String TAG = InviteDelagate.class.getSimpleName();


    @Override
    public Object setLayout() {
        return R.layout.delegate_invite;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        init();
    }


    private void init() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
