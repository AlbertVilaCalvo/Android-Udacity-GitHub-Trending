package eu.albertvila.udacity.githubtrending.data.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import eu.albertvila.udacity.githubtrending.BuildConfig;

/**
 * Created by Albert Vila Calvo on 14/9/16.
 */
public class DbContract {

    // Important: this constant *must* be the same as the string resource content_provider_authority
    public static final String AUTHORITY = String.format("%s.provider", BuildConfig.APPLICATION_ID);

    public static final Uri AUTHORITY_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .build();

    public interface Repo extends BaseColumns {
        String TABLE_NAME = "repo";
        String PATH = "repo";
        Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH);
        // https://developer.android.com/guide/topics/providers/content-provider-creating.html#TableMIMETypes
        String MIME_TYPE_DIR = String.format("%s/vnd.%s.%s",
            ContentResolver.CURSOR_DIR_BASE_TYPE,
            AUTHORITY,
            PATH
        );
        // This is also correct:
//        String MIME_TYPE_DIR =
//                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH;

        // Columns
        String COLUMN_URL = "url";
    }

}
