package com.yijia.common_yijia.main.find.homedoc;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatTextView;
import android.widget.ImageView;

import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.recycler.MultipleRecyclerAdapter;
import com.example.latte.ui.recycler.MultipleViewHolder;
import com.example.yijia.app.Latte;
import com.example.yijia.ui.TextViewUtils;
import com.example.yijia.util.GlideUtils;
import com.yijia.common_yijia.main.friends.CommonStringClickListener;
import com.yijia.common_yijia.main.index.YjIndexItemType;

import java.util.List;


public final class DoctorInUseRecycAdapter extends MultipleRecyclerAdapter {

    private int position = 0;

    private CommonStringClickListener mCommonStringClickListener = null;

    public void setmCommonStringClickListener(CommonStringClickListener mCommonStringClickListener) {
        this.mCommonStringClickListener = mCommonStringClickListener;
    }

    public DoctorInUseRecycAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(YjIndexItemType.DOCTORIN_GROUP_CHAT_USE_RECYC, R.layout.item_tab_homedoctorin);
    }

    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case YjIndexItemType.DOCTORIN_GROUP_CHAT_USE_RECYC:
                //先取出所有值
                final String name = entity.getField(HomeDoctorInMultipleFields.DOCTNAME);
                final String imgUrl = entity.getField(HomeDoctorInMultipleFields.DOCTHEADIMAGE);
                final String text = entity.getField(HomeDoctorInMultipleFields.DOCTLEVELNAME);
                //取出所以控件
                final ImageView titleImg = holder.getView(R.id.title_img);
                final AppCompatTextView tv_title = holder.getView(R.id.tv_title);
                final AppCompatTextView tv_text = holder.getView(R.id.tv_text);
                final ConstraintLayout cl = holder.getView(R.id.cl);

                if(position == holder.getAdapterPosition()) {
                    tv_title.setTextColor(mContext.getResources().getColor(R.color.text_FDBA63));
                    tv_text.setTextColor(mContext.getResources().getColor(R.color.text_FDBA63));
                } else {
                    tv_title.setTextColor(mContext.getResources().getColor(R.color.main_text_black_dark));
                    tv_text.setTextColor(mContext.getResources().getColor(R.color.main_text_gary_99));
                }

                TextViewUtils.AppCompatTextViewSetText(tv_title, name);
                TextViewUtils.AppCompatTextViewSetText(tv_text, text);
                GlideUtils.load(Latte.getApplicationContext(), imgUrl, titleImg, GlideUtils.USERMODE);

                cl.setOnClickListener(v -> {
                    if (mCommonStringClickListener != null) {
//                        final Long id1 = entity.getField(MultipleFields.ID);
                        //TODO
                        mCommonStringClickListener.commonClick(entity.getField(HomeDoctorInMultipleFields.TENCENTIMID)+","+entity.getField(HomeDoctorInMultipleFields.DOCTNAME)+","+entity.getField(HomeDoctorInMultipleFields.DOCTHEADIMAGE)+","+entity.getField(HomeDoctorInMultipleFields.MAJOR)+","+holder.getAdapterPosition());
                    }
                });


                break;
            default:
                break;
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
