package com.yijia.common_yijia.main.message.trtc2;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.latte.ec.R;

import java.lang.ref.WeakReference;

public class TRTCMoreDialog extends Dialog implements View.OnClickListener {
    private final static String TAG                                     = "TRTCMoreDialog";

    private final static String KEY_MORE_SETTING_DATA                   = "KEY_MORE_SETTING_DATA";

    private final static String KEY_CAMERA_FRONT                        = "KEY_CAMERA_FRONT";
    private final static String KEY_VIDEO_FILL_MODE                     = "KEY_VIDEO_FILL_MODE";
    private final static String KEY_VIDEO_VERTICAL                      = "KEY_VIDEO_VERTICAL";
    private final static String KEY_ENABLE_AUDIO_CAPTURE                = "KEY_ENABLE_AUDIO_CAPTURE";
    private final static String KEY_AUDIO_HAND_FREE_MODE                = "KEY_AUDIO_HAND_FREE_MODE";
    private final static String KEY_LOCAL_VIDEO_MIRROR                  = "KEY_LOCAL_VIDEO_MIRROR";
    private final static String KEY_REMOTE_VIDEO_MIRROR                 = "KEY_REMOTE_VIDEO_MIRROR";
    private final static String KEY_ENABLE_GSENSOR_MODE                 = "KEY_ENABLE_GSENSOR_MODE";
    private final static String KEY_AUDIO_VOLUME_EVALUATION             = "KEY_ENABLE_VOLUME_EVALUATION";
    private final static String KEY_ENABLE_CLOUD_MIXTURE                = "KEY_ENABLE_CLOUD_MIXTURE";


    private boolean mCameraFront                                        = true;
    private boolean mVideoFillMode                                      = true;
    private boolean mVideoVertical                                      = true;
    private boolean mEnableAudioCapture                                 = true;
    private boolean mAudioHandFreeMode                                  = true;
    private boolean mLocalVideoMirror                                   = true;
    private boolean mRemoteVideoMirror                                  = true;
    private boolean mEnableGSensorMode                                  = false;
    private boolean mAudioVolumeEvaluation                              = true;
    private boolean mEnableCloudMixture                                 = true;


    private RadioButton mRbCameraFront;
    private RadioButton mRbCameraBack;
    private RadioButton mRbVideoFill;
    private RadioButton mRbVideoAdjust;
    private RadioButton mRbVideoVertical;
    private RadioButton mRbVideoHorizontal;

    private CheckBox    mCbEnableAudio;
    private CheckBox    mCbAudioHandFree;
    private CheckBox    mCbLocalVideoMirror;
    private CheckBox    mCbRemoteVideoMirror;
    private CheckBox    mCbEnableGSensor;
    private CheckBox    mCbAudioVolumeEvaluation;
    private CheckBox    mCbEnableCloudMixture;


    public interface IMoreListener {
        void onSwitchCamera(boolean bFrontCamera);
        void onFillModeChange(boolean bFillMode);
        void onVideoRotationChange(boolean bVertical);
        void onEnableAudioCapture(boolean bEnable);
        void onEnableAudioHandFree(boolean bEnable);
        void onMirrorLocalVideo(boolean bMirror);
        void onMirrorRemoteVideo(boolean bMirror);
        void onEnableGSensor(boolean bEnable);
        void onEnableAudioVolumeEvaluation(boolean bEnable);
        void onEnableCloudMixture(boolean bEnable);
        void onClickButtonGetPlayUrl();
        void onClickButtonLinkMic();
    }

    private WeakReference<IMoreListener> mMoreListener;

    public TRTCMoreDialog(Context context, IMoreListener listener) {
        super(context, R.style.trtc_common_dlg);
        mMoreListener = new WeakReference<>(listener);
        loadLocalCache(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trtc_dlg_more);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        initView();
    }

