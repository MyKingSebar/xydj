package com.yijia.common_yijia.main.mine;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatTextView;

import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.recycler.MultipleRecyclerAdapter;
import com.example.latte.ui.recycler.MultipleViewHolder;
import com.example.yijia.delegates.LatteDelegate;
import com.yijia.common_yijia.main.friends.CommonClickListener;
import com.yijia.common_yijia.main.index.YjIndexItemType;
import com.yijia.common_yijia.main.index.YjIndexMultipleFields;

import java.util.List;


public final class UserProfilesAdapter extends MultipleRecyclerAdapter {
    private LatteDelegate mDelegate = null;
    private CommonClickListener mCommonClickListener=null;

    public void setmCommonClickListener(CommonClickListener mCommonClickListener) {
        this.mCommonClickListener = mCommonClickListener;
    }


    UserProfilesAdapter(List<MultipleItemEntity> data, LatteDelegate delegate) {
        super(data);
        //添加item布局
        addItemType(YjIndexItemType.USERPROFILESLIST_ITEM, R.layout.item_userprofiles);
        mDelegate = delegate;
    }

    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case YjIndexItemType.USERPROFILESLIST_ITEM:
                //先取出所有值
                final long friendUserId = entity.getField(MultipleFields.ID);
                final String nickname = entity.getField(YjIndexMultipleFields.USER_NICK_NAME);

                //取出所以控件
                final AppCompatTextView tvName = holder.getView(R.id.tv_name);
                final ConstraintLayout cl = holder.getView(R.id.cl);
                tvName.setText(nickname);
                cl.setOnClickListener(v -> {
                    if(mCommonClickListener!=null){
                        mCommonClickListener.commonClick(friendUserId+"");
                    }
                });



                break;

            default:
                break;
        }
    }
}
