package com.pluggdd.ijbt.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.ShareActivityy;
import com.pluggdd.ijbt.SharePhotoActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class VideoCapture {

	SurfaceView surfaceView;
	private Camera camera;
	Preview preview;
	String fileName;
	Activity _activity;
	File pictureFile;
	private ImageButton capture_btn;
	byte[] data1;

	static Bitmap bitmapFromCamera;
	static Uri URIOfCameraImage;

	boolean isButtonPressed = false;
	String videoPath;
	SimpleCameraView cameraView;
	Chronometer chronometer;
	// private SurfaceHolder surfaceHolder;

	public MediaRecorder mrec;

	private boolean record_start = true;

	public VideoCapture(Activity _activity, SurfaceView surfaceView,
						Camera camera, ImageButton capture_btn, Preview preview,
						SimpleCameraView cameraView, Chronometer focus) {

		this._activity = _activity;
		this.surfaceView = surfaceView;
		this.camera = camera;
		this.capture_btn = capture_btn;
		this.preview = preview;
		this.cameraView = cameraView;
		this.chronometer = focus;

		manageView();

	}

	private void manageView() {
		CamcorderProfile camcorderProfile = CamcorderProfile
				.get(CamcorderProfile.QUALITY_HIGH);

		float aspect = (float) camcorderProfile.videoFrameHeight
				/ camcorderProfile.videoFrameWidth;

		Display display = ((WindowManager) _activity
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width;
		int height;

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
			width = display.getWidth();
			height = display.getHeight();
		} else {
			Point size = new Point();
			display.getSize(size);
			width = size.x;
			height = size.y;
		}

		ViewGroup.LayoutParams cameraHolderParams = cameraView
				.getLayoutParams();
		cameraHolderParams.width = width;
		cameraHolderParams.height = (int) (width / aspect);
		cameraView.setLayoutParams(cameraHolderParams);
		Camera.Parameters parameters = camera.getParameters();
		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
		camera.setParameters(parameters);

		chronometer
				.setOnChronometerTickListener(new OnChronometerTickListener() {

					@Override
					public void onChronometerTick(Chronometer chronometer) {

						if (ShareActivityy.planStatus
								.equalsIgnoreCase("video")) {

							if ("00:15".equalsIgnoreCase(chronometer.getText()
									.toString())) {
								Toast.makeText(_activity, "Complete",
										Toast.LENGTH_SHORT).show();
								chronometer.stop();
								isButtonPressed = false;
								stopRecording();
							}

						} else if (ShareActivityy.planStatus
								.equalsIgnoreCase("video1")) {

							if ("01:00".equalsIgnoreCase(chronometer.getText()
									.toString())) {
								Toast.makeText(_activity, "Complete",
										Toast.LENGTH_SHORT).show();
								chronometer.stop();
								isButtonPressed = false;
								stopRecording();
							}
						} else if (ShareActivityy.planStatus
								.equalsIgnoreCase("video2")) {

							if ("02:00".equalsIgnoreCase(chronometer.getText()
									.toString())) {
								Toast.makeText(_activity, "Complete",
										Toast.LENGTH_SHORT).show();
								chronometer.stop();
								isButtonPressed = false;
								stopRecording();
							}
						} else if (ShareActivityy.planStatus
								.equalsIgnoreCase("video3")) {

							if ("05:00".equalsIgnoreCase(chronometer.getText()
									.toString())) {
								Toast.makeText(_activity, "Complete",
										Toast.LENGTH_SHORT).show();
								chronometer.stop();
								isButtonPressed = false;
								stopRecording();
							}
						}

					}
				});

		capture_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (record_start) {
					System.out.println("Recoarding start");
					videoPath = SharePhotoActivity.getOutputMediaFile();
					System.out.println("video path = " + videoPath);
					isButtonPressed = true;
					startRecoarding();
					capture_btn
							.setBackgroundResource(R.drawable.revv_share_video_btn);
					chronometer.setBase(SystemClock.elapsedRealtime());
					chronometer.start();
					record_start = false;

				} else {
					chronometer.stop();
					capture_btn
							.setBackgroundResource(R.drawable.revv_share_cam_btn);
					try {
						isButtonPressed = false;
						System.out.println("Recoarding stop");
						stopRecording();
					} catch (Exception e) {
						CreateExternalLogFile(e, "error2");
					}
					record_start = true;

				}

			}
		});

	}

	private void startRecoarding() {
		try {
			startRecording();
		} catch (Exception e) {
			String message = e.getMessage();
			e.printStackTrace();
			CreateExternalLogFile(e, "error1");
			Log.i(null, "Problem Start" + message);
			mrec.release();
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	protected void startRecording() throws IOException {

		System.out.println("recording is going to start start");
		mrec = new MediaRecorder(); // Works well
//		mrec.setOrientationHint(cameraRotet() + 90);
		Camera.Parameters parameters = camera.getParameters();
		CamcorderProfile camcorderProfile = CamcorderProfile
				.get(CamcorderProfile.QUALITY_HIGH);

		Camera.Size cameraSize = parameters.getPictureSize();
		SurfaceHolder surfaceHolder = cameraView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		Display display = ((WindowManager) _activity
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width;
		int height;

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
			width = display.getWidth();
			height = display.getHeight();
		} else {
			Point size = new Point();
			display.getSize(size);
			width = size.x;
			height = size.y;
		}

		float aspect = (float) camcorderProfile.videoFrameHeight
				/ camcorderProfile.videoFrameWidth;

		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
		camera.setParameters(parameters);

		ViewGroup.LayoutParams cameraHolderParams = cameraView
				.getLayoutParams();
		cameraHolderParams.width = width;
		cameraHolderParams.height = (int) (width / aspect);
		cameraView.setLayoutParams(cameraHolderParams);
		camera.unlock();
		mrec.setCamera(camera);
		// mrec.setPreviewDisplay(surfaceHolder.getSurface());
		// ((AudioManager) _activity.getSystemService(Context.AUDIO_SERVICE))
		// .setStreamMute(AudioManager.STREAM_SYSTEM, true);
		mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mrec.setAudioSource(MediaRecorder.AudioSource.MIC);
		mrec.setProfile(camcorderProfile);
		mrec.setVideoEncodingBitRate(3000000);
		mrec.setPreviewDisplay(surfaceHolder.getSurface());
		mrec.setOutputFile(videoPath);
		mrec.prepare();
		mrec.start();
		PreferenceConnector.writeString(_activity,
				PreferenceConnector.lAST_CAPTURE_IMAGE_PATH, videoPath);
		System.out.println("recording start");
	}

	protected void stopRecording() {
		try {

			mrec.stop();
			mrec.release();
			mrec = null;
			Intent intent = new Intent(_activity, SharePhotoActivity.class);
			intent.putExtra("imagepath", videoPath);
			_activity.startActivity(intent);
			if (ShareActivityy.str_prev_activity == null) {
				_activity.finish();
			}

		} catch (Exception e) {
			e.printStackTrace();
			CreateExternalLogFile(e, "error3");
		}
	}

//	public int cameraRotet() {
//		switch (((ShareActivity) _activity).mOrientationRounded) {
//		case 1:
//			return 0;
//		case 2:
//			return 90;
//		case 3:
//			return 180;
//		case 4:
//			return -90;
//
//		default:
//			return 0;
//		}
//	}

	private void CreateExternalLogFile(Exception ex, String fileName) {
		File sdCard = Environment.getExternalStorageDirectory();
		File dir = new File(sdCard.getAbsolutePath());
		dir.mkdirs();
		File file = new File(dir, fileName + ".txt");

		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(file));
			ex.printStackTrace(pw);
			pw.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.i("write",
					"******* File not found. Did you"
							+ " add a WRITE_EXTERNAL_STORAGE permission to the manifest?");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected Camera.Size determinePictureSize(Camera.Size previewSize) {
		Camera.Size retSize = null;
		List<Camera.Size> mPictureSizeList = camera.getParameters()
				.getSupportedPreviewSizes();
		Log.v("total size  image", "width  " + previewSize.width + "  height  "
				+ previewSize.height);
		for (int i = 0; i < mPictureSizeList.size(); i++) {
			Log.v("total size  ", "width  " + mPictureSizeList.get(i).width
					+ "  height  " + mPictureSizeList.get(i).height);
		}
		for (Camera.Size size : mPictureSizeList) {
			if (size.equals(previewSize)) {
				return size;
			}
		}

		/* if (DEBUGGING) { Log.v(LOG_TAG, "Same picture size not found.");} */

		// if the preview size is not supported as a picture size
		float reqRatio = ((float) previewSize.width) / previewSize.height;
		float curRatio, deltaRatio;
		float deltaRatioMin = Float.MAX_VALUE;

		for (Camera.Size size : mPictureSizeList) {
			curRatio = ((float) size.width) / size.height;
			deltaRatio = Math.abs(reqRatio - curRatio);
			if (deltaRatio < deltaRatioMin) {
				deltaRatioMin = deltaRatio;
				retSize = size;
			}
		}

		return retSize;
	}
}
