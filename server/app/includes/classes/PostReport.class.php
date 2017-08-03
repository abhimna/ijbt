<?php
class PostReport extends Models{
    protected static $table="post_report";
    
    public static function makeReport($rs){
          $rs=  array_map("security", $rs);
          if(!Users::isUser($rs['userid']) || !Posts::isPost($rs['post_id']))
            return array("status"=>"false","msg"=>"invalid credentials");
          if(self::count("user_id='".$rs['userid']."' and post_id='".$rs['post_id']."'")->count<=0){
            
              self::save(array("user_id"=>$rs['userid'],
                          "post_id"=>$rs['post_id'],"report_txt"=>$rs['text'],"dtdate"=>  nowDateTime()));
              return array("status"=>"true","msg"=>"Post reported successfully");
          
         }
          else
            return array("status"=>"false","msg"=>"You have already reported for this post");
              
        
    }
  
}

