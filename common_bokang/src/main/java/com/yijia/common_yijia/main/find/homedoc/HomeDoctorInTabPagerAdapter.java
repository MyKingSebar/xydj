package com.yijia.common_yijia.main.find.homedoc;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.latte.ui.recycler.MultipleItemEntity;
import com.yijia.common_yijia.main.message.trtc.PersonalChatFragment;
import com.yijia.common_yijia.main.message.trtc2.PersonalChatFragment2;
import com.yijia.common_yijia.main.message.trtc2.PersonalChatFragmentLittle;
import com.yijia.common_yijia.main.robot.robotmain.RobotGuardianshipDelegate;
import com.yijia.common_yijia.main.robot.robotmain.RobotMyRobotDelegate;

import java.util.ArrayList;
import java.util.List;


public class HomeDoctorInTabPagerAdapter extends FragmentStatePagerAdapter {

    private  List<MultipleItemEntity>  mDataList;

    public HomeDoctorInTabPagerAdapter(FragmentManager fm,List<MultipleItemEntity> mDataList) {
        super(fm);
        this.mDataList=mDataList;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView textView = new TextView(container.getContext());
        textView.setText(mDataList.get(position).getField(HomeDoctorInMultipleFields.DOCTNAME));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(24);
        container.addView(textView);
        return textView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

//    @Override
//    public int getItemPosition(Object object) {
//        TextView textView = (TextView) object;
//        String text = textView.getText().toString();
//        int index = mDataList.indexOf(text);
//        if (index >= 0) {
//            return index;
//        }
//        return POSITION_NONE;
//    }

    @Override
    public Fragment getItem(int position) {
        return PersonalChatFragmentLittle.create(mDataList.get(position).getField(HomeDoctorInMultipleFields.TENCENTIMID),mDataList.get(position).getField(HomeDoctorInMultipleFields.DOCTNAME),mDataList.get(position).getField(HomeDoctorInMultipleFields.DOCTHEADIMAGE));
    }

    @Override
    public int getCount() {
         return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDataList.get(position).getField(HomeDoctorInMultipleFields.DOCTNAME);
    }
}
