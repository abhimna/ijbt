package com.pluggdd.ijbt.util;

import org.json.JSONObject;

import java.io.Serializable;

public class UploadVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 112L;
	private String status;
	private String result;
	private String clip_id;
	private String count;
	private String clip_name;
	private String clip_url;
	private String message;
	private String thumb_url;
	
	public UploadVo(JSONObject objSon){
		this.setStatus(objSon.optString("status"));
		this.setResult(objSon.optString("result"));
//		this.setClip_id(objSon.optString("clip_id"));
//		this.setCount(objSon.optString("msg"));
//		this.setClip_url(objSon.optString("url"));
//		this.setClip_name(objSon.optString("clip_name"));
//		this.setThumb_url(objSon.optString("thumb_url"));
		this.setMessage(objSon.optString("message"));
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getClip_id() {
		return clip_id;
	}

	public void setClip_id(String clip_id) {
		this.clip_id = clip_id;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getClip_name() {
		return clip_name;
	}

	public void setClip_name(String clip_name) {
		this.clip_name = clip_name;
	}

	public String getClip_url() {
		return clip_url;
	}

	public void setClip_url(String clip_url) {
		this.clip_url = clip_url;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getThumb_url() {
		return thumb_url;
	}

	public void setThumb_url(String thumb_url) {
		this.thumb_url = thumb_url;
	}
	
	
}