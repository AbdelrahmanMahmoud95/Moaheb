package com.eg.typicaldesign.moaheb.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eg.typicaldesign.moaheb.ChooseDialog;
import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.models.Sport;

import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by MAGIC on 4/8/2018.
 */

public class SportsAdapter extends ArrayAdapter {
    private Context mContext;
    private int resourceID;
    private List<Sport> sports;
    private SharedPreferences dataSaver;
    private ChooseDialog holder;

    public SportsAdapter(@NonNull Context context, int resource, @NonNull List<Sport> sports, ChooseDialog holder) {
        super(context, resource);
        this.mContext = context;
        this.resourceID = resource;
        this.sports = sports;
        this.holder = holder;
    }


    @Override
    public Sport getItem(int position) {
        return sports.get(position);
    }

    @Override
    public int getCount() {
        return sports.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        row = inflater.inflate(resourceID, parent, false);
        TextView name = (TextView) row.findViewById(R.id.name);
        name.setText(sports.get(position).getName());
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = mContext.getApplicationContext();
                dataSaver = getDefaultSharedPreferences(context);

                dataSaver.edit()
                        .putString(AppKeys.SPORT_NAME, sports.get(position).getName())
                        .commit();
                dataSaver.edit()
                        .putInt(AppKeys.SPORT_ID, sports.get(position).getId())
                        .commit();

                holder.dismiss();
            }
        });
        return row;
    }

}
