package com.pluggdd.ijbt.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class GsonParseProfile {

    private String message;
    private String status;
    private Userdetail userdetail;

    private App_status app_status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Userdetail getUserdetail() {
        return userdetail;
    }

    public void setUserdetail(Userdetail userdetail) {
        this.userdetail = userdetail;
    }

    public App_status getApp_status() {
        return app_status;
    }

    public void setApp_status(App_status app_status) {
        this.app_status = app_status;
    }

    public class Userdetail {

        private String userid;
        private String facebook_id;
        private String twitter_id;
        private String user_name;
        private String fname;
        private String lname;
        private String pass;
        private String email;
        private String gender;
        private String dob;
        private String user_bio;
        private String phone;
        private String location;
        private String account_status;
        private String user_thumbimage;
        private String user_image;
        private String device_token;
        private String token;
        private String email_verify;
        private String allow_follow;
        private String is_private;
        private String status;
        private String admin_deactivate;
        private String dtdate;
        private String totalfollower;
        private String totalrequests;
        private String totalfollowing;
        private String is_blocked;
        private String is_following;

        private Posts posts;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getFacebook_id() {
            return facebook_id;
        }

        public void setFacebook_id(String facebook_id) {
            this.facebook_id = facebook_id;
        }

        public String getTwitter_id() {
            return twitter_id;
        }

        public void setTwitter_id(String twitter_id) {
            this.twitter_id = twitter_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getLname() {
            return lname;
        }

        public void setLname(String lname) {
            this.lname = lname;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getUser_bio() {
            return user_bio;
        }

        public void setUser_bio(String user_bio) {
            this.user_bio = user_bio;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getAccount_status() {
            return account_status;
        }

        public void setAccount_status(String account_status) {
            this.account_status = account_status;
        }

        public String getUser_thumbimage() {
            return user_thumbimage;
        }

        public void setUser_thumbimage(String user_thumbimage) {
            this.user_thumbimage = user_thumbimage;
        }

        public String getUser_image() {
            return user_image;
        }

        public void setUser_image(String user_image) {
            this.user_image = user_image;
        }

        public String getDevice_token() {
            return device_token;
        }

        public void setDevice_token(String device_token) {
            this.device_token = device_token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getEmail_verify() {
            return email_verify;
        }

        public void setEmail_verify(String email_verify) {
            this.email_verify = email_verify;
        }

        public String getAllow_follow() {
            return allow_follow;
        }

        public void setAllow_follow(String allow_follow) {
            this.allow_follow = allow_follow;
        }

        public String getIs_private() {
            return is_private;
        }

        public void setIs_private(String is_private) {
            this.is_private = is_private;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAdmin_deactivate() {
            return admin_deactivate;
        }

        public void setAdmin_deactivate(String admin_deactivate) {
            this.admin_deactivate = admin_deactivate;
        }

        public String getDtdate() {
            return dtdate;
        }

        public void setDtdate(String dtdate) {
            this.dtdate = dtdate;
        }

        public String getTotalfollower() {
            return totalfollower;
        }

        public void setTotalfollower(String totalfollower) {
            this.totalfollower = totalfollower;
        }

        public String getTotalrequests() {
            return totalrequests;
        }

        public void setTotalrequests(String totalrequests) {
            this.totalrequests = totalrequests;
        }

        public String getTotalfollowing() {
            return totalfollowing;
        }

        public void setTotalfollowing(String totalfollowing) {
            this.totalfollowing = totalfollowing;
        }

        public String getIs_blocked() {
            return is_blocked;
        }

        public void setIs_blocked(String is_blocked) {
            this.is_blocked = is_blocked;
        }

        public String getIs_following() {
            return is_following;
        }

        public void setIs_following(String is_following) {
            this.is_following = is_following;
        }

        public Posts getPosts() {
            return posts;
        }

        public void setPosts(Posts posts) {
            this.posts = posts;
        }

        public class Posts {

            private String total_image_data;
            private String total_video_data;
            private ArrayList<Post> post;
            private String totalpage;

            public String getTotal_image_data() {
                return total_image_data;
            }

            public void setTotal_image_data(String total_image_data) {
                this.total_image_data = total_image_data;
            }

            public String getTotal_video_data() {
                return total_video_data;
            }

            public void setTotal_video_data(String total_video_data) {
                this.total_video_data = total_video_data;
            }

            public ArrayList<Post> getData() {
                return post;
            }

            public void setData(ArrayList<Post> data) {
                this.post = data;
            }

            public String getTotal_page() {
                return totalpage;
            }

            public void setTotal_page(String total_page) {
                this.totalpage = total_page;
            }

            public class Post implements Parcelable {

                private String is_post_disliked;
                private String is_post_favourate;
                private String is_post_rated;
                private String average_rate;
                private String count_comment;
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
                private String follow_status;

                public String getFollow_status() {
                    return follow_status;
                }

                public void setFollow_status(String follow_status) {
                    this.follow_status = follow_status;
                }

                private ArrayList<NewsFeedCommentData> last_three_comments;

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

                public String getThumb_image() {
                    return thumb_image;
                }

                public void setThumb_image(String thumb_image) {
                    this.thumb_image = thumb_image;
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
                    this.post_total_dislike = post_total_dislike;
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

                public String getIs_post_disliked() {
                    return is_post_disliked;
                }

                public void setIs_post_disliked(String is_post_disliked) {
                    this.is_post_disliked = is_post_disliked;
                }

                public String getIs_post_favourate() {
                    return is_post_favourate;
                }

                public void setIs_post_favourate(String is_post_favourate) {
                    this.is_post_favourate = is_post_favourate;
                }

                public String getIs_post_rated() {
                    return is_post_rated;
                }

                public void setIs_post_rated(String is_post_rated) {
                    this.is_post_rated = is_post_rated;
                }

                public String getAverage_rate() {
                    return average_rate;
                }

                public void setAverage_rate(String average_rate) {
                    this.average_rate = average_rate;
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

                public String getCount_comment() {
                    return count_comment;
                }

                public void setCount_comment(String count_comment) {
                    this.count_comment = count_comment;
                }

                public ArrayList<NewsFeedCommentData> getLast_three_comments() {
                    return last_three_comments;
                }

                public void setLast_three_comments(ArrayList<NewsFeedCommentData> last_three_comments) {
                    this.last_three_comments = last_three_comments;
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

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {

                }
            }

        }

    }

    public class App_status {

        private String status;
        private String msg;
        private String user_status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        public String getAudio_status() {
            return audio_status;
        }

        public void setAudio_status(String audio_status) {
            this.audio_status = audio_status;
        }

        public String getVideo_status() {
            return video_status;
        }

        public void setVideo_status(String video_status) {
            this.video_status = video_status;
        }

        public String getAudio_plans() {
            return audio_plans;
        }

        public void setAudio_plans(String audio_plans) {
            this.audio_plans = audio_plans;
        }

        public String getVideo_plans() {
            return video_plans;
        }

        public void setVideo_plans(String video_plans) {
            this.video_plans = video_plans;
        }

        private String audio_status;
        private String video_status;
        private String audio_plans;
        private String video_plans;

    }

}
