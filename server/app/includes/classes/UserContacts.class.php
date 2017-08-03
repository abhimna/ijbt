<?php

class UserContacts extends Models{
    protected static $table="user_contacts";
    
    public static function getUserContacts($rs){ 
    $UserContacts=  json_decode($rs['contacts']);
    
         $rs          =  array_map("security", $rs);
         $userid      =  (int)$rs['userid'];
         $type        =  (int)$rs['contact_type'];
          if(!Users::isUser($userid)){
            return array("status"=>"false","msg"=>"invalid user"); 
         }
         $UserContacts=  explode(",", $UserContacts).",0";
         $contactsStr="0";
         if($type=="FACEBOOK"){
         foreach($UserContacts as  $key=>$contact){
             $contactsStr.=",$contact->fb_id";
         }
          $contacts    =  Users::getAll("facebook_id in($contactsStr)");
         }
         else{
             foreach($UserContacts as  $key=>$contact){
             $contactsStr.=",$contact->email";
             }
             
          $contacts    =  Users::getAll("email in($contactsStr)");
         }
         return self::isNullArray(array("status"=>"true",
                                        "msg"=>"success",
                                        "contacts"=>$contacts
                                        ));
         
    }
    
}
