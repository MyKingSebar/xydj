package com.yijia.common_yijia.main.robot.callsetting;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;

import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.recycler.MultipleRecyclerAdapter;
import com.example.latte.ui.recycler.MultipleViewHolder;
import com.example.yijia.app.Latte;
import com.example.yijia.ui.TextViewUtils;
import com.example.yijia.util.GlideUtils;
import com.yijia.common_yijia.main.friends.CommonClickListener;
import com.yijia.common_yijia.main.index.YjIndexItemType;

import java.util.List;


public final class RobotCallSettingNickListAdapter extends MultipleRecyclerAdapter {
    CommonClickListener mDeleteListener = null;

    public void setmDeleteListener(CommonClickListener mDeleteListener) {
        this.mDeleteListener = mDeleteListener;
    }

    CommonClickListener mOkListener = null;

    public void setmOkListener(CommonClickListener mOkListener) {
        this.mOkListener = mOkListener;
    }


    public RobotCallSettingNickListAdapter(List<MultipleItemEntity> data) {
        super(data);
        //添加item布局
        addItemType(YjIndexItemType.ROBOT_CALLSETTING_NICKLIST, R.layout.item_robot_callsetting_nick);
        addItemType(YjIndexItemType.ROBOT_CALLSETTING_NICKLIST_ADD, R.layout.item_robot_callsetting_nick_add);
        addItemType(YjIndexItemType.ROBOT_CALLSETTING_NICKLIST_DESCRIBE, R.layout.item_robot_callsetting_nick_describe);
    }


    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case YjIndexItemType.ROBOT_CALLSETTING_NICKLIST:
                //先取出所有值
                final Long friendNicknameId = entity.getField(RobotCallSettingListFields.FRIENDNICKNAMEID);
                final String friendNickname = entity.getField(RobotCallSettingListFields.FRIENDNICKNAME);

                //取出所以控件
                final AppCompatTextView tvName = holder.getView(R.id.tv_name);
                final ConstraintLayout clDelete = holder.getView(R.id.cl_delete);
                clDelete.setOnClickListener(v -> {
                    if (mDeleteListener != null) {
                        mDeleteListener.commonClick(friendNicknameId + "");
                    }
                });
                //赋值
                TextViewUtils.AppCompatTextViewSetText(tvName, friendNickname);
                break;
            case YjIndexItemType.ROBOT_CALLSETTING_NICKLIST_ADD:
                final AppCompatTextView ok = holder.getView(R.id.tv_ok);
                final AppCompatEditText etName = holder.getView(R.id.et_name);
                ok.setOnClickListener(v -> {
                    if (mOkListener != null) {
                        mOkListener.commonClick(etName.getText().toString());
                    }
                });
                break;
            default:
                break;
        }
    }
}
