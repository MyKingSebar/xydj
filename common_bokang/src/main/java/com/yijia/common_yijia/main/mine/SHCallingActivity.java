package com.yijia.common_yijia.main.mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.latte.ec.R;
import com.example.yijia.util.GlideUtils;
import com.shshcom.SHVoIPSDK;
import com.yijia.common_yijia.main.friends.SHImpl;

public class SHCallingActivity extends AppCompatActivity {

    private ImageView mHead;
    private TextView mName;
    private TextView mCalling;
    private ImageButton mHangup;
    private ImageButton mAnswer;
    private Chronometer mTimer;

    private boolean isIncome;//判断是呼出还是呼入 默认呼出
    private String phoneNum;

    private AnswerBroadCastReceiver answerBroadCastReceiver;

    private EndBroadCastReceiver endBroadCastReceiver;

    private boolean isAnswer = false;//判断来电是否被接听

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shcalling);


        isIncome = getIntent().getBooleanExtra(ExtraString.ISINCOME, false);

        phoneNum = getIntent().getStringExtra(ExtraString.PHONE_NUM);
//        phoneNum = "15510253516";
        initView();

        initData();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isIncome && !TextUtils.isEmpty(phoneNum)) {
                if (ActivityCompat.checkSelfPermission(SHCallingActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 11);
                    return;
                }
            }
        }
        SHImpl.getInstance(this).call(phoneNum);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (null == answerBroadCastReceiver) {
            answerBroadCastReceiver = new AnswerBroadCastReceiver();

        }
        registerReceiver(answerBroadCastReceiver, new IntentFilter(ExtraString.BROADCAST_ANSWER_BEGIN));

        if (null == endBroadCastReceiver) {
            endBroadCastReceiver = new EndBroadCastReceiver();
        }
        registerReceiver(endBroadCastReceiver, new IntentFilter(ExtraString.BROADCAST_END_BEGIN));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != answerBroadCastReceiver) {
            unregisterReceiver(answerBroadCastReceiver);
            answerBroadCastReceiver = null;
        }

        if (null != endBroadCastReceiver) {
            unregisterReceiver(endBroadCastReceiver);
            endBroadCastReceiver = null;
        }
    }

    private void initData() {
        String url = getIntent().getStringExtra(ExtraString.PHONE_URL);
        if (!TextUtils.isEmpty(url))
            GlideUtils.load(this, url, mHead, GlideUtils.USERMODE);

        String name = getIntent().getStringExtra(ExtraString.PHONE_NAME);
        if (!TextUtils.isEmpty(name))
            mName.setText(name);
        else
            mName.setText(phoneNum);
    }

    private void initView() {
        mHead = findViewById(R.id.phone_head);
        mHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//                    LogUtil.e(getPackageName(), "has no record_audio permission");
//                    return;
//                }
//                SHVoIPSDK.getInstance().callOut("13121956152");

//                Intent intent = new Intent(ExtraString.BROADCAST_CALL_FROM_SYSTEM);
//                intent.putExtra("name", "就是这样");
//                sendBroadcast(intent);
            }
        });
        mName = findViewById(R.id.phone_name);
        mHangup = findViewById(R.id.phone_hangup);
        mHangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHVoIPSDK.getInstance().hangup();
                //在超时未接听时 实话 未调取onCallEnd   独立处理finish
                if (!isAnswer)
                    finish();
            }
        });

        mTimer = findViewById(R.id.phone_timer);

        mAnswer = findViewById(R.id.phone_answer);
        mAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(SHCallingActivity.this, Manifest.permission.RECORD_AUDIO)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                        return;
                    }
                }

                SHVoIPSDK.getInstance().answerCall();


            }
        });

        mCalling = findViewById(R.id.phone_calling);

        if (isIncome) {
            mAnswer.setVisibility(View.VISIBLE);

        } else {
            mCalling.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SHImpl.getInstance(this).call(phoneNum);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //通知事件处理service 增加未处理事件
        if (isIncome && !isAnswer) {
            Intent intent = new Intent(ExtraString.BROADCAST_SEND_CALL_NO_RESPONSE);
            intent.putExtra(ExtraString.EXTRA_SEND_CALL_NO_RESPONSE, phoneNum);
            sendBroadcast(intent);
        }
    }

    class AnswerBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            isAnswer = true;
            mAnswer.setVisibility(View.GONE);
            mTimer.setVisibility(View.VISIBLE);
            //设置开始计时时间
            mTimer.setBase(SystemClock.elapsedRealtime());
            //启动计时器
            mTimer.start();
        }
    }

    class EndBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

}
