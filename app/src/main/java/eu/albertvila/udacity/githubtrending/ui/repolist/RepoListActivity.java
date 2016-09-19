package eu.albertvila.udacity.githubtrending.ui.repolist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import eu.albertvila.udacity.githubtrending.R;
import eu.albertvila.udacity.githubtrending.data.db.DbContract;
import eu.albertvila.udacity.githubtrending.data.sync.SyncUtils;
import eu.albertvila.udacity.githubtrending.ui.settings.SettingsActivity;
import timber.log.Timber;

public class RepoListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_REPOS = 0;

    // Columns to select from DB
    private static final String[] COLUMNS_PROJECTION = {
            DbContract.Repo.COLUMN_URL,
            DbContract.Repo.COLUMN_DESCRIPTION
    };

    RecyclerView recyclerView;
    ReposCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadBannerAd();

        setupRecyclerView();

        getSupportLoaderManager().initLoader(LOADER_REPOS, null, this);
    }

    private void loadBannerAd() {
        AdView adView = (AdView) findViewById(R.id.repo_list_adView);
        AdRequest adRequest = new AdRequest.Builder()
                // https://firebase.google.com/docs/admob/android/targeting#test_ads
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("D7A3AA3A1DAE36977EE1652523B33CBD")
                .build();
        adView.loadAd(adRequest);
    }

    private void setupRecyclerView() {
        adapter = new ReposCursorAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.repo_list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader;

        switch (id) {
            case LOADER_REPOS:
                loader =  new CursorLoader(
                        this,
                        DbContract.Repo.CONTENT_URI,
                        COLUMNS_PROJECTION,
                        null, // all
                        null,
                        null // default order
                );
                break;
            default:
                loader = null;
        }

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() == 0) {
            Timber.d("onLoadFinished() - data is null or empty");
            adapter.swapCursor(null);
            // TODO show progress

            // download data
            SyncUtils.get(this).requestExpeditedSync();
        } else {
            Timber.d("onLoadFinished() - data OK");
            adapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Timber.d("onLoaderReset()");
        adapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_repo_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
