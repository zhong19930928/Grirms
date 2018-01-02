package com.yunhu.yhshxc.print.templet;

import java.util.List;

public class Element {
	public static final int TYPE_TEXT = 0;
	public static final int TYPE_NEW_LINE = 1;
	public static final int TYPE_DIVIDER = 2;
	public static final int TYPE_BARCODE = 3;

	protected int type;
	protected boolean loop;
	protected int pType;
	protected int x;
	protected int size = 2;
	protected boolean bold = false;
	protected int variable = -1;
	protected String content;
	protected List<Element> children;
	
	protected Element() {
	}
	
	
	public int getpType() {
		return pType;
	}


	public void setpType(int pType) {
		this.pType = pType;
	}


	protected Element(int type, int x) {
		this.type = type;
		this.x = x;
	}
	
	public int getType() {
		return type;
	}
	
	public boolean isLoop() {
		return loop;
	}
	
	public int getX() {
		return x;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public List<Element> getChildren() {
		return children;
	}

	public void setChildren(List<Element> children) {
		this.children = children;
	}

	public int getVariable() {
		return variable;
	}

	public void setVariable(int variable) {
		this.variable = variable;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
