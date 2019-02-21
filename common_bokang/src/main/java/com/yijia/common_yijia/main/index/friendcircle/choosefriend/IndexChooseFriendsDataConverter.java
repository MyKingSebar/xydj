package com.yijia.common_yijia.main.index.friendcircle.choosefriend;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class IndexChooseFriendsDataConverter {
    String response=null;
    final ArrayList<ChooseFriendData> ENTITIES=new ArrayList<>();
    public IndexChooseFriendsDataConverter(String response) {
        this.response=response;
    }

    public ArrayList<ChooseFriendData> convert() {
        final JSONObject dataObject=JSON.parseObject(response).getJSONObject("data");
        final JSONArray dataArray=dataObject.getJSONArray("friends");
        final int size=dataArray.size();
        for(int i=0;i<size;i++){
            final JSONObject data = dataArray.getJSONObject(i);

            final Long friendUserId = data.getLong("friendUserId");
            final String nickname = data.getString("nickname");
//            final String realName = data.getString("realName");
            final String userStatus = data.getString("userStatus");
            final String userHead = data.getString("userHead");
            final String tencentImUserId = data.getString("tencentImUserId");
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


            final ChooseFriendData entity =new ChooseFriendData();
            entity.setFriendUserId(friendUserId);
            entity.setUserStatus(userStatus);
            entity.setNickname(nickname);
            entity.setUserHead(userHead);
            entity.setTencentImUserId(tencentImUserId);
            ENTITIES.add(entity);



        }

        return ENTITIES;
    }

}
