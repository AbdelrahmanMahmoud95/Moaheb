package com.eg.typicaldesign.moaheb.controllers;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.models.Datum;
import com.eg.typicaldesign.moaheb.models.Result;
import com.eg.typicaldesign.moaheb.views.PlayerDetailsActivity;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by MAGIC on 4/18/2018.
 */

public class SportsContainerAdapter extends RecyclerView.Adapter<SportsContainerAdapter.SportsViewHolder> {
    Context context;
    Result resultList;
    SharedPreferences dataSaver;

    public SportsContainerAdapter(Context context, Result resultList) {
        this.context = context;
        this.resultList = resultList;
    }


    public void clearApplications() {
        int size = this.resultList.getData().size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                resultList.getData().remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public SportsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sports_container, parent, false);
        SportsViewHolder SportstViewHolder = new SportsViewHolder(view);
        return SportstViewHolder;
    }

    @Override
    public void onBindViewHolder(final SportsViewHolder holder, int position) {
        final Datum playerData = resultList.getData().get(position);
        dataSaver = getDefaultSharedPreferences(context);
        holder.sport.setText(playerData.getSport().getName());
        if (playerData.getSportType().equals("1")) {
            holder.positions.setText(playerData.getPosition().getName());
        } else {
            holder.positions.setVisibility(View.GONE);
            holder.positionText.setVisibility(View.GONE);
        }
        if (playerData.getGov().getName()!=null){
            holder.governorate.setText(playerData.getGov().getName());

        }
        else{
            holder.governorate.setText(" ");

        }
        String playerDate = String.valueOf(playerData.getYears().getY());
        holder.playerDate.setText(playerDate + " " + "سنة");
        holder.imageNumber.setText(playerData.getApprovedImagesCount());
        holder.videosNumber.setText(playerData.getApprovedVideosCount());
        String name = playerData.getName();
        String[] split = name.split(" ");
        if (split.length >= 2) {
            holder.playerName.setText(split[0] + " " + split[1]);
        } else {
            holder.playerName.setText(split[0]);
        }
        holder.playerRate.setRating((float) playerData.getRank());

        final SpannableString content = new SpannableString("أعرف المزيد عن اللاعب");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        holder.details.setText(content);
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userId = Integer.parseInt(String.valueOf(playerData.getId()));
                Intent intent = new Intent(context, PlayerDetailsActivity.class);
                intent.putExtra("user_id", userId);
                context.startActivity(intent);
                YoYo.with(Techniques.RollOut)
                        .duration(500)
                        .repeat(2)
                        .playOn(holder.playerImage);

            }
        });

        holder.playerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userId = Integer.parseInt(String.valueOf(playerData.getId()));
                Intent intent = new Intent(context, PlayerDetailsActivity.class);
                intent.putExtra("user_id", userId);
                context.startActivity(intent);

            }
        });


        if (playerData.getClubName() == null) {

            holder.club.setText("غير مشترك");

        } else {
            holder.club.setText(playerData.getClubName().toString());

        }

        Picasso.with(context).load(String.valueOf(playerData.getImage())).into(holder.playerImage);
    }

    @Override
    public int getItemCount() {
        return resultList.getData().size();
    }

    public class SportsViewHolder extends RecyclerView.ViewHolder {
        ImageView playerImage;
        TextView playerName, playerDate, imageNumber, videosNumber, governorate, club, positions, sport, positionText, details;
        SimpleRatingBar playerRate;

        public SportsViewHolder(View itemView) {
            super(itemView);
            playerName = (TextView) itemView.findViewById(R.id.player_name);
            playerDate = (TextView) itemView.findViewById(R.id.player_date);
            imageNumber = (TextView) itemView.findViewById(R.id.images);
            videosNumber = (TextView) itemView.findViewById(R.id.videos);
            governorate = (TextView) itemView.findViewById(R.id.gover);
            positions = (TextView) itemView.findViewById(R.id.position);
            club = (TextView) itemView.findViewById(R.id.club);
            sport = (TextView) itemView.findViewById(R.id.sport);
            positionText = (TextView) itemView.findViewById(R.id.textPosition);
            details = (TextView) itemView.findViewById(R.id.details);
            playerImage = (ImageView) itemView.findViewById(R.id.player_image);
            playerRate = (SimpleRatingBar) itemView.findViewById(R.id.player_rating);

        }
    }

    public void addInRecycle(List<Datum> datum) {
        for (Datum dt : datum) {
            resultList.getData().add(dt);
        }
        notifyDataSetChanged();
    }
}
