package org.devfleet.crest.retrofit;

import org.devfleet.crest.CrestService;
import org.devfleet.crest.model.CrestCharacterStatus;
import org.devfleet.crest.model.CrestToken;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import retrofit2.Response;

abstract class AbstractCrestService implements CrestService {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCrestService.class);

    private final LoginService login;
    private PublicService crest;

    private AuthenticatedService client;
    private CrestToken token;

    public AbstractCrestService(final String clientID, final String clientKey) {
        Validate.isTrue(StringUtils.isNotBlank(clientID));
        Validate.isTrue(StringUtils.isNotBlank(clientKey));

        this.login = CrestRetrofit.newLogin(clientID, clientKey).create(LoginService.class);
        this.crest = CrestRetrofit.newClient().create(PublicService.class);
        this.client = CrestRetrofit.newClient().create(AuthenticatedService.class);
        this.token = null;
    }

    public final void setRefreshToken(final String refreshToken) throws IOException {
        if (StringUtils.isBlank(refreshToken)) {
            setNewToken(null);
            return;
        }

        setNewToken(refreshToken(this.login, refreshToken));
    }

    public final void setAuthCode(final String authCode) throws IOException {
        if (StringUtils.isBlank(authCode)) {
            setNewToken(null);
            return;
        }
        setNewToken(obtainToken(this.login, authCode));
    }

    protected final PublicService publicCrest() {
        return crest;
    }

    protected final AuthenticatedService authenticatedCrest() {
        return client;
    }

    protected final CrestToken getToken() {
        return this.token;
    }

    protected final CrestCharacterStatus getCharacterStatus(final boolean throwOnNull) {
        final CrestCharacterStatus status = refreshToken(this.token);
        if ((null == status) && throwOnNull){
            throw new IllegalStateException("not authenticated");
        }
        return status;
    }

    private CrestCharacterStatus refreshToken(final CrestToken token) {
        if (null == token) {
            if (this.token != null) {
                setNewToken(null);
            }
            return null;
        }

        if ((null == this.token) || !StringUtils.equals(this.token.getAccessToken(), token.getAccessToken())) {
            return setNewToken(token);
        }

        //same AccessToken
        if (this.token.expiresOn() < System.currentTimeMillis()) {
            try {
                return setNewToken(refreshToken(this.login, this.token.getRefreshToken()));
            }
            catch (IOException e) {
                LOG.warn(e.getLocalizedMessage(), e);
                return setNewToken(null);
            }
        }

        //in theory, this token is still valid
        try {
            return this.login.getVerification(token.getAccessToken()).execute().body();
        }
        catch (IOException e) {
            LOG.warn(e.getLocalizedMessage());
            try {
                return setNewToken(refreshToken(this.login, token.getRefreshToken()));
            }
            catch (IOException e2) {
                LOG.warn(e2.getLocalizedMessage());
                return null;
            }
        }
    }

    private CrestCharacterStatus setNewToken(final CrestToken token) {
        if (null == token) {
            this.client = CrestRetrofit.newClient().create(AuthenticatedService.class);
            this.token = null;
            return null;
        }
        try {
            this.client = CrestRetrofit.newClient(token.getAccessToken()).create(AuthenticatedService.class);
            this.token = token;
            return this.login.getVerification(token.getAccessToken()).execute().body();
        }
        catch (IOException e) {
            LOG.warn(e.getLocalizedMessage(), e);
            this.client = CrestRetrofit.newClient().create(AuthenticatedService.class);
            this.token = null;
            return null;
        }
    }

    private static CrestToken obtainToken(final LoginService login, final String authCode) throws IOException {
        final Response<CrestToken> response = login.getUserToken("authorization_code", authCode).execute();
        if (!response.isSuccessful()) {
            throw new IOException("token request unsuccessful: " + response.message());
        }
        return response.body();
    }

    private static CrestToken refreshToken(final LoginService login, final String refreshToken) throws IOException {
        final Response<CrestToken> response = login.getUserTokenUsingRefresh("refresh_token", refreshToken).execute();
        if (!response.isSuccessful()) {
            throw new IOException("token request unsuccessful: " + response.message());
        }
        return response.body();
    }
}
