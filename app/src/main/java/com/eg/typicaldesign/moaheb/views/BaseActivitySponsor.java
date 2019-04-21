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
 * Created by MAGIC on 5/16/2018.
 */
public abstract class BaseActivitySponsor extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    protected BottomNavigationView navigationView1;
    SharedPreferences dataSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        navigationView1 = (BottomNavigationView) findViewById(R.id.navigation1);
        navigationView1.setOnNavigationItemSelectedListener(this);
        updateNavigationBarState1();


    }

    @Override
    protected void onStart() {
        super.onStart();

        updateNavigationBarState1();

    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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
                Intent intentLogOut = new Intent(BaseActivitySponsor.this, LoginForSponsor.class);
                startActivity(intentLogOut);
                break;
            case R.id.navigation1_profile:
                Intent intent = new Intent(BaseActivitySponsor.this, RegisterForSponsor.class);
                startActivity(intent);
                break;

            default:
                return false;
        }
        finish();
        return true;
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