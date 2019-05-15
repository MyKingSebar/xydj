package com.yijia.common_yijia.main.robot.robotmain;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.refresh.PagingBean;
import com.example.latte.ui.refresh.RefreshHandler;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.dialog.JDialogUtil;
import com.example.yijia.util.log.LatteLogger;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.friends.CommonStringClickListener;
import com.yijia.common_yijia.main.index.YjIndexItemType;
import com.yijia.common_yijia.main.index.YjRobotListMultipleFields;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class RobotMainListReFreshHandler extends RefreshHandler {

    public RobotMainListReFreshHandler(SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, DataConverter converter, PagingBean bean, LatteDelegate delegate, CommonStringClickListener mCommonClickListener, String yjyk) {
        super(swipeRefreshLayout, recyclerView, converter, bean);
        DELEGATE = delegate;
        this.mCommonClickListener = mCommonClickListener;
        this.token = yjyk;
    }

    private RobotListAdapter mAdapter = null;
    private final LatteDelegate DELEGATE;
    private final CommonStringClickListener mCommonClickListener;
    private final String token;


    public static RobotMainListReFreshHandler create(SwipeRefreshLayout swipeRefreshLayout,
                                                     RecyclerView recyclerView, DataConverter converter, LatteDelegate delegate, CommonStringClickListener mCommonClickListener, String yjyk) {
        return new RobotMainListReFreshHandler(swipeRefreshLayout, recyclerView, converter, new PagingBean(), delegate, mCommonClickListener, yjyk);
    }

    private void refresh() {
        REFRESH_LAYOUT.setRefreshing(true);
        firstPage();
    }

    public void firstPage() {
        Observable ob1 = RxRestClient.builder()
                .url("robot/query_robot_is_online")
                .params("yjtk", token)
                .params("targetUserId", YjDatabaseManager.getInstance().getDao().loadAll().get(0).getId())
                .build()
                .get()
                .subscribeOn(Schedulers.io());

        Observable ob2 = RxRestClient.builder()
                .url("family/query_family")
                .params("yjtk", token)
                .build()
                .get()
                .subscribeOn(Schedulers.io());

        Observable.zip(ob1, ob2, new BiFunction<String, String, Map>() {
            @Override
            public Map apply(String s, String s2) throws Exception {
                Map map = new HashMap();
                final JSONObject object = JSON.parseObject(s);
                final String status = object.getString("status");
                boolean isOnline = false;
                if (TextUtils.equals(status, "1001")) {
                    JSONObject jo = object.getJSONObject("data");
                    isOnline = jo.getBoolean("isOnline");
                }
                map.put("isOnLine", isOnline);
                map.put("json", s2);
                Log.e("~~get families", s + "--" + s2);
                return map;
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Map>(Latte.getApplicationContext()) {
                    @Override
                    public void onResponse(Map r) {
                        String response = (String)r.get("json");
                        boolean isOnline = (Boolean) r.get("isOnLine");
                        final JSONObject object = JSON.parseObject(response);
                        final String status =object.getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            List<MultipleItemEntity> data  = new RobotListConverter()
                                    .setJsonData(response)
                                    .convert();
                            //增加自己
                            final MultipleItemEntity entity = MultipleItemEntity.builder()
                                    .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.ROBOT_MAIN_LIST)
                                    .setField(MultipleFields.ID, 0)
                                    .setField(MultipleFields.NAME, "")
                                    .setField(YjRobotListMultipleFields.MAINID,  YjDatabaseManager.getInstance().getDao().loadAll().get(0).getId())
                                    .setField(YjRobotListMultipleFields.MAINNAME,  YjDatabaseManager.getInstance().getDao().loadAll().get(0).getNickname())
                                    .setField(YjRobotListMultipleFields.RELATIONSHIP, "本人")
                                    .setField(YjRobotListMultipleFields.ONLINE,  isOnline ? 1 :2)
                                    .setField(MultipleFields.IMAGE_URL,  YjDatabaseManager.getInstance().getDao().loadAll().get(0).getImagePath())
                                    .build();
                            data.add(0, entity);
                            mAdapter = new RobotListAdapter(data);
                            mAdapter.setmRobotListClickListener(mCommonClickListener);
                            mAdapter.setOnLoadMoreListener(RobotMainListReFreshHandler.this, RECYCLERVIEW);
                            final LinearLayoutManager manager = new LinearLayoutManager(Latte.getApplicationContext());
                            RECYCLERVIEW.setLayoutManager(manager);
                            RECYCLERVIEW.setAdapter(mAdapter);
                            BEAN.addIndex();
                            REFRESH_LAYOUT.setRefreshing(false);
                            mAdapter.loadMoreEnd(true);
                            JDialogUtil.INSTANCE.dismiss();

                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(Latte.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            REFRESH_LAYOUT.setRefreshing(false);
                            mAdapter.loadMoreEnd(true);
                            JDialogUtil.INSTANCE.dismiss();

                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(Latte.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        mAdapter.loadMoreEnd(true);
                        REFRESH_LAYOUT.setRefreshing(false);
                        JDialogUtil.INSTANCE.dismiss();

                    }
                });


    }

    @Override
    public void onRefresh() {
        refresh();
    }


    @Override
    public void onLoadMoreRequested() {

    }
}
