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
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import com.eg.typicaldesign.moaheb.models.GeneralResponse;
import com.eg.typicaldesign.moaheb.models.Terms;
import com.eg.typicaldesign.moaheb.models.UserInformation;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class RegisterForPlayer extends BaseActivityPlayer implements View.OnClickListener, DialogInterface.OnDismissListener {
    LinearLayout dateLinear, linearImage;
    AdView adView;
    Button submit;
    EditText userName, phone, email, address, password, overview;
    TextView dateText, selectSport, selectCity, selectGovernorate, selectPosition, selectImage, readTerms;
    String sportName, positionName = "";
    String governorateName = "";
    private Bitmap bitmap;
    private ImageView Image;
    Boolean isSingle;
    double lat, lon;
    int page = 1;
    String selectedImagePath = "";
    final int PICK_IMAGE = 123;
    private static final String READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String CAMERA = Manifest.permission.CAMERA;
    private static final int External_Permission_Request_code = 0505;
    private boolean mExternalPermissionGranted = false;
    SharedPreferences dataSaver;
    int city_id, position_id, sportId, gov_id;
    String city_name;
    private GoogleMap mMap;
    private static final String Fine_location = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String coarse_Location = Manifest.permission.ACCESS_COARSE_LOCATION;

    int TYPE = 1;
    String token;
    private FusedLocationProviderClient fusedLocationProviderClient;
    boolean isTermsRead = false;
    boolean isCityDialog, isSportDialog, isGovernorateDialog, isPositionDialog = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_player);
        dateLinear = (LinearLayout) findViewById(R.id.linear_date);
        dateText = (TextView) findViewById(R.id.date);
        linearImage = (LinearLayout) findViewById(R.id.linear_image);
        Image = (ImageView) findViewById(R.id.register_user_photo);
        phone = (EditText) findViewById(R.id.phone_number);
        email = (EditText) findViewById(R.id.email);
        userName = (EditText) findViewById(R.id.username);
        address = (EditText) findViewById(R.id.location);
        password = (EditText) findViewById(R.id.user_password);
        overview = (EditText) findViewById(R.id.overview);
        selectImage = (TextView) findViewById(R.id.image);
        submit = (Button) findViewById(R.id.register_btn);
        readTerms = (TextView) findViewById(R.id.read_terms);
        LinearLayout contentPain = findViewById(R.id.contentpain);

        contentPain.setAlpha(0);
        contentPain.animate().alpha(1.0f).setDuration(1600).start();
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "bein-normal.ttf", true);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        sportId = dataSaver.getInt(AppKeys.SPORT_ID, -1);
        token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        position_id = dataSaver.getInt(AppKeys.POSITION_ID, -1);
        isSingle = dataSaver.getBoolean(AppKeys.IS_SINGLE_KEY, false);



//        MobileAds.initialize(this, " ca-app-pub-8408797241053494~4862516501");
//        adView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);

        submit.setOnClickListener(this);
        selectImage.setOnClickListener(this);
        linearImage.setOnClickListener(this);
        dateText.setOnClickListener(this);
        dateLinear.setOnClickListener(this);
        if (isSingle) {

            TYPE = TYPE + 1;
            Log.e("TAG", "TYPE" + TYPE);
        }

        getExternalPermission();
        if (!token.equals("")) {
            getUserData();
            isTermsRead = true;
            readTerms.setVisibility(View.GONE);
            submit.setText("تعديل بياناتك");
            navigationView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_register_for_player;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_data;
    }

    public void getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Location currentLocation = (Location) task.getResult();
                        lon = currentLocation.getLongitude();
                        lat = currentLocation.getLatitude();
                        Log.e("TAG", "lat " + lat + "lon " + lon);
                    } else {
                        Toast.makeText(RegisterForPlayer.this, "there is not location such that", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (SecurityException e) {
            e.getMessage();

        }
    }


    private void getExternalPermission() {
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                CAMERA) == PackageManager.PERMISSION_GRANTED) {
            mExternalPermissionGranted = true;

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    coarse_Location) == PackageManager.PERMISSION_GRANTED) {
                mExternalPermissionGranted = true;
                if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                        READ_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    mExternalPermissionGranted = true;

                    if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                            WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        mExternalPermissionGranted = true;
                        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                                Fine_location) == PackageManager.PERMISSION_GRANTED) {
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

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null && resultCode != RESULT_CANCELED) {

            Uri uri = data.getData();

            selectedImagePath = getRealPath(uri, RegisterForPlayer.this);

            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
                System.gc();
            }

            Log.e("TAG", "selectedImagePath5 " + selectedImagePath);


            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Image.setImageBitmap(bitmap);


