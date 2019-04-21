package com.eg.typicaldesign.moaheb.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.controllers.AppKeys;
import com.eg.typicaldesign.moaheb.controllers.PlayerDocsAdapter;
import com.eg.typicaldesign.moaheb.controllers.Service;
import com.eg.typicaldesign.moaheb.models.UserDocs;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class PlayerDocumentsActivity extends BaseActivityPlayer {
    PlayerDocsAdapter docsAdapter;
    AdView adView;
    TextView playerName, playerDate, addNewDocs;
    RecyclerView recyclerViewImages;
    StaggeredGridLayoutManager LayoutManager;
    SharedPreferences dataSaver;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_documents);
        playerName = (TextView) findViewById(R.id.player_name);
        playerDate = (TextView) findViewById(R.id.player_date);
        addNewDocs = (TextView) findViewById(R.id.add_new_docs);
        recyclerViewImages = (RecyclerView) findViewById(R.id.docs_recycle);
        LayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewImages.setLayoutManager(LayoutManager);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "bein-normal.ttf", true);
        getPlayerDocs();

        MobileAds.initialize(this, " ca-app-pub-8408797241053494~4862516501");
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    @Override
    int getContentViewId() {
        return R.layout.activity_player_documents;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_more;
    }

    public void getPlayerDocs() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Log.e("TAG1", "isSuccessful");

        Service.Fetcher.getInstance().getUserDocs(token).enqueue(new Callback<UserDocs>() {

            @Override
            public void onResponse(Call<UserDocs> call, Response<UserDocs> response) {

                if (response.isSuccessful()) {
                    Log.e("TAG2", "isSuccessful");
                    progressDialog.dismiss();
                    UserDocs userDocs = response.body();
                    if (userDocs.getStatus() == 1) {
                        Log.e("TAG3", "isSuccessful");
                        docsAdapter = new PlayerDocsAdapter(PlayerDocumentsActivity.this, userDocs);
                        recyclerViewImages.setAdapter(docsAdapter);
//                        String name = userDocs.getIdentites()
//                        Log.e("TAG", "name " + name);
//                        String[] split = name.split(" ");
//                        if (split.length >= 2) {
//                            playerName.setText(split[0] + " " + split[1]);
//                        } else {
//                            playerName.setText(split[0]);
//                        }

                    }
                }
                Log.e("TAG", "notSuccessful");

            }

            @Override
            public void onFailure(Call<UserDocs> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG ", "onFailure");
                Toast.makeText(PlayerDocumentsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }


    public void addNewDocs(View view) {
        Intent intent = new Intent(this,UploadDocumentsActivity.class);
        startActivity(intent);
    }
}
