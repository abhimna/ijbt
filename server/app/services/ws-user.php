<?php

require_once('../includes/app_top.php');
require_once('../includes/mysql.class.php');

require_once('../includes/global.inc.php');
require_once('../includes/misc.class.php');

require_once('../includes/mailer.class.php');

require_once('../includes/image_lib/image.class.php');

require_once('../includes/functions_general.php');
require_once('../includes/ws-user.class.php');
require_once('../includes/signature.class.php');

$SIGNATURE = new SIGNATURE;
$USER = new USER_CLASS;
$MAILER = new MailerClass;



ErrorLog::log("REQUEST URL", $_SERVER['REQUEST_URI']);
ErrorLog::log("REQUEST", $_REQUEST);
$arr = array("msg" => "no api found", "status" => "false");


if (strtoupper($_REQUEST['type']) == "GET") {
    $data = json_decode(str_replace("\\", "", urldecode($_GET['data'])));
    if ($SIGNATURE->checkValidRequest($_REQUEST)) {
		
        $arr = $USER->getProfile($data[0]);
    }
}


if (strtoupper($_REQUEST['type']) == "EDIT") {
    $data = $_POST;
    if ($SIGNATURE->checkValidRequest($_REQUEST)) {
        $arr = $USER->editProfile($data);
    }
}



if (strtoupper($_REQUEST['type']) == "BLOCKUSER") {

    $data = json_decode(str_replace("\\", "", urldecode($_GET['data'])));

    if ($SIGNATURE->checkValidRequest($_REQUEST)) {

        $arr = UserBlock::blockUser((array) $data[0]);
    }

   
}

if (strtoupper($_REQUEST['type']) == "LOGOUT") {

    $data = json_decode(str_replace("\\", "", urldecode($_GET['data'])));

    if ($SIGNATURE->checkValidRequest($_REQUEST)) {

        $arr = UserDevices::logout((array) $data[0]);
    }

   
}
if (strtoupper($_REQUEST['type']) == "DEACTIVATEUSER") {

    $data = json_decode(str_replace("\\", "", urldecode($_GET['data'])));

    if ($SIGNATURE->checkValidRequest($_REQUEST)) {

        $arr = Users::deactivateUser((array) $data[0]);
    }

   
}
if (strtoupper($_REQUEST['type']) == "SUGGESTION") {

    $data = json_decode(str_replace("\\", "", urldecode($_GET['data'])));

    if ($SIGNATURE->checkValidRequest($_REQUEST)) {

        $arr = Users::suggestion((array) $data[0]);
    }

    
}
if (strtoupper($_REQUEST['type']) == "GETUSERCONTACTS") {

    $data = $_POST;

    if ($SIGNATURE->checkValidRequest($_REQUEST)) {

        $arr = Users::getUserContacts($data);
    }

   
}
if (strtoupper($_REQUEST['type']) == "MYNOTIFICATION") {

    $data = json_decode(str_replace("\\", "", urldecode($_GET['data'])));

    if ($SIGNATURE->checkValidRequest($_REQUEST)) {
        header('Content-Type: application/json; Charset=UTF-8');
        $arr = ActivityLog::myNotifications((array) $data[0]);
    }

   
}
if (strtoupper($_REQUEST['type']) == "FOLLOWERSNOTIFICATION") {

    $data = json_decode(str_replace("\\", "", urldecode($_GET['data'])));

    if ($SIGNATURE->checkValidRequest($_REQUEST)) {
        header('Content-Type: application/json; Charset=UTF-8');
        $arr = ActivityLog::followersNotifications((array) $data[0]);
    }

    
}

if (strtoupper($_REQUEST['type']) == "GETDATE") {
    $data = json_decode(str_replace("\\", "", urldecode($_GET['data'])));

    $arr = $USER::getTimeAgo();

    
}

/* FACEBOOK LOGIN */
if (strtoupper($_REQUEST['type']) == "FACEBOOKLOGIN") {

    $data = $_POST;
    if ($SIGNATURE->checkValidRequest($_REQUEST)) {

        $arr = $USER->facebookLogin($data);
    }


   
}





echo json_encode($arr);
ErrorLog::log("RESPONSE", $arr);
?>