package com.sam_chordas.android.stockhawk.widget;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.MyDetailActivity;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by a rahuul on 4/21/2016.
 */
public class QuoteWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.i("ACTION","UPDATING ST");

        for(int i=0;i<appWidgetIds.length;i++)
        {
            Log.i("ACTION","UPDATING");
            Intent intent=new Intent(context,QuoteWidgetRemoteViewsService.class);
            RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.widget_collection);
            if(remoteViews!=null){
                Log.i("RV","NOT NULL");
            }
            remoteViews.setRemoteAdapter(appWidgetIds[i],R.id.widget_list,intent);

            Intent mainActivityIntent = new Intent(context, MyStocksActivity.class);
            PendingIntent mainActivityPendingItent = PendingIntent.getActivity(context, 1, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget, mainActivityPendingItent);

            Intent launchIntent = new Intent(context, MyStocksActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.widget_list, pendingIntent);

            //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i],R.id.widget_list_item);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equals("com.sam_chordas.android.stockhawk.ACTION_DATA_UPDATED"))
        {
            AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
            int[] appWidgetIds=appWidgetManager.getAppWidgetIds(new ComponentName(context,getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.widget_list);
            Log.i("BROADCAST","RECEIVED");
        }
    }
}