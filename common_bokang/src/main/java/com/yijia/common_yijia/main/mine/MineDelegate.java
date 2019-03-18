package com.yijia.common_yijia.main.mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.yijia.app.AccountManager;
import com.example.yijia.app.IUserChecker;
import com.example.yijia.delegates.bottom.BottomItemDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.widget.HeadLayout;
import com.example.latte.ui.widget.UserTextLineView;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.mine.personaldata.PersonalDataDelegate;
import com.yijia.common_yijia.main.mine.setup.SetUpDelegate;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的页面
 **/
public class MineDelegate extends BottomItemDelegate {

    @BindView(R2.id.head_layout)
    HeadLayout headLayout;
    @BindView(R2.id.head_portrait)
    LinearLayout headPortrait;
    @BindView(R2.id.photo_album)
    UserTextLineView photoAlbum;
    @BindView(R2.id.letter)
    UserTextLineView letter;

    @BindView(R2.id.remote_supervision)
    UserTextLineView remotesupervision;

    @BindView(R2.id.pro_card)
    UserTextLineView pro_card;

    @BindView(R2.id.About_us)
    UserTextLineView AboutUs;
    @BindView(R2.id.help)
    UserTextLineView help;
    @BindView(R2.id.set_up)
    UserTextLineView setUp;
    @BindView(R2.id.user_imagePath)
    ImageView userImagePath;
    @BindView(R2.id.user_nickname)
    TextView userNickname;
    RequestOptions mRequestOptions;

    @Override
    public Object setLayout() {
        return R.layout.delegate_mine;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        //初始化头布局
        initMineHead();
        mRequestOptions = RequestOptions.circleCropTransform()
                // .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        //判断是否登录
        AccountManager.checkAccont(new IUserChecker() {
            @Override
            public void onSignIn() {
                String nickname = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getNickname();
                String imagePath = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getImagePath();
                if (imagePath != null) {
                    Glide.with(_mActivity)
                            .load(imagePath)
                            .apply(mRequestOptions)
                            .into(userImagePath);
                } else {
                    Glide.with(_mActivity)
                            .load(R.mipmap.ic_launcher)
                            .apply(mRequestOptions)
                            .into(userImagePath);
                }
                if (nickname != null) {
                    userNickname.setText(nickname);
                } else {
                    userNickname.setText("点击设置您的昵称");
                }
            }

            @Override
            public void onNoSignIn() {
            }
        });
    }

    private void initMineHead() {
        //初始化头布局
        headLayout.setHeadName("个人中心", "#333333", 18);
        headLayout.setHeadlayoutBagColor("#ffffff");
    }

    @Override
    public void onResume() {
        super.onResume();
//        final String url = "/user/query_user_info";
//        AccountManager.checkAccont(new IUserChecker() {
//            @Override
//            public void onSignIn() {
//                String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
//                String phone = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getPhone();
//                RxRestClient.builder()
//                        .url(url)
//                        .params("yjtk", token)
//                        .params("type", 2)
//                        .params("keyword", phone)
//                        .build()
//                        .post()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new BaseObserver<String>(getContext()) {
//                            @Override
//                            public void onResponse(String response) {
//                                JSONObject object = JSON.parseObject(response);
//                                String status = object.getString("status");
//                                if (TextUtils.equals(status, "1001")) {
//                                    getFriendsSucceed(response, object);
//                                } else {
//                                    Toast.makeText(getContext(), object.getString("msg"), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                            @Override
//                            public void onFail(Throwable e) {
//                                Toast.makeText(_mActivity, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }
//            @Override
//            public void onNoSignIn() {}
//        });
    }

    private void getFriendsSucceed(String response, JSONObject object) {
        final JSONObject dataObject = object.getJSONObject("data");
        JSONObject user = dataObject.getJSONObject("user");
        String nickname = user.getString("nickname");
        String imagePath = user.getString("imagePath");
        userNickname.setText(nickname);
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);

        Glide.with(_mActivity)
                .load(imagePath)
                .apply(mRequestOptions)
                .into(userImagePath);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //点击每个条目的事件
    @OnClick({R2.id.head_portrait, R2.id.photo_album, R2.id.letter, R2.id.About_us, R2.id.help, R2.id.set_up, R2.id.pro_card, R2.id.remote_supervision})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.head_portrait) {
            //点击头像条目  跳转到个人资料页面
            getParentDelegate().getSupportDelegate().start(new PersonalDataDelegate());
        }
        else if (i == R.id.remote_supervision) {
            Toast.makeText(_mActivity, "远程监护", Toast.LENGTH_SHORT).show();
        }
        else if (i == R.id.pro_card) {
            Toast.makeText(_mActivity, "亲属卡", Toast.LENGTH_SHORT).show();
        } else if (i == R.id.photo_album) {
            Toast.makeText(_mActivity, "相册", Toast.LENGTH_SHORT).show();
        } else if (i == R.id.letter) {
            Toast.makeText(_mActivity, "家书", Toast.LENGTH_SHORT).show();
        } else if (i == R.id.About_us) {
            //跳转到关于我们
            getParentDelegate().getSupportDelegate().start(new AboutUsDelegate());
        } else if (i == R.id.help) {
            Toast.makeText(_mActivity, "帮助", Toast.LENGTH_SHORT).show();
        } else if (i == R.id.set_up) {
            //点击设置条目 跳转到设置页面
            getParentDelegate().getSupportDelegate().start(new SetUpDelegate());
        }
    }
}
