package com.example.myec.generators;

import com.example.latte.annotations.AppRegisterGenerator;
import com.example.latte.wechat.template.AppRegisterTemplate;

@SuppressWarnings("unused")
@AppRegisterGenerator(
        packageName = "com.example.myec",
        registerTemplate = AppRegisterTemplate.class
)
public interface AppRegister {
}
