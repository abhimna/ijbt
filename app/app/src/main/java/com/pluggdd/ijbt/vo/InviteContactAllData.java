package com.pluggdd.ijbt.vo;

public class InviteContactAllData {
	private String contact_email;
	String contact_names;
	String id;
	int is_invite;

	public String getContact_email() {
		return this.contact_email;
	}

	public String getContact_names() {
		return this.contact_names;
	}

	public String getId() {
		return this.id;
	}

	public int getIs_invite() {
		return this.is_invite;
	}

	public void setContact_email(String paramString) {
		this.contact_email = paramString;
	}

	public void setContact_names(String paramString) {
		this.contact_names = paramString;
	}

	public void setId(String paramString) {
		this.id = paramString;
	}

	public void setIs_invite(int paramInt) {
		this.is_invite = paramInt;
	}
}