//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
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


                uri = Uri.parse(path);
                selectedImagePath = getRealPathFromURI(uri, RegisterForPlayer.this);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }  }

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
        if (view == address) {
            getDeviceLocation();
        }

        if (view == submit) {
            if (token.equals("")) {
                if (isTermsRead == false) {
                    Toast.makeText(RegisterForPlayer.this, "يجب قراءة الشروط والاحكام", Toast.LENGTH_LONG).show();
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
                    RequestBody userDate = RequestBody.create(MediaType.parse("multipart/form-data"), dateText.getText().toString());
                    RequestBody userOverview = RequestBody.create(MediaType.parse("multipart/form-data"), overview.getText().toString());
                    RequestBody sport = RequestBody.create(MediaType.parse("multipart/form-data"), sportId + "");
                    final RequestBody position = RequestBody.create(MediaType.parse("multipart/form-data"), position_id + "");
                    RequestBody location = RequestBody.create(MediaType.parse("multipart/form-data"), address.getText().toString());
                    RequestBody pass = RequestBody.create(MediaType.parse("multipart/form-data"), password.getText().toString());
                    RequestBody type = RequestBody.create(MediaType.parse("multipart/form-data"), TYPE + "");
                    RequestBody latt = RequestBody.create(MediaType.parse("multipart/form-data"), lat + "");
                    RequestBody lonn = RequestBody.create(MediaType.parse("multipart/form-data"), lon + "");


                    Service.Fetcher.getInstance().registerUser(imagePart, name, phoneNumber, usermail,
                            pass, userDate, position, location, sport, type, userOverview, latt, lonn).enqueue(new Callback<GeneralResponse>() {


                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                            if (response.isSuccessful()) {

                                GeneralResponse generalResponse = response.body();
                                int status = generalResponse.getStatus();
                                if (status == 1) {
                                    Log.e("TAG", "isSuccessful ");
                                    Log.e("TAG", "positionId "+position_id);

                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterForPlayer.this, "تم إنشاء الحساب بنجاح", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(RegisterForPlayer.this, SportsContainerActivity.class);
                                    startActivity(intent);
                                    finish();
                                    dataSaver.edit()
                                            .putInt(AppKeys.CITY_ID, -1)
                                            .apply();
                                    dataSaver.edit()
                                            .putInt(AppKeys.GOVERNORATE_ID, -1)
                                            .apply();

                                    dataSaver.edit()
                                            .putInt(AppKeys.ID_KEY, generalResponse.getId())
                                            .apply();

                                    dataSaver.edit()
                                            .putString(AppKeys.TOKEN_KEY, generalResponse.getToken())
                                            .apply();
                                    dataSaver.edit()
                                            .putBoolean(AppKeys.IS_LOGIN_KEY, true)
                                            .apply();
                                    dataSaver.edit()
                                            .putBoolean(AppKeys.IS_SPONSOR_ACCOUNT, false)
                                            .apply();

                                } else {
                                    Log.e("TAG", "notSuccessful ");
                                    progressDialog.dismiss();
                                    String message = "";
                                    for (int i = 0; i < response.body().getMessages().size(); i++) {
                                        message += response.body().getMessages().get(i);
                                        Toast.makeText(RegisterForPlayer.this, message, Toast.LENGTH_LONG).show();

                                    }
                                }
                            }
                        }


                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {

                            progressDialog.dismiss();
                            Log.e("TAG", "onFailure " + t.getMessage());

                         //   Toast.makeText(RegisterForPlayer.this, t.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });

                }
            } else {
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
                RequestBody userDate = RequestBody.create(MediaType.parse("multipart/form-data"), dateText.getText().toString());
                RequestBody userOverview = RequestBody.create(MediaType.parse("multipart/form-data"), overview.getText().toString());
                RequestBody sport = RequestBody.create(MediaType.parse("multipart/form-data"), sportId + "");
                RequestBody position = RequestBody.create(MediaType.parse("multipart/form-data"), position_id + "");
                RequestBody location = RequestBody.create(MediaType.parse("multipart/form-data"), address.getText().toString());
                RequestBody pass = RequestBody.create(MediaType.parse("multipart/form-data"), password.getText().toString());
                RequestBody type = RequestBody.create(MediaType.parse("multipart/form-data"), TYPE + "");
                RequestBody latt = RequestBody.create(MediaType.parse("multipart/form-data"), lat + "");
                RequestBody lonn = RequestBody.create(MediaType.parse("multipart/form-data"), lon + "");
                RequestBody api_token = RequestBody.create(MediaType.parse("multipart/form-data"), token);
                if (selectedImagePath != null || !selectedImagePath.equals("")) {

                    Service.Fetcher.getInstance().updateUserProfile(page,imagePart, api_token, name, phoneNumber, usermail,
                            pass, userDate, position, location, sport, type, userOverview, latt, lonn).enqueue(new Callback<GeneralResponse>() {


                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                            if (response.isSuccessful()) {

                                GeneralResponse generalResponse = response.body();
                                int status = generalResponse.getStatus();
                                if (status == 1) {
                                    Log.e("TAG", "isSuccessful ");

                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterForPlayer.this, "تم التعديل بنجاح", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(RegisterForPlayer.this, SportsContainerActivity.class);
                                    startActivity(intent);
                                    finish();
                                    dataSaver.edit()
                                            .putInt(AppKeys.CITY_ID, -1)
                                            .commit();
                                    dataSaver.edit()
                                            .putInt(AppKeys.GOVERNORATE_ID, -1)
                                            .commit();
                                    dataSaver.edit()
                                            .putBoolean(AppKeys.IS_LOGIN_KEY, true)
                                            .commit();
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
                                        Toast.makeText(RegisterForPlayer.this, message, Toast.LENGTH_LONG).show();

                                    }
                                }
                            } else {
                                Toast.makeText(RegisterForPlayer.this, "not successful", Toast.LENGTH_LONG).show();

                            }
                        }


                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {

                            progressDialog.dismiss();
                            Log.e("TAG", "onFailure " + t.getMessage());

                         //   Toast.makeText(RegisterForPlayer.this, t.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
                }

                if (selectedImagePath == null || selectedImagePath.equals("")) {
                    imagePart = null;
                }

                    Service.Fetcher.getInstance().updateUserProfile(page,imagePart,api_token, name, phoneNumber, usermail,
                            pass, userDate, position, location, sport, type, userOverview, latt, lonn).enqueue(new Callback<GeneralResponse>() {


                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                            if (response.isSuccessful()) {

                                GeneralResponse generalResponse = response.body();
                                int status = generalResponse.getStatus();
                                if (status == 1) {
                                    Log.e("TAG", "isSuccessful ");

                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterForPlayer.this, "تم التعديل بنجاح", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(RegisterForPlayer.this, SportsContainerActivity.class);
                                    startActivity(intent);
                                    finish();
                                    dataSaver.edit()
                                            .putInt(AppKeys.CITY_ID, -1)
                                            .commit();
                                    dataSaver.edit()
                                            .putInt(AppKeys.GOVERNORATE_ID, -1)
                                            .commit();
                                    dataSaver.edit()
                                            .putBoolean(AppKeys.IS_LOGIN_KEY, true)
                                            .commit();
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
                                        Toast.makeText(RegisterForPlayer.this, message, Toast.LENGTH_LONG).show();

                                    }
                                }
                            }
                        }


                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {

                            progressDialog.dismiss();
                            Log.e("TAG", "onFailure " + t.getMessage());

                         //   Toast.makeText(RegisterForPlayer.this, t.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        }



    public void readTerms(View view) {
        isTermsRead = dataSaver.getBoolean(AppKeys.IS_TERMS_READ, false);

        final ChooseDialog dialog = new ChooseDialog(this, "الشروط والاحكام");

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
                    Toast.makeText(RegisterForPlayer.this, AppKeys.SOME_THING_WENT_WRONG_KEY, Toast.LENGTH_SHORT).show();

                }
            }


            @Override
            public void onFailure(Call<Terms> call, Throwable t) {
                dialog.dismiss();
              //  Toast.makeText(RegisterForPlayer.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
        if (isSportDialog) {
            sportName = dataSaver.getString(AppKeys.SPORT_NAME, "");
            sportId = dataSaver.getInt(AppKeys.SPORT_ID, -1);
            if (sportId != -1) {
                selectSport.setText(sportName);
            }
        }
        if (isGovernorateDialog) {

            governorateName = dataSaver.getString(AppKeys.GOVERNORATE_NAME, "");
            gov_id = dataSaver.getInt(AppKeys.GOVERNORATE_ID, 0);
            if (gov_id != -1) {
                selectGovernorate.setText(governorateName);
            }
        }
        if (isPositionDialog) {
            positionName = dataSaver.getString(AppKeys.POSITION_NAME, "");
            position_id = dataSaver.getInt(AppKeys.POSITION_ID, -1);
            if (position_id != -1) {
                selectPosition.setText(positionName);
            }
        }
    }

    public void getUserData() {
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

                        String name = userInformation.getUserInfo().getName();
                        String userPhone = userInformation.getUserInfo().getPhone();
                        String location = userInformation.getUserInfo().getAddress();
                        String userEmail = userInformation.getUserInfo().getEmail();
                        String userOverview = userInformation.getUserInfo().getOverview();
                        String date = userInformation.getUserInfo().getBirthDate();
                        userName.setText(name);
                        phone.setText(userPhone);
                        address.setText(location);
                        email.setText(userEmail);
                        overview.setText(userOverview);
                        dateText.setText(date);
                        Picasso.with(RegisterForPlayer.this).load(String.valueOf(userInformation.getUserInfo().getImage())).into(Image);


                    }
                }

            }

            @Override
            public void onFailure(Call<UserInformation> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG ", "onFailure");
                Toast.makeText(RegisterForPlayer.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
}

