package com.yijia.common_yijia.main.message.trtc.session;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.example.common_tencent_tuikit.Constants;
import com.example.common_tencent_tuikit.R;
import com.example.common_tencent_tuikit.R2;
import com.yijia.common_yijia.main.find.TtsPopup;
import com.example.yijia.delegates.LatteDelegate;
import com.tencent.qcloud.bokang.session.view.BokangSessionPanel;
import com.tencent.qcloud.bokang.session.view.wedgit.BokangSessionListener;
import com.tencent.qcloud.uikit.business.session.model.SessionInfo;
import com.tencent.qcloud.uikit.business.session.view.wedgit.SessionClickListener;
import com.yijia.common_yijia.main.message.trtc2.GroupChatFragment2;
import com.yijia.common_yijia.main.message.trtc2.PersonalChatFragment2;

import butterknife.OnClick;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;


public class SessionFragment extends LatteDelegate implements SessionClickListener, BokangSessionListener {
    private BokangSessionPanel sessionPanel;
    private TtsPopup mTtsPopup = null;

    LatteDelegate mCurrentFragment = null;

    @OnClick(R2.id.tv_back)
    void back() {
        getSupportDelegate().pop();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_session;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initView(rootView);
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        baseView = inflater.inflate(R.layout.fragment_session, container, false);
//        initView();
//        return baseView;
//    }


    private void initView(View rootView) {
        // 获取会话列表组件，
        sessionPanel = rootView.findViewById(R.id.session_panel);
        sessionPanel.setmBokangSessionListener(this);
        // 会话面板初始化默认功能
        sessionPanel.initDefault();
        // 这里设置会话列表点击的跳转逻辑，告诉添加完SessionPanel后会话被点击后该如何处理
        sessionPanel.setSessionClick(this);
        sessionPanel.mTitleBar.setVisibility(View.GONE);
        initTtsPopu();
    }

    private void initTtsPopu() {
        mTtsPopup = new TtsPopup(getActivity());

    }

    @Override
    public void onSessionClick(SessionInfo session) {
        //此处为demo的实现逻辑，更根据会话类型跳转到相关界面，开发者可根据自己的应用场景灵活实现
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_DATA, session.getPeer());
        if (session.isGroup()) {
            //如果是群组，跳转到群聊界面
            mCurrentFragment = new GroupChatFragment2();
            mCurrentFragment.setArguments(bundle);
            getSupportDelegate().start(mCurrentFragment);
//            ChatActivity.startGroupChat(getActivity(), session.getPeer());
        } else {
            //否则跳转到C2C单聊界面

            mCurrentFragment = new PersonalChatFragment2();
            mCurrentFragment.setArguments(bundle);
            getSupportDelegate().start(mCurrentFragment);
//            ChatActivity.startC2CChat(getActivity(), session.getPeer());


        }
    }

    @Override
    public void sperkClick(String id, View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager
                    .PERMISSION_GRANTED) {
                //不具有获取权限，需要进行权限申请
                requestPermissions(new String[]{
                        Manifest.permission.RECORD_AUDIO}, 1);
                return;
            }

        }

        //TODO TTS
        Log.e("jialei", "view==null" + (view == null));
        mTtsPopup.setId(id);
        mTtsPopup.setPopupGravity(Gravity.CENTER);
        mTtsPopup.setAllowDismissWhenTouchOutside(false);
        mTtsPopup.showPopupWindow();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //    void showTtsPopup(String id,View view) {
//        QuickPopupBuilder.with(getContext())
//                .contentView(R.layout.basepopu_tts)
//                .config(new QuickPopupConfig()
//                        .clipChildren(true)
//                        .backgroundColor(Color.parseColor("#8C617D8A"))
////                        .withShowAnimation(enterAnimation)
////                        .withDismissAnimation(dismissAnimation)
//                        .gravity(Gravity.LEFT | Gravity.CENTER_VERTICAL)
//                        .blurBackground(true, option -> option.setBlurRadius(6)
//                                .setBlurPreScaleRatio(0.9f))
//                        .withClick(R.id.tv_ok, v1 -> {
//
//
//                        }, true)
//                        .withClick(R.id.tv_cancel, v1 -> {
//
//
//                        }, true)
//                        )
//                .show(view);
//    }
}
