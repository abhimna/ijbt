package com.pluggdd.ijbt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.util.URLFactory;
import com.pluggdd.ijbt.vo.GsonParseProfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;


public class MyProfilePostsAdapter extends RecyclerView.Adapter<MyProfilePostsAdapter.MyViewHolder> {

    private ArrayList<GsonParseProfile.Userdetail.Posts.Post> newsFeedPostDatas;
    Context context;
    int width;

public class MyViewHolder extends RecyclerView.ViewHolder {
    private final ProgressBar progressbar;
    private LinearLayout llRoot;
    private ImageView imageViewUploaded;
    private TextView textViewTitle;
    private TextView txtViewProgressLike;
    private TextView txtViewProgressDisLike;

    public MyViewHolder(View view) {
        super(view);
        llRoot = (LinearLayout) view.findViewById(R.id.ll_root);
        imageViewUploaded = (ImageView) view.findViewById(R.id.imageViewUploaded);
        textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        txtViewProgressLike = (TextView) view.findViewById(R.id.txtViewProgressLike);
        txtViewProgressDisLike = (TextView) view.findViewById(R.id.txtViewProgressDisLike);
        progressbar = (ProgressBar) view.findViewById(R.id.progress);
    }

}

    public MyProfilePostsAdapter(Context context, ArrayList<GsonParseProfile.Userdetail.Posts.Post> newsFeedPostDatas, int width) {
        this.context = context;
        this.newsFeedPostDatas = newsFeedPostDatas;
        this.width = width;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_my_uploads, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final GsonParseProfile.Userdetail.Posts.Post newsFeedPostData = newsFeedPostDatas.get(position);
        holder.textViewTitle.setText(newsFeedPostData.getTitle());
        String totallikes = newsFeedPostData.getPost_total_like();
        String totalDislikes = newsFeedPostData.getPost_total_dislike();

        Random r = new Random();
        float likeCount = 0;
        float dislikeCount = 0;
        float totalCount = 0;
        try {
            likeCount = Integer.parseInt(totallikes);
            dislikeCount = Integer.parseInt(totalDislikes);
            totalCount = likeCount + dislikeCount;
        } catch (Exception e) {

        }
        if (totalCount > 0) {
            float likePercentage = (likeCount * 100) / totalCount;
            float disLikePercentage = (dislikeCount * 100) / totalCount;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, Float.parseFloat(String.valueOf(likePercentage)));
            holder.txtViewProgressLike.setLayoutParams(params);
            holder.txtViewProgressLike.setText(Math.round(likePercentage) + " %");
            params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, Float.parseFloat(String.valueOf(disLikePercentage)));
            params.gravity = Gravity.CENTER;
            holder.txtViewProgressDisLike.setLayoutParams(params);
            holder.txtViewProgressDisLike.setText(Math.round(disLikePercentage) + " %");
        }
        holder.llRoot.setLayoutParams(new LinearLayout.LayoutParams(width, width));
        holder.llRoot.invalidate();
        holder.progressbar.setVisibility(View.VISIBLE);
        Picasso.with(context).load(URLFactory.imageUrl + newsFeedPostData.getThumb_image())
                // .placeholder(R.drawable.placeholder_looka)
                .into(holder.imageViewUploaded, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressbar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                    }
                });
        //holder.categoryRootView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height60 / 2));
        /*holder.txtViewProgressLike;
        holder.txtViewProgressDisLike;*/
    }

    @Override
    public int getItemCount() {
        return newsFeedPostDatas.size();
    }

    public void setData(ArrayList<GsonParseProfile.Userdetail.Posts.Post> data) {
        this.newsFeedPostDatas = data;
        notifyDataSetChanged();
    }
}
