package eu.albertvila.udacity.githubtrending;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import eu.albertvila.udacity.githubtrending.data.Settings;
import eu.albertvila.udacity.githubtrending.data.sync.SyncUtils;
import timber.log.Timber;

/**
 * Created by Albert Vila Calvo on 12/9/16.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Logging
        Timber.plant(new Timber.DebugTree() {
            @Override
            protected String createStackElementTag(StackTraceElement element) {
                // Add the line number
                return super.createStackElementTag(element) + ":" + element.getLineNumber();
            }
        });

        // Database Inspection
        Stetho.initializeWithDefaults(this);

        // Initialize the Google Mobile Ads SDK
        // https://firebase.google.com/docs/admob/android/quick-start#initialize_the_google_mobile_ads_sdk
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        // We must create an account for the SyncAdapter to run
        SyncUtils.get(this).addAccountExplicitly();

        SyncUtils.get(this).addPeriodicSync();

        // Set the selected programming language as a user property with Firebase Analytics
        // We do this here in case the user never changes the default programming language
        FirebaseAnalytics.getInstance(this).setUserProperty(
                getString(R.string.analytics_programming_language_key),
                Settings.get(this).getSelectedLanguage()
        );
    }

}
