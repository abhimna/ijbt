<?php
class MailerClass {
	function admin_pw_reset_link($_email,$url)
	{
		global $db;
		global $ADMIN_MAIL;
		
		$post	=	array();
		
/*	collect mail body	*/
		$sqlBody	=	" select mail_body as body ,mail_keys as _keys,subject ".
						" FROM mail_body ".
						" where id=3 ";
		
		$rsObjBody	=	$db->query($sqlBody);
		if ($rsObjBody->size()>0)
		{
			$rowBody 	= $rsObjBody->fetch();
	/*	collect varables	*/
			$post['site_name']	=	site_name;
			$post['body']		= 	$rowBody["body"];
			
			$post['pw_reset_url']		= 	$url;
			
				
			
			$post['u_fname'] 		= 	strtoupper('Administrator');
			//$post['_email'] 	= 	$_email;
			//$post['username'] 	= 	$userid;

	/*	collect key and vars	*/	
			
			$sqlKey	=	" SELECT * FROM mail_keyword ".
						" WHERE id IN (".$rowBody["_keys"].")";
			$queryKey=	$db->query($sqlKey);
			
			while($key	=	$queryKey->fetch())
			{
				//echo $key['key']."  k  - ".$key['vars']."  v  ".$post[$key['vars']]."  val<br>";
				
				
				$post['body'] = str_replace("[".$key['key']."]",$key['vars'],$post['body']);
				$post['body'] = str_replace($key['vars'],$post[$key['vars']],$post['body']);
			}
			//echo $post['body'];die;
			$this->send_mail($_email, $rowBody['subject'], $post['body']);
			
			return true;
		}
		
		return false;		
		
	}
	function user_sign_up($userid)
	{	
		global $db;
		global $ADMIN_MAIL;
		$post	=	array();
		$body	=	'';
/*	collect mail body	*/
		$sqlBody	=	" select mail_body as bodys ,mail_keys as _keys,subject ".
						" FROM mail_body ".
						" where id=2 ";
		
		$rsObjBody	=	$db->query($sqlBody);
		if ($rsObjBody->size()>0)
		{
			$rowBody 	= $rsObjBody->fetch();
			$userid		= intval($userid);
	/*	collect varables	*/
			$post['site_name']	=	site_name;
			$post['bodys']		= 	$rowBody["bodys"];
			
				$sql		=	"select fname,lname , email ".
								" FROM users where userid  	= $userid";
				$rsObjRows	=	$db->query($sql);
				$rows 		= 	$rsObjRows->fetch();
			

				 
		
			$post['u_fname'] 		= 	strtoupper($rows["fname"]." ".$rows["lname"]);
			$post['u_email'] 	= 	$rows["email"];
			$post['reg_date'] 	= 	date('Y-m-d g:i A');	
			

	/*	collect key and vars	*/	
			
			$sqlKey	=	" SELECT * FROM mail_keyword ".
						" WHERE id IN (".$rowBody["_keys"].")";
			$queryKey=	$db->query($sqlKey);
			
			while($key	=	$queryKey->fetch())
			{
				//echo $key['key']."  k  - ".$key['vars']."  v  ".$post[$key['vars']]."  val<br>";
				
				$post['bodys'] = str_replace("[".$key['key']."]",$key['vars'],$post['bodys']);
				$post['bodys'] = str_replace($key['vars'],$post[$key['vars']],$post['bodys']);
			}
			//echo $post['bodys'];die;
			$this->send_mail($post['u_email'] , $rowBody['subject'], $post['bodys']);
			return true;
		}
		
		return false;
	}
	
	function user_email_confirm($userid,$url='')
	{
		global $db;
		global $ADMIN_MAIL;
		
		$post	=	array();
		
/*	collect mail body	*/
		$sqlBody	=	" select mail_body as body ,mail_keys as _keys,subject ".
						" FROM mail_body ".
						" where id=1 ";
		
		$rsObjBody	=	$db->query($sqlBody);
		if ($rsObjBody->size()>0)
		{
			$rowBody 	= $rsObjBody->fetch();
			$userid		= intval($userid);
	/*	collect varables	*/
			$post['site_name']	=	site_name;
			$post['body']		= 	$rowBody["body"];
			
			$post['confirm_email_link']		= 	'<a href="'.$url.'">confirm your email</a>';
			
				$sql		=	"select fname,lname , email ".
								" FROM users where userid  	= $userid";
								
				$rsObjRows	=	$db->query($sql);
				$rows = $rsObjRows->fetch();
		
			$post['u_fname'] 	= 	strtoupper($rows["fname"]." ".$rows["lname"]);
			$post['u_email'] 	= 	$rows["email"];

	/*	collect key and vars	*/	
			
			$sqlKey	=	" SELECT * FROM mail_keyword ".
						" WHERE id IN (".$rowBody["_keys"].")";
			$queryKey=	$db->query($sqlKey);
			
			while($key	=	$queryKey->fetch())
			{
				//echo $key['key']."  k  - ".$key['vars']."  v  ".$post[$key['vars']]."  val<br>";
				
				
				$post['body'] = str_replace("[".$key['key']."]",$key['vars'],$post['body']);
				$post['body'] = str_replace($key['vars'],$post[$key['vars']],$post['body']);
			}
			
			$this->send_mail($post['u_email'], $rowBody['subject'], $post['body']);
			return true;
		}
		
		return false;
	}

