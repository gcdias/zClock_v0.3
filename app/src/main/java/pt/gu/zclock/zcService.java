package pt.gu.zclock;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by GU on 26-04-2015.
 */
public class zcService extends Service {

    private final String       TAG   = "zcService";
    private boolean            debug = true;

    public static final String ZC_BROADCASTUPDATE = "pt.gu.zclock.service";
    public static final String ZC_FORECASTUPDATE  = "pt.gu.zclock.forecast";
    public static final String ZC_LOCATIONUPDATE = "pt.gu.zclock.location";

    private zcProvider         mProvider = zcProvider.getInstance();
    private BroadcastReceiver  mReceiver = null;
    private SharedPreferences  mPreferences;
    private zcWidgetManager    mManager;
    private boolean            pmScreenOn = true;

    private IntentFilter intentFilter;{
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(ZC_FORECASTUPDATE);
        intentFilter.addAction(ZC_LOCATIONUPDATE);
        intentFilter.addAction(ZC_BROADCASTUPDATE);
    }

    private BroadcastReceiver  intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();

            ComponentName widgets = new ComponentName(context, zcProvider.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);

            if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                if (debug) Log.e(TAG + ".onReceive", "Screen OFF, suspending...");
                pmScreenOn = false;
            } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
                if (debug) Log.e(TAG + ".onReceive", "Screen ON: updating");
                pmScreenOn = true;
                updateWidgets(context, manager, manager.getAppWidgetIds(widgets));
            }

            if (pmScreenOn) {
                if (debug) Log.e(TAG + ".onReceive", "Screen on");
                if (action.equals(Intent.ACTION_TIME_CHANGED) || action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                    mManager.updateLocation();
                    if (debug) Log.e(TAG + ".onReceive", "Time/Timezone changed");
                    updateWidgets(context, manager, manager.getAppWidgetIds(widgets));
                }

                if (action.equals(Intent.ACTION_TIME_TICK)) {
                    if (debug) Log.e(TAG + ".onReceive", "Time-tick");
                    mManager.updateLocation();
                    mManager.updateForecast();
                    updateWidgets(context, manager, manager.getAppWidgetIds(widgets));
                }

                if (action.equals(ZC_BROADCASTUPDATE)) {
                    if (debug) Log.e(TAG + ".onReceive", "zc_broadcast Intent");
                    updateWidgets(context, manager, manager.getAppWidgetIds(widgets));
                }
            }

            if (action.equals(ZC_FORECASTUPDATE)){
                if (debug) Log.e(TAG + "onReceive", "Forecast Update");

                mManager.updateWeatherData();
            }
        }
    };

    public zcService(){}

    @Override
    public void onCreate() {
        super.onCreate();
        if (debug) Log.e(TAG, "onCreate");
        Context context = getApplicationContext();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mManager     = new zcWidgetManager(context);
        registerReceiver(intentReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (debug) Log.e(TAG, String.format("onStartCommand: %s,%d,%d", intent.getAction(),flags,startId));

        registerReceiver(intentReceiver, intentFilter);

        return START_STICKY;
    }

    private void updateWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        if (debug) Log.e(TAG,"updateAppWidgets");

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            if (debug) Log.e(TAG,"updateAppWidget id:"+appWidgetId);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);

            Intent settingsIntent = new Intent(context, zcPreferences.class).setAction(zcPreferences.ACTION_PREFS);
            settingsIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, settingsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.imageView, pendingIntent);

            updateWidgetSize(context, appWidgetId);
            remoteViews.setImageViewBitmap(R.id.imageView,mManager.renderBitmap(appWidgetId));
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        }
    }


    private void updateWidgetSize(Context context, int appWidgetId) {

        if (debug) Log.e(TAG,"updateWidgetSize");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Bundle newOptions = AppWidgetManager.getInstance(context).getAppWidgetOptions(appWidgetId);
        int w, h, wCells, hCells;
        if (Resources.getSystem().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            w = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
            h = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        } else {
            w = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            h = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            wCells = (int) Math.ceil((w + 2) / 72);
            hCells = (int) Math.ceil((h + 2) / 72);
        } else {
            wCells = (int) Math.ceil((w + 30) / 70);
            hCells = (int) Math.ceil((h + 30) / 70);
        }

        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putFloat("widgetWidth" + appWidgetId, (float) w);
        ed.putFloat("widgetHeight" + appWidgetId, (float) h);
        ed.putInt("widgetCellWidth" + appWidgetId, wCells);
        ed.putInt("widgetCellHeight" + appWidgetId, hCells);
        ed.commit();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (debug) Log.e(TAG, "onDestroy");
        unregisterReceiver(intentReceiver);
    }

}
