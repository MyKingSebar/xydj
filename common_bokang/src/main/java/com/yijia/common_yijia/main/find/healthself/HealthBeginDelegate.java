package com.yijia.common_yijia.main.find.healthself;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.latte.ec.R;
import com.example.yijia.delegates.LatteDelegate;

public class HealthBeginDelegate extends LatteDelegate {

    @Override
    public Object setLayout() {
        return R.layout.delegate_health_begin;
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
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    private void initView(View rootView) {
        TextView button = rootView.findViewById(R.id.health_self_begin_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportDelegate().pop();
//                ((HealthMainDelegate)getParentDelegate()).loadFragment(new HealthWaitDelegate());
                getSupportDelegate().start(new HealthWaitDelegate());

            }
        });
        ((HealthMainDelegate)getParentDelegate()).setTips(R.string.health_seft_main_warn);
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
