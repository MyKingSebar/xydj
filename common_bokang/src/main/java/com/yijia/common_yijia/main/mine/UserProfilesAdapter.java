package com.yijia.common_yijia.main.mine;

import android.support.v7.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.recycler.MultipleRecyclerAdapter;
import com.example.latte.ui.recycler.MultipleViewHolder;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.util.GlideUtils;
import com.yijia.common_yijia.main.index.YjIndexItemType;
import com.yijia.common_yijia.main.index.YjIndexMultipleFields;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public final class UserProfilesAdapter extends MultipleRecyclerAdapter {
    private LatteDelegate mDelegate = null;
    private GuardianshipListener mGuardianshipListener = null;

    public void setmGuardianshipListener(GuardianshipListener mGuardianshipListener) {
        this.mGuardianshipListener = mGuardianshipListener;
    }

    UserProfilesAdapter(List<MultipleItemEntity> data, LatteDelegate delegate) {
        super(data);
        //添加item布局
        addItemType(YjIndexItemType.GUARDUABSHIPLIST_ITEM, R.layout.item_guardianship);
        mDelegate = delegate;
    }

    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case YjIndexItemType.GUARDUABSHIPLIST_ITEM:
                //先取出所有值
                final long friendUserId = entity.getField(MultipleFields.ID);
                final String tencentImUserId = entity.getField(MultipleFields.TENCENTIMUSERID);
                final String userHead = entity.getField(MultipleFields.IMAGE_URL);
                final int isMain = entity.getField(YjIndexMultipleFields.ISMAIN);
                final String realName = entity.getField(YjIndexMultipleFields.USER_REAL_NAME);
                final String nickname = entity.getField(YjIndexMultipleFields.USER_NICK_NAME);

                //取出所以控件
                final CircleImageView imageView = holder.getView(R.id.im);
                final AppCompatTextView tvName = holder.getView(R.id.tv_name);
                final AppCompatTextView tvDelete = holder.getView(R.id.tv_add);

                //赋值
                tvName.setText(nickname);
                Glide.with(mDelegate)
                        .load(userHead)
                        .apply(GlideUtils.USEROPTIONS)
                        .into(imageView);
                tvDelete.setOnLongClickListener(v -> {
                    if (mGuardianshipListener != null) {
                        mGuardianshipListener.onitemclick(friendUserId);
                    }
                    return false;
                });


                break;

            default:
                break;
        }
    }
}
