package com.bokang.yijia;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bokang.tencent_trtc_sdk.TrtcConfig;
import com.example.common_tencent_tuikit.TuiKitConfig;
import com.example.latte.ui.launcher.ILauncherListener;
import com.example.latte.ui.launcher.OnLauncherFinishTag;
import com.example.yijia.activities.ProxyActivity;
import com.example.yijia.app.AccountManager;
import com.example.yijia.app.IUserChecker;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.util.callback.CallbackManager;
import com.example.yijia.util.callback.CallbackType;
import com.example.yijia.util.callback.IGlobalCallback;
import com.example.yijia.util.log.LatteLogger;
import com.example.yijia.util.storage.LattePreference;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMMessageExt;
import com.tencent.qcloud.bokang.BokangChatListener;
import com.tencent.qcloud.bokang.BokangChatManager;
import com.tencent.qcloud.uikit.TUIKit;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;
import com.tencent.qcloud.uikit.common.IUIKitCallBack;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.YjBottomDelegate_with3;
import com.yijia.common_yijia.main.index.friendcircle.smallvideo.CameraActivity;
import com.yijia.common_yijia.main.index.friendcircle.smallvideo.SmallCameraLisener;
import com.yijia.common_yijia.main.message.trtc.CallIntentData;
import com.yijia.common_yijia.main.message.trtc.CallWaitingActivity;
import com.yijia.common_yijia.main.message.trtc.CalledWaitingActivity;
import com.yijia.common_yijia.main.message.trtc2.TRTCMainActivity2;
import com.yijia.common_yijia.main.message.trtc2.TRTCMainActivity3;
import com.yijia.common_yijia.main.robot.robotmain.AddParentsDelegate;
import com.yijia.common_yijia.sign.ISignListener;
import com.yijia.common_yijia.sign.SignInNoteOnlyDelegate;
import com.yijia.common_yijia.sign.SignUpSecondDelegate;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import qiu.niorgai.StatusBarCompat;

import static com.example.yijia.app.AccountManager.checkAccont;

