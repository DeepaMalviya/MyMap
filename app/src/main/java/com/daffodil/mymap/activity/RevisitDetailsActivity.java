package com.daffodil.mymap.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daffodil.mymap.R;
import com.daffodil.mymap.other.CustomRequest;
import com.daffodil.mymap.other.GPSTracker;
import com.daffodil.mymap.other.base.AppConstants;
import com.daffodil.mymap.other.base.AppController;
import com.daffodil.mymap.other.dto.ChoiceModel;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class RevisitDetailsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "RevisitDetailsActivity";
    Button btnStart, btnEnd, btnSubmitRevisit;
    EditText textViewwRe, textViewNameRE, textViewContactRe, textViewEmailRE;
    CheckBox checkOneRE, checkTwoRE, checkThreeRE, checkFourRE;
    Spinner spinner;
    LinearLayout linearEnd, lineartvDetails;
    ChoiceModel choiceModel;
    String StrNname, Strchoice_id;
    ArrayList<ChoiceModel> choiceModelList = new ArrayList<>();
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> listid = new ArrayList<>();
    String choice_id;
    SharedPreferences sharedpref;
    SharedPreferences.Editor ed;
    String mobile, user_id, otp, user_role, user_name, company_id, company_name;
    String type = "";
    String strDate;
    private static final int LOCATION_REQUEST_CODE = 101;
    TextView tvDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revisit_details);
        if (ActivityCompat.checkSelfPermission(RevisitDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RevisitDetailsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
        intiView();
        getOPtions();
        getStatusOptionCheck();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        strDate = sdf.format(c.getTime());
        Log.e("Date", "DATE : ===" + strDate);

    }

    boolean start_disable, end_disable;

    private void getStatusOptionCheck() {
        String URL = AppConstants.BASEURL + "client/meeting_status?client_id=" + Client_id + "&user_id=" + user_id;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "JSON RETURN===getStatusOptionCheck========= " + response.toString());

                try {
                    System.out.println("JSON RETURN " + response.toString());

                    String data = String.valueOf(response.get("data"));
                    String message = String.valueOf(response.get("message"));
                    int status = response.getInt("status");
                    if (status == 0) {
                        final JSONObject arrayObj = new JSONObject(data);

                        // JSONObject jsonObject = arrayObj.getJSONObject(data);
                        start_disable = arrayObj.getBoolean("start_disable");
                        end_disable = arrayObj.getBoolean("end_disable");
                        Log.e(TAG, "onResponse:====getStatusOptionCheck=== " + start_disable);
                        Log.e(TAG, "onResponse:====getStatusOptionCheck==== " + end_disable);
                        if (!start_disable) {
                            btnStart.setBackground(getResources().getDrawable(R.drawable.bg_white_rounded_blue));
                            btnEnd.setBackground(getResources().getDrawable(R.drawable.bg_white_rounded_gray));
                            btnStart.setEnabled(true);
                            btnEnd.setEnabled(false);
                        }/* else {
                            btnStart.setBackground(getResources().getDrawable(R.drawable.bg_white_rounded_blue));
                            btnEnd.setBackground(getResources().getDrawable(R.drawable.bg_white_rounded_gray));
                            btnEnd.setEnabled(false);
                        }*/
                        if (!end_disable) {
                            btnEnd.setBackground(getResources().getDrawable(R.drawable.bg_white_rounded_blue));
                            btnStart.setBackground(getResources().getDrawable(R.drawable.bg_white_rounded_gray));
                            btnStart.setEnabled(false);
                            btnEnd.setEnabled(true);
                        } /*else {
                            btnEnd.setBackground(getResources().getDrawable(R.drawable.bg_white_rounded_gray));
                            btnStart.setBackground(getResources().getDrawable(R.drawable.bg_white_rounded_blue));
                            btnEnd.setEnabled(false);
                        }*/
                    } else {
                        Toast.makeText(RevisitDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(RevisitDetailsActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(RevisitDetailsActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                //com.android.volley.TimeoutError
                String er = "com.android.volley.TimeoutError";
                // com.android.volley.TimeoutError


                // hide the progress dialog
                String message = null;
                if (error instanceof NetworkError) {
                    //sendError(error.toString(), "user/login?username=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }


    private void UpdateMeettingAPi(final String type) {
        Map<String, String> MyData = new HashMap<String, String>();
        if (type.equals("start")) {
            MyData.put("user_id", user_id);
            MyData.put("client_id", Client_id);
            MyData.put("latitude", String.valueOf(mLastLocation.getLatitude()));
            MyData.put("longitude", String.valueOf(mLastLocation.getLongitude()));
            MyData.put("case", type);
            MyData.put("datetime", strDate);

        } else {
            MyData.put("user_id", user_id);
            MyData.put("client_id", Client_id);
            MyData.put("latitude", String.valueOf(mLastLocation.getLatitude()));
            MyData.put("longitude", String.valueOf(mLastLocation.getLongitude()));
            MyData.put("case", type);
            MyData.put("datetime", strDate);

            MyData.put("name", textViewNameRE.getText().toString().trim());
            MyData.put("email", textViewEmailRE.getText().toString().trim());
            MyData.put("contact_no", textViewContactRe.getText().toString().trim());
            MyData.put("remark", textViewwRe.getText().toString().trim());
            MyData.put("type_id", choice_id);

        }


        Log.e(TAG, "UpdateMeettingAPi updtateApi: " + MyData);

        String URL = AppConstants.BASEURL + "client/meeting";
        Log.e(TAG, "UpdateMeettingAPi updtateApi: " + URL);

        final ProgressDialog pDialog = new ProgressDialog(RevisitDetailsActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        try {

            Response.Listener<JSONObject> reponseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    processJsonObjectCheckIn(jsonObject, type);
                    pDialog.dismiss();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("RESPONSE ERROR", volleyError.toString());
                    Toast.makeText(RevisitDetailsActivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getActivity(), "error==>  " + volleyError.toString(), Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                }
            };
            CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL, MyData, reponseListener, errorListener);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsObjRequest);
        } catch (Exception e) {
            Log.e("RESPONSE ERROR", e.toString());
            VolleyLog.d("RESPONSE ERROR", e.toString());
            pDialog.dismiss();
        }


    }

    private void processJsonObjectCheckIn(JSONObject response, String type) {

        if (response != null) {
            Log.e(TAG, "Response UpdateMeettingAPi" + response + "");

            try {
                String data = String.valueOf(response.get("data"));
                String message = String.valueOf(response.get("message"));
                int status = response.getInt("status");
                // String responce = json.getJSONArray("RESPONSE");
                if (status == 0) {
                    Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                    if (type.equals("start")) {

                    } else {
                        finish();
                    }

                } else {
                    Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e) {
            }
        }

    }

    private void getOPtions() {

        String URL = AppConstants.BASEURL + "client/visit_type_list";
        Log.e(TAG, "getOPtions: visit_type_list" + URL);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());

                try {
                    // System.out.println("JSON RETURN " + response.toString());

                    String data = String.valueOf(response.get("data"));
                    String message = String.valueOf(response.get("message"));
                    int status = response.getInt("status");
                    int total = response.getInt("total");
                    //addRadioButtons(total,jsonObject.getString("choice_id"),jsonObject.getString("name"));

                    if (status == 0) {
                        final JSONArray arrayObj = new JSONArray(data);
                        for (int i = 0; i < arrayObj.length(); i++) {

                            JSONObject jsonObject = arrayObj.getJSONObject(i);
                            choiceModel = new ChoiceModel();
                            StrNname = jsonObject.getString("name");

                            Strchoice_id = jsonObject.getString("type_id");
                            //choiceModel.setChoice_id(Strchoice_id);
                            choiceModel.setName(StrNname);
                            choiceModelList.add(choiceModel);
                            list.add(StrNname);
                            listid.add(Strchoice_id);
                            // addRadioButtons(choiceModelList.size(), choiceModel);


                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RevisitDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner.setAdapter(adapter);
                        ;
                        //  Toast.makeText(UpdateActivity.this, ""+spinner.getSelectedItem(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RevisitDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(RevisitDetailsActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(RevisitDetailsActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                //com.android.volley.TimeoutError
                String er = "com.android.volley.TimeoutError";
                // com.android.volley.TimeoutError


                // hide the progress dialog
                String message = null;
                if (error instanceof NetworkError) {
                    //sendError(error.toString(), "user/login?username=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    String Activity, Image, Name, Client_id;
    private GPSTracker gps;

    private void intiView() {
        gps = new GPSTracker(getApplicationContext());
        GPSTracker gps = new GPSTracker(getApplicationContext());
        String location = "";
        if (gps.canGetLocation()) {
            Log.e(TAG, "intiView:location=== " + location);
            location = gps.getLatitude() + "," + gps.getLongitude();
        }
        //
        Intent intent = getIntent();
        Client_id = intent.getStringExtra("Client_id");
        Log.e(TAG, "intiView:Client_id=== " + Client_id);
        Name = intent.getStringExtra("Name");
        Image = intent.getStringExtra("Image");
        Activity = intent.getStringExtra("Activity");

        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
        mobile = sharedpref.getString("mobile", "");
        user_id = sharedpref.getString("user_id", "");
        otp = sharedpref.getString("otp", "");
        user_role = sharedpref.getString("user_role", "");
        user_name = sharedpref.getString("user_name", "");
        company_id = sharedpref.getString("company_id", "");
        company_name = sharedpref.getString("company_name", "");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Activity.equals("Followup")) {
            getSupportActionBar().setTitle("Followup");
        } else {
            getSupportActionBar().setTitle("Revisit ");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnStart = findViewById(R.id.btnStart);
        linearEnd = findViewById(R.id.linearEnd);
        lineartvDetails = findViewById(R.id.lineartvDetails);
        linearEnd.setVisibility(View.GONE);

        tvDetails = findViewById(R.id.tvDetails);
        btnEnd = findViewById(R.id.btnEnd);
        btnSubmitRevisit = findViewById(R.id.btnSubmitRevisit);
        textViewwRe = findViewById(R.id.textViewwRe);
        textViewNameRE = findViewById(R.id.textViewNameRE);
        textViewContactRe = findViewById(R.id.textViewContactRe);
        textViewEmailRE = findViewById(R.id.textViewEmailRE);
        checkOneRE = findViewById(R.id.checkOneRE);
        checkTwoRE = findViewById(R.id.checkTwoRE);
        checkThreeRE = findViewById(R.id.checkThreeRE);
        checkFourRE = findViewById(R.id.checkFourRE);
        spinner = findViewById(R.id.spinnerr);
        spinner.setOnItemSelectedListener(this);
        latestRemarkApi();
        btnSubmitRevisit.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnEnd.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
            }
        } else {
            buildGoogleApiClient();
        }


    }

    private void latestRemarkApi() {
        String URL = AppConstants.BASEURL + "client/latest_remark?client_id=" + Client_id + "&user_id=" + user_id;
        Log.e(TAG, "latestRemarkApi:============== " + URL);
        final ProgressDialog pDialog = new ProgressDialog(RevisitDetailsActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);

        Log.e(TAG, "loginSevice:urlData=== " + URL);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
                Log.e(TAG, "loginSevice:response.toString()=== " + response.toString());

                try {
                    // System.out.println("JSON RETURN " + response.toString());

                    String data = String.valueOf(response.get("data"));
                    String message = String.valueOf(response.get("message"));
                    int status = response.getInt("status");

                    // Toast.makeText(RevisitDetailsActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    if (status == 0) {
                        final JSONObject arrayObj = new JSONObject(data);


                        //   Toast.makeText(DetailActivity.this, message, Toast.LENGTH_SHORT).show();

                      /*  for (int i = 0; i < arrayObj.length(); i++) {
*/
                        //JSONObject jsonObject = arrayObj.getJSONObject();
                        if (!arrayObj.getString("remark").equals("")) {
                            tvDetails.setText("" + arrayObj.getString("remark"));

                        } else {
                            lineartvDetails.setVisibility(View.GONE);
                        }

                        // modelList.add(loginModel);
                      /*  }
*/

                        pDialog.dismiss();


                    } else {
                        Toast.makeText(RevisitDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(RevisitDetailsActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(RevisitDetailsActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                //com.android.volley.TimeoutError
                String er = "com.android.volley.TimeoutError";

                // com.android.volley.TimeoutError
                if (er.equals("com.android.volley.TimeoutError")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(RevisitDetailsActivity.this);
                    builder.setMessage("Time Out");
                    builder.setPositiveButton("ReTry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (AppConstants.isInternetAvailable(RevisitDetailsActivity.this)) {
                                // loginSevice(username);
                            } else {
                                Toast.makeText(RevisitDetailsActivity.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    //  sendError(error.toString(), "user/login?username=");
                    Toast.makeText(RevisitDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }

                // hide the progress dialog
                pDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    //sendError(error.toString(), "user/login?username=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    // sendError(error.toString(), "user/login?username=");

                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    // sendError(error.toString(), "user/login?username=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof NoConnectionError) {
                    //sendError(error.toString(), "user/login?username=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {
                    //  sendError(error.toString(), "user/login?username=");

                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    //  sendError(error.toString(), "user/login?username=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    public void updateSubmitRevisit(View view) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmitRevisit:
                /*if (textViewNameRE.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Please Enter Company Name", Toast.LENGTH_SHORT).show();
                } else if (textViewContactRe.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Please Enter Contact Number", Toast.LENGTH_SHORT).show();
                } else if (textViewNameRE.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                } else*/
                if (textViewwRe.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Please Enter description", Toast.LENGTH_SHORT).show();
                } else if (textViewwRe.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Please Enter description", Toast.LENGTH_SHORT).show();
                } else {

                    UpdateMeettingAPi(type);

                    // finish();
                }
                break;
            case R.id.btnStart:
                linearEnd.setVisibility(View.GONE);
                type = "start";
                UpdateMeettingAPi(type);
                getStatusOptionCheck();
                latestRemarkApi();
                break;
            case R.id.btnEnd:
                linearEnd.setVisibility(View.VISIBLE);
                type = "end";

                getStatusOptionCheck();
                latestRemarkApi();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            String item = parent.getItemAtPosition(position).toString();
            choice_id = listid.get(position);
            Log.e(TAG, "onItemSelected: " + choice_id);
            Log.e(TAG, "onItemSelected: ====" + item);
        } catch (Exception e) {
            choice_id = listid.get(1);
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    Location mLastLocation;

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;


    }
}
