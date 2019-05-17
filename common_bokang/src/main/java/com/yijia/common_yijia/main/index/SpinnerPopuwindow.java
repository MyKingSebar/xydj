package com.yijia.common_yijia.main.index;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.baidu.ocr.ui.util.DimensionUtil;
import com.example.latte.ec.R;

/**
 * Created by wangpanfeng@bokangzhixin.com on 2019/5/16.
 */
public class SpinnerPopuwindow extends PopupWindow {
    private View conentView;
    private ListView listView;
    private Activity context;
    private ListAdapter adapter;
    /**
     * @param context 上下文
     * @param itemsOnClick listview在activity中的点击监听事件
     */
    @SuppressLint({"InflateParams", "WrongConstant"})
    public SpinnerPopuwindow(final Activity context, ListAdapter adapter,  AdapterView.OnItemClickListener itemsOnClick) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context =context;
        this.adapter = adapter;
        conentView = inflater.inflate(R.layout.popup_listview, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(DimensionUtil.dpToPx(130));
        //    this.setWidth(view.getWidth());
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 刷新状态
        this.update();
        this.setOutsideTouchable(false);
        // 实例化一个ColorDrawable颜色为半透明
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setBackgroundDrawable(context.getDrawable(R.drawable.radius5_background_white));
        }
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
            }
        });
        //解决软键盘挡住弹窗问题
        this.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // 设置SelectPicPopupWindow弹出窗体动画效果
        //  this.setAnimationStyle(R.style.AnimationPreview);

        listView = (ListView) conentView.findViewById(R.id.listView);
        listView.setOnItemClickListener(itemsOnClick);
        listView.setAdapter(adapter);

    }

    //获取选中列表中的数据所对应的position
    public int getText(){
        return listView.getCheckedItemPosition();
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            //  this.showAsDropDown(parent);
            // this.showAsDropDown(parent,0,10);
//            this.showAtLocation(parent, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
            darkenBackground(0.9f);//弹出时让页面背景回复给原来的颜色降低透明度，让背景看起来变成灰色
        }
    }
    /**
     * 关闭popupWindow
     */
    public void dismissPopupWindow() {
        this.dismiss();
        darkenBackground(1f);//关闭时让页面背景回复为原来的颜色

    }
    /**
     * 改变背景颜色，主要是在PopupWindow弹出时背景变化，通过透明度设置
     */
    private void darkenBackground(Float bgcolor){
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgcolor;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
}
