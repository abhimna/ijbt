package com.pluggdd.ijbt.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.util.URLFactory;
import com.pluggdd.ijbt.vo.GsonParseProfile.Userdetail.Posts.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileImagesVideosAdapter extends BaseAdapter {
	private Activity mactivity;
	private ImageView image;
	private ImageView image_type;
	private ArrayList<Post> alArrayList_images = new ArrayList<Post>();
	private Display mDisplay;
	final int width;

	@SuppressWarnings("deprecation")
	public ProfileImagesVideosAdapter(Activity activity,
			ArrayList<Post> arrayList) {
		this.mactivity = activity;

		this.alArrayList_images = arrayList;

		System.out.println("ImageArrayList >" + arrayList);

		mDisplay = activity.getWindowManager().getDefaultDisplay();
		width = mDisplay.getWidth();
	}

	@Override
	public int getCount() {

		return alArrayList_images.size();

	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		LayoutInflater inflater = (LayoutInflater) mactivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.row_explore, null);

		image = (ImageView) view.findViewById(R.id.screen);
		image_type = (ImageView) view.findViewById(R.id.img_type);
		image.getLayoutParams().height = width / 3;
		image.getLayoutParams().width = width / 3;

		Picasso.with(mactivity)
				.load(URLFactory.imageUrl
						+ alArrayList_images.get(position).getThumb_image())
				.placeholder(R.drawable.placeholder_looka).fit().into(image);
		if (alArrayList_images.get(position).getData_type().equals("audio")) {
			if (alArrayList_images.get(position).getData_url().equals("")) {
				image_type.setVisibility(View.GONE);
			} else {
				image_type.setVisibility(View.VISIBLE);
				image_type.setBackgroundResource(R.drawable.play_phone);
			}

		} else {
			Picasso.with(mactivity)
					.load(URLFactory.imageUrl
							+ alArrayList_images.get(position).getThumb_image())
					.placeholder(R.drawable.placeholder_looka).fit()

					.into(image);
			if (alArrayList_images.get(position).getData_type().equals("video")) {
				image_type.setVisibility(View.VISIBLE);
				image_type.setBackgroundResource(R.drawable.play_oval);
			}
		}

		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// if (flag_images) {
				String strUserId = alArrayList_images.get(position)
						.getUser_id();
				String strPostId = alArrayList_images.get(position)
						.getPost_id();
				// Comman.flag_search = true;
				Bundle bundle = new Bundle();
				bundle.putString("userid", strUserId);
				bundle.putString("postid", strPostId);

				/*SinglePostFragment fragment = new SinglePostFragment();
				fragment.setArguments(bundle);

				mactivity
						.getFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.animator.enter_anim,
								R.animator.exit_anim,
								R.animator.back_anim_start,
								R.animator.back_anim_end)
						.add(R.id.container, fragment).addToBackStack(null)
						.commit();*/


				// } else {
				// String strUserId = alArrayList_videos.get(position)
				// .getUser_id();
				// String strPostId = alArrayList_videos.get(position)
				// .getPost_id();
				// Comman.flag_search = true;
				// Bundle bundle = new Bundle();
				// bundle.putString("userid", strUserId);
				// bundle.putString("postid", strPostId);
				//
				// SinglePostFragment fragment = new SinglePostFragment();
				// fragment.setArguments(bundle);
				//
				// mactivity.getFragmentManager().beginTransaction()
				// .add(R.id.container, fragment).addToBackStack(null)
				// .commit();
				// }

			}
		});

		return view;

	}

	public void setData(ArrayList<Post> post) {
		this.alArrayList_images = post;
		notifyDataSetChanged();

	}
}