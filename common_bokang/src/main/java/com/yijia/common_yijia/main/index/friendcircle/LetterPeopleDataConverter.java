package com.yijia.common_yijia.main.index.friendcircle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.yijia.common_yijia.main.index.YjIndexCommentMultipleFields;
import com.yijia.common_yijia.main.index.YjIndexItemType;
import com.yijia.common_yijia.main.index.YjIndexMultipleFields;

import java.util.ArrayList;

public class LetterPeopleDataConverter extends DataConverter {
    private final int PARTVISIBLE = 2;
    private final int PARTINVISIBLE = 3;

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONObject dataObject = JSON.parseObject(getJsonData()).getJSONObject("data");
        final int visibleType = dataObject.getInteger("visibleType");
        if (visibleType != PARTVISIBLE) {
            return null;
        }
        final JSONArray friendList = dataObject.getJSONArray("friendList");
        final int size = friendList.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = friendList.getJSONObject(i);

            final int friendUserId = data.getInteger("friendUserId");
            final String nickname = data.getString("nickname");
            final String realName = data.getString("realName");
            final int userStatus = data.getInteger("userStatus");
            final String userHead = data.getString("userHead");
            final String tencentImUserId = data.getString("tencentImUserId");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.INDEX_USERLIST_ITEM)
                    .setField(MultipleFields.ID, friendUserId)
                    .setField(MultipleFields.TENCENTIMUSERID, tencentImUserId)
                    .setField(MultipleFields.IMAGE_URL, userHead)
//                    .setField(YjIndexMultipleFields.CIRCLEID, circleId)
                    .setField(YjIndexMultipleFields.STATUS, userStatus)
                    .setField(YjIndexMultipleFields.USER_REAL_NAME, realName)
                    .setField(YjIndexMultipleFields.USER_NICK_NAME, nickname)
                    .build();

            ENTITIES.add(entity);


        }


        return ENTITIES;
    }

}
