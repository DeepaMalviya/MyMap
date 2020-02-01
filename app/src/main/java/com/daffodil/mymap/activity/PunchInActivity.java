/*
package com.daffodil.mymap.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daffodil.mymap.R;
import com.daffodil.officeproject.Old.base.AppController;
import com.daffodil.officeproject.Old.base.VolleyMultipartRequest;
import com.daffodil.officeproject.Old.base.VolleySingleton;
import com.daffodil.officeproject.Old.others.DBHelper;
import com.daffodil.officeproject.Old.utilities.AlarmManagerBroadcastReceiver;
import com.daffodil.officeproject.Old.utilities.DBAdapter;
import com.daffodil.officeproject.Old.utilities.GPSTracker;
import com.daffodil.officeproject.Old.utilities.SharePreferanceWrapperSingleton;
import com.daffodil.officeproject.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static com.daffodil.officeproject.Old.base.AppConstants.BASEURL;


public class PunchInActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final String TAG = "PunchInActivity";
    private static final int REQUEST_LOCATION = 1;
    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private static final int LOCATION_REQUEST_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    LinearLayout linearFindMe;
    Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    TextView TvTimeIn, tvDate, tvAddress, TvTimeout;
    private static final int PERMISSION_REQUEST_CODE1 = 201;
    private static final int PERMISSION_REQUEST_CODE2 = 202;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    DBHelper mydb;
    SharedPreferences sharedpref;
    String mobile, user_id, otp, user_role, user_name, company_id, company_name;
    CheckBox checkLocation, checkHalf;
    int PICK_IMAGE_REQUEST = 111;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    String formattedDate;
    int year, month, day, hourofday, minuteofday, totalCurrenttime;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;
    String time_out, time_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punch_in);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            // permission has been granted, continue as usual
            Toast.makeText(this, "permission has been granted, continue as usual", Toast.LENGTH_SHORT).show();
            Task<Location> locationResult = LocationServices
                    .getFusedLocationProviderClient(this)
                    .getLastLocation();
        }

        CheckGpsStatus();

        mydb = new DBHelper(this);
        Intent alarmIntent = new Intent(PunchInActivity.this, AlarmManagerBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(PunchInActivity.this, 0, alarmIntent, 0);


    }

    LocationManager locationManager;
    boolean GpsStatus;

    public void CheckGpsStatus() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (GpsStatus == true) {
            initData();
            getStatusAPi();
            //TimeValidationMethod();
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fetchLastLocation();
            findViewByIdMethod();
        } else {
            Intent i = new
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(i);
        }
    }

    private void TimeValidationMethod() {
        Calendar calender = Calendar.getInstance();
        calender.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        System.out.println(calender.get(Calendar.HOUR_OF_DAY) + ":" + calender.get(Calendar.MINUTE) + ":" + calender.getActualMinimum(Calendar.SECOND));
        int daaa = calender.get(Calendar.HOUR_OF_DAY);
        if (daaa >= 22) {
            TimeInAPi("out");
        } else {
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, Main2Activity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getStatusAPi() {

        String urlData = BASEURL + "user/attendence?company_id=" + company_id + "&user_id=" + user_id;
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
                        JSONObject loginresponce = response.getJSONObject("data");

                        loginresponce.getString("time_in");
                        loginresponce.getString("time_out");
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("time_in", loginresponce.getString("time_in"));
                        ed.putString("time_out", loginresponce.getString("time_out"));
                        ed.apply();
                        ed.commit();

                        Log.e(TAG, "onResponse:=== " + loginresponce.getString("time_in"));
                        Log.e(TAG, "onResponse:=== " + loginresponce.getString("time_out"));
                        time_in = sharedpref.getString("time_in", "");
                        time_out = sharedpref.getString("time_out", "");
                        Log.e(TAG, "onResponse: time_in " + time_in);
                        Log.e(TAG, "onResponse:time_out " + time_out);

                        if (!time_in.equals("")) {
                            TvTimeIn.setText("Time Out");
                            TvTimeIn.setBackgroundResource(R.drawable.bg_white_rounded_grreen);
                            TvTimeIn.setEnabled(true);
                            TvTimeIn.setClickable(true);
                        }
                        if (!time_out.equals("")) {
                            TvTimeIn.setText("Time In");
                            TvTimeIn.setBackgroundResource(R.drawable.bg_white_rounded_gray);
                            TvTimeIn.setEnabled(true);
                            TvTimeIn.setClickable(true);
                        }

                        if (time_in.equals("")) {
                            TvTimeIn.setText("Time In");

                            TvTimeIn.setBackgroundResource(R.drawable.bg_white_rounded_grreen);
                            TvTimeIn.setEnabled(true);
                            TvTimeIn.setClickable(true);
                        }
                        if (!time_in.equals("") && !time_out.equals("")) {
                            TvTimeIn.setText("Time In ");
                            TvTimeIn.setBackgroundResource(R.drawable.bg_white_rounded_gray);
                            TvTimeIn.setEnabled(false);
                            TvTimeIn.setClickable(false);
                        }

                        if (time_in.equals("")) {
                            TvTimeIn.setText("Time In");

                            TvTimeIn.setBackgroundResource(R.drawable.bg_white_rounded_grreen);
                            TvTimeIn.setEnabled(true);
                            TvTimeIn.setClickable(true);
                        }
                    } else {
                        Toast.makeText(PunchInActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(PunchInActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PunchInActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String er = "com.android.volley.TimeoutError";

                if (er.equals("com.android.volley.TimeoutError")) {

                } else {
                    Toast.makeText(PunchInActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    private void initData() {
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(this);

        gps = new GPSTracker(getApplicationContext());
        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
        mobile = sharedpref.getString("mobile", "");
        user_id = sharedpref.getString("user_id", "");
        otp = sharedpref.getString("otp", "");
        user_role = sharedpref.getString("user_role", "");
        user_name = sharedpref.getString("user_name", "");
        company_id = sharedpref.getString("company_id", "");
        company_name = sharedpref.getString("company_name", "");
        type = sharedpref.getString("type", "");
        time_in = sharedpref.getString("time_in", "");
        time_out = sharedpref.getString("time_out", "");
        try {
            Strclickcount1 = sharedpref.getInt("clickcount1", 0);
            Strclickcount = sharedpref.getInt("clickcount", 0);
            Log.e(TAG, "initData:clickcount1== " + Strclickcount1);
            Log.e(TAG, "initData:clickcount== " + Strclickcount);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date1 = sdf.parse("19:28");
            Date date2 = sdf.parse("21:13");

            // Outputs -1 as date1 is before date2
            System.out.println("======" + date1.compareTo(date2));

            // Outputs 1 as date1 is after date1
            System.out.println("======" + date2.compareTo(date1));

            date2 = sdf.parse("19:28");
            // Outputs 0 as the dates are now equal
            System.out.println("======" + date1.compareTo(date2));

        } catch (ParseException e) {
            // Exception handling goes here
        }
    }

    private void insertData() {
        boolean id = mydb.insertContact(user_name, mobile, tvAddress.getText().toString(), currentLocation.getLatitude(), currentLocation.getLongitude(), photo);
        Cursor rs = mydb.getData(1);
        rs.moveToFirst();

        String nam = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
        String phon = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHONE));
        String plac = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_CITY));
        String LATI = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_LATI));
        String LONGI = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_LONGI));
        String IMAGE = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_IMAGE));
        String timestamp = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_TIMESTAMP));
        if (!rs.isClosed()) {
            rs.close();
        }
    }

    private void getDbData() {
        Cursor rs = mydb.getData(1);
        //id_To_Update = Value;
        rs.moveToFirst();

        String nam = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
        String phon = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHONE));
        String plac = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_CITY));
        String LATI = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_LATI));
        String LONGI = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_LONGI));
        String IMAGE = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_IMAGE));
        String timestamp = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_TIMESTAMP));
        if (!rs.isClosed()) {
            rs.close();
        }
    }

    boolean isCurrent_location = false;

    private void findViewByIdMethod() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Punch In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        checkLocation = findViewById(R.id.checkLocation);
        checkHalf = findViewById(R.id.checkHalf);
        checkHalf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG, "onCheckedChanged:====checkHalf======= " + isChecked);
            }
        });
        checkLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG, "onCheckedChanged:====checkHalf======= " + isChecked);
                isCurrent_location = isChecked;
            }
        });

        tvDate = findViewById(R.id.tvDate);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        String sDate = c.get(Calendar.DAY_OF_MONTH) + "-" + month + "-" + c.get(Calendar.YEAR);
        tvDate.setText("Date : " + sDate);
        tvAddress = findViewById(R.id.tvAddress);
        TvTimeIn = findViewById(R.id.TvTimeIn);
        TvTimeout = findViewById(R.id.TvTimeout);
        linearFindMe = findViewById(R.id.linearFindMe);

        TvTimeIn.setOnClickListener(this);
        TvTimeout.setOnClickListener(this);
        linearFindMe.setOnClickListener(this);

        if (!time_in.equals("")) {
            TvTimeIn.setText("Time Out");
            TvTimeIn.setBackgroundResource(R.drawable.bg_white_rounded_grreen);
            TvTimeIn.setEnabled(true);
            TvTimeIn.setClickable(true);
        }
        if (!time_out.equals("")) {
            TvTimeIn.setText("Time In");
            TvTimeIn.setBackgroundResource(R.drawable.bg_white_rounded_gray);
            TvTimeIn.setEnabled(true);
            TvTimeIn.setClickable(true);
        }
        if (!time_in.equals("") && !time_out.equals("")) {
            Log.e(TAG, "onResponse: --------=-------------");
            TvTimeIn.setText("Time In ");
            TvTimeIn.setBackgroundResource(R.drawable.bg_white_rounded_gray);
            TvTimeIn.setEnabled(false);
            TvTimeIn.setClickable(false);
        }
        if (time_in.equals("")) {
            TvTimeIn.setText("Time In");

            TvTimeIn.setBackgroundResource(R.drawable.bg_white_rounded_grreen);
            TvTimeIn.setEnabled(true);
            TvTimeIn.setClickable(true);
        }

        if (ActivityCompat.checkSelfPermission(PunchInActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PunchInActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResult.length > 0
                        && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                // requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
            case LOCATION_REQUEST_CODE:
                if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                } else {
                    Toast.makeText(PunchInActivity.this, "Location permission missing", Toast.LENGTH_SHORT).show();
                }
                break;

            case PERMISSION_REQUEST_CODE1:
                if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                // requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
            case 5:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 12);
                }
                break;
            case CAMERA_REQUEST_CODE:
                Intent takePictureIntenttt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntenttt.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntenttt, 12);
                }
                break;
            case 6:
                Intent takePictureIntentt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntentt.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntentt, 12);
                }
                break;
            case REQUEST_LOCATION:
                if (grantResult.length == 1
                        && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    // We can now safely use the API we requested access to
                    Task<Location> locationResult = LocationServices
                            .getFusedLocationProviderClient(this)
                            .getLastLocation();
                    Toast.makeText(this, "permission has been granted, continue as usual", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Permission was denied or request was cancelled", Toast.LENGTH_SHORT).show();

                    // Permission was denied or request was cancelled
                }
                break;

        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(PunchInActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    Bitmap photo;
    Uri imageUri;
    Uri imageUri1 = Uri.parse("");
    private String selectedPath = "";

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Log.e(TAG, "onActivityResult: requestCode" + requestCode);
        Log.e(TAG, "onActivityResult:resultCode " + resultCode);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    //imageView.setImageBitmap(photo);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    File destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");

                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Uri selectedImageUri = getImageUri(getActivity(), thumbnail);
                    //selectedPath = getRealPathFromURI(selectedImageUri);
                    // imageCompany.setVisibility(View.VISIBLE);
                    imageUri = Uri.fromFile(destination);
                    imageUri1 = Uri.fromFile(destination);
                    selectedPath = destination.getAbsolutePath();

  if (imageUri1 == null) {

                        Toast.makeText(PunchInActivity.this, "Take client Photo", Toast.LENGTH_SHORT).show();
                        // openCameraIntent();
                    } else {
                        if (!time_in.equals("")) {
                            type = "out";
                            TimeInAPi(type);
                        }

                        if (time_in.equals("")) {
                            type = "in";
                            TimeInAPi(type);
                        }
                    }

                    Log.e(TAG, "onCaptureImageResult: ----imageUri1---------" + imageUri1);
                    Log.e(TAG, "onCaptureImageResult: -------imageUri-------" + imageUri);
                    //Toast.makeText(AddClientActivity.this, destination.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    //  imageCompany.setImageBitmap(photo);
                    //  imageCompany.setVisibility(View.VISIBLE);

                }

                break;
            case PERMISSION_REQUEST_CODE2:
                if (resultCode == RESULT_OK) {
                    photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    //imageView.setImageBitmap(photo);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    File destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");

                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Uri selectedImageUri = getImageUri(getActivity(), thumbnail);
                    //selectedPath = getRealPathFromURI(selectedImageUri);
                    // imageCompany.setVisibility(View.VISIBLE);
                    imageUri = Uri.fromFile(destination);
                    imageUri1 = Uri.fromFile(destination);
                    selectedPath = destination.getAbsolutePath();
                    //  type = "in";
if (imageUri1 == null) {

                        Toast.makeText(PunchInActivity.this, "Take client Photo", Toast.LENGTH_SHORT).show();
                        // openCameraIntent();
                    } else {
                        if (!time_in.equals("")) {
                            type = "out";
                            TimeInAPi(type);
                        }

                        if (time_in.equals("")) {
                            type = "in";
                            TimeInAPi(type);
                        }
                    }

                    Log.e(TAG, "onCaptureImageResult: ----imageUri1---------" + imageUri1);
                    Log.e(TAG, "onCaptureImageResult: -------imageUri-------" + imageUri);
                    //Toast.makeText(AddClientActivity.this, destination.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    //  imageCompany.setImageBitmap(photo);
                    //  imageCompany.setVisibility(View.VISIBLE);

                }

                break;
            case 12:
                if (resultCode == RESULT_OK) {
                    photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    //imageView.setImageBitmap(photo);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    File destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");

                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Uri selectedImageUri = getImageUri(getActivity(), thumbnail);
                    //selectedPath = getRealPathFromURI(selectedImageUri);
                    // imageCompany.setVisibility(View.VISIBLE);
                    imageUri = Uri.fromFile(destination);
                    imageUri1 = Uri.fromFile(destination);
                    selectedPath = destination.getAbsolutePath();

                    if (imageUri1 == null) {

                        Toast.makeText(PunchInActivity.this, "Take client Photo", Toast.LENGTH_SHORT).show();
                        // openCameraIntent();
                    } else {
                        if (!time_in.equals("")) {
                            type = "out";
                            TimeInAPi(type);
                        }

                        if (time_in.equals("")) {
                            type = "in";
                            TimeInAPi(type);
                        }
                    }
                    Log.e(TAG, "onCaptureImageResult: ----imageUri1---------" + imageUri1);
                    Log.e(TAG, "onCaptureImageResult: -------imageUri-------" + imageUri);
                    //Toast.makeText(AddClientActivity.this, destination.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    //  imageCompany.setImageBitmap(photo);
                    //  imageCompany.setVisibility(View.VISIBLE);

                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    File destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");

                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Uri selectedImageUri = getImageUri(getActivity(), thumbnail);
                    //selectedPath = getRealPathFromURI(selectedImageUri);
                    // imageCompany.setVisibility(View.VISIBLE);
                    imageUri = Uri.fromFile(destination);
                    imageUri1 = Uri.fromFile(destination);
                    selectedPath = destination.getAbsolutePath();
                    Log.e(TAG, "onCaptureImageResult: ----imageUri1---------" + imageUri1);
                    Log.e(TAG, "onCaptureImageResult: -------imageUri-------" + imageUri);
                    // type = "out";
if (imageUri == null) {

                        Toast.makeText(PunchInActivity.this, "Take client Photo", Toast.LENGTH_SHORT).show();
                        // openCameraIntent();
                    } else {
                        if (!time_in.equals("")) {
                            type = "out";
                            TimeInAPi(type);
                        }

                        if (time_in.equals("")) {
                            type = "in";
                            TimeInAPi(type);
                        }
                    }


                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    File destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");

                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Uri selectedImageUri = getImageUri(getActivity(), thumbnail);
                    //selectedPath = getRealPathFromURI(selectedImageUri);
                    // imageCompany.setVisibility(View.VISIBLE);
                    imageUri = Uri.fromFile(destination);
                    imageUri1 = Uri.fromFile(destination);
                    selectedPath = destination.getAbsolutePath();
                    Log.e(TAG, "onCaptureImageResult: ----imageUri1---------" + imageUri1);
                    Log.e(TAG, "onCaptureImageResult: -------imageUri-------" + imageUri);
                    type = "out";
 if (imageUri == null) {

                        Toast.makeText(PunchInActivity.this, "Take client Photo", Toast.LENGTH_SHORT).show();
                        // openCameraIntent();
                    } else {
                        if (!time_in.equals("")) {
                            type = "out";
                            TimeInAPi(type);
                        }

                        if (time_in.equals("")) {
                            type = "in";
                            TimeInAPi(type);
                        }

                    }


                }
                break;
        }
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                if (imageReturnedIntent != null) {
                    onCaptureImageResult(imageReturnedIntent);
                } else {
                    Toast.makeText(PunchInActivity.this, "Take ID Image", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void onCaptureImageResult(Intent imageReturnedIntent) {
        Bitmap thumbnail = (Bitmap) imageReturnedIntent.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Uri selectedImageUri = getImageUri(getActivity(), thumbnail);
        //selectedPath = getRealPathFromURI(selectedImageUri);
        imageUri = Uri.fromFile(destination);
        imageUri1 = Uri.fromFile(destination);
        selectedPath = destination.getAbsolutePath();
        Log.e(TAG, "onCaptureImageResult: -------------" + imageUri1);
        Log.e(TAG, "onCaptureImageResult: --------------" + imageUri);
        type = "in";
if (imageUri1 == null) {

            Toast.makeText(PunchInActivity.this, "Take client Photo", Toast.LENGTH_SHORT).show();
            // openCameraIntent();
        } else {
            if (!time_in.equals("")) {
                type = "out";
                TimeInAPi(type);
            }

            if (time_in.equals("")) {
                type = "in";
                TimeInAPi(type);
            }
        }

        //Toast.makeText(AddClientActivity.this, destination.getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }

    LatLng latLng;

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    updateAddress(currentLocation);
                    latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Current Position");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                    try {
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(PunchInActivity.this, "No Location recorded", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    String AddressMain;
    double Latitude, Longitude;

    private void updateAddress(Location currentLocation) {
        Log.e(TAG, "updateAddress:--------- " + currentLocation.getLatitude());
        Log.e(TAG, "updateAddress:-------- " + currentLocation.getLongitude());
        Latitude = currentLocation.getLatitude();
        Longitude = currentLocation.getLongitude();
        Log.e(TAG, "Latitude:--------- " + Latitude);
        Log.e(TAG, "Longitude:-------- " + Longitude);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        if (Latitude == 0.0 && Longitude == 0.0) {
            try {
                addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                AddressMain = address + " , " + city + " , " + state + " , " + country + " , " + postalCode + " , " + knownName;
                tvAddress.setText("" + address + " , " + city
+ " , " + state + " , " + country + " , " + postalCode + " , " + knownName
);


                insertData();
                getDbData();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            try {
                addresses = geocoder.getFromLocation(Latitude, Longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                AddressMain = address + " , " + city + " , " + state + " , " + country + " , " + postalCode + " , " + knownName;
                tvAddress.setText("" + address + " , " + city
+ " , " + state + " , " + country + " , " + postalCode + " , " + knownName
);


                insertData();
                getDbData();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
Log.e(TAG, "onLocationChanged: " + location.getLatitude());
        Log.e(TAG, "onLocationChanged: " + location.getLongitude());


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        //stop location updates
        if (mGoogleApiClient != null) {
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    static final Integer CAMERA = 0x5;
    static final Integer CAMERA1 = 0x6;
    boolean current_location = false;
    String type;
    boolean click = false;
    int clickcount = 0;
    int clickcount1 = 0;
    int Strclickcount = 0;
    int Strclickcount1 = 0;
    SharedPreferences.Editor ed;
    private int REQUEST_CAMERA = 0, REQUEST_CAMERA1 = 1;
    private JSONObject json, json2;
    private SharedPreferences pref, spf_user_id;
    SharedPreferences.Editor editor1;
    private DBAdapter dbAdapter;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.TvTimeIn:
                if (ContextCompat.checkSelfPermission(PunchInActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(PunchInActivity.this,
                            Manifest.permission.CAMERA)) {
                    } else {
                        ActivityCompat.requestPermissions(PunchInActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    }
                } else {
                }


                if (ContextCompat.checkSelfPermission(PunchInActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                }
                if (ContextCompat.checkSelfPermission(PunchInActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(PunchInActivity.this,
                            Manifest.permission.CAMERA)) {
                    } else {
                        ActivityCompat.requestPermissions(PunchInActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                    }
                } else {
                    // Permission has already been granted
                }

                type = "in";
                clickcount = clickcount + 1;
                if (clickcount == 1) {

                    sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    ed = sharedpref.edit();
                    ed.putInt("clickcount", clickcount);
                    ed.putString("type", "in");

                    ed.apply();
                    ed.commit();

                    askForPermission(Manifest.permission.CAMERA, CAMERA1);
                    if (checkPermission()) {
                        cameraIntent();
                        time_in = sharedpref.getString("time_in", "");
                        time_out = sharedpref.getString("time_out", "");

                        Log.e(TAG, "onClick: imageUri1     out======" + imageUri1);
                        Log.e(TAG, "onClick: time_in     time_in======" + time_in);
                        Log.e(TAG, "onClick: time_out======" + time_out);
                        if (!time_in.equals("")) {
                            type = "out";
                            TimeInAPi(type);
                        } else if (time_in.equals("")) {
                            type = "in";
                            TimeInAPi(type);
                        }
if (imageUri1 == null) {

                            Toast.makeText(PunchInActivity.this, "Take client Photo", Toast.LENGTH_SHORT).show();
                            // openCameraIntent();
                        } else {
                            TimeInAPi(type);
                        }

                        // TvTimeout.setBackgroundResource(R.drawable.bg_white_rounded_grreen);

                    } else {
                        //requestPermission();
                    }

                } else {

                }


                break;
            case R.id.TvTimeout:
                type = "out";
                clickcount1 = clickcount1 + 1;
                if (clickcount1 == 1) {
                    sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    ed = sharedpref.edit();
                    ed.putInt("clickcount1", clickcount1);
                    ed.putString("type", "out");
                    ed.apply();
                    ed.commit();

                    askForPermission(Manifest.permission.CAMERA, CAMERA1);
                    if (checkPermission()) {
                        openCameraIntentOut();
                        Log.e(TAG, "onClick: imageUri1     out======" + imageUri1);

 if (imageUri1 == null) {

                            Toast.makeText(PunchInActivity.this, "Take client Photo", Toast.LENGTH_SHORT).show();
                            // openCameraIntent();
                        } else {
                            TimeInAPi(type);
                        }

                        // TvTimeout.setBackgroundResource(R.drawable.bg_white_rounded_grreen);

                    } else {
                        //  requestPermission();
                    }

                } else {

                }

                break;
            case R.id.linearFindMe:
                fetchLastLocation();
                break;
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private GPSTracker gps;
    private SharePreferanceWrapperSingleton objSPS;

private class Post_Punch_In_Details extends AsyncTask<String, String, String> {
        JSONArray items_list;
        JSONObject jObj = new JSONObject();
        ProgressDialog dialog = new ProgressDialog(PunchInActivity.this);
        String pTime, pLocation;

        @Override
        protected String doInBackground(String... params) {
            try {
                String punchInLocation = "";
             //   String[] text = TvTimeIn.getText().toString().split(" ");
                String[] text = TvTimeIn.getText().toString().split(" ");
                String totalTime = text[text.length - 1];
                String punchInTime = SharedPreferenceManager.instance().getValueFromShared_Pref("PunchInTime");
                String punchOutTime = SharedPreferenceManager.instance().getValueFromShared_Pref("PunchOutTime");
                String PunchInLocation = SharedPreferenceManager.instance().getValueFromShared_Pref("PunchInLocation");


                json.put("parent_id", pref.getString("parent_id", ""));
                json.put("user_id", spf_user_id.getString("userid", ""));


                // json.put("punchout_time", punchOutTime);
                json.put("punchin_time", punchInTime);

                json.put("punchin_location", PunchInLocation);


                JSONArray postjson = new JSONArray();
                postjson.put(json);
                json2.put("params", postjson);

                System.out.println(json2.toString());
            } catch (JSONException e) {

                e.printStackTrace();
            }

http://infibizapi.daffodillab.in/index.php/user/punchin?user_id=92&parent_id=5&punchin_time=2018-02-19 18:22:08&punchin_location=23.224781, 77.435979

            //String url = "http://43.225.53.248:8080/infibizcrm/rest/comp_details/getcompanydetail";
            // String url2 = getResources().getString(R.string.url) + "/infibizcrm/rest/LoginWebService/insert_punchin_details";
            String url2 = BASEURL + "user/add_bulk_track?";
            Log.e(TAG, "doInBackground:url2== " + url2);
            Log.e(TAG, "doInBackground:json2.toString()== " + json2.toString());
            String response = WebServiceConnection.getData(url2, json2.toString());

  try {
                String response = WebServiceConnection.getData(url2, json2.toString());
                jObj = new JSONObject(response);
                items_list = jObj.getJSONArray("response");
                for (int i = 0; i < items_list.length(); i++) {
                    JSONObject c = items_list.getJSONObject(i);

                    String result = c.getString("");
                    String attendenceId = c.getString("");
                }

            } catch (JSONException e) {

                e.printStackTrace();
            }

            return response;
        }

        GPSTracker gps;

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            json = new JSONObject();
            json2 = new JSONObject();


            String ctime = getCurrentTimeDate();
            String[] cDateArray = ctime.split(" ");
            String cDate = cDateArray[0];
            String yesturdayDate = getYesterdayDateString();
            pTime = ctime;
            // turnGPSOn();
            gps = new GPSTracker(getApplicationContext());
            String location = "";
            if (gps.canGetLocation()) {
                location = gps.getLatitude() + "," + gps.getLongitude();
            }
            pLocation = location;
            SharedPreferenceManager.instance().setValueToSharedPref("PunchInTime", ctime);
            SharedPreferenceManager.instance().setValueToSharedPref("PunchInLocation", location);
            ArrayList<TrackUser> arrlistTrackUser = dbAdapter.getTrackUserInfo();


            if (arrlistTrackUser.size() > 0) {
                for (int i = 0; i < arrlistTrackUser.size(); i++) {
                    TrackUser trackUser = arrlistTrackUser.get(i);
                    String date = trackUser.getDate();
                    //date = "2015-04-16";// + " " + trackUser.getTime();
                    try {
                        if (!date.equals(cDate) && !date.equals(yesturdayDate)) {
                            try {
                                dbAdapter.deleteRow("TrackUser", "date", trackUser.getDate());

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();

                    }
                }
            }

            super.onPreExecute();


        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (dialog.isShowing()) {
                    dialog.dismiss();


                    System.out.println("PunchIn Response:  " + result);
                }


                if (!result.equals("")) {

                    String json111 = new String(result);    // create JSON obj from string

                    String resp[] = json111.split("/");

                    if (!resp[0].equalsIgnoreCase("true")) {
                        if (gps.getLocation() != null) {


                            String profileType = getProfileType();
                            boolean isAirplaneMode = isAirplaneMode();
                            String operatorType = getOperatorType();
                            objSPS.setValueToSharedPref("profileType", profileType);
                            objSPS.setValueToSharedPref("isAirplaneMode", isAirplaneMode + "");
                            objSPS.setValueToSharedPref("operatorType", operatorType);

                            //startRepeatingTimer();
                            start();

                            String chronoText = "";
                            String a[] = chronoText.split(" ");
                            String time = a[a.length - 1];

                            String array[] = time.split(":");

                            GPSTracker gps = new GPSTracker(getApplicationContext());
                            String location = "";
                            if (gps.canGetLocation()) {
                                location = gps.getLatitude() + "," + gps.getLongitude();
                            }
                            //   btnPunch.setBase(SystemClock.elapsedRealtime());
                            resp[1].replace("", "");

                            SharedPreferenceManager.instance().setValueToSharedPref("attendance_id", resp[1].trim());
                            //mChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
                            SharedPreferenceManager.instance().setValueToSharedPref("status", "start");
                            String ctime = getCurrentTimeDate();
                            SharedPreferenceManager.instance().setValueToSharedPref("PunchInTime", ctime);
                            SharedPreferenceManager.instance().setValueToSharedPref("PunchInLocation", location);
                            // btnPunch.start();
                        } else {
                            gps.showAlertDialog("Settings Alert", "GPS is not enabled. Do you want to go to settings menu?", "Settings");
                        }
                    }


                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception ne) {
                ne.printStackTrace();
            }
        }
    }


    private PendingIntent pendingIntent;

    public String getOperatorType() {
        String operatorType = "";
        TelephonyManager telephonyManager = ((TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE));
        operatorType = telephonyManager.getNetworkOperatorName();
        return operatorType;
    }

    public String getProfileType() {
        String profileType = "";
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

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

    public void start() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1000 * 60 * 5;

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        //Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public void cancel() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        // Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

    public boolean isAirplaneMode() {
        boolean airplaneMode = Settings.System.getInt(this.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        return airplaneMode;
    }

    private String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    private String getCurrentTimeDate() {

        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //SimpleDateFormat formatter = new SimpleDateFormat(" hh:mm:ss aa");

        return formatter.format(calendar.getTime());
    }

    private void TimeInAPi(final String type) {
http://daffodillab.in/trackerpgpl/index.php/v1/user/attendence

        //user_id,address,image,latitude,longitude,company_id,type,current_location
        String urlData = BASEURL + "user/attendence";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setCancelable(false);
        pDialog.setContentView(R.layout.custom_progress_bar);
        Log.e(TAG, "TimeInAPi: urlData======" + urlData);

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, urlData,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultResponse = new String(response.data);
                        try {

                            JSONObject result = new JSONObject(resultResponse);
                            //int res = result.getInt("RESPONSECODE");
                            Log.e(TAG, "onResponse:=== " + response.toString());
                            System.out.println("JSON RETURN " + response.toString());
                            String message = String.valueOf(result.get("message"));
                            int status = result.getInt("status");
                            String response1 = String.valueOf(result.get("data"));

                            Log.e(TAG, "message:=== " + message);

                            if (status == 0) {

                                imageUri1 = Uri.parse("");
                                imageUri = Uri.parse("");
if (type.equals("in")) {
                                    TvTimeIn.setClickable(false);
                                    TvTimeout.setClickable(true);
                                    TvTimeIn.setEnabled(false);
                                    TvTimeout.setEnabled(true);
                                    TvTimeIn.setBackgroundResource(R.drawable.bg_white_rounded_grreen);

                                } else {
                                    TvTimeout.setEnabled(false);
                                    TvTimeIn.setEnabled(false);
                                    TvTimeIn.setClickable(false);
                                    TvTimeout.setClickable(false);
                                    TvTimeout.setBackgroundResource(R.drawable.bg_white_rounded_grreen);

                                }

                                if (!time_in.equals("")) {
                                    TvTimeIn.setText("Time Out");
                                    TvTimeIn.setBackgroundResource(R.drawable.bg_white_rounded_grreen);
                                    TvTimeIn.setEnabled(true);
                                    TvTimeIn.setClickable(true);
                                }
                                if (!time_out.equals("")) {
                                    TvTimeIn.setText("Time In");
                                    TvTimeout.setBackgroundResource(R.drawable.bg_white_rounded_grreen);
                                    TvTimeout.setEnabled(false);
                                    TvTimeout.setClickable(false);
                                }
 if (time_in.equals("")) {
                                    TvTimeIn.setBackgroundResource(R.drawable.bg_white_rounded_gray);
                                    TvTimeIn.setEnabled(true);
                                    TvTimeIn.setClickable(true);
                                }
                                if (time_out.equals("")) {
                                    TvTimeout.setBackgroundResource(R.drawable.bg_white_rounded_gray);
                                    TvTimeout.setEnabled(true);
                                    TvTimeout.setClickable(true);
                                }

                                pDialog.dismiss();
                                // String respo = result.getString("RESPONSE");
                                Toast.makeText(PunchInActivity.this, message, Toast.LENGTH_SHORT).show();
                                getStatusAPi();
                                Intent intentSplash = new Intent(PunchInActivity.this, Main2Activity.class);
                                startActivity(intentSplash);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();

                            } else {
                                pDialog.dismiss();
                                // cleareText();
                                Toast.makeText(PunchInActivity.this, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //sendError(e.toString(), "parking/checkout_lost");
                            pDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                        // sendError(errorMessage, "parking/checkout_lost");

                        pDialog.dismiss();
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                        //sendError(errorMessage, "parking/checkout_lost");

                        pDialog.dismiss();
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                            //   sendError(errorMessage, "parking/checkout_lost");
                            pDialog.dismiss();
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                            //  sendError(errorMessage, "parking/checkout_lost");
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                            //  sendError(errorMessage, "parking/checkout_lost");
                            pDialog.dismiss();
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                            //  sendError(errorMessage, "parking/checkout_lost");
                            pDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        pDialog.dismiss();
                        // sendError(e.toString(), "parking/checkout_lost");
                        e.printStackTrace();
                    }
                }
                Log.i(TAG, "Error" + errorMessage);
                pDialog.dismiss();
                error.printStackTrace();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Log.e(TAG, "getParams: ==666====" + gps.getLatitude());
                Log.e(TAG, "getParams: ====666====" + gps.getLongitude());
                Log.e(TAG, "getParams: ====666====" + AddressMain);
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("address", AddressMain);
                params.put("latitude", String.valueOf(gps.getLatitude()));
                params.put("longitude", String.valueOf(gps.getLongitude()));
                params.put("company_id", company_id);
                params.put("type", type);
                params.put("current_location", String.valueOf(isCurrent_location));

                Log.e(TAG, "getParams:params " + params);
//                params.put("operatorId", agentId);
//                params.put("vendorId", vendorId);


                //.addFileToUpload(path, "file") //Adding file
                return params;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {

                Map<String, DataPart> params = new HashMap<>();
                String uploadId = UUID.randomUUID().toString();
                try {
                    InputStream iStream = getApplicationContext().getContentResolver().openInputStream(imageUri1);
                    byte[] inputData = getBytes(iStream);
                    params.put("image", new DataPart(uploadId + ".jpg", inputData, "image/*"));

                    Log.e(TAG, "getParams:params " + params);
//
                } catch (IOException e) {
                    e.printStackTrace();
                    // sendError(e.toString(), "parking/checkout_lost");
                }
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);


    }


    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(PunchInActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(PunchInActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(PunchInActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(PunchInActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }


    private static final int REQUEST_CAPTURE_IMAGE = 100;

    private void openCameraIntent() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }

    private void openCameraIntentOut() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 1);
    }


    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }


}
*/
