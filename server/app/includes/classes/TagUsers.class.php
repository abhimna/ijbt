<?php
class TagUsers extends Models{
    protected static $table="tagged_users";
    
     public static function addUserTags($tags,$relative_id,$type="post"){
         
         if(count($tags)>0){
             foreach($tags as $users){
                 
                 self::save(array("relative_id"=>$relative_id,
                         "type"=>$type,
                         "user_id"=>Users::get("user_name='$users'","userid")->userid,
                         "tag"=>"$users",
                       "dtdate"=>date("Y-m-d H:i:s",time())));
             }
         }
     }
  
}

