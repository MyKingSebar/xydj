package com.example.latte.delegates.bottom;

public final class BottomTabBean {
    private final CharSequence ICON;
    private final CharSequence TITLE;
    private final int ICONID;
    private final int ICONCLICKEDID;

    public BottomTabBean(CharSequence icon, CharSequence title,int iconid,int iconclickedid) {
        this.ICON = icon;
        this.TITLE = title;
        this.ICONID = iconid;
        this.ICONCLICKEDID = iconclickedid;
    }


    public CharSequence getIcon() {
        return ICON;
    }

    public CharSequence getTitle() {
        return TITLE;
    }

    public int getIconId(){
        return ICONID;
    }
    public int getIconclickedid(){
        return ICONCLICKEDID;
    }


}
