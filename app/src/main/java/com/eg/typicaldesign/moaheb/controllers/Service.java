package com.eg.typicaldesign.moaheb.controllers;

import com.eg.typicaldesign.moaheb.models.Cities;
import com.eg.typicaldesign.moaheb.models.GeneralResponse;
import com.eg.typicaldesign.moaheb.models.Governorates;
import com.eg.typicaldesign.moaheb.models.Media;
import com.eg.typicaldesign.moaheb.models.Positions;
import com.eg.typicaldesign.moaheb.models.Search;
import com.eg.typicaldesign.moaheb.models.SliderImage;
import com.eg.typicaldesign.moaheb.models.SponsorProfile;
import com.eg.typicaldesign.moaheb.models.Sports;
import com.eg.typicaldesign.moaheb.models.Terms;
import com.eg.typicaldesign.moaheb.models.UserDocs;
import com.eg.typicaldesign.moaheb.models.UserInformation;
import com.eg.typicaldesign.moaheb.models.playerProfile;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by MAGIC on 4/8/2018.
 */

public interface Service {


    @GET("cities/{gov_id}")
    Call<Cities> getCities(@Path("gov_id") int gov_id);

    @GET("positions/{sport_id}")
    Call<Positions> getPosition(@Path("sport_id") int sport_id);

    @GET("sports/{sport_type}")
    Call<Sports> getSports(@Path("sport_type") int sport_type);

    @GET("profile/{user_id}")
    Call<playerProfile> getPlayerDetails(@Path("user_id") int user_id, @Query("api_token") String api_token);

    @GET("user/media/{type}")
    Call<Media> getPlayerImages(@Path("type") int type, @Query("api_token") String api_token);

    @GET("user/media/{type}")
    Call<Media> getPlayerVideos(@Path("type") int type, @Query("api_token") String api_token);

    @GET("governorates")
    Call<Governorates> getGovernorate();

    @GET("user/profile")
    Call<UserInformation> getUserData(@Query("api_token") String api_token);

    @GET("sponsor/profile")
    Call<SponsorProfile> getSponsorData(@Query("api_token") String api_token);

    @GET("user/profile/identities")
    Call<UserDocs> getUserDocs(@Query("api_token") String api_token);


    @GET("search")
    Call<Search> getPlayersInfo(@Query("sport_type") int sport_type, @Query("page") int page);

    @GET("slider")
    Call<SliderImage> getImages();

    @GET("http://www.app-talent.com/public/uploads/apptalentintro.mp4")
    Call<ResponseBody> downloadFile();

    @GET("sports-positions/{sport_type}")
    Call<Sports> getSportsAndPositions(@Path("sport_type") int sport_type);

    @GET("page/{permalink}")
    Call<Terms> getTerms(@Path("permalink") String permalink);

    @GET("search")
    Call<Search> search(@Query("sport_id") int sport_id,
                        @Query("position_id") int position_id,
                        @Query("gov_id") int gov_id,
                        @Query("sport_type") int sport_type,
                        @Query("champion") int champion,
                        @Query("club_joined") int club_joined,
                        @Query("age_from") String age_from,
                        @Query("age_to") String age_to,
                        @Query("has_video") String hasVideo,
                        @Query("has_image") String hasImage,
                        @Query("single") int single);

    @GET("search")
    Call<Search> sort(@Query("sport_id") int sport_id,
                      @Query("position_id") int position_id,
                      @Query("gov_id") int gov_id,
                      @Query("sport_type") int sport_type,
                      @Query("champion") int champion,
                      @Query("club_joined") int club_joined,
                      @Query("age_from") String age_from,
                      @Query("age_to") String age_to,
                      @Query("has_video") String hasVideo,
                      @Query("has_image") String hasImage,
                      @Query("sort_by") String sortBy,
                      @Query("sorting") String sorting,
                      @Query("most_viewed") String mostView,
                      @Query("most_reviewed") String mostReview);


    @Multipart
    @POST("user-register")
    Call<GeneralResponse> registerUser(@Part MultipartBody.Part image,
                                       @Part("name") RequestBody name,
                                       @Part("phone") RequestBody phone,
                                       @Part("email") RequestBody email,
                                       @Part("password") RequestBody password,
                                       @Part("birth_date") RequestBody birth_date,
                                       @Part("position_id") RequestBody position_id,
                                       @Part("address") RequestBody address,
                                       @Part("sport_id") RequestBody sport_id,
                                       @Part("sport_type") RequestBody sport_type,
                                       @Part("overview") RequestBody overview,
                                       @Part("lat") RequestBody lat,
                                       @Part("lon") RequestBody lon);

    @Multipart
    @POST("user/profile/{page_id}")
    Call<GeneralResponse> updateUserProfile(@Path("page_id") int page_id,
                                            @Part MultipartBody.Part image,
                                            @Part("api_token") RequestBody api_token,
                                            @Part("name") RequestBody name,
                                            @Part("phone") RequestBody phone,
                                            @Part("email") RequestBody email,
                                            @Part("password") RequestBody password,
                                            @Part("birth_date") RequestBody birth_date,
                                            @Part("position_id") RequestBody position_id,
                                            @Part("address") RequestBody address,
                                            @Part("sport_id") RequestBody sport_id,
                                            @Part("sport_type") RequestBody sport_type,
                                            @Part("overview") RequestBody overview,
                                            @Part("lat") RequestBody lat,
                                            @Part("lon") RequestBody lon);


