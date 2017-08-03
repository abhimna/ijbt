<?php

class USER_CLASS extends MISC_CLASS {

    public static function getTimeAgo($oldTime = "2015-02-10 10:11:11") {

        $timeCalc = strtotime(date("Y-m-d H:i:s", time())) - strtotime($oldTime);
        $timeArr = array("min" => 60,
            "hour" => 3600,
            "day" => 86400,
            "week" => 604800,
        );
        foreach ($timeArr as $time => $timeCount)
            $ago = $timeCalc > $timeCount ? round($timeCalc / $timeCount) . " $time ago" : $ago;
        return $ago;
    }

    public static function getTimeInfo($oldTime, $newTime, $timeType = "x") {
        global $db;

        $timeCalc = strtotime($newTime) - strtotime($oldTime);
        if ($timeType == "x") {
            if ($timeCalc > 60) {
                $timeType = "i";
            }
            if ($timeCalc > (60 * 60)) {
                $timeType = "h";
            }
            if ($timeCalc > (60 * 60 * 24)) {
                $timeType = "d";
            }
            if ($timeCalc > (60 * 60 * 24 * 7)) {
                $timeType = "w";
            }
            if ($timeCalc > (60 * 60 * 24 * 7)) {
                $timeType = "w";
            }
        }
        if ($timeType == "s") {
            
        }
        if ($timeType == "i") {

            if (round($timeCalc / 60) <= 1)
                $timeCalc = round($timeCalc / 60) . " min ago";
            else
                $timeCalc = round($timeCalc / 60) . " mins ago";
        }
        elseif ($timeType == "h") {

            if (round($timeCalc / 60 / 60) <= 1) {
                $timeCalc = round($timeCalc / 60 / 60) . " hour ago";
            } else {
                $timeCalc = round($timeCalc / 60 / 60) . " hours ago";
            }
        } elseif ($timeType == "d") {

            if (round($timeCalc / 60 / 60 / 24) <= 1) {
                $timeCalc = round($timeCalc / 60 / 60 / 24) . " day ago";
            } else {
                $timeCalc = round($timeCalc / 60 / 60 / 24) . " days ago";
            }
        } elseif ($timeType == "w") {

            if (round($timeCalc / 60 / 60 / 24 / 7) <= 1) {
                $timeCalc = round($timeCalc / 60 / 60 / 24 / 7) . " week ago";
            } else {
                $timeCalc = round($timeCalc / 60 / 60 / 24 / 7) . " weeks ago";
            }
        } else {
            $timeCalc = " Just now";
        }
        return $timeCalc;
    }

    function verifyUser($rs) {
        global $db;
        $status = "false";
        $username = $rs[0]->username;
        $email = $rs[0]->email;


        if (!validate::checkValidUserName($username))
            $msg = $this->msg;
        elseif (!validate::checkAllowedName($username))
            $msg = $this->msg;
        elseif (!validate::checkValidEmail($email))
            $msg = $this->msg;
        else {
            $msg = "Available";
            $status = "true";
        }
        $arr = array("message" => $msg, "status" => $status);
        return $arr;
    }

    function resendVerificationMail($rs) {
        global $db;
        $status = "false";
        $email = $rs[0]->email;
        if ($email != '') {
            /* Check Valid Email From database */
            if (!is_email_address($email) and $email != "") {
                $msg = "Not a valid email";
                $status = "false";
            } else {

                $sql_user = "SELECT * FROM users WHERE trim(upper(email))='" . strtoupper($email) . "' AND token!=''";
                $exe_user = $db->query($sql_user);
                if ($exe_user->size() > 0) {
                    $res_user = $exe_user->fetch();
                    $user_id = $res_user['userid'];
                    $code = $res_user['token'];
                    $name = $res_user['name'];

                    $link = base_path . "activate.php?email=" . $email . "&token=" . $code;
                    $post = '';
                    $post.="<div style=' width:700px; margin:0 auto'> ";
                    $post.="<div style='background:#fff; border:#888 solid 1px; border-radius:15px; margin-top:20px; padding:20px; box-shadow:0 0 8px #999'>";
                    $post.="<div style='border-bottom:1px solid #ccc; padding-bottom:18px;'> ";
                    $post.="<img style=' width:250px;' src='" . base_path . "uploads/logo.jpg' /></div> ";
                    $post.="<div style='font-size:16px; padding-top:20px;'>";
                    $post.="<p>Hi " . $name . ",</p>";
                    $post.="<p>Your registered email is: " . $email . "</p>";
                    $post.="<p>Please click the link below to confirm the activation of your account.</p>";
                    $post.="<p>Link: " . $link . "</p>";
                    $post.="<p>Thank you,</p>";
                    $post.="<p>IJBT  Team</p>";
                    $post.="</div> </div> </div> ";

                    send_mail($email, "Registration Confirmation Required", $post);



                    $msg = " A confirmation mail send to your registered email id.Please verify it in order to use application";
                    $status = "true";
                } else {
                    $msg = "Email id doesn't exist or you have already activated";
                    $status = "false";
                }
            }
        } else {
            $msg = "Please enter email id";
            $status = "false";
        }
        $arr = array("message" => $msg, "status" => $status);
        return $arr;
    }

