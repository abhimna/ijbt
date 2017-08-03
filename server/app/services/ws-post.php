<?php

header('Content-Type: application/json; Charset=UTF-8');
$dir = ((dirname(__DIR__)));

require_once($dir . '/includes/app_top.php');
require_once($dir . '/includes/mysql.class.php');
require_once($dir . '/includes/global.inc.php');
require_once($dir . '/includes/functions_general.php');
require_once($dir . '/includes/user.class.php');
require_once($dir . '/includes/signature.class.php');
require_once($dir . '/includes/image_functions.php');
require_once($dir . '/includes/follow.class.php');
require_once($dir . '/includes/upload-video.php');


require_once($dir.'/includes/image_lib/image.class.php');

include($dir . "/includes/upl_function.php");

ErrorLog::log("REQUEST URL", $_SERVER['REQUEST_URI']);
ErrorLog::log("REQUEST", $_REQUEST);
$arr = array("msg" => "no api found", "status" => "false");

$SIGNATURE = new SIGNATURE;

function createDir($path) {
    return ((!is_dir($path)) ? @mkdir($path, 0777) : TRUE);
}

if (strtoupper($_REQUEST['type']) == "ADDPOST") {
    $data = $_POST;
    //if ($SIGNATURE->checkValidRequest($_REQUEST)) {
        $arr = Posts::addPost($_POST);
    //}
}



if (strtoupper($_REQUEST['type']) == "HOMEPOSTFAV") {
    $data = json_decode(str_replace("\\", "", urldecode($_GET['data'])));
	
        $arr = Posts::homePostsfav((array) $data[0]);
  
}
if (strtoupper($_REQUEST['type']) == "HOMEPOSTLIKE") {
    $data = json_decode(str_replace("\\", "", urldecode($_GET['data'])));
	
        $arr = Posts::homePostslike((array) $data[0]);
  
}


if (strtoupper($_REQUEST['type']) == "HOMEPOSTIJBT") {
    $data = json_decode(str_replace("\\", "", urldecode($_GET['data'])));
    
        $arr = Posts::homePostsIJBT((array) $data[0]);
    
}



if (strtoupper($_REQUEST['type']) == "POSTLIKE") {
    $data = json_decode(str_replace("\\", "", urldecode($_GET['data'])));
  //  if ($SIGNATURE->checkValidRequest($_REQUEST)) {
        $arr = PostLike::like((array) $data[0]);
   // }
}



if (strtoupper($_REQUEST['type']) == "DELETEPOST") {
    $data = json_decode(str_replace("\\", "", urldecode($_GET['data'])));

    //if ($SIGNATURE->checkValidRequest($_REQUEST)) {

        $arr = Posts::deletePost((array) $data[0]);
    //}
    
}
if (strtoupper($_REQUEST['type']) == "REPORTPOST") {
    $data = json_decode(str_replace("\\", "", urldecode($_GET['data'])));

    if ($SIGNATURE->checkValidRequest($_REQUEST)) {

        $arr = PostReport::makeReport((array) $data[0]);
    }
    
}


echo json_encode($arr);
ErrorLog::log("RESPONSE", $arr);

?>