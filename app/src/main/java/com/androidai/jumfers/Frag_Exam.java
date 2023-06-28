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

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidai.utils.NetworkConnection;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Frag_Exam extends Fragment implements View.OnClickListener {


    String title;

    int category_id,subcategory_id;
    public Frag_Exam() {

    }
    CardView full,sub,topic;

    TextView tv_title,tv_topic,tv_full,tv_subject,total_papers;
    ProgressBar progress_circular;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.lay_exam, container, false);
        full = v.findViewById(R.id.full_paper);
        sub = v.findViewById(R.id.sub_paper);
        topic = v.findViewById(R.id.topic_paper);
        tv_title = v.findViewById(R.id.title);
        progress_circular = v.findViewById(R.id.progress_circular);
        tv_full = v.findViewById(R.id.tv_full);
        tv_topic = v.findViewById(R.id.tv_topic);
        tv_subject = v.findViewById(R.id.tv_subject);
        total_papers = v.findViewById(R.id.total_papers);
        SharedPreferences sp = getContext().getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
        title = sp.getString("CATEGORY_NAME","");
        category_id = sp.getInt("CATEGORY_ID",0);
        subcategory_id = sp.getInt("SUBCATEGORY_ID",0);

        tv_title.setText(title);

        makeVolleyRequest(getContext());
        full.setOnClickListener(this);
        sub.setOnClickListener(this);
        topic.setOnClickListener(this);

        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.primary));
        return v;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        SharedPreferences sp = getContext().getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        switch (id){
            case R.id.full_paper:
                editor.putString("EXAM_TYPE","1");
                editor.apply();
                replaceFragment(new Frag_full_paper());
                break;
            case R.id.sub_paper:
                editor.putString("EXAM_TYPE","2");
                editor.apply();
                replaceFragment(new Frag_sub_wise());
                break;
            case R.id.topic_paper:
                editor.putString("EXAM_TYPE","3");
                editor.apply();
                replaceFragment(new Frag_topic());
                break;
        }


    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void makeVolleyRequest(Context context){
        if(NetworkConnection.checkNetworkStatus(context)) {
            String url = "https://test.jenacademy.in/api/v1/get-paper-count";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progress_circular.setVisibility(View.GONE);
                            try {
                                JSONObject object = new JSONObject(response);
                                String full = object.getString("fullLengthPapers");
                                String sub = object.getString("subjectPapers");
                                String topic = object.getString("topicPapers");
                                tv_full.setText(full+"+ Papers");
                                tv_subject.setText(sub+"+ Subjects");
                                tv_topic.setText(topic+"+ Topics");
                                int total_papers_ = Integer.parseInt(full) + Integer.parseInt(sub) + Integer.parseInt(topic);
                                total_papers.setText(total_papers_+"+ Papers");
                            } catch (Exception e) {

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
                    jsonBody.put("category_id", String.valueOf(category_id));
                    jsonBody.put("subcategory_id", String.valueOf(subcategory_id));
                    return jsonBody;
                }
            };

            Volley.newRequestQueue(context).add(stringRequest);

        }else{
            Toast.makeText(context, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

}