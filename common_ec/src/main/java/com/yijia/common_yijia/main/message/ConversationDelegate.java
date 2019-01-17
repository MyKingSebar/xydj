package com.yijia.common_yijia.main.message;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;

import butterknife.BindView;

public class ConversationDelegate extends LatteDelegate {

//    @BindView(R2.id.conversation)
//    Fragment conversation=null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_conversation;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
//        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
//                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase(Locale.US))
//                .appendQueryParameter("targetId", mTargetId).build();
//
//        fragment.setUri(uri);
    }
}
