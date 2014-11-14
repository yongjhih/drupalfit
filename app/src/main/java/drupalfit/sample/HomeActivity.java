/*
 * Copyright (C) 2014 Antonio Leiva Gordillo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package drupalfit.sample;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import drupalfit.DrupalManager;
import drupalfit.DrupalService;
import drupalfit.DrupalService.*;
import drupalfit.DrupalOAuth2Manager;
import drupalfit.DrupalOAuth2;
import drupalfit.DrupalOAuth2.*;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnLongClick;

import drupalfit.Log8;

import android.net.Uri;
import android.text.TextUtils;
import android.content.Context;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import android.view.inputmethod.InputMethodManager;

import rx.schedulers.*;
import rx.android.schedulers.*;
import rx.functions.*;
import rx.Observable;
import rx.android.observables.*;

public class HomeActivity extends ToolBarActivity {
    @InjectView(R.id.email)
    EditText email;
    @InjectView(R.id.password)
    EditText password;
    @InjectView(R.id.endpoint)
    EditText endpoint;
    @InjectView(R.id.token)
    EditText token;
    @InjectView(R.id.content)
    View view;
    @InjectView(R.id.nid)
    EditText nid;
    @InjectView(R.id.comment_nid)
    EditText commentNid;
    @InjectView(R.id.comment_content)
    EditText commentContent;

    @OnClick(R.id.comment)
    public void comment() {
        String commentNid = this.commentNid.getText().toString();
        String commentContent = this.commentContent.getText().toString();

        if (TextUtils.isEmpty(commentNid)) {
            Toast.makeText(HomeActivity.this, "failure", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(commentContent)) {
            Toast.makeText(HomeActivity.this, "failure", Toast.LENGTH_LONG).show();
            return;
        }

        progress();

        DrupalManager.get()
            .setEndpoint(endpoint.getText().toString())
            .build();

        DrupalManager.get().addComment(commentContent, commentNid, new Callback<Comment>() {
            @Override
            public void success(Comment comment, Response response) {
                done();
                Toast.makeText(HomeActivity.this, "success", Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(RetrofitError error) {
                done();
                Toast.makeText(HomeActivity.this, "failure: " + error, Toast.LENGTH_LONG).show();
                Log8.d(error);
            }
        });
    }

    private void hideSoftInputFromWindow() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void progress() {
        hideSoftInputFromWindow();
        SuperActivityToast.cancelAllSuperActivityToasts();
        SuperActivityToast superActivityToast = new SuperActivityToast(HomeActivity.this, SuperToast.Type.PROGRESS);
        superActivityToast.setText("Progressing...");
        superActivityToast.setIndeterminate(true);
        superActivityToast.setProgressIndeterminate(true);
        superActivityToast.show();
    }

    private void done() {
        SuperActivityToast.cancelAllSuperActivityToasts();
    }

    @OnClick(R.id.signin)
    public void signin() {
        if (TextUtils.isEmpty(endpoint.getText().toString())) {
            Toast.makeText(HomeActivity.this, "failure", Toast.LENGTH_LONG).show();
            return;
        }

        progress();

        DrupalManager.get()
            .setEndpoint(endpoint.getText().toString())
            .build();

        DrupalManager.get().login(email.getText().toString(), password.getText().toString(), new Callback<Login>() {
            @Override
            public void success(Login login, Response response) {
                done();
                Toast.makeText(HomeActivity.this, "success: " + "uid:" + login.user.uid + ", name: " + login.user.name, Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(RetrofitError error) {
                done();
                Toast.makeText(HomeActivity.this, "failure: " + error, Toast.LENGTH_LONG).show();
                Log8.d(error);
            }
        });
    }

    @OnClick(R.id.signup)
    public void signup() {
        if (TextUtils.isEmpty(endpoint.getText().toString())) {
            Toast.makeText(HomeActivity.this, "failure", Toast.LENGTH_LONG).show();
            return;
        }

        progress();

        DrupalManager.get()
            .setEndpoint(endpoint.getText().toString())
            .build();

        DrupalManager.get().register(email.getText().toString(), password.getText().toString(), new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                done();
                Toast.makeText(HomeActivity.this, "success: " + "uid:" + user.uid + ", name: " + user.name, Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(RetrofitError error) {
                done();
                Toast.makeText(HomeActivity.this, "failure: " + error, Toast.LENGTH_LONG).show();
                Log8.d(error);
            }
        });
    }

    @OnClick(R.id.connect_facebook)
    public void connectFacebook() {
        if (TextUtils.isEmpty(endpoint.getText().toString())) {
            Toast.makeText(HomeActivity.this, "failure", Toast.LENGTH_LONG).show();
            return;
        }

        progress();

        String restEndpoint = endpoint.getText().toString();
        Uri uri = Uri.parse(restEndpoint);
        String oauthEndpoint = uri.getScheme() + "://" + uri.getAuthority() + "/oauth2";

        DrupalManager.get()
            .setEndpoint(restEndpoint)
            .setProvider(this, DrupalManager.FACEBOOK, token.getText().toString())
            .build();

        DrupalManager.get().getProfile(new Callback<User>() { // direct access profile, DrupalManager will try to connect
            @Override
            public void success(User user, Response response) {
                done();
                Toast.makeText(HomeActivity.this, "success: " + "uid:" + user.uid + ", name: " + user.name, Toast.LENGTH_LONG).show();
                Log8.d(user.name);
                Log8.d(user.mail);
                Log8.d(user.uid);
            }
            @Override
            public void failure(RetrofitError error) {
                done();
                Toast.makeText(HomeActivity.this, "failure: " + error, Toast.LENGTH_LONG).show();
                Log8.d(error);
            }
        });
    }

    @OnClick(R.id.get_node)
    public void getNode() {
        if (TextUtils.isEmpty(endpoint.getText().toString())) {
            Toast.makeText(HomeActivity.this, "failure", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(nid.getText().toString())) {
            Toast.makeText(HomeActivity.this, "failure", Toast.LENGTH_LONG).show();
            return;
        }

        progress();

        DrupalManager.get()
            .setEndpoint(endpoint.getText().toString())
            .build();

        DrupalManager.get().getNode(nid.getText().toString(), new Callback<Node>() {
            @Override
            public void success(Node node, Response response) {
                done();
                Toast.makeText(HomeActivity.this, "success: " + node, Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(RetrofitError error) {
                done();
                Toast.makeText(HomeActivity.this, "failure: " + error, Toast.LENGTH_LONG).show();
                Log8.d(error);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /** {@inheritDoc} */
    @Override
    protected int getContentView() {
        return R.layout.activity_home;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
