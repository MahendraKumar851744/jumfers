package com.androidai.jumfers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class Act_login extends AppCompatActivity {

    CardView btn_login,google;
    TextView register;
    SharedPreferences mpref;
    SharedPreferences.Editor editor;
    ProgressBar progress_circular;

    private GoogleSignInClient client;

    EditText email,password;

    private FirebaseAuth mAuth;
    TextView tv_forgot_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_login);
        btn_login = findViewById(R.id.btn_login);
        register = findViewById(R.id.register);
        email = findViewById(R.id.email);
        google = findViewById(R.id.google);
        password = findViewById(R.id.password);
        progress_circular = findViewById(R.id.progress_circular);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        progress_circular.setVisibility(View.GONE);
        mpref = getSharedPreferences("BASE_APP", Context.MODE_PRIVATE);
        editor = mpref.edit();
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this,options);
        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Act_login.this,Act_register.class);
                startActivity(i);

            }
        });
        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Act_login.this,Act_ForgotPassword.class);
                startActivity(i);
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!new Validations().isValidEmail(email.getText().toString())){
                    Toast.makeText(Act_login.this, "Enter valid Email!", Toast.LENGTH_SHORT).show();
                }else{
                    if(password.getText().toString().contains(" ")){
                        Toast.makeText(Act_login.this, "Enter valid Password!", Toast.LENGTH_SHORT).show();
                    }else{
                        progress_circular.setVisibility(View.VISIBLE);
                        makeVolleyRequest(Act_login.this,email.getText().toString(),password.getText().toString());
                    }
                }
            }
        });


    }
    public void makeVolleyRequest(Context context,String email,String password){

        if(NetworkConnection.checkNetworkStatus(context)) {
            String url = "https://test.jenacademy.in/api/v1/login";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progress_circular.setVisibility(View.GONE);
                            try {
                                JSONObject mainobj = new JSONObject(response);
                                String status = mainobj.getString("status");
                                if(status.equals("error")){
                                    String message = mainobj.getString("message");
                                    Log.d("APPLICATION",message);
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                }else{
                                    JSONObject user = mainobj.getJSONObject("user");
                                    JSONObject authorisation = mainobj.getJSONObject("authorisation");
                                    editor.putString("username",user.getString("name"));
                                    editor.putString("email",email);
                                    editor.putString("password",password);
                                    editor.putString("token",authorisation.getString("token"));
                                    editor.apply();

                                    Intent i = new Intent(Act_login.this,MainActivity.class);
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
                            progress_circular.setVisibility(View.GONE);
                            try {
                                Log.d("APPLICATION", error.getMessage());
                            }catch (Exception e){
                                Log.d("APPLICATION", e.getMessage());
                            }
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };

            Volley.newRequestQueue(context).add(stringRequest);

        }else{
            Toast.makeText(context, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }
    private void signIn() {
        Intent i = client.getSignInIntent();
        startActivityForResult(i,1234);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1234){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String name = account.getDisplayName();
                String email = account.getEmail();
                Log.d("APPLICATION","NAME: "+name+" email: "+email);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    progress_circular.setVisibility(View.VISIBLE);
                                    makeVolleyRequest(Act_login.this,name,email,"1234567890","password");
                                }else {
                                    Toast.makeText(Act_login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            } catch (ApiException e) {
                e.printStackTrace();
            }

        }
    }

    public void makeVolleyRequest(Context context,String name,String email,String phone,String password){
        if(NetworkConnection.checkNetworkStatus(context)) {
            String url = "https://test.jenacademy.in/api/v1/user-register";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject mainobj = new JSONObject(response);
                                String status = mainobj.getString("status");
                                if(status.equals("error")){
                                    String message = mainobj.getString("message");
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                }else{
                                    makeVolleyRequest(Act_login.this,email,password);
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
                            Log.d("APPLICATION",error.getMessage());
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



}