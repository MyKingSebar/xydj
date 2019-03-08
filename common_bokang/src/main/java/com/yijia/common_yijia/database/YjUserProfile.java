package com.yijia.common_yijia.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "yj_user_profile")
public class YjUserProfile {
    @Id
    Long id = null;
    String yjtk = null;
    String username = null;
    String phone = null;
    String email = null;
    String nickname = null;
    String imagePath = null;
    int isComplete = 0;
    int userStatus = 0;
    String tencentImUserId = null;
    String tencentImUserSig = null;
    @Generated(hash = 57744088)
    public YjUserProfile(Long id, String yjtk, String username, String phone,
            String email, String nickname, String imagePath, int isComplete,
            int userStatus, String tencentImUserId, String tencentImUserSig) {
        this.id = id;
        this.yjtk = yjtk;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.nickname = nickname;
        this.imagePath = imagePath;
        this.isComplete = isComplete;
        this.userStatus = userStatus;
        this.tencentImUserId = tencentImUserId;
        this.tencentImUserSig = tencentImUserSig;
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
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getImagePath() {
        return this.imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public int getIsComplete() {
        return this.isComplete;
    }
    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }
    public int getUserStatus() {
        return this.userStatus;
    }
    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }
    public String getTencentImUserId() {
        return this.tencentImUserId;
    }
    public void setTencentImUserId(String tencentImUserId) {
        this.tencentImUserId = tencentImUserId;
    }
    public String getTencentImUserSig() {
        return this.tencentImUserSig;
    }
    public void setTencentImUserSig(String tencentImUserSig) {
        this.tencentImUserSig = tencentImUserSig;
    }
    



}