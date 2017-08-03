<?php
class VIDEOSTREAM
{
	private $file								= '';
	public $ffmpeg_path							= 'ffmpeg';
	private	$output								= '';
	protected static $REGEX_DURATION         	= '/Duration: ([0-9]{2}):([0-9]{2}):([0-9]{2})(\.([0-9]+))?/';
    protected static $REGEX_FRAME_RATE       	= '/([0-9\.]+\sfps,\s)?([0-9\.]+)\stbr/';    
    protected static $REGEX_COMMENT           	= '/comment\s*(:|=)\s*(.+)/i';
    protected static $REGEX_TITLE             	= '/title\s*(:|=)\s*(.+)/i';
    protected static $REGEX_ARTIST            	= '/(artist|author)\s*(:|=)\s*(.+)/i';
    protected static $REGEX_COPYRIGHT         	= '/copyright\s*(:|=)\s*(.+)/i';
    protected static $REGEX_GENRE             	= '/genre\s*(:|=)\s*(.+)/i';
    protected static $REGEX_TRACK_NUMBER     	= '/track\s*(:|=)\s*(.+)/i';
    protected static $REGEX_YEAR             	= '/year\s*(:|=)\s*(.+)/i';
    protected static $REGEX_FRAME_WH         	= '/Video:.+?([1-9][0-9]*)x([1-9][0-9]*)/';
    protected static $REGEX_PIXEL_FORMAT      	= '/Video: [^,]+, ([^,]+)/';
    protected static $REGEX_BITRATE           	= '/bitrate: ([0-9]+) kb\/s/';    
    protected static $REGEX_VIDEO_BITRATE     	= '/Video:.+?([0-9]+) kb\/s/';
    protected static $REGEX_AUDIO_BITRATE     	= '/Audio:.+?([0-9]+) kb\/s/';
    protected static $REGEX_AUDIO_SAMPLE_RATE 	= '/Audio:.+?([0-9]+) Hz/';
    protected static $REGEX_VIDEO_CODEC       	= '/Video:\s([^,]+),/';
    protected static $REGEX_AUDIO_CODEC       	= '/Audio:\s([^,]+),/';
    protected static $REGEX_AUDIO_CHANNELS    	= '/Audio:\s[^,]+,[^,]+,([^,]+)/';
    protected static $REGEX_HAS_AUDIO         	= '/Stream.+Audio/';
    protected static $REGEX_HAS_VIDEO         	= '/Stream.+Video/';
    protected static $REGEX_ERRORS            	= '/.*(Error|Permission denied|could not seek to position|Invalid pixel format|Unknown encoder|could not find codec|does not contain any stream).*/i';
	
	
	function load($video){
		
		if (!file_exists($video))
			throw new Exception("Video not found ");
			
		$this->file = $video;
		$ffmpeg_output = array();
		
		$ffmpeg_cmd = $this->ffmpeg_path . " -i '" . $video . '\' 2>&1 | cat | egrep -e \'(Duration|Stream)\'';
		@exec($ffmpeg_cmd, $ffmpeg_output);
		$this->output	=	implode(PHP_EOL,$ffmpeg_output);
		
		
	}
	
	public function getDuration() {
    	$match = array();
		$duration	='';
        preg_match(self::$REGEX_DURATION, $this->output, $match);
		if (array_key_exists(1, $match) && array_key_exists(2, $match) && array_key_exists(3, $match)) {                
			$hours     = (int)    $match[1];
			$minutes   = (int)    $match[2];
			$seconds   = (int)    $match[3];                        
			$fractions = (float)  ((array_key_exists(5, $match)) ? "0.$match[5]" : 0.0);
			$duration	= (($hours * (3600)) + ($minutes * 60) + $seconds + $fractions); 
		}else {
			$duration = 0.0;
		}                        
            
            return $duration;
    }
	
	public function getFrameCount() {
       $frameCount = (int) ($this->getDuration() * $this->getFrameRate());            
       return $frameCount;
    }
	
