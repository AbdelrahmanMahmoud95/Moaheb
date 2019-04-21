package com.eg.typicaldesign.moaheb.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.controllers.AppKeys;
import com.eg.typicaldesign.moaheb.controllers.Service;
import com.eg.typicaldesign.moaheb.models.GeneralResponse;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class LoginForPlayer extends AppCompatActivity implements View.OnClickListener {
    LinearLayout linearNewUser;
    AdView adView;
    EditText phoneNumber, password;
    Button submit;
    SharedPreferences dataSaver;
    int playerId;
    Boolean isLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_for_player);
        linearNewUser = (LinearLayout) findViewById(R.id.new_user);
        phoneNumber = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.user_password);
        submit = (Button) findViewById(R.id.submit);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        playerId = dataSaver.getInt(AppKeys.ID_KEY, -1);

        MobileAds.initialize(this, " ca-app-pub-8408797241053494~4862516501");
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        if (playerId != -1) {

            Intent intent = new Intent(this, SportsContainerActivity.class);
            startActivity(intent);

        }


        linearNewUser.setOnClickListener(this);
        submit.setOnClickListener(this);
        LinearLayout contentPain = findViewById(R.id.contentpain);
        contentPain.setAlpha(0);
        contentPain.animate().alpha(1.0f).setDuration(1600).start();
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "bein-normal.ttf", true);

    }

    @Override
    public void onClick(View view) {
        if (view == linearNewUser) {
            Intent intent = new Intent(this, SportTypeActivity.class);
            startActivity(intent);
        }
        if (view == submit) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("جاري التحميل...");
            progressDialog.show();
            String phone = phoneNumber.getText().toString();
            String pass = password.getText().toString();
            Service.Fetcher.getInstance().loginUser(phone, pass).enqueue(new Callback<GeneralResponse>() {

                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    if (response.isSuccessful()) {

                        GeneralResponse generalResponse = response.body();
                        int status = generalResponse.getStatus();

                        if (status == 1) {

                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(password.getWindowToken(), 0);

                            Log.e("TAG", "isSuccessful ");
                            progressDialog.dismiss();
                            Toast.makeText(getApplication(), "تم تسجيل الدخول", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginForPlayer.this, SportsContainerActivity.class);
                            startActivity(intent);
                            finish();
                            dataSaver.edit()
                                    .putBoolean(AppKeys.IS_LOGIN_KEY, true)
                                    .commit();

                            dataSaver.edit()
                                    .putInt(AppKeys.ID_KEY, generalResponse.getId())
                                    .apply();
                            dataSaver.edit()
                                    .putString(AppKeys.TOKEN_KEY, generalResponse.getToken())
                                    .commit();
                            dataSaver.edit()
                                    .putBoolean(AppKeys.IS_SPONSOR_ACCOUNT, false)
                                    .apply();
                        } else {
                            Log.e("TAG", "notSuccessful ");
                            progressDialog.dismiss();
                            String message = "";
                            for (int i = 0; i < response.body().getMessages().size(); i++) {
                                message += response.body().getMessages().get(i);
                                Toast.makeText(LoginForPlayer.this, message, Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    Log.e("TAG", "onFailure ");
                    progressDialog.dismiss();
                    Toast.makeText(LoginForPlayer.this, t.getMessage(), Toast.LENGTH_LONG).show();


                }
            });

        }

    }
}
