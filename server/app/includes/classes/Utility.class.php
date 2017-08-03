<?php
class Utility{
    public static $pageLimit=15;

    public static  function makePager($page=1,$total=0){
        $page      =  (int)$page==0 ? 1 :(int)$page;
        $pageLower =  ($page-1)*self::$pageLimit;
        $pageUpper =  self::$pageLimit;
        $total_page=  ceil($total/self::$pageLimit);
        return (object)array("total_page"=>"$total_page",
                            "total_records"=>"$total",
                            "lowerLimit"=>$pageLower,
                            "upperLimit"=>$pageUpper);
    }
    
    public static  function stringWithDot($str,$count=30){
        if(strlen($str)>=$count){
         $str=   substr($str, 0,$count)."...";
        }
       
        return strval(($str));
    }
    
    public static  function getTimeAgo($time,$newTime="",$timeType="x"){
                $oldTime	=	date("Y-m-d H:i:s",strtotime($time));
                if($newTime==""){
                   $newTime= date('Y-m-d H:i:s');
                }
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
    
    
}
