<?php
function GetMonthNo($monthname='') {

	switch ($monthname) {

		case "Jan":

			return "01";

			break;

		case "Feb":

			return "02";

			break;

		case "Mar":

			return "03";

			break;

		case "Apr":

			return "04";

			break;

		case "May":

			return "05";

			break;

		case "Jun":

			return "06";

			break;

		case "Jul":

			return "07";

			break;

		case "Aug":

			return "08";

			break;

		case "Sep":

			return "09";

			break;

		case "Oct":

			return "10";

			break;

		case "Nov":

			return "11";

			break;

		case "Dec":

			return "12";

			break;
	}
}

function sqlDate($date) {

	$dtDate = explode("-", $date);

	if ($date_format == "m-d-Y") {

		$dtDate = $dtDate[2] . "-" . $dtDate[1] . "-" . $dtDate[0];
	} else
		$dtDate = $dtDate[2] . "-" . $dtDate[1] . "-" . $dtDate[0];



	return $dtDate;
}

function FormatDate($date) {

	global $date_format;



	return date($date_format, strtotime($date));
}

function FormatDateNew($date) {

	return date("m-d-Y", strtotime($date));
}

function FormatDateNewTime($date) {

	return date("M d, Y, h:i A", strtotime($date));
}

function FormatDateNewSecond($date) {

	return date("m/d/Y", strtotime($date));
}

function FormatDateSql($date) {

	return date("Y-m-d", strtotime($date));
}

function FormatDateMonth($date) {

	global $date_format_month;

	return date($date_format_month, strtotime($date));
}

function FormatTime($date) {

	global $time_format;

	return date($time_format, strtotime($date));
}

function nowDate() {

	return date("Y-m-d");
}

function nowDateTime() {

	return date("Y-m-d H:i:s");
}

function hoursToHourMinite($hours) {

	$hourInfo = explode(".", $hours);

	$hmiinit = ($hourInfo[0] * 60);

	$minites = round(($hours * 60) - $hmiinit);

	if ($minites <= 9)
		$minites = "0" . $minites;

	if ($minites > 0)
		$hourminit = $hourInfo[0] . ":" . $minites;
	else
		$hourminit = $hourInfo[0] . ":" . $minites;

	return $hourminit;
}

function nowTime() {

	return date("H:i:s");
}

function convert24to12Hours($time) {

	$time = date("g:i A", strtotime($time));

	return $time;
}

function convert12to24Hours($time) {

	$time = date("H:i", strtotime($time));

	return $time;
}

function FormatDateTime($date) {

	global $date_time_format;

	return date($date_time_format, strtotime($date));
}

function FormatNumber($val) {

	//string number_format ( float $number , int $decimals , string $dec_point , string $thousands_sep )

	return number_format($val, 2, '.', ',');
}

function dateAdd($tp, $interval, $today) {



	if (empty($today))
		$today_day = date("Y-m-d");
	else
		$today_day = $today;

	switch ($tp) {

		case "m":

			$by = $interval . " month";

			break;

		case "y":

			$by = $interval . " year";

			break;

		case "w":

			$by = $interval . " week";

			break;
	}

	$date = strtotime(date("Y-m-d", strtotime($today_day)) . $by);

	$date = date('Y-m-d', $date);

	return $date;
}

function make_page($total_items, $items_per_page, $p) {

	if (($total_items % $items_per_page) != 0) {

		$maxpage = ($total_items) / $items_per_page + 1;
	} else {

		$maxpage = ($total_items) / $items_per_page;
	}

	$maxpage = (int) $maxpage;

	if ($maxpage <= 0) {

		$maxpage = 1;
	}

	if ($p > $maxpage) {

		$p = $maxpage;
	} elseif ($p < 1) {

		$p = 1;
	}

	$start = ($p - 1) * $items_per_page;

	return Array($start, $p, $maxpage);
}

function randomcode($len = "6") {

	$code = NULL;

	for ($i = 0; $i < $len; $i++) {

		$char = chr(rand(48, 122));

		while (!ereg("[a-zA-Z0-9]", $char)) {

			if ($char == $lchar) {

				continue;
			}

			$char = chr(rand(48, 90));
		}

		$pass .= $char;

		$lchar = $char;
	}

	return $pass;
}

function randomNum($len = "6") {

	$code = NULL;

	for ($i = 0; $i < $len; $i++) {

		$char = chr(rand(48, 122));

		while (!ereg("[0-9]", $char)) {

			if ($char == $lchar) {

				continue;
			}

			$char = chr(rand(48, 90));
		}

		$pass .= $char;

		$lchar = $char;
	}

	return $pass;
}

function is_email_address($email) {

	$regexp = "/^[a-z0-9]+([_\\.-][a-z0-9]+)*@([a-z0-9]+([\.-][a-z0-9]+)*)+\\.[a-z]{2,}$/i";

	if (!preg_match($regexp, $email)) {

		return false;
	} else {

		return true;
	}
}

function validate_username($v_username) {

	//if(eregi('[^a-zA-Z0-9_]', $v_username))

	if (!preg_match('/^[A-Za-z][A-Za-z0-9]{1,31}$/', $v_username)) {

		// The username contains an inavlid character

		return FALSE;
	} else {

		return TRUE;
	}
}

if (!function_exists('str_ireplace')) {



	function str_ireplace($search, $replace, $subject) {

		$search = preg_quote($search, "/");

		return preg_replace("/" . $search . "/i", $replace, $subject);
	}

}

