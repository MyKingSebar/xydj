package com.yijia.common_yijia.main.index;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.model.GuidePage;
import com.baidu.ocr.ui.util.DimensionUtil;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.widget.RobotImageView;
import com.example.yijia.app.AccountManager;
import com.example.yijia.app.IUserChecker;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.bottom.BottomItemDelegate;
import com.example.yijia.lisener.AppBarStateChangeListener;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.tool.RxImageTool;
import com.example.yijia.ui.TextViewUtils;
import com.example.yijia.ui.dialog.JDialogUtil;
import com.example.yijia.ui.dialog.RxDialogSureCancelListener;
import com.example.yijia.util.GlideUtils;
import com.example.yijia.util.callback.CallbackManager;
import com.example.yijia.util.callback.CallbackType;
import com.example.yijia.util.callback.IGlobalCallback;
import com.example.yijia.util.dimen.DimenUtil;
import com.example.yijia.util.listener.OnSingleClickListener;
import com.example.yijia.util.log.LatteLogger;
import com.joanzapata.iconify.widget.IconTextView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.friends.FriendsDelegate2;
import com.yijia.common_yijia.main.index.friendcircle.IndexCameraCheckInstener;
import com.yijia.common_yijia.main.index.friendcircle.LetterDelagate;
import com.yijia.common_yijia.main.index.friendcircle.LetterPeopleDelagate;
import com.yijia.common_yijia.main.index.friendcircle.pictureselector.PhotoDelegate2;
import com.yijia.common_yijia.main.index.friendcircle.smallvideo.SmallCameraLisener;
import com.yijia.common_yijia.main.index.friends.IFriendsItemListener;
import com.yijia.common_yijia.main.index.friends.IndexFriendsAdapter;
import com.yijia.common_yijia.main.index.friends.YjIndexFriendsDataConverter;
import com.yijia.common_yijia.main.index.invite.InviteRelationshipDelegate;
import com.yijia.common_yijia.main.message.view.fragment.NoticeDelegate;
import com.yijia.common_yijia.main.mine.MineDelegate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;


public class YjIndexDelegate extends BottomItemDelegate implements IFriendsItemListener, IIndexItemListener, IndexCameraCheckInstener, IIndexCanReadItemListener, IPlayVideoListener, IDeleteListener {
    public final String TAG = getClass().getName();
    private final int SHOWTOPITEMTYPE_MINE = 1;
    private final int SHOWTOPITEMTYPE_CREATER = 2;
    private final int SHOWTOPITEMTYPE_ORDERLY = 3;
    private final int SHOWTOPITEMTYPE_VISITOR = 4;

    private MainFamily mCurrentFamily;
    private final int ALLMODE = 0;
    private final int IMAGEMODE = 1;
    private final int VIDEOMODE = 2;
    private final int AUDIOMODE = 3;
    private final int TEXTMODE = 4;
    @BindView(R2.id.tv_online)
    AppCompatTextView tv_online;
    Unbinder unbinder;
    private Bundle mArgs = null;
    public static final String PICKTYPE = "PICKTYPE";
    boolean isFirst = true;
    private Bundle mArgsLetterpeople = null;
    PopupWindow popupWindow;

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


    @BindView(R2.id.hsv_top_item)
    HorizontalScrollView hsv_top_item = null;

    @BindView(R2.id.cimg_img)
    RobotImageView cimg_img = null;
    @BindView(R2.id.tv_name)
    AppCompatTextView tv_name = null;


    @BindView(R2.id.tv_invite)
    AppCompatTextView tv_invite = null;

    //IndexWebFragment
    AppCompatTextView tvZcjl, tvCtqm, tvKhjl, tvTxjl, tvQin;
    private boolean isConfirm = false;
    LinearLayout ll_top_item_layout;

    LinearLayoutCompat ll_unread, ll_invite;
    ImageView tv_unreadicon;
    private ImageView coverImage;


    private List<MainFamily> families = new ArrayList<>();

    private MainFamilyAdapter adapter;

    private SmallCameraLisener mSmallCameraLisener = null;

    public void setSmallCameraLisener(SmallCameraLisener mSmallCameraLisener) {
        this.mSmallCameraLisener = mSmallCameraLisener;
    }

    private YjReFreshHandler mRefreshHandler = null;

