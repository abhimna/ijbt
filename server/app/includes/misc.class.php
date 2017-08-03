<?php

class MISC_CLASS {

	function getUserInfo($userid) {
		global $db;
		$userinfo = array();
		$sql = "select * from users where userid=" . $userid;
		$result = $db->query($sql);
		if ($result->size() > 0) {
			$rs = $result->fetch();
			if ($rs['user_thumbimage'] == '')
				$rs['user_thumbimage'] = 'uploads/default_img.png';
			if ($rs['user_image'] == '')
				$rs['user_image'] = 'uploads/default_img.png';
			$userinfo = $rs;
		}
		return $userinfo;
	}


}
