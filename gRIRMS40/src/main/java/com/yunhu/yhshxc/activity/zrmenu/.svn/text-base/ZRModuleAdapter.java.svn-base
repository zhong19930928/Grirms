package com.yunhu.yhshxc.activity.zrmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.AssetsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingli on 2017/9/22
 */

public class ZRModuleAdapter extends BaseAdapter {
    private Context mContext;
    private List<AssetsBean> data;
    private LayoutInflater inflater;
    private RequestOptions requestOptions;
    private boolean isCanClick = false;//控制是否展示跳转图标
    private int type;
    public ZRModuleAdapter(Context context , List<AssetsBean> data,int type){
        this.mContext = context;
        this.data=data;
        this.type=type;
        inflater = LayoutInflater.from(context);
        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.image_loading);
        requestOptions.error(R.drawable.image_failed);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            if (type == 1){
                convertView = inflater.inflate(R.layout.zr_module_mine_item1,null);
            }else if(type ==2 ){
                convertView = inflater.inflate(R.layout.zr_module_mine_item2,null);
            }
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.zrmodule_mine_func_icon);
            holder.title = (TextView) convertView.findViewById(R.id.zrmodule_mine_func_title);
            holder.id = (TextView) convertView.findViewById(R.id.zrmodule_mine_func_id);
            holder.status = (TextView) convertView.findViewById(R.id.zrmodule_mine_func_status);
            holder.jumpIcon = (ImageView) convertView.findViewById(R.id.zrmodule_mine_func_jump);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final AssetsBean assetsBean = data.get(position);

        holder.title.setText(assetsBean.getTitle());
        Glide.with(mContext).setDefaultRequestOptions(requestOptions).asBitmap().load(data.get(position).getUrl()).into(holder.imageView);
        holder.id.setText(assetsBean.getCode());
        if (!isCanClick){
            holder.status.setText(assetsBean.getState());
            holder.jumpIcon.setVisibility(View.VISIBLE);
        }else{
            holder.status.setText("( "+assetsBean.getNum()+" )");
            holder.jumpIcon.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
   //获取选择过的信息集合
   public List<AssetsBean>  getSelectData(){
       List<AssetsBean> slData = new ArrayList<>();
       for (int i = 0; i < data.size(); i++) {
           AssetsBean as = data.get(i);
           if (as.isCheck()){
               slData.add(as);
           }
       }
       return slData;
   }

    public void setIsCanClickItem(boolean isCanClickItem){
    this.isCanClick = isCanClickItem;
    }
    class ViewHolder{
        public ImageView imageView;
        public TextView title;
        public TextView id;
        public TextView status;
        public ImageView jumpIcon;

    }
}
