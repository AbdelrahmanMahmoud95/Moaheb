package com.eg.typicaldesign.moaheb.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.models.GeneralResponse;
import com.eg.typicaldesign.moaheb.models.UserDocs;
import com.eg.typicaldesign.moaheb.views.PlayerDocumentsActivity;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by MAGIC on 5/14/2018.
 */

public class PlayerDocsAdapter extends RecyclerView.Adapter<PlayerDocsAdapter.ImageViewHolder> {

    Context context;
    UserDocs userDocs;
    int imageId;
    String token;
    SharedPreferences dataSaver;


    public PlayerDocsAdapter(Context context, UserDocs userDocs) {
        this.context = context;
        this.userDocs = userDocs;
    }

    @Override
    public PlayerDocsAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_docs, parent, false);
        PlayerDocsAdapter.ImageViewHolder ImageViewHolder = new PlayerDocsAdapter.ImageViewHolder(view);
        return ImageViewHolder;
    }

    @Override
    public void onBindViewHolder(final PlayerDocsAdapter.ImageViewHolder holder, final int position) {
        dataSaver = getDefaultSharedPreferences(context);
        Picasso.with(context).load(String.valueOf(userDocs.getIdentities().get(position).getIdentityImage())).into(holder.playerDocs);
        holder.docsName.setText(userDocs.getIdentities().get(position).getIdentityName());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageId = userDocs.getIdentities().get(position).getId();
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
                        Service.Fetcher.getInstance().deleteDocs(imageId, token).enqueue(new Callback<GeneralResponse>() {
                            @Override
                            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {


                                if (response.isSuccessful()) {
                                    Log.e("TAG","isSuccessful");
                                    progressDialog.dismiss();
                                    GeneralResponse generalResponse = response.body();
                                    if (generalResponse.getStatus() == 1) {

                                        progressDialog.dismiss();
                                        Intent intent = new Intent(context, PlayerDocumentsActivity.class);
                                        context.startActivity(intent);
                                        ((Activity) context).finish();
                                        Toast.makeText(context, "تم حذف الصورة", Toast.LENGTH_SHORT).show();

                                    } else {
                                        progressDialog.dismiss();
                                        String message = "";
                                        for (int i = 0; i < response.body().getMessages().size(); i++) {
                                            message += response.body().getMessages().get(i);
                                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                                        }
                                    }
                                }
                                Log.e("TAG","isNotSuccessful");
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
        return userDocs.getIdentities().size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView playerDocs, delete;
        TextView docsName;

        public ImageViewHolder(View itemView) {
            super(itemView);

            playerDocs = (ImageView) itemView.findViewById(R.id.player_docs);
            delete = (ImageView) itemView.findViewById(R.id.delete_icon);
            docsName = (TextView) itemView.findViewById(R.id.name_docs);


        }
    }


}
