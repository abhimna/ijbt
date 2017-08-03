package com.pluggdd.ijbt.vo;

import java.util.ArrayList;

public class GsonParseSingleImage {

	private String status;
	private Posts posts;

	public class Posts {

		private String post_id;
		private String user_id;
		private String title;
		private String image;
		private String thumb_image;
		private String data_type;
		private String data_url;
		private String suspend;
		private String dtdate;
		private String post_total_like;
		private String post_total_comment;
		private String is_post_liked;
		private String user_image;
		private String user_name;

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

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getThumb_image() {
			return thumb_image;
		}

		public void setThumb_image(String thumb_image) {
			this.thumb_image = thumb_image;
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

		public String getSuspend() {
			return suspend;
		}

		public void setSuspend(String suspend) {
			this.suspend = suspend;
		}

		public String getDtdate() {
			return dtdate;
		}

		public void setDtdate(String dtdate) {
			this.dtdate = dtdate;
		}

		public String getPost_total_like() {
			return post_total_like;
		}

		public void setPost_total_like(String post_total_like) {
			this.post_total_like = post_total_like;
		}

		public String getPost_total_comment() {
			return post_total_comment;
		}

		public void setPost_total_comment(String post_total_comment) {
			this.post_total_comment = post_total_comment;
		}

		public String getIs_post_liked() {
			return is_post_liked;
		}

		public void setIs_post_liked(String is_post_liked) {
			this.is_post_liked = is_post_liked;
		}

		public String getUser_image() {
			return user_image;
		}

		public void setUser_image(String user_image) {
			this.user_image = user_image;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getFull_name() {
			return full_name;
		}

		public void setFull_name(String full_name) {
			this.full_name = full_name;
		}

		public String getTime_ago() {
			return time_ago;
		}

		public void setTime_ago(String time_ago) {
			this.time_ago = time_ago;
		}

		public String getBlock_relation() {
			return block_relation;
		}

		public void setBlock_relation(String block_relation) {
			this.block_relation = block_relation;
		}

		public ArrayList<NewsFeedCommentData> getLast_three_comments() {
			return last_three_comments;
		}

		public void setLast_three_comments(
				ArrayList<NewsFeedCommentData> last_three_comments) {
			this.last_three_comments = last_three_comments;
		}

		private String full_name;
		private String time_ago;
		private String block_relation;
		private ArrayList<NewsFeedCommentData> last_three_comments;

	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Posts getPosts() {
		return posts;
	}

	public void setPosts(Posts posts) {
		this.posts = posts;
	}
}
