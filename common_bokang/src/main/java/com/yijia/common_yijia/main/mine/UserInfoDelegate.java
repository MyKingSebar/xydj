package com.yijia.common_yijia.main.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.util.GlideUtils;
import com.google.gson.Gson;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.index.YjIndexDelegate;
import com.yijia.common_yijia.main.index.friendcircle.LetterPeopleAdapter;
import com.yijia.common_yijia.main.index.friendcircle.LetterPeopleDelagate;
import com.yijia.common_yijia.main.message.NoticeDataConverter;
import com.yijia.common_yijia.main.message.NoticesAdapter;
import com.yijia.common_yijia.main.message.view.fragment.NoticeDelegate;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UserInfoDelegate extends LatteDelegate {
    @BindView(R2.id.tv_title)
    AppCompatTextView tv_title;
    @BindView(R2.id.tv_save)
    AppCompatTextView tv_save;
    @BindView(R2.id.tv_name)
    AppCompatTextView tv_name;
    @BindView(R2.id.civ)
    CircleImageView civ;
    @BindView(R2.id.rv)
    RecyclerView rv;

    UserRecentBehaviorAdapter adapter = null;

    String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
    long id = 0;

    @OnClick(R2.id.tv_back)
    void back() {
        getSupportDelegate().pop();
    }

    @OnClick(R2.id.tv_add)
    void add() {
        //TODO
        addfriendreq(id, token);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        id = args.getLong("id");
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_user_info;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        tv_title.setText("详情资料");
        tv_save.setVisibility(View.INVISIBLE);

        getInfo();
        getRecentInfo();
    }

    private void getInfo() {
//        String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
//        long id = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getId();
        RxRestClient.builder()
                .url("/user/query_user_info")
                .params("yjtk", token)
                .params("type", 1)
                .params("keyword", id + "")
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final JSONObject obj = JSON.parseObject(response);
                        final String status = obj.getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            JSONObject data = obj.getJSONObject("data");
                            JSONObject user = data.getJSONObject("user");
                            String nickname = user.getString("nickname");
                            String imagePath = user.getString("imagePath");
                            long id = user.getLong("id");
                            String phone = user.getString("phone");
                            String email = user.getString("email");

                            //用户状态：1-正常，2-注销
                            int userStatus = user.getInteger("userStatus");
                            String tencentImUserId = user.getString("tencentImUserId");
                            String tencentImUserSig = user.getString("tencentImUserSig");
                            String inviteCode = user.getString("inviteCode");

                            if (nickname != null) {
                                tv_name.setText(nickname);
                            }
                            if (imagePath != null) {
                                GlideUtils.load(getContext(), imagePath, civ, GlideUtils.USERMODE);
                            }

                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getRecentInfo() {
//        String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
//        long id = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getId();
        RxRestClient.builder()
                .url("/ope/query_operation_record")
                .params("yjtk", token)
                .params("userId", id)
                .params("pageNo", 1)
                .params("pageSize", 3)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final JSONObject obj = JSON.parseObject(response);
                        final String status = obj.getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            Log.e("jialei", "getRecentInfo:" + new Gson().toJson(response));
                            final List<MultipleItemEntity> data =
                                    new UserRecentBehaviorConverter()
                                            .setJsonData(response)
                                            .convert();
                            adapter = new UserRecentBehaviorAdapter(data, UserInfoDelegate.this);
                            final LinearLayoutManager manager = new LinearLayoutManager(getContext());
                            rv.setLayoutManager(manager);
                            rv.setAdapter(adapter);

                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void addfriendreq(long targetUserId, String token) {
        String url = "/friend/insert_friend_apply";
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("targetUserId", targetUserId)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        final JSONObject object = JSON.parseObject(response);
                        final String status = object.getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            showToast("发送成功");
                        } else {
                            showToast(object.getString("msg"));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showToast(throwable.getMessage());
                    }
                });
    }

}
