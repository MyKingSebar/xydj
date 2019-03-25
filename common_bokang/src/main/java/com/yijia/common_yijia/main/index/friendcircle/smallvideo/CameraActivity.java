package com.yijia.common_yijia.main.index.friendcircle.smallvideo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.BackListener;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.cjt2325.cameralibrary.util.DeviceUtil;
import com.cjt2325.cameralibrary.util.FileUtil;
import com.example.latte.ec.R;
import com.example.yijia.app.Latte;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.dialog.JDialogUtil;
import com.example.yijia.util.log.LatteLogger;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.yijia.common_yijia.database.YjDatabaseManager;

import java.io.File;
import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CameraActivity extends AppCompatActivity {
    private JCameraView jCameraView;
    final String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_smallvideo);
        jCameraView = (JCameraView) findViewById(R.id.jcameraview);
        //设置视频保存路径
        jCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_ONLY_RECORDER);
        jCameraView.setTip("按住拍摄，边拍边说点什么");
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
        jCameraView.setBackkListener(new BackListener() {
            @Override
            public void onBack() {
                CameraActivity.this.finish();
            }
        });
        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //错误监听
                Log.i("CJT", "camera error");
                Intent intent = new Intent();
                setResult(103, intent);
                finish();
            }

            @Override
            public void AudioPermissionError() {
                Toast.makeText(CameraActivity.this, "给点录音权限可以?", Toast.LENGTH_SHORT).show();
            }
        });
        //JCameraView监听
        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
//                Log.i("JCameraView", "bitmap = " + bitmap.getWidth());
                String path = FileUtil.saveBitmap("JCamera", bitmap);
                Intent intent = new Intent();
                intent.putExtra("path", path);
                setResult(101, intent);
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
//                String path = FileUtil.saveBitmap("JCamera", firstFrame);
//                Log.i("CJT", "url = " + url + ", Bitmap = " + path);
//                Intent intent = new Intent();
//                intent.putExtra("path", path);
//                setResult(101, intent);
//                finish();
                File file=new File(url);
                File[] files={file};
                RxUpLoad(token,files);
//                String path = FileUtil.saveBitmap("JCamera", firstFrame);
//                Log.i("CJT", "url = " + url + ", Bitmap = " + path);
//                Intent intent = new Intent();
//                intent.putExtra("path", path);
//                setResult(101, intent);
//                finish();
            }
        });

        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                CameraActivity.this.finish();
            }
        });
        jCameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(CameraActivity.this,"Right", Toast.LENGTH_SHORT).show();
            }
        });
        Log.i("CJT", DeviceUtil.getDeviceModel());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }


    private void RxUpLoad(String token, File[] files) {
        JDialogUtil.INSTANCE.showRxDialogShapeLoading(getContext());
        RxRestClient.builder()
                .url("video/upload")
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
                            upLoadInfo(token, "", filePath);
                        } else {
                            Toast.makeText(getContext(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                            JDialogUtil.INSTANCE.dismiss();
                        }

                    }

                    @Override
                    public void onFail(Throwable e) {
                        LatteLogger.e("picture/upload", "onFail:" + e.getMessage());
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        JDialogUtil.INSTANCE.dismiss();
                    }
                });
    }

    private void upLoadInfo(String token, String content, String filesString) {
        LatteLogger.w("upLoadImg", "upLoadInfo");
        final String url = "circle/insert";
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                //circleType  1-文本，2-照片，3-语音，4-视频，5-家书
                .params("circleType", 4)
                .params("contentType", 1)//1-文字，2-语音
                .params("content", content)
                .params("videoUrl", filesString)
                .params("visibleType", 1)
//                .params("visibleOrInvisibleUserIds",JSONArray.parseArray(JSON.toJSONString(visibleOrInvisibleUserIds2)))
                .params("location", "")
                .params("longitude", 0.0)
                .params("latitude", 0.0)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.json("circle/insert", response);
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            Toast.makeText(getContext(), "发送成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                        JDialogUtil.INSTANCE.dismiss();

                    }

                    @Override
                    public void onFail(Throwable e) {
                        LatteLogger.json("circle/insert", "onFail:" + e.getMessage());
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        JDialogUtil.INSTANCE.dismiss();
                    }
                });
    }

    private Context getContext(){
        return this;
    }

}
