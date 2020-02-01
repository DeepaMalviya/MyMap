package com.daffodil.mymap.other;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;


import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.core.app.ActivityCompat;

/**
 * Created by Daffodil on 12/19/2019.
 */

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmManagerBroadcastRe";
    final public static String ONE_TIME = "onetime";
    GPSTracker gps;
    private int battryPer;
    private Context context;
    private SharedPreferences pref, spf_user_id;
    private String userid, parentid;
    SharedPreferences.Editor editor1;
    private String level, date, time;
    private TrackUser trackUser;
    private DBAdapter dbAdapter;
    private String shutdownTime = "", restartTime = "", signalStrenght = "", networkSubType = "", sentStatus = "", cellIdShort = "", cellIdLong = "", lac = "", mnc = "", mcc = "", device_type = "", basestation_id = "", basestation_latitude = "", basestation_longitude = "";
    private SharePreferanceWrapperSingleton objSPS;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            // Toast.makeText(context, "AlarmManagerBroadcastReceiver called!", Toast.LENGTH_LONG).show();
            this.context = context;
            objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
            objSPS.setPref(context);
            dbAdapter = new DBAdapter(context);
            ConnectionDetector cd = new ConnectionDetector(context);
            boolean isInternetPresent = cd.isConnectingToInternet();


            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
            //Acquire the lock
            wl.acquire();

            IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batt = context.getApplicationContext().registerReceiver(null, filter);
            // int level = 0; // Default to some unknown/wild value