    function signUp($rs) {
        global $db;

        $status = "false";
        $status = "false";
        $fname = addslashes(utf8_encode($rs['fname']));
        $lname = addslashes(utf8_encode($rs['lname']));
        $username = addslashes(utf8_encode($rs['username']));
        $email = addslashes(utf8_encode($rs['email']));
        $pass = addslashes(utf8_encode($rs['password']));
        $dob = addslashes(utf8_encode($rs['dob']));
        $gender = addslashes(utf8_encode($rs['gender']));
        $zipcode = addslashes(utf8_encode($rs['zipcode']));
        $thumb_image = addslashes(utf8_encode($rs['thumb_image']));
        $full_image = addslashes(utf8_encode($rs['thumb_image']));
        $device_token = addslashes(utf8_encode($rs['device_token']));
        $device_type = addslashes(utf8_encode($rs['device_type']));

        if (!validate::checkValidUserName($username)) {
            $msg = $this->msg;
        } else if (!validate::checkValidEmail($email)) {
            $msg = $this->msg;
        } elseif (!validate::checkValidPassword($pass)) {
            $msg = $this->msg;
        } else {

            $code = randomcode(50);
            $sql = "INSERT into users(user_name,fname,pass,email,user_thumbimage,user_image,device_token,token,allow_follow,dtdate)VALUES(" .
                    "'" . $username . "'," .
                    "'" . $fname . "'," .
                    "'" . md5($pass) . "'," .
                    "'" . $email . "'," .
                    "'" . $thumb_image . "'," .
                    "'" . $full_image . "'," .
                    "'" . $device_token . "'," .
                    "'" . $code . "'," .
                    "'0'," .
                    " '" . nowDateTime() . "' " .
                    ")";

            $result = $db->query($sql);
            $insertId = $result->insertID();
            
            $username = urldecode(utf8_decode($username));
            
            UserDevices::remove("device_token='$device_token'");

            UserDevices::addDevice($insertId, $device_token, $device_type);
             if (count($_FILES)) {
                $ar = $this->uploadImg($_FILES['photo']['tmp_name'], $_FILES['photo']['name'], $insertId);
            }
            $link = base_path . "activate.php?email=" . $email . "&token=" . $code;
            $post = '';
            $post.="<div style=' width:700px; margin:0 auto'> ";
            $post.="<div style='background:#fff; border:#888 solid 1px; border-radius:15px; margin-top:20px; padding:20px; box-shadow:0 0 8px #999'>";
            $post.="<div style='border-bottom:1px solid #ccc; padding-bottom:18px; background:none repeat scroll 0 0 #aeaeae;text-align:center;'> ";
            $post.="<img style=' width:250px;' src='" . admin_path . "/images/logo.png' /></div> ";
            $post.="<div style='font-size:16px; padding-top:20px;'>";
            $post.="<p>Hello " . $username . ",</p>";


            //$post.="<p>Date: ".date("m-d-Y h:i:s A")."</p> ";
            //$post.="<p>Name: ".$name."</p>";
            $post.="<p>Your registered email is: " . $email . "</p>";
            $post.="<p>Please click the link below to confirm the activation of your account.</p>";
            $post.="<p>Link: <a href='$link' style='word-wrap:break-word;'>" . $link . "</a></p>";
            $post.="<p>Thank you,</p>";
            $post.="<p>IJBT Team</p>";
            $post.="</div> </div> </div> ";

			send_mail($email, "Registration Confirmation Required", $post);



            $msg = "You have successfully registered.";
            $status = "true";
        }
        $insertId = $insertId != '' ? $insertId : '';
        $arr = array("userid" => $insertId, "user_name" => $username, "message" => $msg, "status" => $status);
        return $arr;
    }

