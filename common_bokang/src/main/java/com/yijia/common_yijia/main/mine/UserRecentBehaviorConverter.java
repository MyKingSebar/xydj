package com.yijia.common_yijia.main.mine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.yijia.common_yijia.main.index.YjIndexItemType;
import com.yijia.common_yijia.main.message.NoticeMultipleFields;

import java.util.ArrayList;

public class UserRecentBehaviorConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONObject dataObject = JSON.parseObject(getJsonData()).getJSONObject("data");
        final JSONArray recordList = dataObject.getJSONArray("recordList");
        final int size = recordList.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = recordList.getJSONObject(i);

            final long id = data.getLong("id");
            final long userId = data.getLong("userId");
            final long type = data.getLong("type");
            final String description = data.getString("description");
            final String createdTime = data.getString("createdTime");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.INDEX_RECENTBEHAVIOR_ITEM)
                    .setField(MultipleFields.ID, id)
                    .setField(RecentBehaviorMultipleFields.USERID, userId)
                    .setField(RecentBehaviorMultipleFields.TYPE, type)
                    .setField(RecentBehaviorMultipleFields.DESCRIPTION, description)
                    .setField(RecentBehaviorMultipleFields.CREATEDTIME, createdTime)
                    .build();

            ENTITIES.add(entity);
        }


        return ENTITIES;
    }

}
