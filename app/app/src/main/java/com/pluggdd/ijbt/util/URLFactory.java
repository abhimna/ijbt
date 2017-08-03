package com.pluggdd.ijbt.util;

/**
 * class is used for managing URLs for each and every service
 *
 * @author
 */
public class URLFactory {
    //Base URL
    public static String baseUrl = "http://ijbt.in/ijbt/app/services/";
    public static String imageUrl = "http://ijbt.in/ijbt/app/";

    public static String GetSuggestionUrl() {
        return baseUrl + "ws-user.php?type=SUGGESTION";
    }

    public static String getContactUrl() {
        return baseUrl + "ws-user.php?type=GETUSERCONTACTS";
    }

    public static String postImageUrl() {
        return baseUrl + "ws-post.php?type=ADDPOST";
    }

    public static String SetAllFollowUrl() {return baseUrl + "ws-follow.php?type=FOLLOWALL";  }

    public static String getEditProfileUrl() {
        return baseUrl + "ws-user.php?type=EDIT";
    }

    public static String GetProfileUrl() {
        return baseUrl + "ws-user.php?type=GET";
    }

    public static String GetFavouritesPostUrl() {
        return baseUrl + "ws-post.php?type=HOMEPOSTFAV";
    }

    public static String GetLikesPostUrl() {
        return baseUrl + "ws-post.php?type=HOMEPOSTLIKE";
    }

    public static String SetBlockUserUrl() {
        return baseUrl + "ws-user.php?type=BLOCKUSER";
    }

    public static String DeactivateUserUrl() {
        return baseUrl + "ws-user.php?type=DEACTIVATEUSER";
    }

    public static String LogoutUserUrl() {
        return baseUrl + "ws-user.php?type=LOGOUT";
    }

    public static String DeleteSinglePostUrl() {
        return baseUrl + "ws-post.php?type=DELETEPOST";
    }

    public static String ReportOnSinglePostUrl() {
        return baseUrl + "ws-post.php?type=REPORTPOST";
    }

    public static String SetFollowRequestUrl() {
        return baseUrl + "ws-follow.php?type=FOLLOWREQUEST";
    }

    public static String GetCardNewsFeedUrl() {
        return baseUrl + "ws-post.php?type=HOMEPOSTIJBT";
    }

    public static String GetPostLikeUrl() {
        return baseUrl + "ws-post.php?type=POSTLIKE";
    }

    public static String GetPostDeleteUrl() {
        return baseUrl + "ws-post.php?type=DELETEPOST";
    }

    public static String GetFollowerUrl() {
        return baseUrl + "ws-follow.php?type=USERFOLLOWERS";
    }

    public static String GetFollowingUrl() {
        return baseUrl + "ws-follow.php?type=USERFOLLOWING";
    }

    public static String GetTimelineFollowersNotificationUrl() {
        return baseUrl + "ws-user.php?type=FOLLOWERSNOTIFICATION";
    }

    public static String GetTimelineMyNotificationUrl() {
        return baseUrl + "ws-user.php?type=MYNOTIFICATION";
    }

    public static String getFBSignUpUrl() {
        return baseUrl + "ws-user.php?type=FACEBOOKLOGIN";
    }
}
