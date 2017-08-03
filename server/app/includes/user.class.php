<?php
class USER_CLASS {
    
       
	function isUser($userid) {

		global $db;

		$sql = "SELECT  userid FROM users WHERE userid  = '$userid'  and status='1'";

		$query = $db->query($sql);



		if ($query->size() > 0)
			return true;
		else
			return false;
	}

	function validateUserEmail($userEmail) {

		global $db;



		$sql = "select * from users where UPPER(email)='" . strtoupper($userEmail) . "'";



		$result = $db->query($sql);

		if ($result->size() > 0) {

			$rs = $result->fetch();

			$userID = $rs['userid'];
		}

		return $userID;
	}

	function getUserInfo($userid) {

		global $db;



		$username = array();

		$sql = "select * from users where userid='" . $userid . "'";

		$result = $db->query($sql);

		if ($result->size() > 0) {

			$rs = $result->fetch();



			if ($rs['user_image'] == '') {


				$rs['user_image'] = "uploads/default_img.png";
			}








			$username = $rs;
		}

		return $username;
	}

	function getPostInfo($id) {

		global $db;



		$info = array();

		$sql = "select * from post where postid=" . $id . "";

		$result = $db->query($sql);

		if ($result->size() > 0) {

			$rs = $result->fetch();

			$rs['timetext'] = $this->getTimeInfo($rs['dtdate'], date("Y-m-d H:i:s"), "x");

			$info = $rs;
		}

		return $info;
	}

	function getCommentInfo($commentid) {
		global $db;

		$info = array();

		$sql = "SELECT * FROM post_comment WHERE id='" . $commentid . "'";
		$exe = $db->query($sql);
		if ($exe->size() > 0) {
			$res = $exe->fetch();
			$res['timetext'] = $this->getTimeInfo($res['dtdate'], date("Y-m-d H:i:s"), "x");

			$info = $res;

			return $info;
		}
	}

	function getTimeInfo_notification($oldTime, $newTime, $timeType) {

		global $db;

$oldTime	=	date("Y-m-d H:i:s",strtotime($oldTime));

		$timeCalc = strtotime($newTime) - strtotime($oldTime);

		if ($timeType == "x") {

			if ($timeCalc > 60) {

				$timeType = "m";
			}

			if ($timeCalc > (60 * 60)) {

				$timeType = "h";
			}

			if ($timeCalc > (60 * 60 * 24)) {

				$timeType = "d";
			}
		}

		if ($timeType == "s") {

		}

		if ($timeType == "m") {

			$timeCalc = "Today : " . date("h:i A", strtotime($oldTime));
		} elseif ($timeType == "h") {

			$timeCalc = "Today : " . date("h:i A", strtotime($oldTime));
		} elseif ($timeType == "d") {

			if (round($timeCalc / 60 / 60 / 24) <= 1)
				$timeCalc = "Yesterday : " . date("h:i A", strtotime($oldTime));
			else
				$timeCalc = "" . date("m-d-Y  h:i A", strtotime($oldTime));
		}else {

			$timeCalc = "Today : " . date("h:i A", strtotime($oldTime));
		}

		return $timeCalc;
	}

