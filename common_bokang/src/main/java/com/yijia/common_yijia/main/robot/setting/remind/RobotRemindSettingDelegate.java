package com.yijia.common_yijia.main.robot.setting.remind;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ec.R;
import com.example.latte.ui.recycler.SpacesItemDecoration;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.TextViewUtils;
import com.example.yijia.ui.seekbar.RxSeekBar;
import com.yijia.common_yijia.database.YjDatabaseManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RobotRemindSettingDelegate extends LatteDelegate {
    public static final String USERID="userid";
    AppCompatTextView tvTitle;
    RelativeLayout rl;
    RecyclerView rv;
    RxSeekBar seekbar4;
    String token = null;
    long userId = 0;

    @Override
    public Object setLayout() {
        return R.layout.delegate_robot_setting_remind;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        assert args != null;
        userId = args.getLong(USERID);
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        initVIew(rootView);

    }

    private void initVIew(View rootView) {
        seekbar4 = rootView.findViewById(R.id.seekbar4);
        rl = rootView.findViewById(R.id.tv_back);
        rl.setOnClickListener(v -> getSupportDelegate().pop());
        tvTitle = rootView.findViewById(R.id.tv_title);
        TextViewUtils.AppCompatTextViewSetText(tvTitle,"提醒设置");
        AppCompatTextView tvSave = rootView.findViewById(R.id.tv_save);
        tvSave.setVisibility(View.INVISIBLE);
        rv=rootView.findViewById(R.id.rv);
        rv.addItemDecoration(new SpacesItemDecoration(1));
        seekbar4.setOnRangeChangedListener(new RxSeekBar.OnRangeChangedListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onRangeChanged(RxSeekBar view, float min, float max, boolean isFromUser) {
                Log.d("RobotRemindSettingDelegate","seekbar4:"+",seekBar:"+view+",min"+min+",max:"+max+",isFromUser:"+isFromUser);
            }
        });
    }

//    private void getInfo(String token, long userId) {
//        if (userId == 0) {
//            showToast("网络异常");
//            return;
//        }
//        RxRestClient.builder()
//                .url("/user/query_user_info")
//                .params("yjtk", token)
//                .params("type", 1)
//                .params("keyword", "" + userId)
//                .build()
//                .get()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserver<String>(getContext()) {
//                    @Override
//                    public void onResponse(String response) {
//                        final JSONObject obj = JSON.parseObject(response);
//                        final String status = obj.getString("status");
//                        if (TextUtils.equals(status, "1001")) {
//                            JSONObject data = obj.getJSONObject("data");
//                            JSONObject user = data.getJSONObject("user");
//                            String nickname = user.getString("nickname");
//                            String imagePath = user.getString("imagePath");
//                            long id = user.getLong("id");
//                            String phone = user.getString("phone");
//                            String email = user.getString("email");
//                            //用户状态：1-正常，2-注销
//                            int userStatus = user.getInteger("userStatus");
//                            String tencentImUserId = user.getString("tencentImUserId");
//                            String tencentImUserSig = user.getString("tencentImUserSig");
//                            String inviteCode = user.getString("inviteCode");
//                            //是否有机器人：1-是，-2否
//                            int hasRobot = user.getInteger("hasRobot");
//                            //活跃度
//                            int activeness = user.getInteger("activeness");
//                            //机器人在线状态：1-是，2-否
//                            int robotOnline = user.getInteger("robotOnline");
//                            TextViewUtils.AppCompatTextViewSetText(tvLiveness, activeness + "");
//                            TextViewUtils.AppCompatTextViewSetText(tvTitle, nickname + "的小壹");
//                        } else {
//                            final String msg = JSON.parseObject(response).getString("msg");
//                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Throwable e) {
//                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

}
