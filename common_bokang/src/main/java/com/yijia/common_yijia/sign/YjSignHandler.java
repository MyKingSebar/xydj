package com.yijia.common_yijia.sign;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.yijia.app.AccountManager;
import com.example.yijia.util.storage.LattePreference;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.database.YjUserProfile;

public class YjSignHandler {
    private static final String SIGNIN="IN";
    private static final String SIGNUP="UP";
    public static final String CODE_SUCCESS="1001";
    public static final String CODE_FAILE="9999";
    public static final String CODE_ILLEGAL_ARGUMENT="1003";
    public static final String CODE_REGISTERED="1004";
    public static final String CODE_ERROR_PHONE="0008";
    public static void onSignIn(String response, ISignListener signListener) {
        final JSONObject object=JSON.parseObject(response);
        final String status=object.getString("status");
        if(TextUtils.equals(status,CODE_SUCCESS)){
            int isComplete=saveAndIsComplete(object);
            signinOrSignup(isComplete,SIGNIN,signListener);
        }else{
            signListener.onSignInFailure(object.getString("msg"));
        }
    }


    public static void onSignUp(String response, ISignListener signListener) {
        final JSONObject object=JSON.parseObject(response);
        final String status=object.getString("status");
        if(TextUtils.equals(status,CODE_SUCCESS)){

        int isComplete=saveAndIsComplete(object);
            signinOrSignup(isComplete,SIGNUP,signListener);
        }else{
            signListener.onSignUpFailure(object.getString("msg"));
        }
    }

    public static void onSkipAddParents(ISignListener signListener) {
        signinOrSignup(1,SIGNUP,signListener);
    }
    private static int saveAndIsComplete(JSONObject object){
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
        final String tencentImUserId = user.getString("tencentImUserId");
        final String tencentImUserSig = user.getString("tencentImUserSig");
        final String inviteCode = user.getString("inviteCode");
        final int robotOnline = 0;
        final String tencentImIdRobot = user.getString("tencentImUserIdRobot");
//        final String shId = user.getString("2");
        final String shPassword = user.getString("shPassword");

        int needAddParents = 1;
        if(user.containsKey("requireAddParent"))
            needAddParents = user.getInteger("requireAddParent");
        LattePreference.setNeedAddParents(1== needAddParents ? true : false);


        final YjUserProfile profile = new YjUserProfile( id, yjtk,username,phone,email,nickname, imagePath, isComplete,
                userStatus, tencentImUserId,tencentImUserSig,inviteCode, robotOnline, tencentImIdRobot , "", shPassword);

        YjDatabaseManager.getInstance().getDao().deleteAll();
        YjDatabaseManager.getInstance().getDao().insert(profile);

        return isComplete;
    }

    private static void signinOrSignup(int isComplete,String type,ISignListener signListener){
        //已经注册并登录成功了
        if(isComplete==1) {
            AccountManager.setIsComplete(true);
            signListener.onSignUpSecondSuccess();

        }else {
            if(TextUtils.equals(type,SIGNIN)){
                signListener.onSignInSuccess();
            }else if(TextUtils.equals(type,SIGNUP)){
                signListener.onSignUpSuccess();
            }

        }
    }


}
