package com.jumfers.mocktestseries;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jumfers.mocktestseries.databases.Exam.Exam_database;

import com.jumfers.mocktestseries.databases.questions_db.Question_database;
import com.jumfers.mocktestseries.utils.NetworkConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class frag_check_answers extends Fragment {
    public frag_check_answers() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

     TextView tv_question_number;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.lay_test_check, container, false);
        tv_question_number = v.findViewById(R.id.tv_question_number);



        return v;
    }

    public void get_result_paper(Context context){
        if(NetworkConnection.checkNetworkStatus(context)) {
            Question_database qdb = Question_database.getDbInstance(getContext());
            qdb.clearAllTables();
            String url = "https://test.jenacademy.in/api/v1/user-review-paper";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject data = jsonObject.getJSONObject("data");
                                Log.d("right_answer",data.getString("right_answer"));

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try{
                                Log.d("APPLICATION",error.getMessage());
                            }catch (Exception e){

                            }

                        }
                    }) {


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> jsonBody = new HashMap<String, String>();
                    jsonBody.put("paper_id", "1");
                    Exam_database ex_db = Exam_database.getDbInstance(getContext());
                    int size = ex_db.dao().getAllQuestions().size();
                    for(int i=0;i<size;i++){
                        String qn_id = "questions_ids["+i+"]";
                        jsonBody.put(qn_id,String.valueOf(ex_db.dao().getAllQuestions().get(i).getQuestion_id()));

                    }
                    for(int i=0;i<size;i++){
                        String qn_id = "answer_ids["+i+"]";
                        jsonBody.put(qn_id,ex_db.dao().getAllQuestions().get(i).getSelected());

                    }
                    return jsonBody;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    String token = context.getSharedPreferences("BASE_APP",Context.MODE_PRIVATE).getString("token","");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }

            };

            Volley.newRequestQueue(context).add(stringRequest);

        }else{
            Toast.makeText(context, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

}