	function user_pw_reset_link($userid,$url)
	{
		global $db;
		global $ADMIN_MAIL;
		
		$post	=	array();
		
/*	collect mail body	*/
		$sqlBody	=	" select mail_body as body ,mail_keys as _keys,subject ".
						" FROM mail_body ".
						" where id=3 ";
		
		$rsObjBody	=	$db->query($sqlBody);
		if ($rsObjBody->size()>0)
		{
			$rowBody 	= $rsObjBody->fetch();
			$userid		= intval($userid);
	/*	collect varables	*/
			$post['site_name']	=	site_name;
			$post['body']		= 	$rowBody["body"];
			
			$post['pw_reset_url']		= 	$url;
			
				$sql		=	"select fname, lname , email  ".
								" FROM users where userid 	= $userid";
				$rsObjRows	=	$db->query($sql);
				$rows = $rsObjRows->fetch();
			
			$post['u_fname'] 		= 	strtoupper($rows["fname"." ".$rows["lname"]]);
			$post['u_email'] 	= 	$rows["email"];
			//$post['username'] 	= 	$rows["username"];

	/*	collect key and vars	*/	
			
			$sqlKey	=	" SELECT * FROM mail_keyword ".
						" WHERE id IN (".$rowBody["_keys"].")";
			$queryKey=	$db->query($sqlKey);
			
			while($key	=	$queryKey->fetch())
			{
				//echo $key['key']."  k  - ".$key['vars']."  v  ".$post[$key['vars']]."  val<br>";
				
				
				$post['body'] = str_replace("[".$key['key']."]",$key['vars'],$post['body']);
				$post['body'] = str_replace($key['vars'],$post[$key['vars']],$post['body']);
			}
			
		
			$this->send_mail($post['u_email'], $rowBody['subject'], $post['body']);
			
			return true;
		}
		
		return false;		
		
	}
	
	function user_pw_reset_confirm($userid)
	{
		global $db;
		global $ADMIN_MAIL;
		
		$post	=	array();
		
/*	collect mail body	*/
		$sqlBody	=	" select mail_body as body ,mail_keys as _keys,subject ".
						" FROM mail_body ".
						" where id=4 ";
		
		$rsObjBody	=	$db->query($sqlBody);
		if ($rsObjBody->size()>0)
		{
			$rowBody 		= $rsObjBody->fetch();
	/*	collect varables	*/
			$post['site_name']	=	site_name;
			$post['body']		= 	$rowBody["body"];
			
				$sql		=	"select u_fname , u_lname , u_email ".
								" FROM users where user_id 	= $userid";
								
				$rsObjRows	=	$db->query($sql);
				$rows = $rsObjRows->fetch();
			
			$post['u_fname'] 		= 	strtoupper($rows["u_fname"]." ".$rows["u_lname"]);
			$post['u_email'] 	= 	$rows["u_email"];
			//$post['username'] 	= 	$rows["username"];

	/*	collect key and vars	*/	
			
			$sqlKey	=	" SELECT * FROM mail_keyword ".
						" WHERE id IN (".$rowBody["_keys"].")";
			$queryKey=	$db->query($sqlKey);
			
			while($key	=	$queryKey->fetch())
			{
				//echo $key['key']."  k  - ".$key['vars']."  v  ".$post[$key['vars']]."  val<br>";
				
				
				$post['body'] = str_replace("[".$key['key']."]",$key['vars'],$post['body']);
				$post['body'] = str_replace($key['vars'],$post[$key['vars']],$post['body']);
			}
			
				
			$this->send_mail($post['u_email'], $rowBody['subject'], $post['body']);
			
			return true;
		}
		
		return false;		
		
	}
	
	function suport($post = array())
	{
		global $db;
		 $sqlBody	=	" select mail_body as body ,subject ".
						" FROM mail_body ".
						" where id=5 ";
		$rsObjBody	=	$db->query($sqlBody);
		
		if ($rsObjBody->size()>0)
		{
			$rowBody 		= $rsObjBody->fetch();
			$post['bodys']	= stripslashes($rowBody['body']);
			$post['bodys'] 	= str_replace("[personal-name]",$post['name'],$post['bodys']);
			$post['bodys'] 	= str_replace("[personal-email]",$post['email'],$post['bodys']);
			$post['bodys'] 	= str_replace("[personal-comment]",$post['comment'],$post['bodys']);
			$post['bodys'] 	= str_replace("[personal-date]",$post['date'],$post['bodys']);			
			//echo $post['bodys'];
			//die;
			$this->send_mail('',$post['subject'],$post['bodys']);
			return true;
		}
		return false;
	}
	
	
	function send_mail($mailto, $mailubject, $mailbody, $mailfrom='') 
	{	global $ADMIN_MAIL,$ADMIN_MAIL2;	
	
		$mailbody	=	stripslashes($mailbody);
		if($mailto=='')
			$mailto	=	$ADMIN_MAIL;
			
		if($mailfrom=='')
			$mailfrom	=	$ADMIN_MAIL;
			
		$replytomail	=	$ADMIN_MAIL2;
		
		$headers = "MIME-Version: 1.0" . "\n";
		$headers .= "Content-type: text/html; charset=iso-8859-1" . "\n";
        $headers .= "X-Priority: 1 (Higuest)\n";
        $headers .= "X-MSMail-Priority: High\n";
        $headers .= "Importance: High\n";
		$headers .= "From: <$mailfrom>" . "\n";	
		$headers .= "Return-Path: <$mailfrom>" . "\n";
		$headers .= "Reply-To: <$replytomail>";

		@mail($mailto, $mailubject, $mailbody, $headers);	
		//die;
		/*echo "mail to : ".$mailto;
		echo "<br>mail from : ".$mailfrom;
		echo "<br>mail sub : ".$mailubject;
		echo "<br>mail body : ".$mailbody;
		echo "<br><br><br><br>";
		die;*/
	
	}
	
	
	

	
}