package com.eg.typicaldesign.moaheb.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.models.GeneralResponse;
import com.eg.typicaldesign.moaheb.models.Media;
import com.eg.typicaldesign.moaheb.views.PlayerVideoActivity;
import com.eg.typicaldesign.moaheb.views.PlayerVideosActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.provider.Contacts.SettingsColumns.KEY;

/**
 * Created by MAGIC on 5/7/2018.
 */

public class PlayerVideosAdapter extends RecyclerView.Adapter<PlayerVideosAdapter.VideosViewHolder> {
    Context context;
    String token;
    SharedPreferences dataSaver;
    int videoId;
    Media media;

    public PlayerVideosAdapter(Context context, Media media) {
        this.context = context;
        this.media = media;
    }

    @Override
    public PlayerVideosAdapter.VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_videos, parent, false);
        PlayerVideosAdapter.VideosViewHolder VideosViewHolder = new PlayerVideosAdapter.VideosViewHolder(view);
        return VideosViewHolder;
    }


    @Override
    public void onBindViewHolder(final PlayerVideosAdapter.VideosViewHolder holder, final int position) {
        dataSaver = getDefaultSharedPreferences(context);

        final String videoPath = media.getMedia().getData().get(position).getPath();

        final String videoDescription = media.getMedia().getData().get(position).getDescription();

        holder.playIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlayerVideoActivity.class);
                intent.putExtra("videoPath", videoPath);
                intent.putExtra("videoDescription", videoDescription);
                Log.e("TAG", "media.getMedia().getApproved() " + media.getMedia().getData().get(position).getApproved());
                if (media.getMedia().getData().get(position).getApproved().equals("0")) {
                    intent.putExtra("videoAcceptation", "هذا الفيديو لم يتم الموافقة عليه بعد");

                }
                context.startActivity(intent);

            }
        });

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

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoId = media.getMedia().getData().get(position).getId();
                deleteVideo();
            }
        });

    }


    public void deleteVideo() {

        token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("هل تريد حذف الفيديو؟").setCancelable(false)
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final ProgressDialog progressDialog = new ProgressDialog(context);
                        progressDialog.setTitle("جاري حذف الفيديو");
                        progressDialog.show();
                        Service.Fetcher.getInstance().deleteVideo(videoId, token).enqueue(new Callback<GeneralResponse>() {
                            @Override
                            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {


                                if (response.isSuccessful()) {
                                    progressDialog.dismiss();
                                    GeneralResponse generalResponse = response.body();
                                    if (generalResponse.getStatus() == 1) {

                                        progressDialog.dismiss();
                                        Intent intent = new Intent(context, PlayerVideosActivity.class);
                                        context.startActivity(intent);
                                        ((Activity) context).finish();
                                        Toast.makeText(context, "تم حذف الفيديو", Toast.LENGTH_SHORT).show();

                                    } else {
                                        progressDialog.dismiss();
                                        String message = "";
                                        for (int i = 0; i < response.body().getMessages().size(); i++) {
                                            message += response.body().getMessages().get(i);
                                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                                Toast.makeText(context, "تحقق من إتصالك بالانترنت", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).setNegativeButton("لا", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return media.getMedia().getData().size();
    }

    public class VideosViewHolder extends RecyclerView.ViewHolder {
        YouTubeThumbnailView youTubeThumbnailView;
        ImageView delete, playIcon;

        public VideosViewHolder(View itemView) {
            super(itemView);

            youTubeThumbnailView = itemView.findViewById(R.id.player_videos);
            playIcon = (ImageView) itemView.findViewById(R.id.play_icon);
            delete = (ImageView) itemView.findViewById(R.id.delete_icon);


        }
    }

}