    private void initView() {
        mRbCameraFront           = (RadioButton)findViewById(R.id.camera_front);
        mRbCameraBack            = (RadioButton)findViewById(R.id.camera_back);
        mRbVideoFill             = (RadioButton)findViewById(R.id.mode_fill);
        mRbVideoAdjust           = (RadioButton)findViewById(R.id.mode_adjust);
        mRbVideoVertical         = (RadioButton)findViewById(R.id.mode_vertical);
        mRbVideoHorizontal       = (RadioButton)findViewById(R.id.mode_horizontal);

        mCbEnableAudio           = (CheckBox)findViewById(R.id.enable_audio);
        mCbAudioHandFree         = (CheckBox)findViewById(R.id.audio_handfree);
        mCbLocalVideoMirror      = (CheckBox)findViewById(R.id.mirror_local_video);
        mCbRemoteVideoMirror     = (CheckBox)findViewById(R.id.mirror_remote_video);
        mCbEnableGSensor         = (CheckBox)findViewById(R.id.enable_gsensor);
        mCbAudioVolumeEvaluation = (CheckBox)findViewById(R.id.enable_audio_volume_evaluation);
        mCbEnableCloudMixture    = (CheckBox)findViewById(R.id.enable_cloud_mixture);

        mRbCameraFront.setChecked(mCameraFront);
        mRbCameraBack.setChecked(!mCameraFront);
        mRbVideoFill.setChecked(mVideoFillMode);
        mRbVideoAdjust.setChecked(!mVideoFillMode);
        mRbVideoVertical.setChecked(mVideoVertical);
        mRbVideoHorizontal.setChecked(!mVideoVertical);
        mCbEnableAudio.setChecked(mEnableAudioCapture);
        mCbAudioHandFree.setChecked(mAudioHandFreeMode);
        mCbLocalVideoMirror.setChecked(mLocalVideoMirror);
        mCbRemoteVideoMirror.setChecked(mRemoteVideoMirror);
        mCbEnableGSensor.setChecked(mEnableGSensorMode);
        mCbAudioVolumeEvaluation.setChecked(mAudioVolumeEvaluation);
        mCbEnableCloudMixture.setChecked(mEnableCloudMixture);

        mRbCameraFront.setOnClickListener(this);
        mRbCameraBack.setOnClickListener(this);
        mRbVideoFill.setOnClickListener(this);
        mRbVideoAdjust.setOnClickListener(this);
        mRbVideoVertical.setOnClickListener(this);
        mRbVideoHorizontal.setOnClickListener(this);
        mCbEnableAudio.setOnClickListener(this);
        mCbAudioHandFree.setOnClickListener(this);
        mCbLocalVideoMirror.setOnClickListener(this);
        mCbRemoteVideoMirror.setOnClickListener(this);
        mCbEnableGSensor.setOnClickListener(this);
        mCbAudioVolumeEvaluation.setOnClickListener(this);
        mCbEnableCloudMixture.setOnClickListener(this);
        findViewById(R.id.btn_get_playurl).setOnClickListener(this);
        findViewById(R.id.btn_linkmic).setOnClickListener(this);
    }


    public boolean isCameraFront() {
        return mCameraFront;
    }

    public boolean isVideoFillMode() {
        return mVideoFillMode;
    }

    public boolean isVideoVertical() {
        return mVideoVertical;
    }

    public boolean isEnableAudioCapture() {
        return mEnableAudioCapture;
    }

    public boolean isAudioHandFreeMode() {
        return mAudioHandFreeMode;
    }

    public boolean isLocalVideoMirror() {
        return mLocalVideoMirror;
    }

    public boolean isRemoteVideoMirror() {
        return mRemoteVideoMirror;
    }

    public boolean isEnableGSensorMode() {
        return mEnableGSensorMode;
    }

    public boolean isAudioVolumeEvaluation() {
        return mAudioVolumeEvaluation;
    }

    public boolean isEnableCloudMixture() {
        return mEnableCloudMixture;
    }

