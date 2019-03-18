package com.yijia.common_yijia.main.index;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.yijia.delegates.bottom.BottomItemDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.yijia.lisener.AppBarStateChangeListener;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.yijia.util.GlideUtils;
import com.example.yijia.util.callback.CallbackManager;
import com.example.yijia.util.callback.CallbackType;
import com.example.yijia.util.callback.IGlobalCallback;
import com.example.yijia.util.log.LatteLogger;
import com.joanzapata.iconify.widget.IconTextView;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.index.friendcircle.IndexCameraCheckInstener;
import com.yijia.common_yijia.main.index.friendcircle.LetterDelagate;
import com.yijia.common_yijia.main.index.friendcircle.LetterPeopleDelagate;
import com.yijia.common_yijia.main.index.friendcircle.pictureselector.PhotoDelegate2;
import com.yijia.common_yijia.main.index.friends.IFriendsItemListener;
import com.yijia.common_yijia.main.index.friends.IndexFriendsAdapter;
import com.yijia.common_yijia.main.index.friends.YjIndexFriendsDataConverter;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.blur.PopupBlurOption;


public class YjIndexDelegate extends BottomItemDelegate implements  IFriendsItemListener, IIndexItemListener ,IndexCameraCheckInstener,IIndexCanReadItemListener {

        private final int ALLMODE = 0;
        private final int IMAGEMODE = 1;
        private final int VIDEOMODE = 2;
        private final int AUDIOMODE = 3;
        private final int TEXTMODE = 4;
        private Bundle mArgs = null;
        public static final String PICKTYPE = "PICKTYPE";
        boolean isFirst=true;
    private Bundle mArgsLetterpeople = null;

