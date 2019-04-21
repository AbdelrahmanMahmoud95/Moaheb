package com.eg.typicaldesign.moaheb.views;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.controllers.AppKeys;
import com.eg.typicaldesign.moaheb.controllers.Service;
import com.eg.typicaldesign.moaheb.models.GeneralResponse;
import com.eg.typicaldesign.moaheb.models.UserInformation;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class PlayerGuardianActivity extends BaseActivityPlayer {
    Button submit;
    EditText parentName, parentNumber;
    String token;
    SharedPreferences dataSaver;
    int page = 4;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_guardian);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "bein-normal.ttf", true);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.alertDialogColor), PorterDuff.Mode.SRC_ATOP);

        parentName = (EditText) findViewById(R.id.parent_name);
        parentNumber = (EditText) findViewById(R.id.parent_number);
        submit = (Button) findViewById(R.id.submit);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        getUserParentData();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(PlayerGuardianActivity.this);
                progressDialog.setMessage("جاري التحميل...");
                progressDialog.show();
                String phone = parentNumber.getText().toString();
                String name = parentName.getText().toString();
                Service.Fetcher.getInstance().updateParentData(page, token, name, phone).enqueue(new Callback<GeneralResponse>() {

                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        if (response.isSuccessful()) {

                            GeneralResponse generalResponse = response.body();
                            int status = generalResponse.getStatus();

                            if (status == 1) {

                                Log.e("TAG", "isSuccessful ");
                                progressDialog.dismiss();
                                Toast.makeText(getApplication(), "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();

                            } else {
                                Log.e("TAG", "notSuccessful ");
                                progressDialog.dismiss();
                                String message = "";
                                for (int i = 0; i < response.body().getMessages().size(); i++) {
                                    message += response.body().getMessages().get(i);
                                    Toast.makeText(PlayerGuardianActivity.this, message, Toast.LENGTH_LONG).show();

                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        Log.e("TAG", "onFailure ");
                        progressDialog.dismiss();
                        Toast.makeText(PlayerGuardianActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();


                    }
                });

            }
        });
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_player_guardian;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_more;
    }

    public void getUserParentData() {
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

                        String name = userInformation.getUserInfo().getGuardian();
                        String phone = userInformation.getUserInfo().getGuardianPhone();

                        parentName.setText(name);
                        parentNumber.setText(phone);


                    }
                }

            }

            @Override
            public void onFailure(Call<UserInformation> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG ", "onFailure");
                Toast.makeText(PlayerGuardianActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
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

