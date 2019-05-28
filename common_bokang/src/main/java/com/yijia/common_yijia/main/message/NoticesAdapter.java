package com.yijia.common_yijia.main.message;

import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.recycler.MultipleRecyclerAdapter;
import com.example.latte.ui.recycler.MultipleViewHolder;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.util.GlideUtils;
import com.example.yijia.util.TimeFormat;
import com.yijia.common_yijia.main.index.IIndexItemListener;
import com.yijia.common_yijia.main.index.YjIndexItemType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public final class NoticesAdapter extends MultipleRecyclerAdapter {
    private LatteDelegate mDelegate = null;
    OkAddLisener mOkAddLisener = null;


    private int[] icons = {R.mipmap.icon_notice1, R.mipmap.icon_notice2, R.mipmap.icon_notice3, R.mipmap.icon_notice4, R.mipmap.icon_notice5, R.mipmap.icon_notice6, R.mipmap.icon_notice7, R.mipmap.icon_notice8,
            R.mipmap.icon_notice9, R.mipmap.icon_notice9, R.mipmap.icon_notice9, R.mipmap.icon_notice9, R.mipmap.icon_notice9, R.mipmap.icon_notice9, R.mipmap.icon_notice9};

    public NoticesAdapter(List<MultipleItemEntity> data, LatteDelegate delegate) {
        super(data);
        //添加item布局
        addItemType(YjIndexItemType.INDEX_NOTICELIST_ITEM, R.layout.item_notices);
        mDelegate = delegate;
    }

    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case YjIndexItemType.INDEX_NOTICELIST_ITEM:
                //先取出所有值
                final Long id = entity.getField(MultipleFields.ID);
                final Long targetUserId = entity.getField(NoticeMultipleFields.TARGETUSERID);
                final int puthTypeId = entity.getField(NoticeMultipleFields.PUTHTYPEID);
                final String title = entity.getField(NoticeMultipleFields.TITLE);
                final String content = entity.getField(NoticeMultipleFields.CONTENT);
                final Long jumpId = entity.getField(NoticeMultipleFields.JUMPID);
                final Long isRead = entity.getField(NoticeMultipleFields.ISREAD);
                final String createdTime = entity.getField(NoticeMultipleFields.CREATEDTIME);
                final String modifiedTime = entity.getField(NoticeMultipleFields.MODIFIEDTIME);
                final String imagePath = entity.getField(MultipleFields.IMAGE_URL);

                final Long friendApplyId = entity.getField(NoticeMultipleFields.FRIENDAPPLYID);
                final int isAgree = entity.getField(NoticeMultipleFields.ISAGREE);

                //取出所以控件
                final AppCompatTextView tvName = holder.getView(R.id.tv_name);
                final AppCompatTextView tvTime = holder.getView(R.id.tv_time);
                final AppCompatTextView tvContent = holder.getView(R.id.tv_content);
                final AppCompatTextView add = holder.getView(R.id.tv_add);
                final ImageView im = holder.getView(R.id.im);

                //赋值
                GlideUtils.load(mContext,imagePath,im,GlideUtils.DEFAULTMODE);
//                im.setImageResource(icons[puthTypeId - 1]);
//                Glide.with(mContext)
//                        .load(icons[puthTypeId - 1])
//                        .apply(GlideUtils.USEROPTIONS)
//                        .into(im);
                tvName.setText(title);
                tvContent.setText(content);
                if (puthTypeId == 9) {

                    tvTime.setVisibility(View.GONE);
                    add.setVisibility(View.VISIBLE);
                    add.setOnClickListener(v -> {
                        if (mOkAddLisener != null) {
                            mOkAddLisener.ok(friendApplyId);
                            add.setBackgroundResource(R.mipmap.button_has_add_friend);
                            add.setClickable(false);
                        }
                    });
                    switch (isAgree) {
                        ///是否同意，0-初始化，1-已同意，2-已拒绝
                        case 0:
                            add.setBackgroundResource(R.mipmap.button_ok_add_friend);
                            add.setClickable(true);
                            break;
                        case 1:
                            add.setBackgroundResource(R.mipmap.button_has_add_friend);
                            add.setClickable(false);
                            break;
                        case 2:
                            add.setBackgroundResource(R.mipmap.button_has_add_friend);
                            add.setClickable(false);
                            break;
                        default:
                            break;
                    }
                } else {
                    tvTime.setVisibility(View.VISIBLE);
                    add.setVisibility(View.GONE);
                    DateFormat df = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
                    try {
                        Date createdTime_d = df.parse(createdTime);
                        Log.d("jialei","createdTime_d："+(createdTime_d.getYear()+- 1900)+","+createdTime_d.getMonth()+","+createdTime_d.getDay());
                        String time= TimeFormat.getCompareNowString2(createdTime_d);
                        tvTime.setText(time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:
                break;
        }
    }

    public void setOkAddListener(OkAddLisener listener) {
        mOkAddLisener = listener;
    }

}
