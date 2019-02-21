package com.yijia.common_yijia.main.mine.personaldata;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;

import com.example.latte.ui.widget.HeadLayout;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.database.YjUserProfile;
import com.yijia.common_yijia.main.mine.setup.UpDateUtils;

import butterknife.BindView;

public class SetNicknameDelegate extends LatteDelegate implements HeadLayout.OnClickHeadReturn, HeadLayout.OnClickHeadRighttext {
    @BindView(R2.id.head_layout)
    HeadLayout headLayout;
    @BindView(R2.id.set_name)
    EditText setName;

    @Override
    public Object setLayout() {
        return R.layout.delegate_setnickname;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initHeade();
        String nickname = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getNickname();
        if (nickname!=null){
            setName.setText(nickname);
        }
    }

    //初始化头布局
    private void initHeade() {
        headLayout.setHeadleftImg(true, R.mipmap.fanhui);
        headLayout.setHeadName("更改昵称", "#333333", 18);
        headLayout.setHeadRightText("保存", true, "#333333");
        headLayout.setHeadlayoutBagColor("#ffffff");
        headLayout.setOnClickHeadReturn(this);
        headLayout.setOnClickHeadRighttext(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (headLayout != null) {
            headLayout = null;
        }
    }

    //点击返回
    @Override
    public void onClickHeadReturn() {
        getSupportDelegate().pop();
    }

    //点击保存
    @Override
    public void onClickHeadRighttext() {
        String new_name = setName.getText().toString();
     UpDateUtils.updatePersonalData(_mActivity, new_name, null, new UpDateUtils.UpDateSuccessAndError() {
            @Override
            public void successAndError(UpDateUtils.UpDatePersonal upDatePersonal) {
                if (upDatePersonal == UpDateUtils.UpDatePersonal.SUCCESS){
                    YjUserProfile profile = YjDatabaseManager.getInstance().getDao().loadAll().get(0);
                    profile.setNickname(new_name);
                    YjDatabaseManager.getInstance().getDao().update(profile);
                    Toast.makeText(_mActivity, "修改昵称成功", Toast.LENGTH_SHORT).show();
                    getSupportDelegate().pop();
                }else{
                    Toast.makeText(_mActivity, "修改昵称失败", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
