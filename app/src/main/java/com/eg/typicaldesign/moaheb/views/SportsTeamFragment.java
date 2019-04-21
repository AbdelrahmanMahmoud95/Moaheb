package com.eg.typicaldesign.moaheb.views;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eg.typicaldesign.moaheb.R;
import com.eg.typicaldesign.moaheb.controllers.Service;
import com.eg.typicaldesign.moaheb.controllers.SportsContainerAdapter;
import com.eg.typicaldesign.moaheb.models.Result;
import com.eg.typicaldesign.moaheb.models.Search;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MAGIC on 4/18/2018.
 */

public class SportsTeamFragment extends Fragment {
    RecyclerView recyclerTeam;
    Result result;
    GridLayoutManager LayoutManager;
    ProgressBar progressBar;
    int pageNumber = 1;
    SportsContainerAdapter containerAdapter;
    int type = 1;
    boolean isLoading = true;
    int pastVisibleItem, visibleItemCount, totalItemCount, previousTotal = 0;
    int viewThreshold = 10;
    final String TAG = "SportsTeamFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sports_team_fragment, container, false);
        recyclerTeam = (RecyclerView) view.findViewById(R.id.sports_team_recycle);
        progressBar = view.findViewById(R.id.progressBar);
        LayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerTeam.setLayoutManager(LayoutManager);
        Calligrapher calligrapher = new Calligrapher(getActivity());
        calligrapher.setFont(getActivity(), "bein-normal.ttf", true);
        getPlayerInformation();

        return view;
    }

    public void getPlayerInformation() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Service.Fetcher.getInstance().getPlayersInfo(type,pageNumber).enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {

                Log.e("TAG1", "isSuccessful");
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    Search search = response.body();
                    result = search.getResult();
                    containerAdapter = new SportsContainerAdapter(getActivity(), result);
                    recyclerTeam.setAdapter(containerAdapter);

                }
                Log.e("TAG2", "isNotSuccessful");
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
        Log.e(TAG, "recyclerTeam.addOnScrollListener");
        recyclerTeam.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = LayoutManager.getChildCount();
                totalItemCount = LayoutManager.getItemCount();
                pastVisibleItem = LayoutManager.findFirstVisibleItemPosition();
                Log.e(TAG, "dy "+dy);

                if (dy > 0) {
                    if (isLoading) {
                        if (totalItemCount > previousTotal) {
                            isLoading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!isLoading && (totalItemCount - visibleItemCount) <= (pastVisibleItem + viewThreshold)) {
                        Log.e(TAG, "performPagination()");

                        pageNumber++;
                        performPagination();
                        isLoading = true;
                        Log.e(TAG, "pageNumber "+pageNumber);

                    }
                }
            }
        });
    }

    public void performPagination() {
        progressBar.setVisibility(View.VISIBLE);
        Service.Fetcher.getInstance().getPlayersInfo(type,pageNumber).enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {

                Log.e("TAG1", "isSuccessful");
                if (response.isSuccessful()) {
                    Search search = response.body();
                    result = search.getResult();
                    containerAdapter.addInRecycle(result.getData());
                    progressBar.setVisibility(View.GONE);
                }
                Log.e("TAG2", "isNotSuccessful");
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {

            }

        });
    }

}
