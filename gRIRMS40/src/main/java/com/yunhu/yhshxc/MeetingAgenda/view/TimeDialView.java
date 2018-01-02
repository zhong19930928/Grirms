package com.yunhu.yhshxc.MeetingAgenda.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yunhu.yhshxc.R;

import java.util.Calendar;

/**
 * Created by xuqingli on 2017/11/30.
 * 时间表盘选择器
 */

public class TimeDialView extends View {

    private int dialBgColor = Color.parseColor("#90f0f1f3");//表盘背景颜色
    private int unAbleAreaColor = Color.parseColor("#90e0e1e3");//不可选取区域
    private int enableAreaColor = Color.parseColor("#90d8dffa");//选取区域的颜色
    private int handPointColor = Color.parseColor("#90c8c8ca");//指针颜色
    private int rulingColor = Color.BLACK;//刻度,圆线颜色
    private int timeColor = Color.BLACK;//时间值的字体颜色
    private int circlePointColor = Color.DKGRAY;//圆心颜色

    private int timeSize = 25;//时间值的字体大小

    private int startTime = 8, endTime = 20;//表盘的指定开始结束时间,有默认值
    private boolean isOpenVibration = true;//是否开启震动,按下时震动
    private int strokeWidth = 3;//表盘以及刻度的宽度

    private Paint dialPaint;//背景
    private Paint uablePaint;//不可选取区域
    private Paint enablePaint;//可选取区域
    private Paint handPaint;//指针1
    private Paint handPaint2;//指针2

    private Paint rulingPaint;//刻度
    private Paint circlePaint;//圆心
    private Paint textPaint;//文本


    private int viewWidth, viewHeight;//当前表盘的宽高
    private int marginL = 10;//实际半径减去值,防止贴边
    private Point point;//表盘的中心点
    private int radius;//表盘半径
    private int circleRadius = 10;//圆心半径
    private int marginR;//空余长度,用于绘制刻度 指针
    private int handLength;//指针长度

    private float unableStartAngle = 0;//记录不可选取区域的角度开始
    private float unableEndAngle = 0;//记录不可选取区域的角

    private float lineAngle;//一个整点的角度
    private float halfLineAngle;//半个整点的角度
    private int initHour;//初始化当前时
    private int minute;//初始化当前分
    private int hours;//当前时间与表盘开始点相差小时数
    private RectF rectF;//绘制扇形的矩形类
    private float handOneAngle = 0;//记录第一根指针当前角度,标记为开始位置
    private float handTwoAngle = 0;//记录第二根指针当前角度,标记为结束位置
    private boolean isHandOneUser = true;//指针1是否可滑动
    private boolean isHandTwoUser = true;//指针2是否可滑动

    private boolean isToday = true;//是否是今天,如果是,则进行时间区域可选性校验

    private int currentHand = 0;//当前操作的指针 1 为开始指针 2为结束指针

    private double downAngle;//点击点的相对于的0度的角度
    private double moveAngle;//滑动过程中相对于0度的角度

    private boolean isOpenShake = true;//开启震动
    private boolean isOpenSound = true;//开启声音

//    private Ringtone ring;//提示音
    //声音资源id
    private int voiceSr;
    private SoundPool soundPool;
    public TimeDialView(Context context) {
        this(context, null);
    }

    public TimeDialView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeDialView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        point = new Point();
        //表盘背景
        dialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dialPaint.setColor(dialBgColor);
        dialPaint.setStyle(Paint.Style.FILL);
        //不可选择区域
        uablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        uablePaint.setColor(unAbleAreaColor);
        uablePaint.setStyle(Paint.Style.FILL);
        //可选择区域
        enablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        enablePaint.setColor(enableAreaColor);
        enablePaint.setStyle(Paint.Style.FILL);
        //指针
        handPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        handPaint.setColor(handPointColor);
        handPaint.setStyle(Paint.Style.FILL);
        handPaint.setStrokeWidth(5);

        handPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        handPaint2.setColor(handPointColor);
        handPaint2.setStyle(Paint.Style.FILL);
        handPaint2.setStrokeWidth(5);
        //刻度
        rulingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rulingPaint.setColor(rulingColor);
        rulingPaint.setStyle(Paint.Style.STROKE);
        rulingPaint.setStrokeWidth(strokeWidth);
        //圆心
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(circlePointColor);
        circlePaint.setStyle(Paint.Style.FILL);

