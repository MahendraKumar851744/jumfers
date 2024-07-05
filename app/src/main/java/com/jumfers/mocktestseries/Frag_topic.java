package com.jumfers.mocktestseries;

import static com.jumfers.mocktestseries.utils.Constants.SUB_PAPERS_LIST;
import static com.jumfers.mocktestseries.utils.Constants.TOPICS_PAPERS_LIST;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jumfers.mocktestseries.Adapters.Ad_Full_Paper;
import com.jumfers.mocktestseries.databases.PapersList.Paper_Count;

import com.jumfers.mocktestseries.utils.NetworkConnection;
import com.jumfers.mocktestseries.viewModels.PapersListViewModel;
import com.jumfers.mocktestseries.viewModels.PapersListViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Frag_topic extends Fragment {
    public Frag_topic() {

    }


    Ad_Full_Paper ad_full_paper;
    RecyclerView rv;

    Context context;

    ProgressBar progress_circular;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.lay_topic_wise, container, false);
        context = getContext();
        rv = v.findViewById(R.id.rv);;
        progress_circular = v.findViewById(R.id.progress_circular);
        SharedPreferences sp = getContext().getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
        int category_id = sp.getInt("CATEGORY_ID",1);
        int subcategory_id = sp.getInt("SUBCATEGORY_ID",1);
        String title = sp.getString("CATEGORY_NAME","");
        String subcategory_name = sp.getString("SUBCATEGORY_NAME","");
        TextView tv_title = v.findViewById(R.id.tv_title);
        tv_title.setText(title+" | "+subcategory_name);
        ImageView iv_back = v.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });


//        makeVolleyRequest(getContext(),category_id,subcategory_id);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.primary));
        PapersListViewModelFactory factory = new PapersListViewModelFactory(requireActivity().getApplication(), category_id,subcategory_id,TOPICS_PAPERS_LIST);
        PapersListViewModel viewModel = new ViewModelProvider(this, factory).get(PapersListViewModel.class);
        viewModel.getPapers().observe(getViewLifecycleOwner(), new Observer<List<Paper_Count>>() {
            @Override
            public void onChanged(List<Paper_Count> items) {
                progress_circular.setVisibility(View.GONE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                rv.setLayoutManager(linearLayoutManager);
                ad_full_paper = new Ad_Full_Paper(getContext(), (ArrayList<Paper_Count>) items,getFragmentManager(),1);
                rv.setAdapter(ad_full_paper);
            }
        });
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
                                    arrayList.add(new Paper_Count(model.getString("id"),category_id,subcategory_id,model.getString("title"),model.getString("paper_count"),3));
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