    YjIndexAdapter mAdapter = null;
    IndexFriendsAdapter friendsAdapter = null;
    /*popu START*/
    Animation enterAnimation = null;
    Animation dismissAnimation = null;
    int gravity;
    /*popu END*/

    @OnClick({R2.id.icon_index_message, R2.id.icon_index_message2})
    void onCLickpublish(View v) {
//        useSDCardWithCheck(v, this);
//        mSend.showContextMenu();
//        getSupportDelegate().start(new PhotoDelegate());
        useSDCardWithCheck(v, this);

    }


    @OnClick({R2.id.icon_index_scan, R2.id.icon_index_scan2})
    void onCLickScanOrCode() {
        getParentDelegate().getSupportDelegate().start(new MineDelegate());
//        startScanWithCheck(this.getParentDelegate());
    }

    @OnClick(R2.id.tv_invite)
    void mInvite() {
        InviteRelationshipDelegate mDelegate = InviteRelationshipDelegate.create(mCurrentFamily.familyId);
        getParentDelegate().getSupportDelegate().start(mDelegate);

//        showToast("暂未开通");
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        mRefreshHandler = YjReFreshHandler.create(mRefreshLayout, mRecyclerView, null, this, this, this, this, this);
        CallbackManager.getInstance()
                .addCallback(CallbackType.ON_SCAN, (IGlobalCallback<String>) args -> Toast.makeText(getContext(), args, Toast.LENGTH_LONG).show());

        this.registerForContextMenu(mSend);
//        mSend.setOnCreateContextMenuListener(this);
        initPopup();
        initTopItem(rootView);
        initView(rootView);
        initGuide();
    }

    private void initGuide() {
        int hightint = DimenUtil.getScreenHeight();
        int weithint = DimenUtil.getScreenWidth();
        NewbieGuide.with(getActivity())
                .setLabel("切换父母")
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                        Log.e(TAG, "NewbieGuide onShowed: ");
                        //引导层显示
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        Log.e(TAG, "NewbieGuide  onRemoved: ");
                        //引导层消失（多页切换不会触发）
                    }
                })
                .setOnPageChangedListener(page -> {
                    //引导页切换，page为当前页位置，从0开始
                    Log.e(TAG, "引导页切换：" + page);
                })
                .alwaysShow(false)//是否每次都显示引导层，默认false，只显示一次
                .addGuidePage(//添加一页引导页
                        GuidePage.newInstance()//创建一个实例
                                .addHighLight(tv_name)//添加高亮的view
//                                .addHighLight(tv_name,
//                                        new RelativeGuide(R.layout.view_relative_guide, Gravity.TOP, 100) {
//                                            @Override
//                                            protected void offsetMargin(MarginInfo marginInfo, ViewGroup viewGroup, View view) {
//                                                marginInfo.leftMargin += 100;
//                                            }
//                                        })
                                .setLayoutRes(R.layout.guide_choose_father)//设置引导页布局
//                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
//
//                                    @Override
//                                    public void onLayoutInflated(View view, Controller controller) {
//                                        //引导页布局填充后回调，用于初始化
//                                        TextView tv = view.findViewById(R.id.textView2);
//                                        tv.setText("我是动态设置的文本");
//                                    }
//                                })
//                                .setEnterAnimation(enterAnimation)//进入动画
//                                .setExitAnimation(exitAnimation)//退出动画
                )
                .addGuidePage(GuidePage.newInstance()
                                .addHighLight(tv_invite)
                                .setLayoutRes(R.layout.guide_invite)//设置引导页布局
//                        .setLayoutRes(R.layout.view_guide_custom, R.id.iv)//引导页布局，点击跳转下一页或者消失引导层的控件id
//                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
//                            @Override
//                            public void onLayoutInflated(View view, final Controller controller) {
//                                view.findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        controller.showPreviewPage();
//                                    }
//                                });
//                            }
//                        })
//                        .setEverywhereCancelable(false)//是否点击任意地方跳转下一页或者消失引导层，默认true
//                        .setBackgroundColor(getResources().getColor(R.color.testColor))//设置背景色，建议使用有透明度的颜色
//                        .setEnterAnimation(enterAnimation)//进入动画
//                        .setExitAnimation(exitAnimation)//退出动画
                )
                .addGuidePage(GuidePage.newInstance()
                                .addHighLight(mSend)
                                .setLayoutRes(R.layout.guide_album)//设置引导页布局
//                        .setEverywhereCancelable(false)//是否点击任意地方跳转下一页或者消失引导层，默认true
                )
                .addGuidePage(GuidePage.newInstance()
                                .addHighLight(new RectF(weithint * 2 / 3, hightint - RxImageTool.dp2px(58), weithint, hightint))
                                .setLayoutRes(R.layout.guide_find)//设置引导页布局
//                        .setEverywhereCancelable(false)//是否点击任意地方跳转下一页或者消失引导层，默认true
                )
                .addGuidePage(GuidePage.newInstance()
                                .addHighLight(new RectF(weithint / 3, hightint - RxImageTool.dp2px(65), weithint * 2 / 3, hightint))
                                .setLayoutRes(R.layout.guide_robot)//设置引导页布局
//                        .setEverywhereCancelable(false)//是否点击任意地方跳转下一页或者消失引导层，默认true
                )
