package com.jack.candyhh.customnumanimdemo.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.jack.candyhh.customnumanimdemo.R;
import com.jack.candyhh.customnumanimdemo.entity.CustomPoint;
import com.jack.candyhh.customnumanimdemo.evaluator.CustomPointEvaluator;
import com.jack.candyhh.customnumanimdemo.listener.CustomAnimListener;


/**
 * Created by jack on 17/2/24
 */

public class CustomNumAnimView extends View {

    private int roundColor;    //圆的颜色
    private int textColor;    //数字的颜色
    private float textSize;    //数字字体大小
    private float roundRadius;    //圆的半径

    private Paint mPaint;     //画笔
    private Rect textRect;    //包裹数字的矩形
    private boolean isFirstInit = false;   //是否是第一次初始化

    private CustomPoint leftPoint;    //左边的数字的实时点
    private ValueAnimator leftAnim;   //左边数字动画
    private String leftNum = "9";
    private boolean isLeftNumInvalidate = false;  //左边数字是否重绘界面

    private CustomPoint middlePoint;   //中间的数字的实时点
    private ValueAnimator middleAnim;   //中间数字动画
    private String middleNum = "9";
    private boolean isMiddleNumInvalidate = false;    //中间数字是否重绘界面

    private CustomPoint rightPoint;    //右边的数字的实时点
    private ValueAnimator rightAnim;   //右边数字动画
    private String rightNum = "9";
    private boolean isRightNumInvalidate = false;    //右边数字是否重绘界面

    public CustomNumAnimView(Context context) {
        this(context, null);
    }

