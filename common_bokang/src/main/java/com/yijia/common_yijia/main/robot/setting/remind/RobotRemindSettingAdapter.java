package com.yijia.common_yijia.main.robot.setting.remind;

import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.recycler.MultipleRecyclerAdapter;
import com.example.latte.ui.recycler.MultipleViewHolder;
import com.example.yijia.app.Latte;
import com.example.yijia.ui.TextViewUtils;
import com.example.yijia.util.GlideUtils;
import com.yijia.common_yijia.main.friends.CommonClickLongStringListener;
import com.yijia.common_yijia.main.friends.CommonStringClickListener;
import com.yijia.common_yijia.main.friends.CommonLongClickListener;
import com.yijia.common_yijia.main.index.YjIndexItemType;

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
public final class RobotRemindSettingAdapter extends MultipleRecyclerAdapter {
    CommonLongClickListener mRemindSettingListener = null;
    CommonLongClickListener mDeleteListener = null;
    CommonClickLongStringListener mOpenListener = null;
//    public static final String OPEN="open";
//    public static final String CLOSE="close";

    public void setmRemindSettingListener(CommonLongClickListener mRemindSettingListener) {
        this.mRemindSettingListener = mRemindSettingListener;
    }

    public void setmDeleteListener(CommonLongClickListener mDeleteListener) {
        this.mDeleteListener = mDeleteListener;
    }

    public void setmOpenListener(CommonClickLongStringListener mOpenListener) {
        this.mOpenListener = mOpenListener;
    }

    public RobotRemindSettingAdapter(List<MultipleItemEntity> data) {
        super(data);
        //添加item布局
        addItemType(YjIndexItemType.ROBOT_REMINDSETTING, R.layout.item_robot_remind_setting);
    }


    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case YjIndexItemType.ROBOT_REMINDSETTING:

                //先取出所有值
                final Long remindId = entity.getField(RobotRemindSettingMultipleFields.REMINDID);
                final String tag = entity.getField(RobotRemindSettingMultipleFields.TAG);
                final String whatDay = entity.getField(RobotRemindSettingMultipleFields.WHATDAY);
                final String startTime = entity.getField(RobotRemindSettingMultipleFields.STARTTIME);
                final String endTime = entity.getField(RobotRemindSettingMultipleFields.ENDTIME);
                final String title = entity.getField(RobotRemindSettingMultipleFields.TITLE);
                final String description = entity.getField(RobotRemindSettingMultipleFields.DESCRIPTION);
                //1是2否
                final int isOpen = entity.getField(RobotRemindSettingMultipleFields.ISOPEN);

                //取出所以控件
                final AppCompatTextView tvName = holder.getView(R.id.tv_name);
                final AppCompatTextView tvTag = holder.getView(R.id.tv_tag);
                final AppCompatTextView tvDescribe = holder.getView(R.id.tv_describe);
                final AppCompatTextView tvTime = holder.getView(R.id.tv_liveness);
                final AppCompatTextView tv_check = holder.getView(R.id.tv_check);
                final ConstraintLayout cl = holder.getView(R.id.cl);
                cl.setOnClickListener(v -> {
                    if(mRemindSettingListener!=null){
                        mRemindSettingListener.commonClick(remindId);
                    }
                });
                cl.setOnLongClickListener(v -> {
                    if(mDeleteListener!=null){
                        mDeleteListener.commonClick(remindId);
                    }
                    //不传递
                    return true;
                });
                //赋值
                TextViewUtils.AppCompatTextViewSetText(tvName, title);
                TextViewUtils.AppCompatTextViewSetText(tvTag, tag);
                if (TextUtils.isEmpty(description)) {
                    tvDescribe.setVisibility(View.GONE);
                } else {
                    tvDescribe.setVisibility(View.VISIBLE);
                    TextViewUtils.AppCompatTextViewSetText(tvDescribe, description);
                }
                TextViewUtils.AppCompatTextViewSetText(tvTag, tag);

                String day="";
                String[] s=whatDay.split(",");
                int length=s.length;
                if(length==7){
                    day="每天";
                }else if(length==1){
                    int i=Integer.parseInt(s[0]);
                    day=RobotRemindSettingAddWeekDataConverter.week[i-1];
                }else if(length>0){
                    int i=Integer.parseInt(s[0]);
                    day=RobotRemindSettingAddWeekDataConverter.week[i-1]+"等";
                }

                TextViewUtils.AppCompatTextViewSetText(tvTime, day + " " + startTime + "~" + endTime);
                switch (isOpen) {
                    case 1:
                        TextViewUtils.setBackground(Latte.getApplicationContext(),tv_check,R.mipmap.button_robot_setting_remind_swtich_c);
                        break;
                    case 2:
                        TextViewUtils.setBackground(Latte.getApplicationContext(),tv_check,R.mipmap.button_robot_setting_remind_swtich);
                        break;
                    default:
                }
                tv_check.setOnClickListener(v->{
                    if(mOpenListener!=null){
                        switch (isOpen) {
                            case 1:
                                TextViewUtils.setBackground(Latte.getApplicationContext(),tv_check,R.mipmap.button_robot_setting_remind_swtich);
                                break;
                            case 2:
                                TextViewUtils.setBackground(Latte.getApplicationContext(),tv_check,R.mipmap.button_robot_setting_remind_swtich_c);
                                break;
                            default:
                        }
                        mOpenListener.commonLongStringClick(remindId,isOpen+"");
                    }
                });


                break;

            default:
                break;
        }
    }
}
