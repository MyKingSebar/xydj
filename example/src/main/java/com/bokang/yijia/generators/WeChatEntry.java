package com.bokang.yijia.generators;

import com.bokang.yijia.ExampleApp;
import com.example.latte.annotations.EntryGenerator;
import com.example.latte.wechat.template.WXEntryTemplate;


@SuppressWarnings("unused")
@EntryGenerator(
        packageName = ExampleApp.PACKAGENAME,
        entryTemplate = WXEntryTemplate.class
)
public interface WeChatEntry {
}
