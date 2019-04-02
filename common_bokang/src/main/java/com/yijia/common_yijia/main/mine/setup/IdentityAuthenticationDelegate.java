package com.yijia.common_yijia.main.mine.setup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.example.baidutext.BDTFileUtil;
import com.example.baidutext.BaiDuTextConfig;
import com.example.baidutext.ResultCode;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.dialog.JDialogUtil;
import com.example.yijia.util.log.LatteLogger;
import com.google.gson.Gson;
import com.yijia.common_yijia.database.YjDatabaseManager;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class IdentityAuthenticationDelegate extends LatteDelegate {
    @BindView(R2.id.tv_save)
    AppCompatTextView tvSave;
    @BindView(R2.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R2.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R2.id.tv_ocr)
    AppCompatTextView tvOcr;
    @BindView(R2.id.tv_name)
    AppCompatTextView tvName;
    @BindView(R2.id.tv_time)
    AppCompatTextView tvTime;
    @BindView(R2.id.tv_id)
    AppCompatTextView tvId;

    String token = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_identityauthentication;
    }

    @OnClick(R2.id.tv_back)
    void back() {
        getSupportDelegate().pop();
    }

    @OnClick(R2.id.tv_ok)
    void ok() {
        getSupportDelegate().pop();
    }

    @OnClick(R2.id.relativeLayout)
    void check() {
//        Intent intent = new Intent(getActivity(), CameraActivity.class);
//        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                BDTFileUtil.getSaveFile(getContext()).getAbsolutePath());
//        intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE,
//                true);
//        // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
//        // 请手动使用CameraNativeHelper初始化和释放模型
//        // 推荐这样做，可以避免一些activity切换导致的不必要的异常
//        intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL,
//                true);
//        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
//        startActivityForResult(intent, ResultCode.MINE_AUTHENTICATION);

        Intent intent = new Intent(getActivity(), CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                BDTFileUtil.getSaveFile(getContext()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
        startActivityForResult(intent, ResultCode.MINE_AUTHENTICATION);    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        initView();
        initCallback();
    }

    private void initCallback() {
//        CallbackManager.getInstance()
//                .addCallback(CallbackType.FRAGMENT_AUTHENTICATION_RESULT, (IGlobalCallback<String>) args -> {
//                    assert args != null;
//                    String[] arg = args.split(",");
//                    if (arg.length == 2) {
//                        friendId = Long.parseLong(arg[1]);
//                        if(friendId!=0){
//                            JDialogUtil.INSTANCE.showRxDialogShapeLoading(getContext());
//                        }
//                    }
//                });

//        final IGlobalCallback<String> callback = CallbackManager
//                .getInstance()
//                .getCallback(CallbackType.ON_SCAN);
//        if (callback != null) {
//            callback.executeCallback(result.getContents());
//        }

    }

    private void initView() {
        tvTitle.setText("监护人管理");
        tvSave.setVisibility(View.GONE);
        BaiDuTextConfig.INSTANCE.init2(getContext());
    }

    private void getGuardianshipInfo(String token, int type) {
        String url = "guardianship/query_guardianship/";
        RxRestClient.builder()
                .url(url + type)
                .params("yjtk", token)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final String status = JSON.parseObject(response).getString("status");
                        Log.e("jialei", "query_guardianship" + new Gson().toJson(response));
                        if (TextUtils.equals(status, "1001")) {
                            JDialogUtil.INSTANCE.dismiss();
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            JDialogUtil.INSTANCE.dismiss();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        JDialogUtil.INSTANCE.dismiss();
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BaiDuTextConfig.INSTANCE.destory();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_PICK_IMAGE_FRONT && resultCode == Activity.RESULT_OK) {
//            Uri uri = data.getData();
//            String filePath = getRealPathFromURI(uri);
//            recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
//        }
//
//        if (requestCode == REQUEST_CODE_PICK_IMAGE_BACK && resultCode == Activity.RESULT_OK) {
//            Uri uri = data.getData();
//            String filePath = getRealPathFromURI(uri);
//            recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
//        }

        if (requestCode == ResultCode.MINE_AUTHENTICATION && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = BDTFileUtil.getSaveFile(getContext()).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
                    }
                }
            }
        }
    }


    private void recIDCard(String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(20);

        OCR.getInstance(getContext()).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
                    showToast(result.toString());
                    LatteLogger.d("recIDCard",result.toString());
                }
            }

            @Override
            public void onError(OCRError error) {
                showToast(error.getMessage());
            }
        });
    }
}
