package com.pluggdd.ijbt.vo;

import java.util.ArrayList;

public class GsonParseUserConnection {

	private String status;
	private String msg;
	private ArrayList<User_list> user_list;

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

	public ArrayList<User_list> getUser_list() {
		return user_list;
	}

	public void setUser_list(ArrayList<User_list> user_list) {
		this.user_list = user_list;
	}

	public class User_list {
		private String allow_follow;
		private String userid;
		private String user_name;
		private String user_thumbimage;

		public String getAllow_follow() {
			return allow_follow;
		}

		public void setAllow_follow(String allow_follow) {
			this.allow_follow = allow_follow;
		}

		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getUser_thumbimage() {
			return user_thumbimage;
		}

		public void setUser_thumbimage(String user_thumbimage) {
			this.user_thumbimage = user_thumbimage;
		}

	}

}
