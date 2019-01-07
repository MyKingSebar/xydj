package com.example.latte.ui.launcher;


import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.bigkoo.convenientbanner.holder.Holder;
import com.example.latte.ui.R;

public class LauncherHolder extends Holder<Integer> {

    private AppCompatImageView mImageView;//注意这里不能=null

    public LauncherHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        mImageView = itemView.findViewById(R.id.ivPost);
    }

    @Override
    public void updateUI(Integer data) {
        mImageView.setBackgroundResource(data);
    }
}
