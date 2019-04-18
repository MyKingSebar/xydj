package com.yijia.common_yijia.main.robot;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ec.R;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.yijia.common_yijia.database.YjDatabaseManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RobotHisRobotDelegate extends LatteDelegate {
    AppCompatTextView tvCall, tvRemind, tvMessage, tvGuardianship, tvHealth, tvLiveness, tvRobotImg;
    String token = null;
    long userId=0;

    @Override
    public Object setLayout() {
        return R.layout.delegate_robot_myrobot;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        token=YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        userId=YjDatabaseManager.getInstance().getDao().loadAll().get(0).getId();
        initVIew(rootView);
        getOnlineStatue(token,userId);
    }

    private void initVIew(View rootView) {
        tvCall = rootView.findViewById(R.id.tv_call);
        tvRemind = rootView.findViewById(R.id.tv_remind);
        tvMessage = rootView.findViewById(R.id.tv_message);
        tvGuardianship = rootView.findViewById(R.id.tv_guardianship);
        tvHealth = rootView.findViewById(R.id.tv_health);
        tvLiveness = rootView.findViewById(R.id.tv_liveness);
        tvRobotImg = rootView.findViewById(R.id.tv_robotimg);
        tvCall.setOnClickListener(v -> {
            //TODO 呼叫设置
        });
        tvRemind.setOnClickListener(v -> {
            //TODO 提醒设置
        });
        tvMessage.setOnClickListener(v -> {
            //TODO 语音留言
        });
        tvGuardianship.setOnClickListener(v -> {
            //TODO 远程看护
        });
        tvHealth.setOnClickListener(v -> {
            //TODO 健康记录
        });
    }

    private void getOnlineStatue(String token,long id) {
        if (TextUtils.isEmpty(token)) {
            return;
        }
        RxRestClient.builder()
                .url("/robot/query_robot_is_online")
                .params("yjtk", token)
                .params("targetUserId", 1)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(Latte.getApplicationContext()) {
                    @Override
                    public void onResponse(String response) {
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            final JSONObject jsondata = JSON.parseObject(response).getJSONObject("data");
                            final boolean isOnline=jsondata.getBoolean("isOnline");
                            Log.d("RobotMyRobotDelegate","isOnline:"+isOnline);
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(Latte.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(Latte.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
