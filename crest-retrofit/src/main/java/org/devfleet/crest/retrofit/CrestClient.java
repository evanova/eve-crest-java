package org.devfleet.crest.retrofit;

import org.devfleet.crest.CrestService;

import java.io.IOException;

public final class CrestClient {

    private final String clientID;
    private final String clientKey;

    public CrestClient(final String clientID, final String clientKey) {
        this.clientID = clientID;
        this.clientKey = clientKey;
    }

    public CrestService fromDefault() {
        final CrestServiceImpl service = new CrestServiceImpl(this.clientID, this.clientKey);
        return service;
    }

    public CrestService fromRefreshToken(final String token) throws IOException {
        final CrestServiceImpl service = new CrestServiceImpl(this.clientID, this.clientKey);
        service.setRefreshToken(token);
        return service;
    }

    public CrestService fromAuthCode(final String authCode) throws IOException {
        final CrestServiceImpl service = new CrestServiceImpl(this.clientID, this.clientKey);
        service.setAuthCode(authCode);
        return service;
    }
}
