package com.jumfers.mocktestseries.NetworkRequest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.jumfers.mocktestseries.utils.NetworkConnection;

import java.util.Map;

public class Network {
    private static final String TAG = "VolleyRequest";

    public interface VolleyCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    

    public static void makeGetRequest(Context context, String url, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response);
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                callback.onError(error.getMessage());
            }
        });

        queue.add(request);
    }

    public static void makePostRequest(Context context,String url,final VolleyCallback callback,Map<String,String> params){
        if(NetworkConnection.checkNetworkStatus(context)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            callback.onSuccess(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try{
                                callback.onError(error.getMessage());
                            }catch (Exception e){

                            }

                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };

            Volley.newRequestQueue(context).add(stringRequest);

        }else{
            Toast.makeText(context, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }
}