//                .addGuidePage(GuidePage.newInstance()
//                        .addHighLight(tvBottom)
//                        .setLayoutRes(R.layout.view_guide_dialog)
//                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
//                            @Override
//                            public void onLayoutInflated(View view, final Controller controller) {
//                                view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        controller.showPage(0);
//                                    }
//                                });
//                            }
//                        })
//                )
                .show();//显示引导层(至少需要一页引导页才能显示)
    }

    private PopupWindow mCoverPopup;
    private int mHeight, mWidth;
    private TextView changeConver;
    private void initTopPopup() {
        View pop_layout = LayoutInflater.from(getContext()).inflate(R.layout.popup_change_cover, null);
        mCoverPopup = new PopupWindow(getContext());
        mCoverPopup.setContentView(pop_layout);
        mCoverPopup.setFocusable(true);
        mCoverPopup.setOutsideTouchable(true);
        mCoverPopup.setWidth(DimensionUtil.dpToPx(150));
        mCoverPopup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mCoverPopup.setFocusable(true);
        mCoverPopup.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
        changeConver = pop_layout.findViewById(R.id.popup_change_cover);
        changeConver.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View v) {
                getImage();
                mCoverPopup.dismiss();
            }
        });

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        pop_layout.measure(w, h);
        //获取PopWindow宽和高
        mHeight = pop_layout.getMeasuredHeight();
        mWidth = pop_layout.getMeasuredWidth();

    }

    private void initTopItem(View rootView) {

        coverImage = rootView.findViewById(R.id.main_cover);
        initTopPopup();
        coverImage.setOnTouchListener(new View.OnTouchListener() {
            boolean show = true;
            int tempX;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        show = true;
                        tempX = (int) event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                            show = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        if(show) {
                            if(mCurrentFamily.familyId != 0 )
                                return false;
                            int X = (int) event.getX();
                            int Y = (int) event.getY();
                            int xoff = X - mWidth / 2;
                            int yoff = 0 - (v.getHeight() - Y) - mHeight;
                            mCoverPopup.showAsDropDown(v, xoff, yoff);
                            return true;
                        } else {
                            return false;
                        }

                }

                return true;
            }

        });

        ll_top_item_layout = rootView.findViewById(R.id.ll_top_item_layout);
        tvZcjl = rootView.findViewById(R.id.zcjl);
        tvCtqm = rootView.findViewById(R.id.ctqm);
        tvKhjl = rootView.findViewById(R.id.khjl);
        tvTxjl = rootView.findViewById(R.id.txjl);
        tvQin = rootView.findViewById(R.id.qin);
        hideTopItem();
        tvZcjl.setOnClickListener(v -> {
            if (isConfirm) {
                getParentDelegate().getSupportDelegate().start(IndexWebFragment.create(mCurrentFamily.mainUserId, IndexWebFragment.ZCJL_TYPE));
            } else {
                Toast.makeText(getContext(), R.string.parent_has_not_confirm, Toast.LENGTH_LONG).show();
            }
        });
        tvCtqm.setOnClickListener(v -> {
            if (isConfirm) {
                getParentDelegate().getSupportDelegate().start(IndexWebFragment.create(mCurrentFamily.mainUserId, IndexWebFragment.CTQM_TYPE));
            } else {
                Toast.makeText(getContext(), R.string.parent_has_not_confirm, Toast.LENGTH_LONG).show();
            }
        });
        tvKhjl.setOnClickListener(v -> {
            if (isConfirm) {
                getParentDelegate().getSupportDelegate().start(IndexWebFragment.create(mCurrentFamily.mainUserId, IndexWebFragment.KHJL_TYPE));
            } else {
                Toast.makeText(getContext(), R.string.parent_has_not_confirm, Toast.LENGTH_LONG).show();
            }
        });
        tvTxjl.setOnClickListener(v -> {
            if (isConfirm) {
                getParentDelegate().getSupportDelegate().start(IndexWebFragment.create(mCurrentFamily.mainUserId, IndexWebFragment.TXJL_TYPE));
            } else {
                Toast.makeText(getContext(), R.string.parent_has_not_confirm, Toast.LENGTH_LONG).show();
            }
        });
        tvQin.setOnClickListener(v -> {
            if (isConfirm) {
                Bundle bundle = new Bundle();
                bundle.putString("familyName", mCurrentFamily.mainUserName);
                bundle.putLong("familyId", mCurrentFamily.familyId);
                FriendsDelegate2 friendsDelegate2 = new FriendsDelegate2();
                friendsDelegate2.setArguments(bundle);
                getParentDelegate().getSupportDelegate().start(friendsDelegate2);
            } else {
                Toast.makeText(getContext(), R.string.parent_has_not_confirm, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getFriendsCount() {
        RxRestClient.builder()
                .url("friend/query_friend_count")
                .params("yjtk", YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk())
                .params("familyId", mCurrentFamily.familyId)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(Latte.getApplicationContext()) {
                    @Override
                    public void onResponse(String r) {
                        final JSONObject object = JSON.parseObject(r);
                        final String status = object.getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            JSONObject jo = object.getJSONObject("data");
                            if (null != jo) {
                                if (jo.containsKey("friendCount")) {
                                    int count = jo.getInteger("friendCount");
                                    tvQin.setText(getString(R.string.num_of_relation, count));
                                }

                            }
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(Latte.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void hideTopItem() {
        tvZcjl.setVisibility(View.GONE);
        tvCtqm.setVisibility(View.GONE);
        tvKhjl.setVisibility(View.GONE);
        tvTxjl.setVisibility(View.GONE);
    }

    private void showTopItem(int type) {
        getFriendsCount();
        switch (type) {
            case SHOWTOPITEMTYPE_MINE:
            case SHOWTOPITEMTYPE_CREATER:
            case SHOWTOPITEMTYPE_ORDERLY:
                tvZcjl.setVisibility(View.VISIBLE);
                tvCtqm.setVisibility(View.VISIBLE);
                tvKhjl.setVisibility(View.VISIBLE);
                tvTxjl.setVisibility(View.VISIBLE);
                hsv_top_item.fullScroll(ScrollView.FOCUS_RIGHT);
                break;
            case SHOWTOPITEMTYPE_VISITOR:
                hideTopItem();
                break;
            default:

        }
    }

    private void initView(View rootview) {
        tv_unreadicon = rootview.findViewById(R.id.tv_unreadicon);
        ll_unread = rootview.findViewById(R.id.ll_unread);
        ll_invite = rootview.findViewById(R.id.ll_invite);
        ll_unread.setVisibility(View.GONE);
        ll_unread.setOnClickListener(v -> getParentDelegate().getSupportDelegate().start(new NoticeDelegate()));
        initTopBar();
        String name = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getNickname();
        if (!TextUtils.isEmpty(name)) {
            tv_name.setText(name);
        }
        CallbackManager.getInstance().addCallback(CallbackType.REFRESH_MAIN_USER_NAME, new IGlobalCallback() {
            @Override
            public void executeCallback(@Nullable Object args) {
                if(null != mCurrentFamily && 0 == mCurrentFamily.familyId) {
                    String name = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getNickname();
                    if (!TextUtils.isEmpty(name)) {
                        tv_name.setText(name);
                    }

                    String url = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getImagePath();
                    if (!TextUtils.isEmpty(name)) {
                        GlideUtils.load(getContext(), url, cimg_img.userImageView(), GlideUtils.USERMODE);
                    }
                }
            }
        });
        tv_name.setOnClickListener(v -> {
            if (null == popupWindow) {
                adapter = new MainFamilyAdapter(getContext(), families, mCurrentFamily);
                popupWindow = new SpinnerPopuwindow(getActivity(), adapter, (parent, view, position, id) -> {
                    mCurrentFamily = families.get(position);
                    showTopItem(families.get(position).permissionType);
                    mRefreshHandler.firstPage(mCurrentFamily.familyId);
                    adapter.updateChecked(mCurrentFamily);
                    TextViewUtils.AppCompatTextViewSetText(tv_name, mCurrentFamily.familyName);
                    GlideUtils.load(getContext(), mCurrentFamily.headImage, cimg_img.userImageView(), GlideUtils.USERMODE);
                    cimg_img.setRobotOnline(mCurrentFamily.robotIsOnline);
                    popupWindow.dismiss();
                    setIsConfirm();
                    getCover();
                });
            }
            if (!popupWindow.isShowing()) {
                ((SpinnerPopuwindow) popupWindow).showPopupWindow(tv_name);
                getFamilyData();
            }
        });
        String img = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getImagePath();
        GlideUtils.load(getContext(), img, cimg_img.userImageView(), GlideUtils.USERMODE);
        setOnlineStatue(0);
        getFamilyData();

        CallbackManager.getInstance().addCallback(CallbackType.ROBOT_REMIND_DELETE, new IGlobalCallback() {
            @Override
            public void executeCallback(@Nullable Object args) {
                if (((long) args) == mCurrentFamily.familyId) {
                    isFirst = true;
                    getFamilyData();
                }
            }
        });
    }

    private void setIsConfirm() {
        if (0L == mCurrentFamily.familyId) {
            isConfirm = true;
        } else {
            getCurrentFamilyMainerIsConfirm();
        }
    }

    private void getCurrentFamilyMainerIsConfirm() {
        RxRestClient.builder()
                .url("family/query_parent_confirm")
                .params("yjtk", YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk())
                .params("familyId", mCurrentFamily.familyId)
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
                            if (null != jsondata && jsondata.containsKey("isConfirm")) {
                                isConfirm = jsondata.getLong("isConfirm") == 1 ? true : false;
                            }
                        } else {
                            isConfirm = false;
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        isConfirm = false;
                    }
                });
    }


    public void checkUnread() {
        RxRestClient.builder()
                .url("notification/query_newest_unread")
                .params("yjtk", YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk())
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(Latte.getApplicationContext()) {
                    @Override
                    public void onResponse(String r) {
                        final JSONObject object = JSON.parseObject(r);
                        final String status = object.getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            JSONObject jo = object.getJSONObject("data");
                            int haveUnread = jo.getInteger("haveUnread");
                            String imagePath = jo.getString("imagePath");
                            if (haveUnread == 1) {
                                ll_invite.setVisibility(View.GONE);
                                GlideUtils.load(getContext(), imagePath, tv_unreadicon, GlideUtils.DEFAULTMODE);
                                ll_unread.setVisibility(View.VISIBLE);
                            } else {
                                ll_unread.setVisibility(View.GONE);
                                ll_invite.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(Latte.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void setOnlineStatue(long id) {
        RxRestClient.builder()
                .url("robot/query_robot_is_online")
                .params("yjtk", YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk())
                .params("targetUserId", id)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(Latte.getApplicationContext()) {
                    @Override
                    public void onResponse(String r) {
                        final JSONObject object = JSON.parseObject(r);
                        final String status = object.getString("status");
                        boolean isOnline = false;
                        if (TextUtils.equals(status, "1001")) {
                            JSONObject jo = object.getJSONObject("data");
                            isOnline = jo.getBoolean("isOnline");
                        }
                        if (isOnline) {
                            tv_online.setText(R.string.xy_online);
                        } else {
                            tv_online.setText(R.string.xy_unonline);
                        }
                        cimg_img.setRobotOnline(isOnline);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(Latte.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void getFamilyData() {
//        hideTopItem();
        Observable ob1 = RxRestClient.builder()
                .url("robot/query_robot_is_online")
                .params("yjtk", YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk())
                .params("targetUserId", YjDatabaseManager.getInstance().getDao().loadAll().get(0).getId())
                .build()
                .get()
                .subscribeOn(Schedulers.io());

        Observable ob2 = RxRestClient.builder()
                .url("family/query_family")
                .params("yjtk", YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk())
                .build()
                .get()
                .subscribeOn(Schedulers.io());

        Observable.zip(ob1, ob2, (BiFunction<String, String, Map>) (s, s2) -> {
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
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Map>(Latte.getApplicationContext()) {
                    @Override
                    public void onResponse(Map r) {
                        String response = (String) r.get("json");
                        boolean isOnline = (Boolean) r.get("isOnLine");
                        final JSONObject object = JSON.parseObject(response);
                        final String status = object.getString("status");

                        families.clear();
                        MainFamily family = null;

                        family = new MainFamily();
                        family.familyId = 0;
                        family.familyName = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getNickname();
                        family.mainUserId = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getId();
                        family.mainUserName = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getNickname();
                        family.relationMainToUser = "本人";
                        family.relationUserToMain = "本人";
                        family.robotIsOnline = isOnline ? 1 : 2;
                        family.headImage = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getImagePath();
                        family.permissionType = 1;
                        families.add(family);

                        if (isFirst) {
                            mCurrentFamily = family;
                        }

                        if (TextUtils.equals(status, "1001")) {
                            final JSONArray guardianUserList = object.getJSONArray("data");
                            final int size = guardianUserList.size();
                            for (int i = 0; i < size; i++) {
                                final JSONObject data = guardianUserList.getJSONObject(i);
                                family = new MainFamily();
                                family.familyId = data.getLong("familyId");
                                family.familyName = data.getString("familyName");
                                family.mainUserId = data.getLong("mainUserId");
                                family.mainUserName = data.getString("mainUserName");
                                family.relationMainToUser = data.getString("relationMainToUser");
                                family.relationUserToMain = data.getString("relationUserToMain");
                                family.robotIsOnline = data.getInteger("robotIsOnline");
                                family.headImage = data.getString("headImage");
                                family.permissionType = data.getInteger("permissionType");
                                if ((2 == family.permissionType || 3 == family.permissionType) && mCurrentFamily.permissionType != 2) {
                                    if (isFirst) {
                                        mCurrentFamily = family;
                                    }
                                }
                                families.add(family);
                            }

                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(Latte.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                        }
                        if (null != adapter && popupWindow.isShowing()) {
                            adapter.notifyDataSetChanged();
                        }
                        if (isFirst) {
                            TextViewUtils.AppCompatTextViewSetText(tv_name, family.familyName);
                            GlideUtils.load(getContext(), mCurrentFamily.headImage, cimg_img.userImageView(), GlideUtils.USERMODE);
                            cimg_img.setRobotOnline(mCurrentFamily.robotIsOnline);
                            showTopItem(SHOWTOPITEMTYPE_CREATER);
                            mRefreshHandler.firstPage(mCurrentFamily.familyId);
                            setIsConfirm();
                            getCover();
                            isFirst = false;
                        }

                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(Latte.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }


    private void initTopBar() {
        abl.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    mToolbar.setVisibility(View.GONE);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    mToolbar.setVisibility(View.VISIBLE);
                } else {
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

//    private void getIndexSucceed(String response) {
//        final ArrayList<MultipleItemEntity> data =
//                new YjIndexDataConverter()
//                        .setJsonData(response)
//                        .convert();
//        mAdapter = new YjIndexAdapter(data, this);
//        mAdapter.setIndexItemListener(this);
//        mAdapter.setIndexCanReadItemListener(this);
//        mAdapter.setmIPlayVideoListener(this);
//        mAdapter.setmDeleteListener(this);
//        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        //scorllview相关
//        manager.setSmoothScrollbarEnabled(true);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setNestedScrollingEnabled(false);
//        mRecyclerView.setFocusableInTouchMode(false);
//        mRecyclerView.setLayoutManager(manager);
//        mRecyclerView.setAdapter(mAdapter);
//    }


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

//    private void initIndexRecyclerView() {
//        final String url = "circle/query_timeline";
//        String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
//        RxRestClient.builder()
//                .url(url)
//                .params("yjtk", token)
//                .params("queryType", 1)
//                .params("pageNo", 1)
//                .params("pageSize", 20)
//                .params("beginCircleId", 0)
//                .build()
//                .post()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserver<String>(getContext()) {
//                    @Override
//                    public void onResponse(String response) {
//                        LatteLogger.json("query_timeline", response);
//                        final String status = JSON.parseObject(response).getString("status");
//                        if (TextUtils.equals(status, "1001")) {
//                            getIndexSucceed(response);
//                        } else {
//                            final String msg = JSON.parseObject(response).getString("msg");
//                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFail(Throwable e) {
//                        Toast.makeText(getContext(), "请稍后尝试", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRefreshLayout();
//        initRecyclerView();
//        initFriendsRecyclerView();
//        initIndexRecyclerView();
        Log.d("refresh", "onLazyInitView");

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        checkUnread();
        if (!isFirst) {
            Log.d("refresh", "onSupportVisible!isFirs");
            mRefreshHandler.firstPage(mCurrentFamily.familyId);
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
        if (!isFirst) {
            Log.d("refresh", "onIndexItemClick");
            mRefreshHandler.firstPage(mCurrentFamily.familyId);
        }
    }

    /*popup Start*/
    void initPopup() {
        enterAnimation = createVerticalAnimation(-1f, 0);
        dismissAnimation = createVerticalAnimation(0, -1f);
//        gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        gravity = Gravity.CENTER;
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


    void showIndexPopup(View v) {
        QuickPopupBuilder.with(getContext())
                .contentView(R.layout.basepopu_index_big)
                .config(new QuickPopupConfig()
                        .clipChildren(true)
                        .backgroundColor(Color.parseColor("#8C617D8A"))
//                        .withShowAnimation(enterAnimation)
//                        .withDismissAnimation(dismissAnimation)
                        .gravity(gravity)
                        .blurBackground(false, option -> option.setBlurRadius(6)
                                .setBlurPreScaleRatio(0.9f))
                        .withClick(R.id.ll_camera, v1 -> {
                            PhotoDelegate2 delegate = new PhotoDelegate2();
                            mArgs.putInt(PICKTYPE, ALLMODE);
                            delegate.setArguments(mArgs);
                            getParentDelegate().getSupportDelegate().start(delegate);
                        }, true)
                        .withClick(R.id.ll_vodeo, v1 -> {
                            if (mSmallCameraLisener != null) {
                                mSmallCameraLisener.go();
                            }
//                            Intent intent=new Intent(getActivity(), CameraActivity.class);
//                            getActivity().startActivity(intent);
//                            PhotoDelegate2 delegate = new PhotoDelegate2();
//                            mArgs.putInt(PICKTYPE, VIDEOMODE);
//                            delegate.setArguments(mArgs);
//                            getParentDelegate().getSupportDelegate().start(delegate);
                        }, true)
                        .withClick(R.id.ll_letter, v1 -> getParentDelegate().getSupportDelegate().start(new LetterDelagate()), true))
//                .show(v);
                .show();
    }
    /*popup END*/


    @Override
    public void checkok(View v) {

    }

    @Override
    public void goCanReadList(long circleId) {
        LetterPeopleDelagate lpDelegate = new LetterPeopleDelagate();
        mArgsLetterpeople = new Bundle();
        mArgsLetterpeople.putLong("circleId", circleId);
        lpDelegate.setArguments(mArgsLetterpeople);
        getParentDelegate().getSupportDelegate().start(lpDelegate);
    }


    @Override
    public void play(String[] mediaUrl) {
        PlayVideoDelagate
                delegate = new PlayVideoDelagate();
        mArgs.putStringArray("videos", mediaUrl);
        delegate.setArguments(mArgs);
        getParentDelegate().getSupportDelegate().start(delegate);
    }

    @Override
    public void delete(long id) {
        JDialogUtil.INSTANCE.showRxDialogSureCancel(getContext(), "", 0, "确认删除？", new RxDialogSureCancelListener() {
            @Override
            public void RxDialogSure() {
                JDialogUtil.INSTANCE.dismiss();
                deletego(id);
            }

            @Override
            public void RxDialogCancel() {
                JDialogUtil.INSTANCE.dismiss();
            }
        });
    }

    private void deletego(long id) {
        JDialogUtil.INSTANCE.showRxDialogShapeLoading(getContext());
        final String url = "circle/delete_circle";
        String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("circleId", id)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.json("delete_circle", response);
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            showToast("删除成功");
                            if (!isFirst) {
                                Log.d("refresh", "onIndexItemClick");
                                mRefreshHandler.firstPage(mCurrentFamily.familyId);
                            }
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                        JDialogUtil.INSTANCE.dismiss();

                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), "请稍后尝试", Toast.LENGTH_SHORT).show();
                        JDialogUtil.INSTANCE.dismiss();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private List<LocalMedia> selectList = new ArrayList<>();
    private void getImage() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(1)
                .minSelectNum(1)
                .selectionMode(PictureConfig.SINGLE)
                .previewImage(true)
                .isCamera(true)
                .enableCrop(true)
                .compress(true)
                .glideOverride(160, 160)
                .previewEggs(true)
                .withAspectRatio(1, 1)
                .hideBottomControls(false)
                .freeStyleCropEnabled(true)
                .showCropFrame(true)
                .showCropGrid(true)
                .selectionMedia(selectList)
                .forResult(500);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 500:
                    // 图片选择
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if (!selectList.isEmpty()) {
                        //上传背景
                        uploadImage(selectList);
                    }
                    break;
            }
        }
    }

    private File[] getFiles(List<LocalMedia> list) {
        if (list != null) {
            int size = list.size();
            File[] files = new File[size];
            for (int i = 0; i < size; i++) {
                files[i] = new File(list.get(i).getPath());
            }
            return files;
        } else {
            LatteLogger.w("upLoadImg", "getFiles() == null");
            return null;
        }
    }

    private void setupCover(String s, String path){
        RxRestClient.builder()
                .url("user/update_background")
                .params("yjtk", YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk())
                .params("imagePath", path)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            showToast("封面背景上传成功");
                            if(mCurrentFamily.familyId == 0) {
                                GlideUtils.load(getContext(), s, coverImage, GlideUtils.COVERMODE);
                            }
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                        JDialogUtil.INSTANCE.dismiss();

                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), "请稍后尝试", Toast.LENGTH_SHORT).show();
                        JDialogUtil.INSTANCE.dismiss();
                    }
                });
    }

    private void getCover(){
        RxRestClient.builder()
                .url("user/query_background")
                .params("yjtk", YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk())
                .params("targetUserId", mCurrentFamily.mainUserId)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            JSONObject object = JSON.parseObject(response).getJSONObject("data");
                            String url = null != object ? object.getString("imagePath") : "";
                                GlideUtils.load(getContext(), url, coverImage, GlideUtils.COVERMODE);
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                        JDialogUtil.INSTANCE.dismiss();

                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), "请稍后尝试", Toast.LENGTH_SHORT).show();
                        JDialogUtil.INSTANCE.dismiss();
                    }
                });
    }

    private void uploadImage(List<LocalMedia> imgPath) {
        JDialogUtil.INSTANCE.showRxDialogShapeLoading(getContext());
        final String url = "picture/upload";

        if (imgPath != null) {
            AccountManager.checkAccont(new IUserChecker() {
                @Override
                public void onSignIn() {
                    String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
                    File[] files = getFiles(imgPath);
                    RxRestClient.builder()
                            .url(url)
                            .params("yjtk", token)
//                .params("files", new File[]{new File(imgPath)})
                            .files(files)
                            .build()
                            .uploadwithparams()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new BaseObserver<String>(getContext()) {
                                @Override
                                public void onResponse(String response) {
                                    LatteLogger.json("picture/upload", response);
                                    final JSONObject object = JSON.parseObject(response);
                                    final String status = object.getString("status");
                                    if (TextUtils.equals(status, "1001")) {
                                        final JSONObject dataObject = object.getJSONObject("data");
                                        String serverAddr = dataObject.getString("serverAddr");
                                        final String imgPath = dataObject.getString("path");
                                        String s = serverAddr + imgPath;
                                        //修改头像
                                        setupCover(s, imgPath);
//                                        GlideUtils.load(getContext(), s, coverImage, GlideUtils.COVERMODE);
                                    } else {
                                        Toast.makeText(getContext(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFail(Throwable e) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                @Override
                public void onNoSignIn() {

                }
            });
        }
    }
}
