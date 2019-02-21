package com.yijia.common_yijia.main.message.trtc;

import com.tencent.imsdk.TIMConversation;

import java.io.Serializable;

public class CallIntentData implements Serializable {
    public String peer;

    public int getRoomid() {
        return roomid;
    }

    public void setRoomid(int roomid) {
        this.roomid = roomid;
    }

    public int roomid;
    public TIMConversation conversation;

    public String getPeer() {
        return peer;
    }

    public void setPeer(String peer) {
        this.peer = peer;
    }

    public TIMConversation getConversation() {
        return conversation;
    }

    public void setConversation(TIMConversation conversation) {
        this.conversation = conversation;
    }
}
