package org.devfleet.crest.retrofit;

import org.devfleet.crest.model.CrestCharacter;
import org.devfleet.crest.model.CrestContact;
import org.devfleet.crest.model.CrestDictionary;
import org.devfleet.crest.model.CrestFitting;
import org.devfleet.crest.model.CrestFleet;
import org.devfleet.crest.model.CrestFleetSquad;
import org.devfleet.crest.model.CrestFleetWing;
import org.devfleet.crest.model.CrestLocation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

interface AuthenticatedService {

    @GET("/characters/{characterId}/")
    Call<CrestCharacter> getCharacter(
            @Path("characterId") final long characterId);

    @GET("/characters/{characterId}/location/")
    Call<CrestLocation> getLocation(
            @Path("characterId") final long characterId);

    @GET("/characters/{characterId}/contacts/")
    Call<CrestDictionary<CrestContact>> getContacts(
            @Path("characterId") final long characterId);

    @POST("/characters/{characterId}/contacts/")
    CrestContact postContact(
            @Path("characterId") final long characterId,
            final CrestContact contact);

    @DELETE("/characters/{characterId}/contacts/{contactId}/")
    boolean deleteContact(
            @Path("characterId") final long characterId,
            @Path("{contactId}") final long contactId);

    @GET("/characters/{characterId}/fittings/")
    Call<CrestDictionary<CrestFitting>> getFittings(
            @Path("characterId") final long characterId);

    @POST("/characters/{characterId}/fittings/")
    CrestFitting postFitting(
            @Path("characterId") final long characterId,
            final CrestFitting fitting);

    @DELETE("/characters/{characterId}/fittings/{fittingId}/")
    boolean deleteFitting(
            @Path("characterId") final long characterId,
            @Path("fittingId") final long fittingId);

    //GET and PUT for information about the fleet such as free move, voice, motd, and is registered
    @GET("/characters/{characterId}/fleets/{fleetId}/")
    CrestFleet getFleet(
            @Path("{characterId}") final long charId,
            @Path("{fleetId}") final long fleetId);

    @PUT("/characters/{characterId}/fleets/{fleetId}/")
    boolean putFleet(
            @Path("{characterId}") final long charId,
            @Path("{fleetId}") final long fleetId,
            final CrestFleet fleet);

    //list all of the characters in a fleet, their current ship, current solar system, current wing, current squad, and role
    @GET("/characters/{characterId}/fleets/{fleetId}/members/")
    List<CrestCharacter> getFleetMembers(
            @Path("{characterId}") final long charId,
            @Path("{fleetId}") final long fleetId
    );

    //invite characters to the fleet
    @POST("/characters/{characterId}/fleets/{fleetId}/members/")
    CrestCharacter postFleetMember(
            @Path("{characterId}") final long charId,
            @Path("{fleetId}") final long fleetId,
            final CrestCharacter member);

    //kick a member from a fleet
    @DELETE("/characters/{characterId}/fleets/{fleetId}/members/{memberId}/")
    boolean deleteFleetMember(
            @Path("{characterId}") final long charId,
            @Path("{fleetId}") final long fleetId,
            @Path("{memberId}") final long memberId);

    //list all of the wings in a fleet and the squads under them including their names
    @GET("/characters/{characterId}/fleets/{fleetId}/wings/")
    List<CrestFleetWing> getFleetWings(
            @Path("{characterId}") final long charId,
            @Path("{fleetId}") final long fleetId);

    //create a new wing
    @POST("/characters/{characterId}/fleets/{fleetId}/wings/")
    CrestFleetWing postFleetWing(
            @Path("{characterId}") final long charId,
            @Path("{fleetId}") final long fleetId,
            final CrestFleetWing wing);

    //rename the wing
    @PUT("/characters/{characterId}/fleets/{fleetId}/wings/{wingId}/")
    boolean putFleetWing(
            @Path("{characterId}") final long charId,
            @Path("{fleetId}") final long fleetId,
            @Path("{wingId}") final long wingId,
            final CrestFleetWing wing);

    //delete the wing
    @DELETE("/characters/{characterId}/fleets/{fleetId}/wings/{wingId}/")
    boolean deleteFleetWing(
            @Path("{characterId}") final long charId,
            @Path("{fleetId}") final long fleetId,
            @Path("{wingId}") final long wingId);

    //create a new squad in that wing
    @POST("/characters/{characterId}/fleets/{fleetId}/wings/{wingId}/squads/")
    CrestFleetSquad postFleetSquad(
            @Path("{characterId}") final long charId,
            @Path("{fleetId}") final long fleetId,
            @Path("{wingId}") final long wingId,
            CrestFleetSquad squad);

    //rename the squad
    @PUT("/characters/{characterId}/fleets/{fleetId}/wings/{wingId}/squads/{squadId}/")
    boolean putFleetSquad(
            @Path("{characterId}") final long charId,
            @Path("{fleetId}") final long fleetId,
            @Path("{wingId}") final long wingId,
            @Path("{squadId}") final long squadId,
            CrestFleetSquad squad);

    //delete the squad
    @DELETE("/characters/{characterId}/fleets/{fleetId}/wings/{wingId}/squads/{squadId}/")
    boolean deleteFleetSquad(
            @Path("{characterId}") final long charId,
            @Path("{fleetId}") final long fleetId,
            @Path("{wingId}") final long wingId,
            @Path("{squadId}") final long squadId);
/*
Added /characters/{characterId}/loyaltypoints/ to list all of the loyalty points a character has
Added /characters/{characterId}/opportunities/ which lists all of the opportunities a character has completed*/
}
