package com.yijia.common_yijia.main.message;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.refresh.PagingBean;
import com.example.latte.ui.refresh.RefreshHandler;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.util.log.LatteLogger;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.index.IIndexCanReadItemListener;
import com.yijia.common_yijia.main.index.IIndexItemListener;
import com.yijia.common_yijia.main.index.YjIndexAdapter;
import com.yijia.common_yijia.main.index.YjIndexDataConverter;
import com.yijia.common_yijia.main.message.view.fragment.NoticeDelegate;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NoticeFreshHandler extends RefreshHandler {

    public NoticeFreshHandler(SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, DataConverter converter, PagingBean bean, LatteDelegate delegate, IIndexItemListener listener) {
        super(swipeRefreshLayout, recyclerView, converter, bean);
        DELEGATE=delegate;
        LISTENER=listener;
    }
//    private final SwipeRefreshLayout REFRESH_LAYOUT;
//    private final PagingBean BEAN;
//    private final RecyclerView RECYCLERVIEW;
    private NoticesAdapter mAdapter = null;
//    private final DataConverter CONVERTER;
      private final LatteDelegate DELEGATE;
      private final IIndexItemListener LISTENER;

    public static NoticeFreshHandler create(SwipeRefreshLayout swipeRefreshLayout,
                                            RecyclerView recyclerView, DataConverter converter, LatteDelegate delegate, IIndexItemListener listener) {

        return new NoticeFreshHandler(swipeRefreshLayout, recyclerView, converter, new PagingBean(),delegate,listener);
    }

    private void refresh() {
        REFRESH_LAYOUT.setRefreshing(true);
        firstPage();
//        Latte.getHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //进行一些网络请求
//                REFRESH_LAYOUT.setRefreshing(false);
//            }
//        }, 2000);
    }
    public void firstPage() {
        String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        RxRestClient.builder()
                .url("/notification/query_list")
                .params("yjtk", token)
                .params("pageNo", 1)
                .params("pageSize", 200)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(Latte.getApplicationContext()) {
                    @Override
                    public void onResponse(String response) {
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            final JSONObject jsondata = JSON.parseObject(response).getJSONObject("data");
                            final int totalCount = jsondata.getInteger("totalCount");
                            BEAN.setTotal(totalCount)
                                    .setPageSize(20).setPageIndex(1);
                           final List<MultipleItemEntity> data =
                                    new NoticeDataConverter()
                                            .setJsonData(response)
                                            .convert();
                            mAdapter = new NoticesAdapter(data, DELEGATE);
                            final LinearLayoutManager manager = new LinearLayoutManager(Latte.getApplicationContext());
                            RECYCLERVIEW.setLayoutManager(manager);
                            RECYCLERVIEW.setAdapter(mAdapter);
                            BEAN.addIndex();
                            REFRESH_LAYOUT.setRefreshing(false);
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(Latte.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            REFRESH_LAYOUT.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(Latte.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        REFRESH_LAYOUT.setRefreshing(false);
                    }
                });
    }

    private void paging() {
        final int pageSize = BEAN.getPageSize();
        final int currentCount = BEAN.getCurrentCount();
        final int total = BEAN.getTotal();
        final int index = BEAN.getPageIndex();
        if(index<=1){
            return;
        }

        if (mAdapter.getData().size() < pageSize || currentCount >= total) {
            mAdapter.loadMoreEnd(true);
        } else {
            String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
            RxRestClient.builder()
                    .url("/notification/query_list")
                    .params("yjtk", token)
                    .params("pageNo", 1)
                    .params("pageSize", 200)
                    .build()
                    .get()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<String>(Latte.getApplicationContext()) {
                        @Override
                        public void onResponse(String response) {
                            LatteLogger.json("query_timeline", response);
                            final JSONObject object = JSON.parseObject(response);
                            final String status = object.getString("status");
                            if (TextUtils.equals(status, "1001")) {
                                if(null==object.getInteger("totalCount")){
                                    return;
                                }
                                final int totalCount = object.getInteger("totalCount");
                                BEAN.setTotal(totalCount)
                                        .setPageSize(200);
                                final List<MultipleItemEntity> data =
                                        new NoticeDataConverter()
                                                .setJsonData(response)
                                                .convert();
                                mAdapter.addData(data);
                                mAdapter.loadMoreComplete();
                                BEAN.addIndex();
                                REFRESH_LAYOUT.setRefreshing(false);
                            } else {
                                final String msg = JSON.parseObject(response).getString("msg");
                                Toast.makeText(Latte.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                REFRESH_LAYOUT.setRefreshing(false);
                            }

                        }

                        @Override
                        public void onFail(Throwable e) {
                            Toast.makeText(Latte.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            REFRESH_LAYOUT.setRefreshing(false);
                        }
                    });
        }
    }

    @Override
    public void onRefresh() {
        refresh();
    }


    @Override
    public void onLoadMoreRequested() {
        paging();
    }
}
