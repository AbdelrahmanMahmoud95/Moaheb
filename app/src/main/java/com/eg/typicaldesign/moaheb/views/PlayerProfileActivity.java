package com.eg.typicaldesign.moaheb.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.controllers.AppKeys;

import me.anwarshahriar.calligrapher.Calligrapher;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class PlayerProfileActivity extends BaseActivityPlayer implements View.OnClickListener {
    LinearLayout userData, userSportData, userGuardian, userImages, userVideos,
            logOut,address, userDocs;
    SharedPreferences dataSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);
        userData = (LinearLayout) findViewById(R.id.user_data);
        userGuardian = (LinearLayout) findViewById(R.id.guardian);
        userImages = (LinearLayout) findViewById(R.id.user_images);
        userVideos = (LinearLayout) findViewById(R.id.user_videos);
        userSportData = (LinearLayout) findViewById(R.id.user_sport);
        address = (LinearLayout) findViewById(R.id.user_location);
        logOut = (LinearLayout) findViewById(R.id.log_out);
        userDocs = (LinearLayout) findViewById(R.id.user_docs);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "bein-normal.ttf", true);

        userSportData.setOnClickListener(this);
        userVideos.setOnClickListener(this);
        userImages.setOnClickListener(this);
        userDocs.setOnClickListener(this);
        userGuardian.setOnClickListener(this);
        userData.setOnClickListener(this);
        address.setOnClickListener(this);
        logOut.setOnClickListener(this);

    }

    @Override
    int getContentViewId() {
        return R.layout.activity_player_profile;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_more;
    }

    @Override
    public void onClick(View view) {
        if (view == userData) {
            startActivity(new Intent(this, RegisterForPlayer.class));

        }
        if (view == userVideos) {
            startActivity(new Intent(this, PlayerVideosActivity.class));

        }
        if (view == userImages) {
            startActivity(new Intent(this, PlayerImagesActivity.class));

        }
        if (view == userSportData) {
            Intent intent = new Intent(PlayerProfileActivity.this, PlayerSportsDataActivity.class);
            intent.putExtra("userSportData",1);
            startActivity(intent);
        }
        if (view == userGuardian) {
            startActivity(new Intent(this, PlayerGuardianActivity.class));

        }
        if (view == address) {
            startActivity(new Intent(this, PlayerAddressActivity.class));

        }
        if (view == userDocs) {
            startActivity(new Intent(this, PlayerDocumentsActivity.class));

        }
        if (view == logOut) {

            dataSaver.edit()
                    .putInt(AppKeys.ID_KEY, -1)
                    .commit();

            dataSaver.edit()
                    .putString(AppKeys.TOKEN_KEY, "")
                    .commit();

            Intent intentLogOut = new Intent(PlayerProfileActivity.this, LoginForPlayer.class);
            startActivity(intentLogOut);
            finish();
        }

    }

}

