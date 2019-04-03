package com.yijia.common_yijia.main.mine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.yijia.common_yijia.main.index.YjIndexItemType;
import com.yijia.common_yijia.main.index.YjIndexMultipleFields;

import java.util.ArrayList;

public class UserProfilesDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONObject dataObject = JSON.parseObject(getJsonData()).getJSONObject("data");
        final JSONArray guardianUserList = dataObject.getJSONArray("idCardInfoList");
        final int size = guardianUserList.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = guardianUserList.getJSONObject(i);

            final long friendUserId = data.getInteger("id");
            final String nickname = data.getString("name");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.USERPROFILESLIST_ITEM)
                    .setField(MultipleFields.ID, friendUserId)
                    .setField(YjIndexMultipleFields.USER_NICK_NAME, nickname)
                    .build();

            ENTITIES.add(entity);


        }


        return ENTITIES;
    }

}
