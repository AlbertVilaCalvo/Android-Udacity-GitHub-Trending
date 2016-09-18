package eu.albertvila.udacity.githubtrending.data.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import eu.albertvila.udacity.githubtrending.data.db.DbContract;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DefaultObserver;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by Albert Vila Calvo on 18/9/16.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, final ContentProviderClient contentProviderClient, SyncResult syncResult) {
        getHtml()
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
                        contentProviderClient.delete(DbContract.Repo.CONTENT_URI, null, null);
                        // Save to DB
                        for (int i = 0; i < strings.size(); i++) {
                            ContentValues values = new ContentValues();
                            values.put(DbContract.Repo.COLUMN_URL, strings.get(i));
                            contentProviderClient.insert(DbContract.Repo.CONTENT_URI, values);
                        }
                    }
                })
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

    // Gets github.com/trending as String
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

}
