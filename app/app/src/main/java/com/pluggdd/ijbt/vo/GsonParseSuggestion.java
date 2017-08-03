package com.pluggdd.ijbt.vo;

import java.util.ArrayList;

public class GsonParseSuggestion {
	private String msg;
	private String status;
	private ArrayList<Suggestions> suggestions;
	private String total_data;
	private String total_page;
	private String total_records;

	public String getTotal_data() {
		return total_data;
	}

	public void setTotal_data(String total_data) {
		this.total_data = total_data;
	}

	public String getTotal_page() {
		return total_page;
	}

	public void setTotal_page(String total_page) {
		this.total_page = total_page;
	}

	public String getTotal_records() {
		return total_records;
	}

	public void setTotal_records(String total_records) {
		this.total_records = total_records;
	}

	public String getMsg() {
		return this.msg;
	}

	public String getStatus() {
		return this.status;
	}

	public ArrayList<Suggestions> getSuggestions() {
		return this.suggestions;
	}

	public void setMsg(String paramString) {
		this.msg = paramString;
	}

	public void setStatus(String paramString) {
		this.status = paramString;
	}

	public void setSuggestions(ArrayList<Suggestions> paramArrayList) {
		this.suggestions = paramArrayList;
	}

	public class Suggestions {
		private String allow_follow;
		private String follow_status;
		private String full_name;
		private String user_name;
		private String user_thumbimage;
		private String userid;

		public Suggestions() {
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
