<?php
$currency = '$';
$currency_code = "USD";

$date_format = "m-d-Y";
$date_format_month = "M d, Y";
$time_format = "h:i a";
$date_time_format = "m-d-Y h:i A";
$SERVER_ADDR = "http://" . $_SERVER['HTTP_HOST'];
$SERVER_ROOT = $_SERVER['DOCUMENT_ROOT'];
//define base path


define("base_path", $SERVER_ADDR . "/app/");
define("main_path", $SERVER_ADDR . "/app/");
define("admin_path", $SERVER_ADDR . "/app/MyCP/");
define("site_name", "IJBT");


  
$host = 'localhost';
$user = 'ijbt_app';
$pass = 'Pluggdd@2016';
$dbname = 'ijbt_post';
  
$db = new MySQL($host, $user, $pass, $dbname);

$connectionstring=mysqli_connect($host,$user,$pass,$dbname);

//
