package com.pluggdd.ijbt.fcm;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {

	// static final String SERVER_URL =
	// "http://10.0.2.2/gcm_server_php/register.php";
	// public static final String SERVER_URL =
	// "http://demo2server.com/sites/mobile/and-push.php";
	public static String SENDER_ID = "1036142001390";

	public static final String TAG = "IJBT";
	public static final String DISPLAY_MESSAGE_ACTION = "org.ninehertzindia.lookagram.notification.DISPLAY_MESSAGE";
	public static final String EXTRA_MESSAGE = "message";

	/**
	 * Notifies UI to display a message.
	 */
	public static void displayMessage(Context context, String message,
			String name) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}
}