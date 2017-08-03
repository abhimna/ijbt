<?php
$con=mysqli_connect("localhost","ijbt_app","Pluggdd@2016","ijbt_post");
//if Connection Success
if($con)											
{
        mysqli_set_charset($con,"utf8");
        $query=mysqli_query($con,"DELETE FROM `post_like` WHERE user_id='7'");
        echo "deleted";
}