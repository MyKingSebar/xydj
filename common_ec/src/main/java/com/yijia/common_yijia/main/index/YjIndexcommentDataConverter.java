package com.yijia.common_yijia.main.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.ItemType;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

public class YjIndexcommentDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONArray dataarray = JSON.parseArray(getJsonData());
        final int size = dataarray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = dataarray.getJSONObject(i);

            final Long commentId = data.getLong("commentId");
            final Long commentUserId = data.getLong("commentUserId");
            final String commentUserNickname = data.getString("commentUserNickname");
            final String commentUserRealName = data.getString("commentUserRealName");
            final String commentUserHead = data.getString("commentUserHead");
            final String commentContent = data.getString("commentContent");
            final int replyUserId = data.getInteger("replyUserId");
            final String replyUserNickname = data.getString("replyUserNickname");
            final String replyUserRealName = data.getString("replyUserRealName");
            final String commentCreatedTime = data.getString("commentCreatedTime");
            final int isOwnComment = data.getInteger("isOwnComment");



            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.INDEX_COMMENTLIST_ITEM)
                    .setField(YjIndexCommentMultipleFields.COMMENTID, commentId)
                    .setField(YjIndexCommentMultipleFields.COMMENTUSERID, commentUserId)
                    .setField(YjIndexCommentMultipleFields.COMMENTUSERNICKNAME, commentUserNickname)
                    .setField(YjIndexCommentMultipleFields.COMMENTUSERREALNAME, commentUserRealName)
                    .setField(YjIndexCommentMultipleFields.COMMENTUSERHEAD, commentUserHead)
                    .setField(YjIndexCommentMultipleFields.COMMENTCONTENT, commentContent)
                    .setField(YjIndexCommentMultipleFields.REPLYUSERID, replyUserId)
                    .setField(YjIndexCommentMultipleFields.REPLYUSERNICKNAME, replyUserNickname)
                    .setField(YjIndexCommentMultipleFields.REPLYUSERREALNAME, replyUserRealName)
                    .setField(YjIndexCommentMultipleFields.COMMENTCREATEDTIME, commentCreatedTime)
                    .setField(YjIndexCommentMultipleFields.ISOWNCOMMENT, isOwnComment)
                    .build();

            ENTITIES.add(entity);


        }


        return ENTITIES;
    }

}
