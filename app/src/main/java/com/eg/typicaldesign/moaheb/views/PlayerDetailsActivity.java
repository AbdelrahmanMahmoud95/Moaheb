package com.eg.typicaldesign.moaheb.views;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.controllers.AppKeys;
import com.eg.typicaldesign.moaheb.controllers.PlayersVideosAdapter;
import com.eg.typicaldesign.moaheb.controllers.Service;
import com.eg.typicaldesign.moaheb.controllers.PlayersImagesAdapter;
import com.eg.typicaldesign.moaheb.models.GeneralResponse;
import com.eg.typicaldesign.moaheb.models.playerProfile;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class PlayerDetailsActivity extends AppCompatActivity {
    PlayersImagesAdapter imagesAdapter;
    PlayersVideosAdapter videosAdapter;
    RecyclerView recyclerViewImages;
    AdView adView;
    ImageView playerImage;
    SharedPreferences dataSaver;
    TextView aboutPlayer, playerName, playerDate, imageNumber, videosNumber, length, club, positions, sport, positionText,
            playerImages, playerVideos, userVisits, sponsorVisits, contactWithPlayer, weight;
    Button saveRating;
    SimpleRatingBar playerRate, setRating;
    LinearLayoutManager categoriesLayoutManager;
    int sponsorId = -1;
    String token = "-1";
    boolean isSponsor = false;
    boolean isLogin = false;

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);
        Intent mIntent = getIntent();
        userId = mIntent.getIntExtra("user_id", -1);
        Log.e("PlayerDetails", "userId " + userId);
        playerName = findViewById(R.id.player_name);
        playerDate = findViewById(R.id.player_date);
        imageNumber = findViewById(R.id.images);
        videosNumber = findViewById(R.id.videos);
        length = findViewById(R.id.length);
        positions = findViewById(R.id.position);
        club = findViewById(R.id.club);
        weight = findViewById(R.id.weight);
        sport = findViewById(R.id.sport);
        positionText = findViewById(R.id.textPosition);
        aboutPlayer = findViewById(R.id.about_player);
        playerImages = findViewById(R.id.player_images);
        playerVideos = findViewById(R.id.player_videos);
        playerImage = findViewById(R.id.player_image);
        playerRate = findViewById(R.id.player_rating);
        setRating = findViewById(R.id.post_rating);
        saveRating = findViewById(R.id.save_rate);
        sponsorVisits = findViewById(R.id.sponsor_visits);
        userVisits = findViewById(R.id.user_visits);
        contactWithPlayer = findViewById(R.id.contact_with_player);

        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        sponsorId = dataSaver.getInt(AppKeys.ID_KEY, -1);
        token = dataSaver.getString(AppKeys.TOKEN_KEY, "-1");
        isSponsor = dataSaver.getBoolean(AppKeys.IS_SPONSOR_ACCOUNT, false);
        isLogin = dataSaver.getBoolean(AppKeys.IS_LOGIN_KEY, false);

        recyclerViewImages = findViewById(R.id.recycle_player_images);
        categoriesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        recyclerViewImages.setLayoutManager(categoriesLayoutManager);
        dataSaver.edit()
                .putInt("userId", userId)
                .apply();

        Calligrapher calligrapher = new Calligrapher(PlayerDetailsActivity.this);
        calligrapher.setFont(PlayerDetailsActivity.this, "bein-normal.ttf", true);

        MobileAds.initialize(this, " ca-app-pub-8408797241053494~4862516501");
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        getPlayerData();
        if (isSponsor == false && !token.equals("-1")) {
            sponsorId = -1;
            Log.e("PlayerDetails", "Player");
            setPlayerRate();
        } else if (isSponsor == true && !token.equals("-1")) {
            setPlayerRate();
            Log.e("PlayerDetails", "Sponsor");

        } else if (isLogin == false) {
            Log.e("PlayerDetails", "visitor");
            token = "-1";
            Log.e("PlayerDetails", "token " + token);
            sponsorId = -1;
            setPlayerRate();
        }

    }

    private void setPlayerRate() {
        saveRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final float stars = setRating.getRating();

                Service.Fetcher.getInstance().setPlayerRate(userId, sponsorId, token, stars).enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                        if (response.body().getStatus() == 1) {
                            Toast.makeText(PlayerDetailsActivity.this, "شكرا لتقيمك", Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(PlayerDetailsActivity.this, "error", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        //  Toast.makeText(OfferDetailsActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }


    public void getPlayerData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Log.e("TAG", "token " + token);
        Log.e("TAG", "sponsorId " + sponsorId);
        if (sponsorId == 0 || sponsorId == -1) {
            token = "-1";
        }
        Log.e("TAG", "token " + token);
        Service.Fetcher.getInstance().getPlayerDetails(userId, token).enqueue(new Callback<playerProfile>() {
            @Override
            public void onResponse(Call<playerProfile> call, Response<playerProfile> response) {

                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    final playerProfile profile = response.body();
                    if (profile.getStatus() == 1) {

                        playerImages.setBackgroundResource(R.drawable.player_images_pressed);
                        imagesAdapter = new PlayersImagesAdapter(PlayerDetailsActivity.this, profile);
                        recyclerViewImages.setAdapter(imagesAdapter);
                        playerVideos.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                playerImages.setBackgroundResource(R.drawable.player_images);
                                playerVideos.setBackgroundResource(R.drawable.player_images_pressed);

                                videosAdapter = new PlayersVideosAdapter(PlayerDetailsActivity.this, profile);
                                recyclerViewImages.setAdapter(videosAdapter);
                            }
                        });
                        playerImages.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                playerVideos.setBackgroundResource(R.drawable.player_images);

                                playerImages.setBackgroundResource(R.drawable.player_images_pressed);
                                imagesAdapter = new PlayersImagesAdapter(PlayerDetailsActivity.this, profile);
                                recyclerViewImages.setAdapter(imagesAdapter);
                            }
                        });


                        String name = profile.getUser().getName();
                        String[] split = name.split(" ");
                        if (split.length >= 2) {
                            playerName.setText(split[0] + " " + split[1]);
                        } else {
                            playerName.setText(split[0]);
                        }
                        String Date = String.valueOf(profile.getUser().getYears().getY());
                        playerDate.setText(Date + " " + "سنة");
                        aboutPlayer.setText(profile.getUser().getOverview());
                        imageNumber.setText(profile.getUser().getApprovedImagesCount());
                        videosNumber.setText(profile.getUser().getApprovedVideosCount());
                        if (profile.getUser().getLength() == null) {
                            length.setText("0");
                        } else {
                            length.setText(profile.getUser().getLength());

                        }
                        if (profile.getUser().getWeight() == null) {
                            weight.setText("0");
                        } else {
                            weight.setText(profile.getUser().getWeight());

                        }
                        if (profile.getUser().getClubName() == null) {
                            club.setText("غير مشترك");
                        } else {
                            club.setText(profile.getUser().getClubName().toString());

                        }
                        sport.setText(profile.getUser().getSport().getName());
                        Picasso.with(PlayerDetailsActivity.this).load(String.valueOf(profile.getUser().getImage())).into(playerImage);
                        playerRate.setRating((float) profile.getUser().getRank());

                        if (profile.getUser().getSportType().equals("1")) {
                            positions.setText(profile.getUser().getPosition().getName());
                        } else {
                            positions.setVisibility(View.GONE);
                            positionText.setVisibility(View.GONE);
                        }
                        userVisits.setText(profile.getUser().getUsersVisits());
                        sponsorVisits.setText(profile.getUser().getSponsorsVisits());


                    }

                }

            }

            @Override
            public void onFailure(Call<playerProfile> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG ", "onFailure");
                Toast.makeText(PlayerDetailsActivity.this, "error", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void contactPlayer(View view) {
        if (isSponsor == false || token.equals("-1")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PlayerDetailsActivity.this/*,R.style.MyDialogTheme*/);
            TextView myMsg = new TextView(this);
            myMsg.setText("اذا اردت معرفة بيانات الاتصال باللاعب يجب ان تكون مسجل كأحد وكلاء اللاعبين");
            myMsg.setTextSize(15);
            myMsg.setGravity(Gravity.CENTER);
            myMsg.setPadding(30, 50, 30, 25);
            myMsg.setTextColor(Color.parseColor("#530707"));
            Typeface typeface = Typeface.createFromAsset(getAssets(), "bein-normal.ttf");
            myMsg.setTypeface(typeface);
            builder.setView(myMsg).setCancelable(false)
                    .setPositiveButton("تسجيل وكيل اللاعبين", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(PlayerDetailsActivity.this, LoginForSponsor.class));

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

            final Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(R.drawable.alert_dialog);
            window.setGravity(Gravity.CENTER);
            dialog.show();
        } else {
            Intent intent = new Intent(PlayerDetailsActivity.this, ContactPlayerActivity.class);
            startActivity(intent);
        }
    }
}

