package com.eg.typicaldesign.moaheb.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.eg.typicaldesign.moaheb.R;
import com.squareup.picasso.Picasso;

public class PlayerImageActivity extends AppCompatActivity {
    ImageView playerImage;
    RelativeLayout layout;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_image);
        Intent mIntent = getIntent();

        image = mIntent.getStringExtra("imagePath");
        playerImage = (ImageView) findViewById(R.id.player_image);

        Picasso.with(PlayerImageActivity.this).load(image).into(playerImage);
    }

}
