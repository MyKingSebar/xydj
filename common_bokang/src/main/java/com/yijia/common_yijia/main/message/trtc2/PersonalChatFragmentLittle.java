package com.yijia.common_yijia.main.message.trtc2;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.common_tencent_tuikit.chat.title_bottom.ChatTitieBottomAdapter;
import com.example.common_tencent_tuikit.chat.title_bottom.TitleBottomItemListener;
import com.example.latte.ec.R;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.ui.TextViewUtils;
import com.example.yijia.util.callback.CallbackIntegerManager;
import com.example.yijia.util.callback.IGlobalCallback;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.qcloud.uikit.business.chat.c2c.view.C2CChatPanel;
import com.tencent.qcloud.uikit.business.chat.view.ChatBottomInputGroupCust;
import com.tencent.qcloud.uikit.common.component.titlebar.PageTitleBar;
import com.yijia.common_yijia.main.find.TtsPopup;
import com.yijia.common_yijia.main.find.homedoc.HomeDoctorInDelegate;
import com.yijia.common_yijia.main.message.trtc.BoKangSendMessageListener;
import com.yijia.common_yijia.main.message.trtc.BokangSendMessageUtil;

/**
 * Created by valxehuang on 2018/7/30.
 */

public class PersonalChatFragmentLittle extends LatteDelegate {

    private static final String PUTKEY_CHATID = "chatId";
    private static final String PUTKEY_CHATNAME = "chatName";
    private static final String PUTKEY_CHATHEADURL = "chatHeadUrl";
    private static final String PUTKEY_DISCRIBE = "describe";
    private static final String POSITION = "position";
    private String chatId;
    private String chatName;
    private String chatHeadUrl;
    private String topDescribe;
    private int position;

    private String TAG = "PersonalChatFragmentLittle";
    private C2CChatPanel chatPanel;
    private PageTitleBar chatTitleBar;

    private TextView cpTitle, cpRingt, cpLeft;
    private AppCompatTextView tvDescribe;

    private RecyclerView mRecycleView = null;
    private ChatTitieBottomAdapter mChatTitieBottomAdapter = null;
    private TitleBottomItemListener mTitleBottomItemListener = null;
    private ChatBottomInputGroupCust mInputGroup;
    private ChatBottomInputGroupCust.MessageHandler mMsgHandler;
    BokangSendMessageUtil bokangSendMessageUtil = null;
    private TtsPopup mTtsPopup;

    //是否可见
    public boolean isVisible = false;
    //是否初始化完成
    public boolean isInit = false;
    //是否已经加载过
    public boolean isLoadOver = false;
    public View mRootView = null;



    public static PersonalChatFragmentLittle create(String chatId, String chatName, String chatHeadUrl, String describe,int position
    ) {
        final Bundle args = new Bundle();
        args.putString(PUTKEY_CHATID, chatId);
        args.putString(PUTKEY_CHATNAME, chatName);
        args.putString(PUTKEY_CHATHEADURL, chatHeadUrl);
        args.putString(PUTKEY_DISCRIBE, describe);
        args.putInt(POSITION, position);
        final PersonalChatFragmentLittle delegate = new PersonalChatFragmentLittle();
        delegate.setArguments(args);
        return delegate;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle datas = getArguments();
        //由会话列表传入的会话ID
        assert datas != null;
        chatId = datas.getString(PUTKEY_CHATID);
        chatName = datas.getString(PUTKEY_CHATNAME);
        chatHeadUrl = datas.getString(PUTKEY_CHATHEADURL);
        topDescribe = datas.getString(PUTKEY_DISCRIBE);
        position = datas.getInt(POSITION);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
//        isVisible=false;
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
//        isVisible=true;
//        init();
    }

