package com.eg.typicaldesign.moaheb.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.controllers.AppKeys;
import com.eg.typicaldesign.moaheb.controllers.ExpandAdapter;
import com.eg.typicaldesign.moaheb.controllers.Service;
import com.eg.typicaldesign.moaheb.models.Sport;
import com.eg.typicaldesign.moaheb.models.Sports;

import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ChooseSportActivity extends AppCompatActivity {
    ExpandAdapter expandListAdapter;
    ExpandableListView listView;
    SharedPreferences dataSaver;
    List<Sport> sports;
    boolean isSingle;
    int TYPE = 1;
    Sports sport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sport);
        listView = (ExpandableListView) findViewById(R.id.expand_list_view);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "bein-normal.ttf", true);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        isSingle = dataSaver.getBoolean(AppKeys.IS_SINGLE_KEY, false);
//        Rect rect;
//        Region.Op op;
//        Canvas canvas = null;
//
//        canvas.clipRect(10f, 20f, 30f, 40f);

        if (isSingle == true) {
            TYPE = TYPE + 1;

            listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                    Intent intent = new Intent(ChooseSportActivity.this, RegisterForPlayer.class);
                    startActivity(intent);
                    dataSaver.edit()
                            .putInt(AppKeys.SPORT_ID, sport.getSports().get(i).getId())
                            .commit();
                    dataSaver.edit()
                            .putBoolean(AppKeys.IS_SPONSOR_ACCOUNT, false)
                            .apply();

                    return false;
                }
            });
        }
        getSportsAndPositions();
    }

    public void getSportsAndPositions() {
        Service.Fetcher.getInstance().getSportsAndPositions(TYPE).enqueue(new Callback<Sports>() {
            @Override
            public void onResponse(Call<Sports> call, Response<Sports> response) {
                if (response.isSuccessful()) {
                    sport = response.body();
                    sports = sport.getSports();
                    expandListAdapter = new ExpandAdapter(ChooseSportActivity.this, sports);
                    listView.setAdapter(expandListAdapter);
                    // expandListAdapter.getGroupCount();
                    for (int i = 0; i < 1; i++) {
                        listView.expandGroup(i);
                    }


                    listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                            Intent intent = new Intent(ChooseSportActivity.this, RegisterForPlayer.class);
                            startActivity(intent);

                            dataSaver.edit()
                                    .putInt(AppKeys.SPORT_ID, sport.getSports().get(i).getId())
                                    .commit();

                            dataSaver.edit()
                                    .putInt(AppKeys.POSITION_ID, sport.getSports().get(i).getPositions().get(i1).getId())
                                    .commit();

                            dataSaver.edit()
                                    .putBoolean(AppKeys.IS_SPONSOR_ACCOUNT, false)
                                    .apply();
                            return false;
                        }
                    });
                } else {

                    Toast.makeText(ChooseSportActivity.this, AppKeys.SOME_THING_WENT_WRONG_KEY, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Sports> call, Throwable t) {

                Toast.makeText(ChooseSportActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
