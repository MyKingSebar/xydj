package com.yijia.common_yijia.main.robot.robotmain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.yijia.common_yijia.main.index.YjIndexItemType;
import com.yijia.common_yijia.main.index.YjIndexMultipleFields;

import java.util.ArrayList;

public class RobotGuardianshipDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONObject dataObject = JSON.parseObject(getJsonData()).getJSONObject("data");
        final JSONArray guardianUserList = dataObject.getJSONArray("userList");
        final int size = guardianUserList.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = guardianUserList.getJSONObject(i);

            final long friendUserId = data.getInteger("userId");
            final String nickname = data.getString("nickname");
            final String realName = data.getString("realName");

            /**
             * 主监护人：1是2否
             */
            final int isMain = data.getInteger("isMain");
            final String headImage = data.getString("headImage");
            final String tencentImUserId = data.getString("tencentImUserId");
            //是否有机器人：1-是，-2否
            final int hasRobot = data.getInteger("hasRobot");
            //活跃度
            final int activeness = data.getInteger("activeness");


            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.ROBOTGUARDUABSHIP_ITEM)
                    .setField(MultipleFields.ID, friendUserId)
                    .setField(MultipleFields.TENCENTIMUSERID, tencentImUserId)
                    .setField(MultipleFields.IMAGE_URL, headImage)
                    .setField(YjIndexMultipleFields.ISMAIN, isMain)
                    .setField(YjIndexMultipleFields.USER_REAL_NAME, realName)
                    .setField(YjIndexMultipleFields.USER_NICK_NAME, nickname)
                    .setField(YjIndexMultipleFields.HASROBOT, hasRobot)
                    .setField(YjIndexMultipleFields.ACTIVENESS, activeness)
                    .build();

            ENTITIES.add(entity);


        }


        return ENTITIES;
    }

}
