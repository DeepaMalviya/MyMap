package com.daffodil.mymap.other.base;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.daffodil.mymap.R;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by Ravi on 13/08/15.
 */

@ReportsCrashes(mailTo = "deepahonchi@gmail.com", customReportContent = {
        ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME,
        ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL,
        ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT},
        mode = ReportingInteractionMode.TOAST, resToastText = R.string.toast_crash, formKey = "")
public class AppController extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    //private static final String TWITTER_KEY = "Y9bGV1WkiLwirao9K17lyuqZc";
    //private static final String TWITTER_SECRET = "v6qUp263ngsykO70GG5XRPb3C40BuwFkrqmXgm1Xgy8Y5YV0dt";

    public static final String TAG = AppController.class
            .getSimpleName();

    private static AppController mInstance;

    private RequestQueue mRequestQueue;
    private static Context sContext;


    public AppCompatActivity activity;


    @Override
    public void onCreate() {
        super.onCreate();

       /* TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));*/
        mInstance = this;
        mRequestQueue = Volley.newRequestQueue(this);

        ACRA.init(this);
        try {
            sContext = getApplicationContext();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }


    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


}