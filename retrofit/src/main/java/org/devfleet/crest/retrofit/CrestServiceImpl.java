package org.devfleet.crest.retrofit;

import org.devfleet.crest.model.CrestCharacter;
import org.devfleet.crest.model.CrestCharacterStatus;
import org.devfleet.crest.model.CrestContact;
import org.devfleet.crest.model.CrestFitting;
import org.devfleet.crest.model.CrestLocation;
import org.devfleet.crest.model.CrestServerStatus;
import org.devfleet.crest.model.CrestSolarSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

final class CrestServiceImpl extends AbstractCrestService {

    private static final Logger LOG = LoggerFactory.getLogger(CrestServiceImpl.class);

    public CrestServiceImpl(final String clientID, final String clientKey) {
        super(clientID, clientKey);
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
        } catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new IllegalStateException(e.getLocalizedMessage(), e);
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

}