    function createDir($path) {
        return ((!is_dir($path)) ? @mkdir($path, 0777) : TRUE);
    }

    function uploadImg($img, $img_name, $userid) {

        global $db;
        $status = "false";
        $USER = new USER_CLASS;
        $userName = $userid;
        $filePath = "";
        $msg = '';

        if ($userid == 0) {
            $msg = "Invalid operation userid ";
            $status = "false";
        } else {
            $photo_dir = "../uploads";
            $dirCreated = ($this->createDir($photo_dir));
            $filePath .="uploads";

            $photo_dir.="/$userName/";
            $dirCreated = ($this->createDir($photo_dir));
            $filePath .="/" . $userName . "/";


            if ($img == "") {
                $msg = "File not found";
                return array("message" => $msg, "status" => $status);
            }

            if (!$dirCreated) {
                $msg = "An error occured to upload image! please try again";
                $status = "false";
            } else {

                if (count(glob($photo_dir . $img_name)) > 0) {
                    $random_digit = rand(000, 999);
                    $ext = pathinfo($img_name, PATHINFO_EXTENSION);
                    $flName = basename($img_name, "." . $ext) . $random_digit;
                    $flNameSmall = ($flName . "-thumb." . $ext);
                    $flName = ($flName . "." . $ext);
                } else {
                    $ext = pathinfo($img_name, PATHINFO_EXTENSION);
                    $flName = basename($img_name, "." . $ext);
                    $flNameSmall = ($flName . "-thumb." . $ext);
                    $flName = ($flName . "." . $ext);
                }



                $target_ori = "{$photo_dir}{$flName}";
                $target_small = "{$photo_dir}{$flNameSmall}";
                $bigphoto = "{$flName}";

                $IMAGE = new UPLOAD_IMAGE();
                $IMAGE->init($img);
                //$path=createDir();
                //$IMAGE->saveImage($target_ori, 100);
                move_uploaded_file($img, $target_ori);

                $extension = strrchr($target_small, '.');
                $extension = strtolower($extension);
                //if($extension == '.png')
                //{
                //	$flNameSmall	=	$flName;
                //}else
                {
                    $IMAGE->resizeImage(200, 200, 'crop');
                    $IMAGE->saveImage($target_small, 100);
                }

                $sql = " UPDATE users SET " .
                        " user_image	=	'" . $filePath . $flName . "'," .
                        " user_thumbimage	=	'" . $filePath . $flNameSmall . "' " .
                        " where userid	=" . $userid;

                $result = $db->query($sql);

                $status = "true";
                $msg = "Successfully";
            }
            $arr = array("message" => $msg, "status" => $status);
        }
    }

