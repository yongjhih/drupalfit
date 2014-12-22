package drupalfit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RestAdapter;
import retrofit.RequestInterceptor;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;
import com.squareup.okhttp.Request;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.io.IOException;
import java.security.SecureRandom;
import drupalfit.DrupalOAuth2.Credential;
import android.net.Uri;
import android.text.TextUtils;
import android.content.Context;

/**
 * OAuth of authentication.
 *
 */
public class DrupalOAuth2Manager {
    protected String endpoint;
    protected String clientId;
    protected String clientSecret;
    //protected String responseType = "code";
    protected String state = "state"; // modify here
    //protected String grantType = "authorization_code";
    protected String cookie;
    protected String username;
    protected String password;

    /*
     * <p>
     * TODO merge into DrupalManager about hybridauth
     * </p>
     *
     * @see hybridauth_ulogin/hybridauth_ulogin.admin.inc.
     *
     * <pre>
     * "vkontakte" => "Vkontakte",
     * "odnoklassniki" => "Odnoklassniki",
     * "mailru" => "Mailru",
     * "facebook" => "Facebook",
     * "twitter" => "Twitter",
     * "google" => "Google",
     * "yandex" => "Yandex",
     * "livejournal" => "",
     * "openid" => "OpenID",
     * "lastfm" => "LastFM",
     * "linkedin" => "LinkedIn",
     * "liveid" => "Live",
     * "soundcloud" => "",
     * "steam" => "Steam",
     * "flickr" => "",
     * "vimeo" => "",
     * "youtube" => "",
     * "webmoney" => "",
     * </pre>
     *
     * @see additional-providers/hybridauth-/Providers/
     *
     * <pre>
     * px500
     * Deezer
     * Disqus
     * Draugiem
     * DrupalOAuth2
     * Freeagent
     * GitHub
     * Goodreads
     * Google
     * Identica
     * Instagram
     * LastFM
     * Latch
     * Mailru
     * Murmur
     * Odnoklassniki
     * PaypalOpenID
     * Paypal
     * PixelPin
     * Pixnet
     * Plurk
     * QQ
     * Sina
     * Skyrock
     * Steam
     * Tumblr
     * TwitchTV
     * Viadeo
     * Vimeo
     * Vkontakte
     * XING
     * Yahoo
     * Yammer
     * Yandex
     * </pre>
     */

    public static final String DEEZER        = "Deezer";
    public static final String DISQUS        = "Disqus";
    public static final String DRAUGIEM      = "Draugiem";
    public static final String DRUPALOAUTH2  = "DrupalOAuth2";
    public static final String FACEBOOK      = "Facebook";
    public static final String FLICKR        = "flickr";
    public static final String FREEAGENT     = "Freeagent";
    public static final String GITHUB        = "GitHub";
    public static final String GOODREADS     = "Goodreads";
    public static final String GOOGLE        = "Google";
    public static final String IDENTICA      = "Identica";
    public static final String INSTAGRAM     = "Instagram";
    public static final String LASTFM        = "LastFM";
    public static final String LATCH         = "Latch";
    public static final String LINKEDIN      = "LinkedIn";
    public static final String LIVEJOURNAL   = "livejournal";
    public static final String LIVE          = "Live";
    public static final String MAILRU        = "Mailru";
    public static final String MURMUR        = "Murmur";
    public static final String ODNOKLASSNIKI = "Odnoklassniki";
    public static final String OPENID        = "OpenID";
    public static final String PAYPALOPENID  = "PaypalOpenID";
    public static final String PAYPAL        = "Paypal";
    public static final String PIXELPIN      = "PixelPin";
    public static final String PIXNET        = "Pixnet";
    public static final String PLURK         = "Plurk";
    public static final String PX500         = "px500";
    public static final String QQ            = "QQ";
    public static final String SINA          = "Sina";
    public static final String SKYROCK       = "Skyrock";
    public static final String SOUNDCLOUD    = "soundcloud";
    public static final String STEAM         = "Steam";
    public static final String TUMBLR        = "Tumblr";
    public static final String TWITCHTV      = "TwitchTV";
    public static final String TWITTER       = "Twitter";
    public static final String VIADEO        = "Viadeo";
    public static final String VIMEO         = "vimeo";
    public static final String VKONTAKTE     = "Vkontakte";
    public static final String WEBMONEY      = "webmoney";
    public static final String XING          = "XING";
    public static final String YAHOO         = "Yahoo";
    public static final String YAMMER        = "Yammer";
    public static final String YANDEX        = "Yandex";
    public static final String YOUTUBE       = "youtube";

    protected String provider = FACEBOOK;
    protected String token;

    public static class Builder {
        String endpoint;
        String clientId;
        String clientSecret;
        String cookie;
        Context context;
        String provider = FACEBOOK;
        String token;

