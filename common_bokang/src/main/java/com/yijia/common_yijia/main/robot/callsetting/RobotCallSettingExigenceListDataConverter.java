package com.yijia.common_yijia.main.robot.callsetting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.yijia.common_yijia.main.index.YjIndexItemType;

import java.util.ArrayList;

public class RobotCallSettingExigenceListDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONObject dataObject = JSON.parseObject(getJsonData()).getJSONObject("data");
        final JSONArray guardianUserList = dataObject.getJSONArray("friends");
        final int size = guardianUserList.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = guardianUserList.getJSONObject(i);

            final long friendUserId = data.getLong("friendUserId");
            final String nickname = data.getString("nickname");
            final String realName = data.getString("realName");
            final int userStatus = data.getInteger("userStatus");

            final String userHead = data.getString("userHead");
            final String tencentImUserId = data.getString("tencentImUserId");
            final JSONArray friendNicknames = data.getJSONArray("friendNicknames");
            final int friendNicknamesSize = friendNicknames.size();
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < friendNicknamesSize; j++) {
                final JSONObject data2 = friendNicknames.getJSONObject(j);
                final String friendNickname = data2.getString("friendNickname");
                if (j == 0) {
                    sb.append(friendNickname);
                } else if(j==1){
                    sb.append(";" + friendNickname);
                }else {

                }
            }
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.ROBOT_CALLSETTING_EXIGENCELIST)
                    .setField(RobotCallSettingListFields.FRIENDUSERID, friendUserId)
                    .setField(RobotCallSettingListFields.TENCENTIMUSERID, tencentImUserId)
                    .setField(RobotCallSettingListFields.NICKNAME, nickname)
                    .setField(RobotCallSettingListFields.USERHEAD, userHead)
                    .setField(RobotCallSettingListFields.REALNAME, realName)
                    .setField(RobotCallSettingListFields.USERSTATUS, userStatus)
                    .setField(RobotCallSettingListFields.FRIENDNICKNAMES, sb.toString())
                    .build();

            ENTITIES.add(entity);


        }


        return ENTITIES;
    }

}
