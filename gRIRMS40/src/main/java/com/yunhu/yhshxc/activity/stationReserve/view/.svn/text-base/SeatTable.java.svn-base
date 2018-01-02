package com.yunhu.yhshxc.activity.stationReserve.view;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.StationBean;
import com.yunhu.yhshxc.bo.StationMapBean;
import com.yunhu.yhshxc.utility.PublicUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.angle;

/**
 * Created by baoyunlong on 16/6/16.
 */
public class SeatTable extends View {
    private final boolean DBG = false;

    Paint paint = new Paint();
    Paint framPaint = new Paint();
    Paint.FontMetrics lineNumberPaintFontMetrics;
    Matrix matrix = new Matrix();
    /**
     * 座位水平间距
     */
    int spacing;

    /**
     * 座位垂直间距
     */
    int verSpacing;
    /**
     * 可选时座位的图片
     */
    Bitmap seatBitmap;


    Bitmap seat_Avable_TopBitmap;//可选座位朝上方向
    Bitmap seat_Checked_TopBitmap;//选中座位朝上方向
    Bitmap seat_Sold_TopBitmap;//已占用座位朝上方向
    Bitmap seat_fix_TopBitmap;//固定工位上方向

    Bitmap seat_Bg_NomalBitmap, seat_Hy_NomalBitmap;
    Bitmap lseat_Avable_TopBitmap, lseat_Checked_TopBitmap, lseat_Sold_TopBitmap, lseat_fix_TopBitmap;
    Bitmap seat_Gz_NomalBitmap;
    Bitmap bitmap3;
    Bitmap seat_AllotBitmap, lseat_AllotBitmap;

    int lastX;
    int lastY;

    /**
     * 整个座位图的宽度
     */
    int seatBitmapWidth;

    /**
     * 整个座位图的高度
     */
    int seatBitmapHeight;

    /**
     * 标识是否需要绘制座位图
     */
    boolean isNeedDrawSeatBitmap = true;


    /**
     * 标识是否正在缩放
     */
    boolean isScaling;
    float scaleX, scaleY;

    /**
     * 是否是第一次缩放
     */
    boolean firstScale = true;

    /**
     * 最多可以选择的座位数量
     */
    int maxSelected = Integer.MAX_VALUE;


    /**
     * 是否第一次执行onDraw
     */
    boolean isFirstDraw = true;


    int overview_checked;
    int overview_sold;
    int txt_color;
    int seatCheckedResTopID;
    int seatSoldResTopID;
    int seatAvailableResTopID;
    int lseatCheckedResTopID, lseatSoldResTopID, lseatAvailableResTopID, lseatFixTopId;
    int seatFixTopId;

    boolean isOnClick;

    /**
     * 座位已售
     */
    private static final int SEAT_TYPE_SOLD = 1;

    /**
     * 座位已经选中
     */
    private static final int SEAT_TYPE_SELECTED = 3;

    /**
     * 座位可选
     */
    private static final int SEAT_TYPE_AVAILABLE = 2;

    /**
     * 座位不可用
     */
    private static final int SEAT_TYPE_NOT_AVAILABLE = 4;

    private static final int YIDONGGONGWEI = 1;//移动工位
    private static final int GUDONGGONGWEI = 2;//固定工位
    private static final int BANGONGS = 3;//办公室
    private static final int HUIYISHI = 4;//会议室
    private static final int QIATANSHI = 5;//洽谈室
    private static final int DAYINSHI = 6;//打印室
    private static final int PEIXIANSHI = 7;//配线室
    private static final int CHUWUJIAN = 8;//储物间
    private static final int DIANTIJIAN = 9;//电梯间
    private static final int BAOANSHI = 10;//保安室
    private static final int XIXIAOJIAN = 11;//洗消间
    private static final int CHUWUJIAN1 = 12;//储物间
    private static final int XIUXIANJIAN = 13;//休闲区
    private static final int KAFEIJIAN = 14;//咖啡间
    private static final int BATAI = 15;//吧台
    private static final int BAOGAOTING = 16;//报告厅
    private static final int JIEDAISHI = 17;//接待室
    private static final int SHEBEIJIAN = 18;//设备间

    private int downX, downY;
    private boolean pointer;
    RectF rectF;
    Rect rect;
    /**
     * 头部下面横线的高度
     */
    Paint redBorderPaint;

    /**
     * 默认的座位图宽度,如果使用的自己的座位图片比这个尺寸大或者小,会缩放到这个大小
     */
    private float defaultImgW = 50;

    /**
     * 默认的座位图高度
     */
    private float defaultImgH = 50;

    /**
     * 座位图片的宽度
     */
    private int seatWidth;

    /**
     * 座位图片的高度
     */
    private int seatHeight;

    private List<StationMapBean> dataList = new ArrayList<>();

    private List<String> checkedList = new ArrayList<>();

    private int x_num, y_num;

    public void setXYNum(int x, int y) {
        this.x_num = x;
        this.y_num = y;
    }

    Map<String, Integer> map;//放置办公室 会议室模块的xy大小值

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

    private ArrayList<String> byList;

