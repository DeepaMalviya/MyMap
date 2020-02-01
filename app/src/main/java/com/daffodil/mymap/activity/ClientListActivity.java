package com.daffodil.mymap.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daffodil.mymap.R;
import com.daffodil.mymap.adapter.ClientListAdapter;
import com.daffodil.mymap.model.ClientListModel;
import com.daffodil.mymap.model.SubjectData;
import com.daffodil.mymap.other.CustomRequest;
import com.daffodil.mymap.other.MyDividerItemDecoration;
import com.daffodil.mymap.other.RecyclerTouchListener;
import com.daffodil.mymap.other.base.AppConstants;
import com.daffodil.mymap.other.base.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.daffodil.mymap.other.base.AppConstants.BASEURL;


public class ClientListActivity extends AppCompatActivity {
    private static final String TAG = "ClientListActivity";
    RecyclerView rvCLientList;
    ClientListAdapter mAdapter;
    private List<SubjectData> moviesList = new ArrayList<>();
    SharedPreferences sharedpref;
    SharedPreferences.Editor ed;
    String mobile, user_id, otp, user_role, user_name, company_id, company_name;
    List<ClientListModel> modelList = new ArrayList<>();
    EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
        getData();
        initView();


    }

    private void clientSearchApi(CharSequence search_text) {
        Map<String, String> MyData = new HashMap<String, String>();

        MyData.put("user_id", user_id);
        MyData.put("search_text", (String) search_text);
        MyData.put("type_id", "1");

        String URL = BASEURL + "client/search";
        Log.e(TAG, "clientSearchApi: "+URL );
        Log.e(TAG, "clientSearchApi: "+MyData );
        final ProgressDialog pDialog = new ProgressDialog(ClientListActivity.this);
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
                    Toast.makeText(ClientListActivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
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

    ClientListModel choiceModel;
    List<ClientListModel> clientListModels = new ArrayList<>();

    private void processJsonObjectCheckIn(JSONObject response) {
        clientListModels.clear();

        if (response != null) {
            Log.e("Response", response + "");

            try {
                String data = String.valueOf(response.get("data"));
                String message = String.valueOf(response.get("message"));
                int status = response.getInt("status");
                // String responce = json.getJSONArray("RESPONSE");
                if (status == 0) {
                    // Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                    final JSONArray arrayObj = new JSONArray(data);
                    for (int i = 0; i < arrayObj.length(); i++) {

                        JSONObject jsonObject = arrayObj.getJSONObject(i);
                        choiceModel = new ClientListModel();
                        //choiceModel.setChoice_id(Strchoice_id);
                        choiceModel.setName(jsonObject.getString("name"));
                        choiceModel.setImage(jsonObject.getString("image"));
                        choiceModel.setClient_id(jsonObject.getString("client_id"));
                        clientListModels.add(choiceModel);

                        // addRadioButtons(choiceModelList.size(), choiceModel);


                    }
                    mAdapter = new ClientListAdapter(ClientListActivity.this, clientListModels);
                    rvCLientList.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                   // mAdapter.updateData(clientListModels);


                } else {
                    Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e) {
            }
        }

    }

    private void getData() {
        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
        mobile = sharedpref.getString("mobile", "");
        user_id = sharedpref.getString("user_id", "");
        otp = sharedpref.getString("otp", "");
        user_role = sharedpref.getString("user_role", "");
        user_name = sharedpref.getString("user_name", "");
        company_id = sharedpref.getString("company_id", "");
        company_name = sharedpref.getString("company_name", "");

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Client List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        inputSearch = findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, "onTextChanged: " + s);
                Log.e(TAG, "onTextChanged: " + start);
                Log.e(TAG, "onTextChanged: " + before);
                Log.e(TAG, "onTextChanged: " + count);
                if (start == 2) {
                    clientSearchApi(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                //
            }
        });
        rvCLientList = findViewById(R.id.rvCLientList);
        prepareClientListData();
        mAdapter = new ClientListAdapter(ClientListActivity.this, modelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvCLientList.setLayoutManager(mLayoutManager);
        rvCLientList.setItemAnimator(new DefaultItemAnimator());
        rvCLientList.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        rvCLientList.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvCLientList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ClientListModel movie = modelList.get(position);
                Intent intent = new Intent(ClientListActivity.this, ClientDetailsActivity.class);
                intent.putExtra("Name", movie.getName());
                intent.putExtra("Image", movie.getImage());
                intent.putExtra("Client_id", movie.getClient_id());
                intent.putExtra("user_id", user_id);
                intent.putExtra("ClientListActivity", "ClientListActivity");
                startActivity(intent);
                // Toast.makeText(getApplicationContext(), movie.getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

// set the adapter
        rvCLientList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }


    private void prepareClientListData() {
       /*http://daffodillab.in/trackerpgpl/index.php/v1/client/list?company_id=1*/
        String URL = BASEURL + "client/list?company_id=" + company_id + "&user_id=" + user_id;
        final ProgressDialog pDialog = new ProgressDialog(ClientListActivity.this);
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

                try {
                    // System.out.println("JSON RETURN " + response.toString());

                    String data = String.valueOf(response.get("data"));
                    String message = String.valueOf(response.get("message"));
                    int status = response.getInt("status");

                    if (status == 0) {
                        final JSONArray arrayObj = new JSONArray(data);


                        //   Toast.makeText(DetailActivity.this, message, Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < arrayObj.length(); i++) {

                            JSONObject jsonObject = arrayObj.getJSONObject(i);

//                          JSONObject checkINresponce = response.getJSONObject("data");
                            ClientListModel loginModel = new ClientListModel();
                            loginModel.setName(jsonObject.getString("name"));
                            loginModel.setImage(jsonObject.getString("image"));
                            loginModel.setClient_id(jsonObject.getString("client_id"));
                            modelList.add(loginModel);
                        }


                        mAdapter = new ClientListAdapter(ClientListActivity.this, modelList);
                        rvCLientList.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        //ed.putString("name", jsonObject.getName());
                        //ed.putString("client_id", jsonObject.getClient_id());

                        ed.apply();
                        ed.commit();

                       /* Intent intentTow = new Intent(ClientListActivity.this, VerifyPhoneActivity.class);
                        intentTow.putExtra("name", loginModel.getName());
                        intentTow.putExtra("client_id", loginModel.getClient_id());
                        startActivity(intentTow);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
*/

                        pDialog.dismiss();


                    } else {
                        Toast.makeText(ClientListActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(ClientListActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(ClientListActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(ClientListActivity.this);
                    builder.setMessage("Time Out");
                    builder.setPositiveButton("ReTry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (AppConstants.isInternetAvailable(ClientListActivity.this)) {
                                // loginSevice(username);
                            } else {
                                Toast.makeText(ClientListActivity.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ClientListActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
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
}
