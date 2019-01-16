package com.yijia.common_yijia.friends.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.latte.ec.R;
import com.yijia.common_yijia.friends.bean.GuardianBean;

import java.util.List;

public class MyGuardianAdapter extends BaseQuickAdapter<GuardianBean,BaseViewHolder> {

    private RequestOptions requestOptions;

    public MyGuardianAdapter(int layoutResId, @Nullable List<GuardianBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GuardianBean item) {
        helper.setText(R.id.nickname,item.getNickname());
        ImageView helperView = helper.getView(R.id.headImage);
        if (requestOptions == null) {
            requestOptions = RequestOptions.circleCropTransform()
                    // .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true);
        }
        Glide.with(mContext)
                .load(item.getHeadImage())
                .apply(requestOptions)
                .into(helperView);
    }
}
