package com.yijia.common_yijia.main.mine;

import android.support.v7.widget.AppCompatTextView;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.recycler.MultipleRecyclerAdapter;
import com.example.latte.ui.recycler.MultipleViewHolder;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.util.GlideUtils;
import com.example.yijia.util.TimeFormat;
import com.yijia.common_yijia.main.index.YjIndexItemType;
import com.yijia.common_yijia.main.index.YjIndexMultipleFields;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public final class UserRecentBehaviorAdapter extends MultipleRecyclerAdapter {
private LatteDelegate mDelegate=null;
    UserRecentBehaviorAdapter(List<MultipleItemEntity> data, LatteDelegate delegate) {
        super(data);
        //添加item布局
        addItemType(YjIndexItemType.INDEX_RECENTBEHAVIOR_ITEM, R.layout.item_recentbehavior);
        mDelegate=delegate;
    }

    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case YjIndexItemType.INDEX_RECENTBEHAVIOR_ITEM:
                //先取出所有值

                final String description = entity.getField(RecentBehaviorMultipleFields.DESCRIPTION);
                final String createdTime = entity.getField(RecentBehaviorMultipleFields.CREATEDTIME);

                //取出所以控件
                final AppCompatTextView tvName = holder.getView(R.id.tv_name);
                final AppCompatTextView tvTime = holder.getView(R.id.tv_time);

                //赋值
                tvName.setText(description);
                DateFormat df = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
                try {
                    Date createdTime_d = df.parse(createdTime);
                    Log.d("jialei","createdTime_d："+(createdTime_d.getYear()+- 1900)+","+createdTime_d.getMonth()+","+createdTime_d.getDay());
                    String time= TimeFormat.getCompareNowString(createdTime_d);
                    tvTime.setText(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;

            default:
                break;
        }
    }
}
