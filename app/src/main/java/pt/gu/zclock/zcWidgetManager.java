package pt.gu.zclock;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;

import net.sourceforge.zmanim.AstronomicalCalendar;
import net.sourceforge.zmanim.ComplexZmanimCalendar;
import net.sourceforge.zmanim.hebrewcalendar.HebrewDateFormatter;
import net.sourceforge.zmanim.hebrewcalendar.JewishCalendar;
import net.sourceforge.zmanim.util.GeoLocation;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import pt.gu.zclock.zcHelper.xColor;

/**
 * Created by GU on 11-04-2015.
 */
public class zcWidgetManager {

    private boolean      debug           = false;
    private final String TAG             = "zcWidgetManager";

    private final int    HASHEM_72       =0;
    private final int    HASHEM_72_SOF   =1;
    private final int    HASHEM_42       =2;
    private final String[] romanNumb     ={"I","II","III","IV","V","VI","VII","VIII","IX","X","XI","XII","XIII","XIV","XV","XVI"};

    private final String[] hebMonthsTransl = {"Nissan", "Iyar", "Sivan", "Tamuz", "Av", "Elul", "Tishri", "Mar Cheshvan",
            "Kislev", "Tevet", "Shevat", "Adar", "Adar II", "Adar I" };

    private final String[] hebHolTrans = { "Erev Pesach", "Pesach", "Chol Hamoed Pesach", "Pesach Sheni",
            "Erev Shavuot", "Shavuot", "17 Tammuz", "Tishah B'Av", "Tu B'Av", "Erev Rosh Hashana",
            "Rosh Hashana", "Jejum de Gedalyah", "Erev Yom Kippur", "Yom Kippur", "Erev Sukot", "Sukot",
            "Chol Hamoed Sukot", "Hoshana Rabbah", "Shemini Atzeret", "Simchat Torah", "Erev Chanukah", "Chanukah",
            "10 Tevet", "Tu B'Shvat", "Jejum de Ester", "Purim", "Shushan Purim", "Purim Katan", "Rosh Chodesh",
            "Yom HaShoah", "Yom Hazikaron", "Yom Ha'atzmaut", "Yom Yerushalayim" };

    private final String[] parshiotTransl = {
            "Bereshit", "Noach", "Lech-Lecha", "Vayera", "Chaye-Sarah", "Toldot","Vayetzei", "Vayishlach", "Vayeshev", "Miketz", "Vayigash", "Vayechi",
            "Shemot", "Vaera", "Bo", "Beshalach", "Yitro", "Mishpatim", "Terumah", "Tetzaveh", "Ki Tisa", "Vayakchel", "Pekude",
            "Vayikra", "Tzav", "Shemini", "Tazria", "Metzora", "Achre Mot", "Kedoshim", "Emor", "Behar", "Bechukotai",
            "Bamidbar", "Naso", "Behaalotcha", "Shelach", "Korach", "Chukat", "Balak", "Pinchas", "Matot", "Masei",
            "Devarim", "Vaetchanan", "Ekev", "Reeh", "Shoftim", "Ki Tetze", "Ki Tavo", "Nitzavim", "Vayelech", "HaAzinu",
            "Vayakchel Pekude", "Tazria Metzora", "Achre Mot Kedoshim", "Behar Bechukotai", "Chukat Balak",
            "Matot Masei", "Nitzavim Vayelech"};

    private final String[]          omerSefirot ={"Chesed","Gevurah","Tiferet","Netzach","Hod","Yesod","Malchut"};
    private final GeoLocation       HarHabait = new GeoLocation("Har Habait", 31.777972f, 35.235806f, 743, TimeZone.getTimeZone("Asia/Jerusalem"));



    private zClock                  mClock;
    private zcWeather               mWeather;
    private zcLocation              mLocation;
    private SharedPreferences       sharedPreferences;
    private Context                 mContext;
    private ComplexZmanimCalendar   zCalendar;
    private JewishCalendar          jCalendar;

    private Calendar                sCalendar = Calendar.getInstance();
    private HebrewDateFormatter     hebrewFormat;{
                                    hebrewFormat = new HebrewDateFormatter();
                                    hebrewFormat.setTransliteratedMonthList(hebMonthsTransl);
                                    hebrewFormat.setTransliteratedParshiosList(parshiotTransl);
                                    hebrewFormat.setTransliteratedHolidayList(hebHolTrans);
    }
    private Date                    alotHarHabait = new ComplexZmanimCalendar(HarHabait).getAlos72();

