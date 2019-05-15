package com.yijia.common_yijia.main.find.homedoc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.yijia.common_yijia.main.index.YjIndexItemType;

import java.util.ArrayList;

public class HomeDoctorInUseRecycDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONArray doclist = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = doclist.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = doclist.getJSONObject(i);

            final int isLeader = data.getInteger("isLeader");
            final long doctAccountId = data.getInteger("doctAccountId");
            final long doctId = data.getInteger("doctId");
            final String doctName = data.getString("doctName");
            final int doctLevelCode = data.getInteger("doctLevelCode");
            final String doctLevelName = data.getString("doctLevelName");
            final String doctHeadImage = data.getString("doctHeadImage");
            final String tencentImId = data.getString("tencentImId");
            final String major = data.getString("major");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.DOCTORIN_GROUP_CHAT_USE_RECYC)
                    .setField(HomeDoctorInMultipleFields.ISLEADER, isLeader)
                    .setField(HomeDoctorInMultipleFields.DOCTACCOUNTID, doctAccountId)
                    .setField(HomeDoctorInMultipleFields.DOCTID, doctId)
                    .setField(HomeDoctorInMultipleFields.DOCTNAME, doctName)
                    .setField(HomeDoctorInMultipleFields.DOCTLEVELCODE, doctLevelCode)
                    .setField(HomeDoctorInMultipleFields.DOCTLEVELNAME, doctLevelName)
                    .setField(HomeDoctorInMultipleFields.DOCTHEADIMAGE, doctHeadImage)
                    .setField(HomeDoctorInMultipleFields.TENCENTIMID, tencentImId)
                    .setField(HomeDoctorInMultipleFields.MAJOR, major)
                    .build();

            ENTITIES.add(entity);


        }


        return ENTITIES;
    }

}
