package com.yijia.common_yijia.main.robot;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yijia.common_yijia.main.message.trtc.session.SessionFragment;
import com.yijia.common_yijia.main.message.view.fragment.NoticeDelegate;

import java.util.ArrayList;


public class RobotMainTabPagerAdapter extends FragmentStatePagerAdapter {

//    private final ArrayList<String> TAB_TITLES = new ArrayList<>();
    private final String[] TAB_TITLES = {"我的小壹 ","被监护人"};
    private final ArrayList<ArrayList<String>> PICTURES = new ArrayList<>();

    public RobotMainTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new RobotMyRobotDelegate();
        } else if (position == 1) {
            return new RobotGuardianshipDelegate();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }
}
