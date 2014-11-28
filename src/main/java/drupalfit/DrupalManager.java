package drupalfit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.client.Header;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;
import retrofit.RequestInterceptor;

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
import android.text.TextUtils;
import drupalfit.DrupalOAuth2.Credential;
import android.net.Uri;
import android.content.Context;

import rx.Observable;
import rx.functions.Func1;

public class DrupalManager implements DrupalService {
    private static DrupalManager sInstance = new DrupalManager();
    private DrupalService mService;
    private String endpoint;
    private String accessToken;
    private String cookie;
    private String xcsrfToken; //X-CSRF-Token
    private String username; // TODO
    private String email; // TODO
    private String password; // TODO

    /*
     * <pre>
     * hybridauth_ulogin/hybridauth_ulogin.admin.inc
     *
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
     *
     * additional-providers/hybridauth-/Providers/
     *
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

    protected SimpleRequestInterceptor mRequestInterceptor;

    private DrupalOAuth2Manager oauth;

    protected Context context;

    public DrupalManager setOAuth(DrupalOAuth2Manager oauth) {
        this.oauth = oauth;
        return this;
    }

    public DrupalOAuth2Manager getOAuth() {
        return oauth;
    }

    public DrupalManager setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public DrupalManager setContext(Context context) {
        this.context = context;
        return this;
    }

    public DrupalManager setProvider(String provider) {
        this.provider = provider;
        return this;
    }

    public DrupalManager setProvider(Context context, String provider, String token) {
        setContext(context);
        setProvider(provider);
        setToken(token);
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DrupalManager build() {
        getService(endpoint);
        return this;
    }

    private DrupalManager() {
    }

    @Override
    public void register(
        String username,
        String email,
        String password,
        final Callback<User> callback) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
        getService().register(username, email, password, new Callback<User>() {
            @Override
            public void success(final User user, final Response response) {
                for (Header header : response.getHeaders()) {
                    if ("Set-Cookie".equalsIgnoreCase(header.getName())) {
                        setCookie(header.getValue());
                        break;
                    }
                }
                getToken(new Callback<Login>() { // Fetch a new xcsrfToken
                    @Override
                    public void success(Login l, Response r) {
                        Log8.d(l.token);
                        setXcsrfToken(l.token);
                        callback.success(user, response);
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        Log8.d("Auto-fetch new xcsrfToken failure");
                        //callback.failure(error);
                    }
                });
            }
            @Override
            public void failure(RetrofitError error) {
                Log8.d();
                callback.failure(error);
            }
        });
    }

    public void register(
        String email,
        String password,
        Callback<User> callback
    ) {
        register(email, email, password, callback);
    }

    @Override
    public void login(
        String username,
        String password,
        final Callback<Login> callback
    ) {
        setUsername(username);
        setPassword(password);
        getService().login(username, password, new Callback<Login>() {
            @Override
            public void success(final Login login, final Response response) {
                Log8.d(login.token);
                //setXcsrfToken(login.token); // Need fetch a new xcsrfToken or not?
                //callback.success(login, response);
                for (Header header : response.getHeaders()) {
                    if ("Set-Cookie".equalsIgnoreCase(header.getName())) {
                        setCookie(header.getValue());
                        break;
                    }
                }
                getToken(new Callback<Login>() { // Fetch a new xcsrfToken
                    @Override
                    public void success(Login l, Response r) {
                        Log8.d(l.token);
                        setXcsrfToken(l.token);
                        callback.success(login, response);
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        Log8.d("Auto-fetch new xcsrfToken failure");
                        //callback.failure(error);
                    }
                });
            }
            @Override
            public void failure(RetrofitError error) {
                Log8.d();
                callback.failure(error);
            }
        });
    }

    @Override
    public void getProfile(
        String accessToken,
        final Callback<User> callback
    ) {
        if (!TextUtils.isEmpty(this.accessToken) && !this.accessToken.equals(accessToken)) {
            setAccessToken(accessToken);
            Log8.d(accessToken);
        }

        getService().getProfile(accessToken, callback);
    }

    private void syncOAuth() {
        if (oauth != null) {
            oauth.setUsername(username);
            //oauth.setEmail(email);
            oauth.setPassword(password);
            oauth.setCookie(cookie);
        }
    }

    @Override
    public void getProfile(
        final Callback<User> callback
    ) {
        if (cookie == null && accessToken == null) {
            if (oauth != null) {
                syncOAuth();
                oauth.getAccessToken(new Callback<Credential>() {
                    @Override
                    public void success(Credential credential, Response response) {
                        setAccessToken(credential.access_token);
                        Log8.d(accessToken);
                        getService().getProfile(callback);
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        Log8.d();
                        callback.failure(error);
                    }
                });
            } else if (TextUtils.isEmpty(cookie) && context != null && !TextUtils.isEmpty(provider) && !TextUtils.isEmpty(token)) {
                getCookie(context, provider, token, new Callback<String>() {
                    @Override
                    public void success(String cookie, Response response) {
                        Log8.d(cookie);
                        setCookie(cookie);
                        if (TextUtils.isEmpty(xcsrfToken)) {
                            getToken(new Callback<Login>() {
                                @Override
                                public void success(Login login, Response response) {
                                    Log8.d(login.token);
                                    setXcsrfToken(login.token);
                                    getService().getProfile(callback);
                                }
                                @Override
                                public void failure(RetrofitError error) {
                                    Log8.d();
                                    callback.failure(error);
                                }
                            });
                        } else {
                            getService().getProfile(callback);
                        }
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        Log8.d();
                        callback.failure(error);
                    }
                });
            }
        } else {
            Log8.d();
            getService().getProfile(callback);
        }
    }

    @Override
    public void logout(
        Callback<Logout> callback
    ) {
        getService().logout(callback);
    }

    public static DrupalManager get() {
        return sInstance;
    }

    public static DrupalService getService() {
        return get().getService(null);
    }

    /* DONT USE on main thread */
    public String getAccessTokenWithNetwork() {
        if (!TextUtils.isEmpty(accessToken)) {
            return accessToken;
        }

        if (oauth != null) {
            syncOAuth();
            Credential c = oauth.getAccessToken();
            if (c != null) {
                setAccessToken(c.access_token);
            }
        }

        return accessToken;
    }

