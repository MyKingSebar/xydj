package com.yijia.common_yijia.main.personaldata;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.widget.HeadLayout;
import com.yijia.common_yijia.database.YjDatabaseManager;

import butterknife.BindView;

import butterknife.OnClick;


public class PersonalDataDelegate extends LatteDelegate implements HeadLayout.OnClickHeadReturn {
    @BindView(R2.id.head_layout)
    HeadLayout headLayout;
    @BindView(R2.id.head_portrait)
    ImageView headPortrait;
    @BindView(R2.id.head_portrait_layout)
    LinearLayout headPortraitLayout;
    @BindView(R2.id.user_name)
    TextView userName;
    @BindView(R2.id.user_name_layout)
    LinearLayout userNameLayout;
    RequestOptions options;

    @Override
    public Object setLayout() {
        return R.layout.delegate_personaldata;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        //从数据库里取头像和昵称
        String nickname = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getNickname();
        String imagePath = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getImagePath();
        if (imagePath != null) {
            Glide.with(_mActivity)
                    .load(imagePath)
                    .apply(options)
                    .into(headPortrait);
        } else {
            Glide.with(_mActivity)
                    .load(R.mipmap.ic_launcher)
                    .apply(options)
                    .into(headPortrait);
        }
        if (nickname != null) {
            userName.setText(nickname);
        } else {
            userName.setText("点击设置您的昵称");
        }
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        //初始化头布局
        initHeade();

        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(60);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,
        // 降低内存消耗
       options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);


    }
    //初始化头布局
    private void initHeade() {
        headLayout.setHeadleftImg(true, R.mipmap.fanhui);
        headLayout.setHeadName("个人资料", "#FDBA63", 18);
        headLayout.setHeadlayoutBagColor("#ffffff");
        headLayout.setOnClickHeadReturn(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (headLayout != null) {
            headLayout = null;
        }
    }

    @OnClick({R2.id.head_portrait_layout, R2.id.user_name_layout})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.head_portrait_layout) {
            //跳转到更改头像页面
            getSupportDelegate().start(new SetUserHeadDelegate());
        } else if (i == R.id.user_name_layout) {
            //跳转到更改昵称页面
            getSupportDelegate().start(new SetNicknameDelegate());
        }
    }

    @Override
    public void onClickHeadReturn() {
        getSupportDelegate().pop();
    }
}
