package com.yijia.common_yijia.main.index.friends;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.recycler.MultipleRecyclerAdapter;
import com.example.latte.ui.recycler.MultipleViewHolder;

import java.util.List;


public final class IndexFriendsAdapter extends MultipleRecyclerAdapter {

    private IFriendsItemListener mIndexFriendsItemListener = null;

    private static final RequestOptions OPTIONS = new RequestOptions()
            .placeholder(R.color.app_text_gray)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    public IndexFriendsAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(FriendsItemType.INDEX_FRIENDS_ITEM, R.layout.item_main_friends);
    }


    public void setCartItemListener(IFriendsItemListener listener) {
        this.mIndexFriendsItemListener = listener;
    }


    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case FriendsItemType.INDEX_FRIENDS_ITEM:
                //先取出所有值
                final Long id = entity.getField(MultipleFields.ID);
                final String name = entity.getField(MultipleFields.NAME);
                final String imgUrl = entity.getField(MultipleFields.IMAGE_URL);
                //取出所以控件
                final AppCompatImageView ivImage = holder.getView(R.id.iv_img);
                final AppCompatTextView tvName = holder.getView(R.id.tv_name);
                final RelativeLayout llc = holder.getView(R.id.llc);

                if(id!=0L){
                    //赋值
                    tvName.setText(name);
                    tvName.setTextColor(ContextCompat.getColor(mContext, R.color.app_text_dark));
                    Glide.with(mContext)
                            .load(imgUrl)
                            .apply(OPTIONS)
                            .into(ivImage);
                }else {
                    tvName.setText("邀请亲朋");
                    tvName.setTextColor(ContextCompat.getColor(mContext, R.color.app_text_gray));
                    Glide.with(mContext)
                            .load(R.mipmap.item_addfriends)
                            .apply(OPTIONS)
                            .into(ivImage);
                }

                llc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mIndexFriendsItemListener != null) {
                            final Long id = entity.getField(MultipleFields.ID);
                            mIndexFriendsItemListener.onFriendsItemClick(id);
                        }
                    }
                });


                break;
            default:
                break;
        }
    }
}
