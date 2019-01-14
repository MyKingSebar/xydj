package com.bokang.yijia.generators;

import com.bokang.yijia.ExampleApp;
import com.example.latte.annotations.AppRegisterGenerator;
import com.example.latte.wechat.template.AppRegisterTemplate;

@SuppressWarnings("unused")
@AppRegisterGenerator(
        packageName = ExampleApp.PACKAGENAME,
        registerTemplate = AppRegisterTemplate.class
)
public interface AppRegister {
}
