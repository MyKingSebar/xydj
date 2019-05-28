package com.bokang.yijia.generators;

import com.bokang.yijia.YiJiaApp;
import com.example.latte.annotations.PayEntryGenerator;
import com.example.yijia.wechat.template.WXPayEntryTemplate;

@SuppressWarnings("unused")
@PayEntryGenerator(
        packageName = YiJiaApp.PACKAGENAME,
        payEntryTemplate = WXPayEntryTemplate.class
)
public interface WeChatPayEntry {
}
