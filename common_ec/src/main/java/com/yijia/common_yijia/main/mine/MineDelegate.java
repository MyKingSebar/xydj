package com.yijia.common_yijia.main.mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.latte.delegates.bottom.BottomItemDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.net.rx.BaseObserver;
import com.example.latte.net.rx.RxRestClient;
import com.example.latte.ui.widget.HeadLayout;
import com.example.latte.ui.widget.UserTextLineView;
import com.yijia.common_yijia.database.YjDatabaseManager;

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
                .params("type",phone)
                .params("keyword",phone)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String s) {
                        Toast.makeText(_mActivity, s, Toast.LENGTH_SHORT).show();
                        Log.e("user_name_yangfan", "onResponse: "+s );
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(_mActivity, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
            Toast.makeText(_mActivity, "设置", Toast.LENGTH_SHORT).show();
        }
    }
}
