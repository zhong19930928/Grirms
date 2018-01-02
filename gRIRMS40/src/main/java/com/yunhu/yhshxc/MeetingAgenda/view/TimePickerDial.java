package com.yunhu.yhshxc.MeetingAgenda.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by qingli on 2017/8/31.
 * 自定义时间表盘选择器
 */

public class TimePickerDial extends View {

    private Paint linePaint;
    private TextPaint textPaint;
    private int viewWidth;//当前VIEW的宽度
    private int viewHeight;//当前Viewde 高度
    private Point centerPoint;//表盘圆心
    private int rm = 10;//防止边界问题,设置差值
    private boolean isInit = true;//第一次为初始,指针于0点上
    private int pointLength = 0;//指针的长度
    private double currentAngle;//当前角度
    private int whichExtra = 1;//记录指针在哪个象限区域

    public TimePickerDial(Context context) {
        this(context, null);
    }

    public TimePickerDial(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimePickerDial(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    //初始化数据
    private void initView(Context context) {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.GRAY);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(20);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        linePaint.setColor(Color.GRAY);
        canvas.save();
        //绘制圆
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(5);
        int radius = viewWidth / 2;
        canvas.drawCircle(centerPoint.x, centerPoint.y, radius - rm, linePaint);
        //绘制圆心
        linePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerPoint.x, centerPoint.y, radius / 30, linePaint);
        //绘制24个时间数字
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3);

        //绘制整点线
        for (int i = 8; i < 20; i++) {
            canvas.rotate(30, centerPoint.x, centerPoint.y);
            int text = i + 1;
            if (i == 19) {
                text = 8;
            }
            canvas.drawLine(viewWidth / 2, viewHeight / 2 - radius + rm, viewWidth / 2, viewHeight / 2 - radius + 30 + rm, linePaint);
            float textWidth = textPaint.measureText(text + "");
            int textY = viewHeight / 2 - radius + 30 + 15 + rm;
            int textX = (int) (viewWidth / 2 - textWidth / 2);
            canvas.drawText(text + "", textX, textY, textPaint);
        }
        for (int i = 0; i < 12; i++) {
            if (i == 0) {
                canvas.rotate((float) (360 / 24), centerPoint.x, centerPoint.y);
            } else {
                canvas.rotate(30, centerPoint.x, centerPoint.y);
            }
            canvas.drawLine(viewWidth / 2, viewHeight / 2 - radius + rm, viewWidth / 2, viewHeight / 2 - radius + 15 + rm, linePaint);
        }

