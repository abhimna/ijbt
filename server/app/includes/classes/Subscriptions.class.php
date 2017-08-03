<?php
class Subscriptions extends Models{
    protected static $table="subscriptions";
    
    function addSubscriber($rs){
        $rs=  array_map("security", $rs);
         $user=$rs['userid'];
         $type=$rs['plan_type'];
         $plan=$rs['plan_id'];
         
         if(!Users::isUser($rs['userid']) )
            return array("status"=>"false","msg"=>"invalid credentials");
       //  $planid=Plans::get("identifier='$plan'")->id;
         self::remove("user_id='$user' and type='$type'");
         self::save(array("type"=>$type,
             "plan_id"=>$plan,
             "user_id"=>$user,"start_on"=>  nowDateTime(),
             "end_on"=>date("Y-m-d H:i:s",strtotime('+1 years',time()))));
         return self::isNullArray(array("msg"=>"success","status"=>"true"));
        
    }
    function userPlanStatus($rs){
        $rs=  array_map("security", $rs);
         
         if(!Users::isUser($rs['userid']) )
            return array("status"=>"false","msg"=>"invalid credentials");
         
         $today=date("Y-m-d",time());
         $audio_plans=Subscriptions::get("user_id='".$rs['userid']."' and type='audio' and  DATE(end_on)>='$today' order by id DESC","plan_id");
         $video_plans=Subscriptions::get("user_id='".$rs['userid']."' and type='video'  and DATE(end_on)>='$today' order by id DESC","plan_id");
         
         if(count($video_plans)>0){
             $video_staus="paid";
         }
         else
             $video_staus="free";
         
         if(count($audio_plans)>0){
             $audio_staus="paid";
         }
         else
             $audio_staus="free";
         
         return Subscriptions::isNullArray(array("msg"=>"success","status"=>"true",
             "audio_status"=>$audio_staus,
         "video_status"=>$video_staus,
         "audio_plans"=>$audio_plans->plan_id,
         "video_plans"=>$video_plans->plan_id,
                 ));
         
    }
}

