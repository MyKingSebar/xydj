package com.yijia.common_yijia.main.find.healthself.CameraMesure;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.example.latte.ec.R;
import com.example.latte.ui.wxvideoedit.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangpanfeng@bokangzhixin.com on 2019/5/9.
 */
public class ProgressView extends View {

    private List<Point> mData = new ArrayList<>();
    private float total;

    /**
     * 横线
     */
    private Paint linePaint;

    /**
     * 横线下面的数字以及标题
     */
    private Paint digitPaint, textPaintName, tagTextPaint;

    /**
     * 横线上的图片
     */
    private Paint tagPaint;

    private int mWidth;

    private Context mContext;

    private int marginLeft = 0;

    private String name = "", per = "";

    private int currentValue = 0;

    public ProgressView(Context context) {
        super(context);

        mContext = context;
        initPaint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPaint();
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initPaint();
    }

    private void initPaint() {

        textPaintName = new Paint();
        textPaintName.setAntiAlias(true);
        textPaintName.setColor(ContextCompat.getColor(mContext, R.color.app_text_dark));
        textPaintName.setTextSize(32);

        digitPaint = new Paint();
        digitPaint.setAntiAlias(true);
        digitPaint.setColor(ContextCompat.getColor(mContext, R.color.text_FDBA63));
        digitPaint.setTextSize(40);

        tagTextPaint = new Paint();
        tagTextPaint.setAntiAlias(true);
        tagTextPaint.setColor(ContextCompat.getColor(mContext, R.color.COLOR_999999));
        tagTextPaint.setTextSize(28);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(UIUtil.dip2px(getContext(), 2));
        linePaint.setColor(ContextCompat.getColor(mContext, R.color.COLOR_49A3E1));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLines(canvas);

        drawTitle(canvas);

        drawValue(canvas);
    }

    private void drawValue(Canvas canvas) {
        Rect rectText = new Rect();
        digitPaint.getTextBounds(currentValue+"", 0, (currentValue+"").length(), rectText);
        float textWidth = rectText.width();//文字的宽
        canvas.drawText(currentValue+"", marginLeft + mWidth * currentValue / total - textWidth/2, 60, digitPaint);


        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.health_self_tag_image);
        canvas.drawBitmap(bitmap, marginLeft + mWidth * currentValue / total - bitmap.getWidth() / 2, 100 - bitmap.getHeight() - 5, null);
    }


    private void drawTitle(Canvas canvas) {
        canvas.drawText(name, marginLeft, 60, textPaintName);

        Rect rectText = new Rect();
        textPaintName.getTextBounds(per, 0, per.length(), rectText);
        float textWidth = rectText.width();//文字的宽
        canvas.drawText(per, mWidth - textWidth - 22, 60, textPaintName);

    }

    private void drawLines(Canvas canvas) {
        int length = mData.size();
        int lineLength = mWidth / (length + 1);
        float perValue = total / (length + 1);
        if (length == 1) {
            canvas.drawLine(marginLeft, 100, marginLeft + (int) (lineLength * mData.get(0).value / perValue), 100, linePaint);

            linePaint.setColor(ContextCompat.getColor(mContext, R.color.COLOR_7AC2A7));
            canvas.drawLine(marginLeft + (int) (lineLength * mData.get(0).value / perValue), 100, marginLeft + mWidth, 100, linePaint);

            Rect rectText = new Rect();
            tagTextPaint.getTextBounds(mData.get(0).value + "", 0, mData.get(0).value + "".length(), rectText);
            float textWidth = rectText.width();//文字的宽
            float textHeight = rectText.width();
            canvas.drawText(mData.get(0).value + "", (int) (lineLength * mData.get(0).value / perValue) - textWidth / 2, 100 + textHeight + 20, tagTextPaint);
        }

        if (length == 2) {

            canvas.drawLine(marginLeft, 100, marginLeft + (int) (lineLength * mData.get(0).value / perValue), 100, linePaint);

            linePaint.setColor(ContextCompat.getColor(mContext, R.color.COLOR_7AC2A7));
            canvas.drawLine(marginLeft + (int) (lineLength * mData.get(0).value / perValue), 100, marginLeft + (int) (lineLength * mData.get(1).value / perValue), 100, linePaint);

            linePaint.setColor(ContextCompat.getColor(mContext, R.color.COLOR_F3837E));
            canvas.drawLine(marginLeft + (int) (lineLength * mData.get(1).value / perValue), 100, marginLeft + mWidth, 100, linePaint);

            Rect rectText = new Rect();
            tagTextPaint.getTextBounds(mData.get(0).tag, 0, mData.get(0).tag.length(), rectText);
            float textWidth = rectText.width();//文字的宽
            float textHeight = rectText.height();
            canvas.drawText(mData.get(0).value + "", (int) (lineLength * mData.get(0).value / perValue) - textWidth / 2, 100 + textHeight + 20, tagTextPaint);

            Rect rectText1 = new Rect();
            tagTextPaint.getTextBounds(mData.get(1).tag, 0, mData.get(1).tag.length(), rectText1);
            float textWidth1 = rectText1.width();//文字的宽
            float textHeight1 = rectText1.height();
            canvas.drawText(mData.get(1).value + "", marginLeft + (int) (lineLength * mData.get(1).value / perValue) - textWidth1 / 2, 100 + textHeight1 + 20, tagTextPaint);
        }

    }

    public void setTitle(String name, String per) {
        this.name = name;
        this.per = per;
        invalidate();
    }

    public void setData(List<Point> points, float total) {
        mData = points;
        this.total = total;
        invalidate();
    }

    public void setValue(int value) {
        currentValue = value;
        invalidate();
    }

    public class Point {
        public String tag;
        public int value;
    }
}
