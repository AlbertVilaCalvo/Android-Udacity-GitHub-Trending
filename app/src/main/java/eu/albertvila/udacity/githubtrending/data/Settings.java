package eu.albertvila.udacity.githubtrending.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import eu.albertvila.udacity.githubtrending.R;
import eu.albertvila.udacity.githubtrending.data.db.DbContract;
import timber.log.Timber;

/**
 * Created by Albert Vila Calvo on 19/9/16.
 */
public class Settings {

    private static final String SELECTED_LANGUAGE_KEY = "selected_language_key";

    private static Settings instance;

    public static synchronized Settings get(Context context) {
        if (instance == null) {
            instance = new Settings(context);
        }
        return instance;
    }

    private final Context context;
    private final String DEFAULT_SELECTED_LANGUAGE;

    private Settings(Context context) {
        this.context = context.getApplicationContext();
        DEFAULT_SELECTED_LANGUAGE = context.getString(R.string.default_selected_language);
    }

    private SharedPreferences prefs() {
        return PreferenceManager.getDefaultSharedPreferences(this.context);
    }

    public String getSelectedLanguage() {
        return prefs().getString(SELECTED_LANGUAGE_KEY, DEFAULT_SELECTED_LANGUAGE);
    }

    public void setSelectedLanguage(String language) {
        Timber.d("Set selected language to %s", language);
        prefs().edit().putString(SELECTED_LANGUAGE_KEY, language).apply();

        // Deleting all data will trigger a manual SyncAdapter sync when we go back to
        // RepoListActivity - see RepoListActivity.onLoadFinished()
        Timber.d("Delete all DB data");
        context.getContentResolver().delete(DbContract.Repo.CONTENT_URI, null, null);
    }

    public boolean isSelectedLanguage(String language) {
        return getSelectedLanguage().equals(language);
    }

    public String getGitHubUrl() {
        String language = getSelectedLanguage();
        if (language.equals(context.getString(R.string.all_languages))) {
            language = "";
        } else {
            language = language.replace(' ', '-');
            language = language.toLowerCase();
        }
        return "https://github.com/trending/" + language;
    }

}
