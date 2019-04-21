package com.eg.typicaldesign.moaheb.views;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.media.MediaRecorder;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import android.widget.VideoView;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.controllers.AppKeys;
import com.eg.typicaldesign.moaheb.controllers.Service;
import com.eg.typicaldesign.moaheb.controllers.VideoCompress;
import com.eg.typicaldesign.moaheb.controllers.ViewPagerAdapter;
import com.eg.typicaldesign.moaheb.models.GeneralResponse;
import com.eg.typicaldesign.moaheb.models.SliderImage;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.anwarshahriar.calligrapher.Calligrapher;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class UploadVideoActivity extends BaseActivityPlayer implements View.OnClickListener {
    ImageView addIcon, cameraIcon;
    AdView adView;
    VideoView newVideo;
    Button addVideo, myVideos;
    private static final int SELECT_VIDEO = 345;
    String selectedImagePath;
    String token;
    String destPath;
    String videoPath;
    String[] command;
    SharedPreferences dataSaver;
    // WifiManager wifiManager;
    LinearLayout galaryLinear, cameraLinear;
    int TYPE = 2;
    MediaRecorder mediaRecorder;
    File fileSize;
    long length;
    long realSpeed;
    FFmpeg ffmpeg;
    String[] complexCommand;
    String outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    String videoOut =  outputDir + File.separator + "video.mp4";
    EditText description;
    private static final String READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int External_Permission_Request_code = 0505;
    private boolean mExternalPermissionGranted = false;
    int speedMbps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        addIcon = (ImageView) findViewById(R.id.add_image);
        cameraIcon = (ImageView) findViewById(R.id.camera);
        description = (EditText) findViewById(R.id.description);
        addVideo = (Button) findViewById(R.id.add_btn);
        myVideos = (Button) findViewById(R.id.my_images);
        newVideo = (VideoView) findViewById(R.id.new_video);
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
        addVideo.setOnClickListener(this);
        myVideos.setOnClickListener(this);
        getExternalPermission();
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        speedMbps = wifiInfo.getLinkSpeed();
        realSpeed = (long) speedMbps / 20;
        Log.e("speedMbps ", "speedMbps" + speedMbps);
        MobileAds.initialize(this, " ca-app-pub-8408797241053494~4862516501");
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        ffmpeg = FFmpeg.getInstance(this);

        Service.Fetcher.getInstance().downloadFile().enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    Log.e("TAG", "isSuccessful1");
                    writeResponseBodyToDisk(response.body());
                 //   Toast.makeText(UploadVideoActivity.this, "success", Toast.LENGTH_LONG).show();
                    Log.e("TAG", "videoPath " + videoPath);


                } else {

                  //  Toast.makeText(UploadVideoActivity.this, "fail", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(UploadVideoActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();

                Log.e("TAG ", "onFailure1");
            }
        });


        //for compress video
        //complexCommand = new String[]{"-y", "-i","/storage/45EA-015B/DCIM/100MEDIA/VIDEO0020.mp4", "-s", "160x120", "-r", "25", "-vcodec", "mpeg4", "-b:v", "150k", "-b:a", "48000", "-ac", "2", "-ar", "22050", "/storage/emulated/0/Download/abdo.mp4"};

        //put image on video
        //complexCommand = new String[]{"-y", "-i", "/storage/45EA-015B/DCIM/100MEDIA/VIDEO0020.mp4", "-i", "/storage/emulated/0/DCIM/100MEDIA/final icon.png", "-filter_complex", "overlay=main_w-overlay_w-5:5", "/storage/emulated/0/Download/abdo.mp4"};

        //for put image over the video
        // String[] cmd = new String[]{  "-y", "-i", "/storage/45EA-015B/DCIM/100MEDIA/VIDEO0020.mp4", "-i", "/storage/emulated/0/DCIM/100MEDIA/IMAG3498.jpg" ,"-filter_complex", "[1][0]scale2ref[i][m];[m][i]overlay[v]" ,"-map", "[v]", "-map",  "0:a?", "-ac", "2", "/storage/emulated/0/Download/abdo.mp4"};


