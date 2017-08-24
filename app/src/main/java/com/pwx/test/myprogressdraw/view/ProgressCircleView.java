package com.pwx.test.myprogressdraw.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.pwx.test.myprogressdraw.R;

/**
 * Created by pwx on 2017/8/23.
 */

public class ProgressCircleView extends View {
    private String mText;
    private int mTextColor;
    private int mTextSize;

    private Paint mpaint; //定义画笔
    private Rect rect;


    public ProgressCircleView(Context context) {
        this(context,null);
    }

    public ProgressCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProgressCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ProgressCircleView,defStyleAttr,0);
        int n = ta.getIndexCount();
        for (int i = 0; i < n; i++){
            //获取自定义的属性
            int attr = ta.getIndex(i);
            switch (attr){
                case R.styleable.ProgressCircleView_context_text:
                    //获取文本
                    mText = ta.getString(attr);
                    break;
                case R.styleable.ProgressCircleView_text_color:
                    //获取文本颜色
                    mTextColor = ta.getColor(attr,Color.BLACK);
                    break;
                case R.styleable.ProgressCircleView_text_font:
                    //获取文本大小   第二个参数设置默认值是16，可以转换成sp  也可以转换成px  看自己怎么样用
                    mTextSize = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,getResources().getDisplayMetrics()));
                    break;
            }
        }
        ta.recycle();


        //初始化画笔
        mpaint = new Paint();
        mpaint.setTextSize(mTextSize);
        rect = new Rect();
        //获取文本边界
        mpaint.getTextBounds(mText,0,mText.length(),rect);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);//获取测量宽的模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);//获取测量的宽
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);//获取测量高的模式
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);//获取测量的高

        int width;
        int height;
        if(widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else {
            /***
             * 这里算计出文本所需要的真实的大小
             * 所以，必须把文本的大小设置好后，才能真实的计算出来
             */
            //设置文本大小
            mpaint.setTextSize(mTextSize);
            //获取文本边界
            mpaint.getTextBounds(mText,0,mText.length(),rect);
            //获取边界的宽
            float textWidth = rect.width();
            //计算文本所需要的真实大小  包含左右的padding
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if(heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else {
            //同上的说明
            mpaint.setTextSize(mTextSize);
            mpaint.getTextBounds(mText,0,mText.length(),rect);
            float textHeight = rect.height();
            int resired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = resired;
        }


        //最后设置测量后的宽和高
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mpaint.setColor(Color.RED);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mpaint);

        mpaint.setColor(mTextColor);
        canvas.drawText(mText,getWidth()/2 - rect.width()/2,getHeight()/2 + rect.height()/2,mpaint);

    }
}
