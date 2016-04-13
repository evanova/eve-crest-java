package org.devfleet.crest.retrofit;

import org.devfleet.crest.model.CrestServerStatus;
import org.devfleet.crest.model.CrestSolarSystem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

interface PublicService {

    @GET("/solarsystems")
    Call<CrestSolarSystem> getSolarSystem(@Path("solarSystemId") long solarSystemId);

    @GET("/")
    Call<CrestServerStatus> getServerStatus();

}
