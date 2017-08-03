package com.pluggdd.ijbt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView.Tokenizer;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.pluggdd.ijbt.adapter.CategorySpinnerAdapter;
import com.pluggdd.ijbt.adapter.CurrencySpinnerAdapter;
import com.pluggdd.ijbt.adapter.ShareTypeSpinnerAdapter;
import com.pluggdd.ijbt.interfaces.AsyncTaskListener;
import com.pluggdd.ijbt.interfaces.OnFbPost;
import com.pluggdd.ijbt.model.Currency;
import com.pluggdd.ijbt.model.TotalCatDetails;
import com.pluggdd.ijbt.model.Totalcatdetail;
import com.pluggdd.ijbt.network.APIResponse;
import com.pluggdd.ijbt.network.IJBTResponseController;
import com.pluggdd.ijbt.session.UserSessionManager;
import com.pluggdd.ijbt.util.AppImageView;
import com.pluggdd.ijbt.util.AppUtils;
import com.pluggdd.ijbt.util.ApplicationConstants;
import com.pluggdd.ijbt.util.CommonMethods;
import com.pluggdd.ijbt.util.GlobalConfig;
import com.pluggdd.ijbt.util.ImageSizeChangedCallback;
import com.pluggdd.ijbt.util.URLFactory;
import com.pluggdd.ijbt.util.VideoUploadInChunk;
import com.pluggdd.ijbt.vo.GsonParseUserConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

