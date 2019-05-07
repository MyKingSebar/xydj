package com.yijia.common_yijia.main.find.homedoc.doctorin_top;

import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.yijia.common_yijia.main.index.YjIndexItemType;

import java.util.ArrayList;

public class DoctorInTitleDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
//        final MultipleItemEntity entity = MultipleItemEntity.builder()
//                .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.DOCTORIN_GROUP_CHAT_TITLE)
//                .setField(MultipleFields.IMAGE_URL, com.example.latte.ui.R.mipmap.icon_im_audiocall)
//                .setField(MultipleFields.NAME,"即时通话")
//                .setField(MultipleFields.CHILD_ITEM_TYPE, DoctorInTitleType.AUDIOCALL.name())
//                .build();
//        ENTITIES.add(entity);
        final MultipleItemEntity entity3 = MultipleItemEntity.builder()
                .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.DOCTORIN_GROUP_CHAT_TITLE)
                .setField(MultipleFields.IMAGE_URL, com.example.latte.ui.R.mipmap.icon_im_doctor)
                .setField(MultipleFields.NAME,"预约就诊")
                .setField(MultipleFields.CHILD_ITEM_TYPE, DoctorInTitleType.APPOINTMENTDOCTOR.name())
                .build();
        ENTITIES.add(entity3);
        final MultipleItemEntity entity4 = MultipleItemEntity.builder()
                .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.DOCTORIN_GROUP_CHAT_TITLE)
                .setField(MultipleFields.IMAGE_URL, com.example.latte.ui.R.mipmap.icon_im_nurse)
                .setField(MultipleFields.NAME,"预约护理")
                .setField(MultipleFields.CHILD_ITEM_TYPE, DoctorInTitleType.APPOINTMENTNURSE.name())
                .build();
        ENTITIES.add(entity4);
        final MultipleItemEntity entity5 = MultipleItemEntity.builder()
                .setField(MultipleFields.ITEM_TYPE, YjIndexItemType.DOCTORIN_GROUP_CHAT_TITLE)
                .setField(MultipleFields.IMAGE_URL, com.example.latte.ui.R.mipmap.icon_im_physiotherapy)
                .setField(MultipleFields.NAME,"预约理疗")
                .setField(MultipleFields.CHILD_ITEM_TYPE, DoctorInTitleType.APPOINTMENTPHYSIOTHERAPY.name())
                .build();
        ENTITIES.add(entity5);

        return ENTITIES;
    }

}
