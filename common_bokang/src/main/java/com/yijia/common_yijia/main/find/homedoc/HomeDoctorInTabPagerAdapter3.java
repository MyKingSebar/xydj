package com.yijia.common_yijia.main.find.homedoc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.latte.ui.recycler.MultipleItemEntity;
import com.yijia.common_yijia.main.message.trtc2.PersonalChatFragmentLittle;
import com.yijia.common_yijia.sign.SignInNoteOnlyDelegate;

import java.util.List;


public class HomeDoctorInTabPagerAdapter3 extends FragmentStatePagerAdapter {

    private  List<MultipleItemEntity>  mDataList;

    public HomeDoctorInTabPagerAdapter3(FragmentManager fm, List<MultipleItemEntity> mDataList) {
        super(fm);
        this.mDataList=mDataList;
    }
    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }
    @Override
    public Fragment getItem(int position) {
        return PersonalChatFragmentLittle.create(mDataList.get(position).getField(HomeDoctorInMultipleFields.TENCENTIMID),mDataList.get(position).getField(HomeDoctorInMultipleFields.DOCTNAME),mDataList.get(position).getField(HomeDoctorInMultipleFields.DOCTHEADIMAGE),mDataList.get(position).getField(HomeDoctorInMultipleFields.MAJOR));
//        return new SignInNoteOnlyDelegate();
    }



//    @Override
//    public CharSequence getPageTitle(int position) {
//        return mDataList.get(position).getField(HomeDoctorInMultipleFields.DOCTNAME);
//    }
}
