package com.yijia.common_yijia.icon;

import com.joanzapata.iconify.Icon;

public enum YjIndexTopIcons implements Icon {
    icon_yj_index_top_mine('\ue605'),
    icon_yj_index_top_camera('\ue756');
    private char character;

    YjIndexTopIcons(char character) {
        this.character = character;
    }



    @Override
    public String key() {
        return name().replace('_', '-');
    }

    @Override
    public char character() {
        return character;
    }
}
