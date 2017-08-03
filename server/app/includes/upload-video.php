<?php
set_time_limit(0);
ini_set('display_errors', 1);
error_reporting(E_ERROR);
class UPLOAD_VIDEO {
	
	function createDir($path){
		
		return ((!is_dir($path)) ? mkdir($path,0777,true):TRUE);
			
	}
	
	function uploadVideoAndroid()
		{
			$video 				= 	'';
			$user_id 			= 	$_REQUEST['user_id'];
			$totalChunk 		=  	$_REQUEST['totalchunk'];
			$count				= 	$_REQUEST['count'];
			$filenameOrignal	= 	$_REQUEST['file_name'];
			$uniqueFileName		=	$_REQUEST['unique_name'];
			$filePath			=	"";
			
			           
            $dir="../tmp_video";
			
			$dirCreated=($this->createDir($dir)); 
			$filePath .="tmp_video";
			
			$dir.="/$user_id/";
			$dirCreated=($this->createDir($dir));
			$filePath .="/".$user_id."/";
			
			
         	
			if (is_dir($dir))
			{
				if($uniqueFileName != '')
				{
                    
					$src_image	= 	$filenameOrignal;
					$ext		=	explode('.',$src_image);
					$allowedExtensions = array('3gp','mp4','mov','flv');
					$loc		=	$dir.$uniqueFileName.".".$ext[1];
					$nameForDB	=	$filePath.$uniqueFileName.".".$ext[1];
					if(in_array(strtolower($ext['1']),$allowedExtensions))	
					{							
						if(file_exists($dir))
						{

							$file=fopen($loc,'a') or die(json_encode(array('status'=>'0','result'=>$loc,'count'=>'','message'=>"Unable to open file!")));
							
							if($count != $totalChunk)
							{
							 	if(file_exists($loc))
							 	{
							  		if($count == 0)
										chmod($loc,0777);
								
									$fdata = $_REQUEST['video'];
								 	if (fwrite($file,$fdata)) {	
										$count++;
										
										if($count == $totalChunk)
										{
											$nameForDB = $this->convertMe($nameForDB,$dir,$ext[1]);
											file_put_contents("response.txt",var_export(array('status'=>'1','video_status'=>'1','count'=>$count,'result'=>$nameForDB,'message'=>"Video Uploaded Successfully"),true),true);
											echo json_encode(array('status'=>'1','video_status'=>'1','count'=>$count,'result'=>$nameForDB,'message'=>"Video Uploaded Successfully"));					
											fclose($file);
											$count = 0;
									
										}
										else{
											echo json_encode(array('status'=>'1','video_status'=>'0','count'=>$count,'result'=>$nameForDB,'message'=>"Success"));			
											fclose($file);
											}
									}
							  	}
									
							}					
	
						}
					}
					else
					{
						echo json_encode(array('status'=>'0','result'=>'','count'=>'','message'=>"This file type is not supportable."));
						exit;
					}
				}
				else
				{
					echo json_encode(array('status'=>'0','result'=>'','count'=>'','message'=>'File name can not be blanked'));
					exit;				
				}					
			}
			else{
				echo json_encode(array('status'=>'0','result'=>'','count'=>'','message'=>'Unable to create directory'));
				exit;	
			}
		}//end function
        
		function convertMe($videoFile,$targetPath,$ext='mp4'){
			include('movie.class.php');
			$VIDEO	= new VIDEOSTREAM(); 
			$videoFullPath	=	realpath("../".$videoFile);
			$VIDEO->load($videoFullPath);
			$target		=	realpath($targetPath);
			$target		=	$target."/".time().".".$ext;
			$VIDEO->convertVideo($target);
			//unlink($videoFullPath); 
			//return $target;
			return $videoFullPath;
		}
		
