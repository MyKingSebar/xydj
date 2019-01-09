package com.yijia.common_yijia.main.mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.latte.delegates.bottom.BottomItemDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.net.rx.BaseObserver;
import com.example.latte.net.rx.RxRestClient;
import com.example.latte.ui.widget.HeadLayout;
import com.example.latte.ui.widget.UserTextLineView;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.sign.YjBottomDelegate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MineDelegate extends BottomItemDelegate {

    @BindView(R2.id.head_layout)
    HeadLayout headLayout;
    Unbinder unbinder;
    @BindView(R2.id.head_portrait)
    LinearLayout headPortrait;
    @BindView(R2.id.photo_album)
    UserTextLineView photoAlbum;
    @BindView(R2.id.sayings)
    UserTextLineView sayings;
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
    Unbinder unbinder1;

    @Override
    public Object setLayout() {
        return R.layout.delegate_mine;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initMine();
    }

    private void initMine() {
        //初始化头布局
        headLayout.setHeadName("个人中心", "#FDBA63", 18);
        headLayout.setHeadlayoutBagColor("#ffffff");
    }

    @Override
    public void onResume() {
        super.onResume();
        final String url = "/user/query_user_info";
        String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        String phone = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getPhone();
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("type", 2)
                .params("keyword", phone)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        JSONObject object = JSON.parseObject(response);
                        String status = object.getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            getFriendsSucceed(response, object);
                        } else {
                            Toast.makeText(getContext(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(_mActivity, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });


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

    @OnClick({R2.id.head_portrait, R2.id.photo_album, R2.id.sayings, R2.id.About_us, R2.id.help, R2.id.set_up})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.head_portrait) {
            Toast.makeText(_mActivity, "头像", Toast.LENGTH_SHORT).show();
        } else if (i == R.id.photo_album) {
            Toast.makeText(_mActivity, "相册", Toast.LENGTH_SHORT).show();
        } else if (i == R.id.sayings) {
            Toast.makeText(_mActivity, "语录", Toast.LENGTH_SHORT).show();
        } else if (i == R.id.About_us) {
            Toast.makeText(_mActivity, "关于我们", Toast.LENGTH_SHORT).show();
        } else if (i == R.id.help) {
            Toast.makeText(_mActivity, "帮助", Toast.LENGTH_SHORT).show();
        } else if (i == R.id.set_up) {
            getParentDelegate().getSupportDelegate().start(new SetUpDelegate());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }
}
