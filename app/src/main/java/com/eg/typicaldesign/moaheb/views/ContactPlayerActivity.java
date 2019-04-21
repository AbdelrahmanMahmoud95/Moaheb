package com.eg.typicaldesign.moaheb.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.controllers.AppKeys;
import com.eg.typicaldesign.moaheb.controllers.PlayersImagesAdapter;
import com.eg.typicaldesign.moaheb.controllers.PlayersVideosAdapter;
import com.eg.typicaldesign.moaheb.controllers.Service;
import com.eg.typicaldesign.moaheb.models.playerProfile;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ContactPlayerActivity extends AppCompatActivity {
    int userId;
    TextView playerPhone, city, gov, address;
    SharedPreferences dataSaver;
    AdView adView;
    String token = "-1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_player);


        playerPhone = findViewById(R.id.phone);
        city = findViewById(R.id.city);
        gov = findViewById(R.id.gov);
        address = findViewById(R.id.address);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        token = dataSaver.getString(AppKeys.TOKEN_KEY, "-1");
        userId = dataSaver.getInt("userId", -1);
        Calligrapher calligrapher = new Calligrapher(ContactPlayerActivity.this);
        calligrapher.setFont(ContactPlayerActivity.this, "bein-normal.ttf", true);

        MobileAds.initialize(this, " ca-app-pub-8408797241053494~4862516501");
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        Log.e("TAG", "userId " + userId);

        getPlayerData();
    }

    public void getPlayerData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Service.Fetcher.getInstance().getPlayerDetails(userId,token).enqueue(new Callback<playerProfile>() {
            @Override
            public void onResponse(Call<playerProfile> call, Response<playerProfile> response) {

                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    final playerProfile profile = response.body();
                    if (profile.getStatus() == 1) {
                        address.setText(profile.getUser().getAddress());
                        city.setText(profile.getUser().getCity().getName());
                        if (profile.getUser().getGov().getName() == null) {
                            gov.setText("لايوجد");
                        } else {
                            gov.setText(profile.getUser().getGov().getName());

                        }
                        playerPhone.setText(profile.getUser().getPhone());

                    }
                }
            }

            @Override
            public void onFailure(Call<playerProfile> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG ", "onFailure");
                Toast.makeText(ContactPlayerActivity.this, "error", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
