package com.pluggdd.ijbt.util;

/**
 * this class is used for defining all global constants
 *
 * @author
 * 
 */
public class ApplicationConstants {

	public static final String BP_SIGNATURE_SUFFIX = "ijbt!@#321";
	/*public static final String BP_SIGNATURE_SUFFIX = "pluggdd!@#321";*/
	/*public static final String BP_SIGNATURE_SUFFIX = "instanine!@#321";*/
	public static final String SHARED_PREF_NAME = "BP_PREFShared";
	public static final String USER_ID = "userid";

	/**
	 * This interface is used for contains the login APIs keys
	 * 
	 * @author
	 */

	/**
	 * Twitter Constant..
	 * 
	 * 
	 */
	public interface TwitterAPI_KEY {

		public static final String CONSUMER_KEY = "hs7rfVSe9NHdGtyCXKWbjtwah";
		public static final String CONSUMER_SECRET = "3AmK3l1cREaeBCLyxccYM2ygS3IsyIvhdncDlEOUUr6bg39H4W";

		public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
		public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
		public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";

		final public static String CALLBACK_SCHEME = "x-latify-oauth-twitter";
		final public static String CALLBACK_URL = CALLBACK_SCHEME
				+ "://callback";
	}

	public interface VarifyAPIKeys {
		public static final String API_TYPE = "verify";
		public static final String EMAIL_KEY = "email";
		public static final String USERNAME_KEY = "username";
	}

	/**
	 * This interface is used for contains the login APIs keys
	 * 
	 * @author
	 */
	public interface LoginAPIKeys {
		public static final String API_TYPE = "login";
		public static final String DEVICE_TOKEN_KEY = "device_token";
		public static final String DEVICE_TYPE_KEY = "device_type";
		public static final String USERNAME_KEY = "username";
		public static final String PASSWORD_KEY = "password";
	}

	/**
	 * This interface is used for contains the forgot password APIs keys
	 * 
	 * @author
	 */
	public interface ForgotPasswordAPIKeys {
		public static final String API_TYPE = "forgot";
		public static final String EMAIL_KEY = "email";
	}

	/**
	 * This interface is used for contains the change password APIs keys
	 * 
	 * @author
	 */
	public interface ChangePasswordAPIKeys {
		public static final String API_TYPE = "change";
		public static final String OLD_PASSWORD_KEY = "oldpass";
		public static final String NEW_PASSWORD_KEY = "newpass";
		public static final String USERID_KEY = "userid";
	}

	/**
	 * This interface is used for contains the Registration APIs keys
	 * 
	 * @author
	 */
	public interface RegistrationAPIKeys {
		public static final String API_TYPE = "signup";
		public static final String FIRST_NAME_KEY = "fname";
		public static final String USERNAME_KEY = "username";
		public static final String PASSWORD_KEY = "password";
		public static final String EMAIL_KEY = "email";
		public static final String DEVICE_TOKEN_KEY = "device_token";
		public static final String PHOTO_KEY = "photo";
		public static final String DEVICE_TYPE_KEY = "device_type";

	}

	/**
	 * This interface is used for contains the post image APIs keys
	 * 
	 * @author
	 */
	public interface PostImageAPIKeys {
		public static final String API_TYPE = "addpost";
		public static final String USERID_KEY = "userid";
		public static final String TITLE_KEY = "title";
		public static final String DATA_TYPE_KEY = "data_type";
		public static final String HASHTAG_KEY = "hashtags";
		public static final String TAGGED_USER_KEY = "tagged_users";
		public static final String IMAGE_KEY = "image";
		public static final String DEVICE_TYPE_KEY = "device_type";
		public static final String VIDEO_PATH_KEY = "video_path";
		public static final String PLACE_BOUGHT = "placebought";
		public static final String CURRENCY = "currency";
		public static final String PRICE = "price";
		public static final String CATEGORY = "category";
		public static final String COMMENT = "comment";
		public static final String DATA_KEY = "data";
		public static final String USER_TYPE = "user_type";
	}

	/**
	 * This interface is used for contains the post image APIs keys
	 * 
	 * @author
	 */
	public interface PostImageOrAudioAPIKeys {
		public static final String API_TYPE = "addpost";
		public static final String USERID_KEY = "userid";
		public static final String TITLE_KEY = "title";
		public static final String DATA_TYPE_KEY = "data_type";
		public static final String HASHTAG_KEY = "hashtags";
		public static final String TAGGED_USER_KEY = "tagged_users";
		public static final String IMAGE_KEY = "image";
		public static final String DATA_KEY = "data";
		public static final String DEVICE_TYPE_KEY = "device_type";
		public static final String VIDEO_PATH_KEY = "video_path";

		public static final String USER_TYPE = "user_type" ;
	}