    function login($rs) {
        global $db;
        $status = "false";
        $detail = array();
        $email_verify = 0;
        $suspend = "false";
        $ar = new stdClass;
        $username = addslashes(utf8_encode($rs->username));
        $pass = addslashes(utf8_encode($rs->password));
        $device_token = addslashes(utf8_encode($rs->device_token));

        $device_type = strtoupper(addslashes(utf8_encode($rs->device_type)));
        $email_set = "";
        if ($username != "" && $pass != "") {
            $sql = "select * from users where UPPER(user_name)='" . strtoupper($username) . "' AND pass='" . md5($pass) . "'";

            $result = $db->query($sql);
            $rsCount = $result->size();
            if ($rsCount > 0) {
                $rs = $result->fetch();

//                if ($rs['email_verify'] == "0") {
//                    $msg = "Please activate your account on registered email address";
//                    $email_set = $rs['email'];
//                } 
                if ($rs['status'] == 0 && $rs['admin_deactivate'] == 0) {
                    $userid = $rs['userid'];
                    Users::save(array("status" => "1"), "userid='$userid'");
                    $msg = "Your account re-activated succesfully";
                    $suspend = "true";
                    $email_verify = $rs['email_verify'];

                    $status = "true";

                    $userid = $rs['userid'];
                    $username = $rs['user_name'];
					$fname = $rs['fname'];
					$lname = $rs['lname'];
					$flname= $fname."".$lname;
                    $ar->userid = $rs['userid'];
                    UserDevices::remove("device_token='$device_token'");
                    UserDevices::addDevice($userid, $device_token, $device_type);

                    $detail = $this->getProfile($ar);
                    if ($detail['status'] == 'true')
                        $userdetail = $detail['userdetail'];
                }
                else if ($rs['status'] == 0 && $rs['admin_deactivate'] == 1) {
                    $msg = "Your account is suspended by IJBT team";
                } else {

                    $email_verify = $rs['email_verify'];
                    $msg = "Login successfull";
                    $status = "true";

                    $userid = $rs['userid'];
                    $username = $rs['user_name'];
                    $ar->userid = $rs['userid'];
                    UserDevices::remove("device_token='$device_token'");
                    UserDevices::addDevice($userid, $device_token, $device_type);

                    $detail = $this->getProfile($ar);
                    if ($detail['status'] == 'true')
                        $userdetail = $detail['userdetail'];
                }
            } else
                $msg = "Not a valid username or password";
        }
        else {
            $msg = "Invalid credential";
        }
        //$arr=array("message"=>$msg, "userid"=>$userid,"detail"=>$userdetail,"email_verify"=>"".$email_verify."","status"=>$status);
        if ($status == 'false') {
            $arr = array("message" => $msg, "status" => $status, "email" => $email_set);
        } else {
            $arr = array("message" => $msg, "suspend" => $suspend, "userid" => $userid, "user_name" => $flname, "status" => $status);
        }
        return $arr;
    }

    /* FACEBOOK LOGIN */

    function getAvailableUsername($username) {
        $username = str_replace(' ', '', $username);
        if (!validate::checkValidUserName($username)) {
            $username.= randomcode(4);
            $this->getAvailableUsername($username);
        }
        return $username;
    }

    

