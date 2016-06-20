package org.devfleet.crest.retrofit;

import org.devfleet.crest.model.CrestDictionary;
import org.devfleet.crest.model.CrestMarketBulkOrder;
import org.devfleet.crest.model.CrestMarketHistory;
import org.devfleet.crest.model.CrestMarketOrder;
import org.devfleet.crest.model.CrestServerStatus;
import org.devfleet.crest.model.CrestSolarSystem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface PublicService {

    @GET("/solarsystems/{solarSystemId}/")
    Call<CrestSolarSystem> getSolarSystem(@Path("solarSystemId") long solarSystemId);

    @GET("/solarsystems/")
    Call<CrestDictionary<CrestSolarSystem>> getSolarSystems();

    @GET("/")
    Call<CrestServerStatus> getServerStatus();

    @GET("/market/{regionId}/types/{itemId}/history/")
    Call<CrestDictionary<CrestMarketHistory>> getMarketHistory(
            @Path("regionId") final long regionId,
            @Path("itemId") final long itemId,
            @Query("page") final int page);

    //https://public-crest.eveonline.com/market/10000002/orders/buy/?type=http://public-crest.eveonline.com/types/32772/
    @GET("/market/{regionId}/orders/{orderType}/")
    Call<CrestDictionary<CrestMarketOrder>> getMarketOrders (
            @Path("regionId") final long regionId,
            @Path("orderType") final String orderType,
            @Query("type") final String typePath);

    @GET("/market/{regionId}/orders/all/")
    Call<CrestDictionary<CrestMarketBulkOrder>> getAllMarketOrders (
            @Path("regionId") final long regionId,
            @Query("page") final int page);
}
