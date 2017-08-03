package com.pluggdd.ijbt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.gson.Gson;
import com.pluggdd.ijbt.camerafilter.ConvertToUtils;
import com.pluggdd.ijbt.camerafilter.ThemeRadioButton;
import com.pluggdd.ijbt.cropping.CropImage;
import com.pluggdd.ijbt.interfaces.OnPhotoCaptured;
import com.pluggdd.ijbt.util.GlobalConfig;
import com.pluggdd.ijbt.util.PhotoCapture;
import com.pluggdd.ijbt.util.PreferenceConnector;
import com.pluggdd.ijbt.util.SimpleCameraView;
import com.yixia.camera.FFMpegUtils;
import com.yixia.camera.MediaRecorder.OnErrorListener;
import com.yixia.camera.MediaRecorder.OnPreparedListener;

import com.yixia.camera.MediaRecorderFilter;
import com.yixia.camera.VCamera;
import com.yixia.camera.model.MediaObject;
import com.yixia.camera.util.DeviceUtils;
import com.yixia.camera.util.FileUtils;
import com.yixia.camera.util.StringUtils;
import com.yixia.camera.view.CameraNdkView;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ShareActivityy extends Activity
        implements OnClickListener, OnCheckedChangeListener, SensorEventListener, OnErrorListener, OnPreparedListener, AnimationListener {

    private ImageButton btn_flash;
    private ImageView mPhotoVideoSwitchImg, mCaptureImg, mGalleryImg;
    private LinearLayout ll_cancel;
    private static final String TAG = "ShareActivity";
    private Camera camera;

    boolean isFlashIsPresent = false;
    boolean isFlashOn = false;
    private boolean isswitch_photo_video = true;
    public static boolean isPhotoCapture = false;
    public static boolean isSkipPressed = false;

    public static File filePhoto = null;
    public static Bitmap bitmapPhoto = null;
    public static int height = 0;
    public static int width = 0;
    private Activity _activityShare;
    public static String str_prev_activity;
    private SensorManager mSensorManager;

    private Chronometer focus;
    private LinearLayout ll_switch_cam;
    public static String planStatus;
    SimpleCameraView cameraView;

    public boolean isphoto = true;
    private boolean isFirstTime = true;

    // Camera Filter variable
    FrameLayout ll_surface_camera;
    private final static int[] FILTER_ICONS = new int[]{R.drawable.filter_original, R.drawable.filter_black_white,
            R.drawable.filter_sharpen, R.drawable.filter_old_film, R.drawable.filter_edge, R.drawable.filter_anti_color,
            R.drawable.filter_radial, R.drawable.filter_8bit, R.drawable.filter_lomo};
    private final static String[] FILTER_VALUES = new String[]{MediaRecorderFilter.CAMERA_FILTER_NO,
            MediaRecorderFilter.CAMERA_FILTER_BLACKWHITE, MediaRecorderFilter.CAMERA_FILTER_SHARPEN,
            MediaRecorderFilter.CAMERA_FILTER_OLD_PHOTOS, MediaRecorderFilter.CAMERA_FILTER_NEON_LIGHT,
            MediaRecorderFilter.CAMERA_FILTER_ANTICOLOR, MediaRecorderFilter.CAMERA_FILTER_PASS_THROUGH,
            MediaRecorderFilter.CAMERA_FILTER_MOSAICS, MediaRecorderFilter.CAMERA_FILTER_REMINISCENCE};

    private CameraNdkView cameraNDK;
    private VCamera v;
    private MediaRecorderFilter mMediaRecorder;
    private int mWindowWidth;
    private ImageView record_start;
    private boolean recording_status = false;
    private boolean filter_status = false;

    private MediaObject mMediaObject;
    private View mRecordFilterLayout;
    private TextView textViewfilter;
    public ProgressDialog pd;
    private Button takephoto;
    public static int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    // SimpleCameraView mCameraView;
    private RadioGroup mRecordFilterContainer;
    private static final long CONFIGURE_DELAY = 500;
    private Handler configurationHandler = new Handler();
    private Runnable reconfigureRunnable = new CustomConfigureRunnable();

    private boolean istorch = true;
    private boolean mStartEncoding = false;
    private ProgressBar progressBartranscod;
    private OnPhotoCaptured onPhotoCaptured;

    private PhotoCapture photoCapture;

    Animation aFillp;
    private Animation animation1;
    private Animation animation2;
    private View view;
    public final static int RECORD_TIME_MAX = 46 * 1000;
    private static final int HANDLE_STOP_RECORD = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//

        mWindowWidth = DeviceUtils.getScreenWidth(this);
        _activityShare = this;
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        // aFillp = AnimationUtils.loadAnimation(_activityShare,
        // R.anim.flip_view);
        //

    }

    void setBodyUI() {
        setContentView(R.layout.copy_share_activity);
        animation1 = AnimationUtils.loadAnimation(this, R.anim.to_middle);
        animation1.setAnimationListener(this);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.from_middle);
        animation2.setAnimationListener(this);
        if (isFirstTime) {
            isFirstTime = false;
        } else {
            view = findViewById(R.id.ll_root);
            view.clearAnimation();
            view.setAnimation(animation1);
            view.startAnimation(animation1);
        }

        mPhotoVideoSwitchImg = (ImageView) findViewById(R.id.img_switch_photo_video);
        // switch_photo_video.setChecked(!isphoto);

        mPhotoVideoSwitchImg.setOnClickListener(this);

        progressBartranscod = (ProgressBar) findViewById(R.id.progressBartranscod);
        cameraNDK = (CameraNdkView) findViewById(R.id.record_screen);
        ll_surface_camera = (FrameLayout) findViewById(R.id.ll_surface_camera);

        focus = (Chronometer) findViewById(R.id.chronometer1);

        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel_header);
        ll_cancel.setOnClickListener(this);

        ll_switch_cam = (LinearLayout) findViewById(R.id.ll_switch_cam);
        ll_switch_cam.setOnClickListener(this);


        mCaptureImg = (ImageView) findViewById(R.id.img_capture);
        mGalleryImg = (ImageView) findViewById(R.id.img_gallery);
        btn_flash = (ImageButton) findViewById(R.id.btn_flash);

        mCaptureImg.setOnClickListener(this);
        mGalleryImg.setOnClickListener(this);
        btn_flash.setOnClickListener(this);


        mRecordFilterContainer = (RadioGroup) findViewById(R.id.record_filter_container);
        mRecordFilterLayout = findViewById(R.id.record_filter_layout);


        if (Camera.getNumberOfCameras() == 1) {
            ll_switch_cam.setVisibility(View.GONE);
        } else {

            ll_switch_cam.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (isphoto) {

                        camera.release();

                        if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                            cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                        } else {
                            cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                        }

                        camera = cameraView.getCamera(cameraId);

                        onResume();
                    } else {

                        try {
                            cameraView = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // cameraView = null;
                        mMediaRecorder.switchCamera();
                        mMediaRecorder.prepare();
                        if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                            cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                        } else {
                            cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                        }
                    }

                }
            });

        }

        checkFlash();

        if (isphoto) {
            focus.setVisibility(View.GONE);
        } else {
            focus.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        setBodyUI();

        if (isphoto) {

            isPhotoCapture = true;

            focus.setVisibility(View.GONE);
            cameraNDK.setVisibility(View.GONE);

            mRecordFilterLayout.setVisibility(View.GONE);
            filter_status = false;

            mPhotoVideoSwitchImg.setImageResource(R.drawable.ic_videocam);

            //btn_capture.setImageResource(R.drawable.cam_capture);
            // btn_gallery_img.setImageResource(R.drawable.glry_icon);

            try {
                // camera.release();
                mMediaRecorder = null;
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            Log.d("OnResume", "isPhoto true");

            cameraView = new SimpleCameraView(this, cameraId, ll_surface_camera);// ,istorch);
            try {
                configureCamera();
            } catch (Exception e) {
                e.printStackTrace();
            }
            camera = cameraView.getCamera(cameraId);
            photoCapture = new PhotoCapture(_activityShare, cameraView, camera, mCaptureImg);

            findViewById(R.id.ll_surface_camera).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        photoCapture.autoFoucs(new AutoFocusCallback() {

                            @Override
                            public void onAutoFocus(boolean success, Camera camera) {

                            }
                        });
                    }
                }
            });

        } else {

            isPhotoCapture = false;

            focus.setVisibility(View.VISIBLE);
            cameraNDK.setVisibility(View.VISIBLE);

            mPhotoVideoSwitchImg.setImageResource(R.drawable.ic_camera);

            // btn_gallery_img.setImageResource(R.drawable.revv_share_video_btn);

            try {
                // camera.release();
                cameraView.stopCamera();

                cameraView = null;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Log.d("OnResume", "is photo false camra NDK");

            if (mMediaRecorder == null)
                initMediaRecorder();
            else {
                mMediaRecorder.setSurfaceHolder(cameraNDK.getHolder());
                mMediaRecorder.prepare();
            }
        }
    }

    private void configureCamera() {

        configurationHandler.postDelayed(reconfigureRunnable, CONFIGURE_DELAY);

    }

    private class CustomConfigureRunnable implements Runnable {
        @Override
        public void run() {
            try {
                boolean isConfigured = cameraView.configureCamera(getResources().getConfiguration());
                if (!isConfigured) {
                    configurationHandler.postDelayed(this, CONFIGURE_DELAY);
                    // cameraView.stopCamera();
                } else {
                    configurationHandler.removeCallbacks(this);
                    cameraView.startCamera();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void checkFlash() {

        if (_activityShare.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            isFlashIsPresent = true;
            btn_flash.setVisibility(View.VISIBLE);
        } else {
            btn_flash.setVisibility(View.GONE);
            isFlashIsPresent = false;
        }

    }

    protected void changeCameraFace() {

        ll_surface_camera.removeAllViews();

        if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;

        } else {
            cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }

        cameraView = new SimpleCameraView(this, cameraId, ll_surface_camera);// ,istorch);

        try {
            configureCamera();
        } catch (Exception e) {
            e.printStackTrace();
        }

        camera = cameraView.getCamera(cameraId);

        new PhotoCapture(_activityShare, cameraView, camera, mCaptureImg);

    }

    @Override
    protected void onPause() {

        isphoto = true;
        // TODO Auto-generated method stub
        super.onPause();

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        isphoto = true;

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        try {
            isphoto = true;
            cameraView.stopCamera();
            cameraView = null;
            finish();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_cancel_header:

                finish();

                break;

            case R.id.img_gallery:

                if (isphoto) {
                    intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                    // if (isPhotoCapture) {
                    intent.setType("image/*");
                    startActivityForResult(intent, 1234);
                    // }
                } else {

                    if (filter_status) {
                        mRecordFilterLayout.setVisibility(View.GONE);
                        filter_status = false;
                    } else {
                        loadFilter();
                        mRecordFilterLayout.setVisibility(View.VISIBLE);
                        filter_status = true;
                    }

                }

                break;

            case R.id.ll_switch_cam:

                if (isphoto) {

                    changeCameraFace();

                } else {

                    try {
                        cameraView = null;
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                    cameraView = null;
                    mMediaRecorder.switchCamera();
                    mMediaRecorder.prepare();

                }

                break;

            case R.id.img_capture:

                if (isphoto) {

                    // new PhotoCapture(_activityShare, cameraView, camera,
                    // btn_capture);

                    // camera.takePicture(shutterCallback, rawCallback,
                    // jpegCallback);

                } else {
                    if (!recording_status) {
                        // start
                        recording_status = true;
                        // Toast.makeText(getBaseContext(), "start", 1000).show();
                        startRecord();
                    } else {
                        stopRecord();
                        // encoding
                        startEncoding();
                    }
                }

                break;

            case R.id.btn_flash:
                // Toast.makeText(getBaseContext(), "flash click", 1000).show();
                try {
                    if (isphoto) {
                        if (!isFlashOn) {

                            Parameters p = camera.getParameters();
                            p.setFlashMode(Parameters.FLASH_MODE_TORCH);

                            camera.setParameters(p);
                            camera.startPreview();
                            isFlashOn = true;
                            btn_flash.setBackgroundResource(R.drawable.revv_share_flash);

                        } else {
                            Parameters p = camera.getParameters();
                            p.setFlashMode(Parameters.FLASH_MODE_OFF);
                            camera.setParameters(p);
                            // camera.stopPreview();
                            isFlashOn = false;
                            btn_flash.setBackgroundResource(R.drawable.revv_share_flash_off);
                        }

                    } else {
                        if (!isFlashOn) {

                            // CameraNdkView.
                            Parameters p = camera.getParameters();
                            p.setFlashMode(Parameters.FLASH_MODE_TORCH);

                            camera.setParameters(p);
                            camera.startPreview();
                            isFlashOn = true;
                            btn_flash.setBackgroundResource(R.drawable.revv_share_flash);

                        } else {
                            Parameters p = camera.getParameters();
                            p.setFlashMode(Parameters.FLASH_MODE_OFF);
                            camera.setParameters(p);
                            // camera.stopPreview();
                            isFlashOn = false;
                            btn_flash.setBackgroundResource(R.drawable.revv_share_flash_off);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;


            case R.id.img_switch_photo_video:
                Toast.makeText(ShareActivityy.this, "Video option is not available currently", Toast.LENGTH_LONG).show();
                /*if (isphoto) {
                    // switch_photo_video.setChecked(isphoto);
                    // photo_video_ischecked = false;
                    isphoto = false;
                    onResume();
                } else {
                    // switch_photo_video.setChecked(isphoto);
                    // photo_video_ischecked = true;
                    isphoto = true;
                    onResume();
                }*/

                break;
            default:
                break;

        }

    }

    private void stopRecord() {

        if (mMediaRecorder != null) {
            mMediaRecorder.stopRecord();

        }
        if (mHandler != null)
            mHandler.removeMessages(HANDLE_STOP_RECORD);
        focus.stop();

    }

    private void startRecord() {
        // mPressedStatus = true;

        if (mMediaRecorder != null) {
            mMediaRecorder.startRecord();
            if (mHandler != null) {
                mHandler.sendEmptyMessageDelayed(HANDLE_STOP_RECORD, RECORD_TIME_MAX - mMediaObject.getDuration());
            }
            focus.setBase(SystemClock.elapsedRealtime());
            focus.start();
        }

    }

    private void startEncoding() {

        Log.d("MAHI", "startEn-cod");

        if (FileUtils.showFileAvailable() < 200) {
            Toast.makeText(this, "Check Disk Space!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mMediaRecorder != null && mMediaObject != null && !mStartEncoding) {

            mStartEncoding = true;

            new AsyncTask<Void, Void, Boolean>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressBartranscod.setVisibility(View.VISIBLE);
                    progressBartranscod.setKeepScreenOn(true);
                    // progressBartranscod.set
                    Log.d("MAHI", "progress dialog start");

                }

                @Override
                protected Boolean doInBackground(Void... params) {

                    mMediaRecorder.release();
                    boolean result = FFMpegUtils.videoTranscoding(mMediaObject, mMediaObject.getOutputTempVideoPath(),
                            mWindowWidth, false);
                    if (result && mMediaRecorder != null) {
                        // mReleased = true;
                    }
                    return result;
                }

                @Override
                protected void onCancelled() {
                    super.onCancelled();
                    // mStartEncoding = false;
                    progressBartranscod.setVisibility(View.GONE);
                    // focus.setBase(SystemClock.elapsedRealtime());
                    onResume();

                }

                @Override
                protected void onPostExecute(Boolean result) {
                    super.onPostExecute(result);

                    Log.d("MAHI", "OnPOstEx");

                    if (result) {
                        if (saveMediaObject(mMediaObject)) {
                            Log.d("save", "ho gya");
                            progressBartranscod.setVisibility(View.GONE);
                            // Toast.makeText(getBaseContext(), "Save Ho gya
                            // Pagal", Toast.LENGTH_SHORT).show();
                            // Intent intent = new
                            // Intent(MediaRecorderActivity.this,
                            // MediaPreviewActivity.class);
                            // intent.putExtra("obj",
                            // mMediaObject.getObjectFilePath());
                            // startActivity(intent);
                            focus.setBase(SystemClock.elapsedRealtime());
                            Intent lol = new Intent(_activityShare, VideoPreView.class);
                            Log.d("File Pathshare", mMediaObject.getObjectFilePath());

                            String path = mMediaObject.getObjectFilePath().substring(0,
                                    mMediaObject.getObjectFilePath().length() - 4);

                            //path = path.endsWith(".obj")?path.replace(".obj",".mp4");
                            path = path + "." + "mp4";

                            lol.putExtra("obj", mMediaObject.getObjectFilePath());
                            startActivity(lol);
                            finish();

                        } else {
                            progressBartranscod.setVisibility(View.GONE);
                            // Toast.makeText(getBaseContext(), "Save ni hua",
                            // Toast.LENGTH_SHORT).show();
                            // Toast.makeText(MediaRecorderActivity.this,
                            // R.string.record_camera_save_faild,
                            // Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        progressBartranscod.setVisibility(View.GONE);
                        // Toast.makeText(getBaseContext(), "result ni aaya",
                        // Toast.LENGTH_SHORT).show();
                        // Toast.makeText(MediaRecorderActivity.this,
                        // R.string.record_video_transcoding_faild,
                        // Toast.LENGTH_SHORT).show();

                    }
                    mStartEncoding = false;
                }
            }.execute();

        }

    }

    public static boolean saveMediaObject(MediaObject mMediaObject) {
        if (mMediaObject != null) {
            try {
                if (StringUtils.isNotEmpty(mMediaObject.getObjectFilePath())) {
                    FileOutputStream out = new FileOutputStream(mMediaObject.getObjectFilePath());
                    Gson gson = new Gson();
                    out.write(gson.toJson(mMediaObject).getBytes());
                    out.flush();
                    out.close();
                    return true;
                }
            } catch (Exception e) {

            }
        }
        return false;
    }

    private void loadFilter() {
        if (!isFinishing() && mRecordFilterContainer.getChildCount() == 0) {
            final String[] filterNames = getResources().getStringArray(R.array.record_filter);
            int leftMargin = ConvertToUtils.dipToPX(this, 10);
            LayoutInflater mInflater = LayoutInflater.from(this);
            for (int i = 0; i < FILTER_ICONS.length; i++) {
                ThemeRadioButton filterView = (ThemeRadioButton) mInflater.inflate(R.layout.view_radio_item, null);
                filterView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int index = ConvertToUtils.toInt(v.getTag().toString());
                        if (mMediaRecorder != null)
                            mMediaRecorder.setCameraFilter(FILTER_VALUES[index]);
                    }
                });
                filterView.setCompoundDrawablesWithIntrinsicBounds(0, FILTER_ICONS[i], 0, 0);
                filterView.setText(filterNames[i]);
                filterView.setTag(i);
                RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = leftMargin;
                mRecordFilterContainer.addView(filterView, lp);
            }

            mRecordFilterContainer.getChildAt(0).performClick();
        }
    }

    private Intent intent;

    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;

            // Write to SD Card
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Camera/PetSutra/");
                dir.mkdirs();

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);

                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();

                refreshGallery(outFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            finish();
        }

    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    /**
     * method called for getting image from gallery
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1234:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    filePhoto = new File(filePath);
                    PreferenceConnector.writeString(ShareActivityy.this,
                            PreferenceConnector.lAST_CAPTURE_IMAGE_PATH,
                            filePhoto.getAbsolutePath());
                    // Bitmap yourSelectedImage =
                    // BitmapFactory.decodeFile(filePath);
                    if (filePhoto.exists()) {
                        isPhotoCapture = true;
                        // Intent intent = new Intent(_activityShare,
                        // CameraPreviewActivity.class);
                        // startActivity(intent);

                        intent = new Intent(ShareActivityy.this, CropImage.class);
                        intent.putExtra("image-path",
                                ShareActivityy.filePhoto.toString());

                        intent.putExtra("scale", true);
                        intent.putExtra("circleCrop", false);
                        intent.putExtra("return-data", false);
                        ShareActivityy.this.startActivityForResult(intent, 3);
                    } else {
                        GlobalConfig.showToast(_activityShare, "File not Found.");
                    }
                }
                // if (resultCode == RESULT_OK) {
                // Uri selectedImage = data.getData();
                // String[] filePathColumn = { MediaStore.Images.Media.DATA };
                //
                // Cursor cursor = getContentResolver().query(selectedImage,
                //// filePathCeFile(filePath);
                // if (filePhoto.exists()) {
                // isPhotoCapture = trolumn, null, null, null);
                // cursor.moveToFirst();
                //
                // int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                // String filePath = cursor.getString(columnIndex);
                // cursor.close();
                //
                // filePhoto = new File(filePath);
                // PreferenceConnector.writeString(ShareActivityy.this,
                // PreferenceConnector.lAST_CAPTURE_IMAGE_PATH,
                // filePhoto.getAbsolutePath());
                // // Bitmap yourSelectedImage =
                // // BitmapFactory.decodue;
                // // Intent intent = new Intent(_activityShare,
                // // CameraPreviewActivity.class);
                // // startActivity(intent);
                //
                // intent = new Intent(ShareActivityy.this, CropImage.class);
                // intent.putExtra("image-path",
                // ShareActivityy.filePhoto.toString());
                //
                // intent.putExtra("scale", true);
                // intent.putExtra("circleCrop", false);
                // intent.putExtra("return-data", false);
                // ShareActivityy.this.startActivityForResult(intent, 3);
                // } else {
                // GlobalConfig.showToast(_activityShare, "File not Found.");
                // }
                // }
                break;

            case 3:

                if (data != null) {
                    String path = data.getStringExtra("imgPath");
                    bitmapPhoto = BitmapFactory.decodeFile(path);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmapPhoto.compress(Bitmap.CompressFormat.JPEG, 100, out);

                    Intent i = new Intent(ShareActivityy.this, PhotoEditGPUActivity.class);
                    startActivity(i);

                    /*Intent intent = new Intent(ShareActivityy.this,
                            SharePhotoActivity.class);

                    System.out.println("file" + path);

                    intent.putExtra("imagepath", path);
                    startActivity(intent);*/


                    // startActivity(intent.setClass(ShareActivityy.this,
                    // PhotoEditGPUActivity.class));
                    ShareActivityy.this.finish();
                    overridePendingTransition(0, 0);
                }

                break;

            case 12345:

                if (data != null) {
                    Uri selectedVieo = data.getData();
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedVieo, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    try {
                        if (filePath != null) {

                            MediaPlayer mp = MediaPlayer.create(this, Uri.parse(filePath));
                            int duration = mp.getDuration();
                            mp.release();
                            // if ((duration / 1000) > 300) {
                            // GlobalConfig.showToast(_activityShare,
                            // "Please Select less 5 minutes video file");
                            // }
                            // else
                            {
                                Intent intent = new Intent(_activityShare, SharePhotoActivity.class);
                                intent.putExtra("imagepath", filePath);
                                _activityShare.startActivity(intent);
                                if (ShareActivityy.str_prev_activity == null) {
                                    _activityShare.finish();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
        }

    }

    ;

    // Custom camera Filter Methods
    private void initMediaRecorder() {
        mMediaRecorder = new MediaRecorderFilter();
        mMediaRecorder.setOnErrorListener(this);
        mMediaRecorder.setOnPreparedListener(this);
        // mMediaRecorder.setVideoBitRate(NetworkUtils.isWifiAvailable(this) ?
        // MediaRecorder.VIDEO_BITRATE_MEDIUM :
        // MediaRecorder.VIDEO_BITRATE_NORMAL);
        // mMediaRecorder.setSurfaceHolder(mSurfaceView.getHolder());
        mMediaRecorder.setSurfaceView(cameraNDK);

        String key = String.valueOf(System.currentTimeMillis());
        mMediaObject = mMediaRecorder.setOutputDirectory(key, VCamera.getVideoCachePath() + key);

        if (mMediaObject != null) {
            mMediaRecorder.prepare();
            mMediaRecorder.setCameraFilter(MediaRecorderFilter.CAMERA_FILTER_NO);
            // mProgressView.setData(mMediaObject);
        } else {
            Toast.makeText(this, "record_camera_init_faild", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    @Override
    public void onAudioError(int arg0, String arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onVideoError(int arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPrepared() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    // orientation
    public static final int UPSIDE_DOWN = 3;
    public static final int LANDSCAPE_RIGHT = 4;
    public static final int PORTRAIT = 1;
    public static final int LANDSCAPE_LEFT = 2;
    public int mOrientationDeg; // last rotation in degrees
    public int mOrientationRounded; // last orientation int from above
    private static final int _DATA_X = 0;
    private static final int _DATA_Y = 1;
    private static final int _DATA_Z = 2;
    private int ORIENTATION_UNKNOWN = -1;

    @Override
    public void onSensorChanged(SensorEvent event) {
        int tempOrientRounded = mOrientationRounded;

        float[] values = event.values;
        int orientation = ORIENTATION_UNKNOWN;
        float X = -values[_DATA_X];
        float Y = -values[_DATA_Y];
        float Z = -values[_DATA_Z];
        float magnitude = X * X + Y * Y;
        // Don't trust the angle if the magnitude is small compared to the y
        // value
        if (magnitude * 4 >= Z * Z) {
            float OneEightyOverPi = 57.29577957855f;
            float angle = (float) Math.atan2(-Y, X) * OneEightyOverPi;
            orientation = 90 - (int) Math.round(angle);
            // normalize to 0 - 359 range
            while (orientation >= 360) {
                orientation -= 360;
            }
            while (orientation < 0) {
                orientation += 360;
            }
        }
        // ^^ thanks to google for that code
        // now we must figure out which orientation based on the degrees

        if (orientation != mOrientationDeg) {
            mOrientationDeg = orientation;
            // figure out actual orientation
            if (orientation == -1) {// basically flat

            } else if (orientation <= 45 || orientation > 315) {// round to 0
                tempOrientRounded = 1;// portrait
            } else if (orientation > 45 && orientation <= 135) {// round to 90
                tempOrientRounded = 2; // lsleft
            } else if (orientation > 135 && orientation <= 225) {// round to 180
                tempOrientRounded = 3; // upside down
            } else if (orientation > 225 && orientation <= 315) {// round to 270
                tempOrientRounded = 4;// lsright
            }

        }

        if (mOrientationRounded != tempOrientRounded) {
            mOrientationRounded = tempOrientRounded;

        }

    }

    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == animation1) {
            view.clearAnimation();
            view.setAnimation(animation2);
            view.startAnimation(animation2);

        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLE_STOP_RECORD:
                    stopRecord();
                    startEncoding();
                    break;
            }
        }
    };

}
