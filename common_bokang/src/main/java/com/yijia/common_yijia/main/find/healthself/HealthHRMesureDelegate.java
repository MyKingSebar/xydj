package com.yijia.common_yijia.main.find.healthself;

import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.latte.ec.R;
import com.example.yijia.delegates.LatteDelegate;
import com.yijia.common_yijia.main.find.healthself.CameraMesure.CameraPreviewView;
import com.yijia.common_yijia.main.find.healthself.CameraMesure.Complex;
import com.yijia.common_yijia.main.find.healthself.CameraMesure.FFT;

import static com.yijia.common_yijia.main.find.healthself.CameraMesure.CameraHelper.calculateAvgGrey;

public class HealthHRMesureDelegate extends LatteDelegate {

    private CameraPreviewView cameraPreviewView;
    private TextView progress;

    @Override
    public Object setLayout() {
        return R.layout.delegate_health_hr_mesure;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        initView(rootView);

        cameraPreviewView.setPreviewCallback(new PCallback());
        cameraPreviewView.openCameraFlashMode();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    private void initView(View rootView) {
        cameraPreviewView = rootView.findViewById(R.id.health_self_mesure_preview);
        progress = rootView.findViewById(R.id.health_self_mesure_progress);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseSource();
    }

    @Override
    public boolean onBackPressedSupport() {
        return false;
    }

    class PCallback implements Camera.PreviewCallback {

        private int[] mRgb;
        private Matrix matrix;
        private int count = 0;
        Complex[] avggreyArray;
        private long mStartTime;
        private long mEndTime;
        private int frameCount = 16;
        private int measureTime = 0;
        private int totalTime = 10;
        private int totalHeartRate = 0;


        public PCallback() {
            matrix = new Matrix();
            matrix.postRotate(90);
            avggreyArray = new Complex[frameCount];
        }

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            int width = camera.getParameters().getPreviewSize().width;
            int height = camera.getParameters().getPreviewSize().height;

            if (mRgb == null) {
                mRgb = new int[width * height];
            }

            float avggrey = calculateAvgGrey(data, width, height);

            //新加，把取到的灰度值散点放到数组中，用于后续的FFT处理
            avggreyArray[count] = new Complex((double) avggrey, 0.0);

            if (count == 0) {
                mStartTime = SystemClock.uptimeMillis();
            }

            count++;

            if (count == frameCount) {
                mEndTime = SystemClock.uptimeMillis();
                Complex[] rateArray = FFT.fft(avggreyArray);
                int maxNum = 1;
                double max = rateArray[1].abs();
                for (int i = 2; i < rateArray.length / 2; i++) {
                    double absnum = rateArray[i].abs();
                    if (absnum > max) {
                        max = absnum;
                        maxNum = i;
                    }
                }
                int heartrate = (int) (60 * 1000.0 / (mEndTime - mStartTime) * maxNum);
                if (heartrate >= 40 && heartrate <= 200) {
                    measureTime++;
                    totalHeartRate += heartrate;
                    progress.setText(measureTime * 100 / totalTime + "%");
                    ((HealthMainDelegate) HealthHRMesureDelegate.this.getParentDelegate()).setTips(R.string.health_self_mesure_right);
                } else {
                    ((HealthMainDelegate) HealthHRMesureDelegate.this.getParentDelegate()).setTips(R.string.health_self_mesure_error);
                }
                count = 0;
                if (measureTime == totalTime) {

                    releaseSource();

                    HealthResultDelegate resultDelegate = new HealthResultDelegate();
                    Bundle bundle = new Bundle();
                    bundle.putInt("result", totalHeartRate / totalTime);
                    resultDelegate.setArguments(bundle);
                    getSupportDelegate().start(resultDelegate);

                    measureTime = 0;
                    totalHeartRate = 0;
                }
            }
        }
    }

    private void releaseSource() {
        cameraPreviewView.closeCameraFlashMode();
        cameraPreviewView.removeCallback();
        cameraPreviewView.releaseCamera();
    }

}