	/**
	 * This interface is used for contains the edit profile APIs keys
	 * 
	 * @author
	 */
	public interface EditProfileAPIKeys {
		public static final String API_TYPE = "edit";
		public static final String USERID_KEY = "userid";
		public static final String FIRST_NAME_KEY = "fname";
		public static final String USERNAME_KEY = "username";
		public static final String EMAIL_KEY = "email";
		public static final String USER_BIO_KEY = "user_bio";
		public static final String PHOTO_KEY = "thumb_image";
		public static final String PHONE_KEY = "phone";
		public static final String IS_PRIVATE_KEY = "is_private";

	}

	/**
	 * This interface is used for contains the Login APIs keys
	 * 
	 * @author
	 */
	public interface SetSettingAPIKeys {
		public static final String API_TYPE = "savesetting";
		public static final String USERID_KEY = "userid";
		public static final String NOTIFICATION_STATUS_KEY = "notification";
	}

	/**
	 * This interface is used for contains the get profile APIs keys
	 * 
	 * @author
	 */
	public interface GetProfileAPIKeys {
		public static final String API_TYPE = "get";
		public static final String USERID_KEY = "userid";
		public static final String FRNDID_KEY = "friendid";
		public static final String PAGE_KEY = "page";
		public static final String TYPE_KEY = "post_type";
		public static final String STR_SEARCH_KEY = "strusertype";
	}

	/**
	 * This interface is used for contains the block user APIs keys
	 * 
	 * @author
	 */
	public interface setBlockUserAPIKeys {
		public static final String API_TYPE = "blockuser";
		public static final String USERID_KEY = "userid";
		public static final String FRNDID_KEY = "other_id";
		public static final String STATUS_KEY = "status";
	}

	/**
	 * This interface is used for contains the get lookagram plan APIs keys
	 * 
	 * @author
	 */
	public interface GetLookagramPlanAPIKeys {
		public static final String API_TYPE = "allplans";
		public static final String USERID_KEY = "userid";
	}

	/**
	 * This interface is used for contains the get notification APIs keys
	 * 
	 * @author
	 */
	public interface SetNotificationStatusAPIKeys {
		public static final String API_TYPE = "setsettings";
		public static final String USERID_KEY = "userid";
		public static final String STR_TYPE_KEY = "notification_type";
		public static final String STR_VALUE_KEY = "status";
	}

	/**
	 * This interface is used for contains the get profile APIs keys
	 * 
	 * @author
	 */
	public interface GetSearchAPIKeys {
		public static final String API_TYPE = "search";
		public static final String USERID_KEY = "userid";
		public static final String SEARCH_TYPE_KEY = "search_type";
		public static final String KEYWORD_KEY = "keyword";
	}

	/**
	 * This interface is used for contains the get comment list APIs keys
	 * 
	 * @author
	 */
	public interface GetCommentListAPIKeys {
		public static final String API_TYPE = "postcomments";
		public static final String USERID_KEY = "userid";
		public static final String POSTID_KEY = "post_id";
	}

	/**
	 * This interface is used for contains the get comment list APIs keys
	 * 
	 * @author
	 */
	public interface GetLikeListAPIKeys {
		public static final String API_TYPE = "likeuserlist";
		public static final String USERID_KEY = "userid";
		public static final String POSTID_KEY = "post_id";
		public static final String PAGE_KEY = "page";
	}

	/**
	 * This interface is used for contains the get single post APIs keys
	 * 
	 * @author
	 */
	public interface GetSinglePostAPIKeys {
		public static final String API_TYPE = "getpost";
		public static final String USERID_KEY = "userid";
		public static final String POSTID_KEY = "post_id";
	}

	/**
	 * This interface is used for contains the get post comment list APIs keys
	 * 
	 * @author
	 */
	public interface GetPostCommentAPIKeys {
		public static final String API_TYPE = "comment";
		public static final String USERID_KEY = "userid";
		public static final String TAGGED_USER_KEY = "tagged_users";
		public static final String POSTID_KEY = "post_id";
		public static final String COMMENT_MSG_KEY = "comment_text";
		public static final String HASHTAG_KEY = "hashtags";
	}

	
	/**
	 * This interface is used for contains the get post comment list to be delete by user(DETETE Comments) APIs keys
	 * 
	 * @author
	 */
	public interface GetSingleCommentDeleteAPIKeys {
		public static final String API_TYPE = "DELETE";
		public static final String USERID_KEY = "userid";
		public static final String POSTID_KEY = "post_id";
		public static final String COMMENTID_KEY = "comment_id";
	}

	/**
	 * This interface is used for contains the get explore APIs keys
	 * 
	 * @author
	 */
	public interface GetNewsFeedAPIKeys {
		public static final String API_TYPE = "homepost";
		public static final String API_TYPE_IJBT = "HOMEPOSTIJBT";
		public static final String USERID_KEY = "userid";
		public static final String PAGE_KEY = "page";
	}

