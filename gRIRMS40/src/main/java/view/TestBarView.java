package view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser;
import com.yunhu.yhshxc.utility.PublicUtils;


/**
 * date：2017/11/1 11:05
 * autor：王凡
 * description:
 * version:
 */

public class TestBarView extends BarView<AdressBookUser> {
    private static final int DEFAULT_WIDTH = 100;

    public TestBarView(Context context) {
        super(context);
    }

    public TestBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void reset(int start) {
        //clear
        for (int i = list.size() - 1; i > start; i--) {
            list.remove(i);
            mFrameLayout.removeView(mConent.get(i));
            mConent.remove(i);
        }
        for (int i = 0; i <= start; i++) {
            View v = mConent.get(i);
            TextView textView = (TextView) v.findViewById(R.id.tv_addrees_title);
            textView.setText(list.get(i).getOn());
            if(i==start){
                textView.setBackground(getResources().getDrawable(R.drawable.adrress_book_title));
            }else{
                textView.setBackgroundColor(getResources().getColor(R.color.zr_address_title_bg));
            }

        }
    }


    @Override
    protected LayoutParams genDefaultPaddingLayoutParams(int postion) {
        int px = PublicUtils.convertDIP2PX(getContext(),DEFAULT_WIDTH);
        LayoutParams layoutParams = new LayoutParams(px, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.leftMargin = postion * px;
        return layoutParams;
    }

    @Override
    protected View createBt(AdressBookUser testBean) {
        return newButton(testBean);
    }

    public View newButton(AdressBookUser testBean) {
//        Button button = new Button(getContext());
//        button.setBackgroundDrawable(getResources().getDrawable(R.drawable.adrress_book_title));
//        button.setGravity(Gravity.CENTER);
//        button.setTextSize(12);
//        button.setTextColor(Color.WHITE);
//        button.setText(testBean.getRn());
        View view= View.inflate(getContext(),R.layout.address_title_item,null);
        TextView textView = (TextView) view.findViewById(R.id.tv_addrees_title);
        textView.setText(testBean.getOn());
        return view;
    }

    @Override
    public void onClick(View v) {
        int postion = (int) v.getTag();
        if (postion != list.size()-1) {
            reset(postion);
            if (null != mBarViewListener) {
                mBarViewListener.close(mConent.get(postion), list.get(postion), postion);
            }
        } else {
            if (null != mBarViewListener) {
                mBarViewListener.open(mConent.get(postion), list.get(postion), postion);
            }
        }

    }
}
