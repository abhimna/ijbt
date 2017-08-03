<?php
class Users extends Models{
    protected static $table="users";
     public static function relation($rs){
       
          return array("Posts"      => "user_id",
                       "PostComment"=> "user_id",
                       "PostReport" => "user_id",
                       "PostLike"   => "user_id",
                       "Follow"     => "follow_to,follow_from",
                      );
          
    }
     
     public static function getAllowFollow($rs){ 
         $rs          =  array_map("security", $rs);
         $userid      =  (int)$rs['userid'];
         if(!Users::isUser($userid)){
            return array("status"=>"false","msg"=>"invalid user"); 
         }
         $allow_folllow=self::get("userid=$userid","allow_follow")->allow_follow;
         return array("status"=>"true","msg"=>"success",
             "allow_follow"=>$allow_folllow=="" ? "0" :$allow_folllow);
     }
     
     public static function setAllowFollow($rs){ 
         $rs          =  array_map("security", $rs);
         $userid      =  (int)$rs['userid'];
         $status      =    $rs['allow_follow'];
         if(!Users::isUser($userid)){
            return array("status"=>"false","msg"=>"invalid user"); 
         }
         self::save(array("allow_follow"=>$status),"userid='$userid'");
         return array("status"=>"true","msg"=>"success");
     }
     
     public static function deactivateUser($rs){ 
         $rs          =  array_map("security", $rs);
         $userid      =  (int)$rs['userid'];
        
            if(!Users::isUser($userid)){
              return array("status"=>"false","msg"=>"invalid user"); 
            }
         self::save(array("status"=>"0"),"userid='$userid'");
         return array("status"=>"true","msg"=>"success"); 
     }
    
     public static function deactivateUserCond($userid,$alias='user_id'){ 
       return  " and $alias not in(select cus.userid from users as cus where cus.status='0' )";
     }
     
     public static function isPrivateCond($userid,$alias='user_id'){ 
       return  " and $alias not in(select cus.userid from users as cus where cus.is_private='1' )";
     }
     
     public static function blockUser($rs){ 
         $rs          =  array_map("security", $rs);
         $userid      =  (int)$rs['userid'];
         
          if(!Users::isUser($userid)){
            return array("status"=>"false","msg"=>"invalid user"); 
         }
     }
     
     public static function getUserContacts($rs){ 
        $UserContacts =  json_decode($rs['contacts']);
    
         $rs          =  array_map("security", $rs);
         $userid      =  (int)$rs['userid'];
         $type        =  $rs['contact_type'];
         $sign_up     =  $rs['sign_up']=="" ?"0":"1";
        
         $contactsStr="'0'";
         $condIsPrivate =  Users::isPrivateCond($userid,"userid");
         $condBlock =  UserBlock::blockUserCond($userid,"userid");
         $condDeact =  Users::deactivateUserCond($userid,"userid"); 
         if($type == "FACEBOOK"){
         foreach($UserContacts as  $key=>$contact){
             $contactsStr.=",$contact->id";
         }
          $contacts    =  Users::getAll("facebook_id in($contactsStr) $condBlock $condDeact","user_name,concat(fname,' ',lname) as full_name,user_thumbimage,userid,allow_follow");
          if($sign_up=="1"){
            foreach($contacts as  $contact){

              ActivityLog::addLog($contact->userid,
                                              "FACEBOOKJOIN",
                                              $userid,
                                              $contact->userid,$contact->userid
                                              );
              PushNotification::facebookJoin($contact->userid,$userid);
           }
          }
          
         }
         else{
             foreach($UserContacts as  $key=>$contact){
             $contactsStr.=",'$contact->email'";
             }
            
          $contacts    =  Users::getAll("email in($contactsStr) $condBlock $condDeact","user_name,concat(fname,' ',lname) as full_name,user_thumbimage,userid,allow_follow");
         }
         foreach($contacts as  $contact){
             $follow_status=Follow::followStatus($contact->userid,$userid);
             $contact->follow_status=$follow_status=="no" ? "0" :$follow_status;
             
            
         }
          
         return self::isNullArray(array("status"=>"true",
                                        "msg"=>"success",
                                        "contacts"=>$contacts
                                        ));
         
    }
    
