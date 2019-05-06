package com.yijia.common_yijia.main.friends.presenter;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.yijia.app.AccountManager;
import com.example.yijia.app.IUserChecker;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.util.log.LatteLogger;
import com.google.gson.Gson;
import com.yijia.common_yijia.main.friends.base.BasePresenter;
import com.yijia.common_yijia.main.friends.view.iview.FriendsView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FriendsPresenter extends BasePresenter<FriendsView> {

    public FriendsPresenter(FriendsView view) {
        super(view);
    }

    public void reqFriendData(String token) {
        AccountManager.checkAccont(new IUserChecker() {
            @SuppressLint("CheckResult")
            @Override
            public void onSignIn() {
                final String url = "friend/query_friends";

                RxRestClient.builder()
                        .url(url)
                        .params("yjtk", token)
                        .params("pageNo", "1")
                        .params("pageSize", "200")
                        .build()
                        .post()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String response) throws Exception {
                                LatteLogger.d("response:"+new Gson().toJson(response));
                                final JSONObject object = JSON.parseObject(response);
                                final String status = object.getString("status");
                                if (TextUtils.equals(status, "1001")) {
                                    final JSONObject data = object.getJSONObject("data");
                                    final JSONArray friends = data.getJSONArray("friends");
                                    getView().respFriendsSuccess(friends);
                                } else {
                                    getView().respFriendsError("请求好友错误");
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                getView().respFriendsError(throwable.getMessage().toString());
                            }
                        });
            }

            @Override
            public void onNoSignIn() {
                getView().respFriendsError("你还没有登录哦~");
            }
        });
    }

//    @SuppressLint("CheckResult")
//    public void reqGuardianData(String token) {
//        String url = "/query_guardianship/"+3;
//        RxRestClient.builder()
//                .url(url)
//                .params("yjtk", token)
////                .params("type", 3)
//                .build()
//                .get()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String response) throws Exception {
//                        final JSONObject object = JSON.parseObject(response);
//                        final String status = object.getString("status");
//                        if (TextUtils.equals(status, "1001")) {
//                            final JSONObject data = object.getJSONObject("data");
//                            final JSONArray guardianUserList = data.getJSONArray("guardianUserList");
//                            final JSONArray oldMapUserList = data.getJSONArray("oldMapUserList");
//                            getView().respGuardianSuccess(guardianUserList, oldMapUserList);
//                        } else {
//                            getView().respGuardianError("请求监护人错误");
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        getView().respGuardianError(throwable.getMessage());
//                    }
//                });
//    }
}
