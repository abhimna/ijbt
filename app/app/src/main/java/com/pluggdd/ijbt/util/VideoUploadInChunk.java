package com.pluggdd.ijbt.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.interfaces.AsyncTaskListener;
import com.pluggdd.ijbt.network.APIResponse;
import com.pluggdd.ijbt.network.IJBTResponseController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

public class VideoUploadInChunk extends AsyncTask<String, String, Void>
        implements IJBTResponseController {

    private String str_selectedPath, str_userid;
    private Activity _activity;
    private int noOfChunks;
    private UploadVo videoUploadVo;
    private File videofile;
    private String lastCountValue;
    private String response = "";
    private String str_uniquename;

    private ProgressDialog progressBar;
    private AsyncTaskListener asynclistener;
    int int_task_type;
    private float setProgress;
    private CommonMethods commonMethods;
    private String title;
    private File thumbImage;
    private StringBuffer sb_hashtag, sb_tag;
    private String str;
    String placebought;
    String isoCode;
    String price;
    String categorynam;
    String comment;
    File outputFile;
    String userType;


    // private volatile boolean running = true;

    public VideoUploadInChunk(String Path, Activity act, String userid,
                              int int_task_type, String str_uniquename, String title,
                              String string, File outputFile) {

        this.str_selectedPath = Path;
        System.out.println("videopath+++" + this.str_selectedPath);
        this._activity = act;
        this.str_userid = userid;
        this.int_task_type = int_task_type;
        this.asynclistener = (AsyncTaskListener) act;
        this.str_uniquename = str_uniquename;
        this.title = title;
        this.thumbImage = outputFile;
        this.sb_hashtag = new StringBuffer();
        this.sb_tag = new StringBuffer();

        commonMethods = new CommonMethods(_activity, this);

        videofile = new File(str_selectedPath);
        lastCountValue = "0";
        progressBar = new ProgressDialog(_activity);
        // progressBar.setCancelable(true);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setMessage("Uploading video...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress((int) setProgress);
        progressBar.setMax(100);
        progressBar.show();
    }

    public VideoUploadInChunk(String Path, Activity act, String userid, int int_task_type, String str_uniquename, String title, String placebought, String isoCode, String price, String categorynam, String comment, File outputFile,String userType) {
        this.str_userid = userid;
        this.placebought = placebought;
        this.isoCode = isoCode;
        this.price = price;
        this.categorynam = categorynam;
        this.comment = comment;
        this.outputFile = outputFile;
        this.str_selectedPath = Path;
        System.out.println("videopath+++" + this.str_selectedPath);
        this._activity = act;
        this.str_userid = userid;
        this.int_task_type = int_task_type;
        this.asynclistener = (AsyncTaskListener) act;
        this.str_uniquename = str_uniquename;
        this.title = title;
        this.thumbImage = outputFile;
        this.sb_hashtag = new StringBuffer();
        this.sb_tag = new StringBuffer();
        this.userType = userType;
        commonMethods = new CommonMethods(_activity, this);
        videofile = new File(str_selectedPath);
        lastCountValue = "0";
        progressBar = new ProgressDialog(_activity);
        // progressBar.setCancelable(true);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setMessage("Uploading video...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress((int) setProgress);
        progressBar.setMax(100);
        progressBar.show();
    }

    @Override
    protected Void doInBackground(final String... args) {
        if (commonMethods.getConnectivityStatus()) {

            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(videofile);
                long maxBufferSize = 100l * 1024l;
                long uploadedSize = 0;
                noOfChunks = (int) Math.ceil((float) videofile.length()
                        / maxBufferSize);
                String nameWithExt = videofile.getName();
                String filenameSec = nameWithExt.substring(0,
                        nameWithExt.lastIndexOf("."))
                        + System.currentTimeMillis();
                String ext = nameWithExt.substring(
                        nameWithExt.lastIndexOf("."), nameWithExt.length());
                String unqName = filenameSec + ext;
                long bytesAvailable = fileInputStream.available();
                int bufferSize = (int) Math.min(bytesAvailable, maxBufferSize);
                byte[] buffer = new byte[bufferSize];
                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    videoUploadVo = uploadFile(videofile, buffer, "1",
                            str_userid, unqName, lastCountValue);

                    System.out.println("videoUploadVo-------" + videoUploadVo);
                    if (videoUploadVo != null) {
                        int int_count = Integer.parseInt(lastCountValue);
                        int_count += 1;
                        lastCountValue = String.valueOf(int_count);
                    }
                    uploadedSize = uploadedSize + maxBufferSize;
                    bytesAvailable = fileInputStream.available();
                    bufferSize = (int) Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    setProgress = ((float) (uploadedSize * 100) / videofile
                            .length());
                    DecimalFormat df = new DecimalFormat("#,###,##0.00");
                    publishProgress(df.format(setProgress));
                    progressBar.setProgress((int) setProgress);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileInputStream != null)
                        fileInputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            // }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onCancelled() {
        VideoUploadInChunk.this.cancel(true);
    }

    protected void onPostExecute(final Void unused) {
        progressBar.dismiss();
        str = videoUploadVo.toString();
        // new AlertDialog.Builder(_activity)
        //
        // .setMessage(response)
        // .setPositiveButton("Yes",
        // new DialogInterface.OnClickListener() {
        //
        // @Override
        // public void onClick(DialogInterface dialog,
        // int which) {
        //
        // }
        //
        // }).setNegativeButton("No", null).show();

        if (!isCancelled()) {
            progressBar.dismiss();

            if (videoUploadVo.getStatus().equalsIgnoreCase("1")) {
                String Original = title.trim();

                for (String retval : Original.split(" ")) {
                    System.out.println("Your value is---" + retval);

                    if (retval.length() >= 2) {

                        if (retval.startsWith("#")) {
                            sb_hashtag.append("," + retval.replace("#", ""));
                        }
                    }
                }
                for (String retval : Original.split(" ")) {
                    System.out.println("Your value is---" + retval);

                    if (retval.length() >= 2) {

                        if (retval.startsWith("@")) {
                            sb_tag.append("," + retval.replace("@", ""));
                        }
                    }
                }

                try {
                    sb_hashtag.deleteCharAt(0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                try {
                    sb_tag.deleteCharAt(0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                try {
                    if (commonMethods.getConnectivityStatus()) {

                        System.out.println("videoupload_result"
                                + videoUploadVo.getResult());
                        commonMethods.postVideo(commonMethods
                                .postVideoRequestParmas(str_userid, title,
                                        "video", "", "", thumbImage,
                                        videoUploadVo.getResult(), "android",placebought,isoCode,price,categorynam,comment,userType));
                        /*commonMethods.postVideo(commonMethods
                                .postVideoRequestParmas(str_userid, title,
                                        "video", "", "", "", thumbImage,
                                        videoUploadVo.getResult(), "android"));*/

                    } else {
                        GlobalConfig.showToast(
                                _activity,
                                _activity.getResources().getString(
                                        R.string.internet_error_message));
                    }
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                GlobalConfig.showToast(_activity, videoUploadVo.getMessage());
            }
        }
        //
    } // onpostexecute -------------------------------

    /**
     * Method to upload single chunk on server
     *
     * @param videofile
     * @param chunk
     * @param clipId
     * @param userId
     * @param uniqueName
     * @param lastCount
     * @return {@link UploadVo}
     */
    public UploadVo uploadFile(File videofile, byte[] chunk, String clipId,
                               String userId, String uniqueName, String lastCount) {

        System.out.println("hh upload file method called");
        UploadVo uploadvo = null;
        int serverResponseCode = 0;
        String fileName = videofile.getName();

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        File sourceFile = videofile;

        if (!sourceFile.isFile()) {

            Log.e("uploadFile",
                    "Source File not exist :" + videofile.getAbsolutePath()
                            + "" + fileName);

            return null;

        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(
                        sourceFile);
                // URL url = new
                // URL("http://anveshanit.com/new/application/saveVideo/saveVideo.php");
                // URL url = new URL(
                // "http://employeetracker.us/chunk/services/ws-upload-video.php");
                // URL url = new URL(URLFactory.baseUrl +
                // "ws-upload-video.php");
                // URL url = new URL(URLFactory.baseUrl
                // + "ws-post.php");

                URL url = new URL(URLFactory.baseUrl + "ws-upload-video.php");

                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("file_name", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);

                // write the user Id in the output stream.
                dos.writeBytes("Content-Disposition: form-data; name=\"user_id\"\r\n\r\n"
                        + userId);
                dos.writeBytes(lineEnd + "--" + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\"totalchunk\"\r\n\r\n"
                        + noOfChunks + "");
                dos.writeBytes(lineEnd + "--" + boundary + lineEnd);
                uniqueName = uniqueName.replaceAll(" ", "_");
                dos.writeBytes("Content-Disposition: form-data; name=\"unique_name\"\r\n\r\n"
                        + str_uniquename);

                dos.writeBytes(lineEnd + "--" + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\"file_name\"\r\n\r\n"
                        + fileName);
                dos.writeBytes(lineEnd + "--" + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"count\"\r\n\r\n"
                        + lastCount);
                dos.writeBytes(lineEnd + "--" + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\"video\"\r\n\r\n");
                dos.write(chunk);
                dos.writeBytes(lineEnd + "--" + boundary + lineEnd);
                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);
                System.out.println("uploadFile,HTTP Response is : "
                        + serverResponseMessage + serverResponseCode);
                if (serverResponseCode == 200) {
                    response = "";
                    try {
                        response = read(conn.getInputStream());
                        Log.i("uploadFile", "HTTP Response is response : "
                                + response + ": " + response);
                        System.out.println("uploadFile is : "
                                + serverResponseMessage + response);
                        // popup(response);
                        //
                        if (response != null) {

                            JSONObject objectjson = new JSONObject(response);
                            uploadvo = new UploadVo(objectjson);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    // catch (JSONException e) {
                    // e.printStackTrace();
                    // }

                }
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();
                System.out.println("hh MalformedURLException Exception ");
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Upload file Exception",
                        "Exception : " + e.getMessage(), e);
            }
            return uploadvo;

        }
    }

    // private void popup(final String response2) {
    //
    // AlertDialog.Builder builder1 = new AlertDialog.Builder(_activity);
    //
    // builder1.setMessage(response2);
    // builder1.setPositiveButton("Yes",
    // new DialogInterface.OnClickListener() {
    //
    // @Override
    // public void onClick(DialogInterface dialog,
    // int which) {
    // GlobalConfig.showToast(_activity, response2);
    //
    //
    // }
    //
    // });
    // builder1.setNegativeButton("No", null);
    // AlertDialog alert11 = builder1.create();
    // alert11.show();
    //
    // }

    private String read(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

    @Override
    public void OnComplete(APIResponse apiResponse) {
        System.out.println("Response of upload video"
                + apiResponse.getResponse());


        if (apiResponse.getCode() == 200) {
            try {
                JSONObject rootJsonObjObject = new JSONObject(
                        apiResponse.getResponse());
                String strMsg = rootJsonObjObject.getString("msg");
                String strStatus = rootJsonObjObject.getString("status");
                String strType = rootJsonObjObject.getString("type");
                String strPostId = rootJsonObjObject.getString("post_id");


                if (strStatus.equals("true")) {

                    String strImage = rootJsonObjObject.getString("image");
                    String strVideoUrl = rootJsonObjObject.getString("data_url");
                    asynclistener.onTaskComplete(strType, strImage, strVideoUrl, strPostId);

                } else {
                    GlobalConfig.showToast(_activity, strMsg);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            GlobalConfig.showToast(_activity, "Please try again");
        }
    }

    // private void showDialog(String response2) {
    // new AlertDialog.Builder(_activity)
    // .setIcon(R.drawable.ic_launcher)
    // .setTitle("Response")
    // .setMessage(response2)
    // .setPositiveButton("Yes",
    // new DialogInterface.OnClickListener() {
    //
    // @Override
    // public void onClick(DialogInterface dialog,
    // int which) {
    //
    // dialog.dismiss();
    // }
    //
    // }).setNegativeButton("No", null).show();
    //
    // }
}