        function saveThumbnail($post_id, $userid,$videoPath){
            
           $photo_dir = "../uploads";
            $dirCreated = (createDir($photo_dir));
            $filePath .="uploads";
			
			$photo_dir .= "/posts";
            $dirCreated = (createDir($photo_dir));
            $filePath .="/posts";

	        $photo_dir.="/$userid";
            $dirCreated = (createDir($photo_dir));
            $filePath .="/" . $userid . "";

            $photo_dir.="/$post_id";
            $dirCreated = (createDir($photo_dir));
            $filePath .="/" . $post_id . "/";
            
			
			if ($_FILES['image']['tmp_name']){
				$img            =   $_FILES['image']['tmp_name'];
			
				$file_name		=	time().str_replace(array(" ","'","\'","_"),"",$_FILES['image']['name']); 
			
				$file_namethumb	=  explode(".", $file_name);
				$file_nameExt	=  end($file_namethumb);
				$thumbName		=	$file_namethumb[0]."-thumb".".$file_nameExt";
				$thumbPath      =   $photo_dir."/".$thumbName;
				$filePath1      .=  $filePath."/".$thumbName;
			}
			else
				$filePath1		=	"";
			
          
//            move_uploaded_file($img, $thumbPath);
            
            $videoFile  =   basename($videoPath);      
	       //	echo $videoPath."\n";
		  	//echo "../".$filePath."/".$videoFile;
		   	$r = rename($videoPath,"../".$filePath."/".$videoFile);
		   	//var_dump($r);
		   	//exit;
		    
			if(!$r){
//            	echo "video file not move";
        	}		
            $arr =  array("thumb"=>$filePath1,"video"=>$filePath.$videoFile);
			
            return $arr;
        }
        
        function uploadVideoIOS($post_id, $userid){
            
            global $db;
            $status = 0;
            $video_name         =   $_FILES['data']['name'];
            $img_name           =   $_FILES['image']['name'];
           
            $photo_dir = "../uploads";
            $dirCreated = (createDir($photo_dir));
            $filePath .="uploads";
			
            $photo_dir.="/$userid";
            $dirCreated = (createDir($photo_dir));
            $filePath .="/" . $userid . "";
			
			$photo_dir .= "/posts";
            $dirCreated = (createDir($photo_dir));
            $filePath .="/posts";

            $photo_dir.="/$post_id";
            $dirCreated = (createDir($photo_dir));
            $filePath .="/" . $post_id . "";
            
            
            $src_image	= 	$video_name;
            $ext		= pathinfo($src_image, PATHINFO_EXTENSION);
            $allowedExtensions = array('3gp','mp4','mov','flv');
            
            if(in_array(strtolower($ext),$allowedExtensions)){
                
                $img            =   $_FILES['photo']['tmp_name'];
                $thumbName      =   $this->renameFiles($img_name,$ext);
                $thumbPath      =   $photo_dir."/".$thumbName;
               
                move_uploaded_file($img, $thumbPath);
                
                $video            =   $_FILES['video']['tmp_name'];
                $videoName      =   $this->renameFiles($video_name,$ext).".".$ext;
                $videoPath      =   $photo_dir."/".$videoName;
              
                move_uploaded_file($video, $videoPath);
               
                
                $this->saveVideo($post_id,$filePath.$thumbName,$filePath.$videoName);
                $status=1;
                
            }
            else
                $msg = "This file type is not supportable.";
            $arr = array("message" => $msg, "status" => $status);
            return $arr;
        }
        
        function renameFiles($fileName,$ext){
            
            $rand = time() . strtolower(randomcode(5));
            $baseflName = basename($fileName, "." . $ext);
            
            $flName = $i . $rand . "_" . $baseflName;
           
            return $flName;
            
        }
        
        function saveVideo($post_id,$thumbPath,$videoPath){
            global $db;
            $sql = " update post SET " .
                    " data_url      =	'" . $videoPath."'," .
                    " image		   =	'" . $thumbPath."'," .
					" thumb_image   =	'" . $thumbPath."'" .
                    " where post_id       =   '" . $post_id . "' ";
			
            $result	=	$db->query($sql);
		
            
        }

}
