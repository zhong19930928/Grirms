package com.yunhu.yhshxc.order3;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.order3.bo.Order3Product;
import com.yunhu.yhshxc.order3.bo.Order3ProductCtrl;
import com.yunhu.yhshxc.order3.bo.Order3Promotion;
import com.yunhu.yhshxc.order3.bo.Order3PromotionSyncInfo;
import com.yunhu.yhshxc.order3.db.Order3ProductCtrlDB;
import com.yunhu.yhshxc.order3.db.Order3PromotionDB;
import com.yunhu.yhshxc.order3.util.Order3Util;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.order3.view.Order3SyncInfoItem;
import com.yunhu.yhshxc.utility.PublicUtils;

public class Order3SyncInfoActivity extends AbsBaseActivity{
	private ListView lv_info_sync;
	private SyncInfoDisAdapter adapter;
	private Order3PromotionDB promotionDB;
	private List<Order3Promotion> promotions;
	private Order3Util order3Util;
	private Order3ProductCtrlDB ctrlDB;
	private List<Order3PromotionSyncInfo> infos = new ArrayList<Order3PromotionSyncInfo>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order3_info_sync);
		initView();
		initWidget();
		initData();
	}
	
	private void initData() {
		adapter = new SyncInfoDisAdapter(this, infos);
		lv_info_sync.setAdapter(adapter);
	}

	private void initView() {
		lv_info_sync = (ListView) findViewById(R.id.lv_info_sync);
	}
	private void initWidget() {
		promotionDB = new Order3PromotionDB(this);
		order3Util = new Order3Util(this);
		ctrlDB = new Order3ProductCtrlDB(this);
		
		int level = SharedPreferencesForOrder3Util.getInstance(this).getOrder3StoreLevel();
		String orgCode = SharedPreferencesForOrder3Util.getInstance(this).getOrder3OrgId();
		promotions = promotionDB.findAllPromotionList(orgCode,level);
		
		for(int i = 0; i<promotions.size();i++){
			Order3ProductCtrl ctrl;
			Order3ProductCtrl ctrlZengPin;
			List<Order3ProductCtrl> list = new ArrayList<Order3ProductCtrl>();
			List<Order3ProductCtrl> listZeng = new ArrayList<Order3ProductCtrl>();
			Order3PromotionSyncInfo info = new Order3PromotionSyncInfo();
			Order3Promotion pro = promotions.get(i);
			Order3Product product = null;
//			JLog.d("aaa", pro.toString());
			if(!TextUtils.isEmpty(pro.getmId())&&!TextUtils.isEmpty(pro.getmUid())){
				 product = order3Util.product(Integer.parseInt(pro.getmId()), Integer.parseInt(pro.getmUid()));
			}
			if(product!=null){
				ctrl = ctrlDB.findProductCtrlByProductIdAndUnitId(product.getProductId(), product.getUnitId());
				if(ctrl!=null){
					list = ctrlDB.parentList(list, ctrl.getpId());
				}
				
			}
			Order3Product productZengPin = null;
			if(!TextUtils.isEmpty(pro.getsId())&&!TextUtils.isEmpty(pro.getsUid())){
				 productZengPin = order3Util.product(Integer.parseInt(pro.getsId()), Integer.parseInt(pro.getsUid()));
			}
			
			if(productZengPin!=null){
				ctrlZengPin = ctrlDB.findProductCtrlByProductIdAndUnitId(productZengPin.getProductId(), productZengPin.getUnitId());
				if(ctrlZengPin!=null){
					listZeng = ctrlDB.parentList(listZeng, ctrlZengPin.getpId());
				}
			}
			
			info.setSyncInfoName(pro.getName());
			String dis = pro.getDisRate();
			if(!TextUtils.isEmpty(dis)){
				 dis = PublicUtils.formatDouble(Double.parseDouble(dis)/Double.parseDouble("10"));
			}
			
			info.setSyncValidTerm(pro.getfTime()+"~"+pro.gettTime());
			info.setSyncInstruction(pro.getMark());
			if(pro.getPreType() ==1){
				if(pro.getDisType() == 3){
					info.setSyncDisFunction("单品买满数量 打折");
					if(product!=null){
						if(list!=null&&list.size()>0){
							StringBuffer sb = new StringBuffer();
							sb.append("【");
							for(int j = list.size()-1; j>=0; j--){
								sb.append(list.get(j).getLabel()).append("-");
							}
							sb.append(product.getName());
							sb.append("】满").append(PublicUtils.formatDouble(pro.getmCnt())).append(product.getUnit()).append(",打").append(dis).append("折");
							info.setSyncContent(sb.toString());
						}
					}
					
					
				}else if(pro.getDisType() == 1){
					info.setSyncDisFunction("单品买满数量 送赠品");
					if(product!=null&&productZengPin!=null){
						if(list!=null&&list.size()>0){
							StringBuffer sb = new StringBuffer();
							sb.append("【");
							for(int j = list.size()-1; j>=0; j--){
								sb.append(list.get(j).getLabel()).append("-");
							}
							sb.append(product.getName());
							sb.append("】满").append(PublicUtils.formatDouble(pro.getmCnt())).append(product.getUnit()).append("送【赠品-");
							if(listZeng!=null&&listZeng.size()>0){
								for(int k = listZeng.size()-1; k>=0;k--){
									sb.append(listZeng.get(k).getLabel()).append("-");
								}
								sb.append(productZengPin.getName());
								sb.append("】").append(PublicUtils.formatDouble(pro.getsCnt())).append(productZengPin.getUnit());
								info.setSyncContent(sb.toString());
							}
						}
					}
					
					
				}else if(pro.getDisType() ==2){
					info.setSyncDisFunction("单品买满数量 减免金额");
					if(product!=null){
						if(list!=null&&list.size()>0){
							StringBuffer sb = new StringBuffer();
							sb.append("【");
							for(int j = list.size()-1; j>=0; j--){
								sb.append(list.get(j).getLabel()).append("-");
							}
							sb.append(product.getName());
							sb.append("】满").append(PublicUtils.formatDouble(pro.getmCnt())).append(product.getUnit()).append(",减免金额").append(PublicUtils.formatDouble(pro.getDisAmount())).append("元");
							info.setSyncContent(sb.toString());
						}
					}
					
					
				}
			}else if(pro.getPreType() == 2){
				if(pro.getDisType() == 3){
					info.setSyncDisFunction("单品买满金额 打折");
					if(product!=null){
						if(list!=null&&list.size()>0){
							StringBuffer sb = new StringBuffer();
							sb.append("【");
							for(int j = list.size()-1; j>=0; j--){
								sb.append(list.get(j).getLabel()).append("-");
							}
							sb.append(product.getName());
							sb.append("】满").append(PublicUtils.formatDouble(pro.getAmount())).append("元,打").append(dis).append("折");
							info.setSyncContent(sb.toString());
						}
					}
					
				}else if(pro.getDisType() == 1){
					info.setSyncDisFunction("单品买满金额 送赠品");
					if(product!=null&&productZengPin!=null){
						if(list!=null&&list.size()>0){
							StringBuffer sb = new StringBuffer();
							sb.append("【");
							for(int j = list.size()-1; j>=0; j--){
								sb.append(list.get(j).getLabel()).append("-");
							}
							sb.append(product.getName());
							sb.append("】满").append(PublicUtils.formatDouble(pro.getAmount())).append("元,送【赠品-");
							if(listZeng!=null&&listZeng.size()>0){
								for(int k = listZeng.size()-1; k>=0;k--){
									sb.append(listZeng.get(k).getLabel()).append("-");
								}
								sb.append(productZengPin.getName());
								sb.append("】").append(PublicUtils.formatDouble(pro.getsCnt())).append(productZengPin.getUnit());
								info.setSyncContent(sb.toString());
							}
						}
					}
					
					
				}else if(pro.getDisType() ==2){
					info.setSyncDisFunction("单品买满金额 减免金额");
					if(product!=null){
						if(list!=null&&list.size()>0){
							StringBuffer sb = new StringBuffer();
							sb.append("【");
							for(int j = list.size()-1; j>=0; j--){
								sb.append(list.get(j).getLabel()).append("-");
							}
							sb.append(product.getName());
							sb.append("】满").append(PublicUtils.formatDouble(pro.getAmount())).append("元,减免金额").append(PublicUtils.formatDouble(pro.getDisAmount())).append("元");
							info.setSyncContent(sb.toString());
						}
					}
					
				}
			}else if(pro.getPreType() == 5){
				if(pro.getDisType() == 3){
					info.setSyncDisFunction("总和买满数量 打折");
					info.setSyncContent("总和买满数量"+PublicUtils.formatDouble(pro.getmCnt())+",打"+dis+"折");
				}else if(pro.getDisType() == 1){
					info.setSyncDisFunction("总和买满数量 送赠品");
					if(productZengPin!=null){
						if(listZeng!=null&&listZeng.size()>0){
							StringBuffer sb = new StringBuffer();
							sb.append("总和买满数量").append(PublicUtils.formatDouble(pro.getmCnt())).append("送【赠品-");
							for(int j = listZeng.size()-1; j>=0; j--){
								sb.append(listZeng.get(j).getLabel()).append("-");
							}
							sb.append(productZengPin.getName());
							sb.append("】").append(PublicUtils.formatDouble(pro.getsCnt())).append(productZengPin.getUnit());
							info.setSyncContent(sb.toString());
						}
					}
				}else if(pro.getDisType() ==2){
					info.setSyncDisFunction("总和买满数量 减免金额");
					info.setSyncContent("总和买满数量"+PublicUtils.formatDouble(pro.getmCnt())+",减免金额"+PublicUtils.formatDouble(pro.getDisAmount())+"元");
				}
			}else if(pro.getPreType() == 6){
				if(pro.getDisType() == 3){
					info.setSyncDisFunction("总和买满金额 打折");
					info.setSyncContent("总和买满金额"+PublicUtils.formatDouble(pro.getAmount())+"元,打"+dis+"折");
				}else if(pro.getDisType() == 1){
					info.setSyncDisFunction("总和买满金额 送赠品");
					if(productZengPin!=null){
						if(listZeng!=null&&listZeng.size()>0){
							StringBuffer sb = new StringBuffer();
							sb.append("总和买满金额").append(PublicUtils.formatDouble(pro.getAmount())).append("元,送【赠品-");
							for(int j = listZeng.size()-1; j>=0; j--){
								sb.append(listZeng.get(j).getLabel()).append("-");
							}
							sb.append(productZengPin.getName());
							sb.append("】").append(PublicUtils.formatDouble(pro.getsCnt())).append(productZengPin.getUnit());
							info.setSyncContent(sb.toString());
						}
					}
					
				}else if(pro.getDisType() ==2){
					info.setSyncDisFunction("总和买满金额 减免金额");
					info.setSyncContent("总和买满金额"+PublicUtils.formatDouble(pro.getAmount())+"元,减免金额"+PublicUtils.formatDouble(pro.getDisAmount())+"元");
				}
		
			}
			infos.add(info);
		}
	}
	class SyncInfoDisAdapter extends BaseAdapter{
		private List<Order3PromotionSyncInfo> syncInfos;
		private Context context;
		public SyncInfoDisAdapter(Context context,List<Order3PromotionSyncInfo> syncInfos){
			this.context = context;
			this.syncInfos = syncInfos;
		}
		@Override
		public int getCount() {
			return syncInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Order3SyncInfoItem item = null;
			if(convertView == null){
				item = new Order3SyncInfoItem(Order3SyncInfoActivity.this);
				convertView = item.getView();
				convertView.setTag(item);
			}else{
				item = (Order3SyncInfoItem) convertView.getTag();
			}
			item.initdata(position,syncInfos.get(position));
			return convertView;
		}
		
	}
}
