package com.yijia.common_yijia.main.robot.robotmain;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.yijia.delegates.bottom.BottomItemDelegate;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RobotMainDelegate extends BottomItemDelegate {

    private static final String[] CHANNELS = new String[]{"我的小壹", "被监护人"};
    private List<String> mDataList = Arrays.asList(CHANNELS);

    @BindView(R2.id.tv_save)
    AppCompatTextView tvSave;
    @BindView(R2.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R2.id.tv_back)
    RelativeLayout tvBack;
    @BindView(R2.id.view_pager)
    ViewPager mViewPager = null;
    @BindView(R2.id.tablayout)
    TabLayout mTabLayout;

    @OnClick(R2.id.tv_back)
    void back() {
        getSupportDelegate().pop();
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_robot_main;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initHead();
        initTabLayout();
//        initMagicIndicator4();
        initPager();
    }

    //初始化头布局
    private void initHead() {
        tvTitle.setText("机器人");
        tvSave.setVisibility(View.GONE);
        tvBack.setVisibility(View.GONE);
    }

    private void initTabLayout() {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setSelectedTabIndicatorColor
                (ContextCompat.getColor(getContext(), R.color.app_text_orange));
        mTabLayout.setTabTextColors(ColorStateList.valueOf(Color.BLACK));
        mTabLayout.setBackgroundColor(Color.WHITE);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initPager() {
        final PagerAdapter adapter = new RobotMainTabPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(adapter);
    }

//    private void initMagicIndicator4() {
//        CommonNavigator commonNavigator = new CommonNavigator(getContext());
//        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
//
//            @Override
//            public int getCount() {
//                return mDataList.size();
//            }
//
//            @Override
//            public IPagerTitleView getTitleView(Context context, final int index) {
//                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
//                simplePagerTitleView.setNormalColor(Color.GRAY);
//                simplePagerTitleView.setSelectedColor(Color.WHITE);
//                simplePagerTitleView.setText(mDataList.get(index));
//                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mViewPager.setCurrentItem(index);
//                    }
//                });
//                return simplePagerTitleView;
//            }
//
//            @Override
//            public IPagerIndicator getIndicator(Context context) {
//                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
//                linePagerIndicator.setMode(LinePagerIndicator.MODE_EXACTLY);
//                linePagerIndicator.setLineWidth(UIUtil.dip2px(context, 10));
//                linePagerIndicator.setColors(Color.WHITE);
//                return linePagerIndicator;
//            }
//        });
//        magicIndicator.setNavigator(commonNavigator);
//        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
//        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        titleContainer.setDividerDrawable(new ColorDrawable() {
//            @Override
//            public int getIntrinsicWidth() {
//                return UIUtil.dip2px(getContext(), 15);
//            }
//        });
//
//        final FragmentContainerHelper fragmentContainerHelper = new FragmentContainerHelper(magicIndicator);
//        fragmentContainerHelper.setInterpolator(new OvershootInterpolator(2.0f));
//        fragmentContainerHelper.setDuration(300);
//        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                fragmentContainerHelper.handlePageSelected(position);
//            }
//        });
//    }
}
