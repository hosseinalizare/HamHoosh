package com.example.koohestantest1.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtils {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final String SP_NAME = "InnerSP";
    private final String KEY_CART_NOTE = "cartnote";
    private final String KEY_COMPANY_NAME = "companyName";
    private final String KEY_COMPANY_ID = "companyId";
    private final String KEY_SESSION_ID = "sessionId";

    public SharedPreferenceUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void editCartNote(String note) {
        if (editor == null)
            editor = sharedPreferences.edit();
        editor.putString(KEY_CART_NOTE, note).apply();
    }

    public String getCartNote() {
        return sharedPreferences.getString(KEY_CART_NOTE, null);
    }

    public void resetCartNote() {
        if (editor == null)
            editor = sharedPreferences.edit();
        editor.remove(KEY_CART_NOTE).apply();
    }

    public void editCompanyName(String name) {
        if (editor == null)
            editor = sharedPreferences.edit();
        editor.putString(KEY_COMPANY_NAME, name).apply();
    }

    public String getCompanyName() {
        return sharedPreferences.getString(KEY_COMPANY_NAME, null);
    }

    public void editCompanyId(String id) {
        if (editor == null)
            editor = sharedPreferences.edit();
        editor.putString(KEY_COMPANY_ID, id).apply();
    }

    public String getCompanyId() {
        return sharedPreferences.getString(KEY_COMPANY_ID, null);
    }

    public int getPublicIntValue(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public String getPublicStringValue(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void editPublicStringValue(String key, String value) {
        if (editor == null)
            editor = sharedPreferences.edit();
        editor.putString(key, value).apply();
    }

    public void editPublicIntValue(String key, int value) {
        if (editor == null) {
            editor = sharedPreferences.edit();
            editor.putInt(key, value).apply();
        }
    }


    public static void saveUserId(Context  context, String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userId",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId",userId);
        editor.apply();
    }

    public static String getUserId(Context context){
        SharedPreferences sharedPreferences =context.getSharedPreferences("userId",0);
        return  sharedPreferences.getString("userId",null);

    }

}
