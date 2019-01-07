package com.example.latte.net.rx;

import android.content.Context;

import com.example.latte.net.RestCreator;
import com.example.latte.ui.loader.LoaderStyle;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RxRestClientBuilder {
    private String mUrl =null;
    private static final Map<String, Object> PARAMS=RestCreator.getParams();
    private RequestBody mBody=null;
    private Context mContext=null;
    private LoaderStyle mLoaderStyle=null;
    private File mFile=null;
    private File[] mFiles=null;

    RxRestClientBuilder() {

    }

    public final RxRestClientBuilder url(String url) {
        this.mUrl = url;
        return this;

    }

    public final RxRestClientBuilder params(WeakHashMap<String, Object> params) {
        this.PARAMS.putAll(params);
        return this;

    }

    public final RxRestClientBuilder params(String key, Object value) {
        this.PARAMS.put(key, value);
        return this;

    }


    public final RxRestClientBuilder file(File file) {
        this.mFile=file;
        return this;

    }

    public final RxRestClientBuilder file(String file) {
        this.mFile=new File(file);
        return this;

    }
    public final RxRestClientBuilder files(File[] files) {
        this.mFiles=files;
        return this;

    }

    public final RxRestClientBuilder files(String[] files) {
        final int filesSize=files.length;
       File[] mfiles= new File[filesSize];
        for(int i=0;i<filesSize;i++){
            mfiles[i]=new File(files[i]);
        }
        this.mFiles=mfiles;
        return this;

    }



    public final RxRestClientBuilder raw(String raw) {
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;

    }

    public final RxRestClientBuilder loader(Context context, LoaderStyle style) {
        this.mContext=context;
        this.mLoaderStyle=style;
        return this;

    }
    public final RxRestClientBuilder loader(Context context) {
        this.mContext=context;
        this.mLoaderStyle=LoaderStyle.BallClipRotatePulseIndicator;
        return this;

    }


    public final RxRestClient build() {
        return new RxRestClient(mUrl, PARAMS, mBody, mFile, mFiles,mContext, mLoaderStyle);
    }

}
