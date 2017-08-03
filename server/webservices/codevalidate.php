<?php
$code=$_POST["code"];
if(($code=='codepluggdd'))
{

$json = array("status" => 1, "msg" => "Valid code");
header('Content-type: application/json');
	echo json_encode($json);
}
else
{

$json = array("status" =>0, "msg" => "Invalid Code");
header('Content-type: application/json');
	echo json_encode($json);
}
