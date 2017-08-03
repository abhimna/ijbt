package com.pluggdd.ijbt.vo;

import java.util.ArrayList;

public class GsonParseFollower {

	private String status;
	private String msg;
	private ArrayList<FollowersListData> followers_list;
	private String total_page;
	private String total_records;

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

	public ArrayList<FollowersListData> getFollowers_list() {
		return followers_list;
	}

	public void setFollowers_list(ArrayList<FollowersListData> followers_list) {
		this.followers_list = followers_list;
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

}
