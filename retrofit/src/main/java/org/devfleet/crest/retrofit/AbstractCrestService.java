package org.devfleet.crest.retrofit;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.devfleet.crest.CrestService;
import org.devfleet.crest.model.CrestCharacterStatus;
import org.devfleet.crest.model.CrestToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

abstract class AbstractCrestService implements CrestService {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCrestService.class);

    private final LoginService login;
    private PublicService crest;

    private AuthenticatedService client;
    private CrestToken token;

    private final String crestHost;
    public AbstractCrestService(
            final String loginHost,
            final String crestHost,
            final String clientID,
            final String clientKey) {
        Validate.isTrue(StringUtils.isNotBlank(clientID));
        Validate.isTrue(StringUtils.isNotBlank(clientKey));

        this.login = CrestRetrofit.newLogin(loginHost, clientID, clientKey).create(LoginService.class);
        this.crest = CrestRetrofit.newClient(crestHost).create(PublicService.class);
        this.client = CrestRetrofit.newClient(crestHost).create(AuthenticatedService.class);
        this.crestHost = crestHost;
    }

    public final void setRefreshToken(final String refreshToken) throws IOException {
        if (StringUtils.isBlank(refreshToken)) {
            setNewToken(null);
        }
        else {
            setNewToken(obtainFromRefresh(this.login, refreshToken));
        }
    }

    public final void setAuthCode(final String authCode) throws IOException {
        if (StringUtils.isBlank(authCode)) {
            setNewToken(null);
        }
        else {
            setNewToken(obtainFromAuth(this.login, authCode));
        }
    }

    protected final PublicService publicCrest() {
        return crest;
    }

    protected final AuthenticatedService authenticatedCrest() {
        return client;
    }

    protected CrestCharacterStatus getCharacterStatus() {
        if (null == this.token) {
            throw new IllegalStateException("Not authenticated");
        }
        if (this.token.expiresOn() < System.currentTimeMillis()) {
            try {
                return setNewToken(obtainFromRefresh(this.login, this.token.getRefreshToken()));
            }
            catch (IOException e) {
                LOG.error(e.getLocalizedMessage(), e);
                throw new IllegalStateException(e.getLocalizedMessage());
            }
        }
        try {
            return this.login.getVerification(token.getAccessToken()).execute().body();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage());
            try {
                return setNewToken(obtainFromRefresh(this.login, token.getRefreshToken()));
            }
            catch (IOException e2) {
                LOG.error(e2.getLocalizedMessage());
                return null;
            }
        }
    }

    protected final CrestToken getToken() {
        return token;
    }

    private CrestCharacterStatus setNewToken(final CrestToken token) throws IOException {
        this.token = token;
        if (null == token) {
            this.client = CrestRetrofit.newClient(this.crestHost, null).create(AuthenticatedService.class);
            return null;
        }
        this.client = CrestRetrofit.newClient(this.crestHost, token.getAccessToken()).create(AuthenticatedService.class);
        return this.login.getVerification(token.getAccessToken()).execute().body();
    }

    private static CrestToken obtainFromAuth(final LoginService login, final String authCode) throws IOException {
        final Response<CrestToken> response = login.getUserToken("authorization_code", authCode).execute();
        if (!response.isSuccessful()) {
            throw new IOException("token request unsuccessful: " + response.message());
        }
        return response.body();
    }

    private static CrestToken obtainFromRefresh(final LoginService login, final String refreshToken) throws IOException {
        final Response<CrestToken> response = login.getUserTokenUsingRefresh("refresh_token", refreshToken).execute();
        if (!response.isSuccessful()) {
            throw new IOException("token request unsuccessful: " + response.message());
        }
        return response.body();
    }
}