	public function getFrameRate() {
		$match = array();
        preg_match(self::$REGEX_FRAME_RATE, $this->output, $match);
        $frameRate = (float) ((array_key_exists(1, $match)) ? $match[1] : 0.0);
        
        
        return $frameRate;
    }
	
	
	public function getFrameHeight() {
		$match = array();
        preg_match(self::$REGEX_FRAME_WH, $this->output, $match);
        if (array_key_exists(1, $match) && array_key_exists(2, $match)) {
        	$frame['width']  = (int) $match[1];
            $frame['height'] = (int) $match[2];
        } else {
        	$frame['width']  =	0;
            $frame['height'] = 	0;
        }
        return $frame;
    }
    
    /**
    * Return the width of the movie in pixels.
    *
    * @return int 
    */
    public function getFrameWidth() {
    	$arr	=	$this->getFrameHeight();
        
        
        return $arr;
    }
    
    /**
    * Return the pixel format of the movie.
    *
    * @return string 
    */
    public function getPixelFormat() {
		$match = array();
		preg_match(self::$REGEX_PIXEL_FORMAT, $this->output, $match);
		$pixelFormat = (array_key_exists(1, $match)) ? trim($match[1]) : '';
		return $pixelFormat;
    }
	
	public function getBitRate() {
		$match = array();
		preg_match(self::$REGEX_BITRATE, $this->output, $match);
		$bitRate = (int) ((array_key_exists(1, $match)) ? ($match[1] * 1000) : 0);
		return $bitRate;    
    }
    
    /**
    * Return the bit rate of the video in bits per second.
    *  
    * NOTE: This only works for files with constant bit rate.
    *
    * @return int 
    */
    public function getVideoBitRate() {
		$match = array();
		preg_match(self::$REGEX_VIDEO_BITRATE, $this->output, $match);
		$videoBitRate = (int) ((array_key_exists(1, $match)) ? ($match[1] * 1000) : 0);
	    
        return $videoBitRate;
    }
	
	/**
    * Return the audio bit rate of the media file in bits per second.
    *
    * @return int 
    */
    public function getAudioBitRate() {
		$match = array();
		preg_match(self::$REGEX_AUDIO_BITRATE, $this->output, $match);
		$audioBitRate = (int) ((array_key_exists(1, $match)) ? ($match[1] * 1000) : 0);
		
		return $audioBitRate;
    }
    
    /**
    * Return the audio sample rate of the media file in bits per second.
    *
    * @return int 
    */
    public function getAudioSampleRate() {
		$match = array();
		preg_match(self::$REGEX_AUDIO_SAMPLE_RATE, $this->output, $match);
		$audioSampleRate = (int) ((array_key_exists(1, $match)) ? $match[1] : 0);
		
		return $audioSampleRate;
    }
	
	
    /**
    * Return the name of the video codec used to encode this movie as a string.
    * 
    * @return string 
    */
    public function getVideoCodec() {
		$match = array();
		preg_match(self::$REGEX_VIDEO_CODEC, $this->output, $match);
		$videoCodec = (array_key_exists(1, $match)) ? trim($match[1]) : '';
		
		return $videoCodec;
    }
    
    /**
    * Return the name of the audio codec used to encode this movie as a string.
    *
    * @return string 
    */
    public function getAudioCodec() {
		$match = array();
		preg_match(self::$REGEX_AUDIO_CODEC, $this->output, $match);
		$audioCodec = (array_key_exists(1, $match)) ? trim($match[1]) : '';
        
        return $audioCodec;
    }
    
    /**
    * Return the number of audio channels in this movie as an integer.
    * 
    * @return int
    */
    public function getAudioChannels() {
		$match = array();
		preg_match(self::$REGEX_AUDIO_CHANNELS, $this->output, $match);
		if (array_key_exists(1, $match)) {
			switch (trim($match[1])) {
				case 'mono':
					$audioChannels = 1; break;
				case 'stereo':
					$audioChannels = 2; break;
				case '5.1':
					$audioChannels = 6; break;
				case '5:1':
					$audioChannels = 6; break;
				default: 
					$audioChannels = (int) $match[1];
			}                 
		} else {
			$audioChannels = 0;
		}

        
        return $this->audioChannels;
    }
    