    public String getAccessToken() {
        if (!TextUtils.isEmpty(accessToken)) {
            return accessToken;
        }

        return accessToken;
    }

    /* DONT USE on main thread */
    public String getAccessToken(String username, String password) {
        return getAccessToken(username, password, (String) null);
    }

    /* DONT USE on main thread */
    public String getAccessToken(String username, String password, String authTokenType) {
        syncOAuth();
        Credential c = oauth.getAccessToken(username, password);
        if (c != null) {
            setAccessToken(c.access_token);
        }
        return accessToken;
    }

    public void getAccessToken(final Callback<Credential> callback) {
        if (oauth != null) {
            syncOAuth();
            oauth.getAccessToken(new Callback<Credential>() {
                @Override
                public void success(Credential credential, Response response) {
                    setAccessToken(credential.access_token);
                    callback.success(credential, response);
                }
                @Override
                public void failure(RetrofitError error) {
                    callback.failure(error);
                }
            });
        }
    }

    public void getAccessToken(String username, String password, final Callback<Credential> callback) {
        getAccessToken(username, password, (String) null, callback);
    }

    public void getAccessToken(String username, String password, String authTokenType, final Callback<Credential> callback) {
        if (oauth != null) {
            syncOAuth();
            oauth.getAccessToken(username, password, new Callback<Credential>() {
                @Override
                public void success(Credential credential, Response response) {
                    setAccessToken(credential.access_token);
                    callback.success(credential, response);
                }
                @Override
                public void failure(RetrofitError error) {
                    callback.failure(error);
                }
            });
        }
    }

