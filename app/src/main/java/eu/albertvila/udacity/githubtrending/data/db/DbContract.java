package eu.albertvila.udacity.githubtrending.data.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import eu.albertvila.udacity.githubtrending.BuildConfig;

/**
 * Created by Albert Vila Calvo on 14/9/16.
 */
public class DbContract {

    public static final String AUTHORITY = String.format("%s.provider", BuildConfig.APPLICATION_ID);

    public static final Uri AUTHORITY_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .build();

    public interface Repo extends BaseColumns {
        String TABLE_NAME = "repo";
        String PATH = "repo";
        // Columns
        String URL = "url";
    }

}
