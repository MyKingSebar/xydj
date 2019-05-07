package com.yijia.common_yijia.main.find.homedoc.doctorin_top;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.widget.RelativeLayout;

import com.example.common_tencent_tuikit.R;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.recycler.MultipleRecyclerAdapter;
import com.example.latte.ui.recycler.MultipleViewHolder;
import com.yijia.common_yijia.main.index.YjIndexItemType;

import java.util.List;


public final class DoctorInTitieAdapter extends MultipleRecyclerAdapter {

    private DoctorInTitleItemListener mTitleBottomItemListener = null;

    public DoctorInTitieAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(YjIndexItemType.DOCTORIN_GROUP_CHAT_TITLE, R.layout.item_im_chat_titlebottom);
    }


    public void setCartItemListener(DoctorInTitleItemListener listener) {
        this.mTitleBottomItemListener = listener;
    }


    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case YjIndexItemType.DOCTORIN_GROUP_CHAT_TITLE:
                //先取出所有值
                final String name = entity.getField(MultipleFields.NAME);
                final int imgUrl = entity.getField(MultipleFields.IMAGE_URL);
                final String childType = entity.getField(MultipleFields.CHILD_ITEM_TYPE);
                //取出所以控件
                final AppCompatImageView ivImage = holder.getView(R.id.iv_img);
                final AppCompatTextView tvName = holder.getView(R.id.tv_name);
                final RelativeLayout llc = holder.getView(R.id.llc);

                tvName.setText(name);
                tvName.setTextColor(ContextCompat.getColor(mContext, R.color.im_text_gray_66));
                ivImage.setBackgroundResource(imgUrl);

                llc.setOnClickListener(v -> {
                    if (mTitleBottomItemListener != null) {
//                        final Long id1 = entity.getField(MultipleFields.ID);
                        //TODO
                        mTitleBottomItemListener.onTitleBottomItemClick(childType,name);
                    }
                });


                break;
            default:
                break;
        }
    }
}
