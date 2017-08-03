<?php
define("MAXMEM", 5 * 1024 * 1024);
ini_set('memory_limit', '-1');
class UploadImage {

	var $image;
	var $image_type;
	public $validate = true;

	function load($filename) {



		$image_info = getimagesize($filename);



		$this->image_type = $image_info[2];



		if ($this->image_type == IMAGETYPE_JPEG) {

			$this->image = imagecreatefromjpeg($filename);
		} elseif ($this->image_type == IMAGETYPE_GIF) {

			$this->image = imagecreatefromgif($filename);
		} elseif ($this->image_type == IMAGETYPE_PNG) {

			$this->image = imagecreatefrompng($filename);
		} else
			$this->validate = false;
	}

	function save($filename, $compression = "100") {



		if (imagetypes() & IMG_JPG) {



			imagejpeg($this->image, $filename, $compression);
		} elseif (imagetypes() & IMG_GIF) {



			imagegif($this->image, $filename);
		} elseif (imagetypes() & IMG_PNG) {



			imagepng($this->image, $filename);
		}
	}

	function output($image_type = IMAGETYPE_JPEG) {



		if ($image_type == IMAGETYPE_JPEG) {

			imagejpeg($this->image);
		} elseif ($image_type == IMAGETYPE_GIF) {



			imagegif($this->image);
		} elseif ($image_type == IMAGETYPE_PNG) {



			imagepng($this->image);
		}
	}

	function getWidth() {



		return imagesx($this->image);
	}

	function getHeight() {



		return imagesy($this->image);
	}

	function resizeToHeight($height) {



		$ratio = $height / $this->getHeight();

		$width = $this->getWidth() * $ratio;

		$this->resize($width, $height);
	}

	function resizeToWidth($width) {

		$ratio = $this->getHeight() / $this->getWidth();

		$height = $width * $ratio;

		$this->resize($width, $height);
	}

	function scale($scale) {

		$width = $this->getWidth() * $scale / 100;

		$height = $this->getheight() * $scale / 100;

		$this->resize($width, $height);
	}

	function resize($width, $height) {

		$new_image = imagecreatetruecolor($width, $height);

		imagecopyresampled($new_image, $this->image, 0, 0, 0, 0, $width, $height, $this->getWidth(), $this->getHeight());

		$this->image = $new_image;
	}

	function enoughmem($x, $y, $rgb = 3) {

		return ( $x * $y * $rgb * 1.7 < MAXMEM - memory_get_usage() );
	}

	function TextOnImage($img, $image_type) {



		if ($image_type == IMAGETYPE_JPEG) {

			$im = imagecreatefromjpeg($img);
		} elseif ($image_type == IMAGETYPE_GIF) {

			$im = imagecreatefromgif($img);
		} elseif ($image_type == IMAGETYPE_PNG) {

			$im = imagecreatefrompng($img);
		}



// Create the image
// Create some colors

		$white = imagecolorallocate($im, 255, 255, 255);

		$grey = imagecolorallocate($im, 128, 128, 128);

		$black = imagecolorallocate($im, 0, 0, 0);

//imagefilledrectangle($im, 0, 0, 399, 29, $white);
// The text to draw

		$text = 'Testing...';

// Replace path by your own font path

		$font = 'fonts/monofont.ttf';



// Add some shadow to the text

		imagettftext($im, 200, 0, 500, 500, $white, $font, $text);



// Add the text
////imagettftext($im, 100, 0, 500, 500, $black, $font, $text);
// Using imagepng() results in clearer text compared with imagejpeg()

		if ($image_type == IMAGETYPE_JPEG) {

			return imagejpeg($im, "a.jpg");
		} elseif ($image_type == IMAGETYPE_GIF) {

			return imagegif($im);
		} elseif ($image_type == IMAGETYPE_PNG) {

			return imagepng($im);
		}

		imagedestroy($im);
	}

	function CreateWaterMark($img, $userdir, $tp = "REPEAT") {

		$stamp = imagecreatefrompng('demo.png');

		$img_name = basename($img);

		$im = imagecreatefromjpeg($img);

		list ($ox, $oy) = @getimagesize($img);

		list($dx, $dy) = @getimagesize('demo.png');



		$sx = imagesx($stamp);

		$sy = imagesy($stamp);



		$w = round($ox / 2);

		$h = round($oy / 2);

		$stx = round($w / $sx * $sy);

		$sty = $h;

		$dest_x = 0;

		$dest_y = 0;



		if ($tp == "REPEAT") {

			imagecopy($im, $stamp, 0, 0, 0, 0, imagesx($stamp), imagesy($stamp));

			$dest_x += $dx;

			$dest_y += $dy;

			for ($i = ($dy * 2); $i < $oy;) {

				imagecopy($im, $stamp, 0, $dest_y, 0, 0, imagesx($stamp), imagesy($stamp));

				$dest_x += $dx;

				$dest_y += $dy;

				$i += $dy;
			}
		} else {

			$x = ceil($oy / 2);

			$y = ceil($dy / 2);



			imagecopy($im, $stamp, 0, $x - $y, 0, 0, imagesx($stamp), imagesy($stamp));
		}

		$target = "{$userdir}{$img_name}";

		imagejpeg($im, $target, 100);

		imagedestroy($im);

		imagedestroy($stamp);
	}

}

?>