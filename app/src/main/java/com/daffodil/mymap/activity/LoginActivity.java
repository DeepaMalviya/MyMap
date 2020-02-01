package com.daffodil.mymap.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daffodil.mymap.R;
import com.daffodil.mymap.model.LoginModel;
import com.daffodil.mymap.other.base.AppConstants;
import com.daffodil.mymap.other.base.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.daffodil.mymap.other.base.AppConstants.BASEURL;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_READ_PHONE_STATE =1 ;
    private EditText editText;
    Button button;
    String imei, urlData;
    SharedPreferences sharedpref;
    SharedPreferences.Editor ed;
    private TelephonyManager mTelephonyManager;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (AppConstants.isInternetAvailable(LoginActivity.this)) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

            if (checkAndRequestPermissions()) {
                Log.e(TAG, "onCreate: checkAndRequestPermissions");
            }else {
                Log.e(TAG, "onCreate: checkAndRequestPermissions");
            }
            initView();
        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.NETWORK_ERROR), Toast.LENGTH_SHORT).show();

        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getDeviceImei();
        }
        if (grantResults.length == 0 || grantResults == null) {
            /*If result is null*/
        } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            /*If We accept permission*/
        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            /*If We Decline permission*/
        }
    }
    @SuppressLint("NewApi")
    private void getDeviceImei() {

        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        imei = mTelephonyManager.getDeviceId();
        Log.e("msg", "DeviceImei ==========" + imei);
    }
    private boolean checkAndRequestPermissions() {
        int permissionReadPhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int permissionProcessOutGogingCalls = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionProcessReadContacts = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionProcessReadCallLog = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        int permissionWriteStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionReadStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionReadPhoneState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (permissionProcessOutGogingCalls != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionProcessReadContacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (permissionProcessReadCallLog != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionReadStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void initView() {

        editText = findViewById(R.id.editTextMobile);
        button = findViewById(R.id.buttonContinuew);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = editText.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    editText.setError("Valid number is required");
                    editText.requestFocus();
                    return;
                }

                String phoneNumber = number;
                validate();


            }
        });
    }

    private void validate() {
        final String username = editText.getText().toString();

        if (AppConstants.isBlank(username)) {
            AppConstants.showToast(LoginActivity.this, getString(R.string.mobile_is_required));
        } else {
            loginSevice(username);


        }
    }

    private void loginSevice(final String username) {
        final ProgressDialog pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        Log.e(TAG, "loginSevice: imei====" + imei);
        if (imei != null && !imei.equals("")) {
            urlData = BASEURL + "user/sendotp?mobile=" + username + "&imei=" + imei;
        } else {
            urlData = BASEURL + "user/sendotp?mobile=" + username + "&imei=";
        }
        Log.e(TAG, "loginSevice:urlData=== " + urlData);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlData, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());

                try {
                    System.out.println("JSON RETURN " + response.toString());

                    String data = String.valueOf(response.get("data"));
                    String message = String.valueOf(response.get("message"));
                    int status = response.getInt("status");

                    if (status == 0) {
                        Toast.makeText(LoginActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        JSONObject loginresponce = response.getJSONObject("data");
                        editText.setText("");


                        LoginModel loginModel = new LoginModel();

                        loginModel.setMobile(loginresponce.getString("mobile"));
                        loginModel.setOtp(loginresponce.getString("otp"));
                        loginModel.setUserId(loginresponce.getString("user_id"));


                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("mobile", loginModel.getMobile());
                        ed.putString("user_id", loginModel.getUserId());
                        ed.putString("otp", loginModel.getOtp());

                        ed.apply();
                        ed.commit();
                        String userRole = "Normal Agent";

                        Intent intentTow = new Intent(LoginActivity.this, VerifyOtpActivity.class);
                        intentTow.putExtra("mobile", loginModel.getMobile());
                        intentTow.putExtra("user_id", loginModel.getUserId());
                        intentTow.putExtra("otp", loginModel.getOtp());
                        startActivity(intentTow);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();


                        pDialog.dismiss();


                    } else {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(LoginActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(LoginActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Time Out");
                    builder.setPositiveButton("ReTry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (AppConstants.isInternetAvailable(LoginActivity.this)) {
                                loginSevice(username);
                            } else {
                                Toast.makeText(LoginActivity.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }

                // hide the progress dialog
                pDialog.dismiss();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

}
