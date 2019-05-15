package com.yijia.common_yijia.main.robot.robotmain;

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
import com.yijia.common_yijia.main.friends.CommonStringClickListener;
import com.yijia.common_yijia.main.index.YjIndexItemType;
import com.yijia.common_yijia.main.index.YjRobotListMultipleFields;

import java.util.List;


public final class RobotListAdapter extends MultipleRecyclerAdapter {
    CommonStringClickListener mRobotGuardianshipListener = null;

    public void setmRobotListClickListener(CommonStringClickListener mRobotGuardianshipListener) {
        this.mRobotGuardianshipListener = mRobotGuardianshipListener;
    }

    public RobotListAdapter(List<MultipleItemEntity> data) {
        super(data);
        //添加item布局
        addItemType(YjIndexItemType.ROBOT_MAIN_LIST, R.layout.item_robot_main_list);
    }


    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case YjIndexItemType.ROBOT_MAIN_LIST:
                //先取出所有值
                final long friendUserId = entity.getField(YjRobotListMultipleFields.MAINID);
                final String headImage = entity.getField(MultipleFields.IMAGE_URL);
                final String name = entity.getField(YjRobotListMultipleFields.MAINNAME);
                final String age = entity.getField(YjRobotListMultipleFields.AGE);
                final int online = entity.getField(YjRobotListMultipleFields.ONLINE);
                final String relationship = entity.getField(YjRobotListMultipleFields.RELATIONSHIP);
                final String record = entity.getField(YjRobotListMultipleFields.RECORD_NUM);

                //取出所以控件
                final ImageView imageView = holder.getView(R.id.iv_img);
                final AppCompatTextView tvName = holder.getView(R.id.tv_name);
                final AppCompatTextView tvAge = holder.getView(R.id.tv_age);
                final AppCompatTextView tvStatus = holder.getView(R.id.tv_robot_status);
                final AppCompatTextView tvRelativeship = holder.getView(R.id.tv_relationship);
                final AppCompatTextView tvRecord = holder.getView(R.id.tv_record);
                final ConstraintLayout cl = holder.getView(R.id.cl);
                cl.setOnClickListener(v -> {
                    if (mRobotGuardianshipListener != null) {
                        mRobotGuardianshipListener.commonClick(friendUserId + "");
                    }
                });
                //赋值
                TextViewUtils.AppCompatTextViewSetText(tvName, name);
                TextViewUtils.AppCompatTextViewSetText(tvAge, age);
                TextViewUtils.AppCompatTextViewSetText(tvStatus, online == 1 ? "小壹在家" : "小壹不在");
                TextViewUtils.AppCompatTextViewSetText(tvRelativeship, relationship);
                TextViewUtils.AppCompatTextViewSetText(tvRecord, record);
                GlideUtils.load(Latte.getApplicationContext(), headImage, imageView, GlideUtils.DEFAULTMODE);
                break;

            default:
                break;
        }
    }
}
