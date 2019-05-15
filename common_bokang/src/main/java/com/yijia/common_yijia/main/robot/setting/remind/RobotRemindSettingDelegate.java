package com.yijia.common_yijia.main.robot.setting.remind;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.recycler.SpacesItemDecoration;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.TextViewUtils;
import com.example.yijia.ui.dialog.JDialogUtil;
import com.example.yijia.ui.dialog.RxDialogSureCancelListener;
import com.example.yijia.ui.seekbar.RxSeekBar;
import com.example.yijia.util.callback.CallbackManager;
import com.example.yijia.util.callback.CallbackType;
import com.example.yijia.util.callback.IGlobalCallback;
import com.warkiz.tickseekbar.OnSeekChangeListener;
import com.warkiz.tickseekbar.SeekParams;
import com.warkiz.tickseekbar.TickSeekBar;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.message.trtc2.PersonalChatFragmentLittle;
import com.yijia.common_yijia.main.robot.robotmain.RobotGuardianshipDataConverter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author JiaLei, Email 15033111957@163.com, Date on 2019/5/13.
 * <p>
 * .,,       .,:;;iiiiiiiii;;:,,.     .,,
 * rGB##HS,.;iirrrrriiiiiiiiiirrrrri;,s&##MAS,
 * r5s;:r3AH5iiiii;;;;;;;;;;;;;;;;iiirXHGSsiih1,
 * .;i;;s91;;;;;;::::::::::::;;;;iS5;;;ii:
 * :rsriii;;r::::::::::::::::::::::;;,;;iiirsi,
 * .,iri;;::::;;;;;;::,,,,,,,,,,,,,..,,;;;;;;;;iiri,,.
 * ,9BM&,            .,:;;:,,,,,,,,,,,hXA8:            ..,,,.
 * ,;&@@#r:;;;;;::::,,.   ,r,,,,,,,,,,iA@@@s,,:::;;;::,,.   .;.
 * :ih1iii;;;;;::::;;;;;;;:,,,,,,,,,,;i55r;;;;;;;;;iiirrrr,..
 * .ir;;iiiiiiiiii;;;;::::::,,,,,,,:::::,,:;;;iiiiiiiiiiiiri
 * iriiiiiiiiiiiiiiii;;;::::::::::::::::;;;iiiiiiiiiiiiiiiir;
 * ,riii;;;;;;;;;;;;;:::::::::::::::::::::::;;;;;;;;;;;;;;iiir.
 * iri;;;::::,,,,,,,,,,:::::::::::::::::::::::::,::,,::::;;iir:
 * .rii;;::::,,,,,,,,,,,,:::::::::::::::::,,,,,,,,,,,,,::::;;iri
 * ,rii;;;::,,,,,,,,,,,,,:::::::::::,:::::,,,,,,,,,,,,,:::;;;iir.
 * ,rii;;i::,,,,,,,,,,,,,:::::::::::::::::,,,,,,,,,,,,,,::i;;iir.
 * ,rii;;r::,,,,,,,,,,,,,:,:::::,:,:::::::,,,,,,,,,,,,,::;r;;iir.
 * .rii;;rr,:,,,,,,,,,,,,,,:::::::::::::::,,,,,,,,,,,,,:,si;;iri
 * ;rii;:1i,,,,,,,,,,,,,,,,,,:::::::::,,,,,,,,,,,,,,,:,ss:;iir:
 * .rii;;;5r,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,sh:;;iri
 * ;rii;:;51,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,.:hh:;;iir,
 * irii;::hSr,.,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,.,sSs:;;iir:
 * irii;;:iSSs:.,,,,,,,,,,,,,,,,,,,,,,,,,,,..:135;:;;iir:
 * ;rii;;:,r535r:...,,,,,,,,,,,,,,,,,,..,;sS35i,;;iirr:
 * :rrii;;:,;1S3Shs;:,............,:is533Ss:,;;;iiri,
 * .;rrii;;;:,;rhS393S55hh11hh5S3393Shr:,:;;;iirr:
 * .;rriii;;;::,:;is1h555555h1si;:,::;;;iirri:.
 * .:irrrii;;;;;:::,,,,,,,,:::;;;;iiirrr;,
 * .:irrrriiiiii;;;;;;;;iiiiiirrrr;,.
 * .,:;iirrrrrrrrrrrrrrrrri;:.
 * ..,:::;;;;:::,,.
 */
