<?php
class COMBINED_IMAGE extends UPLOAD_IMAGE
{
	// *** Class variables
    private $new_image;
	private $img_array = array();
	private $x;
	private $y;
	private $seprator=2;
 
    function __construct($imgArray)
    {
        // *** Open up the file
      	$this->x = 600;
		$this->y = 600;
		$this->new_image = imagecreatetruecolor($this->x, $this->y);
		$bgColor = imagecolorallocate($this->new_image, 0, 0, 0);
		imagefill($this->new_image, 0, 0, $bgColor);
		$this->img_array = $imgArray;
    }
	
	function combinedImage($savePath){
		$nDst_x =0;
		$nDst_y =0;
		$totalImg = count($this->img_array);

		if ($totalImg<=3){
			foreach($this->img_array as $img){
				$this->init($img);
				$newWidth  =($this->x/$totalImg);

				$newHeight = $this->y;
				 $this->resizeImage($newWidth,$newHeight,'crop');
				$image1 =$this->imageResized;
				imagecopymerge($this->new_image,$image1,$nDst_x,0,0,0, 600, 600,100);
				$nDst_x +=($this->x/$totalImg)+$seprator;
			}
		}
		elseif ($totalImg==4 || $totalImg==6){
			$i=1;
			$nDst_x =0;
			$nDst_y =0;
			$imgRow= ($totalImg/2);
			foreach($this->img_array as $img){
				$this->init($img);
				$newWidth  =(($this->x/$imgRow));
				$newHeight = ($this->y/2);
				$this->resizeImage($newWidth,$newHeight,'crop');
				$image1 =$this->imageResized;
				imagecopymerge($this->new_image,$image1,$nDst_x,$nDst_y,0,0, $newWidth, $newHeight,100);
				if (($i%$imgRow)==0){
					$nDst_y +=($this->y/2)+$seprator;
					$nDst_x=0;
				}
				else{
					$nDst_x +=($this->x/$imgRow)+$seprator;	
				}
				$i++;	
			}
		}
		elseif ($totalImg==5){
			$i=1;
			$nDst_x =0;
			$nDst_y =0;
			$imgRow= 2;
			foreach($this->img_array as $img){
				$this->init($img);
				$newWidth  =(($this->x/$imgRow));
				$newHeight = ($this->y/2);
				$this->resizeImage($newWidth,$newHeight,'crop');
				$image1 =$this->imageResized;
				if ($i<5)
					imagecopymerge($this->new_image,$image1,$nDst_x,$nDst_y,0,0, $newWidth, $newHeight,100);
				else{
					$nDst_x =($this->x/2)-($newWidth/2) ;
					$nDst_y =($this->y/2)-($newHeight/2) ;
					imagecopymerge($this->new_image,$image1,$nDst_x,$nDst_y,0,0, $newWidth, $newHeight,100);
				}
		
					if (($i%$imgRow)==0){
						$nDst_y +=($this->y/2)+$seprator;
						$nDst_x=0;
					}
					else{
						$nDst_x +=($this->x/$imgRow)+$seprator;	
					}
					$i++;	
			}
		}
		imagejpeg($this->new_image,$savePath,100);
	}
	
}
	

?>