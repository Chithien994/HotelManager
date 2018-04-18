package com.tcn.handle;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.tcn.hotelmanager.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by MyPC on 27/12/2017.
 */

public class MyAction {
    //Activity
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static String TAG = "MyAction";


    public static void setAction(Context context, boolean action){
        preferences = context.getSharedPreferences(context.getString(R.string.saveInfo), context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putBoolean("action",action);
        editor.commit();
        editor.clear();
        Log.d(TAG, "Save and check action: "+preferences.getBoolean("action",false));
    }

    public static boolean getAction(Context context){
        preferences = context.getSharedPreferences(context.getString(R.string.saveInfo), context.MODE_PRIVATE);
        return preferences.getBoolean("device",false);
    }

    public static void setNameDevice(Context context, String device){
        preferences = context.getSharedPreferences(context.getString(R.string.saveInfo), context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("device",device);
        editor.commit();
        editor.clear();
        Log.d(TAG, "Save name device and check name device: "+preferences.getString("device","Save Err"));
    }

    public static String getNameDevice(Context context){
        preferences = context.getSharedPreferences(context.getString(R.string.saveInfo), context.MODE_PRIVATE);
        return preferences.getString("device","Save Err");
    }

    public static void setToken(Context context, String token){
        preferences = context.getSharedPreferences(context.getString(R.string.saveInfo), context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("token",token);
        editor.commit();
        editor.clear();
        Log.d(TAG, "Save token and check token: "+preferences.getString("token","Save Err"));
    }

    public static String getToken(Context context){
        preferences = context.getSharedPreferences(context.getString(R.string.saveInfo), context.MODE_PRIVATE);
        return preferences.getString("token","Save Err");
    }

}