    @BindView(R2.id.rv_index)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.rv_friends)
    RecyclerView mFriendsRecyclerView = null;
    @BindView(R2.id.srl_index)
    SwipeRefreshLayout mRefreshLayout = null;
    @BindView(R2.id.tb_index)
    Toolbar mToolbar = null;
    @BindView(R2.id.ctl)
    CollapsingToolbarLayout ctl = null;
    @BindView(R2.id.abl)
    AppBarLayout abl = null;
    @BindView(R2.id.icon_index_scan)
    IconTextView mIconScan = null;
    @BindView(R2.id.icon_index_message)
    IconTextView mSend = null;

    @BindView(R2.id.cimg_img)
    CircleImageView cimg_img = null;
    @BindView(R2.id.tv_name)
    AppCompatTextView tv_name = null;



    private YjReFreshHandler mRefreshHandler = null;

    YjIndexAdapter mAdapter = null;
    IndexFriendsAdapter friendsAdapter = null;
    /*popu START*/
    Animation enterAnimation = null;
    Animation dismissAnimation = null;
    int gravity;
    /*popu END*/

    @OnClick({R2.id.icon_index_message,R2.id.icon_index_message2})
    void onCLickpublish(View v) {
        useSDCardWithCheck(v,this);
//        mSend.showContextMenu();
//        getSupportDelegate().start(new PhotoDelegate());

    }


    @OnClick({R2.id.icon_index_scan,R2.id.icon_index_scan2})
    void onCLickScanOrCode() {
        startScanWithCheck(this.getParentDelegate());
    }
    @OnClick(R2.id.tv_invite)
    void mInvite() {
        showToast("暂未开通");
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        mRefreshHandler = YjReFreshHandler.create(mRefreshLayout, mRecyclerView, null, this, this,this);
        CallbackManager.getInstance()
                .addCallback(CallbackType.ON_SCAN, new IGlobalCallback<String>() {
                    @Override
                    public void executeCallback(@Nullable String args) {
                        Toast.makeText(getContext(), args, Toast.LENGTH_LONG).show();
                    }
                });

        this.registerForContextMenu(mSend);
//        mSend.setOnCreateContextMenuListener(this);
        initPopup();
        initView();

    }

    private void initView() {
        initTopBar();
        String name = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getNickname();
        if(!TextUtils.isEmpty(name)){
            tv_name.setText(name);
        }
        String img = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getImagePath();
            Glide.with(Objects.requireNonNull(getContext()))
                    .load(img)
                    .apply(GlideUtils.USEROPTIONS)
                    .into(cimg_img);
    }

    private void initTopBar() {
        abl.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if( state == State.EXPANDED ) {

                    //展开状态
                    mToolbar.setVisibility(View.GONE);
                }else if(state == State.COLLAPSED){

                    //折叠状态
                    mToolbar.setVisibility(View.VISIBLE);
                }else {

                    //中间状态
                    mToolbar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = new Bundle();

    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.add(1, TEXTMODE, 1, "文字");
//        menu.add(1, IMAGEMODE, 1, "图片");
//        menu.add(1, VIDEOMODE, 1, "视频");
//        menu.add(1, AUDIOMODE, 1, "音频");
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        PhotoDelegate delegate = new PhotoDelegate();
//        switch (item.getItemId()) {
//            case IMAGEMODE:
//                mArgs.putInt(PICKTYPE, IMAGEMODE);
//                delegate.setArguments(mArgs);
//                getParentDelegate().getSupportDelegate().start(delegate);
//                break;
//            case VIDEOMODE:
//                mArgs.putInt(PICKTYPE, VIDEOMODE);
//                delegate.setArguments(mArgs);
//                getParentDelegate().getSupportDelegate().start(delegate);
//                break;
//            case AUDIOMODE:
//                mArgs.putInt(PICKTYPE, AUDIOMODE);
//                delegate.setArguments(mArgs);
//                getParentDelegate().getSupportDelegate().start(delegate);
//                break;
//            case TEXTMODE:
//                mArgs.putInt(PICKTYPE, TEXTMODE);
//                delegate.setArguments(mArgs);
//                getParentDelegate().getSupportDelegate().start(delegate);
//                break;
//
//            default:
//                break;
//        }
//        return super.onContextItemSelected(item);
//    }

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
        mAdapter = new YjIndexAdapter(data, this);
        mAdapter.setIndexItemListener(this);
        mAdapter.setIndexCanReadItemListener(this);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        //scorllview相关
        manager.setSmoothScrollbarEnabled(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setFocusableInTouchMode(false);
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
//        initFriendsRecyclerView();
//        initIndexRecyclerView();
        Log.d("refresh","onLazyInitView");
        mRefreshHandler.firstPage();
        isFirst=false;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if(!isFirst){
            Log.d("refresh","onSupportVisible!isFirs");
            mRefreshHandler.firstPage();
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_index_yijia2;
    }



    @Override
    public void onFriendsItemClick(Long id, String rongId, String name) {
        if (id == 0) {
            //TODO 邀请
        } else {
            //TODO IM
//            Conversation.ConversationType type=Conversation.ConversationType.PRIVATE;
//            getParentDelegate().getSupportDelegate().start(new ConversationDelegate());
//            RongIM.getInstance().startPrivateChat(getContext(),"app_"+id, name);
//            getParentDelegate().getSupportDelegate().start(new ConversationDelegate());
        }
    }

    @Override
    public void onIndexItemClick(double itemTotalPrice) {
        if(!isFirst){
            Log.d("refresh","onIndexItemClick");
            mRefreshHandler.firstPage();
        }
    }
    /*popup Start*/
    void initPopup() {
        enterAnimation = createVerticalAnimation(-1f, 0);
        dismissAnimation = createVerticalAnimation(0, -1f);
        gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
    }

    private Animation createVerticalAnimation(float fromY, float toY) {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                fromY,
                Animation.RELATIVE_TO_SELF,
                toY);
        animation.setDuration(500);
        animation.setInterpolator(new DecelerateInterpolator());
        return animation;
    }

    private Animation createHorizontalAnimation(float fromX, float toX) {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                fromX,
                Animation.RELATIVE_TO_SELF,
                toX,
                Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                0f);
        animation.setDuration(500);
        animation.setInterpolator(new DecelerateInterpolator());
        return animation;
    }

    @Override
    public void ok() {
    }

    @Override
    public void ok(View v) {
        showIndexPopup(v);
    }



    void showIndexPopup(View v){
        QuickPopupBuilder.with(getContext())
                .contentView(R.layout.basepopu_index)
                .config(new QuickPopupConfig()
                        .clipChildren(true)
                        .backgroundColor(Color.parseColor("#8C617D8A"))
                        .withShowAnimation(enterAnimation)
                        .withDismissAnimation(dismissAnimation)
                        .gravity(gravity)
                        .blurBackground(true, new BasePopupWindow.OnBlurOptionInitListener() {
                            @Override
                            public void onCreateBlurOption(PopupBlurOption option) {
                                option.setBlurRadius(6)
                                        .setBlurPreScaleRatio(0.9f);
                            }
                        })
                        .withClick(R.id.ll_camera, v1 -> {
                            PhotoDelegate2 delegate = new PhotoDelegate2();
                            mArgs.putInt(PICKTYPE, ALLMODE);
                            delegate.setArguments(mArgs);
                            getParentDelegate().getSupportDelegate().start(delegate);
                        }, true)
                        .withClick(R.id.ll_vodeo, v1 -> {
                            PhotoDelegate2 delegate = new PhotoDelegate2();
                            mArgs.putInt(PICKTYPE, VIDEOMODE);
                            delegate.setArguments(mArgs);
                            getParentDelegate().getSupportDelegate().start(delegate);
                        }, true)
                        .withClick(R.id.ll_letter, v1 -> {
                            getParentDelegate().getSupportDelegate().start(new LetterDelagate());
                        }, true))
                .show(v);
    }
    /*popup END*/



    @Override
    public void checkok(View v) {

    }

    @Override
    public void goCanReadList(int circleId) {
         LetterPeopleDelagate lpDelegate=new LetterPeopleDelagate();
        mArgsLetterpeople=new Bundle();
        mArgsLetterpeople.putInt("circleId",circleId);
        lpDelegate.setArguments(mArgsLetterpeople);
        getParentDelegate().getSupportDelegate().start(lpDelegate);
    }


}