public class ExampleActivity extends ProxyActivity implements
        ISignListener,
        ILauncherListener,
        BokangChatListener, SmallCameraLisener {
    String TAG = "ExampleActivity.DEBUG";
    public static boolean isShowFlout = false;
    BokangChatManager mBokangChatManager = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExampleApp.flag = 0;
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Latte.getConfigurator().withActivity(this);
        StatusBarCompat.translucentStatusBar(this, true);
        if (!isShowFlout) {
            showFlout();
            isShowFlout = true;
        }
        initTencentTuiKit();
        initExit();
        addLogOutCallBack();
    }

    private void initExit() {
        CallbackManager.getInstance()
                .addCallback(CallbackType.EXIT, (IGlobalCallback<String>) args -> getSupportDelegate().startWithPop(new SignInNoteOnlyDelegate()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkAccont()) {
            //TODO 测试
            loginTencentIM();
        }
        mBokangChatManager = BokangChatManager.getInstance();
        mBokangChatManager.setBokangChatListener(this);
    }

    @Override
    public LatteDelegate setRootDelegate() {
//        return new ExampleDelegate();
//        return new SignInDelegate();
//        return new YjBottomDelegate();
        //检查用户是否登陆了APP
        if (checkAccont()) {
            initJPush();
            loginTencentIM();
            YjBottomDelegate_with3 delegate = new YjBottomDelegate_with3();
            delegate.setmSmallCameraLisener(this);
            return delegate;
        } else {
            return new SignInNoteOnlyDelegate();
        }
//        return new SignUpDelegate();
//        return new LauncherScrollDelegate();
    }

    @Override
    public void onSignInSuccess() {
//        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
//        getSupportDelegate().startWithPop(new YjBottomDelegate());
        checkAccont(new IUserChecker() {
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
//        goMain();
        if (LattePreference.isFirstLogin("first_login") && LattePreference.getNeedAddParents()) {
            AddParentsDelegate addParentsDelegate = new AddParentsDelegate();
            Bundle bundle = new Bundle();
            bundle.putBoolean(AddParentsDelegate.EXTRA_ISFIRST_LOGIN, true);
            addParentsDelegate.setArguments(bundle);
            getSupportDelegate().startWithPop(addParentsDelegate);
            LattePreference.setFirstLogin("first_login", false);
        } else {
            goMain();
        }
    }

    @Override
    public void onSignInFailure(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSignUpSuccess() {
//        Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();

        checkAccont(new IUserChecker() {
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

    private void initJPush() {
        String jRegistrationID = JPushInterface.getRegistrationID(getApplicationContext());
        Log.e("jialei", "jRegistrationID:" + jRegistrationID);
        initJRegistrationID(jRegistrationID);
    }

    private void goMain() {
        Log.e("qqqq", "goMain");
        initJPush();
        loginTencentIM();
        YjBottomDelegate_with3 delegate = new YjBottomDelegate_with3();
        delegate.setmSmallCameraLisener(this);
        getSupportDelegate().startWithPop(delegate);
    }

    private void addLogOutCallBack() {
        CallbackManager.getInstance()
                .addCallback(CallbackType.NEED_LOGIN_IN, new IGlobalCallback<String>() {
                    @Override
                    public void executeCallback(@Nullable String args) {
                        assert args != null;
                        loginOut();
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                });
    }

    private void loginOut() {
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
    }

    private static void loginTencentIM() {
        String tencentImUserId = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getTencentImUserId();
        String tencentImUserSig = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getTencentImUserSig();
        Log.e("qqqq", "tencentImUserId:" + tencentImUserId + ",tencentImUserSig:" + tencentImUserSig);
        TUIKit.login(tencentImUserId, tencentImUserSig, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                Log.e("qqqq", "onSuccess: login成功");
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Log.e("qqqq", "onerror:" + errMsg + ",errCode:" + errCode + ",moudle:" + module);
            }
        });
    }


    private void initJRegistrationID(String jRegistrationID) {
        final String url = "notification/add_alias";
        String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("machineType", 1)//1-手机，2-机器人
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
            imageView.setImageResource(R.mipmap.icon_find_door);

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

            imageView.setOnClickListener(v -> {
//                String sig = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getTencentImUserSig();
//                String userId = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getTencentImUserId();
//                final Intent intent = new Intent(ExampleActivity.this, TRTCMainActivity3.class);
//                intent.putExtra("roomId", 100);
//                intent.putExtra("userId", userId);
//                intent.putExtra("sdkAppId", TrtcConfig.SDKAPPID);
//                intent.putExtra("userSig", sig);
//                //TODO 需要指定会话ID（即聊天对象的identify，具体可参考IMSDK接入文档）
////            intent.putExtra("chatId", chatId);
//                startActivity(intent);
                Toast.makeText(getApplicationContext(), "别点了笨蛋！", Toast.LENGTH_SHORT).show();
            });
//            imageView.setOnClickListener(v ->
//
//                    Toast.makeText(getApplicationContext(), "别点了笨蛋！", Toast.LENGTH_SHORT).show()
//
//            );
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

    private void initTencentTuiKit() {
        TuiKitConfig.initTencentTuiKit(getApplicationContext());
    }

    @Override
    public void newBokangMessage(TIMCustomElem ele, TIMConversation conversation) {
        String ss = new String(ele.getExt());
        Log.d("newBokangMessage", "ss:" + ss);
        if (ss == null) {
            return;
        }
        Intent intent = new Intent(this, CalledWaitingActivity.class);
        CallIntentData data = new CallIntentData();
//            data.setConversation(conversation);
        if (ss.equals(MessageInfoUtil.BOKANG_VIDEO_WAIT)) {
            data.setPeer(conversation.getPeer());
            data.setRoomid(Integer.parseInt(ele.getDesc()));
            intent.putExtra("CallIntentData", data);
            intent.putExtra(CalledWaitingActivity.TYPE_KEY, CalledWaitingActivity.TYPE_VIDEO);
            Log.d("newBokangMessage", "peer:" + conversation.getPeer() + ",CallIntentData:" + data);
            startActivity(intent);
        } else if (ss.equals(MessageInfoUtil.BOKANG_VOICE_WAIT)) {
            data.setPeer(conversation.getPeer());
            data.setRoomid(Integer.parseInt(ele.getDesc()));
            intent.putExtra("CallIntentData", data);
            intent.putExtra(CalledWaitingActivity.TYPE_KEY, CalledWaitingActivity.TYPE_VOICE);
            Log.d("newBokangMessage", "peer:" + conversation.getPeer() + ",CallIntentData:" + data);
            startActivity(intent);
        }
    }


    private final int GET_PERMISSION_REQUEST = 100; //权限申请自定义码

    /**
     * 获取权限
     */
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager
                            .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager
                            .PERMISSION_GRANTED) {
                startActivityForResult(new Intent(ExampleActivity.this, CameraActivity.class), 100);
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(ExampleActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, GET_PERMISSION_REQUEST);
            }
        } else {
            startActivityForResult(new Intent(ExampleActivity.this, CameraActivity.class), 100);
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GET_PERMISSION_REQUEST) {
            int size = 0;
            if (grantResults.length >= 1) {
                int writeResult = grantResults[0];
                //读写内存权限
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
                if (!writeGranted) {
                    size++;
                }
                //录音权限
                int recordPermissionResult = grantResults[1];
                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!recordPermissionGranted) {
                    size++;
                }
                //相机权限
                int cameraPermissionResult = grantResults[2];
                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!cameraPermissionGranted) {
                    size++;
                }
                if (size == 0) {
                    startActivityForResult(new Intent(ExampleActivity.this, CameraActivity.class), 100);
                } else {
                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void go() {
        getPermissions();
    }
}
