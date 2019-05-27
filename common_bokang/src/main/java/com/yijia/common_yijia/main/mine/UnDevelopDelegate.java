package com.yijia.common_yijia.main.mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.widget.HeadLayout;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.TextViewUtils;
import com.example.yijia.util.PackageUtils;
import com.google.gson.Gson;
import com.yijia.common_yijia.bean.Status;
import com.yijia.common_yijia.bean.VersionCheckBean;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UnDevelopDelegate extends LatteDelegate  {
private static final String KEY_TITLE="title";
    AppCompatTextView tvTitle, tvSave;
    RelativeLayout rl;
    private String title="";

    public static UnDevelopDelegate creat(String title){
        UnDevelopDelegate mUnDevelopDelegate=new UnDevelopDelegate();
        Bundle args=new Bundle();
        args.putString(KEY_TITLE,title);
        mUnDevelopDelegate.setArguments(args);
        return mUnDevelopDelegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        assert args != null;
        title=args.getString(KEY_TITLE);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_undevelop;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        //初始化头布局
        initHead(rootView);
    }

    //初始化头布局
    private void initHead(View rootView) {
        rl = rootView.findViewById(R.id.tv_back);
        rl.setOnClickListener(v -> getSupportDelegate().pop());
        tvTitle = rootView.findViewById(R.id.tv_title);
        tvSave = rootView.findViewById(R.id.tv_save);
        tvSave.setVisibility(View.INVISIBLE);
        TextViewUtils.AppCompatTextViewSetText(tvTitle, title);
    }

}

