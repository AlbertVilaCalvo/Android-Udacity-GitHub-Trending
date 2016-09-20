package eu.albertvila.udacity.githubtrending.ui.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Albert Vila Calvo on 20/9/16.
 */
public class WidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this, intent);
    }

}
