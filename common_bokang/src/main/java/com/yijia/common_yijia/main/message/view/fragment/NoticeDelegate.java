package com.yijia.common_yijia.main.message.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.yijia.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.index.IIndexItemListener;
import com.yijia.common_yijia.main.index.YjReFreshHandler;
import com.yijia.common_yijia.main.index.friendcircle.LetterPeopleAdapter;
import com.yijia.common_yijia.main.index.friendcircle.LetterPeopleDataConverter;
import com.yijia.common_yijia.main.index.friendcircle.LetterPeopleDelagate;
import com.yijia.common_yijia.main.message.NoticeDataConverter;
import com.yijia.common_yijia.main.message.NoticeFreshHandler;
import com.yijia.common_yijia.main.message.NoticesAdapter;
import com.yijia.common_yijia.main.message.OkAddLisener;
import com.yijia.common_yijia.main.message.presenter.NoticePresenter;
import com.yijia.common_yijia.main.message.view.iview.NoticeView;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 通知页面
 */
public class NoticeDelegate extends LatteDelegate implements NoticeView, IIndexItemListener, OkAddLisener {
    //    @BindView(R2.id.notice_recycler)
//    RecyclerView noticeRecycler;
    private NoticePresenter notice_presenter;
    List<MultipleItemEntity> data = null;
    NoticesAdapter adapter = null;
    @BindView(R2.id.notice_recycler)
    RecyclerView rv = null;
    @BindView(R2.id.srl_index)
    SwipeRefreshLayout mRefreshLayout = null;
    NoticeFreshHandler mRefreshHandler = null;
    boolean isFirst = true;

    @Override
    public Object setLayout() {
        return R.layout.delegate_notice;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initRecycler();

        mRefreshHandler = NoticeFreshHandler.create(mRefreshLayout, rv, null, this, this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRefreshLayout();
        mRefreshHandler.firstPage();
        isFirst = false;
    }

    private void initRecycler() {
        notice_presenter = new NoticePresenter(this);
        notice_presenter.reqNoticeData();
    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light

        );
        //第一个参数true：下拉的时候球由小变大，回弹时由大变小 第二个参数下降起始高度  第三个参数下降终止的高度
        mRefreshLayout.setProgressViewOffset(true, 120, 300);
    }

    private void addfriend(long friendApplyId) {
        String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        RxRestClient.builder()
                .url("/friend/dispose_friend_apply")
                .params("yjtk", token)
                .params("friendApplyId", friendApplyId)
                .params("isAgree", 1)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            showToast("添加成功");
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            showToast(msg);
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        showToast(e.getMessage());
                    }
                });
    }

    private void getMessage(String token, int pageNo, int pageSize) {
        RxRestClient.builder()
                .url("application/x-www-form-urlencoded")
                .params("yjtk", token)
                .params("pageNo", pageNo)
                .params("pageSize", pageSize)
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
                                    new NoticeDataConverter()
                                            .setJsonData(response)
                                            .convert();
                            adapter = new NoticesAdapter(data, NoticeDelegate.this);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        notice_presenter.onDestroy();
    }

    @Override
    public void onIndexItemClick(double itemTotalPrice) {
        if (!isFirst) {
            Log.d("refresh", "onIndexItemClick");
            mRefreshHandler.firstPage();
        }
    }

    @Override
    public void ok(long id) {
        addfriend(id);
    }

    @Override
    public void error() {

    }
}
