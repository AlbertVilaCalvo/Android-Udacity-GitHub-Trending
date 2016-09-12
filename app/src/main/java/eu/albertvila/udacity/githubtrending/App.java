package eu.albertvila.udacity.githubtrending;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

/**
 * Created by Albert Vila Calvo on 12/9/16.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the Google Mobile Ads SDK
        // https://firebase.google.com/docs/admob/android/quick-start#initialize_the_google_mobile_ads_sdk
        MobileAds.initialize(this, getString(R.string.admob_app_id));
    }
}
