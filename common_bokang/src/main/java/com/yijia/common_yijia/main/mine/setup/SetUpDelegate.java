package com.yijia.common_yijia.main.mine.setup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.widget.HeadLayout;
import com.example.yijia.app.AccountManager;
import com.example.yijia.delegates.LatteDelegate;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.mine.GuardianshipDelegate;
import com.yijia.common_yijia.main.mine.NodisturbDelegate;
import com.yijia.common_yijia.sign.SignInDelegate;
import com.yijia.common_yijia.sign.SignInNoteOnlyDelegate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
    @BindView(R2.id.specified)
    LinearLayout specified;
    Unbinder unbinder;

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
        headLayout.setHeadName("设置", "#333333", 18);
        headLayout.setHeadlayoutBagColor("#ffffff");
        headLayout.setOnClickHeadReturn(this);

    }

    @Override
    public void onClickHeadReturn() {
        getSupportDelegate().pop();
    }

    //点击条目事件
    @OnClick({R2.id.setup_pwd, R2.id.nodisturb, R2.id.log_out,R2.id.specified,R2.id.identity})
    public void onViewClicked(View view) {
//        getSupportDelegate().pop();
        getSupportDelegate().popTo(SignInDelegate.class, false);
        int i = view.getId();
        if (i == R.id.setup_pwd) {
            //跳转到修改密码
            getSupportDelegate().start(new ChangePasswordDelegate());
        } else if (i == R.id.nodisturb) {
            //跳转到勿扰模式
            getSupportDelegate().start(new NodisturbDelegate());
        } else if (i == R.id.log_out) {
            showDialog();
        }else if (i == R.id.specified){
            getSupportDelegate().start(new GuardianshipDelegate());
        }
        //身份验证
        else if (i == R.id.identity){
            getSupportDelegate().start(new IdentityAuthenticationDelegate());
        }
    }

    //是否退出的 dialog
    @Override
    public void showDialog() {
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
                // TODO: 腾讯登出操作
                //腾讯IM登出
                TIMManager.getInstance().logout(new TIMCallBack() {
                    @Override
                    public void onError(int code, String desc) {
                        //错误码 code 和错误描述 desc，可用于定位请求失败原因
                        //错误码 code 列表请参见错误码表
                        Log.d("tengxun", "logout failed. code: " + code + " errmsg: " + desc);
                    }
                    @Override
                    public void onSuccess() {
                        //登出成功
                        Log.d("tengxun", "登出成功");
                    }
                });

                getSupportDelegate().pop();
                getSupportDelegate().startWithPop(new SignInNoteOnlyDelegate());
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
