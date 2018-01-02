package com.yunhu.yhshxc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Home_beam;

import java.util.List;

/**
 * ＠author zhonghuibin
 * create at 2017/12/12.
 * describe
 */
public class Home_GridViewAdapter extends BaseAdapter{
    private Context mContent;
    private List<Home_beam> mList;
    private int mType;

    public Home_GridViewAdapter(Context context, List<Home_beam> List,int type){
        mContent = context;
        mList = List;
        mType = type;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHoder viewHoder;
        if (view == null) {
            viewHoder = new ViewHoder();
            view = LayoutInflater.from(mContent).inflate(
                    R.layout.home_gridview_item, viewGroup, false);//item布局
            viewHoder.tv1 = (TextView) view.findViewById(R.id.tv_item1);
            viewHoder.tv2 = (TextView) view.findViewById(R.id.tv_item2);
            viewHoder.iv1 = (ImageView) view.findViewById(R.id.iv_item1);
            view.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) view.getTag();
        }
        if (mType==1){
            switch (i){
                case 0:
                    viewHoder.tv1.setText("今日美食");
                    viewHoder.tv2.setText("吃货的福利");
                    viewHoder.iv1.setImageResource(R.drawable.gridview_iv1);
                    break;
                case 1:
                    viewHoder.tv1.setText("热点资讯");
                    viewHoder.tv2.setText("持续关注热点");
                    viewHoder.iv1.setImageResource(R.drawable.gridview_iv2);
                    break;
                case 2:
                    viewHoder.tv1.setText("健身课程");
                    viewHoder.tv2.setText("练出马甲线");
                    viewHoder.iv1.setImageResource(R.drawable.gridview_iv3);
                    break;
                case 3:
                    viewHoder.tv1.setText("康体讲座");
                    viewHoder.tv2.setText("著名养身专家");
                    viewHoder.iv1.setImageResource(R.drawable.gridview_iv4);
                    break;
                case 4:
                    viewHoder.tv1.setText("自助缴费");
                    viewHoder.tv2.setText("方便自助");
                    viewHoder.iv1.setImageResource(R.drawable.gridview_iv5);
                    break;
                case 5:
                    viewHoder.tv1.setText("发票抬头");
                    viewHoder.tv2.setText("买单请出示");
                    viewHoder.iv1.setImageResource(R.drawable.gridview_iv6);
                    break;
                case 6:
                    viewHoder.tv1.setText("活动号集");
                    viewHoder.tv2.setText("最新活动征集令");
                    viewHoder.iv1.setImageResource(R.drawable.gridview_iv7);
                    break;
                case 7:
                    viewHoder.tv1.setText("拼车信息");
                    viewHoder.tv2.setText("拼车回家啦");
                    viewHoder.iv1.setImageResource(R.drawable.gridview_iv8);
                    break;
            }
        }else{
            switch (i){
                case 0:
                    viewHoder.tv1.setText("用印登记");
                    viewHoder.tv2.setText("登记请戳我");
                    viewHoder.iv1.setImageResource(R.drawable.gridview_iv9);
                    break;
                case 1:
                    viewHoder.tv1.setText("证照登记");
                    viewHoder.tv2.setText("证照登记");
                    viewHoder.iv1.setImageResource(R.drawable.gridview_iv10);
                    break;
                case 2:
                    viewHoder.tv1.setText("中融党建");
                    viewHoder.tv2.setText("永远跟党走");
                    viewHoder.iv1.setImageResource(R.drawable.gridview_iv11);
                    break;
                case 3:
                    viewHoder.tv1.setText("问卷调查");
                    viewHoder.tv2.setText("中融大调查");
                    viewHoder.iv1.setImageResource(R.drawable.gridview_iv12);
                    break;
                case 4:
                    viewHoder.tv1.setText("公司活动");
                    viewHoder.tv2.setText("公司有新活动");
                    viewHoder.iv1.setImageResource(R.drawable.gridview_iv13);
                    break;
                case 5:
                    viewHoder.tv1.setText("我的考勤");
                    viewHoder.tv2.setText("考勤详情查看");
                    viewHoder.iv1.setImageResource(R.drawable.gridview_iv14);
                    break;
                case 6:
                    viewHoder.tv1.setText("中融公益");
                    viewHoder.tv2.setText("让世界充满爱");
                    viewHoder.iv1.setImageResource(R.drawable.gridview_iv15);
                    break;
                case 7:
                    viewHoder.tv1.setText("培训学院");
                    viewHoder.tv2.setText("经济学家今日");
                    viewHoder.iv1.setImageResource(R.drawable.gridview_iv16);
                    break;
            }
        }

//        viewHoder.tv1.setText(mList.get(i).getName());
//        viewHoder.tv1.setText(mList.get(i).getContext());
//        viewHoder.iv1.setText("");
        return view;
    }

    class ViewHoder {
        TextView tv1;
        TextView tv2;
        ImageView iv1;
    }
}
