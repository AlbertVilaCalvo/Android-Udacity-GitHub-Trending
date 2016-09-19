package eu.albertvila.udacity.githubtrending.ui;

import android.content.ContentValues;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import eu.albertvila.udacity.githubtrending.R;
import eu.albertvila.udacity.githubtrending.data.db.DbContract;
import eu.albertvila.udacity.githubtrending.data.sync.SyncUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_REPOS = 0;

    // Columns to select from DB
    private static final String[] COLUMNS_PROJECTION = {
            DbContract.Repo.COLUMN_URL
    };

    RecyclerView recyclerView;
    ReposCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadBannerAd();

        setupRecyclerView();

        getSupportLoaderManager().initLoader(LOADER_REPOS, null, this);

        SyncUtils.get(this).requestExpeditedSync();

        /*
        getHtml()
                .subscribeOn(Schedulers.io())
                .map(new Function<String, List<String>>() {
                    @Override
                    public List<String> apply(String html) throws Exception {
                        Document document = Jsoup.parse(html);
                        Elements elements = document.select(".repo-list-name > a");
                        List<String> urls = new ArrayList<>();
                        for (int i = 0; i < elements.size(); i++) {
                            urls.add(elements.get(i).attr("href"));
                        }
                        Timber.d("urls %s", urls);
                        return urls;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<String>>() {
                    @Override
                    public void onNext(List<String> value) {
                        Timber.d("onNext: %s", value);
                        repos.clear();
                        repos.addAll(value);
                        reposAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "onError");
                    }

                    @Override
                    public void onComplete() {
                        Timber.i("onComplete");
                    }
                });
        */
    }

    private void loadBannerAd() {
        AdView adView = (AdView) findViewById(R.id.main_adView);
        AdRequest adRequest = new AdRequest.Builder()
                // https://firebase.google.com/docs/admob/android/targeting#test_ads
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("D7A3AA3A1DAE36977EE1652523B33CBD")
                .build();
        adView.loadAd(adRequest);
    }

    private void setupRecyclerView() {
        adapter = new ReposCursorAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.main_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    // Downloads github.com/trending as String
    private Observable<String> getHtml() {
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("https://github.com/trending")
                .build();

        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Response response = client.newCall(request).execute();
                String html = response.body().string();
                response.body().close();
                return html;
            }
        });
    }

    // Gets the HTML, parses it and saves the data to the DB
    private void getHtmlParseAndSaveItToDb() {
        getHtml()
                .subscribeOn(Schedulers.io())
                .map(new Function<String, List<String>>() {
                    @Override
                    public List<String> apply(String html) throws Exception {
                        Document document = Jsoup.parse(html);
                        Elements elements = document.select(".repo-list-name > a");
                        List<String> urls = new ArrayList<>();
                        for (int i = 0; i < elements.size(); i++) {
                            urls.add(elements.get(i).attr("href"));
                        }
                        Timber.d("urls %s", urls);
                        return urls;
                    }
                })
                .doOnNext(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        // Delete all items in DB
                        getContentResolver().delete(DbContract.Repo.CONTENT_URI, null, null);
                        // Save to DB
                        for (int i = 0; i < strings.size(); i++) {
                            ContentValues values = new ContentValues();
                            values.put(DbContract.Repo.COLUMN_URL, strings.get(i));
                            getContentResolver()
                                    .insert(DbContract.Repo.CONTENT_URI, values);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<String>>() {
                    @Override
                    public void onNext(List<String> value) {
                        Timber.d("onNext: %s", value);
                    }
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "onError");
                    }
                    @Override
                    public void onComplete() {
                        Timber.i("onComplete");
                    }
                });
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
            Timber.d("onLoadFinished() null or empty");
            adapter.swapCursor(null);
            // TODO show progress
            // TODO download data
        } else {
            adapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
