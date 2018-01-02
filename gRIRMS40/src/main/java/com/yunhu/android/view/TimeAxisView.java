package com.yunhu.android.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.yunhu.yhshxc.bo.TimeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author suhu
 * @data 2017/12/15.
 * @description
 */

public class TimeAxisView extends View{

    private static final int  MARGIN_TOP = 30;
    private static final int  START = 600;
    private static final int  SIZE = 42;

    private static final int  START_LINE = 30;

    private static final String text1 = "已约";
    private static final String text2 = "空闲";
    private static final String text3 = "时段";

    /**
     * 文字边框圆角半径
     * */
    private static final int cornerRadius = 13;

    private Paint redPint ,grayPint,textPint,linePint,smallPint;
    private int width,height;
    private RectF left,right;
    private float startX;
    private List<TimeBean> list;
    private float startYY;
    private float endX;
    private float spacing;



    public TimeAxisView(Context context) {
        super(context);
    }

    public TimeAxisView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeAxisView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TimeAxisView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (list.size()>0){
            drawText(canvas);
            drawLine(canvas);
            drawEffect(canvas);
        }


    }

    private void drawEffect(Canvas canvas) {
        startYY = 180;
        for (TimeBean timeBean : list) {
            String date = timeBean.getDate();
            String [] points = timeBean.getPiont().split(",");
            List<Integer> pointsList = new ArrayList<>();
            for (String point : points) {
                pointsList.add(Integer.parseInt(point));
            }
            drawIt(canvas,date,pointsList);
            startYY = startYY+60;
        }
    }

    private void drawIt(Canvas canvas, String date,List<Integer> points){
        canvas.drawText(date,START_LINE,startYY+25,textPint);
        right.left = startX;
        right.top = startYY;
        right.right = endX;
        right.bottom = right.top+30;
        canvas.drawRoundRect(right,cornerRadius,cornerRadius,grayPint);

        for (Integer point : points) {
            if (point==0){
                left.left = right.left+point*spacing;
                left.top = right.top;
                left.right = left.left+spacing;
                left.bottom = right.bottom;
                canvas.drawRoundRect(left,cornerRadius,cornerRadius,redPint);
                left.left = right.left+point*spacing+18;
                canvas.drawRect(left,redPint);
            }else if(point==23){
                left.left = right.left+point*spacing;
                left.top = right.top;
                left.right = left.left+spacing;
                left.bottom = right.bottom;
                canvas.drawRoundRect(left,cornerRadius,cornerRadius,redPint);
                left.right = left.left+spacing-18;
                canvas.drawRect(left,redPint);
            }else{
                left.left = right.left+point*spacing;
                left.top = right.top;
                left.right = left.left+spacing;
                left.bottom = right.bottom;
                canvas.drawRect(left,redPint);
            }

        }
    }

    private void drawText(Canvas canvas) {
        canvas.drawText(text1,START,MARGIN_TOP+35,textPint);


        float length = textPint.measureText(text1);
        left.left = START + length +20;
        left.top = MARGIN_TOP;
        left.right = left.left+SIZE;
        left.bottom = left.top+SIZE;
        canvas.drawRect(left,redPint);

        float start = left.right+50;
        canvas.drawText(text2,start,MARGIN_TOP+35,textPint);

        float length2 = textPint.measureText(text2);
        right.left = start + length2 +20;
        right.top = MARGIN_TOP;
        right.right = right.left+SIZE;
        right.bottom = right.top+SIZE;
        canvas.drawRect(right,grayPint);

    }


    private void drawLine(Canvas canvas) {
        canvas.drawText(text3 ,START_LINE,MARGIN_TOP+SIZE+50,textPint);
        float length = textPint.measureText(text3);
        startX = START_LINE +length+60;
        float startY = MARGIN_TOP+SIZE+35;
        endX = width-60;
        float endY = MARGIN_TOP+SIZE+35;
        canvas.drawLine(startX,startY,endX,endY,linePint);

        float lineLength = endX-startX;
        spacing = lineLength/24;


        for (int i = 0; i < 25; i++) {
            if (i%2==0){
                canvas.drawLine(startX+i*spacing,startY-10,startX+i*spacing,startY,linePint);
                float len = smallPint.measureText(""+i+"");
                float start = startX+i*spacing-len/2;
                canvas.drawText(""+i+"",start,startY+15,smallPint);
            }else {
                canvas.drawLine(startX+i*spacing,startY-5,startX+i*spacing,startY,linePint);
            }
        }
    }





    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initView();
    }

    private void initView() {
        width = getWidth();
        height = getHeight();

    }

    private void init() {
        list = new ArrayList<>();
        redPint = new Paint();
        redPint.setColor(Color.argb(255,219,31,32));
        redPint.setAntiAlias(true);
        redPint.setDither(true);
        redPint.setStyle(Paint.Style.FILL);
        redPint.setTextSize(10);

        grayPint = new Paint();
        grayPint.setColor(Color.argb(255,223,223,223));
        grayPint.setAntiAlias(true);
        grayPint.setDither(true);
        grayPint.setStyle(Paint.Style.FILL);
        grayPint.setTextSize(10);

        textPint = new Paint();
        textPint.setColor(Color.BLACK);
        textPint.setAntiAlias(true);
        textPint.setDither(true);
        textPint.setStyle(Paint.Style.STROKE);
        textPint.setTextSize(40);


        linePint = new Paint();
        linePint.setColor(Color.BLACK);
        linePint.setAntiAlias(true);
        linePint.setDither(true);
        linePint.setStyle(Paint.Style.STROKE);
        linePint.setTextSize(60);


        smallPint = new Paint();
        smallPint.setColor(Color.BLACK);
        smallPint.setAntiAlias(true);
        smallPint.setDither(true);
        smallPint.setStyle(Paint.Style.STROKE);
        smallPint.setTextSize(15);

        left = new RectF();
        right = new RectF();

    }


    /**
     *@method 设置数据
     *@author suhu
     *@time 2017/12/16 10:42
     *@param list
     *
    */
    public void setList(List<TimeBean> list) {
        this.list = list;
        invalidate();
    }
}
