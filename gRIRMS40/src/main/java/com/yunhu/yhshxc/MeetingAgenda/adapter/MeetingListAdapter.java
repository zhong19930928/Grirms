package com.yunhu.yhshxc.MeetingAgenda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunhu.yhshxc.MeetingAgenda.bo.MeetingRoomDetail;
import com.yunhu.yhshxc.R;

import java.util.List;

/**
 * ＠author zhonghuibin
 * create at 2017/10/30.
 * describe 会议室列表适配器
 */
public class MeetingListAdapter extends BaseAdapter {
    private List<MeetingRoomDetail.DataBeanX.DataBean> mList;
    private Context mContext;
    private String meeting_begin,meeting_end;
    public MeetingListAdapter(List<MeetingRoomDetail.DataBeanX.DataBean> list,Context context,String meeting_begin,String meeting_end){
        this.mContext = context;
        this.mList = list;
        this.meeting_begin = meeting_begin;
        this.meeting_end = meeting_end;

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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Viewholder viewholder;
        if (view == null) {
            viewholder = new Viewholder();
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.item_meetroom, viewGroup, false);
            viewholder.tv1 = (TextView) view.findViewById(R.id.tv_name);
            viewholder.tv2 = (TextView) view.findViewById(R.id.tv_people_number);
            viewholder.tv3 = (TextView) view.findViewById(R.id.tv_article7);
            viewholder.tv_bookroom = (TextView) view.findViewById(R.id.tv_bookroom);
            viewholder.iv1 = (ImageView) view.findViewById(R.id.iv_article1);
            viewholder.iv2 = (ImageView) view.findViewById(R.id.iv_article2);
            viewholder.iv3 = (ImageView) view.findViewById(R.id.iv_article3);
            viewholder.iv4 = (ImageView) view.findViewById(R.id.iv_article4);
            viewholder.iv5 = (ImageView) view.findViewById(R.id.iv_article5);
            view.setTag(viewholder);
        } else {
            viewholder = (Viewholder) view.getTag();
        }
        viewholder.tv1.setText(mList.get(i).getBuildingname()+"栋"+mList.get(i).getFloorname()+"层"+mList.get(i).getName()+"会议室");
        viewholder.tv2.setText("规格："+mList.get(i).getGalleryful()+"人");

        if (mList.get(i).getArticles() != null){
            if (mList.get(i).getArticles().size()>5){
                viewholder.tv3.setVisibility(View.VISIBLE);
            }else{
                viewholder.tv3.setVisibility(View.GONE);
            }
            if (mList.get(i).getArticles().size() == 1){
                Glide.with(mContext).load(mList.get(i).getArticles().get(0).getThumb()).into(viewholder.iv1);
                viewholder.iv2.setImageResource(R.color.transparent);
                viewholder.iv3.setImageResource(R.color.transparent);
                viewholder.iv4.setImageResource(R.color.transparent);
                viewholder.iv5.setImageResource(R.color.transparent);
            }else if (mList.get(i).getArticles().size() == 2){
                Glide.with(mContext).load(mList.get(i).getArticles().get(0).getThumb()).into(viewholder.iv1);
                Glide.with(mContext).load(mList.get(i).getArticles().get(1).getThumb()).into(viewholder.iv2);
                viewholder.iv3.setImageResource(R.color.transparent);
                viewholder.iv4.setImageResource(R.color.transparent);
                viewholder.iv5.setImageResource(R.color.transparent);
            }else if (mList.get(i).getArticles().size() == 3){
                Glide.with(mContext).load(mList.get(i).getArticles().get(0).getThumb()).into(viewholder.iv1);
                Glide.with(mContext).load(mList.get(i).getArticles().get(1).getThumb()).into(viewholder.iv2);
                Glide.with(mContext).load(mList.get(i).getArticles().get(2).getThumb()).into(viewholder.iv3);
                viewholder.iv4.setImageResource(R.color.transparent);
                viewholder.iv5.setImageResource(R.color.transparent);
            }else if (mList.get(i).getArticles().size() == 4){
                Glide.with(mContext).load(mList.get(i).getArticles().get(0).getThumb()).into(viewholder.iv1);
                Glide.with(mContext).load(mList.get(i).getArticles().get(1).getThumb()).into(viewholder.iv2);
                Glide.with(mContext).load(mList.get(i).getArticles().get(2).getThumb()).into(viewholder.iv3);
                Glide.with(mContext).load(mList.get(i).getArticles().get(3).getThumb()).into(viewholder.iv4);
                viewholder.iv5.setImageResource(R.color.transparent);
            }else if (mList.get(i).getArticles().size() > 4){
                Glide.with(mContext).load(mList.get(i).getArticles().get(0).getThumb()).into(viewholder.iv1);
                Glide.with(mContext).load(mList.get(i).getArticles().get(1).getThumb()).into(viewholder.iv2);
                Glide.with(mContext).load(mList.get(i).getArticles().get(2).getThumb()).into(viewholder.iv3);
                Glide.with(mContext).load(mList.get(i).getArticles().get(3).getThumb()).into(viewholder.iv4);
                Glide.with(mContext).load(mList.get(i).getArticles().get(4).getThumb()).into(viewholder.iv5);
            }
        }
        return view;
    }


    class Viewholder{
        TextView tv1;
        TextView tv2;
        TextView tv_bookroom;
        ImageView iv1;
        ImageView iv2;
        ImageView iv3;
        ImageView iv4;
        ImageView iv5;
        TextView tv3;
    }
}
