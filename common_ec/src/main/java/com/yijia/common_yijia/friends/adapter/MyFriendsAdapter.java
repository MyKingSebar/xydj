package com.yijia.common_yijia.friends.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.latte.ec.R;
import com.yijia.common_yijia.friends.bean.FriendsBean;

import java.util.ArrayList;
import java.util.List;

public class MyFriendsAdapter extends BaseQuickAdapter<FriendsBean, BaseViewHolder> {

    private Context context = null;
    private RequestOptions requestOptions = null;

    public MyFriendsAdapter(int layoutResId, @Nullable List<FriendsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendsBean item) {
        helper.setText(R.id.nickname, item.getNickname());
        ImageView userHead = helper.getView(R.id.userHead);
        if (requestOptions == null) {
            requestOptions = RequestOptions.circleCropTransform()
                    // .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true);
        }
        Glide.with(mContext)
                .load(item.getUserHead())
                .apply(requestOptions)
                .into(userHead);
    }
}
