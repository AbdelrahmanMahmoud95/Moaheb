package com.eg.typicaldesign.moaheb.views;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eg.typicaldesign.moaheb.ChooseDialog;
import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.controllers.AppKeys;
import com.eg.typicaldesign.moaheb.controllers.Service;
import com.eg.typicaldesign.moaheb.models.GeneralResponse;
import com.eg.typicaldesign.moaheb.models.Positions;
import com.eg.typicaldesign.moaheb.models.Sports;
import com.eg.typicaldesign.moaheb.models.UserInformation;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class PlayerSportsDataActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnDismissListener {
    Button edit;
    RadioButton singleRadio, teamRadio, joined, disJoined, champion, noChampion;
    TextView sportName, positionName;
    EditText clubName, tall, weight, overView;
    SharedPreferences dataSaver;
    String token;
    LinearLayout positionLinear1, positionLinear2, clubNameLinear, sportNameLinear;
    RadioGroup rgSportType, rgClubJoined, rgChampionType;
    int sportType, joinType, championType;
    int page = 2;
    int sportId , position_id , x;
    boolean isSportDialog, isPositionDialog;
    String sportname, positionname = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_sports_data);
        edit = (Button) findViewById(R.id.edit_btn);
        singleRadio = (RadioButton) findViewById(R.id.single);
        teamRadio = (RadioButton) findViewById(R.id.team);
        joined = (RadioButton) findViewById(R.id.joined);
        disJoined = (RadioButton) findViewById(R.id.dis_joined);
        disJoined = (RadioButton) findViewById(R.id.dis_joined);
        champion = (RadioButton) findViewById(R.id.exist_champion);
        noChampion = (RadioButton) findViewById(R.id.no_champion);
        sportName = (TextView) findViewById(R.id.sport_name);
        positionName = (TextView) findViewById(R.id.position);
        clubName = (EditText) findViewById(R.id.club_name);
        tall = (EditText) findViewById(R.id.tall);
        weight = (EditText) findViewById(R.id.weight);
        overView = (EditText) findViewById(R.id.overview);
        positionLinear1 = (LinearLayout) findViewById(R.id.position_linear);
        positionLinear2 = (LinearLayout) findViewById(R.id.position_linear2);
        clubNameLinear = (LinearLayout) findViewById(R.id.linear_club_name);
        sportNameLinear = (LinearLayout) findViewById(R.id.linear_sport_name);
        rgSportType = (RadioGroup) findViewById(R.id.group_Sport_type);
        rgClubJoined = (RadioGroup) findViewById(R.id.group_club_type);
        rgChampionType = (RadioGroup) findViewById(R.id.group_champion_type);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "bein-normal.ttf", true);
        sportNameLinear.setOnClickListener(this);
        positionLinear2.setOnClickListener(this);
        edit.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.alertDialogColor), PorterDuff.Mode.SRC_ATOP);

        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        rgSportType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);

                Intent intent = getIntent();
                x = intent.getIntExtra("userSportData", -1);

                switch (index) {
                    case 0:
                        if (x != 1) {
                            sportName.setText("");
                            positionName.setText("");
                        }
                        sportType = 1;
                        positionLinear1.setVisibility(View.VISIBLE);
                        positionLinear2.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        if (x != 1) {
                            sportName.setText("");
                            positionName.setText("");
                        }
                        sportType = 2;
                        positionLinear1.setVisibility(View.GONE);
                        positionLinear2.setVisibility(View.GONE);
                        break;
                }
            }
        });

        rgClubJoined.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);

                // Add logic here

                switch (index) {
                    case 0:
                        joinType = 0;
                        clubNameLinear.setVisibility(View.GONE);
                        break;
                    case 1:
                        joinType = 1;
                        clubNameLinear.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        rgChampionType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);

                // Add logic here

                switch (index) {
                    case 0:
                        championType = 0;
                        break;
                    case 1:
                        championType = 1;
                        break;
                }
            }
        });
        getUserSportData();
    }

    public void getUserSportData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Log.e("TAG", "isSuccessful");

        Service.Fetcher.getInstance().getUserData(token).enqueue(new Callback<UserInformation>() {

            @Override
            public void onResponse(Call<UserInformation> call, Response<UserInformation> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    final UserInformation userInformation = response.body();
                    if (userInformation.getStatus() == 1) {

                        String overview = userInformation.getUserInfo().getOverview();
                        String usertall = userInformation.getUserInfo().getLength();
                        String userWeight = userInformation.getUserInfo().getWeight();
                        String sport = userInformation.getUserInfo().getSport().getName();

                        sportId = userInformation.getUserInfo().getSport().getId();
                        position_id = userInformation.getUserInfo().getPosition().getId();

                        overView.setText(overview);
                        tall.setText(usertall);
                        weight.setText(userWeight);
                        sportName.setText(sport);
                        joined.setChecked(true);

                        if (userInformation.getUserInfo().getSportType().equals("1")) {
                            positionLinear1.setVisibility(View.VISIBLE);
                            positionLinear2.setVisibility(View.VISIBLE);
                            String position = userInformation.getUserInfo().getPosition().getName();
                            positionName.setText(position);
                            teamRadio.setChecked(true);
                        } else {
                            singleRadio.setChecked(true);
                        }
                        if (userInformation.getUserInfo().getClubJoined() == 0) {
                            clubNameLinear.setVisibility(View.GONE);
                            disJoined.setChecked(true);

                        } else {
                            String club = userInformation.getUserInfo().getClubName();
                            clubName.setText(club);
                            joined.setChecked(true);
                        }
                        if (userInformation.getUserInfo().getChampion().equals("1")) {
                            champion.setChecked(true);

                        } else {
                            noChampion.setChecked(true);
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<UserInformation> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG ", "onFailure");
                Toast.makeText(PlayerSportsDataActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onClick(View view) {
        if (view == sportNameLinear) {
            dataSaver.edit()
                    .putInt(AppKeys.SPORT_ID, -1)
                    .commit();
            sportname = "";
            positionName.setText("");
            isSportDialog = true;
            final ChooseDialog dialog = new ChooseDialog(this, "اختر الرياضة");
            dialog.setOnDismissListener(this);
            dialog.show();
            Service.Fetcher.getInstance().getSports(sportType).enqueue(new Callback<Sports>() {
                @Override
                public void onResponse(Call<Sports> call, Response<Sports> response) {
                    if (response.isSuccessful()) {

                        dialog.showSportsList(response.body().getSports());

                    } else {
                        dialog.dismiss();
                        Toast.makeText(PlayerSportsDataActivity.this, AppKeys.SOME_THING_WENT_WRONG_KEY, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Sports> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(PlayerSportsDataActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (view == positionLinear2) {
            dataSaver.edit()
                    .putInt(AppKeys.POSITION_ID, -1)
                    .commit();

            positionname = "";
            isPositionDialog = true;
            final ChooseDialog dialog = new ChooseDialog(this, "اختر المركز");
            dialog.setOnDismissListener(this);
            dialog.show();
            Service.Fetcher.getInstance().getPosition(sportId).enqueue(new Callback<Positions>() {
                @Override
                public void onResponse(Call<Positions> call, Response<Positions> response) {
                    if (response.isSuccessful()) {

                        dialog.showPositionList(response.body().getPositions());

                    } else {
                        dialog.dismiss();
                        Toast.makeText(PlayerSportsDataActivity.this, AppKeys.SOME_THING_WENT_WRONG_KEY, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Positions> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(PlayerSportsDataActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (view == edit) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("جاري التحميل...");
            progressDialog.show();
            String playerTall = tall.getText().toString();
            String playerWeight = weight.getText().toString();
            String cName = clubName.getText().toString();
            String overview = overView.getText().toString();
            Log.e("playerTall","playerTall "+playerTall);

            if(tall.getText().toString().equals("")){
                playerTall = null;
            }
            if(weight.getText().toString().equals("")){
                playerWeight = null;
            }
            Log.e("playerTall","playerTall "+playerTall);

            Service.Fetcher.getInstance().updateUserSportData(page, token, sportType, sportId, position_id, joinType, cName, overview, playerTall, playerWeight, championType).enqueue(new Callback<GeneralResponse>() {

                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    if (response.isSuccessful()) {

                        GeneralResponse generalResponse = response.body();
                        int status = generalResponse.getStatus();
                        Log.e("PlayerSport", "sportId " + sportId);
                        if (status == 1) {
                            Log.e("TAG", "isSuccessful ");
                            progressDialog.dismiss();
                            Toast.makeText(getApplication(), "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.e("TAG", "notSuccessful ");
                            progressDialog.dismiss();
                            String message = "";
                            for (int i = 0; i < response.body().getMessages().size(); i++) {
                                message = "";
                                message += response.body().getMessages().get(i);
                                Toast.makeText(PlayerSportsDataActivity.this, message, Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    Log.e("TAG", "onFailure ");
                    progressDialog.dismiss();
                    Toast.makeText(PlayerSportsDataActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();


                }
            });


        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if (isSportDialog) {
            sportname = dataSaver.getString(AppKeys.SPORT_NAME, "");
            sportId = dataSaver.getInt(AppKeys.SPORT_ID, -1);
            if (sportId != -1) {
                sportName.setText(sportname);
            }
        }
        if (isPositionDialog) {
            positionName.setText("");
            positionname = dataSaver.getString(AppKeys.POSITION_NAME, "");
            position_id = dataSaver.getInt(AppKeys.POSITION_ID, -1);
            if (position_id != -1) {
                positionName.setText(positionname);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

}
