package com.yijia.common_yijia.main.mine.setup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.example.latte.app.AccountManager;
import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.widget.HeadLayout;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.mine.NodisturbDelegate;
import com.yijia.common_yijia.sign.SignInDelegate;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置页面
 */
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

    //初始化头布局
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
    //点击条目事件
    @OnClick({R2.id.setup_pwd, R2.id.nodisturb, R2.id.log_out})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.setup_pwd) {
            //跳转到修改密码
            getSupportDelegate().start(new ChangePasswordDelegate());
        } else if (i == R.id.nodisturb) {
            //跳转到勿扰模式
            getSupportDelegate().start(new NodisturbDelegate());
        } else if (i == R.id.log_out) {
            showDialog();
        }
    }

    //是否退出的 dialog
    private void showDialog() {
//        TextView msg = new TextView(_mActivity);
//        msg.setText("是否确定退出？");
//        msg.setPadding(10, 10, 10, 10);
//        msg.setGravity(Gravity.CENTER);

        AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity);
        // builder.setView(msg);
        builder.setMessage("是否确定退出？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AccountManager.setIsComplete(false);
                AccountManager.setSignState(false);
                YjDatabaseManager.getInstance().getDao().deleteAll();
                getSupportDelegate().pop();
                getSupportDelegate().startWithPop(new SignInDelegate());
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}