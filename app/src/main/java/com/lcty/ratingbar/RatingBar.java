package com.lcty.ratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dell on 2017/6/9.
 */

public class RatingBar extends View {

    private Bitmap mSelectedStar; //选中时的图片资源
    private Bitmap mNormalStar;   //正常时的图片资源
    private int mGrade = 5;           //总的分数 默认为5
    private int mCurrentPosition;
    private int mStarPaddingleft = 4;
    private int mStarPaddingRight = 4;
    private int mWidth; //一个星星的绘制宽度 包含左右的padding距离

    public RatingBar(Context context) {
        this(context, null);
    }

    public RatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        int normalId = array.getResourceId(R.styleable.RatingBar_startNormal, 0);
        int selectedId = array.getResourceId(R.styleable.RatingBar_startFocus, 0);
        mGrade = array.getInt(R.styleable.RatingBar_startNumber, mGrade);
        if (normalId == 0) {
            throw new RuntimeException("请设置默认图片资源");
        }

        if (selectedId == 0) {
            throw new RuntimeException("请设置选中图片资源");
        }
        //把图片资源转化为bitmap
        mNormalStar = BitmapFactory.decodeResource(getResources(), normalId);
        mSelectedStar = BitmapFactory.decodeResource(getResources(), selectedId);
        mStarPaddingRight = dp2px(mStarPaddingRight);
        mStarPaddingleft = dp2px(mStarPaddingleft);
        array.recycle();
    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = mNormalStar.getHeight()
                + getPaddingTop() + getPaddingBottom(); //高度等于上下间距 + 星星图片的高度
        mWidth = mNormalStar.getWidth() + mStarPaddingleft + mStarPaddingRight;

        int width = mWidth * mGrade;
        setMeasuredDimension(width, height); //设置宽高
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1.首先 把正常状态下的五角星画出来 通过for循环画五角星
        /**
         *  @param bitmap The bitmap to be drawn
         * @param left   The position of the left side of the bitmap being drawn
         * @param top    The position of the top side of the bitmap being drawn
         * @param paint  The paint used to draw the bitmap (may be null)
         */
        for (int i = 0; i < mGrade; i++) {

            if (mCurrentPosition > i) {
                //画选中的星星
                canvas.drawBitmap(mSelectedStar, i * mWidth + mStarPaddingleft, getPaddingTop(), null);
            } else {
                //画未选中的星星
                canvas.drawBitmap(mNormalStar, i * mWidth + mStarPaddingleft, getPaddingTop(), null);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                float x = event.getX(); //获得触摸控件手指的位置
                Log.e("RatingBar",x+"");
                int curPosition = (int) ((x / mWidth) + 1);
//                if (curPosition == mCurrentPosition) { //触摸在同一个控件的范围内，不进行重新绘制
//                    return true;
//                }
                mCurrentPosition = curPosition;
                invalidate(); //调用invalidata方法 不断的去重绘(调用ondraw方法)

                break;
        }
        return true;
    }
}
