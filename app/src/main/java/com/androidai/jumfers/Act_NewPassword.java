package com.androidai.jumfers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
import com.androidai.utils.NetworkConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Act_NewPassword extends AppCompatActivity {
    EditText password,confirm_password;
    CardView cv_continue;
    ProgressBar progress_circular;
    LinearLayout ll_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_confirm_password);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        cv_continue = findViewById(R.id.cv_continue);
        progress_circular = findViewById(R.id.progress_circular);
        ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(v->{
            onBackPressed();
        });
        cv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().contains(" ")){
                    Toast.makeText(Act_NewPassword.this, "Please Enter Valid Password!", Toast.LENGTH_SHORT).show();
                }else{
                    if(password.getText().toString().equals(confirm_password.getText().toString())){
                        progress_circular.setVisibility(View.VISIBLE);
                        makeVolleyRequest(Act_NewPassword.this,password.getText().toString(),confirm_password.getText().toString());

                    }else{
                        Toast.makeText(Act_NewPassword.this, "passwords are not matching!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    public void makeVolleyRequest(Context context,String pass,String conf_pass){
        if(NetworkConnection.checkNetworkStatus(context)) {
            String url = "https://test.jenacademy.in/api/v1/update-password";
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
                                    createDialog();
                                }else{
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
                    jsonBody.put("password",pass);
                    jsonBody.put("confirmpassword",conf_pass);
                    return jsonBody;
                }
            };

            Volley.newRequestQueue(context).add(stringRequest);

        }else{
            Toast.makeText(context, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }
    private void createDialog() {
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.new_pass_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CardView cv_continue = dialog.findViewById(R.id.cv_continue);
        cv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Act_NewPassword.this,Act_login.class);
                startActivity(i);
                finishAffinity();
            }
        });
        dialog.show();
    }
}