    private void loadLocalCache(Context context) {
        try {
            SharedPreferences shareInfo = context.getSharedPreferences(KEY_MORE_SETTING_DATA, 0);

            mCameraFront            = shareInfo.getBoolean(KEY_CAMERA_FRONT,            mCameraFront);
            mVideoFillMode          = shareInfo.getBoolean(KEY_VIDEO_FILL_MODE,         mVideoFillMode);
            mVideoVertical          = shareInfo.getBoolean(KEY_VIDEO_VERTICAL,          mVideoVertical);
            mEnableAudioCapture     = shareInfo.getBoolean(KEY_ENABLE_AUDIO_CAPTURE,    mEnableAudioCapture);
            mAudioHandFreeMode      = shareInfo.getBoolean(KEY_AUDIO_HAND_FREE_MODE,    mAudioHandFreeMode);
            mLocalVideoMirror       = shareInfo.getBoolean(KEY_LOCAL_VIDEO_MIRROR,      mLocalVideoMirror);
            mRemoteVideoMirror      = shareInfo.getBoolean(KEY_REMOTE_VIDEO_MIRROR,     mRemoteVideoMirror);
            mEnableGSensorMode      = shareInfo.getBoolean(KEY_ENABLE_GSENSOR_MODE,     mEnableGSensorMode);
            mAudioVolumeEvaluation  = shareInfo.getBoolean(KEY_AUDIO_VOLUME_EVALUATION, mAudioVolumeEvaluation);
            mEnableCloudMixture     = shareInfo.getBoolean(KEY_ENABLE_CLOUD_MIXTURE,    mEnableCloudMixture);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveData(Context context) {
        try {
            SharedPreferences shareInfo = context.getSharedPreferences(KEY_MORE_SETTING_DATA, 0);
            SharedPreferences.Editor editor = shareInfo.edit();
            editor.putBoolean(KEY_CAMERA_FRONT,            mCameraFront);
            editor.putBoolean(KEY_VIDEO_FILL_MODE,         mVideoFillMode);
            editor.putBoolean(KEY_VIDEO_VERTICAL,          mVideoVertical);
            editor.putBoolean(KEY_ENABLE_AUDIO_CAPTURE,    mEnableAudioCapture);
            editor.putBoolean(KEY_AUDIO_HAND_FREE_MODE,    mAudioHandFreeMode);
            editor.putBoolean(KEY_LOCAL_VIDEO_MIRROR,      mLocalVideoMirror);
            editor.putBoolean(KEY_REMOTE_VIDEO_MIRROR,     mRemoteVideoMirror);
            editor.putBoolean(KEY_ENABLE_GSENSOR_MODE,     mEnableGSensorMode);
            editor.putBoolean(KEY_AUDIO_VOLUME_EVALUATION, mAudioVolumeEvaluation);
            editor.putBoolean(KEY_ENABLE_CLOUD_MIXTURE,    mEnableCloudMixture);

            editor.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        IMoreListener listener = mMoreListener.get();

        int id = v.getId();
        if (id == R.id.camera_front || id == R.id.camera_back) {
            boolean cameraFront = (id == R.id.camera_front);
            if (cameraFront != mCameraFront) {
                mCameraFront = cameraFront;
                if (listener != null) {
                    listener.onSwitchCamera(mCameraFront);
                }
            }
        } else if (id == R.id.mode_fill || id == R.id.mode_adjust) {
            boolean videoFillMode = (id == R.id.mode_fill);
            if (videoFillMode != mVideoFillMode) {
                mVideoFillMode = videoFillMode;
                if (listener != null) {
                    listener.onFillModeChange(mVideoFillMode);
                }
            }
        } else if (id == R.id.mode_vertical || id == R.id.mode_horizontal) {
            boolean videoVertical = (id == R.id.mode_vertical);
            if (videoVertical != mVideoVertical) {
                mVideoVertical = videoVertical;
                if (listener != null) {
                    listener.onVideoRotationChange(mVideoVertical);
                }
            }
        } else if (id == R.id.enable_audio) {
            boolean enableAudioCapture = mCbEnableAudio.isChecked();
            if (enableAudioCapture != mEnableAudioCapture) {
                mEnableAudioCapture = enableAudioCapture;
                if (listener != null) {
                    listener.onEnableAudioCapture(mEnableAudioCapture);
                }
            }
        } else if (id == R.id.audio_handfree) {
            boolean audioHandFreeMode = mCbAudioHandFree.isChecked();
            if (audioHandFreeMode != mAudioHandFreeMode) {
                mAudioHandFreeMode = audioHandFreeMode;
                if (listener != null) {
                    listener.onEnableAudioHandFree(mAudioHandFreeMode);
                }
            }
        } else if (id == R.id.mirror_local_video) {
            boolean localVideoMirror = mCbLocalVideoMirror.isChecked();
            if (localVideoMirror != mLocalVideoMirror) {
                mLocalVideoMirror = localVideoMirror;
                if (listener != null) {
                    listener.onMirrorLocalVideo(mLocalVideoMirror);
                }
            }
        } else if (id == R.id.mirror_remote_video) {
            boolean remoteVideoMirror = mCbRemoteVideoMirror.isChecked();
            if (remoteVideoMirror != mRemoteVideoMirror) {
                mRemoteVideoMirror = remoteVideoMirror;
                if (listener != null) {
                    listener.onMirrorRemoteVideo(mRemoteVideoMirror);
                }
            }
        } else if (id == R.id.enable_gsensor) {
            boolean enableGSensorMode = mCbEnableGSensor.isChecked();
            if (enableGSensorMode != mEnableGSensorMode) {
                mEnableGSensorMode = enableGSensorMode;
                if (listener != null) {
                    listener.onEnableGSensor(mEnableGSensorMode);
                }
            }
        } else if (id == R.id.enable_audio_volume_evaluation) {
            boolean audioVolumeEvaluation = mCbAudioVolumeEvaluation.isChecked();
            if (audioVolumeEvaluation != mAudioVolumeEvaluation) {
                mAudioVolumeEvaluation = audioVolumeEvaluation;
                if (listener != null) {
                    listener.onEnableAudioVolumeEvaluation(mAudioVolumeEvaluation);
                }
            }
        } else if (id == R.id.enable_cloud_mixture) {
            boolean enableCloudMixture = mCbEnableCloudMixture.isChecked();
            if (enableCloudMixture != mEnableCloudMixture) {
                mEnableCloudMixture = enableCloudMixture;
                if (listener != null) {
                    listener.onEnableCloudMixture(mEnableCloudMixture);
                }
            }
        } else if (id == R.id.btn_get_playurl) {
            if (listener != null) {
                listener.onClickButtonGetPlayUrl();
            }
        } else if (id == R.id.btn_linkmic) {
            if (listener != null) {
                listener.onClickButtonLinkMic();
            }
            dismiss();
        }

        saveData(getContext());
    }

    public void show(boolean beingLinkMic) {
        updateLinkMicState(beingLinkMic);
        show();
    }

    public void updateLinkMicState(boolean beingLinkMic) {
        TextView textView = (TextView)findViewById(R.id.text_linkmic);
        if (textView != null) {
            textView.setText(beingLinkMic ? "结束跨房连麦" : "开始跨房连麦");
        }

        Button button = (Button)findViewById(R.id.btn_linkmic);
        if (button != null) {
            button.setText(beingLinkMic ? "结束" : "开始");
        }
    }

    public void updateVideoFillMode(boolean bFillMode) {
        if (mRbVideoFill != null) {
            mRbVideoFill.setChecked(bFillMode);
        }
        if (mRbVideoAdjust != null) {
            mRbVideoAdjust.setChecked(!bFillMode);
        }

        mVideoFillMode = bFillMode;

        saveData(getContext());
    }
}