//        complexCommand = new String[]{"-y", "-i", "/storage/45EA-015B/DCIM/100MEDIA/VIDEO0020.mp4", "-i ", "/storage/emulated/0/DCIM/100MEDIA/final icon.png", "-filter_complex",
//                "[0:v][1:v] overlay=25:25:enable='between(t,0,20)'", "-map", "[v]", "-map", "0:a?", "-ac", "2", "/storage/emulated/0/Download/abdo.mp4"};

        //put image on video
       // complexCommand = new String[]{"-y", "-i", "/storage/45EA-015B/DCIM/100MEDIA/VIDEO0020.mp4", "-i", videoPath, "-filter_complex", "overlay=main_w-overlay_w-5:5", "/storage/emulated/0/Download/abdo.mp4"};

    }


    private void loadFFMpegBinary() {
        try {
            if (ffmpeg == null) {
                ffmpeg = FFmpeg.getInstance(this);
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    Log.e("TAG", "onFail ffmpeg");

                }

                @Override
                public void onSuccess() {
                    Log.e("TAG", "onSuccess ffmpeg");
                }
            });
        } catch (FFmpegNotSupportedException e) {

        } catch (Exception e) {
        }
    }

    private void execFFmpegBinary(String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Log.e("TAG", "onFailure execffmpeg");
                }

                @Override
                public void onSuccess(String s) {
                    //  Log.d(TAG, “SUCCESS with output : ” + s);
                    Log.e("TAG", "onSuccess execffmpeg");
                }


                @Override
                public void onProgress(String s) {
                    Log.e("TAG", "onProgress execffmpeg");
                }

                @Override
                public void onStart() {
                    Log.e("TAG", "onStart execffmpeg");

                }

                @Override
                public void onFinish() {
                    Log.e("TAG", "onFinish execffmpeg");

                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {

        }
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
        return R.layout.activity_upload_video;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_video;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                mediaRecorder = new MediaRecorder();
                //   mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
                // mediaRecorder.setVideoEncodingBitRate(690000 );
                Uri selectedVideoUri = data.getData();
                selectedImagePath = getPath(selectedVideoUri);
                Log.e("TAG", "selectedImagePath " + selectedImagePath);

                try {
                    fileSize = new File(selectedImagePath);
                    length = fileSize.length();
                    length = length / 1024;
                    Log.e("TAG", "file size " + length);


                } catch (Exception e) {
                    System.out.println("File not found : " + e.getMessage() + e);
                }
            }
            Uri uri = Uri.parse(selectedImagePath);
            newVideo.setVisibility(View.VISIBLE);
            newVideo.setVideoURI(uri);
            newVideo.requestFocus();
            newVideo.seekTo(100);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == galaryLinear) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
        }
        if (view == cameraLinear) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            startActivityForResult(intent, SELECT_VIDEO);
        }
        if (view == myVideos) {
            Intent intent = new Intent(this, PlayerVideosActivity.class);
            startActivity(intent);
            finish();
        }
        if (view == addVideo) {


            if (selectedImagePath == null)

            {

                Toast.makeText(UploadVideoActivity.this, "برجاء اختيار فيديو", Toast.LENGTH_SHORT).show();
            } else {
                //for merge two videos
//                command = new String[]{"-y", "-i", videoPath, "-i",selectedImagePath, "-strict", "experimental", "-filter_complex",
//                        "[0:v]scale=iw*min(1920/iw\\,1080/ih):ih*min(1920/iw\\,1080/ih), pad=1920:1080:(1920-iw*min(1920/iw\\,1080/ih))/2:(1080-ih*min(1920/iw\\,1080/ih))/2,setsar=1:1[v0];[1:v] scale=iw*min(1920/iw\\,1080/ih):ih*min(1920/iw\\,1080/ih), pad=1920:1080:(1920-iw*min(1920/iw\\,1080/ih))/2:(1080-ih*min(1920/iw\\,1080/ih))/2,setsar=1:1[v1];[v0][0:a][v1][1:a] concat=n=2:v=1:a=1",
//                        "-ab", "48000", "-ac", "2", "-ar", "22050", "-s", "1920x1080", "-vcodec", "libx264", "-crf", "27", "-q", "4", "-preset", "ultrafast", videoOut};
//
//
//                loadFFMpegBinary();
//                execFFmpegBinary(command);


                destPath = outputDir + File.separator + "VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss", getLocale()).format(new Date()) + ".mp4";
                final ProgressDialog progressDialog = new ProgressDialog(UploadVideoActivity.this);
                progressDialog.setMessage("جاري ضغط الفيديو...");
                progressDialog.setCancelable(false);
                Log.e("TAG", "outputDir " + outputDir);
                VideoCompress.compressVideoLow(selectedImagePath, destPath, new VideoCompress.CompressListener() {

                    @Override
                    public void onStart() {
                        addVideo.setEnabled(false);
                        progressDialog.show();
                    }

                    @Override
                    public void onSuccess() {
                        addVideo.setEnabled(true);
                        Log.e("TAG", "destPath " + destPath);
                        progressDialog.dismiss();
                        uploadVideo();

                    }

                    @Override
                    public void onFail() {
                        Toast.makeText(UploadVideoActivity.this, "فشل", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onProgress(float percent) {

                    }
                });


            }
        }
    }


    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Video.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));

        cursor.close();


        return path;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private Locale getLocale() {
        Configuration config = getResources().getConfiguration();
        Locale sysLocale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config);
        } else {
            sysLocale = getSystemLocaleLegacy(config);
        }

        return sysLocale;
    }

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config) {
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config) {
        return config.getLocales().get(0);
    }

    public void uploadVideo() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري تحميل الفيديو...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);

        final Handler handle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDialog.incrementProgressBy(1);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Log.e("TRY", "try");


                    while (progressDialog.getProgress() <= progressDialog.getMax()) {
                        long c = 63;
                        long s = length / c;

                        Log.e("C", "c " + c);
                        Log.e("realSpeed", "realSpeed " + realSpeed);
                        Log.e("S", "s " + s);

                        Thread.sleep(s);

                        handle.sendMessage(handle.obtainMessage());
                        if (progressDialog.getProgress() == progressDialog.getMax()) {
                            progressDialog.dismiss();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

        File file = new File(destPath);
        progressDialog.show();
        RequestBody imageFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("media", file.getName(), imageFile);
        RequestBody apiToken = RequestBody.create(MediaType.parse("multipart/form-data"), token);
        RequestBody type2 = RequestBody.create(MediaType.parse("multipart/form-data"), TYPE + "");
        RequestBody des = RequestBody.create(MediaType.parse("multipart/form-data"), description.getText().toString());


        Service.Fetcher.getInstance().uploadVideo(imagePart, apiToken, type2, des
        ).enqueue(new Callback<GeneralResponse>() {


            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                if (response.isSuccessful()) {

                    GeneralResponse generalResponse = response.body();
                    int status = generalResponse.getStatus();
                    if (status == 1) {
                        Log.e("TAG", "isSuccessful ");

                        progressDialog.dismiss();
                        Toast.makeText(UploadVideoActivity.this, "تم إضافة الفيديو بنجاح", Toast.LENGTH_LONG).show();


                    } else {
                        Log.e("TAG", "notSuccessful ");
                        progressDialog.dismiss();
                        String message = "";
                        for (int i = 0; i < response.body().getMessages().size(); i++) {
                            message += response.body().getMessages().get(i);
                            Toast.makeText(UploadVideoActivity.this, message, Toast.LENGTH_LONG).show();

                        }
                    }
                }
            }


            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {


                Log.e("TAG", "onFailure " + t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(UploadVideoActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();

            }
        });
    }


    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            //getExternalFilesDir(null) + File.separator + "Future Studio Icon.png";
            videoPath = outputDir + File.separator + "app-talent.mp4";
            File futureStudioIconFile = new File(videoPath);
            Log.e("TAG", "futureStudioIconFile " + futureStudioIconFile);
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.e("TAG", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

}


