package com.bokang.yijia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.latte.activities.ProxyActivity;
import com.example.latte.app.AccountManager;
import com.example.latte.app.IUserChecker;
import com.example.latte.app.Latte;
import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.launcher.LauncherDelegate;
import com.example.latte.net.rx.BaseObserver;
import com.example.latte.net.rx.RxRestClient;
import com.example.latte.ui.launcher.ILauncherListener;
import com.example.latte.ui.launcher.OnLauncherFinishTag;
import com.example.latte.util.log.LatteLogger;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.sign.ISignListener;
import com.yijia.common_yijia.sign.SignInDelegate;
import com.yijia.common_yijia.sign.SignInNoteOnlyDelegate;
import com.yijia.common_yijia.sign.SignUpSecondDelegate;
import com.yijia.common_yijia.main.YjBottomDelegate;


import cn.jpush.android.api.JPushInterface;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.Fragmentation;
import qiu.niorgai.StatusBarCompat;

public class ExampleActivity extends ProxyActivity implements
        ISignListener,
        ILauncherListener {
    String TAG = "ExampleActivity.DEBUG";
public static boolean isShowFlout=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Latte.getConfigurator().withActivity(this);
        StatusBarCompat.translucentStatusBar(this, true);
        if(!isShowFlout){
            showFlout();
            isShowFlout=true;
        }


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
                getSupportDelegate().startWithPop(new SignInNoteOnlyDelegate());
            }

            @Override
            public void onNoSignIn() {
                getSupportDelegate().startWithPop(new SignUpSecondDelegate());
            }

        });
    }

    @Override
    public void onSignUpSecondSuccess() {
        goMain();


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
                getSupportDelegate().startWithPop(new SignInNoteOnlyDelegate());
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
                goMain();

                break;
            case NOT_SIGNED:
                Toast.makeText(this, "启动成功，用户没登录", Toast.LENGTH_LONG).show();
//                startWithPop(new SignInDelegate());
                getSupportDelegate().startWithPop(new SignInNoteOnlyDelegate());
                break;
            default:
                break;
        }
    }

    private void goMain() {
//        String jRegistrationID = JPushInterface.getRegistrationID(getApplicationContext());
//        LatteLogger.e("jialei", "jRegistrationID:" + jRegistrationID);
//        Log.e("jialei", "jRegistrationID:" + jRegistrationID);
//        initJRegistrationID(jRegistrationID);
        getSupportDelegate().startWithPop(new YjBottomDelegate());
    }

    private void initJRegistrationID(String jRegistrationID) {
        final String url = "push/addAlias";
        String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("machineTyp e", 1)//1-手机，2-机器人
                .params("registrationId", jRegistrationID)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getApplicationContext()) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.json("query_timeline", response);
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {

                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getApplicationContext(), "请稍后尝试", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showFlout() {
        if (TextUtils.equals(ExampleApp.MODE, ExampleApp.MODE_DEBUG)) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(R.mipmap.ic_launcher);

            FloatWindow
                    .with(getApplicationContext())
                    .setView(imageView)
                    .setWidth(100)                               //设置控件宽高
                    .setHeight(Screen.width, 0.2f)
                    .setX(100)                                   //设置控件初始位置
                    .setY(Screen.height, 0.3f)
                    .setDesktopShow(true)                        //桌面显示
                    .setViewStateListener(mViewStateListener)    //监听悬浮控件状态改变
                    .setPermissionListener(mPermissionListener)  //监听权限申请结果
                    //MoveType.slide : 可拖动，释放后自动贴边 （默认）
                    //MoveType.back : 可拖动，释放后自动回到原位置
                    //MoveType.active : 可拖动
                    //MoveType.inactive : 不可拖动
                    .setMoveType(MoveType.active)
//                    .setMoveStyle(500, new AccelerateInterpolator())  //贴边动画时长为500ms，加速插值器
                    .build();

            imageView.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "别点了笨蛋！", Toast.LENGTH_SHORT).show());
        }
    }

    private PermissionListener mPermissionListener = new PermissionListener() {
        @Override
        public void onSuccess() {
            Log.d(TAG, "onSuccess");
        }

        @Override
        public void onFail() {
            Log.d(TAG, "onFail");
        }
    };

    private ViewStateListener mViewStateListener = new ViewStateListener() {
        @Override
        public void onPositionUpdate(int x, int y) {
            Log.d(TAG, "onPositionUpdate: x=" + x + " y=" + y);
        }

        @Override
        public void onShow() {
            Log.d(TAG, "onShow");
        }

        @Override
        public void onHide() {
            Log.d(TAG, "onHide");
        }

        @Override
        public void onDismiss() {
            Log.d(TAG, "onDismiss");
        }

        @Override
        public void onMoveAnimStart() {
            Log.d(TAG, "onMoveAnimStart");
        }

        @Override
        public void onMoveAnimEnd() {
            Log.d(TAG, "onMoveAnimEnd");
        }

        @Override
        public void onBackToDesktop() {
            Log.d(TAG, "onBackToDesktop");
        }
    };
}
