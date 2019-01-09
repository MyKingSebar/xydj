package com.yijia.common_yijia.main.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.widget.HeadLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class SetUpDelegate extends LatteDelegate implements HeadLayout.OnClickHeadReturn {
    @BindView(R2.id.head_layout)
    HeadLayout headLayout;
    @BindView(R2.id.setup_pwd)
    LinearLayout setupPwd;
    @BindView(R2.id.nodisturb)
    LinearLayout nodisturb;
    @BindView(R2.id.log_out)
    LinearLayout logOut;

    @Override
    public Object setLayout() {
        return R.layout.delegate_setup;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initHead();
    }

    private void initHead() {
        headLayout.setHeadleftImg(true, R.mipmap.fanhui);
        headLayout.setHeadName("设置", "#FDBA63", 18);
        headLayout.setHeadlayoutBagColor("#ffffff");
        headLayout.setOnClickHeadReturn(this);

    }

    @Override
    public void onClickHeadReturn() {
        getSupportDelegate().pop();
    }
    @OnClick({R2.id.setup_pwd, R2.id.nodisturb, R2.id.log_out})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.setup_pwd) {
            Toast.makeText(_mActivity, "修改密码", Toast.LENGTH_SHORT).show();
            getSupportDelegate().start(new ChangePasswordDelegate());
        } else if (i == R.id.nodisturb) {
            Toast.makeText(_mActivity, "勿扰模式", Toast.LENGTH_SHORT).show();

        } else if (i == R.id.log_out) {
            Toast.makeText(_mActivity, "退出登录", Toast.LENGTH_SHORT).show();

        }
    }
}