    public CustomNumAnimView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomNumAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomNumAnimView, defStyleAttr, 0);
        roundColor = array.getColor(R.styleable.CustomNumAnimView_round_color, ContextCompat.getColor(context, R.color.colorPrimary));
        roundRadius = array.getDimension(R.styleable.CustomNumAnimView_round_radius, 50);
        textColor = array.getColor(R.styleable.CustomNumAnimView_text_color, Color.WHITE);
        textSize = array.getDimension(R.styleable.CustomNumAnimView_text_size, 30);
        array.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(textSize);
        textRect = new Rect();
        //得到数字矩形的宽高，以用来画数字的时候纠正数字的位置
        mPaint.getTextBounds(middleNum, 0, middleNum.length(), textRect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isFirstInit) {
            //初始化三串数字
            leftPoint = new CustomPoint(getMeasuredWidth() / 2 - textRect.width() / 2 - roundRadius / 2, (float) (getMeasuredHeight() / 2 - roundRadius * (Math.sqrt(3) / 2) - textRect.height() / 2));
            middlePoint = new CustomPoint(getMeasuredWidth() / 2 - textRect.width() / 2, getMeasuredHeight() / 2 - roundRadius - textRect.height() / 2);
            rightPoint = new CustomPoint(getMeasuredWidth() / 2 - textRect.width() / 2 + roundRadius / 2, (float) (getMeasuredHeight() / 2 - roundRadius * (Math.sqrt(3) / 2) - textRect.height() / 2));
            drawText(canvas);
            startAnimation();   //开始动画
            isFirstInit = true;
        } else {
            drawText(canvas);
        }
    }

    /**
     * 画数字
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(roundColor);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, roundRadius, mPaint);
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        if (isLeftNumInvalidate) {
            canvas.drawText(leftNum, leftPoint.getX(), leftPoint.getY(), mPaint);
            isLeftNumInvalidate = false;
        }
        if (isMiddleNumInvalidate) {
            canvas.drawText(middleNum, middlePoint.getX(), middlePoint.getY(), mPaint);
            isMiddleNumInvalidate = false;
        }
        if (isRightNumInvalidate) {
            canvas.drawText(rightNum, rightPoint.getX(), rightPoint.getY(), mPaint);
            isRightNumInvalidate = false;
        }
    }

    public void startAnim() {
        if (isAnimStart(leftAnim)) {
            leftAnim.start();
        }
        if (isAnimStart(middleAnim)) {
            middleAnim.start();
        }
        if (isAnimStart(rightAnim)) {
            rightAnim.start();
        }
    }

    private boolean isAnimStart(ValueAnimator anim) {
        return !anim.isStarted() || anim.isPaused();
    }

    public void pauseAnim() {
        if (isAnimStop(leftAnim)) {
            leftAnim.pause();
        }
        if (isAnimStop(middleAnim)) {
            middleAnim.pause();
        }
        if (isAnimStop(rightAnim)) {
            rightAnim.pause();
        }
    }

    /**
     * 在onDestroy方法中调用
     */
    public void stopAnim() {
        leftAnim.end();
        middleAnim.end();
        rightAnim.end();
        leftAnim = null;
        middleAnim = null;
        rightAnim = null;
    }

    private boolean isAnimStop(ValueAnimator anim) {
        return null != anim && anim.isRunning();
    }

    //开始动画
    private void startAnimation() {
        startLeft();
        startMiddle();
        startRight();
    }

    private void startLeft() {
        final CustomPoint startPoint = new CustomPoint(getMeasuredWidth() / 2 - textRect.width() / 2 - roundRadius / 2, (float) (getMeasuredHeight() / 2 - roundRadius * (Math.sqrt(3) / 2) - textRect.height() / 2));
        final CustomPoint endPoint = new CustomPoint(getMeasuredWidth() / 2 - textRect.width() / 2 - roundRadius / 2, (float) (getMeasuredHeight() / 2 + roundRadius * (Math.sqrt(3) / 2) + textRect.height() / 2));
        leftAnim = ValueAnimator.ofObject(new CustomPointEvaluator(), startPoint, endPoint);
        leftAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                leftPoint = (CustomPoint) animation.getAnimatedValue();
                isLeftNumInvalidate = true;
                invalidate();
            }
        });
        leftAnim.addListener(new CustomAnimListener() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                leftNum = getRandom();
            }
        });
        leftAnim.setStartDelay(100);
        leftAnim.setDuration(300);
        leftAnim.setRepeatCount(ValueAnimator.INFINITE);
    }

    private void startMiddle() {
        //初始化中间数字的开始点的位置
        final CustomPoint startPoint = new CustomPoint(getMeasuredWidth() / 2 - textRect.width() / 2, getMeasuredHeight() / 2 - roundRadius - textRect.height() / 2);
        //初始化中间数字的结束点的位置
        final CustomPoint endPoint = new CustomPoint(getMeasuredWidth() / 2 - textRect.width() / 2, getMeasuredHeight() / 2 + roundRadius + textRect.height() / 2);
        middleAnim = ValueAnimator.ofObject(new CustomPointEvaluator(), startPoint, endPoint);
        //监听从起始点到终点过程中点的变化,并获取点然后重新绘制界面
        middleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                middlePoint = (CustomPoint) animation.getAnimatedValue();
                isMiddleNumInvalidate = true;
                invalidate();
            }
        });
        middleAnim.addListener(new CustomAnimListener() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                middleNum = getRandom();
            }
        });
        middleAnim.setDuration(300);
        middleAnim.setRepeatCount(ValueAnimator.INFINITE);
    }

    private void startRight() {
        final CustomPoint startPoint = new CustomPoint(getMeasuredWidth() / 2 - textRect.width() / 2 + roundRadius / 2, (float) (getMeasuredHeight() / 2 - roundRadius * (Math.sqrt(3) / 2) - textRect.height() / 2));
        final CustomPoint endPoint = new CustomPoint(getMeasuredWidth() / 2 - textRect.width() / 2 + roundRadius / 2, (float) (getMeasuredHeight() / 2 + roundRadius * (Math.sqrt(3) / 2) + textRect.height() / 2));
        rightAnim = ValueAnimator.ofObject(new CustomPointEvaluator(), startPoint, endPoint);
        rightAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rightPoint = (CustomPoint) animation.getAnimatedValue();
                isRightNumInvalidate = true;
                invalidate();
            }
        });
        rightAnim.addListener(new CustomAnimListener() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                rightNum = getRandom();
            }
        });
        rightAnim.setStartDelay(150);
        rightAnim.setDuration(300);
        rightAnim.setRepeatCount(ValueAnimator.INFINITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size;
        int mode;
        int width;
        int height;
        size = MeasureSpec.getSize(widthMeasureSpec);
        mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {    //确定的值或者MATCH_PARENT
            width = size;
        } else {    //表示WARP_CONTENT
            width = (int) (2 * roundRadius);
        }

        mode = MeasureSpec.getMode(heightMeasureSpec);
        size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {    //确定的值或者MATCH_PARENT
            height = size;
        } else {    //表示WARP_CONTENT
            height = (int) (2 * roundRadius);
        }
        setMeasuredDimension(width, height);
    }

    /**
     * 获取0-9之间的随机数
     *
     * @return
     */
    private String getRandom() {
        int random = (int) (Math.random() * 9);
        return String.valueOf(random);
    }
}
