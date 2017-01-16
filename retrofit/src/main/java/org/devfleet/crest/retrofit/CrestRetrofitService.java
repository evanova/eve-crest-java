package org.devfleet.crest.retrofit;

import org.devfleet.crest.model.CrestCharacter;
import org.devfleet.crest.model.CrestContact;
import org.devfleet.crest.model.CrestDictionary;
import org.devfleet.crest.model.CrestFitting;
import org.devfleet.crest.model.CrestFleet;
import org.devfleet.crest.model.CrestFleetSquad;
import org.devfleet.crest.model.CrestFleetWing;
import org.devfleet.crest.model.CrestItem;
import org.devfleet.crest.model.CrestLocation;
import org.devfleet.crest.model.CrestMarketBulkOrder;
import org.devfleet.crest.model.CrestMarketHistory;
import org.devfleet.crest.model.CrestMarketOrder;
import org.devfleet.crest.model.CrestMarketPrice;
import org.devfleet.crest.model.CrestServerStatus;
import org.devfleet.crest.model.CrestSolarSystem;
import org.devfleet.crest.model.CrestType;
import org.devfleet.crest.model.CrestWaypoint;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CrestRetrofitService {

    @GET("/solarsystems/{solarSystemId}/")
    Call<CrestSolarSystem> getSolarSystem(@Path("solarSystemId") long solarSystemId);

    @GET("/solarsystems/")
    Call<CrestDictionary<CrestSolarSystem>> getSolarSystems();

    @GET("/regions/")
    Call<CrestDictionary<CrestItem>> getRegions();

    @GET("/")
    Call<CrestServerStatus> getServerStatus();

    @GET("/inventory/types/{typeId}/")
    Call<CrestType> getInventoryType (@Path("typeId") final int typeId);

    @GET("/market/{regionId}/history/")
    Call<CrestDictionary<CrestMarketHistory>> getMarketHistory(
            @Path("regionId") final long regionId,
            @Query("type") final String typePath);

    @GET("/market/{regionId}/orders/{orderType}/")
    Call<CrestDictionary<CrestMarketOrder>> getMarketOrders (
            @Path("regionId") final long regionId,
            @Path("orderType") final String orderType,
            @Query("type") final String typePath);

    @GET("/market/{regionId}/orders/all/")
    Call<CrestDictionary<CrestMarketBulkOrder>> getAllMarketOrders (
            @Path("regionId") final long regionId,
            @Query("page") final int page);

    @GET("/market/prices/")
    Call<CrestDictionary<CrestMarketPrice>> getAllMarketPrices (@Query("page") final int page);

    @GET("/characters/{characterId}/")
    Call<CrestCharacter> getCharacter(
            @Path("characterId") final long characterId);

    @GET("/characters/{characterId}/location/")
    Call<CrestLocation> getLocation(
            @Path("characterId") final long characterId);

    @POST("/characters/{characterId}/navigation/waypoints/")
    @Headers("Content-Type: application/vnd.ccp.eve.PostWaypoint-v1+json; charset=utf-8")
    Call<Void> addWaypoint(
            @Path("characterId") final long characterId,
            @Body final CrestWaypoint waypoint);

    @GET("/characters/{characterId}/contacts/")
    Call<CrestDictionary<CrestContact>> getContacts(
            @Path("characterId") final long characterId,
            @Query("page") final int page);

    @POST("/characters/{characterId}/contacts/{contactId}/")
    Call<CrestContact> getContact(
            @Path("characterId") final long characterId,
            @Path("contactId") final long contactID);

    @POST("/characters/{characterId}/contacts/")
    @Headers("Content-Type: application/vnd.ccp.eve.ContactCreate-v1+json; charset=utf-8")
    Call<Void> postContact(
            @Path("characterId") final long characterId,
            @Body final CrestContact contact);

    @DELETE("/characters/{characterId}/contacts/{contactId}/")
    Call<Void> deleteContact(
            @Path("characterId") final long characterId,
            @Path("contactId") final long contactId);

    @GET("/characters/{characterId}/fittings/")
    Call<CrestDictionary<CrestFitting>> getFittings(
            @Path("characterId") final long characterId,
            @Query("page") final int page);

    @POST("/characters/{characterId}/fittings/")
    @Headers("Content-Type: application/vnd.ccp.eve.FittingCreate-v1+json; charset=utf-8")
    Call<Void> postFitting(
            @Path("characterId") final long characterId,
            @Body final CrestFitting fitting);

    @DELETE("/characters/{characterId}/fittings/{fittingId}/")
    Call<Void> deleteFitting(
            @Path("characterId") final long characterId,
            @Path("fittingId") final long fittingId);

    //GET and PUT for information about the fleet such as free move, voice, motd, and is registered
    @GET("/characters/{characterId}/fleets/{fleetId}/")
    Call<CrestDictionary<CrestFleet>> getFleet(
            @Path("characterId") final long charId,
            @Path("fleetId") final long fleetId,
            @Query("page") final int page);

    @PUT("/characters/{characterId}/fleets/{fleetId}/")
    @Headers("Content-Type: application/vnd.ccp.eve.FleetUpdate-v1+json; charset=utf-8")
    Call<Void> putFleet(
            @Path("characterId") final long charId,
            @Path("fleetId") final long fleetId,
            @Body final CrestFleet fleet);

    //list all of the characters in a fleet, their current ship, current solar system, current wing, current squad, and role
    @GET("/characters/{characterId}/fleets/{fleetId}/members/")
    Call<CrestDictionary<CrestCharacter>> getFleetMembers(
            @Path("characterId") final long charId,
            @Path("fleetId") final long fleetId,
            @Query("page") final int page);

    //invite characters to the fleet
    @POST("/characters/{characterId}/fleets/{fleetId}/members/")
    @Headers("Content-Type: application/vnd.ccp.eve.FleetMemberInvite-v1+json; charset=utf-8")
    Call<Void> postFleetMember(
            @Path("characterId") final long charId,
            @Path("fleetId") final long fleetId,
            @Body final CrestCharacter member);

    //kick a member from a fleet
    @DELETE("/characters/{characterId}/fleets/{fleetId}/members/{memberId}/")
    Call<Void> deleteFleetMember(
            @Path("characterId") final long charId,
            @Path("fleetId") final long fleetId,
            @Path("memberId") final long memberId);

    //list all of the wings in a fleet and the squads under them including their names
    @GET("/characters/{characterId}/fleets/{fleetId}/wings/")
    Call<CrestDictionary<CrestFleetWing>> getFleetWings(
            @Path("characterId") final long charId,
            @Path("fleetId") final long fleetId,
            @Query("page") final int page);

    //create a new wing
    @POST("/characters/{characterId}/fleets/{fleetId}/wings/")
    @Headers("Content-Type: application/vnd.ccp.eve.FleetWingCreate-v1+json; charset=utf-8")
    Call<Void> postFleetWing(
            @Path("characterId") final long charId,
            @Path("fleetId") final long fleetId,
            @Body final CrestFleetWing wing);

    //rename the wing
    @PUT("/characters/{characterId}/fleets/{fleetId}/wings/{wingId}/")
    @Headers("Content-Type: application/vnd.ccp.eve.FleetWingUpdate-v1+json; charset=utf-8")
    Call<Void> putFleetWing(
            @Path("characterId") final long charId,
            @Path("fleetId") final long fleetId,
            @Path("wingId") final long wingId,
            @Body final CrestFleetWing wing);

    //delete the wing
    @DELETE("/characters/{characterId}/fleets/{fleetId}/wings/{wingId}/")
    @Headers("Content-Type: application/vnd.ccp.eve.FleetWingDelete-v1+json; charset=utf-8")
    Call<Void> deleteFleetWing(
            @Path("characterId") final long charId,
            @Path("fleetId") final long fleetId,
            @Path("wingId") final long wingId);

    //create a new squad in that wing
    @POST("/characters/{characterId}/fleets/{fleetId}/wings/{wingId}/squads/")
    @Headers("Content-Type: application/vnd.ccp.eve.FleetSquadCreate-v1+json; charset=utf-8")
    Call<Void> postFleetSquad(
            @Path("characterId") final long charId,
            @Path("fleetId") final long fleetId,
            @Path("wingId") final long wingId,
            @Body CrestFleetSquad squad);

    //rename the squad
    @PUT("/characters/{characterId}/fleets/{fleetId}/wings/{wingId}/squads/{squadId}/")
    @Headers("Content-Type: application/vnd.ccp.eve.FleetSquadUpdate-v1+json; charset=utf-8")
    Call<Void> putFleetSquad(
            @Path("characterId") final long charId,
            @Path("fleetId") final long fleetId,
            @Path("wingId") final long wingId,
            @Path("squadId") final long squadId,
            @Body CrestFleetSquad squad);

    //delete the squad
    @DELETE("/characters/{characterId}/fleets/{fleetId}/wings/{wingId}/squads/{squadId}/")
    @Headers("Content-Type: application/vnd.ccp.eve.FleetSquadDelete-v1+json; charset=utf-8")
    Call<Void> deleteFleetSquad(
            @Path("characterId") final long charId,
            @Path("fleetId") final long fleetId,
            @Path("wingId") final long wingId,
            @Path("squadId") final long squadId);

}