    function facebookLogin($rs) {

        global $db;
        $status = "false";
        $detail = array();
        $email_verify = 0;
        $ar = new stdClass;
        //print_r($rs); die;
        $fname = (trim($rs['fname']));
        $lname = (trim($rs['lname']));
        $email = addslashes(utf8_encode($rs['email']));
        $gender = addslashes(utf8_encode($rs['gender']));
        $dob = addslashes(utf8_encode($rs['dob']));
        $device_token = addslashes(utf8_encode($rs['device_token']));
        $facebookid = addslashes(utf8_encode($rs['facebookid']));
        $device_type = addslashes(utf8_encode($rs['device_type']));
        $image = $rs['image'];



        $sql = "select * from users where facebook_id='" . $facebookid . "'";
        $result = $db->query($sql);
        $rsCount = $result->size();
        if ($rsCount > 0) {

            $rs = $result->fetch();
//			if ($rs['status'] == 0) {
//				$msg = "Your account is deactived!";
//				$status == 'false';
//			}
            if ($rs['status'] == 0 && $rs['admin_deactivate'] == 0) {
                $userid = $rs['userid'];
                Users::save(array("status" => "1"), "userid='$userid'");
                $msg = "Your account re-activated succesfully";
                $suspend = "true";
                $email_verify = $rs['email_verify'];

                $status = "true";

                $userid = $rs['userid'];
                $username = $rs['user_name'];
                $ar->userid = $rs['userid'];
                UserDevices::remove("device_token='$device_token'");
                UserDevices::addDevice($userid, $device_token, $device_type);

                $detail = $this->getProfile($ar);
                if ($detail['status'] == 'true')
                    $userdetail = $detail['userdetail'];
            }
            else if ($rs['status'] == 0 && $rs['admin_deactivate'] == 1) {
                $msg = "Your account is suspended by IJBT team";
            } else {
                $email_verify = $rs['email_verify'];
                $status = "true";
                $login_status = "1";
                $msg = "Login successfull";
                $userid = $rs['userid'];
                $username = $rs['user_name'];
                $ar->userid = $rs['userid'];
                UserDevices::remove("device_token='$device_token'");
                UserDevices::addDevice($userid, $device_token, $device_type);
                $detail = $this->getProfile($ar);
                if ($detail['status'] == 'true')
                    $userdetail = $detail['userdetail'];
            }
        } else {
           // if ($email != '' && !validate::checkValidFbEmail($email, $facebookid)) {
              //  $msg = $this->msg;
            //} else {
                $chars = "0123456789";
                $u_pass_ml = substr(str_shuffle($chars), 0, 4);
                $pass = md5($u_pass_ml);
                $uname = $this->getAvailableUsername($fname . $lname);

                if ($_FILES['image']['tmp_name'] != '') {
                    $typeimg = 'upload';
                }
                $code = randomcode(50);
                $sql = "INSERT into users(facebook_id,user_name,fname,lname,email,gender,dob,email_verify,status,allow_follow,dtdate)VALUES(" .
                        "'" . $facebookid . "'," .
                        "'" . $uname . "'," .
                        "'" . $fname . "'," .
                        "'" . $lname . "'," .
                        "'" . $email . "'," .
                        "'" . $gender . "'," .
                        "'" . $dob . "'," .
                        "'1'," .
                        "'1'," .
                        "'0'," .
                        " NOW() " .
                        ")";
                $result = $db->query($sql);
                $insertId = $result->insertID();
                UserDevices::remove("device_token='$device_token'");
                UserDevices::addDevice($insertId, $device_token, $device_type);
                if (count($_FILES)) {
                    $ar = $this->uploadImg($_FILES['image']['tmp_name'], $_FILES['image']['name'], $insertId);
                }
                $userid = $insertId;
                $username = Users::userInfo($userid)->user_name;
                $login_status = "0";
                $msg = "Login successfully With new register user.";
                $status = "true";
           // }
        }

        if ($status == 'false') {
            $arr = array("message" => $msg, "status" => $status);
        } else {
            $arr = array("message" => $msg, "userid" => $userid, "login_status" => $login_status, "user_name" => $username, "status" => $status);
        }
        return $arr;
    }

    function changePassword($rs) {
        global $db;
        $status = "false";
        $userid = $rs->userid;
        $pass = escapeChars($rs->newpass);
        $oldpass = escapeChars($rs->oldpass);
        $msg = "";
        if ($userid == "" || $userid <= 0 || $pass == "" || $oldpass == "") {
            $msg = "Invalid credentials";
        } else if (!validate::checkValidUser($userid)) {
            $msg = $this->msg;
        } else {
            $sql = "select * from users where UPPER(pass)='" . md5($oldpass) . "' AND userid = '" . $userid . "'";
            $res = $db->query($sql);
            if ($res->size() > 0) {
                $rows = $res->fetch();

                $sqlup = "UPDATE  users SET
                        pass 	=	'" . md5($pass) . "'
                        WHERE userid	= '" . $rows['userid'] . "' ";

                $db->query($sqlup);

                $msg = "Your password successfully changed.";
                $status = "true";
            } else
                $msg = "Enter valid old password.";
        }
        $arr = array("message" => $msg, "status" => $status);
        return $arr;
    }

