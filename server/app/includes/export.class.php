<?php
/*Class:A simple class to export mysql query and whole html and php page to excel,doc etc*/

class EXPORT_CLASS
{
	
	function exportWithPage($php_page,$excel_file_name)
	{
	
		$this->setHeader($excel_file_name);
	
	}
	
	/*function exportWithPDF($php_page,$file_name){
		ob_start();
	    include($php_page);
    	 $content = ob_get_clean();
		
		
	//	try
   // {
        $html2pdf = new HTML2PDF('P', 'A4', 'en');
        $html2pdf->pdf->SetDisplayMode('fullpage');
	    $html2pdf->writeHTML($content);
        $html2pdf->Output($file_name);
   // }
   // catch(HTML2PDF_exception $e) {
   //     echo $e;
    //    exit;
   // }
		
	}*/
	
	function setHeader($excel_file_name)//this function used to set the header variable
	{
		
		header("Content-type: application/octet-stream");//A MIME attachment with the content type "application/octet-stream" is a binary file.
		//Typically, it will be an application or a document that must be opened in an application, such as a spreadsheet or word processor. 
		header("Content-Disposition: attachment; filename=$excel_file_name");//with this extension of file name you tell what kind of file it is.
		header("Pragma: no-cache");//Prevent Caching
		header("Expires: 0");//Expires and 0 mean that the browser will not cache the page on your hard drive

	}
}
?>