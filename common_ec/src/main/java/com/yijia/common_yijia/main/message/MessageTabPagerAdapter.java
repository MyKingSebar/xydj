package com.yijia.common_yijia.main.message;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ec.detail.ImageDelegate;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.fragment.ConversationListFragment;


public class MessageTabPagerAdapter extends FragmentStatePagerAdapter {

//    private final ArrayList<String> TAB_TITLES = new ArrayList<>();
    private final String[] TAB_TITLES = {"对话","通知"};
    ConversationListFragment conversationListFragment;
    private final ArrayList<ArrayList<String>> PICTURES = new ArrayList<>();

    public MessageTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            if (conversationListFragment==null){
                return new ConversationListFragment();
            }else{
                return conversationListFragment;
            }

        } else if (position == 1) {
            return new RecyeleDelegate();
//            return ImageDelegate.create(PICTURES.get(1));
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
