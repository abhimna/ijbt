<?php

class Models {
    protected static $table="";
    protected static $db="";
    
    private static function getTable($parent)
    {
        global $db;
        self::$table=$parent::$table;
        self::$db=$db;
    }

    public static function getAll($cond="1",$attr="*"){
        $rows=[];
        self::getTable(get_called_class());
//        echo "select $attr from ".self::$table." where $cond";
//        die;
        $result=self::$db->query("select $attr from ".self::$table." where $cond");
        
        if($result->size()>0){
        while($r=$result->fetch())
                $rows[]=(object)$r;
            
        return (object)$rows;
        }
        else
        return array();
    }
    
    public static function count($cond="1",$attr="count(*) as count"){
        self::getTable(get_called_class());
        
        $result=self::$db->query("select $attr from ".self::$table." where $cond");
        return (object)$result->fetch();
       
    }
    
    public static function get($cond="1",$attr="*"){
        self::getTable(get_called_class());
         
        $result=self::$db->query("select $attr from ".self::$table." where $cond limit 1");
        if($result->size()>0)
        return (object)$result->fetch();
        else
        return array();
            
    }
    
    public static function getCalledClass(){
    $arr = array(); 
    $arrTraces = debug_backtrace();
    foreach ($arrTraces as $arrTrace){
       if(!array_key_exists("class", $arrTrace)) continue;
       if(count($arr)==0) $arr[] = $arrTrace['class'];
       else if(get_parent_class($arrTrace['class'])==end($arr)) $arr[] = $arrTrace['class'];
    }
    return end($arr);
    }
    
    public static function remove($cond){
         self::getTable(get_called_class());
        if($cond!=""){
        return $result=self::$db->query("delete from ".self::$table." where $cond");
       
        }
    }
    
    public static function query($query){
        global $db;
        return $db->query($query);
   }
   
    public static function save($data, $condition = "") {               //to save brands
        
        global $db;
        self::getTable(get_called_class());
        $data = self::createSaveStr($data);
        if ($condition == "") { 
//            echo "INSERT INTO ".self::$table." SET $data";
            $result = $db->query("INSERT INTO ".self::$table." SET $data");
            return $result->insertID();
        } else { 
            return $db->query("UPDATE ".self::$table." SET $data WHERE $condition");
        }
    }
    
    public static function stdToArray($obj){
          $reaged = (array)$obj;
          foreach($reaged as $key => &$field){
            if(is_object($field))$field = self::stdToArray($field);
          }
          return $reaged;
    }
    
    public static function arrayToStd($obj){
          $reaged = (object)$obj;
          foreach($reaged as $key => &$field){
            if(is_array($field))$field = self::arrayToStd($field);
          }
          return $reaged;
    }

    public static function isNullArray($array) {        //check all array if any null value exist than replace with empty string
     $array=self::stdToArray($array);
     $newArr=[];   
     foreach($array as $key=>$val){  
         if(is_array($val) && count($val)>0){
            $val=self::isNullArray($val);
         }
        
            $newArr[$key]= empty($val) && !is_array($val) ? "" :is_array($val)? $val : strval($val);
         
     }   
     return $newArr;
    }
    
    public static function isNullArrayV($array) {        //check all array if any null value exist than replace with empty string
     $array=self::arrayToStd($array);
     $newArr=[];   
     foreach($array as $key=>$val){  
         if(is_object($val) && count($val)>0){
            $val=self::isNullArray($val);
         }
        
            $newArr[$key]= empty($val) && !is_object($val) ? "" :is_object($val)? $val : strval($val);
         
     }   
     return $newArr;
    }
   
    private static function createSaveStr($array) {                   //convert array to string in insert query format
        $stringName = "";
        foreach ($array as $field => $value) {
            $stringName.="$field='$value',";
        }
        return substr($stringName, 0, -1);
    }
    
   
}
