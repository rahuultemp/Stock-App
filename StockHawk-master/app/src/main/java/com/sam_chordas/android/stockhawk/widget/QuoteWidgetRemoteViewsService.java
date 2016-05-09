package com.sam_chordas.android.stockhawk.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

/**
 * Created by a rahuul on 4/21/2016.
 */
public class QuoteWidgetRemoteViewsService extends RemoteViewsService {

    public Cursor mCursor=null;
    public Context context=getBaseContext();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.i("CONTEXT","context is "+context);

        Log.i("abc","def");

        return new RemoteViewsFactory() {
            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                Log.i("abc","def");
                if(mCursor!=null)
                {
                    mCursor.close();
                }

                final long identityToken = Binder.clearCallingIdentity();

                mCursor=getContentResolver().query(
                        QuoteProvider.Quotes.CONTENT_URI,
                        new String[]{ QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                                QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                        QuoteColumns.ISCURRENT + " = ?",
                        new String[]{"1"},
                        null);

                Binder.restoreCallingIdentity(identityToken);

            }

            @Override
            public void onDestroy() {
                if(mCursor!=null)
                {
                    mCursor.close();
                    mCursor=null;
                }

            }

            @Override
            public int getCount() {
                if(mCursor!=null)
                {
                    return mCursor.getCount();
                }
                else
                {
                    return 0;
                }
            }

            @Override
            public RemoteViews getViewAt(int position) {
                Log.i("MY_TAG","stockSymbol stockValue");

                if (position == AdapterView.INVALID_POSITION ||
                        mCursor == null || !mCursor.moveToPosition(position)) {
                    return null;
                }

                RemoteViews remoteView=new RemoteViews(getPackageName(), R.layout.widget_collection_item);
                String stockSymbol = mCursor.getString(1);
                String stockValue = mCursor.getString(4);

                if (mCursor.getInt(mCursor.getColumnIndex("is_up")) == 1){
                    remoteView.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
                } else {
                    remoteView.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
                }

                //Toast.makeText(StockWidgetRemoteViewsService.this, stockSymbol + stockValue, Toast.LENGTH_SHORT).show();
                Log.i("MY_TAG",stockSymbol+" "+stockValue);

                remoteView.setTextViewText(R.id.stock_symbol,stockSymbol);
                remoteView.setContentDescription(R.id.stock_symbol,"Stock Symbol "+stockSymbol);

                remoteView.setTextViewText(R.id.change, stockValue);
                remoteView.setContentDescription(R.id.change, "Stock Value " + stockValue);

                Bundle extras = new Bundle();
                extras.putString("SYMBOL", stockSymbol);

                Intent fillInIntent = new Intent();
                fillInIntent.putExtras(extras);
                remoteView.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

                return remoteView;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_collection_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if(mCursor.moveToPosition(position))
                {
                    return (mCursor.getLong(0));
                }
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };

    }
}