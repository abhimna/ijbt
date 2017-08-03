<?php

class Follow extends Models {

    protected static $table = "user_follow";

    public function followStatus($userid, $followid) {    // param(follow_to, follow_from)
        $followStatus = Follow::get(self::followCondition($userid, $followid), 'confirmed');
        return count($followStatus) <= 0 ? "no" : $followStatus->confirmed;
    }

    public function followCondition($userid, $followto) {
        return "(follow_from='$followto' and follow_to='$userid')";
    }

    public static function followall($rs) {

        $Users = json_decode($rs['users']);

        $rs = array_map("security", $rs);
        $userid = (int) $rs['userid'];
        $newUsers = [];

        if (!Users::isUser($userid)) {
            return array("msg" => "invalid user id", "status" => "false");
        }
        foreach ($Users as $key => $user) {
            $status = Users::userInfo($user->userid, "allow_follow")
                    ->allow_follow == "1" ? "-1" : "1";
            $user->follow_status = $status;
            $newUsers[] = $user;
            $confirmSta = self::count("(follow_from='$userid' and follow_to='$user->userid') and confirmed!=0")->count;

//            if($confirmSta<=0){
            $arr = array("userid" => $userid,
                "other_id" => $user->userid,
                "status" => "$status");
            $result = self::followRequest($arr);
//            }
        }
        return self::isNullArray(array("status" => "true",
                    "msg" => "success",
                    "users" => $newUsers)
        );
    }

    public function userConnectionList($userid, $type = "STR") {


        $usertList = self::getAll("(follow_to='$userid' or follow_from='$userid'  )   and confirmed='1'", "follow_from,follow_to");

        foreach ($usertList as $user) {

            if (!in_array($user->follow_from, $allusercon))
                $allusercon[] = $user->follow_from;
            if (!in_array($user->follow_to, $allusercon))
                $allusercon[] = $user->follow_to;
        }
        if ($type == "STR") {
            if (count($allusercon) > 0)
                return implode(",", $allusercon);
            else
                return "$userid";
        } else
            return $allusercon;
    }

    public function userConnections($rs) {

        $rs = array_map("security", $rs);
        $userid = (int) $rs['userid'];
        $allUsers = array();
        if (!Users::isUser($userid)) {
            return array("msg" => "invalid user id", "status" => "false");
        }

        $usertList = self::getAll("(follow_to='$userid' or follow_from='$userid'  )   and confirmed='1'", "follow_from,follow_to");

        foreach ($usertList as $user) {

            if (!in_array($user->follow_from, $allusercon) && $user->follow_from != $userid)
                $allusercon[] = $user->follow_from;
            if (!in_array($user->follow_to, $allusercon) && $user->follow_to != $userid)
                $allusercon[] = $user->follow_to;
        }

        foreach ($allusercon as $request) {
            $user = Users::userInfo($request, "user_name,userid,user_thumbimage,allow_follow");
            $allUsers[] = $user;
        }
        return self::isNullArray(array("status" => "true",
                    "msg" => "success",
                    "user_list" => $allUsers,
        ));
    }

    public function userFollowers($rs) {

        $rs = array_map("security", $rs);
        $userid = (int) $rs['userid'];
        $friendid = (int) $rs['friend_id'];
        $page = (int) $rs['page'] == 0 ? 1 : (int) $rs['page'];
        $pageLower = ($page - 1) * 15;
        $allUsers = array();
        $pageUpper = 15;

        if ($userid <= 0 || !Users::isUser($userid)) {
            return array("msg" => "invalid user id", "status" => "false");
        }
        if ($userid <= 0 || !Users::deactivateUserCond($userid)) {
            return array("msg" => "user deactivate", "status" => "false");
        }
        if ($friendid <= 0 || !Users::isUser($friendid)) {
            return array("msg" => "invalid friend id", "status" => "false");
        }
        $followerscount = self::count("(follow_to='$friendid' ) and confirmed='1'")->count;
        $total_page = ceil($followerscount / 15);
         $condBlock = UserBlock::blockUserCond($rs['userid'],"follow_from");
        $condDeact = Users::deactivateUserCond($rs['userid'],"follow_from");
        
        $requestList = self::getAll("follow_to='$friendid'  and confirmed='1' $condBlock $condDeact limit  $pageLower,$pageUpper", "follow_from,follow_to");

        foreach ($requestList as $request) {
            $user = Users::userInfo($request->follow_from, "user_name, concat(fname,' ',lname) as full_name,userid,user_thumbimage,allow_follow");
            $block_relation = UserBlock::count("(user_id='$request->follow_from' and block_user_id='$userid') or "
                            . "(block_user_id='$request->follow_from' and user_id='$userid') ")->count > 0 ? "1" : "0";
            $isfollow = self::followStatus($request->follow_from, $userid);
            $user->is_following = $isfollow == "no" ? "0" : $isfollow;
            $user->block_relation = $block_relation;
            $allUsers[] = $user;
        }
        return self::isNullArray(array("status" => "true",
                    "msg" => "success",
                    "followers_list" => $allUsers,
                    "total_page" => $total_page,
                    "total_records" => $followerscount));
    }

