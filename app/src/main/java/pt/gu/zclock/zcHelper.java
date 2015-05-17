package pt.gu.zclock;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Created by GU on 01-05-2015.
 */
public class zcHelper {

    private static String TAG = "zcHelper";
    private static final boolean debug = false;

    public static class Range{

        private String TAG = "Utils/Range";
        private int max;
        private int min;
        private boolean sign;

        public Range (int max,int min){
            this.max = max;
            this.min = min;
            this.sign = (max > min);
        }

        public int get (int value){
            if (value > (sign ? max:min)) return max;
            if (value < (sign ? min:max)) return min;
            return value;
        }

        public int scale (int value, Range newRange){
            return (int)this.scaleF(value, newRange);
        }

        public float scaleF (int value, Range newRange){
            float v = this.get(value);
            float s = this.getSize();
            float l = newRange.getSize();
            if (s == 0|| l == 0) return v;
            s = (v-this.min)/s;
            if (debug) Log.e(TAG, String.format("V%.2f S%.2f L%.2f", v, s, l));
            return newRange.min+s*l;
        }

        public int getSize(){
            return this.max-this.min;
        }

    }

    public static class RangeF{

        private String TAG = "Utils/RangeF";
        private float max;
        private float min;
        private boolean sign;

        public RangeF (float max,float min){
            this.max = max;
            this.min = min;
            this.sign = (this.max > this.min);
        }

        public float get (float value){
            if (value > (sign ? max:min)) return max;
            if (value < (sign ? min:max)) return min;
            return value;
        }
        
        public float scale (float value, RangeF newRange){
            float v = this.get(value);
            float s = this.getSize();
            float l = newRange.getSize();
            if (s == 0|| l == 0) return v;
            s = (v-this.min)/s;
            if (debug) Log.e(TAG,String.format("V%.2f S%.2f L%.2f",v,s,l));
            return newRange.min+s*l;
        }
        
        public float getSize(){
            return this.max-this.min;
        }
    }

    public enum owmCode {
        UNKNOWN(Integer.MIN_VALUE),
        /* Thunderstorm */
        THUNDERSTORM_WITH_LIGHT_RAIN(200),
        THUNDERSTORM_WITH_RAIN(201),
        THUNDERSTORM_WITH_HEAVY_RAIN(202),
        LIGHT_THUNDERSTORM(210),
        THUNDERSTORM(211),
        HEAVY_THUNDERSTORM(212),
        RAGGED_THUNDERSTORM(221),
        THUNDERSTORM_WITH_LIGHT_DRIZZLE(230),
        THUNDERSTORM_WITH_DRIZZLE(231),
        THUNDERSTORM_WITH_HEAVY_DRIZZLE(232),
        /* Drizzle */
        LIGHT_INTENSITY_DRIZZLE(300),
        DRIZZLE(301),
        HEAVY_INTENSITY_DRIZZLE(302),
        LIGHT_INTENSITY_DRIZZLE_RAIN(310),
        DRIZZLE_RAIN(311),
        HEAVY_INTENSITY_DRIZZLE_RAIN(312),
        SHOWER_DRIZZLE(321),
        /* Rain */
        LIGHT_RAIN(500),
        MODERATE_RAIN(501),
        HEAVY_INTENSITY_RAIN(502),
        VERY_HEAVY_RAIN(503),
        EXTREME_RAIN(504),
        FREEZING_RAIN(511),
        LIGHT_INTENSITY_SHOWER_RAIN(520),
        SHOWER_RAIN(521),
        HEAVY_INTENSITY_SHOWER_RAIN(522),
        /* Snow */
        LIGHT_SNOW(600),
        SNOW(601),
        HEAVY_SNOW(602),
        SLEET(611),
        SHOWER_SNOW(621),
        /* Atmosphere */
        MIST(701),
        SMOKE(711),
        HAZE(721),
        SAND_OR_DUST_WHIRLS(731),
        FOG(741),
        /* Clouds */
        SKY_IS_CLEAR(800),
        FEW_CLOUDS(801),
        SCATTERED_CLOUDS(802),
        BROKEN_CLOUDS(803),
        OVERCAST_CLOUDS(804),
        /* Extreme */
        TORNADO(900),
        TROPICAL_STORM(901),
        HURRICANE(902),
        COLD(903),
        HOT(904),
        WINDY(905),
        HAIL(906);

