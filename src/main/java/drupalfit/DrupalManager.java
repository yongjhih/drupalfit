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

public class DrupalManager {
    private static DrupalManager sInstance = new DrupalManager();
    private DrupalService mService;
    private String mEndpoint;
    private String accessToken;
    private String cookie;
    private String mXCsrfToken; //X-CSRF-Token

    protected SimpleRequestInterceptor mRequestInterceptor;

    private DrupalManager() {
    }

    public static DrupalManager get() {
        return sInstance;
    }

    public static DrupalService getService() {
        return get().getService(null);
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
            mEndpoint = endpoint;
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(getTrustedFactory());
            okHttpClient.setHostnameVerifier(getTrustedVerifier());
            Client client = new OkClient(okHttpClient);

            if (mRequestInterceptor == null) {
                mRequestInterceptor = new SimpleRequestInterceptor();
            }
            mRequestInterceptor.cookie = cookie;
            mRequestInterceptor.accessToken = accessToken;

            RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(mEndpoint)
                .setErrorHandler(new ErrorHandler())
                .setClient(client)
                .setConverter(new retrofit.converter.JacksonConverter())
                .build();

            mService = restAdapter.create(DrupalService.class);
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

        @Override
        public void intercept(RequestFacade request) {
            if (!TextUtils.isEmpty(cookie)) {
                request.addHeader("Cookie", cookie);
            }
            if (!TextUtils.isEmpty(accessToken)) {
                request.addQueryParam("access_token", accessToken);
            }
        }
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
}
