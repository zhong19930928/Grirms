package com.yunhu.yhshxc.activity.zrmenu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.AssetsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author suhu
 * @data 2017/10/24.
 * @description
 */

public class InventoryAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;
    private OnItemClick onItemClick;
    private int state;
    private List<AssetsBean> assetList;


    /**
     *@method
     *@author suhu
     *@time 2017/10/30 9:50
     *@param context
     *@param state 状态
     *             0：待盘点
     *             1：已盘点
     *             2：盘盈
     *
    */
    public InventoryAdapter(Context context,int state) {
        this.context = context;
        this.state = state;
        layoutInflater = LayoutInflater.from(context);

    }

    public void setAssetList(List<AssetsBean> assetList) {
        this.assetList = assetList;
        notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public int getCount() {
        return assetList != null ? assetList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return assetList != null ? assetList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     *@method
     *@author suhu
     *@time 2017/10/24 13:38
     *state :0：隐藏；1：显示
     *
    */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_invebtory, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        boolean type = assetList.get(position).isCheck();
        if (!type) {
            viewHolder.isShow.setVisibility(View.GONE);
            viewHolder.showText.setText("规格型号，使用人");
            viewHolder.showIm.setImageResource(R.drawable.ivb_open);
        } else {
            viewHolder.isShow.setVisibility(View.VISIBLE);
            viewHolder.showText.setText("收起");
            viewHolder.showIm.setImageResource(R.drawable.ivb_close);
        }
        viewHolder.onClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick!=null){
                    onItemClick.click(position,v);
                }
            }
        });

        String str;
        switch (state){
            case 0:
                str="待盘点";
                break;
            case 1:
                str="已盘点";
                break;
            case 2:
                str="盘盈";
                break;
            default:
                str = "";
        }
        viewHolder.itemState.setText(str);
        AssetsBean bean = assetList.get(position);

        Glide.with(context).load(bean.getUrl()).into(viewHolder.itemImage);
        viewHolder.zName.setText(bean.getTitle());
        viewHolder.barcode.setText(bean.getCode());
        viewHolder.classification.setText(bean.getFineKind());
        viewHolder.company.setText("后台无配置属性");
        viewHolder.useCompany.setText(bean.getUseCompany());
        viewHolder.useName.setText("后台无配置属性");
        viewHolder.time.setText("后台无配置属性");


        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_image)
        ImageView itemImage;
        @BindView(R.id.z_name)
        TextView zName;
        @BindView(R.id.item_state)
        TextView itemState;
        @BindView(R.id.barcode)
        TextView barcode;
        @BindView(R.id.classification)
        TextView classification;
        @BindView(R.id.company)
        TextView company;
        @BindView(R.id.use_company)
        TextView useCompany;
        @BindView(R.id.use_name)
        TextView useName;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.is_show)
        LinearLayout isShow;
        @BindView(R.id.show_text)
        TextView showText;
        @BindView(R.id.show_im)
        ImageView showIm;
        @BindView(R.id.on_click)
        LinearLayout onClick;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClick{

        /**
         * @param position
         * @param view
         *item上的控件的点击事件
         *
         */
        void click(int position,View view);
    }
}
