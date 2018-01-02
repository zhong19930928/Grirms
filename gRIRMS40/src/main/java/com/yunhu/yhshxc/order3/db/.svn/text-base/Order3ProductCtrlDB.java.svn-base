package com.yunhu.yhshxc.order3.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.order3.bo.Order3Dis;
import com.yunhu.yhshxc.order3.bo.Order3ProductCtrl;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;

public class Order3ProductCtrlDB {

	private DatabaseHelper openHelper;
	private Context mContext;
	public Order3ProductCtrlDB(Context context) {
		this.mContext = context;
		openHelper = DatabaseHelper.getInstance(context);
	}

	public Long insert(Order3ProductCtrl productCtrl) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		Long id = db.insert(openHelper.ORDER3_PRODUCT_CTRL, null,putContentValues(productCtrl));
		return id;

	}

	public List<Order3ProductCtrl> findAllProductCtrl(){
		List<Order3ProductCtrl> list = new ArrayList<Order3ProductCtrl>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ORDER3_PRODUCT_CTRL);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			Order3ProductCtrl ctrl = null;
			while (cursor.moveToNext()) {
				ctrl = putOrder3ProductCtrl(cursor);
				if (ctrl!=null) {
					list.add(ctrl);
				}
			}
			cursor.close();
		}
		return list;
	}
	
	public List<Order3ProductCtrl> findAllLastProductCtrlByCconfDid(int did){
		List<Order3ProductCtrl> list = new ArrayList<Order3ProductCtrl>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ORDER3_PRODUCT_CTRL).append(" where cId = ").append(did);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			Order3ProductCtrl ctrl = null;
			while (cursor.moveToNext()) {
				ctrl = putOrder3ProductCtrl(cursor);
				if (ctrl!=null) {
					if (ctrl.isProductLevel()) {
						list.add(ctrl);
					}else{
						childList(list, ctrl.getcId());
					}
				}
			}
			cursor.close();
		}
		return list;
	}
	
	public Order3ProductCtrl findProductCtrlByProductIdAndUnitId(int productId,int unitId){
		Order3ProductCtrl order3ProductCtrl = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ORDER3_PRODUCT_CTRL).append(" where productId = ").append(productId).append(" and unitId = ").append(unitId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			if (cursor.moveToNext()) {
				order3ProductCtrl = putOrder3ProductCtrl(cursor);
			}
			cursor.close();
		}
		return order3ProductCtrl;
	}
	
	
	public List<Order3ProductCtrl> findCommonProductCtrl(){
		List<Order3ProductCtrl> list = new ArrayList<Order3ProductCtrl>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ORDER3_PRODUCT_CTRL).append(" where count > 0").append(" order by count desc");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			Order3ProductCtrl ctrl = null;
			while (cursor.moveToNext()) {
				ctrl = putOrder3ProductCtrl(cursor);
				if (ctrl!=null) {
					list.add(ctrl);
				}
			}
			cursor.close();
		}
		return list;
	}
	
	public void updateProductCtrlCount(Order3ProductCtrl ctrl){
		StringBuffer sql = new StringBuffer();
		sql.append(" update ").append(openHelper.ORDER3_PRODUCT_CTRL).append(" set count = ").append(ctrl.getCount()+1);
		sql.append(" where id = ").append(ctrl.getId());
		openHelper.execSQL(sql.toString());
		if (!"0".equals(ctrl.getpId())) {
			updateParentProductCount(ctrl);
		}
	}
	
	private void updateParentProductCount(Order3ProductCtrl ctrl){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ORDER3_PRODUCT_CTRL).append(" where cId = ").append(ctrl.getpId());
		Cursor cursor = openHelper.query(sql.toString(), null);
		Order3ProductCtrl pCtrl = null;
		if (cursor!=null) {
			if (cursor.moveToNext()) {
				pCtrl = putOrder3ProductCtrl(cursor);
				if (pCtrl!=null) {
					updateProductCtrlCount(pCtrl);
				}
			}
			cursor.close();
		}
	}
	
	public List<Order3ProductCtrl> childList(List<Order3ProductCtrl> cList,String cId){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ORDER3_PRODUCT_CTRL).append(" where pId = '").append(cId).append("'");
		Cursor cursor = openHelper.query(sql.toString(), null);
		Order3ProductCtrl cCtrl = null;
		if (cursor!=null) {
			while (cursor.moveToNext()) {
				cCtrl = putOrder3ProductCtrl(cursor);
				if (cCtrl!=null) {
					if (cCtrl.isProductLevel()) {//说明是最底层
						cList.add(cCtrl);
					}else{
						childList(cList, cCtrl.getcId());
					}
				}
			}
			cursor.close();
		}
		return cList;
	}

	public List<Order3ProductCtrl> parentList(List<Order3ProductCtrl> pList,String pid){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ORDER3_PRODUCT_CTRL).append(" where cId = '").append(pid).append("'");
		Cursor cursor = openHelper.query(sql.toString(), null);
		Order3ProductCtrl pCtrl = null;
		if (cursor!=null) {
			if (cursor.moveToNext()) {
				pCtrl = putOrder3ProductCtrl(cursor);
				if (pCtrl!=null && !pList.contains(pCtrl)) {
					pList.add(pCtrl);
				}
			}
			cursor.close();
			if (pCtrl!=null && !"0".equals(pCtrl.getpId())) {
				parentList(pList, pCtrl.getpId());
			}
		}
		return pList;
	}
	
	public void delete() {
		openHelper.delete(openHelper.ORDER3_PRODUCT_CTRL, null, null);
	}

	private Order3ProductCtrl putOrder3ProductCtrl(Cursor cursor) {
		int i = 0;
		Order3ProductCtrl ctrl = new Order3ProductCtrl();
		ctrl.setId(cursor.isNull(i) ? null : String.valueOf(cursor.getInt(i)));i++;
		ctrl.setcId(cursor.getString(i++));
		ctrl.setpId(cursor.getString(i++));
		ctrl.setLabel(cursor.getString(i++));
		ctrl.setProductId(cursor.getInt(i++));
		ctrl.setUnitId(cursor.getInt(i++));
		ctrl.setCount(cursor.getInt(i++));
		if (!distControl(ctrl)) {
			return null;
		}
		if (ctrl.getProductId() !=0 && ctrl.getUnitId()!=0) {
//			ctrl.setProduct(new Order3Util(mContext).product(ctrl.getProductId(), ctrl.getUnitId()));
			ctrl.setProductLevel(true);
		}else{
			ctrl.setProductLevel(false);
		}
		return ctrl;
	}
	
	/**
	 * 分销控制
	 * @param ctrl
	 * @return true表示能卖，false表示不能卖
	 */
	private boolean distControl(Order3ProductCtrl ctrl){
		boolean flag = false;
		int is_dist_sales  = SharedPreferencesForOrder3Util.getInstance(mContext).getIsdistSales();//是否需要分销管理 1不要 2要 默认2
		if (is_dist_sales == 2) {//需要分销管理的情况查看是否在销售区域内
			String storeOrgCode = SharedPreferencesForOrder3Util.getInstance(mContext).getOrder3OrgId();
			List<Order3Dis> disList = new Order3DisDB(mContext).findOrder3DisByProductId(ctrl.getProductId());
			if (!disList.isEmpty()) {
				for (int j = 0; j < disList.size(); j++) {
					Order3Dis dis = disList.get(j);
					String orgCode = dis.getOrgCode();
					if (storeOrgCode.contains(orgCode)) {//选中的店面不包含分销机构code的话说明不在此区域销售
						flag = true;
						break;
					}
				}
			}else{
				flag = true;
			}
		}else{
			flag = true;
		}
		return flag;
	}

	private ContentValues putContentValues(Order3ProductCtrl productCtrl) {
		ContentValues cv = new ContentValues();
		cv.put("cId", productCtrl.getcId());
		cv.put("pId", productCtrl.getpId());
		cv.put("label", productCtrl.getLabel());
		cv.put("productId", productCtrl.getProductId());
		cv.put("unitId", productCtrl.getUnitId());
		cv.put("count", productCtrl.getCount());
		return cv;
	}

}
