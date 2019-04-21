package com.eg.typicaldesign.moaheb.views;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eg.typicaldesign.moaheb.ChooseDialog;
import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.controllers.AppKeys;
import com.eg.typicaldesign.moaheb.controllers.Service;
import com.eg.typicaldesign.moaheb.controllers.SportsContainerAdapter;
import com.eg.typicaldesign.moaheb.models.Governorates;
import com.eg.typicaldesign.moaheb.models.Positions;
import com.eg.typicaldesign.moaheb.models.Result;
import com.eg.typicaldesign.moaheb.models.Search;
import com.eg.typicaldesign.moaheb.models.Sports;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by MAGIC on 5/15/2018.
 */

public class TeamSearchFragment extends Fragment implements View.OnClickListener, DialogInterface.OnDismissListener {
    //19578
    LinearLayout sportsLinear, positionLinear, ageLinear, govLinear, contentLinear, sort_linear;
    SharedPreferences dataSaver;
    int sportId = -1;
    int position_id = -1;
    int gov_id = -1;
    int single1;
    String to = "-1";
    String from = "-1";
    String sortBy = "-1";
    String mostViewed = "-1";
    String mostReviewed = "-1";
    String sorting = "asc";

    TextView sportName, positionName, selectGovernorate;
    CheckBox joined, hasVideo, hasPhotos, hasChampion, single;
    EditText ageFrom, ageTo;
    Button search;
    RecyclerView recyclerTeam, recyclerSort, recyclerAsc, recyclerDesc;
    Result result;
    Button sortbtn;
    ScrollView scrollSearch, scrollSort, scrollAsc, scrollDes;
    ImageView ascArrow, descArrow;
    StaggeredGridLayoutManager categoriesLayoutManager;
    StaggeredGridLayoutManager categoriesLayoutManager1;
    StaggeredGridLayoutManager categoriesLayoutManager2;
    StaggeredGridLayoutManager categoriesLayoutManager3;
    SportsContainerAdapter containerAdapter;
    String sportname, positionname, governorateName;
    boolean isSportDialog = false, isGovernorateDialog = false,
            isPositionDialog = false;
    String hasV = "-1", hasI = "-1";
    int champion = -1, joinType = -1;
    int sportType = 1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team_search_fragment, container, false);
        sportsLinear = (LinearLayout) view.findViewById(R.id.linear_sport);
        positionLinear = (LinearLayout) view.findViewById(R.id.linear_position);
        contentLinear = (LinearLayout) view.findViewById(R.id.contentpain);
        ageLinear = (LinearLayout) view.findViewById(R.id.linear_age);
        govLinear = (LinearLayout) view.findViewById(R.id.linear_Governorate);
        sort_linear = (LinearLayout) view.findViewById(R.id.sort_linear);
        sportName = (TextView) view.findViewById(R.id.sport_name);
        selectGovernorate = (TextView) view.findViewById(R.id.select_Governorate);
        positionName = (TextView) view.findViewById(R.id.position_name);
        ageFrom = (EditText) view.findViewById(R.id.age_from);
        ageTo = (EditText) view.findViewById(R.id.age_to);
        search = (Button) view.findViewById(R.id.search);
        joined = (CheckBox) view.findViewById(R.id.join);
        hasChampion = (CheckBox) view.findViewById(R.id.has_champion);
        single = (CheckBox) view.findViewById(R.id.single);
        hasPhotos = (CheckBox) view.findViewById(R.id.has_photos);
        hasVideo = (CheckBox) view.findViewById(R.id.has_video);
        sortbtn = (Button) view.findViewById(R.id.sort);
        ascArrow = (ImageView) view.findViewById(R.id.asc);
        descArrow = (ImageView) view.findViewById(R.id.desc);
        scrollSearch = (ScrollView) view.findViewById(R.id.scrol_search);
        scrollSort = (ScrollView) view.findViewById(R.id.scrol_sort);
        scrollAsc = (ScrollView) view.findViewById(R.id.scrol_sort_asc);
        scrollDes = (ScrollView) view.findViewById(R.id.scrol_sort_desc);

        recyclerTeam = (RecyclerView) view.findViewById(R.id.res_search);
        recyclerSort = (RecyclerView) view.findViewById(R.id.res_sort);
        recyclerDesc = (RecyclerView) view.findViewById(R.id.res_sort_desc);
        recyclerAsc = (RecyclerView) view.findViewById(R.id.res_sort_asc);

        categoriesLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerSort.setLayoutManager(categoriesLayoutManager);
        recyclerSort.setHasFixedSize(true);


        categoriesLayoutManager1 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerTeam.setLayoutManager(categoriesLayoutManager1);
        recyclerTeam.setHasFixedSize(true);

        categoriesLayoutManager2 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerDesc.setLayoutManager(categoriesLayoutManager2);
        recyclerDesc.setHasFixedSize(true);

        categoriesLayoutManager3 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerAsc.setLayoutManager(categoriesLayoutManager3);
        recyclerAsc.setHasFixedSize(true);

        dataSaver = getDefaultSharedPreferences(getActivity());
        Calligrapher calligrapher = new Calligrapher(getActivity());
        calligrapher.setFont(getActivity(), "bein-normal.ttf", true);
        sportsLinear.setOnClickListener(this);
        positionLinear.setOnClickListener(this);
        ageLinear.setOnClickListener(this);
        govLinear.setOnClickListener(this);
        search.setOnClickListener(this);
        sortbtn.setOnClickListener(this);
        ascArrow.setOnClickListener(this);
        descArrow.setOnClickListener(this);


        joined.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                              @Override
                                              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                  if (buttonView.isChecked()) {
                                                      joinType = 1;
                                                  } else {
                                                      joinType = -1;
                                                  }
                                              }
                                          }
        );

        single.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    single1 = 1;
                }
            }
        });
        hasVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    if (buttonView.isChecked()) {
                                                        hasV = "true";
                                                    } else {
                                                        hasV = "-1";
                                                    }
                                                }
                                            }
        );
        hasPhotos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                 @Override
                                                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                     if (buttonView.isChecked()) {
                                                         hasI = "true";
                                                     } else {
                                                         hasI = "-1";
                                                     }
                                                 }
                                             }
        );
        hasChampion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                   @Override
                                                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                       if (buttonView.isChecked()) {
                                                           champion = 1;
                                                       } else {
                                                           champion = -1;
                                                       }
                                                   }
                                               }
        );
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == sportsLinear) {
            dataSaver.edit()
                    .putInt(AppKeys.SPORT_ID, -1)
                    .commit();

            sportname = "";
            sportId = -1;
            isSportDialog = true;
            final ChooseDialog dialog = new ChooseDialog(getActivity(), "اختر الرياضة");
            dialog.setOnDismissListener(this);
            dialog.show();
            Service.Fetcher.getInstance().getSports(sportType).enqueue(new Callback<Sports>() {
                @Override
                public void onResponse(Call<Sports> call, Response<Sports> response) {
                    if (response.isSuccessful()) {

                        dialog.showSportsList(response.body().getSports());

                    } else {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), AppKeys.SOME_THING_WENT_WRONG_KEY, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Sports> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (view == positionLinear) {
            dataSaver.edit()
                    .putInt(AppKeys.POSITION_ID, -1)
                    .commit();
            positionname = "";
            position_id = -1;
            isPositionDialog = true;

            if (sportId != -1) {

                final ChooseDialog dialog = new ChooseDialog(getActivity(), "اختر المركز");
                dialog.setOnDismissListener(this);
                dialog.show();
                Service.Fetcher.getInstance().getPosition(sportId).enqueue(new Callback<Positions>() {
                    @Override
                    public void onResponse(Call<Positions> call, Response<Positions> response) {
                        if (response.isSuccessful()) {

                            dialog.showPositionList(response.body().getPositions());

                        } else {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), AppKeys.SOME_THING_WENT_WRONG_KEY, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Positions> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getActivity(), "اختر الرياضة اولا", Toast.LENGTH_SHORT).show();
            }
        }
        if (view == govLinear) {


            dataSaver.edit()
                    .putInt(AppKeys.GOVERNORATE_ID, -1)
                    .commit();
            governorateName = "";
            gov_id = -1;
            isGovernorateDialog = true;
            final ChooseDialog dialog = new ChooseDialog(getActivity(), "اختر المحافظة");
            dialog.setOnDismissListener(this);
            dialog.show();
            Service.Fetcher.getInstance().getGovernorate().enqueue(new Callback<Governorates>() {
                @Override
                public void onResponse(Call<Governorates> call, Response<Governorates> response) {
                    if (response.isSuccessful()) {

                        dialog.showGovernoratesList(response.body().getGovernorates());

                    } else {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), AppKeys.SOME_THING_WENT_WRONG_KEY, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Governorates> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (view == sortbtn) {
            PopupMenu popup = new PopupMenu(getActivity(), sortbtn);

            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.age) {
                        sortBy = "birth_date";
                        mostViewed = "-1";
                        mostReviewed = "-1";
                        sortbtn.setText("السن");
                        sort();
                        return true;
                    } else if (item.getItemId() == R.id.most_viewed) {
                        mostViewed = "true";
                        sortBy = "-1";
                        mostReviewed = "-1";
                        sortbtn.setText("اعلى مشاهدة");
                        sort();
                        return true;
                    } else if (item.getItemId() == R.id.most_reviewed) {
                        mostReviewed = "true";
                        mostViewed = "-1";
                        sortBy = "-1";
                        sortbtn.setText("اعلى تقيم");
                        sort();
                        return true;
                    }
                    return false;
                }
            });
            popup.show();

        }


        if (view == search) {

            to = ageTo.getText().toString();
            from = ageFrom.getText().toString();

            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("جاري التحميل...");
            progressDialog.show();
            Service.Fetcher.getInstance().search(sportId, position_id, gov_id, sportType, champion, joinType, from, to, hasV, hasI, single1).enqueue(new Callback<Search>() {
                @Override
                public void onResponse(Call<Search> call, Response<Search> response) {

                    if (response.isSuccessful()) {

                        sort_linear.setVisibility(View.VISIBLE);
                        Log.e("search", "isSuccessful");
                        Log.e("search", "sportId " + sportId);
                        Log.e("search", "position_id " + position_id);
                        Log.e("search", "gov_id " + gov_id);
                        Log.e("search", "sportType " + sportType);
                        Log.e("search", "champion " + champion);
                        Log.e("search", "from " + from);
                        Log.e("search", "to " + to);
                        Log.e("search", "hasV " + hasV);
                        Log.e("search", "hasI " + hasI);
                        Log.e("search", "mostViewed " + mostViewed);
                        Log.e("search", "mostReviewed " + mostReviewed);
                        progressDialog.dismiss();
                        Search search = response.body();
                        result = search.getResult();
                        scrollSort.setVisibility(View.GONE);
                        scrollAsc.setVisibility(View.GONE);
                        scrollDes.setVisibility(View.GONE);
                        scrollSearch.setVisibility(View.VISIBLE);
                        contentLinear.setVisibility(View.GONE);
                        containerAdapter = new SportsContainerAdapter(getActivity(), result);
                        containerAdapter.notifyDataSetChanged();
                        recyclerTeam.setAdapter(containerAdapter);
                    } else {
                        Log.e("TAG3", "isNotSuccessful");
                    }
                }

                @Override
                public void onFailure(Call<Search> call, Throwable t) {

                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });
        }
        if (view == ascArrow) {

            if (sortbtn.getText().equals("اختر")) {
                Toast.makeText(getActivity(), "اختر نوع الترتيب اولا", Toast.LENGTH_LONG).show();
            } else {

                to = ageTo.getText().toString();
                from = ageFrom.getText().toString();
                sorting = "asc";
                ascArrow.setImageResource(R.drawable.arrow_yellow_up);
                descArrow.setImageResource(R.drawable.down_arrow);
                // sort();
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("جاري التحميل...");
                progressDialog.show();
                Service.Fetcher.getInstance().sort(sportId, position_id, gov_id, sportType, champion, joinType, from, to, hasV, hasI, sortBy, sorting, mostViewed, mostReviewed).enqueue(new Callback<Search>() {
                    @Override
                    public void onResponse(Call<Search> call, Response<Search> response) {

                        if (response.isSuccessful()) {

                            sort_linear.setVisibility(View.VISIBLE);
                            Log.e("ascArrow", "isSuccessful");
                            Log.e("ascArrow", "sortBy " + sortBy);
                            Log.e("ascArrow", "sorting " + sorting);
                            Log.e("ascArrow", "position_id " + position_id);
                            Log.e("ascArrow", "gov_id " + gov_id);
                            Log.e("ascArrow", "sportId " + sportId);
                            Log.e("ascArrow", "sportType " + sportType);
                            Log.e("ascArrow", "champion " + champion);
                            Log.e("ascArrow", "from " + from);
                            Log.e("ascArrow", "to " + to);
                            Log.e("ascArrow", "hasV " + hasV);
                            Log.e("ascArrow", "hasI " + hasI);
                            Log.e("ascArrow", "mostViewed " + mostViewed);
                            Log.e("ascArrow", "mostReviewed " + mostReviewed);
                            progressDialog.dismiss();
                            Search search = response.body();
                            result = search.getResult();

                            scrollSearch.setVisibility(View.GONE);
                            scrollSort.setVisibility(View.GONE);
                            scrollDes.setVisibility(View.GONE);
                            scrollAsc.setVisibility(View.VISIBLE);
                            contentLinear.setVisibility(View.GONE);
                            containerAdapter.clearApplications();
                            //  containerAdapter.swap(result);
                            containerAdapter = new SportsContainerAdapter(getActivity(), result);
                            containerAdapter.notifyDataSetChanged();
                            recyclerAsc.setAdapter(containerAdapter);
                        } else {
                            Log.e("ascArrow", "isNotSuccessful");
                        }
                    }

                    @Override
                    public void onFailure(Call<Search> call, Throwable t) {

                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
            }
        }
        if (view == descArrow) {
            if (sortbtn.getText().equals("اختر")) {
                Toast.makeText(getActivity(), "اختر نوع الترتيب اولا", Toast.LENGTH_LONG).show();
            } else {
                to = ageTo.getText().toString();
                from = ageFrom.getText().toString();

                sorting = "desc";
                descArrow.setImageResource(R.drawable.arrow_yellow_down);
                ascArrow.setImageResource(R.drawable.up_arrow);
                //  sort();
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("جاري التحميل...");
                progressDialog.show();
                Service.Fetcher.getInstance().sort(sportId, position_id, gov_id, sportType, champion, joinType, from, to, hasV, hasI, sortBy, sorting, mostViewed, mostReviewed).enqueue(new Callback<Search>() {
                    @Override
                    public void onResponse(Call<Search> call, Response<Search> response) {

                        if (response.isSuccessful()) {
                            Log.e("descArrow", "isSuccessful");
                            Log.e("descArrow", "sortBy " + sortBy);
                            Log.e("descArrow", "sorting " + sorting);
                            Log.e("descArrow", "sportId " + sportId);
                            Log.e("descArrow", "mostViewed " + mostViewed);
                            Log.e("descArrow", "mostReviewed " + mostReviewed);
                            progressDialog.dismiss();
                            Search search = response.body();
                            result = search.getResult();
                            scrollSearch.setVisibility(View.GONE);
                            scrollSort.setVisibility(View.GONE);
                            scrollAsc.setVisibility(View.GONE);
                            contentLinear.setVisibility(View.GONE);
                            scrollDes.setVisibility(View.VISIBLE);
                            containerAdapter.clearApplications();
                            containerAdapter = new SportsContainerAdapter(getActivity(), result);
                            containerAdapter.notifyDataSetChanged();
                            recyclerDesc.setAdapter(containerAdapter);
                        } else {
                            Log.e("descArrow", "isNotSuccessful");
                        }
                    }

                    @Override
                    public void onFailure(Call<Search> call, Throwable t) {

                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
            }
        }

    }

    public void sort() {
        to = ageTo.getText().toString();
        from = ageFrom.getText().toString();
        ascArrow.setImageResource(R.drawable.arrow_yellow_up);
        descArrow.setImageResource(R.drawable.down_arrow);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Service.Fetcher.getInstance().sort(sportId, position_id, gov_id, sportType, champion, joinType, from, to, hasV, hasI, sortBy, sorting, mostViewed, mostReviewed).enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {

                if (response.isSuccessful()) {

                    sort_linear.setVisibility(View.VISIBLE);
                    Log.e("ascArrow", "isSuccessful");
                    Log.e("ascArrow", "sortBy " + sortBy);
                    Log.e("ascArrow", "sorting " + sorting);
                    Log.e("ascArrow", "position_id " + position_id);
                    Log.e("ascArrow", "sportId " + sportId);
                    Log.e("ascArrow", "gov_id " + gov_id);
                    Log.e("ascArrow", "joinType " + joinType);
                    Log.e("ascArrow", "sportType " + sportType);
                    Log.e("ascArrow", "champion " + champion);
                    Log.e("ascArrow", "from " + from);
                    Log.e("ascArrow", "to " + to);
                    Log.e("ascArrow", "hasV " + hasV);
                    Log.e("ascArrow", "hasI " + hasI);
                    Log.e("ascArrow", "mostViewed " + mostViewed);
                    Log.e("ascArrow", "mostReviewed " + mostReviewed);
                    progressDialog.dismiss();
                    Search search = response.body();
                    result = search.getResult();
                    scrollSearch.setVisibility(View.GONE);
                    scrollDes.setVisibility(View.GONE);
                    scrollAsc.setVisibility(View.GONE);
                    contentLinear.setVisibility(View.GONE);
                    scrollSort.setVisibility(View.VISIBLE);
                    containerAdapter.clearApplications();
                    //containerAdapter.swap(result);
                    containerAdapter = new SportsContainerAdapter(getActivity(), result);
                    containerAdapter.notifyDataSetChanged();
                    recyclerSort.setAdapter(containerAdapter);

                } else {
                    Log.e("ascArrow", "isNotSuccessful");
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }


    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if (isSportDialog) {
            sportname = dataSaver.getString(AppKeys.SPORT_NAME, "");
            sportId = dataSaver.getInt(AppKeys.SPORT_ID, -1);
            if (sportId != -1) {
                sportName.setText(sportname);
            }
        }
        if (isPositionDialog) {
            positionname = dataSaver.getString(AppKeys.POSITION_NAME, "");
            position_id = dataSaver.getInt(AppKeys.POSITION_ID, -1);
            if (position_id != -1) {
                positionName.setText(positionname);
            }
        }

        if (isGovernorateDialog) {

            governorateName = dataSaver.getString(AppKeys.GOVERNORATE_NAME, "");
            gov_id = dataSaver.getInt(AppKeys.GOVERNORATE_ID, -1);
            if (gov_id != -1) {
                selectGovernorate.setText(governorateName);
            }
        }

    }
}
