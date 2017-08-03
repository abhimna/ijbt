<?php

function generateRandomString($length = 10) {

	return substr(str_shuffle("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"), 0, $length);
}

function image_valid($type) {

	$file_types = array(
		'image/pjpeg' => 'jpg',
		'image/jpeg' => 'jpg',
		'image/jpeg' => 'jpeg',
		'image/gif' => 'gif',
		'image/X-PNG' => 'png',
		'image/PNG' => 'png',
		'image/png' => 'png',
		'image/x-png' => 'png',
		'image/JPG' => 'jpg',
		'image/GIF' => 'gif',
	);



	if (!array_key_exists($type, $file_types)) {

		return FALSE;
	} else {

		return TRUE;
	}
}

function validFile($type) {
    
    
	$file_types = array(
		'image/pjpeg' => 'jpg',
		'image/jpeg' => 'jpg',
		'image/jpeg' => 'jpeg',
		'image/gif' => 'gif',
		'image/X-PNG' => 'png',
		'image/PNG' => 'png',
		'image/png' => 'png',
		'image/x-png' => 'png',
		'image/JPG' => 'jpg',
		'image/GIF' => 'gif',
		'application/pdf' => 'PDF',
		'image/doc' => 'DOC',
		'application/vnd.ms-excel' => 'XLS',
		'text/csv' => 'CSV',
		'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet

' => 'XLSX',
		'application/vnd.openxmlformats-officedocument.wordprocessingml.document' => 'DOCX',
	);



	if (!array_key_exists($type, $file_types)) {

		return FALSE;
	} else {

		return TRUE;
	}
}

function validCsv($type) {
   
	$file_types = array(
		'text/csv' => 'CSV',
        'application/vnd.ms-excel' => 'CSV'
	);
	if (!array_key_exists($type, $file_types)) {
		return FALSE;
	} else {
		return TRUE;
	}
}

function checkfile($type) {

	$file_types = array(
		'application/pdf' => 'PDF',
		'image/doc' => 'DOC',
		'text/plain' => 'TXT',
	);



	if (!array_key_exists($type, $file_types)) {

		return false;
	} else {

		return true;
	}
}

function validPdfFile($type) {

	$file_types = array(
		'application/pdf' => 'PDF',
	);



	if (!array_key_exists($type, $file_types)) {

		return false;
	} else {

		return true;
	}
}

function validAudioFile($type) {

	$file_types = array(
		'audio/mpeg' => 'mp3',
	);



	if (!array_key_exists($type, $file_types))
		return false;
	else
		return true;
}

function checkVideofile($type) {

	$file_types = array(
		/*'video/x-flv' => 'flv',*/
		'video/mp4' => 'mp4',
	);



	if (!array_key_exists($type, $file_types))
		return false;
	else
		return true;
}

function UPLOAD_FILES($path, $filelname, $flname) {

	$validdate = true;

	if (!validFile($flname['type'])) {

		$validdate = false;
	}

	if ($flname['size'] > 2 * 1024 * 1024) {

		$validdate = false;
	}

	if ($validdate) {

		$source = $flname['tmp_name'];

		$target = $path . "/" . $filelname;

		move_uploaded_file($source, $target) or die('error');

		return true;
	} else {

		return false;
	}
}

function UPLOAD_CSV($path, $filelname, $flname) {

	$validdate = true;

	if (!validCsv($flname['type'])) {

		$validdate = false;
	}

	if ($flname['size'] > 2 * 1024 * 1024) {

		$validdate = false;
	}

	if ($validdate) {

		$source = $flname['tmp_name'];

		$target = $path . "/" . $filelname;

		move_uploaded_file($source, $target) or die('error');

		return true;
	} else {

		return false;
	}
}

function UPLOAD_DOC_FILES($path, $filelname, $flname) {

	$validdate = true;

	if (!checkfile($flname['type'])) {

		$validdate = false;
	}



	if ($flname['size'] > 2 * 1024 * 1024) {

		$validdate = false;
	}

	if ($validdate) {

		$source = $flname['tmp_name'];

		$target = $path . "/" . $filelname;

		move_uploaded_file($source, $target) or die('error');

		return true;
	} else {

		return false;
	}
}

function UPLOAD_VIDEO_FILES($path, $filelname, $flname) {

	$validdate = true;

	if (!checkVideofile($flname['type'])) {

		$validdate = false;
	}



	if ($flname['size'] > 500 * 1024 * 1024) {

		$validdate = false;
	}

	if ($validdate) {

		$source = $flname['tmp_name'];

		$target = $path . "/" . $filelname;

		move_uploaded_file($source, $target) or die('error');

		return true;
	} else {

		return false;
	}
}

function UPLOAD_AUDIO_FILES($path, $filelname, $flname) {

	$validdate = true;

	if (!validAudioFile($flname['type'])) {

		$validdate = false;
	}



	if ($flname['size'] > 10 * 1024 * 1024) {

		$validdate = false;
	}

	if ($validdate) {

		$source = $flname['tmp_name'];

		$target = $path . "/" . $filelname;

		move_uploaded_file($source, $target) or die('error');

		return true;
	} else {

		return false;
	}
}

function UPLOAD_PDF_FILES($path, $filelname, $flname) {

	$validdate = true;

	if (!validPdfFile($flname['type'])) {

		$validdate = false;
	}



	if ($flname['size'] > 1 * 1024 * 1024) {

		$validdate = false;
	}

	if ($validdate) {

		$source = $flname['tmp_name'];

		$target = $path . $filelname;

		move_uploaded_file($source, $target) or die('error');

		return true;
	} else {

		return false;
	}
}

function getExtension($str) {



	$i = strrpos($str, ".");

	if (!$i) {
		return "";
	}



	$l = strlen($str) - $i;

	$ext = substr($str, $i + 1, $l);

	return $ext;
}

function UPLOAD_IMAGES($path, $filelname, $flname) {

	$validdate = true;



	if (!image_valid($flname['type'])) {

		$validdate = false;
	}



	if ($flname['size'] > 5 * 1024 * 1024) {

		$validdate = false;
	}

	if ($validdate) {

		$flname['size'] = 144 * 107;

		$source = $flname['tmp_name'];

		$target = $path . "/" . $filelname;

		move_uploaded_file($source, $target);

		return true;
	} else {

		return false;
	}
}

?>