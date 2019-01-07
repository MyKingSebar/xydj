package com.example.latte.ec.icon;

import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconFontDescriptor;

public class FontYJModule implements IconFontDescriptor {
    @Override
    public String ttfFileName() {
        return "main.ttf";
    }

    @Override
    public Icon[] characters() {
        return YjIcons.values();
    }
}
