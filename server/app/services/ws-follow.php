<?php
require_once('../includes/app_top.php');
require_once('../includes/mysql.class.php');
require_once('../includes/global.inc.php');
require_once('../includes/functions_general.php');
require_once('../includes/user.class.php');
require_once('../includes/follow.class.php');
require_once('../includes/signature.class.php');
error_reporting(0);
$SIGNATURE = new SIGNATURE;

if (strtoupper($_REQUEST['type']) == "FOLLOWALL") {
        $data = $_POST;
        if ($SIGNATURE->checkValidRequest($_REQUEST)) {
                $arr = Follow::followAll($_REQUEST);
                echo json_encode($arr);
        }
}
if (strtoupper($_REQUEST['type']) == "FOLLOWREQUEST") {
$data = json_decode(str_replace("\\", "", urldecode($_GET['data'])));
        if ($SIGNATURE->checkValidRequest($_REQUEST)) {
                $arr = Follow::followRequest((array)$data[0]);
                echo json_encode($arr);
        }
}

if (strtoupper($_REQUEST['type']) == "USERFOLLOWERS") {
$data = json_decode(str_replace("\\", "", urldecode($_GET['data'])));
        if ($SIGNATURE->checkValidRequest($_REQUEST)) {
                $arr = Follow::userFollowers((array)$data[0]);
                echo json_encode($arr);
}
}

if (strtoupper($_REQUEST['type']) == "USERFOLLOWING") {
$data = json_decode(str_replace("\\", "", urldecode($_GET['data'])));
        if ($SIGNATURE->checkValidRequest($_REQUEST)) {
                $arr = Follow::userFollowings((array)$data[0]);
                echo json_encode($arr);
}        
}




