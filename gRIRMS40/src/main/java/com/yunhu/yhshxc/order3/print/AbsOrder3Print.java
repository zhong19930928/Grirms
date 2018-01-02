package com.yunhu.yhshxc.order3.print;

import java.util.List;

import org.json.JSONArray;

import android.content.Context;

import com.yunhu.yhshxc.print.BluetoothPrinter;
import com.yunhu.yhshxc.print.PrintHelper;
import com.yunhu.yhshxc.print.templet.Element;
import com.yunhu.yhshxc.print.templet.Templet.DataSource;

public class AbsOrder3Print {
	public Context context;
	protected static PrintHelper printHelper;

	public AbsOrder3Print(Context context) {
		this.context = context;
		printHelper = new PrintHelper(context, PrintHelper.DEVICE_BM9000);
		printHelper.initDialog(context);
	}
	
	public void print(Context dialogContext, JSONArray templetSource, DataSource dataSource) throws Exception {
		switch (printHelper.status()) {
			case BluetoothPrinter.STATUS_CONNECTED:
				printHelper.setTemplet(PrintHelper.TEMPLET_JSON, templetSource, dataSource);
				printHelper.printOrder3();
				break;
				
			case BluetoothPrinter.STATUS_DISCONNECTED:
				printHelper.initDialog(dialogContext);
				printHelper.connect();
				break;
		}
	}
	

	public void printLoop(List<Element> columns) {}
	
	public void printRow(List<Element> columns) {}
	
	public void printPromotion(List<Element> columns) {}
	public void printZhekou(List<Element> columns) {}
	public void printDingdanbianhao(List<Element> columns){};
	
	public static void printElement(Element element) {
		printHelper.printElement(element);
	}
	
	public static void printNewLine() {
		printHelper.printNewLine();
	}
	
	public static void printDivider() {
		printHelper.printDivider();
	}
	
	public  void release(){
		if (printHelper != null) {
			printHelper.release();
			printHelper = null;
		}
	}
	
}
