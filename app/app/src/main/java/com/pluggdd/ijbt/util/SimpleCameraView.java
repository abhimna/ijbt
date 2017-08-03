package com.pluggdd.ijbt.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

/**
 * This class is used for creating custom camera and setting camera on this
 * surfaceview with capturing mechanism of save image captured from front camera
 * and back camera
 * 
 * @author
 * 
 */
public class SimpleCameraView extends SurfaceView implements
SurfaceHolder.Callback {

	private static final int DEGREES_0 = 0;
	private static final int DEGREES_90 = 90;
	private static final int DEGREES_180 = 180;
	private static final int DEGREES_270 = 270;
	Handler	handler		= new Handler();
	Runnable	runnable;

	public int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
	private SurfaceHolder surfaceHolder;
	private Camera camera;
	private Camera.PreviewCallback previewCallback;
	private Context context;
	int deviceHeight;
	List<Camera.Size> mSupportedPreviewSizes;
	Camera.Size mPreviewSize ;
	FrameLayout ll_surface_camera;
	@SuppressWarnings("deprecation")
	public SimpleCameraView(Context context, int cameraId, FrameLayout ll_surface_camera) {
		super(context);
		this.context = context;
		this.cameraId = cameraId;
		this.ll_surface_camera = ll_surface_camera;

		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		ll_surface_camera.addView(this);
		setKeepScreenOn(true);

		configureCamera(getResources().getConfiguration());
	}


	@SuppressWarnings("deprecation")
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera.setPreviewDisplay(holder);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		stopCamera();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		try {
			Camera.Parameters parameters = camera.getParameters();

			List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
			Camera.Size selected = sizes.get(0);
			for(int i=0;i<sizes.size();i++)
			{
				if(sizes.get(i).width > selected.width)
					selected = sizes.get(i);
			}
			parameters.setPictureSize(selected.width, selected.height);
			Camera.Size		previewSize		=	determinePictureSize(selected);
			parameters.setPreviewSize(previewSize.width, previewSize.height);
			// parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
			//  parameters.setPreviewSize(displayWidth, deviceHeight);
			/* try {
					parameters.setPreviewSize(selected.width, selected.height);
					camera.setParameters(parameters);
				} catch (Exception e) 
				{
					try {
						parameters.setPreviewSize(displayWidth,(int)aspect*displayWidth);
						camera.setParameters(parameters);

					} catch (Exception e2) {
						// TODO: handle exception
					}
				}*/
			if (cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) 
			{
				
				parameters.setRotation(90);
				
				if(selected.height==selected.width)
				{

					//Log.v("Device Name", "name  "+getDeviceName());
					
					RelativeLayout.LayoutParams layoutParams =(RelativeLayout.LayoutParams)ll_surface_camera.getLayoutParams();
					layoutParams.topMargin		=	dpToPx(50);
				}

			} else {

				parameters.setRotation(90);
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
			}
			float aspect = (float) selected.height / selected.width;
			try {
				camera.setParameters(parameters);
			} catch (Exception e) {
				e.printStackTrace();
				//Toast.makeText(context, "error11", Toast.LENGTH_SHORT).show();
				/*Camera.Parameters parameters1 = camera.getParameters();
				parameters1.setRotation(90);
				 camera.setParameters(parameters1);*/
				// TODO: handle exception
			}
			/* parameters.setPreviewSize(displayWidth,(int)aspect*displayWidth);
			  try {
				  camera.setParameters(parameters);
			} catch (Exception e) {
				Toast.makeText(context, "error22", Toast.LENGTH_SHORT).show();
				// TODO: handle exception
			}*/


			//camera.setParameters(parameters);
			camera.startPreview();
			/**
			 * Set auto focus listener on camera
			 */
			runnable	=	new Runnable() {

				@Override
				public void run() 
				{
					if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
						camera.autoFocus(new AutoFocusCallback() {

							@Override
							public void onAutoFocus(boolean success, Camera camera) 
							{
								Toast.makeText(getContext(), "foucs  "+success, Toast.LENGTH_SHORT).show();
							}
						});
					}
					handler.postDelayed(runnable, 10000);
				}
			};
			//	handler.postDelayed(runnable, 10000);

			startCamera();
		} catch (Exception e) {
			e.printStackTrace();
			//Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
		}

	}

	public Camera getCamera() {
		try {
			camera = Camera.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return camera;
	}

	@SuppressLint("NewApi")
	public Camera getCamera(int cameraId) {
		try {
			camera = Camera.open(cameraId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return camera;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public boolean configureCamera(Configuration configuration) {
		try {
			/*try {
				getCamera(cameraId);
			} catch (Exception e) {
				// TODO: handle exception
			}*/


			if (camera != null) {
				Display display = ((WindowManager) context
						.getSystemService(Context.WINDOW_SERVICE))
						.getDefaultDisplay();
				int width;
				int height;

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
					width = display.getWidth()-5;
					height = display.getHeight();
				} else {
					Point size = new Point();
					display.getSize(size);
					width = size.x;
					height = size.y;
				}
				// width = displayWidth;
				// height = displayWidth;

				int displayOrientationDegrees = getDisplayOrientationDegrees(display);
				camera.setDisplayOrientation(displayOrientationDegrees);

				Camera.Size previewSize = camera.getParameters()
						.getPictureSize();

				// previewSize.width = displayWidth;
				// previewSize.height = displayWidth;

				float aspect = (float) previewSize.height / previewSize.width;

				FrameLayout.LayoutParams cameraHolderParams = (FrameLayout.LayoutParams)getLayoutParams();
				/*if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
					cameraHolderParams.height = height;
					cameraHolderParams.width = (int) (height / aspect);
					// cameraHolderParams.height = height;
					// cameraHolderParams.width = height;
				} else {*/
				cameraHolderParams.width = width;
				cameraHolderParams.height = (int) (width / aspect);
				// cameraHolderParams.width = width;
				// cameraHolderParams.height = width;
				//}
				//setPadding(0, 50, 0, 0);
				setLayoutParams(cameraHolderParams);

				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	private int getDisplayOrientationDegrees(Display display) {
		int displayOrientationDegrees;
		int orientation = getResources().getConfiguration().orientation;

		switch (display.getRotation()) {
		case Surface.ROTATION_0:
			if (orientation == Configuration.ORIENTATION_PORTRAIT)
				displayOrientationDegrees = DEGREES_90;
			else
				displayOrientationDegrees = DEGREES_0;
			break;
		case Surface.ROTATION_90:
			if (orientation == Configuration.ORIENTATION_LANDSCAPE)
				displayOrientationDegrees = DEGREES_0;
			else
				displayOrientationDegrees = DEGREES_270;
			break;
		case Surface.ROTATION_180:
			if (orientation == Configuration.ORIENTATION_PORTRAIT)
				displayOrientationDegrees = DEGREES_270;
			else
				displayOrientationDegrees = DEGREES_180;
			break;
		case Surface.ROTATION_270:
			if (orientation == Configuration.ORIENTATION_LANDSCAPE)
				displayOrientationDegrees = DEGREES_180;
			else
				displayOrientationDegrees = DEGREES_90;
			break;
		default:
			displayOrientationDegrees = DEGREES_0;
		}

		return displayOrientationDegrees;
	}

	public void stopCamera() {
		try {
			if(camera!=null)
			{
				camera.stopPreview();
				camera.setPreviewCallback(null);
				camera.release();
				handler.removeCallbacks(runnable);
				camera = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startCamera() {
		try {
			if (surfaceHolder.getSurface() == null) {
				return;
			}

			camera.reconnect();
			camera.setPreviewDisplay(surfaceHolder);
			mSupportedPreviewSizes = camera.getParameters().getSupportedPreviewSizes();
			if (previewCallback != null)
				camera.setPreviewCallback(previewCallback);
			camera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
		Camera.Size result = null;
		for (Camera.Size size: parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;
					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}
		return (result);
	}
	protected Camera.Size determinePictureSize(Camera.Size previewSize) 
	{
		Camera.Size retSize = null;
		List<Camera.Size>  mPictureSizeList		=	camera.getParameters().getSupportedPreviewSizes();
		Log.v("total size  image", "width  "+previewSize.width+"  height  "+previewSize.height);
		for (int i = 0; i < mPictureSizeList.size(); i++) 
		{
			Log.v("total size  ", "width  "+mPictureSizeList.get(i).width+"  height  "+mPictureSizeList.get(i).height);
		}
		for (Camera.Size size : mPictureSizeList) {
			if (size.equals(previewSize)) {
				return size;
			}
		}

		/*if (DEBUGGING) { Log.v(LOG_TAG, "Same picture size not found.");}*/ 

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
	public int dpToPx(int dp)
	{
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		displayMetrics = context.getResources().getDisplayMetrics();
		return (int)((dp * displayMetrics.density) + 0.5);
		//  return px;
	}
	private static final String capitalize(String str) 
	{
	    
	    final char[] arr = str.toCharArray();
	    boolean capitalizeNext = true;
	    String phrase = "";
	    for (final char c : arr) {
	        if (capitalizeNext && Character.isLetter(c)) {
	            phrase += Character.toUpperCase(c);
	            capitalizeNext = false;
	            continue;
	        } else if (Character.isWhitespace(c)) {
	            capitalizeNext = true;
	        }
	        phrase += c;
	    }
	    return phrase;
	}

	/** Returns the consumer friendly device name */
	public static String getDeviceName() {
	    final String manufacturer = Build.MANUFACTURER;
	    final String model = Build.MODEL;
	    if (model.startsWith(manufacturer)) {
	        return capitalize(model);
	    }
	    if (manufacturer.equalsIgnoreCase("HTC")) {
	        // make sure "HTC" is fully capitalized.
	        return "HTC " + model;
	    }
	    return capitalize(manufacturer) + " " + model;
	}
}
