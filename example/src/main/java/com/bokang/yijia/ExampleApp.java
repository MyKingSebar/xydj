package com.bokang.yijia;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.bokang.yijia.event.TestEvent;
import com.example.commcon_xfyun.XunFei;
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
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lzy.ninegrid.NineGridView;
import com.mabeijianxi.smallvideorecord2.DeviceUtils;
import com.mabeijianxi.smallvideorecord2.JianXiCamera;
import com.tencent.TIMConnListener;
import com.tencent.TIMLogListener;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMUserStatusListener;
import com.yijia.common_yijia.database.YjDatabaseManager;

import java.io.File;
import java.util.List;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;


public class ExampleApp extends MultiDexApplication {
    public static final String PACKAGENAME="com.bokang.yijia";
    @Override
    public void onCreate() {
        super.onCreate();

        initTencentIm();
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
        initXfYun();
        DatabaseManager.getInstance().init(this);
        YjDatabaseManager.getInstance().init(this);
        Fragmentation.builder()
                // 设置 栈视图 模式为 悬浮球模式   SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(BuildConfig.DEBUG)
                // 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        // 建议在该回调处上传至我们的Crash监测服务器
                        // 以Bugtags为例子: 手动把捕获到的 Exception 传到 Bugtags 后台。
                        // Bugtags.sendException(e);
                    }
                })
                .install();


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


    private void initXfYun() {
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"="+XunFei.APPID);
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

    //腾讯IM
    private void initTencentIm() {
        //设置消息监听器，收到新消息时，通过此监听器回调
        TIMManager.getInstance().addMessageListener(new TIMMessageListener() {//消息监听器

            @Override
            public boolean onNewMessages(List<TIMMessage> msgs) {//收到新消息
                //消息的内容解析请参考消息解析
                return true; //返回 true 将终止回调链，不再调用下一个新消息监听器
            }
        });

        //设置网络连接监听器，连接建立／断开时回调
        TIMManager.getInstance().setConnectionListener(new TIMConnListener() {//连接监听器
            @Override
            public void onConnected() {//连接建立
                Log.e("tengxun", "connected");
            }

            @Override
            public void onDisconnected(int code, String desc) {//连接断开
                //接口返回了错误码 code 和错误描述 desc，可用于定位连接断开原因
                //错误码 code 含义请参见错误码表
                Log.e("tengxun", "disconnected");
            }

            @Override
            public void onWifiNeedAuth(String s) {
                Log.e("tengxun", "wifiNeedAuth");
            }
        });

        //设置日志回调，SDK 输出的日志将通过此接口回传一份副本
        //[NOTE] 请注意 level 定义在 TIMManager 中，如 TIMManager.ERROR 等， 并不同于 Android 系统定义
        TIMManager.getInstance().setLogListener(new TIMLogListener() {
            @Override
            public void log(int level, String tag, String msg) {
                //可以通过此回调将 SDK 的 log 输出到自己的日志系统中

            }
        });
        TIMManager.getInstance().init(getApplicationContext());
    }
}
