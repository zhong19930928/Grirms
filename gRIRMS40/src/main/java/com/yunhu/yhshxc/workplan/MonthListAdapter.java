package com.yunhu.yhshxc.workplan;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.workplan.bo.PlanData;
import com.yunhu.yhshxc.workplan.bo.WorkPlanModle;
import com.yunhu.yhshxc.workplan.db.WorkPlanModleDB;


public class MonthListAdapter extends BaseAdapter {
    private Context context;
    private List<PlanData> traceList = new ArrayList<PlanData>();
    private static final int TYPE_TOP = 0x0000;
    private static final int TYPE_NORMAL= 0x0001;
    private WorkPlanModleDB modleDb;

    public MonthListAdapter(Context context,List<PlanData> traceList) {
        this.context = context;
        this.traceList = traceList;
        modleDb= new WorkPlanModleDB(context);
    }

    @Override
    public int getCount() {
        return traceList.size();
    }

    @Override
    public PlanData getItem(int position) {
        return traceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
         PlanData planItem = getItem(position);
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_trace, parent, false);
            holder.plan_item_time = (TextView) convertView.findViewById(R.id.plan_item_time);//时间
            holder.plan_item_title = (TextView) convertView.findViewById(R.id.plan_item_title);//标题
            holder.plan_item_content=(TextView) convertView.findViewById(R.id.plan_item_content);//内容
            holder.plan_item_marks=(TextView) convertView.findViewById(R.id.plan_item_marks);//备注
            holder.tvTopLine = (TextView) convertView.findViewById(R.id.tvTopLine);//背景线
            holder.ll_plan_item_title = (LinearLayout) convertView.findViewById(R.id.ll_plan_item_title);//标题容器
            convertView.setTag(holder);
        }

        if (getItemViewType(position) == TYPE_TOP) {
            // 第一行头的竖线不显示
            holder.tvTopLine.setVisibility(View.INVISIBLE);


        } else if (getItemViewType(position) == TYPE_NORMAL) {
            holder.tvTopLine.setVisibility(View.VISIBLE);

        }
        
        holder.plan_item_time.setText(planItem.getPlanTime());
        
        List<WorkPlanModle> moList = modleDb.findWorkPlanModleListById(planItem.getPlanId());
        holder.ll_plan_item_title.removeAllViews();
        if(moList!=null&&moList.size()>0){
        	StringBuilder strBuilder = new StringBuilder();
        	for (int i = 0; i < moList.size(); i++) {		
//				strBuilder.append((i+1)+".").append(moList.get(i).getPlanTitle()).append("\n");
        		ItemPlanTitleView titleView = new ItemPlanTitleView(context);
        		titleView.setTitle((i+1)+"、"+moList.get(i).getPlanTitle());
        		holder.ll_plan_item_title.addView(titleView.getView());
			}
        	
//        	holder.plan_item_title.setText(strBuilder.toString());
      	
        }else{
//        	holder.plan_item_title.setText("");

        }
        
        return convertView;
    }

    @Override
    public int getItemViewType(int id) {
        if (id == 0) {
            return TYPE_TOP;
        }
        return TYPE_NORMAL;
    }

    static class ViewHolder {
    	//计划事件,计划标题,计划内容,计划,备注
        public TextView plan_item_time, plan_item_title,plan_item_content,plan_item_marks;
        //条目背景线颜色
        public TextView tvTopLine;
        public LinearLayout ll_plan_item_title;
        
    }
}