	function getTimeInfo($oldTime, $newTime, $timeType) {

		global $db;

                $oldTime	=	date("Y-m-d H:i:s",strtotime($oldTime));

		$timeCalc = strtotime($newTime) - strtotime($oldTime);

		if ($timeType == "x") {

			if ($timeCalc > 60) {

				$timeType = "m";
			}

			if ($timeCalc > (60 * 60)) {

				$timeType = "h";
			}

			if ($timeCalc > (60 * 60 * 24)) {

				$timeType = "d";
			}
		}

		if ($timeType == "s") {

		}

		if ($timeType == "m") {



			if (round($timeCalc / 60) <= 1)
				$timeCalc = round($timeCalc / 60) . " min ago";
			else
				$timeCalc = round($timeCalc / 60) . " mins ago";
		}

		elseif ($timeType == "h") {



			if (round($timeCalc / 60 / 60) <= 1)
				$timeCalc = round($timeCalc / 60 / 60) . " hour ago";
			else
				$timeCalc = round($timeCalc / 60 / 60) . " hours ago";
		}

		elseif ($timeType == "d") {





			if (round($timeCalc / 60 / 60 / 24) <= 1)
				$timeCalc = round($timeCalc / 60 / 60 / 24) . " day ago";
			else
				$timeCalc = round($timeCalc / 60 / 60 / 24) . " days ago";
		}else {

			$timeCalc .= " sec ago";
		}

		return $timeCalc;
	}
	function getRemainingTimeInfo($endTime, $currentTime, $timeType) {

		global $db;

$endTime	=	date("Y-m-d H:i:s",strtotime($endTime));

		$timeCalc = strtotime($endTime) - strtotime($currentTime);

		if ($timeType == "x") {

			if ($timeCalc > 60) {

				$timeType = "m";
			}

			if ($timeCalc > (60 * 60)) {

				$timeType = "h";
			}

			if ($timeCalc > (60 * 60 * 24)) {

				$timeType = "d";
			}
		}

		if ($timeType == "s") {

		}

		if ($timeType == "m") {



			if (round($timeCalc / 60) <= 1)
				$timeCalc = round($timeCalc / 60) . " min remaining";
			else
				$timeCalc = round($timeCalc / 60) . " mins remaining";
		}

		elseif ($timeType == "h") {



			if (round($timeCalc / 60 / 60) <= 1)
				$timeCalc = round($timeCalc / 60 / 60) . " hour remaining";
			else
				$timeCalc = round($timeCalc / 60 / 60) . " hours remaining";
		}

		elseif ($timeType == "d") {





			if (round($timeCalc / 60 / 60 / 24) <= 1)
				$timeCalc = round($timeCalc / 60 / 60 / 24) . " day remaining";
			else
				$timeCalc = round($timeCalc / 60 / 60 / 24) . " days remaining";
		}else {

			$timeCalc .= " sec ago";
		}

		return $timeCalc;
	}
	
	function getDealTime($dealid,$today)
	{
		global $db;
		//$today		=	date("Y-m-d h:i:s"); 
        $left_minutes   =   0;
		$sqlDeal	=	"SELECT start_date,end_date FROM deal_post WHERE deal_id='".intval($dealid)."'";
		$exe_deal	=	$db->query($sqlDeal);
		if($exe_deal->size()>0)
		{
			$resDeal	=	$exe_deal->fetch();
			$start_time	=	$resDeal['start_date'];
			$end_time	=	date("Y-m-d H:i:s",strtotime($resDeal['end_date'])); //($resDeal['end_date'],'Y-m-d h:i:s');
           /// echo strtotime($today)-strtotime($end_time)."<br>";
			$timeCalc = strtotime($end_time) - strtotime($today);
			if($today<=$end_time)
			{
				//$time=$this->getRemainingTimeInfo($end_time,$today,'x');
				
				$seconds = strtotime($end_time) - strtotime($today);
				$left_minutes	=	round($seconds/60); 
				//echo "if => ".$left_minutes;
				
				
			/*	$days    = floor($seconds / 86400);
				$hours   = floor(($seconds - ($days * 86400)) / 3600);
				$minutes = floor(($seconds - ($days * 86400) - ($hours * 3600))/60);
				$seconds = floor(($seconds - ($days * 86400) - ($hours * 3600) - ($minutes*60)));*/
				
				
			}
			else
			{
				//$lefttime= $this->getTimeInfo($resDeal['end_date'], $today, "x");
				$left_minutes	=	0;
                //echo "else => ".$left_minutes;
				//$min1	=	"0";
				
			}
            //echo $left_minutes; die;
			
		}
		return "".$left_minutes."";
		
	}
    
    function getDealDetail($dealid)
	{
		global $db;
		//$today		=	date("Y-m-d h:i:s"); 
        $left_minutes   =   0;
		$sqlDeal	=	"SELECT * FROM deal_post WHERE deal_id='".intval($dealid)."'";
		$exe_deal	=	$db->query($sqlDeal);
		if($exe_deal->size()>0)
		{
			$resDeal	=	$exe_deal->fetch();
			
            //echo $left_minutes; die;
			
		}
		return $resDeal;
		
	}

	function countTotalComment($postid) {

		global $db;




		$sql = "select id from post_comment where post_id='" . $postid . "'";

		$result = $db->query($sql);



		return "" . intval($result->size()) . "";
	}

	function getTotalPost($userid, $type) {

		global $db;

		$sql = "select postid from post where userid='" . $userid . "' AND posttype ='" . $type . "'";

		$result = $db->query($sql);



		return "" . intval($result->size()) . "";
	}

	function getTotalLike($postid) {

		global $db;

		$sql = "select id from post_like where post_id='" . $postid . "' AND like_status = '1'";

		$result = $db->query($sql);



		return "" . intval($result->size()) . "";
	}

