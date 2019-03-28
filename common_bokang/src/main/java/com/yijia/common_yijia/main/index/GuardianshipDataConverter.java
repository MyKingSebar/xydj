package com.yijia.common_yijia.main.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

public class GuardianshipDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONObject dataObject = JSON.parseObject(getJsonData()).getJSONObject("data");
        final JSONArray guardianUserList = dataObject.getJSONArray("guardianUserList");
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

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.GUARDUABSHIPLIST_ITEM)
                    .setField(MultipleFields.ID, friendUserId)
                    .setField(MultipleFields.TENCENTIMUSERID, tencentImUserId)
                    .setField(MultipleFields.IMAGE_URL, headImage)
                    .setField(YjIndexMultipleFields.ISMAIN, isMain)
                    .setField(YjIndexMultipleFields.USER_REAL_NAME, realName)
                    .setField(YjIndexMultipleFields.USER_NICK_NAME, nickname)
                    .build();

            ENTITIES.add(entity);


        }


        return ENTITIES;
    }

}
