package com.yijia.common_yijia.main.robot;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.ImageView;

import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.recycler.MultipleRecyclerAdapter;
import com.example.latte.ui.recycler.MultipleViewHolder;
import com.yijia.common_yijia.main.friends.CommonClickListener;
import com.yijia.common_yijia.main.index.YjIndexCommentListener;
import com.yijia.common_yijia.main.index.YjIndexCommentMultipleFields;
import com.yijia.common_yijia.main.index.YjIndexItemType;

import java.util.List;


public final class RobotGuardianshipAdapter extends MultipleRecyclerAdapter {
CommonClickListener mRobotGuardianshipListener=null;

    public void setmRobotGuardianshipListener(CommonClickListener mRobotGuardianshipListener) {
        this.mRobotGuardianshipListener = mRobotGuardianshipListener;
    }

    public RobotGuardianshipAdapter(List<MultipleItemEntity> data) {
        super(data);
        //添加item布局
        addItemType(YjIndexItemType.ROBOTGUARDUABSHIP_ITEM, R.layout.item_robot_guardianship);
    }


    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case YjIndexItemType.ROBOTGUARDUABSHIP_ITEM:
                //先取出所有值
                final Long commentId = entity.getField(YjIndexCommentMultipleFields.COMMENTID);
                final Long commentUserId = entity.getField(YjIndexCommentMultipleFields.COMMENTUSERID);
                final String commentUserNickname = entity.getField(YjIndexCommentMultipleFields.COMMENTUSERNICKNAME);
                final String commentUserRealName = entity.getField(YjIndexCommentMultipleFields.COMMENTUSERREALNAME);
                final String commentUserHead = entity.getField(YjIndexCommentMultipleFields.COMMENTUSERHEAD);
                final String commentContent = entity.getField(YjIndexCommentMultipleFields.COMMENTCONTENT);
                final int replyUserId = entity.getField(YjIndexCommentMultipleFields.REPLYUSERID);
                final String replyUserNickname = entity.getField(YjIndexCommentMultipleFields.REPLYUSERNICKNAME);
                final String replyUserRealName = entity.getField(YjIndexCommentMultipleFields.REPLYUSERREALNAME);
                final String commentCreatedTime = entity.getField(YjIndexCommentMultipleFields.COMMENTCREATEDTIME);
                final int isOwnComment = entity.getField(YjIndexCommentMultipleFields.ISOWNCOMMENT);

                //取出所以控件
                final AppCompatTextView tvName = holder.getView(R.id.tv_name);
                final AppCompatTextView tvLiveness = holder.getView(R.id.tv_liveness);
                final ImageView ivImg = holder.getView(R.id.iv_img);
                final ImageView ivHasrobot = holder.getView(R.id.iv_hasrobot);
                final ConstraintLayout cl = holder.getView(R.id.cl);
                cl.setOnClickListener(v->{
                    if(mRobotGuardianshipListener!=null){
//                        mRobotGuardianshipListener.commonClick();
                    }
                });

                //赋值
                tvName.setText(commentUserNickname + ":");
                StringBuffer comment = new StringBuffer();
                if (replyUserId != 0) {
                    comment.append("@").append(replyUserNickname).append(" ");
                }
                comment.append(commentContent);



                break;

            default:
                break;
        }
    }
}
