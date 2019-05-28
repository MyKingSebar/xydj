package com.yijia.common_yijia.main.index.friendcircle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.yijia.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.yijia.common_yijia.database.YjDatabaseManager;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class LetterPeopleDelagate extends LatteDelegate {
    RecyclerView rv = null;
    LetterPeopleAdapter madapter = null;
    long circleId=0;

     ArrayList<MultipleItemEntity> data=null;

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_letter_friends;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        init(rootView);
    }


    private void init(View view) {
        RelativeLayout rl = view.findViewById(R.id.tv_back);
        rl.setOnClickListener(v -> getSupportDelegate().pop());
        AppCompatTextView tv = view.findViewById(R.id.tv_title);
        tv.setText("该家书的收信人");
        AppCompatTextView tv2 = view.findViewById(R.id.tv_save);
        tv2.setVisibility(View.INVISIBLE);
        rv = view.findViewById(R.id.rv_letter_friends);


        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(manager);
//        madapter = new LetterPeopleAdapter(data,LetterPeopleDelagate.this);
//        rv.setAdapter(madapter);
        String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        getInfo(token,circleId);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args=getArguments();
        if(args==null){
            return;
        }
        circleId=args.getLong("circleId");
    }


    private void getInfo(String token, long circleId) {
        RxRestClient.builder()
                .url("circle/query_visible_or_invisible_users")
                .params("yjtk", token)
                .params("circleId", circleId)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            data =
                                    new LetterPeopleDataConverter()
                                            .setJsonData(response)
                                            .convert();
                            madapter = new LetterPeopleAdapter(data,LetterPeopleDelagate.this);
                            rv.setAdapter(madapter);
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

}
