package com.example.yijia.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangpanfeng@bokangzhixin.com on 2019/5/15.
 */
public class PatternsUtil {

    public static final String INTEGER = "^-?[1-9]\\d*$";            // 整数
    public static final String INTEGER1 = "^[1-9]\\d*$";            // 正整数
    public static final String INTEGER2 = "^-[1-9]\\d*$";            // 负整数
    public static final String NUMBER = "^([+-]?)\\d*$";            // 数字
    public static final String NUMBER1 = "^[1-9]\\d*|0$";            // 正数（正整数 + 0）
    public static final String NUMBER2 = "^-[1-9]\\d*|0$";            // 负数（负整数 + 0）
    public static final String DECMAL = "^([+-]?)\\d*\\.\\d+$";                                // 浮点数
    public static final String DECMAL1 = "^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*$";                // 正浮点数
    public static final String DECMAL2 = "^-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)$";                // 负浮点数
    public static final String DECMAL3 = "^-?([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0)$";    // 浮点数
    public static final String DECMAL4 = "^[1-9]\\d*|0$.\\d*|0.\\d*[1-9]\\d*|0?.0+|0$";        // 非负浮点数（正浮点数 + 0）
    public static final String DECMAL5 = "^(-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*))|0?.0+|0$";    // 非正浮点数（负浮点数 + 0）

    public static final String EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$"; // 邮件
    public static final String COLOR = "^[a-fA-F0-9]{6}$";                                            // 颜色
    public static final String URL = "^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$";        // url
    public static final String CHINESE = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$";                        // 仅中文
    public static final String ASCII = "^[\\x00-\\xFF]+$";                                            // 仅ACSII字符
    public static final String ZIPCODE = "^\\d{6}$";                                                // 邮编
    public static final String MOBILE = "^13[0-9]{9}|15[012356789][0-9]{8}|17[07][0-9]{8}|18[023456789][0-9]{8}|147[0-9]{8}$";    // 手机
    public static final String IP4 = "^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$";    // ip地址
    public static final String NOTEMPTY = "^\\S+$";                                                    // 非空
    public static final String PICTURE = "(.*)\\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga)$";        // 图片
    public static final String RAR = "(.*)\\.(rar|zip|7zip|tgz)$";                                    // 压缩文件
    public static final String DATE = "^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}$";                    // 日期
    public static final String QQ = "^[1-9]*[1-9][0-9]*$";                                            // QQ号码
    public static final String TEL = "^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$";    // 电话号码的函数(包括验证国内区号,国际区号,分机号)
    public static final String TEL2 = "^(([0\\+]\\d{2,3})?(0\\d{2,3}))?(\\d{7,8})((\\d{3,}))?$";    // 电话号码的函数(包括验证国内区号,国际区号,分机号)
    //“^((\d{3,4}\-)|)\d{7,8}(|([-\u8f6c]{1}\d{1,5}))$”座机号
    public static final String USERNAME = "^\\w+$";    //  ^(?![a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{10,20}$	// 用来用户注册。匹配由数字、26个英文字母或者下划线组成的字符串
    public static final String LETTER = "^[A-Za-z]+$";                    // 字母
    public static final String LETTER_U = "^[A-Z]+$";                    // 大写字母
    public static final String LETTER_L = "^[a-z]+$";                    // 小写字母
    public static final String IDCARD = "^[1-9]([0-9]{14}|[0-9]{17})$";    // 身份证

    public static boolean isPhone(String phone) {
        Pattern pattern = Pattern.compile(MOBILE);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

}