        public Builder() {
        }

        public Builder(Context context) {
            setContext(context);
        }

        public Builder setEndpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder setCookie(String cookie) {
            this.cookie = cookie;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setProvider(String provider) {
            this.provider = provider;
            return this;
        }

        public Builder setProvider(Context context, String provider, String token) {
            return setContext(context).setProvider(provider).setToken(token);
        }

        public DrupalOAuth2Manager build() {
            DrupalOAuth2Manager manager = new DrupalOAuth2Manager(context, endpoint, clientId, clientSecret);
            manager.setCookie(cookie);
            manager.setProvider(provider);
            manager.setToken(token);
            return manager;
        }
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    private DrupalOAuth2 mService;

    public DrupalOAuth2Manager(String endpoint) {
        this(endpoint, null, null);
    }

    public DrupalOAuth2Manager(String endpoint, String clientId) {
        this(endpoint, clientId, null);
    }

    private boolean allTrust = true;

    public void disableAllTrust() {
        allTrust = false;
    }

    public void allTrust() {
        allTrust = true;
    }

    /**
     * getAccessToken
     */
    public void getAccessToken(String username, String password, Callback<Credential> callback) {
        mService.token(
            clientId,
            clientSecret,
            "password",
            state,
            username,
            password,
            callback
        );
    }

    // DONT USE on main thread
    public Credential getAccessToken(String username, String password) {
        return mService.token(
            clientId,
            clientSecret,
            "password",
            state,
            username,
            password
        );
    }

    // DONT USE on main thread
    public Credential getAccessToken(String cookie) {
        setCookie(cookie);

        Response response = mService.authorize(
            clientId,
            clientSecret,
            "code",
            state
        );

        Uri uri = Uri.parse(response.getUrl());
        String code = uri.getQueryParameter("code");

        if (!TextUtils.isEmpty(code)) {
            return mService.token(code, clientId, clientSecret, "authorization_code", state);
        }

        return null;
    }

    public void getAccessToken(String cookie, final Callback<Credential> callback) {
        setCookie(cookie);

        final Callback<Response> authorizeCallback = new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log8.d();
                Uri uri = Uri.parse(response.getUrl());
                String code = uri.getQueryParameter("code");
                if (TextUtils.isEmpty(code)) {
                    callback.failure(RetrofitError.unexpectedError(response.getUrl(), new RuntimeException()));
                } else {
                    mService.token(code, clientId, clientSecret, "authorization_code", state, callback);
                }
            }
            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
                Log8.d(error);
            }
        };

