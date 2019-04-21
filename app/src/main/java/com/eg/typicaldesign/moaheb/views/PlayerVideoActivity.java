package com.eg.typicaldesign.moaheb.views;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.eg.typicaldesign.moaheb.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayerVideoActivity extends YouTubeBaseActivity {
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener initializedListener;

    TextView textDescription, acceptedVideo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_video);
        youTubePlayerView = findViewById(R.id.player_video);

        textDescription = (TextView) findViewById(R.id.text_details);
        acceptedVideo = (TextView) findViewById(R.id.text_accepted);

        Intent intent = getIntent();
        final String videoPath = intent.getStringExtra("videoPath");
        final String videoDescription = intent.getStringExtra("videoDescription");
        final String videoAcceptation = intent.getStringExtra("videoAcceptation");

        Log.e("TAG", "videoAcceptation " + videoAcceptation);

        if (videoAcceptation != null) {
            acceptedVideo.setText(videoAcceptation);
            youTubePlayerView.setVisibility(View.GONE);
        }

        textDescription.setText(videoDescription);
        Log.e("TAG", "videoPath " + videoPath);
        initializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                youTubePlayer.loadVideo(videoPath);

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                Toast.makeText(PlayerVideoActivity.this, youTubeInitializationResult.toString() + "", Toast.LENGTH_LONG).show();
            }
        };

        youTubePlayerView.initialize("AIzaSyATigY_fXnrFqwvpBdFuyyhurLpZFHvKhY", initializedListener);


    }
}