    public function userFollowings($rs) {
        $rs = array_map("security", $rs);
        $userid = (int) $rs['userid'];
        $friendid = (int) $rs['friend_id'];
        $allUsers = array();
        $page = (int) $rs['page'] == 0 ? 1 : (int) $rs['page'];
        $pageLower = ($page - 1) * 15;
        $pageUpper = 15;
        if ($userid <= 0 || !Users::isUser($userid)) {
            return array("msg" => "invalid user id", "status" => "false");
        }
        if ($friendid <= 0 || !Users::isUser($friendid)) {
            return array("msg" => "invalid friend id", "status" => "false");
        }
        $followingcount = self::count("(follow_from='$friendid' ) and confirmed='1'")->count;
        $total_page = ceil($followingcount / 15);
       
        $condBlock = UserBlock::blockUserCond($rs['userid'],"follow_to");
        $condDeact = Users::deactivateUserCond($rs['userid'],"follow_to");

        $requestList = self::getAll("(follow_from='$friendid' ) and confirmed='1'$condBlock $condDeact limit $pageLower,$pageUpper", "follow_from,follow_to");

        foreach ($requestList as $request) {


            $user = Users::userInfo($request->follow_to, "user_name,concat(fname,' ',lname) as full_name,userid,user_thumbimage,allow_follow");
            


            $block_relation = UserBlock::count("(user_id='$request->follow_to' and block_user_id='$userid') or "
                            . "(block_user_id='$request->follow_to' and user_id='$userid') ")->count;
            $isfollow = self::followStatus($request->follow_to, $userid);

            $user->is_following = $isfollow == "no" ? "0" : $isfollow;
            $user->block_relation = $block_relation;

            $allUsers[] = $user;
        }
        return self::isNullArray(array("status" => "true",
                    "msg" => "success",
                    "followers_list" => $allUsers,
                    "total_page" => $total_page,
                    "total_records" => $followingcount));
    }

    public function requestAction($rs) {
        $rs = array_map("security", $rs);
        $userid = (int) $rs['userid'];
        $followto = (int) $rs['other_id'];
        $isFriend = self::followStatus($userid, $followto);
        $arr = array("status" => "false", "msg" => "invalid request");
        if ($isFriend == "-1") {
            self::save(array("confirmed" => $rs['status']), "(follow_to='$userid' and follow_from='$followto') ");
            $arr = array("status" => "true", "msg" => "success");
            if ($rs['status'] == "1") {
                $insId = self::get("(follow_to='$userid' and follow_from='$followto') ", "id")->id;
                ActivityLog::addLog($insId, "FOLLOW", $followto, $userid, $userid
                );
            }
        }

        return $arr;
    }

    public function followRequest($rs) {
        $rs = array_map("security", $rs);
        $userid = (int) $rs['userid'];
        $followto = (int) $rs['other_id'];
        $status = $rs['status'];

        if (!$userid > 0 && !Users::isUser($userid)) {
            return array("msg" => "invalid user id", "status" => "false");
        }
        if (!$followto > 0 && !Users::isUser($followto)) {
            return array("msg" => "invalid other id", "status" => "false");
        }

        $isFriend = self::followStatus($followto, $userid);

        if (self::count("(follow_from='$userid' and follow_to='$followto')")->count <= 0) {

            self::save(array("confirmed" => "$status",
                "follow_from" => $userid,
                "follow_to" => $followto, "dtdate" => nowDateTime()));
        } else {

            self::save(array("confirmed" => "$status", "dtdate" => nowDateTime()), "(follow_from='$userid' and follow_to='$followto') ");
        }
        if ($status == "1") {
            $insId = self::get("(follow_from='$userid' and follow_to='$followto')", "id")->id;
            ActivityLog::addLog($insId, "FOLLOW", $userid, $followto, $followto
            );
            PushNotification::follow($userid, $followto);
        }
        if ($status == "0" && $isFriend == "1") {
            $insId = self::get("(follow_from='$userid' and follow_to='$followto')", "id")->id;
            ActivityLog::addLog($insId, "UNFOLLOW", $userid, $followto, $followto
            );
        }

        $arr = array("status" => "true", "msg" => "success",
            "follower_count" => Follow::count("(follow_to='$followto' ) and confirmed='1'")->count,
            "following_count" => Follow::count("(follow_from='$followto' ) and confirmed='1'")->count);
        return self::isNullArray($arr);
    }

    public function requestList($rs) {
        $rs = array_map("security", $rs);
        $userid = (int) $rs['userid'];
        $allUsers = array();
        if (!$userid > 0 && !Users::isUser($userid)) {
            return array("msg" => "invalid user id", "status" => "false");
        }
        $requestList = self::getAll("(follow_to='$userid' ) and confirmed='-1'", "follow_from");

        if (count($requestList)) {
            foreach ($requestList as $request) {
                if (Users::isUser($request->follow_from))
                    $allUsers[] = Users::userInfo($request->follow_from, "user_name,userid,user_thumbimage,allow_follow");
            }
        }
        return self::isNullArray(array("status" => "true",
                    "msg" => "success",
                    "request_list" => $allUsers));
    }

}
