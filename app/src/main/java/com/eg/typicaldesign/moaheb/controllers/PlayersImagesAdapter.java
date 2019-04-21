package com.eg.typicaldesign.moaheb.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.models.playerProfile;
import com.eg.typicaldesign.moaheb.views.PlayerImageActivity;
import com.squareup.picasso.Picasso;

import static android.media.CamcorderProfile.get;

/**
 * Created by MAGIC on 4/24/2018.
 */

public class PlayersImagesAdapter extends RecyclerView.Adapter<PlayersImagesAdapter.ImagesViewHolder> {
    Context context;
    playerProfile profile;


    public PlayersImagesAdapter(Context context, playerProfile profile) {
        this.context = context;
        this.profile = profile;
    }

    @Override
    public PlayersImagesAdapter.ImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.players_images, parent, false);
        PlayersImagesAdapter.ImagesViewHolder ImagesViewHolder = new PlayersImagesAdapter.ImagesViewHolder(view);
        return ImagesViewHolder;
    }

    @Override
    public void onBindViewHolder(final PlayersImagesAdapter.ImagesViewHolder holder, final int position) {

        Picasso.with(context).load(String.valueOf(profile.getUser().getApprovedImages().get(position).getPath())).into(holder.playerImages);

        holder.playerImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageId = (profile.getUser().getApprovedImages().get(position).getPath());
                Intent intent = new Intent(context, PlayerImageActivity.class);
                intent.putExtra("imagePath", imageId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return profile.getUser().getApprovedImages().size();
    }

    public class ImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView playerImages;


        public ImagesViewHolder(View itemView) {
            super(itemView);

            playerImages = (ImageView) itemView.findViewById(R.id.player_images);

        }
    }

}
