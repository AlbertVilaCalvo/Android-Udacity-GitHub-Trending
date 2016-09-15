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
                        .insertOrThrow(DbContract.Repo.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Invalid insert Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = URI_MATCHER.match(uri);
        int deletedRowCount;
        switch (match) {
            case MATCH_ALL_REPOS:
                deletedRowCount = sqLiteOpenHelper
                        .getWritableDatabase()
                        .delete(DbContract.Repo.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Invalid delete Uri: " + uri);
        }

        if (deletedRowCount != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deletedRowCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int match = URI_MATCHER.match(uri);
        int updatedRowCount;
        switch (match) {
            case MATCH_ALL_REPOS:
                updatedRowCount = sqLiteOpenHelper
                        .getWritableDatabase()
                        .update(DbContract.Repo.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Invalid update Uri: " + uri);
        }

        if (updatedRowCount != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updatedRowCount;
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
}
