package com.pluggdd.ijbt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

@SuppressLint("ClickableViewAccessibility")
public class AudioRecordActivity extends Activity {

	private MediaRecorder myAudioRecorder;
	private String outputFile = null;
	private ImageButton reset, stop, play;
	private ImageView start;
	private ProgressBar progressBar;
	private int progressStatus = 0;
	private TextView textView;
	private Handler handler = new Handler();
	Runnable runnable;
	private RelativeLayout ll_left_icon;
	RelativeLayout ll_right_icon;
	private Activity activity;
	private int progress_audio;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.audio_record);

		activity = this;
		reset = (ImageButton) findViewById(R.id.button1);
		start = (ImageView) findViewById(R.id.imageView1);
		play = (ImageButton) findViewById(R.id.button3);

		if (SharePhotoActivity.planStatus.equalsIgnoreCase("audio1")) {
			progress_audio = 15;
		} else {
			progress_audio = 180;
		}

		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar.setMax(progress_audio);
		textView = (TextView) findViewById(R.id.textView1);
		ll_right_icon = (RelativeLayout) findViewById(R.id.ll_right_icon);
		ll_left_icon = (RelativeLayout) findViewById(R.id.ll_left_icon);

		ll_left_icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.finish();

			}
		});

		ll_right_icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("audio", outputFile);
				setResult(RESULT_OK, intent);
				activity.finish();

			}
		});

		runnable = new Runnable() {

			@Override
			public void run() {
				if (progressStatus < progress_audio) {
					progressStatus += 1;
					progressBar.setProgress(progressStatus);
					textView.setText(progressStatus + "/"
							+ progressBar.getMax() + " " + "Sec.");
					handler.postDelayed(runnable, 1000);

				} else {
					handler.removeCallbacks(runnable);
					Toast.makeText(getApplicationContext(),
							"Audio recorded successfully", Toast.LENGTH_LONG)
							.show();
				}

			}
		};

		start.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					try {

						handler.postDelayed(runnable, 1000);

						myAudioRecorder.prepare();
						myAudioRecorder.start();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					Toast.makeText(getApplicationContext(),
							"Recording started", Toast.LENGTH_LONG).show();
					break;
				case MotionEvent.ACTION_UP:
					handler.removeCallbacks(runnable);
					myAudioRecorder.stop();
					myAudioRecorder.release();
					myAudioRecorder = null;
					Toast.makeText(getApplicationContext(),
							"Audio recorded successfully", Toast.LENGTH_LONG)
							.show();
					break;
				}
				return true;
			}
		});

		outputFile = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/myrecording.m4a";

		myAudioRecorder = new MediaRecorder();
		myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.MPEG_4);
		myAudioRecorder.setOutputFile(outputFile);

	}

	public void reset(View view) {
		try {
			myAudioRecorder.release();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		start.setEnabled(false);
		stop.setEnabled(true);
		Toast.makeText(getApplicationContext(), "Recording started",
				Toast.LENGTH_LONG).show();

	}

	public void play(View view) throws IllegalArgumentException,
			SecurityException, IllegalStateException, IOException {

		MediaPlayer m = new MediaPlayer();
		m.setDataSource(outputFile);
		m.prepare();
		m.start();
		Toast.makeText(getApplicationContext(), "Playing audio",
				Toast.LENGTH_LONG).show();

	}

}