	function getReportStatus($userid, $postid) {

		global $db;

		$sql = "select id from report where userid = '" . $userid . "' AND postid='" . $postid . "'";

		$result = $db->query($sql);



		return "" . intval($result->size()) . "";
	}

	function getTotalDislike($postid) {

		global $db;

		$sql = "select id from post_like where post_id='" . $postid . "' AND like_status = '0'";

		$result = $db->query($sql);



		return "" . intval($result->size()) . "";
	}

	function getTotalVote($postid) {
		global $db;

		$sql = "SELECT count(id) as totalVote FROM post_like WHERE post_id='" . $postid . "' AND like_status = '1'";
		$exe = $db->query($sql);
		$res = $exe->fetch();
		$totalvote = $res['totalVote'];
		return "" . intval($totalvote) . "";
	}

	function getLikeStatus($userid, $postid) {

		global $db;

		$status = 0;



		$sql = "select like_status from post_like where user_id = '" . $userid . "' AND post_id='" . $postid . " '";

		$result = $db->query($sql);

		if ($result->size() > 0) {

			$rs = $result->fetch();

			$status = $rs['like_status'];

			if ($status == 1)
				$status = "1";

			elseif ($status == 0)
				$status = "0";
			else
				$status = "0";
		}

		return "" . $status . "";
	}

	function getPostComment($postid) {

		global $db;

		$status = "false";

		$msg = '';

		$comment = array();

		if ($postid <= 0 || $postid == "")
			$msg = "Invalid user or post";

		else {

			$sql = "select id as commentid, comment,dtdate,userid,postid,gpsstatus,location,city,state,country,lat,lng from comment where postid='" . $postid . "' ORDER BY id ASC ";

			$result = $db->query($sql);

			if ($result->size() > 0) {

				while ($rs = $result->fetch($sql)) {

					$userInfo = $this->getUserInfo($rs['userid']);

					$rs['name'] = $userInfo['name'];

					$rs['userphoto'] = $userInfo['user_thumbimage'];

					$rs['newcomment'] = $userInfo['username'] . ": " . $rs['comment'];

					$rs['time_text'] = $this->getTimeInfo($rs['dtdate'], date("Y-m-d H:i:s"), "x");

					$comment[] = $rs;
				}

				$msg = 'Successfully';

				$status = "true";
			} else
				$msg = "No one comment found on this post";
		}

		return $comment;
	}

	function addNotification($userid, $mainid, $ref_id, $msg, $type) {

		global $db;

		$sql = "insert into notification(userid,mainid,ref_id,msg,type,dtdate)VALUES(" .
				"'" . $userid . "'," .
				"'" . $mainid . "'," .
				"'" . $ref_id . "'," .
				"'" . $msg . "'," .
				"'" . $type . "'," .
				" NOW() " .
				")";

		$db->query($sql);



		return true;
	}

	function getStoresLatLon($lat, $lon, $distance = 20) {

		$d = $distance - round((($distance * 20) / 100), 2);  // Miles

		$r = 3959; // earth redious

		$arr = array();

		$latN = rad2deg(asin(sin(deg2rad($lat)) * cos($d / $r) + cos(deg2rad($lat)) * sin($d / $r) * cos(deg2rad(0))));

		$latS = rad2deg(asin(sin(deg2rad($lat)) * cos($d / $r) + cos(deg2rad($lat)) * sin($d / $r) * cos(deg2rad(180))));

		$lonE = rad2deg(deg2rad($lon) + atan2(sin(deg2rad(90)) * sin($d / $r) * cos(deg2rad($lat)), cos($d / $r) - sin(deg2rad($lat)) * sin(deg2rad($latN))));

		$lonW = rad2deg(deg2rad($lon) + atan2(sin(deg2rad(270)) * sin($d / $r) * cos(deg2rad($lat)), cos($d / $r) - sin(deg2rad($lat)) * sin(deg2rad($latN))));

		$arr['latN'] = $latN;

		$arr['latS'] = $latS;

		$arr['lonE'] = $lonE;

		$arr['lonW'] = $lonW;

		return $arr;
	}

	function distance($lat1, $lon1, $lat2, $lon2, $unit) {

		$theta = $lon1 - $lon2;

		$dist = sin(deg2rad($lat1)) * sin(deg2rad($lat2)) + cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($theta));

		$dist = acos($dist);

		$dist = rad2deg($dist);

		$miles = $dist * 60 * 1.1515;

		$unit = strtoupper($unit);



		if ($unit == "K") {

			return (round($miles * 1.609344));
		} else if ($unit == "N") {

			return (round($miles * 0.8684));
		} else {

			return round($miles);
		}
	}

}
