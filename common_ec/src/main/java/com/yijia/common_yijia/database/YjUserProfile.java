package com.yijia.common_yijia.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "yj_user_profile")
public class YjUserProfile {


    @Id
    Long id = null;
    String yjtk = null;
    String nickname = null;
    String realName = null;
    String phone = null;
    String cardNo = null;
    int gender = 0;
    String birthday = null;
    int userStatus = 0;
    int isComplete = 0;
    int isCertification = 0;
    String imagePath=null;
    @Generated(hash = 923891639)
    public YjUserProfile(Long id, String yjtk, String nickname, String realName,
            String phone, String cardNo, int gender, String birthday,
            int userStatus, int isComplete, int isCertification, String imagePath) {
        this.id = id;
        this.yjtk = yjtk;
        this.nickname = nickname;
        this.realName = realName;
        this.phone = phone;
        this.cardNo = cardNo;
        this.gender = gender;
        this.birthday = birthday;
        this.userStatus = userStatus;
        this.isComplete = isComplete;
        this.isCertification = isCertification;
        this.imagePath = imagePath;
    }
    @Generated(hash = 900997910)
    public YjUserProfile() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getYjtk() {
        return this.yjtk;
    }
    public void setYjtk(String yjtk) {
        this.yjtk = yjtk;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getRealName() {
        return this.realName;
    }
    public void setRealName(String realName) {
        this.realName = realName;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getCardNo() {
        return this.cardNo;
    }
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
    public int getGender() {
        return this.gender;
    }
    public void setGender(int gender) {
        this.gender = gender;
    }
    public String getBirthday() {
        return this.birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public int getUserStatus() {
        return this.userStatus;
    }
    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }
    public int getIsComplete() {
        return this.isComplete;
    }
    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }
    public int getIsCertification() {
        return this.isCertification;
    }
    public void setIsCertification(int isCertification) {
        this.isCertification = isCertification;
    }
    public String getImagePath() {
        return this.imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }



    
}
