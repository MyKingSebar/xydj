package com.yijia.common_yijia.main.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.widget.HeadLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ChangePasswordDelegate extends LatteDelegate implements HeadLayout.OnClickHeadReturn {
    @BindView(R2.id.head_layout)
    HeadLayout headLayout;
    @BindView(R2.id.original_pwd)
    EditText originalPwd;
    @BindView(R2.id.new_pwd)
    EditText newPwd;
    @BindView(R2.id.confirm_pwd)
    EditText confirmPwd;

    @Override
    public Object setLayout() {
        return R.layout.delegate_setpassword;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initHead();
        initEdit();
    }

    private void initEdit() {
    }

    private void initHead() {
        headLayout.setHeadleftImg(true, R.mipmap.fanhui);
        headLayout.setHeadName("修改密码", "#FDBA63", 18);
        headLayout.setHeadlayoutBagColor("#ffffff");
        headLayout.setOnClickHeadReturn(this);
    }

    @Override
    public void onClickHeadReturn() {
        getSupportDelegate().pop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (headLayout!=null){
            headLayout = null;
        }
    }
}
