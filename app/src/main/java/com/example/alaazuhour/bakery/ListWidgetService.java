package com.example.alaazuhour.bakery;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

/**
 * Created by alaazuhouer on 03/10/17.
 */

public class ListWidgetService extends RemoteViewsService {
    List<String> remoteIngredientsList;
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return null;
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        Context mContext = null;

        public ListRemoteViewsFactory(Context context,Intent intent) {
            mContext = context;

        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
          remoteIngredientsList= BakeryAppWidget.ingredientsList;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return remoteIngredientsList.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_view_item);

            views.setTextViewText(R.id.widget_text, remoteIngredientsList.get(i));

            Intent intent = new Intent();
            views.setOnClickFillInIntent(R.id.widget_text, intent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
