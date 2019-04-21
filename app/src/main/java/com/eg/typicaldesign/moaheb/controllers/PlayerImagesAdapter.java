package com.eg.typicaldesign.moaheb.controllers;

/**
 * Created by MAGIC on 5/6/2018.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.models.GeneralResponse;
import com.eg.typicaldesign.moaheb.models.Media;
import com.eg.typicaldesign.moaheb.views.PlayerImagesActivity;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


public class PlayerImagesAdapter extends RecyclerView.Adapter<PlayerImagesAdapter.ImageViewHolder> {
    Context context;
    Media media;
    int imageId;
    String token;
    SharedPreferences dataSaver;


    public PlayerImagesAdapter(Context context, Media media) {
        this.context = context;
        this.media = media;
    }

    @Override
    public PlayerImagesAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_images, parent, false);
        PlayerImagesAdapter.ImageViewHolder ImageViewHolder = new PlayerImagesAdapter.ImageViewHolder(view);
        return ImageViewHolder;
    }

    @Override
    public void onBindViewHolder(final PlayerImagesAdapter.ImageViewHolder holder, final int position) {
        dataSaver = getDefaultSharedPreferences(context);
        Picasso.with(context).load(String.valueOf(media.getMedia().getData().get(position).getPath())).into(holder.playerImages);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageId = media.getMedia().getData().get(position).getId();
                deleteImage();
            }
        });
    }

    public void deleteImage() {

        token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("هل تريد حذف الصورة؟").setCancelable(false)
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final ProgressDialog progressDialog = new ProgressDialog(context);
                        progressDialog.setTitle("جاري حذف الصورة");
                        progressDialog.show();
                        Service.Fetcher.getInstance().deleteImage(imageId, token).enqueue(new Callback<GeneralResponse>() {
                            @Override
                            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {


                                if (response.isSuccessful()) {
                                    progressDialog.dismiss();
                                    GeneralResponse generalResponse = response.body();
                                    if (generalResponse.getStatus()==1) {

                                        progressDialog.dismiss();
                                        Intent intent = new Intent(context, PlayerImagesActivity.class);
                                        context.startActivity(intent);
                                        ((Activity) context).finish();
                                        Toast.makeText(context, "تم حذف الصوره", Toast.LENGTH_SHORT).show();

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

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView playerImages, delete;


        public ImageViewHolder(View itemView) {
            super(itemView);

            playerImages = (ImageView) itemView.findViewById(R.id.player_images);
            delete = (ImageView) itemView.findViewById(R.id.delete_icon);

        }
    }

}
