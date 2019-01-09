package com.example.latte.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.latte.ui.R;
import com.example.latte.ui.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用户中心 横排view
 */
public class UserTextLineView extends LinearLayout {

    // text
    @Nullable
    @BindView(R2.id.infoTv)
    TextView infoText;
    // 左侧icon
    @Nullable @BindView(R2.id.iconIv)
    ImageView iconImage;

    @Nullable @BindView(R2.id.dot)
    ImageView dot;

    @Nullable private Drawable iconDrawable;
    @Nullable private String text;
    @Nullable private ColorStateList textColor;
    private boolean hasArrow;

    public UserTextLineView(@NonNull Context context) {
        this(context, null, 0);
    }

    public UserTextLineView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserTextLineView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }


    private void initView(@NonNull Context context, AttributeSet attrs, int defStyleAttr){
        setOrientation(LinearLayout.HORIZONTAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UserTextLineView, defStyleAttr, 0);
        iconDrawable = a.getDrawable(R.styleable.UserTextLineView_iconDrawable);
        text = a.getString(R.styleable.UserTextLineView_text);
        //textSize = a.getDimensionPixelSize(R.styleable.UserTextLineView_textSize, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()));
        textColor = a.getColorStateList(R.styleable.UserTextLineView_textColor);
        hasArrow = a.getBoolean(R.styleable.UserTextLineView_hasArrow, true);
        a.recycle();

        View inflateView = View.inflate(context, R.layout.view_horizontaltextline, this);
        ButterKnife.bind(inflateView, this);

        if (iconDrawable != null) {
            iconImage.setImageDrawable(iconDrawable);
        }
        infoText.setText(!TextUtils.isEmpty(text) ? text : "");
        infoText.setTextColor(textColor != null ? textColor : ColorStateList.valueOf(getResources().getColor(R.color.light_black)));
    }

    public void setRedDotVisibility(boolean isVisible){
        dot.setVisibility(isVisible ? VISIBLE : GONE);
    }



}


