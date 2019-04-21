package com.eg.typicaldesign.moaheb.controllers;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by MAGIC on 5/27/2018.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    SharedPreferences dataSaver;
    final String TAG = "FIRE_BASE_SERVICE";


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        dataSaver = getDefaultSharedPreferences(getApplicationContext());

        dataSaver.edit()
                .putString(AppKeys.FCM_TOKEN_KEY, refreshedToken)
                .commit();

        Log.d(TAG, "refreshedToken "+refreshedToken );
        //sendRegistrationToServer(refreshedToken);
    }
//
//    private void sendRegistrationToServer(String refreshedToken) {
//        int userId = dataSaver.getInt(Keys.ID_KEY, -1);
//        String token = dataSaver.getString(Keys.TOKEN_KEY, "");
//        Service.Fetcher.getInstance().sendFireBaseToken(userId, token, refreshedToken).enqueue(new Callback<FCMReturn>() {
//            @Override
//            public void onResponse(Call<FCMReturn> call, Response<FCMReturn> response) {
//                if (response.isSuccessful()) {
//
//                    Log.d(TAG, response.body().getStatus() + "");
//                    if (response.body().getStatus()==1){
//                        dataSaver.edit()
//                                .putBoolean(Keys.IS_FCM_TOKEN_SENT_KEY,true)
//                                .commit();
//                    }
//                    else {
//                        dataSaver.edit()
//                                .putBoolean(Keys.IS_FCM_TOKEN_SENT_KEY,false)
//                                .commit();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<FCMReturn> call, Throwable t) {
//            }
//        });
//    }
}