        mService.authorize(
            clientId,
            clientSecret,
            "code",
            state,
            authorizeCallback
        );
    }

    // DONT USE on main thread
    public Credential getAccessToken() {
        if (!TextUtils.isEmpty(cookie)) {
            return getAccessToken(cookie);
        } else if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            return getAccessToken(username, password);
        } else {
            return getAccessToken(context, provider, token);
        }
    }

    public void getAccessToken(final Callback<Credential> callback) {
        if (!TextUtils.isEmpty(cookie)) {
            Log8.d();
            getAccessToken(cookie, callback);
        } else if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            Log8.d();
            getAccessToken(username, password, callback);
        } else {
            Log8.d();
            getAccessToken(context, provider, token, callback);
        }
    }

    public void getAccessToken(Context context, final Callback<Credential> callback) {
        getAccessToken(context, provider, token, callback);
    }

    public Credential getAccessToken(Context context, String provider, String token) {
        if (TextUtils.isEmpty(token)) {
            return null;
        }
        if (TextUtils.isEmpty(provider)) {
            return null;
        }
        if (context == null) {
            return null;
        }

        // TODO String cookie = getHybridauthCookie(context, provider, token)
        // return getAccessToken(cookie);
        return null;
    }

    public void getAccessToken(Context context, String provider, String token, final Callback<Credential> callback) {
        if (TextUtils.isEmpty(token)) {
            Log8.d();
            callback.failure(RetrofitError.unexpectedError("oauth://failure", new RuntimeException()));
            return;
        }
        if (TextUtils.isEmpty(provider)) {
            Log8.d();
            callback.failure(RetrofitError.unexpectedError("oauth://failure", new RuntimeException()));
            return;
        }
        if (context == null) {
            Log8.d();
            callback.failure(RetrofitError.unexpectedError("oauth://failure", new RuntimeException()));
            return;
        }

        requestHybridauthCookie(context, provider, token, new Callback<String>() {
            @Override
            public void success(String cookie, Response response) {
                Log8.d(cookie);
                getAccessToken(cookie, callback);
            }
            @Override
            public void failure(RetrofitError error) {
                Log8.d();
                callback.failure(error);
            }
        });
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Allow sign-up with access_token.
     *
     * @see <a href="https://github.com/yongjhih/drupal-hybridauth/commit/268b72a598665b0738e3b06e7b59dcb3bda5b999">Allow sign-up with access_token</a>
     */
    private void requestHybridauthCookie(Context context, String provider, String token, final Callback<String> callback) {
        if (context == null) return;
        if (TextUtils.isEmpty(token)) return;

        Uri uri = Uri.parse(endpoint);
        final String url = uri.getScheme() + "://" + uri.getAuthority() + "/hybridauth/window/" + provider + "?destination=node&destination_error=node&access_token=" + token;

        //new WebDialog(context, url, callback).show();
        Request request = new Request.Builder()
            .url(url)
            .build();
        //com.squareup.okhttp.Response response = getOkHttpClient().newCall(request).execute();
        com.squareup.okhttp.Call call = getOkHttpClient().newCall(request);
        call.enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e){
                Log8.d(e);
                callback.failure(RetrofitError.unexpectedError(url, e));
            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                String cookie = response.header("Set-Cookie");
                if (!TextUtils.isEmpty(cookie)) {
                    setCookie(cookie);
                    callback.success(cookie, (Response) null);
                } else {
                    callback.failure(RetrofitError.unexpectedError(url, new RuntimeException()));
                }
            }
        });
        //call.execute();
    }

    protected Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setProvider(Context context, String provider, String token) {
        setContext(context);
        setProvider(provider);
        setToken(token);
    }

    public DrupalOAuth2Manager(String endpoint, String clientId, String clientSecret) {
        this((Context) null, endpoint, clientId, clientSecret);
    }

    protected OkHttpClient okHttpClient;

    public OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();

            if (allTrust) {
                okHttpClient.setSslSocketFactory(getTrustedFactory());
                okHttpClient.setHostnameVerifier(getTrustedVerifier());
            }

            okHttpClient.setFollowSslRedirects(true);
        }

        return okHttpClient;
    }

    public DrupalOAuth2Manager(Context context, String endpoint, String clientId, String clientSecret) {
        setContext(context);
        setEndpoint(endpoint);
        setClientId(clientId);
        setClientSecret(clientSecret);

        Client client = new OkClient(getOkHttpClient());

        if (mRequestInterceptor == null) {
            mRequestInterceptor = new SimpleRequestInterceptor();
        }
        mRequestInterceptor.cookie = cookie;

        RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(endpoint)
            .setRequestInterceptor(mRequestInterceptor)
            .setErrorHandler(new ErrorHandler())
            .setClient(client)
            .setConverter(new retrofit.converter.JacksonConverter())
            .build();

        mService = restAdapter.create(DrupalOAuth2.class);
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;

        if (mRequestInterceptor != null) {
            mRequestInterceptor.cookie = cookie;
        }
    }

    /**
     * A simple {@link RequestInterceptor} for each request of interception
     */
    class SimpleRequestInterceptor implements RequestInterceptor {
        public String cookie;

        @Override
        public void intercept(RequestFacade request) {
            if (!android.text.TextUtils.isEmpty(cookie)) {
                request.addHeader("Cookie", cookie);
            }
        }
    }

    protected SimpleRequestInterceptor mRequestInterceptor;

    /**
     * A simple {@link retrofit.ErrorHandler}
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ErrorHandler implements retrofit.ErrorHandler {
        public ErrorHandler() {}

        @Override
        public Throwable handleError(RetrofitError cause) {
            cause.printStackTrace();
            return cause;
        }
    }

    /**
     * A all trust SSLSocketFactory
     */
    private static SSLSocketFactory sTrustedFactory;

    /**
     * A getter of all trust SSLSocketFactory
     */
    public static SSLSocketFactory getTrustedFactory() {
        if (sTrustedFactory == null) {
            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    // Intentionally left blank
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    // Intentionally left blank
                }
            } };
            try {
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, trustAllCerts, new SecureRandom());
                sTrustedFactory = context.getSocketFactory();
            } catch (GeneralSecurityException e) {
                IOException ioException = new IOException(
                        "Security exception configuring SSL context");
                ioException.initCause(e);
                e.printStackTrace();
            }
        }
        return sTrustedFactory;
    }

    /**
     * A all trust host name verifier.
     */
    private static HostnameVerifier sTrustedVerifier;

    /**
     * A getter of all trust host name verifier
     */
    public static HostnameVerifier getTrustedVerifier() {
        if (sTrustedVerifier == null) {
            sTrustedVerifier = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
        }

        return sTrustedVerifier;
    }

    public DrupalOAuth2Manager setUsername(String username) {
        this.username = username;
        return this;
    }

    public DrupalOAuth2Manager setPassword(String password) {
        this.password = password;
        return this;
    }
}
