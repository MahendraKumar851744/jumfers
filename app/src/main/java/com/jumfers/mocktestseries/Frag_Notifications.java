package com.jumfers.mocktestseries;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jumfers.mocktestseries.Adapters.Ad_Notification;
import com.jumfers.mocktestseries.Models.NotificationModel;

import com.jumfers.mocktestseries.utils.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Frag_Notifications extends Fragment {
    Ad_Notification ad_notification;
    RecyclerView rv;
    ProgressBar progress_circular;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        assert container != null;
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.frag_favourites,container,false);
        TextView heading = v.findViewById(R.id.heading);
        ImageView iv_back = v.findViewById(R.id.iv_back);
        progress_circular = v.findViewById(R.id.progress_circular);
        iv_back.setVisibility(View.INVISIBLE);
        heading.setText("Notifications");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        rv = v.findViewById(R.id.rv_fav);
        progress_circular.setVisibility(View.VISIBLE);
        makeVolleyRequest();
        return v;
    }
    public void makeVolleyRequest(){
        if(NetworkConnection.checkNetworkStatus(getContext())) {
            String url = "https://test.jenacademy.in/api/v1/get-notification";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progress_circular.setVisibility(View.GONE);
                            Log.d("NOTIFICATIONS",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray data = jsonObject.getJSONArray("data");
                                ArrayList<NotificationModel> arrayList = new ArrayList<>();
                                for(int i=0;i<data.length();i++){
                                    JSONObject model = data.getJSONObject(i);
                                    arrayList.add(new NotificationModel(model.getString("title"),model.getString("description")));
                                }
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                                rv.setLayoutManager(linearLayoutManager);
                                ad_notification = new Ad_Notification(getContext(), arrayList);
                                rv.setAdapter(ad_notification);
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
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("APPLICATION", error.getMessage());
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
            };

            Volley.newRequestQueue(getContext()).add(stringRequest);

        }else{
            Toast.makeText(getContext(), "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }


}
