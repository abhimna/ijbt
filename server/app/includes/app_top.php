<?php 

ob_start();
session_start();
ini_set('display_errors', 1);
error_reporting(E_ERROR);
session_cache_expire(10);
ini_set('session.gc_maxlifetime', 24 * 60 * 60);
ini_set('session.gc_probability', 1);
ini_set('session.gc_divisor', 1);
define("GOOGLE_API_KEY", "AIzaSyAZPvgQDNwOMiMj_gZu8SvPol6MB8frav4");
//define("GOOGLE_API_KEY", "AIzaSyDNGcgrUUprIRSJ9bJhvJKnORSGHU2THTs");
//define("GOOGLE_API_KEY", "AIzaSyCIASnUtGLmNYnyO8ktCgf1uG3-J5vDwvE");
//define("GOOGLE_GCM_URL", "https://android.googleapis.com/gcm/send");
define("GOOGLE_GCM_URL", "https://fcm.googleapis.com/fcm/send");
$sessionCookieExpireTime = 24 * 60 * 60;
session_set_cookie_params($sessionCookieExpireTime);
define("SALT", "ijbt!@#321");

function cheader($url) {
	$url = admin_path . $url;
	if (ereg("Microsoft", $_SERVER['SERVER_SOFTWARE'])) {
		header("Refresh: 0; URL=$url");
	} else {
		header("Location: $url");
	}
	exit();
}
function __autoload($class) { 
    if(file_exists("../includes/classes/$class.class.php")){
        include_once "../includes/classes/$class.class.php";
    }
    if(file_exists("includes/classes/$class.class.php")){
        include_once "includes/classes/$class.class.php";
    }
    if(file_exists("../../includes/classes/$class.class.php")){
        include_once "../../includes/classes/$class.class.php";
    }
     if(file_exists("../../includes/classes/$class.class.php")){
        include_once "../../includes/classes/$class.class.php";
    }
}
define("DM","-1");//DEVELOPMEN MODE 1=>on 0=>OFF ,-1 =>testing
include 'fb.php';
$App_Status=array("is_live"=>"1","url"=>"test.com","msg"=>"testing");

function fheader($url) {
	$url = base_path . $url;
	if (ereg("Microsoft", $_SERVER['SERVER_SOFTWARE'])) {
		header("Refresh: 0; URL=$url");
	} else {
		header("Location: $url");
	}
	exit();
}

function snapheader($url) {
	$url = base_path_snap . $url;
	if (ereg("Microsoft", $_SERVER['SERVER_SOFTWARE'])) {
		header("Refresh: 0; URL=$url");
	} else {
		header("Location: $url");
	}
	exit();
}
function otheader($url) {
	$url = base_path_ot . $url;
	if (ereg("Microsoft", $_SERVER['SERVER_SOFTWARE'])) {
		header("Refresh: 0; URL=$url");
	} else {
		header("Location: $url");
	}
	exit();
}

if ($PageTitle == "")
	$PageTitle = "IJBT";

function send_mail($mailto, $mailubject, $mailbody, $mailfrom = '') {
	 $mailbody = stripslashes($mailbody);
	 
	$mailfrom = "info@ijbt.in";

	$headers = "MIME-Version: 1.0" . "\n";
	$headers .= "Content-type: text/html; charset=iso-8859-1" . "\n";
	$headers .= "X-Priority: 1 (Higuest)\n";
	$headers .= "X-MSMail-Priority: High\n";
	$headers .= "Importance: High\n";

	$headers .= "From: <$mailfrom>" . "\n";
	$headers .= "Return-Path: <$mailfrom>" . "\n";
	$headers .= "Reply-To: <$mailfrom>";


	@mail($mailto, $mailubject, $mailbody, $headers);
}
 

?>
