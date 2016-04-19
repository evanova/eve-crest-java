package org.devfleet.crest.retrofit;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.devfleet.crest.model.CrestCharacter;
import org.devfleet.crest.model.CrestCharacterStatus;
import org.devfleet.crest.model.CrestContact;
import org.devfleet.crest.model.CrestDictionary;
import org.devfleet.crest.model.CrestFitting;
import org.devfleet.crest.model.CrestLocation;
import org.devfleet.crest.model.CrestServerStatus;
import org.devfleet.crest.model.CrestSolarSystem;

import org.devfleet.crest.model.CrestWaypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

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
    public CrestSolarSystem getLocation(long solarSystemId) {
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
            return character;
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new IllegalStateException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public CrestSolarSystem getLocation() {
        final CrestCharacterStatus status = getCharacterStatus();
        try {
            final CrestLocation location = this.authenticatedCrest().getLocation(status.getCharacterID()).execute().body();
            if ((null == location) || (location.getId() == 0)) {
                return null;
            }
            return this.publicCrest().getSolarSystem(location.getId()).execute().body();
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
            return this.authenticatedCrest().getContacts(status.getCharacterID()).execute().body().getItems();
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
            //Having to set hrefs is not cool
            contact.setHref(null);
            contact.getContact().setHref(href("characters/" + contact.getContact().getId()));
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
            return this.authenticatedCrest().getFittings(status.getCharacterID()).execute().body().getItems();
        } catch (IOException e) {
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