    public void setByList(ArrayList<String> byList) {
        this.byList = byList;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    private int flag;//2,3表示只显示详细信息

    public void setLouName(String louName) {
        this.louName = louName;
    }

    public void setCengName(String cengName) {
        this.cengName = cengName;
    }

    private String louName,cengName;//该楼层名字




    /**
     * 标识是否需要绘制概览图
     */
    boolean isDrawOverview = false;

    /**
     * 标识是否需要更新概览图
     */
    boolean isDrawOverviewBitmap = true;

    /**
     * 整个概览图的宽度
     */
    float rectW;

    /**
     * 整个概览图的高度
     */
    float rectH;

    /**
     * 概览图的比例
     */
    float overviewScale = 4.8f;
    /**
     * 概览图bitmap
     */
    Bitmap overviewBitmap;
    /**
     * 概览图白色方块高度
     */
    float rectHeight;

    /**
     * 概览图白色方块的宽度
     */
    float rectWidth;


    public SeatTable(Context context) {
        super(context);
    }

    public SeatTable(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private boolean isfirst;
    private int xxx, yyy;

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SeatTableView);
        overview_checked = typedArray.getColor(R.styleable.SeatTableView_overview_checked, Color.parseColor("#5A9E64"));
        overview_sold = typedArray.getColor(R.styleable.SeatTableView_overview_sold, Color.RED);
        txt_color = typedArray.getColor(R.styleable.SeatTableView_txt_color, Color.BLACK);

        seatCheckedResTopID = typedArray.getResourceId(R.styleable.SeatTableView_seat_checked_top, R.drawable.seat_checked_top);
        seatSoldResTopID = typedArray.getResourceId(R.styleable.SeatTableView_seat_sold_top, R.drawable.seat_sold_top);
        seatAvailableResTopID = typedArray.getResourceId(R.styleable.SeatTableView_seat_available_top, R.drawable.seat_avable_top);
        seatFixTopId = typedArray.getResourceId(R.styleable.SeatTableView_seat_fix_top, R.drawable.seat_fix_top);

        lseatAvailableResTopID = typedArray.getResourceId(R.styleable.SeatTableView_lseat_available_top, R.drawable.lseat_avable_top);
        lseatCheckedResTopID = typedArray.getResourceId(R.styleable.SeatTableView_lseat_checked_top, R.drawable.lseat_checked_top);
        lseatSoldResTopID = typedArray.getResourceId(R.styleable.SeatTableView_lseat_sold_top, R.drawable.lseat_sold_top);
        lseatFixTopId = typedArray.getResourceId(R.styleable.SeatTableView_lseat_fix_top, R.drawable.lseat_fix_top);
        typedArray.recycle();
    }

    public SeatTable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    float xScale1 = 1;
    float yScale1 = 1;

    private void init() {
        float density = getResources().getDisplayMetrics().density;
        spacing = (int) dip2Px(0);
        verSpacing = (int) dip2Px(0);

        seatBitmap = BitmapFactory.decodeResource(getResources(), seatAvailableResTopID);

        seat_Avable_TopBitmap = BitmapFactory.decodeResource(getResources(), seatAvailableResTopID);
        seat_Checked_TopBitmap = BitmapFactory.decodeResource(getResources(), seatCheckedResTopID);
        seat_Sold_TopBitmap = BitmapFactory.decodeResource(getResources(), seatSoldResTopID);
        seat_fix_TopBitmap = BitmapFactory.decodeResource(getResources(), seatFixTopId);
        lseat_Avable_TopBitmap = BitmapFactory.decodeResource(getResources(), lseatAvailableResTopID);
        lseat_Checked_TopBitmap = BitmapFactory.decodeResource(getResources(), lseatCheckedResTopID);
        lseat_Sold_TopBitmap = BitmapFactory.decodeResource(getResources(), lseatSoldResTopID);
        lseat_fix_TopBitmap = BitmapFactory.decodeResource(getResources(), lseatFixTopId);

        float scaleX = defaultImgW / seatBitmap.getWidth();
        float scaleY = defaultImgH / seatBitmap.getHeight();
        xScale1 = scaleX;
        yScale1 = scaleY;

        seatHeight = (int) (seatBitmap.getHeight() * yScale1);
        seatWidth = (int) (seatBitmap.getWidth() * xScale1);


        seatBitmapWidth = (int) ((x_num + 1) * seatBitmap.getWidth() * xScale1 + (x_num) * spacing);
        seatBitmapHeight = (int) ((y_num + 1) * seatBitmap.getHeight() * yScale1 + (y_num) * verSpacing);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);


        redBorderPaint = new Paint();
        redBorderPaint.setAntiAlias(true);
        redBorderPaint.setColor(Color.RED);
        redBorderPaint.setStyle(Paint.Style.STROKE);
        redBorderPaint.setStrokeWidth(getResources().getDisplayMetrics().density * 1);

        rectF = new RectF();
        rect = new Rect();

        rectHeight = seatHeight / overviewScale;
        rectWidth = seatWidth / overviewScale;
        rectW = seatBitmapWidth/overviewScale;
        rectH = seatBitmapHeight/overviewScale;

        matrix.postTranslate(spacing, verSpacing);


        bitmap3 = Bitmap.createBitmap(seatBitmap.getWidth(), seatBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        bitmap3.eraseColor(Color.parseColor("#1B8AFF"));
        seat_Bg_NomalBitmap = Bitmap.createBitmap(seatBitmap.getWidth(), seatBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        seat_Bg_NomalBitmap.eraseColor(Color.parseColor("#AED7FA"));
        seat_Hy_NomalBitmap = Bitmap.createBitmap(seatBitmap.getWidth(), seatBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        seat_Hy_NomalBitmap.eraseColor(Color.parseColor("#F6F3C7"));

        seat_Gz_NomalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.seat_guizi);
        seat_AllotBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.seat_noallot_top);
        lseat_AllotBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lseat_noallot_top);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        long startTime = System.currentTimeMillis();
        if (dataList.size() == 0) {
            return;
        }
        drawFrame(canvas);
        drawSeat(canvas);
        if (!isfirst && xxx != 0 && yyy != 0) {
            matrix.postScale(3, 3, xxx, yyy);
            matrix.postTranslate(-xxx+20 , -yyy+20);
            invalidate();
        }
        isfirst = true;

        if (isDrawOverview) {
            if (isDrawOverviewBitmap) {
                isDrawOverviewBitmap=false;
//                overviewBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.suo_leu_icon),(int)rectW, (int)rectH,true);
//                overviewBitmap = Bitmap.createBitmap((int)rectW, (int)rectH, Bitmap.Config.ARGB_8888);
//                overviewBitmap.eraseColor(Color.parseColor("#1B8AFF"));
                checkOverviewBitmap();
            }
            if(overviewBitmap!=null){
                canvas.drawBitmap(overviewBitmap, 0, 0, null);
                drawOverview(canvas);
            }
        }


