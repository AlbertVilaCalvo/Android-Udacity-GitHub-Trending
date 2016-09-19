package eu.albertvila.udacity.githubtrending.data.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import eu.albertvila.udacity.githubtrending.R;
import eu.albertvila.udacity.githubtrending.data.Settings;
import eu.albertvila.udacity.githubtrending.data.db.DbContract;
import timber.log.Timber;

/**
 * Created by Albert Vila Calvo on 19/9/16.
 */
public class SyncUtils {

    private static SyncUtils instance;

    public static final String GITHUB_URL_KEY = "github_url";

    public static synchronized SyncUtils get(Context context) {
        if (instance == null) {
            instance = new SyncUtils(context);
        }
        return instance;
    }

    private final Context context;
    private final Account account;

    private SyncUtils(Context context) {
        this.context = context.getApplicationContext();
        this.account = new Account(
                context.getString(R.string.app_name),
                context.getString(R.string.authenticator_account_type));
    }

    // Set up an account type in the Android system
    public void addAccountExplicitly() {
        AccountManager accountManager = AccountManager.get(context);
        if (accountManager.addAccountExplicitly(account, null, null)) {
            Timber.d("addAccountExplicitly() - The account does not exist yet");
        } else {
            Timber.d("addAccountExplicitly() - The account already exists");
        }
    }

    // From https://developer.android.com/reference/android/content/ContentResolver.html#addPeriodicSync(android.accounts.Account,%20java.lang.String,%20android.os.Bundle,%20long)
    // If there is already another periodic sync scheduled with the account, authority and extras
    // then a new periodic sync won't be added, instead the frequency of the previous one will be
    // updated.
    public void addPeriodicSync() {
        Timber.d("addPeriodicSync()");

        // No need to call ContentResolver.setIsSyncable(newAccount, AUTHORITY, 1) because we
        // added android:syncable="true" in our <provider> element in the manifest

        // http://stackoverflow.com/a/20398983/4034572
        // It seems that addPeriodicSync() needs setSyncAutomatically()
        ContentResolver.setSyncAutomatically(
                account,
                context.getString(R.string.content_provider_authority),
                true
        );

        ContentResolver.addPeriodicSync(
                account,
                context.getString(R.string.content_provider_authority),
                Bundle.EMPTY,
                60 * 60 * 1 // every X hours
        );
    }

    public void requestExpeditedSync() {
        Timber.d("requestExpeditedSync()");

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        // When we run an expedited sync, we must pass the URL using the Bundle. Otherwise we don't
        // get the latest URL value in the SyncAdapter because it runs in a different process
        bundle.putString(GITHUB_URL_KEY, Settings.get(context).getGitHubUrl());

        ContentResolver.requestSync(account, DbContract.AUTHORITY, bundle);
    }

}
