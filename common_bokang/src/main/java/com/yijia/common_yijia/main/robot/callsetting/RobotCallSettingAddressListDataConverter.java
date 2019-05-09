package com.yijia.common_yijia.main.robot.callsetting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.yijia.common_yijia.main.index.YjIndexItemType;

import java.util.ArrayList;

public class RobotCallSettingAddressListDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONObject dataObject = JSON.parseObject(getJsonData()).getJSONObject("data");
        final JSONArray guardianUserList = dataObject.getJSONArray("callList");
        final int size = guardianUserList.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = guardianUserList.getJSONObject(i);

            final long callId = data.getLong("callId");
            final String nickname = data.getString("nickname");
            final String phone = data.getString("phone");
            final String headImage = data.getString("headImage");
            final JSONArray nicknames = data.getJSONArray("nicknames");
            final int nicknamesSize = nicknames.size();
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < nicknamesSize; j++) {
                final JSONObject data2 = nicknames.getJSONObject(j);
                final String callNickname = data2.getString("callNickname");
                if (j == 0) {
                    sb.append(callNickname);
                } else if(j==1){
                    sb.append(";" + callNickname);
                }else {

                }
            }
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.ROBOT_CALLSETTING_ADDRESSLIST)
                    .setField(RobotCallSettingListFields.CALLID, callId)
                    .setField(RobotCallSettingListFields.NICKNAME, nickname)
                    .setField(RobotCallSettingListFields.PHONE, phone)
                    .setField(RobotCallSettingListFields.USERHEAD, headImage)
                    .setField(RobotCallSettingListFields.FRIENDNICKNAMES, sb.toString())
                    .build();

            ENTITIES.add(entity);


        }


        return ENTITIES;
    }

}
