package com.bokang.yijia;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.example.baidutext.BaiDuTextConfig;
import com.example.commcon_xfyun.XunFei;
import com.example.common_tencent_tuikit.TuiKitConfig;
import com.example.latte.ui.ninegridview.GlideImageLoader;
import com.example.yijia.app.Latte;
import com.example.yijia.net.Interceptors.DebugInterceptor;
import com.example.yijia.util.callback.CallbackManager;
import com.example.yijia.util.callback.CallbackType;
import com.example.yijia.util.callback.IGlobalCallback;
import com.facebook.stetho.Stetho;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lzy.ninegrid.NineGridView;
import com.mabeijianxi.smallvideorecord2.DeviceUtils;
import com.mabeijianxi.smallvideorecord2.JianXiCamera;
import com.mob.MobSDK;
import com.simple.spiderman.SpiderMan;
import com.tencent.qcloud.bokang.BokangChatManager;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.icon.FontEcModule;
import com.yijia.common_yijia.icon.FontYJIndexTopModule;
import com.yijia.common_yijia.icon.FontYJModule;

import java.io.File;

import cn.jpush.android.api.JPushInterface;
import me.yokeyword.fragmentation.BuildConfig;
import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;


public class ExampleApp extends MultiDexApplication {
    public static final String PACKAGENAME = "com.bokang.yijia";
    public static final String MODE = "DEBUG";
    public static final String MODE_DEBUG = "DEBUG";
    public static final String MODE_RELEASE = "RELEASE";


    @Override
    public void onCreate() {
        super.onCreate();
        initSpiderMan();
        initJpush();
        initCallBack();
//        initTencentTuiKit();
        initLatte();
        initStetho();
        initNineGrideView();
//        initSmallVideo();
        initXfYun();
        initMob();
//        DatabaseManager.getInstance().init(this);
        YjDatabaseManager.getInstance().init(this);

        if (TextUtils.equals(MODE, MODE_DEBUG)) {
            initFragmentDeBug();
        }
        initBauDuText();
        initBokangChatManager();

    }

    private void initBokangChatManager() {
        BokangChatManager.getInstance().init();
    }

    private void initBauDuText() {
        BaiDuTextConfig.INSTANCE.initAccessTokenLicenseFile(this);
    }

    private void initSpiderMan() {
        //放在其他库初始化前
        SpiderMan.init(this);
    }

    private void initLatte() {
        Latte.init(this)
                .withIcon(new FontAwesomeModule())
                .withIcon(new FontEcModule())
                .withIcon(new FontYJModule())
                .withIcon(new FontYJIndexTopModule())
                .withLoaderDelayed(1000)
                .withApiHost("http://47.104.86.251:8080/")
                .withInterceptor(new DebugInterceptor("test", R.raw.test))
//                .withWeChatAppId("你的微信AppKey")
//                .withWeChatAppSecret("你的微信AppSecret")
//                .withJavascriptInterface("latte")
//                .withWebEvent("test", new TestEvent())
//                .withWebEvent("share", new ShareEvent())
                //添加Cookie同步拦截器
//                .withWebHost("www.baidu.com/")
//                .withInterceptor(new AddCookieInterceptor())
                .configure();
    }


    private void initXfYun() {
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=" + XunFei.APPID);
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

    private void initJpush() {
        //开启极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    private void initCallBack() {
        CallbackManager.getInstance()
                .addCallback(CallbackType.TAG_OPEN_PUSH, new IGlobalCallback() {
                    @Override
                    public void executeCallback(@Nullable Object args) {
                        if (JPushInterface.isPushStopped(Latte.getApplicationContext())) {
                            //开启极光推送
                            JPushInterface.setDebugMode(true);
                            JPushInterface.init(Latte.getApplicationContext());
                        }
                    }
                })
                .addCallback(CallbackType.TAG_STOP_PUSH, new IGlobalCallback() {
                    @Override
                    public void executeCallback(@Nullable Object args) {
                        if (!JPushInterface.isPushStopped(Latte.getApplicationContext())) {
                            //开启极光推送
                            JPushInterface.setDebugMode(true);
                            JPushInterface.stopPush(Latte.getApplicationContext());
                        }
                    }
                });
    }

    private void initFragmentDeBug() {
        Fragmentation.builder()
                // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(true) // 实际场景建议.debug(BuildConfig.DEBUG)
                /**
                 * 可以获取到{@link me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning}
                 * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                 */
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
                        // Bugtags.sendException(e);
                    }
                })
                .install();
    }

    private void initTencentTuiKit() {
        TuiKitConfig.initTencentTuiKit(getApplicationContext());
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    private void initMob() {
        MobSDK.init(this);
    }

}
