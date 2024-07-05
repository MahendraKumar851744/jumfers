package com.jumfers.mocktestseries.utils;

import static com.jumfers.mocktestseries.utils.Constants.CATEGORIES;
import static com.jumfers.mocktestseries.utils.Constants.PAPERS_COUNT;
import static com.jumfers.mocktestseries.utils.Constants.SUBCATEGORIES;
import static com.jumfers.mocktestseries.utils.Constants.SUB_PAPERS_LIST;
import static com.jumfers.mocktestseries.utils.Constants.TOPICS_PAPERS_LIST;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jumfers.mocktestseries.databases.Categories.CategoryModel;
import com.jumfers.mocktestseries.databases.Categories.cat_database;
import com.jumfers.mocktestseries.databases.PapersList.Paper_Count;
import com.jumfers.mocktestseries.databases.PapersList.PapersList_dao;
import com.jumfers.mocktestseries.databases.PapersList.PapersList_database;
import com.jumfers.mocktestseries.databases.SubCategories.SubCategoryItem;
import com.jumfers.mocktestseries.databases.SubCategories.Subcat_database;
import com.jumfers.mocktestseries.databases.paperCountDb.PapersCount;
import com.jumfers.mocktestseries.databases.paperCountDb.Papers_dao;
import com.jumfers.mocktestseries.databases.paperCountDb.Papers_database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestHandler {
    private static final String TAG = RequestHandler.class.getSimpleName();

    private Context context;
    private RequestQueue requestQueue;

    public RequestHandler(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public interface ResponseCallback {
        void onComplete(boolean success);
    }

    public void makeGetRequest(String url,final ResponseCallback callback,int type) {
        makeRequest(Request.Method.GET, url, null, callback,type);
    }

    public void makePostRequest(String url, final Map<String, String> params, final ResponseCallback callback,int type) {
        makeRequest(Request.Method.POST, url, params, callback,type);
    }

    private void makeRequest(int method, String url, final Map<String, String> params, final ResponseCallback callback,int type) {
        PapersList_database papersListDatabase = PapersList_database.getDbInstance(context);
        PapersList_dao papersListDao = papersListDatabase.dao();
        StringRequest stringRequest = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("NETWORK_REQUEST", response);
                        JSONObject jsonObject = null;
                        switch (type){
                            case CATEGORIES:

                                Log.d("Categories",response);

                                cat_database cat_db = cat_database.getDbInstance(context);
                                cat_db.clearAllTables();
                                try {
                                    jsonObject = new JSONObject(response);
                                    JSONArray data = jsonObject.getJSONArray("data");
                                    for(int i=0;i<data.length();i++){
                                        JSONObject model = data.getJSONObject(i);
                                        cat_db.dao().insert(new CategoryModel(model.getString("id"),model.getString("title"),model.getString("image")));
                                    }
                                    callback.onComplete(true);
                                } catch (JSONException e) {
                                    callback.onComplete(false);
                                }


                                break;

                            case SUBCATEGORIES:
                                Subcat_database db = Subcat_database.getDbInstance(context);
                                try {
                                    if(params.get("category_id") != null){
                                        int category_id = Integer.parseInt(Objects.requireNonNull(params.get("category_id")));
                                        jsonObject = new JSONObject(response);
                                        JSONArray data = jsonObject.getJSONArray("data");
                                        for(int i=0;i<data.length();i++){
                                            JSONObject jsonObject1 = data.getJSONObject(i);
                                            db.dao().insert(new SubCategoryItem(jsonObject1.getInt("id"),jsonObject1.getString("title"),category_id));
                                        }
                                        callback.onComplete(true);
                                    }else{
                                        callback.onComplete(false);
                                    }
                                } catch (JSONException e) {
                                    Log.d("NETWORK_REQUEST", Objects.requireNonNull(e.getMessage()));
                                    callback.onComplete(false);
                                }

                                break;

                            case PAPERS_COUNT:
                                Papers_database pdb = Papers_database.getDbInstance(context);
                                Papers_dao papersDao = pdb.dao();
                                try {
                                    int cat = Integer.parseInt(Objects.requireNonNull(params.get("category_id")));
                                    int subcat = Integer.parseInt(Objects.requireNonNull(params.get("subcategory_id")));
                                    JSONObject object = new JSONObject(response);
                                    String full = object.getString("fullLengthPapers");
                                    String sub = object.getString("subjectPapers");
                                    String topic = object.getString("topicPapers");
                                    int total_papers_ = Integer.parseInt(full) + Integer.parseInt(sub) + Integer.parseInt(topic);
                                    papersDao.insert(new PapersCount(cat,subcat,Integer.parseInt(full),Integer.parseInt(sub),Integer.parseInt(topic),total_papers_));
                                    callback.onComplete(true);

                                } catch (Exception e) {
                                    Log.d("NETWORK_REQUEST", Objects.requireNonNull(e.getMessage()));
                                    callback.onComplete(false);
                                }
                                break;
                            case SUB_PAPERS_LIST:
                            case TOPICS_PAPERS_LIST:
                                try {
                                    int cat = Integer.parseInt(Objects.requireNonNull(params.get("category_id")));
                                    int subcat = Integer.parseInt(Objects.requireNonNull(params.get("subcategory_id")));
                                    JSONObject object = new JSONObject(response);
                                    JSONArray data1 = object.getJSONArray("data");
                                    for(int i=0;i<data1.length();i++){
                                        JSONObject model = data1.getJSONObject(i);
                                        papersListDao.insert(new Paper_Count(model.getString("id"),cat,subcat,model.getString("title"),model.getString("paper_count"),type));
                                    }
                                    callback.onComplete(true);

                                } catch (Exception e) {
                                    Log.d("NETWORK_REQUEST", Objects.requireNonNull(e.getMessage()));
                                    callback.onComplete(false);
                                }
                                break;
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = error.getMessage();
                        Log.d("NETWORK_REQUEST", Objects.requireNonNull(errorMessage));
                        callback.onComplete(false);

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = context.getSharedPreferences("BASE_APP",Context.MODE_PRIVATE).getString("token","");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 3, 1.2f));

        Volley.newRequestQueue(context).add(stringRequest);
    }
}
