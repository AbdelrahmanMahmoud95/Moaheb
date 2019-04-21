package com.eg.typicaldesign.moaheb.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.controllers.AppKeys;
import com.eg.typicaldesign.moaheb.controllers.Service;
import com.eg.typicaldesign.moaheb.models.Terms;

import maes.tech.intentanim.CustomIntent;
import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivacyActivity extends AppCompatActivity implements View.OnClickListener {
    TextView privacy, skipText;
    ImageView arrowImage;
    Terms terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        privacy = findViewById(R.id.privacy);
        arrowImage = findViewById(R.id.arrow_image);
        skipText = findViewById(R.id.skip_text);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "bein-normal.ttf", true);

        arrowImage.setOnClickListener(this);
        skipText.setOnClickListener(this);
        getPrivacy();
    }

    public void getPrivacy() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Service.Fetcher.getInstance().getTerms("sponsor-help").enqueue(new Callback<Terms>() {
            @Override
            public void onResponse(Call<Terms> call, Response<Terms> response) {
                if (response.isSuccessful()) {
                    terms = response.body();
                    progressDialog.dismiss();
                    privacy.setText(terms.getPage().getDetails());
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(PrivacyActivity.this, AppKeys.SOME_THING_WENT_WRONG_KEY, Toast.LENGTH_SHORT).show();

                }
            }


            @Override
            public void onFailure(Call<Terms> call, Throwable t) {
                progressDialog.dismiss();
                // Toast.makeText(RegisterForPlayer.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == arrowImage) {
            Intent intent = new Intent(this, RegisterForSponsor.class);
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");

        }
        if (view == skipText) {
            Intent intent = new Intent(this, RegisterForSponsor.class);
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");

        }
    }

}
