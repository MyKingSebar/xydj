package com.bokang.common_im.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.bokang.common_im.R;

public class ConversationActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delegate_imconversation);
    }
}