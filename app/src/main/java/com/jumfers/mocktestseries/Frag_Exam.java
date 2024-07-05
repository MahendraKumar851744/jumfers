package com.jumfers.mocktestseries;

import static com.jumfers.mocktestseries.utils.Constants.FULL_PAPERS_LIST;
import static com.jumfers.mocktestseries.utils.Constants.GET_FULL_PAPER_LIST;
import static com.jumfers.mocktestseries.utils.Constants.GET_PAPERS_COUNT;
import static com.jumfers.mocktestseries.utils.Constants.GET_SUB_PAPERS_LIST;
import static com.jumfers.mocktestseries.utils.Constants.GET_TOPICS_PAPERS_LIST;
import static com.jumfers.mocktestseries.utils.Constants.PAPERS_COUNT;
import static com.jumfers.mocktestseries.utils.Constants.SUB_PAPERS_LIST;
import static com.jumfers.mocktestseries.utils.Constants.TOPICS_PAPERS_LIST;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.jumfers.mocktestseries.databases.PapersList.PapersList_dao;
import com.jumfers.mocktestseries.databases.PapersList.PapersList_database;
import com.jumfers.mocktestseries.databases.paperCountDb.PapersCount;
import com.jumfers.mocktestseries.databases.paperCountDb.Papers_dao;
import com.jumfers.mocktestseries.databases.paperCountDb.Papers_database;
import com.jumfers.mocktestseries.utils.FragmentUtil;
import com.jumfers.mocktestseries.utils.NetworkConnection;
import com.jumfers.mocktestseries.utils.RequestHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Frag_Exam extends Fragment implements View.OnClickListener {


    String title;

    int category_id,subcategory_id;
    public Frag_Exam() {

    }
    CardView full,sub,topic;

    TextView tv_title,tv_topic,tv_full,tv_subject,total_papers,tv_cat;
    ProgressBar progress_circular;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        tv_cat = v.findViewById(R.id.tv_cat);
        SharedPreferences sp = getContext().getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
        title = sp.getString("CATEGORY_NAME","");
        category_id = sp.getInt("CATEGORY_ID",0);
        subcategory_id = sp.getInt("SUBCATEGORY_ID",0);
        String subcategory_name = sp.getString("SUBCATEGORY_NAME","");

        tv_cat.setText(title);
        tv_title.setText(subcategory_name);

        full.setOnClickListener(this);
        sub.setOnClickListener(this);
        topic.setOnClickListener(this);

        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.primary));

        ImageView iv_back = v.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });


        Papers_database papersDatabase = Papers_database.getDbInstance(getContext());
        PapersCount papersCount = papersDatabase.dao().getAllItems(category_id,subcategory_id);
        tv_full.setText(papersCount.getFull()+"+ Papers");
        tv_subject.setText(papersCount.getSub()+"+ Subjects");
        tv_topic.setText(papersCount.getTopic()+"+ Topics");
        total_papers.setText(papersCount.getTotal()+"+ Papers");
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

//                Map<String, String> params = new HashMap<String, String>();
//                params.put("category_id", String.valueOf(category_id));
//                params.put("subcategory_id", String.valueOf(subcategory_id));
//                RequestHandler handler = new RequestHandler(getContext());
//                handler.makePostRequest(GET_FULL_PAPER_LIST, params, new RequestHandler.ResponseCallback() {
//                    @Override
//                    public void onComplete(boolean success) {
//                        if(success){
//                            progress_circular.setVisibility(View.GONE);
//                            new FragmentUtil(requireActivity().getSupportFragmentManager(),R.id.flFragment).addFragment(new Frag_Exam(),null);
//                        }else{
//                            Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
//                            progress_circular.setVisibility(View.GONE);
//                        }
//                    }
//                },FULL_PAPERS_LIST);

                replaceFragment(new Frag_full_paper());
                break;
            case R.id.sub_paper:
                editor.putString("EXAM_TYPE","2");
                editor.apply();
                getData(GET_SUB_PAPERS_LIST,SUB_PAPERS_LIST);
                break;
            case R.id.topic_paper:
                editor.putString("EXAM_TYPE","3");
                editor.apply();
                getData(GET_TOPICS_PAPERS_LIST,TOPICS_PAPERS_LIST);
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


    private void getData(String url,int type){
        Fragment fr;
        if(type==SUB_PAPERS_LIST){
            fr = new Frag_sub_wise();
        }else{
            fr = new Frag_topic();
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("category_id", String.valueOf(category_id));
        params.put("subcategory_id", String.valueOf(subcategory_id));
        RequestHandler handler = new RequestHandler(getContext());
        progress_circular.setVisibility(View.VISIBLE);


        PapersList_database pdb = PapersList_database.getDbInstance(getContext());
        PapersList_dao papersDao = pdb.dao();

        papersDao.doesItemExist(category_id,subcategory_id,type).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){
                    handler.makePostRequest(url, params, new RequestHandler.ResponseCallback() {
                        @Override
                        public void onComplete(boolean success) {
                            if(success){
                                progress_circular.setVisibility(View.GONE);

                                new FragmentUtil(requireActivity().getSupportFragmentManager(),R.id.flFragment).replaceFragment(fr,null);
                            }else{
                                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                progress_circular.setVisibility(View.GONE);
                            }
                        }
                    },type);
                }else{
                    progress_circular.setVisibility(View.GONE);
                    new FragmentUtil(requireActivity().getSupportFragmentManager(),R.id.flFragment).replaceFragment(fr,null);
                }
            }
        });



    }

}