        if (DBG) {
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        int x = (int) event.getX();
        super.onTouchEvent(event);

        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        int pointerCount = event.getPointerCount();
        if (pointerCount > 1) {
            pointer = true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointer = false;
                downX = x;
                downY = y;
                isDrawOverview = true;
                handler.removeCallbacks(hideOverviewRunnable);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isScaling && !isOnClick) {
                    int downDX = Math.abs(x - downX);
                    int downDY = Math.abs(y - downY);
                    if ((downDX > 10 || downDY > 10) && !pointer) {
                        int dx = x - lastX;
                        int dy = y - lastY;
                        matrix.postTranslate(dx, dy);
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                handler.postDelayed(hideOverviewRunnable, 1500);

                autoScale();
                int downDX = Math.abs(x - downX);
                int downDY = Math.abs(y - downY);
                if ((downDX > 10 || downDY > 10) && !pointer) {
                    autoScroll();
                }

                break;
        }
        isOnClick = false;
        lastY = y;
        lastX = x;

        return true;
    }

    private Runnable hideOverviewRunnable = new Runnable() {
        @Override
        public void run() {
            isDrawOverview = false;
            invalidate();
        }
    };


    Matrix tempMatrix = new Matrix();

    void drawSeat(Canvas canvas) {
        zoom = getMatrixScaleX();
        long startTime = System.currentTimeMillis();
        float translateX = getTranslateX();
        float translateY = getTranslateY();
        float scaleX = zoom;
        float scaleY = zoom;

        int minx = 0, maxx = 0, miny = 0, maxy = 0;
        float width = seatBitmap.getWidth() * xScale1 * scaleX;
        float height = seatBitmap.getHeight() * yScale1 * scaleY;


        for (int i = 0; i < dataList.size(); i++) {
            float top = dataList.get(i).getYnum() * height + dataList.get(i).getYnum() * verSpacing * scaleY + translateY;
            float left = dataList.get(i).getxNum() * width + dataList.get(i).getxNum() * spacing * scaleX + translateX;
            if (dataList.get(i).getGwdwd() == 1 && !isfirst) {
                xxx = (int) left;
                yyy = (int) top;
            }
            float bottom = top + height;
            if (bottom < 0 || top > getHeight()) {
                continue;
            }

            float right = (left + width);
            if (right < 0 || left > getWidth()) {
                continue;
            }
            tempMatrix.setTranslate(left, top);
            tempMatrix.postScale(xScale1, yScale1, left, top);
            tempMatrix.postScale(scaleX, scaleY, left, top);
            if (dataList.get(i).getGwstye() == YIDONGGONGWEI||dataList.get(i).getGwstye() == GUDONGGONGWEI) {
                if (dataList.get(i).getFpstate() == 1) {
                    //已分配
                    if(dataList.get(i).getGwstye() == YIDONGGONGWEI){
                        if (dataList.get(i).getGwxh() == 1) {
                            //标准工位
                            if (dataList.get(i).getSystate() == SEAT_TYPE_AVAILABLE) {
                                drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), true);
                                canvas.drawBitmap(seat_Avable_TopBitmap, tempMatrix, paint);
                                if (zoom >= 1.2) {
//                                    drawText(canvas, dataList.get(i).getGwsn(), top, left);
                                }
                                drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), false);
                            } else if (dataList.get(i).getSystate() == SEAT_TYPE_SELECTED) {
                                drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), true);
                                canvas.drawBitmap(seat_Checked_TopBitmap, tempMatrix, paint);
                                if (zoom >= 1.2) {
//                                    drawText(canvas, dataList.get(i).getGwsn(), top, left);
                                }
                                drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), false);
                            } else if (dataList.get(i).getSystate() == SEAT_TYPE_SOLD) {
                                drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), true);
                                canvas.drawBitmap(seat_Sold_TopBitmap, tempMatrix, paint);
                                if (zoom >= 1.2) {
                                    if(dataList.get(i).getFx()!=6){
                                        drawText(canvas, dataList.get(i).getYgname() != null&&!"".equals(dataList.get(i).getYgname())&&!"null".equals(dataList.get(i).getYgname()) ? dataList.get(i).getYgname() :"" , top, left);
                                    }
                                }
                                drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), false);
                                if (zoom >= 1.2) {
                                    if(dataList.get(i).getFx()==6){
                                        drawTopText(canvas, dataList.get(i).getYgname() != null&&!"".equals(dataList.get(i).getYgname())&&!"null".equals(dataList.get(i).getYgname()) ? dataList.get(i).getYgname() :"" , top, left);
                                    }
                                }
                            }

                        } else if (dataList.get(i).getGwxh() == 2) {
                            //l型工位
                            if (dataList.get(i).getSystate() == SEAT_TYPE_AVAILABLE) {
                                drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), true);
                                canvas.drawBitmap(lseat_Avable_TopBitmap, tempMatrix, paint);
                                if (zoom >= 1.2) {
//                                    drawText(canvas, dataList.get(i).getGwsn(), top, left);
                                }
                                drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), false);
                            } else if (dataList.get(i).getSystate() == SEAT_TYPE_SELECTED) {
                                drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), true);
                                canvas.drawBitmap(lseat_Checked_TopBitmap, tempMatrix, paint);
                                if (zoom >= 1.2) {
//                                    drawText(canvas, dataList.get(i).getGwsn(), top, left);
                                }
                                drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), false);
                            } else if (dataList.get(i).getSystate() == SEAT_TYPE_SOLD) {
                                drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), true);
                                canvas.drawBitmap(lseat_Sold_TopBitmap, tempMatrix, paint);
                                drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), false);
                                if (zoom >= 1.2) {
                                    if(dataList.get(i).getFx()==4||dataList.get(i).getFx()==7){
                                        drawText(canvas, dataList.get(i).getYgname() != null&&!"".equals(dataList.get(i).getYgname())&&!"null".equals(dataList.get(i).getYgname()) ? dataList.get(i).getYgname() :"", top, left);
                                    }else{
                                        drawTopText(canvas, dataList.get(i).getYgname() != null&&!"".equals(dataList.get(i).getYgname())&&!"null".equals(dataList.get(i).getYgname()) ? dataList.get(i).getYgname() :"", top, left);
                                    }
                                }
                            }

                        }
                    }else if(dataList.get(i).getGwstye() == GUDONGGONGWEI){
                        if (dataList.get(i).getGwxh() == 1) {
                            drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), true);
