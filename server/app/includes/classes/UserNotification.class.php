<?php
class UserNotification extends Models{
    protected static $table="notification_user";
    
    public function addLog($relative_id,$type,$userid,$notify_to="FOLLOWERS",$main_id="0"){
        $activity=ActivityLog::getAll("relative_id=$relative_id and type='$type'","activity_user");
        
       if(self::count("relative_id=$relative_id and type='$type'")->count<=0){
        $data= array("relative_id"=>$relative_id,
            "type"=>$type,
            "main_id"=>$main_id,
            "activity_user"=>$userid,
            "notify_to"=>$notify_to,"user_message"=>"");
        $data['dtdate']=date("Y-m-d H:i:s",time());
        self::save($data);
       }
       else{
           $already=$userid.",".self::get("relative_id=$relative_id and type='$type'","user_message")->user_message;
           $data= array("user_message"=>$already,"dtdate"=>  nowDateTime());
           self::save($data,"relative_id=$relative_id and type='$type'");
       }
       
    }
    
    public function createMessage($notification,$userid){
                
                $notifyTo                   = Users::userInfo($notification->notify_to);
                $you                        = $notification->type=="LIKEPOST" || $notification->type=="COMMENTPOST"  ?"your" : "you";
                $notificationUsers          = self::notificationUsers($notification->all_users);
                $notification->users    = $notification->all_users;
                
                $notification->all_users    = $notificationUsers['allusers'];
                if(count($notificationUsers['allusers'])<=1){
                    if($notification->notify_to==$notificationUsers['allusers'][0]['userid'])
                        $on_user="own";
                    else
                         $on_user=$notifyTo->user_name;
                }
                else{
                        $on_user=$notifyTo->user_name;
                }
                $notification->on_user      = $userid ==$notification->notify_to ? $you : $on_user;
                $notification->message      = $notificationUsers['text'];
                $notification->time_ago     = Utility::getTimeAgo($notification->dtdate);
                return $notification;
    }
    public function notificationUsers($users) {

        $users = explode(",", $users);
        $notUsers = array();
        $allusers = array();
        $counter = 0;
        if (count($users) > 0) {
            foreach ($users as $userid) {
                if (!in_array($userid, $allusers)) {
                    $allusers[] = $userid;
                    $notUsers[$counter]['userid'] = $userid;
                    $notUsers[$counter] = (array)Users::userInfo($userid) ;
                     if (count($allusers) == 1)
                        $rs .= $notUsers[$counter]['user_name'];
                    else if (count($allusers) == 2)
                        $rs.= " and " .  $notUsers[$counter]['user_name'];
                    else {
			    $rs = str_replace(" and ", ", ", $rs);
                        $others = (count($allusers) - 2);
                        $rs1 = $others > 1 ? " and $others others" : " and $others other";
		          $notUsers[$counter]['full_name'] = str_replace(" and ", "", $rs1);
                    }
                    $counter++;
                }
            }
	     $rs = $rs;
            $rs = str_replace("And", "and", $rs);
            $rs = $rs . $rs1;
        }
        return array("text" => $rs, "allusers" => $notUsers);
    }
    public function notificationData($allNotifications,$userid){
        $newNotification=[];
        foreach($allNotifications as $notification){
             $notification               = self::createMessage($notification,$userid);
             if($notification->type=="FOLLOW"){
                
                $notification->message      = "$notification->message started following $notification->on_user.";
             }
             elseif($notification->type=="UNFOLLOW"){
                $notification->message      = "$notification->message stopped following $notification->on_user.";
             }
             elseif($notification->type=="LIKEPOST"){
                $post_id                    = PostLike::get("id=$notification->relative_id","post_id")->post_id; 
                $notification->post_detail  = Posts::get("post_id=$post_id","post_id,thumb_image,title"); ;
                $notification->message      = "$notification->message liked $notification->on_user post:'". Utility::stringWithDot($notification->post_detail->title)."'";
                }
             elseif($notification->type=="COMMENTPOST"){
                 
                $comment                    = PostComment::get("id=$notification->relative_id","post_id,comment");
                $notification->post_detail  = Posts::get("post_id=$comment->post_id","post_id,thumb_image,title"); ;
                $notification->message      = "$notification->message commented on $notification->on_user post:'".Utility::stringWithDot($comment->comment)."'";
               }
               $newNotification[]=$notification;
         }
        return $newNotification;
    }
    public function myNotifications($rs){
        header('Content-Type: application/json; Charset=UTF-8');
        $rs        =  array_map("security", $rs);
        
        $userid    =  (int)$rs['userid'];
        $userid = 15;
         if(!Users::isUser($userid)){
            return array("msg"=>"invalid user id","status"=>"false");
         }
         $allNotifications=self::getall("notify_to='$userid' and activity_user!='$userid' group by main_id,type order by dtdate DESC",
                 "activity_log.*,group_concat(activity_user) as all_users ");
         $allNotifications=self::notificationData($allNotifications,$userid);
         return self::isNullArray(array("status"=>"true",
                                "msg"=>"success","notifications"=>$allNotifications,
             "followers"=>self::followersNotifications($rs)));
        
    }
     public function followersNotifications($rs){
        $rs        =  array_map("security", $rs);
        $userid    =  (int)$rs['userid'];
         if(!Users::isUser($userid)){
            return array("msg"=>"invalid user id","status"=>"false");
         }
         $allNotifications=self::getall("notify_to in "
                 . "(select follow_to from user_follow where follow_from=$userid and confirmed='1') "
                 . "and type!='COMMENTPOST' and activity_user!=$userid group by main_id,type order by dtdate DESC",
                 "activity_log.*,MAX(activity_log.dtdate) as  dtdate,group_concat(activity_user) as all_users ");
         
         $allNotifications=self::notificationData($allNotifications,$userid);
         return $allNotifications;
        
    }
    public function removeLog($relative_id,$type,$userid){
        self::remove("relative_id=$relative_id and  type='$type' and activity_user=$userid");
    }
   
}

