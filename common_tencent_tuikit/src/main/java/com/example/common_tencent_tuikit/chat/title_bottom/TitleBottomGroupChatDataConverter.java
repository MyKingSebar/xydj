package com.example.common_tencent_tuikit.chat.title_bottom;

import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

public class TitleBottomGroupChatDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final MultipleItemEntity entity = MultipleItemEntity.builder()
                .setField(MultipleFields.ITEM_TYPE,TitleBottomItemType.IM_CHAT_TITLEBOTTOM)
                .setField(MultipleFields.IMAGE_URL, com.example.latte.ui.R.mipmap.icon_im_audiocall)
                .setField(MultipleFields.NAME,"即时通话")
                .setField(MultipleFields.CHILD_ITEM_TYPE,TitleBottomChildType.AUDIOCALL.name())
                .build();
        ENTITIES.add(entity);
        final MultipleItemEntity entity3 = MultipleItemEntity.builder()
                .setField(MultipleFields.ITEM_TYPE,TitleBottomItemType.IM_CHAT_TITLEBOTTOM)
                .setField(MultipleFields.IMAGE_URL, com.example.latte.ui.R.mipmap.icon_im_doctor)
                .setField(MultipleFields.NAME,"预约就诊")
                .setField(MultipleFields.CHILD_ITEM_TYPE,TitleBottomChildType.APPOINTMENTDOCTOR.name())
                .build();
        ENTITIES.add(entity3);
        final MultipleItemEntity entity4 = MultipleItemEntity.builder()
                .setField(MultipleFields.ITEM_TYPE,TitleBottomItemType.IM_CHAT_TITLEBOTTOM)
                .setField(MultipleFields.IMAGE_URL, com.example.latte.ui.R.mipmap.icon_im_nurse)
                .setField(MultipleFields.NAME,"预约护理")
                .setField(MultipleFields.CHILD_ITEM_TYPE,TitleBottomChildType.APPOINTMENTNURSE.name())
                .build();
        ENTITIES.add(entity4);
        final MultipleItemEntity entity5 = MultipleItemEntity.builder()
                .setField(MultipleFields.ITEM_TYPE,TitleBottomItemType.IM_CHAT_TITLEBOTTOM)
                .setField(MultipleFields.IMAGE_URL, com.example.latte.ui.R.mipmap.icon_im_physiotherapy)
                .setField(MultipleFields.NAME,"预约理疗")
                .setField(MultipleFields.CHILD_ITEM_TYPE,TitleBottomChildType.APPOINTMENTPHYSIOTHERAPY.name())
                .build();
        ENTITIES.add(entity5);

        return ENTITIES;
    }

}
