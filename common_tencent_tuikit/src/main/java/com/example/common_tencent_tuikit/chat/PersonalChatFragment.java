package com.example.common_tencent_tuikit.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common_tencent_tuikit.Constants;
import com.example.common_tencent_tuikit.R;
import com.example.common_tencent_tuikit.chat.title_bottom.ChatTitieBottomAdapter;
import com.example.common_tencent_tuikit.chat.title_bottom.TitleBottomItemListener;
import com.example.common_tencent_tuikit.chat.title_bottom.TitleBottomPersonalChatDataConverter;
import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.tencent.qcloud.uikit.business.chat.c2c.view.C2CChatPanel;
import com.tencent.qcloud.uikit.common.component.titlebar.PageTitleBar;

import java.util.ArrayList;


public class PersonalChatFragment extends LatteDelegate {
    private View mBaseView;
    private C2CChatPanel chatPanel;
    private PageTitleBar chatTitleBar;
//    private ChatBottomInputGroup mInputGroup;
    private String chatId;

    private RecyclerView mRecycleView=null;
    private ChatTitieBottomAdapter mChatTitieBottomAdapter=null;
    private TitleBottomItemListener mTitleBottomItemListener=null;

    @Override
    public Object setLayout() {
        return R.layout.chat_fragment_personal;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initView(rootView);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle datas = getArguments();
        //由会话列表传入的会话ID
        chatId = datas.getString(Constants.INTENT_DATA);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        mBaseView = inflater.inflate(R.layout.chat_fragment_personal, container, false);
//        Bundle datas = getArguments();
//        //由会话列表传入的会话ID
//        chatId = datas.getString(Constants.INTENT_DATA);
//        return mBaseView;
//    }


    private void initView(View rootView) {

        //从布局文件中获取聊天面板组件
        chatPanel = rootView.findViewById(R.id.chat_panel);
        //单聊组件的默认UI和交互初始化
        chatPanel.initDefault();
        /*
         * 需要指定会话ID（即聊天对象的identify，具体可参考IMSDK接入文档）来加载聊天消息。在上一章节SessionClickListener中回调函数的参数SessionInfo对象中持有每一会话的会话ID，所以在会话列表点击时都可传入会话ID。
         * 特殊的如果用户应用不具备类似会话列表相关的组件，则需自行实现逻辑获取会话ID传入。
         */
        chatPanel.setBaseChatId(chatId);

        //获取单聊面板的标题栏
        chatTitleBar = chatPanel.getTitleBar();
        //单聊面板标记栏返回按钮点击事件，这里需要开发者自行控制
        chatTitleBar.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportDelegate().pop();
            }
        });
        //单聊面板 标题下方功能栏
        mRecycleView=chatTitleBar.getmBottomRecycle();
        initBottomRecycle();

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
        mTitleBottomItemListener= (id, rongId, name) -> {

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
