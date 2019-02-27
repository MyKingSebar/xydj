package com.yijia.common_yijia.main.index.friendcircle.choosefriend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import com.example.latte.ui.R;
import com.example.latte.ui.contactlist.ConfigUtils;
import com.example.latte.ui.contactlist.cn.CNPinyin;
import com.example.latte.ui.contactlist.cn.CNPinyinIndex;
import com.example.latte.ui.contactlist.cn.CNPinyinIndexFactory;
import com.example.latte.ui.contactlist.search.TextViewChangedOnSubscribe;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by you on 2017/9/12.
 */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    /**
     * activity中的根目录
     */
    private View ll_root;
    /**
     * 窗体控件上一次的高度,用于监听键盘弹起
     */
    private int mLastHeight;

    private EditText et_search;

    private RecyclerView rv_main;
    private SearchAdapter adapter;

    private ArrayList<CNPinyin<ChooseFriendData>> ChooseFriendDataList;
    private Subscription subscription;

    public static void lanuch(Context context, ArrayList<CNPinyin<ChooseFriendData>> ChooseFriendDataList) {
        if (ChooseFriendDataList == null) {return;}
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("ChooseFriendDataList", ChooseFriendDataList);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ChooseFriendDataList = (ArrayList<CNPinyin<ChooseFriendData>>) getIntent().getSerializableExtra("ChooseFriendDataList");

        ll_root = findViewById(R.id.ll_root);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();

        rv_main = (RecyclerView) findViewById(R.id.rv_main);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_main.setLayoutManager(manager);
        adapter = new SearchAdapter(this,new ArrayList<CNPinyinIndex<ChooseFriendData>>());
        rv_main.setAdapter(adapter);

        final View decorView = this.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        decorView.getWindowVisibleDisplayFrame(r);
                        if (mLastHeight != r.bottom) {
                            mLastHeight = r.bottom;
                            ViewGroup.LayoutParams params = ll_root.getLayoutParams();
                            params.height = r.bottom - ll_root.getTop()/*  - statusHeight*/;
                            ll_root.setLayoutParams(params);
                        }
                    }
                });
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View actionView = getLayoutInflater().inflate(R.layout.actionbar_search, null);
        actionView.findViewById(R.id.iv_return).setOnClickListener(this);
        et_search = (EditText) actionView.findViewById(R.id.et_search);
        ActionBar.LayoutParams actionBarParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        bar.setCustomView(actionView, actionBarParams);
        ConfigUtils.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary));

        /**
         * 下面是搜索框智能
         */
        TextViewChangedOnSubscribe plateSubscribe = new TextViewChangedOnSubscribe();
        plateSubscribe.addTextViewWatcher(et_search);
        subscription = Observable.create(plateSubscribe).debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .switchMap(new Func1<String, Observable<ArrayList<CNPinyinIndex<ChooseFriendData>>>>() {
                    @Override
                    public Observable<ArrayList<CNPinyinIndex<ChooseFriendData>>> call(final String s) {
                        return createObservable(s).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
                    }
                }).subscribe(new Subscriber<ArrayList<CNPinyinIndex<ChooseFriendData>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArrayList<CNPinyinIndex<ChooseFriendData>> cnPinyinIndices) {
                        adapter.setNewDatas(cnPinyinIndices);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_return) {
            finish();

        }
    }

    @Override
    protected void onDestroy() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    /**
     * 搜索订阅
     *
     * @return
     */
    private Observable<ArrayList<CNPinyinIndex<ChooseFriendData>>> createObservable(final String keywork) {
        return Observable.create(new Observable.OnSubscribe<ArrayList<CNPinyinIndex<ChooseFriendData>>>() {
            @Override
            public void call(Subscriber<? super ArrayList<CNPinyinIndex<ChooseFriendData>>> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(CNPinyinIndexFactory.indexList(ChooseFriendDataList, keywork));
                }
            }
        });
    }

}
