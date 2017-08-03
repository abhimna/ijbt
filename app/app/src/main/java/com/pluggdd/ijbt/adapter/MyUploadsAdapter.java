package com.pluggdd.ijbt.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.fragment.ProductDetailsFragment;
import com.pluggdd.ijbt.util.ApplicationConstants;
import com.pluggdd.ijbt.util.GlobalConfig;
import com.pluggdd.ijbt.util.OnProductDeletedListener;
import com.pluggdd.ijbt.util.URLFactory;
import com.pluggdd.ijbt.vo.GsonParseProfile;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class MyUploadsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<GsonParseProfile.Userdetail.Posts.Post> newsFeedPostDatas;
    Activity context;
    int width;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    OnProductDeletedListener onProductDeletedListener;

    public void setData(ArrayList<GsonParseProfile.Userdetail.Posts.Post> newsFeedPostDatas) {
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
        position = position % newsFeedPostDatas.size();
        return newsFeedPostDatas.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar progressbar;
        private final ImageView imageViewRecycle;
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
            imageViewRecycle = (ImageView) view.findViewById(R.id.imageViewRecycle);

        }
    }

    public MyUploadsAdapter(Activity context, ArrayList<GsonParseProfile.Userdetail.Posts.Post> newsFeedPostDatas, int width, OnProductDeletedListener onProductDeletedListener) {
        this.context = context;
        this.newsFeedPostDatas = newsFeedPostDatas;
        this.onProductDeletedListener = onProductDeletedListener;
        this.width = width;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_my_uploads, parent, false);

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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        //position = position % newsFeedPostDatas.size();
        if (holder instanceof MyViewHolder) {
            final MyViewHolder myViewHolder = (MyViewHolder) holder;
            final GsonParseProfile.Userdetail.Posts.Post newsFeedPostData = newsFeedPostDatas.get(position);
            myViewHolder.textViewTitle.setText(newsFeedPostData.getTitle());
            String totallikes = newsFeedPostData.getPost_total_like();
            String totalDislikes = newsFeedPostData.getPost_total_dislike();

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
            }else {
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
                    // .placeholder(R.drawable.placeholder_looka)
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
                            myViewHolder.progressbar.setVisibility(View.INVISIBLE);
                        }
                    });

            /*myViewHolder.imageViewUploaded.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    myViewHolder.imageViewUploaded.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int height = myViewHolder.imageViewUploaded.getHeight();

                    myViewHolder.progressbar.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(URLFactory.imageUrl + newsFeedPostData.getImage())
                            // .placeholder(R.drawable.placeholder_looka)
                            //.networkPolicy(NetworkPolicy.OFFLINE)
                            //.error(R.drawable.ic_404)
                            .resize(height, height)
                            .into(myViewHolder.imageViewUploaded, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    myViewHolder.progressbar.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onError() {
                                    myViewHolder.progressbar.setVisibility(View.INVISIBLE);
                                }
                            });
                }
            });*/
            myViewHolder.llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim,
                                    R.animator.back_anim_start, R.animator.back_anim_end)
                            .replace(R.id.container, ProductDetailsFragment.newInstance(newsFeedPostData))
                            .addToBackStack(null).commit();
                }
            });
            final int finalPosition = position;
            myViewHolder.imageViewRecycle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletePost(newsFeedPostData.getPost_id(), newsFeedPostData.getUser_id());
                    newsFeedPostDatas.remove(finalPosition);
                    MyUploadsAdapter.this.notifyItemRemoved(finalPosition);
                    MyUploadsAdapter.this.notifyItemRangeChanged(finalPosition, newsFeedPostDatas.size());
                }
            });

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    private void deletePost(String postId, String userId) {
        String url = null;
        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(ApplicationConstants.GetPostLikeAPIKeys.USERID_KEY, userId);
            rootJsonObj.put(ApplicationConstants.GetPostLikeAPIKeys.POST_ID_KEY, postId);
            rootJsonArr.put(rootJsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            url = URLFactory.GetPostDeleteUrl() + "&data="
                    + URLEncoder.encode(rootJsonArr.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(6000);
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {

                    try {
                        JSONObject rootJsonObjObject = new JSONObject(response);
                        String strMsg = rootJsonObjObject.getString("msg");
                        String strStatus = rootJsonObjObject.getString("status");

                        if (strStatus.equals("true")) {
                            onProductDeletedListener.onProductDeleted();
                        } else {
                            GlobalConfig.showToast(context, strMsg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    GlobalConfig.showToast(context, "Please try again");
                }

            }

            @SuppressWarnings("deprecation")
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, String content) {
                super.onFailure(statusCode, headers, error, content);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

        });
    }
    /*@Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }*/

    @Override
    public int getItemCount() {
        return newsFeedPostDatas.size();
    }
}
