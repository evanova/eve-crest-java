package org.devfleet.crest.retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.devfleet.crest.model.CrestCharacter;
import org.devfleet.crest.model.CrestCharacterStatus;
import org.devfleet.crest.model.CrestContact;
import org.devfleet.crest.model.CrestDictionary;
import org.devfleet.crest.model.CrestFitting;
import org.devfleet.crest.model.CrestItem;
import org.devfleet.crest.model.CrestLocation;
import org.devfleet.crest.model.CrestMarketBulkOrder;
import org.devfleet.crest.model.CrestMarketHistory;
import org.devfleet.crest.model.CrestMarketOrder;
import org.devfleet.crest.model.CrestMarketPrice;
import org.devfleet.crest.model.CrestServerStatus;
import org.devfleet.crest.model.CrestSolarSystem;
import org.devfleet.crest.model.CrestWaypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class CrestServiceImpl extends AbstractCrestService {

    private static final Logger LOG = LoggerFactory.getLogger(CrestServiceImpl.class);

    private final String crestHost;

    public CrestServiceImpl( final String loginHost,
                             final String crestHost,
                             final String clientID,
                             final String clientKey) {
        super(loginHost, crestHost, clientID, clientKey);
        this.crestHost = crestHost;
    }

    @Override
    public CrestServerStatus getServerStatus() {
        try {
            return this.publicCrest().getServerStatus().execute().body();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public CrestSolarSystem getSolarSystem(long solarSystemId) {
        try {
            return this.publicCrest().getSolarSystem(solarSystemId).execute().body();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new IllegalStateException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public List<CrestSolarSystem> getLocations() {
        try {
            return this.publicCrest().getSolarSystems().execute().body().getItems();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new IllegalStateException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public CrestCharacter getCharacter() {
        final CrestCharacterStatus status = getCharacterStatus();
        try {
            final CrestCharacter character = this.authenticatedCrest().getCharacter(status.getCharacterID()).execute().body();
            character.setRefreshToken((null == this.getToken()) ? null : this.getToken().getRefreshToken());
            character.setAccessToken((null == this.getToken()) ? null : this.getToken().getAccessToken());
            return character;
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new IllegalStateException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public CrestLocation getLocation() {
        final CrestCharacterStatus status = getCharacterStatus();
        try {
            return this.authenticatedCrest().getLocation(status.getCharacterID()).execute().body();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new IllegalStateException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public List<CrestContact> getContacts() {
        final CrestCharacterStatus status = getCharacterStatus();
        try {
            final List<CrestContact> returned = new ArrayList<>();

            CrestDictionary<CrestContact> dictionary;
            int page = 0;
            do {
                page = page + 1;
                dictionary = this.authenticatedCrest().getContacts(status.getCharacterID(), page).execute().body();
                returned.addAll(dictionary.getItems());
            }
            while (dictionary.getPageCount() > page);

            return returned;
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new IllegalStateException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public CrestContact getContact(long contactID) {
        final CrestCharacterStatus status = getCharacterStatus();
        try {
            return this.authenticatedCrest().getContact(status.getCharacterID(), contactID).execute().body();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new IllegalStateException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public boolean addContact(CrestContact contact) {
        final CrestCharacterStatus status = getCharacterStatus();
        try {

            contact.setBlocked(null);
            contact.setWatched(null);
            contact.setHref(null);
            contact.setCharacter(null);

            final CrestItem sudo = new CrestItem();
            sudo.setId(contact.getContact().getId());
            sudo.setHref(href("characters/" + contact.getContact().getId()));
            sudo.setName("");
            contact.setContact(sudo);

            return this.authenticatedCrest().postContact(status.getCharacterID(), contact).execute().isSuccessful();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override
    public boolean deleteContact(long contactID) {
        final CrestCharacterStatus status = getCharacterStatus();
        try {
            return this.authenticatedCrest().deleteContact(status.getCharacterID(), contactID).execute().isSuccessful();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override
    public List<CrestFitting> getFittings() {
        final CrestCharacterStatus status = getCharacterStatus();
        try {
            final List<CrestFitting> returned = new ArrayList<>();

            CrestDictionary<CrestFitting> dictionary;
            int page = 0;
            do {
                page = page + 1;
                dictionary = this.authenticatedCrest().getFittings(status.getCharacterID(), page).execute().body();
                returned.addAll(dictionary.getItems());
            }
            while (dictionary.getPageCount() > page);

            return returned;
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new IllegalStateException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public boolean addFitting(CrestFitting fitting) {
        final CrestCharacterStatus status = getCharacterStatus();
        try {
            return this.authenticatedCrest().postFitting(status.getCharacterID(), fitting).execute().isSuccessful();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override
    public boolean deleteFitting(long fittingID) {
        final CrestCharacterStatus status = getCharacterStatus();
        try {
            return this.authenticatedCrest().deleteFitting(status.getCharacterID(), fittingID).execute().isSuccessful();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override
    public boolean addWaypoints(List<CrestWaypoint> waypoints) {
        return setWaypoints(waypoints, false);
    }

    @Override
    public boolean setWaypoints(final List<CrestWaypoint> waypoints) {
        return setWaypoints(waypoints, true);
    }

    private boolean setWaypoints(final List<CrestWaypoint> waypoints, final boolean replace) {
        Validate.isTrue(!waypoints.isEmpty(), "Waypoint list cannot be empty.");
        final CrestCharacterStatus status = getCharacterStatus();
        try {
            boolean first = replace;
            for (CrestWaypoint wp: waypoints) {
                wp.setClearOtherWaypoints(first);
                wp.setFirst(first);
                wp.getSolarSystem().setHref(href("/solarsystems/" + wp.getSolarSystem().getId()));
                first = false;

                if (!this.authenticatedCrest().addWaypoint(status.getCharacterID(), wp).execute().isSuccessful()) {
                    LOG.error("AddWaypoint failed {}", ToStringBuilder.reflectionToString(wp));
                    return false;
                }
            }
            return true;
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override
    public List<CrestMarketHistory> getMarketHistory(long regionId, long itemId) {
        try {
            final List<CrestMarketHistory> returned = new ArrayList<>();
            CrestDictionary<CrestMarketHistory> dictionary;
            
            final String typePath = href("inventory/types") + itemId + "/";
            dictionary = this.publicCrest().getMarketHistory(regionId, typePath).execute().body();
            if (null == dictionary) {
                LOG.error("getMarketHistory: null dictionary {}, {}", regionId, itemId);
            }
            returned.addAll(dictionary.getItems());
            return returned;
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new IllegalStateException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public List<CrestMarketOrder> getMarketOrders(final long regionId, final String orderType, final long itemId) {
        try {
            CrestDictionary<CrestMarketOrder> dictionary;

            // TODO: Change this so that it pulls the url from the root CREST
            // endpoint
            final String typePath = href("inventory/types") + itemId + "/";

            dictionary = this.publicCrest().getMarketOrders(regionId, orderType, typePath).execute().body();

            return dictionary.getItems();
        } catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new IllegalStateException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public List<CrestMarketBulkOrder> getAllMarketOrders(final long regionId) {
        try {
            final List<CrestMarketBulkOrder> returned = new ArrayList<>();
            CrestDictionary<CrestMarketBulkOrder> dictionary;
            int page = 0;
            do {
                page = page + 1;
                dictionary = this.publicCrest().getAllMarketOrders(regionId, page).execute().body();
                if (null == dictionary) {
                    LOG.error("getAllMarketOrders: null dictionary {}", regionId);
                    break;
                }
                returned.addAll(dictionary.getItems());
            } while (dictionary.getPageCount() > page);
            return returned;
        } catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new IllegalStateException(e.getLocalizedMessage(), e);
        }
    }
    
    @Override
    public List<CrestMarketPrice> getAllMarketPrices() {
         try {
            final List<CrestMarketPrice> returned = new ArrayList<>();

            CrestDictionary<CrestMarketPrice> dictionary;
            int page = 0;
            do {
                page = page + 1;
                dictionary = this.publicCrest().getAllMarketPrices(page).execute().body();
                if (null == dictionary) {
                    LOG.error("getAllMarketPrices: null dictionary {}", page);
                }
                returned.addAll(dictionary.getItems());
            } while (dictionary.getPageCount() > page);
            return returned;
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage());
            throw new IllegalStateException(e.getLocalizedMessage(), e);
    	}
    }

    private String href(String path) {
        return new StringBuilder()
            .append("https://")
            .append(this.crestHost)
            .append("/")
            .append(path)
            .append("/")
            .toString();
    }
}
