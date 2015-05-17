package pt.gu.zclock;

import android.graphics.Color;

import static pt.gu.zclock.zcHelper.*;

/**
 * Created by GU on 22-04-2015.
 */
public class WeatherData{

    public long   dt;
    public double main_temp;
    public double main_temp_min;
    public double main_temp_max;
    public double main_pressure;
    public double main_sea_level;
    public double main_grnd_level;
    public int    main_humidity;
    public int    main_temp_kf;
    public int    weather_id;
    public int    clouds_all;
    public double rain_3h;
    public double wind_speed;
    public double wind_deg;
    public String sys_pod;

    public long getTime(){
        return dt*1000;
    }

    public void setTime(Long milis){
        this.dt = milis/1000;
    }

    public float getCelsius(){
        return (float)this.main_temp-273f;
    }

    public void setCelsius(float celsius){
        this.main_temp = (double)celsius+273;
    }

    public int getColorCondition(int alpha,float maxTempScale, float minTempScale, float startHue, float adjustsat){
        float[] hsv = {0,0.5f,0.5f};
        float t = (float)this.main_temp-273f;
        if (t>maxTempScale) t=maxTempScale;
        if (t<minTempScale)  t=minTempScale;
        hsv[0] = 360*startHue*(1-(t-minTempScale)/(maxTempScale-minTempScale));
        conditionCode c = getEnum(this.weather_id);
        hsv[1] = c.getColorS() * adjustsat;
        hsv[2] = c.getColorV();
        return Color.HSVToColor(alpha,hsv);
    }

    public int getColorCondition(float startHue){
        float[] hsv = {0,0.5f,0.5f};
        RangeF tRange = new RangeF(42f,-8f);
        float t = tRange.get(this.getCelsius());
        hsv[0]  = tRange.scale(t, new RangeF(0, 360 * startHue));
        conditionCode c = getEnum(this.weather_id);
        RangeF rRain = new RangeF(5,0);
        float rain = rRain.get((float)this.rain_3h);
        hsv[1]  = c.getColorS() - rRain.scale(rain,new RangeF(0.5f,0.1f));
        hsv[2]  = c.getColorS();
        Range rAlpha = new Range(100,0);
        int a = rAlpha.scale(this.clouds_all, new Range(192, 255));
        //int alpha = new zcUtils.Range(128,240).scale(this.clouds_all,new zcUtils.Range(100,0));
        return Color.HSVToColor(a,hsv);
    }

    public int getCloudnRainColor(){
        float[] hsv = {0,0.5f,0.5f};
        hsv[0] = 0;
        hsv[1] = 0;
        hsv[2] = 1;
        int alpha = (int)(255*(1f-(float)this.clouds_all/100f));
        return Color.HSVToColor(alpha,hsv);
    }

    private conditionCode getEnum(int code){
        for (conditionCode c:conditionCode.values()){
            if (c.id == code) return c;
        }
        return null;
    }
}
