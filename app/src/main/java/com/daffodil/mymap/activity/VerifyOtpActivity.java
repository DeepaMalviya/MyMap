package com.daffodil.mymap.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daffodil.mymap.R;
import com.daffodil.mymap.model.VerifyOTP;
import com.daffodil.mymap.other.base.AppConstants;
import com.daffodil.mymap.other.base.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import static com.daffodil.mymap.other.base.AppConstants.BASEURL;

public class VerifyOtpActivity extends AppCompatActivity {
    private static final String TAG = "VerifyOtpActivity";
    private ProgressBar progressBar;
    private EditText editText;
    String code;
    Intent intent;
    String StrMobile, StrUserId, StrOtp;
    SharedPreferences sharedpref;
    SharedPreferences.Editor ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        getIntentData();
        initApp();

    }

    private void getIntentData() {
        intent = getIntent();
        StrMobile = intent.getStringExtra("mobile");
        StrUserId = intent.getStringExtra("user_id");
        StrOtp = intent.getStringExtra("otp");


    }

    private void initApp() {
        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);
        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 4) {

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
        findViewById(R.id.buttonResend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ResendOTP();
            }
        });
    }

    private void ResendOTP() {
        final ProgressDialog pDialog = new ProgressDialog(VerifyOtpActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        String urlData = BASEURL + "user/resendotp?mobile=" + StrMobile + "&user_id=" + StrUserId;
        Log.e(TAG, "verifyOtpApi: " + urlData);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlData, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
                try {

                    System.out.println("JSON RETURN " + response.toString());

                    Log.e(TAG, "onResponse: "+"JSON RETURN " + response.toString() );
                    String data = String.valueOf(response.get("data"));

                    String message = String.valueOf(response.get("message"));
                    int status = response.getInt("status");

                    if (status == 0) {
                        Toast.makeText(VerifyOtpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        JSONObject loginresponce = response.getJSONObject("data");

                        pDialog.dismiss();

                        VerifyOTP verifyOTP = new VerifyOTP();

                        verifyOTP.setMobile(loginresponce.getString("mobile"));
                        // verifyOTP.setOtp(loginresponce.getString("otp"));
                        verifyOTP.setUser_id(loginresponce.getString("user_id"));
                        verifyOTP.setUser_role(loginresponce.getString("user_role"));
                        verifyOTP.setUser_name(loginresponce.getString("user_name"));
                        verifyOTP.setCompany_id(loginresponce.getString("company_id"));
                        verifyOTP.setCompany_name(loginresponce.getString("company_name"));
                        Log.e(TAG, "onResponse: otp=========="+loginresponce.getString("otp") );



                    } else {
                        pDialog.dismiss();
                        Toast.makeText(VerifyOtpActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                //com.android.volley.TimeoutError
                String er = "com.android.volley.TimeoutError";



                // hide the progress dialog
                pDialog.dismiss();
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

    private void verifyCode(String code) {
        if (code.equals(StrOtp)) {
            if (AppConstants.isInternetAvailable(VerifyOtpActivity.this)) {
                verifyOtpApi();
            } else {
                Toast.makeText(VerifyOtpActivity.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyOtpApi() {

        final ProgressDialog pDialog = new ProgressDialog(VerifyOtpActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        String urlData = BASEURL + "/user/verifyotp?mobile=" + StrMobile + "&otp=" + code + "&user_id=" + StrUserId;
        Log.e(TAG, "verifyOtpApi: " + urlData);
        Log.e(TAG, "verifyOtpApi:StrOtp " + code);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlData, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());
                try {

                    System.out.println("JSON RETURN " + response.toString());


                    String data = String.valueOf(response.get("data"));

                    String message = String.valueOf(response.get("message"));
                    int status = response.getInt("status");

                    if (status == 0) {
                        Toast.makeText(VerifyOtpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        JSONObject loginresponce = response.getJSONObject("data");

                        pDialog.dismiss();

                        VerifyOTP verifyOTP = new VerifyOTP();

                        verifyOTP.setMobile(loginresponce.getString("mobile"));
                        // verifyOTP.setOtp(loginresponce.getString("otp"));
                        verifyOTP.setUser_id(loginresponce.getString("user_id"));
                        verifyOTP.setUser_role(loginresponce.getString("user_role"));
                        verifyOTP.setUser_name(loginresponce.getString("user_name"));
                        verifyOTP.setCompany_id(loginresponce.getString("company_id"));
                        verifyOTP.setCompany_name(loginresponce.getString("company_name"));


                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("mobile", verifyOTP.getMobile());
                        ed.putString("user_id", verifyOTP.getUser_id());
                        // ed.putString("otp", verifyOTP.getOtp());
                        ed.putString("user_role", verifyOTP.getUser_role());
                        ed.putString("user_name", verifyOTP.getUser_name());
                        ed.putString("company_id", verifyOTP.getCompany_id());
                        ed.putString("company_name", verifyOTP.getCompany_name());

                        ed.apply();
                        ed.commit();

                        Intent intentTow = new Intent(VerifyOtpActivity.this, MainActivity.class);
                        intentTow.putExtra("mobile", verifyOTP.getMobile());
                        intentTow.putExtra("user_id", verifyOTP.getUser_id());
                        // intentTow.putExtra("otp", verifyOTP.getOtp());
                        intentTow.putExtra("user_role", verifyOTP.getUser_role());
                        intentTow.putExtra("user_name", verifyOTP.getUser_name());
                        intentTow.putExtra("company_id", verifyOTP.getCompany_id());
                        intentTow.putExtra("company_name", verifyOTP.getCompany_name());
                        startActivity(intentTow);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        finish();


                    } else {
                        pDialog.dismiss();
                        Toast.makeText(VerifyOtpActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                //com.android.volley.TimeoutError
                String er = "com.android.volley.TimeoutError";


                // hide the progress dialog
                pDialog.dismiss();
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
}
