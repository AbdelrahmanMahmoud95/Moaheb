package com.eg.typicaldesign.moaheb.controllers;

/**
 * Created by MAGIC on 4/16/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.models.Position;
import com.eg.typicaldesign.moaheb.models.Sport;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


public class ExpandAdapter extends BaseExpandableListAdapter {
    private Context mcontext;
    private List<Sport> sports;
    private List<String> list;
    private List<Position> positions;
    private HashMap<String, List<Sport>> hashMap;
    SharedPreferences dataSaver;


    public ExpandAdapter(Context context, List<Sport> sports) {
        this.mcontext = context;
        this.sports = sports;
        this.hashMap = hashMap;


    }

    @Override
    public int getGroupCount() {
        Log.e("TAg", "getGroupCount ");
        return sports.size();
    }

    @Override
    public int getChildrenCount(int i) {
        Log.e("TAg", "getChildrenCount ");
        return sports.get(i).getPositions().size();
    }

    @Override
    public Object getGroup(int i) {
        return sports.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {

        Log.e("TAg", "getChildrenCount1 ");
        return sports.get(i).getPositions().get(i1); // i = Group Item , i1 = ChildItem
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        Log.e("TAg", "getGroupView ");
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.sport_title, null);
        }
        TextView name = (TextView) view.findViewById(R.id.lblListHeader);
        name.setTypeface(null, Typeface.BOLD);
        name.setText(sports.get(i).getName());
        Log.e("TAG", "sport.getName() " + sports.get(i).getName());
        dataSaver = getDefaultSharedPreferences(mcontext);
        boolean isSingle;
        isSingle = dataSaver.getBoolean(AppKeys.IS_SINGLE_KEY, false);
        ImageView leftArrow = (ImageView) view.findViewById(R.id.left_arrow1);
        ImageView sportIcon = (ImageView) view.findViewById(R.id.sport_icon);
        View separator = (View) view.findViewById(R.id.separator);
        Picasso.with(mcontext).load(String.valueOf(sports.get(i).getImage())).into(sportIcon);

        LinearLayout linearSportTitle = (LinearLayout) view.findViewById(R.id.linear_sport_title);

        if (isSingle == true) {

            leftArrow.setImageResource(R.drawable.leftarrow);
            leftArrow.setVisibility(View.VISIBLE);
            separator.setVisibility(View.VISIBLE);
            Picasso.with(mcontext).load(sports.get(i).getImage()).into(sportIcon);
            Log.e("sports.getImage()", sports.get(i).getImage());
            name.setTextColor(Color.parseColor("#530707"));
            linearSportTitle.setBackgroundResource(R.drawable.round_rect);

            ViewGroup.LayoutParams size = linearSportTitle.getLayoutParams();
            size.height = 150;
            // size.width = 450;
            linearSportTitle.setLayoutParams(size);

        }

        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {

//        final String position = (String) getChild(i, i1);
        Log.e("TAg", "getChildView()1");
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_sport, null);
        }
        Log.e("TAg", "getChildView()2");
        TextView name = (TextView) view.findViewById(R.id.lblListItem);
        name.setText(sports.get(i).getPositions().get(i1).getName());
        ImageView leftArrow = (ImageView) view.findViewById(R.id.left_arrow);
        leftArrow.setImageResource(R.drawable.leftarrow);


        // Picasso.with(mcontext).load("").placeholder(R.drawable.left_arrow).into(leftArrow);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}