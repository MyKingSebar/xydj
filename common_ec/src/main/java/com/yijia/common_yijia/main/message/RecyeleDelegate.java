package com.yijia.common_yijia.main.message;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ec.detail.RecyclerImageAdapter;
import com.example.latte.ui.recycler.ItemType;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

import butterknife.BindView;


public class RecyeleDelegate extends LatteDelegate {


    @Override
    public Object setLayout() {
        return R.layout.delegate_recycleview;
    }

    private void initImages() {
    }


    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initImages();
    }
}
