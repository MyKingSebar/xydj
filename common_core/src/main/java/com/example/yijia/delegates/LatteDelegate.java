package com.example.yijia.delegates;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.latte.R;
import com.example.yijia.app.Latte;
import com.example.yijia.ui.dialog.RxDialogShapeLoading;

public abstract class LatteDelegate extends PermissionCheckerDelegate {
    RxDialogShapeLoading rxDialogShapeLoading = null;

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
        if(getFragmentManager().getBackStackEntryCount() <= 1) {
            return false;
        } else {
            getSupportDelegate().pop();
            return true;
        }
    }
}
