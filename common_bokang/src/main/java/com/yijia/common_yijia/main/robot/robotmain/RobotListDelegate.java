package com.yijia.common_yijia.main.robot.robotmain;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.bottom.BottomItemDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.friends.CommonLongIntClickListener;
import com.yijia.common_yijia.main.index.YjRobotListMultipleFields;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RobotListDelegate extends BottomItemDelegate implements CommonLongIntClickListener {

    private ImageView imageButton;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private RobotMainListReFreshHandler robotMainListReFreshHandler = null;
    private String token = null;
    private List<MultipleItemEntity> data = new ArrayList<>();
    private RobotListAdapter mAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_robot_list;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        initView(rootView);
        robotMainListReFreshHandler = RobotMainListReFreshHandler.create(refreshLayout, recyclerView, null, this, this, token);
    }

    private void initView(View rootView) {
        imageButton = rootView.findViewById(R.id.robot_list_add);
        imageButton.setOnClickListener(v -> getParentDelegate().getSupportDelegate().start(new AddParentsDelegate()));

        refreshLayout = rootView.findViewById(R.id.robot_list_refresh_layout);
        recyclerView = rootView.findViewById(R.id.robot_list_recycleview);
        LinearLayoutManager manager = new LinearLayoutManager(Latte.getApplicationContext());
        recyclerView.setLayoutManager(manager);
        mAdapter = new RobotListAdapter(data);
        mAdapter.setmRobotListClickListener(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRefreshLayout();
        getOnLineStatus();
        robotMainListReFreshHandler.addOwn(mAdapter, data, false);
        robotMainListReFreshHandler.firstPage(mAdapter, data);
    }

    private void getOnLineStatus() {
        RxRestClient.builder()
                .url("robot/query_robot_is_online")
                .params("yjtk", token)
                .params("targetUserId", YjDatabaseManager.getInstance().getDao().loadAll().get(0).getId())
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(Latte.getApplicationContext()) {
                    @Override
                    public void onResponse(String r) {
                        final JSONObject object = JSON.parseObject(r);
                        final String status = object.getString("status");
                        boolean isOnline = false;
                        if (TextUtils.equals(status, "1001")) {
                            JSONObject jo = object.getJSONObject("data");
                            isOnline = jo.getBoolean("isOnline");
                        }
                        data.get(0).setField(YjRobotListMultipleFields.ONLINE, isOnline ? 1 : 2);
                        mAdapter.notifyItemChanged(1);

                    }

                    @Override
                    public void onFail(Throwable e) {
                    }
                });
    }

    private void initRefreshLayout() {
        refreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light

        );
        //第一个参数true：下拉的时候球由小变大，回弹时由大变小 第二个参数下降起始高度  第三个参数下降终止的高度
        refreshLayout.setProgressViewOffset(true, 120, 300);
    }


    @Override
    public void commonClick(long id, int tyoe) {
        RobotHisRobotDelegate mDelegate = new RobotHisRobotDelegate();
        Bundle bundle = new Bundle();
        if (id == 0) {
            showToast("网络异常id=0");
            return;
        }
        bundle.putLong(RobotHisRobotDelegate.USERID, id);
        bundle.putInt(RobotHisRobotDelegate.PERMISSIONTYPE, tyoe);
        mDelegate.setArguments(bundle);
        getParentDelegate().getSupportDelegate().start(mDelegate);
    }
}
