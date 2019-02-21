package com.tencent.qcloud.uikit.business.chat.bokang;

import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMCustomElem;

public interface BokangChatListener {
     void newBokangMessage(TIMCustomElem ele,TIMConversation conversation);
}
