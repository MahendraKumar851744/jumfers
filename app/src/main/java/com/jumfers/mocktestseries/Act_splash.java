package com.jumfers.mocktestseries;

import static com.jumfers.mocktestseries.utils.Constants.CATEGORIES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.RoomDatabase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.jumfers.mocktestseries.databases.Categories.cat_database;
import com.jumfers.mocktestseries.databases.PapersList.PapersList_database;
import com.jumfers.mocktestseries.databases.SubCategories.Subcat_database;
import com.jumfers.mocktestseries.databases.paperCountDb.Papers_database;
import com.jumfers.mocktestseries.utils.NetworkConnection;
import com.jumfers.mocktestseries.utils.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Act_splash extends AppCompatActivity {
    private static final int SPLASH_SCREEN_TIME_OUT = 3500;
    SharedPreferences mpref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_act_splash);
        mpref = getSharedPreferences("BASE_APP", Context.MODE_PRIVATE);
        editor = mpref.edit();

//        Thread workerThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Papers_database pdb = Papers_database.getDbInstance(Act_splash.this);
//                pdb.clearAllTables();
//                cat_database cat_db = cat_database.getDbInstance(Act_splash.this);
//                cat_db.clearAllTables();
//                PapersList_database papersListDatabase = PapersList_database.getDbInstance(Act_splash.this);
//                papersListDatabase.clearAllTables();
//            }
//        });
//      workerThread.start();

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(new Task(Papers_database.getDbInstance(this)));
        executorService.submit(new Task(PapersList_database.getDbInstance(this)));
        executorService.submit(new Task(Subcat_database.getDbInstance(this)));
        executorService.shutdown();

        if(mpref.getString("username","").isEmpty()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent i = new Intent(Act_splash.this, Act_Walkthrough.class);
                    startActivity(i);
                    finish();

                }
            }, SPLASH_SCREEN_TIME_OUT);

        }else{
            String email = mpref.getString("email","");
            String password = mpref.getString("password","");
            makeVolleyRequest(this,email,password);

        }
    }

    public void makeVolleyRequest(Context context,String email,String password){
        if(NetworkConnection.checkNetworkStatus(context)) {
            String url = "https://test.jenacademy.in/api/v1/login";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject mainobj = new JSONObject(response);
                                String status = mainobj.getString("status");
                                if(status.equals("error")){
                                    String message = mainobj.getString("message");
                                    Log.d("APPLICATION",message);
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                }else{
                                    JSONObject authorisation = mainobj.getJSONObject("authorisation");;
                                    editor.putString("token",authorisation.getString("token"));
                                    Log.d("APPLICATION","TOKEN: "+authorisation.getString("token"));
                                    editor.apply();

                                    RequestHandler handler = new RequestHandler(Act_splash.this);
                                    handler.makeGetRequest("https://test.jenacademy.in/api/v1/category-listing", new RequestHandler.ResponseCallback() {
                                        @Override
                                        public void onComplete(boolean success) {
                                            if(success){
                                                Intent i = new Intent(Act_splash.this,MainActivity.class);
                                                startActivity(i);
                                                finish();
                                            }else{
                                                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    },CATEGORIES);


                                }

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            progress_circular.setVisibility(View.GONE);
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

}
class Task implements Runnable {
    private RoomDatabase db;

    public Task(RoomDatabase db) {
        this.db = db;
    }

    @Override
    public void run() {
        db.clearAllTables();
    }
}