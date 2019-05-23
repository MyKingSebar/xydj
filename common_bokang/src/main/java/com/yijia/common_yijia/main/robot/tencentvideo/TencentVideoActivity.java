//package com.yijia.common_yijia.main.robot.tencentvideo;
//
//import android.Manifest;
//import android.animation.ValueAnimator;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.media.Ringtone;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.RemoteException;
//import android.os.SystemClock;
//import android.support.constraint.Group;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Chronometer;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.latte.ec.R;
//import com.tencent.rtmp.ui.TXCloudVideoView;
//import com.tencent.trtc.TRTCCloud;
//import com.tencent.trtc.TRTCCloudDef;
//
//public class TencentVideoActivity extends AppCompatActivity {
//
//    private TXCloudVideoView ownLayout, otherLayout;
//
//    private Group voiceGroup;
//
//    private ImageView mHead;
//    private TextView mName;
//    private TextView mCalling;
//    private ImageButton mHangup;
//    private ImageButton mAnswer;
//    private Chronometer mTimer;
//
//    private boolean isIncome;//判断是呼出还是呼入 默认呼出
//    private String type;
//    private String caller;//标识对方
//    private Ringtone ringtone;
//    private boolean isInRoom = false;
//
//    private EndBroadCastReceiver endBroadCastReceiver;
//
//    private boolean isVideo = false;
//
//
//    private int roomId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tencent_vedio);
//
//
//        isIncome = getIntent().getBooleanExtra(TencentExtraString.ISINCOME, false);
//        caller = getIntent().getStringExtra(TencentExtraString.EXTRA_TIM_TOUSER);
//        type = getIntent().getStringExtra(TencentExtraString.EXTRA_TIM_TYPE);
//        String romid = getIntent().getStringExtra(TencentExtraString.EXTRA_TIM_ROOMID);
//        if (!TextUtils.isEmpty(romid)) {
//            roomId = Integer.parseInt(romid);
//        }
//
//
//        isVideo = TencentExtraString.TYPE_VIDEO.equals(type);
//
//        initView();
//
//        initData();
//
//        setCallback(true);
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (null == endBroadCastReceiver) {
//            endBroadCastReceiver = new EndBroadCastReceiver();
//        }
//        registerReceiver(endBroadCastReceiver, new IntentFilter(TencentExtraString.ACTION_FINISH_ACTIVITY));
//
//    }
//
//    private void initView() {
//
//        mCalling = findViewById(R.id.nurse_view_calling);
//        mAnswer = findViewById(R.id.nurse_view_answer);
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
//
//        }
//
//        if (isVideo) {
//
//            ownLayout = findViewById(R.id.nurse_view_own);
//
//            ownLayout.setVisibility(View.VISIBLE);
//
//            initOtherView();
//
//            TRTCCloud.sharedInstance(TencentVideoActivity.this).setLocalViewRotation(180);
//            TRTCCloud.sharedInstance(TencentVideoActivity.this).setLocalViewFillMode(TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FIT);
//            TRTCCloud.sharedInstance(TencentVideoActivity.this).startLocalPreview(true, otherLayout);
//
//        }
//        if (isIncome) {
//            mAnswer.setVisibility(View.VISIBLE);
//
//        } else {
//            mCalling.setVisibility(View.VISIBLE);
//
//            int roomID = (int) ((Math.random() * 9 + 1) * 10000000);
//            if (isVideo) {
//                TIMController.getInstance(TencentVideoActivity.this).sendMsgViaTIM(TencentExtraString.TYPE_VIDEO, caller, roomID + "");
//            } else {
//                TIMController.getInstance(TencentVideoActivity.this).sendMsgViaTIM(TencentExtraString.TYPE_VOICE, caller, roomID + "");
//            }
//            TRTCCloud.sharedInstance(TencentVideoActivity.this).enterRoom(application.getTrtcParams(), TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL);
//
//        }
//
//        voiceGroup = findViewById(R.id.nuser_voice_group);
//
//        mHead = findViewById(R.id.nurse_view_head);
//        mName = findViewById(R.id.nurse_view_name);
//        mHangup = findViewById(R.id.nurse_view_hangup);
//        mHangup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isInRoom) {
//                    TRTCCloud.sharedInstance(TencentVideoActivity.this).exitRoom();
//                    isInRoom = false;
//                }
//                TIMController.getInstance(TencentVideoActivity.this).sendMsgViaTIM(TencentExtraString.TYPE_CLOSE, caller);
//                ringtone.stop();
//                finish();
//            }
//        });
//
//        mTimer = findViewById(R.id.nurse_view_timer);
//
//        mAnswer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ringtone.stop();
//
//                if (ActivityCompat.checkSelfPermission(TencentVideoActivity.this, Manifest.permission.RECORD_AUDIO)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    LogUtil.e(getPackageName(), "no permission for record_audio");
//                    return;
//                }
//
//                if (isVideo) {
//                    voiceGroup.setVisibility(View.GONE);
//
//                }
//                beginCommunicate();
//                isInRoom = true;
//                application.getTrtcParams().roomId = roomId;
//
//                TRTCCloud.sharedInstance(TencentVideoActivity.this).enterRoom(application.getTrtcParams(), TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL);
//
//                TIMController.getInstance(TencentVideoActivity.this).sendMsgViaTIM(TencentExtraString.TYPE_CONNECT, caller);
//
//            }
//        });
//    }
//
//    private void initOtherView() {
////        otherStub = findViewById(R.id.nurse_view_other_stub);
////        if (null != otherStub)
////            otherStub.inflate();
//
//        otherLayout = findViewById(R.id.nurse_view_other);
//        otherLayout.setVisibility(View.VISIBLE);
//    }
//
//
//    boolean aBoolean = true;
//    private void setCallback(boolean isNotNull) {
//        if (isNotNull)
//            application.setCallBack(new NurseApplication.CallBack() {
//                @Override
//                public void onEnterRoom() {
//                }
//
//                @Override
//                public void onUserEnter(String userId) {
//                    LogUtil.e("~~~onUserEnter~~~" + userId);
//
//                }
//
//                @Override
//                public void onUserVideoAvailable(String s, boolean b) {
//                    //TODO  为什么会回调两次？？
//                    if(b && aBoolean) {
//                        voiceGroup.setVisibility(View.GONE);
//                        beginCommunicate();
//                        TRTCCloud.sharedInstance(TencentVideoActivity.this).setRemoteViewFillMode(s, TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FIT);
//                        TRTCCloud.sharedInstance(TencentVideoActivity.this).startRemoteView(s, ownLayout);
//
//                        LogUtil.e("~~~onUserVideoAvailable~~~" + s + "~~" + b);
//
//                        changeView();
//                        aBoolean = false;
//                    } else {
//
//                    }
//                }
//
//                @Override
//                public void onUserAudioAvailable(String s, boolean b) {
//                    LogUtil.e("~~~onUserAudioAvailable~~~" + s + "~~" + b);
//                    beginCommunicate();
//                }
//
//                @Override
//                public void onUserExit(String s, int i) {
//                    if (isVideo) {
//                        TRTCCloud.sharedInstance(TencentVideoActivity.this).stopRemoteView(s);
//                    } else {
//                        TRTCCloud.sharedInstance(TencentVideoActivity.this).stopLocalAudio();
//                    }
//                }
//            });
//        else
//            application.setCallBack(null);
//    }
//
//    private void changeView() {
////        ViewGroup.LayoutParams ownParam = ownLayout.getLayoutParams();
////        ViewGroup.LayoutParams otherParam = otherLayout.getLayoutParams();
////
////        ConstraintLayout layout = findViewById(R.id.nurse_main_container);
////
////        ownLayout.setLayoutParams(otherParam);
////        otherLayout.setLayoutParams(ownParam);
////        layout.bringChildToFront(ownLayout);
//
//
//        ValueAnimator anim = ValueAnimator.ofFloat(1f, 0.3f);
//        anim.setDuration(500);
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            ViewGroup.LayoutParams otherParam = otherLayout.getLayoutParams();
//            int width = otherLayout.getWidth();
//            int height = otherLayout.getHeight();
//
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float currentValue = (float) animation.getAnimatedValue();
//                otherParam.width = (int)(width*currentValue);
//                otherParam.height = (int)(height*currentValue);
//
//            }
//        });
//        anim.start();
//    }
//
//    private void beginCommunicate() {
//        mCalling.setVisibility(View.GONE);
//        mAnswer.setVisibility(View.GONE);
//        mTimer.setVisibility(View.VISIBLE);
//        //设置开始计时时间
//        mTimer.setBase(SystemClock.elapsedRealtime());
//        //启动计时器
//        mTimer.start();
//
//        TRTCCloud.sharedInstance(TencentVideoActivity.this).startLocalAudio();
//    }
//
//    private void initData() {
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//
//                TIMController.getInstance(TencentVideoActivity.this).getUserNameAndHeadUrl(caller, new ITIMServiceUserInfoCallback.Stub() {
//                    @Override
//                    public void onSuccess(final String name, final String url) throws RemoteException {
//                        LogUtil.i("~~im login success--" + name + "--" + url);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (!TextUtils.isEmpty(name)) {
//                                    mName.setText(name);
//                                }
//                                if (!TextUtils.isEmpty(url)) {
//                                    Picasso.with(TencentVideoActivity.this)
//                                            .load(url)
//                                            .transform(new CircleTransform())
//                                            .error(R.mipmap.phone_default_head)
//                                            .into(mHead);
//                                }
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onError(int i, String s) throws RemoteException {
//                        LogUtil.e("~~im login error--" + i + "---" + s);
//                    }
//                });
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//
//                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//                ringtone = RingtoneManager.getRingtone(TencentVideoActivity.this, notification);
//                ringtone.play();
//            }
//        }.execute();
//
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        ringtone.stop();
//        setCallback(false);
//        if (isInRoom) {
//            TRTCCloud.sharedInstance(TencentVideoActivity.this).exitRoom();
//        }
//        if (null != endBroadCastReceiver) {
//            unregisterReceiver(endBroadCastReceiver);
//            endBroadCastReceiver = null;
//        }
//    }
//
//
//    class EndBroadCastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            finish();
//        }
//    }
//}
