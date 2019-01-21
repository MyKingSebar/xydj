package com.yijia.common_yijia.main.friends.view.iview;

import com.alibaba.fastjson.JSONObject;
import com.yijia.common_yijia.main.friends.base.BaseView;

public interface SearchFriendsView extends BaseView {
    void respNoFriend();

    void respfriendsdetailsSuccess(JSONObject user);

    void respfriendsdetailsError(String message);
}
