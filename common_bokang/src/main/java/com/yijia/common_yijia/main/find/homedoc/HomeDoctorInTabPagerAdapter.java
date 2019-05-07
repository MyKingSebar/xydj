//package com.yijia.common_yijia.main.find.homedoc;
//
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//
//import com.example.latte.ui.recycler.MultipleItemEntity;
//import com.yijia.common_yijia.main.message.trtc.PersonalChatFragment;
//import com.yijia.common_yijia.main.message.trtc2.PersonalChatFragment2;
//import com.yijia.common_yijia.main.message.trtc2.PersonalChatFragmentLittle;
//import com.yijia.common_yijia.main.robot.robotmain.RobotGuardianshipDelegate;
//import com.yijia.common_yijia.main.robot.robotmain.RobotMyRobotDelegate;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class HomeDoctorInTabPagerAdapter extends FragmentStatePagerAdapter {
//
//    private  List<MultipleItemEntity>  mDataList;
//
//    public HomeDoctorInTabPagerAdapter(FragmentManager fm,List<MultipleItemEntity> mDataList) {
//        super(fm);
//        this.mDataList=mDataList;
//    }
//    @Override
//    public int getCount() {
//        return mDataList == null ? 0 : mDataList.size();
//    }
//    @Override
//    public Fragment getItem(int position) {
//        return PersonalChatFragmentLittle.create(mDataList.get(position).getField(HomeDoctorInMultipleFields.TENCENTIMID),mDataList.get(position).getField(HomeDoctorInMultipleFields.DOCTNAME),mDataList.get(position).getField(HomeDoctorInMultipleFields.DOCTHEADIMAGE),mDataList.get(position).getField(HomeDoctorInMultipleFields.MAJOR));
//    }
//
//
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return mDataList.get(position).getField(HomeDoctorInMultipleFields.DOCTNAME);
//    }
//}
