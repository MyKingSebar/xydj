package com.example.baidutext;

import android.content.Context;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;

public enum  BaiDuTextConfig {
    //INSTANCE
    INSTANCE;

    private boolean hasGotToken = false;
    /**
     * 用明文ak，sk初始化
     */
    public void initAccessTokenWithAkSk(final Context mContext) {
        OCR.getInstance(mContext).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                Toast.makeText(mContext,"AK，SK方式获取token失败"+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }, mContext,  "Dm05PQBMiZr6TOK077cRf1HH", "Xm29pAWSQ93XLA6nmyldLUDFtgjtoWSz");
    }

    /**
     * 自定义license的文件路径和文件名称，以license文件方式初始化
     */
    public void initAccessTokenLicenseFile(final Context mContext) {
        OCR.getInstance(mContext).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                Toast.makeText(mContext,"自定义文件路径licence方式获取token失败"+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }, "aip.license", mContext);
    }

    public boolean checkTokenStatus(Context mContext) {
        if (!hasGotToken) {
            Toast.makeText(mContext, "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }


    public void init2(final Context mContext){
        CameraNativeHelper.init(mContext, OCR.getInstance(mContext).getLicense(),
                new CameraNativeHelper.CameraNativeInitCallback() {
                    @Override
                    public void onError(int errorCode, Throwable e) {
                        String msg;
                        switch (errorCode) {
                            case CameraView.NATIVE_SOLOAD_FAIL:
                                msg = "加载so失败，请确保apk中存在ui部分的so";
                                break;
                            case CameraView.NATIVE_AUTH_FAIL:
                                msg = "授权本地质量控制token获取失败";
                                break;
                            case CameraView.NATIVE_INIT_FAIL:
                                msg = "本地质量控制";
                                break;
                            default:
                                msg = String.valueOf(errorCode);
                        }
                        Toast.makeText(mContext, "本地质量控制初始化错误，错误原因： " + msg, Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void destory(){
        // 释放本地质量控制模型
        CameraNativeHelper.release();
    }
}
