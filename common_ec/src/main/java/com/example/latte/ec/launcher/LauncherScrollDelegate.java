package com.example.latte.ec.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.latte.app.AccountManager;
import com.example.latte.app.IUserChecker;
import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ui.launcher.ILauncherListener;
import com.example.latte.ui.launcher.LauncherHolderCreator;
import com.example.latte.ui.launcher.OnLauncherFinishTag;
import com.example.latte.ui.launcher.ScrollLaunchTag;
import com.example.latte.util.storage.LattePreference;

import java.util.ArrayList;

public class LauncherScrollDelegate extends LatteDelegate implements OnItemClickListener {

    private ConvenientBanner<Integer> mConvenientBanner = null;
    private static final ArrayList<Integer> INTEGERS = new ArrayList<>();
    private ILauncherListener mILauncherListener = null;

    private void initBanner() {
        INTEGERS.add(R.mipmap.launcher_01);
        INTEGERS.add(R.mipmap.launcher_02);
        INTEGERS.add(R.mipmap.launcher_03);
        mConvenientBanner
                .setPages(new LauncherHolderCreator(), INTEGERS)
//                .setPages(new CBViewHolderCreator() {
//                    @Override
//                    public LauncherHolder createHolder(View itemView) {
//                        return new LauncherHolder(itemView);
//                    }
//
//                    @Override
//                    public int getLayoutId() {
//                        return R.layout.item_localimage;
//                    }
//                }, INTEGERS)
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(this)
                .setCanLoop(false)
        ;


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ILauncherListener) {
            mILauncherListener = (ILauncherListener) activity;
        }
    }

    @Override
    public Object setLayout() {
        mConvenientBanner = new ConvenientBanner<Integer>(getContext());
        return mConvenientBanner;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initBanner();
    }


    @Override
    public void onItemClick(int position) {
        //如果点击的事最后一个
        if (position == INTEGERS.size() - 1) {
            LattePreference.setAppFlag(ScrollLaunchTag.HAS_FIRST_LAUNCHER_APP.name(), true);
            //检查用户是否已经登陆
            AccountManager.checkAccont(new IUserChecker() {
                @Override
                public void onSignIn() {
                    if (mILauncherListener != null) {
                        mILauncherListener.onLauncherFinish(OnLauncherFinishTag.SIGNED);

                    }
                }

                @Override
                public void onNoSignIn() {
                    if (mILauncherListener != null) {
                        mILauncherListener.onLauncherFinish(OnLauncherFinishTag.NOT_SIGNED);

                    }
                }

            });
        }
    }
}
