package com.yijia.common_yijia.main.message.trtc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatTextView;

import com.bokang.tencent_trtc_sdk.TrtcConfig;
import com.example.latte.ec.R;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.qcloud.bokang.BokangChatListener;
import com.tencent.qcloud.bokang.BokangChatManager;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.message.trtc2.TRTCMainActivity2;
import com.yijia.common_yijia.main.message.trtc2.TRTCMainActivity3;

/**
 * @author JiaLei, Email 15033111957@163.com, Date on 2019/2/27.
 * PS: Not easy to write code, please indicate.
 */
public class CallWaitingActivity extends Activity implements BokangChatListener, BoKangSendMessageListener {
    public static final String TYPE_KEY = "type";
    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_VOICE = 2;
    public static final int TYPE_KANHU = 3;
    int type = 0;

    BokangChatManager mBokangChatManager = null;
    TIMConversation conversation = null;
    Intent intent = null;
    int roomId = 0;
    String chatId = null;
    BokangSendMessageUtil bokangSendMessageUtil = null;

    ConstraintLayout clMute = null, clDrop = null, clSperker = null;
    AppCompatTextView tvDrop = null, tvDropb = null, tvMute = null, tvMuteb = null, tvSpeaker = null, tvSperkerb = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        if (intent != null) {
            roomId = intent.getIntExtra("roomid", 0);
            chatId = intent.getStringExtra("chatId");
            type = intent.getIntExtra(TYPE_KEY, 0);
        }
        setContentView(R.layout.activity_call_waiting_cl);
        mBokangChatManager = BokangChatManager.getInstance();
        mBokangChatManager.setBokangChatListener(this);
        conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, chatId);
        bokangSendMessageUtil = new BokangSendMessageUtil(conversation, this, getApplicationContext());

        initView();
    }

    private void initView() {
        clDrop = findViewById(R.id.cl_drop);
        tvDrop = findViewById(R.id.tv_drop);
        tvDropb = findViewById(R.id.tv_drop_b);
        clMute = findViewById(R.id.cl_mute);
        tvMute = findViewById(R.id.tv_mute);
        tvMuteb = findViewById(R.id.tv_mute_b);
        clSperker = findViewById(R.id.cl_speaker);
        tvSpeaker = findViewById(R.id.tv_speaker);
        tvSperkerb = findViewById(R.id.tv_speaker_b);

        clDrop.setOnClickListener(v -> {
            bokangSendMessageUtil.sendOnLineMessage(bokangSendMessageUtil.buildBokangMessage(MessageInfoUtil.BOKANG_VIDEO_REFUSE));
            finish();
        });
    }

    @Override
    public void newBokangMessage(TIMCustomElem ele, TIMConversation conversation) {
        if (new String(ele.getExt()).equals(MessageInfoUtil.BOKANG_VIDEO_CONNECT)) {
            if (chatId == null || "".equals(chatId) || roomId == 0) {
                return;
            }
            String sig = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getTencentImUserSig();
            String userId = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getTencentImUserId();
            final Intent intent = new Intent(this, TRTCMainActivity3.class);
            intent.putExtra("roomId", roomId);
            intent.putExtra("userId", userId);
            intent.putExtra("sdkAppId", TrtcConfig.SDKAPPID);
            intent.putExtra("userSig", sig);
            //TODO 需要指定会话ID（即聊天对象的identify，具体可参考IMSDK接入文档）
            intent.putExtra("chatId", chatId);
            switch (type) {
                case TYPE_VIDEO:
                    intent.putExtra(TRTCMainActivity3.TYPE_KEY, TRTCMainActivity3.TYPE_VIDEO);
                    break;
                case TYPE_VOICE:
                    intent.putExtra(TRTCMainActivity3.TYPE_KEY, TRTCMainActivity3.TYPE_VOICE);
                    break;
                case TYPE_KANHU:
                    intent.putExtra(TRTCMainActivity3.TYPE_KEY, TRTCMainActivity3.TYPE_KANHU);
                    break;
                default:
                    break;

            }

            startActivity(intent);
            finish();
        } else if (new String(ele.getExt()).equals(MessageInfoUtil.BOKANG_VIDEO_REFUSE)) {
            finish();
        }
    }

    @Override
    public void messageSuccess(TIMMessage timMessage) {

    }

    @Override
    public void messageError(int code, String desc) {

    }
}
