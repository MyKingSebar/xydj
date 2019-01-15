package com.bokang.yijia;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDexApplication;

import com.bokang.yijia.event.TestEvent;
import com.example.latte.app.Latte;
import com.example.latte.ec.database.DatabaseManager;
import com.example.latte.ec.icon.FontEcModule;
import com.example.latte.ec.icon.FontYJModule;
import com.example.latte.net.Interceptors.DebugInterceptor;
import com.example.latte.net.rx.AddCookieInterceptor;
import com.example.latte.ui.ninegridview.GlideImageLoader;
import com.example.latte.util.callback.CallbackManager;
import com.example.latte.util.callback.CallbackType;
import com.example.latte.util.callback.IGlobalCallback;
import com.facebook.stetho.Stetho;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lzy.ninegrid.NineGridView;
import com.mabeijianxi.smallvideorecord2.DeviceUtils;
import com.mabeijianxi.smallvideorecord2.JianXiCamera;
import com.yijia.common_yijia.database.YjDatabaseManager;

import java.io.File;

import io.rong.imkit.RongIM;


public class ExampleApp extends MultiDexApplication {
    public static final String PACKAGENAME="com.bokang.yijia";
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化融云IM
        initRongIm();

        Latte.init(this)
                .withIcon(new FontAwesomeModule())
                .withIcon(new FontEcModule())
                .withIcon(new FontYJModule())
                .withLoaderDelayed(1000)
                .withApiHost("http://47.104.86.251:8080/")
                .withInterceptor(new DebugInterceptor("test", R.raw.test))
                .withWeChatAppId("你的微信AppKey")
                .withWeChatAppSecret("你的微信AppSecret")
                .withJavascriptInterface("latte")
                .withWebEvent("test", new TestEvent())
//                .withWebEvent("share", new ShareEvent())
                //添加Cookie同步拦截器
                .withWebHost("www.baidu.com/")
                .withInterceptor(new AddCookieInterceptor())
                .configure();
        initStetho();
        initNineGrideView();
//        initSmallVideo();
        DatabaseManager.getInstance().init(this);
        YjDatabaseManager.getInstance().init(this);


//        //开启极光推送
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);
//
//
//        CallbackManager.getInstance()
//                .addCallback(CallbackType.TAG_OPEN_PUSH, new IGlobalCallback() {
//                    @Override
//                    public void executeCallback(@Nullable Object args) {
//                        if (JPushInterface.isPushStopped(Latte.getApplicationContext())) {
//                            //开启极光推送
//                            JPushInterface.setDebugMode(true);
//                            JPushInterface.init(Latte.getApplicationContext());
//                        }
//                    }
//                })
//                .addCallback(CallbackType.TAG_STOP_PUSH, new IGlobalCallback() {
//                    @Override
//                    public void executeCallback(@Nullable Object args) {
//                        if (!JPushInterface.isPushStopped(Latte.getApplicationContext())) {
//                            //开启极光推送
//                            JPushInterface.setDebugMode(true);
//                            JPushInterface.stopPush(Latte.getApplicationContext());
//                        }
//                    }
//                });
    }

    //初始化融云IM
    private void initRongIm() {
        RongIM.init(this);
    }

    private void initNineGrideView() {
        NineGridView.setImageLoader(new GlideImageLoader());

    }

    private void initStetho() {
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build()
        );
    }

    public static void initSmallVideo() {
        // 设置拍摄视频缓存路径
        File dcim = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                JianXiCamera.setVideoCachePath(dcim + "/mabeijianxi/");
            } else {
                JianXiCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
                        "/sdcard-ext/")
                        + "/mabeijianxi/");
            }
        } else {
            JianXiCamera.setVideoCachePath(dcim + "/mabeijianxi/");
        }
        // 初始化拍摄，遇到问题可选择开启此标记，以方便生成日志
        JianXiCamera.initialize(false, null);
    }
}
