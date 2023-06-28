package com.androidai.jumfers;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
import com.androidai.jumfers.Adapters.Ad_Sub_Category;
import com.androidai.jumfers.Models.SubCategoryItem;
import com.androidai.jumfers.database.questions_db.Options;
import com.androidai.jumfers.database.questions_db.Question_model;
import com.androidai.utils.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Frag_Sub_Category extends Fragment {

    String title;
    TextView tv_title;
    Ad_Sub_Category ad_sub_category;
    RecyclerView rv;
    int category_id;
    ProgressBar progress_circular;
    ItemClickListener itemClickListener;
    TextView total_papers;

    public Frag_Sub_Category() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        assert container != null;
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.lay_sub_category,container,false);
        tv_title = v.findViewById(R.id.title);
        rv = v.findViewById(R.id.rv_sub_category_items);
        total_papers = v.findViewById(R.id.total_papers);
        progress_circular = v.findViewById(R.id.progress_circular);

        SharedPreferences sp = getContext().getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        title = sp.getString("CATEGORY_NAME","");
        category_id = sp.getInt("CATEGORY_ID",1);

        tv_title.setText(title);
        requireActivity().getWindow().setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.primary));
        itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(int position,int subcategory_id) {
                editor.putInt("SUBCATEGORY_ID",subcategory_id);
                editor.apply();
                replaceFragment(new Frag_Exam());
            }
        };
        getData();
        return v;
    }

    private void getData(){
        if(NetworkConnection.checkNetworkStatus(getContext())) {
            String url = "https://test.jenacademy.in/api/v1/sub-category-listing";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progress_circular.setVisibility(View.GONE);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray data = jsonObject.getJSONArray("data");
                                ArrayList<SubCategoryItem> arrayList = new ArrayList<>();
                                for(int i=0;i<data.length();i++){
                                    JSONObject jsonObject1 = data.getJSONObject(i);
                                    arrayList.add(new SubCategoryItem(jsonObject1.getInt("id"),jsonObject1.getString("title"),category_id));
                                }
                                total_papers.setText(arrayList.size()+" Papers");
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                                rv.setLayoutManager(linearLayoutManager);
                                ad_sub_category = new Ad_Sub_Category(getContext(),arrayList,itemClickListener);
                                rv.setAdapter(ad_sub_category);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            try {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("Questions", error.getMessage());
                            }catch (Exception e){
                                Log.d("APPLICATION", e.getMessage());
                            }
                        }
                    }){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    String token = getContext().getSharedPreferences("BASE_APP",Context.MODE_PRIVATE).getString("token","");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> jsonBody = new HashMap<String, String>();
                    jsonBody.put("category_id", String.valueOf(category_id));
                    return jsonBody;
                }
            };

            Volley.newRequestQueue(getContext()).add(stringRequest);

        }else{
            Toast.makeText(getContext(), "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