    private WeatherData[]           weatherForecast;

    private boolean                 clockUpdated = false;
    private final long              locationUpdateTimeout = 3*60*60*1000;
    private final long              forecastUpdateTimeout = 3*60*60*1000;
    private final long              zmanimUpdateTimeout   = 24*60*60*1000;


    public zcWidgetManager (Context context){
        this.mContext          = context.getApplicationContext();
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        this.mLocation         = new zcLocation(mContext);
        this.zCalendar         = new ComplexZmanimCalendar(mLocation.geoLocation());
        this.mWeather          = new zcWeather(mContext);
        this.mClock            = new zClock(mContext);
        this.jCalendar         = new JewishCalendar();
    }

    public void updateWeatherData() {
        weatherForecast = mWeather.get24hForecast(System.currentTimeMillis());
        mClock.setForecastData(weatherForecast);
    }

    public void updateLocation() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (System.currentTimeMillis()-sharedPreferences.getLong("lastLocationUpdate",0) > locationUpdateTimeout) {
            mLocation.update();
        }
    }

    public void updateForecast() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (System.currentTimeMillis()-sharedPreferences.getLong("lastForecastUpdate",0) > forecastUpdateTimeout || weatherForecast == null) {
            mWeather.updateForecast(mLocation.latitude,mLocation.longitude);
            mClock.setForecastData(weatherForecast);
        }
    }

    private void updateZmanin(int appWidgetId){

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        long newday = getNewDayTime(appWidgetId), now = System.currentTimeMillis();


        if (now-sharedPreferences.getLong("lastZmanimUpdate",0) > zmanimUpdateTimeout) {

            if (debug) Log.e(TAG, String.format("updateZmanim: %d,%d",now,newday));
            mLocation.update();
            zCalendar = new ComplexZmanimCalendar(mLocation.geoLocation());
            SharedPreferences.Editor ed = sharedPreferences.edit();
            ed.putLong("lastZmanimUpdate",newday);
            ed.apply();
            resetClock(appWidgetId);
        }

        if (!mClock.hasTimeMarks()){
            resetClock(appWidgetId);
        }
    }

    public Bitmap renderBitmap(int appWidgetId){

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        int cMode = getIntPref("clockMode", appWidgetId);

        long newday = getNewDayTime(appWidgetId);

        //boolean nday = (sCalendar.getTime().after(new Date(newday)));
        //sCalendar.add(Calendar.MINUTE, (int) ((86400000 - newday % 86400000) / 60000));
        //jCalendar = new JewishCalendar(new Date(System.currentTimeMillis()+getNewDayShift(newday)));

        boolean bkgDark = getBoolPref("bWhiteOnBlack", appWidgetId);

        switch (cMode) {

            default:
                if (debug) Log.e(TAG, "mClock mode <3");
                mClock.setup(appWidgetId,newday);
                updateZmanin(appWidgetId);

                if (getBoolPref("show72Hashem", appWidgetId)) {
                    mClock.setBackgroundPicture(
                            getHashemNames(mClock.getPxClock(), appWidgetId, HASHEM_72, bkgDark ? 0x08ffffff : 0x08000000, 0));
                }

                return mClock.draw(appWidgetId,newday);

            case 3:
                return getHashemNames(getWidgetSizePrefs(appWidgetId, true),
                        appWidgetId, HASHEM_72, bkgDark ? 0xffffffff : 0xff000000, bkgDark ? 0x80000000 : 0x80ffffff);

            case 4:
                return getHashemNames(getWidgetSizePrefs(appWidgetId, true),
                        appWidgetId, HASHEM_42, bkgDark ? 0xffffffff : 0xff000000, bkgDark ? 0x80000000 : 0x80ffffff);

            case 5:
                return renderPasuk(getWidgetSizePrefs(appWidgetId, true),
                        appWidgetId, 0, bkgDark ? 0xffffffff : 0xff000000, bkgDark ? 0x80000000 : 0x80ffffff);

        }
    }

    private Date getNewDateShift(int appWidgetId) {
        return new Date(System.currentTimeMillis()+getNewDayShift(appWidgetId));
    }

    public void resetClock(int appWidgetId) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        final Typeface tfThin = Typeface.create(mContext.getString(R.string.font_thin), Typeface.NORMAL);
        final Typeface tfCond = Typeface.create(mContext.getString(R.string.font_condensed), Typeface.NORMAL);
        final Typeface tfSTAM = Typeface.createFromAsset(mContext.getAssets(), "fonts/stmvelish.ttf");

        boolean bHeb = getBoolPref("bLangHebrew", appWidgetId);
        hebrewFormat.setHebrewFormat(bHeb);

        long newday = getNewDayTime(appWidgetId);

        jCalendar = new JewishCalendar(new Date(System.currentTimeMillis()+getNewDayShift(newday)));

        mClock.setup(appWidgetId, getNewDayTime(appWidgetId));

        int cTime  = getColorPref("cTime",appWidgetId);
        int cDate = xColor.copyAlpha(getColorPref("cDate", appWidgetId), cTime);
        int cParshat = xColor.copyAlpha(getColorPref("cParshat", appWidgetId), cTime);

        if (getBoolPref("showHebDate", appWidgetId)) {
            mClock.addLabel(hebrewFormat.format(jCalendar),
                    getDimensPref("szDate", appWidgetId),
                    cDate,
                    bHeb ? tfSTAM : tfCond,
                    getDimensPref("wClockMargin", appWidgetId));

            if (jCalendar.getDayOfOmer()>-1) {
                int o = jCalendar.getDayOfOmer()-1;
                String sep = bHeb ? hebrewFormat.getHebrewOmerPrefix() : mContext.getResources().getString(R.string.omerSep);
                String omer = String.format("%s (%s %s %s)",hebrewFormat.formatOmer(jCalendar), omerSefirot[o%7],sep,omerSefirot[(int)(o/7)]);
                mClock.addLabel(omer,
                        getDimensPref("szParshat", appWidgetId),
                        cParshat,
                        bHeb ? tfSTAM : tfThin,
                        getDimensPref("wClockMargin", appWidgetId));
            }

            if (jCalendar.isYomTov()){
                String yomtov =hebrewFormat.formatYomTov(jCalendar);
                if (jCalendar.getYomTovIndex() == JewishCalendar.PESACH) yomtov += " " + romanNumb[jCalendar.getDayOfOmer()];
                if (jCalendar.getYomTovIndex() == JewishCalendar.CHANUKAH) yomtov += " " + romanNumb[jCalendar.getDayOfChanukah()+1];
                mClock.addLabel(
                        yomtov,
                        getDimensPref("szParshat", appWidgetId),
                        cParshat,
                        bHeb ? tfSTAM : tfThin,
                        getDimensPref("wClockMargin", appWidgetId));
            }

            if (jCalendar.isErevYomTov()){
                mClock.addLabel(
                        bHeb ? " ערב" : "Erev " + hebrewFormat.formatYomTov(jCalendar),
                        getDimensPref("szParshat", appWidgetId),
                        cParshat,
                        bHeb ? tfSTAM : tfThin,
                        getDimensPref("wClockMargin", appWidgetId));
            }

            if (jCalendar.isRoshChodesh()) {
                mClock.addLabel(hebrewFormat.formatRoshChodesh(jCalendar),
                        getDimensPref("szParshat", appWidgetId),
                        cParshat,
                        bHeb ? tfSTAM : tfThin,
                        getDimensPref("wClockMargin", appWidgetId));
            }
        }

        if (getBoolPref("showParashat", appWidgetId)) {
            String s = getParshaHashavua(bHeb, false);
            if (s.length() != 0) {
                mClock.addLabel(s,
                        getDimensPref("szParshat", appWidgetId),
                        cParshat,
                        bHeb ? tfSTAM : tfThin,
                        getDimensPref("wClockMargin", appWidgetId));
            }
        }

        setZmanimMarks(appWidgetId);
    }

    private long getNewDayShift(int appWidgetId) {
        return 86400000-(getNewDayTime(appWidgetId)%86400000);
    }

    private long getNewDayShift(long newdaytime) {
        return 86400000-(newdaytime%86400000);
    }

    private void setZmanimMarks(int appWidgetId) {

        final Typeface tfCondN = Typeface.create(mContext.getString(R.string.font_condensed), Typeface.NORMAL);
        final Typeface tfLight = Typeface.create(mContext.getString(R.string.font_light), Typeface.NORMAL);
        final Typeface tfRegularB = Typeface.create(mContext.getString(R.string.font_regular), Typeface.BOLD);

        mClock.resetTimeMarks();

        int cTime  = getColorPref("cTime",appWidgetId);
        int cFrame  = getColorPref("cClockFrameOn",appWidgetId);
        int cHours = xColor.copyAlpha(getColorPref("cTimemarks", appWidgetId), cTime);
        int czSun = xColor.copyAlpha(getColorPref("cZmanim_sun", appWidgetId), cFrame);
        int czMain = xColor.copyAlpha(getColorPref("cZmanim_main", appWidgetId),cFrame);

        //mClock Hours 0-23h
        if (getBoolPref("showTimeMarks", appWidgetId)) {
            Date[] timeHours = new Date[24];
            Calendar c = Calendar.getInstance();
            Date d;
            for (int i = 0; i < 24; i++) {
                d = c.getTime();
                d.setHours(i);
                d.setMinutes(0);
                timeHours[i] = d;
            }
            mClock.addMarks(tfCondN,
                    cHours,
                    getDimensPref("szTimemarks", appWidgetId),
                    getStringPref("tsTimemarks", appWidgetId),
                    getBoolPref("iTimemarks", appWidgetId),
                    timeHours);
        }

        int zMode = getIntPref("zmanimMode", appWidgetId);
        Date[] sunsr;

        switch (zMode) {
            case 1:
                sunsr = new Date[]{zCalendar.getTzais60(), zCalendar.getAlos60()};
                break;
            case 2:
                sunsr = new Date[]{zCalendar.getTzais72(), zCalendar.getAlos72()};
                break;
            case 3:
                sunsr = new Date[]{zCalendar.getTzais90(), zCalendar.getAlos90()};
                break;
            case 4:
                sunsr = new Date[]{zCalendar.getTzais120(), zCalendar.getAlos120()};
                break;
            case 5:
                sunsr = new Date[]{zCalendar.getTzais16Point1Degrees(), zCalendar.getAlos16Point1Degrees()};
                break;
            case 6:
                sunsr = new Date[]{zCalendar.getTzais18Degrees(), zCalendar.getAlos18Degrees()};
                break;
            case 7:
                sunsr = new Date[]{zCalendar.getTzais19Point8Degrees(), zCalendar.getAlos19Point8Degrees()};
                break;
            case 8:
                sunsr = new Date[]{zCalendar.getTzais26Degrees(), zCalendar.getAlos26Degrees()};
                break;
            default:
                sunsr = new Date[]{zCalendar.getSunset(), zCalendar.getSunrise()};
        }

        mClock.setNewDayTimeMilis(sunsr[0].getTime());

        Date d1 = (zMode == 0) ? zCalendar.getTzais() : zCalendar.getSunset();
        Date d2 = (zMode == 0) ? zCalendar.getAlosHashachar() : zCalendar.getSunrise();

        mClock.addMarks(tfRegularB,
                czSun,
                getDimensPref("szZmanim_sun", appWidgetId),
                getStringPref("tsZmanim_sun", appWidgetId),
                getBoolPref("iZmanim_sun", appWidgetId),
                new Date[]{
                        sunsr[0],
                        sunsr[1],
                        d1,
                        d2,
                        zCalendar.getChatzos(),
                        zCalendar.getSolarMidnight()});

        if (jCalendar.getDayOfWeek() == 6) {
            mClock.addMarks(tfRegularB,
                    0xff800000,
                    getDimensPref("szZmanim_sun", appWidgetId),
                    getStringPref("tsZmanim_sun", appWidgetId),
                    getBoolPref("iZmanim_sun", appWidgetId),
                    new Date[]{zCalendar.getCandleLighting()});
        }

        if (jCalendar.getDayOfWeek() == 7) {
            mClock.addMarks(tfRegularB,
                    0xff804000,
                    getDimensPref("szZmanim_sun", appWidgetId),
                    getStringPref("tsZmanim_sun", appWidgetId),
                    getBoolPref("iZmanim_sun", appWidgetId),
                    new Date[]{sunsr[0]});
        }

        if (getBoolPref("showZmanim", appWidgetId)) {

            mClock.addMarks(tfLight,
                    czMain,
                    getDimensPref("szZmanim_main", appWidgetId),
                    getStringPref("tsZmanim_main", appWidgetId),
                    getBoolPref("iZmanim_main", appWidgetId),
                    new Date[]{
                            alotHarHabait,
                            zCalendar.getSunriseOffsetByDegrees(AstronomicalCalendar.ASTRONOMICAL_ZENITH - 11),
                            zCalendar.getSofZmanShma(sunsr[1], sunsr[0]),
                            zCalendar.getSofZmanTfila(sunsr[1], sunsr[0]),
                            zCalendar.getMinchaKetana(sunsr[1], sunsr[0]),
                            zCalendar.getMinchaGedola(sunsr[1], sunsr[0]),
                            zCalendar.getPlagHamincha(sunsr[1], sunsr[0])
                    });

        }

        mClock.updateTimeMarks();
    }

    private Bitmap getHashemNames(PointF size,int appWidgetId,int type, int colorForeground, int colorBackground){

        //PointF wdgtSize = getWidgetSizePrefs(appWidgetId, true);
        int index;
        int glowSteps = (colorBackground == 0) ? 0 : 3;
        float f = 0;
        String s1 = "", s2[] = null;
        if (type < 2) {
            index = (int) (sCalendar.getTime().getTime() / 60000 / getIntPref("nShemot", appWidgetId)) % 72;
            try {
                s1 = new String(
                        Base64.decode(
                                mContext.getResources().getStringArray(R.array.short_shemot)[type], Base64.DEFAULT), "UTF-8")
                        .split("\\r?\\n")[index];
                /*if (wdgtSize.x > 2 * wdgtSize.y) {
                    s2 = new String[]{String.valueOf(index)};
                    f = 10f;
                } else */
                {
                    s2 = new String(
                            Base64.decode(
                                    mContext.getResources().getStringArray(R.array.long_shemot)[0], Base64.DEFAULT), "UTF-8")
                            .split("\\r?\\n");
                    f = 0f;
                }
            } catch (UnsupportedEncodingException ignored) {
                if (debug) Log.e("getHashemNames","UnsupportedEncoding");
            }

        } else {
            index =jCalendar.getDayOfWeek();
            try {
                s1 = new String(
                        Base64.decode(
                                mContext.getResources().getStringArray(R.array.short_ab)[index - 1], Base64.DEFAULT), "UTF-8");
                s2 = new String[]{new String(
                        Base64.decode(
                                mContext.getResources().getStringArray(R.array.long_ab)[index - 1], Base64.DEFAULT), "UTF-8")};
            } catch (UnsupportedEncodingException ignored) {
            }
            f = 0;
        }

        return renderText(
                size,
                Typeface.createFromAsset(mContext.getAssets(), "fonts/stmvelish.ttf"),
                s1, s2,
                colorForeground, 0, colorForeground, f,
                glowSteps, colorBackground, 13);
    }

    private String[] getCurrentPasuk(int mode) {
        int l = 0, index;
        int d = jCalendar.getDayOfWeek() - 1;
        int dm = (int) ((zCalendar.getSunset().getTime() - zCalendar.getSunrise().getTime()) / 60000);
        int m = (int) (sCalendar.getTime().getTime() / 60000) % 1440;
        String pasuk, ref;
        try {
            int i = getParshaHashavuaIndex(sCalendar);
            if (debug) Log.e("Parsha index",String.valueOf(i));

            String[] source = mContext.getResources().getStringArray(R.array.torah)[i].split("#");
            int[] yom = decodeB64Int(source[0]); //String[] yom = decodeB64String(source[0]);//new String(Base64.decode(source[0], Base64.DEFAULT), "UTF-8").split("\\r?\\n");
            String[] parsha;

            if (i>52) {
                int[] p  = decodeB64Int(source[1]);//new String(Base64.decode(source[1], Base64.DEFAULT), "UTF-8").split("\\r?\\n");
                String[] p1 = new String(Base64.decode(mContext.getResources().getStringArray(R.array.torah)[p[0]].split("#")[1], Base64.DEFAULT), "UTF-8").split("\\r?\\n");
                String[] p2 = new String(Base64.decode(mContext.getResources().getStringArray(R.array.torah)[p[1]].split("#")[1], Base64.DEFAULT), "UTF-8").split("\\r?\\n");
                //String[] p1 = new String(Base64.decode(p[0], Base64.DEFAULT), "UTF-8").split("\\r?\\n");
                //String[] p2 = new String(Base64.decode(p[1], Base64.DEFAULT), "UTF-8").split("\\r?\\n");
                parsha = new String[p1.length+p2.length];
                System.arraycopy(p1,0,parsha,0,p1.length);
                System.arraycopy(p2,0,parsha,p1.length,p2.length);
            } else {
                parsha = new String(Base64.decode(source[1], Base64.DEFAULT), "UTF-8").split("\\r?\\n");
            }

            int v = 0;
            for (int n = 0; n < d; n++) {
                //v += Integer.valueOf(yom[n]);
                v += yom[n];
            }

            index = 1 + v + (m * yom[d] / 1440);
            if (debug) Log.e("Parashat", String.format("parsha %d day %d line %d/%d", i, d + 1, v, index));
            pasuk = parsha[index];
            int iref = pasuk.indexOf(" ");
            ref = (iref > 0) ? String.format("%s %s", getParshaHashavua(true, true), pasuk.substring(0, iref)) : "error";
            pasuk = (iref > 0) ? pasuk.substring(iref + 1) : String.format("ERRO %d/%d", index, l);
            if (mode ==1) pasuk = toNiqqud(pasuk);
            if (mode > 1) pasuk = toOtiot(pasuk);
        } catch (Exception ignored) {
            if (debug) Log.e(TAG,"decoding pasuk error: "+ignored.toString());
            return null;
        }
        return new String[]{ref,pasuk};
    }

    private String getParshaHashavua(boolean inHebrew, boolean parshaNameOnly) {
        int day = jCalendar.getDayOfWeek();
        String result = (parshaNameOnly) ? "" : (inHebrew) ? ((day % 7 == 0) ? "שבת " : "פרשת השבוע ") : ((day % 7 == 0) ? "Shabbat " : "Parashat Hashavua ");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 7 - day);
        hebrewFormat.setHebrewFormat(inHebrew);
        String p =hebrewFormat.formatParsha(new JewishCalendar(c.getTime()));
        return (p == "") ? "" : result + p ;
    }

    private int getParshaHashavuaIndex(Calendar cal) {
        Calendar c = (Calendar) cal.clone();
        int day = c.get(Calendar.DAY_OF_WEEK);
        c.add(Calendar.DATE, 7 - day);
        return new JewishCalendar(c).getParshaIndex();
    }

    private long getNewDayTime(int appWidgetId) {

        switch (getIntPref("zmanimMode", appWidgetId)) {
            case 1:
                return zCalendar.getTzais60().getTime();
            case 2:
                return zCalendar.getTzais72().getTime();
            case 3:
                return zCalendar.getTzais90().getTime();
            case 4:
                return zCalendar.getTzais120().getTime();
            case 5:
                return zCalendar.getTzais16Point1Degrees().getTime();
            case 6:
                return zCalendar.getTzais18Degrees().getTime();
            case 7:
                return zCalendar.getTzais19Point8Degrees().getTime();
            case 8:
                return zCalendar.getTzais26Degrees().getTime();
            default:
                return zCalendar.getSunset().getTime();
        }
    }

    //region String helper Methods

    private String[] decodeB64String(String source) throws UnsupportedEncodingException{
        return new String(Base64.decode(source, Base64.DEFAULT), "UTF-8").split("\\r?\\n");
    }

    private int[] decodeB64Int(String source) throws UnsupportedEncodingException{
        String[] s = new String(Base64.decode(source, Base64.DEFAULT), "UTF-8").split("\\r?\\n");
        int res[] = new int[s.length];
        for(int i=0;i<s.length;i++){
            res[i]=Integer.parseInt(s[i]);
        }
        return res;
    }

    private String toNiqqud(String txt) {
        String res = "";
        txt = txt.replace("{\u05e1}","\u05c8")
                 .replace("{\u05e4}","\u05c9")
                 .replace("{\u05e8}","\u05ca")
                 .replace("{\u05e9}","\u05cb");
        for (char c : txt.toCharArray())
            if (c < 0x041 || c == 0x05be || (c > 0x05af && c < 0x05eb && c!=0x05bd)) {
                res += c;
            }
        return res;
    }

    private String toOtiot(String txt) {
        String res = "";
        for (char c : txt.toCharArray()) {
            if (c > 0x05ef && c < 0x05eb) res += c;
        }
        return res;
    }

    //endregion

    //region SharedPreferences helper Methods

    private int     getIntPref( String key, int appWidgetId) {
        int ResId = mContext.getResources().getIdentifier(key, "integer", mContext.getPackageName());
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt(key + appWidgetId, mContext.getResources().getInteger(ResId));
    }

    private float   getDimensPref( String key, int appWidgetId) {
        int ResId = mContext.getResources().getIdentifier(key, "dimen", mContext.getPackageName());
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt(key + appWidgetId, 100) / 100f * mContext.getResources().getDimension(ResId);
    }

    private float   getSizePref( String key, int appWidgetId) {
        int ResId = mContext.getResources().getIdentifier(key, "dimen", mContext.getPackageName());
        return sharedPreferences.getFloat(key + appWidgetId, mContext.getResources().getDimension(ResId));
    }

    private String  getStringPref( String key, int appWidgetId) {
        int ResId = mContext.getResources().getIdentifier(key, "string", mContext.getPackageName());
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString(key + appWidgetId, mContext.getResources().getString(ResId));
    }

    private boolean getBoolPref( String key, int appWidgetId) {
        int ResId = mContext.getResources().getIdentifier(key, "bool", mContext.getPackageName());
        return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(key + appWidgetId, mContext.getResources().getBoolean(ResId));
    }

    private int     getColorPref( String key, int appWidgetId) {
        int ResId = mContext.getResources().getIdentifier(key, "color", mContext.getPackageName());
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt(key + appWidgetId, mContext.getResources().getColor(ResId));
    }
    //endregion

    //region draw methods
    //updated to zcZmanim
    private Bitmap renderText(PointF size,
                             Typeface typeface,
                             String title, String[] subtitle,
                             int title_color, float title_size,
                             int subtitle_color, float subtitle_size,
                             int glowSteps,
                             int bkgColor,
                             float corners) {

        Bitmap bitmap = Bitmap.createBitmap((int) size.x, (int) size.y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        PointF centro = new PointF(size.x / 2, size.y / 2);

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
        p.setColor(bkgColor);
        canvas.drawRoundRect(new RectF(0, 0, size.x, size.y), corners, corners, p);
        float y_pos = centro.y;
        p.setTypeface(typeface);
        p.setTextAlign(Paint.Align.CENTER);

        //Draw title
        if (title != null) {
            Rect b = new Rect();
            if (title_size == 0) {
                p.setTextSize(100);
                p.getTextBounds(title, 0, title.length(), b);
                p.setTextSize(Math.min(90 * size.x / b.width(), 50 * size.y / b.height()));
            } else p.setTextSize(title_size);
            p.setColor(title_color);
            p.getTextBounds(title, 0, title.length(), b);
            y_pos = b.height();
            if (glowSteps > 0) {
                float blur_rad = mClock.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22 / glowSteps);
                p.setAlpha(128);
                for (int i = 0; i < glowSteps; i++) {
                    canvas.drawText(title, centro.x, y_pos, p);
                    blur_rad = blur_rad / 2;
                }
                p.setMaskFilter(null);
            }
            p.setColor(title_color);
            canvas.drawText(title, centro.x, y_pos, p);
        }


        //Draw subtitle
        if (subtitle != null) {
            Rect b = new Rect();
            if (subtitle_size == 0) {
                p.setTextSize(100);
                p.getTextBounds(subtitle[0], 0, subtitle[0].length(), b);
                subtitle_size = Math.min(80 * size.x / b.width(), 50 * size.y / b.height() / subtitle.length);
            }
            p.setTextSize(subtitle_size);
            p.getTextBounds(subtitle[0], 0, subtitle[0].length(), b);
            y_pos = size.y*0.56f;
            for (String st : subtitle) {
                p.setColor(subtitle_color);
                canvas.drawText(st, centro.x, y_pos - 2.5f * (p.descent() + p.ascent()), p);
                y_pos += b.height();
            }
        }

        return bitmap;
    }

    //updated to zcZmanim
    private Bitmap renderBackground(Bitmap bitmap, int bkgColor, float corners) {
        Canvas canvas = new Canvas(bitmap);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(bkgColor);
        canvas.drawRoundRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), corners, corners, p);
        return bitmap;
    }

    //updated to zcZmanim
    private Bitmap renderTextBlock(Bitmap bitmap,
                                  Typeface typeface,
                                  String text,
                                  int color, float size, float yPos) {

        Canvas canvas = new Canvas(bitmap);
        float margin=0.92f;

        //if (debug) Log.e("Draw Parsha", text);
        float slmargin = bitmap.getWidth() * (1-margin);
        float slwidth = bitmap.getWidth() * margin;
        TextPaint p = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(color);
        p.setTypeface(typeface);
        p.setTextSize(size);
        canvas.translate(slmargin/2, yPos);
        canvas.save();
        //p.setMaskFilter(new BlurMaskFilter(7f, BlurMaskFilter.Blur.NORMAL));
        StaticLayout sl;
        do {
            sl = new StaticLayout("" + text, p, (int) slwidth, Layout.Alignment.ALIGN_NORMAL, margin, 0.5f, false);
            p.setTextSize(--size);
        } while ((sl.getHeight() > bitmap.getHeight() * margin));

        sl.draw(canvas);
        //p.setMaskFilter(null);
        //sl = new StaticLayout(""+text,p,(int)(bitmap.getWidth()*0.95f), Layout.Alignment.ALIGN_NORMAL,1.0f,0.0f,false);
        //sl.draw(canvas);
        canvas.restore();
        return bitmap;
    }



    private Bitmap renderPasuk( PointF size, int appWidgetId, int type, int fColor, int bColor) {

        boolean bkgDark = getBoolPref("bWhiteOnBlack", appWidgetId);
        Bitmap bitmap = Bitmap.createBitmap((int) size.x, (int) size.y, Bitmap.Config.ARGB_8888);
        bitmap = renderBackground(bitmap, bkgDark ? 0x80000000 : 0x80ffffff, 13);

        final Typeface tfStam = Typeface.createFromAsset(mContext.getAssets(), "fonts/stmvelish.ttf");

        String[] currentPasuk = getCurrentPasuk(1);

        if (currentPasuk==null) return bitmap;

        renderTextBlock(bitmap, tfStam, currentPasuk[0], bkgDark ? 0xa0ffffff : 0xa0000000, 28f, bitmap.getHeight() * 0.92f - 26f);

        return renderTextBlock(bitmap, tfStam, currentPasuk[1], bkgDark ? 0xffffffff : 0xff000000, 50f, bitmap.getHeight() * 0.08f + 13f);

    }

    //endregion

    private PointF getWidgetSizePrefs( int appWidgetId, boolean applyDimension) {
        float w = applyDimension ?
                mClock.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getSizePref("widgetWidth", appWidgetId)) :
                getSizePref("widgetWidth", appWidgetId);
        float h = applyDimension ?
                mClock.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getSizePref("widgetHeight", appWidgetId)) :
                getSizePref("widgetWidth", appWidgetId);
        return new PointF(w, h);
    }

    //region Deprecated Code
    /*

    private Calendar updateCalendar(Calendar c, long newday) {
        c.add(Calendar.MINUTE, (int) ((86400000 - newday % 86400000) / 60000));
        return c;
    }

        //updated to zcZmanim
    private Bitmap renderTextLine(Bitmap bitmap,
                                 Typeface typeface,
                                 String text, int color, float size, PointF position) {

        Canvas canvas = new Canvas(bitmap);

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
        p.setColor(color);
        p.setTypeface(typeface);
        p.setTextSize(size);
        canvas.drawText(text, position.x, position.y, p);
        return bitmap;
    }

        protected void updateClock() {
        clockUpdated = false;
    }

    public void updateLocation() {

        if (!zcProvider.checkLocationUpdate(mContext,false)) return;

        GeoLocation g = new zcLocation(mContext).geoLocation();

        if (debug) Log.e("zcProvider GPS:", String.format("loc:%s lat:%f long:%f alt:%f tz:%s",
                g.getLocationName(),
                g.getLatitude(),
                g.getLongitude(),
                g.getElevation(),
                g.getTimeZone().getDisplayName()
        ));

        alotHarHabait = new ComplexZmanimCalendar(HarHabait).getAlos72();

        zCalendar = new ComplexZmanimCalendar(g);
    }*/
    //endregion



}
