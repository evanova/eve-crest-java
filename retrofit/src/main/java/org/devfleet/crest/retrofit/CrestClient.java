package org.devfleet.crest.retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.devfleet.crest.CrestService;
import org.devfleet.crest.model.CrestToken;

public final class CrestClient {
//characterFittingsRead
    //characterLocationRead
    //characterLoyaltyPointsRead
    ///characterOpportunitiesRead
    ///characterStatsRead
    //fleetread
    //structureVulnUpdate
    private static final Map<String, Long> MASKS;
    static {
        MASKS = new HashMap<>();
        MASKS.put("characterAccountRead", 33554432l);
        MASKS.put("characterStatsRead", 16777216l);
        MASKS.put("characterWalletRead", 1l | 2097152l | 4194304l);
        MASKS.put("characterAssetsRead", 2l | 134217728l);
        MASKS.put("characterCalendarRead", 4l | 1048576l);
        MASKS.put("characterContactsRead", 16l | 32l | 524288l);
        MASKS.put("characterFactionalWarfareRead", 64l);
        MASKS.put("characterIndustryJobsRead", 128l);
        MASKS.put("characterKillsRead", 256l);
        MASKS.put("characterMailRead", 512l | 1024l | 2048l);
        MASKS.put("characterMarketOrdersRead", 4096l);
        MASKS.put("characterMedalsRead", 8192l);
        MASKS.put("characterNotificationsRead", 16384l | 32768l);
        MASKS.put("characterResearchRead", 65536l);
        MASKS.put("characterSkillsRead", 131072l | 262144l | 1073741824l);
        MASKS.put("characterAccountRead", 33554432l);
        MASKS.put("characterContractsRead", 67108864l);
        MASKS.put("characterBookmarksRead", 268435456l);
        MASKS.put("characterChatChannelsRead", 536870912l);
        MASKS.put("characterClonesRead", 2147483648l);

        MASKS.put("corporationWalletRead", 1l | 8l | 1048576l | 2097152l);
        MASKS.put("corporationAssetsRead", 2l | 32l | 16777216l);
        MASKS.put("corporationMedalsRead", 4l | 8192l);
        MASKS.put("corporationContactsRead", 16l | 262144l);
        MASKS.put("corporationFactionalWarfareRead", 64l);
        MASKS.put("corporationIndustryJobsRead", 128l);
        MASKS.put("corporationKillsRead", 256l);
        MASKS.put("corporationMembersRead", 512l | 1024l | 2048l | 4194304l | 33554432l);
        MASKS.put("corporationMarketOrdersRead", 4096l);
        MASKS.put("corporationStructuresRead", 16384l | 32768l | 131072l);
        MASKS.put("corporationShareholdersRead", 65536l);
        MASKS.put("corporationContractsRead", 8388608l);
        MASKS.put("corporationBookmarksRead", 67108864l);
    };

    public static final String[] PUBLIC_SCOPES = {
            "publicData"
    };

    public static final String[] CHARACTER_SCOPES = {
            "characterAccountRead",
            "characterAssetsRead",
            "characterBookmarksRead",
            "characterCalendarRead",
            "characterChatChannelsRead",
            "characterClonesRead",
            "characterContactsRead",
            "characterContactsWrite",
            "characterContractsRead",
            "characterFactionalWarfareRead",
            "characterFittingsRead",
            "characterFittingsWrite",
            "characterIndustryJobsRead",
            "characterKillsRead",
            "characterLocationRead",
            "characterLoyaltyPointsRead",
            "characterMailRead",
            "characterMarketOrdersRead",
            "characterMedalsRead",
            "characterNavigationWrite",
            "characterNotificationsRead",
            "characterOpportunitiesRead",
            "characterResearchRead",
            "characterSkillsRead",
            "characterStatsRead",
            "characterWalletRead",
            "fleetRead",
            "fleetWrite",
            "structureVulnUpdate"
    };

