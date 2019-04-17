package com.yijia.common_yijia.icon;

import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconFontDescriptor;

public class FontYJIndexTopModule implements IconFontDescriptor {
    @Override
    public String ttfFileName() {
        return "index_top.ttf";
    }

    @Override
    public Icon[] characters() {
        return YjIndexTopIcons.values();
    }
}
