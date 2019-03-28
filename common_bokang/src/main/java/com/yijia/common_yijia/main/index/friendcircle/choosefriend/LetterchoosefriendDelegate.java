package com.yijia.common_yijia.main.index.friendcircle.choosefriend;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.contactlist.cn.CNPinyin;
import com.example.latte.ui.contactlist.cn.CNPinyinFactory;
import com.example.latte.ui.contactlist.search.CharIndexView;
import com.example.latte.ui.contactlist.stickyheader.StickyHeaderDecoration;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.util.callback.CallbackManager;
import com.example.yijia.util.callback.CallbackType;
import com.example.yijia.util.callback.IGlobalCallback;
import com.example.yijia.util.log.LatteLogger;
import com.google.gson.Gson;
import com.yijia.common_yijia.database.YjDatabaseManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public class LetterchoosefriendDelegate extends LatteDelegate implements ChooseFriendItemLisener {
    public enum choosefriendType{
        LETTERCHOOSEFRIEND,
        GUARDIANSHIPCHOOSEFRIEND
    }
     String choosefriendTypeString=null;
    public static final String CHOOSEFRIENDTYPEKEY="choosefriendType";
    @BindView(R2.id.tv_save)
    AppCompatTextView tvSave;
    @BindView(R2.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R2.id.tv_index)
    AppCompatTextView tvIndex;
    @BindView(R2.id.iv_main)
    CharIndexView iv_main;
    @BindView(R2.id.rv_main)
    RecyclerView rv_main;

    private ArrayList<CNPinyin<ChooseFriendData>> contactList = new ArrayList<>();
    private Subscription subscription;
    private ContactAdapter adapter;


    @OnClick(R2.id.tv_back)
    void back() {
//        Bundle bundle = new Bundle();
//        bundle.putLong("friendId", 1);
//        bundle.putString("friendName", "1111111111");
//        getSupportDelegate().setFragmentResult(RESULT_OK, bundle);
        getSupportDelegate().pop();
//        getSupportDelegate().pop();
    }

    @OnClick(R2.id.rl_search)
    void bt_search() {
        SearchActivity.lanuch(getContext(), contactList);
    }


    private final static String TAG = LetterchoosefriendDelegate.class.getSimpleName();


    @Override
    public Object setLayout() {
        return R.layout.delegate_index_edit_letter_choosefriend;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        init();
    }


    private void init() {
        tvTitle.setText("选择好友");
        tvSave.setVisibility(View.GONE);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rv_main.setLayoutManager(manager);

        iv_main.setOnCharIndexChangedListener(new CharIndexView.OnCharIndexChangedListener() {
            @Override
            public void onCharIndexChanged(char currentIndex) {
                for (int i = 0; i < contactList.size(); i++) {
                    if (contactList.get(i).getFirstChar() == currentIndex) {
                        manager.scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }

            @Override
            public void onCharIndexSelected(String currentIndex) {
                if (currentIndex == null) {
                    tvIndex.setVisibility(View.INVISIBLE);
                } else {
                    tvIndex.setVisibility(View.VISIBLE);
                    tvIndex.setText(currentIndex);
                }
            }
        });


        adapter = new ContactAdapter(getContext(), contactList, this);
        rv_main.setAdapter(adapter);
        rv_main.addItemDecoration(new StickyHeaderDecoration(adapter));
        initFriendsRecyclerView();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        choosefriendTypeString = args.getString(LetterchoosefriendDelegate.CHOOSEFRIENDTYPEKEY);
    }

    private void initFriendsRecyclerView() {
        LatteLogger.e("initFriendsRecyclerView", "aaaa");
        final String url = "friend/query_friends";
        String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("pageNo", 1)
                .params("pageSize", 500)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.e("initFriendsRecyclerView",(String)new Gson().toJson(response));
                        LatteLogger.json("USER_FRIENDS", response);
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            getFriendsSucceed(response);
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), "请稍后尝试", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getFriendsSucceed(String response) {
        final ArrayList<ChooseFriendData> data =
                new IndexChooseFriendsDataConverter(response).convert();
        getPinyinList(data);
//        adapter = new ContactAdapter(data);
//        friendsAdapter.setCartItemListener(this);
//        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        //调整RecyclerView的排列方向
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mFriendsRecyclerView.setLayoutManager(manager);
//        mFriendsRecyclerView.setAdapter(friendsAdapter);
    }

    private void getPinyinList(ArrayList<ChooseFriendData> data) {
        subscription = Observable.create(new Observable.OnSubscribe<List<CNPinyin<ChooseFriendData>>>() {
            @Override
            public void call(Subscriber<? super List<CNPinyin<ChooseFriendData>>> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    List<CNPinyin<ChooseFriendData>> contactList = CNPinyinFactory.createCNPinyinList(data);
                    Collections.sort(contactList);
                    subscriber.onNext(contactList);
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CNPinyin<ChooseFriendData>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<CNPinyin<ChooseFriendData>> cnPinyins) {
                        contactList.addAll(cnPinyins);
                        adapter.notifyDataSetChanged();
                    }
                });
    }


    @Override
    public void onDestroyView() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        super.onDestroyView();

    }

    @Override
    public void onItemClick(ChooseFriendData contact) {
        Bundle bundle = new Bundle();
        bundle.putLong("friendId", contact.getFriendUserId());
        bundle.putString("friendName", contact.getNickname());
        getSupportDelegate().setFragmentResult(4, bundle);
        final IGlobalCallback<String> callback;
        if(TextUtils.equals(choosefriendTypeString,choosefriendType.LETTERCHOOSEFRIEND.name())){
            callback= CallbackManager
                    .getInstance()
                    .getCallback(CallbackType.FRAGMENT_LETTER_CHOOSEFRIEND_RESULT);
        }else if(TextUtils.equals(choosefriendTypeString,choosefriendType.GUARDIANSHIPCHOOSEFRIEND.name())){
            callback= CallbackManager
                    .getInstance()
                    .getCallback(CallbackType.FRAGMENT_GUARDIANSHIP_CHOOSEFRIEND_RESULT);
        }else {
            callback=null;
            return;
        }

        if (callback != null) {
            callback.executeCallback(contact.getNickname() + "," + contact.getFriendUserId());
        }
        getSupportDelegate().pop();
    }


}
