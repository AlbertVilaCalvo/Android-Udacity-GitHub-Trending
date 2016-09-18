package eu.albertvila.udacity.githubtrending;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;

import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.MobileAds;

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
        createAccount();
    }

    private void createAccount() {
        AccountManager accountManager = AccountManager.get(this);

        Account newAccount = new Account(
                getString(R.string.app_name),
                getString(R.string.authenticator_account_type));

        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            
        }
    }

}
