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

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

/**
 * DrupalService.
 *
 * https://gist.github.com/kylebrowning/affc9864487bb1b9c918
 * Node Resource
 * Retrieve
 *
 * Args:
 * HTTP Method : GET
 * Example URL : http://drupal6-services/services/plist/node/1
 * Expected Response(in JSON): {"nid":"1","type":"story","language":"","uid":"0","status":"0","created":"1286592762","changed":"1286592762","comment":"2","promote":"0","moderate":"0","sticky":"0","tined":"0","translate":"0","vid":"1","revision_uid":"1","title":"test","body":"test","teaser":"test","log":"","revision_timestamp":"1286592762","format":"1","name":"","picture":"","data":null,"last_comment_timestamp":"1286592762","last_comment_name":null,"comment_count":"0","taxonomy":[],"files":[],"uric":"http:\/\/drupal6-services\/services\/plist\/node\/1"}
 * Create
 *
 * Args: node*
 * HTTP Method: POST
 * Example URL : http://drupal6-services/services/plist/node
 * Example: &node[title]=testnode&node[type]=story&node[field_test][0][value]=testtting
 * Notes: field_test is a CCK field.
 * Expected Response(in JSON): {"nid":"45","uri":"http:\/\/drupal6-services\/services\/plist\/node\/45"}
 * Update
 *
 * Args: node*
 * HTTP Method: PUT
 * Example URL : http://drupal6-services/services/plist/node/1
 * Example: &node[title]=testnode&node[type]=story&node[field_test][0][value]=testtting
 * Expected Response(in JSON): "1"
 * Delete
 *
 * Args:
 * HTTP Method: DELETE
 * Example URL : http://drupal6-services/services/plist/node/1
 * Example:
 * Expected Response(in JSON): 1
 * Comment Resource
 * Retrieve
 *
 * Args:
 * HTTP Method : GET
 * Example URL : http://drupal6-services/services/plist/comment/30
 * Expected Response(in JSON): {"cid":"30","pid":"0","nid":"48","uid":"1","subject":"asdfadf","comment":"dfgsdfgsdg","hostname":"127.0.0.1","timestamp":"1294792128","status":"0","format":"1","thread":"01\/","name":"admin","mail":"","homepage":""}
 * Create
 *
 * Args: comment*
 * HTTP Method: POST
 * Example URL : http://drupal6-services/services/plist/comment
 * Example:&comment[body]=commentbody&comment[nid]=49
 * Expected Response(in JSON): {"cid":"31","uri":"http:\/\/drupal6-services\/services\/plist\/comment\/31"}
 * Update
 *
 * Args: data*
 * HTTP Method: PUT
 * Example URL : http://drupal6-services/services/plist/comment/30
 * Example: &data[body]=commentbody&data[nid]=49
 * Expected Response(in JSON): "30"
 * Delete
 *
 * Args:
 * HTTP Method: DELETE
 * Example URL : http://drupal6-services/services/plist/comment/30
 * Example:
 * Expected Response(in JSON): 1
 * User Resource
 * Retrieve
 *
 * Args:
 * HTTP Method : GET
 * Example URL : http://drupal6-services/services/plist/user/1
 * Expected Response(in JSON): {"uid":"1","name":"admin","pass":"1a1dc91c907325c69271ddf0c944bc72","mail":"kyle@workhabit.com","mode":"0","sort":"0","threshold":"0","theme":"","signature":"","signature_format":"0","created":"1286571725","access":"1294792121","login":"1293782855","status":"1","timezone":null,"language":"","picture":"","init":"kyle@workhabit.com","data":"a:0:{}","roles":{"2":"authenticated user"}}
 * Create
 *
 * Args: account*
 * HTTP Method: POST
 * Example URL : http://drupal6-services/services/plist/user
 * Example: &account[name]=test&account[mail]=test@test.com&account[pass]=pass
 * Expected Response(in JSON): {"uid":"15","name":"test","pass":"1a1dc91c907325c69271ddf0c944bc72","mail":"test@test.com","mode":"0","sort":"0","threshold":"0","theme":"","signature":"","signature_format":"0","created":"1294793391","access":"1294793391","login":"0","status":"1","timezone":"-25200","language":"","picture":"","init":"test@test.com","data":"a:0:{}","roles":{"2":"authenticated user"},"password":"pass"}
 * Update
 *
 * Args: data*
 * HTTP Method: PUT
 * Example URL : http://drupal6-services/services/plist/user/15
 * Example: &data[name]=test&data[mail]=test@testing.com&data[pass]=pass
 * Expected Response(in JSON): {"name":"test","mail":"test@testing.com","pass":"pass","uid":"15"}
 * Delete
 *
 * Args:
 * HTTP Method: DELETE
 * Example URL : http://drupal6-services/services/plist/user/15
 * Example:
 * Expected Response(in JSON): 1
 * Login
 *
 * Args:
 * HTTP Method: POST
 * Example URL : http://drupal6-services/services/plist/user/login
 * Example: &name=admin&pass=pass
 * Expected Response(in JSON): {"sessid":"853c6c7f6eaa051724080dff202eeec0","session_name":"SESS8b1f176c338bbcc3922a56004cec3c41","user":{"uid":"1","name":"admin","pass":"1a1dc91c907325c69271ddf0c944bc72","mail":"kyle@workhabit.com","mode":"0","sort":"0","threshold":"0","theme":"","signature":"","signature_format":"0","created":"1286571725","access":"1294794381","login":1294794548,"status":"1","timezone":null,"language":"","picture":"","init":"kyle@workhabit.com","data":"a:0:{}","roles":{"2":"authenticated user"}}}
 * Logout
 *
 * Args:
 * HTTP Method: POST
 * Example URL : http://drupal6-services/services/plist/user/logout
 * Example:
 * Expected Response(in JSON): 1
 */
