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
import retrofit.http.Query;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

/**
 * DrupalOAuth2.
 */
public interface DrupalOAuth2 {
    /**
     * curl -L -I -k -b b.cookie -X GET 'https://example.com/oauth2/authorize?client_id=id&client_secret=secret&response_type=code&state=state'
     */
    @GET("/authorize")
    void authorize(
        @Query("client_id") String clientId,
        @Query("client_secret") String clientSecret,
        @Query("response_type") String responseType,
        @Query("state") String state,
        Callback<Response> callback
    );

    /**
     * curl -k -X POST 'https://example.com/oauth2/token' -d 'code=aa5b25e58cb0ecbb1ddf5d671e769b04cabcdefg&state=8tory&grant_type=authorization_code&client_id=id&client_secret=secret'
     */
    @Multipart
    @POST("/token")
    void token(
        @Part("code") String code,
        @Part("client_id") String clientId,
        @Part("client_secret") String clientSecret,
        @Part("grant_type") String grantType,
        @Part("state") String state,
        Callback<Credential> callback
    );

    /**
     * curl -k -X POST 'https://example.com/oauth2/token' -d 'grant_type=password&client_id=id&client_secret=secret&state=state&username=foo&password=bar'
     */
    @Multipart
    @POST("/token")
    void token(
        @Part("client_id") String clientId,
        @Part("client_secret") String clientSecret,
        @Part("grant_type") String grantType,
        @Part("state") String state,
        @Part("username") String username,
        @Part("password") String password,
        Callback<Credential> callback
    );

    @Keep
    @KeepClassMembers
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Credential {
        public String access_token;
        public Long expires_in;
        public Boolean array_key_exists;
        public String scope;
        public String refresh_token;

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("access_token: " + access_token + "\n");
            sb.append("expires_in: " + expires_in + "\n");
            sb.append("array_key_exists: " + array_key_exists + "\n");
            sb.append("scope: " + scope + "\n");
            sb.append("refresh_token: " + refresh_token + "\n");
            return sb.toString();
        }
    }
}
