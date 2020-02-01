package com.daffodil.mymap.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.daffodil.mymap.R;
import com.daffodil.mymap.other.base.AppConstants;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = "SplashScreenActivity";
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    SharedPreferences sharedpref;
    int PERMISSION_ALL = 1;
    String user_id, user_name;
    String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (AppConstants.isInternetAvailable(SplashScreenActivity.this)) {

            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
            initView();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (user_id.equals("")) {
                            Intent intentSplash = new Intent(SplashScreenActivity.this, LoginActivity.class);
                            startActivity(intentSplash);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        } else {
                            Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }

                    } catch (Exception err) {
                        err.printStackTrace();
                    }
                }

            }, SPLASH_DISPLAY_LENGTH);
        } else {
            Toast.makeText(this, getString(R.string.NETWORK_ERROR), Toast.LENGTH_SHORT).show();
        }

    }

    private void initView() {
        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
        user_id = sharedpref.getString("user_id", "");
        user_name = sharedpref.getString("user_name", "");

        Log.e(TAG, "initView:time_in  " + user_id);
        Log.e(TAG, "initView:time_out  " + user_name);


    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
