package com.yijia.common_yijia.friends.bean;

public class GuardianBean {
    String userId;
    String realName;
    String nickname;
    String headImage;
    String isMain;
    String rongCloudToken;

    public GuardianBean() {
    }

    public GuardianBean(String userId, String realName, String nickname, String headImage, String isMain, String rongCloudToken) {
        this.userId = userId;
        this.realName = realName;
        this.nickname = nickname;
        this.headImage = headImage;
        this.isMain = isMain;
        this.rongCloudToken = rongCloudToken;
    }

    @Override
    public String toString() {
        return "GuardianBean{" +
                "userId='" + userId + '\'' +
                ", realName='" + realName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", headImage='" + headImage + '\'' +
                ", isMain='" + isMain + '\'' +
                ", rongCloudToken='" + rongCloudToken + '\'' +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
    }

    public String getRongCloudToken() {
        return rongCloudToken;
    }

    public void setRongCloudToken(String rongCloudToken) {
        this.rongCloudToken = rongCloudToken;
    }
}
