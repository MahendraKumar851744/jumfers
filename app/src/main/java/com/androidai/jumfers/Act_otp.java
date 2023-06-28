package com.androidai.jumfers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidai.utils.NetworkConnection;
import com.goodiebag.pinview.Pinview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Act_otp extends AppCompatActivity {

    Pinview pinview;
    String str_otp = "";
    ProgressBar progress_circular;
    CardView cv_continue;
    LinearLayout ll_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_otp);
        pinview = findViewById(R.id.pinview);
        progress_circular = findViewById(R.id.progress_circular);
        cv_continue = findViewById(R.id.cv_continue);
        ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(v->{
            onBackPressed();
        });
        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(@Nullable Pinview pinview, boolean fromUser) {
                if(fromUser && pinview != null){
                    str_otp = pinview.getValue();
                }
            }
        });
        cv_continue.setOnClickListener(v->{
            progress_circular.setVisibility(View.VISIBLE);
            makeVolleyRequest(this);
        });

    }
    public void makeVolleyRequest(Context context){
        if(NetworkConnection.checkNetworkStatus(context)) {
            String url = "https://test.jenacademy.in/api/v1/forgot-password-otp";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progress_circular.setVisibility(View.GONE);
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if(status.equalsIgnoreCase("success")){
                                    Intent i = new Intent(Act_otp.this, Act_NewPassword.class);
                                    startActivity(i);
                                    finish();

                                }else{
                                    Log.d("APPLICATION",jsonObject.getString("message"));
                                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progress_circular.setVisibility(View.GONE);
                            try {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("APPLICATION", error.getMessage());
                            }catch (Exception e){
                                Log.d("APPLICATION", e.getMessage());
                            }
                        }
                    }){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    String token = context.getSharedPreferences("BASE_APP",Context.MODE_PRIVATE).getString("token","");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> jsonBody = new HashMap<String, String>();
                    jsonBody.put("otp",str_otp);
                    return jsonBody;
                }
            };

            Volley.newRequestQueue(context).add(stringRequest);

        }else{
            Toast.makeText(context, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }
}