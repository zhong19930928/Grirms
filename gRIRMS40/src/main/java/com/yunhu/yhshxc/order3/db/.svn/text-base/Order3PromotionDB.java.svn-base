package com.yunhu.yhshxc.order3.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.order3.bo.Order3Promotion;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.utility.DateUtil;

public class Order3PromotionDB {

	private DatabaseHelper openHelper;
	private Context context;
	public Order3PromotionDB(Context context) {
		this.context = context;
		openHelper = DatabaseHelper.getInstance(context);
	}

	public Long insert(Order3Promotion conf) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		Long id = db.insert(openHelper.ORDER3_PROMOTION, null, putContentValues(conf));
		
		return id;

	}
	
	public List<Order3Promotion> findAllPromotionList(String orgId,int level){
		List<Order3Promotion> list = new ArrayList<Order3Promotion>();
		String now = DateUtil.getCurDate();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ORDER3_PROMOTION);
		sql.append(" where (fTime <'").append(now).append("' or fTime ='").append(now).append("') and (tTime >'").append(now).append("' or tTime='").append(now).append("')");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if(cursor!=null){
			while (cursor.moveToNext()) {
				Order3Promotion p = putOrder3Promotion(cursor);
				if (TextUtils.isEmpty(p.getOrgId()) || orgId.contains(p.getOrgId())) {
					if (level > 0) {
						if (TextUtils.isEmpty(p.getLevel()) || String.valueOf(level).equals(p.getLevel())) {
							list.add(p);
						}
					}else{
						list.add(p);
					}
				}
			}
			cursor.close();
		}
		return list;
	}
	
	/**
	 * 根据促销类型查找
	 * @param orgId
	 * @param preType 促销类型 1单品买满数量 2单品买满金额 3分类买满数量 4分类买满金额 5综合买满数量 6综合买满金额
	 * @return
	 */
	public List<Order3Promotion> findPromotionByPreType(String orgId,int preType,int disType,int level){
		List<Order3Promotion> list = new ArrayList<Order3Promotion>();
		String now = DateUtil.getCurDate();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ORDER3_PROMOTION);
		sql.append(" where preType = ").append(preType);
		sql.append(" and disType = ").append(disType);
		sql.append(" and (fTime <'").append(now).append("' or fTime ='").append(now).append("') and (tTime >'").append(now).append("' or tTime='").append(now).append("')");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if(cursor!=null){
			while (cursor.moveToNext()) {
				Order3Promotion p = putOrder3Promotion(cursor);
				if (TextUtils.isEmpty(p.getOrgId()) || orgId.contains(p.getOrgId())) {
					if (level > 0) {
						if (TextUtils.isEmpty(p.getLevel()) || String.valueOf(level).equals(p.getLevel())) {
							list.add(p);
						}
					}else{
						list.add(p);
					}
				}
			}
			cursor.close();
		}
		return list;
	}
	
	/**
	 * 查找分类类型的促销
	 * @param orgId
	 * @param preType 促销类型 1单品买满数量 2单品买满金额 3分类买满数量 4分类买满金额 5综合买满数量 6综合买满金额
	 * @return
	 */
	public Order3Promotion findClassPromotion(String orgId,int preType,int productId,int level){
		Order3Promotion p = null;
		String now = DateUtil.getCurDate();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ORDER3_PROMOTION);
		sql.append(" where preType = ").append(preType);
		sql.append(" and mId = '").append(productId).append("'");
		sql.append(" and (fTime <'").append(now).append("' or fTime ='").append(now).append("') and (tTime >'").append(now).append("' or tTime='").append(now).append("')");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if(cursor!=null){
			if (cursor.moveToNext()) {
				Order3Promotion pro = putOrder3Promotion(cursor);
				if (TextUtils.isEmpty(pro.getOrgId()) || orgId.contains(pro.getOrgId())) {
					if (level > 0) {
						if (TextUtils.isEmpty(pro.getLevel()) || String.valueOf(level).equals(pro.getLevel())) {
							p = pro;
						}
					}else{
						p = pro;
					}
				}
			}
			cursor.close();
		}
		return p;
	}
	
	
	
	public List<Order3Promotion> findPromotionByProductIdAndUnitId(int productId,int unitId,String orgId,int level){
		String now = DateUtil.getCurDate();
		List<Order3Promotion> list = new ArrayList<Order3Promotion>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ORDER3_PROMOTION).append(" where mId = ").append(productId).append(" and mUid = ").append(unitId);
		
		sql.append(" and (fTime <'").append(now).append("' or fTime ='").append(now).append("') and (tTime >'").append(now).append("' or tTime='").append(now).append("')");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			while (cursor.moveToNext()) {
				Order3Promotion p = putOrder3Promotion(cursor);
				if (TextUtils.isEmpty(p.getOrgId()) || orgId.contains(p.getOrgId())) {
					if (level > 0) {
						if (TextUtils.isEmpty(p.getLevel()) || String.valueOf(level).equals(p.getLevel())) {
							list.add(p);
						}
					}else{
						list.add(p);
					}
				}
			}
			cursor.close();
		}
		return list;
	}
	
	public boolean  isPromotion(int productId,int unitId,String orgId,int level){
		int is_promotion = SharedPreferencesForOrder3Util.getInstance(context).getIsPromotion();//1没有促销 2是有促销
		if (is_promotion==1 || TextUtils.isEmpty(orgId)) {
			return false;
		}
		String now = DateUtil.getCurDate();
		boolean isPromotion = false;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ORDER3_PROMOTION).append(" where mId = ").append(productId).append(" and mUid = ").append(unitId);
		sql.append(" and (fTime <'").append(now).append("' or fTime ='").append(now).append("') and (tTime >'").append(now).append("' or tTime='").append(now).append("')");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			while (cursor.moveToNext()) {
				Order3Promotion p = putOrder3Promotion(cursor);
				if (TextUtils.isEmpty(p.getOrgId()) || orgId.contains(p.getOrgId())) {
					if (level > 0) {
						if (TextUtils.isEmpty(p.getLevel()) || String.valueOf(level).equals(p.getLevel())) {
							isPromotion = true;
							break;
						}
					}else{
						isPromotion = true;
						break;
					}
				}
			}
			cursor.close();
		}
		return isPromotion;
	}
	
	public Order3Promotion findPromotionByPromotionId(int promotionId){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		Order3Promotion conf = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ORDER3_PROMOTION).append(" where promotionId = ").append(promotionId);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor!=null) {
			if (cursor.moveToNext()) {
				conf = putOrder3Promotion(cursor);
			}
			cursor.close();
		}
		return conf;
	}
	
	
	
	public void delete(){
		openHelper.delete(openHelper.ORDER3_PROMOTION, null, null);
	}
	
	private ContentValues putContentValues(Order3Promotion conf){
		ContentValues cv = new ContentValues();
		cv.put("promotionId", conf.getPromotionId());
		cv.put("name", conf.getName());
		cv.put("mCnt", conf.getmCnt());
		cv.put("sCnt", conf.getsCnt());
		cv.put("amount", conf.getAmount());
		cv.put("preType", conf.getPreType());
		cv.put("disType", conf.getDisType());
		cv.put("isDouble", conf.getIsDouble());
		cv.put("fTime", conf.getfTime());
		cv.put("tTime", conf.gettTime());
		cv.put("disRate", conf.getDisRate());
		cv.put("disAmount", conf.getDisAmount());
		cv.put("level", conf.getLevel());
		cv.put("mark", conf.getMark());
		cv.put("orgId", conf.getOrgId());
		cv.put("mTab", conf.getmTab());
		cv.put("mId", conf.getmId());
		cv.put("mUid", conf.getmUid());
		cv.put("sTab", conf.getsTab());
		cv.put("sId", conf.getsId());
		cv.put("sUid", conf.getsUid());
		return cv;
	}
	
	private Order3Promotion putOrder3Promotion(Cursor cursor) {
		int i = 0;
		Order3Promotion conf = new Order3Promotion();
		conf.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		conf.setPromotionId(cursor.getInt(i++));
		conf.setName(cursor.getString(i++));
		conf.setmCnt(Double.parseDouble(cursor.getString(i++)));
		conf.setsCnt(Double.parseDouble(cursor.getString(i++)));
		conf.setAmount(Double.parseDouble(cursor.getString(i++)));
		conf.setPreType(cursor.getInt(i++));
		conf.setDisType(cursor.getInt(i++));
		conf.setIsDouble(cursor.getInt(i++));
		conf.setfTime(cursor.getString(i++));
		conf.settTime(cursor.getString(i++));
		conf.setDisRate(cursor.getString(i++));
		conf.setDisAmount(Double.parseDouble(cursor.getString(i++)));
		conf.setLevel(cursor.getString(i++));
		conf.setMark(cursor.getString(i++));
		conf.setOrgId(cursor.getString(i++));
		conf.setmTab(cursor.getString(i++));
		conf.setmId(cursor.getString(i++));
		conf.setmUid(cursor.getString(i++));
		conf.setsTab(cursor.getString(i++));
		conf.setsId(cursor.getString(i++));
		conf.setsUid(cursor.getString(i++));
		return conf;
	}
	
	
}
