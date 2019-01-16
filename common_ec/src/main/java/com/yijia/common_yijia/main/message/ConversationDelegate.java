package com.yijia.common_yijia.main.message;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.R;

public class ConversationDelegate extends LatteDelegate {

    @Override
    public Object setLayout() {
        return R.layout.delegate_conversationlist;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }
}
