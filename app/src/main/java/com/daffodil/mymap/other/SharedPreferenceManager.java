package com.daffodil.mymap.other;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Manager (singleton) for SharedPreference operations
 */
public class SharedPreferenceManager {
    public static final String TIME_SPENT_ON_LEVEL = "time.spent.on.level";
    private static SharedPreferenceManager sInstance;

    /**
     * Used to make commits to the shared preferences file, don't use this directly, use the getter!!
     */
    private SharedPreferences mPreferencesManager;

    /**
     * Private Constructor
     */
    private SharedPreferenceManager() {

    }

    /**
     * Gets the instance(object) for this class
     *
     * @return The Instance of SharedManager singleton class
     */
    public static SharedPreferenceManager instance() {
        if (sInstance == null) {
            sInstance = new SharedPreferenceManager();
        }


        return sInstance;
    }

    public SharedPreferences getPreferencesManager() {

        if (mPreferencesManager == null) {
            mPreferencesManager = ApplicationContextProvider.getContext().getSharedPreferences("saveState", Context.MODE_PRIVATE);
        }

        return mPreferencesManager;
    }

    /**
     * Save the time spent on a level
     *
     * @param milliseconds time spent on a level
     */
    public void persistTimeSpentOnLevel(long milliseconds) {

        getPreferencesManager().edit().putLong(TIME_SPENT_ON_LEVEL, milliseconds).commit();

    }

    public void setValueToSharedPref(String key, String value) {
        getPreferencesManager().edit().putString(key, value).commit();

    }

    public String getValueFromShared_Pref(String key) {
        return getPreferencesManager().getString(key, "");
    }

    public void setLongValueToSharedPref(String key, long value) {
        getPreferencesManager().edit().putLong(key, value).commit();

    }

    public long getLongValueFromShared_Pref(String key) {
        return getPreferencesManager().getLong(key, 0);
    }


    /**
     * Returns the time spent on a level or 0 if the game is new
     *
     * @return milliseconds passed since the game started
     */
    public long getTimeSpentOnLevel() {

        return getPreferencesManager().getLong(TIME_SPENT_ON_LEVEL, 0);

    }
}