        //文本
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(timeColor);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(timeSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        //绘制双指针的初始时间
        Calendar calendar = Calendar.getInstance();
        //当前时
        initHour = calendar.get(Calendar.HOUR_OF_DAY);
        //当前分
        minute = calendar.get(Calendar.MINUTE);

//        Uri notification = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.unlock1);
//        ring = RingtoneManager.getRingtone(getContext(), notification);

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        voiceSr = soundPool.load(getContext(), R.raw.clock3, 1);
    }



    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();
        //绘制表盘背景
        canvas.drawCircle(point.x, point.y, radius, dialPaint);
        //绘制圆盘线
        canvas.drawCircle(point.x, point.y, radius, rulingPaint);
        //绘制圆心
        canvas.drawCircle(point.x, point.y, circleRadius, circlePaint);
        //绘制刻度线,根据设置的时间段
        int lines = endTime - startTime;
        lineAngle = (float) 360 / lines;//每个整点角度
        halfLineAngle = lineAngle / 2;//半个整点角度
        for (int i = startTime; i < endTime; i++) {
            canvas.drawLine(viewWidth / 2, viewHeight / 2 - radius, viewWidth / 2, viewHeight / 2 - radius + marginR, rulingPaint);
            String timeStr = i + "";
//            文本宽度
            float textWidth = textPaint.measureText(timeStr);
            canvas.drawText(timeStr, viewWidth / 2, viewHeight / 2 - radius + marginR + textWidth + 10, textPaint);
            handLength = (int) (radius - marginR - textWidth - 10 - 5);
            canvas.rotate(lineAngle, point.x, point.y);
        }
        //绘制半点刻度
        for (int i = startTime; i < endTime; i++) {
            canvas.rotate(halfLineAngle, point.x, point.y);
            canvas.drawLine(viewWidth / 2, viewHeight / 2 - radius, viewWidth / 2, viewHeight / 2 - radius + marginR / 2, rulingPaint);
            canvas.rotate(halfLineAngle, point.x, point.y);
        }
        //把画布旋转-90度,从0度开始绘制时间的开始结束便于计算
        canvas.rotate(-90, point.x, point.y);
        //当前事件与表盘开始事件的差值,用于计算指针角度
        hours = initHour - startTime;
        if (isToday) {
            //如果当前处于时间段以外,则无需校验,自由选取时间
            if (initHour < startTime) {
                isToday(false);
            }
        }
        //绘制不可选区域
        if (isToday) {
            float sweepAngle;
            if (minute >= 30) {
                //如果分钟数大于等于半点则直到下个整点为不可选取区域
                //绘制不可选取区域
                sweepAngle = lineAngle * (hours + 1);
                canvas.drawArc(rectF, 0, sweepAngle, true, uablePaint);
            } else {
                //否则到该小时点的半点刻度为不可选取区域
                sweepAngle = lineAngle * hours + halfLineAngle;
                canvas.drawArc(rectF, 0, sweepAngle, true, uablePaint);
            }
            unableStartAngle = 0;//不可选开始角度
            unableEndAngle = sweepAngle;//不可选结束角度
        }

