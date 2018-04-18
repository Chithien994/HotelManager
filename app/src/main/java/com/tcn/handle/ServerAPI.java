package com.tcn.handle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tcn.hotelmanager.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MyPC on 17/03/2018.
 */

public class ServerAPI {

    public ServerAPI(){}

    public static void post(final Activity activity, JSONObject object){
        String url = "http://mybigger.ga/api/check/version/";
        final Cache cache = new DiskBasedCache(activity.getCacheDir(), 1024 * 1024);

        Network network = new BasicNetwork(new HurlStack());

        final RequestQueue requestQueue= new RequestQueue(cache, network);

        requestQueue.start();

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    try {
                        Log.d("Response",response.toString());
                        checkVersion(activity, response);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
        requestQueue.add(objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                27000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)));
    }

    private static void checkVersion(final Activity activity, JSONObject response) {
        String versionName = BuildConfig.VERSION_NAME;
        try {
            String newVersion = response.getString("version");
            final String urlApp = response.getString("url");
            String description = response.getString("description");
            String developers = response.getString("developers");
            if (!versionName.equalsIgnoreCase(newVersion)){
                new AlertDialog.Builder(activity)
                        .setTitle("Cập nhật")
                        .setMessage("Phiên bản hiện tại: " + versionName + "\n"
                                +"Phiên bản mới: " + newVersion +" \n"+
                        description+" "+developers)
                        .setNegativeButton("Cập nhật", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent op = new Intent(Intent.ACTION_VIEW);
                                op.setData(Uri.parse(urlApp));
                                activity.startActivity(op);

                            }
                        })
                        .setPositiveButton("Để sau", null).show();
            }else{
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
