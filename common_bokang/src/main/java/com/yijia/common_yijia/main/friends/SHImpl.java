package com.yijia.common_yijia.main.friends;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.shshcom.SHConfig;
import com.shshcom.SHVoIPListener;
import com.shshcom.SHVoIPSDK;
import com.shshcom.voip.SHVoIPAccount;
import com.shshcom.voip.SipCode;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.mine.ExtraString;
import com.yijia.common_yijia.main.mine.SHCallingActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangpanfeng@bokangzhixin.com on 2019/5/20.
 */
public class SHImpl implements SHVoIPListener {


    private String domain = "https://dev.shshcom.com";

    private String appKey = "A11903002";

    private String appSecret = "25f47b6c-f59f-4f1c-807a-592a38d7fffd";

    private String businessId = "3";

    private static SHImpl shImpl;

    private Context context;

    private SHImpl(Context context) {
        this.context = context;
    }

    public static SHImpl getInstance(Context context) {
        if (null == shImpl) {
            shImpl = new SHImpl(context);
        }

        return shImpl;
    }

    @SuppressLint("MissingPermission")
    public void call(String phone) {
        SHVoIPSDK.getInstance().callOut(phone);
    }


    public void init() {
        Log.v("shihua ", "init begin");
        /**
         * 第一步，初始化
         */
        SHVoIPSDK.getInstance().init(context);

        /**
         * 第二步，设置监听
         */
        SHVoIPSDK.getInstance().setSHVoIPListener(shImpl);

        SHConfig.Builder builder = new SHConfig.Builder()
                .setAppKey(appKey)
                .setAppSecret(appSecret)
                .setDomain(domain)
                .setBusinessId(businessId);

        /**
         * 第三步，设置配置
         */
        SHVoIPSDK.getInstance().setSHConfig(builder.build());

        /**
         * 第四步，登录
         */

        Log.v("shihua ", "login begin");
        login();
    }

    public void login() {
        if (null == YjDatabaseManager.getInstance().getDao() || YjDatabaseManager.getInstance().getDao().loadAll().size() == 0 || null == YjDatabaseManager.getInstance().getDao().loadAll().get(0))
            return;
        String phone = null, password = null;
        phone = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getPhone();
        password = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getShPassword();
        Log.v("shihua ", "login phone:" + phone + " pas:" + password);
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
            SHVoIPSDK.getInstance().login(phone, password, new SHVoIPSDK.SHApiCallBack() {
                @Override
                public void onSuccess() {
                    Log.v("shihua ", "shihua login success");
                }

                @Override
                public void onFail(String msg) {
                    Log.e("shihua ", "shihua login failed：" + msg);
                }
            });

        }
    }

    @Override
    public void onRegisterSuccess(SHVoIPAccount shVoIPAccount) {
        Log.v("shihua ", "called is registed success--" + shVoIPAccount.toString());
    }

    @Override
    public void onRegisterFail(SHVoIPAccount shVoIPAccount, SipCode sipCode) {
        Log.e("shihua", "called is registed failed--" + shVoIPAccount.toString() + "--" + sipCode.toString());
    }

    @Override
    public void onIncomingCall(String s) {

        String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        RxRestClient.builder()
                .url("user/query_user_info")
                .params("yjtk", token)
                .params("type", 2)
                .params("keyword", s)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(context) {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", "user/query_user_info:" + response);
                        JSONObject object = JSONObject.parseObject(response);
                        String headUrl = null;
                        String name = null;
                        try {
                            headUrl = object.getJSONObject("data").getJSONObject("user").getString("imagePath");

                            name = object.getJSONObject("data").getJSONObject("user").getString("nickname");
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            Intent intent = new Intent(context, SHCallingActivity.class);
                            intent.putExtra(ExtraString.ISINCOME, true);
                            intent.putExtra(ExtraString.PHONE_NUM, s);
                            intent.putExtra(ExtraString.PHONE_NAME, name);
                            intent.putExtra(ExtraString.PHONE_URL, headUrl);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                    }
                });
    }

    @Override
    public void onCallRing(String s, SipCode sipCode) {
        Log.v("shihua", "call ring--" + s);
    }

    @Override
    public void onCallOK(String s, SipCode sipCode) {
        Log.v("shihua", "call ok--" + s);
        Intent intent = new Intent(ExtraString.BROADCAST_ANSWER_BEGIN);
        context.sendBroadcast(intent);
    }

    @Override
    public void onCallEnd(String s, SipCode sipCode) {
        Log.v("shihua", "call end--" + s);
        Intent intent = new Intent(ExtraString.BROADCAST_END_BEGIN);
        context.sendBroadcast(intent);
    }
}
