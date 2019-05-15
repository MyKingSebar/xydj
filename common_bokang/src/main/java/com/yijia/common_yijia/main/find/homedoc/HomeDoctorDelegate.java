package com.yijia.common_yijia.main.find.homedoc;

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
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.google.gson.Gson;
import com.yijia.common_yijia.database.YjDatabaseManager;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeDoctorDelegate extends LatteDelegate implements  SwipeRefreshLayout.OnRefreshListener {
    RecyclerView mRecyelerView = null;
    String token = null;
    SwipeRefreshLayout mRefreshLayout = null;
    HomeDoctorAdapter mAdapter = null;
    AppCompatTextView tvTitle = null;
    AppCompatTextView tvSave = null;
    RelativeLayout tvBack = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_homedoctor;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        initVIew(rootView);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRefreshLayout();
        Log.d("refresh", "onLazyInitView");
        getHomedoc();
    }

    private void initVIew(View rootView) {
        tvTitle = rootView.findViewById(R.id.tv_title);
        tvSave = rootView.findViewById(R.id.tv_save);
        tvBack = rootView.findViewById(R.id.tv_back);
        tvTitle.setText("家庭医生");
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



    public void getHomedoc() {
        mRefreshLayout.setRefreshing(true);
        String url = "doct/query_doct_team";
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(Latte.getApplicationContext()) {
                    @Override
                    public void onResponse(String response) {
                        final JSONObject object = JSON.parseObject(response);
                        final String status = object.getString("status");
                        Log.e("jialei", "query_doct_team" + new Gson().toJson(response));
                        if (TextUtils.equals(status, "1001")) {
                            List<MultipleItemEntity> data = new HomeDoctorDataConverter()
                                    .setJsonData(response)
                                    .convert();
                            if(data==null){
                                return;
                            }
                            mAdapter = new HomeDoctorAdapter(data);
                            mAdapter.setCommonClickListener(info -> getSupportDelegate().start(HomeDoctorInDelegate.create(info)));
//                            mAdapter.setOnLoadMoreListener(HomeDoctorDelegate.this, mRecyelerView);
                            final LinearLayoutManager manager = new LinearLayoutManager(Latte.getApplicationContext());
                            mRecyelerView.setLayoutManager(manager);
                            mRecyelerView.setAdapter(mAdapter);
                            mRefreshLayout.setRefreshing(false);
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(Latte.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            mRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
//                        Toast.makeText(Latte.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        mRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onRefresh() {
        getHomedoc();
    }

}