        //绘制初始化指针
        if (handOneAngle == 0 && handTwoAngle == 0) {//需要进行初始化,还未进行操作
            if (isToday) {//如果有不可选区域
                handOneAngle = unableEndAngle;
            } else {//否则从0开始
                handTwoAngle = 0;
            }
            if (handOneAngle > (lineAngle * (float) ((endTime - startTime) - 0.5))) {
                //可选时间已经结束,两指针归0,并且不可滑动
                isHandOneUser = false;
                isHandTwoUser = false;
                handOneAngle = 0;
                handTwoAngle = 0;
            } else {
                handTwoAngle = handOneAngle + halfLineAngle;
            }
            int[] ints1 = getPointFromAngleAndRadius((int) handOneAngle, handLength);
            //绘制第一根指针
            canvas.drawLine(point.x, point.y, ints1[0], ints1[1], handPaint);
            int[] ints2 = getPointFromAngleAndRadius((int) handTwoAngle, handLength);
            //绘制第二根指针
            canvas.drawLine(point.x, point.y, ints2[0], ints2[1], handPaint2);
            //绘制两点之间的选择区域
            drawArc(canvas, handOneAngle, Math.abs(handTwoAngle - handOneAngle));
        } else {//根据其旋转的角度进行绘制
            int[] ints1 = getPointFromAngleAndRadius((int) handOneAngle, handLength);
            //绘制第一根指针
            canvas.drawLine(point.x, point.y, ints1[0], ints1[1], handPaint);
            int[] ints2 = getPointFromAngleAndRadius((int) handTwoAngle, handLength);
            //绘制第二根指针
            canvas.drawLine(point.x, point.y, ints2[0], ints2[1], handPaint2);
            //绘制两点之间的选择区域
            if (handOneAngle < handTwoAngle) {
                drawArc(canvas, handOneAngle, Math.abs(handTwoAngle - handOneAngle));
            } else {
                drawArc(canvas, handTwoAngle, Math.abs(handTwoAngle - handOneAngle));
            }


        }
//        Log.d("views", "handOneAngle: " + handOneAngle + "----- handTwoAngle: " + handTwoAngle);
        getTime();
    }

    //    private float firstAngle, secondAngle;//记录滑动过程中相邻的两个点,如果差值达到360,则视为第一象限和第四象限的跨域,不执行操作
//    private long shuju = 2;
    private int lastEra = 0;
    private int currentEra = 0;
    private float tempAngle;//临时记录角度用

    @Override

    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //点击的点
                float x1 = event.getX();
                float y1 = event.getY();
                //在不同象限获取当前点与X轴的角度,来判断是点击在哪根针上
                if (x1 > point.x && y1 < point.y) {//第一象限
                    float xLine = x1 - point.x;
                    float yLine = point.y - y1;
                    downAngle = Math.atan2(xLine, yLine) / (Math.PI / 180);
                } else if (x1 > point.x && y1 > point.y) {//第二象限
                    float xLine = x1 - point.x;
                    float yLine = y1 - point.y;
                    downAngle = Math.atan2(yLine, xLine) / (Math.PI / 180) + 90;
                } else if (x1 < point.x && y1 > point.y) {//第三象限
                    float xLine = point.x - x1;
                    float yLine = y1 - point.y;
                    downAngle = Math.atan2(xLine, yLine) / (Math.PI / 180) + 180;
                } else if (x1 < point.x && y1 < point.y) {//第四象限
                    float xLine = point.x - x1;
                    float yLine = point.y - y1;
                    downAngle = Math.atan2(yLine, xLine) / (Math.PI / 180) + 270;
                }

                //对比当前角度,判断点击到了哪根指针
                double oneValue = Math.abs(handOneAngle - downAngle);
                double twoValue = Math.abs(handTwoAngle - downAngle);
                currentHand = 0;
                if (handOneAngle == 0 || handOneAngle == 360) {
                    oneValue = downAngle;
                    if (oneValue >= 355 && oneValue <= 360 || oneValue >= 0 && oneValue <= 5) {
                        currentHand = 1;
                    }

                }
                if (handTwoAngle == 0 || handTwoAngle == 360) {
                    twoValue = downAngle;
                    if (twoValue >= 350 && twoValue <= 360 || twoValue >= 0 && twoValue <= 10) {
                        currentHand = 2;
                    }
                }

                //优先默认第一根指针
                if (oneValue < 10 && oneValue > 0 || oneValue > -10 && oneValue < 0) {
                    //则点击到第一根指针
                    currentHand = 1;
                    lastEra = 0;
                    currentEra = -1;
//                    Log.d("views", "接近第一根");

                } else if (twoValue < 10 && twoValue > 0 || twoValue > -10 && twoValue < 0) {
                    //点击到第二根指针
                    currentHand = 2;
                    lastEra = 0;
                    currentEra = -1;
//                    Log.d("views", "接近第二根");

                }
//                else {
//                    currentHand = 0;//没有点击到指针上
////                    Log.d("views", "没有点击到指针上");
//                }
                break;
            case MotionEvent.ACTION_MOVE:
