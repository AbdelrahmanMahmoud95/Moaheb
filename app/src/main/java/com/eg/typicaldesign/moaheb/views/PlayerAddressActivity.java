package com.eg.typicaldesign.moaheb.views;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eg.typicaldesign.moaheb.ChooseDialog;
import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.controllers.AppKeys;
import com.eg.typicaldesign.moaheb.controllers.Service;
import com.eg.typicaldesign.moaheb.models.Cities;
import com.eg.typicaldesign.moaheb.models.GeneralResponse;
import com.eg.typicaldesign.moaheb.models.Governorates;
import com.eg.typicaldesign.moaheb.models.UserInformation;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class PlayerAddressActivity extends BaseActivityPlayer implements View.OnClickListener, DialogInterface.OnDismissListener {
    EditText address;
    Button update;
    TextView selectCity, selectGovernorate;
    LinearLayout linearGovernorate, linearCity;
    SharedPreferences dataSaver;
    String token, governorateName, city_name;
    int gov_id, city_id;
    int page = 3;
    boolean isGovernorateDialog, isCityDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_address);
        address = (EditText) findViewById(R.id.location);
        selectCity = (TextView) findViewById(R.id.select_city);
        selectGovernorate = (TextView) findViewById(R.id.select_Governorate);
        linearGovernorate = (LinearLayout) findViewById(R.id.linear_Governorate);
        linearCity = (LinearLayout) findViewById(R.id.linear_country);
        update = (Button) findViewById(R.id.update_btn);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "bein-normal.ttf", true);
        linearCity.setOnClickListener(this);
        linearGovernorate.setOnClickListener(this);
        update.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.alertDialogColor), PorterDuff.Mode.SRC_ATOP);

        getUserAddressData();
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_player_address;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_more;
    }


    public void getUserAddressData() {
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

                        String location = userInformation.getUserInfo().getAddress();
                        String cityName = userInformation.getUserInfo().getCity().getName();
                        String gov = userInformation.getUserInfo().getGov().getName();

                        address.setText(location);
                        selectCity.setText(cityName);
                        selectGovernorate.setText(gov);


                    }
                }

            }

            @Override
            public void onFailure(Call<UserInformation> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG ", "onFailure");
                Toast.makeText(PlayerAddressActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onClick(View view) {
        if (view == linearGovernorate) {


            dataSaver.edit()
                    .putInt(AppKeys.GOVERNORATE_ID, -1)
                    .commit();
            selectCity.setText("");

            governorateName = "";
            gov_id = 0;
            isGovernorateDialog = true;
            final ChooseDialog dialog = new ChooseDialog(this, "اختر المحافظة");
            dialog.setOnDismissListener(this);
            dialog.show();
            Service.Fetcher.getInstance().getGovernorate().enqueue(new Callback<Governorates>() {
                @Override
                public void onResponse(Call<Governorates> call, Response<Governorates> response) {
                    if (response.isSuccessful()) {

                        dialog.showGovernoratesList(response.body().getGovernorates());

                    } else {
                        dialog.dismiss();
                        Toast.makeText(PlayerAddressActivity.this, AppKeys.SOME_THING_WENT_WRONG_KEY, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Governorates> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(PlayerAddressActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (view == linearCity) {
            dataSaver.edit()
                    .putInt(AppKeys.CITY_ID, -1)
                    .commit();

            city_id = 0;
            city_name = "";
            isCityDialog = true;
            final ChooseDialog dialog = new ChooseDialog(this, "اختر المدينة");
            dialog.setOnDismissListener(this);
            dialog.show();
            Service.Fetcher.getInstance().getCities(gov_id).enqueue(new Callback<Cities>() {
                @Override
                public void onResponse(Call<Cities> call, Response<Cities> response) {
                    if (response.isSuccessful()) {
                        Cities cities = response.body();
                        dialog.showCitiesList(cities.getCities());


                    } else {
                        dialog.dismiss();
                        Toast.makeText(PlayerAddressActivity.this, AppKeys.SOME_THING_WENT_WRONG_KEY, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Cities> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(PlayerAddressActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (view == update) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("جاري التحميل...");
            progressDialog.show();
            String add = address.getText().toString();
            Service.Fetcher.getInstance().updateUserAddressData(page, token, gov_id, city_id, add).enqueue(new Callback<GeneralResponse>() {

                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    if (response.isSuccessful()) {

                        GeneralResponse generalResponse = response.body();
                        int status = generalResponse.getStatus();

                        if (status == 1) {

                            Log.e("TAG1", "isSuccessful ");
                            Log.e("TAG", "isSuccessful ");
                            progressDialog.dismiss();
                            Toast.makeText(getApplication(), "تم تعديل البيانات", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.e("TAG", "notSuccessful ");
                            progressDialog.dismiss();
                            String message = "";
                            for (int i = 0; i < response.body().getMessages().size(); i++) {
                                message += response.body().getMessages().get(i);
                                Toast.makeText(PlayerAddressActivity.this, message, Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                    Log.e("TAG", "isNotSuccessful ");
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    Log.e("TAG", "onFailure ");
                    progressDialog.dismiss();
                    Toast.makeText(PlayerAddressActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();


                }
            });
        }

    }


    @Override
    public void onDismiss(DialogInterface dialogInterface) {

        if (isCityDialog) {
            city_name = dataSaver.getString(AppKeys.CITY_NAME, "");
            city_id = dataSaver.getInt(AppKeys.CITY_ID, -1);
            if (city_id != -1) {
                selectCity.setText(city_name);
            }

        }


        if (isGovernorateDialog) {

            governorateName = dataSaver.getString(AppKeys.GOVERNORATE_NAME, "");
            gov_id = dataSaver.getInt(AppKeys.GOVERNORATE_ID, 0);
            if (gov_id != -1) {
                selectGovernorate.setText(governorateName);
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



