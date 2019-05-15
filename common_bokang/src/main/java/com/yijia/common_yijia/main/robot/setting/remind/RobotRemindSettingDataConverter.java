package com.yijia.common_yijia.main.robot.setting.remind;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.yijia.common_yijia.main.index.YjIndexItemType;
import com.yijia.common_yijia.main.index.YjIndexMultipleFields;

import java.util.ArrayList;
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
public class RobotRemindSettingDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONArray datas = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = datas.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = datas.getJSONObject(i);

            final long remindId = data.getLong("remindId");
            final String tag = data.getString("tag");
            //"1,2,3",         //星期几
            final String whatDay = data.getString("whatDay");
            //"01:00:00",     //开始时间
            final String startTime = data.getString("startTime");
            final String endTime = data.getString("endTime");
            final String title = data.getString("title");
            final String description = data.getString("description");
            //是否开启 1开启 2关闭
            final int isOpen = data.getInteger("isOpen");


            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.ROBOT_REMINDSETTING)
                    .setField(RobotRemindSettingMultipleFields.REMINDID, remindId)
                    .setField(RobotRemindSettingMultipleFields.TAG, tag)
                    .setField(RobotRemindSettingMultipleFields.WHATDAY, whatDay)
                    .setField(RobotRemindSettingMultipleFields.STARTTIME, startTime)
                    .setField(RobotRemindSettingMultipleFields.ENDTIME, endTime)
                    .setField(RobotRemindSettingMultipleFields.TITLE, title)
                    .setField(RobotRemindSettingMultipleFields.DESCRIPTION, description)
                    .setField(RobotRemindSettingMultipleFields.ISOPEN, isOpen)
                    .build();

            ENTITIES.add(entity);


        }


        return ENTITIES;
    }

}
