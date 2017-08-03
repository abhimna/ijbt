package com.pluggdd.ijbt.vo;

import java.util.ArrayList;

public class GsonParseTimeLineFollowersNotification {

	private String status;
	private String msg;
	private ArrayList<TimeLineMainData> notifications;
	private String total_current_record;
	private String total_page;
	private String notification_total_record;
	private App_status app_status;

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

	public String getTotal_current_record() {
		return total_current_record;
	}

	public void setTotal_current_record(String total_current_record) {
		this.total_current_record = total_current_record;
	}

	public String getTotal_page() {
		return total_page;
	}

	public void setTotal_page(String total_page) {
		this.total_page = total_page;
	}

	public String getNotification_total_record() {
		return notification_total_record;
	}

	public void setNotification_total_record(String notification_total_record) {
		this.notification_total_record = notification_total_record;
	}

	public ArrayList<TimeLineMainData> getNotifications() {
		return notifications;
	}

	public void setNotifications(ArrayList<TimeLineMainData> notifications) {
		this.notifications = notifications;
	}

	public App_status getApp_status() {
		return app_status;
	}

	public void setApp_status(App_status app_status) {
		this.app_status = app_status;
	}

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
