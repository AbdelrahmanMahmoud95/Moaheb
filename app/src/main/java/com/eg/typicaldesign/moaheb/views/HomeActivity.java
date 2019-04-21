package com.eg.typicaldesign.moaheb.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eg.typicaldesign.moaheb.R;

import maes.tech.intentanim.CustomIntent;
import me.anwarshahriar.calligrapher.Calligrapher;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    TextView playerText, sponsorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        playerText = (TextView) findViewById(R.id.player);
        sponsorText = (TextView) findViewById(R.id.sponsor);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,"bein-normal.ttf",true);

        playerText.setOnClickListener(this);
        sponsorText.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == playerText){
            Intent intent = new Intent(this,LoginForPlayer.class);
            startActivity(intent);

        }
        if (view == sponsorText){
            Intent intent = new Intent(this,LoginForPlayer.class);
            startActivity(intent);

        }
        /**left-to-right
         *right-to-left
         *bottom-to-up
         *up-to-bottom
         *fadein-to-fadeout
         *rotateout-to-rotatein*/

    }
    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this,"right-to-left");
    }
}
