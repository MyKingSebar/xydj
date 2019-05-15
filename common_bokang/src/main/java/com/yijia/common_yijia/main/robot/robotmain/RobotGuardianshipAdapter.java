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
import com.yijia.common_yijia.main.index.YjIndexMultipleFields;

import java.util.List;

/**
 * @author JiaLei, Email 15033111957@163.com, Date on 2019/5/13.
 * 
 *            .,,       .,:;;iiiiiiiii;;:,,.     .,,                   
 *          rGB##HS,.;iirrrrriiiiiiiiiirrrrri;,s&##MAS,                
 *         r5s;:r3AH5iiiii;;;;;;;;;;;;;;;;iiirXHGSsiih1,               
 *            .;i;;s91;;;;;;::::::::::::;;;;iS5;;;ii:                  
 *          :rsriii;;r::::::::::::::::::::::;;,;;iiirsi,               
 *       .,iri;;::::;;;;;;::,,,,,,,,,,,,,..,,;;;;;;;;iiri,,.           
 *    ,9BM&,            .,:;;:,,,,,,,,,,,hXA8:            ..,,,.       
 *   ,;&@@#r:;;;;;::::,,.   ,r,,,,,,,,,,iA@@@s,,:::;;;::,,.   .;.      
 *    :ih1iii;;;;;::::;;;;;;;:,,,,,,,,,,;i55r;;;;;;;;;iiirrrr,..       
 *   .ir;;iiiiiiiiii;;;;::::::,,,,,,,:::::,,:;;;iiiiiiiiiiiiri         
 *   iriiiiiiiiiiiiiiii;;;::::::::::::::::;;;iiiiiiiiiiiiiiiir;        
 *  ,riii;;;;;;;;;;;;;:::::::::::::::::::::::;;;;;;;;;;;;;;iiir.       
 *  iri;;;::::,,,,,,,,,,:::::::::::::::::::::::::,::,,::::;;iir:       
 * .rii;;::::,,,,,,,,,,,,:::::::::::::::::,,,,,,,,,,,,,::::;;iri       
 * ,rii;;;::,,,,,,,,,,,,,:::::::::::,:::::,,,,,,,,,,,,,:::;;;iir.      
 * ,rii;;i::,,,,,,,,,,,,,:::::::::::::::::,,,,,,,,,,,,,,::i;;iir.      
 * ,rii;;r::,,,,,,,,,,,,,:,:::::,:,:::::::,,,,,,,,,,,,,::;r;;iir.      
 * .rii;;rr,:,,,,,,,,,,,,,,:::::::::::::::,,,,,,,,,,,,,:,si;;iri       
 *  ;rii;:1i,,,,,,,,,,,,,,,,,,:::::::::,,,,,,,,,,,,,,,:,ss:;iir:       
 *  .rii;;;5r,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,sh:;;iri        
 *   ;rii;:;51,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,.:hh:;;iir,        
 *    irii;::hSr,.,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,.,sSs:;;iir:         
 *     irii;;:iSSs:.,,,,,,,,,,,,,,,,,,,,,,,,,,,..:135;:;;iir:          
 *      ;rii;;:,r535r:...,,,,,,,,,,,,,,,,,,..,;sS35i,;;iirr:           
 *       :rrii;;:,;1S3Shs;:,............,:is533Ss:,;;;iiri,            
 *        .;rrii;;;:,;rhS393S55hh11hh5S3393Shr:,:;;;iirr:              
 *          .;rriii;;;::,:;is1h555555h1si;:,::;;;iirri:.               
 *            .:irrrii;;;;;:::,,,,,,,,:::;;;;iiirrr;,                  
 *               .:irrrriiiiii;;;;;;;;iiiiiirrrr;,.                    
 *                  .,:;iirrrrrrrrrrrrrrrrri;:.                        
 *                        ..,:::;;;;:::,,.    
 */
public final class RobotGuardianshipAdapter extends MultipleRecyclerAdapter {
    CommonStringClickListener mRobotGuardianshipListener = null;

    public void setmRobotGuardianshipListener(CommonStringClickListener mRobotGuardianshipListener) {
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
                final Long friendUserId = entity.getField(MultipleFields.ID);
                final String headImage = entity.getField(MultipleFields.IMAGE_URL);
                //主监护人：1是2否
                final int isMain = entity.getField(YjIndexMultipleFields.ISMAIN);
                final String realName = entity.getField(YjIndexMultipleFields.USER_REAL_NAME);
                final String nickname = entity.getField(YjIndexMultipleFields.USER_NICK_NAME);
                final int activeness = entity.getField(YjIndexMultipleFields.ACTIVENESS);
                //是否有机器人：1-是，-2否
                final int hasRobot = entity.getField(YjIndexMultipleFields.HASROBOT);

                //取出所以控件
                final AppCompatTextView tvName = holder.getView(R.id.tv_name);
                final AppCompatTextView tvLiveness = holder.getView(R.id.tv_liveness);
                final ImageView ivImg = holder.getView(R.id.iv_img);
                final ImageView ivHasrobot = holder.getView(R.id.iv_hasrobot);
                final ConstraintLayout cl = holder.getView(R.id.cl);
                cl.setOnClickListener(v -> {
                    if (mRobotGuardianshipListener != null) {
                        mRobotGuardianshipListener.commonClick(friendUserId + "");
                    }
                });
                //赋值
                TextViewUtils.AppCompatTextViewSetText(tvName, nickname);
                TextViewUtils.AppCompatTextViewSetText(tvLiveness, activeness + "");
                if (activeness <= 20) {
                    tvLiveness.setTextColor(ContextCompat.getColor(Latte.getApplicationContext(), R.color.text_E8635D));
                } else if (activeness <= 80) {
                    tvLiveness.setTextColor(ContextCompat.getColor(Latte.getApplicationContext(), R.color.text_3DC39A));
                } else if (activeness <= 100) {
                    tvLiveness.setTextColor(ContextCompat.getColor(Latte.getApplicationContext(), R.color.text_EC8B2F));
                }
                GlideUtils.load(Latte.getApplicationContext(), headImage, ivImg, GlideUtils.DEFAULTMODE);
                break;

            default:
                break;
        }
    }
}
