package com.yijia.common_yijia.main.robot.robotmain;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.text.PrecomputedTextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.delegates.bottom.BottomItemDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.TextViewUtils;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.message.MessageTabPagerAdapter;
import com.yijia.common_yijia.main.message.NoticeDataConverter;
import com.yijia.common_yijia.main.message.NoticesAdapter;
import com.yijia.common_yijia.main.message.trtc.BoKangSendMessageListener;
import com.yijia.common_yijia.main.message.trtc.BokangSendMessageUtil;
import com.yijia.common_yijia.main.message.trtc.CallWaitingActivity;
import com.yijia.common_yijia.main.robot.callsetting.RobotCallSettingDelegate;
import com.yijia.common_yijia.main.robot.setting.remind.RobotRemindSettingDelegate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RobotMyRobotDelegate extends BottomItemDelegate {
    AppCompatTextView tvCall, tvRemind, tvMessage, tvGuardianship, tvHealth, tvLiveness, tvRobotImg;
    String token = null;
    long userId=0;
    boolean isOnline=false;

    String testChatId="ee5ef61d9c2c4ed0905860c568501855";

    BokangSendMessageUtil bokangSendMessageUtil=null;

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
        getActiveness(token,userId);
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
            checkRobotLogin();
            getParentDelegate().getSupportDelegate().start(new RobotCallSettingDelegate());
        });
        tvRemind.setOnClickListener(v -> {
            //TODO 提醒设置
            if(checkRobotLogin()){
                if(userId==0){
                    showToast("网络异常id=0");
                    return;
                }
                RobotRemindSettingDelegate delegate=RobotRemindSettingDelegate.create(userId);
                getParentDelegate().getSupportDelegate().start(delegate);
            };

        });
        tvMessage.setOnClickListener(v -> {
            //TODO 语音留言
            checkRobotLogin();
        });
        tvGuardianship.setOnClickListener(v -> {
            //TODO 远程看护
            checkRobotLogin();
            final Intent intent2 = new Intent(getContext(), CallWaitingActivity.class);
            int userId = (YjDatabaseManager.getInstance().getDao().loadAll().get(0).getId()).intValue();
            intent2.putExtra("roomid",userId);
            intent2.putExtra("chatId",testChatId);
            intent2.putExtra(CallWaitingActivity.TYPE_KEY,CallWaitingActivity.TYPE_VIDEO);
            getActivity().startActivity(intent2);
            bokangSendMessageUtil.sendOnLineMessage(bokangSendMessageUtil.buildBokangMessage(MessageInfoUtil.BOKANG_VIDEO_WAIT,userId+""));
        });
        tvHealth.setOnClickListener(v -> {
            //TODO 健康记录
            checkRobotLogin();
        });

        bokangSendMessageUtil = new BokangSendMessageUtil(TIMManager.getInstance().getConversation(TIMConversationType.C2C, testChatId), new BoKangSendMessageListener() {
            @Override
            public void messageSuccess(TIMMessage timMessage) {

            }

            @Override
            public void messageError(int code, String desc) {

            }
        }, getContext());


    }
    private boolean checkRobotLogin(){
        return true;
//        if(isOnline){
//            return true;
//        }else {
//            return false;
//        }
    }

    private void getOnlineStatue(String token,long userId) {
        if (TextUtils.isEmpty(token)) {
            return;
        }
        RxRestClient.builder()
                .url("robot/query_robot_is_online")
                .params("yjtk", token)
                .params("targetUserId", userId)
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
                            boolean isOnline2=jsondata.getBoolean("isOnline");
                            Log.d("RobotMyRobotDelegate","isOnline:"+isOnline2);
                            isOnline=isOnline2;
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
    private void getActiveness(String token,long userId) {
        if (TextUtils.isEmpty(token)) {
            return;
        }
        RxRestClient.builder()
                .url("robot/query_activeness")
                .params("yjtk", token)
                .params("targetUserId", userId)
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
                            final int activeness=jsondata.getInteger("activeness");
                            Log.d("RobotMyRobotDelegate","activeness:"+activeness);
                            TextViewUtils.AppCompatTextViewSetText(tvLiveness,""+activeness);
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
