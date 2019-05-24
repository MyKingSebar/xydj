package com.yijia.common_yijia.main.mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.dialog.JDialogUtil;
import com.google.gson.Gson;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.friends.CommonStringClickListener;
import com.yijia.common_yijia.main.mine.setup.IdentityAuthenticationDelegate;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserProfilesDelegate extends LatteDelegate implements CommonStringClickListener {
    @BindView(R2.id.tv_save)
    AppCompatTextView tvSave;
    @BindView(R2.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R2.id.tv_icon)
    ImageView tvIcon;
    @BindView(R2.id.rv)
    RecyclerView rv;

    UserProfilesAdapter mAdapter = null;
    ArrayList<MultipleItemEntity> data = null;
    String token = null;
    long friendId=0;

    @Override
    public Object setLayout() {
        return R.layout.delegate_userprofiles;
    }

    @OnClick(R2.id.tv_back)
    void back() {
        getSupportDelegate().pop();
    }

    @OnClick(R2.id.tv_icon)
    void add() {
        getSupportDelegate().start(new IdentityAuthenticationDelegate());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        initView();
//        initCallback();
        getUserProfilesInfo(token);
    }

//    private void initCallback() {
//        CallbackManager.getInstance()
//                .addCallback(CallbackType.FRAGMENT_GUARDIANSHIP_CHOOSEFRIEND_RESULT, new IGlobalCallback<String>() {
//                    @Override
//                    public void executeCallback(@Nullable String args) {
//                        assert args != null;
//                        String[] arg = args.split(",");
//                        if (arg.length == 2) {
//                            friendId = Long.parseLong(arg[1]);
//                            if(friendId!=0){
//                                JDialogUtil.INSTANCE.showRxDialogShapeLoading(getContext());
//                                saveGuardianshipInfo(token,friendId);
//                            }
//                        }
//                    }
//                });
//
//        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        rv.setLayoutManager(manager);
//
//
//    }

    private void initView() {
        tvTitle.setText("用户档案");
        tvIcon.setVisibility(View.VISIBLE);
        tvSave.setVisibility(View.GONE);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(manager);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        getUserProfilesInfo(token);
    }

    private void getUserProfilesInfo(String token) {
        String url = "user/query_id_card_info_list";
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final String status = JSON.parseObject(response).getString("status");
                        Log.e("jialei","query_guardianship"+new Gson().toJson(response));
                        if (TextUtils.equals(status, "1001")) {
                            data = new UserProfilesDataConverter()
                                    .setJsonData(response)
                                    .convert();
                            mAdapter = new UserProfilesAdapter(data, UserProfilesDelegate.this);
                            mAdapter.setmCommonClickListener(UserProfilesDelegate.this);
                            rv.setAdapter(mAdapter);
                            JDialogUtil.INSTANCE.dismiss();
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            JDialogUtil.INSTANCE.dismiss();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        JDialogUtil.INSTANCE.dismiss();
                    }
                });
    }


    @Override
    public void commonClick(String info) {
        IdentityAuthenticationDelegate mIdentityAuthenticationDelegate=new IdentityAuthenticationDelegate();
        Bundle bundle=new Bundle();
        bundle.putString("id",info);
        mIdentityAuthenticationDelegate.setArguments(bundle);
        getSupportDelegate().start(mIdentityAuthenticationDelegate);
    }
}
