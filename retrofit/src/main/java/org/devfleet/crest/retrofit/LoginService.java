package org.devfleet.crest.retrofit;

import org.devfleet.crest.model.CrestCharacterStatus;
import org.devfleet.crest.model.CrestToken;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface LoginService {

    @POST("/oauth/token/")
    Call<CrestToken> getUserToken(@Query("grant_type") String type, @Query("code") String code);

    @POST("/oauth/token/")
    Call<CrestToken> getUserTokenUsingRefresh(@Query("grant_type") String type, @Query("refresh_token") String token);

    @GET("/oauth/verify/")
    Call<CrestCharacterStatus> getVerification(@Header("Authorization") String token);
}