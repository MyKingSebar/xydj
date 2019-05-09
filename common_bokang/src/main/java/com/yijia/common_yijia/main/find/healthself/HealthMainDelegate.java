package com.yijia.common_yijia.main.find.healthself;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.yijia.delegates.LatteDelegate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HealthMainDelegate extends LatteDelegate {

    @BindView(R2.id.tv_back)
    RelativeLayout tvBack;
    @BindView(R2.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R2.id.tv_save)
    AppCompatTextView tvSave;
    @BindView(R2.id.health_self_warn_text)
    TextView healthSelfWarnText;
    @BindView(R2.id.health_self_view_cardview)
    CardView healthSelfViewCardview;
    Unbinder unbinder;

    @Override
    public Object setLayout() {
        return R.layout.delegate_health_seif;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        initView();

        getSupportDelegate().loadRootFragment(R.id.health_self_view_cardview, new HealthBeginDelegate());
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void initView() {
        tvTitle.setText(R.string.health_seft_test);
        tvSave.setVisibility(View.GONE);
    }

    public void setTips(int tip) {
        healthSelfWarnText.setText(tip);
    }

    public void loadFragment(LatteDelegate latteDelegate) {
        getSupportDelegate().loadRootFragment(R.id.health_self_view_cardview, latteDelegate);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R2.id.tv_back)
    public void onViewClicked() {
        getSupportDelegate().pop();
    }

    @Override
    public boolean onBackPressedSupport() {
        return super.onBackPressedSupport();
    }
}
