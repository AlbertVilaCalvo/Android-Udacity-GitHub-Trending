package eu.albertvila.udacity.githubtrending.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import eu.albertvila.udacity.githubtrending.R;
import eu.albertvila.udacity.githubtrending.ui.repolist.RepoListActivity;

/**
 * Created by Albert Vila Calvo on 20/9/16.
 */
public class WidgetProvider extends AppWidgetProvider {

    // AppWidgetProvider extends BroadcastReceiver
    // https://developer.android.com/guide/topics/appwidgets/index.html#AppWidgetProvider

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

            remoteViews.setRemoteAdapter(
                    R.id.widget_listView,
                    new Intent(context, WidgetRemoteViewsService.class));

            Intent clickIntent = new Intent(context, RepoListActivity.class);
            PendingIntent clickPendingIntent = PendingIntent.getActivity(context, 0, clickIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget_layout, clickPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    // Our SyncAdapter will send a broadcast every time it updates the data inside
    // the ContentProvider. See:
    // https://www.youtube.com/watch?v=DJsNmS-PvD8
    // https://www.youtube.com/watch?v=9GyoNEbouYc
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent); // DO NOT REMOVE THIS LINE

        if (context.getString(R.string.action_data_updated).equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds =
                    appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listView);
        }
    }

}