// registerReceiver method call could return null, so check that!
            if (batt != null) {
                level = batt.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) + "";
            }


            //You can do the processing here.
            Bundle extras = intent.getExtras();
            StringBuilder msgStr = new StringBuilder();

            if (extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)) {
                //Make sure this intent has been sent by the one-time timer button.
                msgStr.append("One time Timer : ");
            }
            // Format formatter = new SimpleDateFormat("hh:mm:ss a");
            Format formatter = new SimpleDateFormat("yyyy-MM-dd");
            this.date = formatter.format(new Date());

            //msgStr.append(formatter.format(new Date()));
            formatter = new SimpleDateFormat("HH:mm:ss");
            this.time = formatter.format(new Date());

            shutdownTime = objSPS.getValueFromShared_Pref("ShutdownTime");

            //restartTime = objSPS.getValueFromShared_Pref("RestartTime");
            restartTime = SharedPreferenceManager.instance().getValueFromShared_Pref("RestartTime");
            System.out.println("shutdownTime : " + shutdownTime + "    restartTime: " + restartTime);
            try {
                signalStrenght = ConnectionDetector.getSignalStrenght(context);
            } catch (Exception e) {
                e.printStackTrace();
                fillDataInDatabase(context);


            }
            try {
                networkSubType = ConnectionDetector.getNetworkSubType(context);
            } catch (Exception e) {
                e.printStackTrace();
                fillDataInDatabase(context);

            }


            try {
                String profileType = getProfileType();
                boolean isAirplaneMode = isAirplaneMode();
                String operatorType = getOperatorType();
                objSPS.setValueToSharedPref("profileType", profileType);
                objSPS.setValueToSharedPref("isAirplaneMode", isAirplaneMode + "");
                objSPS.setValueToSharedPref("operatorType", operatorType);
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                CellLocation cellLoc = telephonyManager.getCellLocation();
                if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {

                    device_type = "GSM";
                    String networkOperator = telephonyManager.getNetworkOperator();
                    if (!networkOperator.equals("")) {
                        mcc = networkOperator.substring(0, 3);
                        mnc = networkOperator.substring(3);
                    }

                    CellLocation.requestLocationUpdate();
                    if (cellLoc instanceof GsmCellLocation) {
                        GsmCellLocation cellLocation = (GsmCellLocation) cellLoc;
                        cellIdLong = cellLocation.getCid() + "";
                        short cIdShort = 0;
                        if (!cellIdLong.equals("")) {
                            cIdShort = (short) Integer.parseInt(cellIdLong);
                        }
                        cellIdShort = cIdShort + "";

                        lac = cellLocation.getLac() + "";
                        // do work
                    }


                } else if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
                    device_type = "CDMA";
                    CdmaCellLocation cellLocation = (CdmaCellLocation) cellLoc;

                    basestation_id = cellLocation.getBaseStationId() + "";
                    basestation_latitude = cellLocation.getBaseStationLatitude() + "";
                    //int lat = ((L * 90) / 1296000);
                    basestation_longitude = cellLocation.getBaseStationLongitude() + "";
                }


            } catch (Exception e) {
                e.printStackTrace();
                cellIdShort = "";
                cellIdLong = "";
                mcc = "";
                mnc = "";
                lac = "";
                device_type = "GSM";
                basestation_id = "";
                basestation_latitude = "";
                basestation_longitude = "";


            }


            if (isInternetPresent && ConnectionDetector.isConnectedFast(context)) {
                // Toast.makeText(context, "isInternetPresent", Toast.LENGTH_LONG).show();
                gps = new GPSTracker(this.context, "Alarm Manager");
         /*   if (gps.canGetLocation()) {
                Location loc = gps.getLocation();
                // getBatteryPercentage(context);
                //Toast.makeText(context, "battery level" + level + " %" + " Date: " + date + " Time:  " + time + "  Lat:" + loc.getLatitude() + "  Long:" + loc.getLongitude(), Toast.LENGTH_LONG).show();

                //new Insert_Geo_Location_Details().execute();
            } else {


                Toast.makeText(context, "Please on GPS", Toast.LENGTH_LONG).show();
            }*/
            } else {
                // Toast.makeText(context, "isInternetPresent not", Toast.LENGTH_LONG).show();
       /*     if (device_type.equals("GSM")) {
                if (!cellIdShort.equals("") && !lac.equals("")) {
                    gps = new GPSTracker(this.context, "Alarm Manager");
                }

            } else if (device_type.equals("CDMA")) {
                if (!basestation_id.equals("") && !basestation_latitude.equals("") && !basestation_longitude.equals("")) {
                    gps = new GPSTracker(this.context, "Alarm Manager");
                }

            } else {*/

                Log.d("Not having internet", "Database task");
                fillDataInDatabase(this.context);
                //  }

            }


            //Release the lock
            wl.release();
        } catch (Exception e) {
            e.printStackTrace();
            fillDataInDatabase(context);

        }
    }

    public boolean isAirplaneMode() {
        boolean airplaneMode = Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        return airplaneMode;
    }

    public String getOperatorType() {
        String operatorType = "";
        TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
        operatorType = telephonyManager.getNetworkOperatorName();
        return operatorType;
    }


    public String getProfileType() {
        String profileType = "";
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                Log.i("MyApp", "Silent mode");
                profileType = "Silent";
                //Toast.makeText(getApplicationContext(), "MyApp Silent mode", Toast.LENGTH_LONG).show();
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                Log.i("MyApp", "Vibrate mode");
                profileType = "Vibrate";
                //Toast.makeText(getApplicationContext(), "MyApp Vibrate mode", Toast.LENGTH_LONG).show();
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                Log.i("MyApp", "Normal mode");
                profileType = "Normal";
                //Toast.makeText(getApplicationContext(), "MyApp Normal mode", Toast.LENGTH_LONG).show();
                break;

        }


        // String operatorName = telephonyManager.getSimOperatorName();
        return profileType;
    }

    public void manageAlarm(String time) {
        if (time.equals("")) {
            CancelAlarm(this.context);
        }

    }

    public void fillDataInDatabase(Context context) {
        // Toast.makeText(context, "fillDataInDatabase", Toast.LENGTH_LONG).show();
        dbAdapter = new DBAdapter(context);
        String lat, log;
        spf_user_id = this.context.getSharedPreferences("userid", Context.MODE_PRIVATE);
        long rowid = 0;

        pref = this.context.getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 -
        editor1 = pref.edit();
        gps = new GPSTracker(this.context);
        //if (gps.canGetLocation()) {
        Location loc = gps.getLocation();

        userid = spf_user_id.getString("userid", "");
        parentid = pref.getString("parent_id", "");
        //if (loc != null) {
        if (loc != null) {
            lat = loc.getLatitude() + "";
            log = loc.getLongitude() + "";
        } else {
            lat = loc.getLatitude() + "";
            log = loc.getLongitude() + "";
            /*lat = "0";
            log = "0";*/
        }
        trackUser = new TrackUser();
        trackUser.setParent_id(parentid);
        trackUser.setUser_id(userid);
        trackUser.setLatitude(lat);
        trackUser.setLongitude(log);
        trackUser.setBattery_persentage(level);
        trackUser.setDate(this.date);
        trackUser.setTime(this.time);
        trackUser.setNetwork_type("");
        trackUser.setCellIdShort(cellIdShort);

        trackUser.setCellIdLong(cellIdLong);
        trackUser.setLac(lac);
        trackUser.setMcc(mcc);
        trackUser.setMnc(mnc);

        trackUser.setDevice_type(device_type);
        trackUser.setBasestation_id(basestation_id);
        trackUser.setBasestation_latitude(basestation_latitude);
        trackUser.setBasestation_longitude(basestation_longitude);


        trackUser.setProfileType(objSPS.getValueFromShared_Pref("profileType"));
        trackUser.setOperatorType(objSPS.getValueFromShared_Pref("operatorType"));
        trackUser.setShutdownTime(shutdownTime);
        trackUser.setRestartTime(restartTime);
        trackUser.setSignalStrenght(signalStrenght);
        trackUser.setNetworkSubType(networkSubType);
        trackUser.setSentStatus("");

        rowid = dbAdapter.insertTrackUserInfo(trackUser);
        Log.d(" Inserte offlinerowid: ", rowid + "");
        ArrayList<TrackUser> trackUserArrayList = dbAdapter.getTrackUserInfo();
        Log.d(TAG,"trackUserArrayList size:"  + trackUserArrayList.size());
        Log.d(TAG,"trackUserArrayList size:"  + trackUserArrayList.get(0).getOperatorType());

        // }


        //  }
    }
    /*public void start() {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int interval = 1000 * 10;

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();
    }*/


    public void SetAlarm(Context context) {
        // getBatteryPercentage(context);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        try {
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 1, pi);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Something happend wrong!", Toast.LENGTH_LONG).show();

        }
        // am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 1, pi);

    }

    public void CancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    // @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setOnetimeTimer(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        // int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // if (currentapiVersion >= 19) {
        // am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
        //   } else {
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
        // }
        SetAlarm(context);
    }
}

