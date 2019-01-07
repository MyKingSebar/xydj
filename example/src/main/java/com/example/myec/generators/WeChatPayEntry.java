package com.example.myec.generators;

import com.example.latte.annotations.PayEntryGenerator;
import com.example.latte.wechat.template.WXPayEntryTemplate;

@SuppressWarnings("unused")
@PayEntryGenerator(
        packageName = "com.example.myec",
        payEntryTemplate = WXPayEntryTemplate.class
)
public interface WeChatPayEntry {
}
