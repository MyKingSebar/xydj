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
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.refresh.PagingBean;
import com.example.latte.ui.refresh.RefreshHandler;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.dialog.JDialogUtil;
import com.example.yijia.util.log.LatteLogger;
import com.google.gson.Gson;
import com.yijia.common_yijia.main.friends.CommonClickListener;
import com.yijia.common_yijia.main.index.YjIndexMultipleFields;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RobotGuardianshipReFreshHandler extends RefreshHandler {

    public RobotGuardianshipReFreshHandler(SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, DataConverter converter, PagingBean bean, LatteDelegate delegate,CommonClickListener mCommonClickListener,String yjyk ) {
        super(swipeRefreshLayout, recyclerView, converter, bean);
        DELEGATE=delegate;
        this.mCommonClickListener=mCommonClickListener;
        this.token=yjyk;
    }
    private RobotGuardianshipAdapter mAdapter = null;
      private final LatteDelegate DELEGATE;
      private final CommonClickListener mCommonClickListener;
      private final String token;
      private final int TYPE=2;
      private final int PAGESIZE=20;


    public static RobotGuardianshipReFreshHandler create(SwipeRefreshLayout swipeRefreshLayout,
                                                         RecyclerView recyclerView, DataConverter converter, LatteDelegate delegate,CommonClickListener mCommonClickListener,String yjyk) {
        return new RobotGuardianshipReFreshHandler(swipeRefreshLayout, recyclerView, converter, new PagingBean(),delegate,mCommonClickListener,yjyk);
    }

    private void refresh() {
        REFRESH_LAYOUT.setRefreshing(true);
        firstPage();
    }
    public void firstPage() {
        String url = "guardianship/query_guardianship/"+TYPE+"/"+1+"/"+PAGESIZE;
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
//                .params("queryType", 2)
//                .params("pageNo", 1)
//                .params("pageSize", 20)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(Latte.getApplicationContext()) {
                    @Override
                    public void onResponse(String response) {
                        final JSONObject object = JSON.parseObject(response);
                        final String status =object.getString("status");
                        Log.e("jialei","query_guardianship"+new Gson().toJson(response));
                        if (TextUtils.equals(status, "1001")) {
                            final JSONObject jsondata = object.getJSONObject("data");
                            final int totalCount = jsondata.getInteger("totalCount");
                            BEAN.setTotal(totalCount)
                                    .setPageSize(20).setPageIndex(1);
                            List<MultipleItemEntity> data  = new RobotGuardianshipDataConverter()
                                    .setJsonData(response)
                                    .convert();
                            mAdapter = new RobotGuardianshipAdapter(data);
                            mAdapter.setmRobotGuardianshipListener(mCommonClickListener);
                            mAdapter.setOnLoadMoreListener(RobotGuardianshipReFreshHandler.this, RECYCLERVIEW);
                            final LinearLayoutManager manager = new LinearLayoutManager(Latte.getApplicationContext());
                            RECYCLERVIEW.setLayoutManager(manager);
                            RECYCLERVIEW.setAdapter(mAdapter);
                            BEAN.addIndex();
                            REFRESH_LAYOUT.setRefreshing(false);
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(Latte.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            JDialogUtil.INSTANCE.dismiss();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(Latte.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        JDialogUtil.INSTANCE.dismiss();
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
            String url = "query_guardianship/"+TYPE+"/"+index+"/"+PAGESIZE;
            RxRestClient.builder()
                    .url(url)
                    .params("yjtk", token)
//                    .params("queryType", 2)
//                    .params("pageNo", index)
//                    .params("pageSize", 20)
                    .build()
                    .post()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<String>(Latte.getApplicationContext()) {
                        @Override
                        public void onResponse(String response) {

                            LatteLogger.json("query_timeline", response);
                            final JSONObject object = JSON.parseObject(response);
                            final String status = object.getString("status");
                            if (TextUtils.equals(status, "1001")) {
                                final JSONObject jsondata = object.getJSONObject("data");

                                if(null==jsondata.getInteger("totalCount")){
                                    return;
                                }
                                final int totalCount = jsondata.getInteger("totalCount");
                                BEAN.setTotal(totalCount)
                                        .setPageSize(pageSize);
                                final ArrayList<MultipleItemEntity> data =
                                        new RobotGuardianshipDataConverter()
                                                .setJsonData(response)
                                                .convert();
                                if(data.size()>0){

                                }else {
                                    mAdapter.loadMoreEnd(true);
                                    REFRESH_LAYOUT.setRefreshing(false);
                                    return;
                                }
                                mAdapter.addData(data);
                                int size=mAdapter.getData().size();
                                BEAN.setCurrentCount(mAdapter.getData().size());
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