    function forgotPassword($rs) {
        global $db;
        $email_verify = "0";
        $status = "false";
        $msg = "";
        $username = escapeChars($rs->email);
        $userid = validate::validateUserName($username);
        if (!$userid)
            $msg = "Not a valid email";
        else {
            $pass = randomcode(6);
            $code = randomcode(50);
            $userInfo = $this->getUserInfo($userid);


            $sqldele = "delete from reset_password where userid =" . $userid;
            $db->query($sqldele);

            $sql = "INSERT INTO reset_password SET  " .
                    " userid 	= 	'" . $userid . "'," .
                    " email 	= 	'" . $userInfo['email'] . "'," .
                    " pass		=	'" . $pass . "'," .
                    " token 	= 	'" . $code . "'," .
                    " dtdate	=	NOW()";
            $db->query($sql);

            $link = base_path . "change-password.php?type=forgot&email=" . $userInfo['email'] . "&token=" . $code;


           $post = '';
                $post.="<div style=' width:700px; margin:0 auto'> ";
                $post.="<div style='background:#fff; border:#888 solid 1px; border-radius:15px; margin-top:20px; padding:20px; box-shadow:0 0 8px #999'>";
                $post.="<div style='border-bottom:1px solid #ccc; padding-bottom:18px; background:none repeat scroll 0 0 #aeaeae;text-align:center;'> ";
                $post.="<img style=' width:250px;' src='" . admin_path . "/images/logo.png' /></div> ";
                $post.="<div style='font-size:16px; padding-top:20px;'>";
            $post.="<p>Hello " . $userInfo['user_name'] . ",</p>";
            //$post.="<p>Date: ".date("m-d-Y h:i:s A")."</p> ";
            //$post.="<p>Name: ".$userInfo['name']."</p>";
            $post.="<p>Your registered email is: " . $userInfo['email'] . "</p>";
            $post.="<p>This link is only valid for the next 72 hours.</p>";
            $post.="<p>Link: <font color='#0030ff'><a href=" . $link . " style='word-wrap:break-word;'>" . $link . "</a></font></p>";
            $post.="<p>Thank you,</p>";
            $post.="<p>IJBT Team</p>";
            $post.="</div> </div> </div> ";



            send_mail($userInfo['email'], "Forgot Password Request", $post);

            $msg = "An email for forgot password request has been sent.";
            $status = "true";
        }
        $arr = array("message" => $msg, "userid" => "" . $userid . "", "email_verify" => $email_verify, "status" => $status);
        return $arr;
    }

    function getUserAllPost($userid) {
        global $db;
        $status = "false";
        $msg = '';
        $USER = new USER_CLASS;
        ;

        $comment = array();
        $info = array();

        $latestcomment = array();
        if ($userid <= 0 || $userid == "")
            $msg = "Invlid user or post";
        else {

            $sql = "select * from post where user_id in(select userid from users WHERE userid='" . $userid . "') ORDER BY post_id DESC";
            $result = $db->query($sql);




            if ($result->size() > 0) {
                while ($rs = $result->fetch($sql)) {
                    $userInfo = $USER->getUserInfo($rs['user_id']);

                    $info['userid'] = $userInfo['userid'];
                    /* $info['name'] = $userInfo['name'];
                      $info['username'] = $userInfo['user_name'];
                      $info['fullname'] = $userInfo['name'];
                      $info['userphoto'] = $userInfo['user_thumbimage']; */

                    $info['postid'] = $rs['post_id'];
                    $info['posttile'] = $rs['title'];
                    $info['post_img'] = $rs['image'];
                    $info['post_thumb_img'] = $rs['thumb_image'];

                    $comment[] = $info;
                }
            } else
                $msg = "Posts not found.";
        }
        $arr = $comment;
        return $arr;
    }

    function getProfile($rs) {
        global $db;
        $status = "false";
        $userArray = array();
        $postdetail = array();

        $userid = intval($rs->userid);
        $friendid = $rs->friendid;


        $pageNo = $rs->page;

        if (!$userid && !$friendid)
            $msg = "Not a valid username";
        else {
            if ($rs->strUserType == "STRING")
                $friendid = Users::get("user_name='$friendid'", "userid")->userid;


            $condBlock = " and userid not in(select cub.user_id from user_block  as cub where cub.block_user_id='$userid' )";
            $condDeact = Users::deactivateUserCond($friendid, "userid");
            $sql = "select * from users where userid='" . $friendid . "' $condBlock  $condDeact";


            $result = $db->query($sql);
            if ($result->size() > 0) {

                $rs = $result->fetch();
				$friendid = $rs['userid'];
                if ($rs['user_image'] == '') {

                    $rs['user_image'] = "uploads/default_img.png";
                }
                if ($rs['user_thumbimage'] == '') {

                    $rs['user_thumbimage'] = "uploads/default_img.png";
                }
                $rs['totalfollower'] = Follow::count("follow_to=$friendid and confirmed='1'")->count;
                $rs['totalrequests'] = Follow::count("follow_to=$friendid and confirmed='-1'")->count;
                $rs['totalfollowing'] = Follow::count("follow_from=$friendid and confirmed='1'")->count;
                $isfollow = Follow::followStatus($friendid, $userid);
                $rs["posts"] = Posts::userPostData($friendid, $pageNo);
                $rs['is_blocked'] = UserBlock::count("user_id='$userid' and block_user_id='$friendid'")->count > 0 ? 1 : 0;
                $rs['is_following'] = $isfollow == "no" ? "0" : $isfollow;

                $rs["is_private"] = $rs["is_private"];
                $userArray = $rs;
                $msg = "success";
                $status = "true";
            } else
                $msg = "Record not found";
        }
        $arr = array("message" => $msg, "userdetail" => $userArray, "status" => $status);
        return $arr;
    }

