package com.example.latte.net.rx;

import android.content.Context;
import android.widget.Toast;

import com.example.latte.util.NetWorkUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


public abstract class BaseObserver<String> implements Observer<String> {

    private Context context;

    public <T> BaseObserver(Context context) {
        this.context = context;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull String string) {
        onResponse(string);
    }

    public abstract void onResponse(String string);

    @Override
    public void onError(@NonNull Throwable e) {
        Logger.e(new Gson().toJson(e));
        if (!NetWorkUtils.isNetworkConnected(context)) {
//            NetWorkSetDialog.showSetNetworkUI(context);
            Toast.makeText(context, "没有可用的网络", Toast.LENGTH_LONG).show();
        }
        if(e.getMessage()==null){
            Logger.e("error","e.getMessage()==null");
            return;
        }
        if (e.getMessage().contains("404")) {
            Toast.makeText(context, "网络404错误", Toast.LENGTH_LONG).show();
        }
        if (e.getMessage().contains("500")) {
            Toast.makeText(context, "网络500错误", Toast.LENGTH_LONG).show();
        }
        if (e instanceof IllegalStateException) {
            Toast.makeText(context, "数据解析异常", Toast.LENGTH_LONG).show();
        }
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "请求超时", Toast.LENGTH_LONG).show();
        }
        e.printStackTrace();
        onFail(e);
    }

    public abstract void onFail(Throwable e);

    @Override
    public void onComplete() {
        Logger.i("-----------已完成----------");
    }
}
