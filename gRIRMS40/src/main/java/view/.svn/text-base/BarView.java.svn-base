package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.baidu.platform.comapi.map.E;
import com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser;

import java.util.ArrayList;
import java.util.List;


public abstract class BarView<AdressBookUser> extends HorizontalScrollView implements View.OnClickListener {
    protected FrameLayout mFrameLayout;
    protected List<com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser> list;
    protected List<View> mConent;
    protected BarViewListener mBarViewListener;

    public BarView(Context context) {
        this(context, null);
    }

    public BarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFillViewport(true);
        mFrameLayout = new FrameLayout(getContext());
        addView(mFrameLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mConent = new ArrayList<>();
        list = new ArrayList<>();
    }

    public final void addBt(com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser e) {
        if(e!=null){
            View view = createBt(e);
            int currentPostion = list.size();
            addBtView(view, currentPostion);
            view.setTag(currentPostion);
            view.setOnClickListener(this);
            list.add(e);
            mConent.add(view);
            reset(currentPostion);
        }else {
            list.clear();
            int currentPostion = list.size();
            addBtView(null, currentPostion);
            mConent.clear();
        }


    }

    public abstract void reset(int start);

    private void addBtView(View view, int posion) {
        if (view == null) {
            mFrameLayout.removeAllViews();
//            throw new RuntimeException("the view is null");
        }else{
            mFrameLayout.addView(view, genDefaultPaddingLayoutParams(posion));
            post(new Runnable() {
                @Override
                public void run() {
                    BarView.this.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                }
            });
        }

    }

    protected abstract LayoutParams genDefaultPaddingLayoutParams(int posion);

    protected abstract View createBt(com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser e);

    public void setBarListener(BarViewListener mBarViewListener) {
        this.mBarViewListener = mBarViewListener;
    }


    public interface BarViewListener<AdressBookUser> {
        void open(View v, com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser e, int postion);

        void close(View v, com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser e, int postion);
    }
}