public interface DrupalService {
    /**
     * userRegister.
     * Args: account*
     * HTTP Method: POST
     * Example URL : http://drupal6-services/services/plist/user
     * Example: &account[name]=test&account[mail]=test@test.com&account[pass]=pass
     * Expected Response(in JSON):
     *
     * {
     *   "password": "pass",
     *   "roles": {
     *     "2": "authenticated user"
     *   },
     *   "data": "a:0:{}",
     *   "init": "test@test.com",
     *   "picture": "",
     *   "theme": "",
     *   "threshold": "0",
     *   "sort": "0",
     *   "mode": "0",
     *   "mail": "test@test.com",
     *   "pass": "1a1dc91c907325c69271ddf0c944bc72",
     *   "name": "test",
     *   "uid": "15",
     *   "signature": "",
     *   "signature_format": "0",
     *   "created": "1294793391",
     *   "access": "1294793391",
     *   "login": "0",
     *   "status": "1",
     *   "timezone": "-25200",
     *   "language": ""
     * }
     */
    @FormUrlEncoded
    @POST("/user/register.json")
    void userRegister(
        @Field("name") String username,
        @Field("mail") String email,
        @Field("pass") String password,
        Callback<User> callback
    );

    /*
    @FormUrlEncoded
    @POST("/user/register")
    void userRegister(
        @Field("mail") String email,
        @Field("pass") String password,
        Callback<User> callback
    );
    */

    @Keep
    @KeepClassMembers
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        public User() {
        }

