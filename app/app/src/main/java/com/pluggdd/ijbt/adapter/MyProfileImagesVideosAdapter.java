package com.pluggdd.ijbt.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.util.URLFactory;
import com.pluggdd.ijbt.vo.GsonParseProfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyProfileImagesVideosAdapter extends RecyclerView.Adapter<MyProfileImagesVideosAdapter.MyViewHolder> {
	private Activity mactivity;

	private ArrayList<GsonParseProfile.Userdetail.Posts.Post> alArrayList_images = new ArrayList<GsonParseProfile.Userdetail.Posts.Post>();
	private Display mDisplay;
	final int width;

	public class MyViewHolder extends RecyclerView.ViewHolder {
		private ImageView image;
		private ImageView image_type;

		public MyViewHolder(View view) {
			super(view);
			image = (ImageView) view.findViewById(R.id.screen);
			image_type = (ImageView) view.findViewById(R.id.img_type);
		}
	}

	@SuppressWarnings("deprecation")
	public MyProfileImagesVideosAdapter(Activity activity,
										ArrayList<GsonParseProfile.Userdetail.Posts.Post> arrayList) {
		this.mactivity = activity;

		this.alArrayList_images = arrayList;

		System.out.println("ImageArrayList >" + arrayList);

		mDisplay = activity.getWindowManager().getDefaultDisplay();
		width = mDisplay.getWidth();
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.row_explore, parent, false);
		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, final int position) {
		holder.image.getLayoutParams().height = width / 3;
		holder.image.getLayoutParams().width = width / 3;

		Picasso.with(mactivity)
				.load(URLFactory.imageUrl
						+ alArrayList_images.get(position).getThumb_image())
				.placeholder(R.drawable.placeholder_looka).fit().into(holder.image);
		if (alArrayList_images.get(position).getData_type().equals("audio")) {
			if (alArrayList_images.get(position).getData_url().equals("")) {
				holder.image_type.setVisibility(View.GONE);
			} else {
				holder.image_type.setVisibility(View.VISIBLE);
				holder.image_type.setBackgroundResource(R.drawable.play_phone);
			}

		} else {
			Picasso.with(mactivity)
					.load(URLFactory.imageUrl
							+ alArrayList_images.get(position).getThumb_image())
					.placeholder(R.drawable.placeholder_looka).fit()

					.into(holder.image);
			if (alArrayList_images.get(position).getData_type().equals("video")) {
				holder.image_type.setVisibility(View.VISIBLE);
				holder.image_type.setBackgroundResource(R.drawable.play_oval);
			}
		}

		holder.image.setOnClickListener(new OnClickListener() {

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
			}
		});
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public int getItemCount() {
		return alArrayList_images.size();
	}

	public void setData(ArrayList<GsonParseProfile.Userdetail.Posts.Post> post) {
		this.alArrayList_images = post;
		notifyDataSetChanged();

	}
}