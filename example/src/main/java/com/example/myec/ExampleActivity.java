package com.example.myec;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import com.example.latte.activities.ProxyActivity;
import com.example.latte.app.AccountManager;
import com.example.latte.app.IUserChecker;
import com.example.latte.app.Latte;
import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.launcher.LauncherDelegate;
import com.example.latte.ui.launcher.ILauncherListener;
import com.example.latte.ui.launcher.OnLauncherFinishTag;
import com.yijia.common_yijia.sign.ISignListener;
import com.yijia.common_yijia.sign.SignInDelegate;
import com.yijia.common_yijia.sign.SignUpSecondDelegate;
import com.yijia.common_yijia.sign.YjBottomDelegate;

import qiu.niorgai.StatusBarCompat;

public class ExampleActivity extends ProxyActivity implements
        ISignListener,
        ILauncherListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Latte.getConfigurator().withActivity(this);
        StatusBarCompat.translucentStatusBar(this, true);
    }

    @Override
    public LatteDelegate setRootDelegate() {
//        return new ExampleDelegate();
//        return new SignInDelegate();
//        return new YjBottomDelegate();
        return new LauncherDelegate();
//        return new SignUpDelegate();
//        return new LauncherScrollDelegate();
    }

    @Override
    public void onSignInSuccess() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
//        getSupportDelegate().startWithPop(new YjBottomDelegate());
        AccountManager.checkAccont(new IUserChecker() {
            @Override
            public void onSignIn() {
                getSupportDelegate().startWithPop(new SignInDelegate());
            }

            @Override
            public void onNoSignIn() {
                getSupportDelegate().startWithPop(new SignUpSecondDelegate());
            }

        });
    }

    @Override
    public void onSignUpSecondSuccess() {
        getSupportDelegate().startWithPop(new YjBottomDelegate());

    }

    @Override
    public void onSignInFailure(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSignUpSuccess() {
//        Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();

        AccountManager.checkAccont(new IUserChecker() {
            @Override
            public void onSignIn() {
                getSupportDelegate().startWithPop(new SignInDelegate());
            }

            @Override
            public void onNoSignIn() {
                getSupportDelegate().startWithPop(new SignUpSecondDelegate());
            }

        });
    }

    @Override
    public void onSignUpFailure(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onLauncherFinish(OnLauncherFinishTag tag) {
        Toast.makeText(this, "onLauncherFinish" + tag, Toast.LENGTH_LONG).show();
        switch (tag) {
            case SIGNED:
                Toast.makeText(this, "启动成功，用户登录了", Toast.LENGTH_LONG).show();
                getSupportDelegate().startWithPop(new YjBottomDelegate());
                break;
            case NOT_SIGNED:
                Toast.makeText(this, "启动成功，用户没登录", Toast.LENGTH_LONG).show();
//                startWithPop(new SignInDelegate());
                getSupportDelegate().startWithPop(new SignInDelegate());
                break;
            default:
                break;
        }
    }

}