    public DrupalManager setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        Log8.d(accessToken);

        if (mRequestInterceptor != null) {
            mRequestInterceptor.accessToken = accessToken;
        }

        return this;
    }

    public DrupalService getService(String endpoint) {
        if (endpoint == null) return mService;

        if (mService == null) {
            this.endpoint = endpoint;
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(getTrustedFactory());
            okHttpClient.setHostnameVerifier(getTrustedVerifier());
            Client client = new OkClient(okHttpClient);

            if (mRequestInterceptor == null) {
                mRequestInterceptor = new SimpleRequestInterceptor();
            }
            mRequestInterceptor.cookie = cookie;
            mRequestInterceptor.accessToken = accessToken;
            mRequestInterceptor.xcsrfToken = xcsrfToken;

            mService = new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setRequestInterceptor(mRequestInterceptor)
                .setErrorHandler(new ErrorHandler())
                .setClient(client)
                .setConverter(new retrofit.converter.JacksonConverter())
                .build().create(DrupalService.class);
        }

        return mService;
    }

    public String getCookie() {
        return cookie;
    }

    public DrupalManager setCookie(String cookie) {
        this.cookie = cookie;

        if (mRequestInterceptor != null) {
            mRequestInterceptor.cookie = cookie;
        }
        if (oauth != null) {
            oauth.setCookie(cookie);
        }

        return this;
    }

    /**
     * Allow sign-up with access_token.
     *
     * @see <a href="https://github.com/yongjhih/drupal-hybridauth/commit/268b72a598665b0738e3b06e7b59dcb3bda5b999">Allow sign-up with access_token</a>
     */
    private void getCookie(Context context, String provider, String token, Callback<String> callback) {
        if (context == null) return;
        if (TextUtils.isEmpty(token)) return;

        Uri uri = Uri.parse(endpoint);

        new WebDialog(context, uri.getScheme() + "://" + uri.getAuthority() + "/hybridauth/window/" + provider + "?destination=node&destination_error=node&access_token=" + token, callback).show();
    }

    public class SimpleRequestInterceptor implements RequestInterceptor {
        public String cookie;
        public String accessToken;
        public String xcsrfToken;

        @Override
        public void intercept(RequestFacade request) {
            if (!TextUtils.isEmpty(cookie)) {
                Log8.d(cookie);
                request.addHeader("Cookie", cookie);
            }
            if (!TextUtils.isEmpty(xcsrfToken)) {
                Log8.d(xcsrfToken);
                request.addHeader("X-CSRF-Token", xcsrfToken);
            }
            if (!TextUtils.isEmpty(accessToken)) {
                Log8.d(accessToken);
                //request.addQueryParam("access_token", accessToken);
                request.addEncodedQueryParam("access_token", accessToken);
            }
        }
    }

    public DrupalManager setXcsrfToken(String xcsrfToken) {
        this.xcsrfToken = xcsrfToken;

        if (mRequestInterceptor != null) {
            mRequestInterceptor.xcsrfToken = xcsrfToken;
        }

        return this;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ErrorHandler implements retrofit.ErrorHandler {
        public ErrorHandler() {}

        @Override
        public Throwable handleError(RetrofitError cause) {
            cause.printStackTrace();
            return cause;
        }
    }

    private static SSLSocketFactory sTrustedFactory;

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

    private static HostnameVerifier sTrustedVerifier;

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

    /** {@inheritDoc} */
    @Override
    public void systemConnect(
        Callback<User> callback
    ) {
        getService().systemConnect(callback);
    }

    /** {@inheritDoc} */
    @Override
    public void getNode(
        int nid,
        Callback<Node> callback
    ) {
        getService().getNode(nid, callback);
    }

    public void getNode(
        String nid,
        Callback<Node> callback
    ) {
        int i;
        try {
            i = Integer.parseInt(nid);
        } catch (NumberFormatException e) {
            callback.failure(RetrofitError.unexpectedError("failure", e));
            return;
        }
        getNode(i, callback);
    }

    /** {@inheritDoc} */
    @Override
    public void getTaxonomyVocabulary(
        int vid,
        int parent,
        Callback<Vocabulary> callback
    ) {
        getService().getTaxonomyVocabulary(vid, parent, callback);
    }

    /** {@inheritDoc} */
    @Override
    public void getTaxonomyVocabulary(
        int vid,
        int parent,
        int maxdepth,
        Callback<Vocabulary> callback
    ) {
        getService().getTaxonomyVocabulary(vid, parent, maxdepth, callback);
    }

    /** {@inheritDoc} */
    @Override
    public void getViews(
        String name,
        int limit,
        String args,
        int displayId,
        Callback<Views> callback
    ) {
        getService().getViews(name, limit, args, displayId, callback);
    }

    /** {@inheritDoc} */
    @Override
    public void getToken(
        String username,
        String password,
        Callback<Login> callback
    ) {
        setUsername(username);
        setPassword(password);
        getService().getToken(username, password, callback);
    }

    /** {@inheritDoc} */
    @Override
    public void getToken(
        Callback<Login> callback
    ) {
        getService().getToken(callback);
    }

    @Override
    public void getComment(
        int cid,
        Callback<Comment> callback
    ) {
        getService().getComment(cid, callback);
    }

    @Override
    public void addComment(
        String comment,
        int nid,
        Callback<Comment> callback
    ) {
        getService().addComment(comment, nid, callback);
    }

    public void addComment(
        String comment,
        String nid,
        Callback<Comment> callback
    ) {
        int i;
        try {
            i = Integer.parseInt(nid);
        } catch (NumberFormatException e) {
            callback.failure(RetrofitError.unexpectedError("failure", e));
            return;
        }
        addComment(comment, i, callback);
    }

    @Override
    public void addComment(
        String subject,
        String comment,
        int nid,
        Callback<Comment> callback
    ) {
        getService().addComment(subject, comment, nid, callback);
    }

    public void addComment(
        String subject,
        String comment,
        String nid,
        Callback<Comment> callback
    ) {
        int i;
        try {
            i = Integer.parseInt(nid);
        } catch (NumberFormatException e) {
            callback.failure(RetrofitError.unexpectedError("failure", e));
            return;
        }
        getService().addComment(subject, comment, i, callback);
    }

    @Override
    public void setComment(
        int cid,
        String comment,
        int nid,
        Callback<Comment> callback
    ) {
        getService().setComment(cid, comment, nid, callback);
    }

    @Override
    public void deleteComment(
        int cid,
        Callback<Comment> callback
    ) {
        getService().deleteComment(cid, callback);
    }

    @Override
    public void getUser(
        int uid,
        String accessToken,
        Callback<User> callback
    ) {
        getService().getUser(uid, accessToken, callback);
    }

    @Override
    public void getUser(
        int uid,
        Callback<User> callback
    ) {
        getService().getUser(uid, callback);
    }

    @Override
    public Observable<Login> observeLogin(
        String username,
        String password
    ) {
        setUsername(username);
        setPassword(password);
        return getService().observeLogin(username, password);
    }

    @Override
    public Observable<Login> observeToken() {
        return getService().observeToken();
    }

    public DrupalManager setUsername(String username) {
        this.username = username;
        if (oauth != null) {
            oauth.setUsername(username);
        }
        return this;
    }

    public DrupalManager setEmail(String email) {
        this.email = email;
        //if (oauth != null) {
            //oauth.setEmail(email);
        //}
        return this;
    }

    public DrupalManager setPassword(String password) {
        this.password = password;
        if (oauth != null) {
            oauth.setPassword(password);
        }
        return this;
    }

    @Override
    public void getUser(String email, Callback<User> callback) {
        getService().getUser(email, callback);
    }

    @Override
    public void getUser(String email, String accessToken, Callback<User> callback) {
        getService().getUser(email, accessToken, callback);
    }
}
