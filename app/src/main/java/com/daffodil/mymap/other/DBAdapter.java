package com.daffodil.mymap.other;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Environment;
import android.util.Log;


import com.daffodil.mymap.other.dto.ActivityDTO;
import com.daffodil.mymap.other.dto.CaptureShowDTO;
import com.daffodil.mymap.other.dto.Companydto;
import com.daffodil.mymap.other.dto.ContactDTO;
import com.daffodil.mymap.other.dto.Event;
import com.daffodil.mymap.other.dto.Leaddto;
import com.daffodil.mymap.other.dto.TrackInfoDTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class DBAdapter extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.pgplactivitytracker/databases/";
    // private static String DB_PATH = Environment.getExternalStorageDirectory() + "/";
    private static String DB_NAME = "activity_tracker.sqlite";

    public static SQLiteDatabase myDataBase;
    private String[] allColumns = {"parent_id", "creator_id", "Companyname", "Address", "City", "State", "PinCode", "Country", "CompWebsite", "CompPhoneNo", "CompEmailID", "CompIndustry", "ContTitle",
            "ContFName", "ContLName", "ContJobTitle", "ContPhoneNo", "ContMobileNo", "ContEmailID", "Note", "Latitude", "Longitude", "Photo", "compId", "contProfilePic", "CompLogo"};


    private final Context myContext;

    public DBAdapter(Context context) {
        super(context, DB_NAME, null, 3);
        this.myContext = context;

       /* try {
            copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println(" onCreate called");
       /* try {
            createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /*try {
            copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();

        }*/


    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteCantOpenDatabaseException sq) {
            sq.printStackTrace();

        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }


    public void showDataBaseOfDevice(Context con) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                //String currentDBPath = "/data/" + con.getPackageName() + "/databases/";
                String currentDBPath = DB_PATH + DB_NAME;
                String backupDBPath = "backupname.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {
        System.out.println("Inside copyDataBase");
        //Log.i("DBAdapter", "copyDataBase");
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    /*public void createDataBase() throws IOException {
        // copyDataBase();

        Log.v("inside dbadapter", "inside oncreate");
        boolean dbExist = checkDataBase();
        //Log.i("DBAdapter", ""+dbExist);
        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getWritableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {
                e.printStackTrace();
                throw new Error("Error copying database");

            }
        }

    }*/
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
               e.printStackTrace();
            }
        }
    }

    public void openDataBase() throws SQLException {
        /*if (myContext.deleteDatabase(DB_NAME)) {
            Toast.makeText(myContext, "Data base is deleted", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(myContext, "Data base could not delete", Toast.LENGTH_LONG).show();
        }*/
        try {
            createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
            //throw new Error("Ha sido imposible crear la Base de Datos");
        }
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        myContext.deleteDatabase(DB_NAME);
        try {
            System.out.println("onUpgrade called");
            copyDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    public String getStartTimeStamp(String user_id, String date) {
        String timestamp = "";

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "select start_timestamp from trackinfo where userid = '" + user_id + "' and date = '" + date + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                timestamp = cursor.getString(cursor.getColumnIndex("start_timestamp"));
            }
        }

        return timestamp;
    }

    public long insertCompany(Companydto compdto) {


        ContentValues contentValues = new ContentValues();
        contentValues.put("parent_id", compdto.getParent_id());
        contentValues.put("creator_id", compdto.getCreator_id());
        contentValues.put("Companyname", compdto.getComp_name());
        contentValues.put("Address", compdto.getC_add());
        contentValues.put("City", compdto.getC_city());
        contentValues.put("State", compdto.getC_state());
        contentValues.put("PinCode", compdto.getC_pincode());
        contentValues.put("Country", compdto.getC_country());
        contentValues.put("CompWebsite", compdto.getCompWebsite());
        contentValues.put("CompPhoneNo", compdto.getCompPhoneNo());
        contentValues.put("CompEmailID", compdto.getCompEmailId());

        contentValues.put("CompIndustry", compdto.getCompIndustry());
        contentValues.put("ContId", compdto.getContId());
        contentValues.put("ContTitle", compdto.getContTitle());
        contentValues.put("ContFName", compdto.getContFirstName());


        contentValues.put("ContLName", compdto.getContLastName());
        contentValues.put("ContJobTitle", compdto.getContJobTitle());

        contentValues.put("ContPhoneNo", compdto.getPhno());
        contentValues.put("ContMobileNo", compdto.getMobno());
        contentValues.put("ContEmailID", compdto.getEmailid());
        contentValues.put("Note", compdto.getNote());
        contentValues.put("Latitude", compdto.getLat());
        contentValues.put("Longitude", compdto.getLog());
        contentValues.put("Photo", compdto.getContProfilePic());
        contentValues.put("CompId", (compdto.getComp_id()) == null ? "0" : compdto.getComp_id());
        contentValues.put("CompLogo", compdto.getCompLogo());
        // DBAdapter.myDataBase = this.getWritableDatabase();
        long row_id = -1;
        if (checkDataBase()) {
            myDataBase = this.getWritableDatabase();
            row_id = myDataBase.insertWithOnConflict("Company", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            Log.d("row_id", "" + row_id);
        } else {
            try {
                copyDataBase();
                if (checkDataBase()) {
                    showDataBaseOfDevice(myContext);
                    row_id = DBAdapter.myDataBase.insertWithOnConflict("Company", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                    Log.d("row_id", "" + row_id);
                } else {
                    Log.d("Could not copy", "Yes");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<Companydto> arrlist = getCompany_Info();
        }
        ArrayList<Companydto> arrlist = getCompany_Info();
        Log.d("arrlist size", arrlist.size() + "");


        return row_id;
    }

    public long updateCompanyInfo(Companydto companydto) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Companyname", companydto.getComp_name());
        contentValues.put("compId", companydto.getComp_id());
        long row_id = -1;
        if (checkDataBase()) {
            myDataBase = this.getWritableDatabase();
            myDataBase.update("company_info", contentValues, "Companyname" + " = ?",
                    new String[]{String.valueOf(companydto.getComp_name())});
        }
        return row_id;
    }

    public long insertCompanyInfo(Companydto compdto) {


        ContentValues contentValues = new ContentValues();
        contentValues.put("parent_id", compdto.getParent_id());
        contentValues.put("creator_id", compdto.getCreator_id());
        contentValues.put("Companyname", compdto.getComp_name());
        contentValues.put("Address", compdto.getC_add());
        contentValues.put("City", compdto.getC_city());
        contentValues.put("State", compdto.getC_state());
        contentValues.put("PinCode", compdto.getC_pincode());
        contentValues.put("Country", compdto.getC_country());
        contentValues.put("CompWebsite", compdto.getCompWebsite());
        contentValues.put("CompPhoneNo", compdto.getCompPhoneNo());
        contentValues.put("CompEmailID", compdto.getCompEmailId());

        contentValues.put("CompIndustry", compdto.getCompIndustry());

        contentValues.put("ContTitle", compdto.getContTitle());
        contentValues.put("ContFName", compdto.getContFirstName());


        contentValues.put("ContLName", compdto.getContLastName());
        contentValues.put("ContJobTitle", compdto.getContJobTitle());

        contentValues.put("ContPhoneNo", compdto.getPhno());
        contentValues.put("ContMobileNo", compdto.getMobno());
        contentValues.put("ContEmailID", compdto.getEmailid());
        contentValues.put("Note", compdto.getNote());
        contentValues.put("Latitude", compdto.getLat());
        contentValues.put("Longitude", compdto.getLog());
        contentValues.put("Photo", compdto.getPhoto());
        contentValues.put("compId", compdto.getComp_id());
        contentValues.put("contProfilePic", compdto.getContProfilePic());
        contentValues.put("CompLogo", compdto.getCompLogo());


        // DBAdapter.myDataBase = this.getWritableDatabase();
        long row_id = -1;
        if (checkDataBase()) {
            myDataBase = this.getWritableDatabase();
            row_id = myDataBase.insertWithOnConflict("company_info", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            Log.d("row_id", "" + row_id);
        } else {
            try {
                copyDataBase();
                if (checkDataBase()) {
                    showDataBaseOfDevice(myContext);
                    row_id = DBAdapter.myDataBase.insertWithOnConflict("company_info", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                    Log.d("row_id", "" + row_id);
                } else {
                    Log.d("Could not copy", "Yes");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<Companydto> arrlist = getCompany_Info();
        }
        ArrayList<Companydto> arrlist = getCompany_Info();
        Log.d("arrlist size", arrlist.size() + "");


        return row_id;
    }

    public long insertContact(ContactDTO contact) {


        ContentValues contentValues = new ContentValues();
        contentValues.put("parent_id", contact.getParent_id());
        contentValues.put("creator_id", contact.getCreator_id());
        contentValues.put("Companyname", contact.getComp_name());
        contentValues.put("CompId", contact.getCompId());
        contentValues.put("compAdd", contact.getCompAdd());
        contentValues.put("compCity", contact.getCompCity());
        contentValues.put("compState", contact.getCompState());
        contentValues.put("compPincode", contact.getCompPincode());
        contentValues.put("compCountry", contact.getCompCountry());

        contentValues.put("compWebsite", contact.getCompWebsite());
        contentValues.put("compPhoneNo", contact.getCompPhoneNo());
        contentValues.put("compEmailID", contact.getCompEmailID());

        contentValues.put("compIndustry", contact.getCompIndustry());
        contentValues.put("compLogo", contact.getCompLogo());

        contentValues.put("contId", contact.getContId());
        contentValues.put("contTitle", contact.getContTitle());
        contentValues.put("contFirstName", contact.getContFirstName());


        contentValues.put("contLastName", contact.getContLastName());
        contentValues.put("contJobTitle", contact.getContJobTitle());

        contentValues.put("contPhoneNo", contact.getContPhoneNum());
        contentValues.put("contMobileNo", contact.getContMobileNum());
        contentValues.put("contEmailID", contact.getContEmailId());
        contentValues.put("contNote", contact.getContNote());

        contentValues.put("contLatitude", contact.getContLatitude());
        contentValues.put("contLongitude", contact.getContLongitude());
        contentValues.put("contProfilePic", contact.getContProfilePic());
        // DBAdapter.myDataBase = this.getWritableDatabase();
        long row_id = -1;
        if (checkDataBase()) {
            myDataBase = this.getWritableDatabase();
            row_id = myDataBase.insertWithOnConflict("Contact", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            Log.d("row_id", "" + row_id);
        } else {
            try {
                copyDataBase();
                if (checkDataBase()) {
                    showDataBaseOfDevice(myContext);
                    row_id = DBAdapter.myDataBase.insertWithOnConflict("Contact", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                    Log.d("row_id", "" + row_id);
                } else {
                    Log.d("Could not copy", "Yes");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<Companydto> arrlist = getCompany_Info();
        }
        ArrayList<Companydto> arrlist = getCompany_Info();
        Log.d("arrlist size", arrlist.size() + "");


        return row_id;
    }

    public long insertContactList(ContactDTO contact) {


        ContentValues contentValues = new ContentValues();
        contentValues.put("parent_id", contact.getParent_id());
        contentValues.put("creator_id", contact.getCreator_id());
        contentValues.put("compName", contact.getComp_name());
        contentValues.put("compID", contact.getCompId());

        contentValues.put("contId", contact.getContId());
        contentValues.put("contTitle", contact.getContTitle());
        contentValues.put("contFirstName", contact.getContFirstName());


        contentValues.put("contLastName", contact.getContLastName());
        contentValues.put("contJobTitle", contact.getContJobTitle());

        contentValues.put("contPhoneNo", contact.getContPhoneNum());
        contentValues.put("contMobileNo", contact.getContMobileNum());
        contentValues.put("contEmailID", contact.getContEmailId());

        contentValues.put("contProfilePic", contact.getContProfilePic());
        contentValues.put("contPhotoId", contact.getContPhotoId());
        // DBAdapter.myDataBase = this.getWritableDatabase();
        long row_id = -1;
        if (checkDataBase()) {
            myDataBase = this.getWritableDatabase();
            row_id = myDataBase.insertWithOnConflict("ContactList", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            Log.d("row_id", "" + row_id);
        } else {
            try {
                copyDataBase();
                if (checkDataBase()) {
                    showDataBaseOfDevice(myContext);
                    row_id = DBAdapter.myDataBase.insertWithOnConflict("ContactList", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                    Log.d("row_id", "" + row_id);
                } else {
                    Log.d("Could not copy", "Yes");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<Companydto> arrlist = getCompany_Info();
        }
        ArrayList<Companydto> arrlist = getCompany_Info();
        Log.d("arrlist size", arrlist.size() + "");


        return row_id;
    }

    public long insertTrackUserInfo(TrackUser trackUser) {
        long row_id = -1;

        // DBAdapter.myDataBase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("parent_id", trackUser.getParent_id());
        contentValues.put("user_id", trackUser.getUser_id());
        contentValues.put("latitude", trackUser.getLatitude());
        contentValues.put("longitude", trackUser.getLongitude());
        contentValues.put("date", trackUser.getDate());
        contentValues.put("time", trackUser.getTime());
        contentValues.put("battery_persentage", trackUser.getBattery_persentage());
        contentValues.put("network_type", trackUser.getNetwork_type());

        contentValues.put("cell_id_short", trackUser.getCellIdShort());
        contentValues.put("cell_id_long", trackUser.getCellIdLong());
        contentValues.put("lac", trackUser.getLac());
        contentValues.put("mnc", trackUser.getMnc());
        contentValues.put("mcc", trackUser.getMcc());

        contentValues.put("device_type", trackUser.getDevice_type());
        contentValues.put("basestation_id", trackUser.getBasestation_id());
        contentValues.put("basestation_latitude", trackUser.getBasestation_latitude());
        contentValues.put("basestation_longitude", trackUser.getBasestation_longitude());

        contentValues.put("sentStatus", trackUser.getSentStatus());
        contentValues.put("shutdownTime", trackUser.getShutdownTime());
        contentValues.put("restartTime", trackUser.getRestartTime());
        contentValues.put("signalStrenght", trackUser.getSignalStrenght());
        contentValues.put("networkSubType", trackUser.getNetworkSubType());
        contentValues.put("profileType", trackUser.getProfileType());
        contentValues.put("operatorType", trackUser.getOperatorType());

        try {
            if (checkDataBase()) {
                myDataBase = this.getWritableDatabase();
                row_id = myDataBase.insertWithOnConflict("TrackUser", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                Log.d("row_id", "" + row_id);
            } else {
                try {
                    copyDataBase();
                    if (checkDataBase()) {
                        showDataBaseOfDevice(myContext);
                        row_id = DBAdapter.myDataBase.insertWithOnConflict("TrackUser", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                        Log.d("row_id", "" + row_id);
                    } else {
                        Log.d("Could not copy", "Yes");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ArrayList<TrackUser> arrlist = getTrackUserInfo();
            }
            ArrayList<TrackUser> arrlist = getTrackUserInfo();
            Log.d("arrlist size", arrlist.size() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Toast.makeText(myContext, "User track data inserted List size: " + arrlist.size() + "", Toast.LENGTH_LONG).show();


       /* try {
            // createDataBase();
            row_id = DBAdapter.myDataBase.insertWithOnConflict("TrackUser", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("row_id", "" + row_id);*/
        return row_id;
    }

    public long insertActivity(ActivityDTO activity) {
        long row_id = 0;
        ContentValues contentValues = new ContentValues();
        try {

            contentValues.put("parent_id", activity.getParent_id());
            contentValues.put("creator_id", activity.getCreator_id());

            contentValues.put("actTitle", activity.getActTitle());
            contentValues.put("actDetail", activity.getActDetail());

            contentValues.put("actRemark", activity.getActRemark());
            contentValues.put("actDate", activity.getActDate());
            contentValues.put("actTime", activity.getActTime());
            contentValues.put("leadTitle", activity.getLeadTitle());

            // contentValues.put("leadStatus", leaddto.getLeadStatus());
            //contentValues.put("leadSource", leaddto.getLeadSource());

            contentValues.put("compId", activity.getCompId());
            contentValues.put("contTitle", activity.getContTitle());
            contentValues.put("contFirstName", activity.getContFirstName());
            contentValues.put("contLastName", activity.getContLastName());

            contentValues.put("contJobTitle", activity.getContJobTitle());
            contentValues.put("contPhoneNo", activity.getContPhoneNo());
            contentValues.put("contMobileNo", activity.getContMobileNo());
            contentValues.put("contEmailID", activity.getContEmailID());
            contentValues.put("contNote", activity.getContNote());


            contentValues.put("compName", activity.getCompName());

            contentValues.put("compAdd", activity.getCompAdd());
            contentValues.put("compCity", activity.getCompCity());
            contentValues.put("compState", activity.getCompState());

            contentValues.put("compPincode", activity.getCompPincode());

            contentValues.put("compCountry", activity.getCompCountry());

            contentValues.put("compWebsite", activity.getCompWebsite());

            contentValues.put("compPhoneNo", activity.getCompPhoneNo());
            contentValues.put("compEmailID", activity.getCompEmailID());

            contentValues.put("compIndustry", activity.getCompIndustry());
            contentValues.put("compLogo", activity.getCompLogo());


            contentValues.put("latitude", activity.getLatitude());
            contentValues.put("longitude", activity.getLongitude());

            contentValues.put("contProfilePic", activity.getContProfilePic());
            contentValues.put("actPhoto", activity.getActPhoto());
            contentValues.put("actId", activity.getActId());
            contentValues.put("selectedContactIds", activity.getSelectedContactIds());


            if (checkDataBase()) {
                myDataBase = this.getWritableDatabase();
                row_id = DBAdapter.myDataBase.insertWithOnConflict("Activity", null, contentValues, 0);
            }
        } catch (SQLiteException se) {
            se.printStackTrace();

        }
        return row_id;
    }

    public long insertlead_info_new(Leaddto leaddto) {
        ContentValues contentValues = new ContentValues();


        contentValues.put("parent_id", leaddto.getParent_id());
        contentValues.put("creator_id", leaddto.getCreater_id());

        contentValues.put("date", leaddto.getDate());
        contentValues.put("time", leaddto.getTime());

        contentValues.put("leadTitle", leaddto.getLeadTitle());
        contentValues.put("leadDetail", leaddto.getLeadDetail());
        contentValues.put("leadClosingDate", leaddto.getLeadClosingTime());
        contentValues.put("leadBudget", leaddto.getLeadBudget());
        contentValues.put("leadStatus", leaddto.getLeadStatus());
        contentValues.put("leadSource", leaddto.getLeadSource());

        contentValues.put("compId", leaddto.getCompId());
        contentValues.put("contTitle", leaddto.getContTitle());
        contentValues.put("contFirstName", leaddto.getContFirstNAme());
        contentValues.put("contLastName", leaddto.getContLastName());

        contentValues.put("contJobTitle", leaddto.getContJobTitle());
        contentValues.put("contPhoneNo", leaddto.getContPhoneNo());
        contentValues.put("contMobileNo", leaddto.getContMobileNo());
        contentValues.put("contEmailID", leaddto.getContEmailId());
        contentValues.put("contNote", leaddto.getContNote());

        contentValues.put("Photo", leaddto.getPhoto());
        contentValues.put("compName", leaddto.getCompname());

        contentValues.put("compAdd", leaddto.getCompAdd());
        contentValues.put("compCity", leaddto.getCompCity());
        contentValues.put("compState", leaddto.getCompState());

        contentValues.put("compPincode", leaddto.getCompPincode());

        contentValues.put("compCountry", leaddto.getCompCountry());

        contentValues.put("compWebsite", leaddto.getCompWebsite());

        contentValues.put("compPhoneNumber", leaddto.getCompPhoneNumber());
        contentValues.put("compEmailID", leaddto.getCompEmailID());

        contentValues.put("compIndustry", leaddto.getCompIndustry());
        contentValues.put("compLogo", leaddto.getCompLogo());
        contentValues.put("compLatitude", leaddto.getCompLat());
        contentValues.put("compLongitude", leaddto.getCompLong());

        contentValues.put("leadId", leaddto.getLead_id());
        contentValues.put("selectedContactIds", leaddto.getSelectedContactIds());


        long row_id = 0;
        if (checkDataBase()) {
            myDataBase = this.getWritableDatabase();

            row_id = DBAdapter.myDataBase.insertWithOnConflict("Lead", null, contentValues, 0);
        }
        return row_id;
    }

    public long insertEvent(Event event) {
        ContentValues contentValues = new ContentValues();


        contentValues.put("parentId", event.getParentId());

        contentValues.put("creatorId", event.getCreatorId());

        contentValues.put("eventId", event.getEventId());
        contentValues.put("eventTitle", event.getEventTitle());

        contentValues.put("eventDetail", event.getEventDetail());
        contentValues.put("eventStartTime", event.getEventStartTime());
        contentValues.put("eventEndTime", event.getEventEndTime());
        contentValues.put("eventLocation", event.getEventLocation());
        contentValues.put("eventRemark", event.getEventRemarks());

        contentValues.put("compId", event.getComp_id());

        contentValues.put("compName", event.getCompName());

        contentValues.put("compAdd", event.getCompAdd());
        contentValues.put("compCity", event.getCompCity());
        contentValues.put("compState", event.getCompState());

        contentValues.put("compPincode", event.getCompPin());

        contentValues.put("compCountry", event.getCompCountry());

        contentValues.put("compWebsite", event.getCompWeb());

        contentValues.put("compPhoneNo", event.getCompphone());
        contentValues.put("compEmailID", event.getCompEmail());

        contentValues.put("compIndustry", event.getCompIndustry());
        contentValues.put("compLogo", event.getCompLogo());


        contentValues.put("contId", event.getContId());

        contentValues.put("contTitle", event.getContTitle());
        contentValues.put("contFirstName", event.getContFirstName());
        contentValues.put("contLastName", event.getContLastName());

        contentValues.put("contJobTitle", event.getContJobTitle());
        contentValues.put("contPhoneNo", event.getContPhoneNumber());
        contentValues.put("contMobileNo", event.getContMob());
        contentValues.put("contEmailID", event.getContEmail());
        contentValues.put("contNote", event.getContNote());


        contentValues.put("contProfilePic", event.getContProfilePic());

        contentValues.put("eventLatitude", event.getEventLatitude());
        contentValues.put("eventLogitude", event.getEventLongitude());

        contentValues.put("eventPhoto", event.getEventPhoto());
        contentValues.put("selectedContactIds", event.getSelectedContactIds());

        long row_id = 0;
        if (checkDataBase()) {
            myDataBase = this.getWritableDatabase();

            row_id = DBAdapter.myDataBase.insertWithOnConflict("Event", null, contentValues, 0);
            System.out.println("Inserted Event-----" + row_id);
        }
        return row_id;
    }


    public long insertsale_info_new(Leaddto leaddto) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("parent_id", leaddto.getParent_id());
        contentValues.put("creator_id", leaddto.getCreater_id());
        contentValues.put("lead_name", leaddto.getLead_name());
        contentValues.put("date", leaddto.getDate());
        contentValues.put("time", leaddto.getTime());
        contentValues.put("c_name", leaddto.getC_name());
        contentValues.put("lat", leaddto.getLat());
        contentValues.put("long", leaddto.getLog());
        contentValues.put("status", leaddto.getStatus());
        contentValues.put("name", leaddto.getName());
        contentValues.put("phno", leaddto.getPhno());
        contentValues.put("emailid", leaddto.getEmailid());
        contentValues.put("note", leaddto.getNote());
        contentValues.put("photo", leaddto.getPhoto());

        long row_id = DBAdapter.myDataBase.insertWithOnConflict("sales_info_new", null, contentValues, 0);
        return row_id;
    }

    public long insertdeal_info_new(Leaddto leaddto) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("parent_id", leaddto.getParent_id());
        contentValues.put("creator_id", leaddto.getCreater_id());
        contentValues.put("lead_name", leaddto.getLead_name());
        contentValues.put("date", leaddto.getDate());
        contentValues.put("time", leaddto.getTime());
        contentValues.put("c_name", leaddto.getC_name());
        contentValues.put("lat", leaddto.getLat());
        contentValues.put("long", leaddto.getLog());
        contentValues.put("status", leaddto.getStatus());
        contentValues.put("name", leaddto.getName());
        contentValues.put("phno", leaddto.getPhno());
        contentValues.put("emailid", leaddto.getEmailid());
        contentValues.put("note", leaddto.getNote());
        contentValues.put("photo", leaddto.getPhoto());

        long row_id = DBAdapter.myDataBase.insertWithOnConflict("dealer_info_new", null, contentValues, 0);
        return row_id;
    }

    public long insertTrackInfo(TrackInfoDTO trackInfoDto) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("userid", trackInfoDto.getUserid());
        contentValues.put("date", trackInfoDto.getDate());
        contentValues.put("start_timestamp", trackInfoDto.getStart_timestamp());
        contentValues.put("startpt_lat", trackInfoDto.getStartpt_lat());
        contentValues.put("startpt_long", trackInfoDto.getStartpt_long());
        contentValues.put("start_name", trackInfoDto.getStart_name());
        contentValues.put("start_place", trackInfoDto.getStart_place());
        contentValues.put("start_ward", trackInfoDto.getStart_ward());
        contentValues.put("start_town", trackInfoDto.getStart_town());
        contentValues.put("start_city", trackInfoDto.getStart_city());
        contentValues.put("start_state", trackInfoDto.getStart_state());

        long row_id = DBAdapter.myDataBase.insertWithOnConflict("trackinfo", null, contentValues, 0);
        return row_id;
    }

    public int updateTrackInfo(TrackInfoDTO trackInfoDto) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("end_timestamp", trackInfoDto.getEnd_timestamp());
        contentValues.put("endpt_lat", trackInfoDto.getEndpt_lat());
        contentValues.put("endpt_long", trackInfoDto.getEndpt_long());
        contentValues.put("end_name", trackInfoDto.getEnd_name());
        contentValues.put("end_place", trackInfoDto.getEnd_place());
        contentValues.put("end_ward", trackInfoDto.getEnd_ward());
        contentValues.put("end_town", trackInfoDto.getEnd_town());
        contentValues.put("end_city", trackInfoDto.getEnd_city());
        contentValues.put("end_state", trackInfoDto.getEnd_state());
        contentValues.put("totaldistance", trackInfoDto.getTotaldistance());

        int success = DBAdapter.myDataBase.update("trackinfo", contentValues, "userid = ? and date = ?",
                new String[]{trackInfoDto.getUserid(), trackInfoDto.getDate()});
        return success;
    }

    public ArrayList<CaptureShowDTO> getAllEvents(String user_id, String date) {
        ArrayList<CaptureShowDTO> captureShows = new ArrayList<CaptureShowDTO>();
        CaptureShowDTO show;
        String sql = "select * from capture_show where userid = '" + user_id + "' and userdate = '" + date + "'";
        Cursor cursor = DBAdapter.myDataBase.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                long startTimstamp = cursor.getLong(cursor.getColumnIndex("start_timestamp"));
                int i = (int) ((System.currentTimeMillis() - startTimstamp) / (1000 * 60 * 60));
                if (i <= 24) {
                    Log.v("inside if getallevents", "" + i);
                    Log.v("event id", cursor.getString(cursor.getColumnIndex("id")));
                    show = new CaptureShowDTO();
                    show.setEventname(cursor.getString(cursor.getColumnIndex("eventname")));
                    show.setEventid(cursor.getString(cursor.getColumnIndex("id")));
                    captureShows.add(show);
                } else {
                    //Log.v("inside else getallevents", "" + i);
                }
            }
        }

        return captureShows;
    }

    public boolean checkEventName(String eventname, String userid, String date) {
        boolean check = false;
        String sql = "select * from capture_show where userid = '" + userid + "' and userdate = '" + date + "'" +
                " and eventname = '" + eventname + "'";

        Cursor cursor = DBAdapter.myDataBase.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                check = true;
            }
        }

        return check;
    }

    public ArrayList<TrackUser> getTrackUserInfo() {


        ArrayList<TrackUser> trackUserArrayList = new ArrayList<TrackUser>();
        TrackUser trackUser;


        String[] allColumn = {"parent_id", "user_id", "latitude", "longitude", "date", "time",
                "battery_persentage", "network_type", "cell_id_short", "cell_id_long", "lac", "mnc", "mcc",
                "device_type", "basestation_id", "basestation_latitude", "basestation_longitude", "sentStatus", "shutdownTime", "restartTime", "signalStrenght", "networkSubType", "profileType", "operatorType"};

        String sql = "select * from TrackUser";
        try {
            //myDataBase = this.getReadableDatabase();
            //createDataBase();
            //openDataBase();
            myDataBase = this.getReadableDatabase();
            Cursor cursor = myDataBase.query("TrackUser",
                    allColumn, null, null, null, null, null);

            //Cursor cursor = myDataBase.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    trackUser = new TrackUser();


                    trackUser.setParent_id(cursor.getString(cursor.getColumnIndex("parent_id")));
                    trackUser.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                    trackUser.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                    trackUser.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
                    trackUser.setDate(cursor.getString(cursor.getColumnIndex("date")));
                    trackUser.setTime(cursor.getString(cursor.getColumnIndex("time")));
                    trackUser.setBattery_persentage(cursor.getString(cursor.getColumnIndex("battery_persentage")));
                    trackUser.setNetwork_type(cursor.getString(cursor.getColumnIndex("network_type")));

                    trackUser.setCellIdShort(cursor.getString(cursor.getColumnIndex("cell_id_short")));
                    trackUser.setCellIdLong(cursor.getString(cursor.getColumnIndex("cell_id_long")));
                    trackUser.setLac(cursor.getString(cursor.getColumnIndex("lac")));
                    trackUser.setMnc(cursor.getString(cursor.getColumnIndex("mnc")));
                    trackUser.setMcc(cursor.getString(cursor.getColumnIndex("mcc")));

                    trackUser.setDevice_type(cursor.getString(cursor.getColumnIndex("device_type")));
                    trackUser.setBasestation_id(cursor.getString(cursor.getColumnIndex("basestation_id")));
                    trackUser.setBasestation_latitude(cursor.getString(cursor.getColumnIndex("basestation_latitude")));
                    trackUser.setBasestation_longitude(cursor.getString(cursor.getColumnIndex("basestation_longitude")));

                    trackUser.setSentStatus(cursor.getString(cursor.getColumnIndex("sentStatus")));
                    trackUser.setShutdownTime(cursor.getString(cursor.getColumnIndex("shutdownTime")));
                    trackUser.setRestartTime(cursor.getString(cursor.getColumnIndex("restartTime")));
                    trackUser.setSignalStrenght(cursor.getString(cursor.getColumnIndex("signalStrenght")));

                    trackUser.setNetworkSubType(cursor.getString(cursor.getColumnIndex("networkSubType")));
                    trackUser.setProfileType(cursor.getString(cursor.getColumnIndex("profileType")));
                    trackUser.setOperatorType(cursor.getString(cursor.getColumnIndex("operatorType")));

                    trackUserArrayList.add(trackUser);


                }
            }
        } /*catch (IOException se) {
            se.printStackTrace();

        }*/ catch (SQLiteException se) {
            se.printStackTrace();

        }
        return trackUserArrayList;

    }

    public ArrayList<ContactDTO> getContactsList() {

        ArrayList<ContactDTO> captureShows = new ArrayList<ContactDTO>();
        ContactDTO show;


        String[] allColumn = {"parent_id", "creator_id", "compName", "contTitle",
                "contFirstName", "contLastName", "contJobTitle", "contPhoneNo", "contMobileNo", "contEmailID",
                "contProfilePic", "compID", "contId", "contPhotoId"};


        Cursor cursor = myDataBase.query("ContactList",
                allColumn, null, null, null, null, null);

        //Cursor cursor = myDataBase.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                show = new ContactDTO();
                show.setParent_id(cursor.getString(cursor.getColumnIndex("parent_id")));
                show.setCreator_id(cursor.getString(cursor.getColumnIndex("creator_id")));
                show.setCompId(cursor.getString(cursor.getColumnIndex("compID")));
                show.setContId(cursor.getString(cursor.getColumnIndex("contId")));
                show.setComp_name(cursor.getString(cursor.getColumnIndex("compName")));

                show.setContTitle(cursor.getString(cursor.getColumnIndex("contTitle")));


                show.setContFirstName(cursor.getString(cursor.getColumnIndex("contFirstName")));
                show.setContLastName(cursor.getString(cursor.getColumnIndex("contLastName")));
                show.setContJobTitle(cursor.getString(cursor.getColumnIndex("contJobTitle")));

                show.setContPhoneNum(cursor.getString(cursor.getColumnIndex("contPhoneNo")));
                show.setContMobileNum(cursor.getString(cursor.getColumnIndex("contMobileNo")));

                show.setContEmailId(cursor.getString(cursor.getColumnIndex("contEmailID")));

                show.setContProfilePic(cursor.getString(cursor.getColumnIndex("contProfilePic")));
                show.setContPhotoId(cursor.getString(cursor.getColumnIndex("contPhotoId")));


                captureShows.add(show);


            }
        }
        // } catch (SQLiteException sqe) {
        //     sqe.printStackTrace();

        //  }
        return captureShows;
    }

    public ArrayList<Companydto> getCompany() {

        ArrayList<Companydto> captureShows = new ArrayList<Companydto>();
        Companydto show;

        String[] allColumn = {"parent_id", "creator_id", "Companyname", "Address", "City", "State", "PinCode", "Country", "CompWebsite", "CompPhoneNo", "CompEmailID", "CompIndustry", "ContTitle",
                "ContFName", "ContLName", "ContJobTitle", "ContPhoneNo", "ContMobileNo", "ContEmailID", "Note", "Latitude", "Longitude", "Photo", "CompId", "ContId", "CompLogo"};


        Cursor cursor = myDataBase.query("Company",
                allColumn, null, null, null, null, null);

        //Cursor cursor = myDataBase.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                show = new Companydto();
                show.setParent_id(cursor.getString(cursor.getColumnIndex("parent_id")));
                show.setCreator_id(cursor.getString(cursor.getColumnIndex("creator_id")));
                show.setComp_name(cursor.getString(cursor.getColumnIndex("Companyname")));
                show.setC_add(cursor.getString(cursor.getColumnIndex("Address")));
                show.setC_city(cursor.getString(cursor.getColumnIndex("City")));
                show.setC_state(cursor.getString(cursor.getColumnIndex("State")));
                show.setC_pincode(cursor.getString(cursor.getColumnIndex("PinCode")));
                show.setC_country(cursor.getString(cursor.getColumnIndex("Country")));

                show.setCompWebsite(cursor.getString(cursor.getColumnIndex("CompWebsite")));
                show.setCompPhoneNo(cursor.getString(cursor.getColumnIndex("CompPhoneNo")));
                show.setCompEmailId(cursor.getString(cursor.getColumnIndex("CompEmailID")));
                show.setCompIndustry(cursor.getString(cursor.getColumnIndex("CompIndustry")));
                show.setContTitle(cursor.getString(cursor.getColumnIndex("ContTitle")));


                show.setContFirstName(cursor.getString(cursor.getColumnIndex("ContFName")));
                show.setContLastName(cursor.getString(cursor.getColumnIndex("ContLName")));
                show.setContJobTitle(cursor.getString(cursor.getColumnIndex("ContJobTitle")));

                show.setPhno(cursor.getString(cursor.getColumnIndex("ContPhoneNo")));
                show.setMobno(cursor.getString(cursor.getColumnIndex("ContMobileNo")));
                show.setEmailid(cursor.getString(cursor.getColumnIndex("ContEmailID")));
                show.setNote(cursor.getString(cursor.getColumnIndex("Note")));
                show.setLat(cursor.getString(cursor.getColumnIndex("Latitude")));
                show.setLog(cursor.getString(cursor.getColumnIndex("Longitude")));
                show.setContProfilePic(cursor.getString(cursor.getColumnIndex("Photo")));
                show.setComp_id(cursor.getString(cursor.getColumnIndex("CompId")));
                show.setContId(cursor.getString(cursor.getColumnIndex("ContId")));
                show.setCompLogo(cursor.getString(cursor.getColumnIndex("CompLogo")));

                captureShows.add(show);


            }
        }
        // } catch (SQLiteException sqe) {
        //     sqe.printStackTrace();

        //  }
        return captureShows;
    }

    public ArrayList<ContactDTO> getContacts() {

        ArrayList<ContactDTO> captureShows = new ArrayList<ContactDTO>();
        ContactDTO show;


        String[] allColumn = {"parent_id", "creator_id", "CompId", "Companyname", "compAdd", "compCity",
                "compState", "compPincode", "compCountry", "compWebsite", "compPhoneNo", "compEmailID",
                "compIndustry", "compLogo", "contId", "contTitle",
                "contFirstName", "contLastName", "contJobTitle", "contPhoneNo", "contMobileNo", "contEmailID",
                "contNote", "contLatitude", "contLongitude", "contProfilePic"};


        Cursor cursor = myDataBase.query("Contact",
                allColumn, null, null, null, null, null);

        //Cursor cursor = myDataBase.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                show = new ContactDTO();
                show.setParent_id(cursor.getString(cursor.getColumnIndex("parent_id")));
                show.setCreator_id(cursor.getString(cursor.getColumnIndex("creator_id")));
                show.setCompId(cursor.getString(cursor.getColumnIndex("CompId")));
                show.setCompName(cursor.getString(cursor.getColumnIndex("Companyname")));
                show.setCompAdd(cursor.getString(cursor.getColumnIndex("compAdd")));

                show.setCompCity(cursor.getString(cursor.getColumnIndex("compCity")));

                show.setCompState(cursor.getString(cursor.getColumnIndex("compState")));
                show.setCompPincode(cursor.getString(cursor.getColumnIndex("compPincode")));

                show.setCompCountry(cursor.getString(cursor.getColumnIndex("compCountry")));

                show.setCompWebsite(cursor.getString(cursor.getColumnIndex("compWebsite")));

                show.setCompPhoneNo(cursor.getString(cursor.getColumnIndex("compPhoneNo")));

                show.setCompEmailID(cursor.getString(cursor.getColumnIndex("compEmailID")));

                show.setCompIndustry(cursor.getString(cursor.getColumnIndex("compIndustry")));
                show.setCompLogo(cursor.getString(cursor.getColumnIndex("compLogo")));
                show.setContId(cursor.getString(cursor.getColumnIndex("contId")));
                show.setContTitle(cursor.getString(cursor.getColumnIndex("contTitle")));


                show.setContFirstName(cursor.getString(cursor.getColumnIndex("contFirstName")));
                show.setContLastName(cursor.getString(cursor.getColumnIndex("contLastName")));
                show.setContJobTitle(cursor.getString(cursor.getColumnIndex("contJobTitle")));

                show.setContPhoneNum(cursor.getString(cursor.getColumnIndex("contPhoneNo")));
                show.setContMobileNum(cursor.getString(cursor.getColumnIndex("contMobileNo")));

                show.setContEmailId(cursor.getString(cursor.getColumnIndex("contEmailID")));
                show.setContNote(cursor.getString(cursor.getColumnIndex("contNote")));
                show.setContLatitude(cursor.getString(cursor.getColumnIndex("contLatitude")));
                show.setContLongitude(cursor.getString(cursor.getColumnIndex("contLongitude")));
                show.setContProfilePic(cursor.getString(cursor.getColumnIndex("contProfilePic")));


                captureShows.add(show);


            }
        }
        // } catch (SQLiteException sqe) {
        //     sqe.printStackTrace();

        //  }
        return captureShows;
    }

    public ArrayList<Companydto> getCompany_Info() {


        ArrayList<Companydto> captureShows = new ArrayList<Companydto>();
        Companydto show;
        try {
            //String sql = "select * from company_info";
            myDataBase = this.getReadableDatabase();
            //  try {
            Cursor cursor = myDataBase.query("company_info",
                    allColumns, null, null, null, null, null);

            //Cursor cursor = myDataBase.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    show = new Companydto();
                    show.setParent_id(cursor.getString(cursor.getColumnIndex("parent_id")));
                    show.setCreator_id(cursor.getString(cursor.getColumnIndex("creator_id")));
                    show.setComp_name(cursor.getString(cursor.getColumnIndex("Companyname")));
                    show.setC_add(cursor.getString(cursor.getColumnIndex("Address")));
                    show.setC_city(cursor.getString(cursor.getColumnIndex("City")));
                    show.setC_state(cursor.getString(cursor.getColumnIndex("State")));
                    show.setC_pincode(cursor.getString(cursor.getColumnIndex("PinCode")));
                    show.setC_country(cursor.getString(cursor.getColumnIndex("Country")));

                    show.setCompWebsite(cursor.getString(cursor.getColumnIndex("CompWebsite")));
                    show.setCompPhoneNo(cursor.getString(cursor.getColumnIndex("CompPhoneNo")));
                    show.setCompEmailId(cursor.getString(cursor.getColumnIndex("CompEmailID")));
                    show.setCompIndustry(cursor.getString(cursor.getColumnIndex("CompIndustry")));
                    show.setContTitle(cursor.getString(cursor.getColumnIndex("ContTitle")));


                    show.setContFirstName(cursor.getString(cursor.getColumnIndex("ContFName")));
                    show.setContLastName(cursor.getString(cursor.getColumnIndex("ContLName")));
                    show.setContJobTitle(cursor.getString(cursor.getColumnIndex("ContJobTitle")));

                    show.setPhno(cursor.getString(cursor.getColumnIndex("ContPhoneNo")));
                    show.setMobno(cursor.getString(cursor.getColumnIndex("ContMobileNo")));
                    show.setEmailid(cursor.getString(cursor.getColumnIndex("ContEmailID")));
                    show.setNote(cursor.getString(cursor.getColumnIndex("Note")));
                    show.setLat(cursor.getString(cursor.getColumnIndex("Latitude")));
                    show.setLog(cursor.getString(cursor.getColumnIndex("Longitude")));
                    show.setPhoto(cursor.getString(cursor.getColumnIndex("Photo")));
                    show.setComp_id(cursor.getString(cursor.getColumnIndex("compId")));
                    show.setContProfilePic(cursor.getString(cursor.getColumnIndex("contProfilePic")));
                    show.setCompLogo(cursor.getString(cursor.getColumnIndex("CompLogo")));
                    captureShows.add(show);


                }
            }
        } catch (SQLiteException se) {
            se.printStackTrace();

        }
        // } catch (SQLiteException sqe) {
        //     sqe.printStackTrace();


        //  }
        return captureShows;
    }

    public ArrayList<ActivityDTO> getActivityList() {


        String[] allColumnsActivity = {"parent_id", "creator_id", "actTitle", "actDetail", "leadTitle", "actRemark", "actDate", "actTime",
                "compId", "contTitle", "contFirstName", "contLastName", "contJobTitle",
                "contPhoneNo", "contMobileNo", "contEmailID", "contNote", "compName", "latitude", "longitude", "compAdd", "compCity",
                "compState", "compPincode", "compCountry", "compWebsite", "compPhoneNo", "compEmailID", "compIndustry", "compLogo", "contProfilePic", "actPhoto", "actId", "selectedContactIds"};
        ArrayList<ActivityDTO> captureShows = new ArrayList<ActivityDTO>();
        ActivityDTO show;
        try {
            // String sql = "select * from Lead";
            myDataBase = this.getReadableDatabase();
            //  try {
            Cursor cursor = myDataBase.query("Activity",
                    allColumnsActivity, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    show = new ActivityDTO();


                    show.setParent_id(cursor.getString(cursor.getColumnIndex("parent_id")));
                    show.setCreator_id(cursor.getString(cursor.getColumnIndex("creator_id")));

                    show.setActTitle(cursor.getString(cursor.getColumnIndex("actTitle")));
                    show.setLeadTitle(cursor.getString(cursor.getColumnIndex("leadTitle")));

                    show.setActDetail(cursor.getString(cursor.getColumnIndex("actDetail")));

                    show.setActRemark(cursor.getString(cursor.getColumnIndex("actRemark")));

                    show.setActDate(cursor.getString(cursor.getColumnIndex("actDate")));

                    show.setActTime(cursor.getString(cursor.getColumnIndex("actTime")));


                    show.setCompId(cursor.getString(cursor.getColumnIndex("compId")));

                    show.setContTitle(cursor.getString(cursor.getColumnIndex("contTitle")));

                    show.setContFirstName(cursor.getString(cursor.getColumnIndex("contFirstName")));
                    show.setContLastName(cursor.getString(cursor.getColumnIndex("contLastName")));

                    show.setContJobTitle(cursor.getString(cursor.getColumnIndex("contJobTitle")));


                    show.setContPhoneNo(cursor.getString(cursor.getColumnIndex("contPhoneNo")));
                    show.setContMobileNo(cursor.getString(cursor.getColumnIndex("contMobileNo")));
                    show.setContEmailID(cursor.getString(cursor.getColumnIndex("contEmailID")));
                    show.setContNote(cursor.getString(cursor.getColumnIndex("contNote")));

                    show.setActPhoto(cursor.getString(cursor.getColumnIndex("actPhoto")));

                    show.setCompName(cursor.getString(cursor.getColumnIndex("compName")));
                    show.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                    show.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
                    show.setCompAdd(cursor.getString(cursor.getColumnIndex("compAdd")));
                    show.setCompCity(cursor.getString(cursor.getColumnIndex("compCity")));
                    show.setCompState(cursor.getString(cursor.getColumnIndex("compState")));
                    show.setCompPincode(cursor.getString(cursor.getColumnIndex("compPincode")));
                    show.setCompCountry(cursor.getString(cursor.getColumnIndex("compCountry")));
                    show.setCompWebsite(cursor.getString(cursor.getColumnIndex("compWebsite")));

                    show.setCompPhoneNo(cursor.getString(cursor.getColumnIndex("compPhoneNo")));
                    show.setCompEmailID(cursor.getString(cursor.getColumnIndex("compEmailID")));
                    show.setCompIndustry(cursor.getString(cursor.getColumnIndex("compIndustry")));
                    show.setCompLogo(cursor.getString(cursor.getColumnIndex("compLogo")));
                    show.setContProfilePic(cursor.getString(cursor.getColumnIndex("contProfilePic")));
                    show.setActId(cursor.getString(cursor.getColumnIndex("actId")));
                    show.setSelectedContactIds(cursor.getString(cursor.getColumnIndex("selectedContactIds")));

                    captureShows.add(show);
                }
            }
        } catch (SQLiteException se) {
            se.printStackTrace();
        }

        return captureShows;
    }

    public ArrayList<Leaddto> getLead_Info() {


        String[] allColumnsLeads = {"parent_id", "creator_id", "date", "time", "leadTitle", "leadDetail", "leadClosingDate", "leadStatus",
                "leadSource", "compId", "contTitle", "contFirstName", "contLastName", "contJobTitle",
                "contPhoneNo", "contMobileNo", "contEmailID", "contNote", "Photo", "compName", "compAdd", "compCity", "compState",
                "compPincode", "compCountry", "compWebsite", "compPhoneNumber",
                "compEmailID", "compIndustry", "compLogo", "compLatitude", "compLongitude", "leadId", "leadBudget", "selectedContactIds"};
        ArrayList<Leaddto> captureShows = new ArrayList<Leaddto>();
        Leaddto show;

        // String sql = "select * from Lead";
        myDataBase = this.getReadableDatabase();
        //  try {
        Cursor cursor = myDataBase.query("Lead",
                allColumnsLeads, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                show = new Leaddto();
                show.setParent_id(cursor.getString(cursor.getColumnIndex("parent_id")));
                show.setCreater_id(cursor.getString(cursor.getColumnIndex("creator_id")));

                show.setDate(cursor.getString(cursor.getColumnIndex("date")));
                show.setLeadTitle(cursor.getString(cursor.getColumnIndex("leadTitle")));
                show.setTime(cursor.getString(cursor.getColumnIndex("time")));

                show.setLeadDetail(cursor.getString(cursor.getColumnIndex("leadDetail")));

                show.setLeadClosingTime(cursor.getString(cursor.getColumnIndex("leadClosingDate")));
                show.setLeadBudget(cursor.getString(cursor.getColumnIndex("leadBudget")));

                show.setLeadStatus(cursor.getString(cursor.getColumnIndex("leadStatus")));

                show.setLeadSource(cursor.getString(cursor.getColumnIndex("leadSource")));

                show.setCompId(cursor.getString(cursor.getColumnIndex("compId")));

                show.setCompname(cursor.getString(cursor.getColumnIndex("compName")));
                show.setCompAdd(cursor.getString(cursor.getColumnIndex("compAdd")));

                show.setCompCity(cursor.getString(cursor.getColumnIndex("compCity")));
                show.setCompState(cursor.getString(cursor.getColumnIndex("compState")));
                show.setCompPincode(cursor.getString(cursor.getColumnIndex("compPincode")));
                show.setCompCountry(cursor.getString(cursor.getColumnIndex("compCountry")));

                show.setCompWebsite(cursor.getString(cursor.getColumnIndex("compWebsite")));
                show.setCompPhoneNumber(cursor.getString(cursor.getColumnIndex("compPhoneNumber")));

                show.setCompEmailID(cursor.getString(cursor.getColumnIndex("compEmailID")));

                show.setCompIndustry(cursor.getString(cursor.getColumnIndex("compIndustry")));
                show.setCompLogo(cursor.getString(cursor.getColumnIndex("compLogo")));

                show.setCompLat(cursor.getString(cursor.getColumnIndex("compLatitude")));
                show.setCompLong(cursor.getString(cursor.getColumnIndex("compLongitude")));


                show.setContTitle(cursor.getString(cursor.getColumnIndex("contTitle")));

                show.setContFirstNAme(cursor.getString(cursor.getColumnIndex("contFirstName")));
                show.setContLastName(cursor.getString(cursor.getColumnIndex("contLastName")));

                show.setContJobTitle(cursor.getString(cursor.getColumnIndex("contJobTitle")));


                show.setContPhoneNo(cursor.getString(cursor.getColumnIndex("contPhoneNo")));
                show.setContMobileNo(cursor.getString(cursor.getColumnIndex("contMobileNo")));
                show.setContEmailId(cursor.getString(cursor.getColumnIndex("contEmailID")));
                show.setContNote(cursor.getString(cursor.getColumnIndex("contNote")));
                show.setPhoto(cursor.getString(cursor.getColumnIndex("Photo")));
                show.setLead_id(cursor.getString(cursor.getColumnIndex("leadId")));
                show.setSelectedContactIds(cursor.getString(cursor.getColumnIndex("selectedContactIds")));


                captureShows.add(show);
            }
        }
        return captureShows;
    }

    public ArrayList<Event> getEventList() {


        String[] allColumnsEvent = {"parentId", "creatorId", "eventId", "eventTitle", "eventDetail", "eventStartTime", "eventEndTime",
                "eventLocation", "eventRemark", "compId", "compName", "compAdd", "compCity", "compState", "compPincode", "compCountry",
                "compWebsite", "compPhoneNo", "compEmailID", "compIndustry", "compLogo", "contId", "contTitle", "contFirstName",
                "contLastName", "contJobTitle", "contPhoneNo", "contMobileNo", "contEmailID", "contNote", "contProfilePic",
                "eventLatitude", "eventLogitude", "eventPhoto", "selectedContactIds"
        };
        ArrayList<Event> captureShows = new ArrayList<Event>();
        Event show;

        // String sql = "select * from Lead";
        myDataBase = this.getReadableDatabase();
        try {
            Cursor cursor = myDataBase.query("Event",
                    allColumnsEvent, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    show = new Event();


                    show.setParentId(cursor.getString(cursor.getColumnIndex("parentId")));
                    show.setCreatorId(cursor.getString(cursor.getColumnIndex("creatorId")));

                    show.setEventId(cursor.getString(cursor.getColumnIndex("eventId")));
                    show.setEventTitle(cursor.getString(cursor.getColumnIndex("eventTitle")));
                    show.setEventDetail(cursor.getString(cursor.getColumnIndex("eventDetail")));

                    show.setEventStartTime(cursor.getString(cursor.getColumnIndex("eventStartTime")));

                    show.setEventEndTime(cursor.getString(cursor.getColumnIndex("eventEndTime")));
                    show.setEventLocation(cursor.getString(cursor.getColumnIndex("eventLocation")));

                    show.setEventRemarks(cursor.getString(cursor.getColumnIndex("eventRemark")));

                    show.setComp_id(cursor.getString(cursor.getColumnIndex("compId")));

                    show.setCompName(cursor.getString(cursor.getColumnIndex("compName")));
                    show.setCompAdd(cursor.getString(cursor.getColumnIndex("compAdd")));
                    show.setCompCity(cursor.getString(cursor.getColumnIndex("compCity")));
                    show.setCompState(cursor.getString(cursor.getColumnIndex("compState")));

                    show.setCompPin(cursor.getString(cursor.getColumnIndex("compPincode")));
                    show.setCompCountry(cursor.getString(cursor.getColumnIndex("compCountry")));
                    show.setCompWeb(cursor.getString(cursor.getColumnIndex("compWebsite")));
                    show.setCompphone(cursor.getString(cursor.getColumnIndex("compPhoneNo")));
                    show.setCompEmail(cursor.getString(cursor.getColumnIndex("compEmailID")));

                    show.setCompIndustry(cursor.getString(cursor.getColumnIndex("compIndustry")));
                    show.setCompLogo(cursor.getString(cursor.getColumnIndex("compLogo")));
                    show.setContId(cursor.getString(cursor.getColumnIndex("contId")));


                    show.setContTitle(cursor.getString(cursor.getColumnIndex("contTitle")));


                    show.setContFirstName(cursor.getString(cursor.getColumnIndex("contFirstName")));
                    show.setContLastName(cursor.getString(cursor.getColumnIndex("contLastName")));

                    show.setContJobTitle(cursor.getString(cursor.getColumnIndex("contJobTitle")));


                    show.setContPhoneNumber(cursor.getString(cursor.getColumnIndex("contPhoneNo")));
                    show.setContMob(cursor.getString(cursor.getColumnIndex("contMobileNo")));
                    show.setContEmail(cursor.getString(cursor.getColumnIndex("contEmailID")));
                    show.setContNote(cursor.getString(cursor.getColumnIndex("contNote")));
                    show.setContProfilePic(cursor.getString(cursor.getColumnIndex("contProfilePic")));


                    show.setEventLatitude(cursor.getString(cursor.getColumnIndex("eventLatitude")));
                    show.setEventLongitude(cursor.getString(cursor.getColumnIndex("eventLogitude")));
                    show.setEventPhoto(cursor.getString(cursor.getColumnIndex("eventPhoto")));
                    show.setSelectedContactIds(cursor.getString(cursor.getColumnIndex("selectedContactIds")));


                    captureShows.add(show);
                }
            }
        } catch (SQLiteException se) {
            se.printStackTrace();
        }
        return captureShows;
    }

    public ArrayList<Leaddto> getDeal_Info() {
        ArrayList<Leaddto> captureShows = new ArrayList<Leaddto>();
        Leaddto show;
        String sql = "select * from dealer_info_new";
        Cursor cursor = DBAdapter.myDataBase.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                show = new Leaddto();
                show.setParent_id(cursor.getString(cursor.getColumnIndex("parent_id")));
                show.setCreater_id(cursor.getString(cursor.getColumnIndex("creator_id")));
                show.setLead_name(cursor.getString(cursor.getColumnIndex("lead_name")));
                show.setDate(cursor.getString(cursor.getColumnIndex("date")));
                show.setTime(cursor.getString(cursor.getColumnIndex("time")));
                show.setC_name(cursor.getString(cursor.getColumnIndex("c_name")));
                show.setLat(cursor.getString(cursor.getColumnIndex("lat")));
                show.setLog(cursor.getString(cursor.getColumnIndex("long")));
                show.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                show.setName(cursor.getString(cursor.getColumnIndex("name")));
                show.setPhno(cursor.getString(cursor.getColumnIndex("phno")));
                show.setEmailid(cursor.getString(cursor.getColumnIndex("emailid")));
                show.setNote(cursor.getString(cursor.getColumnIndex("note")));
                show.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));

                captureShows.add(show);
            }
        }
        return captureShows;
    }

    public ArrayList<Leaddto> getSale_Info() {
        ArrayList<Leaddto> captureShows = new ArrayList<Leaddto>();
        Leaddto show;
        String sql = "select * from sales_info_new";
        Cursor cursor = DBAdapter.myDataBase.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                show = new Leaddto();
                show.setParent_id(cursor.getString(cursor.getColumnIndex("parent_id")));
                show.setCreater_id(cursor.getString(cursor.getColumnIndex("creator_id")));
                show.setLead_name(cursor.getString(cursor.getColumnIndex("lead_name")));
                show.setDate(cursor.getString(cursor.getColumnIndex("date")));
                show.setTime(cursor.getString(cursor.getColumnIndex("time")));
                show.setC_name(cursor.getString(cursor.getColumnIndex("c_name")));
                show.setLat(cursor.getString(cursor.getColumnIndex("lat")));
                show.setLog(cursor.getString(cursor.getColumnIndex("long")));
                show.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                show.setName(cursor.getString(cursor.getColumnIndex("name")));
                show.setPhno(cursor.getString(cursor.getColumnIndex("phno")));
                show.setEmailid(cursor.getString(cursor.getColumnIndex("emailid")));
                show.setNote(cursor.getString(cursor.getColumnIndex("note")));
                show.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));

                captureShows.add(show);
            }
        }
        return captureShows;
    }

    public ArrayList<ContactDTO> getContactList(String compId) {
        ArrayList<ContactDTO> captureShows = new ArrayList<ContactDTO>();
        ContactDTO show;
        String sql = "select * from ContactList Where compID = " + compId;
        myDataBase = this.getReadableDatabase();
        Cursor cursor = myDataBase.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                show = new ContactDTO();


                show.setParent_id(cursor.getString(cursor.getColumnIndex("parent_id")));
                show.setCreator_id(cursor.getString(cursor.getColumnIndex("creator_id")));
                show.setContId(cursor.getString(cursor.getColumnIndex("contId")));
                show.setCompId(cursor.getString(cursor.getColumnIndex("compID")));
                show.setCompName(cursor.getString(cursor.getColumnIndex("compName")));
                show.setContTitle(cursor.getString(cursor.getColumnIndex("contTitle")));
                show.setContFirstName(cursor.getString(cursor.getColumnIndex("contFirstName")));
                show.setContLastName(cursor.getString(cursor.getColumnIndex("contLastName")));
                show.setContJobTitle(cursor.getString(cursor.getColumnIndex("contJobTitle")));
                show.setContPhoneNum(cursor.getString(cursor.getColumnIndex("contPhoneNo")));

                show.setContMobileNum(cursor.getString(cursor.getColumnIndex("contMobileNo")));
                show.setContEmailId(cursor.getString(cursor.getColumnIndex("contEmailID")));
                show.setContProfilePic(cursor.getString(cursor.getColumnIndex("contProfilePic")));
                show.setContPhotoId(cursor.getString(cursor.getColumnIndex("contPhotoId")));


                captureShows.add(show);
            }
        }
        return captureShows;
    }


    public boolean CheckIsDataAlreadyInDBorNot(String TableName,
                                               String dbfield, String fieldValue) {
        String Query = "";
        //myDataBase = this.getReadableDatabase();
        // SQLiteDatabase sqldb = EGLifeStyleApplication.sqLiteDatabase;
        try {
            if (dbfield.equals("leadTitle")) {
                Query = "Select * from " + TableName + " where " + dbfield + " = '" + fieldValue + "'";
            } else {
                Query = "Select * from " + TableName + " where " + dbfield + " = " + fieldValue;
            }

            Cursor cursor = myDataBase.rawQuery(Query, null);
            if (cursor.getCount() <= 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public int updateRowLeads(Leaddto leaddto, String flag) {

        ContentValues contentValues = new ContentValues();


        contentValues.put("parent_id", leaddto.getParent_id());
        contentValues.put("creator_id", leaddto.getCreater_id());

        contentValues.put("date", leaddto.getDate());
        contentValues.put("time", leaddto.getTime());

        contentValues.put("leadTitle", leaddto.getLeadTitle());
        contentValues.put("leadDetail", leaddto.getLeadDetail());
        contentValues.put("leadClosingDate", leaddto.getLeadClosingTime());
        contentValues.put("leadBudget", leaddto.getLeadBudget());
        contentValues.put("leadStatus", leaddto.getLeadStatus());
        contentValues.put("leadSource", leaddto.getLeadSource());

        contentValues.put("compId", leaddto.getCompId());
        contentValues.put("contTitle", leaddto.getContTitle());
        contentValues.put("contFirstName", leaddto.getContFirstNAme());
        contentValues.put("contLastName", leaddto.getContLastName());

        contentValues.put("contJobTitle", leaddto.getContJobTitle());
        contentValues.put("contPhoneNo", leaddto.getContPhoneNo());
        contentValues.put("contMobileNo", leaddto.getContMobileNo());
        contentValues.put("contEmailID", leaddto.getContEmailId());
        contentValues.put("contNote", leaddto.getContNote());

        contentValues.put("Photo", leaddto.getPhoto());
        contentValues.put("compName", leaddto.getCompname());

        contentValues.put("compAdd", leaddto.getCompAdd());
        contentValues.put("compCity", leaddto.getCompCity());
        contentValues.put("compState", leaddto.getCompState());

        contentValues.put("compPincode", leaddto.getCompPincode());

        contentValues.put("compCountry", leaddto.getCompCountry());

        contentValues.put("compWebsite", leaddto.getCompWebsite());

        contentValues.put("compPhoneNumber", leaddto.getCompPhoneNumber());
        contentValues.put("compEmailID", leaddto.getCompEmailID());

        contentValues.put("compIndustry", leaddto.getCompIndustry());
        contentValues.put("compLogo", leaddto.getCompLogo());
        contentValues.put("compLatitude", leaddto.getCompLat());
        contentValues.put("compLongitude", leaddto.getCompLong());

        contentValues.put("leadId", leaddto.getLead_id());
        contentValues.put("selectedContactIds", leaddto.getSelectedContactIds());
        int rowUpdated = 0;
        if (flag.equals("ID")) {
            rowUpdated = myDataBase.update("Lead", contentValues, "leadId" + "=" + leaddto.getLead_id(), null);
        } else if (flag.equals("TITLE")) {
            rowUpdated = myDataBase.update("Lead", contentValues, "leadTitle" + "=" + leaddto.getLeadTitle(), null);
        }

        return rowUpdated;
    }

    public int updateTrackData(TrackUser trackUser) {

        long row_id = -1;

        // DBAdapter.myDataBase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("parent_id", trackUser.getParent_id());
        contentValues.put("user_id", trackUser.getUser_id());
        contentValues.put("latitude", trackUser.getLatitude());
        contentValues.put("longitude", trackUser.getLongitude());
        contentValues.put("date", trackUser.getDate());
        contentValues.put("time", trackUser.getTime());
        contentValues.put("battery_persentage", trackUser.getBattery_persentage());
        contentValues.put("network_type", trackUser.getNetwork_type());

        contentValues.put("cell_id_short", trackUser.getCellIdShort());
        contentValues.put("cell_id_long", trackUser.getCellIdLong());
        contentValues.put("lac", trackUser.getLac());
        contentValues.put("mnc", trackUser.getMnc());
        contentValues.put("mcc", trackUser.getMcc());

        contentValues.put("device_type", trackUser.getDevice_type());
        contentValues.put("basestation_id", trackUser.getBasestation_id());
        contentValues.put("basestation_latitude", trackUser.getBasestation_latitude());
        contentValues.put("basestation_longitude", trackUser.getBasestation_longitude());

        contentValues.put("sentStatus", trackUser.getSentStatus());
        contentValues.put("shutdownTime", trackUser.getShutdownTime());
        contentValues.put("restartTime", trackUser.getRestartTime());
        contentValues.put("signalStrenght", trackUser.getSignalStrenght());
        contentValues.put("networkSubType", trackUser.getNetworkSubType());
        contentValues.put("profileType", trackUser.getProfileType());
        contentValues.put("operatorType", trackUser.getOperatorType());

      //  int rowUpdated = 0;

        row_id = myDataBase.update("TrackUser", contentValues, "time" + " = '" + trackUser.getTime() + "' ", null);

        System.out.println("update row_id:" + row_id);

        return (int) row_id;
    }

    public int updateRowActivity(ActivityDTO activity) {

        int rowUpdated = 0;
        ContentValues contentValues = new ContentValues();
        try {

            contentValues.put("parent_id", activity.getParent_id());
            contentValues.put("creator_id", activity.getCreator_id());

            contentValues.put("actTitle", activity.getActTitle());
            contentValues.put("actDetail", activity.getActDetail());

            contentValues.put("actRemark", activity.getActRemark());
            contentValues.put("actDate", activity.getActDate());
            contentValues.put("actTime", activity.getActTime());
            contentValues.put("leadTitle", activity.getLeadTitle());

            // contentValues.put("leadStatus", leaddto.getLeadStatus());
            //contentValues.put("leadSource", leaddto.getLeadSource());

            contentValues.put("compId", activity.getCompId());
            contentValues.put("contTitle", activity.getContTitle());
            contentValues.put("contFirstName", activity.getContFirstName());
            contentValues.put("contLastName", activity.getContLastName());

            contentValues.put("contJobTitle", activity.getContJobTitle());
            contentValues.put("contPhoneNo", activity.getContPhoneNo());
            contentValues.put("contMobileNo", activity.getContMobileNo());
            contentValues.put("contEmailID", activity.getContEmailID());
            contentValues.put("contNote", activity.getContNote());


            contentValues.put("compName", activity.getCompName());

            contentValues.put("compAdd", activity.getCompAdd());
            contentValues.put("compCity", activity.getCompCity());
            contentValues.put("compState", activity.getCompState());

            contentValues.put("compPincode", activity.getCompPincode());

            contentValues.put("compCountry", activity.getCompCountry());

            contentValues.put("compWebsite", activity.getCompWebsite());

            contentValues.put("compPhoneNo", activity.getCompPhoneNo());
            contentValues.put("compEmailID", activity.getCompEmailID());

            contentValues.put("compIndustry", activity.getCompIndustry());
            contentValues.put("compLogo", activity.getCompLogo());


            contentValues.put("latitude", activity.getLatitude());
            contentValues.put("longitude", activity.getLongitude());

            contentValues.put("contProfilePic", activity.getContProfilePic());
            contentValues.put("actPhoto", activity.getActPhoto());
            contentValues.put("actId", activity.getActId());
            contentValues.put("selectedContactIds", activity.getSelectedContactIds());


            rowUpdated = myDataBase.update("Activity", contentValues, "actId" + "=" + activity.getActId(), null);
        } catch (SQLiteException se) {
            se.printStackTrace();
            return rowUpdated;
        }
        return rowUpdated;
    }

    public int updateRowEvent(Event event) {
        int rowUpdated = 0;
        try {
            ContentValues contentValues = new ContentValues();


            contentValues.put("parentId", event.getParentId());

            contentValues.put("creatorId", event.getCreatorId());

            contentValues.put("eventId", event.getEventId());
            contentValues.put("eventTitle", event.getEventTitle());

            contentValues.put("eventDetail", event.getEventDetail());
            contentValues.put("eventStartTime", event.getEventStartTime());
            contentValues.put("eventEndTime", event.getEventEndTime());
            contentValues.put("eventLocation", event.getEventLocation());
            contentValues.put("eventRemark", event.getEventRemarks());

            contentValues.put("compId", event.getComp_id());

            contentValues.put("compName", event.getCompName());

            contentValues.put("compAdd", event.getCompAdd());
            contentValues.put("compCity", event.getCompCity());
            contentValues.put("compState", event.getCompState());

            contentValues.put("compPincode", event.getCompPin());

            contentValues.put("compCountry", event.getCompCountry());

            contentValues.put("compWebsite", event.getCompWeb());

            contentValues.put("compPhoneNo", event.getCompphone());
            contentValues.put("compEmailID", event.getCompEmail());

            contentValues.put("compIndustry", event.getCompIndustry());
            contentValues.put("compLogo", event.getCompLogo());


            contentValues.put("contId", event.getContId());

            contentValues.put("contTitle", event.getContTitle());
            contentValues.put("contFirstName", event.getContFirstName());
            contentValues.put("contLastName", event.getContLastName());

            contentValues.put("contJobTitle", event.getContJobTitle());
            contentValues.put("contPhoneNo", event.getContPhoneNumber());
            contentValues.put("contMobileNo", event.getContMob());
            contentValues.put("contEmailID", event.getContEmail());
            contentValues.put("contNote", event.getContNote());


            contentValues.put("contProfilePic", event.getContProfilePic());

            contentValues.put("eventLatitude", event.getEventLatitude());
            contentValues.put("eventLogitude", event.getEventLongitude());

            contentValues.put("eventPhoto", event.getEventPhoto());
            contentValues.put("selectedContactIds", event.getSelectedContactIds());


            rowUpdated = myDataBase.update("Event", contentValues, "eventId" + "=" + event.getEventId(), null);
        } catch (SQLiteException se) {
            se.printStackTrace();
            return rowUpdated;
        }
        return rowUpdated;
    }


    public boolean deleteRow(String tableName, String KEY_NAME, String Key_Value) {
        myDataBase = this.getWritableDatabase();
        int deletedRow = myDataBase.delete(tableName, KEY_NAME + " = '" + Key_Value + "' ", null);
        return deletedRow > 0;
    }

    public void delete_comp() {
        myDataBase = this.getWritableDatabase();
        String sql = "Delete from company_info";
        DBAdapter.myDataBase.execSQL(sql);

    }

    public void delete_companyList() {
        myDataBase = this.getWritableDatabase();
        String sql = "Delete from Company";
        DBAdapter.myDataBase.execSQL(sql);

    }


    public void delete_contact() {
        myDataBase = this.getWritableDatabase();
        String sql = "Delete from Contact";
        DBAdapter.myDataBase.execSQL(sql);

    }

    public void delete_activity() {
        myDataBase = this.getWritableDatabase();
        String sql = "Delete from Activity";
        DBAdapter.myDataBase.execSQL(sql);

    }

    public void delete_contact_list() {
        myDataBase = this.getWritableDatabase();
        String sql = "Delete from ContactList";

        DBAdapter.myDataBase.execSQL(sql);

    }

    public void delete_trackUser() {
        myDataBase = this.getWritableDatabase();
        String sql = "Delete from TrackUser";
        DBAdapter.myDataBase.execSQL(sql);

    }

    public void delete_Lead() {
        myDataBase = this.getWritableDatabase();
        String sql = "Delete from Lead";
        DBAdapter.myDataBase.execSQL(sql);

    }

    public void delete_Event() {
        myDataBase = this.getWritableDatabase();
        String sql = "Delete from Event";
        DBAdapter.myDataBase.execSQL(sql);

    }


    public void delete_lead() {
        String sql = "Delete from lead_info_new";
        DBAdapter.myDataBase.execSQL(sql);
    }

    public void delete_deal() {
        String sql = "Delete from dealer_info_new";
        DBAdapter.myDataBase.execSQL(sql);
    }

    public void delete_sale() {
        String sql = "Delete from sales_info_new";
        DBAdapter.myDataBase.execSQL(sql);
    }

}


