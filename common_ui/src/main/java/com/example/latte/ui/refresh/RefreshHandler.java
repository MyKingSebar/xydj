package com.example.latte.ui.refresh;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.yijia.app.Latte;
import com.example.yijia.net.RestClient;
import com.example.yijia.net.callback.ISuccess;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleRecyclerAdapter;
import com.example.yijia.util.log.LatteLogger;


public class RefreshHandler implements
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {

    public final SwipeRefreshLayout REFRESH_LAYOUT;
    public final PagingBean BEAN;
    public final RecyclerView RECYCLERVIEW;
    public MultipleRecyclerAdapter mAdapter = null;
    public final DataConverter CONVERTER;

    public RefreshHandler(SwipeRefreshLayout swipeRefreshLayout,
                          RecyclerView recyclerView,
                          DataConverter converter, PagingBean bean) {
        this.REFRESH_LAYOUT = swipeRefreshLayout;
        this.RECYCLERVIEW = recyclerView;
        this.CONVERTER = converter;
        this.BEAN = bean;
        REFRESH_LAYOUT.setOnRefreshListener(this);
    }

    public static RefreshHandler create(SwipeRefreshLayout swipeRefreshLayout,
                                        RecyclerView recyclerView, DataConverter converter) {
        return new RefreshHandler(swipeRefreshLayout, recyclerView, converter, new PagingBean());
    }



    @Override
    public void onRefresh() {
    }


    @Override
    public void onLoadMoreRequested() {

    }
}
