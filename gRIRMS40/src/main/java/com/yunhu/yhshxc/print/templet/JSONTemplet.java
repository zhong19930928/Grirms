package com.yunhu.yhshxc.print.templet;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yunhu.yhshxc.print.BluetoothPrinter;

public class JSONTemplet extends Templet<JSONArray> {

	public JSONTemplet(BluetoothPrinter printer, JSONArray source) throws Exception {
		super(printer, source);
	}

	@Override
	protected void build(JSONArray source) throws JSONException {
		for (int i = 0; i < source.length(); i++) {
			JSONObject group = source.getJSONObject(i);
			
			JSONArray data = group.getJSONArray("data");
			
			Element parent = new Element();
			int fLoop = group.getInt("loop");
			parent.loop = fLoop == 1|| fLoop==2;
			parent.setpType(fLoop);
			elements.add(parent);
			
			ArrayList<Element> children = new ArrayList<Element>(data.length());
			parent.children = children;
			for (int j = 0; j < data.length(); j++) {
				JSONObject item = data.getJSONObject(j);
				
				int x = getAbsolutelyX(item.getDouble("rate"));
				
				String name = item.getString("name");

				if (name.matches("^\\d+$")) {
					if ("0".equals(name)) {//分隔符
						addDivider(children);
					}
					else if (name.equals("10")) {//换行符
						addNewLine(children);
					}
					else {
						Element element = new Element(Element.TYPE_TEXT, x);
						element.bold = item.getInt("bold") == 1;
						element.size = item.getInt("size") >= 1 ? 1 : 0;
						element.variable = Integer.parseInt(name);
						children.add(element);
					}
				}
				else {//固定文字
					Element element = new Element(Element.TYPE_TEXT, x);
					element.bold = item.getInt("bold") == 1;
					element.size = item.getInt("size") >= 1 ? 1 : 0;
					element.content = item.getString("name");
					children.add(element);
				}
			}
		}
	}
	
	protected int getAbsolutelyX(double relativeX) {
		return (int)(relativeX * deviceWidth / 100);
	}
}
