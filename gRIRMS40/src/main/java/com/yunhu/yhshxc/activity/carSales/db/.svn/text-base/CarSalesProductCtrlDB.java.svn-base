package com.yunhu.yhshxc.activity.carSales.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductCtrl;
import com.yunhu.yhshxc.database.DatabaseHelper;

public class CarSalesProductCtrlDB {

	private DatabaseHelper openHelper;
	private Context mContext;
	public CarSalesProductCtrlDB(Context context) {
		this.mContext = context;
		openHelper = DatabaseHelper.getInstance(context);
	}

	public Long insert(CarSalesProductCtrl productCtrl) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		Long id = db.insert(openHelper.CAR_SALES_PRODUCT_CTRL, null,putContentValues(productCtrl));
		return id;

	}

	public List<CarSalesProductCtrl> findAllProductCtrl(){
		List<CarSalesProductCtrl> list = new ArrayList<CarSalesProductCtrl>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CTRL);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			CarSalesProductCtrl ctrl = null;
			while (cursor.moveToNext()) {
				ctrl = putCarSalesProductCtrl(cursor);
				if (ctrl!=null) {
					list.add(ctrl);
				}
			}
			cursor.close();
		}
		return list;
	}
	
	public List<CarSalesProductCtrl> findAllLastProductCtrlByCconfDid(int did){
		List<CarSalesProductCtrl> list = new ArrayList<CarSalesProductCtrl>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" where cId = ").append(did);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			CarSalesProductCtrl ctrl = null;
			while (cursor.moveToNext()) {
				ctrl = putCarSalesProductCtrl(cursor);
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
	
	public CarSalesProductCtrl findProductCtrlByProductIdAndUnitId(int productId,int unitId){
		CarSalesProductCtrl CarSalesProductCtrl = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" where productId = ").append(productId).append(" and unitId = ").append(unitId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			if (cursor.moveToNext()) {
				CarSalesProductCtrl = putCarSalesProductCtrl(cursor);
			}
			cursor.close();
		}
		return CarSalesProductCtrl;
	}
	public CarSalesProductCtrl findProductCtrlById(int id){
		CarSalesProductCtrl CarSalesProductCtrl = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" where id = ").append(id);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			if (cursor.moveToNext()) {
				CarSalesProductCtrl = putCarSalesProductCtrl(cursor);
			}
			cursor.close();
		}
		return CarSalesProductCtrl;
	}
	
	/**
	 * 查询常用列表
	 * @return
	 */
	public List<CarSalesProductCtrl> findCommonProductCtrl(){
		List<CarSalesProductCtrl> list = new ArrayList<CarSalesProductCtrl>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" where count > 0").append(" order by count desc");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			CarSalesProductCtrl ctrl = null;
			while (cursor.moveToNext()) {
				ctrl = putCarSalesProductCtrl(cursor);
				if (ctrl!=null) {
					list.add(ctrl);
				}
			}
			cursor.close();
		}
		return list;
	}
	/**
	 * 查询退货列表
	 * @return
	 */
	public List<CarSalesProductCtrl> findReturnProductCtrl(){
		List<CarSalesProductCtrl> list = new ArrayList<CarSalesProductCtrl>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" where returnCount is not null").append(" and returnCount <> '").append("").append("'").append(" and returnCount <> '0' and returnCount <> '0.0'");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			CarSalesProductCtrl ctrl = null;
			while (cursor.moveToNext()) {
				ctrl = putCarSalesProductCtrl(cursor);
				if (ctrl!=null) {
					list.add(ctrl);
				}
			}
			cursor.close();
		}
		return list;
	}
	/**
	 * 查询装车列表
	 * @return
	 */
	public List<CarSalesProductCtrl> findLoadingProductCtrl(){
		List<CarSalesProductCtrl> list = new ArrayList<CarSalesProductCtrl>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" where loadingCount is not null").append(" and loadingCount <> '").append("").append("'");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			CarSalesProductCtrl ctrl = null;
			while (cursor.moveToNext()) {
				ctrl = putCarSalesProductCtrl(cursor);
				if (ctrl!=null) {
					list.add(ctrl);
				}
			}
			cursor.close();
		}
		return list;
	}
	/**
	 * 查询卸车列表
	 * @return
	 */
	public List<CarSalesProductCtrl> findUnLoadingProductCtrl(){
		List<CarSalesProductCtrl> list = new ArrayList<CarSalesProductCtrl>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" where unLoadingCount is not null").append(" and unLoadingCount <> '").append("").append("'");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			CarSalesProductCtrl ctrl = null;
			while (cursor.moveToNext()) {
				ctrl = putCarSalesProductCtrl(cursor);
				if (ctrl!=null) {
					list.add(ctrl);
				}
			}
			cursor.close();
		}
		return list;
	}
	/**
	 * 查询补货列表
	 * @return
	 */
	public List<CarSalesProductCtrl> findRplenishmentProductCtrl(){
		List<CarSalesProductCtrl> list = new ArrayList<CarSalesProductCtrl>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" where replenishmentCount is not null").append(" and replenishmentCount <> '").append("").append("'");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			CarSalesProductCtrl ctrl = null;
			while (cursor.moveToNext()) {
				ctrl = putCarSalesProductCtrl(cursor);
				if (ctrl!=null) {
					list.add(ctrl);
				}
			}
			cursor.close();
		}
		return list;
	}
	/**
	 * 查询缺货列表
	 * @return
	 */
	public List<CarSalesProductCtrl> findOutProductCtrl(){
		List<CarSalesProductCtrl> list = new ArrayList<CarSalesProductCtrl>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" where outCount is not null").append(" and outCount <> '").append("").append("'");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			CarSalesProductCtrl ctrl = null;
			while (cursor.moveToNext()) {
				ctrl = putCarSalesProductCtrl(cursor);
				if (ctrl!=null) {
					list.add(ctrl);
				}
			}
			cursor.close();
		}
		return list;
	}
	/**
	 * 查询库存盘点列表
	 * @return
	 */
	public List<CarSalesProductCtrl> findInvertyProductCtrl(){
		List<CarSalesProductCtrl> list = new ArrayList<CarSalesProductCtrl>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" where inverty is not null").append(" and inverty <> '").append("").append("'");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			CarSalesProductCtrl ctrl = null;
			while (cursor.moveToNext()) {
				ctrl = putCarSalesProductCtrl(cursor);
				if (ctrl!=null) {
					list.add(ctrl);
				}
			}
			cursor.close();
		}
		return list;
	}
	public void updateAllChangedCtrl(List<CarSalesProductCtrl> ctrls ){
		DatabaseHelper.getInstance(mContext).beginTransaction();
		for(int i = 0; i<ctrls.size(); i++){
			updateProductCtrlReturnCount(ctrls.get(i));
		}
		DatabaseHelper.getInstance(mContext).endTransaction();
	}
	public void updateAllChangedLoadingCtrl(List<CarSalesProductCtrl> ctrls ){
		DatabaseHelper.getInstance(mContext).beginTransaction();
		for(int i = 0; i<ctrls.size(); i++){
			updateProductCtrlLoadingCount(ctrls.get(i));
		}
		DatabaseHelper.getInstance(mContext).endTransaction();

	}
	public void updateAllChangedUnLoadingCtrl(List<CarSalesProductCtrl> ctrls ){
		DatabaseHelper.getInstance(mContext).beginTransaction();
		for(int i = 0; i<ctrls.size(); i++){
			updateProductCtrlUnLoadingCount(ctrls.get(i));
		}
		DatabaseHelper.getInstance(mContext).endTransaction();
	}
	public void updateAllChangedRplenishmentCtrl(List<CarSalesProductCtrl> ctrls ){
		DatabaseHelper.getInstance(mContext).beginTransaction();
		for(int i = 0; i<ctrls.size(); i++){
			updateProductCtrlrRplenishmentCount(ctrls.get(i));
		}
		DatabaseHelper.getInstance(mContext).endTransaction();
	}
	public void updateAllChangedOutCtrl(List<CarSalesProductCtrl> ctrls ){
		DatabaseHelper.getInstance(mContext).beginTransaction();
		for(int i = 0; i<ctrls.size(); i++){
			updateProductCtrlOutCount(ctrls.get(i));
		}
		DatabaseHelper.getInstance(mContext).endTransaction();
	}
	public void updateAllChangedInvertyCtrl(List<CarSalesProductCtrl> ctrls ){
		DatabaseHelper.getInstance(mContext).beginTransaction();
		for(int i = 0; i<ctrls.size(); i++){
			updateProductCtrlInvertyCount(ctrls.get(i));
		}
		DatabaseHelper.getInstance(mContext).endTransaction();
	}
	/**
	 * 更新成为常用列表
	 * @param ctrl
	 */
	public void updateProductCtrlCount(CarSalesProductCtrl ctrl){
		StringBuffer sql = new StringBuffer();
		sql.append(" update ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" set count = ").append(ctrl.getCount()+1);
		sql.append(" where id = ").append(Integer.parseInt(ctrl.getId()));
		openHelper.execSQL(sql.toString());
		if (!"0".equals(ctrl.getpId())) {
			updateParentProductCount(ctrl);
		}
	}
	
	private void updateParentProductCount(CarSalesProductCtrl ctrl){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" where cId = '").append(ctrl.getpId()).append("'");
		Cursor cursor = openHelper.query(sql.toString(), null);
		CarSalesProductCtrl pCtrl = null;
		if (cursor!=null) {
			if (cursor.moveToNext()) {
				pCtrl = putCarSalesProductCtrl(cursor);
				if (pCtrl!=null) {
					updateProductCtrlCount(pCtrl);
				}
			}
			cursor.close();
		}
	}
	
	public void findReturnProductParent(CarSalesProductCtrl ctrl,HashMap<String, CarSalesProductCtrl> arr){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" where cId = '").append(ctrl.getpId()).append("'");
		Cursor cursor = openHelper.query(sql.toString(), null);
		CarSalesProductCtrl pCtrl = null;
		if (cursor!=null) {
			if (cursor.moveToNext()) {
				pCtrl = putCarSalesProductCtrl(cursor);
				if (pCtrl!=null) {
					arr.put(pCtrl.getcId(), pCtrl);
					findReturnProductParent(pCtrl,arr);
				}
			}
			cursor.close();
		}
	}
	public List<CarSalesProductCtrl> getAllReturnList(List<CarSalesProductCtrl> child,HashMap<String, CarSalesProductCtrl> parent){
//		int key = 0;
//		for(int i = 0; i<parent.size(); i++){
//			key = parent.keyAt(i);
//			CarSalesProductCtrl obj = parent.get(key);
//			child.add(obj);
//		}
		
		for(Map.Entry<String, CarSalesProductCtrl> entry : parent.entrySet()){    
			CarSalesProductCtrl  obj = entry.getValue();
			child.add(obj);
		} 
		return child;
	}
	/**
	 * 更新成为退货列表
	 * @param ctrl
	 */
	public void updateProductCtrlReturnCount(CarSalesProductCtrl ctrl){
		StringBuffer sql = new StringBuffer();
		sql.append(" update ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" set returnCount = '").append(ctrl.getReturnCount()).append("'");
		sql.append(" where id = ").append(ctrl.getId());
		openHelper.execSQL(sql.toString());
//		if (ctrl.getpId()!=0) {
//			updateParentProductReturnCount(ctrl);
//		}
	}
	
	/**
	 * 更新成为装车列表
	 * @param ctrl
	 */
	public void updateProductCtrlLoadingCount(CarSalesProductCtrl ctrl){
		StringBuffer sql = new StringBuffer();
		sql.append(" update ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" set loadingCount = '").append(ctrl.getLoadingCount()).append("'").append(" ,dataId = ").append(ctrl.getDataId());
		sql.append(" where id = ").append(ctrl.getId());
		openHelper.execSQL(sql.toString());
	}
	
	/**
	 * 更新成为卸车列表
	 * @param ctrl
	 */
	public void updateProductCtrlUnLoadingCount(CarSalesProductCtrl ctrl){
		StringBuffer sql = new StringBuffer();
		sql.append(" update ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" set unLoadingCount = '").append(ctrl.getUnLoadingCount()).append("'").append(" ,dataId = ").append(ctrl.getDataId());
		sql.append(" where id = ").append(ctrl.getId());
		openHelper.execSQL(sql.toString());
	}
	/**
	 * 更新成为补货列表
	 * @param ctrl
	 */
	public void updateProductCtrlrRplenishmentCount(CarSalesProductCtrl ctrl){
		StringBuffer sql = new StringBuffer();
		sql.append(" update ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" set replenishmentCount = '").append(ctrl.getReplenishmentCount()).append("'");
		sql.append(" where id = ").append(ctrl.getId());
		openHelper.execSQL(sql.toString());
	}
	/**
	 * 更新成为缺货列表
	 * @param ctrl
	 */
	public void updateProductCtrlOutCount(CarSalesProductCtrl ctrl){
		StringBuffer sql = new StringBuffer();
		sql.append(" update ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" set outCount = '").append(ctrl.getOutCount()).append("'");
		sql.append(" where id = ").append(ctrl.getId());
		openHelper.execSQL(sql.toString());
	}
	/**
	 * 更新成为库存盘点列表
	 * @param ctrl
	 */
	public void updateProductCtrlInvertyCount(CarSalesProductCtrl ctrl){
		StringBuffer sql = new StringBuffer();
		sql.append(" update ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" set inverty = '").append(ctrl.getInverty()).append("'");
		sql.append(" where id = ").append(ctrl.getId());
		openHelper.execSQL(sql.toString());
	}
	
	
	public List<CarSalesProductCtrl> childList(List<CarSalesProductCtrl> cList,String cId){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" where pId = '").append(cId).append("'");
		Cursor cursor = openHelper.query(sql.toString(), null);
		CarSalesProductCtrl cCtrl = null;
		if (cursor!=null) {
			while (cursor.moveToNext()) {
				cCtrl = putCarSalesProductCtrl(cursor);
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

	public List<CarSalesProductCtrl> parentList(List<CarSalesProductCtrl> pList,String pid){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" where cId = '").append(pid).append("'");
		Cursor cursor = openHelper.query(sql.toString(), null);
		CarSalesProductCtrl pCtrl = null;
		if (cursor!=null) {
			if (cursor.moveToNext()) {
				pCtrl = putCarSalesProductCtrl(cursor);
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
	/**
	 * 统计退货列表数量
	 * @return
	 */
	public int count(){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		int count = 0;
		StringBuffer sql = new StringBuffer("select count(*) from ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" where returnCount is not null and returnCount <> '").append("").append("' and productId != 0");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor!=null) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}
		cursor.close();
		return count;
	}
	/**
	 * 统计装车列表数量
	 * @return
	 */
	public int loadingCount(){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		int count = 0;
		StringBuffer sql = new StringBuffer("select count(*) from ").append(openHelper.CAR_SALES_PRODUCT_CTRL).append(" where loadingCount is not null and loadingCount <> '").append("").append("' and productId != 0");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor!=null) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}
		cursor.close();
		return count;
	}
	
	
	public void delete() {
		openHelper.delete(openHelper.CAR_SALES_PRODUCT_CTRL, null, null);
	}

	private CarSalesProductCtrl putCarSalesProductCtrl(Cursor cursor) {
		int i = 0;
		CarSalesProductCtrl ctrl = new CarSalesProductCtrl();
		ctrl.setId(cursor.isNull(i) ? null : String.valueOf(cursor.getInt(i)));i++;
		ctrl.setcId(cursor.getString(i++));
		ctrl.setpId(cursor.getString(i++));
		ctrl.setLabel(cursor.getString(i++));
		ctrl.setLevelLable(cursor.getString(i++));
		ctrl.setProductId(cursor.getInt(i++));
		ctrl.setUnitId(cursor.getInt(i++));
		ctrl.setCount(cursor.getInt(i++));
		String returnCount = cursor.getString(i++);
		ctrl.setReturnCount(!TextUtils.isEmpty(returnCount)?returnCount:"");
		String loadingCount = cursor.getString(i++);
		ctrl.setLoadingCount(!TextUtils.isEmpty(loadingCount)&&!loadingCount.equals("0")&&!loadingCount.equals("0.0")?loadingCount:"");
		String unLoadingCount = cursor.getString(i++);
		ctrl.setUnLoadingCount(!TextUtils.isEmpty(unLoadingCount)&&!unLoadingCount.equals("0")&&!unLoadingCount.equals("0.0")?unLoadingCount:"");
		String replenishmentCount = cursor.getString(i++);
		ctrl.setReplenishmentCount(!TextUtils.isEmpty(replenishmentCount)?replenishmentCount:"");
		String outCount = cursor.getString(i++);
		ctrl.setOutCount(!TextUtils.isEmpty(outCount)?outCount:"");
		String inverty = cursor.getString(i++);
		ctrl.setInverty(!TextUtils.isEmpty(inverty)?inverty:"");
//		if (!distControl(ctrl)) {
//			return null;
//		}
		if (ctrl.getProductId() !=0 && ctrl.getUnitId()!=0) {
//			ctrl.setProduct(new CarSalesUtil(mContext).product(ctrl.getProductId(), ctrl.getUnitId()));
			ctrl.setProductLevel(true);
		}else{
			ctrl.setProductLevel(false);
		}
		
		ctrl.setDataId(cursor.getInt(i++));
		return ctrl;
	}
	
//	/**
//	 * 分销控制
//	 * @param ctrl
//	 * @return true表示能卖，false表示不能卖
//	 */
//	private boolean distControl(CarSalesProductCtrl ctrl){
//		boolean flag = false;
//		int is_dist_sales  = SharedPreferencesForOrder3Util.getInstance(mContext).getIsdistSales();//是否需要分销管理 1不要 2要 默认2
//		if (is_dist_sales == 2) {//需要分销管理的情况查看是否在销售区域内
//			String storeOrgCode = SharedPreferencesForOrder3Util.getInstance(mContext).getOrder3OrgId();
//			List<Order3Dis> disList = new Order3DisDB(mContext).findOrder3DisByProductId(ctrl.getProductId());
//			if (!disList.isEmpty()) {
//				for (int j = 0; j < disList.size(); j++) {
//					Order3Dis dis = disList.get(j);
//					String orgCode = dis.getOrgCode();
//					if (storeOrgCode.contains(orgCode)) {//选中的店面不包含分销机构code的话说明不在此区域销售
//						flag = true;
//						break;
//					}
//				}
//			}else{
//				flag = true;
//			}
//		}else{
//			flag = true;
//		}
//		return flag;
//	}

	private ContentValues putContentValues(CarSalesProductCtrl productCtrl) {
		ContentValues cv = new ContentValues();
		cv.put("cId", productCtrl.getcId());
		cv.put("pId", productCtrl.getpId());
		cv.put("label", productCtrl.getLabel());
		cv.put("levelLable", productCtrl.getLevelLable());
		cv.put("productId", productCtrl.getProductId());
		cv.put("unitId", productCtrl.getUnitId());
		cv.put("count", productCtrl.getCount());
		cv.put("returnCount", productCtrl.getReturnCount());
		cv.put("loadingCount", productCtrl.getLoadingCount());
		cv.put("unLoadingCount", productCtrl.getUnLoadingCount());
		cv.put("replenishmentCount", productCtrl.getReplenishmentCount());
		cv.put("outCount", productCtrl.getOutCount());
		cv.put("inverty", productCtrl.getInverty());
		cv.put("dataId", productCtrl.getDataId());
		return cv;
	}

}
