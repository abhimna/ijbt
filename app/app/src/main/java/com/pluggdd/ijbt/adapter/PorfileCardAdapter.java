package com.pluggdd.ijbt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.util.CardTypeListener;
import com.pluggdd.ijbt.util.URLFactory;
import com.pluggdd.ijbt.vo.NewsFeedPostData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class PorfileCardAdapter extends BaseAdapter {

    private List<NewsFeedPostData> data;
    private Context context;
    private Animation animation;
    CardTypeListener cardTypeListener;

    public PorfileCardAdapter(ArrayList<NewsFeedPostData> data, Context context, CardTypeListener cardTypeListener) {
        this.data = data;
        this.context = context;
        this.cardTypeListener = cardTypeListener;
        this.animation = AnimationUtils.loadAnimation(context, R.anim.pulse);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            // normally use a viewholder
            v = inflater.inflate(R.layout.swipe_item, parent, false);
        }

        final NewsFeedPostData newsFeedPostData = data.get(position);
        final ImageView img_like_double = (ImageView) v.findViewById(R.id.img_like_double);
        ImageView img_post_image = (ImageView) v.findViewById(R.id.img_post_image);
        final ImageView img_type = (ImageView) v.findViewById(R.id.img_type);
        final ProgressBar progressbar = (ProgressBar) v.findViewById(R.id.progress);
        //TextView productTextView = (TextView) v.findViewById(R.id.textViewTitle);
        //productTextView.setText(newsFeedPostData.getTitle());

        if (newsFeedPostData.getData_type().equals("audio")) {

            if (newsFeedPostData.getData_url().equals("")) {
                img_type.setVisibility(View.INVISIBLE);
            } else {
                img_type.setVisibility(View.VISIBLE);
                img_type.setBackgroundResource(R.drawable.play_phone);
            }

            Picasso.with(context).load(URLFactory.imageUrl + newsFeedPostData.getImage())
                    // .placeholder(R.drawable.placeholder_looka)
                    .into(img_post_image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressbar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                        }
                    });
        } else if (newsFeedPostData.getData_type().equals("video")) {
            /*Bitmap bm = ThumbnailUtils.createVideoThumbnail(URLFactory.imageUrl + newsFeedPostData.getImage(), MediaStore.Images.Thumbnails.MINI_KIND);
            img_post_image.setImageBitmap(bm);
            img_type.setVisibility(View.VISIBLE);
            img_type.setBackgroundResource(R.drawable.play_oval);*/
            Picasso.with(context).load(URLFactory.imageUrl + newsFeedPostData.getImage())
                    // .placeholder(R.drawable.placeholder_looka)
                    .into(img_post_image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            img_type.setVisibility(View.VISIBLE);
                            img_type.setBackgroundResource(R.drawable.play_oval);
                            progressbar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                        }
                    });
        } else {
            Picasso.with(context).load(URLFactory.imageUrl + newsFeedPostData.getImage())
                    // .placeholder(R.drawable.placeholder_looka)
                    .into(img_post_image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressbar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                        }
                    });
        }

        /*img_post_image.setOnTouchListener(new View.OnTouchListener() {

            private GestureDetector gestureDetector = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener() {

                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {
                            return false;
                        }

                        @Override
                        public boolean onDoubleTap(MotionEvent e) {

                            animation.setAnimationListener(new Animation.AnimationListener() {

                                @Override
                                public void onAnimationStart(Animation arg0) {
                                    img_like_double.setTag(position);
                                    img_like_double.setAlpha(1f);
                                }

                                @Override
                                public void onAnimationRepeat(Animation arg0) {
                                    // TODO Auto-generated method
                                    // stub

                                }

                                @Override
                                public void onAnimationEnd(Animation arg0) {
                                    img_like_double.setTag(position);
                                    img_like_double.setAlpha(0f);

                                }
                            });

                            img_like_double.startAnimation(animation);
                            notifyDataSetChanged();
                            return super.onDoubleTap(e);
                        }
                    });

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // holder.img_post_image.setTag(position);
                gestureDetector.onTouchEvent(arg1);
                return true;
            }
        });*/

        img_type.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cardTypeListener.performAction(newsFeedPostData);
            }
        });

        return v;
    }
}
