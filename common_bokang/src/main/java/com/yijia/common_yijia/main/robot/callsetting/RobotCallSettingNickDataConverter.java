package com.yijia.common_yijia.main.robot.callsetting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.yijia.common_yijia.main.index.YjIndexItemType;

import java.util.ArrayList;

public class RobotCallSettingNickDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONArray guardianUserList = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = guardianUserList.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = guardianUserList.getJSONObject(i);

            final long friendNicknameId = data.getLong("friendNicknameId");
            final String friendNickname = data.getString("friendNickname");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.ROBOT_CALLSETTING_NICKLIST)
                    .setField(RobotCallSettingListFields.FRIENDNICKNAMEID, friendNicknameId)
                    .setField(RobotCallSettingListFields.FRIENDNICKNAME, friendNickname)
                    .build();

            ENTITIES.add(entity);


        }
        final MultipleItemEntity add = MultipleItemEntity.builder()
                .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.ROBOT_CALLSETTING_NICKLIST_ADD)
                .build();
        ENTITIES.add(add);
        final MultipleItemEntity des = MultipleItemEntity.builder()
                .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.ROBOT_CALLSETTING_NICKLIST_DESCRIBE)
                .build();
        ENTITIES.add(des);

        return ENTITIES;
    }

}
