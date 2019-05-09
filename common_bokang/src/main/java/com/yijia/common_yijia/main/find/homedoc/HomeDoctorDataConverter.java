package com.yijia.common_yijia.main.find.homedoc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.yijia.common_yijia.main.index.YjIndexItemType;
import com.yijia.common_yijia.main.index.YjIndexMultipleFields;

import java.util.ArrayList;

public class HomeDoctorDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONArray doclist = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = doclist.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = doclist.getJSONObject(i);

            final long doctTeamId = data.getLong("doctTeamId");
            final long hospitalId = data.getLong("hospitalId");
            final long idCardInfoId = data.getLong("idCardInfoId");
            final String doctTeamName = data.getString("doctTeamName");
            final String hospitalName = data.getString("hospitalName");
            final String idCardInfoName = data.getString("idCardInfoName");
            final String headImage = data.getString("headImage");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.FIND_HOMEDOCTOR_ITEM)
                    .setField(HomeDoctorMultipleFields.DOCTTEAMID, doctTeamId)
                    .setField(HomeDoctorMultipleFields.HOSPITALID, hospitalId)
                    .setField(HomeDoctorMultipleFields.IDCARDINFOID, idCardInfoId)
                    .setField(HomeDoctorMultipleFields.DOCTTEAMNAME, doctTeamName)
                    .setField(HomeDoctorMultipleFields.HOSPITALNAME, hospitalName)
                    .setField(HomeDoctorMultipleFields.IDCARDINFONAME, idCardInfoName)
                    .setField(HomeDoctorMultipleFields.HEADIMAGE, headImage)
                    .build();

            ENTITIES.add(entity);


        }


        return ENTITIES;
    }

}
