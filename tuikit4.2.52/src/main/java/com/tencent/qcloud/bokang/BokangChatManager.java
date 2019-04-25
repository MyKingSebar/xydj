package com.tencent.qcloud.bokang;

import android.util.Log;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMMessageExt;
import com.tencent.imsdk.log.QLog;
import com.tencent.qcloud.uikit.business.chat.c2c.model.C2CChatInfo;
import com.tencent.qcloud.uikit.business.chat.c2c.model.C2CChatProvider;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfo;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BokangChatManager implements TIMMessageListener{
    private static final String TAG = "C2CChatManager";
    private static BokangChatManager instance = new BokangChatManager();
    private static final int MSG_PAGE_COUNT = 10;
    private C2CChatProvider mCurrentProvider;
    private TIMConversation mCurrentConversation;
    private TIMConversationExt mCurrentConversationExt;
    private C2CChatInfo mCurrentChatInfo;
    private Map<String, C2CChatInfo> mC2CChats = new HashMap<>();
    private boolean hasMore;
    private boolean mLoading;

    private BokangChatListener mBokangChatListener=null;

    public static BokangChatManager getInstance() {
        return instance;
    }


    private BokangChatManager() {

    }

    public void setBokangChatListener(BokangChatListener mBokangChatListener){
        this.mBokangChatListener=mBokangChatListener;
    }

    public void init() {
        TIMManager.getInstance().addMessageListener(instance);
    }

    public void setCurrentChatInfo(C2CChatInfo info) {
        if (info == null) {
            return;
        }
        mCurrentChatInfo = info;
        mCurrentConversation = TIMManager.getInstance().getConversation(info.getType(), info.getPeer());
        mCurrentConversationExt = new TIMConversationExt(mCurrentConversation);
        mCurrentProvider = new C2CChatProvider();
        hasMore = true;
        mLoading = false;
    }


    public boolean addChatInfo(C2CChatInfo chatInfo) {
        return mC2CChats.put(chatInfo.getPeer(), chatInfo) == null;
    }

    public C2CChatInfo getC2CChatInfo(String peer) {
        C2CChatInfo chatInfo = mC2CChats.get(peer);
        if (chatInfo == null) {
            chatInfo = new C2CChatInfo();
            chatInfo.setPeer(peer);
            chatInfo.setChatName(peer);
            mC2CChats.put(peer, chatInfo);
        }
        return chatInfo;
    }







    @Override
    public boolean onNewMessages(List<TIMMessage> msgs) {
        for (TIMMessage msg : msgs) {
            long count=msg.getElementCount();
            TIMConversation conversation=msg.getConversation();
            for(int i=0;i<count;i++){
                TIMElem ele=msg.getElement(i);
//                TIMCustomElem ele= (TIMCustomElem) msg.getElement(i);
                if(ele.getType()==TIMElemType.Custom){
                    //TODO 自定义消息接收
                    TIMCustomElem customElem = (TIMCustomElem) ele;
                    String data = new String(customElem.getData());
                    if (data.equals(MessageInfoUtil.BOKANG)) {
                        if(mBokangChatListener!=null){
                            mBokangChatListener.newBokangMessage(customElem,conversation);
                            TIMConversationExt conExt = new TIMConversationExt(conversation);
                            conExt.setReadMessage(null, new TIMCallBack() {
                                @Override
                                public void onError(int code, String desc) {
                                    Log.e("~~~", "setReadMessage failed, code: " + code + "|desc: " + desc);
                                }

                                @Override
                                public void onSuccess() {
                                    Log.d("~~~", "setReadMessage succ");
                                }
                            });
                            TIMMessageExt msgExt = new TIMMessageExt(msg);
                            msgExt.remove();
//        Log.e("~~TIMService dm:", msgExt.remove() + "");
                        }
                        Log.d("jialei","bokang自定义消息接收:"+new String(customElem.getExt()));
                    }
                }
            }
        }
        return false;
    }

    private void onReceiveMessage(final TIMConversation conversation, final TIMMessage msg) {
        if (conversation == null || conversation.getPeer() == null || mCurrentChatInfo == null) {
            QLog.i(TAG, "onReceiveMessage::: " + (conversation == null) + (conversation.getPeer() == null) + (mCurrentChatInfo == null));
            return;
        }
        executeMessage(conversation, msg);
    }


    private void executeMessage(TIMConversation conversation, TIMMessage msg) {
        final MessageInfo msgInfo = MessageInfoUtil.TIMMessage2MessageInfo(msg, false);
        Log.d("jialei","MSG_TYPE_BOKANGgetTIMMessage:"+msgInfo.getExtra());
        if(msgInfo.getMsgType()==MessageInfo.MSG_TYPE_BOKANG){
            Log.d("jialei","getMsgType:"+msgInfo.getMsgType());
        }else if (msgInfo != null && mCurrentConversation != null && mCurrentConversation.getPeer().equals(conversation.getPeer())) {
            mCurrentProvider.addMessageInfo(msgInfo);
            msgInfo.setRead(true);
            mCurrentConversationExt.setReadMessage(null, new TIMCallBack() {
                @Override
                public void onError(int code, String desc) {
                    QLog.e(TAG, "setReadMessage failed, code: " + code + "|desc: " + desc);
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "setReadMessage succ");
                }
            });
        }
    }


    public void destroyBOKanghat() {
    }

}