	/**
	 * This interface is used for contains the get explore APIs keys
	 * 
	 * @author
	 */
	public interface GetPostLikeAPIKeys {
		public static final String API_TYPE = "postlike";
		public static final String USERID_KEY = "userid";
		public static final String POST_ID_KEY = "post_id";
		public static final String STATUS_KEY = "status";
		public static final String STATUS_FAVOURITE = "fav_status";
	}

	/**
	 * This interface is used for contains the get follower APIs keys
	 * 
	 * @author
	 */
	public interface GetFollowerAPIKeys {
		public static final String API_TYPE_FOLLOWER = "userfollowers";
		public static final String API_TYPE_FOLLOWING = "userfollowing";
		public static final String USERID_KEY = "userid";
		public static final String FRNDID_KEY = "friend_id";
		public static final String PAGE_KEY = "page";
	}

	/**
	 * This interface is used for contains the get explore APIs keys
	 * 
	 * @author
	 */
	public interface GetExploreAPIKeys {
		public static final String API_TYPE = "popularposts";
		public static final String USERID_KEY = "userid";
		public static final String PAGE_KEY = "page";
	}

	/**
	 * This interface is used for contains the get hashtag post APIs keys
	 * 
	 * @author
	 */
	public interface GetHashtagPostAPIKeys {
		public static final String API_TYPE = "hashposts";
		public static final String USERID_KEY = "userid";
		public static final String HASH_KEY = "hashtag";
	}

	/**
	 * This interface is used for contains the set follow and unfollow APIs keys
	 * 
	 * @author
	 */
	public interface SetFollowUnfollowAPIKeys {
		public static final String API_TYPE = "followrequest";
		public static final String USERID_KEY = "userid";
		public static final String OTHER_ID_KEY = "other_id";
		public static final String STATUS_KEY = "status";
	}

	/**
	 * This interface is used for contains the get free user APIs keys
	 * 
	 * @author
	 */
	public interface GetFreeUserAPIKeys {
		public static final String API_TYPE = "userplanstatus";
		public static final String USERID_KEY = "userid";
	}

	/**
	 * This interface is used for contains the get timeline APIs keys
	 * 
	 * @author
	 */
	public interface GetTimelineFollowerNotificationAPIKeys {
		public static final String API_TYPE = "followersnotification";
		public static final String USERID_KEY = "userid";
		public static final String PAGE_KEY = "page";
	}

	/**
	 * This interface is used for contains the get timeline APIs keys
	 * 
	 * @author
	 */
	public interface GetTimelineMyNotificationAPIKeys {
		public static final String API_TYPE = "mynotification";
		public static final String USERID_KEY = "userid";
	}

	/**
	 * This interface is used for contains the change mobile number APIs keys
	 * 
	 * @author
	 */
	public interface ChangeMobileAPIKeys {
		public static final String API_TYPE = "changemobile";
		public static final String USERID_KEY = "userid";
		public static final String OLD_MOBILE_NUMBER_KEY = "oldnumber";
		public static final String NEW_MOBILE_NUMBER_KEY = "newnumber";
	}
	/**
	 * This interface is used for contains the get lookagram plan APIs keys
	 *
	 * @author
	 */
	public interface GetInfluencerAPIKeys {
		public static final String INFLUENCER_CODE = "code";
	}

	/**
	 * This interface is used for handling constants of web services
	 * 
	 * @author
	 * 
	 */
	public interface TaskType {
		public static final int GET_PROFILE_TASK = 1001;
		public static final int EDIT_PROFILE_TASK = 1002;
		public static final int GET_COMMENT_TASK = 1003;
		public static final int POST_COMENT_TASK = 1004;
		public static final int MYACTIVITY = 1005;
		public static final int OTHERACTIVITY = 1006;
		public static final int GETFREEUSER = 1007;
		public static final int GETNEWSFEED = 1008;
		public static final int GETOTHERUSERPROFILE = 1009;
		public static final int SETFOLLOWREQUEST = 1010;
		public static final int SETBLOCK = 1011;
		public static final int GETALLOWFOLLOW = 1012;
		public static final int SETALLOWFOLLOW = 1013;
		public static final int SETDEACTIVATE = 1014;
		public static final int LOGOUT = 1015;
		public static final int LOGIN = 1016;
		public static final int RESENDEMAIL = 1017;
		public static final int GETHASHTAGLIST = 1018;
		public static final int SETPOST = 1019;
		public static final int GETSUGGESTION = 1020;
		public static final int SETFOLLOWALL = 1021;
		public static final int Getsettings = 1022;
		public static final int SetSettings = 1023;
		public static final int FBLOGIN = 2000;
		public static final int TWITTERLOGIN = 3000;
		public static final int DETETESINGLEPOST=4000;
		public static final int GETSINGLEPOSTIMAGE=5000;
		//public static final int AUTHENTICATE_CODE = 6000;
		public static final int FETCH_CATEGORY_CURRENCY = 6001;

		public static final int GET_FAVOURITES_POST = 7000;
		public static final int GET_LIKES_POST = 7001;
	}

}