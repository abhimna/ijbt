<?php
class HashTag extends Models{
    protected static $table="hashtags";
    
     public static function addHashTags($tags,$relative_id,$type="post"){
         if(count($tags)>0){
             foreach($tags as $hashtag){
                 self::save(array("relative_id"=>$relative_id,
                                    "type"=>$type,
                                    "tag"=>"$hashtag",
                                    "dtdate"=>date("Y-m-d h:i:s",time())));
             }
         }
     }
  
}

