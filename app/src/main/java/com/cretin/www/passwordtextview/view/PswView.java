package com.cretin.www.passwordtextview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.cretin.www.passwordtextview.R;

/**
 * Created by cretin on 2017/1/6.
 */

public class PswView extends View {
    //记录View的实际大小
    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    //记录密码个数
    private int mPointNums;
    //自定义属性
    private int mPsw_size;
    private int mPsw_color;
    private int mPsw_count;
    private int mBorder_color;
    //当前密码
    private String mCurrPsw = "";
    private OnPswChangedListener onPswChanged;

    public PswView(Context context) {
        this(context, null, 0);
    }

    public PswView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PswView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.PwdTextView);//获取配置属性
        mPsw_size = tArray.getDimensionPixelSize(R.styleable.PwdTextView_psw_size, 20);
        mPsw_color = tArray.getColor(R.styleable.PwdTextView_psw_color, Color.BLACK);
        mPsw_count = tArray.getInt(R.styleable.PwdTextView_psw_count, 6);
        mBorder_color = tArray.getColor(R.styleable.PwdTextView_border_color, Color.parseColor("#9b9b9b"));
        init(context);
    }

    //初始化
    private void init(Context context) {
        mPaint = new Paint();
        //设置抗锯齿，如果不设置，加载位图的时候可能会出现锯齿状的边界，如果设置，边界就会变的稍微有点模糊，锯齿就看不到了
        mPaint.setAntiAlias(true);
        //设置是否抖动，如果不设置感觉就会有一些僵硬的线条，如果设置图像就会看的更柔和一些，
        mPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 200;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int width;
        if ( widthMode == MeasureSpec.EXACTLY ) {
            width = widthSize;
        } else if ( widthMode == MeasureSpec.AT_MOST ) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }
        mWidth = width;
        mHeight = width / mPsw_count;
        //强制设置视图大小
        setMeasuredDimension(mWidth, mHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置基础画笔
        mPaint.setColor(mBorder_color);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.FILL);
        //画边框
        canvas.drawLine(1, 1, mWidth - 1, 1, mPaint);// 画线
        canvas.drawLine(1, 1, 1, mHeight - 1, mPaint);// 画线
        canvas.drawLine(1, mHeight - 1, mWidth - 1, mHeight - 1, mPaint);// 画线
        canvas.drawLine(mWidth - 1, 1, mWidth - 1, mHeight - 1, mPaint);// 画线
        //画竖线
        int w1 = mWidth / mPsw_count;
        for ( int i = 1; i < mPsw_count; i++ ) {
            canvas.drawLine(w1 * i, 1, w1 * i, mHeight - 1, mPaint);// 画线
        }
        //画实心圆 绘制密码
        int w = mWidth / (mPsw_count * 2);
        mPaint.setColor(mPsw_color);
        for ( int i = 0; i < mPointNums; i++ ) {
            canvas.drawCircle(w * (2 * i + 1), w, mPsw_size, mPaint);
        }
    }

    //设置数据
    public void setDatas(String psw) {
        //将传进来的值本地存储
        mCurrPsw = psw;
        int piontNums = psw.length();
        //防止越界
        if ( piontNums < 0 ) {
            piontNums = 0;
        }
        if ( piontNums > mPsw_count )
            piontNums = mPsw_count;
        mPointNums = piontNums;
        //重绘
        invalidate();
        //接口回调
        if ( onPswChanged != null )
            if ( psw.length() == mPsw_count ) {
                onPswChanged.onPswChanged(psw, true);
            } else {
                onPswChanged.onPswChanged(psw, false);
            }
    }

    public String getmCurrPsw() {
        return mCurrPsw;
    }

    public OnPswChangedListener getOnPswChanged() {
        return onPswChanged;
    }

    public void setOnPswChanged(OnPswChangedListener onPswChanged) {
        this.onPswChanged = onPswChanged;
    }

    interface OnPswChangedListener {
        void onPswChanged(String psw, boolean complete);
    }

    public int getmPsw_count() {
        return mPsw_count;
    }

    public void setmPsw_count(int mPsw_count) {
        this.mPsw_count = mPsw_count;
    }
}
