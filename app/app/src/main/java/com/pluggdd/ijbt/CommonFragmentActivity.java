package com.pluggdd.ijbt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.pluggdd.ijbt.footerfragment.MyNewsFeedFragment;
import com.pluggdd.ijbt.footerfragment.ProfileFragment;
import com.pluggdd.ijbt.util.CommonMethods;
import com.pluggdd.ijbt.util.DataPassed;
import com.pluggdd.ijbt.util.SelctedLineareLayout;

public class CommonFragmentActivity extends Activity implements
        OnClickListener, DataPassed {

    private Activity activity;
    private LinearLayout ll_newsfeed, ll_camera,
            ll_profile;
    private SelctedLineareLayout root;
    private FragmentManager fragmentManager;
    private CommonMethods commonMethods;
    private Bundle bundle;
    private String str;
    private LinearLayout llfooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.common_fragment);

        SetBodyUI();
    }

    @SuppressLint("NewApi")
    private void SetBodyUI() {
        activity = this;
        commonMethods = new CommonMethods(activity, this);
        fragmentManager = getFragmentManager();
        bundle = new Bundle();

        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.animator.enter_anim,
                        R.animator.exit_anim)
                //.replace(R.id.container, MyNewsFeedFragment.newInstance())
                .replace(R.id.container, MyNewsFeedFragment.newInstance())
                .commit();

        root = (SelctedLineareLayout) findViewById(R.id.root);

        ll_newsfeed = (LinearLayout) findViewById(R.id.ll_newsfeed);
        ll_camera = (LinearLayout) findViewById(R.id.ll_camera);
        ll_profile = (LinearLayout) findViewById(R.id.ll_profile);
        llfooter = (LinearLayout) findViewById(R.id.llfooter);
        ll_newsfeed.setOnClickListener(this);
        ll_camera.setOnClickListener(this);
        ll_profile.setOnClickListener(this);

        /***** Device ID  *****/
        String android_id = Secure.getString(getBaseContext().getContentResolver(),
                Secure.ANDROID_ID);
        System.out.println("Device=" + android_id);


        // commonMethods.actionBarProcess();
        root.setselected(0);
        fragmentManager.addOnBackStackChangedListener(getListener());
    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {

                Fragment fragment = getFragmentManager().findFragmentById(
                        R.id.container);

                if (fragment != null) {
                    if (fragment instanceof MyNewsFeedFragment) {
                        fragmentManager
                                .beginTransaction()
                                .setCustomAnimations(R.animator.enter_anim,
                                        R.animator.exit_anim)
                                .replace(R.id.container, MyNewsFeedFragment.newInstance())
                                .commit();
                    } else {
                        commonMethods.hideKeyboard();
                        fragment.onResume();
                    }
                } else {
                    finish();
                }
            }
        };
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_upload, menu);*/
        return true;
    }


    @Override
    public void onClick(View v) {
        final Fragment fragment = getFragmentManager().findFragmentById(
                R.id.container);
        switch (v.getId()) {

            case R.id.ll_newsfeed:
                if (root.getselected() != 0) {
                    root.setselected(0);
                    if (!(fragment instanceof MyNewsFeedFragment)) {
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.container,
                                        MyNewsFeedFragment.newInstance()).commit();
                    }
                }

                break;

           /* case R.id.ll_explore:
                if (root.getselected() != 1) {
                    root.setselected(1);

                    if (!(fragment instanceof ExploreFragment)) {

                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.container,
                                        ExploreFragment.newInstance()).commit();
                    }
                }
                break;*/

            case R.id.ll_camera:
                Intent intent = new Intent();
                intent.setClass(activity, ShareActivityy.class);
                activity.startActivity(intent);
                break;

           /* case R.id.ll_timeline:
                if (root.getselected() != 3) {
                    root.setselected(3);
                    if (!(fragment instanceof TimelineFragment)) {

                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.container,
                                        TimelineFragment.newInstance()).commit();
                    }
                }
                break;*/

            case R.id.ll_profile:

                if (root.getselected() != 2) {
                    root.setselected(2);
                    if ((fragment instanceof MyNewsFeedFragment)) {
                        ((MyNewsFeedFragment) fragment).removeNotAvailbleLayout();
                    }
                    if (!(fragment instanceof ProfileFragment)) {
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.container,
                                        ProfileFragment.newInstance(this)).commit();
                    }
                }
                break;

            /*case R.id.ll_favourite:
                PopupMenu popup = new PopupMenu(getBaseContext(), v);

                *//** Adding menu items to the popumenu *//*
                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

                *//** Defining menu item click listener for the popup menu *//*
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action1) {
                            if (!(fragment instanceof MyUploadsFragment)) {
                                root.setselected(5);
                                fragmentManager
                                        .beginTransaction()
                                        .replace(R.id.container,
                                                MyUploadsFragment.newInstance()).commit();
                            }
                        } else if (item.getItemId() == R.id.action2) {
                            if (!(fragment instanceof MyFavouritesFragment)) {
                                root.setselected(5);
                                fragmentManager
                                        .beginTransaction()
                                        .replace(R.id.container,
                                                MyFavouritesFragment.newInstance()).commit();
                            }
                        } else if (item.getItemId() == R.id.action3) {
                            if (!(fragment instanceof MyLikesFragment)) {
                                root.setselected(5);
                                fragmentManager
                                        .beginTransaction()
                                        .replace(R.id.container,
                                                MyLikesFragment.newInstance()).commit();
                            }
                        }
                        return true;
                    }
                });

                *//** Showing the popup menu *//*
                popup.show();

*//*
                if (root.getselected() != 5) {
                    root.setselected(5);
                    if (!(fragment instanceof MyUploadsFragment)) {

                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.container,
                                        MyUploadsFragment.newInstance()).commit();
                    }
                }*//*
                break;*/


            default:
                break;
        }
    }

    public void goToProfilePage() {
        Fragment fragment = getFragmentManager().findFragmentById(
                R.id.container);
        if (root.getselected() != 2) {
            root.setselected(2);
            if ((fragment instanceof MyNewsFeedFragment)) {
                ((MyNewsFeedFragment) fragment).removeNotAvailbleLayout();
            }
            if (!(fragment instanceof ProfileFragment)) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container,
                                ProfileFragment.newInstance(this)).commit();
            }
        }
    }

    /*
     * On Back pressed method(non-Javadoc)
     *
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            // additional code
        } else {
            super.onBackPressed();

            // Fragment fragment = getFragmentManager().findFragmentById(
            // R.id.container);
            // if (fragment instanceof SinglePostFragment) {
            // commonMethods.defaultActionBarProcess();
            // llfooter.setVisibility(View.VISIBLE);
            // ((SinglePostFragment) fragment)
            // .onRefreshCommentOnSinglePostFragment();
            // } else if (fragment instanceof MyNewsFeedFragment) {
            // commonMethods.actionBarProcess();
            // llfooter.setVisibility(View.VISIBLE);
            // ((MyNewsFeedFragment) fragment)
            // .onRefreshCommentOnMyNewsFeedFragment();
            // } else if (fragment instanceof ExploreFragment) {
            // //commonMethods.actionBarProcess();
            // commonMethods.actionBarProcessProfile(bundle);
            // } else if (fragment instanceof TimelineFragment) {
            // commonMethods.actionBarProcessProfile(bundle);
            // //commonMethods.actionBarProcess();
            // } else if (fragment instanceof ProfileFragment) {
            // commonMethods.actionBarProcessProfile(bundle);
            // } else if (fragment instanceof AboutFragment) {
            // commonMethods.ActionBarProcessForMiddletext("About ijbt");
            // } else if (fragment instanceof ChangePasswordFragment) {
            // // commonMethods.defaultActionBarProcess();
            // } else if (fragment instanceof CommentFragment) {
            // llfooter.setVisibility(View.GONE);
            // commonMethods.defaultActionBarProcess();
            // } else if (fragment instanceof EditProfileFragment) {
            // // commonMethods.defaultActionBarProcess();
            // } else if (fragment instanceof FindPeopleFragment) {
            // commonMethods.defaultActionBarProcess();
            // } else if (fragment instanceof FollowerFollowingFragment) {
            // commonMethods
            // .ActionBarProcessForMiddletext(Comman.forStoreHeaderFollow);
            // } else if (fragment instanceof HashTagFragment) {
            // commonMethods.ActionBarProcessForMiddletext("#"
            // + Comman.forStoreHashTagName);
            // } else if (fragment instanceof InviteFragment) {
            // commonMethods
            // .ActionBarProcessForMiddletext(Comman.forStoreHeaderInvite);
            // } else if (fragment instanceof LookagramPlanFragment) {
            // // commonMethods.ActionBarProcessForMiddletext(str);
            // } else if (fragment instanceof OtherUserProfileFragment) {
            // commonMethods.defaultActionBarProcess();
            // } else if (fragment instanceof PrivacyPolicyFragment) {
            // commonMethods.ActionBarProcessForMiddletext("Privacy Policy");
            // } else if (fragment instanceof PushNotificationFragment) {
            // commonMethods
            // .ActionBarProcessForMiddletext("Push Notifications");
            // } else if (fragment instanceof RequestFragment) {
            // commonMethods.defaultActionBarProcess();
            // } else if (fragment instanceof SearchFragment) {
            // commonMethods.defaultActionBarProcess();
            // } else if (fragment instanceof SinglePostFragment) {
            // commonMethods.defaultActionBarProcess();
            // } else if (fragment instanceof TermsAndConditionFragment) {
            // commonMethods.ActionBarProcessForMiddletext("Terms of Service");
            // } else if (fragment instanceof TotalVotersFragment) {
            // commonMethods.ActionBarProcessForMiddletext("Total Likes");
            // } else if (fragment instanceof LookagramPlanFragment) {
            // commonMethods.defaultActionBarProcess();
            // } else if (fragment instanceof SettingFragment) {
            // commonMethods.defaultActionBarProcess();
            // }

        }
    }

    @Override
    public void onSuccess(String tag) {
        this.str = tag;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onActivityResult(int, int,
     * android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getFragmentManager().findFragmentById(
                R.id.container);
        fragment.onActivityResult(requestCode, resultCode, data);

    }

}
