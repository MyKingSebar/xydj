package com.yijia.common_yijia.main.message.trtc2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.common_tencent_tuikit.Constants;
import com.example.common_tencent_tuikit.chat.title_bottom.ChatTitieBottomAdapter;
import com.example.common_tencent_tuikit.chat.title_bottom.TitleBottomChildType;
import com.example.common_tencent_tuikit.chat.title_bottom.TitleBottomItemListener;
import com.example.common_tencent_tuikit.chat.title_bottom.TitleBottomPersonalChatDataConverter;
import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.dialog.JDialogUtil;
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
import com.yijia.common_yijia.main.mine.ExtraString;
import com.yijia.common_yijia.main.mine.SHCallingActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by valxehuang on 2018/7/30.
 */

public class PersonalChatFragment2 extends LatteDelegate {
    private String TAG="PersonalChatFragment2";
    private C2CChatPanel chatPanel;
    private PageTitleBar chatTitleBar;
    private String chatId, chatName;

    private TextView cpTitle,cpRingt,cpLeft;

    private RecyclerView mRecycleView=null;
    private ChatTitieBottomAdapter mChatTitieBottomAdapter=null;
    private TitleBottomItemListener mTitleBottomItemListener=null;
    private ChatBottomInputGroupCust mInputGroup;
    private ChatBottomInputGroupCust.MessageHandler mMsgHandler;
    BokangSendMessageUtil bokangSendMessageUtil=null;
    private TtsPopup mTtsPopup;

    public static PersonalChatFragment2 create(String chatId,String chatName,String chatHeadUrl) {
        final Bundle args = new Bundle();
        args.putString(Constants.INTENT_DATA,chatId);
        args.putString(Constants.INTENT_NAME,chatName);
        args.putString(Constants.INTENT_URL,chatHeadUrl);
        final PersonalChatFragment2 delegate = new PersonalChatFragment2();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public Object setLayout() {
        return R.layout.chat_fragment_personal;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initView(rootView);
        initView2(rootView);

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

    private void initView2(View rootView) {
        if(chatId==null){
            return;
        }
        List<String> users = new ArrayList<String>();
        users.add(chatId);
        TIMFriendshipManager.getInstance().getUsersProfile(users, false, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int code, String desc){
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                Log.e(TAG, "getUsersProfile failed: " + code + " desc");
            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                for(TIMUserProfile res : timUserProfiles){
                    cpTitle.setText(res.getNickName());
                    Log.e(TAG, "identifier: " + res.getIdentifier() + " nickName: " + res.getNickName());
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle datas = getArguments();
        //由会话列表传入的会话ID
        chatId = datas.getString(Constants.INTENT_DATA);
        chatName = datas.getString(Constants.INTENT_NAME);
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
        chatPanel.setBaseChatId(chatId, chatName);

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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager
                            .PERMISSION_GRANTED) {
                        //不具有获取权限，需要进行权限申请
                        requestPermissions(new String[]{
                                Manifest.permission.RECORD_AUDIO}, 1);
                        return;
                    }

                }

                mTtsPopup.showPopupWindow();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mTtsPopup.showPopupWindow();
        }

        if (requestCode == 11 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (shNumber != null) {
                startCall(shNumber, chatName, imageUrl);
            } else {
                Toast.makeText(getContext(), R.string.get_data_error, Toast.LENGTH_LONG).show();
            }
        }
    }

    private String shNumber, imageUrl;

    private void startCall(String phone, String name, String url) {
        Intent intent = new Intent(getContext(), SHCallingActivity.class);
        intent.putExtra(ExtraString.ISINCOME, false);
        intent.putExtra(ExtraString.PHONE_NUM, phone);
        intent.putExtra(ExtraString.PHONE_NAME, name);
        intent.putExtra(ExtraString.PHONE_URL, url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


//    private void getUserData() {
//        JDialogUtil.INSTANCE.showRxDialogShapeLoading(getContext());
//        String url = "friend/query_friend_info";
//        RxRestClient.builder()
//                .url(url)
//                .params("yjtk", YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk())
//                .params("friendUserId", userId)
//                .build()
//                .get()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserver<String>(getContext()) {
//                    @Override
//                    public void onResponse(String response) {
//                        final String status = JSON.parseObject(response).getString("status");
//                        if (TextUtils.equals(status, "1001")) {
//                            JSONObject object = JSON.parseObject(response).getJSONObject("data");
//                            if (null != object) {
//                                shNumber = object.getString("shNumber");
//                                imageUrl = "";
//                            }
//                        } else {
//                            final String msg = JSON.parseObject(response).getString("msg");
//                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
//                        }
//
//                        JDialogUtil.INSTANCE.dismiss();
//                    }
//
//                    @Override
//                    public void onFail(Throwable e) {
//                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                        JDialogUtil.INSTANCE.dismiss();
//                    }
//                });
//    }

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

            if (TextUtils.equals(type, TitleBottomChildType.PHONECALL.name())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{
                                Manifest.permission.RECORD_AUDIO}, 11);
                        return;
                    }
                }
                if (shNumber != null) {
                    startCall(shNumber, chatName, imageUrl);
                } else {
                    Toast.makeText(getContext(), R.string.get_data_error, Toast.LENGTH_LONG).show();
                }
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
