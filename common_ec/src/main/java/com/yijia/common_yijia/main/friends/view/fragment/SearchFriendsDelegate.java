package com.yijia.common_yijia.main.friends.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.friends.presenter.SearchFriendsPresenter;
import com.yijia.common_yijia.main.friends.view.iview.SearchFriendsView;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchFriendsDelegate extends LatteDelegate implements SearchFriendsView {
    @BindView(R2.id.return_button)
    RelativeLayout returnButton;
    @BindView(R2.id.search_edittext)
    EditText searchEdittext;
    @BindView(R2.id.search_button)
    TextView searchButton;
    private SearchFriendsPresenter searchFriendsPresenter;
    private String token;

    @Override
    public Object setLayout() {
        return R.layout.delegate_searchfriends;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        searchFriendsPresenter = new SearchFriendsPresenter(this);
        token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
    }

    @OnClick({R2.id.return_button, R2.id.search_button})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.return_button) {
            getSupportDelegate().pop();

        } else if (i == R.id.search_button) {
            String friends_phone = searchEdittext.getText().toString();
            searchFriendsPresenter.reqfriendsdetails(friends_phone,token);
        }
    }
    //没有好友的处理
    @Override
    public void respNoFriend() {

    }
    //查询成功的处理
    @Override
    public void respfriendsdetailsSuccess(JSONObject user) {
        if (user == null){
            return;
        }
        String nickname = user.getString("nickname");
        String imagePath = user.getString("imagePath");

    }
    //查询错误的处理
    @Override
    public void respfriendsdetailsError(String message) {

    }
}
