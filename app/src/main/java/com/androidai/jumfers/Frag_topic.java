package com.androidai.jumfers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidai.jumfers.Adapters.Ad_Full_Paper;
import com.androidai.jumfers.Models.Paper_Count;
import com.androidai.utils.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Frag_topic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag_topic extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Frag_topic() {

    }

    int id;

    public Frag_topic(int id) {
        this.id = id;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Frag_topic.
     */
    // TODO: Rename and change types and number of parameters
    public static Frag_topic newInstance(String param1, String param2) {
        Frag_topic fragment = new Frag_topic();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    Ad_Full_Paper ad_full_paper;
    RecyclerView rv;

    Context context;

    ProgressBar progress_circular;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v =  inflater.inflate(R.layout.lay_topic_wise, container, false);
        context = getContext();
        rv = v.findViewById(R.id.rv);;
        progress_circular = v.findViewById(R.id.progress_circular);
        SharedPreferences sp = getContext().getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
        int category_id = sp.getInt("CATEGORY_ID",1);
        int subcategory_id = sp.getInt("SUBCATEGORY_ID",1);
        makeVolleyRequest(getContext(),category_id,subcategory_id);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.primary));
        return v;
    }


    public void makeVolleyRequest(Context context,int category_id,int subcategory_id){
        if(NetworkConnection.checkNetworkStatus(context)) {
            String url = "https://test.jenacademy.in/api/v1/topic-wise-paper-count";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progress_circular.setVisibility(View.GONE);
                            ArrayList<Paper_Count> arrayList = new ArrayList<>();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray data = jsonObject.getJSONArray("data");
                                for(int i=0;i<data.length();i++){
                                    JSONObject model = data.getJSONObject(i);
                                    arrayList.add(new Paper_Count(model.getString("id"),model.getString("title"),model.getString("paper_count")));
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                            rv.setLayoutManager(linearLayoutManager);
                            ad_full_paper = new Ad_Full_Paper(getContext(),arrayList,getFragmentManager(),1);
                            rv.setAdapter(ad_full_paper);

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