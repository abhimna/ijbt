<?php

class PostRate extends Models {

    protected static $table = "post_rate";

    public static function likeUsersList($rs) {
        $rs = array_map("security", $rs);
        $userid = (int) $rs['userid'];
        $postid = (int) $rs['post_id'];
        $users = [];

        if (!Users::isUser($userid) || !Posts::isPost($postid))
            return array("status" => "false", "msg" => "invalid credentials");
        $condBlock = UserBlock::blockUserCond($userid);
        $condDeact = Users::deactivateUserCond($userid);
        $pager = Utility::makePager($rs['page'], self::count("post_id=$postid")->count);
        $allLikes = self::getAll("post_id=$postid  $condBlock $condDeact order by dtdate DESC  limit $pager->lowerLimit,$pager->upperLimit", 'user_id');
        foreach ($allLikes as $like)
            $users[] = Users::get("userid='$like->user_id' order by dtdate DESC", "userid,user_thumbimage,concat(fname,' ',lname) as full_name,user_name");

        return self::isNullArray(array("users" => $users,
                    "total_records" => $pager->total_records,
                    "total_page" => $pager->total_page,
                    "status" => "true",
                    "msg" => "success"));
    }

    public static function rate($rs) {
        $rs = array_map("security", $rs);
        $userid = (int) $rs['userid'];
        $postid = (int) $rs['post_id'];
        $rate = (int) $rs['rate'];
        $condBlock = UserBlock::blockUserCond($userid);
        $condDeact = Users::deactivateUserCond($userid);
        if (Users::count("userid=$userid")->count > 0 && Posts::isPost($postid)) {
            
                   
                if (self::count("user_id='$userid' and post_id='$postid'")->count <= 0) {
                    $insId = self::save(array("post_id" => $postid,
                                "user_id" => $userid,
                                "rate" => $rate,
                                "dtdate" => date("Y-m-d H:i:s", time())));
                    $postuser = Posts::get("post_id='$postid'", "user_id")->user_id;
                    ActivityLog::addLog($insId, "RATEPOST", $userid, $postuser, $postid
                    );
                     if ($postuser != $userid) {
                    PushNotification::ratePost($postuser, $postid);
                     }
                }
                $arr = array("msg" => "Post rated successfully",
                    "status" => "true",
                    "rate" => $rate,
                    "average_rate" => self::average_rate($postid),
                    "is_rate" => PostRate::count("post_id=$postid and user_id='$userid' ")->count);
            

            return self::isNullArray($arr);
        } else {
            return array("msg" => "invalid user id", "status" => "false");
        }
    }
    
    
    public static function average_rate($post_id) {

        global $db;

        $query = $db->query("SELECT rating FROM post WHERE post_id = '" . $post_id . "'");
        if ($query->size() > 0) {
            $tuples = $query->fetch();

            return floor($tuples['rating']);
        }
    }

}
