<?php
class NotificationSettings extends Models{
    protected static $table="notification_settings";
    public static function isUserSettingExist($user){
    
        if(self::count("user_id='$user'")->count==0){
            self::save(array("user_id"=>$user));
        }
        
    }
    
    public static function setSettings($rs){
        $rs        =  array_map("security", $rs);
        $userid    =  (int)$rs['userid'];
        $type      =  $rs['notification_type'];
        $status    =  (int)$rs['status'];
        if(!$userid>0 && !Users::isUser($userid)){
           return array("msg"=>"invalid user id","status"=>"false");
        }
        switch ($type){
        case "like":
            $dbStatus="`like`";
            break;
        case "follow":
            $dbStatus="follow";
            break;
        case "facebook":
            $dbStatus="facebook_join";
            break;
        case "comment":
            $dbStatus="comment";
            break;
        case "mention":
            $dbStatus="mention";
            break;
        default :
            $dbStatus="none";
        }
        self::isUserSettingExist($userid);
        if($dbStatus!="none"){
        self::save(array($dbStatus=>$status),"user_id='$userid'");
         return array("status"=>"true","msg"=>"Setting saved successfully");
        }
        else{
        return array("status"=>"false","msg"=>"Setting not saved");
        }
    }
    public static function getSettings($rs){ 
        $rs        =  array_map("security", $rs);
        $userid    =  (int)$rs['userid'];
        if(!$userid>0 && !Users::isUser($userid)){
           return array("msg"=>"invalid user id","status"=>"false");
        }
        self::isUserSettingExist($userid);
        $data=self::get("user_id='$userid'");
         return array("msg"=>"success","settings"=>$data,"status"=>"true");
    }
    public function userSetting($user,$setting){
        $data=self::get("user_id='$user'");
        if(count($data)>0){
           return $data->$setting;
        }
        else{
            return 1;
        }
    }
    
}

