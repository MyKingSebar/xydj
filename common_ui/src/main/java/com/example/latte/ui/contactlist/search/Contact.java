package com.example.latte.ui.contactlist.search;

import com.example.latte.ui.contactlist.cn.CN;

/**
 * Created by you on 2017/9/11.
 */

public class Contact implements CN {

    public final String name;

    public final int imgUrl;

    public Contact(String name, int imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
    }

    @Override
    public String chinese() {
        return name;
    }
}
