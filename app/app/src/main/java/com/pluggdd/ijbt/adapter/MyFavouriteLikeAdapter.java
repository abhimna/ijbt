package com.pluggdd.ijbt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.util.URLFactory;
import com.pluggdd.ijbt.vo.NewsFeedPostData;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;


public class MyFavouriteLikeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<NewsFeedPostData> newsFeedPostDatas;
    Context context;
    int width;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public void setData(ArrayList<NewsFeedPostData> newsFeedPostDatas) {
        this.newsFeedPostDatas = newsFeedPostDatas;
        notifyDataSetChanged();
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return newsFeedPostDatas.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar progressbar;
        private LinearLayout llRoot;
        private LinearLayout layoutImageView;
        private ImageView imageViewUploaded;

        private TextView textViewTitle;
        private TextView txtViewProgressLike;
        private TextView txtViewProgressDisLike;

        public MyViewHolder(View view) {
            super(view);
            llRoot = (LinearLayout) view.findViewById(R.id.ll_root);
            layoutImageView = (LinearLayout) view.findViewById(R.id.layoutImageView);
            imageViewUploaded = (ImageView) view.findViewById(R.id.imageViewUploaded);
            textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
            txtViewProgressLike = (TextView) view.findViewById(R.id.txtViewProgressLike);
            txtViewProgressDisLike = (TextView) view.findViewById(R.id.txtViewProgressDisLike);
            progressbar = (ProgressBar) view.findViewById(R.id.progress);
        }
    }

    public MyFavouriteLikeAdapter(Context context, ArrayList<NewsFeedPostData> newsFeedPostDatas, int width) {
        this.context = context;
        this.newsFeedPostDatas = newsFeedPostDatas;
        this.width = width;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_fav_like, parent, false);

            vh = new MyViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_progress
                            , parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            final MyViewHolder myViewHolder = (MyViewHolder) holder;
            final NewsFeedPostData newsFeedPostData = newsFeedPostDatas.get(position);
            myViewHolder.textViewTitle.setText(newsFeedPostData.getTitle());
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
                myViewHolder.txtViewProgressLike.setLayoutParams(params);
                myViewHolder.txtViewProgressLike.setText(Math.round(likePercentage) + " %");
                params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, Float.parseFloat(String.valueOf(disLikePercentage)));
                params.gravity = Gravity.CENTER;
                myViewHolder.txtViewProgressDisLike.setLayoutParams(params);
                myViewHolder.txtViewProgressDisLike.setText(Math.round(disLikePercentage) + " %");
            }
            else {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, Float.parseFloat(String.valueOf(100)));
                params.gravity = Gravity.CENTER;
                myViewHolder.txtViewProgressLike.setLayoutParams(params);
                myViewHolder.txtViewProgressLike.setText("0%");
            }
            Double widthDouble =width/1.5;
            int widthInt = widthDouble.intValue();
            myViewHolder.llRoot.setLayoutParams(new LinearLayout.LayoutParams(widthInt,widthInt));
            myViewHolder.llRoot.invalidate();
            myViewHolder.progressbar.setVisibility(View.VISIBLE);
            Picasso.with(context).load(URLFactory.imageUrl + newsFeedPostData.getImage())
                    //.networkPolicy(NetworkPolicy.OFFLINE)
                    //.resize(height, height)
                    .into(myViewHolder.imageViewUploaded, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            myViewHolder.progressbar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                            myViewHolder.progressbar.setVisibility(View.INVISIBLE);
                            Picasso.with(context).load(URLFactory.imageUrl + newsFeedPostData.getImage())
                                    //.networkPolicy(NetworkPolicy.OFFLINE)
                                    //.error(R.drawable.ic_404)
                                    //.resize(height, height)
                                    .into(myViewHolder.imageViewUploaded, new com.squareup.picasso.Callback() {
                                        @Override
                                        public void onSuccess() {
                                            myViewHolder.progressbar.setVisibility(View.INVISIBLE);
                                        }

                                        @Override
                                        public void onError() {
                                            Log.v("Picasso", "Could not fetch image");
                                        }
                                    });
                        }
                    });
            /*myViewHolder.imageViewUploaded.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    myViewHolder.imageViewUploaded.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    final int height = myViewHolder.imageViewUploaded.getHeight();

                    myViewHolder.progressbar.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(URLFactory.imageUrl + newsFeedPostData.getImage())
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .resize(height, height)
                            .into(myViewHolder.imageViewUploaded, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    myViewHolder.progressbar.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onError() {
                                    myViewHolder.progressbar.setVisibility(View.INVISIBLE);
                                    Picasso.with(context).load(URLFactory.imageUrl + newsFeedPostData.getImage())
                                            .networkPolicy(NetworkPolicy.OFFLINE)
                                            .error(R.drawable.ic_404)
                                            .resize(height, height)
                                            .into(myViewHolder.imageViewUploaded, new com.squareup.picasso.Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    myViewHolder.progressbar.setVisibility(View.INVISIBLE);
                                                }

                                                @Override
                                                public void onError() {
                                                    Log.v("Picasso", "Could not fetch image");
                                                }
                                            });
                                }
                            });
                }
            });*/
            /*myViewHolder.layoutImageView.setLayoutParams(new RelativeLayout.LayoutParams(width, width));
            myViewHolder.layoutImageView.invalidate();
            myViewHolder.llRoot.invalidate();
            myViewHolder.progressbar.setVisibility(View.VISIBLE);
            Picasso.with(context).load(URLFactory.imageUrl + newsFeedPostData.getImage())
                    // .placeholder(R.drawable.placeholder_looka)
                    .resize(width, width)
                    .into(myViewHolder.imageViewUploaded, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            myViewHolder.progressbar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                        }
                    });*/
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return newsFeedPostDatas.size();
    }
}