    function getOtherProfile($rs) {
        global $db;
        $status = "false";
        $userArray = array();

        $loginid = $rs->loginid;
        $userid = $rs->userid;
        if (!$userid)
            $msg = "Not a valid username";
        else {
            $sql = "select * from users where userid=" . $userid;
            $result = $db->query($sql);
            if ($result->size() > 0) {
                $rs = $result->fetch();
                if ($rs['user_image'] == '') {

                    $rs['user_image'] = "uploads/default_img.png";
                }
                if ($rs['user_thumbimage'] == '') {

                    $rs['user_thumbimage'] = "uploads/default_img.png";
                }
                /* $rs['totalfollower']	=	$this->getTotalUserFollower($userid);
                  $rs['totalfollowing']	=	$this->getTotalUserFollowing($userid);
                  $rs['totalimage']	=	$this->getTotalUserPost($userid,'IMAGE');
                  $rs['totalvideo']	=	$this->getTotalUserPost($userid,'VIDEO');
                  $rs['totalaudio']	=	$this->getTotalUserPost($userid,'AUDIO');
                  $rs['follow_status']	=	$this->getUserStatus($loginid,$userid); */
                $userArray = $rs;
                $status = "true";
            } else
                $msg = "Record not found";
        }
        $arr = array("message" => $msg, "userdetail" => $userArray, "status" => $status);
        return $arr;
    }

    function getTotalUserPost($userid) {
        global $db;
        $sql = "select post_id from post where user_id='" . $userid . "' #AND posttype ='" . $type . "'";
        $result = $db->query($sql);

        return "" . intval($result->size()) . "";
    }

    function getUserStatus($Loginid, $userid) {
        global $db;
        $status = 0;
        $sql = "select * from user_follow where follower_id=" . $Loginid . " AND status= 1 and following_id=" . $userid . "";
        $result = $db->query($sql);
        if ($result->size() > 0)
            $status = 1;
        return "" . $status . "";
    }

    function getTotalUserFollower($userid) {
        global $db;
        $count = 0;
        $sql = "select count(follower_id) as cnt from user_follow where following_id =" . $userid . " AND status = 1";
        $result = $db->query($sql);
        $rs = $result->fetch();
        if (!is_null($rs['cnt']))
            $count = $rs['cnt'];

        return $count;
    }

    function getTotalUserFollowing($userid) {
        global $db;
        $count = 0;
        $sql = "select count(following_id) as cnt from user_follow where follower_id =" . $userid . " and status=1";
        $result = $db->query($sql);
        $rs = $result->fetch();
        if (!is_null($rs['cnt']))
            $count = $rs['cnt'];

        return $count;
    }

