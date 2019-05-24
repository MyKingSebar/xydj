package com.yijia.common_yijia.main.robot.robotmain;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.latte.ec.R;
import com.example.yijia.delegates.LatteDelegate;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.sign.ISignListener;
import com.yijia.common_yijia.sign.YjSignHandler;

public class AddParentsDelegate extends LatteDelegate {

    public static final String EXTRA_ISFATHER = "is_father";
    public static final String EXTRA_ISFIRST_LOGIN = "is_first_login";
    private String token = null;

    private RelativeLayout titleLeft;
    private AppCompatTextView title, titleRight;
    private boolean isFirstLogin = false;

    private TextView addFather, addMather;

    @Override
    public Object setLayout() {
        return R.layout.delegate_add_parents;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        isFirstLogin = getArguments() == null ? false : getArguments().getBoolean(EXTRA_ISFIRST_LOGIN);
        initView(rootView);
    }

    private void initView(View rootView) {
        titleLeft = rootView.findViewById(R.id.tv_back);

        title = rootView.findViewById(R.id.tv_title);
        title.setVisibility(View.GONE);

        titleRight = rootView.findViewById(R.id.tv_save);
        if (isFirstLogin) {
            titleRight.setText(R.string.add_parents_skip);
            titleRight.setBackgroundResource(R.color.transparent);
            titleRight.setTextColor(getResources().getColor(R.color.text_FDBF6F));
            titleRight.setOnClickListener(v -> YjSignHandler.onSkipAddParents(signListener));
            titleLeft.setVisibility(View.GONE);
        } else {
            titleRight.setVisibility(View.GONE);
            titleLeft.setOnClickListener(v -> getSupportDelegate().pop());
        }

        addFather = rootView.findViewById(R.id.add_parents_father);
        addFather.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(EXTRA_ISFATHER, true);
            bundle.putBoolean(EXTRA_ISFIRST_LOGIN, isFirstLogin);
            ParentInfoDelegate parentInfoDelegate = new ParentInfoDelegate();
            parentInfoDelegate.setArguments(bundle);
            getSupportDelegate().start(parentInfoDelegate);
        });

        addMather = rootView.findViewById(R.id.add_parents_mather);
        addMather.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(EXTRA_ISFATHER, false);
            bundle.putBoolean(EXTRA_ISFIRST_LOGIN, isFirstLogin);
            ParentInfoDelegate parentInfoDelegate = new ParentInfoDelegate();
            parentInfoDelegate.setArguments(bundle);
            getSupportDelegate().start(parentInfoDelegate);
        });
    }

    private ISignListener signListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        signListener = (ISignListener) activity;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

}
