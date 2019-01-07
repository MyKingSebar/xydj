package com.yijia.common_yijia.sign;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.net.rx.BaseObserver;
import com.example.latte.net.rx.RxRestClient;
import com.example.latte.util.log.LatteLogger;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SignInDelegate extends LatteDelegate {

    @BindView(R2.id.edit_login_phone)
    TextInputEditText mPhone = null;
    @BindView(R2.id.edit_login_password)
    TextInputEditText mPassword = null;

//    @BindView(R2.id.v_signup_phone_line)
//    View mPhoneLine = null;
//    @BindView(R2.id.v_signin_password_line)
//    View mPasswordLine = null;

    @BindView(R2.id.btn_sign_in)
    AppCompatButton mLogin = null;

    private ISignListener mISignListener = null;

    private TextWatcher mTextWatcher;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignListener) {
            mISignListener = (ISignListener) activity;
        }
    }

    @OnClick(R2.id.tv_login_chenge_note)
        void changenote(){
        getSupportDelegate().start(new SignInNoteDelegate());

        }
    @OnClick(R2.id.tv_login_forget)
        void forget(){

        }
    @OnClick(R2.id.im_login_wechart)
    void onClickWeChat(){

    }
    @OnClick(R2.id.im_login_qq)
        void onClickQQ(){

        }
    @OnClick(R2.id.im_login_weibo)
        void onClickWeiBo(){

        }


    @OnClick(R2.id.tv_link_sign_up)
    void onClickLink() {
        getSupportDelegate().start(new SignUpDelegate());
//        getSupportDelegate().startWithPop(new SignUpDelegate());
    }



    @OnClick(R2.id.btn_sign_in)
    void onClickSignIn() {
        if (checkForm()) {
            LatteLogger.json("user/login", mPassword.getText().toString());
            final String url = "user/login";
            RxRestClient.builder()
                    .url(url)
                    .params("phone",mPhone.getText().toString())
                    .params("credential",mPassword.getText().toString())
                    .params("loginType","2")//1-手机号验证码，2-账号密码
                    .build()
                    .post()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<String>(getContext()) {
                        @Override
                        public void onResponse(String response) {
                            LatteLogger.json("user/login", response);
                            YjSignHandler.onSignIn(response, mISignListener);
                        }

                        @Override
                        public void onFail(Throwable e) {

                        }
                    });


        }
    }




    private boolean checkForm() {
        final String phone = mPhone.getText().toString();
        final String password = mPassword.getText().toString();

        boolean isPass = true;

        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
//            mPhone.setError("错误的手机格式");
            Toast.makeText(getContext(),"错误的手机格式",Toast.LENGTH_SHORT).show();
            isPass = false;
            return false;
        } else {
//            mPhone.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
//            mPassword.setError("请填写至少6位数密码");
            Toast.makeText(getContext(),"请填写至少6位数密码",Toast.LENGTH_SHORT).show();
            isPass = false;
            return false;
        } else {
//            mPassword.setError(null);
        }

        return isPass;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_in_yijia;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mLogin.setClickable(false);
        mTextWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(mPhone.getText().toString())&&!TextUtils.isEmpty(mPassword.getText().toString())&&mPassword.getText().toString().length()>5){
                    mLogin.setClickable(true);
                    mLogin.setBackgroundResource(R.mipmap.buttom_login_orange);
                    mLogin.setTextColor(getResources().getColor(R.color.app_text_orange));
                }else {
                    mLogin.setClickable(false);
                    mLogin.setBackgroundResource(R.mipmap.buttom_login_gray);
                    mLogin.setTextColor(getResources().getColor(R.color.app_text_gray));
                }
            }
        };
//        mPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    mPhoneLine.setBackgroundResource(R.color.app_text_orange);
//                } else {
//                    mPhoneLine.setBackgroundResource(R.color.app_text_gray);
//                }
//            }
//        });
//
//        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    mPasswordLine.setBackgroundResource(R.color.app_text_orange);
//                } else {
//                    mPasswordLine.setBackgroundResource(R.color.app_text_gray);
//                }
//            }
//        });
        mPhone.addTextChangedListener(mTextWatcher);
        mPassword.addTextChangedListener(mTextWatcher);


    }

}
