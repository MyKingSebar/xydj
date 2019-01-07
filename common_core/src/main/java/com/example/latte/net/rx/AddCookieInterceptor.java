package com.example.latte.net.rx;

import android.support.annotation.NonNull;

import com.example.latte.util.storage.LattePreference;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookieInterceptor implements Interceptor {


    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        final Request.Builder builder=chain.request().newBuilder();
        Observable.
                just(LattePreference.getCustomAppProfile("cookie"))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String cookie) throws Exception {
                        //给原生API请求附带WebView拦截下来的Cookie
                        builder.addHeader("Cookie",cookie);
                    }
                });

        return chain.proceed(builder.build());
    }
}
