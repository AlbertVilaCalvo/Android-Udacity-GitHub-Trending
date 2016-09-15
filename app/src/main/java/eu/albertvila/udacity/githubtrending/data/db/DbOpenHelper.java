package eu.albertvila.udacity.githubtrending.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Albert Vila Calvo on 14/9/16.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "repo.db";

    private static final String CREATE_REPO_TABLE = ""
            + "CREATE TABLE " + DbContract.Repo.TABLE_NAME + "("
            + DbContract.Repo._ID + " INTEGER NOT NULL PRIMARY KEY,"
            + DbContract.Repo.URL + " TEXT NOT NULL"
            + ")";

    public DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_REPO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