        public final int id;

        owmCode(final int code) {
            this.id = code;
        }

        public int index() {
            return this.ordinal();
        }

        owmCode getEnum(int code){
            for (owmCode c:owmCode.values()){
                if (c.id == code) return c;
            }
            return null;
        }

        public float getColorS() {
                switch (this) {
                    case UNKNOWN:
                        return 0;
        /* Thunderstorm */
                    case THUNDERSTORM_WITH_LIGHT_RAIN:
                        return 0.5f;
                    case THUNDERSTORM_WITH_RAIN:
                        return 0.4f;
                    case THUNDERSTORM_WITH_HEAVY_RAIN:
                        return 0.3f;
                    case LIGHT_THUNDERSTORM:
                        return 0.5f;
                    case THUNDERSTORM:
                        return 0.3f;
                    case HEAVY_THUNDERSTORM:
                        return 0.2f;
                    case RAGGED_THUNDERSTORM:
                        return 0.4f;
                    case THUNDERSTORM_WITH_LIGHT_DRIZZLE:
                        return 0.5f;
                    case THUNDERSTORM_WITH_DRIZZLE:
                        return 0.45f;
                    case THUNDERSTORM_WITH_HEAVY_DRIZZLE:
                        return 0.3f;
        /* Drizzle */
                    case LIGHT_INTENSITY_DRIZZLE:
                        return 0.3f;
                    case DRIZZLE:
                        return 0.3f;
                    case HEAVY_INTENSITY_DRIZZLE:
                        return 0.3f;
                    case LIGHT_INTENSITY_DRIZZLE_RAIN:
                        return 0.3f;
                    case DRIZZLE_RAIN:
                        return 0.3f;
                    case HEAVY_INTENSITY_DRIZZLE_RAIN:
                        return 0.3f;
                    case SHOWER_DRIZZLE:
                        return 0.2f;
        /* Rain */
                    case LIGHT_RAIN:
                        return 0.4f;
                    case MODERATE_RAIN:
                        return 0.3f;
                    case HEAVY_INTENSITY_RAIN:
                        return 0.2f;
                    case VERY_HEAVY_RAIN:
                        return 0.1f;
                    case EXTREME_RAIN:
                        return 0.1f;
                    case FREEZING_RAIN:
                        return 0.3f;
                    case LIGHT_INTENSITY_SHOWER_RAIN:
                        return 0.4f;
                    case SHOWER_RAIN:
                        return 0.3f;
                    case HEAVY_INTENSITY_SHOWER_RAIN:
                        return 0.3f;
        /* Snow */
                    case LIGHT_SNOW:
                        return 0.3f;
                    case SNOW:
                        return 0.5f;
                    case HEAVY_SNOW:
                        return 0.2f;
                    case SLEET:
                        return 0.6f;
                    case SHOWER_SNOW:
                        return 0.4f;
        /* Atmosphere */
                    case MIST:
                        return 0.7f;
                    case SMOKE:
                        return 0.4f;
                    case HAZE:
                        return 0.2f;
                    case SAND_OR_DUST_WHIRLS:
                        return 0.7f;
                    case FOG:
                        return 0.2f;
        /* Clouds */
                    case SKY_IS_CLEAR:
                        return 0.9f;
                    case FEW_CLOUDS:
                        return 0.85f;
                    case SCATTERED_CLOUDS:
                        return 0.82f;
                    case BROKEN_CLOUDS:
                        return 0.8f;
                    case OVERCAST_CLOUDS:
                        return 0.75f;
        /* Extreme */
                    case TORNADO:
                        return 0.7f;
                    case TROPICAL_STORM:
                        return 0.7f;
                    case HURRICANE:
                        return 0.7f;
                    case COLD:
                        return 1;
                    case HOT:
                        return 1;
                    case WINDY:
                        return 1;
                    case HAIL:
                        return 0;

                    default:
                        return 0;
                }
        }