public class RobotRemindSettingDelegate extends LatteDelegate implements SwipeRefreshLayout.OnRefreshListener {
    public static final String USERID = "userid";
    AppCompatTextView tvTitle;
    RelativeLayout rl;
    RecyclerView rv;
    com.warkiz.tickseekbar.TickSeekBar seekbar4;
    String token = null;
    long userId = 0;
    RobotRemindSettingAdapter mAdapter = null;
    private SwipeRefreshLayout mRefreshLayout = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_robot_setting_remind;
    }

    public static RobotRemindSettingDelegate create(long userid) {
        final Bundle args = new Bundle();
        args.putLong(USERID, userid);
        final RobotRemindSettingDelegate delegate = new RobotRemindSettingDelegate();
        delegate.setArguments(args);
        return delegate;
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
        mRefreshLayout = rootView.findViewById(R.id.srl_index);
        mRefreshLayout.setOnRefreshListener(this);
        rl = rootView.findViewById(R.id.tv_back);
        rl.setOnClickListener(v -> getSupportDelegate().pop());
        tvTitle = rootView.findViewById(R.id.tv_title);
        TextViewUtils.AppCompatTextViewSetText(tvTitle, "提醒设置");
        AppCompatTextView tvSave = rootView.findViewById(R.id.tv_save);
        tvSave.setVisibility(View.GONE);
        AppCompatTextView tvIcon = rootView.findViewById(R.id.tv_icon);
        tvIcon.setVisibility(View.VISIBLE);
        TextViewUtils.setBackground(getContext(), tvIcon, R.mipmap.icon_robot_remindsetting_add);
        tvIcon.setOnClickListener(v -> {
            RobotRemindSettingAddTypeDelegate mRobotRemindSettingAddTypeDelegate = RobotRemindSettingAddTypeDelegate.create(userId);
            getSupportDelegate().start(mRobotRemindSettingAddTypeDelegate);
        });
        rv = rootView.findViewById(R.id.rv);
        rv.addItemDecoration(new SpacesItemDecoration(1));
        seekbar4.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

            }

            @Override
            public void onStartTrackingTouch(TickSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(TickSeekBar seekBar) {
                int progress = seekBar.getProgress();
                setVolume(userId, progress);
            }
        });
        initCallback();
        initRefreshLayout();
        getvolume(token, userId);
        getInfo(token, userId);
    }

    private void initCallback() {
        CallbackManager.getInstance()
                .addCallback(CallbackType.ROBOT_REMIND_ADD, (IGlobalCallback<String>) args -> {
                    getInfo(token, userId);
                });
    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light

        );
        //第一个参数true：下拉的时候球由小变大，回弹时由大变小 第二个参数下降起始高度  第三个参数下降终止的高度
        mRefreshLayout.setProgressViewOffset(true, 120, 300);
    }

    private void getInfo(String token, long userId) {
        if (userId == 0) {
            showToast("网络异常");
            return;
        }
        mRefreshLayout.setRefreshing(true);
        RxRestClient.builder()
                .url("remind/query_remind_list")
                .params("yjtk", token)
                .params("targetUserId", userId)
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
                            List<MultipleItemEntity> data = new RobotRemindSettingDataConverter()
                                    .setJsonData(response)
                                    .convert();
                            initAdapter(data);
                            mRefreshLayout.setRefreshing(false);
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            mRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        mRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void getvolume(String token, long targetUserId) {
        if (targetUserId == 0) {
            showToast("网络异常");
            return;
        }
        mRefreshLayout.setRefreshing(true);
        RxRestClient.builder()
                .url("remind/query_volume")
                .params("yjtk", token)
                .params("targetUserId", targetUserId)
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
                            int volume = data.getInteger("volume");
                            seekbar4.setProgress(volume);
                            mRefreshLayout.setRefreshing(false);
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            mRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        mRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void deleteRemind(long remindId) {
        if (userId == 0) {
            showToast("网络异常");
            return;
        }
        RxRestClient.builder()
                .url("remind/delete_remind")
                .params("yjtk", token)
                .params("remindId", remindId)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final JSONObject obj = JSON.parseObject(response);
                        final String status = obj.getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            showToast("删除成功");
                            getInfo(token, userId);
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

    private void setVolume(long targetUserId, int volume) {
        if (targetUserId == 0) {
            showToast("网络异常");
            return;
        }
        RxRestClient.builder()
                .url("remind/update_volume")
                .params("yjtk", token)
                .params("targetUserId", targetUserId)
                .params("volume", volume)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final JSONObject obj = JSON.parseObject(response);
                        final String status = obj.getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            showToast("设置成功");
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

    private void updateOnOff(long remindId) {
        if (remindId == 0) {
            showToast("网络异常");
            return;
        }
        RxRestClient.builder()
                .url("remind/update_on_off")
                .params("yjtk", token)
                .params("remindId", remindId)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final JSONObject obj = JSON.parseObject(response);
                        final String status = obj.getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            showToast("切换成功");
                            getInfo(token, userId);
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


    private void initAdapter(List<MultipleItemEntity> data) {
        mAdapter = new RobotRemindSettingAdapter(data);
        mAdapter.setmRemindSettingListener(id -> {
            RobotRemindSettingAddDelegate mRobotRemindSettingAddDelegate = RobotRemindSettingAddDelegate.create(0, 0, id);
            getSupportDelegate().start(mRobotRemindSettingAddDelegate);
        });
        mAdapter.setmDeleteListener(id -> {
            JDialogUtil.INSTANCE.showRxDialogSureCancel(getContext(), null, 0, "确认删除", new RxDialogSureCancelListener() {
                @Override
                public void RxDialogSure() {
                    JDialogUtil.INSTANCE.dismiss();
                    deleteRemind(id);
                }

                @Override
                public void RxDialogCancel() {
                    JDialogUtil.INSTANCE.dismiss();
                }
            });
        });
        mAdapter.setmOpenListener((id, info) -> {
            updateOnOff(id);
        });
        final LinearLayoutManager manager = new LinearLayoutManager(Latte.getApplicationContext());
        rv.setLayoutManager(manager);
        rv.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        getInfo(token, userId);
    }
}
