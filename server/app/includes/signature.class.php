<?php
class SIGNATURE {

	private function getApiSignature($param) {

		ksort($param);
		foreach ($param as $key => $val) {
                    $paramStr .= strtolower($key);
		}
		$paramStr .= SALT;
                ErrorLog::log("API_paramStr", $paramStr);
                 fb::info($paramStr,"App paramStr");
//		echo $paramStr."\n";
//		  exit;
		$HASH_STR = SHA1($paramStr);
		return $HASH_STR;
	}

	function checkValidRequest($post) {
		$type = strtolower($post['type']);
		$signature = $post['signature'];
		$param = $this->checkRequest();

		$param[$type] = $type;

		$API_signature = $this->getApiSignature($param);
		   fb::info($API_signature,"App SIGNATURE");
                ErrorLog::log("API_signature", $API_signature);

//		print_r($API_signature); die;

		if ($API_signature == $signature)
			return true;
		else {
			$arr = array("msg" => "Not a valid request", "status" => "false");
			echo json_encode($arr);
			exit;
		}
	}

	function checkRequest() {
		if ($_SERVER['REQUEST_METHOD'] === 'GET') {
			$data = json_decode(str_replace("\\", "", urldecode($_GET['data'])));
			$data = $data[0];
		} else
			$data = $_POST;

		$data = $this->objectToArray($data);

		return $data;
	}

	private function objectToArray($d) {
		if (is_object($d)) {
			$d = get_object_vars($d);
		}
		return $d;
	}

}
