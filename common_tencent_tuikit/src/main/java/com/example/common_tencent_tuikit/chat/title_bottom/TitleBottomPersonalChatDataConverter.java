package com.example.common_tencent_tuikit.chat.title_bottom;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ui.recycler.DataConverter;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.tencent.qcloud.uikit.common.component.titlebar.PageTitleBar;

import java.util.ArrayList;

public class TitleBottomPersonalChatDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final MultipleItemEntity entity = MultipleItemEntity.builder()
                .setField(MultipleFields.ITEM_TYPE,TitleBottomItemType.IM_CHAT_TITLEBOTTOM)
                .setField(MultipleFields.IMAGE_URL, com.example.latte.ui.R.mipmap.icon_im_audiocall)
                .setField(MultipleFields.NAME,"打电话")
                .setField(MultipleFields.CHILD_ITEM_TYPE,TitleBottomChildType.PHONECALL.name())
                .build();
        ENTITIES.add(entity);
//        final MultipleItemEntity entity2 = MultipleItemEntity.builder()
//                .setField(MultipleFields.ITEM_TYPE,TitleBottomItemType.IM_CHAT_TITLEBOTTOM)
//                .setField(MultipleFields.IMAGE_URL, com.example.latte.ui.R.mipmap.icon_im_zhidingjianhu)
//                .setField(MultipleFields.NAME,"指定监护")
//                .setField(MultipleFields.CHILD_ITEM_TYPE,TitleBottomChildType.DESIGNATEDGUARDIAN.name())
//                .build();
//        ENTITIES.add(entity2);

        return ENTITIES;
    }

}