    function editProfile($rs) {
        global $db;
        $status = "false";
        $userArray = array();

        $userid = $rs['userid'];
        $fname = addslashes(utf8_encode($rs['fname']));
        $lname = addslashes(utf8_encode($rs['lname']));
        $username = addslashes(utf8_encode($rs['username']));
        $phone = addslashes(utf8_encode($rs['phone']));
        $email = addslashes(utf8_encode($rs['email']));
        $gender = addslashes(utf8_encode($rs['gender']));
        $location = addslashes(utf8_encode($rs['location']));
        $account_status = addslashes(utf8_encode($rs['is_private']));
        $user_bio = specialCharRemove(trim($rs['user_bio']));
        $thumb_image = specialCharRemove(trim($rs['thumb_image']));

        if (!validate::checkValidUserName($username, $userid)) {
            $msg = $this->msg;
        } else if (!validate::checkValidUser($userid)) {
            $msg = $this->msg;
        } else {
            $sql = "update users set fname		='" . $fname . "'," .
                    "lname		='" . $lname . "'," .
                    "user_bio	='" . $user_bio . "'," .
                    "email		='" . $email . "'," .
                    "user_name	='" . $username . "'," .
                    "gender		='" . $gender . "'," .
                    "phone	='" . $phone . "'," .
                    "location		='" . $location . "'," .
                    "is_private		='" . $account_status . "' 
					where userid='" . $userid . "'";

            $result = $db->query($sql);

            if (count($_FILES)) {

                $ar = $this->uploadImg($_FILES['thumb_image']['tmp_name'], $_FILES['thumb_image']['name'], $userid);
            }
            $msg = "Profile updated successfully";
            $status = "true";
        }
        $arr = array("message" => $msg, "status" => $status);
        return $arr;
    }

}

class validate {

    var $msg = "";

    function checkValidUser($userid) {
        global $db;
        if (intval($userid) > 0) {
            $sql = "select * from users where userid='" . intval($userid) . "'";
            $result = $db->query($sql);
            if ($result->size() <= 0) {
                $this->msg = "Not a valid user";
                unset($result);
                return false;
            }
        }
        /* else{
          $this->msg = " Not a valid user";
          return false;
          } */

        return true;
    }

    function checkValidUserName($userName, $userid) {
        global $db;
//        if (validate_username($userName) and $userName != "") {
        if ($userName != "") {
            $sql = "select * from users where trim(upper(user_name))='" . strtoupper($userName) . "' AND userid!='" . $userid . "'";
            $result = $db->query($sql);
            if ($result->size() > 0) {
                $this->msg = "Username already exist.Please try with other username";
                unset($result);
                return false;
            }
        } else {
            $this->msg = " Not a valid username";
            return false;
        }

        return true;
    }

    function checkValidEmail($email, $userid) {
        global $db;
        //$email=str_replace("%40","@",$email);
        //echo $email; die;
        if (is_email_address($email) and $email != "") {
//        if (is_email_address($email) and $email != "") {
            $sql = "select * from users where trim(upper(email))='" . strtoupper($email) . "' AND userid!='" . $userid . "'";
            $result = $db->query($sql);

            if ($result->size() > 0) {
                $this->msg = "The email address entered is already registered";
                unset($result);
                return false;
            }
        } else {
            $this->msg = " Not a valid email";
            return false;
        }
        return true;
    }

    function checkValidFbEmail($email, $fbid) {
        global $db;
        //$email=str_replace("%40","@",$email);
        //echo $email; die;
        if (is_email_address($email) and $email != "") {
            $sql = "select * from users where trim(upper(email))='" . strtoupper($email) . "' AND userid!='" . $userid . "'";
            $result = $db->query($sql);

            if ($result->size() > 0) {
                $this->msg = "The email address entered is already registered";
                unset($result);
                return false;
            }
        } else {
            $this->msg = " Not a valid email";
            return false;
        }
        return true;
    }

    function checkValidPassword($pass) {
        global $db;
        if ($pass == "" || strlen($pass) < 4) {
            $this->msg = "Invalid password or password length ";
            return false;
        }
        return true;
    }

    function validateUserName($userName) {
        global $db;
        $userID = 0;
        $sql = "select * from users where UPPER(email)='" . strtoupper($userName) . "'";
        $result = $db->query($sql);
        if ($result->size() > 0) {
            $rs = $result->fetch();
            $userID = $rs['userid'];
        }
        return $userID;
    }

    function checkAllowedName($uname) {
        global $db;
        $restricted_nm = array("IJBT", "admin", "administrator", "team");
        $uname = strtolower($uname);
        $allowed = true;
        foreach ($restricted_nm as $rest) {
            $rest = strtolower($rest);
            if (strpos($uname, $rest) !== false) {
                $this->msg = "Username already exist.Please try with other username";
                $allowed = FALSE;
            }
        }
        return $allowed;
    }

}
