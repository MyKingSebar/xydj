package com.bokang.yijia.generators;

import com.bokang.yijia.YiJiaApp;
import com.example.latte.annotations.AppRegisterGenerator;
import com.example.yijia.wechat.template.AppRegisterTemplate;

@SuppressWarnings("unused")
@AppRegisterGenerator(
        packageName = YiJiaApp.PACKAGENAME,
        registerTemplate = AppRegisterTemplate.class
)
public interface AppRegister {
}
