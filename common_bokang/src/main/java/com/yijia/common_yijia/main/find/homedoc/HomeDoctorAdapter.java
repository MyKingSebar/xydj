package com.yijia.common_yijia.main.find.homedoc;

import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.widget.ImageView;

import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.recycler.MultipleRecyclerAdapter;
import com.example.latte.ui.recycler.MultipleViewHolder;
import com.example.yijia.app.Latte;
import com.example.yijia.ui.TextViewUtils;
import com.example.yijia.util.GlideUtils;
import com.yijia.common_yijia.main.friends.CommonClickListener;
import com.yijia.common_yijia.main.index.YjIndexItemType;
import com.yijia.common_yijia.main.index.YjIndexMultipleFields;

import java.util.List;


public final class HomeDoctorAdapter extends MultipleRecyclerAdapter {
    CommonClickListener commonClickListener = null;

    public void setCommonClickListener(CommonClickListener commonClickListener) {
        this.commonClickListener = commonClickListener;
    }

    public HomeDoctorAdapter(List<MultipleItemEntity> data) {
        super(data);
        //添加item布局
        addItemType(YjIndexItemType.FIND_HOMEDOCTOR_ITEM, R.layout.item_find_homedoctor);
    }


    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case YjIndexItemType.FIND_HOMEDOCTOR_ITEM:

                //先取出所有值
                final Long doctTeamId = entity.getField(HomeDoctorMultipleFields.DOCTTEAMID);
                final Long hospitalId = entity.getField(HomeDoctorMultipleFields.HOSPITALID);
                final Long idCardInfoId = entity.getField(HomeDoctorMultipleFields.IDCARDINFOID);
                final String doctTeamName = entity.getField(HomeDoctorMultipleFields.DOCTTEAMNAME);
                final String hospitalName = entity.getField(HomeDoctorMultipleFields.HOSPITALNAME);
                final String idCardInfoName = entity.getField(HomeDoctorMultipleFields.IDCARDINFONAME);
                final String headImage = entity.getField(HomeDoctorMultipleFields.HEADIMAGE);

                //取出所以控件
                final AppCompatTextView tvName = holder.getView(R.id.tv_name);
                final AppCompatTextView tvHname = holder.getView(R.id.tv_d_liveness);
                final ImageView ivImg = holder.getView(R.id.iv_img);
                final ConstraintLayout cl = holder.getView(R.id.cl);
                cl.setOnClickListener(v -> {
                    if (commonClickListener != null) {
                        commonClickListener.commonClick(doctTeamId + "");
                    }
                });
                //赋值
                TextViewUtils.AppCompatTextViewSetText(tvName, doctTeamName);
                TextViewUtils.AppCompatTextViewSetText(tvHname, hospitalName);
                GlideUtils.load(Latte.getApplicationContext(), headImage, ivImg, GlideUtils.DEFAULTMODE);
                break;

            default:
                break;
        }
    }
}
