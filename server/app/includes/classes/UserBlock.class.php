<?php

class UserBlock extends Models{
    protected static $table="user_block";
    
    public static function blockUser($rs){ 
         $rs          =  array_map("security", $rs);
         $userid      =  (int)$rs['userid'];
         $otherid     =  (int)$rs['other_id'];
         $status      =  (int)$rs['status'];
          if(!Users::isUser($userid)){
            return array("status"=>"false","msg"=>"invalid user"); 
         }
         if($status=="0"){
             $insId=self::remove("user_id=$userid and block_user_id=$otherid");
             return array("status"=>"true","msg"=>"User unblocked successfully");
         }
         else{
         Follow::remove("(follow_from=$userid and follow_to=$otherid) or (follow_from=$otherid and follow_to=$userid)");
         if(self::count("(user_id=$userid and block_user_id=$otherid) or (user_id=$otherid and block_user_id=$userid)")->count<=0){
         $insId=self::save(array("user_id"=>$userid,
             "block_user_id"=>$otherid,"dtdate"=> nowDateTime()));
         }
         if($insId){
         return array("status"=>"true","msg"=>"User blocked successfully");
         }
         }
         
     }
     public static function blockUserCond($userid,$alias="user_id"){ 
         
       return  " and $alias not in(select IF(cub.user_id=$userid,cub.block_user_id,cub.user_id) as user_id from user_block  as cub where cub.block_user_id='$userid' or cub.user_id='$userid')";
     }
}
