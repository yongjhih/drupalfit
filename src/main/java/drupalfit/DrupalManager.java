package drupalfit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Callback;
import retrofit.RestAdapter;
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

public class DrupalManager implements DrupalService {
    private static DrupalManager sInstance = new DrupalManager();
    private DrupalService mService;
    private String endpoint;
    private String accessToken;
    private String cookie;
    private String mXCsrfToken; //X-CSRF-Token
    private String username; // TODO
    private String email; // TODO
    private String password; // TODO

    protected SimpleRequestInterceptor mRequestInterceptor;

    private DrupalOAuth2Manager oauth;

    public DrupalManager setOAuth(DrupalOAuth2Manager oauth) {
        this.oauth = oauth;
        return sInstance;
    }

    public DrupalOAuth2Manager getOAuth() {
        return oauth;
    }

    public DrupalManager setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return sInstance;
    }

    public DrupalManager build() {
        getService(endpoint);
        return sInstance;
    }

    private DrupalManager() {
    }

    @Override
    public void register(
        String username,
        String email,
        String password,
        Callback<User> callback) {
        /*
        this.username = username;
        this.password = password;
        this.email = email;
        */
        getService().register(username, email, password, callback);
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
        Callback<Login> callback
    ) {
        /*
        this.username = username;
        this.password = password;
        */
        getService().login(username, password, callback);
    }

    @Override
    public void getProfile(
        String accessToken,
        final Callback<User> callback
    ) {
        setAccessToken(accessToken);
        getService().getProfile(accessToken, callback);
    }

    @Override
    public void getProfile(
        final Callback<User> callback
    ) {
        if (cookie == null && accessToken == null) {
            if (oauth != null) {
                Log8.d();
                oauth.getAccessToken(new Callback<Credential>() {
                    @Override
                    public void success(Credential credential, Response response) {
                        setAccessToken(credential.access_token);
                        Log8.d(accessToken);
                        getService().userProfile(callback);
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

    public String getAccessToken() {
        if (!TextUtils.isEmpty(accessToken)) {
            return accessToken;
        }

        if (oauth != null) {
            Credential c = oauth.getAccessToken();
            if (c != null) {
                setAccessToken(c.access_token);
            }
        }

        return accessToken;
    }

    public String getAccessToken(String username, String password) {
        return getAccessToken(username, password, null);
    }

    public String getAccessToken(String username, String password, String authTokenType) {
        Credential c = oauth.getAccessToken(username, password);
        if (c != null) {
            setAccessToken(c.access_token);
        }
        return accessToken;
    }

    public DrupalManager setAccessToken(String accessToken) {
        this.accessToken = accessToken;

        if (mRequestInterceptor != null) {
            mRequestInterceptor.accessToken = accessToken;
        }

        return sInstance;
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
            mRequestInterceptor.xcsrfToken = mXCsrfToken;

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

        return sInstance;
    }

    public class SimpleRequestInterceptor implements RequestInterceptor {
        public String cookie;
        public String accessToken;
        public String xcsrfToken;

        @Override
        public void intercept(RequestFacade request) {
            if (!TextUtils.isEmpty(cookie)) {
                request.addHeader("Cookie", cookie);
            }
            if (!TextUtils.isEmpty(mXCsrfToken)) {
                request.addHeader("X-CSRF-Token", xcsrfToken);
            }
            if (!TextUtils.isEmpty(accessToken)) {
                Log8.d();
                //request.addQueryParam("access_token", accessToken);
                request.addEncodedQueryParam("access_token", accessToken);
            }
        }
    }

    public DrupalManager setXCsrfToken(String xcsrfToken) {
        mXCsrfToken = xcsrfToken;

        if (mRequestInterceptor != null) {
            mRequestInterceptor.xcsrfToken = xcsrfToken;
        }

        return sInstance;
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
            i = Integer.valueOf(nid);
        } catch (Exception e) {
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
        getService().getToken(username, password, callback);
    }

    /** {@inheritDoc} */
    @Override
    public void getToken(
        Callback<Login> callback
    ) {
        getService().getToken(callback);
    }

}