//                shuju += 1;
                //点击的点
                float x2 = event.getX();
                float y2 = event.getY();
                //在不同象限获取当前点与X轴的角度,来判断是点击在哪根针上
                if (x2 > point.x && y2 < point.y) {//第一象限
                    lastEra = 1;
                    float xLine = x2 - point.x;
                    float yLine = point.y - y2;
                    moveAngle = Math.atan2(xLine, yLine) / (Math.PI / 180);

                } else if (x2 > point.x && y2 > point.y) {//第二象限
//                    lastEra=0;currentEra=-1;
                    float xLine = x2 - point.x;
                    float yLine = y2 - point.y;
                    moveAngle = Math.atan2(yLine, xLine) / (Math.PI / 180) + 90;
                } else if (x2 < point.x && y2 > point.y) {//第三象限
//                    lastEra=0;currentEra=-1;
                    float xLine = point.x - x2;
                    float yLine = y2 - point.y;
                    moveAngle = Math.atan2(xLine, yLine) / (Math.PI / 180) + 180;
                } else if (x2 < point.x && y2 < point.y) {//第四象限
                    currentEra = 1;
                    float xLine = point.x - x2;
                    float yLine = point.y - y2;
                    moveAngle = Math.atan2(yLine, xLine) / (Math.PI / 180) + 270;
                }

                //每次至少移动半个点的角度
                int resultAngle = (int) moveAngle;

                float mo = resultAngle % halfLineAngle;
                if (mo != 0) {
                    resultAngle = (int) (resultAngle - mo + halfLineAngle);
                }

                //震动,每当选中半个点就震动一次
                if (isOpenShake && currentHand != 0 && Math.abs(resultAngle - tempAngle) >= 15) {
                    beginShake();
                }
                tempAngle = resultAngle;

//                if (shuju % 2 == 0) {
//                    firstAngle = resultAngle;
//                } else {
//                    secondAngle = resultAngle;
//                }
//                Log.e("views", "" + resultAngle);
                if (currentHand == 1) {
                    //如果当前有不可选的区域 需要进行校验
                    if (isToday) {
                        if (resultAngle <= 360 && resultAngle >= unableEndAngle) {
//                            float abs = Math.abs(secondAngle - firstAngle);
//                            Log.e("views", "abs: " + abs);
//                            if (abs != 360) {
                            if (lastEra != currentEra || handOneAngle == 360) {
                                handOneAngle = (float) resultAngle;
                                invalidate();
                            }

//                            } else {
//                                Log.e("views", "第yi设置false: " );
//                                isHandOneUser = false;
//                            }

                        }
                    } else {
                        handOneAngle = (float) resultAngle;
                        invalidate();
                    }


                } else if (currentHand == 2) {
                    //如果当前有不可选的区域 需要进行校验
                    if (isToday) {
                        if (resultAngle <= 360 && resultAngle >= unableEndAngle) {
//                            float abs = Math.abs(secondAngle - firstAngle);
//                            Log.e("views", "abs: " + abs);
//                            if (abs != 360) {
                            if (lastEra != currentEra || handTwoAngle == 360) {
                                handTwoAngle = (float) resultAngle;
                                invalidate();
                            }
//                            } else {
//                                Log.e("views", "第二设置false: " );
//                                isHandTwoUser = false;
//                            }
                        }
                    } else {
                        handTwoAngle = (float) resultAngle;
                        invalidate();
                    }


                }
                break;

            case MotionEvent.ACTION_UP:
                currentHand = 0;
                break;

        }
        return true;
    }

    /**
     * 开启震动功能
     */
    private void beginShake() {
        //检查权限
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
            //开始震动
            Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(new long[]{0, 100}, -1);
        }

        //播放声音
