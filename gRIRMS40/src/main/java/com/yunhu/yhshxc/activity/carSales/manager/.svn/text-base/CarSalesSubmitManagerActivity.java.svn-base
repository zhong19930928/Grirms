package com.yunhu.yhshxc.activity.carSales.manager;

import java.util.List;

import com.yunhu.yhshxc.submitManager.SubmitManagerActivity;
import com.yunhu.yhshxc.submitManager.bo.TablePending;

/**
 * 未上报车销数据
 * @author jishen
 *
 */
public class CarSalesSubmitManagerActivity extends SubmitManagerActivity{
	
	@Override
	public List<TablePending> tablePending() {
		List<TablePending> list = tablePendingDB.findAllCarSalesTablePending();
		return list;
	}
}
