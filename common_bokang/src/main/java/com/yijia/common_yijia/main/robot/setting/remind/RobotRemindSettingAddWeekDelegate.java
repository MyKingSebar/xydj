package com.yijia.common_yijia.main.robot.setting.remind;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.TextViewUtils;
import com.example.yijia.util.callback.CallbackManager;
import com.example.yijia.util.callback.CallbackType;
import com.example.yijia.util.callback.IGlobalCallback;
import com.yijia.common_yijia.database.YjDatabaseManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author JiaLei, Email 15033111957@163.com, Date on 2019/5/13.
 * <p>
 * ,s555SB@@&
 * :9H####@@@@@Xi
 * 1@@@@@@@@@@@@@@8
 * ,8@@@@@@@@@B@@@@@@8
 * :B@@@@X3hi8Bs;B@@@@@Ah,
 * ,8i                  r@@@B:     1S ,M@@@@@@#8;
 * 1AB35.i:               X@@8 .   SGhr ,A@@@@@@@@S
 * 1@h31MX8                18Hhh3i .i3r ,A@@@@@@@@@5
 * ;@&i,58r5                 rGSS:     :B@@@@@@@@@@A
 * 1#i  . 9i                 hX.  .: .5@@@@@@@@@@@1
 * sG1,  ,G53s.              9#Xi;hS5 3B@@@@@@@B1
 * .h8h.,A@@@MXSs,           #@H1:    3ssSSX@1
 * s ,@@@@@@@@@@@@Xhi,       r#@@X1s9M8    .GA981
 * ,. rS8H#@@@@@@@@@@#HG51;.  .h31i;9@r    .8@@@@BS;i;
 * .19AXXXAB@@@@@@@@@@@@@@#MHXG893hrX#XGGXM@@@@@@@@@@MS
 * s@@MM@@@hsX#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&,
 * :GB@#3G@@Brs ,1GM@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@B,
 * .hM@@@#@@#MX 51  r;iSGAM@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@8
 * :3B@@@@@@@@@@@&9@h :Gs   .;sSXH@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@:
 * s&HA#@@@@@@@@@@@@@@M89A;.8S.       ,r3@@@@@@@@@@@@@@@@@@@@@@@@@@@r
 * ,13B@@@@@@@@@@@@@@@@@@@5 5B3 ;.         ;@@@@@@@@@@@@@@@@@@@@@@@@@@@i
 * 5#@@#&@@@@@@@@@@@@@@@@@@9  .39:          ;@@@@@@@@@@@@@@@@@@@@@@@@@@@;
 * 9@@@X:MM@@@@@@@@@@@@@@@#;    ;31.         H@@@@@@@@@@@@@@@@@@@@@@@@@@:
 * SH#@B9.rM@@@@@@@@@@@@@B       :.         3@@@@@@@@@@@@@@@@@@@@@@@@@@5
 * ,:.   9@@@@@@@@@@@#HB5                 .M@@@@@@@@@@@@@@@@@@@@@@@@@B
 * ,ssirhSM@&1;i19911i,.             s@@@@@@@@@@@@@@@@@@@@@@@@@@S
 * ,,,rHAri1h1rh&@#353Sh:          8@@@@@@@@@@@@@@@@@@@@@@@@@#:
 * .A3hH@#5S553&@@#h   i:i9S          #@@@@@@@@@@@@@@@@@@@@@@@@@A.
 * <p>
 * <p>
 * 又看源码，看你妹妹呀！
 */
public class RobotRemindSettingAddWeekDelegate extends LatteDelegate {
    AppCompatTextView tvTitle;
    RecyclerView rv;
    RelativeLayout rl;
    RobotRemindSettingAddWeekAdapter mAdapter = null;
    ArrayList<Integer> whatday = null;
    private static final String WHATDAY = "whatday";

    @Override
    public Object setLayout() {
        return R.layout.delegate_robot_setting_remind_add_week;
    }

    public static RobotRemindSettingAddWeekDelegate create(ArrayList<Integer> whatday) {
        final Bundle args = new Bundle();
        args.putIntegerArrayList(WHATDAY, whatday);
        final RobotRemindSettingAddWeekDelegate delegate = new RobotRemindSettingAddWeekDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        assert args != null;
        whatday = args.getIntegerArrayList(WHATDAY);
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initVIew(rootView);

    }

    private void initVIew(View rootView) {
        rl = rootView.findViewById(R.id.tv_back);
        rl.setOnClickListener(v -> {
            beforeExit();
            getSupportDelegate().pop();
        });
        tvTitle = rootView.findViewById(R.id.tv_title);
        TextViewUtils.AppCompatTextViewSetText(tvTitle, "重复");
        AppCompatTextView tvSave = rootView.findViewById(R.id.tv_save);
        tvSave.setVisibility(View.GONE);
        rv = rootView.findViewById(R.id.rv);
        List<MultipleItemEntity> data = new RobotRemindSettingAddWeekDataConverter()
                .setJsonData("")
                .convert();
        initAdapter(data);
    }

    @Override
    public boolean onBackPressedSupport() {
        beforeExit();
        return super.onBackPressedSupport();
    }

    private void beforeExit() {
        boolean[] booleans = mAdapter.getChecks();
        StringBuffer sb = new StringBuffer();
        int size = booleans.length;
        for (int i = 0; i < size; i++) {
            if (booleans[i]) {
                if (sb.length() != 0) {
                    sb.append(",");
                }
                sb.append(i + 1 + "");
            }
        }
        final IGlobalCallback<String> callback;
        callback = CallbackManager
                .getInstance()
                .getCallback(CallbackType.ROBOT_REMIND_WEEK);
        if (callback != null) {
            callback.executeCallback(sb.toString());
        }
    }

    private void initAdapter(List<MultipleItemEntity> data) {
        mAdapter = new RobotRemindSettingAddWeekAdapter(data, whatday);
        final LinearLayoutManager manager = new LinearLayoutManager(Latte.getApplicationContext());
        rv.setLayoutManager(manager);
        rv.setAdapter(mAdapter);
    }


}
