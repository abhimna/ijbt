<?php

class ErrorLog {

    public static function log($name, $error) {
        if (DM == "1" || DM == "-1") {
            if (!file_exists("raw_error_log.txt"))
                file_put_contents("raw_error_log.txt", "");
            //filesize($filename) 

            if (filesize("raw_error_log.txt") >= 1024 * 300) {   //1024=1kb 
                file_put_contents("raw_error_log.txt", "");
            }
            if (is_array($error)) {
                $error = var_export($error, true);
            }

            $data = $name . " : " . $error . " at " . date("Y-m-d H:i:s") . " \n";
            file_put_contents("raw_error_log.txt", $data, FILE_APPEND);
        }
    }

    public static function clear() {

        file_put_contents("raw_error_log.txt", "");
    }

}
