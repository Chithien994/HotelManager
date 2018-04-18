package com.tcn.handle;

import android.app.Activity;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by MyPC on 16/03/2018.
 */

public class Handle {
    public static String handleMoney(int giaSP) {
        String sTong = "";
        int tong=giaSP;
        int phanDu;

        if(tong<10000000&&tong>=1000000) {

            phanDu = tong % 1000000;
            sTong = String.valueOf(tong / 1000000) + ".";
            if(phanDu>0){
                sTong+=String.valueOf(phanDu/1000)+".000 đ";
            }else {
                sTong+="000.000 đ";
            }
        }else if (tong<1000000 && tong > 0){
            sTong=String.valueOf(tong/1000)+".000 đ";

        }else if (tong <= 0){
            sTong = "0 đ";
        }
        return sTong;
    }

    public static int convertToInt(String str){
        try {
            return Integer.parseInt(str);
        }catch (Exception e){
            return 0;
        }
    }

    public static long countTheDays(String sDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar today = Calendar.getInstance();
        long diff = -1;
        try {
            Date dateStart = simpleDateFormat.parse(sDate);
            Date dateEnd = simpleDateFormat.parse(simpleDateFormat.format(today.getTime()));

            //time is always 00:00:00 so rounding should help to ignore the missing hour when going from winter to summer time as well as the extra hour in the other direction
            diff = Math.round((dateEnd.getTime() - dateStart.getTime()) / (double) 86400000);
        } catch (Exception e) {
            //handle the exception according to your own situation
        }
        return diff;
    }

    //check keyboard hide or show
    //If it's showing then hide it
    public static void hideKeyboard(final Activity activity, final View contentView) {

        Rect r = new Rect();
        contentView.getWindowVisibleDisplayFrame(r);
        int screenHeight = contentView.getRootView().getHeight();

        // r.bottom is the position above soft keypad or device button.
        // if keypad is shown, the r.bottom is smaller than that before.
        int keypadHeight = screenHeight - r.bottom;

        Log.d("keypadHeight", keypadHeight +"");

        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
            // keyboard is opened
            if (contentView != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
        else {
            // keyboard is closed
        }
    }

}