@SuppressLint("SimpleDateFormat")
public class SharePhotoActivity extends Activity
        implements OnClickListener, AsyncTaskListener, IJBTResponseController {


    private static final int REQUEST_CODE_TUMBLR_LOGIN = 555;
    private AppImageView imageViewSelected;
    private MultiAutoCompleteTextView captionEditText;
    private TextView openPopup;
    private InputMethodManager imm;
    private ImageView backArrow;
    String searchText;
    Context context;
    GPUImage gp;
    Activity _activity;
    private CommonMethods commonMethods;
    private int int_video_task = 1001;
    String imagepath;
    private String userid;
    private Intent intent;
    Bitmap bitmap;
    String audio_path = null;
    public static String planStatus;
    protected int tasktype;
    SharedPreferences pref;


    ArrayList<String> al;
    GsonParseUserConnection gsonParseUserConnection;
    GsonParseUserConnection localgsonParseUserConnection;
    private StringBuffer sb_hashtag, sb_tag;
    private OnFbPost onFbPost;
    String strPostId;
    String strPostUrl;
    private String capturevideoUrl;
    private String ThumbImgUrl;
    private int imageshare = 0;

    private CallbackManager mFBCallbackManager;
    private ShareDialog mShareDialog;

    VideoUploadInChunk video_upload_taskxxx;
    private RelativeLayout shareBackLayout;
    private TextView logoImage;
    private TextView textViewShareType;
    private TextView arrowImage;
    private EditText editTextTitle;
    private EditText editTextPlaceBought;
    private EditText editTextPrice;
    private Spinner spinnerCurrencyDropdown;
    private Spinner spinnerCategoryDropdown;
    private Spinner spinnerShareType;
    private TextView textViewCategory;
    private Button btnPost;
    CurrencySpinnerAdapter currencySpinnerAdapter;
    CategorySpinnerAdapter categorySpinnerAdapter;
    ShareTypeSpinnerAdapter shareTypeSpinnerAdapter;
    List<Currency> currencyItems;
    List<Totalcatdetail> categoryItems;
    List<String> shareTypeItems;
    String userType = "0";
    private VideoUploadInChunk video_upload_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.share_user_photo_screen);

        ProgressDialog mProgress = new ProgressDialog(this);
        mProgress.setMessage("Loading data ...");

        _activity = this;
        context = this;
        imagepath = getIntent().getStringExtra("imagepath");
        commonMethods = new CommonMethods(_activity, this);
        initViewFromXml();

        System.out.println("imagepathhhh-------" + imagepath);

        if (imagepath.contains(".png") || imagepath.contains(".jpg")) {
            imageViewSelected.setImageBitmap(GPUImageView.result);
        } else {
            bitmap = getThumbnailFromVideo(_activity, imagepath);
            imageViewSelected.setImageBitmap(bitmap);
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        mFBCallbackManager = CallbackManager.Factory.create();
        mShareDialog = new ShareDialog(this);


        LoginManager.getInstance().registerCallback(mFBCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("FB", "sucess");
                if (AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken().isExpired()) {
                    if (imageshare == 1) {
                        share(URLFactory.imageUrl + strPostUrl, strPostId);
                    } else {
                        shareTOFbVideo(URLFactory.imageUrl + ThumbImgUrl, URLFactory.imageUrl + capturevideoUrl, strPostId);
                    }
                }
            }

            @Override
            public void onCancel() {
                Log.i("FB", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("FB", "error");
            }
        });
    }

    public void updateImageView(File selectedFile) {
        final File finalSelectedFile = selectedFile;
        imageViewSelected.setOnImageViewSizeChanged(new ImageSizeChangedCallback() {
            @Override
            public void invoke(ImageView v, final int w, final int h) {
                Bitmap bmp = AppUtils.decodeFile(finalSelectedFile, w, h);
                if (bmp != null) {
                    imageViewSelected.setImageBitmap(bmp);
                }
            }
        });
    }

    private void initViewFromXml() {

        userid = _activity.getSharedPreferences("prefs_login", Activity.MODE_PRIVATE).getString("userid", "");
        intent = new Intent();
        pref = getPreferences(MODE_PRIVATE);
        pref = _activity.getPreferences(0);
        pref = getPreferences(0);
        sb_hashtag = new StringBuffer();
        sb_tag = new StringBuffer();

        shareBackLayout = (RelativeLayout) findViewById(R.id.share_back_layout);
        logoImage = (TextView) findViewById(R.id.logo_image);
        arrowImage = (TextView) findViewById(R.id.arrow_image);
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextPlaceBought = (EditText) findViewById(R.id.editTextPlaceBought);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        spinnerCurrencyDropdown = (Spinner) findViewById(R.id.spinnerCurrencyDropdown);
        spinnerCategoryDropdown = (Spinner) findViewById(R.id.spinnerCategoryDropdown);
        spinnerShareType = (Spinner) findViewById(R.id.spinnerShareType);
        textViewCategory = (TextView) findViewById(R.id.textViewCategory);
        textViewShareType = (TextView) findViewById(R.id.textViewShareType);
        btnPost = (Button) findViewById(R.id.btnPost);
        backArrow = (ImageView) findViewById(R.id.back_arrow);

        backArrow.setOnClickListener(this);
        btnPost.setOnClickListener(this);
        imageViewSelected = (AppImageView) findViewById(R.id.imageViewSelected);
        captionEditText = (MultiAutoCompleteTextView) findViewById(R.id.add_caption_edit_text);
        captionEditText.setTokenizer(new Tokenizer() {
            @Override
            public CharSequence terminateToken(CharSequence text) {
                int i = text.length();

                while (i > 0 && text.charAt(i - 1) == ' ') {
                    i--;
                }

                if (i > 0 && text.charAt(i - 1) == ' ') {
                    return text;
                } else {
                    if (text instanceof Spanned) {
                        SpannableString sp = new SpannableString(text + " ");
                        TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
                        return sp;
                    } else {
                        return text + " ";
                    }
                }
            }

            @Override
            public int findTokenStart(CharSequence text, int cursor) {
                int i = cursor;

                while (i > 0 && text.charAt(i - 1) != '@') {
                    i--;
                }

                // Check if token really started with @, else we don't have a
                // valid token
                if (i < 1 || text.charAt(i - 1) != '@') {
                    return cursor;
                }

                return i;
            }

            @Override
            public int findTokenEnd(CharSequence text, int cursor) {
                int i = cursor;
                int len = text.length();

                while (i < len) {
                    if (text.charAt(i) == ' ') {
                        return i;
                    } else {
                        i++;
                    }
                }

                return len;
            }
        });
        openPopup = (TextView) findViewById(R.id.arrow_image);
        openPopup.setOnClickListener(this);

        if (ShareActivityy.isPhotoCapture) {

            if (CommonMethods.getPreferences(_activity, "audio_status").equalsIgnoreCase("free")) {
                //txt_audio.setVisibility(View.GONE);
            } else {
                // txt_audio.setVisibility(View.VISIBLE);
                if (CommonMethods.getPreferences(_activity, "audio_plans").equalsIgnoreCase("com.ijbt.audio1")) {
                    planStatus = "audio1";
                    // 15 sec
                } else {
                    planStatus = "audio2";
                    // 180 sec
                }

            }

        } else {

            if (CommonMethods.getPreferences(_activity, "video_status").equalsIgnoreCase("free")) {
                planStatus = "video";
                // 15 sec video
            } else {
                if (CommonMethods.getPreferences(_activity, "video_plans").equalsIgnoreCase("com.ijbt.video1")) {
                    planStatus = "video1";
                    // 60sec video
                } else if (CommonMethods.getPreferences(_activity, "video_plans")
                        .equalsIgnoreCase("com.ijbt.video2")) {
                    planStatus = "video2";
                    // 120sec
                } else if (CommonMethods.getPreferences(_activity, "video_plans")
                        .equalsIgnoreCase("com.ijbt.video3")) {
                    planStatus = "video3";
                    // 300sec
                }
            }
            //txt_audio.setVisibility(View.GONE);
        }

        //txt_audio.setOnClickListener(this);

        onFbPost = new OnFbPost() {

            @Override
            public void AfterPostonfb() {
                intent.setClass(_activity, CommonFragmentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                _activity.finish();
                overridePendingTransition(0, 0);

            }
        };

        currencyItems = new ArrayList<Currency>();
        currencyItems.add(new Currency("NULL", " "));
        categoryItems = new ArrayList<Totalcatdetail>();
        currencySpinnerAdapter = new CurrencySpinnerAdapter(this, currencyItems);

        categoryItems.add(new Totalcatdetail("-1", "No Items"));
        categorySpinnerAdapter = new CategorySpinnerAdapter(this, categoryItems);

        if (UserSessionManager.getInstance(this).isInfluencer()) {
            textViewShareType.setVisibility(View.GONE);
            spinnerShareType.setVisibility(View.GONE);
        } else {
            textViewShareType.setVisibility(View.VISIBLE);
            spinnerShareType.setVisibility(View.VISIBLE);
            shareTypeItems = new ArrayList<String>();
            shareTypeItems.add("Only Friends");
            shareTypeItems.add("Everyone");
            shareTypeSpinnerAdapter = new ShareTypeSpinnerAdapter(this, shareTypeItems);
            spinnerShareType.setAdapter(shareTypeSpinnerAdapter);
        }
        spinnerCurrencyDropdown.setAdapter(currencySpinnerAdapter);
        spinnerCategoryDropdown.setAdapter(categorySpinnerAdapter);
        if (commonMethods.getConnectivityStatus()) {
            updateCategoryAndCurrency();
        }
    }

    private void updateCategoryAndCurrency() {
        tasktype = ApplicationConstants.TaskType.FETCH_CATEGORY_CURRENCY;
        commonMethods.fetchCategoryAndCurrency("http://ijbt.in/ijbt/webservices/category_web_service.php");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnPost:
                postProduct();
                break;

            case R.id.arrow_image:
                postProduct();
                break;

            case R.id.back_arrow:

                hideSoftKeyboard(captionEditText);

                finish();

                break;


            default:
                break;
        }
    }

    // this method is used to post a product to the server with all the necessary details like name,place bought, type of post,
    // category, price etc...
    private void postProduct() {
        if (UserSessionManager.getInstance(this).isInfluencer()) {
            userType = "0";
        } else {
            if (spinnerShareType.getSelectedItemPosition() == 0) {
                userType = "2";
            } else {
                userType = "1";
            }
        }
        tasktype = ApplicationConstants.TaskType.SETPOST;
        String comment = "";
        String title = editTextTitle.getText().toString();
        String placebought = editTextPlaceBought.getText().toString();
        String price = editTextPrice.getText().toString();
        //CurrencyI spinnerCurrencyDropdown.getSelectedItem();
        Currency currencyItem = (Currency) spinnerCurrencyDropdown.getSelectedItem();
        Totalcatdetail totalcatdetail = (Totalcatdetail) spinnerCategoryDropdown.getSelectedItem();
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        System.out.println("currentDateTimeString........." + currentDateTimeString);
        File F = new File(imagepath);
        System.out.println("FILE PATH IS>>>>> " + F);
       /* if (title.isEmpty()) {
            GlobalConfig.showToast(_activity, "Please fill about the product");
        } else if (placebought.isEmpty()) {
            GlobalConfig.showToast(_activity, "Please fill the place bought");
        } else if (price.isEmpty()) {
            GlobalConfig.showToast(_activity, "Please fill the price");
        } else {*/
        if (ShareActivityy.isPhotoCapture) {
            if (audio_path != null) {
                File F2 = new File(audio_path);
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
                    tasktype = ApplicationConstants.TaskType.SETPOST;
                    commonMethods.postIJBTImage(commonMethods.postAudioRequestParmas(userid, title, "audio",
                            sb_hashtag.toString(), sb_tag.toString(), placebought, currencyItem.getIsoCode(), price, totalcatdetail.getCategorynam(), comment, F, F2, userType));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {

                System.out.println("Photo Sharing start");

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
                    commonMethods.postIJBTImage(commonMethods.postImageRequestParmas(userid, title, "audio",
                            sb_hashtag.toString(), sb_tag.toString(), placebought, currencyItem.getIsoCode(), price, totalcatdetail.getCategorynam(), comment, F, userType));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                    /*try {
                        tasktype = ApplicationConstants.TaskType.SETPOST;
						commonMethods.postImage(commonMethods.postImageRequestParmas(userid, title.trim(), "audio",
								sb_hashtag.toString(), sb_tag.toString(), F));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}*/
            }

        } else {
            File outputDir = _activity.getCacheDir(); // context being the
            // // Activity pointer
            File outputFile = null;
            try {
                outputFile = File.createTempFile("thumb", ".jpeg", outputDir);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (!outputFile.exists()) {
                outputFile.mkdirs();
            }
            try {
                FileOutputStream outStream = new FileOutputStream(outputFile);
                bitmap.compress(CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
            String currentTimeStamp = dateFormat.format(new Date());
            System.out.println("imagepath >>>>>>" + imagepath);
            video_upload_task = new VideoUploadInChunk(imagepath, this, userid, int_video_task, "video",
                    title, placebought, currencyItem.getIsoCode(), price, totalcatdetail.getCategorynam(), comment, outputFile, userType);
            video_upload_task.execute("");
        }
        //}
    }

    private void openDialogWithMoreApp() {

        final Dialog dialogMapMain = new Dialog(_activity);
        dialogMapMain.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialogMapMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMapMain.setContentView(R.layout.dialog_more_option);
        dialogMapMain.getWindow().setGravity(Gravity.BOTTOM);

        dialogMapMain.setCanceledOnTouchOutside(true);
        LinearLayout ll_delete = (LinearLayout) dialogMapMain.findViewById(R.id.ll_delete);
        LinearLayout ll_sharefb = (LinearLayout) dialogMapMain.findViewById(R.id.ll_sharefb);
        LinearLayout ll_addPhoto = (LinearLayout) dialogMapMain.findViewById(R.id.ll_addPhoto);
        LinearLayout ll_cancel = (LinearLayout) dialogMapMain.findViewById(R.id.ll_cancel);
        TextView txtDelete = (TextView) dialogMapMain.findViewById(R.id.txtDelete);
        TextView txt_savetogallery = (TextView) dialogMapMain.findViewById(R.id.txt_savetogallery);
        txtDelete.setText("Audio From Device");
        txt_savetogallery.setText("Record Audio");
        ll_sharefb.setVisibility(View.GONE);
        ll_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogMapMain.dismiss();
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("audio/*");
                Intent c = Intent.createChooser(i, "Select soundfile");
                startActivityForResult(c, 1);

            }
        });
        ll_addPhoto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                intent.setClass(_activity, AudioRecordActivity.class);
                startActivityForResult(intent, 3);
                dialogMapMain.dismiss();

            }
        });
        ll_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogMapMain.dismiss();
            }
        });

        dialogMapMain.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        try {

            hideSoftKeyboard(captionEditText);

            try {

                finish();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getpathring(Uri path) {
        String result;
        Cursor cursor = getContentResolver().query(path, null, null, null, null);
        if (cursor == null) {
            result = path.getPath();
            int name = result.lastIndexOf("/");
            // mp3_name = result.substring(name + 1);
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            result = cursor.getString(idx);
            cursor.close();
            int name = result.lastIndexOf("/");
            // mp3_name = result.substring(name + 1);
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFBCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private String getFileName(String string) {
        int slashIndex = audio_path.lastIndexOf("/") + 1;
        int dotIndex = audio_path.lastIndexOf(".") + 1;
        return audio_path.substring(slashIndex, dotIndex - 1);

    }

    private void hideSoftKeyboard(EditText v) {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    @Override
    public void OnComplete(APIResponse apiResponse) {
        switch (tasktype) {
            case ApplicationConstants.TaskType.FETCH_CATEGORY_CURRENCY:
                try {
                    JSONObject rootJsonObjObject = new JSONObject(apiResponse.getResponse());
                    Gson gson = new Gson();
                    TotalCatDetails totalCatDetails = gson.fromJson(apiResponse.getResponse(), TotalCatDetails.class);
                    currencyItems = totalCatDetails.getCurrencies();
                    categoryItems = totalCatDetails.getTotalcatdetails();
                    currencySpinnerAdapter = new CurrencySpinnerAdapter(this, currencyItems);
                    categorySpinnerAdapter = new CategorySpinnerAdapter(this, categoryItems);
                    spinnerCurrencyDropdown.setAdapter(currencySpinnerAdapter);
                    spinnerCategoryDropdown.setAdapter(categorySpinnerAdapter);
                    for (int i = 0; i < currencyItems.size(); i++) {
                        if (currencyItems.get(i).getIsoCode().equalsIgnoreCase("INR")) {
                            spinnerCurrencyDropdown.setSelection(i);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case ApplicationConstants.TaskType.SETPOST:
                System.out.println("Response of post image" + apiResponse.getResponse());
                if (apiResponse.getCode() == 200) {
                    try {
                        JSONObject rootJsonObjObject = new JSONObject(apiResponse.getResponse());
                        String strMsg = rootJsonObjObject.getString("msg");
                        String strStatus = rootJsonObjObject.getString("status");
                        strPostId = rootJsonObjObject.getString("post_id");
                        String strType = rootJsonObjObject.getString("type");

                        if (strStatus.equals("true")) {
                            strPostUrl = rootJsonObjObject.getString("image");
                            imageshare = 1;
                            intent.setClass(_activity, CommonFragmentActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            _activity.finish();
                            overridePendingTransition(0, 0);
                        } else {
                            GlobalConfig.showToast(_activity, strMsg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    GlobalConfig.showToast(_activity, "Please try again");
                }
                break;

            default:
                break;
        }

    }

    private void share(final String string, final String postId) {
        if (AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken().isExpired()) {

            Bundle params = new Bundle();
            params.putString("name", "IJBT - Share your favourites");
            params.putString("message", "Try my new stuff");
            params.putString("description", captionEditText.getText().toString());
            params.putString("picture", string);
            params.putString("link", string);
            GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(),
                    "/me/feed", params, HttpMethod.POST,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse graphResponse) {
                            if (graphResponse.getError() == null) {
                                GlobalConfig.showToast(_activity, " Successfully Posted on Facebook...");
                            } else {
                                if (graphResponse.getError().getErrorCode() == 200) {
                                    LoginManager.getInstance().logInWithReadPermissions(_activity, Arrays.asList("public_profile,user_friends,publish_actions"));
                                } else {
                                    GlobalConfig.showToast(_activity, "Failed to post on Facebook..." + graphResponse.getRawResponse());
                                }
                            }
                        }
                    });
            request.executeAsync();

        } else
            LoginManager.getInstance().logInWithReadPermissions(_activity, Arrays.asList("public_profile,user_friends,publish_actions"));
    }

    // method for caputre video facebook sharing
    private void shareTOFbVideo(final String string, final String videopath,
                                final String postId) {
        if (AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken().isExpired()) {
            Bundle params = new Bundle();
            params.putString("name", "IJBT - Share your favourites");
            params.putString("message", "Try my new stuff");
            params.putString("description", captionEditText.getText().toString());
            params.putString("picture", string);
            params.putString("link", videopath);

            GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(),
                    "/me/feed", params, HttpMethod.POST,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse graphResponse) {

                            if (graphResponse.getError() == null) {
                                GlobalConfig.showToast(_activity, " Successfully Posted on Facebook...");
                                intent.setClass(_activity, CommonFragmentActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                _activity.finish();
                                overridePendingTransition(0, 0);
                            } else {
                                if (graphResponse.getError().getErrorCode() == 200) {
                                    LoginManager.getInstance().logInWithReadPermissions(_activity, Arrays.asList("public_profile,user_friends,publish_actions"));
                                } else {
                                    GlobalConfig.showToast(_activity, "Failed to post on Facebook...");
                                }
                            }

                        }
                    });
            request.executeAsync();

        } else {
            LoginManager.getInstance().logInWithReadPermissions(_activity, Arrays.asList("public_profile,user_friends,publish_actions"));
        }

    }

    /**
     * method for getting the path of that video
     *
     * @return {@link String}
     */
    public static String getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
                "PetSutra");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Demo App", "failed to create directory");
                System.out.println("failed to create directory  getOutputMediaFile");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String path = mediaStorageDir.getPath() + File.separator + timeStamp + ".mp4";

        return path;
    }

    /***************
     * get Image Bitmap thumbnail from video url(sdcard)
     ****************/
    public static Bitmap getThumbnailFromVideo(Activity activity, String video_url) {
        Bitmap bm = ThumbnailUtils.createVideoThumbnail(video_url, Images.Thumbnails.MINI_KIND);
        return bm;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(CompressFormat.JPEG, 100, bytes);
        String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    @Override
    public void onTaskComplete(String type, String imageUrl, String videoUrl, String postId) {
        capturevideoUrl = videoUrl;
        ThumbImgUrl = imageUrl = imageUrl;
        strPostId = postId;
        intent.setClass(_activity, CommonFragmentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        _activity.finish();
        overridePendingTransition(0, 0);
    }
}
