package com.example.yijia.delegates;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.latte.R;
import com.example.yijia.app.Latte;
import com.example.yijia.ui.dialog.RxDialogShapeLoading;

import java.lang.reflect.Method;

public abstract class LatteDelegate extends PermissionCheckerDelegate {
    RxDialogShapeLoading rxDialogShapeLoading = null;
    private View decorView;

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        initBottomBar();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decorView = getActivity().getWindow().getDecorView();
    }

    public void initBottomBar(){
        int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide
                //  | View.SYSTEM_UI_FLAG_FULLSCREEN // 不隐藏状态栏，因为隐藏了比如时间电量等信息也会隐藏
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        //判断当前版本在4.0以上并且存在虚拟按键，否则不做操作
        if (Build.VERSION.SDK_INT < 19 || !checkDeviceHasNavigationBar()) {
            //一定要判断是否存在按键，否则在没有按键的手机调用会影响别的功能。如之前没有考虑到，导致图传全屏变成小屏显示。
            return;
        } else {
            //自定义工具，设置状态栏颜色是透明
//            ViewUtil.setWindowStatusBarColor(this,R.color.transparent);
            // 获取属性
            decorView.setSystemUiVisibility(flag);
        }
    }

    /**
     * 判断是否存在虚拟按键
     * @return
     */
    public boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }

    @SuppressWarnings("unchecked")
    public <T extends LatteDelegate> T getParentDelegate() {
        return (T) getParentFragment();
    }

    public void showToast(String s) {
        Context mContext = getContext();
        if (mContext == null) {
            return;
        }
        Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
    }

    public void showDialog() {
        if (rxDialogShapeLoading == null) {
            rxDialogShapeLoading = new RxDialogShapeLoading(getContext());
        }
        rxDialogShapeLoading.show();
    }

    public void closeDialog() {
        if (rxDialogShapeLoading != null && rxDialogShapeLoading.isShowing()) {
            rxDialogShapeLoading.cancel();
        }
    }

    public void hideInput() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //强制隐藏键盘
        imm.hideSoftInputFromWindow(this.getView().getWindowToken(), 0);
    }

    @Override
    public boolean onBackPressedSupport() {
        if(getFragmentManager().getBackStackEntryCount() == 0 || getFragmentManager().getBackStackEntryAt(0).getName().equals("com.yijia.common_yijia.sign.SignInNoteOnlyDelegate")) {
            return false;
        } else {
            getSupportDelegate().pop();
            return true;
        }
    }
}
