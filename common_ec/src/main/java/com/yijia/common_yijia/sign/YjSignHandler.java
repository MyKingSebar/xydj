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
    public static void onSignIn(String response, ISignListener signListener) {
        final JSONObject object=JSON.parseObject(response);
        final String status=object.getString("status");
        if(TextUtils.equals(status,CODE_SUCCESS)){
            final JSONObject profileJson = object.getJSONObject("data");
            final String yjtk= profileJson.getString("yjtk");
            final JSONObject user=profileJson.getJSONObject("user");
            final Long id = user.getLong("id");
            final String username = user.getString("username");
            final String phone = user.getString("phone");
            final String email = user.getString("email");
            final String nickname = user.getString("nickname");
            final String imagePath = user.getString("imagePath");
            final int isComplete = user.getInteger("isComplete");
            final int userStatus = user.getInteger("userStatus");
            final String identifier = user.getString("identifier");
            final String userSig = user.getString("userSig");


            final YjUserProfile profile = new YjUserProfile( id, yjtk,username,phone,email,nickname, imagePath, isComplete,
                    userStatus, identifier,userSig);

            YjDatabaseManager.getInstance().getDao().deleteAll();
            YjDatabaseManager.getInstance().getDao().insert(profile);
            //已经注册并登录成功了
            if(isComplete==1){
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
            final String username = user.getString("username");
            final String phone = user.getString("phone");
            final String email = user.getString("email");
            final String nickname = user.getString("nickname");
            final String imagePath = user.getString("imagePath");
            final int isComplete = user.getInteger("isComplete");
            final int userStatus = user.getInteger("userStatus");
            final String identifier = user.getString("identifier");
            final String userSig = user.getString("userSig");


            final YjUserProfile profile = new YjUserProfile( id, yjtk,username,phone,email,nickname, imagePath, isComplete,
                    userStatus, identifier,userSig);

            YjDatabaseManager.getInstance().getDao().deleteAll();
            YjDatabaseManager.getInstance().getDao().insert(profile);

            //已经注册并登录成功了
            if(isComplete==1) {
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
