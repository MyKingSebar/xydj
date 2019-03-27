package com.example.yijia.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

/**
 * @author JiaLei, Email 15033111957@163.com, Date on 2019/3/25.
 * PS: Not easy to write code, please indicate.
 */
public enum  JDialogUtil {
    //instance
    INSTANCE;
    private Dialog dialog=null;
    public void showRxDialogShapeLoading(Context context){
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
        dialog=new RxDialogShapeLoading(context);
        dialog.setCanceledOnTouchOutside(false);
         dialog.show();
    }
    public void showRxDialogSureCancel(Context context,String titletxt,int logo,String text,RxDialogSureCancelListener mRxDialogSureCancelListener){
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
        //提示弹窗
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(context);
        if(!TextUtils.isEmpty(titletxt)){
            rxDialogSureCancel.getTitleView().setText(titletxt);
        }
        if(logo!=0){
            rxDialogSureCancel.getLogoView().setBackgroundResource(logo);
        }
        if(!TextUtils.isEmpty(text)){
            rxDialogSureCancel.getContentView().setText(text);
        }
        rxDialogSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRxDialogSureCancelListener!=null){
                    mRxDialogSureCancelListener.RxDialogSure();
                }
            }
        });
        rxDialogSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRxDialogSureCancelListener!=null){
                    mRxDialogSureCancelListener.RxDialogCancel();
                }
            }
        });
        dialog=rxDialogSureCancel;
        dialog.show();
    }


    public void dismiss(){
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }
    
}
