package com.yijia.common_yijia.main.robot.callsetting;

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
import com.yijia.common_yijia.main.friends.CommonClickLongStringListener;
import com.yijia.common_yijia.main.index.YjIndexItemType;
import com.yijia.common_yijia.main.index.YjIndexMultipleFields;

import java.util.List;


public final class RobotCallSettingFriendListAdapter extends MultipleRecyclerAdapter {
    CommonClickLongStringListener mRobotGuardianshipListener = null;

    public void setCommonClickListener(CommonClickLongStringListener mRobotGuardianshipListener) {
        this.mRobotGuardianshipListener = mRobotGuardianshipListener;
    }

    public RobotCallSettingFriendListAdapter(List<MultipleItemEntity> data) {
        super(data);
        //添加item布局
        addItemType(YjIndexItemType.ROBOT_CALLSETTING_FRIENDLIST, R.layout.item_robot_callsetting_friend);
    }


    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case YjIndexItemType.ROBOT_CALLSETTING_FRIENDLIST:

                //先取出所有值
                final Long friendUserId = entity.getField(RobotCallSettingListFields.FRIENDUSERID);
                final String tencentImUserId = entity.getField(RobotCallSettingListFields.TENCENTIMUSERID);
                final String nickname = entity.getField(RobotCallSettingListFields.NICKNAME);
                final String userHead = entity.getField(RobotCallSettingListFields.USERHEAD);
                final String realName = entity.getField(RobotCallSettingListFields.REALNAME);
                final int userStatus = entity.getField(RobotCallSettingListFields.USERSTATUS);
                final String sb = entity.getField(RobotCallSettingListFields.FRIENDNICKNAMES);

                //取出所以控件
                final AppCompatTextView tvName = holder.getView(R.id.tv_name);
                final ImageView tvHasrobot = holder.getView(R.id.iv_hasrobot);
                final ImageView iv_img = holder.getView(R.id.iv_img);
                final AppCompatTextView nickness = holder.getView(R.id.tv_d_liveness);
                final ConstraintLayout cl = holder.getView(R.id.cl);
                cl.setOnClickListener(v -> {
                    if (mRobotGuardianshipListener != null) {
                        mRobotGuardianshipListener.commonLongStringClick(friendUserId ,nickname);
                    }
                });
                //赋值
                TextViewUtils.AppCompatTextViewSetText(tvName, nickname);
                if(sb.length()!=0){
                    TextViewUtils.AppCompatTextViewSetText(nickness, "昵称："+sb );
                }
                GlideUtils.load(Latte.getApplicationContext(), userHead, iv_img, GlideUtils.USERMODE);
                break;

            default:
                break;
        }
    }
}
