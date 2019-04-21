package com.eg.typicaldesign.moaheb.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.controllers.AppKeys;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by MAGIC on 5/3/2018.
 */


public abstract class BaseActivityPlayer extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    protected BottomNavigationView navigationView;
    protected BottomNavigationView navigationView1;
    SharedPreferences dataSaver;
    boolean isSponsor = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        isSponsor = dataSaver.getBoolean(AppKeys.IS_SPONSOR_ACCOUNT, false);
        if (isSponsor == true) {
            navigationView1 = (BottomNavigationView) findViewById(R.id.navigation1);
            navigationView1.setOnNavigationItemSelectedListener(this);
            updateNavigationBarState1();
        }


        updateNavigationBarState();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isSponsor == true) {
            updateNavigationBarState1();
        }
        updateNavigationBarState();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (isSponsor == false) {
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    startActivity(new Intent(this, SportsContainerActivity.class));

                    break;
                case R.id.navigation_images:
                    startActivity(new Intent(this, PlayerImagesActivity.class));

                    break;
                case R.id.navigation_video:
                    startActivity(new Intent(this, PlayerVideosActivity.class));
                    break;
                case R.id.navigation_data:
                    startActivity(new Intent(this, RegisterForPlayer.class));

                    break;
                case R.id.navigation_more:
                    startActivity(new Intent(this, PlayerProfileActivity.class));
                    finish();
                    break;

                default:
                    return false;

            }

            finish();

        } else {
            switch (item.getItemId()) {

                case R.id.navigation1_home:
                    startActivity(new Intent(this, SportsContainerActivity.class));

                    break;
                case R.id.navigation1_search:
                    startActivity(new Intent(this, SponsorSearchActivity.class));

                    break;
                case R.id.navigation1_more:
                    dataSaver.edit()
                            .putInt(AppKeys.ID_KEY, -1)
                            .commit();

                    dataSaver.edit()
                            .putString(AppKeys.TOKEN_KEY, "")
                            .commit();

                    Intent intentLogOut = new Intent(BaseActivityPlayer.this, LoginForSponsor.class);
                    startActivity(intentLogOut);
                    break;
                case R.id.navigation1_profile:

                    Intent intent = new Intent(BaseActivityPlayer.this, RegisterForSponsor.class);
                    startActivity(intent);

                    break;


                default:
                    return false;

            }
            finish();

        }

        return true;
    }


    private void updateNavigationBarState() {
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        Menu menu = navigationView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                item.setChecked(true);
                break;
            }
        }
    }


    private void updateNavigationBarState1() {
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem1(actionId);
    }

    void selectBottomNavigationBarItem1(int itemId) {
        Menu menu = navigationView1.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                item.setChecked(true);
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.icon:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    abstract int getContentViewId();

    abstract int getNavigationMenuItemId();


}
