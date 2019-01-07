package com.yijia.common_yijia.main.index;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.latte.delegates.bottom.BottomItemDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ec.main.index.IndexDataConverter;
import com.example.latte.ec.main.index.search.SearchDelegate;
import com.example.latte.net.rx.BaseObserver;
import com.example.latte.net.rx.RxRestClient;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.refresh.RefreshHandler;
import com.example.latte.util.callback.CallbackManager;
import com.example.latte.util.callback.CallbackType;
import com.example.latte.util.callback.IGlobalCallback;
import com.example.latte.util.log.LatteLogger;
import com.joanzapata.iconify.widget.IconTextView;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.index.friends.IFriendsItemListener;
import com.yijia.common_yijia.main.index.friends.IndexFriendsAdapter;
import com.yijia.common_yijia.main.index.friends.YjIndexFriendsDataConverter;
import com.yijia.common_yijia.main.index.pictureselector.PhotoFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class YjIndexDelegate extends BottomItemDelegate implements View.OnFocusChangeListener, IFriendsItemListener, IIndexItemListener {

    private final int ALLMODE = 0;
    private final int IMAGEMODE = 1;
    private final int VIDEOMODE = 2;
    private final int AUDIOMODE = 3;
    private Bundle mArgs = null;
    public static final String PICKTYPE="PICKTYPE";


    @BindView(R2.id.rv_index)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.rv_friends)
    RecyclerView mFriendsRecyclerView = null;
    @BindView(R2.id.srl_index)
    SwipeRefreshLayout mRefreshLayout = null;
    @BindView(R2.id.tb_index)
    Toolbar mToolbar = null;
    @BindView(R2.id.icon_index_scan)
    IconTextView mIconScan = null;
    @BindView(R2.id.icon_index_message)
    IconTextView mSend = null;



    private RefreshHandler mRefreshHandler = null;

    YjIndexAdapter mAdapter = null;
    IndexFriendsAdapter friendsAdapter = null;

    @OnClick(R2.id.icon_index_message)
    void onCLickpublish() {
        mSend.showContextMenu();
//        getSupportDelegate().start(new PhotoFragment());
    }


    @OnClick(R2.id.icon_index_scan)
    void onCLickScanOrCode() {
        startScanWithCheck(this.getParentDelegate());
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {


        mRefreshHandler = RefreshHandler.create(mRefreshLayout, mRecyclerView, new IndexDataConverter());
        CallbackManager.getInstance()
                .addCallback(CallbackType.ON_SCAN, new IGlobalCallback<String>() {
                    @Override
                    public void executeCallback(@Nullable String args) {
                        Toast.makeText(getContext(), args, Toast.LENGTH_LONG).show();
                    }
                });

        this.registerForContextMenu(mSend);
//        mSend.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs=new Bundle();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(1,IMAGEMODE,1,"图片");
        menu.add(1,VIDEOMODE,1,"视频");
        menu.add(1,AUDIOMODE,1,"音频");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        PhotoFragment delegate = new PhotoFragment();
        switch (item.getItemId()) {
            case IMAGEMODE:
                mArgs.putInt(PICKTYPE,IMAGEMODE);
                delegate.setArguments(mArgs);
                getParentDelegate().getSupportDelegate().start(delegate);
                break;
            case VIDEOMODE:
                mArgs.putInt(PICKTYPE,VIDEOMODE);
                delegate.setArguments(mArgs);
                getParentDelegate().getSupportDelegate().start(delegate);
                break;
            case AUDIOMODE:
                mArgs.putInt(PICKTYPE,AUDIOMODE);
                delegate.setArguments(mArgs);
                getParentDelegate().getSupportDelegate().start(delegate);
                break;

            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void getFriendsSucceed(String response) {
        final ArrayList<MultipleItemEntity> data =
                new YjIndexFriendsDataConverter()
                        .setJsonData(response)
                        .convert();
        friendsAdapter = new IndexFriendsAdapter(data);
        friendsAdapter.setCartItemListener(this);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        //调整RecyclerView的排列方向
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mFriendsRecyclerView.setLayoutManager(manager);
        mFriendsRecyclerView.setAdapter(friendsAdapter);
    }

    private void getIndexSucceed(String response) {
        final ArrayList<MultipleItemEntity> data =
                new YjIndexDataConverter()
                        .setJsonData(response)
                        .convert();
        mAdapter = new YjIndexAdapter(data,this);
        mAdapter.setIndexItemListener(this);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
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

    private void initRecyclerView() {
//        final GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
//        mRecyclerView.setLayoutManager(manager);
//        mRecyclerView.addItemDecoration
//                (BaseDecoration.create(ContextCompat.getColor(getContext(), R.color.app_background), 5));
//        final YjBottomDelegate yjBottomDelegate = getParentDelegate();
//        mRecyclerView.addOnItemTouchListener(IndexItemClickListener.create(yjBottomDelegate));
//
//        final ArrayList<MultipleItemEntity> data =
//                new YjIndexcommentDataConverter()
//                        .setJsonData(commentList.toJSONString())
//                        .convert();
//        mCommentAdapter = new YjIndexCommentAdapter(data);
//        final LinearLayoutManager manager = new LinearLayoutManager(mContext);
//        rvComment.setLayoutManager(manager);
//        rvComment.setAdapter(mCommentAdapter);
    }

    private void initFriendsRecyclerView() {
        final String url = "friend/query_friends";
        String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
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

    private void initIndexRecyclerView() {
        final String url = "circle/query_timeline";
        String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("queryType", 1)
                .params("yjtk", token)
                .params("pageNo", 1)
                .params("pageSize", 20)
                .params("beginCircleId", 0)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.json("query_timeline", response);
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            getIndexSucceed(response);
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

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRefreshLayout();
        initRecyclerView();
        initFriendsRecyclerView();
        initIndexRecyclerView();
        mRefreshHandler.firstPage("index.php");
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_index_yijia;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            getParentDelegate().getSupportDelegate().start(new SearchDelegate());
        }
    }

    @Override
    public void onFriendsItemClick(Long id) {
        if (id == 0) {
            //TODO 邀请
        } else {
            //TODO IM
        }
    }

    @Override
    public void onIndexItemClick(double itemTotalPrice) {

    }
}
