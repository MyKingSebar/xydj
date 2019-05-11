package com.yijia.common_yijia.main.find.healthself;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                            .PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager
                                    .PERMISSION_GRANTED) {

                    } else {
                        //不具有获取权限，需要进行权限申请
                        requestPermissions(new String[]{
                                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
                        return;
                    }

                }

//                getSupportDelegate().pop();
//                ((HealthMainDelegate)getParentDelegate()).loadFragment(new HealthWaitDelegate());
                HealthWaitDelegate waitDelegate = new HealthWaitDelegate();
//                FragmentAnimator animator = new FragmentAnimator(R.anim.h_fragment_enter,R.anim.h_fragment_exit,R.anim.h_fragment_pop_enter,R.anim.h_fragment_pop_exit);
//                setFragmentAnimator(animator);
//                ((ProxyActivity)getActivity()).setFragmentAnimator(a);
                getSupportDelegate().startWithPop(waitDelegate);
            }
        });
        ((HealthMainDelegate) getParentDelegate()).setTips(R.string.health_seft_main_warn);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11) {
            getSupportDelegate().pop();
//                ((HealthMainDelegate)getParentDelegate()).loadFragment(new HealthWaitDelegate());
            getSupportDelegate().start(new HealthWaitDelegate());
        }
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
