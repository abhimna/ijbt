package com.pluggdd.ijbt.vo;

import java.util.ArrayList;

public class GsonParseLookagramPlan {

	private String status;
	private String msg;
	private plans plans;
	private active_plan active_plan;

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

	

	public active_plan getActive_plan() {
		return active_plan;
	}

	public void setActive_plan(active_plan active_plan) {
		this.active_plan = active_plan;
	}

	public plans getPlans() {
		return plans;
	}

	public void setPlans(plans plans) {
		this.plans = plans;
	}

	public class plans {

		private ArrayList<Common> audio;
		private ArrayList<Common> video;

		public ArrayList<Common> getAudio() {
			return audio;
		}

		public void setAudio(ArrayList<Common> audio) {
			this.audio = audio;
		}

		public ArrayList<Common> getVideo() {
			return video;
		}

		public void setVideo(ArrayList<Common> video) {
			this.video = video;
		}

	}

	public class active_plan {
		private ArrayList<Common> audio;
		private ArrayList<Common> video;

		public ArrayList<Common> getAudio() {
			return audio;
		}

		public void setAudio(ArrayList<Common> audio) {
			this.audio = audio;
		}

		public ArrayList<Common> getVideo() {
			return video;
		}

		public void setVideo(ArrayList<Common> video) {
			this.video = video;
		}
	}

	public class Common {
		private String id;
		private String type;
		private String plan_name;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getPlan_name() {
			return plan_name;
		}

		public void setPlan_name(String plan_name) {
			this.plan_name = plan_name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getDuration() {
			return duration;
		}

		public void setDuration(String duration) {
			this.duration = duration;
		}

		public String getIdentifier() {
			return identifier;
		}

		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getDtdate() {
			return dtdate;
		}

		public void setDtdate(String dtdate) {
			this.dtdate = dtdate;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		private String description;
		private String duration;
		private String identifier;
		private String price;
		private String dtdate;
		private String status;
	}

}
