package com.yijia.common_yijia.main.robot.robotmain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.yijia.common_yijia.main.index.YjIndexItemType;
import com.yijia.common_yijia.main.index.YjRobotListMultipleFields;

import java.util.ArrayList;

public class RobotListConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONArray guardianUserList = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = guardianUserList.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = guardianUserList.getJSONObject(i);

            final long familyId = data.getLong("familyId");
            final String familyName = data.getString("familyName");
            final long mainUserId = data.getLong("mainUserId");
            final String mainUserName = data.getString("mainUserName");
            final String relationMainToUser = data.getString("relationMainToUser");
            final String relationUserToMain = data.getString("relationUserToMain");
            final int robotIsOnline = data.getInteger("robotIsOnline");
            final String headImage = data.getString("headImage");
            final int isAdmin  = data.getInteger("isAdmin");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.ROBOT_MAIN_LIST)
                    .setField(MultipleFields.ID, familyId)
                    .setField(MultipleFields.NAME, familyName)
                    .setField(YjRobotListMultipleFields.MAINID, mainUserId)
                    .setField(YjRobotListMultipleFields.MAINNAME, mainUserName)
                    .setField(YjRobotListMultipleFields.RELATIONSHIP, relationUserToMain)
                    .setField(YjRobotListMultipleFields.ONLINE, robotIsOnline)
                    .setField(YjRobotListMultipleFields.ISADMIN, isAdmin)
                    .setField(MultipleFields.IMAGE_URL, headImage)
                    .build();

            ENTITIES.add(entity);


        }


        return ENTITIES;
    }

}
