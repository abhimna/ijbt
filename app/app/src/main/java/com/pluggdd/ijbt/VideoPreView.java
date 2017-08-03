package com.pluggdd.ijbt;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pluggdd.ijbt.camerafilter.MediaPlayer;
import com.pluggdd.ijbt.camerafilter.VideoView;
import com.yixia.camera.model.MediaObject;
import com.yixia.camera.model.MediaObject.MediaPart;
import com.yixia.camera.util.DeviceUtils;
import com.yixia.camera.util.FileUtils;

import java.io.File;

public class VideoPreView extends Activity implements OnClickListener, OnPreparedListener, VideoView.OnPlayStateListener {

	private VideoView mVideoView;

	private View mRecordPlay;

	private MediaPlayer mAudioPlayer;
	private int mWindowWidth;
	private MediaObject mMediaObject;
	private File mThemeCacheDir;
	private String mCurrentTheme = null;
	private boolean mNeedResume;
	private boolean mNeedResumeThemeAudio;
	private String obj;

	private TextView back, next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		 obj = getIntent().getStringExtra("obj");
		Log.d("share", "obj" + obj);
		mMediaObject = restoneMediaObject(obj);
		if (mMediaObject == null) {
			Toast.makeText(this, "record_read_object_faild", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mWindowWidth = DeviceUtils.getScreenWidth(this);

		setContentView(R.layout.videopreview);

		mVideoView = (VideoView) findViewById(R.id.record_preview);
		mRecordPlay = findViewById(R.id.record_play);
		back = (TextView) findViewById(R.id.title_left);
		next = (TextView) findViewById(R.id.title_right);

		back.setOnClickListener(this);
		next.setOnClickListener(this);

		mVideoView.setOnClickListener(this);
		mVideoView.setOnPreparedListener(this);
		mVideoView.setOnPlayStateListener(this);

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && !isExternalStorageRemovable())
			mThemeCacheDir = new File(getExternalCacheDir(), "Theme");
		else
			mThemeCacheDir = new File(getCacheDir(), "Theme");
		mVideoView.setVideoPath(mMediaObject.getOutputTempVideoPath());
		
		
		mVideoView.start();
		if (mAudioPlayer != null)
			mAudioPlayer.start();

	}

	protected static MediaObject restoneMediaObject(String obj) {
		try {
			String str = FileUtils.readFile(new File(obj));
			Gson gson = new Gson();
			MediaObject result = gson.fromJson(str.toString(), MediaObject.class);
			result.getCurrentPart();
			preparedMediaObject(result);
			return result;
		} catch (Exception e) {
			if (e != null) {

			}
		}
		return null;
	}

	public static void preparedMediaObject(MediaObject mMediaObject) {
		if (mMediaObject != null && mMediaObject.getMedaParts() != null) {
			int duration = 0;
			for (MediaPart part : mMediaObject.getMedaParts()) {
				part.startTime = duration;
				part.endTime = part.startTime + part.duration;
				duration += part.duration;
			}
		}
	}

	@Override
	public void onStateChanged(boolean isPlaying) {
		if (isPlaying)
			mRecordPlay.setVisibility(View.GONE);
		else
			mRecordPlay.setVisibility(View.VISIBLE);
	}

	public static boolean isExternalStorageRemovable() {
		if (DeviceUtils.hasGingerbread())
			return Environment.isExternalStorageRemovable();
		else
			return Environment.MEDIA_REMOVED.equals(Environment.getExternalStorageState());
	}

	@Override
	public void onPrepared(android.media.MediaPlayer mp) {
		// TODO Auto-generated method stub
//		mVideoView.start();
//		mAudioPlayer.start();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.record_preview:

			if (mVideoView.isPlaying()) {
				mVideoView.pause();
				if (mAudioPlayer != null)
					mAudioPlayer.pause();
			} else {
				mVideoView.start();
				if (mAudioPlayer != null)
					mAudioPlayer.start();
			}

			break;

		case R.id.title_left:
			mVideoView.pause();
			VideoPreView.this.finish();
			
			break;
		case R.id.title_right:
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
				if (mAudioPlayer != null)
					mAudioPlayer.pause();
			}
			Intent send_path = new Intent(VideoPreView.this, SharePhotoActivity.class);
			
			Log.d("send_path", ""+obj);
			
			String path =  obj.substring(0, obj.length() - 4);
			
			path = path+"."+"mp4";
			
			Log.d("video+path", ""+path);
			
			send_path.putExtra("imagepath", path);
			
			startActivity(send_path);
			
			break;

		default:
			break;
		}

	}
	
	@Override
	public void onBackPressed() {
		startActivity(new Intent(getBaseContext(), ShareActivityy.class));
	}
}
