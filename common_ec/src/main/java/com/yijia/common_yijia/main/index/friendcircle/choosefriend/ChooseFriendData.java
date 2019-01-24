package com.yijia.common_yijia.main.index.friendcircle.choosefriend;

import com.example.latte.ui.contactlist.cn.CN;

public class ChooseFriendData implements CN {

     Long friendUserId;
     String nickname;
     String userStatus;
     String userHead;
     String tencentImUserId;

    public Long getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(Long friendUserId) {
        this.friendUserId = friendUserId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public String getTencentImUserId() {
        return tencentImUserId;
    }

    public void setTencentImUserId(String tencentImUserId) {
        this.tencentImUserId = tencentImUserId;
    }

    @Override
    public String chinese() {
        return null;
    }
}
