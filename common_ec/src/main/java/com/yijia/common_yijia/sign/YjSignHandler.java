package com.yijia.common_yijia.sign;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.app.AccountManager;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.database.YjUserProfile;

public class YjSignHandler {
    public static final String CODE_SUCCESS="1001";
    public static final String CODE_FAILE="9999";
    public static final String CODE_ILLEGAL_ARGUMENT="1003";
    public static final String CODE_REGISTERED="1004";
    public static final String CODE_ERROR_PHONE="0008";
    public static final String CODE_ERROR_AUTH_CODE="0009";

    public static void onSignIn(String response, ISignListener signListener) {
        final JSONObject object=JSON.parseObject(response);
        final String status=object.getString("status");
        if(TextUtils.equals(status,CODE_SUCCESS)){
            final JSONObject profileJson = object.getJSONObject("data");
            final String yjtk= profileJson.getString("yjtk");
            final JSONObject user=profileJson.getJSONObject("user");
            final Long id = user.getLong("id");
            final String nickname = user.getString("nickname");
            final String realName = user.getString("realName");
            final String phone = user.getString("phone");
            final String cardNo = user.getString("cardNo");
//            final String cardImage = user.getString("cardImage");
            final int gender = user.getInteger("gender");
            final String birthday = user.getString("birthday");
            final int userStatus = user.getInteger("userStatus");
            final int isComplete = user.getInteger("isComplete");
            final int isCertification = user.getInteger("isCertification");
//            final int inviterId = user.getInteger("inviterId");
//            final String createdTime = user.getString("createdTime");
//            final String modifiedTime = user.getString("modifiedTime");
            final String imagePath = user.getString("imagePath");

            final YjUserProfile profile = new YjUserProfile( id, yjtk,nickname, realName, phone,
                    cardNo, gender, birthday, userStatus, isComplete, isCertification
                    ,imagePath);

            //已经注册并登录成功了
            if(isComplete==1){

                YjDatabaseManager.getInstance().getDao().deleteAll();
                YjDatabaseManager.getInstance().getDao().insert(profile);
                AccountManager.setIsComplete(true);
                signListener.onSignUpSecondSuccess();
            }else {

                signListener.onSignInSuccess();
            }
        }else{
            signListener.onSignInFailure(object.getString("msg"));
        }
    }


    public static void onSignUp(String response, ISignListener signListener) {
        final JSONObject object=JSON.parseObject(response);
        final String status=object.getString("status");
        if(TextUtils.equals(status,CODE_SUCCESS)){
            final JSONObject profileJson = object.getJSONObject("data");
            final String yjtk= profileJson.getString("yjtk");
            final JSONObject user=profileJson.getJSONObject("user");
            final Long id = user.getLong("id");
            final String nickname = user.getString("nickname");
            final String realName = user.getString("realName");
            final String phone = user.getString("phone");
            final String cardNo = user.getString("cardNo");
//            final String cardImage = user.getString("cardImage");
            final int gender = user.getInteger("gender");
            final String birthday = user.getString("birthday");
            final int userStatus = user.getInteger("userStatus");
            final int isComplete = user.getInteger("isComplete");
            final int isCertification = user.getInteger("isCertification");
//            final int inviterId = user.getInteger("inviterId");
//            final String createdTime = user.getString("createdTime");
//            final String modifiedTime = user.getString("modifiedTime");
            final String imagePath = user.getString("imagePath");

            final YjUserProfile profile = new YjUserProfile( id, yjtk,nickname, realName, phone,
                    cardNo, gender, birthday, userStatus, isComplete, isCertification
                    ,imagePath);

            //已经注册并登录成功了
            if(isComplete==1) {
                YjDatabaseManager.getInstance().getDao().deleteAll();
                YjDatabaseManager.getInstance().getDao().insert(profile);
                AccountManager.setIsComplete(true);
                signListener.onSignUpSecondSuccess();
            }else {

                signListener.onSignUpSuccess();
            }
        }else{
            signListener.onSignUpFailure(object.getString("msg"));
        }

    }
}
