package com.daffodil.mymap.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daffodil.mymap.R;
import com.daffodil.mymap.other.base.AppConstants;
import com.daffodil.mymap.other.base.AppController;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ClientDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ClientDetailsActivity";
    ImageView ClientImage;
    TextView ClientName, ClientEmail, ClientNumber, ClientStatus, ClientDetails;
    String user_id, Client_id, Image, data;
    String Strimage, Strname, Strcontact_no, Strdescription, type_id, Stremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);
        initView();
        getDetailsApi();
    }

    private void initView() {

        Intent intent = getIntent();
        String ClientListActivity = intent.getStringExtra("ClientListActivity");
        data = intent.getStringExtra("Name");
        Image = intent.getStringExtra("Image");
        Client_id = intent.getStringExtra("Client_id");
        user_id = intent.getStringExtra("user_id");
        Log.e(TAG, "initVew: user_id" + user_id);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Client Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ClientImage = findViewById(R.id.ClientImage);
        ClientDetails = findViewById(R.id.ClientDetails);
        ClientName = findViewById(R.id.ClientName);
        ClientNumber = findViewById(R.id.ClientNumber);
        ClientEmail = findViewById(R.id.ClientEmail);
        ClientStatus = findViewById(R.id.ClientStatus);
    }

    private void getDetailsApi() {

        String URL = AppConstants.BASEURL + "client/detail?client_id=" + Client_id;

        final ProgressDialog pDialog = new ProgressDialog(ClientDetailsActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);

        Log.e(TAG, "getDetailsApi:urlData=== " + URL);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());

                try {
                    // System.out.println("JSON RETURN " + response.toString());

                    String data = String.valueOf(response.get("data"));
                    String message = String.valueOf(response.get("message"));
                    int status = response.getInt("status");

                    if (status == 0) {
                        final JSONObject arrayObj = new JSONObject(data);
                        Strname = arrayObj.getString("name");
                        Strimage = arrayObj.getString("image");
                        Strcontact_no = arrayObj.getString("contact_no");
                        Stremail = arrayObj.getString("email");
                        type_id = arrayObj.getString("type_id");
                        Strdescription = arrayObj.getString("description");

                        Log.e(TAG, "getDetailsApi: " + arrayObj.getString("name"));
                        Log.e(TAG, "getDetailsApi: " + arrayObj.getString("image"));
                        Log.e(TAG, "getDetailsApi: " + arrayObj.getString("contact_no"));
                        Log.e(TAG, "getDetailsApi: " + arrayObj.getString("email"));
                        try {
                            ClientName.setText("Client Name :     "+Strname);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            ClientNumber.setText("Client Number :     "+Strcontact_no);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            ClientEmail.setText("Client Email :     "+Stremail);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            ClientDetails.setText("Client Details :     "+Strdescription);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (Integer.parseInt(type_id) == 1) {
                                ClientStatus.setText("Client Status :     Intrested");
                            } else if (Integer.parseInt(type_id) == 2) {
                                ClientStatus.setText("Client Status :     Not intrested");
                            } else if (Integer.parseInt(type_id) == 3) {
                                ClientStatus.setText("Client Status :     Schedule for next visit");
                            } else {
                                ClientStatus.setText("Client Status :     Schedule for followup");
                            }

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        try {
                            Picasso.with(ClientDetailsActivity.this).load(Strimage).into(ClientImage);
                        } finally {

                        }

                        pDialog.dismiss();


                    } else {
                        Toast.makeText(ClientDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(ClientDetailsActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(ClientDetailsActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
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
