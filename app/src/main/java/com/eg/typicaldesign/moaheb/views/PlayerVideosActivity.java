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
import com.eg.typicaldesign.moaheb.controllers.PlayerVideosAdapter;
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

public class PlayerVideosActivity extends BaseActivityPlayer {
    SharedPreferences dataSaver;
    AdView adView;
    String token;
    int type = 2;
    PlayerVideosAdapter videosAdapter;
    RecyclerView recyclerViewImages;
    TextView playerName;
    StaggeredGridLayoutManager LayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_videos);
        playerName = (TextView) findViewById(R.id.player_name);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        recyclerViewImages = (RecyclerView) findViewById(R.id.image_recycle);
        LayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewImages.setLayoutManager(LayoutManager);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "bein-normal.ttf", true);

        MobileAds.initialize(this, " ca-app-pub-8408797241053494~4862516501");
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        getPlayerVideos();
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_player_videos;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_video;
    }

    public void getPlayerVideos() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Log.e("TAG", "isSuccessful");

        Service.Fetcher.getInstance().getPlayerVideos(type, token).enqueue(new Callback<Media>() {

            @Override
            public void onResponse(Call<Media> call, Response<Media> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    final Media media = response.body();
                    if (media.getStatus() == 1) {
                        videosAdapter = new PlayerVideosAdapter(PlayerVideosActivity.this, media);
                        recyclerViewImages.setAdapter(videosAdapter);

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
                Toast.makeText(PlayerVideosActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void addNewVideos(View view) {
        Intent intent = new Intent(this, UploadVideoActivity.class);
        startActivity(intent);

    }
}
