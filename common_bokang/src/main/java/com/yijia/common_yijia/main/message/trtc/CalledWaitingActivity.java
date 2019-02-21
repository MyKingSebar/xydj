package com.yijia.common_yijia.main.message.trtc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.bokang.tencent_trtc_sdk.TrtcConfig;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.conversation.MessageListener;
import com.tencent.qcloud.uikit.business.chat.bokang.BokangChatListener;
import com.tencent.qcloud.uikit.business.chat.bokang.BokangChatManager;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;
import com.yijia.common_yijia.database.YjDatabaseManager;

import butterknife.OnClick;

public class CalledWaitingActivity extends Activity implements BoKangSendMessageListener, BokangChatListener {
    TIMConversation conversation = null;
    BokangSendMessageUtil bokangSendMessageUtil = null;
    BokangChatManager mBokangChatManager = null;

    AppCompatTextView tvCancle,tvConnect=null;
    int roomid=0;

//    @OnClick(R2.id.tv_cancle)
//    void cancle() {
//        bokangSendMessageUtil.sendMessage(bokangSendMessageUtil.buildBokangMessage(MessageInfoUtil.BOKANG_VIDEO_REFUSE));
//        finish();
//    }

//    @OnClick(R2.id.tv_connect)
//    void connect() {
//        bokangSendMessageUtil.sendMessage(bokangSendMessageUtil.buildBokangMessage(MessageInfoUtil.BOKANG_VIDEO_CONNECT));
//        String sig = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getTencentImUserSig();
//        String userId = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getTencentImUserId();
//        final Intent intent = new Intent(this, TRTCMainActivity.class);
//        intent.putExtra("roomId", 100);
//        intent.putExtra("userId", userId);
//        intent.putExtra("sdkAppId", TrtcConfig.SDKAPPID);
//        intent.putExtra("userSig", sig);
//        //TODO 需要指定会话ID（即聊天对象的identify，具体可参考IMSDK接入文档）
//        intent.putExtra("chatId", conversation.getPeer());
//        startActivity(intent);
//        finish();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_called_waiting);
        Intent intent = getIntent();
        CallIntentData data = (CallIntentData) intent.getSerializableExtra("CallIntentData");
        roomid=data.getRoomid();
        if (data != null) {
//            conversation = data.getConversation();
            conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C,data.getPeer());
        }
        if (conversation == null) {
            Toast.makeText(getApplicationContext(), "获取会话失败", Toast.LENGTH_LONG).show();
            finish();
        }
        bokangSendMessageUtil = new BokangSendMessageUtil(conversation, this, getApplicationContext());
        mBokangChatManager = BokangChatManager.getInstance();
        mBokangChatManager.setBokangChatListener(this);
        tvCancle=findViewById(R.id.tv_cancle);
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bokangSendMessageUtil.sendMessage(bokangSendMessageUtil.buildBokangMessage(MessageInfoUtil.BOKANG_VIDEO_REFUSE));
                finish();
            }
        });
        tvConnect=findViewById(R.id.tv_connect);
        tvConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bokangSendMessageUtil.sendMessage(bokangSendMessageUtil.buildBokangMessage(MessageInfoUtil.BOKANG_VIDEO_CONNECT));
                String sig = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getTencentImUserSig();
                String userId = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getTencentImUserId();
                final Intent intent = new Intent(CalledWaitingActivity.this, TRTCMainActivity.class);
                intent.putExtra("roomId", roomid);
                intent.putExtra("userId", userId);
                intent.putExtra("sdkAppId", TrtcConfig.SDKAPPID);
                intent.putExtra("userSig", sig);
                //TODO 需要指定会话ID（即聊天对象的identify，具体可参考IMSDK接入文档）
                intent.putExtra("chatId", conversation.getPeer());
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void messageSuccess(TIMMessage timMessage) {

    }

    @Override
    public void messageError(int code, String desc) {

    }

    @Override
    public void newBokangMessage(TIMCustomElem ele, TIMConversation conversation) {
        if (new String(ele.getExt()).equals(MessageInfoUtil.BOKANG_VIDEO_REFUSE)) {
            finish();
        }
    }
}
