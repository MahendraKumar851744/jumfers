package com.androidai.jumfers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidai.jumfers.Adapters.Ad_Full_Paper;
import com.androidai.jumfers.Adapters.Ad_Paper;
import com.androidai.jumfers.Models.Paper;
import com.androidai.jumfers.Models.Paper_Count;
import com.androidai.jumfers.database.questions_db.Options;
import com.androidai.jumfers.database.questions_db.Question_database;
import com.androidai.jumfers.database.questions_db.Question_model;
import com.androidai.utils.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Frag_paper extends Fragment {

    Ad_Paper ad_paper;
    RecyclerView rv;

    Context context;

    ProgressBar progress_circular;
    TextView title;

    ItemClickListener itemClickListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.lay_sub_wise, container, false);
        context = getContext();
        rv = v.findViewById(R.id.rv);;
        progress_circular = v.findViewById(R.id.progress_circular);
        title = v.findViewById(R.id.title);
        SharedPreferences sp = getContext().getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
        int subject_id = sp.getInt("SUBJECT_ID",1);
        String sub_title = sp.getString("SUBJECT_TITLE","");
        String exam_type = sp.getString("EXAM_TYPE","");
        title.setText(sub_title);
        itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(int position, int id) {
                progress_circular.setVisibility(View.VISIBLE);
                get_paper(getContext(),String.valueOf(id));
            }
        };
        if(exam_type.equalsIgnoreCase("2")){
            getSubjectWiseListing(getContext(),subject_id);
        }else{
            getTopicWiseListing(getContext(),subject_id);
        }


        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.primary));
        return v;
    }

    public void getSubjectWiseListing(Context context,int subject_id){
        if(NetworkConnection.checkNetworkStatus(context)) {
            String url = "https://test.jenacademy.in/api/v1/subject-wise-listing";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progress_circular.setVisibility(View.GONE);
                            ArrayList<Paper> arrayList = new ArrayList<>();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray data = jsonObject.getJSONArray("data");
                                for(int i=0;i<data.length();i++){
                                    JSONObject model = data.getJSONObject(i);
                                    arrayList.add(new Paper(model.getString("id"),model.getString("title")));
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                            rv.setLayoutManager(linearLayoutManager);
                            ad_paper = new Ad_Paper(getContext(),arrayList,itemClickListener);
                            rv.setAdapter(ad_paper);

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
                    jsonBody.put("subject_id", String.valueOf(subject_id));
                    return jsonBody;
                }
            };

            Volley.newRequestQueue(context).add(stringRequest);

        }else{
            Toast.makeText(context, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getTopicWiseListing(Context context,int topic_id){
        if(NetworkConnection.checkNetworkStatus(context)) {
            String url = "https://test.jenacademy.in/api/v1/topic-wise-listing";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progress_circular.setVisibility(View.GONE);
                            ArrayList<Paper> arrayList = new ArrayList<>();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray data = jsonObject.getJSONArray("data");
                                for(int i=0;i<data.length();i++){
                                    JSONObject model = data.getJSONObject(i);
                                    arrayList.add(new Paper(model.getString("id"),model.getString("title")));
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                            rv.setLayoutManager(linearLayoutManager);
                            ad_paper = new Ad_Paper(getContext(),arrayList,itemClickListener);
                            rv.setAdapter(ad_paper);

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
                    jsonBody.put("topic_id", String.valueOf(topic_id));
                    return jsonBody;
                }
            };

            Volley.newRequestQueue(context).add(stringRequest);

        }else{
            Toast.makeText(context, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }


    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    public void get_paper(Context context,String paper_id){
        Question_database q_db = Question_database.getDbInstance(context);
        q_db.clearAllTables();
        if(NetworkConnection.checkNetworkStatus(context)) {
            String url = "https://test.jenacademy.in/api/v1/get-paper";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject data = jsonObject.getJSONObject("data");
                                JSONArray questions = data.getJSONArray("questions");
                                for(int i=0;i<questions.length();i++){
                                    JSONObject obj = questions.getJSONObject(i);
                                    JSONObject ooption = obj.getJSONObject("options");
                                    Options options = new Options(ooption.getString("option1"),
                                            ooption.getString("option2"),
                                            ooption.getString("option3"),
                                            ooption.getString("option4"),
                                            ooption.getString("is_right")
                                    );
                                    Question_model model = new Question_model(
                                            Integer.toString(data.getInt("id")),
                                            obj.getString("question"),
                                            obj.getString("detail"),
                                            obj.getString("subject_id"),
                                            obj.getString("topic_id"),
                                            options
                                    );
                                    q_db.dao().insert(model);
                                }
                                Log.d("sdvsfdsdvc", q_db.dao().getAllQuestions().size()+"");

                                if(q_db.dao().getAllQuestions().size()>0){
                                    progress_circular.setVisibility(View.GONE);
                                    replaceFragment(new Frag_test());
                                }else{
                                    Toast.makeText(context, "No Questions Available", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
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
                    jsonBody.put("paper_id", paper_id);
                    return jsonBody;
                }
            };

            Volley.newRequestQueue(context).add(stringRequest);

        }else{
            Toast.makeText(context, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }
}