package com.example.session;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.common_tencent_tuikit.Constants;
import com.example.common_tencent_tuikit.R;
import com.example.common_tencent_tuikit.chat.GroupChatFragment;
import com.example.common_tencent_tuikit.chat.PersonalChatFragment;
import com.example.latte.delegates.LatteDelegate;
import com.tencent.qcloud.uikit.business.session.model.SessionInfo;
import com.tencent.qcloud.uikit.business.session.view.SessionPanel;
import com.tencent.qcloud.uikit.business.session.view.wedgit.SessionClickListener;


public class SessionFragment extends LatteDelegate implements SessionClickListener {
    private View baseView;
    private SessionPanel sessionPanel;

    LatteDelegate mCurrentFragment = null;

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
        // 会话面板初始化默认功能
        sessionPanel.initDefault();
        // 这里设置会话列表点击的跳转逻辑，告诉添加完SessionPanel后会话被点击后该如何处理
        sessionPanel.setSessionClick(this);

    }

    @Override
    public void onSessionClick(SessionInfo session) {
        //此处为demo的实现逻辑，更根据会话类型跳转到相关界面，开发者可根据自己的应用场景灵活实现
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_DATA, session.getPeer());
        if (session.isGroup()) {
            //如果是群组，跳转到群聊界面
            mCurrentFragment = new GroupChatFragment();
            mCurrentFragment.setArguments(bundle);
            getParentDelegate().getSupportDelegate().start(mCurrentFragment);
//            ChatActivity.startGroupChat(getActivity(), session.getPeer());
        } else {
            //否则跳转到C2C单聊界面

            mCurrentFragment = new PersonalChatFragment();
            mCurrentFragment.setArguments(bundle);
            getParentDelegate().getSupportDelegate().start(mCurrentFragment);
//            ChatActivity.startC2CChat(getActivity(), session.getPeer());

        }
    }
}
