package com.eg.typicaldesign.moaheb.controllers;

/**
 * Created by MAGIC on 4/26/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.models.playerProfile;
import com.eg.typicaldesign.moaheb.views.PlayerVideoActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import android.widget.ImageView;
import android.widget.VideoView;

import static android.media.CamcorderProfile.get;
import static android.provider.Contacts.SettingsColumns.KEY;

/**
 * Created by MAGIC on 4/26/2018.
 */

public class PlayersVideosAdapter extends RecyclerView.Adapter<PlayersVideosAdapter.VideosViewHolder> {
    Context context;
    playerProfile profile;


    public PlayersVideosAdapter(Context context, playerProfile profile) {
        this.context = context;
        this.profile = profile;
    }

    @Override
    public PlayersVideosAdapter.VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.players_videos, parent, false);
        PlayersVideosAdapter.VideosViewHolder VideosViewHolder = new PlayersVideosAdapter.VideosViewHolder(view);
        return VideosViewHolder;
    }


    @Override
    public void onBindViewHolder(final PlayersVideosAdapter.VideosViewHolder holder,  final int position) {
        final String videoPath = profile.getUser().getApprovedVideos().get(position).getPath();
        final String videoDescription = profile.getUser().getApprovedVideos().get(position).getDescription();

        holder.playVideo.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent = new Intent(context, PlayerVideoActivity.class);
                                                    intent.putExtra("videoPath",videoPath);
                                                    intent.putExtra("videoDescription",videoDescription);
                                                    if (profile.getUser().getApprovedVideos().get(position).getApproved().equals("0")) {
                                                        intent.putExtra("videoAcceptation", "هذا الفيديو لم يتم الموافقة عليه بعد");

                                                    }
                                                    context.startActivity(intent);
                                              }
                                            }
        );
        holder.youTubeThumbnailView.initialize(KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(final YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo(videoPath);
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {

                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView childYouTubeThumbnailView, String s) {
                        // holder.loding.setVisibility(View.GONE);
                        youTubeThumbnailLoader.release(); // spy ga memory lick
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        youTubeThumbnailLoader.release(); // spy ga memory lick
                    }
                });


            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return profile.getUser().getApprovedVideos().size();
    }

    public class VideosViewHolder extends RecyclerView.ViewHolder {
        YouTubeThumbnailView youTubeThumbnailView;
        ImageView playVideo;


        public VideosViewHolder(View itemView) {
            super(itemView);

            youTubeThumbnailView =  itemView.findViewById(R.id.player_videos);
            playVideo = (ImageView) itemView.findViewById(R.id.play_icon);

        }
    }

}
