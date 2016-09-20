package eu.albertvila.udacity.githubtrending.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import eu.albertvila.udacity.githubtrending.R;
import eu.albertvila.udacity.githubtrending.data.db.DbContract;

/**
 * Created by Albert Vila Calvo on 20/9/16.
 */
public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    // https://developer.android.com/guide/topics/appwidgets/index.html#collections

    // In the context of an app widget, the Adapter is replaced by a RemoteViewsFactory, which
    // is simply a thin wrapper around the Adapter interface

    private Context context;

    public WidgetRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
    }

    private Cursor cursor;

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }

        // This method is called by the app hosting the widget (e.g., the launcher)
        // However, our ContentProvider is not exported so it doesn't have access to the
        // data. Therefore we need to clear (and finally restore) the calling identity so
        // that calls use our process and permission
        long identityToken = Binder.clearCallingIdentity();

        cursor = context.getContentResolver()
                .query(
                        DbContract.Repo.CONTENT_URI,
                        new String[]{
                                DbContract.Repo._ID,
                                DbContract.Repo.COLUMN_URL,
                                DbContract.Repo.COLUMN_DESCRIPTION},
                        null, // all rows
                        null,
                        null  // default order
                );

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    @Override
    public int getCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || cursor == null || !cursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_item_repo_widget);

        String repoUrl = cursor.getString(cursor.getColumnIndex(DbContract.Repo.COLUMN_URL));
        String repoDescription = cursor.getString(cursor.getColumnIndex(DbContract.Repo.COLUMN_DESCRIPTION));

        remoteViews.setTextViewText(R.id.list_item_repo_url, repoUrl);
        remoteViews.setTextViewText(R.id.list_item_repo_description, repoDescription);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(context.getPackageName(), R.layout.list_item_repo);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if (cursor.moveToPosition(position)) {
            return cursor.getLong(cursor.getColumnIndex(DbContract.Repo._ID));
        } else {
            return position;
        }
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
