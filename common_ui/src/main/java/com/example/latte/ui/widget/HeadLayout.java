package com.example.latte.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.latte.ui.R;

public class HeadLayout extends LinearLayout implements View.OnClickListener {
    private ViewStub headimage;
    private ViewStub headname;
    private ViewStub headreturn;
    private ViewStub headRightText1;
    private View inflate;
    private LinearLayout headlayout;
    private OnClickHeadReturn onClickHeadReturn;//返回键
    private OnClickHeadRighttext onClickHeadRighttext;//最右的textview
    private OnClickHeadHeadImage onClickHeadHeadImage;
    private ViewGroup.LayoutParams layoutParams1;
    private LinearLayout head_left_linear;

    public HeadLayout(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public HeadLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public HeadLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    //加载布局
    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        inflate = View.inflate(context, R.layout.headlayout, this);
        headimage = findViewById(R.id.head_right_image);
        headname = findViewById(R.id.head_name);
        headreturn = findViewById(R.id.head_return);
        headRightText1 = findViewById(R.id.head_right_text);
        headlayout = findViewById(R.id.head_layout);
        head_left_linear = findViewById(R.id.head_return_linear);
//        headreturn.setOnClickListener(this);
//        headRightText1.setOnClickListener(this);
//        headimage.setOnClickListener(this);
    }


//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
//        ViewGroup.LayoutParams layoutParams1 =
//                new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, heightMode);
//        headlayout.setLayoutParams(layoutParams1);
//        ViewGroup.LayoutParams layoutParams = getLayoutParams();
//        int width = measureSelfWidthOrHeight(MeasureSpec.getMode(widthMeasureSpec),
//                MeasureSpec.getSize(widthMeasureSpec),
//                getPaddingLeft() + getPaddingRight(),
//                layoutParams.width, getSuggestedMinimumWidth());
//        int height = measureSelfWidthOrHeight(MeasureSpec.getMode(heightMeasureSpec),
//                MeasureSpec.getSize(heightMeasureSpec),
//                getPaddingTop() + getPaddingBottom(),
//                layoutParams.height, getSuggestedMinimumHeight());
//        setMeasuredDimension(width, height);
//        for (int i = 0; i < getChildCount(); i++) {
//            if (getChildAt(i).getVisibility() != GONE) {
//                getChildAt(i).measure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(width), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(height), MeasureSpec.EXACTLY));
//            }
//        }
//    }
//
//    private int measureSelfWidthOrHeight(int heightMode, int heightSize, int extraHeight, int layoutParamHeight, int suggestedMinHeight) {
//        int height = 0;
//        switch (heightMode) {
//            case MeasureSpec.EXACTLY: // 高度是确定的
//                height = heightSize;
//                break;
//            case MeasureSpec.AT_MOST: // AT_MOST一般是因为设置了wrap_content属性获得，但不全是这
//                if (layoutParamHeight == LayoutParams.WRAP_CONTENT) {
//                    int disert = Math.max(suggestedMinHeight, extraHeight);
//                    height = Math.min(disert, heightSize);
//                } else if (layoutParamHeight == LayoutParams.MATCH_PARENT) {
//                    height = heightSize;
//                } else {
//                    height = Math.min(layoutParamHeight + extraHeight, heightSize);
//                }
//                break;
//            case MeasureSpec.UNSPECIFIED:
//                if (layoutParamHeight == LayoutParams.WRAP_CONTENT || layoutParamHeight == LayoutParams.MATCH_PARENT) {
//                    height = Math.max(suggestedMinHeight, extraHeight);
//                } else {
//                    height = layoutParamHeight + extraHeight;
//                }
//                break;
//            default:
//        }
//        return height;
//    }


    //设置左边图片 和是否显示 默认 true 显示。 false 显示，false 不显示(不显示时imgId可传null)
    public void setHeadleftImg(boolean isimage, int imgId) {
        if (isimage) {
            head_left_linear.setVisibility(VISIBLE);
            //headreturn.setVisibility(VISIBLE);
            //headreturn.setImageDrawable(getResources().getDrawable(imgId));
            View inflate = headreturn.inflate();
            ImageView head_left = inflate.findViewById(R.id.head_left);
            head_left.setImageDrawable(getResources().getDrawable(imgId));
            head_left_linear.setOnClickListener(this);
        } else {
            headreturn.setVisibility(GONE);
            //headreturn.setImageDrawable(getResources().getDrawable(imgId));
        }
    }

    //设置头布局名字
    public void setHeadName(String name, String textColor, int textsize) {
        View inflate = headname.inflate();
        TextView headname = inflate.findViewById(R.id.head_name);
        headname.setTextColor(Color.parseColor(textColor));
        headname.setText(name);
        headname.setTextSize(TypedValue.COMPLEX_UNIT_SP, textsize);
    }

    //设置右边的图片 和是否显示 默认 false 不显示。 true 显示，false 不显示
    public void setRightHeadImage(int imgId, boolean isimage) {
        /**
         * 注：最右边的图片和文字只能存在一个
         */
        if (isimage) {
            View inflate = headimage.inflate();
            ImageView headrightimage = inflate.findViewById(R.id.head_right_image);
            headRightText1.setVisibility(GONE);
            headrightimage.setImageDrawable(getResources().getDrawable(imgId));
            headrightimage.setOnClickListener(this);
        }
    }

    //设置右边的文字是否显示 和设置文字，默认false 不显示。true 显示，false 不显示

    public void setHeadRightText(String name, boolean istext,String textColor) {
        /**
         * 注：最右边的图片和文字只能存在一个
         */
        if (istext) {
            View inflate = headRightText1.inflate();
//                headRightText1.setVisibility(VISIBLE);
            TextView viewById = inflate.findViewById(R.id.head_right_text);
            headimage.setVisibility(GONE);
            viewById.setText(name);
            viewById.setTextColor(Color.parseColor(textColor));
            viewById.setOnClickListener(this);
        }
    }
    //设置整体背景颜色

    /***
     * 必须是十六进制 如红色 为 #ff0000
     */
    public void setHeadlayoutBagColor(String color) {
        headlayout.setBackgroundColor(Color.parseColor(color));//
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.head_return_linear) {
            onClickHeadReturn.onClickHeadReturn();

        } else if (i == R.id.head_right_image) {
            onClickHeadHeadImage.onClickHeadHeadImage();

        } else if (i == R.id.head_right_text) {
            onClickHeadRighttext.onClickHeadRighttext();

        }
    }

    //返回键的单击回调
    public void setOnClickHeadReturn(OnClickHeadReturn onClickHeadReturn) {
        this.onClickHeadReturn = onClickHeadReturn;
    }

    //最右的textview的单击回调
    public void setOnClickHeadRighttext(OnClickHeadRighttext onClickHeadRighttext) {
        this.onClickHeadRighttext = onClickHeadRighttext;
    }

    //最右边的imageview的单击回调
    public void setOnClickHeadRightImage(OnClickHeadHeadImage onClickHeadHeadImage) {
        this.onClickHeadHeadImage = onClickHeadHeadImage;
    }

    public interface OnClickHeadReturn {
        void onClickHeadReturn();
    }

    public interface OnClickHeadRighttext {
        void onClickHeadRighttext();
    }

    public interface OnClickHeadHeadImage {
        void onClickHeadHeadImage();
    }

}
