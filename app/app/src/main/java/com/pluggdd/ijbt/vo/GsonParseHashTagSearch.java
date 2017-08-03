package com.pluggdd.ijbt.vo;

import java.util.ArrayList;

public class GsonParseHashTagSearch {

	private String msg;
	private String status;
	private ArrayList<posts> posts;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<posts> getPosts() {
		return posts;
	}

	public void setPosts(ArrayList<posts> posts) {
		this.posts = posts;
	}

	public class posts {

		private String tag;
		private String total_posts;

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		public String getTotal_posts() {
			return total_posts;
		}

		public void setTotal_posts(String total_posts) {
			this.total_posts = total_posts;
		}
	}
}
