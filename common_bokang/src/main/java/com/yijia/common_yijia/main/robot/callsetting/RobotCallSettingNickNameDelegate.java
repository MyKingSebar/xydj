package com.yijia.common_yijia.main.robot.callsetting;

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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.dialog.JDialogUtil;
import com.example.yijia.util.log.LatteLogger;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.friends.CommonClickListener;
import com.yijia.common_yijia.main.robot.robotmain.RobotHisRobotDelegate;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RobotCallSettingNickNameDelegate extends LatteDelegate implements CommonClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyelerView = null;
    private String token = null;
    private SwipeRefreshLayout mRefreshLayout = null;
    private RobotCallSettingNickListAdapter mAdapter = null;
    private long friendUserId = 0;
    private String name = null;
    static final String PUTKEY_FRIENDUSERID = "friendUserId";
    static final String PUTKEY_NAME = "name";
    AppCompatTextView tvTitle = null;
    AppCompatTextView tvSave = null;
    RelativeLayout tvBack = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_robot_callsetting_nicklist;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        initView(rootView);

    }

    public static RobotCallSettingNickNameDelegate creat(long friendUserId,String name) {
        final Bundle args = new Bundle();
        args.putLong(PUTKEY_FRIENDUSERID, friendUserId);
        args.putString(PUTKEY_NAME, name);
        RobotCallSettingNickNameDelegate delegate = new RobotCallSettingNickNameDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle datas = getArguments();
        assert datas != null;
        friendUserId = datas.getLong(PUTKEY_FRIENDUSERID);
        name = datas.getString(PUTKEY_NAME);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRefreshLayout();
        Log.d("refresh", "onLazyInitView");
        getFriends();
    }

    private void initView(View rootView) {
        tvTitle = rootView.findViewById(R.id.tv_title);
        tvSave = rootView.findViewById(R.id.tv_save);
        tvBack = rootView.findViewById(R.id.tv_back);
        tvTitle.setText(name);
        tvSave.setVisibility(View.INVISIBLE);
        tvBack.setOnClickListener(v-> getSupportDelegate().pop());
        mRecyelerView = rootView.findViewById(R.id.rv_guardianship);
        mRefreshLayout = rootView.findViewById(R.id.srl_index);
        mRefreshLayout.setOnRefreshListener(this);
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


    @Override
    public void commonClick(String info) {
        RobotHisRobotDelegate mDelegate = new RobotHisRobotDelegate();
        Bundle bundle = new Bundle();
        long id = Long.parseLong(info);
        if (id == 0) {
            showToast("网络异常id=0");
            return;
        }
        bundle.putLong(RobotHisRobotDelegate.USERID, id);
        mDelegate.setArguments(bundle);
        getParentDelegate().getSupportDelegate().start(mDelegate);
    }

    @Override
    public void onRefresh() {
        getFriends();
    }

    public void getFriends() {
        mRefreshLayout.setRefreshing(true);
        String url = "friend/query_friend_nickname";
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("friendUserId", friendUserId)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.json("USER_FRIENDS", response);
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            List<MultipleItemEntity> data = new RobotCallSettingNickDataConverter()
                                    .setJsonData(response)
                                    .convert();
                            if (data == null) {
                                return;
                            }
                            mAdapter = new RobotCallSettingNickListAdapter(data);
                            mAdapter.setmDeleteListener(info -> {
                                if (TextUtils.isEmpty(info)) {
                                    return;
                                }
                                long id = Long.parseLong(info);
                                deleteNick(id);
                            });
                            mAdapter.setmOkListener(info -> {
                                if (TextUtils.isEmpty(info)) {
                                    showToast("请输入昵称！");
                                    return;
                                }
                                addNick(info);
                            });
                            final LinearLayoutManager manager = new LinearLayoutManager(Latte.getApplicationContext());
                            mRecyelerView.setLayoutManager(manager);
                            mRecyelerView.setAdapter(mAdapter);
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

    public void addNick(String info) {
        JDialogUtil.INSTANCE.showRxDialogShapeLoading(getContext());
        String url = "friend/insert_friend_nickname";
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("friendUserId", friendUserId)
                .params("friendNickname", info)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.json("insert_friend_nickname", response);
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            JDialogUtil.INSTANCE.dismiss();
                            showToast("添加成功");
                            getFriends();
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

    public void deleteNick(long id) {
        JDialogUtil.INSTANCE.showRxDialogShapeLoading(getContext());
        String url = "friend/delete_friend_nickname";
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("friendNicknameId", id)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.json("insert_friend_nickname", response);
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            JDialogUtil.INSTANCE.dismiss();
                            showToast("删除成功");
                            getFriends();
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
