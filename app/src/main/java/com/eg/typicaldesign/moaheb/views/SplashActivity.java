package com.eg.typicaldesign.moaheb.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.controllers.AppKeys;
import com.eg.typicaldesign.moaheb.controllers.Service;
import com.eg.typicaldesign.moaheb.controllers.ViewPagerAdapter;
import com.eg.typicaldesign.moaheb.models.SliderImage;

import java.util.Timer;
import java.util.TimerTask;

import maes.tech.intentanim.CustomIntent;
import me.anwarshahriar.calligrapher.Calligrapher;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView arrowImage;
    TextView skipText;
    ViewPager mPager;
    ViewPagerAdapter pagerAdapter;
    private static int currentPage = 0;
    SliderImage sliderImage;
    SharedPreferences dataSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        arrowImage = (ImageView) findViewById(R.id.arrow_image);
        skipText = (TextView) findViewById(R.id.skip_text);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        mPager = (ViewPager) findViewById(R.id.pager);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "bein-normal.ttf", true);

        arrowImage.setOnClickListener(this);
        skipText.setOnClickListener(this);
        getImages();
    }

    public void getImages() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Log.e("TAG", "isSuccessful");

        Service.Fetcher.getInstance().getImages().enqueue(new Callback<SliderImage>() {

            @Override
            public void onResponse(Call<SliderImage> call, Response<SliderImage> response) {

                if (response.isSuccessful()) {
                    Log.e("TAG", "isSuccessful1");
                    progressDialog.dismiss();
                    sliderImage = response.body();
                    pagerAdapter = new ViewPagerAdapter(SplashActivity.this, sliderImage.getSlider());
                    mPager.setAdapter(pagerAdapter);
                    init();

                } else {
                    progressDialog.dismiss();
                    Log.e("TAG", "isNotSuccessful");
                }

            }

            @Override
            public void onFailure(Call<SliderImage> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG ", "onFailure");
            }
        });
    }

    private void init() {
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == sliderImage.getSlider().size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 6000, 3000);
    }


    @Override
    public void onClick(View view) {
        if (view == arrowImage) {
            Intent intent = new Intent(this, SportsContainerActivity.class);
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");
            dataSaver.edit()
                    .putBoolean(AppKeys.IS_LOGIN_KEY, false)
                    .apply();
        }
        if (view == skipText) {
            Intent intent = new Intent(this, SportsContainerActivity.class);
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");
            dataSaver.edit()
                    .putBoolean(AppKeys.IS_LOGIN_KEY, false)
                    .apply();
        }
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }
}
