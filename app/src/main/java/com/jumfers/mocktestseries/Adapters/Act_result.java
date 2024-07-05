package com.jumfers.mocktestseries.Adapters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.jumfers.mocktestseries.databases.Exam.Exam_database;
import com.jumfers.mocktestseries.databases.Exam.Model;
import com.jumfers.mocktestseries.databases.History.His_database;
import com.jumfers.mocktestseries.databases.History.HistoryModel;
import com.jumfers.mocktestseries.databases.History.his_dao;
import com.jumfers.mocktestseries.R;

import com.jumfers.mocktestseries.databases.questions_db.Question_database;
import com.jumfers.mocktestseries.databases.weakness.WeaknessModel;
import com.jumfers.mocktestseries.databases.weakness.weak_dao;
import com.jumfers.mocktestseries.databases.weakness.weak_database;
import com.jumfers.mocktestseries.utils.NetworkConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Act_result extends AppCompatActivity {

    TextView tv_score,paper_name;
    CardView check_answers,next_paper;
    String paper_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_result_fragment);

        TextView total_time = findViewById(R.id.total_time);
        check_answers = findViewById(R.id.check_answers);
        next_paper = findViewById(R.id.next_paper);
        paper_name = findViewById(R.id.paper_name);
        long milliseconds = Long.parseLong(Act_result.this.getSharedPreferences("BASEAPP", Context.MODE_PRIVATE).getString("totaltime","0")); // Replace with your actual milliseconds value
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(minutes);

        total_time.setText(minutes+" minute "+seconds+" seconds");
        tv_score = findViewById(R.id.tv_score);

        SharedPreferences sp = Act_result.this.getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
        String title = sp.getString("PAPER_NAME","");
        paper_id = sp.getString("PAPER_ID","-1");

        paper_name.setText(title);


        check_answers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Act_result.this,Act_review.class);
                startActivity(i);
                onBackPressed();
            }
        });

        next_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Exam_database ex_db = Exam_database.getDbInstance(Act_result.this);
        int db_size = ex_db.dao().getAllQuestions().size();
        int correct = 0;


        weak_database weakDatabase = weak_database.getDbInstance(Act_result.this);
        weak_dao weakDao = weakDatabase.dao();

        for(int i=0;i<db_size;i++){
            if(ex_db.dao().getAllQuestions().get(i).getSelected().equals(ex_db.dao().getAllQuestions().get(i).getIs_right())){
                correct+=1;
            }
        }

        tv_score.setText(correct+"/"+db_size);
        SharedPreferences sp1 = Act_result.this.getSharedPreferences("BASE_APP",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp1.edit();
        editor.putString("RECENT_PAPER_SCORE",tv_score.getText().toString());
        editor.putString("RECENT_PAPER_NAME",title);
        editor.apply();

        WeaknessDB();
        HistoryDB();

        submit_data(this,paper_id);
    }
    private void WeaknessDB(){
        SharedPreferences sp = getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
        String title = sp.getString("CATEGORY_NAME","");

        Exam_database ex_db = Exam_database.getDbInstance(Act_result.this);
        int db_size = ex_db.dao().getAllQuestions().size();
        weak_database weakDatabase = weak_database.getDbInstance(Act_result.this);
        weak_dao weakDao = weakDatabase.dao();
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<db_size;i++){
                    Log.d("DEBUG_APP",ex_db.dao().getAllQuestions().get(i).getSelected());
                    if(ex_db.dao().getAllQuestions().get(i).getSelected().equalsIgnoreCase("0")){
                        int flag = 0;
                        for(WeaknessModel model:weakDao.getAllQuestions()){
                            if(model.getQuestion().equalsIgnoreCase(ex_db.dao().getAllQuestions().get(i).getQuestion())){
                                flag = 1;
                                break;
                            }
                        }
                        if(flag==0){
                            Model m = ex_db.dao().getAllQuestions().get(i);
                            weakDao.insert(new WeaknessModel(title,paper_name.getText().toString(),m.getQuestion(),"Skipped",m.getDetail()));
                            Log.d("DEBUG_APP",weakDao.getAllQuestions().size()+"");
                        }


                    }else{
                        if (!ex_db.dao().getAllQuestions().get(i).getSelected().equals(ex_db.dao().getAllQuestions().get(i).getIs_right())) {
                            int flag = 0;
                            for(WeaknessModel model:weakDao.getAllQuestions()){
                                if(model.getQuestion().equalsIgnoreCase(ex_db.dao().getAllQuestions().get(i).getQuestion())){
                                    flag = 1;
                                    break;
                                }
                            }
                            if(flag==0){
                                Model m = ex_db.dao().getAllQuestions().get(i);
                                weakDao.insert(new WeaknessModel(title,paper_name.getText().toString(),m.getQuestion(),"Wrong",m.getDetail()));
                            }
                        }
                    }
                }
            }
        });
        executorService.shutdown();
    }
    private void HistoryDB(){
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                His_database his_database = His_database.getDbInstance(Act_result.this);
                his_dao hisDao = his_database.dao();
                SharedPreferences sp = getSharedPreferences("BASEAPP", Context.MODE_PRIVATE);
                String title = sp.getString("SUBCATEGORY_NAME","");
                String cat_name = sp.getString("CATEGORY_NAME","");
                String paper_num = sp.getString("PAPER_NAME","");
                String formattedTime = "";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalTime currentTime = LocalTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
                    formattedTime = currentTime.format(formatter);
                }else{
                    Calendar calendar = Calendar.getInstance();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("h:mm a");
                    formattedTime = format.format(calendar.getTime());
                }
                HistoryModel model = new HistoryModel(title,paper_num,cat_name,formattedTime);
                hisDao.insert(model);
            }
        });
        executorService.shutdown();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void submit_data(Context context,String paper_id){
        Question_database q_db = Question_database.getDbInstance(context);
        q_db.clearAllTables();
        if(NetworkConnection.checkNetworkStatus(context)) {
            String url = "https://test.jenacademy.in/api/v1/user-paper";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject data = jsonObject.getJSONObject("data");
                                String right_answer = data.getString("right_answer");
                                Log.d("right_answer",right_answer);

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("Questions", error.getMessage());
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
                    Exam_database ex_db = Exam_database.getDbInstance(Act_result.this);
                    int db_size = ex_db.dao().getAllQuestions().size();

                    Log.d("RESSULT",paper_id);
                    jsonBody.put("paper_id", paper_id);
                    for(int i=0;i<db_size;i++){
                        jsonBody.put("questions_ids["+i+"]", String.valueOf(i+1));
                        Log.d("RESSULT","questions_ids["+i+"]"+" "+String.valueOf(i+1));

                    }
                    for(int i=0;i<db_size;i++){
                        jsonBody.put("answer_ids["+i+"]", ex_db.dao().getAllQuestions().get(i).getSelected());
                        Log.d("RESSULT","answer_ids["+i+"]"+" "+ex_db.dao().getAllQuestions().get(i).getSelected());

                    }


                    return jsonBody;
                }
            };

            Volley.newRequestQueue(context).add(stringRequest);

        }else{
            Toast.makeText(context, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

}