package com.yijia.common_yijia.main.message.trtc;

import android.content.Context;
import android.util.Log;

import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.log.QLog;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfo;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;

public class BokangSendMessageUtil {
    private BoKangSendMessageListener messageListener=null;
    private TIMConversation conversation=null;
    private Context mcontext=null;

    public BokangSendMessageUtil(TIMConversation conversation, BoKangSendMessageListener messageListener,Context mcontext) {
        this.conversation=conversation;
        this.messageListener = messageListener;
        this.mcontext=mcontext;
    }

//    public void setMessageListener(BoKangSendMessageListener messageListener) {
//        this.messageListener = messageListener;
//    }

    public void sendMessage(TIMMessage mMessage){
        if(conversation==null){
            Log.e("jialei","BokangSendMessageUtil.conversation==null");
            return;
        }
        conversation.sendMessage(mMessage, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(final int code, final String desc) {
                if(messageListener!=null){
                    messageListener.messageError(code,desc);
                }

            }

            @Override
            public void onSuccess(final TIMMessage timMessage) {
                if(messageListener!=null){
                    messageListener.messageSuccess(timMessage);
                }


            }
        });
    }


    public TIMMessage buildBokangMessage(String message){
        TIMMessage TIMMsg = new TIMMessage();
        TIMCustomElem ele =new TIMCustomElem();
        ele.setData(MessageInfoUtil.BOKANG.getBytes());
        ele.setExt(message.getBytes());
        TIMMsg.addElement(ele);
        return TIMMsg;
    }

}
