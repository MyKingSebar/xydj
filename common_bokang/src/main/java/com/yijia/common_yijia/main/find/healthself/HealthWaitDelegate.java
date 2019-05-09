package com.yijia.common_yijia.main.find.healthself;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

import com.example.latte.ec.R;
import com.example.yijia.delegates.LatteDelegate;

public class HealthWaitDelegate extends LatteDelegate {

    private int time = 4;

    @Override
    public Object setLayout() {
        return R.layout.delegate_health_wait;
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
        ((HealthMainDelegate)getParentDelegate()).setTips(R.string.health_self_wait_warn);
        Chronometer chronometer = rootView.findViewById(R.id.health_self_wait_text);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                chronometer.setText(getString(R.string.health_self_wait_text, --time));
                if(0 == time) {
//                    HealthWaitDelegate.this.get
                    chronometer.stop();
                }
            }
        });
        chronometer.start();
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
