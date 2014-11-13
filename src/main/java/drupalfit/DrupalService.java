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
import retrofit.http.PUT;
import retrofit.http.DELETE;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

import rx.Observable;
import rx.functions.Func1;

/**
 * DrupalService.
 *
 * <pre>
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
 * </pre>
 *
 * @see <a href="https://gist.github.com/kylebrowning/affc9864487bb1b9c918">drupal_services_api.md</a>
 *
 */
public interface DrupalService {
    /**
     * Register.
     *
     * <pre>
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
     * </pre>
     */
    @FormUrlEncoded
    @POST("/user/register.json")
    void register(
        @Field("name") String username,
        @Field("mail") String email,
        @Field("pass") String password,
        Callback<User> callback
    );

    @Keep
    @KeepClassMembers
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        public User() {
        }

        //"access": "1414653769",
        public String access; // TODO Date
        //"created": "1414155320",
        public String created; // TODO Date
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
     * <pre>
     * Args:
     * HTTP Method: POST
     * Example URL : http://drupal6-services/services/plist/user/login
     * Example: &name=admin&pass=pass
     * </pre>
     */
    @FormUrlEncoded
    @POST("/user/login.json")
    void login(
        @Field("username") String username,
        @Field("password") String password,
        Callback<Login> callback
    );

    @FormUrlEncoded
    @POST("/user/login.json")
    Observable<Login> observeLogin(
        @Field("username") String username,
        @Field("password") String password
    );

    /**
     * userProfile.
     *
     * <p>
     * resource provided by oauth2_login_provider module.
     * </p>
     *
     * <pre>
     * Args:
     * HTTP Method: POST
     * Example URL : http://drupal6-services/services/plist/user/login
     * Example: &access_token or with cookie/session header
     * </pre>
     */
    @POST("/user/profile.json")
    void getProfile(
        Callback<User> callback
    );

    @FormUrlEncoded
    @POST("/user/profile.json")
    void getProfile(
        @Field("access_token") String accessToken,
        Callback<User> callback
    );

    @Keep
    @KeepClassMembers
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Login {
        public Login() {
        }

        /**
         * {@link User}
         */
        public User user;

        /**
         * <pre>
         * "session_name": "SESS8b1f176c338bbcc3922a56004cec1234",
         * </pre>
         */
        public String session_name;

        /**
         * <pre>
         * "sessid": "853c6c7f6eaa051724080dff202e1234"
         * </pre>
         */
        public String sessid;

        /**
         * X-CSRF-Token.
         */
        public String token;
    }

    /**
     * userLogout.
     *
     * <pre>
     * Args:
     * HTTP Method: POST
     * Example URL : http://drupal6-services/services/plist/user/logout
     * Example:
     * Expected Response(in JSON): 1
     * </pre>
     */
    @POST("/user/logout.json")
    void logout(
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

/*
*/
    /**
     * Node.
     *
     * <pre>
     * Args:
     * HTTP Method : GET
     * Example URL : http://drupal6-services/services/plist/node/1
     * Expected Response(in JSON): {"nid":"1","type":"story","language":"","uid":"0","status":"0","created":"1286592762","changed":"1286592762","comment":"2","promote":"0","moderate":"0","sticky":"0","tined":"0","translate":"0","vid":"1","revision_uid":"1","title":"test","body":"test","teaser":"test","log":"","revision_timestamp":"1286592762","format":"1","name":"","picture":"","data":null,"last_comment_timestamp":"1286592762","last_comment_name":null,"comment_count":"0","taxonomy":[],"files":[],"uric":"http:\/\/drupal6-services\/services\/plist\/node\/1"}
     * </pre>
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
        public String type;
        public String created; // TODO Date
        public String changed; // TODO Date

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("nid: " + nid + ", ");
            sb.append("uid: " + uid + ", ");
            sb.append("title: " + title + ", ");
            sb.append("type: " + type + ", ");
            return sb.toString();
        }
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

    @FormUrlEncoded
    @POST("/user/token.json")
    void getToken(
        @Field("username") String username,
        @Field("password") String password,
        Callback<Login> callback
    );

    @POST("/user/token.json")
    void getToken(
        Callback<Login> callback
    );

    @POST("/user/token.json")
    Observable<Login> observeToken();

    /**
     * Get comment.
     *
     * <pre>
     * Example URL : http://drupal6-services/services/plist/comment/30
     * Expected Response(in JSON): {"cid":"30","pid":"0","nid":"48","uid":"1","subject":"asdfadf","comment":"dfgsdfgsdg","hostname":"127.0.0.1","timestamp":"1294792128","status":"0","format":"1","thread":"01\/","name":"admin","mail":"","homepage":""}
     *
     * curl -k -c cookie -b cookie -X GET 'https://example.com/api/comment/1.json' -H 'X-CSRF-Token: JAndrohmVrkBkRq7PoGwSsI6MoPGQz0VixZgyKC7XaQ'
     * </pre>
     */
    @GET("/comment/{cid}.json")
    void getComment(
        @Field("cid") int cid,
        Callback<Comment> callback
    );

    @Keep
    @KeepClassMembers
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Comment {
        public Comment() {
        }

        public int nid;
        public int cid;
        public int uid;
        public int pid;
        public String subject;
        public String comment;
        public String timestamp; // TODO Date
        public String name;
        public String mail;
        public String uri;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("nid: " + nid + ", ");
            sb.append("cid: " + cid + ", ");
            sb.append("uid: " + uid + ", ");
            sb.append("pid: " + pid + ", ");
            sb.append("subject: " + subject + ", ");
            sb.append("comment: " + comment + ", ");
            sb.append("name: " + name + ", ");
            sb.append("mail: " + mail + ", ");
            sb.append("timestamp: " + timestamp + ", ");
            return sb.toString();
        }
    }

