package com.example.latte.delegates;

import android.widget.Toast;

import com.example.latte.ui.dialog.RxDialogShapeLoading;

public abstract class LatteDelegate extends PermissionCheckerDelegate {
    RxDialogShapeLoading rxDialogShapeLoading=null;

    @SuppressWarnings("unchecked")
    public <T extends LatteDelegate> T getParentDelegate(){
        return (T)getParentFragment();
    }

    public void showToast(String s){
        Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
    }

    public void showDialog(){
        if(rxDialogShapeLoading==null){
            rxDialogShapeLoading = new RxDialogShapeLoading(getContext());
        }
        rxDialogShapeLoading.show();
    }

    public void closeDialog(){
        if(rxDialogShapeLoading!=null&&rxDialogShapeLoading.isShowing()){
            rxDialogShapeLoading.cancel();
        }
    }

}
