package com.yijia.common_yijia.main.friends.bean;

public class FriendsBean {
    private long friendUserId;
    private String nickname;
    private String realName;
    private String userStatus;
    private String userHead;
    private String identifier;
    private int isOnline;
    private String relation;

    public FriendsBean(long friendUserId, String nickname, String realName, String userStatus, String userHead, String identifier, int isOnline, String relation) {
        this.friendUserId = friendUserId;
        this.nickname = nickname;
        this.realName = realName;
        this.userStatus = userStatus;
        this.userHead = userHead;
        this.identifier = identifier;
        this.isOnline = isOnline;
        this.relation = relation;
    }

    public FriendsBean() {
    }

    public long getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(long friendUserId) {
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public boolean isOnline() {
        return isOnline == 1 ? true : false;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
