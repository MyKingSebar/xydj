package com.yijia.common_yijia.main.robot.callsetting;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * @author JiaLei
 * Created on 2019/5/8 20:25
 * E-Mail Address：15033111957@163.com
 */
public class RobotCallSettingTabPagerAdapter extends FragmentStatePagerAdapter{

        private final String[] TAB_TITLES = {"亲友", "通讯录","紧急呼叫"};

        public RobotCallSettingTabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new RobotCallSettingFriendListDelegate();
            } else if (position == 1) {
                return new RobotCallSettingAddressListDelegate();
            }else if (position == 2) {
                return new RobotCallSettingExigenceListDelegate();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position];
        }
}
