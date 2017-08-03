package com.pluggdd.ijbt.vo;

import java.util.ArrayList;

public class GsonParsehashTag {

	private String msg;
	private String status;
	private String total_data;

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

	public String getTotal_data() {
		return total_data;
	}

	public void setTotal_data(String total_data) {
		this.total_data = total_data;
	}

	public ArrayList<Hash_posts> getHash_posts() {
		return hash_posts;
	}

	public void setHash_posts(ArrayList<Hash_posts> hash_posts) {
		this.hash_posts = hash_posts;
	}

	private ArrayList<Hash_posts> hash_posts;

	public class Hash_posts {

		private String post_id;
		private String user_id;

		public String getPost_id() {
			return post_id;
		}

		public void setPost_id(String post_id) {
			this.post_id = post_id;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String getData_type() {
			return data_type;
		}

		public void setData_type(String data_type) {
			this.data_type = data_type;
		}

		public String getData_url() {
			return data_url;
		}

		public void setData_url(String data_url) {
			this.data_url = data_url;
		}

		public String getThumb_image() {
			return thumb_image;
		}

		public void setThumb_image(String thumb_image) {
			this.thumb_image = thumb_image;
		}

		private String data_type;
		private String data_url;
		private String thumb_image;
	}

}
