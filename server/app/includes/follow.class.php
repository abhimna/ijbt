<?php
class FOLLOW_CLASS extends USER_CLASS {

	public $notify = false;

	function setNotificationFollow($type, $follower_id, $following_id) {

		global $db;
		$chk_sql = "SELECT * FROM user_notification WHERE user_id='" . $follower_id . "' AND following_id='" . $following_id . "' AND type='" . $type . "'";
		$exe_sql = $db->query($chk_sql);

		if ($exe_sql->size() <= 0) {
			$sql_notifi = "INSERT INTO user_notification (type,user_id,following_id,dtdate) VALUES (" .
					"'" . $type . "'," .
					"'" . intval($follower_id) . "'," .
					"'" . intval($following_id) . "'," .
					"NOW())";

			$db->query($sql_notifi);
		} else {

			$sql_notifi_del = $db->query("DELETE FROM user_notification WHERE user_id='" . $follower_id . "' AND following_id='" . $following_id . "' AND type='" . $type . "'");
		}
	}

	function addFollow($rs) {

		global $db;

		global $NOTIFI;

		$status = "false";



		$follower_id = intval(trim($rs->userid));

		$following_id = intval(trim($rs->following_id));

		//$status			=	intval(trim($rs->status));

		if ($follower_id == "") {

			$status = "false";

			$msg = "User not found.";
		} else {

			$sql = "select * from user_follow where follower_id='" . $follower_id . "' AND following_id=" . $following_id;

			$res = $db->query($sql);

			if ($res->size() <= 0) {

				$sql = "INSERT INTO user_follow(follower_id,following_id,dtdate)VALUES(" .
						"'" . $follower_id . "'," .
						"'" . $following_id . "'," .
						" NOW() " .
						")";

				$result = $db->query($sql);

				$followid = $result->insertID();



				/* $this->addNotification("follow", $follower_id, $followid, '', "FOLLOW");


				  $NOTIFI->followNotification($following_id, $follower_id); */





				$msg = "You have successfully follow this user";
			} else {

				$row = $res->fetch();

				$sqlupdatede = " DELETE FROM user_follow  WHERE follower_id ='" . intval($follower_id) . "' AND following_id='" . intval($following_id) . "'";

				$db->query($sqlupdatede);

				$msg = "Unfollow successfully";
			}
			$this->setNotificationFollow("follow", $follower_id, $following_id);

			$status = "true";
		}

		$arr = array("message" => $msg, "status" => $status);

		return $arr;
	}

	function getFollowerList($data, $friend = false) {

		global $db;

		$status = "false";

		$msg = '';

		$usercommntArray = array();



		$user_id = intval(trim($data->userid));



		if ($user_id > 0) {



			$sql = "select * from user_follow where following_id='" . $user_id . " '";



			$result = $db->query($sql);

			if ($result->size() > 0) {

				while ($rs = $result->fetchAsso()) {

					$userInfo = $this->getUserInfo($rs['follower_id']);



					$usercommntArray[] = $userInfo;
				}



				$status = "true";

				$msg = "Get successfully";
			} else
				$msg = "No record found.";
		} else
			$msg = 'User not found.';



		$arr = array("message" => $msg, "status" => $status, "type" => "FOLLOWER LIST", "data" => $usercommntArray);

		return $arr;
	}

	function getFollowingList($data, $friend = false) {

		global $db;

		$status = "false";

		$msg = '';

		$usercommntArray = array();



		$user_id = intval(trim($data->userid));



		if ($user_id > 0) {

			$sql = "select * from user_follow where follower_id='" . $user_id . " ' ";



			$result = $db->query($sql);

			if ($result->size() > 0) {

				while ($rs = $result->fetchAsso()) {



					$userInfo = $this->getUserInfo($rs['following_id']);





					//$userInfo["follow_status"] = "" . $this->getUserStatus($loginid, $rs['following_id'], "FOLLOWING") . "";




					$usercommntArray[] = $userInfo;
				}





				$status = "true";

				$msg = "Get successfully";
			} else
				$msg = "No record found.";
		} else
			$msg = 'User not found.';



		$arr = array("message" => $msg, "status" => $status, "type" => "FOLLOWING LIST", "data" => $usercommntArray);

		return $arr;
	}

	function getFollowList($rs) {

		$status = "false";

		$user_id = intval(trim($rs->userid));



		if ($user_id > 0 || $userid != "") {

			$following = $this->getFollowingList($rs);

			$follower = $this->getFollowerList($rs);

			$arr['follower'] = $follower['data'];

			$arr['following'] = $following['data'];

			$status = "true";
		} else
			$msg = 'User not found.';







		$arr = array("message" => $msg, "status" => $status, "data" => $arr);

		return $arr;
	}

	function getUserStatus($Loginid, $userid, $tp = "") {

		global $db;

		$status = 0;

		if ($tp == "FOLLOWING")
			$sql = "select * from user_follow where follower_id=" . $Loginid . " and following_id=" . $userid . "";

		elseif ($tp == "FOLLOWER")
			$sql = "select * from user_follow where following_id=" . $Loginid . " and follower_id=" . $userid . "";
		else
			$sql = "select * from user_follow where (follower_id=" . $Loginid . " and following_id=" . $userid . ") or (following_id=" . $Loginid . " and follower_id=" . $userid . ")";

		//echo $sql;

		$result = $db->query($sql);

		if ($result->size() > 0) {

			$rs = $result->fetch();

			$status = $rs['status'];
		}

		return $status;
	}

}

?>