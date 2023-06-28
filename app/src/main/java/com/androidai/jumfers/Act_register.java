package com.androidai.jumfers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidai.utils.NetworkConnection;
import com.androidai.utils.Validations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Act_register extends AppCompatActivity {

    CardView register;
    TextView signin;
    ProgressBar progress_circular;
    EditText name,email,phone,password,confirm_password;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_register);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        register = findViewById(R.id.register);
        signin = findViewById(R.id.signin);
        progress_circular = findViewById(R.id.progress_circular);
        progress_circular.setVisibility(View.GONE);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();

            }

        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Act_register.this,Act_login.class);
                startActivity(i);
                finish();
            }
        });

    }
    public void makeVolleyRequest(Context context,String name,String email,String phone,String password){
        if(NetworkConnection.checkNetworkStatus(context)) {
            String url = "https://test.jenacademy.in/api/v1/user-register";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dialog.dismiss();
                            try {
                                JSONObject mainobj = new JSONObject(response);
                                String status = mainobj.getString("status");
                                if(status.equals("error")){
                                    String message = mainobj.getString("message");
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                }else{
                                    SharedPreferences mpref = getSharedPreferences("BASE_APP", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = mpref.edit();
                                    editor.putString("password",password);
                                    editor.putString("username",name);
                                    editor.putString("email",email);
                                    editor.putString("mobile",phone);
                                    editor.apply();

                                    Intent i = new Intent(Act_register.this,Act_login.class);
                                    startActivity(i);
                                    finish();
                                }

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();
                            Toast.makeText(Act_register.this,"Email Already Exists",Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> jsonBody = new HashMap<String, String>();
                    jsonBody.put("name", name);
                    jsonBody.put("email", email);
                    jsonBody.put("phone", phone);
                    jsonBody.put("password", password);
                    return jsonBody;
                }

            };

            Volley.newRequestQueue(context).add(stringRequest);

        }else{
            Toast.makeText(context, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }


    private void createDialog() {
        dialog=new Dialog(this);
        dialog.setContentView(R.layout.register_success_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        makeVolleyRequest(this,name.getText().toString(),email.getText().toString(), phone.getText().toString(),password.getText().toString());
    }
    private void validation(){
        if(!name.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !phone.getText().toString().isEmpty() &&
                !password.getText().toString().isEmpty() && !confirm_password.getText().toString().isEmpty()
        ){
            if(!new Validations().isValidEmail(email.getText().toString()) ){
                Toast.makeText(this, "Please Enter Valid Email!", Toast.LENGTH_SHORT).show();
            }else{
                if(!new Validations().isValidMobileNumber(phone.getText().toString())){
                    Toast.makeText(this, "Please Enter Valid Mobile!", Toast.LENGTH_SHORT).show();
                }else{
                    if(password.getText().toString().contains(" ")){
                        Toast.makeText(this, "Please Enter Valid Password!", Toast.LENGTH_SHORT).show();
                    }else{
                        if(password.getText().toString().equals(confirm_password.getText().toString())){
                            createDialog();

                        }else{
                            Toast.makeText(this, "passwords are not matching!", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }

        }else{
            Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show();
        }
    }


}