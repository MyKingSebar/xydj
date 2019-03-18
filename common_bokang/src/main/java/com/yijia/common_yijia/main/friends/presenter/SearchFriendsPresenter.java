package com.yijia.common_yijia.main.friends.presenter;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.yijia.net.rx.RxRestClient;
import com.yijia.common_yijia.main.friends.base.BasePresenter;
import com.yijia.common_yijia.main.friends.view.iview.SearchFriendsView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchFriendsPresenter extends BasePresenter<SearchFriendsView> {

    public SearchFriendsPresenter(SearchFriendsView view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    public void reqfriendsdetails(String friendsPhone, String token) {
        String url = "/user/query_user_info";
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("type", "2")
                .params("keyword",friendsPhone)
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
                            final JSONObject data = object.getJSONObject("data");
                            if (data.size()==0){
                                getView().respNoFriend();
                            }else{
                                final JSONObject user = data.getJSONObject("user");
                                getView().respfriendsdetailsSuccess(user);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().respfriendsdetailsError(throwable.getMessage());
                    }
                });
    }
    @SuppressLint("CheckResult")
    public void reqaddfriendsdetails(int targetUserId, String token) {
        String url = "/friend/insert_friend_apply";
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("targetUserId",targetUserId)
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
                                getView().respaddfriendsdetailsSuccess();
                        }else{
                            getView().respaddfriendsdetailsError( object.getString("msg"));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().respaddfriendsdetailsError(throwable.getMessage());
                    }
                });
    }




}
