package drupalfit.sample;

import android.accounts.*;
import android.content.Context;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class AuthenticatorService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        AbstractAccountAuthenticator authenticator = new Authenticator(this);
        return authenticator.getIBinder();
    }
}
