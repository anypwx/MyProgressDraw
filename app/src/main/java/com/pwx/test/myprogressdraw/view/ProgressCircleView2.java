package com.pwx.test.myprogressdraw.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.pwx.test.myprogressdraw.R;

import org.w3c.dom.Text;

/**
 * Created by pwx on 2017/8/23.
 */

public class ProgressCircleView2 extends View {
    private String mText;
    private int mTextColor;
    private int mTextSize;

    private Paint mpaint; //定义画笔
    private TextPaint mtpaint; //定义画笔
    private int circleR = 200; //圆直径
    private Rect rect;
    private RectF rectF;

    private int progressValue = 0;
    private int progressValue2 = 0;


    public ProgressCircleView2(Context context) {
        //注意这里 层层调用
        this(context,null);
    }

    public ProgressCircleView2(Context context, @Nullable AttributeSet attrs) {
        //注意这里层层调用
        this(context, attrs,0);
    }

    public ProgressCircleView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        //注意这里层层调用
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
        mtpaint = new TextPaint();
        rectF = new RectF();
        mpaint.setTextSize(mTextSize);
        rect = new Rect();
        //获取文本边界
        mpaint.getTextBounds(mText,0,mText.length(),rect);
        startAnimation();
        startAnimation2();


    }
//先测量出要画的VIEW的大小
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
            //设置文本大小
            mtpaint.setTextSize(mTextSize);
            //获取文本边界
            mtpaint.getTextBounds(mText,0,mText.length(),rect);
            //获取边界的宽
            float textWidth = rect.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
//            int desired = getPaddingLeft() + getWidth() + getPaddingRight();
            width = desired;
        }

        if(heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else {
            //同上的说明
            mtpaint.setTextSize(mTextSize);
            mtpaint.getTextBounds(mText,0,mText.length(),rect);
            float textHeight = rect.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
//            int desired = getPaddingTop() + getHeight() + getPaddingBottom();
            height = desired;
        }


        //最后设置测量后的宽和高
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    //再根据测量出的大小来画出布局，再画你要画的图案
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mpaint.setColor(Color.WHITE);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mpaint);

        mpaint.setColor(Color.BLUE);
        mpaint.setStyle(Paint.Style.STROKE);
        mpaint.setAntiAlias(true);
        mpaint.setDither(true);
        mpaint.setStrokeWidth(20);
        /**
         * 第一个参数是坐标x   第二个参数是坐标Y   第三个参数是控制圆的大小，半径
         */
        canvas.drawCircle(getMeasuredWidth()/2,getMeasuredHeight()/2,circleR,mpaint);

        mpaint.setColor(Color.RED);
        mpaint.setStyle(Paint.Style.STROKE);
        //设置当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，如圆形样式 Cap.ROUND,或方形样式Cap.SQUARE
        mpaint.setStrokeCap(Paint.Cap.ROUND);
        mpaint.setAntiAlias(true);
        mpaint.setDither(true);
        mpaint.setStrokeWidth(20);
        /**
         * 这里rectF的四个参数 如果要话圆弧的话，根据上面画圆的参数来，因为圆弧附着在圆上。
         * 公式  RectF rect = new RectF(center - radius, center - radius, center + radius, center + radius);
         */
        rectF.set(getMeasuredWidth()/2 -circleR,getMeasuredHeight()/2 -circleR,getMeasuredWidth()/2+circleR,getMeasuredHeight()/2+circleR);
        /**
         * 第二个参数 圆弧起始的角度
         * 第三个参数是圆弧的角度
         * 第四个参数 是否显示半径连线，true表示显示圆弧与圆心的半径连线，false表示不显示
         */
        canvas.drawArc(rectF,270,progressValue,false,mpaint);


        /**
         * 要想画出的文本能换行，必须使用这个TextPaint
         */
        mtpaint.setColor(mTextColor);
        mtpaint.setTextSize(50.0f);
        mtpaint.setStyle(Paint.Style.FILL);
        mtpaint.setAntiAlias(true);
        mtpaint.setDither(true);
        StaticLayout staticLayout = new StaticLayout("当前你的信用余额"+progressValue2+"元",mtpaint,circleR,Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F,true);
        canvas.save();
        canvas.translate(getMeasuredWidth()/2-(circleR/2),getMeasuredHeight()/2-(circleR/2));//从100，100开始画
        staticLayout.draw(canvas);
        canvas.restore();//别忘了restore
//        canvas.drawText(mText,getWidth()/2 - rect.width()/2,getHeight()/2 + rect.height()/2,mpaint);



    }

    /**
     * 更新圆环动画
     */
    private void startAnimation(){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,320);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                Log.i("any","动画更新的值："+value);
                progressValue = value;
                invalidate();

            }
        });
        valueAnimator.start();
    }


    /**
     * 更新文字数值动画
     */
    private void startAnimation2(){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,30000);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                Log.i("any","动画更新的值："+value);
                progressValue2 = value;
                invalidate();

            }
        });
        valueAnimator.start();
    }
}
