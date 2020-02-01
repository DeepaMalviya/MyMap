package com.daffodil.mymap.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daffodil.mymap.R;
import com.daffodil.mymap.other.AlarmManagerBroadcastReceiver;
import com.daffodil.mymap.other.ConnectionDetector;
import com.daffodil.mymap.other.GPSTracker;
import com.daffodil.mymap.other.SharePreferanceWrapperSingleton;
import com.daffodil.mymap.other.base.AppConstants;
import com.daffodil.mymap.other.base.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission_group.CAMERA;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_REQUEST_CODE = 200;
    Button btnClientList, btnPunch, btnRevisit, btnAddFollowup, btnAddClient;
    private static final String TAG = "MainActivity";
    String mobile, user_id, otp, user_role, user_name, company_id, company_name;
    String time, date;
    String pTime, pLocation, cellIdShort = "", cellIdLong = "", lac = "", mnc = "", mcc = "", device_type = "";
    int level = 0;
    String shutdownTime = "", restartTime = "", signalStrenght = "", networkSubType = "";
    SharedPreferences sharedpref;
    String operatorType, profileType;
    boolean isAirplaneMode;
    private PendingIntent pendingIntent;
    SharedPreferences.Editor editor1;
    String time_out, time_in;
    TextView tvUserName;
    private JSONObject json, json2;
    String parentid, userid;
    private SharePreferanceWrapperSingleton objSPS;
    private SharedPreferences pref, spf_user_id;
    GPSTracker gps;
    String basestation_id = "", basestation_latitude = "", basestation_longitude = "";
    private AlarmManagerBroadcastReceiver alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (AppConstants.isInternetAvailable(MainActivity.this)) {

            if (!checkPermission()) {
                requestPermission();
            } else {
                Toast.makeText(MainActivity.this, "Permission already granted.", Toast.LENGTH_SHORT).show();
            }
            initView();
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.NETWORK_ERROR), Toast.LENGTH_SHORT).show();

        }


    }
    private void initView() {
        tvUserName = findViewById(R.id.tvUserName);
        btnClientList = findViewById(R.id.btnClientList);
        btnRevisit = findViewById(R.id.btnRevisit);
        btnAddFollowup = findViewById(R.id.btnAddFollowup);
        btnAddClient = findViewById(R.id.btnAddClient);
        btnPunch = findViewById(R.id.btnPunch);

        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(this);
        spf_user_id = getApplicationContext().getSharedPreferences("userid", Context.MODE_PRIVATE);
        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 -
        editor1 = pref.edit();


        alarm = new AlarmManagerBroadcastReceiver();
        gps = new GPSTracker(getApplicationContext());
        pref = getSharedPreferences("MyPref", 0);


        editor1 = pref.edit();
        parentid = pref.getString("parent_id", "");

        gps = new GPSTracker(MainActivity.this);
        spf_user_id = getSharedPreferences("userid", MODE_PRIVATE);
        json = new JSONObject();
        json2 = new JSONObject();
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(this);
        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
        mobile = sharedpref.getString("mobile", "");
        user_id = sharedpref.getString("user_id", "");
        otp = sharedpref.getString("otp", "");
        user_role = sharedpref.getString("user_role", "");
        user_name = sharedpref.getString("user_name", "");
        company_id = sharedpref.getString("company_id", "");
        company_name = sharedpref.getString("company_name", "");
        getStatusAPi();
        //  btnPunch.setText("Punch Out");

        time_in = sharedpref.getString("time_in", "");
        time_out = sharedpref.getString("time_out", "");
        if (!user_name.equals("")) {
            tvUserName.setText("" + user_name);
        }
        if (!time_in.equals("")) {
            btnPunch.setText("Punch Out");

        }
        if (!time_out.equals("")) {
            btnPunch.setText("Punch In");

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        btnPunch.setOnClickListener(this);
        btnAddClient.setOnClickListener(this);
        btnAddFollowup.setOnClickListener(this);
        btnRevisit.setOnClickListener(this);
        btnClientList.setOnClickListener(this);
        setSupportActionBar(toolbar);

        Intent alarmIntent = new Intent(MainActivity.this, AlarmManagerBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

        /*try {
            dbAdapter = new DBAdapter(MainActivity.this);

            dbAdapter.openDataBase();
            dbAdapter.showDataBaseOfDevice(getApplicationContext());
        }  catch (SQLiteCantOpenDatabaseException e) {
            e.printStackTrace();

        } catch (SQLiteException sqe) {
            sqe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

        }*/
     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
*/

        String ctime = getCurrentTimeDate();
        pTime = ctime;
        GPSTracker gps = new GPSTracker(MainActivity.this);
        String location = "";
        if (gps.canGetLocation()) {
            location = gps.getLatitude() + "," + gps.getLongitude();
        }
        pLocation = location;
        // SharedPreferenceManager.instance().setValueToSharedPref("PunchInTime", ctime);
        // SharedPreferenceManager.instance().setValueToSharedPref("PunchInLocation", location);
        profileType = getProfileType();
        isAirplaneMode = isAirplaneMode();
        operatorType = getOperatorType();
        try {
            signalStrenght = ConnectionDetector.getSignalStrenght(MainActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
            //  fillDataInDatabase("");


        }
        try {
            networkSubType = ConnectionDetector.getNetworkSubType(MainActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
            // fillDataInDatabase("");

        }

        try {
            IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batt = getApplicationContext().registerReceiver(null, filter);
            // Default to some unknown/wild value
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
            TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


            //sendToServer();
              /*  makJsonObject(company_idd,user_idd,user_latitudee,user_longitudee,track_datee,track_timee,battery_persentagee,
                    network_typee,profile_typee,operator_typee,cellid_shortt,cellid_longg,lac,mcc,mnc,device_type,basestation_id,
                    basestation_latitude,basestation_longitude,shutdown_timee,restart_timee,signal_strengthh,network_subtypee);
           // dummyData();
            trackApiCallng();*/


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
    }

    public String getOperatorType() {
        String operatorType = "";
        TelephonyManager telephonyManager = ((TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE));
        operatorType = telephonyManager.getNetworkOperatorName();
        return operatorType;
    }
    public boolean isAirplaneMode() {
        boolean airplaneMode = Settings.System.getInt(this.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        return airplaneMode;
    }
    public String getProfileType() {
        String profileType = "";
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                Log.i("MyApp", "Silent mode");
                profileType = "Silent";
                //Toast.makeText(Main2Activity.this, "MyApp Silent mode", Toast.LENGTH_LONG).show();
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                Log.i("MyApp", "Vibrate mode");
                profileType = "Vibrate";
                //Toast.makeText(Main2Activity.this, "MyApp Vibrate mode", Toast.LENGTH_LONG).show();
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                Log.i("MyApp", "Normal mode");
                profileType = "Normal";
                //Toast.makeText(Main2Activity.this, "MyApp Normal mode", Toast.LENGTH_LONG).show();
                break;

        }


        // String operatorName = telephonyManager.getSimOperatorName();
        return profileType;
    }

    private String getCurrentTimeDate() {

        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //SimpleDateFormat formatter = new SimpleDateFormat(" hh:mm:ss aa");

        return formatter.format(calendar.getTime());
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA}, PERMISSION_REQUEST_CODE);

    }
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted) {
                    }
                    // Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    else {

                        // Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;

        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnPunch:
                startActivity(new Intent(this, MapsActivity.class));
                break;
            case R.id.btnClientList:
                startActivity(new Intent(this, ClientListActivity.class));
                break;
            case R.id.btnRevisit:
                startActivity(new Intent(this, RevisitActivity.class));
                break;
            case R.id.btnAddFollowup:
                startActivity(new Intent(this, AddFollowupActivity.class));
                break;
            case R.id.btnAddClient:
                startActivity(new Intent(this, AddClientActivity.class));
                break;
        }

    }
    private void getStatusAPi() {
        final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        String urlData = AppConstants.BASEURL + "user/attendence?company_id=" + company_id + "&user_id=" + user_id;
        Log.e(TAG, "loginSevice:urlData=== " + urlData);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlData, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "response===========" + response.toString());

                try {
                    String data = String.valueOf(response.get("data"));
                    String message = String.valueOf(response.get("message"));
                    int status = response.getInt("status");

                    if (status == 0) {
                        // Toast.makeText(MainActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        JSONObject loginresponce = response.getJSONObject("data");

                        loginresponce.getString("user_id");
                        loginresponce.getString("company_id");
                        loginresponce.getString("time_in");
                        loginresponce.getString("time_out");
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        editor1 = sharedpref.edit();
                        editor1.putString("time_in", loginresponce.getString("time_in"));
                        editor1.putString("time_out", loginresponce.getString("time_out"));
                        editor1.apply();
                        editor1.commit();

                        pDialog.dismiss();
                        time_in = sharedpref.getString("time_in", "");
                        time_out = sharedpref.getString("time_out", "");
                        if (time_in.equals("")) {
                            btnPunch.setText("Punch Out");
                            btnRevisit.setEnabled(true);
                            btnAddFollowup.setEnabled(true);
                            btnAddClient.setEnabled(true);
                            btnRevisit.setBackground(getResources().getDrawable(R.drawable.bg_white_rounded));
                            btnAddFollowup.setBackground(getResources().getDrawable(R.drawable.bg_white_rounded));
                            btnAddClient.setBackground(getResources().getDrawable(R.drawable.bg_white_rounded));
                        }
                        if (time_out.equals("")) {
                            btnPunch.setText("Punch In");
                            btnRevisit.setEnabled(false);
                            btnAddFollowup.setEnabled(false);
                            btnAddClient.setEnabled(false);
                            btnRevisit.setBackground(getResources().getDrawable(R.drawable.bg_white_ractangle_gray));
                            btnAddFollowup.setBackground(getResources().getDrawable(R.drawable.bg_white_ractangle_gray));
                            btnAddClient.setBackground(getResources().getDrawable(R.drawable.bg_white_ractangle_gray));

                        }
                        if (!time_in.equals("")) {
                            btnPunch.setText("Punch Out");
                            btnRevisit.setEnabled(true);
                            btnAddFollowup.setEnabled(true);
                            btnAddClient.setEnabled(true);

                            btnRevisit.setBackground(getResources().getDrawable(R.drawable.bg_white_rounded));
                            btnAddFollowup.setBackground(getResources().getDrawable(R.drawable.bg_white_rounded));
                            btnAddClient.setBackground(getResources().getDrawable(R.drawable.bg_white_rounded));

                        }
                        if (!time_out.equals("")) {
                            btnPunch.setText("Punch In");
                            btnRevisit.setEnabled(false);
                            btnAddFollowup.setEnabled(false);
                            btnAddClient.setEnabled(false);
                            btnRevisit.setBackground(getResources().getDrawable(R.drawable.bg_white_ractangle_gray));
                            btnAddFollowup.setBackground(getResources().getDrawable(R.drawable.bg_white_ractangle_gray));
                            btnAddClient.setBackground(getResources().getDrawable(R.drawable.bg_white_ractangle_gray));

                        }

                    } else {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(MainActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(MainActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG,
                // "Error: " + error.getMessage());
                //com.android.volley.TimeoutError
                String er = "com.android.volley.TimeoutError";

                // com.android.volley.TimeoutError
                if (er.equals("com.android.volley.TimeoutError")) {

                } else {
                    //  sendError(error.toString(), "user/login?username=");
                    Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }


                pDialog.dismiss();

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

}
