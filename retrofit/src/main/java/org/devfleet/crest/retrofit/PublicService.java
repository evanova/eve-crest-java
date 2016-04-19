package org.devfleet.crest.retrofit;

import org.devfleet.crest.model.CrestDictionary;
import org.devfleet.crest.model.CrestServerStatus;
import org.devfleet.crest.model.CrestSolarSystem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

interface PublicService {

    @GET("/solarsystems/{solarSystemId}")
    Call<CrestSolarSystem> getSolarSystem(@Path("solarSystemId") long solarSystemId);

    @GET("/solarsystems/")
    Call<CrestDictionary<CrestSolarSystem>> getSolarSystems();

    @GET("/")
    Call<CrestServerStatus> getServerStatus();

}
