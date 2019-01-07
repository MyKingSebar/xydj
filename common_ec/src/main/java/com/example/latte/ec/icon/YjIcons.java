package com.example.latte.ec.icon;

import com.joanzapata.iconify.Icon;

public enum YjIcons implements Icon {
    icon_yscan('\ue624'),
    icon_ycamera('\ue657'),
    icon_ytime('\ue613'),
    icon_yzan('\ue6a3'),
    icon_ycomment('\ue63a'),
    icon_yshare('\ue7c5'),
    icon_yadd('\ue7a9'),
    icon_yback('\ue605'),
    icon_ydelete('\ue669'),
    icon_yedit('\ue66c');
    private char character;

    YjIcons(char character) {
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
