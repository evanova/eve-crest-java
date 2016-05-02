package org.devfleet.crest.retrofit;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.devfleet.crest.CrestService;
import org.devfleet.crest.model.CrestCharacterStatus;
import org.devfleet.crest.model.CrestToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

abstract class AbstractCrestService implements CrestService {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCrestService.class);

    private final LoginService loginService;
    private PublicService publicService;

    private AuthenticatedService authService;
    private CrestToken token;

    private final String crestHost;
    public AbstractCrestService(
            final String loginHost,
            final String crestHost,
            final String clientID,
            final String clientKey) {

        this.loginService =
                (StringUtils.isNoneBlank(clientID, clientKey)) ?
                CrestRetrofit.newLogin(loginHost, clientID, clientKey).create(LoginService.class) :
                null;
        this.publicService = CrestRetrofit.newClient(crestHost).create(PublicService.class);
        this.authService = CrestRetrofit.newClient(crestHost).create(AuthenticatedService.class);
        this.crestHost = crestHost;
    }

    public final void setRefreshToken(final String refreshToken) throws IOException {
        if (null == this.loginService) {
            throw new IOException("setRefreshToken: no login service without a client id/key.");
        }
        if (StringUtils.isBlank(refreshToken)) {
            setNewToken(null);
        }
        else {
            setNewToken(obtainFromRefresh(this.loginService, refreshToken));
        }
    }

    public final void setAuthCode(final String authCode) throws IOException {
        if (null == this.loginService) {
            throw new IOException("setAuthCode: no login service without a client id/key.");
        }
        if (StringUtils.isBlank(authCode)) {
            setNewToken(null);
        }
        else {
            setNewToken(obtainFromAuth(this.loginService, authCode));
        }
    }

    public final void setAccessToken(final CrestToken token) throws IOException {
        setNewToken(token);
    }

    protected final PublicService publicCrest() {
        return publicService;
    }

    protected final AuthenticatedService authenticatedCrest() {
        return authService;
    }

    protected CrestCharacterStatus getCharacterStatus() {
        if (null == this.loginService) {
            throw new IllegalStateException("No login service");
        }
        if (null == this.token) {
            throw new IllegalStateException("Not authenticated");
        }
        if (this.token.expiresOn() < System.currentTimeMillis()) {
            try {
                return setNewToken(obtainFromRefresh(this.loginService, this.token.getRefreshToken()));
            }
            catch (IOException e) {
                LOG.error(e.getLocalizedMessage(), e);
                throw new IllegalStateException(e.getLocalizedMessage());
            }
        }
        try {
            return this.loginService.getVerification(token.getAccessToken()).execute().body();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage());
            try {
                return setNewToken(obtainFromRefresh(this.loginService, token.getRefreshToken()));
            }
            catch (IOException e2) {
                LOG.error(e2.getLocalizedMessage());
                throw new IllegalStateException(e.getLocalizedMessage());
            }
        }
    }

    protected final CrestToken getToken() {
        return token;
    }

    private CrestCharacterStatus setNewToken(final CrestToken token) throws IOException {
        this.token = token;
        if (null == token) {
            this.authService = CrestRetrofit.newClient(this.crestHost, null).create(AuthenticatedService.class);
            return null;
        }
        this.authService = CrestRetrofit.newClient(this.crestHost, token.getAccessToken()).create(AuthenticatedService.class);
        return this.loginService.getVerification(token.getAccessToken()).execute().body();
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
