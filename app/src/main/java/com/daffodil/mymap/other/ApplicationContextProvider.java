package com.daffodil.mymap.other;

import android.app.Application;
import android.content.Context;

import com.daffodil.mymap.R;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(formKey = "", // will not be used
        mailTo = "roshni@daffodilglobal.com",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.toast_crash)
public class ApplicationContextProvider extends Application {

    /**
     * Keeps a reference of the application context
     */
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        // ACRA.init(this);
        try {
            sContext = getApplicationContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the application context
     *
     * @return application context
     */
    public static Context getContext() {

        return sContext;
    }

}
