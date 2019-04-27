package com.tencent.qcloud.uikit.business.chat.c2c.presenter;

import android.text.TextUtils;

import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.qcloud.uikit.common.IUIKitCallBack;
import com.tencent.qcloud.uikit.business.chat.c2c.model.C2CChatInfo;
import com.tencent.qcloud.uikit.business.chat.c2c.model.C2CChatManager;
import com.tencent.qcloud.uikit.business.chat.c2c.model.C2CChatProvider;
import com.tencent.qcloud.uikit.business.chat.c2c.view.C2CChatPanel;
import com.tencent.qcloud.uikit.common.BackgroundTasks;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfo;
import com.tencent.qcloud.uikit.common.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by valxehuang on 2018/7/18.
 */

public class C2CChatPresenter {
    C2CChatPanel mChatPanel;
    C2CChatManager mChatManager;

    public C2CChatPresenter(C2CChatPanel chatPanel) {
        this.mChatPanel = chatPanel;
        mChatManager = C2CChatManager.getInstance();
    }


    public C2CChatInfo getC2CChatInfo(String peer, String name, String url) {
        C2CChatInfo chatInfo = mChatManager.getC2CChatInfo(peer, name, url);
        mChatManager.setCurrentChatInfo(chatInfo);
        return chatInfo;
    }

    public void loadChatMessages(final MessageInfo lastMessage) {
        mChatManager.loadChatMessages(lastMessage, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                if (lastMessage == null && data != null) {
                    C2CChatProvider cp = (C2CChatProvider) data;
                    ArrayList<MessageInfo> dataSource = (ArrayList<MessageInfo>)cp.getDataSource();
                        for(int j=dataSource.size()-1;j>0;j--){
                            MessageInfo info=dataSource.get(j);
                            for (int i = (int)(info.getTIMMessage().getElementCount() - 1); i >0 ; i--) {
                                TIMElem ele = info.getTIMMessage().getElement(i);
                                if (ele.getType() == TIMElemType.Custom) {
                                    TIMCustomElem tele = (TIMCustomElem) ele;
                                    if (TextUtils.equals(new String(tele.getData()), "bokang")) {
                                        dataSource.remove(j);
                                        break;
                                    }
                                }
                            }
                        }
                    cp.setDataSource(dataSource);
//                    mChatPanel.setDataProvider((C2CChatProvider) data);
                    mChatPanel.setDataProvider(cp);
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                UIUtils.toastLongMessage(errMsg);
            }
        });
    }

    public void sendC2CMessage(MessageInfo message, boolean reSend) {
        mChatManager.sendC2CMessage(message, reSend, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatPanel.scrollToEnd();
                    }
                });
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                UIUtils.toastLongMessage(errMsg);
            }
        });
    }

    public void deleteC2CMessage(int position, MessageInfo message) {
        mChatManager.deleteMessage(position, message);
    }

    public void revokeC2CMessage(int position, MessageInfo message) {
        mChatManager.revokeMessage(position, message);
    }

    public void exitC2CChat() {

    }
}