    @Multipart
    @POST("sponsor/profile")
    Call<GeneralResponse> updateSponsorProfile(@Part MultipartBody.Part image,
                                               @Part("api_token") RequestBody api_token,
                                               @Part("name") RequestBody name,
                                               @Part("phone") RequestBody phone,
                                               @Part("email") RequestBody email,
                                               @Part("password") RequestBody password,
                                               @Part("address") RequestBody address,
                                               @Part("gov_id") RequestBody gov_id,
                                               @Part("city_id") RequestBody city_id);


    @Multipart
    @POST("sponsor-register")
    Call<GeneralResponse> registerSponsor(@Part MultipartBody.Part image,
                                          @Part("name") RequestBody name,
                                          @Part("phone") RequestBody phone,
                                          @Part("email") RequestBody email,
                                          @Part("password") RequestBody password,
                                          @Part("address") RequestBody address,
                                          @Part("gov_id") RequestBody gov_id,
                                          @Part("city_id") RequestBody city_id);

    @Multipart
    @POST("user/upload-media")
    Call<GeneralResponse> uploadImage(@Part MultipartBody.Part image,
                                      @Part("api_token") RequestBody api_token,
                                      @Part("type") RequestBody type);

    @Multipart
    @POST("user/upload-media")
    Call<GeneralResponse> uploadVideo(@Part MultipartBody.Part image,
                                      @Part("api_token") RequestBody api_token,
                                      @Part("type") RequestBody type,
                                      @Part("description") RequestBody description);

    @Multipart
    @POST("user/profile/{page_id}")
    Call<GeneralResponse> uploadDocs(@Path("page_id") int page_id,
                                     @Part MultipartBody.Part image,
                                     @Part("api_token") RequestBody api_token,
                                     @Part("identity_name") RequestBody identity_name);

    @FormUrlEncoded
    @POST("user-login")
    Call<GeneralResponse> loginUser(@Field("phone") String phone, @Field("password") String password);

    @FormUrlEncoded
    @POST("user/set-fcm-token")
    Call<GeneralResponse> sendFCMForPlayer(@Field("api_token") String token, @Field("fcm_token") String firebaseToken);

    @FormUrlEncoded
    @POST("sponsor/set-fcm-token")
    Call<GeneralResponse> sendFCMForSponsor(@Field("api_token") String token, @Field("fcm_token") String firebaseToken);

    @FormUrlEncoded
    @POST("user/media/{media_id}/delete")
    Call<GeneralResponse> deleteImage(@Path("media_id") int media_id, @Field("api_token") String api_token);

    @FormUrlEncoded
    @POST("user/profile/identity/{identity_id}/delete")
    Call<GeneralResponse> deleteDocs(@Path("identity_id") int identity_id, @Field("api_token") String api_token);

    @FormUrlEncoded
    @POST("user/media/{media_id}/delete")
    Call<GeneralResponse> deleteVideo(@Path("media_id") int media_id, @Field("api_token") String api_token);

    @FormUrlEncoded
    @POST("sponsor-login")
    Call<GeneralResponse> loginSponsor(@Field("phone") String phone, @Field("password") String password);

    @FormUrlEncoded
    @POST("user/profile/{page_id}")
    Call<GeneralResponse> updateUserSportData(@Path("page_id") int page_id,
                                              @Field("api_token") String api_token,
                                              @Field("sport_type") int sport_type,
                                              @Field("sport_id") int sport_id,
                                              @Field("position_id") int position_id,
                                              @Field("club_joined") int club_joined,
                                              @Field("club_name") String club_name,
                                              @Field("overview") String overview,
                                              @Field("length") String length,
                                              @Field("weight") String weight,
                                              @Field("champion") int champion);

    @FormUrlEncoded
    @POST("user/profile/{page_id}")
    Call<GeneralResponse> updateUserAddressData(@Path("page_id") int page_id,
                                                @Field("api_token") String api_token,
                                                @Field("gov_id") int gov_id,
                                                @Field("city_id") int city_id,
                                                @Field("address") String address);

    @FormUrlEncoded
    @POST("user/profile/{page_id}")
    Call<GeneralResponse> updateParentData(@Path("page_id") int page_id,
                                           @Field("api_token") String api_token,
                                           @Field("guardian") String guardianName,
                                           @Field("guardian_phone") String guardianPhone);

    @FormUrlEncoded
    @POST("rank/{user_id}")
    Call<GeneralResponse> setPlayerRate(@Path("user_id") int userId,
                                        @Field("sponsor_id") int sponsorId,
                                        @Field("api_token") String token,
                                        @Field("stars") float stars);

    String BASE_URL = "http://app-talent.com/api/";


    class Fetcher {
        private static Service service = null;

        public static Service getInstance() {
            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build();
                service = retrofit.create(Service.class);
            }
            return service;
        }
    }
}
