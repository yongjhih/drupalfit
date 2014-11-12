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
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;

import android.accounts.AccountManager;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;

import android.text.TextUtils;
import android.content.Intent;

public abstract class ToolBarActivity extends ActionBarActivity implements AccountAuthenticator {
    @InjectView(R.id.toolbar)
    protected Toolbar toolbar;
    @InjectView(R.id.drawer)
    protected DrawerLayout drawerLayout;
    protected ActionBarDrawerToggle toggle;

    private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;
    private Bundle mResultBundle = null;

    private AccountManager mAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccountAuthenticatorResponse =
                getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }

        setContentView(getContentView());
        ButterKnife.inject(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
            toggle.setDrawerIndicatorEnabled(true);
            drawerLayout.setDrawerListener(toggle);
        }
    }

    protected abstract int getContentView();

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    /** {@inheritDoc} */
    @Override
    public final void setAccountAuthenticatorResult(Bundle result) {
        mResultBundle = result;
    }

    /** {@inheritDoc} */
    @Override
    public void addAccount(String username, String password, String authToken) {
        String accountType = "drualfit";
        String authTokenType = "drualfit";
        Account account = new Account(username, accountType);
        Bundle userdata = null;

        Intent intent = new Intent();

        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, authToken);

        if (TextUtils.isEmpty(password)) {
            boolean added = getAccountManager().addAccountExplicitly(account, password, userdata);
        } else {
            getAccountManager().setPassword(account, password);
        }

        getAccountManager().setAuthToken(account, authTokenType, authToken);

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
    }

    private AccountManager getAccountManager() {
        if (mAccountManager == null) {
            mAccountManager = AccountManager.get(this);
        }
        return mAccountManager;
    }

    @Override
    public void finish() {
        if (mAccountAuthenticatorResponse != null) {
            // send the result bundle back if set, otherwise send an error.
            if (mResultBundle != null) {
                mAccountAuthenticatorResponse.onResult(mResultBundle);
            } else {
                mAccountAuthenticatorResponse.onError(AccountManager.ERROR_CODE_CANCELED,
                        "canceled");
            }
            mAccountAuthenticatorResponse = null;
        }
        super.finish();
    }
}
