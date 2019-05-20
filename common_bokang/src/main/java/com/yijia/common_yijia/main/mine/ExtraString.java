package com.yijia.common_yijia.main.mine;

/**
 * Created by wangpanfeng@bokangzhixin.com on 2019/4/12.
 */
public class ExtraString {
    public static final String ISINCOME = "phone_isincome";

    public static final String PHONE_NUM = "phone_num";
    public static final String PHONE_NAME = "phone_name";
    public static final String PHONE_URL = "phone_url";

    public static final String BROADCAST_ANSWER_BEGIN = "broadcast_answer_the_phone";

    public static final String BROADCAST_END_BEGIN = "broadcast_end_the_phone";


    //事件处理service  机器人端未接通时通知后台的广播
    public static final String BROADCAST_SEND_CALL_NO_RESPONSE = "com.bk.broadcast.robotapp.relatives";

    public static final String EXTRA_SEND_CALL_NO_RESPONSE = "noanswer";

    //机器人语音拨打电话  system通知的人员姓名广播
    public static final String BROADCAST_CALL_FROM_SYSTEM = "com.bk.send.broadcast.robotapp.relatives";
    public static final String BROADCAST_CALL_FROM_SYSTEM_EXTRA = "name";

}
