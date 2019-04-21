package com.eg.typicaldesign.moaheb.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.controllers.AppKeys;

import maes.tech.intentanim.CustomIntent;
import me.anwarshahriar.calligrapher.Calligrapher;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class SportTypeActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout linearSingle, linearMulti;
    SharedPreferences dataSaver;
    Animation upToDown, downToUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_type);
        linearSingle = findViewById(R.id.linear_left);
        linearMulti = findViewById(R.id.linear_right);
        upToDown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        downToUp = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "bein-normal.ttf", true);

        linearSingle.setAnimation(downToUp);
        linearMulti.setAnimation(upToDown);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        linearSingle.setOnClickListener(this);
        linearMulti.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == linearSingle) {
            dataSaver.edit()
                    .putBoolean(AppKeys.IS_SINGLE_KEY, true)
                    .commit();

            Intent intent = new Intent(this, ChooseSportActivity.class);
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");
        }
        if (view == linearMulti) {
            dataSaver.edit()
                    .putBoolean(AppKeys.IS_SINGLE_KEY, false)
                    .commit();
            Intent intent = new Intent(this, ChooseSportActivity.class);
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");
        }

    }
}
