package com.daffodil.mymap.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daffodil.mymap.R;
import com.daffodil.mymap.other.CustomRequest;
import com.daffodil.mymap.other.base.AppConstants;
import com.daffodil.mymap.other.base.AppController;
import com.daffodil.mymap.other.dto.ChoiceModel;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "UpdateActivity";
    EditText textView, textViewName, textViewContactNo, textViewEmail;
    CheckBox checkboxOne, checkboxTwo, checkboxThree, checkboxFour;
    Button btnSubmitUpdate;
    ImageView imageADDClient;
    String user_id, Client_id, Image, data;
    Spinner spinner;
    String StrNname, Strchoice_id;
    ChoiceModel choiceModel;
    ArrayList<ChoiceModel> choiceModelList = new ArrayList<>();
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> listid = new ArrayList<>();
    String choice_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initVew();

    }

    private void initVew() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnSubmitUpdate = findViewById(R.id.btnSubmitUpdate);
        spinner = findViewById(R.id.spinner);
        btnSubmitUpdate.setOnClickListener(this);
        checkboxFour = findViewById(R.id.checkboxFour);
        checkboxThree = findViewById(R.id.checkboxThree);
        checkboxTwo = findViewById(R.id.checkboxTwo);
        checkboxOne = findViewById(R.id.checkboxOne);
        imageADDClient = findViewById(R.id.imageADDClient);

        textViewEmail = findViewById(R.id.textViewEmail);
        textViewContactNo = findViewById(R.id.textViewContactNo);
        textViewName = findViewById(R.id.textViewName);
        textView = findViewById(R.id.textVieww);
        getOPtions();

        spinner.setOnItemSelectedListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Client");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        String ClientListActivity = intent.getStringExtra("ClientListActivity");
        data = intent.getStringExtra("Name");
        Image = intent.getStringExtra("Image");
        Client_id = intent.getStringExtra("Client_id");
        user_id = intent.getStringExtra("user_id");
        Log.e(TAG, "initVew: user_id" + user_id);
        getSupportActionBar().setTitle(data);
        // textView.setText(data);
        textViewName.setText(data);
        Picasso.with(UpdateActivity.this).load(Image).into(imageADDClient);
        if (ClientListActivity.equals("ClientListActivity")) {
            btnSubmitUpdate.setVisibility(View.INVISIBLE);
            btnSubmitUpdate.setEnabled(false);
            textViewName.setEnabled(false);
            textViewEmail.setEnabled(false);
            textViewContactNo.setEnabled(false);
            textViewName.setEnabled(false);
            textView.setEnabled(false);
        } else {
            btnSubmitUpdate.setVisibility(View.VISIBLE);
            btnSubmitUpdate.setEnabled(true);
            btnSubmitUpdate.setOnClickListener(this);
            textViewName.setEnabled(true);
            textViewEmail.setEnabled(true);
            textViewContactNo.setEnabled(true);
            textViewName.setEnabled(true);
            textView.setEnabled(true);
        }
        getDetailsApi();

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
        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getOPtions() {

        String URL = AppConstants.BASEURL + "client/visit_type_list";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
                list.clear();
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

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateActivity.this, android.R.layout.simple_spinner_dropdown_item, list);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spinner.setAdapter(adapter);

                        //  Toast.makeText(UpdateActivity.this, ""+spinner.getSelectedItem(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UpdateActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(UpdateActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(UpdateActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
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

    String Strimage, Strname, Strcontact_no, Strdescription, type_id, Stremail;
    RadioButton rdbtn;


    private void getDetailsApi() {

        String URL = AppConstants.BASEURL + "client/detail?client_id=" + Client_id;

        final ProgressDialog pDialog = new ProgressDialog(UpdateActivity.this);
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
                            textViewName.setText(Strname);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            textViewContactNo.setText(Strcontact_no);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            textViewEmail.setText(Stremail);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            textView.setText(Strdescription);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            spinner.setSelection(Integer.parseInt(type_id));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        try {
                            Picasso.with(UpdateActivity.this).load(Strimage).into(imageADDClient);
                        } finally {

                        }

                        pDialog.dismiss();


                    } else {
                        Toast.makeText(UpdateActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(UpdateActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(UpdateActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateSubmit(View view) {
        finish();
    }

    String companyName, Email, contactNumber, description, Name;
    boolean one, two, three, four;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSubmitUpdate:
                Log.e(TAG, "onClick: " );
                companyName = textViewName.getText().toString().trim();
                Email = textViewEmail.getText().toString().trim();
                contactNumber = textViewContactNo.getText().toString().trim();
                Name = textViewName.getText().toString().trim();
                description = textView.getText().toString().trim();

                if (textViewName.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Please Enter Company Name", Toast.LENGTH_SHORT).show();
                } else if (textViewContactNo.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Please Enter Contact Number", Toast.LENGTH_SHORT).show();
                } else if (textViewName.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                } else if (choice_id.equals("")) {
                    Toast.makeText(this, "Please select Type", Toast.LENGTH_SHORT).show();
                }  else {

                    updtateApi();

                    // finish();
                }
                break;
        }
    }

    private void updtateApi() {
        Map<String, String> MyData = new HashMap<String, String>();

        MyData.put("user_id", user_id);
        MyData.put("name", textViewName.getText().toString().trim());
        MyData.put("email", textViewEmail.getText().toString().trim());
        MyData.put("contact_no", textViewContactNo.getText().toString().trim());
        MyData.put("description", textView.getText().toString().trim());
        MyData.put("type_id", choice_id);
        MyData.put("client_id", Client_id);
        Log.e(TAG, "updtateApi: " + MyData);

        String URL = AppConstants.BASEURL + "client/update";
        Log.e(TAG, "updtateApi: " + URL);

        final ProgressDialog pDialog = new ProgressDialog(UpdateActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        try {

            Response.Listener<JSONObject> reponseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    processJsonObjectCheckIn(jsonObject);
                    pDialog.dismiss();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("RESPONSE ERROR", volleyError.toString());
                    Toast.makeText(UpdateActivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
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

    private void processJsonObjectCheckIn(JSONObject response) {

        if (response != null) {
            Log.e("Response", response + "");

            try {
                String data = String.valueOf(response.get("data"));
                String message = String.valueOf(response.get("message"));
                int status = response.getInt("status");
                // String responce = json.getJSONArray("RESPONSE");
                if (status == 0) {
                    Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e) {
            }
        }

    }
}
