package com.pluggdd.ijbt.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.pluggdd.ijbt.ShareActivityy;
import com.pluggdd.ijbt.cropping.CropImage;

@SuppressWarnings("deprecation")
public class PhotoCapture {
	SimpleCameraView cameraView;

	SurfaceView surfaceView;
	private Camera camera;
	String fileName;
	static Activity _activity;
	File pictureFile;
	private ImageView capture_btn;
	byte[] data1;

	// static Bitmap bitmapFromCamera;
	static Uri URIOfCameraImage;

	public PhotoCapture(Activity _activity, SurfaceView surfaceView, Camera camera, ImageView capture_btn) {

		PhotoCapture._activity = _activity;
		this.surfaceView = surfaceView;
		this.camera = camera;
		this.capture_btn = capture_btn;

		manageView();
	}

	private void manageView() {

		capture_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					/*
					 * camera.autoFocus(new AutoFocusCallback() {
					 *
					 * @Override public void onAutoFocus(boolean success, Camera
					 * camera) { if (success)
					 */

					// camera = cameraView.getCamera();
					// camera.startPreview();
					// camera.getParameters();
					// camera.takePicture(null, null, mPicture);
					camera.setPreviewCallback(null);
					camera.takePicture(null, null, mPicture);

					//
					// camera.stopPreview();
					// camera.release();

					/*
					 * } });
					 */
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void resetCam() {
		// cameraView.startCamera();
		moveToNextSceen();
	}

	private void moveToNextSceen() {
		// System.gc();
		Intent intent = new Intent(_activity, CropImage.class);
		intent.putExtra("image-path", ShareActivityy.filePhoto.toString());

		intent.putExtra("scale", true);
		intent.putExtra("circleCrop", false);
		intent.putExtra("return-data", false);
		_activity.startActivityForResult(intent, 3);

	}

	PictureCallback mPicture = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			System.out.println("PictureCallback");
			pictureFile = getOutputMediaFile();
			data1 = data;
			if (pictureFile == null) {
				return;
			}
			new ProcessImage().execute();
		}
	};

	@SuppressLint("SimpleDateFormat")
	private static File getOutputMediaFile() {
		// File mediaStorageDir = new File(
		// Environment
		// .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
		// "revvolution");
		// if (!mediaStorageDir.exists()) {
		// if (!mediaStorageDir.mkdirs()) {
		// Log.d("MyCameraApp", "failed to create directory");
		// return null;
		// }
		// }
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File outputDir = _activity.getCacheDir(); // context being the
		// // Activity pointer
		File outputFile = null;
		try {
			outputFile = File.createTempFile(timeStamp, ".jpeg", outputDir);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (!outputFile.exists()) {
			outputFile.mkdirs();
		}

		System.out.println("All fine");
		// Create a media file name

		File mediaFile;
		String filePath = outputDir.getPath() + File.separator + "IMG_" + timeStamp + ".JPG";
		mediaFile = new File(filePath);
		URIOfCameraImage = Uri.fromFile(mediaFile);
		ShareActivityy.filePhoto = mediaFile;// capture

		System.out.println("mediaFile :" + mediaFile);

		return mediaFile;
	}

	Bitmap bitmap;

	class ProcessImage extends AsyncTask<Void, Void, Void> {
		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			mProgressDialog = ProgressDialog.show(_activity, null, "Processing image...", true);
			mProgressDialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(data1, 0, data1.length);
			switch (((ShareActivityy) _activity).mOrientationRounded) {
				case 1:
					bitmap = RotateBitmap(bitmap, 0);
					break;
				case 2:
					bitmap = RotateBitmap(bitmap, 90);
					break;
				case 3:
					bitmap = RotateBitmap(bitmap, 180);
					break;
				case 4:
					bitmap = RotateBitmap(bitmap, -90);
					break;

				default:
					break;
			}
			ShareActivityy.bitmapPhoto = bitmap;
			ShareActivityy.height = bitmap.getHeight();
			ShareActivityy.width = bitmap.getWidth();
			pictureFile = getOutputMediaFile();
			PreferenceConnector.writeString(_activity, PreferenceConnector.lAST_CAPTURE_IMAGE_PATH,
					pictureFile.getAbsolutePath());
			try {
				FileOutputStream out = new FileOutputStream(pictureFile);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
				out.flush();
				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			resetCam();
		}

	}

	public static Bitmap RotateBitmap(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		// matrix.preRotate(90);

		if (source.getWidth() > source.getHeight()) {
			angle = angle + 90;
		}
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}

	public void autoFoucs(AutoFocusCallback autoFocusCallback) {
		// camera.autoFocus(autoFocusCallback);
	}

}