        public float getColorV(){
            switch (this){
                case UNKNOWN: return 0;
        /* Thunderstorm */
                case THUNDERSTORM_WITH_LIGHT_RAIN: return 0.5f;
                case THUNDERSTORM_WITH_RAIN: return 0.4f;
                case THUNDERSTORM_WITH_HEAVY_RAIN: return 0.3f;
                case LIGHT_THUNDERSTORM: return 0.5f;
                case THUNDERSTORM: return 0.3f;
                case HEAVY_THUNDERSTORM: return 0.2f;
                case RAGGED_THUNDERSTORM: return 0.4f;
                case THUNDERSTORM_WITH_LIGHT_DRIZZLE: return 0.5f;
                case THUNDERSTORM_WITH_DRIZZLE: return 0.45f;
                case THUNDERSTORM_WITH_HEAVY_DRIZZLE: return 0.3f;
        /* Drizzle */
                case LIGHT_INTENSITY_DRIZZLE: return 0.3f;
                case DRIZZLE: return 0.3f;
                case HEAVY_INTENSITY_DRIZZLE: return 0.3f;
                case LIGHT_INTENSITY_DRIZZLE_RAIN: return 0.3f;
                case DRIZZLE_RAIN: return 0.3f;
                case HEAVY_INTENSITY_DRIZZLE_RAIN: return 0.3f;
                case SHOWER_DRIZZLE: return 0.2f;
        /* Rain */
                case LIGHT_RAIN: return 0.4f;
                case MODERATE_RAIN: return 0.3f;
                case HEAVY_INTENSITY_RAIN: return 0.2f;
                case VERY_HEAVY_RAIN: return 0.1f;
                case EXTREME_RAIN: return 0.1f;
                case FREEZING_RAIN: return 0.3f;
                case LIGHT_INTENSITY_SHOWER_RAIN: return 0.4f;
                case SHOWER_RAIN: return 0.3f;
                case HEAVY_INTENSITY_SHOWER_RAIN: return 0.3f;
        /* Snow */
                case LIGHT_SNOW: return 0.3f;
                case SNOW: return 0.5f;
                case HEAVY_SNOW: return 0.2f;
                case SLEET: return 0.6f;
                case SHOWER_SNOW: return 0.4f;
        /* Atmosphere */
                case MIST: return 0.7f;
                case SMOKE: return 0.4f;
                case HAZE: return 0.2f;
                case SAND_OR_DUST_WHIRLS: return 0.7f;
                case FOG: return 0.2f;
        /* Clouds */
                case SKY_IS_CLEAR: return 0.95f;
                case FEW_CLOUDS: return 0.85f;
                case SCATTERED_CLOUDS: return 0.8f;
                case BROKEN_CLOUDS: return 0.72f;
                case OVERCAST_CLOUDS: return 0.65f;
        /* Extreme */
                case TORNADO: return 0.7f;
                case TROPICAL_STORM: return 0.7f;
                case HURRICANE: return 0.7f;
                case COLD: return 1;
                case HOT: return 1;
                case WINDY: return 1;
                case HAIL: return 0;

                default: return 0;
            }
        }
    }
    
    public static class xColor extends Color {

        public static int setAlpha(int alpha, int color){
            return ( alpha << 24 ) | ( color & 0x00ffffff);
        }

        public static int copyAlpha(int source, int dest){
            int alpha = Color.alpha(source);
            return setAlpha(alpha,dest);
        }

        public static int setHue(float hue, int color){
            float hsv[] = new float[3];
            int a = Color.alpha(color);
            Color.colorToHSV(color,hsv);
            hsv[0] = hue;
            return Color.HSVToColor(a,hsv);
        }

        public static float getHue(int color){
            float hsv[] = new float[3];
            Color.colorToHSV(color,hsv);
            return hsv[0];
        }

        public static int copyHue(int source, int dest) {
            float h = getHue(dest);
            return setHue(h,source);
        }

        public static float[] ColorToAHSL(int color){
            float hsv[] = new float[3];
            Color.colorToHSV(color,hsv);
            float hsl[] = HSVtoHSL(hsv);
            return new float[]{Color.alpha(color),hsl[0],hsl[1],hsl[2]};
        }

        public static int AHSLToColor(float[] ahsl){
            float ahsv[] = AHSLtoAHSV(ahsl);
            return Color.HSVToColor((int)ahsv[0],new float[]{ahsv[1],ahsv[2],ahsv[3]});
        }

