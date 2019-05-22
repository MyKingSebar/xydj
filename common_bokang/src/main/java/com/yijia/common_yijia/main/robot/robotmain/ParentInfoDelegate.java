package com.yijia.common_yijia.main.robot.robotmain;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ec.R;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.util.PatternsUtil;
import com.example.yijia.util.callback.CallbackManager;
import com.example.yijia.util.callback.CallbackType;
import com.example.yijia.util.callback.IGlobalCallback;
import com.example.yijia.util.listener.OnSingleClickListener;
import com.luck.picture.lib.tools.ToastManage;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.sign.ISignListener;
import com.yijia.common_yijia.sign.YjSignHandler;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ParentInfoDelegate extends LatteDelegate {

    private String token = null;

    private RelativeLayout titleLeft;
    private AppCompatTextView title, titleRight;
    private boolean isFirstLogin = false;
    private boolean isFather = true;
    private EditText name, birth, phone;

    private RadioButton sonRadio;

    private TextView done;


    @Override
    public Object setLayout() {
        return R.layout.delegate_add_parent_info;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        isFather = getArguments().getBoolean(AddParentsDelegate.EXTRA_ISFATHER);
        isFirstLogin = getArguments().getBoolean(AddParentsDelegate.EXTRA_ISFIRST_LOGIN);
        initView(rootView);
    }

    private void initView(View rootView) {
        titleLeft = rootView.findViewById(R.id.tv_back);
        titleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportDelegate().pop();
            }
        });

        title = rootView.findViewById(R.id.tv_title);
        title.setText(isFather ? R.string.add_parents_father_info : R.string.add_parents_mather_info);

        titleRight = rootView.findViewById(R.id.tv_save);
        titleRight.setVisibility(View.GONE);

        name = rootView.findViewById(R.id.parent_info_name_value);
        name.setHint(isFather ? R.string.add_parents_father_name_hint : R.string.add_parents_mather_name_hint);
        birth = rootView.findViewById(R.id.parent_info_birth_value);
        birth.setHint(isFather ? R.string.add_parents_father_birth_hint : R.string.add_parents_mather_birth_hint);
        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        birth.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, 1970, 0, 1);
                dialog.show();
            }
        });
        phone = rootView.findViewById(R.id.parent_info_phone_value);
        phone.setHint(isFather ? R.string.add_parents_father_phone_hint : R.string.add_parents_mather_phone_hint);

        sonRadio = rootView.findViewById(R.id.parent_info_son);

        done = rootView.findViewById(R.id.parent_info_done);
        done.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {

                if(TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(getContext(), isFather ?R.string.add_parents_father_name_hint:R.string.add_parents_mather_name_hint, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(birth.getText().toString())) {
                    Toast.makeText(getContext(), isFather ?R.string.add_parents_father_birth_hint:R.string.add_parents_mather_birth_hint, Toast.LENGTH_SHORT).show();
                    return;
                }

                String p = phone.getText().toString();
                if (TextUtils.isEmpty(p) || !PatternsUtil.isPhone(p)) {
                    Toast.makeText(getContext(), "错误的手机格式", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveInfo();
            }
        });

    }

    private void saveInfo() {
        RxRestClient.builder()
                .url("family/insert_parent")
                .params("yjtk", YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk())
                .params("mainUserName", name.getText().toString())
                .params("mainUserBirthday", birth.getText().toString())
                .params("mainUserPhone", phone.getText().toString())
                .params("relationUserToMain", isFather ? 1 : 2)
                .params("relationMainToUser", sonRadio.isChecked() ? 3 : 4)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        Log.e("~~ save parents info", response);
                        final JSONObject object = JSON.parseObject(response);
                        final String status = object.getString("status");
                        if ("1001".equals(status)) {
                            getSupportDelegate().hideSoftInput();
                            IGlobalCallback callback = CallbackManager.getInstance().getCallback(CallbackType.ROBOT_REMIND_TAG);
                            if(null != callback) {
                                callback.executeCallback(new Object());
                            }
                            getSupportDelegate().popTo(AddParentsDelegate.class, true);
                            if (isFirstLogin) {
                                YjSignHandler.onSkipAddParents(signListener);
                            }
                        } else {
                            String msg = object.getString("msg");
                            ToastManage.s(getContext(), msg == null ? "服务器出错，请稍后再试" : msg);
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                    }
                });
    }

    private ISignListener signListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        signListener = (ISignListener) activity;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

}
