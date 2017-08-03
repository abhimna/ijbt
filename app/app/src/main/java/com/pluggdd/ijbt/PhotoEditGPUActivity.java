package com.pluggdd.ijbt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pluggdd.ijbt.util.CommonMethods;
import com.pluggdd.ijbt.util.FilterHelper;
import com.pluggdd.ijbt.util.SelctedLineareLayout;

import org.insta.InstaFilter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.GPUImageView.OnPictureSavedListener;

/**
 * Activity for edit the given photo
 * 
 * @author
 * 
 */
@SuppressLint("SimpleDateFormat")
public class PhotoEditGPUActivity extends Activity implements
		OnPictureSavedListener, OnClickListener {

	Bitmap bmp_photo, base_bmp_photo;

	View ll_one, ll_two, ll_three, ll_four, ll_five, ll_six, ll_seven,
			ll_eight, ll_nine, ll_ten, ll_11, ll_12, ll_13, ll_14, ll_15,
			ll_16, ll_17, ll_18;

	GPUImageFilterGroup gpuImageFilterGroup;
	private GPUImageView mGPUImageView;
	private Activity _activityPhotoEditGPU;
	LinearLayout img_btn_close, img_btn_save;
	HorizontalScrollView horizontalScrollView1;
	LinearLayout ll_setting_bar, ll_seekbarSetting;
	public static Bitmap iconBitmap;
	public static Bitmap EffectedBitmap;
	Intent intent;
	TextView text_share;
	private GPUImageView btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9,
			btn10, btn11, btn12, btn13, btn14, btn15, btn16, btn17, btn18;
	Bitmap myBitmap;
	List<GPUImageFilter> list = new LinkedList<GPUImageFilter>();
	SelctedLineareLayout layout;
	Bitmap bitmap;
	InstaFilter filter;
	CommonMethods common;
	private LinearLayout ll_back;
	private LinearLayout ll_next;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.photo_edit_acti_gpu);

		_activityPhotoEditGPU = this;
		common = new CommonMethods(_activityPhotoEditGPU, this);
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		display.getHeight();
		display.getWidth();

		text_share = (TextView) findViewById(R.id.text_share);

		layout = (SelctedLineareLayout) findViewById(R.id.root);
		layout.setselected(0);
		/**
		 * finding Buttons
		 */
		btn1 = (GPUImageView) findViewById(R.id.btn_one);
		btn2 = (GPUImageView) findViewById(R.id.btn_two);
		btn3 = (GPUImageView) findViewById(R.id.btn_three);
		btn4 = (GPUImageView) findViewById(R.id.btn_four);
		btn5 = (GPUImageView) findViewById(R.id.btn_five);
		btn6 = (GPUImageView) findViewById(R.id.btn_six);
		btn7 = (GPUImageView) findViewById(R.id.btn_seven);
		btn8 = (GPUImageView) findViewById(R.id.btn_eight);
		btn9 = (GPUImageView) findViewById(R.id.btn_nine);
		btn10 = (GPUImageView) findViewById(R.id.btn_ten);
		btn11 = (GPUImageView) findViewById(R.id.btn_11);
		btn12 = (GPUImageView) findViewById(R.id.btn_12);
		btn13 = (GPUImageView) findViewById(R.id.btn13);
		btn14 = (GPUImageView) findViewById(R.id.btn14);
		btn15 = (GPUImageView) findViewById(R.id.btn15);
		btn16 = (GPUImageView) findViewById(R.id.btn16);
		btn17 = (GPUImageView) findViewById(R.id.btn17);
		btn18 = (GPUImageView) findViewById(R.id.btn18);

		horizontalScrollView1 = (HorizontalScrollView) findViewById(R.id.horizontalScrollView1);

		iconBitmap = ShareActivityy.bitmapPhoto;

		gpuImageFilterGroup = new GPUImageFilterGroup(list);

		mGPUImageView = (GPUImageView) findViewById(R.id.gpuimage);
		mGPUImageView.setRatio(1);
		mGPUImageView.setImage(iconBitmap);
		// mGPUImageView.setFilter(gpuImageFilterGroup);

		ll_one = findViewById(R.id.ll_one);
		ll_two = findViewById(R.id.ll_two);
		ll_three = findViewById(R.id.ll_three);
		ll_four = findViewById(R.id.ll_four);
		ll_five = findViewById(R.id.ll_five);
		ll_six = findViewById(R.id.ll_six);
		ll_seven = findViewById(R.id.ll_seven);
		ll_eight = findViewById(R.id.ll_eight);
		ll_nine = findViewById(R.id.ll_nine);
		ll_ten = findViewById(R.id.ll_ten);
		ll_11 = findViewById(R.id.ll_11);
		ll_12 = findViewById(R.id.ll_12);
		ll_13 = findViewById(R.id.ll_13);
		ll_14 = findViewById(R.id.ll_14);
		ll_15 = findViewById(R.id.ll_15);
		ll_16 = findViewById(R.id.ll_16);
		ll_17 = findViewById(R.id.ll_17);
		ll_18 = findViewById(R.id.ll_18);
		//
		ll_one.setOnClickListener(this);
		ll_two.setOnClickListener(this);
		ll_three.setOnClickListener(this);
		ll_four.setOnClickListener(this);
		ll_five.setOnClickListener(this);
		ll_six.setOnClickListener(this);
		ll_seven.setOnClickListener(this);
		ll_eight.setOnClickListener(this);
		ll_nine.setOnClickListener(this);
		ll_ten.setOnClickListener(this);
		ll_11.setOnClickListener(this);
		ll_12.setOnClickListener(this);
		ll_13.setOnClickListener(this);
		ll_14.setOnClickListener(this);
		ll_15.setOnClickListener(this);
		ll_16.setOnClickListener(this);
		ll_17.setOnClickListener(this);
		ll_18.setOnClickListener(this);

		setImageSmallWithFilter();
		/**
		 * setting LinearLayout
		 */
		ll_back = (LinearLayout) findViewById(R.id.ll_back);
		ll_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});

		ll_next = (LinearLayout) findViewById(R.id.ll_next);
		ll_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				saveImage();
			}
		});
	}

	private void setImageSmallWithFilter() {
		// add GPUImageView in arrayList
		ArrayList<GPUImageView> arr = new ArrayList<GPUImageView>();
		for (int i = 0; i < 18; i++) {
			arr.add(btn1);
			arr.add(btn2);
			arr.add(btn3);
			arr.add(btn4);
			arr.add(btn5);
			arr.add(btn6);
			arr.add(btn7);
			arr.add(btn8);
			arr.add(btn9);
			arr.add(btn10);
			arr.add(btn11);
			arr.add(btn12);
			arr.add(btn13);
			arr.add(btn14);
			arr.add(btn15);
			arr.add(btn16);
			arr.add(btn17);
			arr.add(btn18);

		}

		// set bitmap on GPUImageView and Filter
		for (int i = 0; i < 18; i++) {
			arr.get(i).setImage(iconBitmap);

			try {
				InstaFilter filter = FilterHelper.getFilter(this, i);
				if (filter != null) {
					arr.get(i).setFilter(filter);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	public void onClick(View v) {
		List<GPUImageFilter> lisFilters;
		switch (v.getId()) {

		case R.id.ll_one:
			layout.setselected(0);
			lisFilters = new LinkedList<GPUImageFilter>();
			lisFilters.addAll(list);
			list = new LinkedList<GPUImageFilter>();
			for (int i = 0; i < lisFilters.size() - 1; i++) {
				list.add(lisFilters.get(i));
			}
			list.add(FilterHelper.getFilter(this, 0));
			gpuImageFilterGroup = new GPUImageFilterGroup(list);
			mGPUImageView.setFilter(gpuImageFilterGroup);
			mGPUImageView.requestRender();
			break;

		case R.id.ll_two:
			layout.setselected(1);
			lisFilters = new LinkedList<GPUImageFilter>();
			lisFilters.addAll(list);
			list = new LinkedList<GPUImageFilter>();
			for (int i = 0; i < lisFilters.size() - 1; i++) {
				list.add(lisFilters.get(i));
			}
			list.add(FilterHelper.getFilter(this, 1));
			gpuImageFilterGroup = new GPUImageFilterGroup(list);
			mGPUImageView.setFilter(gpuImageFilterGroup);
			mGPUImageView.requestRender();
			break;

		case R.id.ll_three:
			layout.setselected(2);
			lisFilters = new LinkedList<GPUImageFilter>();
			lisFilters.addAll(list);
			list = new LinkedList<GPUImageFilter>();
			for (int i = 0; i < lisFilters.size() - 1; i++) {
				list.add(lisFilters.get(i));
			}
			list.add(FilterHelper.getFilter(this, 2));
			gpuImageFilterGroup = new GPUImageFilterGroup(list);
			mGPUImageView.setFilter(gpuImageFilterGroup);
			mGPUImageView.requestRender();
			break;

		case R.id.ll_four:
			layout.setselected(3);
			lisFilters = new LinkedList<GPUImageFilter>();
			lisFilters.addAll(list);
			list = new LinkedList<GPUImageFilter>();
			for (int i = 0; i < lisFilters.size() - 1; i++) {
				list.add(lisFilters.get(i));
			}
			list.add(FilterHelper.getFilter(this, 3));
			gpuImageFilterGroup = new GPUImageFilterGroup(list);
			mGPUImageView.setFilter(gpuImageFilterGroup);
			mGPUImageView.requestRender();
			break;

		case R.id.ll_five:
			layout.setselected(4);
			lisFilters = new LinkedList<GPUImageFilter>();
			lisFilters.addAll(list);
			list = new LinkedList<GPUImageFilter>();
			for (int i = 0; i < lisFilters.size() - 1; i++) {
				list.add(lisFilters.get(i));
			}
			list.add(FilterHelper.getFilter(this, 4));
			gpuImageFilterGroup = new GPUImageFilterGroup(list);
			mGPUImageView.setFilter(gpuImageFilterGroup);
			mGPUImageView.requestRender();
			break;

		case R.id.ll_six:
			layout.setselected(5);
			lisFilters = new LinkedList<GPUImageFilter>();
			lisFilters.addAll(list);
			list = new LinkedList<GPUImageFilter>();
			for (int i = 0; i < lisFilters.size() - 1; i++) {
				list.add(lisFilters.get(i));
			}
			list.add(FilterHelper.getFilter(this, 5));
			gpuImageFilterGroup = new GPUImageFilterGroup(list);
			mGPUImageView.setFilter(gpuImageFilterGroup);
			mGPUImageView.requestRender();
			break;

		case R.id.ll_seven:
			layout.setselected(6);
			lisFilters = new LinkedList<GPUImageFilter>();
			lisFilters.addAll(list);
			list = new LinkedList<GPUImageFilter>();
			for (int i = 0; i < lisFilters.size() - 1; i++) {
				list.add(lisFilters.get(i));
			}
			list.add(FilterHelper.getFilter(this, 6));
			gpuImageFilterGroup = new GPUImageFilterGroup(list);
			mGPUImageView.setFilter(gpuImageFilterGroup);
			mGPUImageView.requestRender();
			break;

		case R.id.ll_eight:
			layout.setselected(7);

			lisFilters = new LinkedList<GPUImageFilter>();
			lisFilters.addAll(list);
			list = new LinkedList<GPUImageFilter>();
			for (int i = 0; i < lisFilters.size() - 1; i++) {
				list.add(lisFilters.get(i));
			}
			list.add(FilterHelper.getFilter(this, 7));
			gpuImageFilterGroup = new GPUImageFilterGroup(list);
			mGPUImageView.setFilter(gpuImageFilterGroup);
			mGPUImageView.requestRender();
			break;

		case R.id.ll_nine:
			layout.setselected(8);
			lisFilters = new LinkedList<GPUImageFilter>();
			lisFilters.addAll(list);
			list = new LinkedList<GPUImageFilter>();
			for (int i = 0; i < lisFilters.size() - 1; i++) {
				list.add(lisFilters.get(i));
			}
			list.add(FilterHelper.getFilter(this, 8));
			gpuImageFilterGroup = new GPUImageFilterGroup(list);
			mGPUImageView.setFilter(gpuImageFilterGroup);
			mGPUImageView.requestRender();
			break;

		case R.id.ll_ten:
			layout.setselected(9);
			lisFilters = new LinkedList<GPUImageFilter>();
			lisFilters.addAll(list);
			list = new LinkedList<GPUImageFilter>();
			for (int i = 0; i < lisFilters.size() - 1; i++) {
				list.add(lisFilters.get(i));
			}
			list.add(FilterHelper.getFilter(this, 9));
			gpuImageFilterGroup = new GPUImageFilterGroup(list);
			mGPUImageView.setFilter(gpuImageFilterGroup);
			mGPUImageView.requestRender();
			break;

		case R.id.ll_11:
			layout.setselected(10);
			lisFilters = new LinkedList<GPUImageFilter>();
			lisFilters.addAll(list);
			list = new LinkedList<GPUImageFilter>();
			for (int i = 0; i < lisFilters.size() - 1; i++) {
				list.add(lisFilters.get(i));
			}
			list.add(FilterHelper.getFilter(this, 10));
			gpuImageFilterGroup = new GPUImageFilterGroup(list);
			mGPUImageView.setFilter(gpuImageFilterGroup);
			mGPUImageView.requestRender();
			break;

		case R.id.ll_12:
			layout.setselected(11);
			lisFilters = new LinkedList<GPUImageFilter>();
			lisFilters.addAll(list);
			list = new LinkedList<GPUImageFilter>();
			for (int i = 0; i < lisFilters.size() - 1; i++) {
				list.add(lisFilters.get(i));
			}
			list.add(FilterHelper.getFilter(this, 11));
			gpuImageFilterGroup = new GPUImageFilterGroup(list);
			mGPUImageView.setFilter(gpuImageFilterGroup);
			mGPUImageView.requestRender();
			break;

		case R.id.ll_13:
			layout.setselected(12);
			lisFilters = new LinkedList<GPUImageFilter>();
			lisFilters.addAll(list);
			list = new LinkedList<GPUImageFilter>();
			for (int i = 0; i < lisFilters.size() - 1; i++) {
				list.add(lisFilters.get(i));
			}
			list.add(FilterHelper.getFilter(this, 12));
			gpuImageFilterGroup = new GPUImageFilterGroup(list);
			mGPUImageView.setFilter(gpuImageFilterGroup);
			mGPUImageView.requestRender();
			break;

		case R.id.ll_14:
			layout.setselected(13);
			lisFilters = new LinkedList<GPUImageFilter>();
			lisFilters.addAll(list);
			list = new LinkedList<GPUImageFilter>();
			for (int i = 0; i < lisFilters.size() - 1; i++) {
				list.add(lisFilters.get(i));
			}
			list.add(FilterHelper.getFilter(this, 13));
			gpuImageFilterGroup = new GPUImageFilterGroup(list);
			mGPUImageView.setFilter(gpuImageFilterGroup);
			mGPUImageView.requestRender();
			break;

		case R.id.ll_15:
			layout.setselected(14);
			lisFilters = new LinkedList<GPUImageFilter>();
			lisFilters.addAll(list);
			list = new LinkedList<GPUImageFilter>();
			for (int i = 0; i < lisFilters.size() - 1; i++) {
				list.add(lisFilters.get(i));
			}
			list.add(FilterHelper.getFilter(this, 14));
			gpuImageFilterGroup = new GPUImageFilterGroup(list);
			mGPUImageView.setFilter(gpuImageFilterGroup);
			mGPUImageView.requestRender();
			break;

		case R.id.ll_16:
			layout.setselected(15);
			lisFilters = new LinkedList<GPUImageFilter>();
			lisFilters.addAll(list);
			list = new LinkedList<GPUImageFilter>();
			for (int i = 0; i < lisFilters.size() - 1; i++) {
				list.add(lisFilters.get(i));
			}
			list.add(FilterHelper.getFilter(this, 15));
			gpuImageFilterGroup = new GPUImageFilterGroup(list);
			mGPUImageView.setFilter(gpuImageFilterGroup);
			mGPUImageView.requestRender();
			break;

		case R.id.ll_17:
			layout.setselected(16);
			lisFilters = new LinkedList<GPUImageFilter>();
			lisFilters.addAll(list);
			list = new LinkedList<GPUImageFilter>();
			for (int i = 0; i < lisFilters.size() - 1; i++) {
				list.add(lisFilters.get(i));
			}
			list.add(FilterHelper.getFilter(this, 16));
			gpuImageFilterGroup = new GPUImageFilterGroup(list);
			mGPUImageView.setFilter(gpuImageFilterGroup);
			mGPUImageView.requestRender();
			break;

		case R.id.ll_18:
			layout.setselected(17);
			lisFilters = new LinkedList<GPUImageFilter>();
			lisFilters.addAll(list);
			list = new LinkedList<GPUImageFilter>();
			for (int i = 0; i < lisFilters.size() - 1; i++) {
				list.add(lisFilters.get(i));
			}
			list.add(FilterHelper.getFilter(this, 17));
			gpuImageFilterGroup = new GPUImageFilterGroup(list);
			mGPUImageView.setFilter(gpuImageFilterGroup);
			mGPUImageView.requestRender();
			break;

		default:
			break;
		}

	}

	@Override
	public void onPictureSaved(File file) {

		Intent intent = new Intent(_activityPhotoEditGPU,
				SharePhotoActivity.class);

		System.out.println("file" + file.getAbsolutePath());

		intent.putExtra("imagepath", file.getAbsolutePath());
		startActivity(intent);
		common.dismissProgressDialog();

	}

	private void saveImage() {
		try {
			String fileName = "";
			String filepath = "";
			common.showProgressDialog();

			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
					.format(new Date());

			File outputDir = _activityPhotoEditGPU.getCacheDir(); // context
																	// being the
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
			File mediaFile;
			mediaFile = new File(outputDir.getPath() + File.separator + "IMG_"
					+ timeStamp + ".png");
			fileName = "IMG_" + timeStamp + ".png";
			filepath = mediaFile.getPath();
			System.out.println("edit photo path " + filepath);
			mGPUImageView.saveToPictures("PetSutra", fileName, this);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}