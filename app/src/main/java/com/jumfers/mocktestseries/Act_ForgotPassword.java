package com.jumfers.mocktestseries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.jumfers.mocktestseries.utils.NetworkConnection;
import com.jumfers.mocktestseries.utils.Validations;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Act_ForgotPassword extends AppCompatActivity {

    EditText email;
    CardView cv_continue;
    LinearLayout ll_back;
    ProgressBar progressBar;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_forgot_password);
        email = findViewById(R.id.email);
        cv_continue = findViewById(R.id.cv_continue);
        ll_back = findViewById(R.id.ll_back);
        progressBar = findViewById(R.id.progress_circular);
        dialog=new Dialog(this);
        cv_continue.setOnClickListener(v->{
            if(new Validations().isValidEmail(email.getText().toString())){
//                Intent i = new Intent(this,Act_otp.class);
//                startActivity(i);
                progressBar.setVisibility(View.VISIBLE);

                makeVolleyRequest(this,email.getText().toString());
            }else{
                Toast.makeText(this, "Enter the valid email!", Toast.LENGTH_SHORT).show();
            }
        });
        ll_back.setOnClickListener(v->{
            onBackPressed();
        });
    }
    public void makeVolleyRequest(Context context, String email){
        if(NetworkConnection.checkNetworkStatus(context)) {
            String url = "https://test.jenacademy.in/api/v1/forgot-passsword";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);
                            try {
                                JSONObject mainobj = new JSONObject(response);
                                String status = mainobj.getString("status");
                                if(status.equals("error")){
                                    Toast.makeText(context, mainobj.getString("message"), Toast.LENGTH_SHORT).show();
                                }else{
                                    Intent i = new Intent(Act_ForgotPassword.this,Act_otp.class);
                                    SharedPreferences sp = getSharedPreferences("BASE_APP",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("FORGOT_EMAIL",email);
                                    editor.apply();
                                    startActivity(i);
                                    Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Act_ForgotPassword.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                        }

                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> jsonBody = new HashMap<String, String>();
                    jsonBody.put("email_id", email);
                    return jsonBody;
                }

            };

            Volley.newRequestQueue(context).add(stringRequest);

        }else{
            Toast.makeText(context, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }


}