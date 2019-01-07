package com.example.latte.app;

import com.example.latte.util.storage.LattePreference;

public class AccountManager {

    private enum SignTag {
        SIGN_TAG,
        ISCOMPLETE
    }

    //保存用户登录状态，登陆后调用
    public static void setSignState(boolean state) {
        LattePreference.setAppFlag(SignTag.SIGN_TAG.name(), state);
    }
    //保存用户登录状态，登陆后调用
    public static void setIsComplete(boolean iscomplete) {
        LattePreference.setAppFlag(SignTag.ISCOMPLETE.name(), iscomplete);
    }

    private static boolean isSignIn() {
        return LattePreference.getAppFlag(SignTag.SIGN_TAG.name());
    }
    private static boolean isComplete() {
        return LattePreference.getAppFlag(SignTag.ISCOMPLETE.name());
    }

    public static void checkAccont(IUserChecker checker) {
        if (isComplete()) {
            checker.onSignIn();
        } else {
            checker.onNoSignIn();
        }
    }

}
