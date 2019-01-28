package com.yijia.common_yijia.main.index.friendcircle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
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
import com.example.latte.ui.camera.RequestCodes;
import com.example.latte.util.callback.CallbackManager;
import com.example.latte.util.callback.CallbackType;
import com.example.latte.util.callback.IGlobalCallback;
import com.example.latte.util.log.LatteLogger;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.index.friendcircle.choosefriend.LetterchoosefriendDelegate;
import com.yijia.common_yijia.main.mine.setup.ChangePasswordDelegate;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class LetterDelagate extends LatteDelegate implements LatCallbackInterface {
    public static final int LETTERCODE=6000;
    private Lat mlat = null;
    StringBuffer mStringBuffer=null;
    //光标所在位置
    int index=0;
    @BindView(R2.id.et_text)
    AppCompatEditText etText;
    @BindView(R2.id.tv_recipients)
    AppCompatTextView tv_recipients;

    private long friendId=0;

    //朋友圈参数
    //1-文本，2-照片，3-语音，4-视频
    private int circleType = 5;
    ////1-文字，2-语音
    private int contentType = 1;
    //可见类型：1-全部可见，2-部分可见，3-部分不可见
    private int visibleType = 2;
    private int[] visibleOrInvisibleUserIds = null;
    private String location=null;
    private double longitude=0;
    private double latitude=0;


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
    @OnClick(R2.id.ll_recipients)
    void recipients() {
        getSupportDelegate().startForResult(new LetterchoosefriendDelegate(),LETTERCODE);
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        Toast.makeText(_mActivity, "qqqqq", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        Toast.makeText(_mActivity, "qqqqq", Toast.LENGTH_SHORT).show();
        showToast("ssssssssss");
        Log.e("jialei","requestCode:"+requestCode);
        Log.e("jialei","resultCode:"+resultCode);
        Log.e("jialei","ididid:"+data.getLong("friendId",0));
        int i=requestCode;
        int i1=resultCode;
        Bundle bundle=data;
        String t1=bundle.getString("friendName","");
        Toast.makeText(_mActivity, ""+requestCode+resultCode+data.toString(), Toast.LENGTH_SHORT).show();

        if (requestCode == LETTERCODE&&resultCode==4) {
            if (data != null) {
                tv_recipients.setText(data.getString("friendName",""));
                friendId=data.getLong("friendId",0);
//                final String qrCode = data.getString("SCAN_RESULT");
//                final IGlobalCallback<String> callback = CallbackManager
//                        .getInstance()
//                        .getCallback(CallbackType.ON_SCAN);
//                if (callback != null) {
//                    callback.executeCallback(qrCode);
//                }
            }
        }
    }

    @OnClick(R2.id.tv_save)
    void save() {
        final String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        contentType = 1;
        upLoadInfo(token, etText.getText().toString(), "");

    }


    private final static String TAG = LetterDelagate.class.getSimpleName();


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


    private void upLoadInfo(String token, String content, String filesString) {
        LatteLogger.w("upLoadImg", "upLoadInfo");
        final String url = "circle/insert";
        if (circleType == 1) {
            filesString = "";
        }
        if(friendId==0){
            showToast("请选择好友");
            return;
        }
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                //circleType  1-文本，2-照片，3-语音，4-视频
                .params("circleType", circleType)
                .params("contentType", contentType)//1-文字，2-语音
                .params("content", content)
//                .params(urlType, filesString)
                .params("visibleType", visibleType)
                .params("visibleOrInvisibleUserIds",new long[]{friendId})
                .params("location", location)
                .params("longitude", longitude)
                .params("latitude", latitude)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.json("circle/insert", response);
                        getSupportDelegate().pop();
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void latSuccess(String s) {
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
