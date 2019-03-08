package com.yijia.common_yijia.sign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.latte.app.Latte;
import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.net.rx.BaseObserver;
import com.example.latte.net.rx.RxRestClient;
import com.example.latte.util.log.LatteLogger;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.YjBottomDelegate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class SignUpSecondDelegate extends LatteDelegate {

    private final int GETIMGREQUEST = 500;
    private String imgPath = null;
    private List<LocalMedia> selectList = new ArrayList<>();
    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();


    @BindView(R2.id.edit_name)
    TextInputEditText mName = null;
//    @BindView(R2.id.edit_password)
//    TextInputEditText mWord = null;

    @BindView(R2.id.ci_img)
    CircleImageView cvImg = null;

    @OnClick(R2.id.ci_img)
    void getImg() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(1)
                .minSelectNum(1)
                .selectionMode(PictureConfig.SINGLE)
                .previewImage(true)
                .isCamera(true)
                .enableCrop(true)
                .compress(true)
                .glideOverride(160, 160)
                .previewEggs(true)
                .withAspectRatio(1, 1)
                .hideBottomControls(false)
                .freeStyleCropEnabled(true)
                .showCropFrame(true)
                .showCropGrid(true)
                .selectionMedia(selectList)
                .forResult(GETIMGREQUEST);
    }


    @BindView(R2.id.btn_sign_in)
    AppCompatButton mLogin = null;


    private TextWatcher mTextWatcher;


    private ISignListener mISignListener = null;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignListener) {
            mISignListener = (ISignListener) activity;
        }
    }


    @OnClick(R2.id.btn_sign_in)
    void onClickSignUp() {
        final String name = mName.getText().toString();
//        final String word = mWord.getText().toString();
        if (checkForm(name)) {
            final String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
            if (!TextUtils.isEmpty(imgPath)) {
                upLoadImg(token,name);
            } else {
                upLoadInfo(token,"",name);
            }

        }
    }

    private void upLoadImg(String token,String name) {
        final String url = "picture/upload";
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .files(new File[]{new File(imgPath)})
                .build()
                .uploadwithparams()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.json("picture/upload", response);
                        final JSONObject object=JSON.parseObject(response);
                        final String status = object.getString("status");
                        if(TextUtils.equals(status,"1001")){

                            final JSONObject dataObject = object.getJSONObject("data");
                            final String filePath = dataObject.getString("path");
                            upLoadInfo(token,filePath,name);
                        }else{
                            Toast.makeText(getContext(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), "请稍后尝试", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void upLoadInfo(String token,String path,String name) {
        final String url = "user/complete_info";
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("nickname", name)
                .params("imagePath", path)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.json("picture/upload", response);
                        YjSignHandler.onSignUp(response, mISignListener);
//                        getSupportDelegate().startWithPop(new YjBottomDelegate());

                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private boolean checkForm(String name ) {

        boolean isPass = true;

        if (name.isEmpty()) {
//            mPhoneL.setError("错误的手机格式");
            Toast.makeText(getContext(), "请填写姓名", Toast.LENGTH_SHORT).show();
            isPass = false;
            return false;
        } else {
//            mPhoneL.setError(null);
        }

        return isPass;
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_up_second_yijia;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        mLogin.setClickable(false);
        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(mName.getText().toString()) ) {
                    mLogin.setClickable(true);
                    mLogin.setBackgroundResource(R.mipmap.buttom_login_orange);
                    mLogin.setTextColor(getResources().getColor(R.color.app_text_orange));
                } else {
                    mLogin.setClickable(false);
                    mLogin.setBackgroundResource(R.mipmap.buttom_login_gray);
                    mLogin.setTextColor(getResources().getColor(R.color.app_text_gray));
                }
            }
        };
        mName.addTextChangedListener(mTextWatcher);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GETIMGREQUEST:
                    // 图片选择
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if (!selectList.isEmpty()) {
                        imgPath = selectList.get(0).getCompressPath();
                        Glide.with(this)
                                .load(imgPath)
                                .apply(OPTIONS)
                                .into(cvImg);
                    }
                    break;
            }
        }

    }



}