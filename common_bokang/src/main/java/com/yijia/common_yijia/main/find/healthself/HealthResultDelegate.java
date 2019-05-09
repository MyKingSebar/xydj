package com.yijia.common_yijia.main.find.healthself;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.latte.ec.R;
import com.example.yijia.delegates.LatteDelegate;
import com.yijia.common_yijia.main.find.healthself.CameraMesure.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class HealthResultDelegate extends LatteDelegate {

    private ProgressView progressView;

    @Override
    public Object setLayout() {
        return R.layout.delegate_health_result;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        initView(rootView);
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

    private void initView(View rootView) {
        ((HealthMainDelegate) getParentDelegate()).setTips(R.string.health_self_result_warn);

        int rate = getArguments().getInt("result");
        progressView = rootView.findViewById(R.id.health_self_result_hr);
        progressView.setTitle("心率", "（次/分）");
        progressView.setData(addHRStand(), 150);
        progressView.setValue(rate);

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