    @Override
    public Object setLayout() {
        return R.layout.chat_fragment_personal_little;
    }


    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        if(mRootView==null){
            mRootView=rootView;
            isInit=true;
        }
//        init();
        initView(rootView);
        initView2(rootView);
    }

    private void init(){
        if(isInit&&!isLoadOver&&isVisible){
            initView(mRootView);
            initView2(mRootView);
            isLoadOver=true;
        }
    }

    private void initView2(View rootView) {
        tvDescribe = rootView.findViewById(R.id.tv_describe);
        if (TextUtils.isEmpty(topDescribe)) {
            tvDescribe.setVisibility(View.GONE);
        } else {
            if (HomeDoctorInDelegate.closeStatue) {
                tvDescribe.setVisibility(View.GONE);
            } else {
                tvDescribe.setVisibility(View.VISIBLE);
            }

            TextViewUtils.AppCompatTextViewSetText(tvDescribe, chatName + "医生详情：" + topDescribe);
        }
        final View decorView = getActivity().getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            decorView.getWindowVisibleDisplayFrame(r);
            int screenHeight = decorView.getRootView().getHeight();
            int height = screenHeight - r.bottom;
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) chatPanel.getLayoutParams();
            lp.setMargins(0, 0, 0, height);
            chatPanel.requestLayout();
        });
        initVisibility();
    }

    private void initVisibility() {
        CallbackIntegerManager.getInstance()
                .addCallback(position, (IGlobalCallback<String>) args -> {
                    showToast(topDescribe);
                    if (HomeDoctorInDelegate.closeStatue) {
                        tvDescribe.setVisibility(View.GONE);
                    } else {
                        tvDescribe.setVisibility(View.VISIBLE);
                    }
                });
        CallbackIntegerManager.getInstance()
                .addCallback(position+100, (IGlobalCallback<String>) args -> {

                });
    }


    private void initView(View rootView) {

        //从布局文件中获取聊天面板组件
        chatPanel = rootView.findViewById(R.id.chat_panel);
        //单聊组件的默认UI和交互初始化
        chatPanel.initDefault();
        /*
         * 需要指定会话ID（即聊天对象的identify，具体可参考IMSDK接入文档）来加载聊天消息。在上一章节SessionClickListener中回调函数的参数SessionInfo对象中持有每一会话的会话ID，所以在会话列表点击时都可传入会话ID。
         * 特殊的如果用户应用不具备类似会话列表相关的组件，则需自行实现逻辑获取会话ID传入。
         */
        chatPanel.setBaseChatId(chatId, chatName, chatHeadUrl);

        //获取单聊面板的标题栏
        chatTitleBar = chatPanel.getTitleBar();
        chatTitleBar.setVisibility(View.GONE);
        //单聊面板 标题下方功能栏
        mRecycleView = chatTitleBar.getmBottomRecycle();
        mRecycleView.setVisibility(View.GONE);

//        //单聊面板标记栏返回按钮点击事件，这里需要开发者自行控制
//        chatTitleBar.setLeftClick(view -> getSupportDelegate().pop());
//        cpTitle=chatTitleBar.getCenterTitle();
//
//        initBottomRecycle();

        this.mInputGroup = chatPanel.mInputGroup;
        mMsgHandler = mInputGroup.getMsgHandler();

        bokangSendMessageUtil = new BokangSendMessageUtil(TIMManager.getInstance().getConversation(TIMConversationType.C2C, chatId), new BoKangSendMessageListener() {
            @Override
            public void messageSuccess(TIMMessage timMessage) {

            }

            @Override
            public void messageError(int code, String desc) {

            }
        }, getContext());

        mTtsPopup = new TtsPopup(getActivity());
        mTtsPopup.setId(chatId);
        mTtsPopup.setPopupGravity(Gravity.CENTER);
        mTtsPopup.setAllowDismissWhenTouchOutside(false);
        mTtsPopup.setmTtsPopuCallBack(s -> chatPanel.sendChatMsg(s));
        chatPanel.setOnVoiceClickListener(() -> mTtsPopup.showPopupWindow());

    }
//    private void initBottomRecycle() {
//        mChatTitieBottomAdapter = new ChatTitieBottomAdapter(initTitleBottomData());
//        mChatTitieBottomAdapter.setCartItemListener(initTitleBottomItemListener());
//        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        //调整RecyclerView的排列方向
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mRecycleView.setLayoutManager(manager);
//        mRecycleView.setAdapter(mChatTitieBottomAdapter);
//    }

//        private TitleBottomItemListener initTitleBottomItemListener() {
//        mTitleBottomItemListener = (type, name) -> {
//            Log.d("jialei","type:"+type);
//            if (type == null) {
//                return;
//            }
//            if (TextUtils.equals(type, TitleBottomChildType.AUDIOCALL.name())) {
//                final Intent intent2 = new Intent(getContext(), CallWaitingActivity.class);
//                int userId = (YjDatabaseManager.getInstance().getDao().loadAll().get(0).getId()).intValue();
//                intent2.putExtra("roomid",userId);
//                intent2.putExtra("chatId",chatId);
//                intent2.putExtra(CallWaitingActivity.TYPE_KEY,CallWaitingActivity.TYPE_VIDEO);
//                getActivity().startActivity(intent2);
////                BokangSendMessageUtil  bokangSendMessageUtil = new BokangSendMessageUtil(TIMManager.getInstance().getConversation(TIMConversationType.C2C, chatId), new BoKangSendMessageListener() {
////                    @Override
////                    public void messageSuccess(TIMMessage timMessage) {
////
////                    }
////
////                    @Override
////                    public void messageError(int code, String desc) {
////
////                    }
////                }, getContext());
//                bokangSendMessageUtil.sendOnLineMessage(bokangSendMessageUtil.buildBokangMessage(MessageInfoUtil.BOKANG_VIDEO_WAIT,userId+""));
////                if (mMsgHandler != null) {
////                    mMsgHandler.sendMessage(MessageInfoUtil.buildBokangMessage(MessageInfoUtil.BOKANG_VIDEO_WAIT, userId + ""));
////                }
////                String sig = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getTencentImUserSig();
////                String userId = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getTencentImUserId();
////                final Intent intent = new Intent(getContext(), TRTCMainActivity.class);
////                intent.putExtra("roomId", 100);
////                intent.putExtra("userId", userId);
////                intent.putExtra("sdkAppId", TrtcConfig.SDKAPPID);
////                intent.putExtra("userSig", sig);
////                //TODO 需要指定会话ID（即聊天对象的identify，具体可参考IMSDK接入文档）
////                intent.putExtra("chatId", chatId);
////                getActivity().startActivity(intent);
////                //TODO 自定义消息
////                if (mMsgHandler != null)
////                    mMsgHandler.sendMessage(MessageInfoUtil.buildBokangMessage("aaaa"));
//            }
//        };
//        return mTitleBottomItemListener;
//    }

//    private ArrayList<MultipleItemEntity> initTitleBottomData(){
//        final ArrayList<MultipleItemEntity> data =
//                new TitleBottomPersonalChatDataConverter()
//                        .convert();
//        return data;
//    }
}
