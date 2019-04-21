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
import com.eg.typicaldesign.moaheb.controllers.PlayerImagesAdapter;
import com.eg.typicaldesign.moaheb.controllers.Service;
import com.eg.typicaldesign.moaheb.models.Media;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class PlayerImagesActivity extends BaseActivityPlayer {
    PlayerImagesAdapter imagesAdapter;
    AdView adView;
    TextView playerName, playerDate, addNewImage;
    RecyclerView recyclerViewImages;
    StaggeredGridLayoutManager LayoutManager;
    SharedPreferences dataSaver;
    int type = 1;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_images);
        playerName = (TextView) findViewById(R.id.player_name);
        playerDate = (TextView) findViewById(R.id.player_date);
        addNewImage = (TextView) findViewById(R.id.add_new_image);
        recyclerViewImages = (RecyclerView) findViewById(R.id.image_recycle);
        LayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewImages.setLayoutManager(LayoutManager);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "bein-normal.ttf", true);

        MobileAds.initialize(this, " ca-app-pub-8408797241053494~4862516501");
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        getPlayerImages();
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_player_images;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_images;
    }

    public void getPlayerImages() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Log.e("TAG", "isSuccessful");

        Service.Fetcher.getInstance().getPlayerImages(type, token).enqueue(new Callback<Media>() {

            @Override
            public void onResponse(Call<Media> call, Response<Media> response) {

                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    final Media media = response.body();
                    if (media.getStatus() == 1) {
                        imagesAdapter = new PlayerImagesAdapter(PlayerImagesActivity.this, media);
                        recyclerViewImages.setAdapter(imagesAdapter);
                        String name = media.getUserName();
                        Log.e("TAG", "name " + name);
                        String[] split = name.split(" ");
                        if (split.length >= 2) {
                            playerName.setText(split[0] + " " + split[1]);
                        } else {
                            playerName.setText(split[0]);
                        }

                    }
                }

            }

            @Override
            public void onFailure(Call<Media> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG ", "onFailure");
                Toast.makeText(PlayerImagesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
  }

    public void addNewImages(View view) {
        Intent intent = new Intent(this,UploadImagesActivity.class);
        startActivity(intent);

    }
}