if (!function_exists('htmlspecialchars_decode')) {



	function htmlspecialchars_decode($text, $ent_quotes = "") {

		$text = str_replace("&quot;", "\"", $text);

		$text = str_replace("&#039;", "'", $text);

		$text = str_replace("&lt;", "<", $text);

		$text = str_replace("&gt;", ">", $text);

		$text = str_replace("&amp;", "&", $text);

		return $text;
	}

}

if (!function_exists('str_split')) {



	function str_split($string, $split_length = 1) {

		$count = strlen($string);

		if ($split_length < 1) {

			return false;
		} elseif ($split_length > $count) {

			return array($string);
		} else {

			$num = (int) ceil($count / $split_length);

			$ret = array();

			for ($i = 0; $i < $num; $i++) {

				$ret[] = substr($string, $i * $split_length, $split_length);
			}

			return $ret;
		}
	}

}

function security($value) {

	if (is_array($value)) {

		$value = array_map('security', $value);
	} else {

		if (!get_magic_quotes_gpc()) {

			$value = htmlspecialchars($value, ENT_QUOTES);
		} else {

			$value = htmlspecialchars(stripslashes($value), ENT_QUOTES);
		}

		$value = str_replace("\\", "\\\\", $value);
	}

	return $value;
}

function escapeChars($value) {

	if (is_array($value))
		$value = array_map('security', $value);
	else
		$value = mysql_real_escape_string($value);



	return $value;
}

function unEscapeChars($value) {

	if (is_array($value))
		$value = array_map('security', $value);
	else
		$value = stripslashes($value);



	return $value;
}

function checkAbuseWord($data = '') {

	$keyarray = array("Asshole", "Blowjob", "Bullshit", "Chink", "Clit", "Cock", "Cum", "Cunt", "Dago", "Fag", "Faggot", "Fuck", "Fucked", "Fucker", "Fucking", "Goddamn", "Heeb", "Jizz", "Kike", "Mofo", "Motherfucker", "Nigga", "Nigger", "Poonani", "Poontang", "Prick", "Pussy", "Shit", "Shitting", "Spic", "Skeet", "Taint", "Tits", "Titties", "Twat", "Wetback", "Wigger");



	/* foreach($keyarray as $val)

	  {

	  $data	=	str_replace(strtoupper($val),"****",strtoupper($data));

	  }

	  $data	=	ucfirst(strtolower($data)); */

	return $keyarray;
}

/// Pagging funtion

function getPagingHtml($count, $limit, $currentPage, $url) {

	if ($count > 0) {

		$p;

		$maxDisplayedPages = 7;

		//var n = 1;



		$strPaging = "";

		$doneDots = false;

		$lastPage = ceil($count / $limit);

		$firstDisplayedPage = min($lastPage - ($maxDisplayedPages - 2), max($currentPage - ($maxDisplayedPages / 2) + 1, 1));

		$lastDisplayedPage;

		if ($firstDisplayedPage == 1)
			$lastDisplayedPage = min($maxDisplayedPages - 1, $lastPage);
		else
			$lastDisplayedPage = min($firstDisplayedPage + $maxDisplayedPages - 2, $lastPage);



		if ($currentPage > 1) {

			$strPaging.="<a class='dbl_prev' href='" . $url . "&pageindex=1'><img src='images/pre2.png' border='0' /></a>&nbsp;";

			$strPaging.="<a class='prev' href='" . $url . "&pageindex=" . ($currentPage - 1) . "'><img src='images/pre.png' border='0' /></a>&nbsp;";
		} else {



			$strPaging.="&nbsp;";
		}

		for ($p = 1; $p <= $lastPage; $p++) {

			if ($currentPage == $p) {

				$strPaging.= "<span class='text-red'><b>$p</b></span> ";
			} else {

				if ($p == 1 || $p == $lastPage || ($p >= $firstDisplayedPage && $p <= $lastDisplayedPage)) {

					$strPaging.= "<a class='page' href='" . $url . "&pageindex=" . $p . "'>$p</a> ";

					$doneDots = false;
				} else if (!$doneDots) {

					$strPaging.= "<span class='page'>... </span>";

					$doneDots = true;
				}
			}
		}



		if ($currentPage < ($count / $limit)) {

			$strPaging.= "<a class='next' href='" . $url . "&pageindex=" . ($currentPage + 1) . "'><img src='images/next.png' border='0' /></a>";



			$strPaging.= "<a class='dbl_next' href='" . $url . "&pageindex=" . ($lastPage) . "'><img src='images/next2.png' border='0' /></a>";
		} else {

			$strPaging.= "&nbsp;";
		}
	}

	return $strPaging;
}

function getSecureFormat($str) {

	$result = "";

	for ($i = 0; $i < strlen($str); $i++)
		$result.="*";



	return $result;
}

function MonthDays($someMonth, $someYear) {

	return date("t", strtotime($someYear . "-" . $someMonth . "-01"));
}

function shortString($str, $num = 50) {

	if (strlen($str) > $num)
		return substr($str, 0, $num) . "...";
	else
		return substr($str, 0, $num);
}

function specialCharRemove($text) {

	$text = str_replace("<?", "", $text);

	$text = str_replace("<!", "", $text);
	
	//$text	=	str_replace("%26","&",$text)
	
	

	return escapeChars($text);
}

function IsInt($int) {



	// First check if it's a numeric value as either a string or number

	if (is_numeric($int) === TRUE) {



		// It's a number, but it has to be an integer

		if ((int) $int == $int) {



			return TRUE;



			// It's a number, but not an integer, so we fail
		} else {



			return FALSE;
		}



		// Not a number
	} else {



		return FALSE;
	}
}

?>