    /**
     * Create comment.
     *
     * <pre>
     * Args: comment*
     * HTTP Method: POST
     * Example URL : http://drupal6-services/services/plist/comment
     * Example:&comment[body]=commentbody&comment[nid]=49
     * Expected Response(in JSON): {"cid":"31","uri":"http:\/\/drupal6-services\/services\/plist\/comment\/31"}
     *
     * curl -k -c cookie.txt -b cookie.txt -X POST 'https://example.com/api/comment.json' -H 'X-CSRF-Token: JAndrohmVrkBkRq7PoGwSsI6MoPGQz0VixZgyKC7XaQ' -d 'nid=2&subject=yo&comment_body[und][0][value]=yoo'
     * </pre>
     *
     * @see tests/functional/ServicesResourceCommentTests.test
     */
    @FormUrlEncoded
    @POST("/comment.json")
    void addComment(
        @Field("subject") String subject,
        @Field("comment_body[und][0][value]") String comment,
        @Field("nid") int nid,
        Callback<Comment> callback
    );

    @FormUrlEncoded
    @POST("/comment.json")
    void addComment(
        @Field("comment_body[und][0][value]") String comment,
        @Field("nid") int nid,
        Callback<Comment> callback
    );

    /**
     * Update comment.
     *
     * <pre>
     * Args: data*
     * HTTP Method: PUT
     * Example URL : http://drupal6-services/services/plist/comment/30
     * Example: &data[body]=commentbody&data[nid]=49
     * Expected Response(in JSON): "30"
     * </pre>
     *
     * <pre>
     * {
     *   "path": "https://example.com/content/test",
     *   "data": "a:2:{s:20:\"l10n_client_disabled\";b:0;s:7:\"overlay\";i:1;}",
     *   "picture": "0",
     *   "name": "foo",
     *   "comment_count": "0",
     *   "last_comment_uid": "1",
     *   "last_comment_name": null,
     *   "last_comment_timestamp": "1389341070",
     *   "cid": "0",
     *   "rdf_mapping": {
     *     "last_activity": {
     *       "callback": "date_iso8601",
     *       "datatype": "xsd:dateTime",
     *       "predicates": [
     *         "sioc:last_activity_date"
     *       ]
     *     },
     *     "rdftype": [
     *       "foaf:Document"
     *     ],
     *     "title": {
     *       "predicates": [
     *         "dc:title"
     *       ]
     *     },
     *     "created": {
     *       "callback": "date_iso8601",
     *       "datatype": "xsd:dateTime",
     *       "predicates": [
     *         "dc:date",
     *         "dc:created"
     *       ]
     *     },
     *     "changed": {
     *       "callback": "date_iso8601",
     *       "datatype": "xsd:dateTime",
     *       "predicates": [
     *         "dc:modified"
     *       ]
     *     },
     *     "body": {
     *       "predicates": [
     *         "content:encoded"
     *       ]
     *     },
     *     "uid": {
     *       "type": "rel",
     *       "predicates": [
     *         "sioc:has_creator"
     *       ]
     *     },
     *     "name": {
     *       "predicates": [
     *         "foaf:name"
     *       ]
     *     },
     *     "comment_count": {
     *       "datatype": "xsd:integer",
     *       "predicates": [
     *         "sioc:num_replies"
     *       ]
     *     }
     *   },
     *   "body": {
     *     "und": [
     *       {
     *         "safe_summary": "",
     *         "safe_value": "<p>test</p>\n",
     *         "format": "filtered_html",
     *         "summary": "",
     *         "value": "test"
     *       }
     *     ]
     *   },
     *   "revision_uid": "1",
     *   "sticky": "0",
     *   "promote": "0",
     *   "comment": "1",
     *   "status": "1",
     *   "log": "",
     *   "title": "test",
     *   "uid": "1",
     *   "vid": "1",
     *   "nid": "1",
     *   "type": "page",
     *   "language": "und",
     *   "created": "1389341070",
     *   "changed": "1389341070",
     *   "tnid": "0",
     *   "translate": "0",
     *   "revision_timestamp": "1389341070"
     * }
     * </pre>
     */
    @FormUrlEncoded
    @PUT("/comment/{cid}.json")
    void setComment(
        @Path("cid") int cid,
        @Field("data[body]") String comment,
        @Field("data[nid]") int nid,
        Callback<Comment> callback // FIXME
    );

    /**
     * Delete comment.
     *
     * <pre>
     * Args: n/a
     * HTTP Method: DELETE
     * Example URL : http://drupal6-services/services/plist/comment/30
     * Example:
     * Expected Response(in JSON): 1
     * </pre>
     */
    @DELETE("/comment/{cid}.json")
    void deleteComment(
        @Path("cid") int cid,
        Callback<Comment> callback // FIXME
    );
}
