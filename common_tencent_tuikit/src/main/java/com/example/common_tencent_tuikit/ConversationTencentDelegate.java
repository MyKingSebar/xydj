package com.example.common_tencent_tuikit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.latte.delegates.LatteDelegate;
import com.tencent.qcloud.uikit.business.chat.c2c.view.C2CChatPanel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ConversationTencentDelegate extends LatteDelegate {
    private String identifier;
    private String nickName;

//    @BindView(R2.id.text_chat)
//    TextView text_chat=null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        identifier=args.getString("identifier");
        nickName=args.getString("nickname");
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_conversationtencent;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
//        text_chat.setText(111+"");
        String s="1";
        initview(rootView);
    }

    private void initview(View rootView) {
        //从布局文件中获取聊天面板组件
        C2CChatPanel chatPanel = rootView.findViewById(R.id.chat_panel);
        //单聊组件的默认UI和交互初始化
        chatPanel.initDefault();
        chatPanel.mTitleBar.mCenterTitle.setText(nickName);
        /*
         * 需要指定会话ID（即聊天对象的identify，具体可参考IMSDK接入文档）来加载聊天消息。在上一章节SessionClickListener中回调函数的参数SessionInfo对象中持有每一会话的会话ID，所以在会话列表点击时都可传入会话ID。
         * 特殊的如果用户应用不具备类似会话列表相关的组件，则需自行实现逻辑获取会话ID传入。
         */
        chatPanel.setBaseChatId(identifier);
    }




}
