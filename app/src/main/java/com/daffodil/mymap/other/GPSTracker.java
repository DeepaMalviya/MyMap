package com.daffodil.mymap.other;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;

public class GPSTracker extends Service implements LocationListener {

    private Context mContext = this;
    private int battryPer;
    private JSONObject json, json2;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    private SharedPreferences pref, spf_user_id;
    private String userid, parentid;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 50; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 5; // 1 minute
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;

    // Declaring a Location Manager
    protected LocationManager locationManager;
    private ProgressDialog progress;
    SharedPreferences.Editor editor1;
    private DBAdapter dbAdapter;
    private SharePreferanceWrapperSingleton objSPS;
    private String shutdownTime = "", restartTime = "", signalStrenght = "", networkSubType = "", sentStatus = "", cellIdShort = "", cellIdLong = "", lac = "", mnc = "", mcc = "", device_type = "", basestation_id = "", basestation_latitude = "", basestation_longitude = "";
    private static int count = 0;
    private TrackUser trackUser;

    public GPSTracker(Context context) {
        this.mContext = context;
        //progress = new ProgressDialog(getApplicationContext());
        getLocation();
    }

    public GPSTracker(Context context, String comeFrom) {
        try {
            this.mContext = context;
            //Toast.makeText(context, "GPSTracker", Toast.LENGTH_LONG).show();

           /* Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
            intent.putExtra("enabled", true);
            sendBroadcast(intent);*/

            spf_user_id = mContext.getSharedPreferences("userid", MODE_PRIVATE);
            dbAdapter = new DBAdapter(context);
            objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
            objSPS.setPref(mContext);
            String profileType = getProfileType();
            boolean isAirplaneMode = isAirplaneMode();
            String operatorType = getOperatorType();
            //Toast.makeText(context, "GPSTracker 1", Toast.LENGTH_LONG).show();

            // dbAdapter.showDataBaseOfDevice(context);

            pref = mContext.getSharedPreferences("MyPref", 0);


            editor1 = pref.edit();
            parentid = pref.getString("parent_id", "");

            json = new JSONObject();
            json2 = new JSONObject();
            //Toast.makeText(context, "GPSTracker 2", Toast.LENGTH_LONG).show();
            try {
                objSPS.setValueToSharedPref("" + "", profileType);
            } catch (Exception e) {
                profileType = "";
                e.printStackTrace();

            }
            try {
                objSPS.setValueToSharedPref("isAirplaneMode", isAirplaneMode + "");
            } catch (Exception e) {
                isAirplaneMode = false;
                e.printStackTrace();

            }
            try {
                objSPS.setValueToSharedPref("operatorType", operatorType);
            } catch (Exception e) {
                operatorType = "";
                e.printStackTrace();

            }


            // getBatteryPercentage(context);
            //Log.d("GPSTracker*********constructor*******************", "GPSTracker called");
            getLocation();


            sendToServer();
        } catch (Exception e) {
            e.printStackTrace();

        }
        // new Insert_Geo_Location_Details().execute();

    }


    private void sendToServer() {
        //Toast.makeText(mContext, "sendToServer", Toast.LENGTH_LONG).show();
        /*ConnectionDetector cd = new ConnectionDetector(mContext);
        boolean isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            //Toast.makeText(mContext, "sendToServer isInternetPresent", Toast.LENGTH_LONG).show();
            new Insert_Geo_Location_Details().execute();
        }*/

        new Insert_Geo_Location_Details().execute();
    }

    public boolean isAirplaneMode() {
        boolean airplaneMode = Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        return airplaneMode;
    }

