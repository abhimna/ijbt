package com.pluggdd.ijbt.fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;
import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.adapter.ContactsAdapter;
import com.pluggdd.ijbt.adapter.SuggestionAdapter;
import com.pluggdd.ijbt.network.APIResponse;
import com.pluggdd.ijbt.network.IJBTResponseController;
import com.pluggdd.ijbt.util.ApplicationConstants;
import com.pluggdd.ijbt.util.CommonMethods;
import com.pluggdd.ijbt.util.GlobalConfig;
import com.pluggdd.ijbt.util.SignatureCommonMenthods;
import com.pluggdd.ijbt.util.URLFactory;
import com.pluggdd.ijbt.vo.ContactVO;
import com.pluggdd.ijbt.vo.GsonParseContacts;
import com.pluggdd.ijbt.vo.GsonParseSuggestion;
import com.pluggdd.ijbt.vo.GsonParseSuggestion.Suggestions;
import com.pluggdd.ijbt.vo.InviteContactAllData;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@SuppressLint({ "NewApi" })
public class InviteFragment extends Fragment implements
		IJBTResponseController, OnClickListener {
	private Activity activity;
	private ArrayList<ContactVO> al_users_list = new ArrayList();
	private CommonMethods commonMethods;
	private ContactsAdapter contactAdapter;
	private ArrayList<InviteContactAllData> contactDatasAll = new ArrayList();
	private LinearLayout ll_follow_everyone;
	private ListView lv_invite;
	private List<NameValuePair> nameValuePairs;
	private ProgressDialog pDialog;
	private String str_follow;
	private SuggestionAdapter suggestionAdapter;
	private String userid;
	private View view;
	ArrayList<String> al_fb = new ArrayList<String>();
	JSONArray jsonArray2;
	int int_total_page = 1;
	private boolean loadingMore = false;
	private int pageNumberForService = 1;
	GsonParseSuggestion gsonParseSuggestion;
	GsonParseContacts gsonParseContacts;
	protected int tasktype;

	public InviteFragment(String paramString) {
		this.str_follow = paramString;
	}

	public static InviteFragment newInstance(String paramString) {
		InviteFragment fragment = new InviteFragment(paramString);
		return fragment;
	}

	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		view = paramLayoutInflater.inflate(R.layout.invite, paramViewGroup,
				false);
		setBodyUI();
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		defaultactionbar(str_follow);
	}

	@SuppressLint({ "SetJavaScriptEnabled" })
	private void setBodyUI() {
		activity = getActivity();
		userid = activity.getSharedPreferences("prefs_login", 0).getString(
				"userid", "");
		commonMethods = new CommonMethods(activity, this);
		// commonMethods.ActionBarProcessForMiddletext(str_follow);
		defaultactionbar(str_follow);

		lv_invite = ((ListView) view.findViewById(R.id.lv_invite));
		ll_follow_everyone = ((LinearLayout) view
				.findViewById(R.id.ll_follow_everyone));
		ll_follow_everyone.setOnClickListener(this);
		if (str_follow.equals("SUGGESTED")) {
			if (commonMethods.getConnectivityStatus()) {
				try {
					gsonParseSuggestion = null;
					tasktype = ApplicationConstants.TaskType.GETSUGGESTION;
					commonMethods.GetSuggestionListUser(commonMethods
							.getSuggestionListUserRequestParmas(userid,
									pageNumberForService));
				} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
					localUnsupportedEncodingException.printStackTrace();
				}
			} else {

				GlobalConfig.showToast(
						activity,
						getResources().getString(
								R.string.internet_error_message));
			}
		}

		else if (str_follow.equals("CONTACTS")) {
			new FillArraylistAsyncTask().execute();
		} else if (str_follow.equals("FACEBOOK")) {
			facebookFriends();
			/*Session localSession = Session.getActiveSession();
			if ((localSession != null) && (localSession.isOpened())) {
				facebookFriends();
			} else {
				facebookConnection.loginFromFaceBook(this);
				facebookFriends();
			}*/
		}

		if (str_follow.equals("SUGGESTED")) {
			lv_invite.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
					System.out.println("State Change is---" + scrollState);

				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					System.out.println("scroll starting");

					int i = firstVisibleItem + visibleItemCount;

					System.out.println("scroll view item > " + i + " / "
							+ totalItemCount + " / " + pageNumberForService
							+ " / " + int_total_page + " / " + loadingMore);

					if ((i == totalItemCount) && (!loadingMore)) {

						System.out.println("scroll in condition");

						System.out
								.println("scroll view load more data called 1");
						if (pageNumberForService <= int_total_page) {
							System.out
									.println("scroll view load more data called 2");
							loadingMore = true;
							callSecondTimeWebServices(pageNumberForService);
						}
					}
					System.out.println("scroll out condition");
				}

				private void callSecondTimeWebServices(int pageNumberForService) {
					if (commonMethods.getConnectivityStatus()) {
						try {
							commonMethods.GetSuggestionListUser(commonMethods
									.getSuggestionListUserRequestParmas(userid,
											pageNumberForService));
						} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
							localUnsupportedEncodingException.printStackTrace();
						}
					} else {

						GlobalConfig.showToast(activity, getResources()
								.getString(R.string.internet_error_message));
					}

				}
			});
		}

	}

	private void defaultactionbar(String str) {

		ActionBar mActionBar = activity.getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(activity);
		View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);

		// content of action bar

		ImageView logo = (ImageView) mCustomView.findViewById(R.id.logo);
		ImageView imgbtn_back_home = (ImageView) mCustomView
				.findViewById(R.id.imgbtn_back_home);
		ImageView imgbtn_setting = (ImageView) mCustomView
				.findViewById(R.id.imgbtn_setting);
		ImageView imgbtn_notification = (ImageView) mCustomView
				.findViewById(R.id.imgbtn_notification);
		TextView Middle_text = (TextView) mCustomView
				.findViewById(R.id.Middle_text);
		TextView txt_reset = (TextView) mCustomView
				.findViewById(R.id.txt_reset);
		TextView txt_cancel = (TextView) mCustomView
				.findViewById(R.id.txt_cancel);
		Middle_text.setText(str);
		txt_reset.setVisibility(View.INVISIBLE);
		Middle_text.setVisibility(View.VISIBLE);
		txt_cancel.setVisibility(View.INVISIBLE);
		logo.setVisibility(View.VISIBLE);
		imgbtn_back_home.setVisibility(View.VISIBLE);
		imgbtn_setting.setVisibility(View.INVISIBLE);
		imgbtn_notification.setVisibility(View.INVISIBLE);

		RelativeLayout ll_left_icon = (RelativeLayout) mCustomView
				.findViewById(R.id.ll_left_icon);
		RelativeLayout ll_right_icon = (RelativeLayout) mCustomView
				.findViewById(R.id.ll_right_icon);

		ll_left_icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.onBackPressed();

			}
		});

		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

	}

	private void defaultActionBarProcess(String paramString) {
		ActionBar mActionBar = activity.getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);

		LayoutInflater mInflater = LayoutInflater.from(activity);
		View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);

		ImageView logo = (ImageView) mCustomView.findViewById(R.id.logo);
		ImageView imgbtn_back_home = (ImageView) mCustomView
				.findViewById(R.id.imgbtn_back_home);
		ImageView imgbtn_setting = (ImageView) mCustomView
				.findViewById(R.id.imgbtn_setting);

		TextView Middle_text = (TextView) mCustomView
				.findViewById(R.id.Middle_text);
		TextView txt_reset = (TextView) mCustomView
				.findViewById(R.id.txt_reset);
		TextView txt_cancel = (TextView) mCustomView
				.findViewById(R.id.txt_cancel);

		txt_reset.setVisibility(View.INVISIBLE);
		Middle_text.setVisibility(View.VISIBLE);
		txt_cancel.setVisibility(View.INVISIBLE);
		logo.setVisibility(View.GONE);
		imgbtn_back_home.setVisibility(View.VISIBLE);
		imgbtn_setting.setVisibility(View.INVISIBLE);

		Middle_text.setText(paramString);

		RelativeLayout ll_left_icon = (RelativeLayout) mCustomView
				.findViewById(R.id.ll_left_icon);
		RelativeLayout ll_right_icon = (RelativeLayout) mCustomView
				.findViewById(R.id.ll_right_icon);

		ll_left_icon.setOnClickListener(new OnClickListener() {
			public void onClick(View paramAnonymousView) {
				activity.onBackPressed();
			}
		});
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
	}

	private void dialogFollow() {
		new AlertDialog.Builder(activity)
				.setTitle("Are you sure?")
				.setPositiveButton("Yes I'm sure",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface paramAnonymousDialogInterface,
									int paramAnonymousInt) {
								setFollowAll();
								// if (str_follow.equals("SUGGESTED")) {
								// } else if (str_follow.equals("CONTACTS")) {
								//
								// } else if (str_follow.equals("SUGGESTED")) {
								//
								// }
							}

						}).setNegativeButton("Cancel", null).show();
	}

	private void facebookFriends() {

		new GraphRequest(
				AccessToken.getCurrentAccessToken(),
				"me/friends",
				null,
				HttpMethod.GET,
				new GraphRequest.Callback() {
					public void onCompleted(GraphResponse response) {
						try {
							JSONObject graphObj = response.getJSONObject();
							JSONArray jsonArrayFriends = graphObj.getJSONArray("data");
							for (int i = 0; i < jsonArrayFriends.length(); i++) {
								JSONObject friendObject = jsonArrayFriends.getJSONObject(i);
								String id = friendObject.getString("id");
								al_fb.add(id);
							}
							jsonArray2 = new JSONArray();
							for (int i = 0; i < al_fb.size(); i++) {
								JSONObject friendObject = new JSONObject();
								friendObject.put("id", al_fb.get(i));
								jsonArray2.put(friendObject);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						new GetFbContactAsyncTask().execute();
					}
				}
		).executeAsync();

		/*new Request(Session.openActiveSessionFromCache(this.activity),
				"me/friends", null, HttpMethod.GET, new Request.Callback() {
					public void onCompleted(Response paramAnonymousResponse) {

						try {
							JSONObject rootJsonObjObject = new JSONObject(
									paramAnonymousResponse.getRawResponse());

							JSONArray jsonArray = rootJsonObjObject
									.getJSONArray("data");

							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObject = jsonArray
										.getJSONObject(i);

								String id = jsonObject.getString("id");
								al_fb.add(id);
							}

							jsonArray2 = new JSONArray();

							for (int i = 0; i < al_fb.size(); i++) {

								JSONObject jsonObject = new JSONObject();

								jsonObject.put("id", al_fb.get(i));

								jsonArray2.put(jsonObject);

							}

						} catch (Exception e) {
							// TODO: handle exception
						}
						new GetFbContactAsyncTask().execute();
					}
				}).executeAsync();*/
	}

	public void OnComplete(APIResponse paramAPIResponse) {

		switch (tasktype) {
		case ApplicationConstants.TaskType.GETSUGGESTION:
			System.out.println("Response of get seggestion"
					+ paramAPIResponse.getResponse());
			if (paramAPIResponse.getCode() == 200) {

				Gson gson = new Gson();
				if (gsonParseSuggestion == null) {
					gsonParseSuggestion = gson.fromJson(
							paramAPIResponse.getResponse(),
							GsonParseSuggestion.class);
					System.out.println("hhhhhhhhh------"
							+ gsonParseSuggestion.getTotal_page());
//					 int_total_page = Integer.parseInt(gsonParseSuggestion
//					 .getTotal_page());
					if (gsonParseSuggestion.getStatus().equals("true")) {
						this.suggestionAdapter = new SuggestionAdapter(
								this.activity,
								gsonParseSuggestion.getSuggestions());
						this.lv_invite.setAdapter(this.suggestionAdapter);
					}

					if (pageNumberForService <= int_total_page) {
						pageNumberForService = (1 + pageNumberForService);
					}
					loadingMore = false;
				} else {
					GsonParseSuggestion localGsonParseSuggestion = gson
							.fromJson(paramAPIResponse.getResponse(),
									GsonParseSuggestion.class);
//					int_total_page = Integer.parseInt(localGsonParseSuggestion
//							.getTotal_page());
					if (localGsonParseSuggestion.getStatus().equals("true")) {
						gsonParseSuggestion.getSuggestions().addAll(
								localGsonParseSuggestion.getSuggestions());
						suggestionAdapter.setData(gsonParseSuggestion
								.getSuggestions());
						loadingMore = false;
					}

					if (pageNumberForService <= int_total_page) {
						pageNumberForService = (1 + pageNumberForService);
					}
				}

			} else {
				GlobalConfig.showToast(this.activity, "Please try again");
			}

			break;

		case ApplicationConstants.TaskType.SETFOLLOWALL:
			System.out.println("Response of set follow all"
					+ paramAPIResponse.getResponse());

			if (paramAPIResponse.getCode() == 200) {
				try {
					JSONObject rootJsonObjObject = new JSONObject(
							paramAPIResponse.getResponse());
					String strMsg = rootJsonObjObject.getString("msg");
					String strStatus = rootJsonObjObject.getString("status");

					if (strStatus.equals("true")) {

						JSONArray jsonArray = rootJsonObjObject
								.getJSONArray("users");

						Gson gson = new Gson();

						if (str_follow.equals("SUGGESTED")) {
							ArrayList<Suggestions> arr = new ArrayList<Suggestions>();

							for (int i = 0; i < jsonArray.length(); i++) {

								Suggestions suggestions = gson.fromJson(
										jsonArray.getJSONObject(i).toString(),
										Suggestions.class);

								arr.add(suggestions);
							}

							for (int i = 0; i < arr.size(); i++) {
								gsonParseSuggestion
										.getSuggestions()
										.get(i)
										.setFollow_status(
												arr.get(i).getFollow_status());
							}

							suggestionAdapter.notifyDataSetChanged();
						} else if (str_follow.equals("CONTACTS")) {
							ArrayList<GsonParseContacts.Contacts> arr = new ArrayList<GsonParseContacts.Contacts>();

							for (int i = 0; i < jsonArray.length(); i++) {

								GsonParseContacts.Contacts contacts = gson.fromJson(jsonArray
										.getJSONObject(i).toString(),
										GsonParseContacts.Contacts.class);

								arr.add(contacts);
							}

							for (int i = 0; i < arr.size(); i++) {
								gsonParseContacts
										.getContacts()
										.get(i)
										.setFollow_status(
												arr.get(i).getFollow_status());
							}

							contactAdapter.notifyDataSetChanged();
						} else if (str_follow.equals("FACEBOOK")) {
							ArrayList<GsonParseContacts.Contacts> arr = new ArrayList<GsonParseContacts.Contacts>();

							for (int i = 0; i < jsonArray.length(); i++) {

								GsonParseContacts.Contacts contacts = gson.fromJson(jsonArray
										.getJSONObject(i).toString(),
										GsonParseContacts.Contacts.class);

								arr.add(contacts);
							}

							for (int i = 0; i < arr.size(); i++) {
								gsonParseContacts
										.getContacts()
										.get(i)
										.setFollow_status(
												arr.get(i).getFollow_status());
							}

							contactAdapter.notifyDataSetChanged();
						}

					} else {
						GlobalConfig.showToast(activity, strMsg);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				GlobalConfig.showToast(activity, "Please try again");
			}

			break;

		default:
			break;
		}

	}



	@Override
	public void onClick(View paramView) {
		switch (paramView.getId()) {

		case R.id.ll_follow_everyone:
			dialogFollow();
			break;

		default:
			return;
		}

	}

	public ArrayList<InviteContactAllData> getNameEmailDetails() {
		// contactDatasAll = new ArrayList<InviteContactAllData>();
		InviteContactAllData localInviteContactAllData = new InviteContactAllData();
		ContentResolver cr = activity.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				Cursor cur1 = cr.query(
						ContactsContract.CommonDataKinds.Email.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Email.CONTACT_ID
								+ " = ?", new String[] { id }, null);
				while (cur1.moveToNext()) {
					// to get the contact names
					String name = cur1
							.getString(cur1
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					localInviteContactAllData.setContact_names(name);
					Log.e("Name :", name);
					String email = cur1
							.getString(cur1
									.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
					localInviteContactAllData.setContact_email(email);
					Log.e("Email", email);
					if (email != null) {
						contactDatasAll.add(localInviteContactAllData);
					}
				}
				cur1.close();
			}
		}
		return contactDatasAll;
	}

	public class FillArraylistAsyncTask extends
			AsyncTask<String, String, ArrayList<InviteContactAllData>> {

		public FillArraylistAsyncTask() {
		}

		protected ArrayList<InviteContactAllData> doInBackground(
				String... paramVarArgs) {
			contactDatasAll.clear();

			return getNameEmailDetails();
		}

		protected void onPostExecute(
				ArrayList<InviteContactAllData> paramArrayList) {
			System.out.println("size......" + paramArrayList.size());
			String str = new Gson().toJson(paramArrayList);
			try {
				nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("userid",
						InviteFragment.this.userid));
				nameValuePairs.add(new BasicNameValuePair("contact_type",
						"contacts"));
				nameValuePairs.add(new BasicNameValuePair("sign_up", "0"));
				nameValuePairs.add(new BasicNameValuePair("contacts", str));
				new InviteFragment.GetContactAsyncTask().execute();
			} catch (Exception localException) {
				localException.printStackTrace();
			}
		}

		protected void onPreExecute() {
			pDialog = ProgressDialog.show(activity, null, "");
			pDialog.setContentView(R.layout.view_progress);
			pDialog.getWindow().clearFlags(2);
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
	}

	public class GetContactAsyncTask extends AsyncTask<String, Process, String> {
		ProgressDialog mprogressDialog;
		String response = "";
		String strMsg;
		String strStatus;

		public GetContactAsyncTask() {
		}

		protected String doInBackground(String... paramVarArgs) {
			InviteFragment.this.al_users_list.clear();
			ArrayList<String> localArrayList = new ArrayList<String>();
			localArrayList.add("getusercontacts");
			localArrayList.add("userid");
			localArrayList.add("contact_type");
			localArrayList.add("contacts");
			localArrayList.add("sign_up");
			String str = SignatureCommonMenthods
					.getSignatureForAPI(localArrayList);
			DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
			System.out.println("url" + URLFactory.getContactUrl()
					+ "&signature=" + str);
			HttpPost localHttpPost = new HttpPost(URLFactory.getContactUrl()
					+ "&signature=" + str);
			int i = 0;
			for (;;) {
				if (i >= InviteFragment.this.nameValuePairs.size()) {
				}
				try {
					localHttpPost.setEntity(new UrlEncodedFormEntity(
							InviteFragment.this.nameValuePairs));
					this.response = getStringFromInputStream(localDefaultHttpClient
							.execute(localHttpPost).getEntity().getContent());
					System.out.println("values response"
							+ this.response.toString());
					label231: return this.response;
					// System.out
					// .println("values"
					// + ((NameValuePair) InviteFragment.this.nameValuePairs
					// .get(i)).getName()
					// + "---"
					// + ((NameValuePair) InviteFragment.this.nameValuePairs
					// .get(i)).getValue());
					// i++;
				} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
					localUnsupportedEncodingException.printStackTrace();
				} catch (IOException localIOException) {
					localIOException.printStackTrace();
				}
			}
		}

		/**
		 * Method is used for get string from input strem
		 * 
		 * @param is
		 * @return
		 */

		public String getStringFromInputStream(InputStream is) {

			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();

			String line;
			try {

				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return sb.toString();

		}

		protected void onPostExecute(String paramString) {
			if (InviteFragment.this.pDialog != null) {
				InviteFragment.this.pDialog.dismiss();
				InviteFragment.this.pDialog = null;
			}
			System.out.println("Result" + paramString);
			if (!paramString.equals(null)) {
				gsonParseContacts = (GsonParseContacts) new Gson().fromJson(
						paramString, GsonParseContacts.class);
				if (gsonParseContacts.getStatus().equals("true")) {
					InviteFragment.this.contactAdapter = new ContactsAdapter(
							InviteFragment.this.activity,
							gsonParseContacts.getContacts());
					InviteFragment.this.lv_invite
							.setAdapter(InviteFragment.this.contactAdapter);
				}
			} else {
				GlobalConfig.showToast(InviteFragment.this.activity,
						"Please try again");
			}

		}

		protected void onPreExecute() {
		}
	}

	public class GetFbContactAsyncTask extends
			AsyncTask<String, Process, String> {
		ProgressDialog mprogressDialog;
		String response = "";
		String strMsg;
		String strStatus;

		public GetFbContactAsyncTask() {
		}

		protected String doInBackground(String... paramVarArgs) {
			al_fb.clear();

			// String str = new Gson().toJson(al_fb);
			try {
				nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("userid", userid));
				nameValuePairs.add(new BasicNameValuePair("contact_type",
						"FACEBOOK"));
				nameValuePairs.add(new BasicNameValuePair("sign_up", "0"));
				nameValuePairs.add(new BasicNameValuePair("contacts",
						jsonArray2.toString()));
				//new InviteFragment.GetContactAsyncTask().execute();
			} catch (Exception localException) {
				localException.printStackTrace();
			}

			ArrayList<String> localArrayList = new ArrayList<String>();
			localArrayList.add("getusercontacts");
			localArrayList.add("userid");
			localArrayList.add("contact_type");
			localArrayList.add("contacts");
			localArrayList.add("sign_up");
			String str_signature = SignatureCommonMenthods
					.getSignatureForAPI(localArrayList);
			DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
			System.out.println("url" + URLFactory.getContactUrl()
					+ "&signature=" + str_signature);
			HttpPost localHttpPost = new HttpPost(URLFactory.getContactUrl()
					+ "&signature=" + str_signature);
			try {
				localHttpPost
						.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				response = getStringFromInputStream(localDefaultHttpClient
						.execute(localHttpPost).getEntity().getContent());
				System.out
						.println("values response" + this.response.toString());

			} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
				localUnsupportedEncodingException.printStackTrace();
			} catch (IOException localIOException) {
				localIOException.printStackTrace();
			}
			return response;
		}

		/**
		 * Method is used for get string from input strem
		 * 
		 * @param is
		 * @return
		 */

		public String getStringFromInputStream(InputStream is) {

			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();

			String line;
			try {

				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return sb.toString();

		}

		protected void onPostExecute(String paramString) {
			if (InviteFragment.this.pDialog != null) {
				InviteFragment.this.pDialog.dismiss();
				InviteFragment.this.pDialog = null;
			}
			System.out.println("Result" + paramString);
			if (!paramString.equals(null)) {
				gsonParseContacts = (GsonParseContacts) new Gson().fromJson(
						paramString, GsonParseContacts.class);
				if (gsonParseContacts.getStatus().equals("true")) {
					contactAdapter = new ContactsAdapter(activity,
							gsonParseContacts.getContacts());
					lv_invite.setAdapter(contactAdapter);
				}
			} else {
				GlobalConfig.showToast(activity, "Please try again");
			}

		}

		protected void onPreExecute() {
		}
	}

	private void setFollowAll() {
		String contactJsonArray = null;
		if (str_follow.equals("SUGGESTED")) {
			for (int i = 0; i < gsonParseSuggestion.getSuggestions().size(); i++) {
				gsonParseSuggestion.getSuggestions().get(i)
						.setFollow_status("1");

				contactJsonArray = new Gson().toJson(gsonParseSuggestion
						.getSuggestions());
			}
		} else if (str_follow.equals("CONTACTS")) {
			for (int i = 0; i < gsonParseContacts.getContacts().size(); i++) {
				gsonParseContacts.getContacts().get(i).setFollow_status("1");

				contactJsonArray = new Gson().toJson(gsonParseContacts
						.getContacts());
			}
		} else if (str_follow.equals("FACEBOOK")) {
			if (gsonParseContacts.getContacts() == null) {
				GlobalConfig.showToast(activity, "No Friend to  follow");

			} else {
				for (int i = 0; i < gsonParseContacts.getContacts().size(); i++) {
					gsonParseContacts.getContacts().get(i)
							.setFollow_status("1");

					contactJsonArray = new Gson().toJson(gsonParseContacts
							.getContacts());
				}
			}

		}

		if (commonMethods.getConnectivityStatus()) {
			try {
				try {
					tasktype = ApplicationConstants.TaskType.SETFOLLOWALL;
					commonMethods.setAllFollow(commonMethods
							.setAllowRequestParmas(userid, contactJsonArray));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
				localUnsupportedEncodingException.printStackTrace();
			}
		} else {
			GlobalConfig.showToast(
					activity,
					activity.getResources().getString(
							R.string.internet_error_message));
		}
	}

}
