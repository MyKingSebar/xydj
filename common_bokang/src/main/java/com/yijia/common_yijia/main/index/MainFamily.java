package com.yijia.common_yijia.main.index;

import java.io.Serializable;

/**
 * Created by wangpanfeng@bokangzhixin.com on 2019/5/16.
 */
public class MainFamily implements Serializable {

    public long familyId;
    public String familyName;
    public long mainUserId;
    public String mainUserName;
    public String relationMainToUser;
    public String relationUserToMain;
    public int robotIsOnline;
    public String headImage;
    public int permissionType;
    public long createdUserId;
}
