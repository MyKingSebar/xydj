package com.yijia.common_yijia.main.friends.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.yijia.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.friends.presenter.SearchFriendsPresenter;
import com.yijia.common_yijia.main.friends.view.iview.SearchFriendsView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 查询好友页面
 */
public class SearchFriendsDelegate extends LatteDelegate implements SearchFriendsView {
    @BindView(R2.id.return_button)
    RelativeLayout returnButton;
    @BindView(R2.id.search_edittext)
    EditText searchEdittext;
    @BindView(R2.id.search_button)
    TextView searchButton;
    @BindView(R2.id.user_head)
    ImageView userHead;
    @BindView(R2.id.user_name)
    TextView userName;
    @BindView(R2.id.thecontact_layout)
    LinearLayout thecontactLayout;
    @BindView(R2.id.no_friends)
    LinearLayout noFriends;
    @BindView(R2.id.cancel_edit)
    ImageView cancelEdit;
    TextWatcher textWatcher;
    private SearchFriendsPresenter searchFriendsPresenter;
    private String token;
    RequestOptions mRequestOptions;

    int id =0;

    @Override
    public Object setLayout() {
        return R.layout.delegate_searchfriends;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        searchFriendsPresenter = new SearchFriendsPresenter(this);
        token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        mRequestOptions = RequestOptions.circleCropTransform()
                // .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        showCancelEdit();
    }

    private void showCancelEdit() {
         textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(searchEdittext.getText().toString())||TextUtils.equals("",searchEdittext.getText().toString())){
                    cancelEdit.setVisibility(View.GONE);
                }else{
                    cancelEdit.setVisibility(View.VISIBLE);
                }
            }
        };
        searchEdittext.addTextChangedListener(textWatcher);
    }

    @OnClick({R2.id.return_button, R2.id.search_button, R2.id.thecontact_layout,R2.id.cancel_edit})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.return_button) {
            getSupportDelegate().pop();

        } else if (i == R.id.search_button) {
            String friends_phone = searchEdittext.getText().toString();
            searchFriendsPresenter.reqfriendsdetails(friends_phone, token);
        } else if (i == R.id.thecontact_layout) {
            //todo 点击好友 跳转详情
            searchFriendsPresenter.reqaddfriendsdetails(id,token);
        }else if (i == R.id.cancel_edit){
            //点击清空edittext
            searchEdittext.setText("");
        }
    }

    //没有好友的处理
    @Override
    public void respNoFriend() {
        noFriends.setVisibility(View.VISIBLE);
        thecontactLayout.setVisibility(View.GONE);
    }

    //查询成功的处理
    @Override
    public void respfriendsdetailsSuccess(JSONObject user) {
        if (user == null) {
            return;
        }
        noFriends.setVisibility(View.GONE);
        thecontactLayout.setVisibility(View.VISIBLE);
        String nickname = user.getString("nickname");
        String imagePath = user.getString("imagePath");
        id =user.getInteger("id");
        userName.setText(nickname);
        Glide.with(_mActivity)
                .load(imagePath)
                .apply(mRequestOptions)
                .into(userHead);
    }

    //查询错误的处理
    @Override
    public void respfriendsdetailsError(String message) {
        Toast.makeText(_mActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void respaddfriendsdetailsSuccess() {
        showToast("发送成功!");
    }

    @Override
    public void respaddfriendsdetailsError(String message) {
        showToast("添加失败："+message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R2.id.cancel_edit)
    public void onViewClicked() {
    }
}
