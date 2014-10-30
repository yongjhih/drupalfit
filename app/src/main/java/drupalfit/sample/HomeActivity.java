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
import drupalfit.DrupalService.User;

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

import drupalfit.DrupalOAuth2Manager;
import drupalfit.Log8;

import android.net.Uri;

public class HomeActivity extends ToolBarActivity {
    @InjectView(R.id.email)
    EditText email;
    @InjectView(R.id.password)
    EditText password;
    @InjectView(R.id.endpoint)
    EditText endpoint;
    @InjectView(R.id.client_id)
    EditText clientId;
    @InjectView(R.id.client_secret)
    EditText clientSecret;
    @InjectView(R.id.token)
    EditText token;

    @OnClick(R.id.sign)
    public void sign() {
        //DrupalManager.get().getService(endpoint.getText().toString()).userRegister(email.getText().toString(), email.getText().toString(), password.getText().toString(), new Callback<User>() {
        String restEndpoint = endpoint.getText().toString();
        Uri uri = Uri.parse(restEndpoint);
        String oauthEndpoint = uri.getScheme() + "://" + uri.getAuthority() + "/oauth2";

        DrupalManager.get()
            .setEndpoint(restEndpoint)
            .setOAuth(
                new DrupalOAuth2Manager.Builder()
                    .setEndpoint(oauthEndpoint)
                    .setClientId(clientId.getText().toString())
                    .setClientSecret(clientSecret.getText().toString())
                    .setProvider(HomeActivity.this, DrupalOAuth2Manager.FACEBOOK, token.getText().toString())
                    .build()
            ).build();

        DrupalManager.get().userProfile(new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                Toast.makeText(HomeActivity.this, "success: " + "uid:" + user.uid + ", name: " + user.name + ", accessToken: " + DrupalManager.get().getAccessToken(), Toast.LENGTH_SHORT).show();
                Log8.d(user.name);
                Log8.d(user.mail);
                Log8.d(user.uid);
                Log8.d(DrupalManager.get().getAccessToken());
            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(HomeActivity.this, "failure", Toast.LENGTH_SHORT).show();
                Log8.d(error);
            }
        });
    }

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActionBarIcon(R.drawable.ic_ab_drawer);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
    }

    @Override protected int getContentView() {
        return R.layout.activity_home;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(Gravity.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
