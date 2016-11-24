package org.devfleet.crest;

import java.util.List;

import org.devfleet.crest.model.*;

public interface CrestService {

    CrestServerStatus getServerStatus();

    CrestCharacterStatus getCharacterStatus();

    CrestCharacter getCharacter();

    CrestLocation getLocation();

    CrestSolarSystem getSolarSystem(long solarSystemId);

    CrestType getInventoryType (int typeId);

    List<CrestSolarSystem> getLocations();

    List<CrestItem> getRegions();

    List<CrestContact> getContacts();

    CrestContact getContact(final long contactID);

    boolean addContact(final CrestContact contact);

    boolean deleteContact(final long contactID);

    List<CrestFitting> getFittings();

    boolean addFitting(final CrestFitting fitting);

    boolean deleteFitting(final long fittingID);

    boolean addWaypoints(final List<CrestWaypoint> waypoints);

    boolean setWaypoints(final List<CrestWaypoint> waypoints);

    List<CrestMarketHistory> getMarketHistory(final long regionId, final long itemId);

    List<CrestMarketOrder> getMarketOrders(final long regionId, final String orderType, final long itemId);

    List<CrestMarketBulkOrder> getAllMarketOrders(final long regionId );
    
    List<CrestMarketPrice> getAllMarketPrices();
}
