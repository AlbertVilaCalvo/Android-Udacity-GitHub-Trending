package eu.albertvila.udacity.githubtrending.data.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {

    private StubAccountAuthenticator authenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        authenticator = new StubAccountAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        return authenticator.getIBinder();
    }
}
