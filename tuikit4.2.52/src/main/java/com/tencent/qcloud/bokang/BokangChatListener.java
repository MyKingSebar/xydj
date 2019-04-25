package com.tencent.qcloud.bokang;

import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMCustomElem;

public interface BokangChatListener {
     void newBokangMessage(TIMCustomElem ele, TIMConversation conversation);
}
