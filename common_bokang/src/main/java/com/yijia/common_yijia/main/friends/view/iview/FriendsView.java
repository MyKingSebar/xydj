package com.yijia.common_yijia.main.friends.view.iview;

import com.alibaba.fastjson.JSONArray;
import com.yijia.common_yijia.main.friends.base.BaseView;

public interface FriendsView extends BaseView {
    void respFriendsError(String error);

    void respFriendsSuccess(JSONArray friends);

    void respGuardianSuccess(JSONArray guardianUserList, JSONArray oldMapUserList);

    void respGuardianError(String guardianError);
}
