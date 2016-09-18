package eu.albertvila.udacity.githubtrending.data.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncService extends Service {

    private static final Object lock = new Object();

    private static SyncAdapter syncAdapter = null;

    @Override
    public void onCreate() {
        super.onCreate();

        synchronized (lock) {
            if (syncAdapter == null) {
                syncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        return syncAdapter.getSyncAdapterBinder();
    }

}
