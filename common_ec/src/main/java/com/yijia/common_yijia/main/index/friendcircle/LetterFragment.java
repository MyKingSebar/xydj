package com.yijia.common_yijia.main.index.friendcircle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.commcon_xfyun.Lat;
import com.example.commcon_xfyun.LatCallbackInterface;
import com.example.latte.app.Latte;
import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.net.rx.BaseObserver;
import com.example.latte.net.rx.RxRestClient;
import com.example.latte.ui.wxvideoedit.EsayVideoEditActivity;
import com.example.latte.util.log.LatteLogger;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.index.YjIndexDelegate;
import com.yijia.common_yijia.main.index.friendcircle.pictureselector.FullyGridLayoutManager;
import com.yijia.common_yijia.main.index.friendcircle.pictureselector.GridImageAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


public class LetterFragment extends LatteDelegate implements LatCallbackInterface {
    private Lat mlat = null;
    StringBuffer mStringBuffer=null;
    //光标所在位置
    int index=0;
    @BindView(R2.id.et_text)
    AppCompatEditText etText;

    @OnClick(R2.id.tv_back)
    void back() {
        getSupportDelegate().pop();
    }

    @OnClick(R2.id.bt_say)
    void say() {
        checkLat();
        index=etText.getSelectionStart();
        mlat.iatStart();
    }


    //朋友圈参数
    //1-文本，2-照片，3-语音，4-视频
    private int contentType = 0;
    private String urlType = "pictureUrl";
    private String urlTop = null;

    @OnClick(R2.id.tv_save)
    void save() {
        final String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        contentType = 1;
        upLoadInfo(token, etText.getText().toString(), "");

    }


    private final static String TAG = LetterFragment.class.getSimpleName();


    @Override
    public Object setLayout() {
        return R.layout.delegate_edit_letter;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        init();
    }


    private void init() {
        mStringBuffer=new StringBuffer();
        checkLat();


    }
private void checkLat(){
    if (null == mlat) {
            Context context=getContext();
            if(null==context){
                return;
            }
        mlat = new Lat(getContext(), Lat.INPUTASS, this);
    }
}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void RxUpLoad(String token, File[] files) {
        RxRestClient.builder()
                .url(urlTop)
                .params("yjtk", token)
//                .params("files", new File[]{new File(imgPath)})
                .files(files)
                .build()
                .uploadwithparams()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.json("picture/upload", response);
                        final JSONObject object = JSON.parseObject(response);
                        final String status = object.getString("status");
                        if (TextUtils.equals(status, "1001")) {

                            final JSONObject dataObject = object.getJSONObject("data");
                            final String filePath = dataObject.getString("path");
                            upLoadInfo(token, etText.getText().toString(), filePath);
                        } else {
                            Toast.makeText(getContext(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void upLoadInfo(String token, String content, String filesString) {
        LatteLogger.w("upLoadImg", "upLoadInfo");
        final String url = "circle/insert";
        if (contentType == 1) {
            filesString = "";
        }
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                //contentType  1-文本，2-照片，3-语音，4-视频
                .params("contentType", contentType)
                .params("content", content)
                .params(urlType, filesString)
//                .params("location", location)
//                .params("longitude", longitude)
//                .params("latitude", latitude)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.json("circle/insert", response);
                        getSupportDelegate().pop();
                        //清缓存
                        PictureFileUtils.deleteCacheDirFile(Latte.getApplicationContext());
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void latSuccess(String s) {
        Log.e("jialei","latSuccess:"+s);
        mStringBuffer.setLength(0);
        mStringBuffer.append(etText.getText().toString());
        if (index < 0 || index >= mStringBuffer.length() ){
            mStringBuffer.append(s);
        }else{
            mStringBuffer.insert(index,s);//光标所在位置插入文字
        }
        etText.setText(mStringBuffer);
        index+=s.length();
        etText.setSelection(index);
    }

    @Override
    public void latError(String s) {

    }
}
