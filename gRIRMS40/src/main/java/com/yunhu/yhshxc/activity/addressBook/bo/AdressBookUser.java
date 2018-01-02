package com.yunhu.yhshxc.activity.addressBook.bo;

public class AdressBookUser {
	
		private int id;
		private int uId;//用户ID
		private int rId;//角色ID
		private String un;//用户名称
		private String pn;//电话号码
		private String rn;//角色名称
		private int rl;//角色级别
		private String on;//机构名称
		private String oc;//机构CODE
		private int ol;//机构层级
		private int oId;//机构CODE
		private String op;//机构名称 （穿透）
		private String mailAddr;//邮箱地址
		private String photo;//头像
		private String patch_id;
    	private int ids;
		private boolean isOrg;
		private int isVisit;//0未参与  1 参与
		private int unClickble;//0可点击 1不可点击
	    private int olevel;

	public int getOlevel() {
		return olevel;
	}

	public void setOlevel(int olevel) {
		this.olevel = olevel;
	}

	public String getMailAddr() {
		return mailAddr;
	}

	public void setMailAddr(String mailAddr) {
		this.mailAddr = mailAddr;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPatch_id() {
		return patch_id;
	}

	public void setPatch_id(String patch_id) {
		this.patch_id = patch_id;
	}

	public int getIds() {
		return ids;
	}

	public void setIds(int ids) {
		this.ids = ids;
	}

	public int getUnClickble() {
		return unClickble;
	}

	public void setUnClickble(int unClickble) {
		this.unClickble = unClickble;
	}

	public int getIsVisit() {
		return isVisit;
	}

	public void setIsVisit(int isVisit) {
		this.isVisit = isVisit;
	}

	public boolean isOrg() {
			return isOrg;
		}
		public void setOrg(boolean isOrg) {
			this.isOrg = isOrg;
		}
		public String getOp() {
			return op;
		}
		public void setOp(String op) {
			this.op = op;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getPn() {
			return pn;
		}
		public void setPn(String pn) {
			this.pn = pn;
		}
		public int getuId() {
			return uId;
		}
		public void setuId(int uId) {
			this.uId = uId;
		}
		public int getrId() {
			return rId;
		}
		public void setrId(int rId) {
			this.rId = rId;
		}
		public String getUn() {
			return un;
		}
		public void setUn(String un) {
			this.un = un;
		}
		public String getRn() {
			return rn;
		}
		public void setRn(String rn) {
			this.rn = rn;
		}
		public int getRl() {
			return rl;
		}
		public void setRl(int rl) {
			this.rl = rl;
		}
		public String getOn() {
			return on;
		}
		public void setOn(String on) {
			this.on = on;
		}
		public String getOc() {
			return oc;
		}
		public void setOc(String oc) {
			this.oc = oc;
		}
		public int getOl() {
			return ol;
		}
		public void setOl(int ol) {
			this.ol = ol;
		}
		public int getoId() {
			return oId;
		}
		public void setoId(int oId) {
			this.oId = oId;
		}
		
		
		
}
