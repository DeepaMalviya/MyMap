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
import android.view.Menu;
import android.view.MenuItem;
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
import com.daffodil.mymap.adapter.FollowAdapter;
import com.daffodil.mymap.adapter.RevisitAdapter;
import com.daffodil.mymap.model.ClientListModel;
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

public class RevisitActivity extends AppCompatActivity {
    private static final String TAG = "RevisitActivity";
    // List view
    private RecyclerView lv;

    // Listview Adapter
    //  ArrayAdapter<ClientListModel> adapter;
    RevisitAdapter revisitAdapter;
    // Search EditText
    EditText inputSearch;

    SharedPreferences sharedpref;
    SharedPreferences.Editor ed;
    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;
    String mobile, user_id, otp, user_role, user_name, company_id, company_name;
    FollowAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revisit);
        GetIntentData();
        initView();

    }

    private void GetIntentData() {
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
        getSupportActionBar().setTitle("Revisit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        GetRevisitApi();

        lv = findViewById(R.id.rvListR);
        lv.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        lv.setLayoutManager(mLayoutManager);
        lv.setItemAnimator(new DefaultItemAnimator());
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        lv.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 5));

        lv.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), lv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ClientListModel listModel = modelList.get(position);
                Intent intent = new Intent(RevisitActivity.this, RevisitDetailsActivity.class);
                intent.putExtra("Client_id", listModel.getClient_id());
                intent.putExtra("Name", listModel.getName());
                intent.putExtra("Image", listModel.getImage());
                intent.putExtra("Activity", "Revisit");
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //   RevisitActivity.this.revisitAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                Log.e(TAG, "onTextChanged: " + arg0);
                Log.e(TAG, "onTextChanged: " + arg1);
                Log.e(TAG, "onTextChanged: " + arg2);
                Log.e(TAG, "onTextChanged: " + arg3);
                if (arg1 == 2) {
                    clientSearchApi(arg0.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable arg0) {
               // clientSearchApi(arg0.toString());
            }
        });

    }

    List<ClientListModel> modelList = new ArrayList<>();

    private void clientSearchApi(CharSequence search_text) {
        Map<String, String> MyData = new HashMap<String, String>();

        MyData.put("user_id", user_id);
        MyData.put("search_text", (String) search_text);
        MyData.put("type_id","3");

        String URL = AppConstants.BASEURL + "client/search";
        Log.e(TAG, "clientSearchApi: "+URL );
        Log.e(TAG, "clientSearchApi: \n"+MyData );
        final ProgressDialog pDialog = new ProgressDialog(RevisitActivity.this);
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
                    Toast.makeText(RevisitActivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
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
    ClientListModel    choiceModel;
    List<ClientListModel>clientListModels= new ArrayList<>();
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
                    mAdapter = new FollowAdapter(RevisitActivity.this, clientListModels);
                    lv.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                   // mAdapter.updateData(modelList);

                } else {
                    Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e) {
            }
        }

    }

    private void GetRevisitApi() {

        String URL = AppConstants.BASEURL + "client/list?company_id=" + company_id + "&case=revisit"+"&user_id="+user_id;

        final ProgressDialog pDialog = new ProgressDialog(RevisitActivity.this);
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


                        Log.e(TAG, "onResponse: " + modelList.size());
                        mAdapter = new FollowAdapter(RevisitActivity.this, modelList);

                        lv.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                       // mAdapter.updateData(modelList);

                        pDialog.dismiss();


                    } else {
                        Toast.makeText(RevisitActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(RevisitActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //sendError(e.toString(), "user/login?username=");
                    Toast.makeText(RevisitActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(RevisitActivity.this);
                    builder.setMessage("Time Out");
                    builder.setPositiveButton("ReTry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (AppConstants.isInternetAvailable(RevisitActivity.this)) {
                                // loginSevice(username);
                            } else {
                                Toast.makeText(RevisitActivity.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RevisitActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