//                    canvas.drawBitmap(seat_fix_TopBitmap, tempMatrix, paint);
                            canvas.drawBitmap(seat_Sold_TopBitmap, tempMatrix, paint);
                            if (zoom >= 1.2) {
                                if(dataList.get(i).getFx()!=6){
                                    drawText(canvas, dataList.get(i).getYgname() != null&&!"".equals(dataList.get(i).getYgname())&&!"null".equals(dataList.get(i).getYgname()) ? dataList.get(i).getYgname() :"" , top, left);
                                }
                            }
                            drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), false);
                            if (zoom >= 1.2) {
                                if(dataList.get(i).getFx()==6){
                                    drawTopText(canvas, dataList.get(i).getYgname() != null&&!"".equals(dataList.get(i).getYgname())&&!"null".equals(dataList.get(i).getYgname()) ? dataList.get(i).getYgname() :"" , top, left);
                                }
                            }
                        } else if (dataList.get(i).getGwxh() == 2) {
                            drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), true);
//                    canvas.drawBitmap(lseat_fix_TopBitmap, tempMatrix, paint);
                            canvas.drawBitmap(lseat_Sold_TopBitmap, tempMatrix, paint);
                            drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), false);
                            if (zoom >= 1.2) {
                                if(dataList.get(i).getFx()==4||dataList.get(i).getFx()==7){
                                    drawText(canvas, dataList.get(i).getYgname() != null&&!"".equals(dataList.get(i).getYgname())&&!"null".equals(dataList.get(i).getYgname()) ? dataList.get(i).getYgname() :"", top, left);
                                }else{
                                    drawTopText(canvas, dataList.get(i).getYgname() != null&&!"".equals(dataList.get(i).getYgname())&&!"null".equals(dataList.get(i).getYgname()) ? dataList.get(i).getYgname() :"", top, left);
                                }
                            }
                        } else if (dataList.get(i).getGwxh() == 3) {
                            //柜子
                            canvas.drawBitmap(seat_Gz_NomalBitmap, tempMatrix, paint);
                        }
                    }

                } else if (dataList.get(i).getFpstate() == 2) {
                    //未分配
                    if (dataList.get(i).getGwxh() == 1) {
                        //标准工位
                        drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), true);
                        canvas.drawBitmap(seat_AllotBitmap, tempMatrix, paint);
                        if (zoom >= 1.2) {
//                            drawText(canvas, dataList.get(i).getGwsn(), top, left);
                        }
                        drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), false);

                    } else if (dataList.get(i).getGwxh() == 2) {
                        //L工位
                        drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), true);
                        canvas.drawBitmap(lseat_AllotBitmap, tempMatrix, paint);
                        if (zoom >= 1.2) {
//                            drawText(canvas, dataList.get(i).getGwsn(), top, left);
                        }
                        drawAngle(canvas, left + (width / 2), top + (height / 2), dataList.get(i).getFx(), false);
                    }

                }

            } else if (dataList.get(i).getGwstye() == HUIYISHI) {
                minx = map.get(dataList.get(i).getGwsn() + "minx");
                maxx = map.get(dataList.get(i).getGwsn() + "maxx");
                miny = map.get(dataList.get(i).getGwsn() + "miny");
                maxy = map.get(dataList.get(i).getGwsn() + "maxy");
                canvas.drawBitmap(seat_Hy_NomalBitmap, tempMatrix, paint);
                paint.setColor(Color.parseColor("#999999"));
                if (dataList.get(i).getxNum() == minx && dataList.get(i).getYnum() == miny) {
                    canvas.drawLine(left, top, left, top + height, paint);//左
                    canvas.drawLine(left, top, left + width, top, paint);//上
                } else if (dataList.get(i).getxNum() == maxx && dataList.get(i).getYnum() == maxy) {
                    canvas.drawLine(left + width, top, left + width, top + height, paint);
                    canvas.drawLine(left, top + height, left + width, top + height, paint);
                } else if (dataList.get(i).getxNum() == minx && dataList.get(i).getYnum() == maxy) {
                    canvas.drawLine(left, top, left, top + height, paint);//左
                    canvas.drawLine(left, top + height, left + width, top + height, paint);//下
                } else if (dataList.get(i).getxNum() == maxx && dataList.get(i).getYnum() == miny) {
                    canvas.drawLine(left + width, top, left + width, top + height, paint);//右
                    canvas.drawLine(left, top, left + width, top, paint);//上
                } else if (dataList.get(i).getxNum() == minx && dataList.get(i).getYnum() > miny) {
                    canvas.drawLine(left, top, left, top + height, paint);//左
                } else if (dataList.get(i).getxNum() == maxx && dataList.get(i).getYnum() > miny) {
                    canvas.drawLine(left + width, top, left + width, top + height, paint);//右
                } else if (dataList.get(i).getxNum() > minx && dataList.get(i).getYnum() == miny) {
                    canvas.drawLine(left, top, left + width, top, paint);//上
                } else if (dataList.get(i).getxNum() > minx && dataList.get(i).getYnum() == maxy) {
                    canvas.drawLine(left, top + height, left + width, top + height, paint);//下
                } else {
                    // canvas.drawBitmap(seat_Hy_NomalBitmap, tempMatrix, paint);
                    // drawText(canvas, dataList.get(i).getGwsn(), top, left);
                }


            } else if (dataList.get(i).getGwstye() == BANGONGS) {
                minx = map.get(dataList.get(i).getGwsn() + "minx");
                maxx = map.get(dataList.get(i).getGwsn() + "maxx");
                miny = map.get(dataList.get(i).getGwsn() + "miny");
                maxy = map.get(dataList.get(i).getGwsn() + "maxy");
                canvas.drawBitmap(seat_Bg_NomalBitmap, tempMatrix, paint);
                paint.setColor(Color.parseColor("#999999"));
                if (dataList.get(i).getxNum() == minx && dataList.get(i).getYnum() == miny) {
                    canvas.drawLine(left, top, left, top + height, paint);//左
                    canvas.drawLine(left, top, left + width, top, paint);//上

                } else if (dataList.get(i).getxNum() == maxx && dataList.get(i).getYnum() == maxy) {
                    canvas.drawLine(left + width, top, left + width, top + height, paint);
                    canvas.drawLine(left, top + height, left + width, top + height, paint);

                } else if (dataList.get(i).getxNum() == minx && dataList.get(i).getYnum() == maxy) {
                    canvas.drawLine(left, top, left, top + height, paint);//左
                    canvas.drawLine(left, top + height, left + width, top + height, paint);//下

                } else if (dataList.get(i).getxNum() == maxx && dataList.get(i).getYnum() == miny) {
                    canvas.drawLine(left + width, top, left + width, top + height, paint);//右
                    canvas.drawLine(left, top, left + width, top, paint);//上

                } else if (dataList.get(i).getxNum() == minx && dataList.get(i).getYnum() > miny) {
                    canvas.drawLine(left, top, left, top + height, paint);//左

                } else if (dataList.get(i).getxNum() == maxx && dataList.get(i).getYnum() > miny) {
                    canvas.drawLine(left + width, top, left + width, top + height, paint);//右

                } else if (dataList.get(i).getxNum() > minx && dataList.get(i).getYnum() == miny) {
                    canvas.drawLine(left, top, left + width, top, paint);//上

                } else if (dataList.get(i).getxNum() > minx && dataList.get(i).getYnum() == maxy) {
                    canvas.drawLine(left, top + height, left + width, top + height, paint);//下
                } else {
                    //canvas.drawBitmap(seat_Bg_NomalBitmap, tempMatrix, paint);
                    // drawText(canvas, dataList.get(i).getGwsn(), top, left);
                }


            } else {
//                canvas.drawBitmap(bitmap3, tempMatrix, paint);
                //drawText(canvas, dataList.get(i).getGwsn(), top, left);
            }

        }
        if (byList != null && byList.size() > 0) {
            for (int k = 0; k < byList.size(); k++) {
                minx = map.get(byList.get(k) + "minx");
                maxx = map.get(byList.get(k) + "maxx");
                miny = map.get(byList.get(k) + "miny");
                maxy = map.get(byList.get(k) + "maxy");
                drawHyText(canvas, byList.get(k), miny * height + translateY, minx * width + translateX, maxx - minx, maxy - miny);
            }
        }

        if (DBG) {
            long drawTime = System.currentTimeMillis() - startTime;
            Log.d("drawTime", "seatDrawTime:" + drawTime);
        }
    }


    /**
     * 绘制选中座位的行号列号 文字在下方
     */
    private void drawText(Canvas canvas, String name, float top, float left) {

        TextPaint txtPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
//        txtPaint.setColor(txt_color);
        txtPaint.setColor(Color.WHITE);
        txtPaint.setTypeface(Typeface.DEFAULT_BOLD);
        float seatHeight = this.seatHeight * getMatrixScaleX();
        float seatWidth = this.seatWidth * getMatrixScaleX();
        txtPaint.setTextSize(seatWidth / 5);

        float txtWidth = txtPaint.measureText(name);
        if (txtWidth >= seatWidth - 20) {
            txtPaint.setTextSize(seatWidth / 7);
            txtWidth = txtPaint.measureText(name);
        }
        txtPaint.getTextBounds(name, 0, name.length(), rect);
        int width = rect.width();//文字宽
        int height = rect.height();//文字高
        float startX = left + seatWidth / 2 - width / 2;

        float y = top + 3 * seatHeight / 4 + height / 2;
        canvas.drawText(name, startX, y, txtPaint);

        if (DBG) {
            Log.d("drawTest:", "top:" + top);
        }
    }

    /**
     * 绘制选中座位的行号列号 文字在上方
     */
    private void drawTopText(Canvas canvas, String name, float top, float left) {

        TextPaint txtPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
//        txtPaint.setColor(txt_color);
        txtPaint.setColor(Color.WHITE);
        txtPaint.setTypeface(Typeface.DEFAULT_BOLD);
        float seatHeight = this.seatHeight * getMatrixScaleX();
        float seatWidth = this.seatWidth * getMatrixScaleX();
        txtPaint.setTextSize(seatWidth / 5);

        float txtWidth = txtPaint.measureText(name);
        if (txtWidth >= seatWidth - 20) {
            txtPaint.setTextSize(seatWidth / 7);
            txtWidth = txtPaint.measureText(name);
        }
        txtPaint.getTextBounds(name, 0, name.length(), rect);
        int width = rect.width();//文字宽
        int height = rect.height();//文字高
        float startX = left + seatWidth / 2 - width / 2;

        float y = top + seatHeight / 5 + height / 2;
        canvas.drawText(name, startX, y, txtPaint);

        if (DBG) {
            Log.d("drawTest:", "top:" + top);
        }
    }


    /**
     * 绘制会议办公室名称
     * x x方向的差值
     * y y方向的差值
     */
    private void drawHyText(Canvas canvas, String name, float top, float left, int xNum, int yNum) {

        TextPaint txtPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        txtPaint.setColor(txt_color);
        txtPaint.setTypeface(Typeface.DEFAULT_BOLD);
        float seatHeight = 0;
        float seatWidth = 0;
        if (xNum == 0) {
            seatWidth = this.seatWidth * getMatrixScaleX();
        } else {
            seatWidth = this.seatWidth * xNum * getMatrixScaleX();
        }
        if (yNum == 0) {
            seatHeight = this.seatHeight * getMatrixScaleX();
        } else {
            seatHeight = this.seatHeight * yNum * getMatrixScaleX();
        }


        txtPaint.setTextSize(seatHeight / 6);

        txtPaint.getTextBounds(name, 0, name.length(), rect);
        int width = rect.width();//文字宽
        int height = rect.height();//文字高
        float startX = left + seatWidth / 2 - width / 2;

        float y = top + 3 * seatHeight / 4 + height / 2;
        canvas.drawText(name, startX, y, txtPaint);

        if (DBG) {
            Log.d("drawTest:", "top:" + top);
        }
    }


    /**
     * 显示不同方向的文字
     *
     * @param canvas
     * @param text
     * @param x
     * @param y
     * @param paint
     * @param angle
     */
    private void drawText(Canvas canvas, String text, float x, float y, Paint paint, float angle) {
        if (angle != 0) {
            canvas.rotate(angle, x, y);
        }
        canvas.drawText(text, x, y, paint);
        if (angle != 0) {
            canvas.rotate(-angle, x, y);
        }
    }


    /**
     * @param canvas
     * @param x
     * @param y
     * @param staus  工位方向
     * @param flag   方向还原表示
     */
    private void drawAngle(Canvas canvas, float x, float y, int staus, boolean flag) {
        float angle = 0;
        if (staus == 4) {
            angle = 0;
        } else if (staus == 6) {
            angle = 180;
        } else if (staus == 7) {
            angle = 270;
        } else if (staus == 5) {
            angle = 90;
        }
        if (flag) {
            if (angle != 0) {
                canvas.rotate(angle, x, y);
            }
        } else {
            if (angle != 0) {
                canvas.rotate(-angle, x, y);
            }
        }


    }

    /**
     * 绘制概览图
     */
    void drawOverview(Canvas canvas) {

        //绘制红色框
        int left = (int) -getTranslateX();
        if (left < 0) {
            left = 0;
        }
        left /= overviewScale;
        left /= getMatrixScaleX();

        int currentWidth = (int) (getTranslateX() + seatBitmapWidth * getMatrixScaleX());
        if (currentWidth > getWidth()) {
            currentWidth = currentWidth - getWidth();
        } else {
            currentWidth = 0;
        }
        int right = (int) (rectW - currentWidth / overviewScale / getMatrixScaleX());

        float top = -getTranslateY();
        if (top < 0) {
            top = 0;
        }
        top /= overviewScale;
        top /= getMatrixScaleY();
//        if (top > 0) {
//            top += overviewVerSpacing;
//        }

        int currentHeight = (int) (getTranslateY() + seatBitmapHeight * getMatrixScaleY());
        if (currentHeight > getHeight()) {
            currentHeight = currentHeight - getHeight();
        } else {
            currentHeight = 0;
        }
        int bottom = (int) (rectH - currentHeight / overviewScale / getMatrixScaleY());

        canvas.drawRect(left, top, right, bottom, redBorderPaint);
    }


    /**
     * 根据楼层名字选择不同的资源图片
     */
    private void checkOverviewBitmap(){
        if(louName!=null&&cengName!=null){
            if("A".equals(louName)){
                if("1".equals(cengName)){
                    overviewBitmap=zoomDrawable(getResources().getDrawable(R.drawable.a_1f),(int)rectW, (int)rectH);
                }else if("2".equals(cengName)){
                    overviewBitmap=zoomDrawable(getResources().getDrawable(R.drawable.a_2f),(int)rectW, (int)rectH);
                }else if("3".equals(cengName)){
                    overviewBitmap=zoomDrawable(getResources().getDrawable(R.drawable.a_3f),(int)rectW, (int)rectH);
                }else if("4".equals(cengName)){
                    overviewBitmap=zoomDrawable(getResources().getDrawable(R.drawable.a_4f),(int)rectW, (int)rectH);
                }else if("5".equals(cengName)){
                    overviewBitmap=zoomDrawable(getResources().getDrawable(R.drawable.a_5f),(int)rectW, (int)rectH);
                }else{
                    overviewBitmap = Bitmap.createBitmap((int)rectW, (int)rectH, Bitmap.Config.ARGB_8888);
                    overviewBitmap.eraseColor(Color.parseColor("#1B8AFF"));
                }

            }else if("B".equals(louName)){
                if("1".equals(cengName)){
                    overviewBitmap=zoomDrawable(getResources().getDrawable(R.drawable.b_1f),(int)rectW, (int)rectH);
                }else if("2".equals(cengName)){
                    overviewBitmap=zoomDrawable(getResources().getDrawable(R.drawable.b_2f),(int)rectW, (int)rectH);
                }else if("3".equals(cengName)){
                    overviewBitmap=zoomDrawable(getResources().getDrawable(R.drawable.b_3f),(int)rectW, (int)rectH);
                }else if("4".equals(cengName)){
                    overviewBitmap=zoomDrawable(getResources().getDrawable(R.drawable.b_4f),(int)rectW, (int)rectH);
                }else if("5".equals(cengName)){
                    overviewBitmap=zoomDrawable(getResources().getDrawable(R.drawable.b_5f),(int)rectW, (int)rectH);
                }else{
                    overviewBitmap = Bitmap.createBitmap((int)rectW, (int)rectH, Bitmap.Config.ARGB_8888);
                    overviewBitmap.eraseColor(Color.parseColor("#1B8AFF"));
                }

            }else if("C".equals(louName)){
                if("1".equals(cengName)){
                    overviewBitmap=zoomDrawable(getResources().getDrawable(R.drawable.c_1f),(int)rectW, (int)rectH);
                }else if("2".equals(cengName)){
                    overviewBitmap=zoomDrawable(getResources().getDrawable(R.drawable.c_2f),(int)rectW, (int)rectH);
                }else if("3".equals(cengName)){
                    overviewBitmap=zoomDrawable(getResources().getDrawable(R.drawable.c_3f),(int)rectW, (int)rectH);
                }else{
                    overviewBitmap = Bitmap.createBitmap((int)rectW, (int)rectH, Bitmap.Config.ARGB_8888);
                    overviewBitmap.eraseColor(Color.parseColor("#1B8AFF"));
                }
            }else{
                overviewBitmap = Bitmap.createBitmap((int)rectW, (int)rectH, Bitmap.Config.ARGB_8888);
                overviewBitmap.eraseColor(Color.parseColor("#1B8AFF"));
            }
        }
    }


    int bacColor = Color.parseColor("#7e000000");

    /**
     * 自动回弹
     * 整个大小不超过控件大小的时候:
     * 往左边滑动,自动回弹到行号右边
     * 往右边滑动,自动回弹到右边
     * 往上,下滑动,自动回弹到顶部
     * <p>
     * 整个大小超过控件大小的时候:
     * 往左侧滑动,回弹到最右边,往右侧滑回弹到最左边
     * 往上滑动,回弹到底部,往下滑动回弹到顶部
     */
    private void autoScroll() {
        float currentSeatBitmapWidth = seatBitmapWidth * getMatrixScaleX();
        float currentSeatBitmapHeight = seatBitmapHeight * getMatrixScaleY();
        float moveYLength = 0;
        float moveXLength = 0;

        //处理左右滑动的情况
        if (currentSeatBitmapWidth < getWidth()) {
            if (getTranslateX() < 0 || getMatrixScaleX() < spacing) {
                //计算要移动的距离
                if (getTranslateX() < 0) {
                    moveXLength = (-getTranslateX()) + spacing;
                } else {
                    moveXLength = spacing - getTranslateX();
                }
            }
        } else {

            if (getTranslateX() < 0 && getTranslateX() + currentSeatBitmapWidth > getWidth()) {

            } else {
                //往左侧滑动
                if (getTranslateX() + currentSeatBitmapWidth < getWidth()) {
                    moveXLength = getWidth() - (getTranslateX() + currentSeatBitmapWidth);
                } else {
                    //右侧滑动
                    moveXLength = -getTranslateX() + spacing;
                }
            }
        }
        float startYPosition = verSpacing * getMatrixScaleY();
        //处理上下滑动
        if (currentSeatBitmapHeight < getHeight()) {

            if (getTranslateY() < startYPosition) {
                moveYLength = startYPosition - getTranslateY();
            } else {
                moveYLength = -(getTranslateY() - (startYPosition));
            }

        } else {

            if (getTranslateY() < 0 && getTranslateY() + currentSeatBitmapHeight > getHeight()) {

            } else {
                //往上滑动
                if (getTranslateY() + currentSeatBitmapHeight < getHeight()) {
                    moveYLength = getHeight() - (getTranslateY() + currentSeatBitmapHeight);
                } else {
                    moveYLength = -(getTranslateY() - (startYPosition));
                }
            }
        }

        Point start = new Point();
        start.x = (int) getTranslateX();
        start.y = (int) getTranslateY();

        Point end = new Point();
        end.x = (int) (start.x + moveXLength);
        end.y = (int) (start.y + moveYLength);

        moveAnimate(start, end);

    }

    private void autoScale() {

        if (getMatrixScaleX() > 3.0) {
            zoomAnimate(getMatrixScaleX(), 3.0f);
        } else if (getMatrixScaleX() < 0.98) {
//            zoomAnimate(getMatrixScaleX(), 1.0f);
        }
    }

    Handler handler = new Handler();


    float[] m = new float[9];

    private float getTranslateX() {
        matrix.getValues(m);
        return m[2];
    }

    private float getTranslateY() {
        matrix.getValues(m);
        return m[5];
    }

    private float getMatrixScaleY() {
        matrix.getValues(m);
        return m[4];
    }

    private float getMatrixScaleX() {
        matrix.getValues(m);
        return m[Matrix.MSCALE_X];
    }

    private float dip2Px(float value) {
        return getResources().getDisplayMetrics().density * value;
    }

    private float getBaseLine(Paint p, float top, float bottom) {
        Paint.FontMetrics fontMetrics = p.getFontMetrics();
        int baseline = (int) ((bottom + top - fontMetrics.bottom - fontMetrics.top) / 2);
        return baseline;
    }

    private void moveAnimate(Point start, Point end) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new MoveEvaluator(), start, end);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        MoveAnimation moveAnimation = new MoveAnimation();
        valueAnimator.addUpdateListener(moveAnimation);
        valueAnimator.setDuration(400);
        valueAnimator.start();
    }

    private void zoomAnimate(float cur, float tar) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(cur, tar);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        ZoomAnimation zoomAnim = new ZoomAnimation();
        valueAnimator.addUpdateListener(zoomAnim);
        valueAnimator.addListener(zoomAnim);
        valueAnimator.setDuration(400);
        valueAnimator.start();
    }

    private float zoom;

    private void zoom(float zoom) {
        float z = zoom / getMatrixScaleX();
        matrix.postScale(z, z, scaleX, scaleY);
        invalidate();
    }

    private void move(Point p) {
        float x = p.x - getTranslateX();
        float y = p.y - getTranslateY();
        matrix.postTranslate(x, y);
        invalidate();
    }

    class MoveAnimation implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            Point p = (Point) animation.getAnimatedValue();

            move(p);
        }
    }

    class MoveEvaluator implements TypeEvaluator {

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Point startPoint = (Point) startValue;
            Point endPoint = (Point) endValue;
            int x = (int) (startPoint.x + fraction * (endPoint.x - startPoint.x));
            int y = (int) (startPoint.y + fraction * (endPoint.y - startPoint.y));
            return new Point(x, y);
        }
    }

    class ZoomAnimation implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            zoom = (Float) animation.getAnimatedValue();
            zoom(zoom);

            if (DBG) {
                Log.d("zoomTest", "zoom:" + zoom);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        @Override
        public void onAnimationStart(Animator animation) {
        }

    }

    public void setData(List<StationMapBean> list) {
        this.dataList = list;
        init();
        invalidate();
    }

    ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            isScaling = true;
            float scaleFactor = detector.getScaleFactor();
            if (getMatrixScaleY() * scaleFactor > 3) {
                scaleFactor = 3 / getMatrixScaleY();
            }
            if (firstScale) {
                scaleX = detector.getCurrentSpanX();
                scaleY = detector.getCurrentSpanY();
                firstScale = false;
            }

            if (getMatrixScaleY() * scaleFactor < 0.5) {
                scaleFactor = 0.5f / getMatrixScaleY();
            }
            matrix.postScale(scaleFactor, scaleFactor, scaleX, scaleY);
            invalidate();
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            isScaling = false;
            firstScale = true;
        }
    });

    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            isOnClick = true;
            int x = (int) e.getX();
            int y = (int) e.getY();

            for (int i = 0; i < dataList.size(); i++) {
                int tempX = (int) ((dataList.get(i).getxNum() * seatWidth + dataList.get(i).getxNum() * spacing) * getMatrixScaleX() + getTranslateX());
                int maxTemX = (int) (tempX + seatWidth * getMatrixScaleX());

                int tempY = (int) ((dataList.get(i).getYnum() * seatHeight + dataList.get(i).getYnum() * verSpacing) * getMatrixScaleY() + getTranslateY());
                int maxTempY = (int) (tempY + seatHeight * getMatrixScaleY());

                if (dataList.get(i).getGwstye() == YIDONGGONGWEI && x >= tempX && x <= maxTemX && y >= tempY && y <= maxTempY) {
                    if(flag==2||flag==3){
                        if(clickPop!=null&&dataList.get(i).getSystate() == SEAT_TYPE_SOLD){
                            clickPop.clickSeat(1,dataList.get(i));
                        }
                        return super.onSingleTapConfirmed(e);
                    }else{
                        if (dataList.get(i).getSystate() == SEAT_TYPE_SOLD) {
                            if(clickPop!=null){
                                clickPop.clickSeat(1,dataList.get(i));
                            }
                            return super.onSingleTapConfirmed(e);
                        } else if (dataList.get(i).getSystate() == SEAT_TYPE_SELECTED) {
                            dataList.get(i).setSystate(SEAT_TYPE_AVAILABLE);
                            checkedList.remove(dataList.get(i).getId() + "");
                        } else if (dataList.get(i).getSystate() == SEAT_TYPE_AVAILABLE) {
                            if (checkedList.size() >= maxSelected) {
                                Toast.makeText(getContext(), "最多只能选择" + maxSelected + "个工位", Toast.LENGTH_SHORT).show();
                                return super.onSingleTapConfirmed(e);
                            }
                            dataList.get(i).setSystate(SEAT_TYPE_SELECTED);
                            checkedList.add(dataList.get(i).getId() + "");
                        }
                    }
                    isNeedDrawSeatBitmap = true;
                    float currentScaleY = getMatrixScaleY();

                    if (currentScaleY < 1.2) {
                        scaleX = x;
                        scaleY = y;
                        zoomAnimate(currentScaleY, 1.5f);
                    }
                    invalidate();
                    break;
                }else if(dataList.get(i).getGwstye() == GUDONGGONGWEI && x >= tempX && x <= maxTemX && y >= tempY && y <= maxTempY){
                    if(dataList.get(i).getGwxh() == 1||dataList.get(i).getGwxh() == 2){
                        if(clickPop!=null){
                            clickPop.clickSeat(1,dataList.get(i));
                        }
                        return super.onSingleTapConfirmed(e);
                    }
                    invalidate();
                    break;
                }
            }
            return super.onSingleTapConfirmed(e);
        }
    });


    public interface SeatChecker {
        /**
         * 是否可用座位
         *
         * @param row
         * @param column
         * @return
         */
        boolean isValidSeat(int row, int column);

        /**
         * 是否已售
         *
         * @param row
         * @param column
         * @return
         */
        boolean isSold(int row, int column);

        void checked(int row, int column);

        void unCheck(int row, int column);

        /**
         * 获取选中后座位上显示的文字
         *
         * @param row
         * @param column
         * @return 返回2个元素的数组, 第一个元素是第一行的文字, 第二个元素是第二行文字, 如果只返回一个元素则会绘制到座位图的中间位置
         */
        String[] checkedSeatTxt(int row, int column);

    }


    public interface ClickPop{
        void clickSeat(int type, StationMapBean stationMapBean);
    }


    public void setMaxSelected(int maxSelected) {
        this.maxSelected = maxSelected;
    }


    /**
     * 绘制边框
     */
    private void drawFrame(Canvas canvas) {
        //绘制边框
        framPaint.setStrokeWidth(spacing);
        framPaint.setColor(Color.GRAY);
        canvas.drawLine(0, 0, getWidth(), 0, framPaint);
        canvas.drawLine(0, 0, 0, getHeight(), framPaint);
        canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), framPaint);
    }


    public List<String> getCheckedList() {
        return checkedList;
    }

    public void clearBitmap() {
        if (seat_Avable_TopBitmap != null && !seat_Avable_TopBitmap.isRecycled()) {
            seat_Avable_TopBitmap.recycle();
            seat_Avable_TopBitmap = null;
        }
        if (seat_Checked_TopBitmap != null && !seat_Checked_TopBitmap.isRecycled()) {
            seat_Checked_TopBitmap.recycle();
            seat_Checked_TopBitmap = null;
        }
        if (seat_Sold_TopBitmap != null && !seat_Sold_TopBitmap.isRecycled()) {
            seat_Sold_TopBitmap.recycle();
            seat_Sold_TopBitmap = null;
        }
        if (seat_fix_TopBitmap != null && !seat_fix_TopBitmap.isRecycled()) {
            seat_fix_TopBitmap.recycle();
            seat_fix_TopBitmap = null;
        }
        if (seat_Hy_NomalBitmap != null && !seat_Hy_NomalBitmap.isRecycled()) {
            seat_Hy_NomalBitmap.recycle();
            seat_Hy_NomalBitmap = null;
        }
        if (seat_Bg_NomalBitmap != null && !seat_Bg_NomalBitmap.isRecycled()) {
            seat_Bg_NomalBitmap.recycle();
            seat_Bg_NomalBitmap = null;
        }
        if (lseat_fix_TopBitmap != null && !lseat_fix_TopBitmap.isRecycled()) {
            lseat_fix_TopBitmap.recycle();
            lseat_fix_TopBitmap = null;
        }
        if (lseat_Sold_TopBitmap != null && !lseat_Sold_TopBitmap.isRecycled()) {
            lseat_Sold_TopBitmap.recycle();
            lseat_Sold_TopBitmap = null;
        }
        if (lseat_Checked_TopBitmap != null && !lseat_Checked_TopBitmap.isRecycled()) {
            lseat_Checked_TopBitmap.recycle();
            lseat_Checked_TopBitmap = null;
        }
        if (lseat_Avable_TopBitmap != null && !lseat_Avable_TopBitmap.isRecycled()) {
            lseat_Avable_TopBitmap.recycle();
            lseat_Avable_TopBitmap = null;
        }
        if (seat_Gz_NomalBitmap != null && !seat_Gz_NomalBitmap.isRecycled()) {
            seat_Gz_NomalBitmap.recycle();
            seat_Gz_NomalBitmap = null;
        }
        if (bitmap3 != null && !bitmap3.isRecycled()) {
            bitmap3.recycle();
            bitmap3 = null;
        }
        if (seat_AllotBitmap != null && !seat_AllotBitmap.isRecycled()) {
            seat_AllotBitmap.recycle();
            seat_AllotBitmap = null;
        }
        if (lseat_AllotBitmap != null && !lseat_AllotBitmap.isRecycled()) {
            lseat_AllotBitmap.recycle();
            lseat_AllotBitmap = null;
        }
        if (overviewBitmap != null && !overviewBitmap.isRecycled()) {
            overviewBitmap.recycle();
            overviewBitmap = null;
        }

        if (map != null) {
            map = null;
        }
        if (dataList != null) {
            dataList.clear();
            dataList = null;
        }
        if (byList != null) {
            byList.clear();
            byList = null;
        }


    }

    public void setClickPop(ClickPop clickPop) {
        this.clickPop = clickPop;
        invalidate();
    }

    private ClickPop clickPop;


    /**
     * 压缩图片
     * @param drawable
     * @param w
     * @param h
     * @return
     */
    private Bitmap zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return newbmp;
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

}