    /**
    * Return boolean value indicating whether the movie has an audio stream.
    *
    * @return boolean 
    */
    public function hasAudio() {
        return (boolean) preg_match(self::$REGEX_HAS_AUDIO, $this->output);
    }
    
    /**
    * Return boolean value indicating whether the movie has a video stream.
    *
    * @return boolean 
    */
    public function hasVideo() {
        return (boolean) preg_match(self::$REGEX_HAS_VIDEO, $this->output);
    }
	
	function video_info(){
		$info						=	array();
		$info['duration']			=	$this->getDuration();
		$info['frameCount']			=	$this->getFrameCount();
		$info['frameRate']			=	$this->getFrameRate();
		$info['size']				=	$this->getFrameWidth();
		$info['bitRate']			=	$this->getBitRate();
		$info['videoBitRate']		=	$this->getVideoBitRate();
		$info['audioBitRate']		=	$this->getAudioBitRate();
		$info['audioSampleBitRate']	=	$this->getAudioSampleRate();
		$info['videoCodec']			=	$this->getVideoCodec();
		$info['audioCodec']			=	$this->getAudioCodec();
		$info['audioChannel']		=	$this->getAudioChannels();
		$info['hasAudio']			=	$this->hasAudio();
		$info['hasVideo']			=	$this->hasVideo();
		
		return $info;
		
	}
	
	
	public function convertVideo($targetPath,$compression=95){
		$size		=	'';
		$command	=	$this->ffmpeg_path.' -i '.$this->file.' -vcodec libx264';
		if ((int)$compression < 100){
			$compression	=	100-$compression;
			$size		=	$this->getFrameWidth();
			$oWidth		=	$size['width'];
			$oHeight	=	$size['height'];
			$width		=	ceil($oWidth-(($oWidth *(int)$compression)/100));
			$height		=	ceil($oHeight-(($oHeight *(int)$compression)/100));
			$size 		=	" -s {$width}x{$height}";
		}
		$cmd	=	$command.$size.' -y -strict -2 '.$targetPath;
		@exec($cmd, $ffmpeg_output,$retval);
	}
	

	
	public function getFrameAtTime($seconds = null, $width = null, $height = null, $quality = null, $frameFilePath = null, &$output = null) 	{
		 // Set frame position for frame extraction
        $frameTime = ($seconds === null) ? 0 : $seconds;    
        $ff_dest_file	=	realpath('../').'/uploads/'.time().".jpg";
	
        // time out of range
        if (!is_numeric($frameTime) || $frameTime < 0 || $frameTime > $this->getDuration()) {
			throw(new Exception('Frame time is not in range '.$frameTime.'/'.$this->getDuration().' '.$this->getFilename()));
        }
		
		 if(is_numeric($height) && is_numeric($width)) {
            $image_size = ' -s '.$width.'x'.$height;
        } else {
            $image_size = '';
        }
        
        if(is_numeric($quality)) {
            $quality = ' -qscale '.$quality;
        } else {
            $quality = '';
        }
		
		if ($frameTime > 30) {
			$seek1 = $frameTime - 30;
			$seek2 = 30;
		} else {
			$seek1 = 0;
			$seek2 = $frameTime;
		}
		$rotate	=	'-vf transpose=1';
		
         $cmd = $this->ffmpeg_path." -i $this->file -an -ss $seek1 -r 1 -vframes 1 $rotate $image_size -y $ff_dest_file";
         @exec($cmd, $output, $retval);
		if($retval==0){
			$this->downloadFile($ff_dest_file);
		}
			
    }
	
	private function downloadFile($filename){
		header("Pragma: public"); // required
		header("Expires: 0");
		header("Cache-Control: must-revalidate, post-check=0, pre-check=0");
		header("Cache-Control: private",false); // required for certain browsers 
		header("Content-Type: image/jpg");
		// change, added quotes to allow spaces in filenames,
		header("Content-Disposition: attachment; filename=\"".basename($filename)."\";" );
		header("Content-Transfer-Encoding: binary");
		header("Content-Length: ".filesize($filename));
		ob_clean();
		flush();
		readfile($filename);
		exit();
	}
}
?>