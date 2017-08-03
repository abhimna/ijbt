package com.pluggdd.ijbt.vo;

import java.util.ArrayList;

public class GsonParseRequestUser {

	private String status;
	private String msg;

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

	public ArrayList<Request_list> getRequest_list() {
		return request_list;
	}

	public void setRequest_list(ArrayList<Request_list> request_list) {
		this.request_list = request_list;
	}

	private ArrayList<Request_list> request_list;

	public class Request_list {

		private String user_name;
		private String userid;

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

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

		public String getAllow_follow() {
			return allow_follow;
		}

		public void setAllow_follow(String allow_follow) {
			this.allow_follow = allow_follow;
		}

		private String user_thumbimage;
		private String allow_follow;

	}

}
