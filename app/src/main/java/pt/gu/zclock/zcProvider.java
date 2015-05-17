package pt.gu.zclock;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link zcPreferences}
 */
public class zcProvider extends AppWidgetProvider {

    private static boolean      debug                 = true;
    private static String       TAG                   = "zcProvider";

    public static final String  ZC_APPWIDGETUPDATE = "pt.gu.zclock.appwidget";

    static final Intent         iService = new Intent(ZC_APPWIDGETUPDATE);
    static final ComponentName  mWidget = new ComponentName("pt.gu.zclock","pt.gu.zclock.zcProvider");

    private static      zcProvider sProvider;

    static synchronized zcProvider getInstance(){
        if (sProvider == null){
            sProvider = new zcProvider();
        }
        return sProvider;
    }

    private SharedPreferences   sharedPreferences;

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        if (debug) Log.e(TAG, "onDisabled");
        context.stopService(new Intent(zcService.ZC_BROADCASTUPDATE));
        if (debug) Toast.makeText(context, "zClock removed", Toast.LENGTH_SHORT).show();
        context.getApplicationContext().stopService(new Intent(context.getApplicationContext(), zcService.class));
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.clear();
        ed.commit();
        super.onDisabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        if (debug) Log.e(TAG, "onUpdate");
        context.startService(new Intent(context,zcService.class));
        context.sendStickyBroadcast(new Intent(zcService.ZC_BROADCASTUPDATE));

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {

        // When the user deletes the widget, delete the preference associated with it.

        if (debug) Log.e(TAG, "onDeleted "+appWidgetIds.length);

        for (int appWidgetId : appWidgetIds) {
            removeWidgetPreferences(context, appWidgetId);
        }
        super.onDeleted(context, appWidgetIds);
    }


    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);

        if (debug) Log.e(TAG,"onAppWidgetOptionsChanged");
        //context.startService(new Intent(zcService.ZC_BROADCASTUPDATE));

        //update widgets
        updateWidgetSize(context, appWidgetId);
        context.sendBroadcast(new Intent(zcService.ZC_BROADCASTUPDATE));
    }

    public void removeWidgetPreferences(Context context,int appWidgetId){

        if (debug) Log.e(TAG,"removeWidgetPreferences");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = sharedPreferences.edit();

        ed.remove("clockMode"+appWidgetId);

        ed.remove("zmanimMode"+appWidgetId);

        ed.remove("szZmanim_sun"+appWidgetId);
        ed.remove("szZmanim_main"+appWidgetId);
        ed.remove("szZmanimAlotTzet"+appWidgetId);
        ed.remove("szTimemarks"+appWidgetId);
        ed.remove("szTime"+appWidgetId);
        ed.remove("szDate"+appWidgetId);
        ed.remove("szParshat"+appWidgetId);
        ed.remove("szShemot"+appWidgetId);

        ed.remove("cClockFrameOn"+appWidgetId);
        ed.remove("cClockFrameOff"+appWidgetId);
        ed.remove("cZmanim_sun"+appWidgetId);
        ed.remove("cZmanim_main"+appWidgetId);
        ed.remove("cZmanimAlotTzet"+appWidgetId);
        ed.remove("cTimemarks"+appWidgetId);
        ed.remove("cTime"+appWidgetId);
        ed.remove("cDate"+appWidgetId);
        ed.remove("cParshat"+appWidgetId);
        ed.remove("cShemot"+appWidgetId);

        ed.remove("wClockMargin"+appWidgetId);
        ed.remove("wClockFrame"+appWidgetId);
        ed.remove("wClockPoer"+appWidgetId);
        ed.remove("resTimeMins "+appWidgetId);
        ed.remove("szTimeMins"+appWidgetId);

        ed.remove("tsZmanim_sun"+appWidgetId);
        ed.remove("tsZmanim_main"+appWidgetId);
        ed.remove("tsZmanimAlotTzet"+appWidgetId);
        ed.remove("tsTimemarks"+appWidgetId);

        ed.remove("iZmanim_sun"+appWidgetId);
        ed.remove("iZmanim_main"+appWidgetId);
        ed.remove("iZmanimAlotTzet"+appWidgetId);
        ed.remove("iTimemarks"+appWidgetId);

        //ed.remove("bAlotTzet72"+appWidgetId);
        ed.remove("showHebDate"+appWidgetId);
        ed.remove("showParashat"+appWidgetId);
        ed.remove("showAnaBekoach"+appWidgetId);
        ed.remove("show72Hashem"+appWidgetId);
        ed.remove("showZmanim"+appWidgetId);
        ed.remove("showTimeMarks"+appWidgetId);
        ed.remove("nShemot"+appWidgetId);

        ed.commit();

    }

    void updateWidgetSize(Context context, int appWidgetId) {

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

    private boolean hasInstances(Context context){
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] ids = manager.getAppWidgetIds(mWidget);
        return (ids.length>0);
    }

    //region Deprecated Code
    /*

    public  zcWeather           mWeather;
    public  zcLocation          mLocation;
    private long            lastLocationUpdate    = 0;
    private long            lastWeatherUpdate     = 0;
    private long            lastZmanimUpdate      = 0;

    public void updateClock(Context context){

        if (debug) Log.e(TAG,"updateClock");

        mManager = new zcWidgetManager(context);
        mManager.updateClock();
    }

    public void setLastWeatherUpdate(long time){
        lastWeatherUpdate = time;
    }



    public boolean checkLocationUpdate(Context context,boolean forceUpdate) {

        try {
            if (debug) Log.e(TAG, "checkLocationUpdate");
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            if (forceUpdate) {
                return mLocation.update();
            }
            long locationUpdateTimeout = 3 * 3600000;
            return System.currentTimeMillis() - sharedPreferences.getLong("lastLocationUpdate", 0) > locationUpdateTimeout && mLocation.update();
        } catch (NullPointerException ignore){
            return false;
        }
    }

    public boolean checkWeatherUpdate(Context context,boolean forceUpdate) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (debug) Log.e(TAG,"checkWeatherUpdate");

        if (sharedPreferences.getLong("lastLocationUpdate",0)==0){
            return false;
        }
        if (forceUpdate) {
            return mWeather.updateForecast();
        }
        long weatherUpdateTimeout = 3 * 3600000;
        if (System.currentTimeMillis()-sharedPreferences.getLong("lastForecastUpdate",0) > weatherUpdateTimeout){
            return mWeather.updateForecast();
        }
        return false;
    }


    public void setWeatherForecast(WeatherData[] hForecast) {
        if (debug) Log.e(TAG,"setWeatherForecast");
        weatherForecast = hForecast;
    }

    public WeatherData[] getWeatherForecast(){
        if (debug) Log.e(TAG,"getWeatherForecast");
        return weatherForecast;
    }
    */
    

    /*
    public static long getLastWeatherUpdate(){
        return lastWeatherUpdate;
    }
    public static void updateLocation(Context context){
        wManager = new zcWidgetManager(context);
        wManager.updateLocation(context);
    }

    public void setLastLocationUpdate(long time){
        if (debug) Log.e(TAG,"setLastLocationUpdate");
        lastLocationUpdate = time;
    }

    public long getLastLocationUpdate(){
        if (debug) Log.e(TAG,"getLastLocationUpdate");
        return lastLocationUpdate;
    }

    public static zcLocation getwLocation(){
        if (debug) Log.e(TAG,"getwLocation");
        return wLocation;
    }

    public static void setLastZmanimUpdate(long time){
        if (debug) Log.e(TAG,"setLastZmanimUpdate");
        lastZmanimUpdate = time;
    }

    public static long getLastZmanimUpdate(){
        if (debug) Log.e(TAG,"getLastZmanimUpdate");
        return lastZmanimUpdate;
    }

    public static boolean isInitialized() {
        return (wLocation == null);
    }

    public static void initialize(Context context) {
        if (debug) Log.e(TAG,"initialize");
        if (wLocation== null) wLocation= new zcLocation(context);
        if (wWeather == null) wWeather = new zcWeather(context,wLocation.latitude,wLocation.longitude);
        if (wManager == null) wManager = new zcWidgetManager(context);
    }*/
    //endregion
}
