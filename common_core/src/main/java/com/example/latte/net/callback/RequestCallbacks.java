package com.example.latte.net.callback;

import android.os.Handler;

import com.example.latte.app.ConfigKeys;
import com.example.latte.app.Latte;
import com.example.latte.ui.loader.LatteLoader;
import com.example.latte.ui.loader.LoaderStyle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestCallbacks implements Callback<String> {
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final LoaderStyle LOADER_STYLE;
    private static final Handler HANDLERl = new Handler();

    public RequestCallbacks(IRequest request, ISuccess sucess,
                            IFailure failure, IError error, LoaderStyle style) {
        this.REQUEST = request;
        this.SUCCESS = sucess;
        this.FAILURE = failure;
        this.ERROR = error;
        this.LOADER_STYLE = style;
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if (response.isSuccessful()) {
            if (call.isExecuted()) {
                if (SUCCESS != null) {
                    SUCCESS.onSuccess(response.body());
                }
            }
        } else {
            if (ERROR != null) {
                ERROR.onError(response.code(), response.message());
            }
        }
        stopLoading();

    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        if (FAILURE != null) {
            FAILURE.onFailure();

        }
        if (REQUEST != null) {
            REQUEST.onRequestEnd();

        }
        stopLoading();

    }

    private void stopLoading() {
        final long delayed = Latte.getConfiguration(ConfigKeys.LOADER_DELAYED);
        if (LOADER_STYLE != null) {
            HANDLERl.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LatteLoader.stopLoading();
                }
            }, delayed);
        }
    }
}
