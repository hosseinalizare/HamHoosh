package com.example.koohestantest1.classDirectory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;


public class HardwareIdsMobile {
    BaseCodeClass baseCodeClass = new BaseCodeClass();
    Context mContext;


    public HardwareIdsMobile(Context context) {
        mContext = context;
    }

    //   @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.DONUT)
    @SuppressLint("HardwareIds")
    public boolean getMobileConfig() {
        try {
            baseCodeClass.deviceModel = Build.MANUFACTURER
                    + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                    + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
        } catch (Exception e) {
            logMessage("ERROR IN Device Model", mContext);
            return false;
        }
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        //IMEI Code
        try {



//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                baseCodeClass.IMEI = "Q" + Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
//            } else {
//                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
//                    return false;
//                }
//                baseCodeClass.IMEI = tm.getDeviceId();
//            }
//            BaseCodeClass.IMEI = "IMEI No longer required";
            BaseCodeClass.IMEI = BaseCodeClass.CompanyID;


            return true;

        } catch (SecurityException ex) {
            logMessage(ex.getMessage(), mContext);
        } catch (IllegalArgumentException e) {
            logMessage(e.getMessage(), mContext);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            logMessage("ERROR IN IMEI", mContext);
        }
        return false;
    }//end public void getMobileConfig// end getMobileConfig()
}
