package com.yijia.common_yijia.main.find.healthself;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.yijia.common_yijia.main.find.healthself.CameraMesure.ProgressView;
import com.yijia.common_yijia.main.robot.callsetting.RobotCallSettingNickDataConverter;
import com.yijia.common_yijia.main.robot.callsetting.RobotCallSettingNickListAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HealthResultDelegate extends LatteDelegate {

    private ProgressView progressView;
    private TextView close;
    private int rate;

    @Override
    public Object setLayout() {
        return R.layout.delegate_health_result;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        rate = getArguments().getInt("result");
        initView(rootView);

        noticeServer();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    private void noticeServer() {
        String url = "health/heartrate/insert";
        RxRestClient.builder()
                .url(url)
                .params("yjtk", YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk())
                .params("heartRate", rate)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        Log.e("~~health self result", response);
                    }

                    @Override
                    public void onFail(Throwable e) {
                    }
                });
    }

    private void initView(View rootView) {
        ((HealthMainDelegate) getParentDelegate()).setTips(R.string.health_self_result_warn);

        progressView = rootView.findViewById(R.id.health_self_result_hr);
        progressView.setTitle("心率", "（次/分）");
        progressView.setData(addHRStand(), 150);
        progressView.setValue(rate);

        close = rootView.findViewById(R.id.health_self_result_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentDelegate().getSupportDelegate().pop();
            }
        });

    }

    private List<ProgressView.Point> addHRStand() {
        List<ProgressView.Point> points = new ArrayList<>();
        ProgressView.Point low = progressView.new Point();
        low.value = 60;
        low.tag = "60";
        points.add(low);

        ProgressView.Point high = progressView.new Point();
        high.value = 100;
        high.tag = "100";
        points.add(high);
        return points;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public boolean onBackPressedSupport() {
        return false;
    }

}
