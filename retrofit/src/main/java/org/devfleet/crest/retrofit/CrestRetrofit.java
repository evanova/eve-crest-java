package org.devfleet.crest.retrofit;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.devfleet.crest.CrestService;
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
import org.devfleet.crest.model.CrestToken;
import org.devfleet.crest.model.CrestType;
import org.devfleet.crest.model.CrestWaypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class CrestRetrofit implements CrestService {

    interface VerifyService {
        @GET("/oauth/verify")
        Call<CrestCharacterStatus> getVerification(@Header("Authorization") String token);
    }

    private static final Logger LOG = LoggerFactory.getLogger(CrestRetrofit.class);

    private static final String AGENT = "eve-crest-java (https://github.com/evanova/eve-crest-java)";

    private static final class ClientInterceptor implements Interceptor {
        private final CrestRetrofit cr;

        public ClientInterceptor(CrestRetrofit cr) {
            this.cr = cr;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder =
                    chain
                    .request()
                    .newBuilder();
            final CrestToken token = cr.store.get(cr.refresh);
            if (null != token) {
                builder.addHeader("Authorization", "Bearer " + token.getAccessToken());
            }
            return chain.proceed(builder.build());
        }
    }

    private static final class UserAgentInterceptor implements  Interceptor {
        private final String host;
        private final String agent;

        public UserAgentInterceptor(String host, String agent) {
            this.host = host;
            this.agent = agent;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder =
                    chain
                        .request()
                        .newBuilder()
                        .addHeader("Host", host)
                        .addHeader("User-Agent", agent);
            return chain.proceed(builder.build());
        }
    }

    private final CrestRetrofitService retrofit;
    private final VerifyService verify;

    private final String host;

    private final OAuth20Service oAuth;
    private final CrestStore store;
    private final String refresh;

    protected CrestRetrofit(
            final String host,
            final String login,
            final OAuth20Service oAuth,
            final CrestStore store) {
        this(host, login, oAuth, store, null, AGENT);
    }

    protected CrestRetrofit(
            final String host,
            final String login,
            final OAuth20Service oAuth,
            final CrestStore store,
            final String refresh) {
        this(host, login, oAuth, store, refresh, AGENT);
    }

    protected CrestRetrofit(
            final String host,
            final String login,
            final OAuth20Service oAuth,
            final CrestStore store,
            final String refresh,
            final String agent) {

        Validate.isTrue(StringUtils.isNotBlank(host), "host parameter cannot be empty.");
        Validate.isTrue(StringUtils.isNotBlank(login), "login parameter cannot be empty.");
        Validate.isTrue(StringUtils.isNotBlank(agent), "agent parameter cannot be empty.");

        Validate.notNull(oAuth, "oAuth parameter cannot be null.");
        Validate.notNull(store, "store parameter cannot be null.");

        this.host = host;
        this.store = store;
        this.refresh = refresh;
        this.oAuth = oAuth;

        OkHttpClient.Builder retrofitClient =
                new OkHttpClient.Builder()
                        .addInterceptor(new UserAgentInterceptor(host, agent))
                        .addInterceptor(new ClientInterceptor(this));
        if (LOG.isDebugEnabled()) {
            retrofitClient = retrofitClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        this.retrofit =
                new Retrofit.Builder()
                .baseUrl("https://" + host + "/")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(retrofitClient.build())
                .build()
                .create(CrestRetrofitService.class);

        OkHttpClient.Builder verifyClient =
                new OkHttpClient.Builder()
                        .addInterceptor(new UserAgentInterceptor(login, agent));
        if (LOG.isDebugEnabled()) {
            verifyClient = verifyClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        this.verify =
                new Retrofit.Builder()
                .baseUrl("https://" + login + "/")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(verifyClient.build())
                .build()
                .create(VerifyService.class);
    }

    @Override
    public final CrestServerStatus getServerStatus() {
        try {
            return this.retrofit.getServerStatus().execute().body();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public final CrestSolarSystem getSolarSystem(long solarSystemId) {
        try {
            return this.retrofit.getSolarSystem(solarSystemId).execute().body();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public CrestType getInventoryType (int typeId) {
        try {
            return this.retrofit.getInventoryType(typeId).execute().body();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public final List<CrestSolarSystem> getLocations() {
        try {
            return this.retrofit.getSolarSystems().execute().body().getItems();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public final List<CrestItem> getRegions ( ) {
        try {
            final List<CrestItem> returned = new ArrayList<>();
            CrestDictionary<CrestItem> dictionary;
            int page = 0;
            do {
                page = page + 1;
                dictionary = this.retrofit.getRegions().execute().body();
                if (null == dictionary) {
                    LOG.error("getRegions: null dictionary");
                    break;
                }
                returned.addAll(dictionary.getItems());
            } while (dictionary.getPageCount() > page);
            return returned;
        } catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public final CrestCharacterStatus getCharacterStatus() {
        try {
            return verifyCharacterStatus();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public final CrestCharacter getCharacter() {
        try {
            final CrestCharacterStatus status = verifyCharacterStatus();
            final CrestCharacter character = this.retrofit.getCharacter(status.getCharacterID()).execute().body();
            return character;
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public final CrestLocation getLocation() {
        try {
            final CrestCharacterStatus status = verifyCharacterStatus();
            return this.retrofit.getLocation(status.getCharacterID()).execute().body();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public final List<CrestContact> getContacts() {
        try {
            final CrestCharacterStatus status = verifyCharacterStatus();
            final List<CrestContact> returned = new ArrayList<>();

            CrestDictionary<CrestContact> dictionary;
            int page = 0;
            do {
                page = page + 1;
                dictionary = this.retrofit.getContacts(status.getCharacterID(), page).execute().body();
                returned.addAll(dictionary.getItems());
            }
            while ((null != dictionary) && dictionary.getPageCount() > page);

            return returned;
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public final CrestContact getContact(long contactID) {
        try {
            final CrestCharacterStatus status = verifyCharacterStatus();
            return this.retrofit.getContact(status.getCharacterID(), contactID).execute().body();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public final boolean addContact(CrestContact contact) {
        try {
            final CrestCharacterStatus status = verifyCharacterStatus();

            contact.setBlocked(null);
            contact.setWatched(null);
            contact.setHref(null);
            contact.setCharacter(null);

            final CrestItem sudo = new CrestItem();
            sudo.setId(contact.getContact().getId());
            sudo.setHref(href("characters/" + contact.getContact().getId()));
            sudo.setName("");
            contact.setContact(sudo);

            return this.retrofit.postContact(status.getCharacterID(), contact).execute().isSuccessful();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override
    public final boolean deleteContact(long contactID) {
        try {
            final CrestCharacterStatus status = verifyCharacterStatus();
            return this.retrofit.deleteContact(status.getCharacterID(), contactID).execute().isSuccessful();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override
    public final List<CrestFitting> getFittings() {
        try {
            final CrestCharacterStatus status = verifyCharacterStatus();

            final List<CrestFitting> returned = new ArrayList<>();
            CrestDictionary<CrestFitting> dictionary;
            int page = 0;
            do {
                page = page + 1;
                dictionary = this.retrofit.getFittings(status.getCharacterID(), page).execute().body();
                if (null == dictionary) {
                    LOG.error("getFittings: null dictionary");
                }
                else {
                    returned.addAll(dictionary.getItems());
                }
            }
            while ((null != dictionary) && dictionary.getPageCount() > page);

            return returned;
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public final boolean addFitting(CrestFitting fitting) {
        try {
            final CrestCharacterStatus status = verifyCharacterStatus();
            return this.retrofit.postFitting(status.getCharacterID(), fitting).execute().isSuccessful();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override
    public final boolean deleteFitting(long fittingID) {

        try {
            final CrestCharacterStatus status = verifyCharacterStatus();
            return this.retrofit.deleteFitting(status.getCharacterID(), fittingID).execute().isSuccessful();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override
    public final boolean addWaypoints(List<CrestWaypoint> waypoints) {
        return setWaypoints(waypoints, false);
    }

    @Override
    public final boolean setWaypoints(final List<CrestWaypoint> waypoints) {
        return setWaypoints(waypoints, true);
    }

    private boolean setWaypoints(final List<CrestWaypoint> waypoints, final boolean replace) {
        Validate.isTrue(!waypoints.isEmpty(), "Waypoint list cannot be empty.");

        try {
            final CrestCharacterStatus status = verifyCharacterStatus();

            boolean first = replace;
            for (CrestWaypoint wp: waypoints) {
                wp.setClearOtherWaypoints(first);
                wp.setFirst(first);
                wp.getSolarSystem().setHref(href("/solarsystems/" + wp.getSolarSystem().getId()));
                first = false;

                if (!this.retrofit.addWaypoint(status.getCharacterID(), wp).execute().isSuccessful()) {
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
    public final List<CrestMarketHistory> getMarketHistory(long regionId, long itemId) {
        try {
            final List<CrestMarketHistory> returned = new ArrayList<>();
            CrestDictionary<CrestMarketHistory> dictionary;

            final String typePath = href("inventory/types") + itemId + "/";
            dictionary = this.retrofit.getMarketHistory(regionId, typePath).execute().body();
            if (null == dictionary) {
                LOG.error("getMarketHistory: null dictionary {}, {}", regionId, itemId);
            }
            returned.addAll(dictionary.getItems());
            return returned;
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public final List<CrestMarketOrder> getMarketOrders(final long regionId, final String orderType, final long itemId) {
        try {
            CrestDictionary<CrestMarketOrder> dictionary;

            // TODO: Change this so that it pulls the url from the root CREST
            // endpoint
            final String typePath = href("inventory/types") + itemId + "/";

            dictionary = this.retrofit.getMarketOrders(regionId, orderType, typePath).execute().body();

            return dictionary.getItems();
        } catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public final List<CrestMarketBulkOrder> getAllMarketOrders(final long regionId) {
        try {
            final List<CrestMarketBulkOrder> returned = new ArrayList<>();
            CrestDictionary<CrestMarketBulkOrder> dictionary;
            int page = 0;
            do {
                page = page + 1;
                dictionary = retrofit.getAllMarketOrders(regionId, page).execute().body();
                if (null == dictionary) {
                    LOG.error("getAllMarketOrders: null dictionary {}", regionId);
                    break;
                }
                returned.addAll(dictionary.getItems());
            } while (dictionary.getPageCount() > page);
            return returned;
        } catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public final List<CrestMarketPrice> getAllMarketPrices() {
        try {
            final List<CrestMarketPrice> returned = new ArrayList<>();

            CrestDictionary<CrestMarketPrice> dictionary;
            int page = 0;
            do {
                page = page + 1;
                dictionary = retrofit.getAllMarketPrices(page).execute().body();
                if (null == dictionary) {
                    LOG.error("getAllMarketPrices: null dictionary {}", page);
                }
                returned.addAll(dictionary.getItems());
            } while (dictionary.getPageCount() > page);
            return returned;
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage());
            return null;
        }
    }

    private String href(String path) {
        return new StringBuilder()
                .append("https://")
                .append(this.host)
                .append("/")
                .append(path)
                .append("/")
                .toString();
    }

    private CrestCharacterStatus verifyCharacterStatus() throws IOException {
        return verifyCharacterStatus(true);
    }

    private CrestCharacterStatus verifyCharacterStatus(boolean retry) throws IOException {
        if (StringUtils.isBlank(this.refresh)) {
            throw new IOException("No refresh token available");
        }

        CrestToken stored = this.store.get(this.refresh);
        if (null == stored) {
            stored = new CrestToken().setRefreshToken(this.refresh);
            this.store.save(stored);
        }

        if (StringUtils.isBlank(stored.getAccessToken())) {
            updateToken(stored, this.oAuth.refreshAccessToken(this.refresh));
            this.store.save(stored);
        }

        retrofit2.Response<CrestCharacterStatus> r = this.verify.getVerification("Bearer " + stored.getAccessToken()).execute();
        if (r.isSuccessful()) {
            return r.body();
        }
        if (r.errorBody().string().contains("invalid_token")) {
            updateToken(stored, this.oAuth.refreshAccessToken(stored.getRefreshToken()));
            this.store.save(stored);
            if (retry) {
                return verifyCharacterStatus(false);
            }
        }
        throw new IOException(r.message());
    }

    private static void updateToken(final CrestToken token, OAuth2AccessToken with) {
        LOG.debug("updateToken {} -> {} ", token.getAccessToken(), with.getAccessToken());
        token.setAccessToken(with.getAccessToken());
        token.setExpiresIn(with.getExpiresIn());
        token.setScope(with.getScope());
        token.setTokenType(with.getTokenType());
    }
}
