package com.eg.typicaldesign.moaheb.views;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.controllers.AppKeys;
import com.eg.typicaldesign.moaheb.controllers.Service;
import com.eg.typicaldesign.moaheb.models.GeneralResponse;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.ByteArrayOutputStream;
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

public class UploadDocumentsActivity extends BaseActivityPlayer implements View.OnClickListener {
    ImageView addIcon, cameraIcon, image;
    AdView adView;
    Button addImage, myImages;
    final int PICK_IMAGE = 122;
    private static final int CAMERA_IMAGE = 345;
    String selectedImagePath;
    Bitmap bitmap;
    String token;
    SharedPreferences dataSaver;
    EditText title;
    LinearLayout galaryLinear, cameraLinear;
    int page = 4;
    private static final String READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int External_Permission_Request_code = 0505;
    private boolean mExternalPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_documents);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        addIcon = (ImageView) findViewById(R.id.add_image);
        cameraIcon = (ImageView) findViewById(R.id.camera);
        addImage = (Button) findViewById(R.id.add_btn);
        myImages = (Button) findViewById(R.id.my_images);
        image = (ImageView) findViewById(R.id.new_image);
        title = (EditText) findViewById(R.id.title);
        galaryLinear = (LinearLayout) findViewById(R.id.linear_galary);
        cameraLinear = (LinearLayout) findViewById(R.id.linear_camera);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "bein-normal.ttf", true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.alertDialogColor), PorterDuff.Mode.SRC_ATOP);

        galaryLinear.setOnClickListener(this);
        cameraLinear.setOnClickListener(this);
        addImage.setOnClickListener(this);
        myImages.setOnClickListener(this);

        MobileAds.initialize(this, " ca-app-pub-8408797241053494~4862516501");
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        getExternalPermission();
    }


    private void getExternalPermission() {
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                READ_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mExternalPermissionGranted = true;

        } else {
            ActivityCompat.requestPermissions(this, permission, External_Permission_Request_code);
        }
    }


    @Override
    int getContentViewId() {
        return R.layout.activity_upload_documents;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_more;
    }

    @Override
    public void onClick(View view) {
        if (view == galaryLinear) {
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
        if (view == cameraLinear) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_IMAGE);
        }
        if (view == myImages) {
            Intent intent = new Intent(this, PlayerDocumentsActivity.class);
            startActivity(intent);
            finish();
        }
        if (view == addImage) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("جاري تحميل الصورة...");
            progressDialog.show();
            if (selectedImagePath == null) {
                progressDialog.dismiss();
                Toast.makeText(UploadDocumentsActivity.this, "برجاء اختيار صورة", Toast.LENGTH_SHORT).show();
            } else {
                File file = new File(selectedImagePath);
                Log.e("image path", "selectedImagePath " + selectedImagePath);
                RequestBody imageFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("identity_image", file.getName(), imageFile);
                RequestBody apiToken = RequestBody.create(MediaType.parse("multipart/form-data"), token);
                RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), title.getText().toString());


                Service.Fetcher.getInstance().uploadDocs(page, imagePart, apiToken, name).enqueue(new Callback<GeneralResponse>() {

                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                        if (response.isSuccessful()) {

                            GeneralResponse generalResponse = response.body();
                            int status = generalResponse.getStatus();
                            if (status == 1) {
                                Log.e("TAG", "isSuccessful ");

                                progressDialog.dismiss();
                                Toast.makeText(UploadDocumentsActivity.this, "تم إضافة الصورة بنجاح", Toast.LENGTH_LONG).show();


                            } else {
                                Log.e("TAG", "notSuccessful ");
                                progressDialog.dismiss();
                                String message = "";
                                for (int i = 0; i < response.body().getMessages().size(); i++) {
                                    message += response.body().getMessages().get(i);
                                    Toast.makeText(UploadDocumentsActivity.this, message, Toast.LENGTH_LONG).show();

                                }
                            }
                        }
                    }


                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {

                        progressDialog.dismiss();
                        Log.e("TAG", "onFailure " + t.getMessage());

                        Toast.makeText(UploadDocumentsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null && resultCode != RESULT_CANCELED) {

            Uri uri = data.getData();

            selectedImagePath = getRealPath(uri, UploadDocumentsActivity.this);

            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
                System.gc();
            }

            Log.e("TAG", "selectedImagePath5 " + selectedImagePath);
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                image.setVisibility(View.VISIBLE);
                image.setImageBitmap(bitmap);
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
                selectedImagePath = getRealPathFromURI(uri, UploadDocumentsActivity.this);


            } catch (IOException e) {
                e.printStackTrace();
            }


        }


        if (requestCode == CAMERA_IMAGE && resultCode == RESULT_OK) {

            Uri uri;

            Bitmap photo = (Bitmap) data.getExtras().get("data");

            image.setImageBitmap(photo);

            int maxImageSize = 700;
            if (photo.getWidth() >= maxImageSize) {
                maxImageSize = 700;
            } else if (photo.getWidth() < maxImageSize) {
                maxImageSize = photo.getWidth();
            }
            if (photo.getHeight() >= maxImageSize) {
                maxImageSize = 700;
            } else if (photo.getHeight() < maxImageSize) {
                maxImageSize = photo.getHeight();
            }
            float ratio = Math.min(
                    (float) maxImageSize / photo.getWidth(),
                    (float) maxImageSize / photo.getHeight());
            int width = Math.round((float) ratio * photo.getWidth());
            int height = Math.round((float) ratio * photo.getHeight());
            Bitmap scaled = Bitmap.createScaledBitmap(photo, width,
                    height, true);
            String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), scaled, "Title", null);
            uri = Uri.parse(path);
            selectedImagePath = getRealPathFromURI(uri, UploadDocumentsActivity.this);



        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }


}

