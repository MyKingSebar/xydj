package com.yijia.common_yijia.main.message.trtc;

import com.tencent.imsdk.TIMMessage;

public interface BoKangSendMessageListener {
     void messageSuccess(final TIMMessage timMessage);
     void messageError(final int code, final String desc);
}
