package pt.gu.zclock;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.ListPreference;
import android.util.AttributeSet;

/**
 * Created by GU on 18-12-2014.
 */
public class gPreferences {

    public class ListInteger extends ListPreference {
        public ListInteger(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ListInteger(Context context) {
            super(context);
        }

        @Override
        protected boolean persistString(String value) {
            if (value == null) {
                return false;
            } else {
                return persistInt(Integer.valueOf(value));
            }
        }

        @Override
        protected String getPersistedString(String defaultReturnValue) {
            if (getSharedPreferences().contains(getKey())) {
                int intValue = getPersistedInt(0);
                return String.valueOf(intValue);
            } else {
                return defaultReturnValue;
            }
        }
    }

    public class ListDimens extends ListPreference {
        public ListDimens(Context context, AttributeSet attrs) {
            super(context, attrs);
            setAttrs(attrs);
        }

        private void setAttrs(AttributeSet attrs) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ColorPreference);
            int res = a.getInt(R.styleable.ColorPreference_Res, 10);
        }

        public ListDimens(Context context) {
            super(context);
        }

        @Override
        protected boolean persistString(String value) {
            if (value == null) {
                return false;
            } else {
                return persistFloat(Float.valueOf(value));
            }
        }

        @Override
        protected String getPersistedString(String defaultReturnValue) {
            if (getSharedPreferences().contains(getKey())) {
                float fValue = getPersistedFloat(0);
                return String.valueOf(fValue);
            } else {
                return defaultReturnValue;
            }
        }
    }
}