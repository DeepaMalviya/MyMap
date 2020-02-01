package com.daffodil.mymap.other.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.daffodil.mymap.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class AppConstants {


    public static final String BASEURL = "http://daffodillab.in/trackerpgpl/index.php/v1/";  //stg
    public static final String BASEURL_client_ist = "http://daffodillab.in/trackerpgpl/index.php/v1/";  //stg
    public static final String IMAGE_URL = "http://daffodillab.in/trackerpgpl/global/client/";  //stg


    public static final String COMMON_TAG = "type";
    public static final String TAG_DATA = "data";
    public static final String LOGIN_API_END_POINT = "user/login";
    public static final String ALERT_TITLE = "Alert!";
    public static final String ALERT_TITLE_SUCCESS = "Success!";
    public static final String ALERT_TITLE_FAILED = "Failed!";
    public static final int PHONE_NUMBER_LENGTH = 10;
    public static final int PINCODE_LENGTH = 5;
    public static final String COMMON_DATE_FORMAT = "EEE, MMM d, yyyy";
    public static final String UPDATE_DEVICE_TOKEN_TAG = "updateUserDevice";
    public static final String FORGOT_PASSWORD_API_END_POINT = "mobile/actions_V2/resetPassword";
    public static final String FORGOT_PASSWORD_TAG = "forgotPassword";
    public static final String RESPONSE_TYPE_CHECKIN = "checkin";
    public static final String RESPONSE_TYPE_CHECKOUT = "checkout";
    public static final String APIERROR = "apierror/add";


    final static Pattern NUMBER_PATTERN = Pattern.compile("[+-]?\\d*\\.?\\d+");
    final static Pattern MOBILE_NUMBER_PATTERN = Pattern.compile("^[^0][0-9]{9}?$");
    public static final String LOGIN_TAG = "LoginTag";
    public static final String UNKNOWN_ERROR = "Something went wrong.";
    public static final String MODE_EMPLOYEE_ID = "EmployeeID";
    public static final String MODE_MOBILE_NO = "MobileNo";
    public static final String MODE_VEHICEL_NO = "VehicleNo";
    public static final String VALIDATE_EMPLOYEE_END_PIONT = "parking/validate_user";
    public static final String VEHICLE_CHECKIN_END_PIONT = "parking/checkin";
    public static final String VEHICLE_CHECKOUT_END_PIONT = "parking/checkout";
    public static final String VEHICLE_RECEIPT_IN_END_PIONT = "parking/receipt_checkin";
    public static final String VEHICLE_RECEIPT_OUT_END_PIONT = "parking/receipt_checkout";
    public static final String SPOTS_CHECK_PIONT = "parking/inventory";
    public static final String QR_SCAN = "QRScan";
    public static final String VALIDATE_FROM_API = "ApiValidation";
    public static final String PRINTER_ADDRESS = "printerAddress";
    public static final String PRINTER_NAME = "PrinterName";


    public static String USER_TYPE_CORPORATE = "Corporate Agent";
    public static String USER_TYPE_AGENT = "Normal Agent";

    public static String Checkin_Attendent = "Checkin Attendent";
    public static String Checkout_Attendent = "Checkout Attendent";
    public static String Towing_Operator = "Towing Operator";


    public static String USER_ID = "user_id";
    public static String LoginKey = "user_id";
    public static String userRole = "userRole";
    public static String userName = "userName";
    public static String userContactNo = "userContactNo";
    public static String vendorId = "vendorId";
    public static String vendorName = "vendorName";
    public static String parkingName = "parkingName";
    public static String parkingType = "parkingType";
    public static String parkingId = "parkingId";
    public static String TAGID = "TagId";

    public static String setData(String str) {
        if (null != str && !"".equals(str)) {
            return str;
        }
        return "";
    }


    /**********
     * Method to check the field is blank or not (i.e. if the field is blank then method will return true otherwise false).
     **********/
    public static boolean isBlank(String field) {
        if (null == field) {
            return true;
        } else if (field.equals("null")) {
            return true;
        } else {
            return field.trim().isEmpty();
        }
    }

    /**********
     * Method to show toast message by passing message on it.
     *********/
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
    }

    /**********
     * Method to print Log message by passing tag and message.
     **********/
    public static void log(String tag, String message) {
        try {
            //  Log.d(tag, message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getString(Context act, String key, String def) {

        SharedPreferences signUp = act.getSharedPreferences(act.getApplicationInfo().packageName + "_preferences", Context.MODE_PRIVATE);

        return signUp.getString(key, def);
    }


    public static void setString(Context act, String key, String value) {

        SharedPreferences signUp = act.getSharedPreferences(act.getApplicationInfo().packageName + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = signUp.edit();
        editor.putString(key, value);
        editor.apply();

    }

    public static boolean isGPSEnabled(Context mContext) {
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isNetworkProviderAvailable(Context mContext) {
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public static boolean setSetting(Context act, String key, Boolean value) {
        SharedPreferences settings = act.getSharedPreferences(act.getApplicationInfo().packageName + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);

        // Commit the edits!
        editor.apply();

        return value;
    }

    public static boolean getSetting(Context act, String key, Boolean value) {

        SharedPreferences settings = act.getSharedPreferences(act.getApplicationInfo().packageName + "_preferences", Context.MODE_PRIVATE);
        return settings.getBoolean(key, value);
    }

    /**********
     * Method to hide keyboard.
     **********/
    public static void hideKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } else {

        }
    }

    /**********
     * Method to check data connection availability (if network is available then it will return true otherwise false).
     **********/
    public static boolean isInternetAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting() && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
                return true;
            } else {
                //  showSnckbar(context, context.getString(R.string.NETWORK_ERROR));
              Toast.makeText(context, context.getString(R.string.NETWORK_ERROR), Toast.LENGTH_SHORT).show();
            }
            return false;
        } catch (Exception err) {
            err.printStackTrace();
        }
        return false;
    }


    /*****************
     * Method to check valid format of mobile number
     ***********************/
    public static boolean isMobileValid(String field) {
        Matcher m = MOBILE_NUMBER_PATTERN.matcher(field);
        return m.matches();
    }


    /**
     * @param t       have error info
     * @param context required to if we want to change the flow at run time.
     */
    public static void retrofitOnFailure(Throwable t, Context context) {

    }


    public static boolean isBluetoothEnabled() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            // Bluetooth is not enable :)
            return false;
        } else {
            return true;
        }

    }


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((AppCompatActivity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    //method to enable bluetooth
    public static void enableBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
    }

    //method to disable bluetooth
    public static void disableBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }
    }

}
