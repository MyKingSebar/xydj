package com.yijia.common_yijia.main.message.trtc2;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.common_tencent_tuikit.Constants;
import com.example.common_tencent_tuikit.chat.title_bottom.ChatTitieBottomAdapter;
import com.example.common_tencent_tuikit.chat.title_bottom.TitleBottomChildType;
import com.example.common_tencent_tuikit.chat.title_bottom.TitleBottomItemListener;
import com.example.common_tencent_tuikit.chat.title_bottom.TitleBottomPersonalChatDataConverter;
import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.yijia.delegates.LatteDelegate;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.uikit.business.chat.c2c.view.C2CChatPanel;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;
import com.tencent.qcloud.uikit.business.chat.view.ChatBottomInputGroupCust;
import com.tencent.qcloud.uikit.common.component.titlebar.PageTitleBar;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.find.TtsPopuCallBack;
import com.yijia.common_yijia.main.find.TtsPopup;
import com.yijia.common_yijia.main.message.trtc.BoKangSendMessageListener;
import com.yijia.common_yijia.main.message.trtc.BokangSendMessageUtil;
import com.yijia.common_yijia.main.message.trtc.CallWaitingActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by valxehuang on 2018/7/30.
 */

public class PersonalChatFragment2 extends LatteDelegate {
    private String TAG="PersonalChatFragment2";
    private C2CChatPanel chatPanel;
    private PageTitleBar chatTitleBar;
    private String chatId, chatName, chatHeadUrl;

    private TextView cpTitle,cpRingt,cpLeft;

    private RecyclerView mRecycleView=null;
    private ChatTitieBottomAdapter mChatTitieBottomAdapter=null;
    private TitleBottomItemListener mTitleBottomItemListener=null;
    private ChatBottomInputGroupCust mInputGroup;
    private ChatBottomInputGroupCust.MessageHandler mMsgHandler;
    BokangSendMessageUtil bokangSendMessageUtil=null;
    private TtsPopup mTtsPopup;


    @Override
    public Object setLayout() {
        return R.layout.chat_fragment_personal;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initView(rootView);
//        initView2(rootView);

        final View decorView = getActivity().getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);
                int screenHeight = decorView.getRootView().getHeight();
                int height = screenHeight - r.bottom;

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) chatPanel.getLayoutParams();
                lp.setMargins(0, 0, 0, height);
                chatPanel.requestLayout();
            }
        });

    }

//    private void initView2(View rootView) {
//        if(chatId==null){
//            return;
//        }
//        List<String> users = new ArrayList<String>();
//        users.add(chatId);
//        TIMFriendshipManager.getInstance().getUsersProfile(users, false, new TIMValueCallBack<List<TIMUserProfile>>() {
//            @Override
//            public void onError(int code, String desc){
//                //错误码 code 和错误描述 desc，可用于定位请求失败原因
//                //错误码 code 列表请参见错误码表
//                Log.e(TAG, "getUsersProfile failed: " + code + " desc");
//            }
//
//            @Override
//            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
//                for(TIMUserProfile res : timUserProfiles){
//                    cpTitle.setText(res.getNickName());
//                    Log.e(TAG, "identifier: " + res.getIdentifier() + " nickName: " + res.getNickName());
//                }
//            }
//        });
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle datas = getArguments();
        //由会话列表传入的会话ID
        chatId = datas.getString(Constants.INTENT_DATA);
        chatName = datas.getString(Constants.INTENT_NAME);
        chatHeadUrl = datas.getString(Constants.INTENT_URL);
        return super.onCreateView(inflater, container, savedInstanceState);

//        return mBaseView;
    }


    private void initView(View rootView){

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
        //单聊面板标记栏返回按钮点击事件，这里需要开发者自行控制
        chatTitleBar.setLeftClick(view -> getSupportDelegate().pop());
        cpTitle=chatTitleBar.getCenterTitle();
        //单聊面板 标题下方功能栏
        mRecycleView=chatTitleBar.getmBottomRecycle();
        initBottomRecycle();

        this.mInputGroup=chatPanel.mInputGroup;
        mMsgHandler=mInputGroup.getMsgHandler();

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
        mTtsPopup.setmTtsPopuCallBack(new TtsPopuCallBack() {
            @Override
            public void TtsBack(String s) {
                chatPanel.sendChatMsg(s);
            }
        });

        chatPanel.setOnVoiceClickListener(new ChatBottomInputGroupCust.VoiceClickListener() {
            @Override
            public void onClick() {

                mTtsPopup.showPopupWindow();
            }
        });

    }
    private void initBottomRecycle() {
        mChatTitieBottomAdapter = new ChatTitieBottomAdapter(initTitleBottomData());
        mChatTitieBottomAdapter.setCartItemListener(initTitleBottomItemListener());
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        //调整RecyclerView的排列方向
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycleView.setLayoutManager(manager);
        mRecycleView.setAdapter(mChatTitieBottomAdapter);
    }

        private TitleBottomItemListener initTitleBottomItemListener() {
        mTitleBottomItemListener = (type, name) -> {
            Log.d("jialei","type:"+type);
            if (type == null) {
                return;
            }
            if (TextUtils.equals(type, TitleBottomChildType.AUDIOCALL.name())) {
                final Intent intent2 = new Intent(getContext(), CallWaitingActivity.class);
                int userId = (YjDatabaseManager.getInstance().getDao().loadAll().get(0).getId()).intValue();
                intent2.putExtra("roomid",userId);
                intent2.putExtra("chatId",chatId);
                intent2.putExtra(CallWaitingActivity.TYPE_KEY,CallWaitingActivity.TYPE_VIDEO);
                getActivity().startActivity(intent2);
//                BokangSendMessageUtil  bokangSendMessageUtil = new BokangSendMessageUtil(TIMManager.getInstance().getConversation(TIMConversationType.C2C, chatId), new BoKangSendMessageListener() {
//                    @Override
//                    public void messageSuccess(TIMMessage timMessage) {
//
//                    }
//
//                    @Override
//                    public void messageError(int code, String desc) {
//
//                    }
//                }, getContext());
                bokangSendMessageUtil.sendOnLineMessage(bokangSendMessageUtil.buildBokangMessage(MessageInfoUtil.BOKANG_VIDEO_WAIT,userId+""));
//                if (mMsgHandler != null) {
//                    mMsgHandler.sendMessage(MessageInfoUtil.buildBokangMessage(MessageInfoUtil.BOKANG_VIDEO_WAIT, userId + ""));
//                }
//                String sig = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getTencentImUserSig();
//                String userId = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getTencentImUserId();
//                final Intent intent = new Intent(getContext(), TRTCMainActivity.class);
//                intent.putExtra("roomId", 100);
//                intent.putExtra("userId", userId);
//                intent.putExtra("sdkAppId", TrtcConfig.SDKAPPID);
//                intent.putExtra("userSig", sig);
//                //TODO 需要指定会话ID（即聊天对象的identify，具体可参考IMSDK接入文档）
//                intent.putExtra("chatId", chatId);
//                getActivity().startActivity(intent);
//                //TODO 自定义消息
//                if (mMsgHandler != null)
//                    mMsgHandler.sendMessage(MessageInfoUtil.buildBokangMessage("aaaa"));
            }
        };
        return mTitleBottomItemListener;
    }

    private ArrayList<MultipleItemEntity> initTitleBottomData(){
        final ArrayList<MultipleItemEntity> data =
                new TitleBottomPersonalChatDataConverter()
                        .convert();
        return data;
    }
}
