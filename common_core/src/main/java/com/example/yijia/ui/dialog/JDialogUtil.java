package com.example.yijia.ui.dialog;

import android.app.Dialog;
import android.content.Context;
/**
 * @author JiaLei, Email 15033111957@163.com, Date on 2019/3/25.
 * PS: Not easy to write code, please indicate.
 */
public enum  JDialogUtil {
    //instance
    INSTANCE;
    private Dialog dialog=null;
    public void showRxDialogShapeLoading(Context context){
        if (dialog.isShowing()){
            dialog.dismiss();
        }
        dialog=new RxDialogShapeLoading(context);
        dialog.setCanceledOnTouchOutside(false);
         dialog.show();
    }
    public void dismiss(){
        if (dialog.isShowing()){
            dialog.dismiss();
        }
    }
    
}
