package com.yijia.common_yijia.sign;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
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
import com.example.latte.util.timer.BaseTimerTask;
import com.example.latte.util.timer.ITimeListener;

import java.text.MessageFormat;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ForgetPasswordDelegate extends LatteDelegate implements ITimeListener {

    @BindView(R2.id.edit_logup_phone)
    TextInputEditText mPhone = null;
    @BindView(R2.id.edit_logup_note)
    TextInputEditText mNote = null;
    @BindView(R2.id.edit_forget_password)
    TextInputEditText mPassword = null;
//    @BindView(R2.id.edit_forget_password_again)
//    TextInputEditText mPasswordAgain = null;
    @BindView(R2.id.tv_getnote)
    AppCompatTextView mGetNote = null;


    @BindView(R2.id.btn_sign_up)
    AppCompatButton mLogin = null;

    private Timer mTimer = null;
    private int mCount = 60;

    private TextWatcher mTextWatcher;


    private ISignListener mISignListener = null;

    private void initTimer() {
        mTimer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        mTimer.schedule(task, 0, 1000);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignListener) {
            mISignListener = (ISignListener) activity;
        }
    }

    @OnClick(R2.id.tv_getnote)
    void onClickGetNote() {
        final String phone = mPhone.getText().toString();
        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
//            mPhoneL.setError("错误的手机格式");
            Toast.makeText(getContext(), "错误的手机格式", Toast.LENGTH_SHORT).show();
        } else {
//            mPhoneL.setError(null);
            mCount=60;
            mGetNote.setClickable(false);
            mGetNote.setTextColor(getResources().getColor(R.color.app_text_gray));
            mGetNote.setBackgroundResource(R.mipmap.buttom_logup_getnote_gray);
            initTimer();

            final String url = "sms/auth_code_send";
            RxRestClient.builder()
                    .url(url)
                    .params("phone", mPhone.getText().toString())
                    .build()
                    .post()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<String>(getContext()) {
                        @Override
                        public void onResponse(String response) {
                            LatteLogger.json("USER_PROFILE", response);

                            Toast.makeText(getContext(), "发送成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFail(Throwable e) {

                        }
                    });
        }

    }

    @OnClick(R2.id.tv_back)
    void back(){
        getSupportDelegate().pop();
    }
    @OnClick(R2.id.btn_sign_up)
    void onClickSignUp() {
        if (checkForm()) {
            final String url = "user/register";
            RxRestClient.builder()
                    .url(url)
                    .params("phone", mPhone.getText().toString())
                    .params("authCode", mNote.getText().toString())
                    .build()
                    .post()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<String>(getContext()) {
                        @Override
                        public void onResponse(String response) {
                            LatteLogger.json("USER_PROFILE", response);
                            YjSignHandler.onSignUp(response, mISignListener);
                        }

                        @Override
                        public void onFail(Throwable e) {
                            Toast.makeText(getContext(), "请稍后尝试", Toast.LENGTH_SHORT).show();
                        }
                    });
//            Toast.makeText(getContext(), "验证通过", Toast.LENGTH_SHORT).show();
        }
    }



    private boolean checkForm() {
        final String phone = mPhone.getText().toString();
        final String note = mNote.getText().toString();
        final String password = mPassword.getText().toString();
//        final String passwordagain = mPasswordAgain.getText().toString();

        boolean isPass = true;

        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
//            mPhoneL.setError("错误的手机格式");
            Toast.makeText(getContext(), "错误的手机格式", Toast.LENGTH_SHORT).show();
            isPass = false;
            return false;
        } else {
//            mPhoneL.setError(null);
        }
        if (note.isEmpty()) {
            Toast.makeText(getContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
//            mNoteL.setError("验证码不能为空");
            isPass = false;
            return false;
        } else {
//            mNoteL.setError(null);
        }
        if (password.isEmpty()) {
            Toast.makeText(getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
//            mNoteL.setError("验证码不能为空");
            isPass = false;
            return false;
        } else if(password.length()<6){
            Toast.makeText(getContext(), "密码请大于6位", Toast.LENGTH_SHORT).show();
        }
//        if (passwordagain.isEmpty()) {
//            Toast.makeText(getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
////            mNoteL.setError("验证码不能为空");
//            isPass = false;
//            return false;
//        } else if(!TextUtils.equals(password,passwordagain)){
//            Toast.makeText(getContext(), "密码不一致", Toast.LENGTH_SHORT).show();
//        }


        return isPass;
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_forget_password_yijia;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
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
                if(!TextUtils.isEmpty(mPhone.getText().toString())&&!TextUtils.isEmpty(mNote.getText().toString())){
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
        mPhone.addTextChangedListener(mTextWatcher);
        mNote.addTextChangedListener(mTextWatcher);

    }

    @Override
    public void onTimer() {

        getProxyActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mGetNote != null) {
                    mGetNote.setText(MessageFormat.format("({0}s)", mCount));
                    mCount--;
                    if (mCount < 0) {
                        if (mTimer != null) {
                            mTimer.cancel();
                            mTimer = null;
                            mGetNote.setClickable(true);
                            mGetNote.setBackgroundResource(R.mipmap.buttom_logup_getnote_orange);
                            mGetNote.setText("获取验证码");
                            mGetNote.setTextColor(getResources().getColor(R.color.app_text_orange));
                        }
                    }
                }
            }
        });

    }
}
