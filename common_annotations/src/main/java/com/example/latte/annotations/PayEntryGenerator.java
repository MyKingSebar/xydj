package com.example.latte.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//告诉编译器方法是用在类上的
@Target(ElementType.TYPE)
//编译器在处理注解的时候是在源码阶段
@Retention(RetentionPolicy.SOURCE)
public @interface PayEntryGenerator {

    String packageName();

    Class<?> payEntryTemplate();

}
