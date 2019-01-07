package com.example.myec.generators;

import com.example.latte.annotations.EntryGenerator;
import com.example.latte.wechat.template.WXEntryTemplate;


@SuppressWarnings("unused")
@EntryGenerator(
        packageName = "com.example.myec",
        entryTemplate = WXEntryTemplate.class
)
public interface WeChatEntry {
}
