package eu.albertvila.udacity.githubtrending.data.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

public class DbProvider extends ContentProvider {

    // Maps a URI to a table
    private static final int MATCH_ALL_REPOS = 100;

    // Maps a URI to an int
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(DbContract.AUTHORITY, DbContract.Repo.PATH, MATCH_ALL_REPOS);
    }

    private SQLiteOpenHelper sqLiteOpenHelper;

    @Override
    public boolean onCreate() {
        // Initialize your content provider on startup
        // See https://developer.android.com/guide/topics/providers/content-provider-creating.html#OnCreate
        sqLiteOpenHelper = new DbOpenHelper(getContext());

        // return true if the provider was successfully loaded
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int match = URI_MATCHER.match(uri);
        long id;
        switch (match) {
            case MATCH_ALL_REPOS:
                id = sqLiteOpenHelper
                        .getWritableDatabase()
                        .insertOrThrow(DbContract.Repo.TABLE, null, values);
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
