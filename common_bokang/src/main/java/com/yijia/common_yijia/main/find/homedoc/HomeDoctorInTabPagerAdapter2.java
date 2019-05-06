package com.yijia.common_yijia.main.find.homedoc;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.latte.ui.recycler.MultipleItemEntity;
import com.yijia.common_yijia.main.message.trtc2.PersonalChatFragmentLittle;

import java.util.List;


public class HomeDoctorInTabPagerAdapter2 extends PagerAdapter {

    private  List<MultipleItemEntity>  mDataList;

    public HomeDoctorInTabPagerAdapter2( List<MultipleItemEntity> mDataList) {

        this.mDataList=mDataList;
    }

    @Override
    public int getCount() {
         return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        PersonalChatFragmentLittle textView = new PersonalChatFragmentLittle(container.getContext());
//        textView.setText(mDataList.get(position));
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextColor(Color.BLACK);
//        textView.setTextSize(24);
//        container.addView(textView);
//        return textView;
         return PersonalChatFragmentLittle.create(mDataList.get(position).getField(HomeDoctorInMultipleFields.TENCENTIMID),mDataList.get(position).getField(HomeDoctorInMultipleFields.DOCTNAME),mDataList.get(position).getField(HomeDoctorInMultipleFields.DOCTHEADIMAGE));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
//        TextView textView = (TextView) object;
//        String text = textView.getText().toString();
//        int index = mDataList.indexOf(text);
//        if (index >= 0) {
//            return index;
//        }
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDataList.get(position).getField(HomeDoctorInMultipleFields.DOCTNAME);
    }
}
