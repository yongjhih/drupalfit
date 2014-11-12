package drupalfit.sample;

import android.os.Bundle;

public interface AccountAuthenticator {
    //void onCreate(Bundle savedInstanceState);
    void setAccountAuthenticatorResult(Bundle result); // AccountAuthenticatorResponse
    void addAccount(String username, String password, String authToken);
    //void finish();
    public static class SimpleAccountAuthenticator implements AccountAuthenticator {
        public void setAccountAuthenticatorResult(Bundle result) {
        }
        public void addAccount(String username, String password, String authToken) {
        }
    }
}
