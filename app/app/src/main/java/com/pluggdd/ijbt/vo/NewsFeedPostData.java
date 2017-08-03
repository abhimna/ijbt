package com.pluggdd.ijbt.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class NewsFeedPostData implements Parcelable {

    private String post_id;
    private String user_id;
    private String title;
    private String image;
    private String thumb_image;
    private String data_type;
    private String data_url;
    private String suspend;
    private String dtdate;
    private String post_total_like;
    private String post_total_dislike;
    private String post_total_comment;
    private String is_post_liked;
    private String user_image;
    private String user_name;
    private String full_name;
    private String time_ago;
    private String placebought;
    private String currency;
    private String price;
    private String rating;
    private String category;
    private String comment;
    private String usertype;
    private String is_post_disliked;
    private String is_post_rated;
    private String is_post_favourate;
    private String average_rate;
    private String count_comment;
    private String follow_status;


    private ArrayList<NewsFeedCommentData> last_three_comments;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getIs_post_disliked() {
        return is_post_disliked;
    }

    public void setIs_post_disliked(String is_post_disliked) {
        this.is_post_disliked = is_post_disliked;
    }

    public String getIs_post_rated() {
        return is_post_rated;
    }

    public void setIs_post_rated(String is_post_rated) {
        this.is_post_rated = is_post_rated;
    }

    public String getIs_post_favourate() {
        return is_post_favourate;
    }

    public void setIs_post_favourate(String is_post_favourate) {
        this.is_post_favourate = is_post_favourate;
    }

    public String getAverage_rate() {
        return average_rate;
    }

    public void setAverage_rate(String average_rate) {
        this.average_rate = average_rate;
    }

    public String getCount_comment() {
        return count_comment;
    }

    public void setCount_comment(String count_comment) {
        this.count_comment = count_comment;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public String getData_url() {
        return data_url;
    }

    public void setData_url(String data_url) {
        this.data_url = data_url;
    }

    public String getSuspend() {
        return suspend;
    }

    public void setSuspend(String suspend) {
        this.suspend = suspend;
    }

    public String getDtdate() {
        return dtdate;
    }

    public void setDtdate(String dtdate) {
        this.dtdate = dtdate;
    }

    public String getPost_total_like() {
        return post_total_like;
    }

    public void setPost_total_like(String post_total_like) {
        this.post_total_like = post_total_like;
    }

    public String getPost_total_dislike() {
        return post_total_dislike;
    }

    public void setPost_total_dislike(String post_total_dislike) {
        this.post_total_like = post_total_like;
    }

    public String getPost_total_comment() {
        return post_total_comment;
    }

    public void setPost_total_comment(String post_total_comment) {
        this.post_total_comment = post_total_comment;
    }

    public String getIs_post_liked() {
        return is_post_liked;
    }

    public void setIs_post_liked(String is_post_liked) {
        this.is_post_liked = is_post_liked;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getTime_ago() {
        return time_ago;
    }

    public void setTime_ago(String time_ago) {
        this.time_ago = time_ago;
    }

    public String getPlacebought() {
        return placebought;
    }

    public void setPlacebought(String placebought) {
        this.placebought = placebought;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ArrayList<NewsFeedCommentData> getLast_three_comments() {
        return last_three_comments;
    }

    public void setLast_three_comments(
            ArrayList<NewsFeedCommentData> last_three_comments) {
        this.last_three_comments = last_three_comments;
    }

    public String getFollow_status() {
        return follow_status;
    }

    public void setFollow_status(String follow_status) {
        this.follow_status = follow_status;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    private NewsFeedPostData(Parcel in) {
        this.post_id = in.readString();
        this.user_id = in.readString();
        this.title = in.readString();
        this.image = in.readString();
        this.thumb_image = in.readString();
        this.data_type = in.readString();
        this.data_url = in.readString();
        this.suspend = in.readString();
        this.dtdate = in.readString();
        this.post_total_like = in.readString();
        this.post_total_dislike = in.readString();
        this.post_total_comment = in.readString();
        this.is_post_liked = in.readString();
        this.user_image = in.readString();
        this.user_name = in.readString();
        this.full_name = in.readString();
        this.time_ago = in.readString();
        this.placebought = in.readString();
        this.currency = in.readString();
        this.price = in.readString();
        this.rating = in.readString();
        this.category = in.readString();
        this.comment = in.readString();
        this.usertype = in.readString();
        this.is_post_disliked = in.readString();
        this.is_post_rated = in.readString();
        this.is_post_favourate = in.readString();
        this.average_rate = in.readString();
        this.count_comment = in.readString();
        this.follow_status = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(post_id);
        dest.writeString(user_id);
        dest.writeString(title);
        dest.writeString(image);
        dest.writeString(thumb_image);
        dest.writeString(data_type);
        dest.writeString(data_url);
        dest.writeString(suspend);
        dest.writeString(dtdate);
        dest.writeString(post_total_like);
        dest.writeString(post_total_dislike);
        dest.writeString(post_total_comment);
        dest.writeString(is_post_liked);
        dest.writeString(user_image);
        dest.writeString(user_name);
        dest.writeString(full_name);
        dest.writeString(time_ago);
        dest.writeString(placebought);
        dest.writeString(currency);
        dest.writeString(price);
        dest.writeString(rating);
        dest.writeString(category);
        dest.writeString(comment);
        dest.writeString(usertype);
        dest.writeString(is_post_disliked);
        dest.writeString(is_post_rated);
        dest.writeString(is_post_favourate);
        dest.writeString(average_rate);
        dest.writeString(count_comment);
        dest.writeString(follow_status);
    }
}
