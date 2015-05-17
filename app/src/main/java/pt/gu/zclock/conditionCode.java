package pt.gu.zclock;

/**
 * Created by GU on 22-04-2015.
 */
public enum conditionCode {
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

    conditionCode(final int code) {
        this.id = code;
    }

    public int index() {
        return this.ordinal();
    }

    conditionCode getEnum(int code){
        for (conditionCode c:conditionCode.values()){
            if (c.id == code) return c;
        }
        return null;
    }

    public float getColorS(){
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
            case SKY_IS_CLEAR: return 1f;
            case FEW_CLOUDS: return 0.95f;
            case SCATTERED_CLOUDS: return 0.92f;
            case BROKEN_CLOUDS: return 0.9f;
            case OVERCAST_CLOUDS: return 0.85f;
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
    };

    public float getColorV(){
        switch (this){
            case UNKNOWN: return 0;
        /* Thunderstorm */
            case THUNDERSTORM_WITH_LIGHT_RAIN: return 0.5f;
            case THUNDERSTORM_WITH_RAIN: return 0.4f;
            case THUNDERSTORM_WITH_HEAVY_RAIN: return 0.3f;
            case LIGHT_THUNDERSTORM: return 0.5f;
            case THUNDERSTORM: return 0.3f;
            case HEAVY_THUNDERSTORM: return 0.1f;
            case RAGGED_THUNDERSTORM: return 0.2f;
            case THUNDERSTORM_WITH_LIGHT_DRIZZLE: return 0.2f;
            case THUNDERSTORM_WITH_DRIZZLE: return 0.2f;
            case THUNDERSTORM_WITH_HEAVY_DRIZZLE: return 0.2f;
        /* Drizzle */
            case LIGHT_INTENSITY_DRIZZLE: return 0.7f;
            case DRIZZLE: return 0.8f;
            case HEAVY_INTENSITY_DRIZZLE: return 0.7f;
            case LIGHT_INTENSITY_DRIZZLE_RAIN: return 0.8f;
            case DRIZZLE_RAIN: return 0.6f;
            case HEAVY_INTENSITY_DRIZZLE_RAIN: return 0.8f;
            case SHOWER_DRIZZLE: return 0.6f;
        /* Rain */
            case LIGHT_RAIN: return 0.5f;
            case MODERATE_RAIN: return 0.4f;
            case HEAVY_INTENSITY_RAIN: return 0.2f;
            case VERY_HEAVY_RAIN: return 0.1f;
            case EXTREME_RAIN: return 0.1f;
            case FREEZING_RAIN: return 0.3f;
            case LIGHT_INTENSITY_SHOWER_RAIN: return 0.6f;
            case SHOWER_RAIN: return 0.3f;
            case HEAVY_INTENSITY_SHOWER_RAIN: return 0.2f;
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
            case SKY_IS_CLEAR: return 0.5f;
            case FEW_CLOUDS: return 0.5f;
            case SCATTERED_CLOUDS: return 0.5f;
            case BROKEN_CLOUDS: return 0.5f;
            case OVERCAST_CLOUDS: return 0.5f;
        /* Extreme */
            case TORNADO: return 0.1f;
            case TROPICAL_STORM: return 0.3f;
            case HURRICANE: return 0.1f;
            case COLD: return 0.5f;
            case HOT: return 0.5f;
            case WINDY: return 0.5f;
            case HAIL: return 0.9f;

            default: return 0.5f;
        }
    };
}
