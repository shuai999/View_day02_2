package com.jackchen.view_day02_2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/3/17 12:08
 * Version 1.0
 * Params:
 * Description:   自定义TextView
*/

public class TextView extends LinearLayout {

    /**
     * 解决 自定义TextView 继承 LinearLayout（RelativeLayout、ViewGroup），文字不显示的3种解决方法
     *     第一种：复写dispatchDraw()方法；
     *     第二种：在布局文件中设置背景或者在代码中设置背景；
     *     第三种：在代码中设置
     */

    // 文字
    private String mText ;
    // 文字大小
    private int mTextSize = 15 ;
    // 文字颜色
    private int mTextColor = Color.BLACK ;
    // 画笔
    private Paint mPaint ;

    public TextView(Context context) {
        this(context,null);
    }

    public TextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TextView);

        // 获取文字
        mText = array.getString(R.styleable.TextView_jackchentext) ;
        // 获取文字颜色
        mTextColor = array.getColor(R.styleable.TextView_jackchentextColor , mTextColor) ;  // mTextColor表示上边定义的默认黑色
        // 获取文字大小
        mTextSize = array.getDimensionPixelSize(R.styleable.TextView_jackchentextSize , sp2px(mTextSize)) ;

        // 回收
        array.recycle();

        mPaint = new Paint() ;
        // 设置抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        // 设置文字大小和颜色
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);

        // 方法2：默认给一个透明的背景
//        setBackgroundColor(Color.TRANSPARENT);
        // 方法3：调用下边方法
        setWillNotDraw(false);

    }


    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,
                getResources().getDisplayMetrics());
    }


    /**
     * 测量文字
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 如果自定义TextView中的宽高给的是确定的值，比如10dp、20dp、match_parent，这个时候不需要计算，给的多少就是多少
        // 如果自定义TextView中的宽高给的是wrap_content，则需要计算宽高

        int widthMode = MeasureSpec.getMode(widthMeasureSpec) ;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec) ;

        // 1. 如果文字的大小给的是确定的值，比如10dp、20dp、match_parent，这个时候不需要计算，给的多少就是多少
        int width = MeasureSpec.getSize(widthMeasureSpec) ;
        // 2. 如果文字的大小给的是wrap_content，则需要计算大小
        if (widthMode == MeasureSpec.AT_MOST){
            // 区域
            // 计算的宽度 与字体的大小、字体长度有关
            Rect bounds = new Rect() ;
            // 获取TextView文本的区域
            // 参数1：要测量的文字 参数2：表示从位置0开始 参数3：表示到整个文字的长度
            mPaint.getTextBounds(mText , 0 , mText.length() , bounds);
            width = bounds.width() + getPaddingLeft() + getPaddingRight() ;
        }

        int height = MeasureSpec.getSize(heightMeasureSpec) ;
        if (heightMode == MeasureSpec.AT_MOST){
            // 区域
            Rect bounds = new Rect() ;
            mPaint.getTextBounds(mText , 0 , mText.length() , bounds);
            height = bounds.height() + getPaddingTop() + getPaddingBottom() ;
        }

        // 设置控件的宽高，这里就是给文字设置宽高
        setMeasuredDimension(width , height);

    }


    /**
     * 绘制文字
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 中心点：getHeight()/2
        // 参数1：文字 参数2：x 参数3：y 参数4：画笔
        // x：是文字开始的距离
        // y：是基线 baseLine 是要求的？    getHeight()/2是中心位置  已知

        // dy：是高度的一半到基线baseLine的位置
        // top：是baseLine到文字顶部的距离，是一个负值
        // bottom：是baseLine到文字底部的距离，是一个正值
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom ;

        int baseLine = getHeight()/2 + dy ;

        int x = getPaddingLeft() ;

        canvas.drawText(mText , x, baseLine, mPaint);
    }



//    /**
//     *
//     * 绘制文字
//     * @param canvas
//     */
//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
//        // 中心点：getHeight()/2
//        // 参数1：文字 参数2：x 参数3：y 参数4：画笔
//        // x：是文字开始的距离
//        // y：是基线 baseLine 是要求的？    getHeight()/2是中心位置  已知
//
//        // dy：是高度的一半到基线baseLine的位置
//        // top：是baseLine到文字顶部的距离，是一个负值
//        // bottom：是baseLine到文字底部的距离，是一个正值
//        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
//        int dy = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom ;
//
//        int baseLine = getHeight()/2 + dy ;
//
//        int x = getPaddingLeft() ;
//
//        canvas.drawText(mText , x, baseLine, mPaint);
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                 break;
            case MotionEvent.ACTION_MOVE:
                 break;
            case MotionEvent.ACTION_UP:

                 break;
        }

        invalidate();
        return super.onTouchEvent(event);
    }
}
