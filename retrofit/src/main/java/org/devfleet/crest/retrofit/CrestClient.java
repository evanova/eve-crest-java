package org.devfleet.crest.retrofit;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.devfleet.crest.CrestAccess;
import org.devfleet.crest.CrestService;
import org.devfleet.crest.model.CrestToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CrestClient {

    private static final CrestStore STORE = new CrestStore() {
        private Map<String, CrestToken> map = new HashMap<>();

        @Override
        public void save(CrestToken token) {
            this.map.put(token.getRefreshToken(), token);
        }

        @Override
        public void delete(String refresh) {
            this.map.remove(refresh);
        }

        @Override
        public CrestToken get(String refresh) {
            return this.map.get(refresh);
        }
    };

    private static final Logger LOG = LoggerFactory.getLogger(CrestClient.class);

    private static final class CrestOAuth extends DefaultApi20 {
        private final String loginHost;

        public CrestOAuth(String loginHost) {
            this.loginHost = loginHost;
        }

        @Override
        public String getAccessTokenEndpoint() {
            return "https://"  + loginHost + "/oauth/token";
        }

        @Override
        protected String getAuthorizationBaseUrl() {
            return "https://"  + loginHost + "/oauth/authorize";
        }
    }

    private static final String TQ_LOGIN = "login.eveonline.com";
    private static final String TQ_CREST = "crest-tq.eveonline.com";

    private static final String SISI_LOGIN = "sisilogin.testeveonline.com";
    private static final String SISI_CREST = "api-sisi.testeveonline.com";

    public static final class Builder {

        private final List<String> scopes;
        private CrestStore store = STORE;

        private String loginHost = TQ_LOGIN;
        private String crestHost = SISI_LOGIN;

        private String clientID;
        private String clientKey;
        private String clientRedirect = "http://localhost/redirect";

        private String userAgent = null;

        public Builder() {
            this.scopes = new ArrayList<>();
            this.scopes.addAll(Arrays.asList(CrestAccess.PUBLIC_SCOPES));
        }

        public CrestClient.Builder store(final CrestStore store) {
            this.store = store;
            return this;
        }

        public CrestClient.Builder login(final String host) {
            this.loginHost = host;
            return this;
        }

        public CrestClient.Builder api(final String host) {
            this.crestHost = host;
            return this;
        }

        public CrestClient.Builder id(final String clientID) {
            this.clientID = clientID;
            return this;
        }

        public CrestClient.Builder key(final String clientKey) {
            this.clientKey = clientKey;
            return this;
        }

        public CrestClient.Builder redirect(final String to) {
            this.clientRedirect = to;
            return this;
        }

        public CrestClient.Builder scopes(final String... scopes) {
            for (String s: scopes) {
                if (!this.scopes.contains(s)) {
                    this.scopes.add(s);
                }
            }
            return this;
        }

        public CrestClient.Builder agent(final String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public final CrestClient build() {
            return new CrestClient(
                    this.loginHost,
                    this.crestHost,
                    this.clientID,
                    this.clientKey,
                    this.clientRedirect,
                    this.userAgent,
                    this.store,
                    this.scopes.toArray(new String[this.scopes.size()]));
        }
    }

    private final OAuth20Service oAuth;
    private final String crestHost;
    private final String loginHost;

    private final String userAgent;

    private final CrestStore store;

    private CrestClient(
            final String loginHost,
            final String crestHost,
            final String clientID,
            final String clientKey,
            final String callback,
            final String userAgent,
            final CrestStore store,
            final String... scopes) {

        this.crestHost = crestHost;
        this.loginHost = loginHost;
        this.userAgent = userAgent;

        this.store = store;

        StringBuilder scope = new StringBuilder();
        if (!ArrayUtils.isEmpty(scopes)) {
            for (String s : scopes) {
                scope.append(s);
                scope.append(" ");
            }
        }
        this.oAuth = new ServiceBuilder()
                .apiKey(clientID)
                .apiSecret(clientKey)
                .scope(StringUtils.removeEnd(scope.toString(), " "))
                .callback(callback)
                .build(new CrestOAuth(loginHost));
    }

    public static CrestClient.Builder SISI() {
        return new CrestClient.Builder()
                .login(SISI_LOGIN)
                .api(SISI_CREST);
    }

    public static CrestClient.Builder SISI(String... scopes) {
        return new CrestClient.Builder()
                .login(SISI_LOGIN)
                .api(SISI_CREST)
                .scopes(scopes);
    }

    public static CrestClient.Builder TQ(String... scopes) {
        return new CrestClient.Builder()
                .login(TQ_LOGIN)
                .api(TQ_CREST)
                .scopes(scopes);
    }

    public static CrestClient.Builder TQ() {
        return new CrestClient.Builder()
                .login(TQ_LOGIN)
                .api(TQ_CREST);
    }

    public String getAuthorizationUrl() {
        return this.oAuth.getAuthorizationUrl();
    }

    public CrestToken fromAuthCode(final String authCode) {
        try {
            final OAuth2AccessToken token = this.oAuth.getAccessToken(authCode);
            return save(token);
        }
        catch (OAuthException | IOException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    public CrestToken fromRefresh(final String refresh) {
        try {
            CrestToken existing = this.store.get(refresh);
            if ((null == existing) || (existing.getExpiresOn() < (System.currentTimeMillis() - 5 * 1000))) {
                final OAuth2AccessToken token = this.oAuth.refreshAccessToken(refresh);
                return save(token);
            }
            return existing;
        }
        catch (OAuthException | IOException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    public CrestService newCrestService() {
        return new CrestRetrofit(this.crestHost, this.loginHost, this.oAuth, this.store);
    }

    public CrestService newCrestService(final String refresh) {
        return new CrestRetrofit(this.crestHost, this.loginHost, this.oAuth, this.store, refresh, this.userAgent);
    }

    private CrestToken save(final OAuth2AccessToken token) {
        CrestToken returned =
                new CrestToken()
                        .setAccessToken(token.getAccessToken())
                        .setRefreshToken(token.getRefreshToken())
                        .setTokenType(token.getTokenType())
                        .setScope(token.getScope())
                        .setExpiresIn(token.getExpiresIn());
        this.store.save(returned);
        return returned;
    }
}