    public static final String[] CORPORATION_SCOPES = {
            "corporationAssetRead",
            "corporationBookmarksRead",
            "corporationContractsRead",
            "corporationFactionalWarfareRead",
            "corporationIndustryJobsRead",
            "corporationKillsRead",
            "corporationMarketOrdersRead",
            "corporationMedalsRead",
            "corporationMembersRead",
            "corporationShareholdersRead",
            "corporationStructuresRead",
            "corporationWalletRead",
            "structureVulnUpdate"
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
            this.scopes.addAll(Arrays.asList(PUBLIC_SCOPES));
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

        public Builder scopes(final String... scopes) {
            for (String s: scopes) {
                if (!this.scopes.contains(s)) {
                    this.scopes.add(s);
                }
            }
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

    private final long characterAccessMask;
    private final long corporationAccessMask;

    private CrestClient(
            final String loginHost,
            final String crestHost,
            final String clientID,
            final String clientKey,
            final String clientRedirect,
            final String[] scopes) {
        Validate.isTrue(StringUtils.isNotBlank(loginHost), "loginHost cannot be empty.");
        Validate.isTrue(StringUtils.isNotBlank(crestHost), "crestHost cannot be empty.");

        this.loginHost = loginHost;
        this.crestHost = crestHost;
        this.clientID = clientID;
        this.clientKey = clientKey;

        this.loginUri = getLoginUri(
                    loginHost,
                    clientID,
                    clientKey,
                    clientRedirect,
                    scopes);
        this.characterAccessMask = toAccessMask(scopes, CHARACTER_SCOPES);
        this.corporationAccessMask = toAccessMask(scopes, CORPORATION_SCOPES);
    }

    public static Builder SISI() {
        return new Builder()
                .login(SISI_LOGIN)
                .api(SISI_CREST)
                .scopes(CORPORATION_SCOPES)
                .scopes(CHARACTER_SCOPES);
    }

    public static Builder SISI(String... scopes) {
        return new Builder()
                .login(SISI_LOGIN)
                .api(SISI_CREST)
                .scopes(scopes);
    }

    public static Builder TQ(String... scopes) {
        return new Builder()
                .login(TQ_LOGIN)
                .api(TQ_CREST)
                .scopes(scopes);
    }

    public static Builder TQ() {
        return new Builder()
                .login(TQ_LOGIN)
                .api(TQ_CREST)
                .scopes(CORPORATION_SCOPES)
                .scopes(CHARACTER_SCOPES);
    }

    public static String getLoginUri(
             final String loginHost,
             final String clientID,
             final String clientKey,
             final String clientRedirect,
             final String[] scopes) {

        if (StringUtils.isBlank(clientID) || StringUtils.isBlank(clientKey) || StringUtils.isBlank(clientRedirect)) {
            return null;
        }

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

        return
            new StringBuilder()
            .append("https://")
            .append(loginHost)
            .append("/oauth/authorize/?response_type=code&redirect_uri=")
            .append(clientRedirect)
            .append("&client_id=")
            .append(clientID)
            .append("&scope=")
            .append(StringUtils.removeEnd(builder.toString(), "%20"))
            .toString();
    }

    public String getLoginUri() {
        return this.loginUri;
    }

    public long getCharacterAccessMask() {
        return characterAccessMask;
    }

    public long getCorporationAccessMask() {
        return corporationAccessMask;
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

    public CrestService fromAccessToken(final CrestToken token) throws IOException {
        final CrestServiceImpl service = newService();
        service.setAccessToken(token);
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

    private static long toAccessMask(final String[] scopes, final String[] ref) {
        long mask = 0;
        for (String scope: scopes) {
            if (ArrayUtils.contains(ref, scope)) {
                Long value = MASKS.get(scope);
                if (null != value) {
                    mask = mask | value;
                }
            }
        }
        return mask;
    }
}
