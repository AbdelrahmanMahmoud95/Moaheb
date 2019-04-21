package com.eg.typicaldesign.moaheb;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eg.typicaldesign.moaheb.controllers.AppKeys;
import com.eg.typicaldesign.moaheb.controllers.CitiesAdapter;
import com.eg.typicaldesign.moaheb.controllers.GovernorateAdapter;
import com.eg.typicaldesign.moaheb.controllers.PositionAdapter;
import com.eg.typicaldesign.moaheb.controllers.SportsAdapter;
import com.eg.typicaldesign.moaheb.models.City;
import com.eg.typicaldesign.moaheb.models.Governorate;
import com.eg.typicaldesign.moaheb.models.Position;
import com.eg.typicaldesign.moaheb.models.Sport;

import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by MAGIC on 4/8/2018.
 */

public class ChooseDialog extends Dialog {
    Context context;
    ListView listView;
    TextView loadingTxt;
    ProgressBar progressBar;
    TextView title;
    TextView terms;
    String titleToSet;
    Button termsBtn;
    int i;

    SharedPreferences dataSaver;

    public ChooseDialog(@NonNull Context context, String titleToSet) {
        super(context);
        this.context = context;
        this.titleToSet = titleToSet;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_dialog);

        listView = (ListView) findViewById(R.id.city_or_area_names);
        loadingTxt = (TextView) findViewById(R.id.loading_txt);
        progressBar = (ProgressBar) findViewById(R.id.loading_photo);
        terms = (TextView) findViewById(R.id.terms_text);
        termsBtn = (Button) findViewById(R.id.term_btn);
        title = (TextView) findViewById(R.id.dialog_title);
        title.setText(titleToSet);

        loadingTxt.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void showSportsList(List<Sport> list) {
        loadingTxt.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        final ArrayAdapter adapter = new SportsAdapter(context, R.layout.basic_view, list, this);
        listView.setAdapter(adapter);
    }

    public void showCitiesList(List<City> list) {
        loadingTxt.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        final ArrayAdapter adapter = new CitiesAdapter(context, R.layout.basic_view, list, this);
        listView.setAdapter(adapter);

    }

    public void showGovernoratesList(List<Governorate> list) {
        loadingTxt.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        final ArrayAdapter adapter = new GovernorateAdapter(context, R.layout.basic_view, list, this);
        listView.setAdapter(adapter);


    }

    public void showPositionList(List<Position> list) {
        loadingTxt.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        final ArrayAdapter adapter = new PositionAdapter(context, R.layout.basic_view, list, this);
        listView.setAdapter(adapter);
    }

    public void showTermsList(final String text) {
        loadingTxt.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        termsBtn.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        terms.setText(text);
        termsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSaver = getDefaultSharedPreferences(getContext());
                dataSaver.edit()
                        .putBoolean(AppKeys.IS_TERMS_READ, true)
                        .apply();

                dismiss();

            }
        });

    }

}
