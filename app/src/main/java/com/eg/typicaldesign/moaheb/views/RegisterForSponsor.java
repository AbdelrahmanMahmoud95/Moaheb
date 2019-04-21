package com.eg.typicaldesign.moaheb.views;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eg.typicaldesign.moaheb.ChooseDialog;
import com.eg.typicaldesign.moaheb.DateDialog;
import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.controllers.AppKeys;
import com.eg.typicaldesign.moaheb.controllers.Service;
import com.eg.typicaldesign.moaheb.models.Cities;
import com.eg.typicaldesign.moaheb.models.GeneralResponse;
import com.eg.typicaldesign.moaheb.models.Governorates;
import com.eg.typicaldesign.moaheb.models.SponsorProfile;
import com.eg.typicaldesign.moaheb.models.Terms;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.IOException;

import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class RegisterForSponsor extends BaseActivitySponsor implements View.OnClickListener, DialogInterface.OnDismissListener {
    LinearLayout dateLinear,
            linearImage, linearGovernorate, linearCity;
    AdView adView;
    Button submit;
    EditText userName, phone, email, address, password;
    TextView dateText, selectCity, selectGovernorate, readTerms, selectImage;
    String governorateName = "";
    private Bitmap bitmap;
    Bitmap chosenPhoto = null;
    private ImageView Image;
    Uri fileUri;
    String selectedImagePath = "";
    final int PICK_IMAGE = 123;
    private static final String READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String CAMERA = Manifest.permission.CAMERA;
    private static final int External_Permission_Request_code = 0505;
    private boolean mExternalPermissionGranted = false;
    SharedPreferences dataSaver;
    int city_id, gov_id;
    String city_name, token;
    boolean isTermsRead;
    boolean isCityDialog, isGovernorateDialog = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_sponsor);
        dateLinear = (LinearLayout) findViewById(R.id.linear_date);
        dateText = (TextView) findViewById(R.id.date);
        linearGovernorate = (LinearLayout) findViewById(R.id.linear_Governorate);

        linearCity = (LinearLayout) findViewById(R.id.linear_country);
        linearImage = (LinearLayout) findViewById(R.id.linear_image);
        Image = (ImageView) findViewById(R.id.register_sponsor_photo);
        phone = (EditText) findViewById(R.id.phone_number);
        email = (EditText) findViewById(R.id.email);
        userName = (EditText) findViewById(R.id.username);
        address = (EditText) findViewById(R.id.location);
        password = (EditText) findViewById(R.id.user_password);
        selectImage = (TextView) findViewById(R.id.image);
        readTerms = (TextView) findViewById(R.id.read_terms);
        navigationView1 = (BottomNavigationView) findViewById(R.id.navigation1);
        navigationView1.setOnNavigationItemSelectedListener(this);
        selectCity = (TextView) findViewById(R.id.select_city);
        selectGovernorate = (TextView) findViewById(R.id.select_Governorate);
        submit = (Button) findViewById(R.id.register_btn);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        token = dataSaver.getString(AppKeys.TOKEN_KEY, "");

//        Intent mIntent = getIntent();
//        i = mIntent.getIntExtra("dismiss", -1);

