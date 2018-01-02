package com.yunhu.yhshxc.activity.carSales.manager;

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
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProduct;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductCtrl;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesPromotion;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesPromotionSyncInfo;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesProductCtrlDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesPromotionDB;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.utility.PublicUtils;

public class CarSalesSyncInfoActivity extends AbsBaseActivity{
	private ListView lv_info_sync;
	private SyncInfoDisAdapter adapter;
	private CarSalesPromotionDB promotionDB;
	private List<CarSalesPromotion> promotions;
	private CarSalesUtil carSalesUtil;
	private CarSalesProductCtrlDB ctrlDB;
	private List<CarSalesPromotionSyncInfo> infos = new ArrayList<CarSalesPromotionSyncInfo>();
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
		promotionDB = new CarSalesPromotionDB(this);
		carSalesUtil = new CarSalesUtil(this);
		ctrlDB = new CarSalesProductCtrlDB(this);
		
		int level = SharedPreferencesForCarSalesUtil.getInstance(this).getCarSalesStoreLevel();
		String orgCode = SharedPreferencesForCarSalesUtil.getInstance(this).getCarSalesOrgId();
		promotions = promotionDB.findAllPromotionList(orgCode,level);
		
		for(int i = 0; i<promotions.size();i++){
			CarSalesProductCtrl ctrl;
			CarSalesProductCtrl ctrlZengPin;
			List<CarSalesProductCtrl> list = new ArrayList<CarSalesProductCtrl>();
			List<CarSalesProductCtrl> listZeng = new ArrayList<CarSalesProductCtrl>();
			CarSalesPromotionSyncInfo info = new CarSalesPromotionSyncInfo();
			CarSalesPromotion pro = promotions.get(i);
			CarSalesProduct product = null;
//			JLog.d("aaa", pro.toString());
			if(!TextUtils.isEmpty(pro.getmId())&&!TextUtils.isEmpty(pro.getmUid())){
				 product = carSalesUtil.product(Integer.parseInt(pro.getmId()), Integer.parseInt(pro.getmUid()));
			}
			if(product!=null){
				ctrl = ctrlDB.findProductCtrlByProductIdAndUnitId(product.getProductId(), product.getUnitId());
				if(ctrl!=null){
					list = ctrlDB.parentList(list, ctrl.getpId());
				}
				
			}
			CarSalesProduct productZengPin = null;
			if(!TextUtils.isEmpty(pro.getsId())&&!TextUtils.isEmpty(pro.getsUid())){
				 productZengPin = carSalesUtil.product(Integer.parseInt(pro.getsId()), Integer.parseInt(pro.getsUid()));
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
		private List<CarSalesPromotionSyncInfo> syncInfos;
		private Context context;
		public SyncInfoDisAdapter(Context context,List<CarSalesPromotionSyncInfo> syncInfos){
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
			CarSalesSyncInfoItem item = null;
			if(convertView == null){
				item = new CarSalesSyncInfoItem(CarSalesSyncInfoActivity.this);
				convertView = item.getView();
				convertView.setTag(item);
			}else{
				item = (CarSalesSyncInfoItem) convertView.getTag();
			}
			item.initdata(position,syncInfos.get(position));
			return convertView;
		}
		
	}
}
