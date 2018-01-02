package com.yunhu.yhshxc.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.request.RequestOptions;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.AssetsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingli on 2017/9/22
 */

public class SearchChooseAdapter extends BaseAdapter {
    private Context mContext;
    private List<AssetsBean> data;
    private LayoutInflater inflater;
    private RequestOptions requestOptions;
    public SearchChooseAdapter(Context context , List<AssetsBean> data){
        this.mContext = context;
        this.data=data;
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
            convertView = inflater.inflate(R.layout.search_result_item_t1,null);
            holder = new ViewHolder();
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.list_func_check);
            holder.imageView = (ImageView) convertView.findViewById(R.id.list_func_icon);
            holder.title = (TextView) convertView.findViewById(R.id.list_func_title);
            holder.id = (TextView) convertView.findViewById(R.id.list_func_id);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();

        }

        final AssetsBean assetsBean = data.get(position);
        //根据传递的数据来判断是否已经选中
        if (comeData!=null&&comeData.size()>0){
            for (int i = 0; i < comeData.size(); i++) {
                AssetsBean comeBean = comeData.get(i);
                String comeId = comeBean.getId();
                if (assetsBean.getId().equals(comeId)){
                    assetsBean.setCheck(true);
                }
            }
        }
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(assetsBean.isCheck());
        holder.title.setText(assetsBean.getTitle());
        Glide.with(mContext).setDefaultRequestOptions(requestOptions).asBitmap().load(data.get(position).getUrl()).into(holder.imageView);
        holder.id.setText(assetsBean.getCode());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 assetsBean.setCheck(isChecked);
            }
        });
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

   private List<AssetsBean> comeData;
    //设置已经选择过得
   public  void setComeData(List<AssetsBean> comeData){
       this.comeData = comeData;
   }

    class ViewHolder{
        public ImageView imageView;
        public CheckBox checkBox;
        public TextView title;
        public TextView id;

    }


}