    public String getOperatorType() {
        String operatorType = "";
        TelephonyManager telephonyManager = ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE));
        operatorType = telephonyManager.getNetworkOperatorName();
        return operatorType;
    }


    public String getProfileType() {
        String profileType = "";
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

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

    @SuppressLint("MissingPermission")
    @TargetApi(Build.VERSION_CODES.M)
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;

                if (isGPSEnabled) {
                    if (location == null) {


                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                //Toast.makeText(mContext, "GPS", Toast.LENGTH_LONG).show();
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    if (isNetworkEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("Network", "Network Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if (location != null) {
                                    // Toast.makeText(mContext, "Network", Toast.LENGTH_LONG).show();
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // getDeviceImei();
        }
    }


/*    public Location getLocation() {
        try {
            //Toast.makeText(mContext, "getLocation", Toast.LENGTH_LONG).show();
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);


                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            //Toast.makeText(mContext, "getLocation GPS Enabled", Toast.LENGTH_LONG).show();
                            Log.d("GPS Enabled", "GPS Enabled");
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isNetworkEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);


                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                //Toast.makeText(mContext, "getLocation  Network", Toast.LENGTH_LONG).show();
                                Log.d("Network", "Network");
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }*/

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
  /*  public void stopUsingGPS() {
        if (locationManager != null) {
            System.out.println("stopUsingGPS called");
            locationManager.removeUpdates(this);

        }
    }*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        // stopUsingGPS();
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            NumberFormat formatter = new DecimalFormat("#0.0000000");
            latitude = location.getLatitude();
            String lat = formatter.format(latitude);
            latitude = Double.parseDouble(lat);

        }

        // return latitude
        return latitude;
    }

    public Location fetchLocationShowDialog() {
        Location loc = getLocation();
        if (loc == null) {

            progress = ProgressDialog.show(getApplicationContext(), "Fetching Location...",
                    "Your current location is fetching Please wait..", true);

        }
        return loc;

    }


    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            //longitude = location.getLongitude();
            NumberFormat formatter = new DecimalFormat("#0.0000000");
            longitude = location.getLongitude();
            String log = formatter.format(longitude);
            longitude = Double.parseDouble(log);
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showAlertDialog(String title, String message, final String posBtnTitle) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        // alertDialog.setTitle("Fetching Location...");
        alertDialog.setTitle(title);

        // Setting Dialog Message
        //
        // alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setMessage(message);

        // On pressing Settings button "Settings"
        alertDialog.setPositiveButton(posBtnTitle, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (posBtnTitle.equals("Ok")) {
                    dialog.cancel();

                } else {

                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void onLocationChanged(Location location) {
        //progress.dismiss();
        progress = new ProgressDialog(mContext);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            if (progress.isShowing()) {
                progress.dismiss();
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public JSONArray prepareJSONData() {
        String lat, log;
        int i = 0;
        //dbAdapter.delete_trackUser();
        //  JSONObject json=new JSONObject();
        JSONArray postjson = new JSONArray();


        ArrayList<TrackUser> arrlistTrackUser = dbAdapter.getTrackUserInfo();


        if (arrlistTrackUser.size() > 0) {
            for (i = 0; i < arrlistTrackUser.size(); i++) {
                TrackUser trackUser = arrlistTrackUser.get(i);
                if (trackUser.getNetwork_type().equals("") && !trackUser.getSentStatus().equals("SENT")) {
                    try {

                        Log.d("canGetLocation", "canGetLocation yes");

                        json = new JSONObject();

                        json.put("parent_id", trackUser.getParent_id());
                        json.put("user_id", trackUser.getUser_id());
                        json.put("user_latitude", trackUser.getLatitude());
                        json.put("user_longitude", trackUser.getLongitude());

                        json.put("track_date", trackUser.getDate());
                        json.put("track_time", trackUser.getTime());
                        json.put("battery_persentage", trackUser.getBattery_persentage());


                        json.put("network_type", trackUser.getNetwork_type());

                        json.put("profile_type", trackUser.getProfileType());
                        json.put("operator_type", trackUser.getOperatorType());

                        json.put("cellid_short", trackUser.getCellIdShort());
                        json.put("cellid_long", trackUser.getCellIdLong());
                        json.put("lac", trackUser.getLac());
                        json.put("mcc", trackUser.getMcc());
                        json.put("mnc", trackUser.getMnc());

                        json.put("device_type", trackUser.getDevice_type());
                        json.put("basestation_id", trackUser.getBasestation_id());
                        json.put("basestation_latitude", trackUser.getBasestation_latitude());
                        json.put("basestation_longitude", trackUser.getBasestation_longitude());


                        json.put("shutdown_time", trackUser.getShutdownTime());
                        json.put("restart_time", trackUser.getRestartTime());
                        json.put("signal_strength", trackUser.getSignalStrenght());
                        json.put("network_subtype", trackUser.getNetworkSubType());


                        postjson.put(json);
                        dbAdapter.deleteRow("TrackUser", "time", trackUser.getTime());
                        ArrayList<TrackUser> aList = dbAdapter.getTrackUserInfo();

                        Log.d("Status: ", "Could not find Location");

                    } catch (JSONException je) {
                        je.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //Having Network
                }

            }
            //dbAdapter.delete_trackUser();
        }

        try {
            shutdownTime = objSPS.getValueFromShared_Pref("ShutdownTime");

            //restartTime = objSPS.getValueFromShared_Pref("RestartTime");
            restartTime = SharedPreferenceManager.instance().getValueFromShared_Pref("RestartTime");
            System.out.println("shutdownTime : " + shutdownTime + "    restartTime: " + restartTime);

            signalStrenght = ConnectionDetector.getSignalStrenght(mContext);
            networkSubType = ConnectionDetector.getNetworkSubType(mContext);

            IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batt = mContext.getApplicationContext().registerReceiver(null, filter);
            String level = "0"; // Default to some unknown/wild value
// registerReceiver method call could return null, so check that!
            if (batt != null) {
                level = batt.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) + "";
            }

            // Format formatter = new SimpleDateFormat("hh:mm:ss a");
            Format formatter = new SimpleDateFormat("yyyy-MM-dd");
            String date = formatter.format(new Date());
            //msgStr.append(formatter.format(new Date()));
            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            formatter = new SimpleDateFormat("HH:mm:ss");
          //  String time = formatter.format(new Date());
            String time = dateFormat.format(new Date());
            try {
                TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                @SuppressLint("MissingPermission") CellLocation cellLoc = telephonyManager.getCellLocation();
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

            //  if (canGetLocation()) {
            Log.d("canGetLocation", "canGetLocation yes");
            Location loc = getLocation();
              /*  try {
                    lat = loc.getLatitude() + "";
                    log = loc.getLongitude() + "";
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                    lat = "0";
                    log = "0";
                }*/
            if (loc != null) {
                lat = loc.getLatitude() + "";
                log = loc.getLongitude() + "";
            } else {
                lat = "0";
                log = "0";
            }

            userid = spf_user_id.getString("userid", "");
            parentid = pref.getString("parent_id", "");
            json = new JSONObject();
            json.put("parent_id", parentid);
            json.put("user_id", userid);
            json.put("user_latitude", lat);
            json.put("user_longitude", log);

            json.put("track_date", date);
            json.put("track_time", time);
            json.put("battery_persentage", level);


            json.put("network_type", ConnectionDetector.network_type);
            json.put("profile_type", objSPS.getValueFromShared_Pref("profileType"));
            json.put("operator_type", objSPS.getValueFromShared_Pref("operatorType"));

            json.put("cellid_short", cellIdShort);
            json.put("cellid_long", cellIdLong);
            json.put("lac", lac);
            json.put("mcc", mcc);
            json.put("mnc", mnc);

            json.put("device_type", device_type);
            json.put("basestation_id", basestation_id);
            json.put("basestation_latitude", basestation_latitude);
            json.put("basestation_longitude", basestation_longitude);
            json.put("shutdown_time", shutdownTime);

            json.put("restart_time", restartTime);
            json.put("signal_strength", signalStrenght);
            json.put("network_subtype", networkSubType);

            postjson.put(json);



               /* json2.put("params", postjson);
                System.out.println(json2.toString());*/


          /*  } else {
                Log.d("Status: ", "Could not find Location");
            }*/

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return postjson;
    }

    private class Insert_Geo_Location_Details extends AsyncTask<String, String, String> {
        JSONArray items_list;
        //ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected String doInBackground(String... params) {
            String serverResponse = "";
            try {
                try {
                    IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                    Intent batt = mContext.getApplicationContext().registerReceiver(null, filter);
                    int level = 0; // Default to some unknown/wild value
// registerReceiver method call could return null, so check that!
                    if (batt != null) {
                        level = batt.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                    }

                    // Format formatter = new SimpleDateFormat("hh:mm:ss a");
                    Format formatter = new SimpleDateFormat("MM-dd-yyyy");
                    String date = formatter.format(new Date());
                    //msgStr.append(formatter.format(new Date()));
                    formatter = new SimpleDateFormat("hh:mm");
                    String time = formatter.format(new Date());

                    if (canGetLocation()) {
                        Log.d("canGetLocation", "canGetLocation yes");
                        Location loc = getLocation();

                        userid = spf_user_id.getString("userid", "");
                        parentid = pref.getString("parent_id", "");

                        json.put("parent_id", parentid);
                        json.put("user_id", userid);
                        if (loc != null) {
                            json.put("Latitude", loc.getLatitude());
                            json.put("Longitude", loc.getLongitude());
                        } else {
                            json.put("Latitude", "0");
                            json.put("Longitude", "0");
                        }

                        json.put("date", date);
                        json.put("time", time);
                        json.put("battery_persentage", level);


                        json.put("network_type", ConnectionDetector.network_type);


                    } else {
                        Log.d("Status: ", "Could not find Location");
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                // JSONArray postjson = prepareJSONData();


                JSONArray postjson = new JSONArray();
                postjson.put(json);
                json2.put("params", postjson);
                System.out.println(json2.toString());


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                // if (canGetLocation()) {
                JSONArray postjson = prepareJSONData();
                json2.put("params", postjson);
                System.out.println(json2.toString());
                //  }


            } catch (JSONException je) {
                je.printStackTrace();

            }
            try {
                /*http://infibizapi.daffodillab.in/index.php/user/inserttrackbulk*/
                //String url2 = "http://infibizapi.daffodillab.in/index.php/user/inserttrack";
                String url2 = "http://infibizapi.daffodillab.in/index.php/user/inserttrackbulk";
              //  serverResponse = WebServiceConnection.getData(url2, json2.toString());
                System.out.println(" Server response *********** serverResponse" + serverResponse);

            } catch (Exception e) {

                e.printStackTrace();
                fillDataInDatabase("");
            }


            return serverResponse.trim();
        }

        @Override
        protected void onPreExecute() {


            super.onPreExecute();
            // Toast.makeText(mContext, "onPreExecute", Toast.LENGTH_LONG).show();


        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            if (!result.equals("") && !result.equals(null)) {
                if (result.equalsIgnoreCase("true")) {
                    // Toast.makeText(mContext, "onPostExecute", Toast.LENGTH_LONG).show();
                    fillDataInDatabase("SENT");

                    Log.d("User track", "data sent successfully");
                } else {
                    try {
                        fillDataInDatabase("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                Log.d("User track", "data could not send successfully");
                try {
                    fillDataInDatabase("");
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            // stopUsingGPS();


        }
    }

    public void fillDataInDatabase(String sentStatus) {

        try {
            long rowid = 0;

            JSONArray jsonArray = (JSONArray) json2.getJSONArray("params");

            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);


                    trackUser = new TrackUser();
                    trackUser.setParent_id(jsonObject.getString("parent_id"));
                    trackUser.setUser_id(jsonObject.getString("user_id"));
                    trackUser.setLatitude(jsonObject.getString("user_latitude"));
                    trackUser.setLongitude(jsonObject.getString("user_longitude"));
                    trackUser.setBattery_persentage(jsonObject.getString("battery_persentage"));
                    trackUser.setDate(jsonObject.getString("track_date"));
                    trackUser.setTime(jsonObject.getString("track_time"));

                    trackUser.setNetwork_type(jsonObject.getString("network_type"));
                    trackUser.setProfileType(jsonObject.getString("profile_type"));
                    trackUser.setOperatorType(jsonObject.getString("operator_type"));

                    trackUser.setCellIdShort(jsonObject.getString("cellid_short"));

                    trackUser.setCellIdLong(jsonObject.getString("cellid_long"));

                    trackUser.setLac(jsonObject.getString("lac"));
                    trackUser.setMcc(jsonObject.getString("mcc"));
                    trackUser.setMnc(jsonObject.getString("mnc"));

                    trackUser.setDevice_type(jsonObject.getString("device_type"));
                    trackUser.setBasestation_id(jsonObject.getString("basestation_id"));
                    trackUser.setBasestation_latitude(jsonObject.getString("basestation_latitude"));
                    trackUser.setBasestation_longitude(jsonObject.getString("basestation_longitude"));

                    trackUser.setProfileType(jsonObject.getString("profile_type"));
                    trackUser.setOperatorType(jsonObject.getString("operator_type"));
                    trackUser.setShutdownTime(jsonObject.getString("shutdown_time"));
                    trackUser.setRestartTime(jsonObject.getString("restart_time"));
                    trackUser.setSignalStrenght(jsonObject.getString("signal_strength"));
                    trackUser.setNetworkSubType(jsonObject.getString("network_subtype"));
                    trackUser.setSentStatus(sentStatus);
                    if (!trackUser.getNetwork_type().equals("")) {
                        rowid = dbAdapter.insertTrackUserInfo(trackUser);
                    } else {

                        rowid = dbAdapter.updateTrackData(trackUser);
                    }


                    System.out.println("row inserted:" + rowid);
                }

            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
       /* String lat = "0", log = "0";
        shutdownTime = objSPS.getValueFromShared_Pref("ShutdownTime");

        //restartTime = objSPS.getValueFromShared_Pref("RestartTime");
        restartTime = SharedPreferenceManager.instance().getValueFromShared_Pref("RestartTime");
        System.out.println("shutdownTime : " + shutdownTime + "    restartTime: " + restartTime);

        signalStrenght = ConnectionDetector.getSignalStrenght(mContext);
        networkSubType = ConnectionDetector.getNetworkSubType(mContext);

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batt = mContext.getApplicationContext().registerReceiver(null, filter);
        String level = "0"; // Default to some unknown/wild value
// registerReceiver method call could return null, so check that!
        if (batt != null) {
            level = batt.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) + "";
        }

        // Format formatter = new SimpleDateFormat("hh:mm:ss a");
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(new Date());
        //msgStr.append(formatter.format(new Date()));
        formatter = new SimpleDateFormat("HH:mm:ss");
        String time = formatter.format(new Date());
        try {
            TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
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

        //  if (canGetLocation()) {
        Log.d("canGetLocation", "canGetLocation yes");
        Location loc = getLocation();
              *//*  try {
                    lat = loc.getLatitude() + "";
                    log = loc.getLongitude() + "";
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                    lat = "0";
                    log = "0";
                }*//*
        if (loc != null) {
            lat = loc.getLatitude() + "";
            log = loc.getLongitude() + "";
        } else {
            lat = "0";
            log = "0";
        }*/






}
