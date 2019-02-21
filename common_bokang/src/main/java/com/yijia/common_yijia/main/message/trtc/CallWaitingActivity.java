package com.yijia.common_yijia.main.message.trtc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.bokang.tencent_trtc_sdk.TrtcConfig;
import com.example.latte.activities.ProxyActivity;
import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.qcloud.uikit.business.chat.bokang.BokangChatListener;
import com.tencent.qcloud.uikit.business.chat.bokang.BokangChatManager;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;
import com.yijia.common_yijia.database.YjDatabaseManager;

import butterknife.BindView;
import butterknife.OnClick;

public class CallWaitingActivity extends Activity implements BokangChatListener ,BoKangSendMessageListener{
    BokangChatManager mBokangChatManager=null;
    TIMConversation conversation=null;
    Intent intent=null;
    int roomId=0;
    String chatId=null;
    BokangSendMessageUtil bokangSendMessageUtil=null;

    AppCompatTextView tvCancle=null;

//   @OnClick(R2.id.tv_cancle)
//   void cancle(){
//       bokangSendMessageUtil.sendMessage(bokangSendMessageUtil.buildBokangMessage(MessageInfoUtil.BOKANG_VIDEO_REFUSE));
//       finish();
//   }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent=getIntent();
        if(intent!=null){
            roomId=intent.getIntExtra("roomid",0);
            chatId=intent.getStringExtra("chatId");
        }
        setContentView(R.layout.activity_call_waiting);
        mBokangChatManager = BokangChatManager.getInstance();
        mBokangChatManager.setBokangChatListener(this);
        conversation=TIMManager.getInstance().getConversation(TIMConversationType.C2C,chatId);
        bokangSendMessageUtil=new BokangSendMessageUtil(conversation,this,getApplicationContext());
        tvCancle=findViewById(R.id.tv_cancle);
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bokangSendMessageUtil.sendMessage(bokangSendMessageUtil.buildBokangMessage(MessageInfoUtil.BOKANG_VIDEO_REFUSE));
                finish();
            }
        });
    }

    @Override
    public void newBokangMessage(TIMCustomElem ele, TIMConversation conversation) {
        if (new String(ele.getExt()).equals(MessageInfoUtil.BOKANG_VIDEO_CONNECT)) {
            if(chatId==null||chatId.equals("")||roomId==0){
                return;
            }
                String sig = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getTencentImUserSig();
                String userId = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getTencentImUserId();
                final Intent intent = new Intent(this, TRTCMainActivity.class);
                intent.putExtra("roomId", roomId);
                intent.putExtra("userId", userId);
                intent.putExtra("sdkAppId", TrtcConfig.SDKAPPID);
                intent.putExtra("userSig", sig);
                //TODO 需要指定会话ID（即聊天对象的identify，具体可参考IMSDK接入文档）
                intent.putExtra("chatId", chatId);
                startActivity(intent);
                finish();
        }else if(new String(ele.getExt()).equals(MessageInfoUtil.BOKANG_VIDEO_REFUSE)){
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
