package com.pluggdd.ijbt.interfaces;

import com.facebook.GraphResponse;

public interface FBPostListener {

	public void onPostComplete(String postType);
	public void onPostError(GraphResponse graphResponse);
	
}
