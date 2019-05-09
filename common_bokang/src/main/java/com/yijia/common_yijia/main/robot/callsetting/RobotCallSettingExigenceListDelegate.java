package com.yijia.common_yijia.main.robot.callsetting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.util.log.LatteLogger;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.friends.CommonClickListener;
import com.yijia.common_yijia.main.friends.CommonClickLongStringListener;
import com.yijia.common_yijia.main.robot.robotmain.RobotHisRobotDelegate;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RobotCallSettingExigenceListDelegate extends LatteDelegate implements CommonClickListener ,SwipeRefreshLayout.OnRefreshListener{
    RecyclerView mRecyelerView=null;
    String token = null;
    SwipeRefreshLayout mRefreshLayout = null;
    RobotCallSettingExigenceListAdapter mAdapter=null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_robot_callsetting_friendlist;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        token=YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        initVIew(rootView);

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRefreshLayout();
        Log.d("refresh", "onLazyInitView");
        getFriends();
    }

    private void initVIew(View rootView) {
        mRecyelerView = rootView.findViewById(R.id.rv_guardianship);
        mRefreshLayout=rootView.findViewById(R.id.srl_index);
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
        RobotHisRobotDelegate mDelegate=new RobotHisRobotDelegate();
        Bundle bundle=new Bundle();
        long id=Long.parseLong(info);
        if(id==0){
            showToast("网络异常id=0");
            return;
        }
        bundle.putLong(RobotHisRobotDelegate.USERID,id);
        mDelegate.setArguments(bundle);
        getParentDelegate().getSupportDelegate().start(mDelegate);
    }

    @Override
    public void onRefresh() {
        getFriends();
    }

    public void getFriends() {
        mRefreshLayout.setRefreshing(true);
        String url = "call/query_call_list";
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("type", 1)
                .params("pageNo", 1)
                .params("pageSize", 500)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.json("USER_FRIENDS", response);
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            List<MultipleItemEntity> data = new RobotCallSettingExigenceListDataConverter()
                                    .setJsonData(response)
                                    .convert();
                            if(data==null){
                                return;
                            }
                            mAdapter = new RobotCallSettingExigenceListAdapter(data);
                            mAdapter.setCommonClickListener((id,info) -> getParentDelegate().getSupportDelegate().start(RobotCallSettingNickNameDelegate.creat(id,info)));
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
}
