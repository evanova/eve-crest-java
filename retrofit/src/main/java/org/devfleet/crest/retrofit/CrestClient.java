package org.devfleet.crest.retrofit;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.devfleet.crest.CrestService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CrestClient {

    private static final String[] SCOPES = {
            "publicData",
            "characterContactsRead",
            "characterContactsWrite",
            "characterFittingsRead",
            "characterFittingsWrite",
            "characterNavigationWrite"
    };

    private static final String TQ_LOGIN = "login.eveonline.com";
    private static final String TQ_CREST = "crest-tq.eveonline.com";

    private static final String SISI_LOGIN = "sisilogin.testeveonline.com";
    private static final String SISI_CREST = "api-sisi.testeveonline.com";

    public static final class Builder {

        private final List<String> scopes;
        private String loginHost = TQ_LOGIN;
        private String crestHost = SISI_LOGIN;

        private String clientID;
        private String clientKey;
        private String clientRedirect = "http://localhost/redirect";

        public Builder() {
            this.scopes = new ArrayList<>();
            this.scopes.add("publicData");
        }

        public Builder login(final String host) {
            this.loginHost = host;
            return this;
        }

        public Builder api(final String host) {
            this.crestHost = host;
            return this;
        }

        public Builder id(final String clientID) {
            this.clientID = clientID;
            return this;
        }

        public Builder key(final String clientKey) {
            this.clientKey = clientKey;
            return this;
        }

        public Builder redirect(final String to) {
            this.clientRedirect = to;
            return this;
        }

        public Builder all() {
            return scopes(SCOPES);
        }

        public Builder scopes(final String... scopes) {
            this.scopes.clear();
            this.scopes.addAll(Arrays.asList(scopes));
            return this;
        }

        public final CrestClient build() {
            return new CrestClient(
                    this.loginHost,
                    this.crestHost,
                    this.clientID,
                    this.clientKey,
                    this.clientRedirect,
                    this.scopes.toArray(new String[this.scopes.size()]));
        }
    }

    private final String clientID;
    private final String clientKey;
    private final String loginHost;
    private final String crestHost;

    private final String loginUri;

    private CrestClient(
            final String loginHost,
            final String crestHost,
            final String clientID,
            final String clientKey,
            final String clientRedirect,
            final String[] scopes) {
        Validate.isTrue(StringUtils.isNotBlank(loginHost), "loginHost cannot be empty.");
        Validate.isTrue(StringUtils.isNotBlank(crestHost), "crestHost cannot be empty.");
        Validate.isTrue(StringUtils.isNotBlank(clientID), "clientID cannot be empty.");
        Validate.isTrue(StringUtils.isNotBlank(clientKey), "clientKey cannot be empty.");
        Validate.isTrue(StringUtils.isNotBlank(clientRedirect), "clientRedirect cannot be empty.");

        this.loginHost = loginHost;
        this.crestHost = crestHost;
        this.clientID = clientID;
        this.clientKey = clientKey;

        StringBuilder builder = new StringBuilder();
        if (ArrayUtils.isEmpty(scopes)) {
            builder.append("publicData");
        }
        else {
            for (String scope: scopes) {
                builder.append(scope);
                builder.append("%20");
            }
        }
        this.loginUri =
                new StringBuilder()
                .append("https://")
                .append(this.loginHost)
                .append("/oauth/authorize/?response_type=code&redirect_uri=")
                .append(clientRedirect)
                .append("&client_id=")
                .append(this.clientID)
                .append("&scope=")
                .append(StringUtils.removeEnd(builder.toString(), "%20"))
                .toString();
    }

    public static Builder SISI() {
        return new Builder()
                .login(SISI_LOGIN)
                .api(SISI_CREST)
                .all();
    }

    public static Builder TQ() {
        return new Builder()
                .login(TQ_LOGIN)
                .api(TQ_CREST)
                .all();
    }

    public String getLoginUri() {
        return this.loginUri;
    }

    public CrestService fromDefault() throws IOException {
        final CrestServiceImpl service = newService();
        return service;
    }

    public CrestService fromRefreshToken(final String token) throws IOException {
        final CrestServiceImpl service = newService();
        service.setRefreshToken(token);
        return service;
    }

    public CrestService fromAuthCode(final String authCode) throws IOException {
        final CrestServiceImpl service = newService();
        service.setAuthCode(authCode);
        return service;
    }

    private CrestServiceImpl newService() throws IOException {
        return new CrestServiceImpl(
                this.loginHost,
                this.crestHost,
                this.clientID,
                this.clientKey);
    }

}
