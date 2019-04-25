package com.yijia.common_yijia.main.robot.robotmain;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.latte.ec.R;
import com.example.yijia.delegates.LatteDelegate;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.friends.CommonClickListener;

public class RobotGuardianshipDelegate extends LatteDelegate implements CommonClickListener {
    RecyclerView mRecyelerView=null;
    String token = null;
    private RobotGuardianshipReFreshHandler mRobotGuardianshipReFreshHandler = null;
    SwipeRefreshLayout mRefreshLayout = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_robot_guardianship;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        token=YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        initVIew(rootView);
        mRobotGuardianshipReFreshHandler=RobotGuardianshipReFreshHandler.create(mRefreshLayout,mRecyelerView,null,this,this,token);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRefreshLayout();
        Log.d("refresh", "onLazyInitView");
        mRobotGuardianshipReFreshHandler.firstPage();
    }

    private void initVIew(View rootView) {
        mRecyelerView = rootView.findViewById(R.id.rv_guardianship);
        mRefreshLayout=rootView.findViewById(R.id.srl_index);
    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light

        );
        //第一个参数true：下拉的时候球由小变大，回弹时由大变小 第二个参数下降起始高度  第三个参数下降终止的高度
        mRefreshLayout.setProgressViewOffset(true, 120, 300);
    }


    @Override
    public void commonClick(String info) {
        RobotHisRobotDelegate mDelegate=new RobotHisRobotDelegate();
        Bundle bundle=new Bundle();
        long id=Long.parseLong(info);
        if(id==0){
            showToast("网络异常id=0");
            return;
        }
        bundle.putLong(RobotHisRobotDelegate.USERID,id);
        mDelegate.setArguments(bundle);
        getParentDelegate().getSupportDelegate().start(mDelegate);
    }
}