      //"access": "1414653769",
        public String access;
      //"created": "1414155320",
        public String created;
      //"signature_format": null,
      //"signature": "",
      //"theme": "",
      //"mail": "foo@bar.com",
        public String mail;
      //"name": "foo",
        public String name;
      //"uid": "1234",
        public String uid;
      //"login": "1414652619",
        public String login;
      //"status": "1",
      //"timezone": "Asia/Taipei",
      //"language": "en_GB",
      //"picture": {
        //"url": "https://bar.com/sites/default/files/pictures/picture-24824-1414155320.jpg",
        //"rdf_mapping": [],
        //"fid": "24340",
        //"uid": "1234",
        //"filename": "picture-24824-1414155320.jpg",
        //"uri": "public://pictures/picture-24824-1414155320.jpg",
        //"filemime": "image/jpeg",
        //"filesize": "9422",
        //"status": "1",
        //"timestamp": "1414155320"
      //},
      //"init": "foo@bar.com",
        public String init;
      //"data": {
        //"mimemail_textonly": 0,
        //"l10n_client_disabled": false,
        //"contact": 1
      //},
      //"roles": {
        //"2": "authenticated user"
      //}
    }

    /**
     * userLogin.
     *
     * Args:
     * HTTP Method: POST
     * Example URL : http://drupal6-services/services/plist/user/login
     * Example: &name=admin&pass=pass
     */
    @FormUrlEncoded
    @POST("/user/login.json")
    void userLogin( // TODO login()
        @Field("username") String username,
        @Field("password") String password,
        Callback<Login> callback
    );

    /**
     * userProfile.
     *
     * resource provided by oauth2_login_provider module.
     *
     * Args:
     * HTTP Method: POST
     * Example URL : http://drupal6-services/services/plist/user/login
     * Example: &access_token or with cookie/session header
     */
    @POST("/user/profile.json")
    void userProfile( // TODO getProfile()
        Callback<User> callback
    );

    @FormUrlEncoded
    @POST("/user/profile.json")
    void userProfile(// TODO getProfile()
        @Field("access_token") String accessToken,
        Callback<User> callback
    );

    @Keep
    @KeepClassMembers
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Login {
        public Login() {
        }

        public User user;
        //"session_name": "SESS8b1f176c338bbcc3922a56004cec1234",
        public String session_name;
        //"sessid": "853c6c7f6eaa051724080dff202e1234"
        public String sessid;
    }

    /**
     * userLogout.
     *
     * Args:
     * HTTP Method: POST
     * Example URL : http://drupal6-services/services/plist/user/logout
     * Example:
     * Expected Response(in JSON): 1
     */
    @POST("/user/logout.json")
    void userLogout(// TODO logout
        Callback<Logout> callback
    );

    @Keep
    @KeepClassMembers
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Logout {
        public Logout() {
        }

        // [true]
    }

    @POST("/system/connect.json")
    void systemConnect(
        Callback<User> callback
    );

    @GET("/node/{nid}.json")
    void getNode(
        @Path("nid") int nid,
        Callback<Node> callback
    );

    /**
     * Args:
     * HTTP Method : GET
     * Example URL : http://drupal6-services/services/plist/node/1
     * Expected Response(in JSON): {"nid":"1","type":"story","language":"","uid":"0","status":"0","created":"1286592762","changed":"1286592762","comment":"2","promote":"0","moderate":"0","sticky":"0","tined":"0","translate":"0","vid":"1","revision_uid":"1","title":"test","body":"test","teaser":"test","log":"","revision_timestamp":"1286592762","format":"1","name":"","picture":"","data":null,"last_comment_timestamp":"1286592762","last_comment_name":null,"comment_count":"0","taxonomy":[],"files":[],"uric":"http:\/\/drupal6-services\/services\/plist\/node\/1"}
     */
    @Keep
    @KeepClassMembers
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Node {
        public Node() {
        }

        public int nid;
        public int uid;
        public String title;
        public String body;
        public String type;
        public long created; // TODO Date
        public long changed; // TODO Date
    }

    @Keep
    @KeepClassMembers
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Vocabulary {
        public Vocabulary() {
        }
    }

    @FormUrlEncoded
    @POST("/taxonomy_vocabulary/getTree.json")
    void getTaxonomyVocabulary(
        @Field("vid") int vid,
        @Field("parent") int parent,
        Callback<Vocabulary> callback
    );

    @FormUrlEncoded
    @POST("/taxonomy_vocabulary/getTree.json")
    void getTaxonomyVocabulary(
        @Field("vid") int vid,
        @Field("parent") int parent,
        @Field("maxdepth") int maxdepth,
        Callback<Vocabulary> callback
    );

    @Keep
    @KeepClassMembers
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Views {
        public Views() {
        }
    }

    @GET("/views/{name}.json?limit={limit}&offset={offset}&args={args}&display_id={displayId}")
    void getViews(
        @Path("name") String name,
        @Path("limit") int limit,
        @Path("args") String args,
        @Path("display_id") int displayId,
        Callback<Views> callback
    );
}
