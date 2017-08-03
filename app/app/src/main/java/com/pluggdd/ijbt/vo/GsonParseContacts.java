package com.pluggdd.ijbt.vo;

import java.util.ArrayList;

public class GsonParseContacts {
	private ArrayList<Contacts> contacts;
	private String msg;
	private String status;

	public ArrayList<Contacts> getContacts() {
		return this.contacts;
	}

	public String getMsg() {
		return this.msg;
	}

	public String getStatus() {
		return this.status;
	}

	public void setContacts(ArrayList<Contacts> paramArrayList) {
		this.contacts = paramArrayList;
	}

	public void setMsg(String paramString) {
		this.msg = paramString;
	}

	public void setStatus(String paramString) {
		this.status = paramString;
	}

	public class Contacts {
		private String allow_follow;
		private String follow_status;
		private String full_name;
		private String user_name;
		private String user_thumbimage;
		private String userid;

		public Contacts() {
		}

		public String getAllow_follow() {
			return this.allow_follow;
		}

		public String getFollow_status() {
			return this.follow_status;
		}

		public String getFull_name() {
			return this.full_name;
		}

		public String getUser_name() {
			return this.user_name;
		}

		public String getUser_thumbimage() {
			return this.user_thumbimage;
		}

		public String getUserid() {
			return this.userid;
		}

		public void setAllow_follow(String paramString) {
			this.allow_follow = paramString;
		}

		public void setFollow_status(String paramString) {
			this.follow_status = paramString;
		}

		public void setFull_name(String paramString) {
			this.full_name = paramString;
		}

		public void setUser_name(String paramString) {
			this.user_name = paramString;
		}

		public void setUser_thumbimage(String paramString) {
			this.user_thumbimage = paramString;
		}

		public void setUserid(String paramString) {
			this.userid = paramString;
		}
	}
}