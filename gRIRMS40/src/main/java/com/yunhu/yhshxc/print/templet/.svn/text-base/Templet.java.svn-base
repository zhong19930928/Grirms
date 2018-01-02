package com.yunhu.yhshxc.print.templet;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import com.yunhu.yhshxc.print.BluetoothPrinter;

abstract public class Templet<S> {
	private static final String TAG = "Templet";
	
	protected float deviceWidth = 100;

	protected final Collection<Element> elements = new LinkedList<Element>();
	
	protected BluetoothPrinter printer;
	protected DataSource dataSource;
	
	public Templet(BluetoothPrinter printer, S source) throws Exception {
		this.printer = printer;
		deviceWidth = printer.getDeviceWidth();
		build(source);
	}
	
	abstract protected void build(S source) throws Exception;
	
	public Collection<Element> getElements() {
		return elements;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public boolean isEmpty() {
		return elements.isEmpty();
	}
	
	public void addNewLine(Collection<Element> elements) {
		elements.add(new Element(Element.TYPE_NEW_LINE, 0));
	}
	
	public void addDivider(Collection<Element> elements) {
		elements.add(new Element(Element.TYPE_DIVIDER, 0));
	}
	
	public void addBarcode(String code) {
		Element e = new Element(Element.TYPE_BARCODE, 0);
		e.content = code;
		elements.add(e);
	}
	
	public void print() {
		Log.i(TAG, "print");
		for (Element e : elements) {
			if (e.loop) {
				dataSource.printLoop(e.children);
			}else {
				dataSource.printRow(e.children);
			}
		}
		printer.printNewLine();

		try {
			TimeUnit.SECONDS.sleep(2);
		}
		catch (InterruptedException e) {
		}
	}
	
	public void printOrder3() {
		Log.i(TAG, "print");
		for (Element e : elements) {
			if (e.pType == 1) {
				dataSource.printLoop(e.children);
			}else if(e.pType == 0){
				dataSource.printRow(e.children);
			}else if(e.pType == 2){
				dataSource.printPromotion(e.children); 
			}else if(e.pType == 3){
				dataSource.printZhekou(e.children);
			}else if(e.pType == 4){
				dataSource.printDingdanbianhao(e.children);
			}
		}
		printer.printNewLine();

		try {
			TimeUnit.SECONDS.sleep(2);
		}
		catch (InterruptedException e) {
		}
	}
	
	
	public void printElement(Element element) {
		switch (element.getType()) {
			case Element.TYPE_TEXT:
				printer.printText(element.getContent(), element.getX(), element.getSize(), element.getSize(), 0, element.isBold());
				break;
				
			case Element.TYPE_NEW_LINE:
				printer.printNewLine();
				break;
				
			case Element.TYPE_DIVIDER:
				printer.printDivider();
				break;
				
			case Element.TYPE_BARCODE:
				printer.printBarCode(element.getContent(), element.getX());
				break;
		}
	}
	
	public interface DataSource {
		public void printLoop(List<Element> columns);
		
		public void printRow(List<Element> columns);
		
		public void printPromotion(List<Element> columns);

		public void printZhekou(List<Element> columns);
		
		public void printDingdanbianhao(List<Element> columns);
	}
}
