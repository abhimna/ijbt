package com.pluggdd.ijbt.vo;

import java.io.Serializable;


public class NewsFeedCommentData implements Serializable{

	private String id;
	private String user_id;
	private String post_id;
	private String comment;
	private String audio;
	private String dtdate;
	private String full_name;
	private String user_name;
	private String user_thumbimage;
	private String time_ago;
	private String type;
	private String post_userid;
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDtdate() {
		return dtdate;
	}

	public void setDtdate(String dtdate) {
		this.dtdate = dtdate;
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

	public String getUser_thumbimage() {
		return user_thumbimage;
	}

	public void setUser_thumbimage(String user_thumbimage) {
		this.user_thumbimage = user_thumbimage;
	}

	public String getTime_ago() {
		return time_ago;
	}

	public void setTime_ago(String time_ago) {
		this.time_ago = time_ago;
	}

	public String getAudio() {
		return audio;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

	public String getPost_userid() {
		return post_userid;
	}

	public void setPost_userid(String post_userid) {
		this.post_userid = post_userid;
	}

}
