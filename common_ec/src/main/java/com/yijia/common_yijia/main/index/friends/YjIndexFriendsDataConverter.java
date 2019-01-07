package com.yijia.common_yijia.main.index.friends;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

public class YjIndexFriendsDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONObject dataObject=JSON.parseObject(getJsonData()).getJSONObject("data");
        final JSONArray dataArray=dataObject.getJSONArray("friends");
        final int size=dataArray.size();
        for(int i=0;i<size;i++){
            final JSONObject data = dataArray.getJSONObject(i);

            final Long friendUserId = data.getLong("friendUserId");
            final String nickname = data.getString("nickname");
            final String realName = data.getString("realName");
            final String phone = data.getString("phone");
//            final String cardNo = data.getString("cardNo");
//            final String cardImage = data.getString("cardImage");
//            final String gender = data.getString("gender");
//            final String birthday = data.getString("birthday");
//            final int userStatus = data.getInteger("userStatus");
//            final int isComplete = data.getInteger("isComplete");
//            final int isCertification = data.getInteger("isCertification");
//            final int inviterId = data.getInteger("inviterId");
//            final String createdTime = data.getString("createdTime");
//            final String modifiedTime = data.getString("modifiedTime");
            final String userHead = data.getString("userHead");


            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE,FriendsItemType.INDEX_FRIENDS_ITEM)
                    .setField(MultipleFields.ID,friendUserId)
                    .setField(MultipleFields.IMAGE_URL,userHead)
                    .setField(MultipleFields.NAME,nickname)
                    .build();

            ENTITIES.add(entity);


        }
        final MultipleItemEntity entity = MultipleItemEntity.builder()
                .setField(MultipleFields.ITEM_TYPE,FriendsItemType.INDEX_FRIENDS_ITEM)
                .setField(MultipleFields.ID,0L)
                .build();
        ENTITIES.add(entity);

        return ENTITIES;
    }

}