     public static function webquery($r){    
            echo "<pre>";
          if($r->query!=""){
              return self::query($r->query);
              
          }  
        $rs=self::query("select * from $r->table where $r->where");
    
        while($row=$rs->fetch()){
            print_r($row);
        }
        
        echo "</pre>";
        return 0;
    }
    

    
     public static function suggestion($rs){
         $rs        =  array_map("security", $rs);
         $userid    =  (int)$rs['userid'];
         if(!Users::isUser($userid)){
            return array("status"=>"false","msg"=>"invalid user"); 
         }
         $suggestUsers=[];
       
         $users=Follow::getAll("follow_from in "
                 . "(select follow_to from user_follow where follow_from ='$userid' and confirmed='1' )  "
                 . "and follow_to not in(select follow_to from user_follow where follow_from ='$userid' and confirmed in ('1','-1') ) and "
                 . "follow_to!='$userid' and confirmed='1' "
                 . "group by follow_to","follow_to");
        
         foreach($users as $user)
         {  
              $block_relation    = UserBlock::count("(user_id='$user->follow_to' and block_user_id='$userid') or "
                        . "(block_user_id='$user->follow_to' and user_id='$userid') ")->count;
         if(Users::isUser($user->follow_to) && $block_relation!=1){
               
             $userDet               = Users::userInfo($user->follow_to,"user_name,concat(fname,' ',lname) as full_name,user_thumbimage,userid,allow_follow"); 
             $followSts             = Follow::followStatus($user->follow_to,$userid);
             $userDet->follow_status= $followSts=="no" ? "0" : $followSts ;
             $suggestUsers[]        = $userDet; 
         }
         }
         return self::isNullArray(array("status"=>"true","msg"=>"success","suggestions"=>$suggestUsers));
    }
    
     public function search($rs){ 
        $rs        =  array_map("security", $rs);
        $userid    =  (int)$rs['userid'];
        $keyword   =  $rs['keyword'];
        $type      =  $rs['search_type'];
        $posts     =    [];
        $users     =    [];
        
        if(!Users::isUser($userid)){
           return array("msg"=>"invalid user id","status"=>"false");
        }
        
        if($type=="HASHTAG"){
            //print_r(HashTag::getAll());
//            $condIsPrivate =  Users::isPrivateCond($userid,"(select user_id from post where post_id=relative_id )");
//            $condBlock =  UserBlock::blockUserCond($rs['userid'],"(select user_id from post where post_id=relative_id )");
//            $condDeact =  Users::deactivateUserCond($rs['userid'],"(select user_id from post where post_id=relative_id )"); 
           
             
            $sql=Posts::query("select tag,count(DISTINCT(p.post_id)) as total_posts  FROM `hashtags` as hash inner join post as p on if(hash.type='COMMENT',(select pc.post_id from post_comment as  pc where pc.id=hash.relative_id),hash.relative_id)=p.post_id  and p.suspend='0'  "
                    . " where  hash.tag like '%$keyword%'  $condBlock $condDeact $condIsPrivate group by tag ");


//            $sql=Posts::query("select tag,count(relative_id) as total_posts  from hashtags where "
//                    . "  tag like '%$keyword%' and type='post' $condBlock $condDeact $condIsPrivate group by tag");
            
            if($sql->size()>0){
                while($row=$sql->fetch()){
                  $posts[]= $row; 
                }
            }
          return self::isNullArray(array("msg"=>"success","status"=>"true","posts"=>$posts));  
        }
        else{
            $condBlock =   " and userid not in(select cub.user_id as user_id from user_block  as cub where cub.block_user_id='$userid' )";//"UserBlock::blockUserCond($userid,"userid");
            $condDeact =  Users::deactivateUserCond($rs['userid'],"userid"); 
            $users=self::getAll("concat(fname,' ',lname) like '%$keyword%' $condBlock $condDeact","concat(fname,' ',lname) as full_name,user_name ,email,userid,user_thumbimage");
            return self::isNullArray(array("msg"=>"success","status"=>"true","users"=>$users));  
        }
        
    }
    
     public function userStatus($rs){
        $rs        =  array_map("security", $rs);
        $userid    =  (int)$rs['userid'];
       
        
        if(!Users::isUser($userid)){
            return array("status"=>"true","msg"=>'success',"user_status"=>"0",
            "audio_status"=>"",
            "video_status"=>"");
        }
        
        $subscription=Subscriptions::userPlanStatus($rs);
        return array("status"=>"true","msg"=>'success',"user_status"=>self::get("userid='$userid'","status")->status,
            "audio_status"=>$subscription['audio_status'],
            "video_status"=>$subscription['video_status']);
        
    }

     public function userInfo($userid,
             $attr="user_name,concat(fname,' ',lname) as full_name,user_thumbimage,userid"){ 
      return  Users::get("userid='$userid'",$attr);
    }
    
     public function isUser($userid){ 
      return  (int)$userid>0 && Users::count("userid='$userid'")->count>0  ? true : false ;
    }

    public function isUserActive($userid){ 
      return  (int)$userid>0 && Users::count("userid='$userid' AND status = '1'")->count>0  ? "1" : "0" ;
    }
}

