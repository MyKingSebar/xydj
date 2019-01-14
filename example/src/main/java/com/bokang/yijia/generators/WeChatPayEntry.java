package com.bokang.yijia.generators;

import com.bokang.yijia.ExampleApp;
import com.example.latte.annotations.PayEntryGenerator;
import com.example.latte.wechat.template.WXPayEntryTemplate;

@SuppressWarnings("unused")
@PayEntryGenerator(
        packageName = ExampleApp.PACKAGENAME,
        payEntryTemplate = WXPayEntryTemplate.class
)
public interface WeChatPayEntry {
}
