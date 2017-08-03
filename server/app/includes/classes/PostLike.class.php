<?php

class PostLike extends Models {

    protected static $table = "post_like";

    public static function likeUsersList($rs) {
        $rs = array_map("security", $rs);
        $userid = (int) $rs['userid'];
        $postid = (int) $rs['post_id'];
        $users = [];

        if (!Users::isUser($userid) || !Posts::isPost($postid))
            return array("status" => "false", "msg" => "invalid credentials");
        $condBlock = UserBlock::blockUserCond($userid);
        $condDeact = Users::deactivateUserCond($userid);
        $pager = Utility::makePager($rs['page'], self::count(" like_staus = '1' AND post_id=$postid")->count);
        $allLikes = self::getAll("post_id=$postid AND like_status = '1'  $condBlock $condDeact order by dtdate DESC  limit $pager->lowerLimit,$pager->upperLimit", 'user_id');
        foreach ($allLikes as $like)
            $users[] = Users::get("userid='$like->user_id'  order by dtdate DESC", "userid,user_thumbimage,concat(fname,' ',lname) as full_name,user_name");

        return self::isNullArray(array("users" => $users,
                    "total_records" => $pager->total_records,
                    "total_page" => $pager->total_page,
                    "status" => "true",
                    "msg" => "success"));
    }

    public static function DislikeUsersList($rs) {
        $rs = array_map("security", $rs);
        $userid = (int) $rs['userid'];
        $postid = (int) $rs['post_id'];
        $users = [];

        if (!Users::isUser($userid) || !Posts::isPost($postid))
            return array("status" => "false", "msg" => "invalid credentials");
        $condBlock = UserBlock::blockUserCond($userid);
        $condDeact = Users::deactivateUserCond($userid);
        $pager = Utility::makePager($rs['page'], self::count("like_staus = '-1' AND post_id=$postid")->count);
        $allLikes = self::getAll("post_id=$postid AND like_status = '-1' $condBlock $condDeact order by dtdate DESC  limit $pager->lowerLimit,$pager->upperLimit", 'user_id');
        foreach ($allLikes as $like)
            $users[] = Users::get("userid='$like->user_id' order by dtdate DESC", "userid,user_thumbimage,concat(fname,' ',lname) as full_name,user_name");

        return self::isNullArray(array("users" => $users,
                    "total_records" => $pager->total_records,
                    "total_page" => $pager->total_page,
                    "status" => "true",
                    "msg" => "success"));
    }

    public static function like($rs) {
        $rs = array_map("security", $rs);
        $userid = (int) $rs['userid'];
        $postid = (int) $rs['post_id'];
$host = 'localhost';
$user = 'ijbt_app';
$pass = 'Pluggdd@2016';
$dbname = 'ijbt_post';
$connectionstring=mysqli_connect($host,$user,$pass,$dbname);
$sql_fav=mysqli_query($connectionstring,"SELECT * FROM  `post_like`  where post_id=$postid and user_id=$userid order by id desc Limit 1");
		if(!$sql_fav)
			echo mysqli_error($connectionstring);
		$favourate=mysqli_fetch_assoc($sql_fav);
		if(isset($rs['status']))
        $like_status = (int) $rs['status'];
		else
		{
			if(($favourate['like_status']=='1'))
				$like_status = 1;
			else if(($favourate['like_status']=='-1'))
				$like_status = -1;
			else
				$like_status = 0;
			// $like_status = 0;
		}
		 if(isset($rs['fav_status']))
		$fav_status=(int) $rs['fav_status'];
	else
		{
			$fav_status = ($favourate['fav_status']=='1')?1:0;
			//$fav_status=0;
		}
        $condBlock = UserBlock::blockUserCond($userid);
        $condDeact = Users::deactivateUserCond($userid);
        if (Users::count("userid=$userid")->count > 0 && Posts::isPost($postid)) {
            if ($like_status == "1") {

                $likeid = self::get("user_id=$userid and post_id=$postid", "id")->id;
                ActivityLog::removeLog($likeid, "DISLIKEPOST", $userid);
                self::remove("user_id=$userid and post_id=$postid");
                if (self::count("user_id='$userid' and post_id='$postid'")->count <= 0) {
                    $insId = self::save(array("post_id" => $postid,
                                "user_id" => $userid,
                                "like_status" => "1",
								"fav_status" => $fav_status,
                                "dtdate" => date("Y-m-d H:i:s", time())));
                    $postuser = Posts::get("post_id='$postid'", "user_id")->user_id;
                    ActivityLog::addLog($insId, "LIKEPOST", $userid, $postuser, $postid
                    );
                    if ($postuser != $userid) {
                        PushNotification::likePost($postuser, $postid);
                    }
                }
                $arr = array("msg" => "Post liked succesfully",
                    "status" => "true",
                    "like_status" => "1",
					"fav_status" => $fav_status,
                    "total_votes" => self::count("post_id=$postid AND like_status = '1'   $condDeact $condBlock")->count,
                    "total_unlike" => self::count("post_id=$postid AND like_status = '-1'   $condDeact $condBlock")->count,
                    "is_vote" => self::count(" post_id=$postid  AND user_id=$userid")->count);
            } else if ($like_status == "-1") {

                $likeid = self::get("user_id=$userid and post_id=$postid", "id")->id;
                ActivityLog::removeLog($likeid, "LIKEPOST", $userid);
                self::remove("user_id=$userid and post_id=$postid");
                if (self::count("user_id='$userid' and post_id='$postid'")->count <= 0) {
                    $insId = self::save(array("post_id" => $postid,
                                "user_id" => $userid,
                                "like_status" => "-1",
								"fav_status" => $fav_status,
                                "dtdate" => date("Y-m-d H:i:s", time())));
                    $postuser = Posts::get("post_id='$postid'", "user_id")->user_id;
                    ActivityLog::addLog($insId, "DISLIKEPOST", $userid, $postuser, $postid
                    );
                    if ($postuser != $userid) {
                    PushNotification::DislikePost($postuser, $postid);
                    }
                }
                $arr = array("msg" => "Post disliked succesfully",
                    "status" => "true",
                    "like_status" => "-1",
					"fav_status" => $fav_status,
                    "total_votes" => self::count("post_id=$postid AND like_status = '1'   $condDeact $condBlock")->count,
                    "total_unlike" => self::count("post_id=$postid AND like_status = '-1'   $condDeact $condBlock")->count,
                    "is_vote" => self::count(" post_id=$postid  AND user_id=$userid")->count);
            } else {
                $likeid = self::get("user_id=$userid and post_id=$postid", "id")->id;
                $insId = self::save(array("post_id" => $postid,
                                "user_id" => $userid,
"like_status" => $like_status,
								"fav_status" => $fav_status,
                                "dtdate" => date("Y-m-d H:i:s", time())));
                $arr = array("msg" => "Succesfully Updated",
                    "status" => "true",
                    "like_status" => $like_status,
					"Post_ID" => $postid,
					"fav_status" => $fav_status,
                    "total_votes" => self::count("post_id=$postid AND like_status = '1'   $condDeact $condBlock")->count,
                    "total_unlike" => self::count("post_id=$postid AND like_status = '-1'   $condDeact $condBlock")->count,
                    "is_vote" => self::count(" post_id=$postid  AND user_id=$userid")->count);
                ActivityLog::removeLog($likeid, "LIKEPOST", $userid);
                ActivityLog::removeLog($likeid, "DISLIKEPOST", $userid);
            }

            return self::isNullArray($arr);
        } else {
            return array("msg" => "invalid user id", "status" => "false");
        }
    }

}