        public static float[] AHSVtoAHSL(float[] ahsv){
            float ahsl[] = new float[4];
            ahsl[0] = ahsv[0];
            ahsl[1] = ahsv[1];
            ahsl[2] = ahsv[2] * ahsv[3];
            ahsl[3] = (2-ahsv[1])*ahsv[3];
            ahsl[2] /= (ahsl[3]<=1) ? ahsl[3] : 2-ahsl[3];
            ahsl[3] /= 2;
            return ahsl;
        }

        public static float[] HSVtoHSL(float[] hsv){
            float hsl[] = new float[3];
            hsl[0] = hsv[0];
            hsl[1] = hsv[1] * hsv[2];
            hsl[2] = (2-hsv[0])*hsv[2];
            hsl[1] /= (hsl[2]<=1) ? hsl[2] : 2-hsl[2];
            hsl[2] /= 2;
            return hsl;
        }

        public static float[] AHSLtoAHSV(float[] ahsl){
            float ahsv[] = new float[4];
            ahsv[1] = ahsl[1];
            ahsl[3] *= 2;
            ahsl[2] *= (ahsl[3]<=1) ? ahsl[3] : 2-ahsl[3];
            ahsv[3] = (ahsl[2]+ahsl[3])/2;
            ahsv[2] = 2*ahsl[2] / (ahsl[3]+ahsl[2]);
            return ahsv;
        }

        public static float[] HSLtoHSV(float[] hsl){
            float hsv[] = new float[3];
            hsv[0] = hsl[0];
            hsl[2] *= 2;
            hsl[1] *= (hsl[2]<=1) ? hsl[2] : 2-hsl[2];
            hsv[2] = (hsl[1]+hsl[2])/2;
            hsv[1] = 2*hsl[1] / (hsl[2]+hsl[1]);
            return hsv;
        }
    }

    public static class WeatherData{

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
            try{
                owmCode c = getEnum(this.weather_id);
                hsv[1] = c.getColorS() * adjustsat;
                hsv[2] = c.getColorV();
            } catch (Exception ignore){
                if (debug) Log.e(TAG+"/getColorCondition:",ignore.toString());
            }
            return Color.HSVToColor(alpha,hsv);
        }

        public int getColorCondition(float startHue){
            float[] hsv = {0,0.5f,0.5f};
            RangeF tRange = new RangeF(50f,-20f);
            float t = tRange.get(this.getCelsius());
            hsv[0]  = tRange.scale(t,new RangeF(0,360*startHue));
            owmCode c = getEnum(this.weather_id);
            hsv[1]  = c.getColorS();
            hsv[2]  = c.getColorV();
            int alpha = new Range(90,216).scale(this.clouds_all,new Range(100,0));
            return Color.HSVToColor(alpha,hsv);
        }

        public int getCloudnRainColor(){
            float[] hsv = {0,0.5f,0.5f};
            hsv[0] = 0;
            hsv[1] = 0;
            hsv[2] = 1;
            int alpha = (int)(255*(1f-(float)this.clouds_all/100f));
            return Color.HSVToColor(alpha,hsv);
        }

