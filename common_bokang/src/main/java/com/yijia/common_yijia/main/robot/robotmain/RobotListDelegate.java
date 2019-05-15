package com.yijia.common_yijia.main.robot.robotmain;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.latte.ec.R;
import com.example.yijia.delegates.bottom.BottomItemDelegate;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.friends.CommonStringClickListener;

public class RobotListDelegate extends BottomItemDelegate implements CommonStringClickListener {

    private ImageView imageButton;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private RobotMainListReFreshHandler robotMainListReFreshHandler = null;
    private String token = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_robot_list;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        initView(rootView);
        robotMainListReFreshHandler = RobotMainListReFreshHandler.create(refreshLayout, recyclerView, null, this, this, token);
    }

    private void initView(View rootView) {
        imageButton = rootView.findViewById(R.id.robot_list_add);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentDelegate().getSupportDelegate().start(new AddParentsDelegate());
            }
        });

        refreshLayout = rootView.findViewById(R.id.robot_list_refresh_layout);
        recyclerView = rootView.findViewById(R.id.robot_list_recycleview);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRefreshLayout();
        robotMainListReFreshHandler.firstPage();
    }

    private void initRefreshLayout() {
        refreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light

        );
        //第一个参数true：下拉的时候球由小变大，回弹时由大变小 第二个参数下降起始高度  第三个参数下降终止的高度
        refreshLayout.setProgressViewOffset(true, 120, 300);
    }

    @Override
    public void commonClick(String info) {
        RobotHisRobotDelegate mDelegate = new RobotHisRobotDelegate();
        Bundle bundle = new Bundle();
        long id = Long.parseLong(info);
        if (id == 0) {
            showToast("网络异常id=0");
            return;
        }
        bundle.putLong(RobotHisRobotDelegate.USERID, id);
        mDelegate.setArguments(bundle);
        getParentDelegate().getSupportDelegate().start(mDelegate);
    }
}
