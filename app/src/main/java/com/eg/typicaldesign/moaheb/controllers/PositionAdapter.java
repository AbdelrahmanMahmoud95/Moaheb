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
import com.eg.typicaldesign.moaheb.models.Position;

import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by MAGIC on 4/11/2018.
 */

public class PositionAdapter extends ArrayAdapter {
    private Context mContext;
    private int resourceID;
    private List<Position> positions;
    private SharedPreferences dataSaver;
    private ChooseDialog holder;

    public PositionAdapter(@NonNull Context context, int resource, @NonNull List<Position> positions, ChooseDialog holder) {
        super(context, resource);
        this.mContext = context;
        this.resourceID = resource;
        this.positions = positions;
        this.holder = holder;
    }

    @Override
    public int getCount() {
        return positions.size();
    }

    @Override
    public Position getItem(int position) {
        return positions.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        row = inflater.inflate(resourceID, parent, false);
        TextView name = (TextView) row.findViewById(R.id.name);
        name.setText(positions.get(position).getName());
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = mContext.getApplicationContext();
                dataSaver = getDefaultSharedPreferences(context);

                dataSaver.edit()
                        .putString(AppKeys.POSITION_NAME, positions.get(position).getName())
                        .commit();
                dataSaver.edit()
                        .putInt(AppKeys.POSITION_ID, positions.get(position).getId())
                        .commit();

                holder.dismiss();
            }
        });
        return row;
    }
}

