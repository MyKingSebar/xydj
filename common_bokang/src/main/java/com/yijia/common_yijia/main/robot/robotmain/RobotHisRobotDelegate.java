package com.yijia.common_yijia.main.robot.robotmain;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ec.R;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.TextViewUtils;
import com.example.yijia.util.GlideUtils;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.message.trtc.BoKangSendMessageListener;
import com.yijia.common_yijia.main.message.trtc.BokangSendMessageUtil;
import com.yijia.common_yijia.main.message.trtc.CallWaitingActivity;
import com.yijia.common_yijia.main.robot.callsetting.RobotCallSettingDelegate;
import com.yijia.common_yijia.main.robot.setting.remind.RobotRemindSettingDelegate;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RobotHisRobotDelegate extends LatteDelegate {
    public static final String USERID = "userid";
    public static final String ISADMIN = "isadmin";
    public static final String CHATID = "chatId";
    AppCompatTextView tvCall, tvRemind, tvMessage, tvGuardianship, tvHealth, tvLiveness, tvRobotImg, tvTitle;
    RelativeLayout rl;
    String token = null;
    long userId = 0;
    int isAdmin = 0;
    boolean isMine=false;
//    String chatId = null;
long myId=0;
    int hasRobot = 0;
    boolean isOnline = false;
    String tencentImUserIdRobot = null;
    BokangSendMessageUtil bokangSendMessageUtil = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_robot_hisrobot;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        assert args != null;
        userId = args.getLong(USERID);
        isAdmin = args.getInt(ISADMIN);
//        chatId = args.getString(CHATID);
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        myId=YjDatabaseManager.getInstance().getDao().loadAll().get(0).getId();
        if(myId==userId){
            isAdmin=1;
            isMine=true;
        }
        initVIew(rootView);
        getOnlineStatue(token,userId);
        getInfo(token, userId);
    }

    private void initVIew(View rootView) {
        rl = rootView.findViewById(R.id.tv_back);
        rl.setOnClickListener(v -> getSupportDelegate().pop());
        tvTitle = rootView.findViewById(R.id.tv_title);
        AppCompatTextView tvSave = rootView.findViewById(R.id.tv_save);
        tvSave.setVisibility(View.INVISIBLE);

        tvCall = rootView.findViewById(R.id.tv_call);
        tvRemind = rootView.findViewById(R.id.tv_remind);
        tvMessage = rootView.findViewById(R.id.tv_message);
        tvGuardianship = rootView.findViewById(R.id.tv_guardianship);
        tvHealth = rootView.findViewById(R.id.tv_health);
        tvLiveness = rootView.findViewById(R.id.tv_liveness);
        tvRobotImg = rootView.findViewById(R.id.tv_robotimg);


        tvCall.setOnClickListener(v -> {
            //TODO 呼叫设置
            if (!checkAdmin()) {
                showToast("您不是管理员，无法进行该操作");
                return;
            }
            if(isMine){
                getSupportDelegate().start(new RobotCallSettingDelegate());
            }else {
                showToast("只有本人可以设置");
            }
        });
        tvRemind.setOnClickListener(v -> {
            //TODO 提醒设置
            if (!checkAdmin()) {
                showToast("您不是管理员，无法进行该操作");
                return;
            }
            if (userId == 0) {
                showToast("网络异常id=0");
                return;
            }
            RobotRemindSettingDelegate delegate = RobotRemindSettingDelegate.create(userId);
            getSupportDelegate().start(delegate);
        });
        tvMessage.setOnClickListener(v -> {
            //TODO 语音留言
        });
        tvGuardianship.setOnClickListener(v -> {
            //TODO 远程看护
            if (!checkAdmin()) {
                showToast("您不是管理员，无法进行该操作");
                return;
            }
            if (!checkRobotLogin()) {
                return;
            }
            final Intent intent2 = new Intent(getContext(), CallWaitingActivity.class);
//            int userId = (YjDatabaseManager.getInstance().getDao().loadAll().get(0).getId()).intValue();
            intent2.putExtra("roomid", (int)myId);
            intent2.putExtra("chatId", tencentImUserIdRobot);
            intent2.putExtra(CallWaitingActivity.TYPE_KEY, CallWaitingActivity.TYPE_KANHU);
            getActivity().startActivity(intent2);
            bokangSendMessageUtil.sendOnLineMessage(bokangSendMessageUtil.buildBokangMessage(MessageInfoUtil.BOKANG_MONITOR_WAIT, userId + ""));
        });
        tvHealth.setOnClickListener(v -> {
            //TODO 健康记录
            if (!checkAdmin()) {
                showToast("您不是管理员，无法进行该操作");
                return;
            }
        });
    }

    private boolean checkAdmin() {
        if (isAdmin != 1) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkRobotLogin() {
        return isOnline;
//        if (TextUtils.isEmpty(tencentImUserIdRobot) || TextUtils.isEmpty(tencentImUserId)) {
//            return false;
//        } else {
//            return true;
//        }
    }
    private void getOnlineStatue(String token, long userId) {
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
                            boolean isOnline2 = jsondata.getBoolean("isOnline");
                            Log.d("RobotMyRobotDelegate", "isOnline:" + isOnline2);
                            isOnline = isOnline2;
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(Latte.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {

                    }
                });
    }

    private void getInfo(String token, long userId) {
        if (userId == 0) {
            showToast("网络异常");
            return;
        }
        RxRestClient.builder()
                .url("user/query_user_info")
                .params("yjtk", token)
                .params("type", 1)
                .params("keyword", "" + userId)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final JSONObject obj = JSON.parseObject(response);
                        final String status = obj.getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            JSONObject data = obj.getJSONObject("data");
                            JSONObject user = data.getJSONObject("user");
                            if (null == user){
                                return;
                            }
                            String nickname = user.getString("nickname");
                            String imagePath = user.getString("imagePath");
                            long id = user.getLong("id");
                            String phone = user.getString("phone");
                            String email = user.getString("email");
                            //用户状态：1-正常，2-注销
                            int userStatus = user.getInteger("userStatus");
                            String tencentImUserId = user.getString("tencentImUserId");
                             tencentImUserIdRobot = user.getString("tencentImUserIdRobot");
                            String tencentImUserSig = user.getString("tencentImUserSig");
                            String inviteCode = user.getString("inviteCode");

//                            switch (robotOnline) {
//                                case 1:
//                                    isOnline = true;
//                                    break;
//                                case 2:
//                                    isOnline = false;
//                                    break;
//                                default:
//                                    break;
//                            }
//                            TextViewUtils.AppCompatTextViewSetText(tvLiveness, activeness + "");
                            TextViewUtils.AppCompatTextViewSetText(tvTitle, nickname + "的小壹");
                            bokangSendMessageUtil = new BokangSendMessageUtil(TIMManager.getInstance().getConversation(TIMConversationType.C2C, tencentImUserIdRobot), new BoKangSendMessageListener() {
                                @Override
                                public void messageSuccess(TIMMessage timMessage) {

                                }

                                @Override
                                public void messageError(int code, String desc) {

                                }
                            }, getContext());
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
