package com.daffodil.mymap.other;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePreferanceWrapperSingleton {

    private static SharedPreferences pref;
    private Editor editor;

    private static SharePreferanceWrapperSingleton singleton;

    /*
     * A private Constructor prevents any other class from instantiating.
     */
    private SharePreferanceWrapperSingleton() {
    }

    /* Static 'instance' method */
    public static SharePreferanceWrapperSingleton getSingletonInstance() {

        if (null == singleton) {
            singleton = new SharePreferanceWrapperSingleton();
        }
        return singleton;
    }

    public int getValueFromSharedPref(String key) {

        return pref.getInt(key, 0);
    }

    public void setValueToSharedPref(String key, int value) {
        try {
            editor.putInt(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void setValueToSharedPref(String key, String value) {
        editor.putString(key, value);
        editor.commit();

    }


    public String getValueFromShared_Pref(String key) {
        String and = pref.getString(key, "");
        return pref.getString(key, "");
    }

    public void setValueToSharedPref(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();

    }

    public boolean getBoolValueFromSharedPref(String key) {

        return pref.getBoolean(key, false);
    }

    public void setPreferances(SharePreferanceWrapperSingleton objSPS,
                               int heightScreen, int widthScreen) {

        objSPS.setValueToSharedPref(Constants.SCREEN_WIDTH, widthScreen);
        objSPS.setValueToSharedPref(Constants.SCREEN_HEIGHT, heightScreen);

        objSPS.setValueToSharedPref(Constants.LOGO_HEIGHT,
                (int) (heightScreen * 0.08));
        objSPS.setValueToSharedPref(Constants.LOGO_WIDTH,
                (int) (widthScreen * 0.15));


        objSPS.setValueToSharedPref(Constants.LISTVIEW_HEIGHT,
                (int) (heightScreen * 0.76));
        objSPS.setValueToSharedPref(Constants.NEWS_ICON_HEIGHT,
                (int) (heightScreen * 0.15));
        objSPS.setValueToSharedPref(Constants.NEWS_ICON_WIDTH,
                (int) (widthScreen * 0.16));
        objSPS.setValueToSharedPref(Constants.ADD_IMAGE_HEIGHT,
                (int) (heightScreen * 0.07));
        objSPS.setValueToSharedPref(Constants.HEADER_HEIGHT,
                (int) (heightScreen * 0.2));
        objSPS.setValueToSharedPref(Constants.WEATHER_ICON_HEIGHT,
                (int) (widthScreen * 0.06));
        objSPS.setValueToSharedPref(Constants.WEATHER_ICON_WIDTH,
                (int) (widthScreen * 0.14));
        objSPS.setValueToSharedPref(Constants.ARROW_HEIGHT,
                (int) (heightScreen * 0.03));
        objSPS.setValueToSharedPref(Constants.ARROW_WIDTH,
                (int) (widthScreen * 0.04));
        objSPS.setValueToSharedPref(Constants.ARROW_ViewFlipper_HEIGHT,
                (int) (heightScreen * 0.07));
        objSPS.setValueToSharedPref(Constants.ARROW_ViewFlipper_WIDTH,
                (int) (widthScreen * 0.1));
        objSPS.setValueToSharedPref(Constants.TEXT_SIZE_LARGE,
                (int) (widthScreen * 0.055));
        objSPS.setValueToSharedPref(Constants.TEXT_SIZE_MEDIUM,
                (int) (widthScreen * 0.045));
        objSPS.setValueToSharedPref(Constants.TEXT_SIZE_SMALL,
                (int) (widthScreen * 0.04));
        objSPS.setValueToSharedPref(Constants.TEXT_SIZE_MINI,
                (int) (widthScreen * 0.035));
        objSPS.setValueToSharedPref(Constants.DIVIDER_HEIGHT,
                (int) (heightScreen * 0.004));
        objSPS.setValueToSharedPref(Constants.PADDING_XLARGE,
                (int) (heightScreen * 0.035));
        objSPS.setValueToSharedPref(Constants.PADDING_LARGE,
                (int) (heightScreen * 0.02));
        objSPS.setValueToSharedPref(Constants.PADDING_MEDIUM,
                (int) (heightScreen * 0.01));
        objSPS.setValueToSharedPref(Constants.PADDING_SMALL,
                (int) (heightScreen * 0.005));
        objSPS.setValueToSharedPref(Constants.PADDING_MINI,
                (int) (heightScreen * 0.03));
        objSPS.setValueToSharedPref(Constants.MARGIN_SMALL,
                (int) (heightScreen * 0.01));
        objSPS.setValueToSharedPref(Constants.MARGIN_MINI,
                (int) (heightScreen * 0.02));
        objSPS.setValueToSharedPref(Constants.MARGIN_MEDIUM,
                (int) (heightScreen * 0.04));
        objSPS.setValueToSharedPref(Constants.MARGIN_MEDIUM_CALL_BUTTON,
                (int) (heightScreen * 0.15));
        objSPS.setValueToSharedPref(Constants.LOGIN_BTN_HEIGHT, (int) (heightScreen * 0.06));
        objSPS.setValueToSharedPref("mLastStopTime", "0");
        objSPS.setValueToSharedPref("PunchInText", "Punch In             00:00:00");

        objSPS.setValueToSharedPref(Constants.DIALOG_HEIGHT,
                (int) (heightScreen * 0.48));
        objSPS.setValueToSharedPref(Constants.DIALOG_WIDTH,
                (int) (widthScreen * 0.75));
        objSPS.setValueToSharedPref(Constants.SEARCH_BAR_WIDTH,
                (int) (widthScreen * 0.65));

        objSPS.setValueToSharedPref(Constants.BTN_HEIGHT,
                (int) (heightScreen * 0.05));
        objSPS.setValueToSharedPref(Constants.BTN_WIDTH,
                (int) (widthScreen * 0.2));
        objSPS.setValueToSharedPref(Constants.SPINNER_HEIGHT,
                (int) (heightScreen * 0.06));
        objSPS.setValueToSharedPref(Constants.SPINNER_WIDTH,
                (int) (widthScreen * 0.4));
        objSPS.setValueToSharedPref(Constants.COMPANYNAME_WIDTH,
                (int) (widthScreen * 0.5));


        this.editor.commit();
    }

    @SuppressWarnings("static-access")
    public void setPref(Context context) {
        this.pref = context.getSharedPreferences("infibiz",
                context.MODE_PRIVATE);
    }

    public void setEditor() {
        this.editor = pref.edit();
        this.editor.commit();

    }

}
