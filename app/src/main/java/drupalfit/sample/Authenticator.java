package drupalfit.sample;

import android.accounts.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.app.Activity;

import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT;

import drupalfit.DrupalManager;
import drupalfit.DrupalOAuth2Manager;
import drupalfit.DrupalOAuth2.*;
import drupalfit.DrupalService.*;

public class Authenticator extends AbstractAuthenticator {
    public Authenticator(Context context) {
        super(context);
    }

    /** {@inheritDoc} */
    @Override
    public Class<? extends Activity> getActivityType() {
        return HomeActivity.class;
    }

    /** {@inheritDoc} */
    @Override
    public String login(String username, String password, String authTokenType) {
        Credential c = DrupalManager.get().getOAuth().getAccessToken(username, password);
        if (c != null) return c.access_token;
        return null;
    }
}
