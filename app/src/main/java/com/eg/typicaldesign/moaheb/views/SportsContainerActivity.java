package com.eg.typicaldesign.moaheb.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.controllers.AppKeys;
import com.eg.typicaldesign.moaheb.controllers.Service;
import com.eg.typicaldesign.moaheb.models.GeneralResponse;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class SportsContainerActivity extends BaseActivityPlayer implements View.OnClickListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    String token;
    private TextView loginForPlayer, loginForSponsor;
    SharedPreferences dataSaver;
    int city_id, gov_id, playerId, sponsorId;
    LinearLayout notLogged;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean isSponsor = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_container);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        notLogged = (LinearLayout) findViewById(R.id.not_logged);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        city_id = dataSaver.getInt(AppKeys.CITY_ID, -1);
        gov_id = dataSaver.getInt(AppKeys.GOVERNORATE_ID, -1);
        playerId = dataSaver.getInt(AppKeys.ID_KEY, -1);
        sponsorId = dataSaver.getInt(AppKeys.ID_KEY, -1);
        token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        isSponsor = dataSaver.getBoolean(AppKeys.IS_SPONSOR_ACCOUNT, false);
        if (isSponsor == true) {
            navigationView1 = (BottomNavigationView) findViewById(R.id.navigation1);
            navigationView1.setOnNavigationItemSelectedListener(this);
            sendFCMForSponsor();
        } else {
            sendFCMForPlayer();
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }

        });
        if (playerId != -1 && isSponsor == false) {
//            token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
//            sendFCMForPlayer();
            navigationView.setVisibility(View.VISIBLE);
            // navigationView1.setVisibility(View.GONE);
            notLogged.setVisibility(View.GONE);

            if (city_id == -1 || gov_id == -1) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SportsContainerActivity.this/*,R.style.MyDialogTheme*/);

                TextView myMsg = new TextView(this);
                myMsg.setText("من فضلك اكمل بياناتك الشخصية وبيانات الاتصال بك لسهولة الوصول اليك");
                myMsg.setTextSize(15);
                myMsg.setGravity(Gravity.CENTER);
                myMsg.setPadding(30, 50, 30, 25);
                myMsg.setTextColor(Color.parseColor("#530707"));
                Typeface typeface = Typeface.createFromAsset(getAssets(), "bein-normal.ttf");
                myMsg.setTypeface(typeface);
                builder.setView(myMsg).setCancelable(false)
                        .setPositiveButton("اكمل الان", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(SportsContainerActivity.this, RegisterForPlayer.class));

                            }
                        }).setNegativeButton("لاحقا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                }).create();
                final AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        Button negButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                        Button posButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);

                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#ffffff"));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#530707"));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundResource(R.drawable.negative_alert_dialog);
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundResource(R.drawable.positive_alert_dialog);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int height = displayMetrics.heightPixels;
                        int width = displayMetrics.widthPixels;
                        params.width = (int) (width / 3.5);
                        params.height = (int) (height / 13);
                        params.gravity = Gravity.START;
                        params.setMargins(0, 10, 0, 10);
                        negButton.setLayoutParams(params);

                        params.width = (int) (width / 3.5);
                        params.height = (int) (height / 13);
                        params.gravity = Gravity.END;
                        params.setMargins(0, 10, (int) (height / 16), 10);
                        posButton.setLayoutParams(params);


                    }

                });

//                dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog);
//                dialog.show();
                final Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawableResource(R.drawable.alert_dialog);
                window.setGravity(Gravity.CENTER);
                dialog.show();
                //    dialog.getWindow().setLayout(500, 350);
            }

        }
        if (sponsorId != -1 && isSponsor == true) {
//            token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
//            sendFCMForSponsor();
            navigationView1.setVisibility(View.VISIBLE);
            navigationView.setVisibility(View.GONE);
            notLogged.setVisibility(View.GONE);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        loginForPlayer = (TextView) findViewById(R.id.player);
        loginForSponsor = (TextView) findViewById(R.id.sponsor);
        loginForSponsor.setOnClickListener(this);
        loginForPlayer.setOnClickListener(this);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }

    public void sendFCMForPlayer() {
        String fcmToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("TAG", "fcmToken " + fcmToken);
        Service.Fetcher.getInstance().sendFCMForPlayer(token, fcmToken).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.isSuccessful()) {
                    GeneralResponse generalResponse = response.body();
                    if (generalResponse.getStatus() == 1) {
                        //  Toast.makeText(SportsContainerActivity.this, "تم اضافة FCM", Toast.LENGTH_LONG).show();
                    } else {
                        //   Toast.makeText(SportsContainerActivity.this, "لم يتم اضافة FCM", Toast.LENGTH_LONG).show();
                    }
                } else {
                    //  Toast.makeText(SportsContainerActivity.this, "not Successful", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
            }
        });
    }


    public void sendFCMForSponsor() {
        String fcmToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("TAG", "fcmToken " + fcmToken);
        Service.Fetcher.getInstance().sendFCMForSponsor(token, fcmToken).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.isSuccessful()) {
                    GeneralResponse generalResponse = response.body();
                    if (generalResponse.getStatus() == 1) {
                        // Toast.makeText(SportsContainerActivity.this, "تم اضافة FCM", Toast.LENGTH_LONG).show();
                    }
                    // Toast.makeText(SportsContainerActivity.this, "لم يتم اضافة FCM", Toast.LENGTH_LONG).show();

                } else {
                    // Toast.makeText(SportsContainerActivity.this, "not Successful", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    int getContentViewId() {
        if (isSponsor == true) {
            return R.layout.activity_sports_container;

        }
        return R.layout.activity_sports_container;
    }

    @Override
    int getNavigationMenuItemId() {
        if (isSponsor == true) {
            return R.id.navigation1_home;
        }
        return R.id.navigation_home;
    }

    @Override
    public void onClick(View view) {
        if (view == loginForPlayer) {
            Intent intent = new Intent(this, LoginForPlayer.class);
            startActivity(intent);

            finish();

        }
        if (view == loginForSponsor) {
            Intent intent = new Intent(this, LoginForSponsor.class);
            startActivity(intent);

            finish();

        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    SportsTeamFragment sportsTeamFragment = new SportsTeamFragment();
                    return sportsTeamFragment;

                case 1:
                    SportsSingleFragment sportsSingleFragment = new SportsSingleFragment();
                    return sportsSingleFragment;


            }
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
