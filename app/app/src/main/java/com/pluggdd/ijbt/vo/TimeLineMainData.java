package com.pluggdd.ijbt.vo;

import java.util.ArrayList;

public class TimeLineMainData {

	private String log_id;
	private String relative_id;
	private String main_id;
	private String type;
	private String activity_user;
	private String notify_to;
	private String read_status;
	private String status;
	private String dtdate;
	private String users;
	private String on_user;
	private String message;
	private String time_ago;
	private ArrayList<TimeLineAllUsersData> all_users;
	private TimelinePostDetailData post_detail;

	public String getLog_id() {
		return log_id;
	}

	public void setLog_id(String log_id) {
		this.log_id = log_id;
	}

	public String getRelative_id() {
		return relative_id;
	}

	public void setRelative_id(String relative_id) {
		this.relative_id = relative_id;
	}

	public String getMain_id() {
		return main_id;
	}

	public void setMain_id(String main_id) {
		this.main_id = main_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getActivity_user() {
		return activity_user;
	}

	public void setActivity_user(String activity_user) {
		this.activity_user = activity_user;
	}

	public String getNotify_to() {
		return notify_to;
	}

	public void setNotify_to(String notify_to) {
		this.notify_to = notify_to;
	}

	public String getRead_status() {
		return read_status;
	}

	public void setRead_status(String read_status) {
		this.read_status = read_status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDtdate() {
		return dtdate;
	}

	public void setDtdate(String dtdate) {
		this.dtdate = dtdate;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public String getOn_user() {
		return on_user;
	}

	public void setOn_user(String on_user) {
		this.on_user = on_user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime_ago() {
		return time_ago;
	}

	public void setTime_ago(String time_ago) {
		this.time_ago = time_ago;
	}

	public ArrayList<TimeLineAllUsersData> getAll_users() {
		return all_users;
	}

	public void setAll_users(ArrayList<TimeLineAllUsersData> all_users) {
		this.all_users = all_users;
	}

	public TimelinePostDetailData getPost_detail() {
		return post_detail;
	}

	public void setPost_detail(TimelinePostDetailData post_detail) {
		this.post_detail = post_detail;
	}

}
