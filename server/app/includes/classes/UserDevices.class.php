<?php
class UserDevices extends Models{
    protected static $table="user_devices";
    
    public static  function addDevice($user,$token,$device_type){   
        if(UserDevices::count("device_token='$token'")->count>0){
            UserDevices::remove("device_token='$token'");
        }
        UserDevices::save(array("user_id"=>$user,"device_token"=>$token, "device_type" => $device_type));
    }
    public static  function deleteDevice($user,$token){   
        UserDevices::remove(array("user_id"=>$user,"device_token"=>$token));
    }
    public static  function setBedge($rs){   
        $token      = $rs['device_token'];
        UserDevices::save(array("bedge"=>"0"),"device_token='$token'");
        return array("msg"=>"success","status"=>"true");
    }
    public static  function logout($rs){   
        $rs    =  array_map("security", $rs);
        $userid= (int)$rs['userid'];
        $token= $rs['device_token'];
        UserDevices::remove("device_token='$token'");
        return array("msg"=>"user logout successfully","status"=>"true");
    }
    
  
}