//        if (isOpenSound) {
//        soundPool.play(voiceSr, 1.0f, 1.0f, 1, 0, 1.0f);
//            if (ring != null) {
//                ring.play();
//            }
//        }


    }

    /**
     * 根据开始和结束的角度计算时间
     *
     * @return
     */
    public String getTime() {
        String resultTime;
        String handOneTime;
        String handTwoTime;
        float halfHourStart = handOneAngle / halfLineAngle;//获取开始角度的小时数
        if (halfHourStart % 2 == 0) {
            //说明没有半点
            int start = (int) (halfHourStart / 2) + 8;
            if (start < 10) {
                handOneTime = "0" + start + ":00";
            } else {
                handOneTime = start + ":00";
            }

        } else {
            int start2 = ((int) ((halfHourStart - 1) / 2) + 8);
            if (start2 < 10) {
                handOneTime = "0" + start2 + ":30";
            } else {
                handOneTime = start2 + ":30";
            }

        }
        //结束时间
        float halfHourEnd = handTwoAngle / halfLineAngle;//获取开始角度的小时数
        if (halfHourEnd % 2 == 0) {
            //说明没有半点
            int end = (int) (halfHourEnd / 2) + 8;
            if (end < 10) {
                handTwoTime = "0" + end + ":00";
            } else {
                handTwoTime = end + ":00";
            }
        } else {

            int end2 = ((int) ((halfHourEnd - 1) / 2) + 8);
            if (end2 < 10) {
                handTwoTime = "0" + end2 + ":30";
            } else {
                handTwoTime = end2 + ":30";
            }

        }
        if (halfHourStart > halfHourEnd) {
            resultTime = handTwoTime + "-" + handOneTime;
        } else if (halfHourStart < halfHourEnd) {
            resultTime = handOneTime + "-" + handTwoTime;
        } else{
            resultTime = handOneTime + "-" + handTwoTime;
        }
        if (changeListener != null) {
            changeListener.onTimeChanged(resultTime);
        }
        return resultTime;

    }

    /**
     *  判断是否为有效的时间段
     *  比如两根针指向同一角度,不符合要求
     * @return
     */
    public boolean isUserTime(){
        return !(handOneAngle==handTwoAngle);
    }

    /**
     * 根据开始位置和实际滑动角度来绘制扇形
     */
    private void drawArc(Canvas canvas, float startAngle, float sweepAngle) {
        float start = startAngle;
        float sweep = sweepAngle;
        if (sweep == 0) {
            return;
        }
        canvas.drawArc(rectF, start, sweep, true, enablePaint);
    }

    /**
     * 根据角度和半径计算指针点的坐标
     *
     * @param angle
     * @param radius
     * @return
     */
    private int[] getPointFromAngleAndRadius(int angle, int radius) {
        double x = radius * Math.cos(angle * Math.PI / 180) + point.x;
        double y = radius * Math.sin(angle * Math.PI / 180) + point.y;
        return new int[]{(int) Math.round(x), (int) Math.round(y)};
    }

    /**
     * 是否开启震动
     *
     * @param isOpenShake
     */
    public void isOpenShakes(boolean isOpenShake) {
        this.isOpenShake = isOpenShake;
    }

    /**
     * 是都开启声音
     *
     * @param isOpenSound
     */
    public void isOpenSound(boolean isOpenSound) {
        this.isOpenSound = isOpenSound;
    }

    /**
     * 设置是否为今天,当不为今天整个表盘没有不可选区域
     *
     * @param isToday
     */
    public void isToday(boolean isToday) {
        this.isToday = isToday;
        handOneAngle = 0;
        handTwoAngle = 0;
        invalidate();
    }

    /**
     * 设置开始时间
     */
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    /**
     * 设置结束时间
     */
    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    /**
     * 设置是否开启震动和音效
     */
    public void setOpenVibration(boolean isOpenVibration) {
        this.isOpenVibration = isOpenVibration;
    }

    /**
     * 计算当前view的宽度高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            widthSize = 400;
        }
        if (heightMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            heightSize = 400;
        }
        heightSize = widthSize;

        //记录当前view的宽高以及中心点
        viewWidth = widthSize;
        viewHeight = heightSize;
        point.x = viewWidth / 2;
        point.y = viewHeight / 2;
        radius = viewWidth / 2 - marginL;
        marginR = (int) (viewWidth * 0.05);
        rectF = new RectF();
        rectF.left = marginL;
        rectF.top = marginL;
        rectF.right = viewWidth - marginL;
        rectF.bottom = viewHeight - marginL;
        setMeasuredDimension(widthSize, heightSize);


    }

    /**
     * 回调时间的监听器
     */
    private OnTimeChangeListener changeListener;

    public void setOnTimeChangeListener(OnTimeChangeListener listener) {
        this.changeListener = listener;
    }

    public interface OnTimeChangeListener {

        void onTimeChanged(String time);
    }
}
