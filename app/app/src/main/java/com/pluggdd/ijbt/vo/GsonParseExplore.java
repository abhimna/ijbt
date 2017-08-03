package com.pluggdd.ijbt.vo;

import java.util.ArrayList;

public class GsonParseExplore {

	private String total_data;
	private String total_page;
	private ArrayList<ExploreData> Popular_posts;

	private App_status app_status;

	public String getTotal_data() {
		return total_data;
	}

	public void setTotal_data(String total_data) {
		this.total_data = total_data;
	}

	public String getTotal_page() {
		return total_page;
	}

	public void setTotal_page(String total_page) {
		this.total_page = total_page;
	}

	public ArrayList<ExploreData> getPopular_posts() {
		return Popular_posts;
	}

	public void setPopular_posts(ArrayList<ExploreData> popular_posts) {
		Popular_posts = popular_posts;
	}

	public String getTotal_records() {
		return total_records;
	}

	public void setTotal_records(String total_records) {
		this.total_records = total_records;
	}

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

	public App_status getApp_status() {
		return app_status;
	}

	public void setApp_status(App_status app_status) {
		this.app_status = app_status;
	}

	private String total_records;
	private String status;
	private String msg;

	public class App_status {

		private String status;
		private String msg;
		private String user_status;

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

		public String getUser_status() {
			return user_status;
		}

		public void setUser_status(String user_status) {
			this.user_status = user_status;
		}

		public String getAudio_status() {
			return audio_status;
		}

		public void setAudio_status(String audio_status) {
			this.audio_status = audio_status;
		}

		public String getVideo_status() {
			return video_status;
		}

		public void setVideo_status(String video_status) {
			this.video_status = video_status;
		}

		public String getAudio_plans() {
			return audio_plans;
		}

		public void setAudio_plans(String audio_plans) {
			this.audio_plans = audio_plans;
		}

		public String getVideo_plans() {
			return video_plans;
		}

		public void setVideo_plans(String video_plans) {
			this.video_plans = video_plans;
		}

		private String audio_status;
		private String video_status;
		private String audio_plans;
		private String video_plans;

	}
}
