package com.yunhu.yhshxc.MeetingAgenda.view.calendars;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CalendarView extends ViewGroup {

    private static final String TAG = "CalendarView";

    private int selectPostion = -1;

    private CaledarAdapter adapter;
    private List<CalendarBean> data;
    private OnItemClickListener onItemClickListener;

    private int row = 6;
    private int column = 7;
    private int itemWidth;
    private int itemHeight;

    private boolean isToday;

    public interface OnItemClickListener {
        void onItemClick(View view, int postion, CalendarBean bean);
    }

    public interface OnGetMonthListener {
        void onMonth(CalendarBean bean);
    }

    public CalendarView(Context context, int row) {
        super(context);
        this.row = row;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public int getItemHeight() {
        return itemHeight;
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public void setAdapter(CaledarAdapter adapter) {
        this.adapter = adapter;
    }

    public void setData(List<CalendarBean> data,boolean isToday) {
        this.data = data;
        this.isToday=isToday;
        setItem();
        requestLayout();
    }

    private void setItem() {

        selectPostion = -1;
        if (adapter == null) {
            throw new RuntimeException("adapter is null,please setadapter");
        }

        for (int i = 0; i < data.size(); i++) {
            CalendarBean bean = data.get(i);
            View view = getChildAt(i);
            View chidView = adapter.getView(view, this, bean);

            if (view == null || view != chidView) {
                addViewInLayout(chidView, i, chidView.getLayoutParams(), true);
            }

            if(isToday&&selectPostion==-1){
                int[]date=CalendarUtil.getYMD(new Date());
                if(bean.year==date[0]&&bean.moth==date[1]&&bean.day==date[2]){
                     selectPostion=i;
                }
            }else {
                if (selectPostion == -1 && bean.day == 1) {
                    selectPostion = i;
                }
            }

            chidView.setSelected(selectPostion==i);
            //如果为选中状态则将字体设为白色
            if (selectPostion==i){
                lastTextView  = (TextView) chidView.findViewById(R.id.text_day_item);
                lastTextView.setTextColor(Color.WHITE);
            }
            setItemClick(chidView, i, bean);

        }
    }

    public Object[] getSelect(){
         return new Object[]{getChildAt(selectPostion),selectPostion,data.get(selectPostion)};
    }
    //用于记录上一次选中的日期控件
    private TextView lastTextView= null;

    public void setItemClick(final View view, final int potsion, final CalendarBean bean) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectPostion != -1) {
                    getChildAt(selectPostion).setSelected(false);
                    getChildAt(potsion).setSelected(true);
                }
                selectPostion = potsion;

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, potsion, bean);
                }
                //如果记录的上次选中控件不为空,则恢复非选中字体颜色
                if (lastTextView!=null){
                    if (bean.mothFlag != 0) {
                        lastTextView.setTextColor(Color.GRAY);
                    } else {
                        lastTextView.setTextColor(Color.BLACK);
                    }
                }
                //将本次选中的字体颜色设为白色
                lastTextView = (TextView) view.findViewById(R.id.text_day_item);
                lastTextView.setTextColor(Color.WHITE);

            }
        });
    }

    public void setOtherItemClick(final View view, final int potsion, final CalendarBean bean) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {



                if (selectPostion != -1) {
                    getChildAt(selectPostion).setSelected(false);
                    getChildAt(potsion).setSelected(true);
                }
                selectPostion = potsion;

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, potsion, bean);
                }
                //如果记录的上次选中控件不为空,则恢复非选中字体颜色
                if (lastTextView!=null){
                    if (bean.mothFlag != 0) {
                        lastTextView.setTextColor(Color.GRAY);
                    } else {
                        lastTextView.setTextColor(Color.BLACK);
                    }
                }
                //将本次选中的字体颜色设为白色
                lastTextView = (TextView) view.findViewById(R.id.text_day_item);
                lastTextView.setTextColor(Color.WHITE);

            }
        });
    }



    public int[] getSelectPostion() {
        Rect rect = new Rect();
        try {
            getChildAt(selectPostion).getHitRect(rect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new int[]{rect.left, rect.top, rect.right, rect.top};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int parentWidth = MeasureSpec.getSize(MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY));

        itemWidth = parentWidth / column;
        itemHeight = itemWidth;

        View view = getChildAt(0);
        if (view == null) {
            return;
        }
        LayoutParams params = view.getLayoutParams();
        if (params != null && params.height > 0) {
            itemHeight = params.height;
        }
        setMeasuredDimension(parentWidth, itemHeight * row);


        for(int i=0;i<getChildCount();i++){
            View childView=getChildAt(i);
            childView.measure(MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY));
        }

        Log.i(TAG, "onMeasure() called with: itemHeight = [" + itemHeight + "], itemWidth = [" + itemWidth + "]");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i <getChildCount(); i++) {
            layoutChild(getChildAt(i), i, l, t, r, b);
        }
    }

    private void layoutChild(View view, int postion, int l, int t, int r, int b) {

        int cc = postion % column;
        int cr = postion / column;

        int itemWidth = view.getMeasuredWidth();
        int itemHeight = view.getMeasuredHeight();

        l = cc * itemWidth;
        t = cr * itemHeight;
        r = l + itemWidth;
        b = t + itemHeight;
        view.layout(l, t, r, b);

    }

    public void setIsStation(boolean station) {
        isStation = station;
    }

    private boolean isStation;//表示从选工位页面进入


    public void setCheckTime( String stardTime,String endTime,boolean isremove){
        SimpleDateFormat formate = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        String time;
        for (int i = 0; i < data.size(); i++) {
            CalendarBean bean = data.get(i);
            View view = getChildAt(i);
            time = new StringBuffer().append(bean.year).append("-").append(getDisPlayNumber(bean.moth)).append("-").append(getDisPlayNumber(bean.day)).append(" 00:00:00").toString();
//            View chidView = adapter.getView(view, this, bean);
            try {
                if(isremove){
                    lastTextView = (TextView) view.findViewById(R.id.text_day_item);
                    if(DateUtil.isInDate(formate.parse(time),stardTime,endTime)){
                        view.setSelected(false);
                        lastTextView.setTextColor(Color.BLACK);
                    }else{
                        view.setSelected(false);
                        if (bean.mothFlag != 0) {
                            lastTextView.setTextColor(Color.GRAY);
                        } else {
                            lastTextView.setTextColor(Color.BLACK);
                        }
                    }
                }else{
                    lastTextView = (TextView) view.findViewById(R.id.text_day_item);
                    if(DateUtil.isInDate(formate.parse(time),stardTime,endTime)){
                        view.setSelected(true);
                        lastTextView.setTextColor(Color.WHITE);
                    }else {
                        view.setSelected(false);
                        if (bean.mothFlag != 0) {
                            lastTextView.setTextColor(Color.GRAY);
                        } else {
                            lastTextView.setTextColor(Color.BLACK);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        requestLayout();

    }

    private String getDisPlayNumber(int num) {
        return num < 10 ? "0" + num : "" + num;
    }
}
