package com.yijia.common_yijia.main.mine;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.common_tencent_tuikit.Constants;
import com.example.latte.ec.R;
import com.example.latte.ui.widget.RobotImageView;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.dialog.JDialogUtil;
import com.example.yijia.util.GlideUtils;
import com.example.yijia.util.listener.OnSingleClickListener;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.message.trtc2.PersonalChatFragment2;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserDetailDelegate extends LatteDelegate {

    private RelativeLayout mBack;

    private AppCompatTextView mTitle;

    private RobotImageView robotImageView;

    private AppCompatTextView mName, mNickName, mRelationKey, mRelationValue, mPermissionValue, mSendMsg, mSendCall;

    private long userId, familyId;

    private String tencentImUserId, shNumber;

    private String name, url;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = getArguments().getLong("id");

        familyId = getArguments().getLong("familyId");

        name = getArguments().getString("name");
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_user_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initView(rootView);

        getData();
    }

    private void initView(View rootView) {
        mBack = rootView.findViewById(R.id.tv_back);
        mBack.setOnClickListener(v -> getSupportDelegate().pop());

        mTitle = rootView.findViewById(R.id.tv_title);
        mTitle.setText(R.string.user_detail_title);

        rootView.findViewById(R.id.tv_save).setVisibility(View.GONE);

        robotImageView = rootView.findViewById(R.id.user_detail_image);

        mName = rootView.findViewById(R.id.user_detail_name);
        mName.setText(name);

        mNickName = rootView.findViewById(R.id.user_detail_nickname);

        mRelationKey = rootView.findViewById(R.id.user_detail_relation_key);
        if (familyId == 0) {
            mRelationKey.setText(getString(R.string.user_detail_relation, getString(R.string.user_detail_own)));
        } else {
            mRelationKey.setText(getString(R.string.user_detail_relation, getArguments().getString("familyName")));
        }

        mRelationValue = rootView.findViewById(R.id.user_detail_relation_value);

        mPermissionValue = rootView.findViewById(R.id.user_detail_permission_value);

        mSendMsg = rootView.findViewById(R.id.user_detail_send_msg);
        mSendMsg.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View v) {
                if (tencentImUserId == null) {
                    Toast.makeText(getContext(), R.string.get_data_error, Toast.LENGTH_LONG).show();
                    return;
                }
                Bundle mArgs = new Bundle();
                mArgs.putString(Constants.INTENT_DATA, tencentImUserId + "");
                mArgs.putString(Constants.INTENT_NAME, name);
                PersonalChatFragment2 personalChatFragment2 = new PersonalChatFragment2();
                personalChatFragment2.setArguments(mArgs);
                getSupportDelegate().start(personalChatFragment2);
            }
        });

        mSendCall = rootView.findViewById(R.id.user_detail_send_call);
        mSendCall.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{
                                Manifest.permission.RECORD_AUDIO}, 11);
                        return;
                    }
                }
                if (shNumber != null) {
                    startCall(shNumber, name, url);
                } else {
                    Toast.makeText(getContext(), R.string.get_data_error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setValue(JSONObject object) {
        url = object.getString("userHead");
        GlideUtils.load(getContext(), url, robotImageView.userImageView(), GlideUtils.USERMODE);
        name = object.getString("nickname");
        mName.setText(name);

        mRelationValue.setText(object.getString("relationTypeName"));

        mPermissionValue.setText(permissionConvert(object.getInteger("permissionType")));

    }

    private int permissionConvert(int type) {
        switch (type) {
            case 1:
                return R.string.user_detail_own;
            case 2:
                return R.string.user_detail_creator;
            case 3:
                return R.string.user_detail_protector;
            default:
                return R.string.user_detail_no_permission;
        }
    }

    private void getData() {
        JDialogUtil.INSTANCE.showRxDialogShapeLoading(getContext());
        String url = "friend/query_friend_info";
        RxRestClient.builder()
                .url(url)
                .params("yjtk", YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk())
                .params("familyId", familyId)
                .params("friendUserId", userId)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            JSONObject object = JSON.parseObject(response).getJSONObject("data");
                            if (null != object) {
                                tencentImUserId = object.getString("tencentImUserId");
                                shNumber = object.getString("shNumber");
                                setValue(object);
                            }
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }

                        JDialogUtil.INSTANCE.dismiss();
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        JDialogUtil.INSTANCE.dismiss();
                    }
                });
    }

    private void startCall(String phone, String name, String url) {
        Intent intent = new Intent(getContext(), SHCallingActivity.class);
        intent.putExtra(ExtraString.ISINCOME, false);
        intent.putExtra(ExtraString.PHONE_NUM, phone);
        intent.putExtra(ExtraString.PHONE_NAME, name);
        intent.putExtra(ExtraString.PHONE_URL, url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (shNumber != null) {
                startCall(shNumber, name, url);
            } else {
                Toast.makeText(getContext(), R.string.get_data_error, Toast.LENGTH_LONG).show();
            }
        }
    }


}
