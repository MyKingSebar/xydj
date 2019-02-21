package com.yijia.common_yijia.sign;


public interface ISignListener {

    void onSignInSuccess();

    void onSignUpSecondSuccess();

    void onSignInFailure(String msg);

    void onSignUpSuccess();

    void onSignUpFailure(String msg);
}
