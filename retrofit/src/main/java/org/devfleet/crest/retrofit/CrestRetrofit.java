package org.devfleet.crest.retrofit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

final class CrestRetrofit {
    private static final Logger LOG = LoggerFactory.getLogger(CrestRetrofit.class);

    private static final String AGENT = "FleetMob (https://github.com/evanova/eve-fleet-mob)";

    private static final class LoginInterceptor implements  Interceptor {
        private final String auth;
        private final String host;

        public LoginInterceptor(String host, String auth) {
            this.host = host;
            this.auth = auth;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            Request.Builder builder = request.newBuilder()
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Host", host);

            if (request.header("Authorization") == null || request.header("Authorization").equals("")) {
                builder.addHeader("Authorization", "Basic " + auth);
            }
            builder.addHeader("User-Agent", AGENT);
            return chain.proceed(builder.build());
        }
    };

    private static final class ClientInterceptor implements  Interceptor {
        private final String token;
        private final String host;
        public ClientInterceptor(String host, String token) {
            this.host = host;
            this.token = token;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder =
                    chain
                    .request()
                    .newBuilder()
                    .addHeader("Host", host)
                    .addHeader("User-Agent", AGENT);
            if (StringUtils.isNotBlank(token)) {
                builder.addHeader("Authorization", "Bearer " + token);
            }
            return chain.proceed(builder.build());
        }
    }


    private CrestRetrofit() {}

    public static Retrofit newLogin(final String loginHost, final String clientID, final String clientKey) {
        return newRetrofit(
                newBuilder()
                .addInterceptor(new LoginInterceptor(loginHost, toAuth(clientID, clientKey)))
                .build(),
                "https://" + loginHost + "/");
    }


    public static Retrofit newClient(final String crest) {
        return newClient(crest, null);
    }

    public static Retrofit newClient(final String crest, final String token) {
        return newRetrofit(newBuilder()
                .addInterceptor(new ClientInterceptor(crest, token))
                .build(),
                "https://" + crest + "/");
    }

    private static OkHttpClient.Builder newBuilder() {
        OkHttpClient.Builder bob = new OkHttpClient.Builder();
        if (LOG.isDebugEnabled()) {
            bob.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        return bob;
    }

    private static Retrofit newRetrofit(final OkHttpClient client, final String baseUrl) {
        return
            new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build();
    }

    private static String toAuth(final String clientID, final String clientKey) {
        try {
            final byte[] bytes = new StringBuilder()
                .append(clientID)
                .append(":")
                .append(clientKey)
                .toString()
                .getBytes("UTF-8");
            return Base64.encodeToString(bytes, Base64.DEFAULT).replaceAll("(\\r|\\n)", "");
        }
        catch (UnsupportedEncodingException e) {
            //cannot happen
            throw new RuntimeException(e);
        }
    }
}
