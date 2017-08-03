package com.pluggdd.ijbt.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class GsonParseCommentList implements Serializable {

	private String status;
	private String msg;
	private ArrayList<NewsFeedCommentData> comments;

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

	public ArrayList<NewsFeedCommentData> getComments() {
		return comments;
	}

	public void setComments(ArrayList<NewsFeedCommentData> comments) {
		this.comments = comments;
	}

}
