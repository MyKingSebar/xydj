package com.yijia.common_yijia.main.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.yijia.common_yijia.main.index.YjIndexItemType;
import com.yijia.common_yijia.main.index.YjIndexMultipleFields;

import java.util.ArrayList;

public class NoticeDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONObject dataObject = JSON.parseObject(getJsonData()).getJSONObject("data");
        final JSONArray noticesList = dataObject.getJSONArray("opePushRecords");
        final int size = noticesList.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = noticesList.getJSONObject(i);

            final Long id = data.getLong("id");
            final Long targetUserId = data.getLong("targetUserId");
            final int pushType = data.getInteger("pushType");
            final String title = data.getString("title");
            final String content = data.getString("content");
            final Long jumpId = data.getLong("jumpId");
            final Long isRead = data.getLong("isRead");
            final String createdTime = data.getString("createdTime");
            final String modifiedTime = data.getString("modifiedTime");
            final String imagePath = data.getString("imagePath");
            final Long friendApplyId = data.getLong("friendApplyId");
            final int isAgree = data.getInteger("isAgree");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.INDEX_NOTICELIST_ITEM)
                    .setField(MultipleFields.ID, id)
                    .setField(MultipleFields.IMAGE_URL, imagePath)
                    .setField(NoticeMultipleFields.TARGETUSERID, targetUserId)
                    .setField(NoticeMultipleFields.PUTHTYPEID, pushType)
                    .setField(NoticeMultipleFields.TITLE, title)
                    .setField(NoticeMultipleFields.CONTENT, content)
                    .setField(NoticeMultipleFields.JUMPID, jumpId)
                    .setField(NoticeMultipleFields.ISREAD, isRead)
                    .setField(NoticeMultipleFields.CREATEDTIME, createdTime)
                    .setField(NoticeMultipleFields.MODIFIEDTIME, modifiedTime)
                    .setField(NoticeMultipleFields.FRIENDAPPLYID, friendApplyId)
                    .setField(NoticeMultipleFields.ISAGREE, isAgree)
                    .build();

            ENTITIES.add(entity);
        }


        return ENTITIES;
    }

}
