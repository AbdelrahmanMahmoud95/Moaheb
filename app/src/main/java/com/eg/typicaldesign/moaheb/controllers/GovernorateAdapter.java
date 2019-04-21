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
import com.eg.typicaldesign.moaheb.models.Governorate;

import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by MAGIC on 4/11/2018.
 */

public class GovernorateAdapter extends ArrayAdapter {
    private Context mContext;
    private int resourceID;
    private List<Governorate> governorates;
    private SharedPreferences dataSaver;
    private ChooseDialog holder;

    public GovernorateAdapter(@NonNull Context context, int resource, @NonNull List<Governorate> governorates, ChooseDialog holder) {
        super(context, resource);
        this.mContext = context;
        this.resourceID = resource;
        this.governorates = governorates;
        this.holder = holder;
    }

    @Override
    public int getCount() {
        return governorates.size();
    }

    @Override
    public Governorate getItem(int position) {
        return governorates.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        row = inflater.inflate(resourceID, parent, false);
        TextView name = (TextView) row.findViewById(R.id.name);
        name.setText(governorates.get(position).getName());
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = mContext.getApplicationContext();
                dataSaver = getDefaultSharedPreferences(context);

                dataSaver.edit()
                        .putString(AppKeys.GOVERNORATE_NAME, governorates.get(position).getName())
                        .commit();
                dataSaver.edit()
                        .putInt(AppKeys.GOVERNORATE_ID, governorates.get(position).getId())
                        .commit();

                holder.dismiss();
            }
        });
        return row;
    }
}

