package com.bokang.yijia.generators;

import com.bokang.yijia.YiJiaApp;
import com.example.latte.annotations.EntryGenerator;
import com.example.yijia.wechat.template.WXEntryTemplate;


@SuppressWarnings("unused")
@EntryGenerator(
        packageName = YiJiaApp.PACKAGENAME,
        entryTemplate = WXEntryTemplate.class
)
public interface WeChatEntry {
}
