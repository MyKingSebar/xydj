package com.yijia.common_yijia.main.friends.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.latte.ec.R;
import com.example.latte.ui.widget.RobotImageView;
import com.example.yijia.util.GlideUtils;
import com.yijia.common_yijia.main.friends.CommonStringClickListener;
import com.yijia.common_yijia.main.friends.bean.FriendsBean;

import java.util.List;

public class MyFriendsAdapter extends BaseQuickAdapter<FriendsBean, BaseViewHolder> {
    private RequestOptions requestOptions = null;
    private CommonStringClickListener mCommonClickListener = null;

    public void setmCommonClickListener(CommonStringClickListener mCommonClickListener) {
        this.mCommonClickListener = mCommonClickListener;
    }

    public MyFriendsAdapter(int layoutResId, @Nullable List<FriendsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendsBean item) {
        helper.setText(R.id.nickname, item.getNickname());
        RobotImageView userHead = helper.getView(R.id.userHead);
//        if (requestOptions == null) {
//            requestOptions = RequestOptions.circleCropTransform()
//                    // .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true);
//        }
//        Glide.with(mContext)
//                .load(item.getUserHead())
//                .apply(requestOptions)
//                .into(userHead);
        GlideUtils.load(mContext,item.getUserHead(),userHead.userImageView(),GlideUtils.USERMODE);
        userHead.setRobotOnline(item.isOnline());
        helper.getView(R.id.ll_friend).setOnLongClickListener(v -> {
            if (mCommonClickListener != null) {
                mCommonClickListener.commonClick(item.getFriendUserId()+"");
            }
            return false;
        });

        helper.setText(R.id.relation, item.getRelation());
    }
}
