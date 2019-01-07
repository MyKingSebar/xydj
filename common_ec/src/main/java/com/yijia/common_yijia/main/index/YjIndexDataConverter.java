package com.yijia.common_yijia.main.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.ItemType;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

public class YjIndexDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONObject dataObject = JSON.parseObject(getJsonData()).getJSONObject("data");
        final JSONArray cricleList = dataObject.getJSONArray("cricleList");
        final int size = cricleList.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = cricleList.getJSONObject(i);

            final Long userId = data.getLong("userId");
            final String userNickname = data.getString("userNickname");
            final String userRealName = data.getString("userRealName");
            final String userHead = data.getString("userHead");
            final int isOwn = data.getInteger("isOwn");
            final int circleId = data.getInteger("circleId");
            final int contentType = data.getInteger("contentType");
            final String content = data.getString("content");
            final String pictureUrl = data.getString("pictureUrl");
            final String voiceUrl = data.getString("voiceUrl");
            final String videoUrl = data.getString("videoUrl");
            final String location = data.getString("location");
            final String longitude = data.getString("longitude");
            final String latitude = data.getString("latitude");
            final String createdTime = data.getString("createdTime");
            final JSONArray commentList = data.getJSONArray("commentList");
            final JSONArray likeList = data.getJSONArray("likeList");
            final String[] imgs = pictureUrl.split(",");

            StringBuffer likes=new StringBuffer();
            final int likesSize=likeList.size();
            for (int j = 0; j < likesSize; j++) {
                final JSONObject likeData = likeList.getJSONObject(j);
                likes.append(likeData.getString("likeUserNickname"));
                if(j<likesSize-1){
                    likes.append("、");
                }
            }

            int type = 0;
            switch (contentType) {
                //1-文本，2-照片，3-语音，4-视频
                case 1:
                    type=YjIndexItemType.INDEX_TEXT_ITEM;
                    break;
                case 2:
//                    if(imgs.length>1){
                        type=YjIndexItemType.INDEX_IMAGES_ITEM;
//                    }else {
//                        type=YjIndexItemType.INDEX_IMAGE_ITEM;
//                    }
                    break;
                case 3:
                    type=YjIndexItemType.INDEX_VOICE_ITEM;
                    break;
                case 4:
                    type=YjIndexItemType.INDEX_VIDEO_ITEM;
                    break;
                default:
                    break;

            }
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, type)
                    .setField(MultipleFields.ID, userId)
                    .setField(MultipleFields.TEXT, content)
                    .setField(MultipleFields.IMAGE_URL, userHead)
                    .setField(YjIndexMultipleFields.USER_NICK_NAME, userNickname)
                    .setField(YjIndexMultipleFields.USER_REAL_NAME, userRealName)
                    .setField(YjIndexMultipleFields.ISOWN, isOwn)
                    .setField(YjIndexMultipleFields.CIRCLEID, circleId)
                    .setField(YjIndexMultipleFields.LOCATION, location)
                    .setField(YjIndexMultipleFields.LONGITUDE, longitude)
                    .setField(YjIndexMultipleFields.LATITUDE, latitude)
                    .setField(YjIndexMultipleFields.CREATEDTIME, createdTime)
                    .setField(YjIndexMultipleFields.COMMENTLIST, commentList)
                    .setField(YjIndexMultipleFields.LIKELIST, likes.toString())
                    .setField(YjIndexMultipleFields.IMGS, imgs)
                    .setField(YjIndexMultipleFields.VOICEURL, voiceUrl)
                    .setField(YjIndexMultipleFields.VIDEOURL, videoUrl)
                    .build();

            ENTITIES.add(entity);


        }


        return ENTITIES;
    }

}
