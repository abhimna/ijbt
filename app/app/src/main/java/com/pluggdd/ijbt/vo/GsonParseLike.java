package com.pluggdd.ijbt.vo;

import java.util.ArrayList;

public class GsonParseLike {

	private String status;
	private String msg;
	private String total_records;
	private String total_page;
	private ArrayList<Users> users;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTotal_records() {
		return total_records;
	}

	public void setTotal_records(String total_records) {
		this.total_records = total_records;
	}

	public String getTotal_page() {
		return total_page;
	}

	public void setTotal_page(String total_page) {
		this.total_page = total_page;
	}

	public ArrayList<Users> getAlUsers() {
		return users;
	}

	public void setAlUsers(ArrayList<Users> alUsers) {
		this.users = alUsers;
	}

	public class Users {
		private String userid;
		private String user_thumbimage;
		private String full_name;
		private String user_name;
		private String email;

		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

		public String getUser_thumbimage() {
			return user_thumbimage;
		}

		public void setUser_thumbimage(String user_thumbimage) {
			this.user_thumbimage = user_thumbimage;
		}

		public String getFull_name() {
			return full_name;
		}

		public void setFull_name(String full_name) {
			this.full_name = full_name;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
	}

}
