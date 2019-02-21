package com.example.common_tencent_tuikit.chat;

import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;

public class TIMBokangElem extends TIMElem {
    private String text;

    public TIMBokangElem() {
        this.type = TIMElemType.Text;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
