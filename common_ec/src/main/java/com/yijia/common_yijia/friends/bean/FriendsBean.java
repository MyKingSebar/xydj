package com.yijia.common_yijia.friends.bean;

public class FriendsBean {
    private String friendUserId;
    private String nickname;
    private String realName;
    private String userStatus;
    private String userHead;
    private String rongCloudToken;

    public FriendsBean() {

    }

    public FriendsBean(String friendUserId, String nickname, String realName, String userStatus, String userHead, String rongCloudToken) {
        this.friendUserId = friendUserId;
        this.nickname = nickname;
        this.realName = realName;
        this.userStatus = userStatus;
        this.userHead = userHead;
        this.rongCloudToken = rongCloudToken;
    }

    public String getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(String friendUserId) {
        this.friendUserId = friendUserId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public String getRongCloudToken() {
        return rongCloudToken;
    }

    public void setRongCloudToken(String rongCloudToken) {
        this.rongCloudToken = rongCloudToken;
    }

    @Override
    public String toString() {
        return "FriendsBean{" +
                "friendUserId='" + friendUserId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", realName='" + realName + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", userHead='" + userHead + '\'' +
                ", rongCloudToken='" + rongCloudToken + '\'' +
                '}';
    }
}
