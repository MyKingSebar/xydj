package com.yijia.common_yijia.main.message.trtc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatTextView;
import android.widget.Toast;

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

/**
 * @author JiaLei, Email 15033111957@163.com, Date on 2019/2/27.
 * PS: Not easy to write code, please indicate.
 */
public class CalledWaitingActivity extends Activity implements BoKangSendMessageListener, BokangChatListener {
    TIMConversation conversation = null;
    BokangSendMessageUtil bokangSendMessageUtil = null;
    BokangChatManager mBokangChatManager = null;

    ConstraintLayout clDrop=null,clAnswer=null;
    AppCompatTextView tvDrop=null,tvAnswer=null;
    AppCompatTextView tvDropb=null,tvAnswerb=null;
    int roomid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_called_waiting_cl);
        Intent intent = getIntent();
        CallIntentData data = (CallIntentData) intent.getSerializableExtra("CallIntentData");
        roomid=data.getRoomid();
        if (data != null) {
            conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C,data.getPeer());
        }
        if (conversation == null) {
            Toast.makeText(getApplicationContext(), "获取会话失败", Toast.LENGTH_LONG).show();
            finish();
        }
        bokangSendMessageUtil = new BokangSendMessageUtil(conversation, this, getApplicationContext());
        mBokangChatManager = BokangChatManager.getInstance();
        mBokangChatManager.setBokangChatListener(this);
        initVIew();
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

    private void initVIew(){
        clDrop=findViewById(R.id.cl_drop);
        tvDrop=findViewById(R.id.tv_drop);
        tvDropb=findViewById(R.id.tv_drop_b);
        clAnswer=findViewById(R.id.cl_answer);
        tvAnswer=findViewById(R.id.tv_answer);
        tvAnswerb=findViewById(R.id.tv_answer_b);
        clDrop.setOnClickListener(v -> {
            bokangSendMessageUtil.sendMessage(bokangSendMessageUtil.buildBokangMessage(MessageInfoUtil.BOKANG_VIDEO_REFUSE));
            finish();
        });
        clAnswer.setOnClickListener(v -> {
            bokangSendMessageUtil.sendMessage(bokangSendMessageUtil.buildBokangMessage(MessageInfoUtil.BOKANG_VIDEO_CONNECT));
            String sig = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getTencentImUserSig();
            String userId = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getTencentImUserId();
            final Intent intent = new Intent(CalledWaitingActivity.this, TRTCMainActivity2.class);
            intent.putExtra("roomId", roomid);
            intent.putExtra("userId", userId);
            intent.putExtra("sdkAppId", TrtcConfig.SDKAPPID);
            intent.putExtra("userSig", sig);
            //TODO 需要指定会话ID（即聊天对象的identify，具体可参考IMSDK接入文档）
            intent.putExtra("chatId", conversation.getPeer());
            startActivity(intent);
            finish();
        });
    }
}