//        MobileAds.initialize(this, " ca-app-pub-8408797241053494~4862516501");
//        adView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);

        LinearLayout contentPain = findViewById(R.id.contentpain);
        contentPain.setAlpha(0);
        contentPain.animate().alpha(1.0f).setDuration(1600).start();
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "bein-normal.ttf", true);

        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        submit.setOnClickListener(this);
        selectImage.setOnClickListener(this);
        linearGovernorate.setOnClickListener(this);
        linearImage.setOnClickListener(this);
        linearCity.setOnClickListener(this);
        if (!token.equals("")) {
            getSponsorData();
//            adView.setVisibility(View.GONE);
            isTermsRead = true;
            readTerms.setVisibility(View.GONE);
            submit.setText("تعديل بياناتك");
            navigationView1.setVisibility(View.VISIBLE);

        }

    }


    public void getSponsorData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Log.e("TAG", "isSuccessful");

        Service.Fetcher.getInstance().getSponsorData(token).enqueue(new Callback<SponsorProfile>() {

            @Override
            public void onResponse(Call<SponsorProfile> call, Response<SponsorProfile> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    final SponsorProfile Information = response.body();
                    if (Information.getStatus() == 1) {

                        String name = Information.getSponsorInfo().getName();
                        String userPhone = Information.getSponsorInfo().getPhone();
                        String location = Information.getSponsorInfo().getAddress();
                        String userEmail = Information.getSponsorInfo().getEmail();
                        String city = Information.getSponsorInfo().getCity().getName();
                        String gov = Information.getSponsorInfo().getGov().getName();
                        userName.setText(name);
                        phone.setText(userPhone);
                        address.setText(location);
                        email.setText(userEmail);
                        selectCity.setText(city);
                        selectGovernorate.setText(gov);
                        Picasso.with(RegisterForSponsor.this).load(String.valueOf(Information.getSponsorInfo().getImage())).into(Image);


                    }
                }

            }

            @Override
            public void onFailure(Call<SponsorProfile> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG ", "onFailure");
                Toast.makeText(RegisterForSponsor.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
    @Override
    int getContentViewId() {
        return R.layout.activity_register_for_sponsor;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation1_profile;
    }

    private void getExternalPermission() {
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                READ_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mExternalPermissionGranted = true;

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                mExternalPermissionGranted = true;

                if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                        CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    mExternalPermissionGranted = true;

                } else {
                    ActivityCompat.requestPermissions(this, permission, External_Permission_Request_code);
                }
            } else {
                ActivityCompat.requestPermissions(this, permission, External_Permission_Request_code);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, External_Permission_Request_code);
        }

    }


    public static String getRealPath(Uri contentURI, Activity context) {
        // Intent data ;
        //    Log.e(TAG,"data "+data);


        String wholeID = DocumentsContract.getDocumentId(contentURI);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        @SuppressWarnings("deprecation")
        // MediaStore.Images.Media.EXTERNAL_CONTENT_URI

                Cursor cursor = context.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);


        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            String filePath = cursor.getString(columnIndex);
            return filePath;
        }
        cursor.close();


        return null;
    }
    public static String getRealPathFromURI(Uri contentURI, Activity context) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = context.getContentResolver().query(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }
        // cursor.close();
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            fileUri = data.getData();

            selectedImagePath = getRealPath(fileUri, this);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                Image.setImageBitmap(bitmap);

                int maxImageSize = 700;
                if (bitmap.getWidth() >= maxImageSize) {
                    maxImageSize = 700;
                } else if (bitmap.getWidth() < maxImageSize) {
                    maxImageSize = bitmap.getWidth();
                }
                if (bitmap.getHeight() >= maxImageSize) {
                    maxImageSize = 700;
                } else if (bitmap.getHeight() < maxImageSize) {
                    maxImageSize = bitmap.getHeight();
                }
                float ratio = Math.min(
                        (float) maxImageSize / bitmap.getWidth(),
                        (float) maxImageSize / bitmap.getHeight());
                int width = Math.round((float) ratio * bitmap.getWidth());
                int height = Math.round((float) ratio * bitmap.getHeight());
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, width,
                        height, true);

                String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), scaled, "Title", null);


                fileUri = Uri.parse(path);
                selectedImagePath = getRealPathFromURI(fileUri, RegisterForSponsor.this);


            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "تم اختيار الصورة", Toast.LENGTH_SHORT).show();
            selectImage.setText("تم اختيار الصورة");

        }
    }

    @Override
    public void onClick(View view) {
        if (view == dateText) {
            DateDialog dateDialog = new DateDialog(view);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            dateDialog.show(fragmentTransaction, "Date Picker");
        }
        if (view == selectImage || view == linearImage) {
            getExternalPermission();
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
            } else {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        }

        if (view == linearGovernorate) {


            dataSaver.edit()
                    .putInt(AppKeys.GOVERNORATE_ID, -1)
                    .commit();

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
                        Toast.makeText(RegisterForSponsor.this, AppKeys.SOME_THING_WENT_WRONG_KEY, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Governorates> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(RegisterForSponsor.this, "error", Toast.LENGTH_SHORT).show();
                }
            });

        }
        if (view == linearCity) {
            dataSaver.edit()
                    .putInt(AppKeys.CITY_ID, -1)
                    .commit();

            if (gov_id == 0) {
                Toast.makeText(this, "برجاء اختيار المحافظة اولا", Toast.LENGTH_SHORT).show();

            } else {
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
                            Toast.makeText(RegisterForSponsor.this, AppKeys.SOME_THING_WENT_WRONG_KEY, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Cities> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(RegisterForSponsor.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        if (view == submit) {
            if (token.equals("")) {
            Log.e("TAG", "isTermsRead " + isTermsRead);

            if (isTermsRead == false) {

                Toast.makeText(RegisterForSponsor.this, "يجب قراءة الشروط والاحكام", Toast.LENGTH_LONG).show();

            } else {

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("جاري التحميل...");
                progressDialog.show();
                File file = new File(selectedImagePath);
                Log.e("image path", "selectedImagePath " + selectedImagePath);
                RequestBody imageFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), imageFile);
                RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), userName.getText().toString());
                RequestBody phoneNumber = RequestBody.create(MediaType.parse("multipart/form-data"), phone.getText().toString());
                RequestBody usermail = RequestBody.create(MediaType.parse("multipart/form-data"), email.getText().toString());
                RequestBody governorate = RequestBody.create(MediaType.parse("multipart/form-data"), gov_id + "");
                RequestBody location = RequestBody.create(MediaType.parse("multipart/form-data"), address.getText().toString());
                RequestBody city = RequestBody.create(MediaType.parse("multipart/form-data"), city_id + "");
                RequestBody pass = RequestBody.create(MediaType.parse("multipart/form-data"), password.getText().toString());


                Service.Fetcher.getInstance().registerSponsor(imagePart, name, phoneNumber, usermail,
                        pass, location, governorate, city).enqueue(new Callback<GeneralResponse>() {


                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {


                        if (response.isSuccessful()) {

                            GeneralResponse generalResponse = response.body();
                            int status = generalResponse.getStatus();
                            if (status == 1) {
                                Log.e("TAG", "isSuccessful ");

                                progressDialog.dismiss();
                                Intent intent = new Intent(RegisterForSponsor.this, SportsContainerActivity.class);
                                startActivity(intent);
                                dataSaver.edit()
                                        .putBoolean(AppKeys.IS_SPONSOR_ACCOUNT, true)
                                        .apply();
                                dataSaver.edit()
                                        .putInt(AppKeys.ID_KEY, generalResponse.getId())
                                        .apply();

                                dataSaver.edit()
                                        .putString(AppKeys.TOKEN_KEY, generalResponse.getToken())
                                        .apply();
                                finish();
                                Toast.makeText(RegisterForSponsor.this, "تم إنشاء الحساب بنجاح", Toast.LENGTH_LONG).show();

                            } else {
                                Log.e("TAG", "notSuccessful ");
                                progressDialog.dismiss();
                                String message = "";
                                for (int i = 0; i < response.body().getMessages().size(); i++) {
                                    message += response.body().getMessages().get(i);
                                    Toast.makeText(RegisterForSponsor.this, message, Toast.LENGTH_LONG).show();

                                }
                            }
                        }
                    }


                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {

                        progressDialog.dismiss();
                        Log.e("TAG", "onFailure " + t.getMessage());

                      //  Toast.makeText(RegisterForSponsor.this, t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });


            }
        }
            else {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("جاري التحميل...");
                progressDialog.show();
                Log.e("TAG", "selectedImagePath " + selectedImagePath);
                File file = new File(selectedImagePath);
                Log.e("image path", "selectedImagePath " + selectedImagePath);
                RequestBody imageFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), imageFile);
                RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), userName.getText().toString());
                RequestBody phoneNumber = RequestBody.create(MediaType.parse("multipart/form-data"), phone.getText().toString());
                RequestBody usermail = RequestBody.create(MediaType.parse("multipart/form-data"), email.getText().toString());
                RequestBody location = RequestBody.create(MediaType.parse("multipart/form-data"), address.getText().toString());
                RequestBody pass = RequestBody.create(MediaType.parse("multipart/form-data"), password.getText().toString());
                RequestBody api_token = RequestBody.create(MediaType.parse("multipart/form-data"), token);
                RequestBody governorate = RequestBody.create(MediaType.parse("multipart/form-data"), gov_id + "");
                RequestBody city = RequestBody.create(MediaType.parse("multipart/form-data"), city_id + "");

                if (selectedImagePath != null || !selectedImagePath.equals("")) {

                    Service.Fetcher.getInstance().updateSponsorProfile(imagePart, api_token, name, phoneNumber, usermail,
                            pass,  location,governorate,city).enqueue(new Callback<GeneralResponse>() {


                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                            if (response.isSuccessful()) {

                                GeneralResponse generalResponse = response.body();
                                int status = generalResponse.getStatus();
                                if (status == 1) {
                                    Log.e("TAG", "isSuccessful ");

                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterForSponsor.this, "تم التعديل بنجاح", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(RegisterForSponsor.this, SportsContainerActivity.class);
                                    startActivity(intent);
                                    finish();

                                    dataSaver.edit()
                                            .putBoolean(AppKeys.IS_LOGIN_KEY, true)
                                            .commit();
                                    dataSaver.edit()
                                            .putBoolean(AppKeys.IS_SPONSOR_ACCOUNT, true)
                                            .apply();

                                } else {
                                    Log.e("TAG", "notSuccessful ");
                                    progressDialog.dismiss();
                                    String message = "";
                                    for (int i = 0; i < response.body().getMessages().size(); i++) {
                                        message += response.body().getMessages().get(i);
                                        Toast.makeText(RegisterForSponsor.this, message, Toast.LENGTH_LONG).show();

                                    }
                                }
                            } else {
                                Toast.makeText(RegisterForSponsor.this, "not successful", Toast.LENGTH_LONG).show();
                            }
                        }


                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {

                            progressDialog.dismiss();
                            Log.e("TAG1", "onFailure " + t.getMessage());

                            //Toast.makeText(RegisterForSponsor.this, t.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
                }

                if (selectedImagePath == null || selectedImagePath.equals("")) {
                    imagePart = null;
                }

                Service.Fetcher.getInstance().updateSponsorProfile(imagePart,api_token, name, phoneNumber, usermail,
                        pass, location,governorate,city).enqueue(new Callback<GeneralResponse>() {


                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                        if (response.isSuccessful()) {

                            GeneralResponse generalResponse = response.body();
                            int status = generalResponse.getStatus();
                            if (status == 1) {
                                Log.e("TAG", "isSuccessful ");

                                progressDialog.dismiss();
                                Toast.makeText(RegisterForSponsor.this, "تم التعديل بنجاح", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterForSponsor.this, SportsContainerActivity.class);
                                startActivity(intent);
                                finish();

                                dataSaver.edit()
                                        .putBoolean(AppKeys.IS_LOGIN_KEY, true)
                                        .commit();
                                dataSaver.edit()
                                        .putBoolean(AppKeys.IS_SPONSOR_ACCOUNT, true)
                                        .apply();

                            } else {
                                progressDialog.dismiss();
                                String message = "";
                                for (int i = 0; i < response.body().getMessages().size(); i++) {
                                    message += response.body().getMessages().get(i);
                                    Toast.makeText(RegisterForSponsor.this, message, Toast.LENGTH_LONG).show();

                                }
                            }
                        }
                        Log.e("TAG null", "notSuccessful ");
                    }


                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {

                        progressDialog.dismiss();
                        Log.e("TAG2", "onFailure " + t.getMessage());

                     //   Toast.makeText(RegisterForSponsor.this, "", Toast.LENGTH_LONG).show();

                    }
                });
            }
        }
    }


    public void readTerm(View view) {
        isTermsRead = dataSaver.getBoolean(AppKeys.IS_TERMS_READ, false);

        final ChooseDialog dialog = new ChooseDialog(this, "قراءة الشروط");

        dialog.setOnDismissListener(this);
        dialog.show();

        Service.Fetcher.getInstance().getTerms("privacy").enqueue(new Callback<Terms>() {
            @Override
            public void onResponse(Call<Terms> call, Response<Terms> response) {
                if (response.isSuccessful()) {
                    dialog.showTermsList(response.body().getPage()
                            .getDetails().toString());
                } else {
                    dialog.dismiss();
                    Toast.makeText(RegisterForSponsor.this, AppKeys.SOME_THING_WENT_WRONG_KEY, Toast.LENGTH_SHORT).show();

                }
            }


            @Override
            public void onFailure(Call<Terms> call, Throwable t) {
                dialog.dismiss();
               // Toast.makeText(RegisterForSponsor.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


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
}