        private owmCode getEnum(int code){
            for (owmCode c:owmCode.values()){
                if (c.id == code) return c;
            }
            return null;
        }
    }

    public static class clockTimeLabel {

        private Date[] date;
        private float size;
        private int color;
        private Typeface type;
        private boolean alignRight;
        private String format;
        private boolean insideFrame = false;

        public clockTimeLabel(Date[] date, float size, int color, Typeface t, String format, boolean insideFrame) {
            this.date = date;
            this.size = size;
            this.color = color;
            this.type = t;
            this.format = format;
            this.insideFrame = insideFrame;
        }

        public int lenght(){
            return this.date.length;
        }

        public void setPaint(Paint p){
            p.setColor(this.color);
            p.setTextSize(this.getSizePx());
            p.setTypeface(this.type);
            p.setTextAlign((this.alignRight) ? Paint.Align.RIGHT : Paint.Align.LEFT);
        }


        public Path getPath(int index, PointF centro, float pad, float raio, float angle_offset,float markWidth){
            float r1, r2;
            if (this.insideFrame) {
                r2 = raio - pad;
                r1 = r2 - markWidth;
            } else {
                r1 = raio + pad;
                r2 = r1 + markWidth;
            }
            Path p = new Path();
            double angle = (this.dateToAngle(index) + angle_offset) * Math.PI / 180;
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);
            if (cos < 0) {
                p.moveTo((float) (r2 * cos) + centro.x, (float) (r2 * sin) + centro.y);
                p.lineTo((float) (r1 * cos) + centro.x, (float) (r1 * sin) + centro.y);
                this.alignRight = !this.insideFrame;
            } else {
                p.moveTo((float) (r1 * cos) + centro.x, (float) (r1 * sin) + centro.y);
                p.lineTo((float) (r2 * cos) + centro.x, (float) (r2 * sin) + centro.y);
                this.alignRight = this.insideFrame;
            }
            return p;
        }

        public float getSizePx(){
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, this.size, Resources.getSystem().getDisplayMetrics());
        }

        public boolean isInsideFrame(){ return insideFrame;}

        public boolean isAlignRight() { return alignRight;}

        public void    setAlignRight(boolean alignRight){ this.alignRight = alignRight;}

        public String getString(int index) {
            if (index>this.date.length-1) return null;
            SimpleDateFormat s = new SimpleDateFormat(this.format);
            return s.format(this.date[index]);
        }

        public String getSample() {
            SimpleDateFormat s = new SimpleDateFormat(this.format);
            return s.format(new Date(0,0,0,23,59,59));
        }

        public float dateToAngle(int index) {
            if (index>this.date.length-1) return 0;
            float f = (this.date[index].getTime() / 60000f) % 1440;
            return f * 360f / 1440f;
        }
    }

    public static class dateLabel {

        private String label;
        private float size;
        private int color;
        private Typeface type;
        private float pad;

        public dateLabel(String text, float size, int color, Typeface t, float pad) {
            this.label = text;
            this.size = size;
            this.color = color;
            this.type = t;
            this.pad = pad;
        }

        public void setPaint(Paint p){
            p.setTypeface(this.type);
            p.setColor(this.color);
            p.setTextSize(this.getSizePx());
        }

        public void setPaint(Paint p,float line, float factor){
            p.setTypeface(this.type);
            p.setColor(this.color);
            p.setTextSize(this.getSizePx());
            Rect bounds = new Rect();
            p.getTextBounds(this.label, 0, this.label.length(), bounds);
            line += factor * bounds.height();
        }

        public float getSizePx(){
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, this.size, Resources.getSystem().getDisplayMetrics());
        }

        public String getLabel(){
            return label;
        }
    }

    public static class hebString {

        //region class enumerations

        public enum chilufi {
            Albam,      //
            Atbash,     //
            Achbi,      //
            AyiqBekher, //
            AchasBeta,  //
            Atbach      //
        }

        public enum Mispar {

            //http://www.jewishencyclopedia.com/articles/6571-gematria

            Hechrachi,      //1. Normal: m' mechrach, mispar hechrachi

            MeugalKlali,    //2. Ciclico ou menor: m qatan, mispar me'ugal klali, hagilgul chezrat

            Qidmi,          //3. Inclusivo: mispar qdimi (letra valor triangular)

            Musafi,          //4. Aditorio: mispar musafi quando no. externo de palavras ou letras adicionado

            MereviaKlali,   //5. Quadratico da palavra: mispar merevia klali: valor da palavra * valor de cada letra = quadrado da palavra

            MereviaPerati,  //6. Quadratico da letra: mispar merevia perati: soma dos quadrados da letras

            Shemi72,        //7. Nominal: mispar shemi: valor do nome da letra
            Shemi63,
            Shemi52,
            Shemi45,

            Mispari,        //8. Numeral: mispar mispari: valor do nome do numero da letra

            MispariHagadol, //9. Numeral Maior: mispari hagadol: numeral com integracao: (yod) = yod = esrim;

            Chitzuni,       //10. Externo: mispar chitzuni: (contagem de letras) todas as letras valem 1 nao aplicado a YHVH (Asis Rimonim 36b)

            Gadol,          //11. Maior: mispar gadol: contagem das formas finais 500-900 (mispar gadol mntzpkh)

            Kaful,          //12. Multiplo: mispar kaful: (cf.III.D.c) multiplicacoo das letras

            Chalqi,         //13. Quociente: mispar chalqui: (cf. III.D.d)

            MeaqavKlali,    //14. Cubico da palavra: m' meshalosh klali, mispar me'akav klali: valor cubico da palavra

            MeaqavPerati,   //15. Cubico da letra: mispar me'akav perati: cubo da letra normal (cf. Chayyath, Minchat Yehudi)

            EserKlalot,     //16. Involucao primeira decada: eser mispar klalot: cf.III.D.a

            HaklalotKlalot, //17. Involucao segunda decada: haklalot mispar klalot:

            ShemiSheni,     //18. Dupla integracao: mispar shemi sheni

            Temuri,         //19. Permutacao: mispar temuri (II.2.c), quando o valor das letras permutadas

            Revua           //20-22. Quaternionico: mispar revua: da palavra (20), integrada (21) e integracao dupla (22)
        }

        public enum gematriaTable {
            MisparHechrachi,  //0-Standard
            MisparGadol,      //1-Sofit
            Milui72,          //2
            Milui63,          //3
            Milui45,          //4
            Milui52,          //5
            MisparSiduri,     //6-Contagem ordinal 1>22
            MisparKatan,      //7-sem zeros iod=1, kaf=2,...
            MisparKidmi,      //8-Standard triangular 1,3,6...(n^2+n)/2
            MisparPerati,     //9-Standard quadratico 1,4,9...n^2
            MisparNeelam      //10
        }

        public enum gematriaMethod {
            Sum,    //0,
            Mul,    //1,
            Div,    //2,
            Sub     //3;
        }

        //endregion

        public final int hebKEEPSPACE= 1;
        public final int hebOTIOT    = 2;
        public final int hebNIQUDIM  = 4;
        public final int hebTAAMIM   = 8;
        public final int hebHEBPUNCT = 16;
        public final int hebPUNCT    = 32;

        private String hebstring;
        private String hebniqqud;
        private String otsequence;

        public hebString(String string){
            this.hebstring  = string;
            this.otsequence = getOtsequence();
        }

        @Override
        public String toString(){
            return hebstring;
        }

        public String toString(int flags){

            String res = "";
            for (char c : hebstring.toCharArray()) {
                if (
                        ((flags & hebKEEPSPACE) == hebKEEPSPACE && c == 0x0020) ||
                                ((flags & hebOTIOT) == hebOTIOT && c > 0x05ef && c < 0x05eb) ||
                                ((flags & hebNIQUDIM) == hebNIQUDIM && c > 0x05af && c < 0x05c8 && c != 0x05be && c != 0x05c0 && c != 0x05c3 && c != 0x05c6) ||
                                ((flags & hebTAAMIM) == hebTAAMIM && c > 0x0590 && c < 0x05af) ||
                                ((flags & hebKEEPSPACE) == hebKEEPSPACE && c == 0x0020) ||
                                ((flags & hebHEBPUNCT) == hebHEBPUNCT && (c == 0x05be || c == 0x05c0 || c == 0x05c3 || c == 0x05c6 || c == 0x05f3 || c == 0x05f4)) ||
                                ((flags & hebPUNCT) == hebPUNCT && (c > 0x0020 && c < 0x0040))
                        )
                    res += c;
            }
            return res;
        }

        private String getOtsequence(){
            String res = "";
            for (char c : hebstring.toCharArray()) {
                if (c > 0x05ef && c < 0x05eb) res += c;
            }
            return res;
        }

        private int[] getFactorArray(){
            int i = otsequence.length();
            Vector<Integer> fa = new Vector<>();
            for (int j=i;j>0;j--){
                //TODO
            }
            return new int[27];
        }

        public class Gematria{
            //TODO
            private int[] gematriaTable;

            private Mispar _msp;

            private double _gematria;

            private String _word;

            public Gematria(Mispar mispar, String word){
                _msp = mispar;
                setMispar();
                this._word = word;
            }

            private int[] setMispar(){
                switch (_msp){

                    case Hechrachi:
                        //1. Normal: m' mechrach, mispar hechrachi
                    case Musafi:
                        //4. Aditorio: mispar musafi quando no externo de palavras ou letras adicionado
                        return new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 20, 30, 40, 40, 50, 50, 60, 70, 80, 80, 90, 90, 100, 200, 300, 400 };

                    case MeugalKlali:
                        //2. Ciclico ou valor menor: m qatan, mispar me'ugal klali, hagilgul chezrat
                        return new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 2, 3, 4, 4, 5, 5, 6, 7, 8, 8, 9, 9, 1, 2, 3, 4 };

                    case Qidmi:
                        //3. Inclusivo: mispar qdimi (letra valor triangular)
                        return new int[] { 1,3,6,10,15,21,28,36,45,55,210,210,465,820,820,1275,1275,1830,2485,3240,3240,4095,4095,5050,20100,45150,80200};

                    case MereviaKlali:
                        //5. Quadratico da palavra: mispar merevia klali: valor da palavra * valor de cada letra = quadrado da palavra

                    case MereviaPerati:
                        //6. Quadratico da letra: mispar merevia perati: soma dos quadrados da letras

                    case Shemi72:
                        //7. Nominal: mispar shemi: valor do nome da letra
                        gematriaTable = new int[] { 111, 412, 83, 434, 10, 12, 67, 418, 419, 20, 100, 100, 74, 80, 80, 106, 106, 120, 130, 81, 81, 104, 104, 186, 510, 360, 406 };
                        break;

                    case Shemi63:
                        gematriaTable = new int[] { 111, 412, 83, 434, 15, 13, 67, 418, 419, 20, 100, 100, 74, 80, 80, 106, 106, 120, 130, 81, 81, 104, 104, 186, 510, 360, 406 };
                        break;

                    case Shemi52:
                        gematriaTable = new int[] { 111, 412, 83, 434, 10, 12, 67, 418, 419, 20, 100, 100, 74, 80, 80, 106, 106, 120, 130, 81, 81, 104, 104, 186, 510, 360, 406 };
                        break;

                    case Shemi45:
                        gematriaTable = new int[] { 111, 412, 83, 434, 6, 13, 67, 418, 419, 20, 100, 100, 74, 80, 80, 106, 106, 120, 130, 81, 81, 104, 104, 186, 510, 360, 406 };
                        break;

                    case Mispari:
                        //8. Numeral: mispar mispari: valor do nome do numero da letra

                    case MispariHagadol: //9. Numeral Maior: mispari hagadol: numeral com integracao: (yod) = yod = esrim;


                    case Chitzuni:       //10. Externo: mispar chitzuni: (contagem de letras) todas as letras valem 1 nao aplicado a YHVH (Asis Rimonim 36b)

                    case Gadol:
                        //11. Maior: mispar gadol: contagem das formas finais 500-900 (mispar gadol mntzpkh)
                        return new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 500, 20, 30, 600, 40, 700, 50, 60, 70, 800, 80, 900, 90, 100, 200, 300, 400 };

                    case Kaful:          //12. Multiplo: mispar kaful: (cf.III.D.c) multiplicacao das letras

                    case Chalqi:         //13. Quociente: mispar chalqui: (cf. III.D.d)

                    case MeaqavKlali:    //14. Cubico da palavra: m' meshalosh klali, mispar me'akav klali: valor cubico da palavra

                    case MeaqavPerati:   //15. Cubico da letra: mispar me'akav perati: cubo da letra normal (cf. Chayyath, Minchat Yehudi)

                    case EserKlalot:     //16. Involucao primeira decada: eser mispar klalot: cf.III.D.a

                    case HaklalotKlalot: //17. Involucao segunda decada: haklalot mispar klalot:

                    case ShemiSheni:     //18. Dupla integracao: mispar shemi sheni

                    case Temuri:         //19. Permutacao: mispar temuri (II.2.c), quando o valor das letras permutadas

                    case Revua:          //20-22. Quaternionico: mispar revua: da palavra (20), integrada (21) e integracao dupla (22)



                        //case Siduri: gematriaTable = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 23, 11, 12, 24, 13, 25, 14, 15, 16, 26, 17, 18, 16, 19, 20, 21, 22 }; break;    //6-Contagem ordinal 1>22
                        //case Perati: gematriaTable = new int[] {}; break;    //9-Standard quadratico 1,4,9...n^2
                        //case Neelam: gematriaTable = new int[] {}; break;   //10
                }
                return null;
            }

            public void setMisparMethod(Mispar value) {
                _msp = value;
                int[] tbl;
                switch (_msp) {
                    case Hechrachi:
                        tbl = new int[]{
                                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 20, 30, 40, 40, 50, 50, 60, 70, 80, 80, 90, 90, 100, 200, 300, 400
                        };
                        _gematria = GetGematria(tbl);
                        break;
                    case MeugalKlali:
                        tbl = new int[] {
                                1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 5, 2, 3, 6, 4, 7, 5, 6, 7, 8, 8, 9, 9, 1, 2, 3, 4
                        } ;
                        _gematria = GetGematria(tbl);
                        break;
                    case Qidmi:
                        tbl = new int[] {
                                1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 210, 210, 465, 820, 820, 1275, 1275, 1830, 2485, 3240, 3240, 4095, 4095, 5050, 20100, 45150, 80200
                        } ;
                        _gematria = GetGematria(tbl);
                        break;
                    case Musafi:
                        tbl = new int[] {
                                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 20, 30, 40, 40, 50, 50, 60, 70, 80, 80, 90, 90, 100, 200, 300, 400
                        } ;
                        _gematria = GetGematria(tbl);
                        _gematria += OtiotCount();
                        break;
                    case MereviaKlali:
                        tbl = new int[] {
                                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 20, 30, 40, 40, 50, 50, 60, 70, 80, 80, 90, 90, 100, 200, 300, 400
                        } ;
                        _gematria = GetGematria(tbl);
                        _gematria *= _gematria;
                        break;
                    case MereviaPerati:
                        tbl = new int[] {
                                1, 4, 9, 16, 25, 36, 49, 64, 81, 100, 400, 400, 900, 1600, 1600, 2500, 2500, 3600, 4900, 6400, 6400, 8100, 8100, 10000, 40000, 90000, 160000
                        } ;
                        _gematria = GetGematria(tbl);
                        break;
                    case Shemi72:
                        tbl = new int[] {
                                111, 412, 83, 434, 10, 12, 67, 418, 419, 20, 100, 100, 74, 80, 80, 106, 106, 120, 130, 81, 81, 104, 104, 186, 510, 360, 406
                        } ;
                        _gematria = GetGematria(tbl);
                        break;
                    case Shemi63:
                        tbl = new int[] {
                                111, 412, 83, 434, 15, 13, 67, 418, 419, 20, 100, 100, 74, 80, 80, 106, 106, 120, 130, 81, 81, 104, 104, 186, 510, 360, 406
                        } ;
                        _gematria = GetGematria(tbl);
                        break;
                    case Shemi45:
                        tbl = new int[] {
                                111, 412, 83, 434, 6, 13, 67, 418, 419, 20, 100, 100, 74, 80, 80, 106, 106, 120, 130, 81, 81, 104, 104, 186, 510, 360, 406
                        } ;
                        _gematria = GetGematria(tbl);
                        break;
                    case Shemi52:
                        tbl = new int[] {
                                111, 412, 83, 434, 10, 12, 67, 418, 419, 20, 100, 100, 74, 80, 80, 106, 106, 120, 130, 81, 81, 104, 104, 186, 510, 360, 406
                        } ;
                        _gematria = GetGematria(tbl);
                        break;
                }
            }

            private int GetGematria(int[]tbl){
                return Get(tbl);
            }

            private int Get(int[] table) {
                int g = 0;
                for (char c : _word.toCharArray()) {
                    int i = c - 0x05d0;
                    if (i >= 0 && i < 27) g += (char) (table[i] + 0x05cf);
                }
                return g;
            }

            private int OtiotCount() {
                int g = 0;
                for (char c : _word.toCharArray()) {
                    int i = c - 0x05d0;
                    g += (i >= 0 && i < 27) ? 1 : 0;
                }
                return g;
            }

            private int MilimCount() {
                if (_word == null) return 0;
                //String[] sep = {" ", "-", "?"};
                String[] s = _word.split(" |-|/?");
                return s.length;
            }
        }

    }
}