        canvas.restore();
        //绘制表针
        linePaint.setStrokeWidth(6);
        linePaint.setColor(Color.BLUE);
        if (isInit) {
            canvas.drawLine(viewWidth / 2, viewHeight / 2 - radius + 30 + 15 + rm + 10, centerPoint.x, centerPoint.y, linePaint);
            pointLength = centerPoint.y - (viewHeight / 2 - radius + 30 + 15 + rm + 10);
            //初始指针范围
            handX1 = viewWidth / 2 - 10;
            handX2 = viewWidth / 2 + 10;
            handY1 = viewHeight / 2 - radius + 30 + 15 + rm + 10;
            handY2 = centerPoint.y;
        } else {
            //处理滑动后的指针位置
            canvas.drawLine(centerPoint.x, centerPoint.y, rx, ry, linePaint);
            calcuteResultTime();

        }


    }

    //计算此刻选择的时间
    private void calcuteResultTime() {
        int allTime = 12 * 60;//分钟
        float bt = (float) currentAngle / 360;
        int thisTime = (int) (allTime * bt);
        //将分钟数转化为时间
        int hour = 8+(thisTime / 60);//小时
        int minute = thisTime - (hour-8) * 60;
        String hourStr = hour >= 10 ? hour + "" : "0" + hour;
        String minStr = minute >= 10 ? minute + "" : "0" + minute;
        if (mListener != null) {
            mListener.resultTime(hourStr + ":" + minStr, thisTime);
        }
//        Log.d("views",hourStr + ":" + minStr);

    }

    //记录当前指针范围的四个坐标点
    private int handX1, handX2, handY1, handY2;
    //是否触摸到了指针
    private boolean isTounchHand = false;

    float rx, ry;//记录滑动后的指针终点位置

    boolean isUped=false;

    /**
     * 如果触摸点位于表针范围内,则根据触摸点的旋转角度进行相应的旋转
     * 落于非触摸点则不进行任何操作
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x1 = event.getX();
        float y1 = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                isInit = false;//
                //根据触摸位置的点计算出一个指针长度的点,然后画新的指针线,还要计算出旋转的角度,以便计算出所选时间
                //判断当前点属于第几象限
                if (x1 > centerPoint.x && y1 < centerPoint.y) {//第一象限
                    whichExtra = 1;
                    float a1 = x1 - centerPoint.x;
                    float b1 = centerPoint.y - y1;
                    float c1 = (float) Math.sqrt((a1 * a1 + b1 * b1));
                    float a2, b2;
                    a2 = a1 * pointLength / c1;
                    b2 = (float) Math.sqrt(pointLength * pointLength - a2 * a2);
                    rx = a2 + centerPoint.x;
                    ry = centerPoint.y - b2;
                    //超出表盘圆范围则无效
                    if (x1 > rx && !isUped) {
                        break;
                    }
                    //计算正切角度
                    //tana = a1/b1;
                    currentAngle = Math.atan2(a1, b1) / (Math.PI / 180);
                    //四舍五入
//                    currentAngle = Math.round(currentAngle-0.5);

                } else if (x1 > centerPoint.x && y1 > centerPoint.y) {//第二象限
                    whichExtra = 2;
                    //旋转角度在已经拥有90度的基础上进行
                    float a1 = x1 - centerPoint.x;
                    float b1 = y1 - centerPoint.y;
                    float c1 = (float) Math.sqrt((a1 * a1 + b1 * b1));
                    float a2, b2;
                    a2 = a1 * pointLength / c1;
                    b2 = (float) Math.sqrt(pointLength * pointLength - a2 * a2);
                    rx = a2 + centerPoint.x;
                    ry = b2 + centerPoint.y;
                    //超出表盘圆范围则无效
                    if (x1 > rx&& !isUped) {
                        break;
                    }
                    //计算正切的角度
                    currentAngle = Math.atan2(b1, a1) / (Math.PI / 180);
                    //四舍五入
//                    currentAngle = (Math.round(currentAngle-0.5) + 90);
                    currentAngle = currentAngle + 90;


                } else if (x1 < centerPoint.x && y1 > centerPoint.y) {//第三象限
                    whichExtra = 3;
                    //旋转角度在已经有180度的基础上进行
                    float a1 = centerPoint.x - x1;
                    float b1 = y1 - centerPoint.y;
                    float c1 = (float) Math.sqrt((a1 * a1 + b1 * b1));
                    float a2, b2;
                    b2 = b1 * pointLength / c1;
                    a2 = (float) Math.sqrt(pointLength * pointLength - b2 * b2);
                    rx = centerPoint.x - a2;
                    ry = centerPoint.y + b2;
                    //超出表盘圆范围则无效
                    if (x1 < rx&& !isUped) {
                        break;
                    }
                    //计算正切角度
                    currentAngle = Math.atan2(a1, b1) / (Math.PI / 180);
                    //四舍五入
//                    currentAngle = (Math.round(currentAngle-0.5) + 180);
                    currentAngle = currentAngle + 180;

                } else if (x1 < centerPoint.x && y1 < centerPoint.y) {//第四象限
                    whichExtra = 4;
                    //旋转角度在已拥有270度的基础上进行
                    float a1 = centerPoint.x - x1;
                    float b1 = centerPoint.y - y1;
                    float c1 = (float) Math.sqrt((a1 * a1 + b1 * b1));
                    float a2, b2;
                    a2 = a1 * pointLength / c1;
                    b2 = (float) Math.sqrt(pointLength * pointLength - a2 * a2);
                    rx = centerPoint.x - a2;
                    ry = centerPoint.y - b2;
                    //超出表盘圆范围则无效
                    if (x1 < rx&& !isUped) {
                        break;
                    }
                    //计算正切角度
                    currentAngle = Math.atan2(b1, a1) / (Math.PI / 180);
                    //四舍五入
//                    currentAngle = (Math.round(currentAngle-0.5) + 270);
                    currentAngle = currentAngle + 270;

                }
                //重新进行绘制
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isUped = true;
                break;
        }


        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //计算当前View的所给宽高,选择合适的点进行图表的绘画
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            viewWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            viewWidth = 400;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            viewHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            viewHeight = 400;
        }
        centerPoint = new Point(viewWidth / 2, viewHeight / 2);
        setMeasuredDimension(viewWidth, viewHeight);
    }


    private OnTimeChangeListener mListener;

    public void setOnTimeChangeListener(OnTimeChangeListener listener) {
        this.mListener = listener;
    }

    //转动表盘的时间回调
    public interface OnTimeChangeListener {
        void resultTime(String time, int minutes);
    }

}
