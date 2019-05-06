package com.yijia.common_yijia.main.mine.setup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.yijia.app.AccountManager;
import com.example.yijia.app.IUserChecker;
import com.example.yijia.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.latte.ui.widget.HeadLayout;
import com.yijia.common_yijia.database.YjDatabaseManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 修改密码
 */
public class ChangePasswordDelegate extends LatteDelegate implements HeadLayout.OnClickHeadReturn {
    @BindView(R2.id.head_layout)
    HeadLayout headLayout;
    @BindView(R2.id.original_pwd)
    EditText originalPwd;
    @BindView(R2.id.new_pwd)
    EditText newPwd;
    @BindView(R2.id.confirm_pwd)
    EditText confirmPwd;
    @BindView(R2.id.btn_sign_in)
    AppCompatButton btnSignIn;
    private TextWatcher mTextWatcher;

    @Override
    public Object setLayout() {
        return R.layout.delegate_setpassword;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initHead();
        buttonColor();
    }

    //设置完成按钮的颜色变化
    private void buttonColor() {
        btnSignIn.setClickable(false);
        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(originalPwd.getText().toString()) && !TextUtils.isEmpty(newPwd.getText().toString()) && !TextUtils.isEmpty(confirmPwd.getText().toString())) {
                    btnSignIn.setClickable(true);
                    btnSignIn.setBackgroundResource(R.mipmap.buttom_login_orange);
                    btnSignIn.setTextColor(getResources().getColor(R.color.app_text_orange));
                } else {
                    btnSignIn.setClickable(false);
                    btnSignIn.setBackgroundResource(R.mipmap.buttom_login_gray);
                    btnSignIn.setTextColor(getResources().getColor(R.color.app_text_gray));
                }
            }
        };
        originalPwd.addTextChangedListener(mTextWatcher);
        newPwd.addTextChangedListener(mTextWatcher);
        confirmPwd.addTextChangedListener(mTextWatcher);
    }

    //初始化头布局
    private void initHead() {
        headLayout.setHeadleftImg(true, R.mipmap.fanhui);
        headLayout.setHeadName("修改密码", "#333333", 18);
        headLayout.setHeadlayoutBagColor("#ffffff");
        headLayout.setOnClickHeadReturn(this);
    }

    //销毁页面
    @Override
    public void onClickHeadReturn() {
        getSupportDelegate().pop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (headLayout != null) {
            headLayout = null;
        }
    }


    @OnClick(R2.id.btn_sign_in)
    public void onViewClicked() {
        boolean zhengze = zhengze(newPwd.getText().toString());
        //验证是不是8-16位的字母和数字
        if (zhengze) {
            //验证新密码是否和旧密码相等
            if (newPwd.getText().toString().equals(confirmPwd.getText().toString())) {
                //验证新密码是否和原密码相等
                if (TextUtils.equals(newPwd.getText().toString(),originalPwd.getText().toString())){
                    Toast.makeText(_mActivity, "新密码不能和旧密码相同", Toast.LENGTH_SHORT).show();
                }else{
                    final String url = "user/update_password";
                    AccountManager.checkAccont(new IUserChecker() {
                        @Override
                        public void onSignIn() {
                            String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
                            String oldPassword = originalPwd.getText().toString();
                            String newPassword = newPwd.getText().toString();
                            String confirmNewPawword = confirmPwd.getText().toString();

                            RxRestClient.builder()
                                    .url(url)
                                    .params("yjtk", token)
                                    .params("oldPassword", oldPassword)
                                    .params("newPassword", newPassword)
                                    .params("confirmNewPassword", confirmNewPawword)
                                    .build()
                                    .post()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new BaseObserver<String>(getContext()) {
                                        @Override
                                        public void onResponse(String response) {
                                            JSONObject object = JSON.parseObject(response);
                                            String status = object.getString("status");
                                            if (TextUtils.equals(status, "1001")) {
                                                getSupportDelegate().pop();
                                                Toast.makeText(_mActivity, "修改密码成功", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        @Override
                                        public void onFail(Throwable e) {
                                            Toast.makeText(_mActivity, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        @Override
                        public void onNoSignIn() {
                        }
                    });


                }

            } else {
                Toast.makeText(_mActivity, "新密码和原密码输入不一致", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(_mActivity, "新密码密码必须是8-16位的数字和字母", Toast.LENGTH_SHORT).show();
        }
    }

    //正则表达式 必须是8-16位的数字和字母
    private boolean zhengze(String string) {
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

}
