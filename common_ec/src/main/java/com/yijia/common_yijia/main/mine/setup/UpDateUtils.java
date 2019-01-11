package com.yijia.common_yijia.main.mine.setup;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.app.AccountManager;
import com.example.latte.app.IUserChecker;
import com.example.latte.net.rx.BaseObserver;
import com.example.latte.net.rx.RxRestClient;
import com.example.latte.net.rx.RxRestClientBuilder;
import com.yijia.common_yijia.database.YjDatabaseManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UpDateUtils {
    public static void   updatePersonalData(Context context,String nickname, String userHead,UpDateSuccessAndError upDateSuccessAndError){
        final String url = "/user/update_user_info";
        AccountManager.checkAccont(new IUserChecker() {
            @Override
            public void onSignIn() {
                String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
                RxRestClientBuilder yjtk = RxRestClient.builder()
                        .url(url)
                        .params("yjtk", token);
                if (userHead != null){
                    yjtk.params("userHead", userHead);
                }
                if (nickname!=null){
                    yjtk.params("nickname", nickname);
                }
                        yjtk
                        .build()
                        .post()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<String>(context) {
                            @Override
                            public void onResponse(String response) {
                                JSONObject object = JSON.parseObject(response);
                                String status = object.getString("status");
                                if (TextUtils.equals(status, "1001")) {
                                    upDateSuccessAndError.successAndError(UpDatePersonal.SUCCESS);
                                } else {
                                    upDateSuccessAndError.successAndError(UpDatePersonal.ERROR);
                                }
                            }
                            @Override
                            public void onFail(Throwable e) {
                                Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            @Override
            public void onNoSignIn() {}
        });
    }
    public enum UpDatePersonal{
        SUCCESS,
        ERROR
    }
    public interface UpDateSuccessAndError{
        void successAndError(UpDatePersonal upDatePersonal);
    }

}
