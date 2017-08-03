<?php
$con=mysqli_connect("localhost","ijbt_app","Pluggdd@2016","ijbt_post");
//if Connection Success
if($con)											
{
        mysqli_set_charset($con,"utf8");
        $query=mysqli_query($con,"SELECT * FROM `category_details` where delstatus='0'");

	while($rw=mysqli_fetch_array($query,MYSQLI_ASSOC))
	{
	$cid=$rw['fld_id'];
	$categoryname=$rw['categoryname'];
	
	$catdetails[]=array("Categoryid"=>$cid,"Categorynam"=>$categoryname);
		
	}
 $query=mysqli_query($con,"SELECT * FROM `currencies`");

	while($rw=mysqli_fetch_array($query,MYSQLI_ASSOC))
	{
	$cid=$rw['iso_code'];
	$categoryname=$rw['symbol'];
	
	$currencies[]=array("iso_code"=>$cid,"symbol"=>$categoryname);
		
	}

	$totalcategory=array("totalcatdetails"=>$catdetails,"currencies"=>$currencies);
	header('Content-type: application/json');
	echo json_encode($totalcategory);

}
else
{

$json = array("status" => 0, "msg" => "Network Error");
header('Content-type: application/json');
	echo json_encode($json);
}



?>