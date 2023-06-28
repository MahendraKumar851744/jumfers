package com.androidai.jumfers;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.androidai.jumfers.Exam.Exam_database;
import com.androidai.jumfers.Exam.Model;
import com.androidai.jumfers.Favourites.Fav_database;
import com.androidai.jumfers.Favourites.FavouriteModel;
import com.androidai.jumfers.Favourites.fav_dao;
import com.androidai.jumfers.History.His_database;
import com.androidai.jumfers.History.HistoryModel;
import com.androidai.jumfers.History.his_dao;
import com.androidai.jumfers.database.questions_db.Question_database;
import com.androidai.jumfers.database.questions_db.Question_model;
import com.androidai.jumfers.weakness.WeaknessModel;
import com.androidai.jumfers.weakness.weak_dao;
import com.androidai.jumfers.weakness.weak_database;
import com.androidai.utils.NetworkConnection;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class result_fragment extends Fragment {


    public result_fragment() {
    }

    public result_fragment(long x) {
    }

    public static result_fragment newInstance(String param1, String param2) {
        result_fragment fragment = new result_fragment();
        return fragment;
    }

    TextView tv_score,paper_name;
    CardView check_answers,next_paper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_result_fragment, container, false);

        TextView total_time = v.findViewById(R.id.total_time);
        check_answers = v.findViewById(R.id.check_answers);
        next_paper = v.findViewById(R.id.next_paper);
        paper_name = v.findViewById(R.id.paper_name);
        long milliseconds = Long.parseLong(getContext().getSharedPreferences("BASEAPP", Context.MODE_PRIVATE).getString("totaltime","0")); // Replace with your actual milliseconds value
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(minutes);

        total_time.setText(minutes+" minute "+seconds+" seconds");
        tv_score = v.findViewById(R.id.tv_score);

        SharedPreferences sp = getContext().getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
        String title = sp.getString("PAPER_NAME","");
        paper_name.setText(title);

        check_answers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.disallowAddToBackStack();
                fragmentTransaction.replace(R.id.flFragment, new Frag_review());
                fragmentTransaction.commit();
            }
        });

        next_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        Exam_database ex_db = Exam_database.getDbInstance(getContext());
        int db_size = ex_db.dao().getAllQuestions().size();
        int correct = 0;


        weak_database weakDatabase = weak_database.getDbInstance(getContext());
        weak_dao weakDao = weakDatabase.dao();

        for(int i=0;i<db_size;i++){
            if(ex_db.dao().getAllQuestions().get(i).getSelected().equals(ex_db.dao().getAllQuestions().get(i).getIs_right())){
                correct+=1;
            }
        }
        tv_score.setText(correct+"/"+db_size);
        WeaknessDB();
        HistoryDB();
        return v;
    }
    private void WeaknessDB(){
        Exam_database ex_db = Exam_database.getDbInstance(getContext());
        int db_size = ex_db.dao().getAllQuestions().size();
        weak_database weakDatabase = weak_database.getDbInstance(getContext());
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
                            weakDao.insert(new WeaknessModel("Railway, RRB NTPC","Paper 1",m.getQuestion(),"Skipped",m.getDetail()));
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
                                weakDao.insert(new WeaknessModel("Railway, RRB NTPC","Paper 1",m.getQuestion(),"Wrong",m.getDetail()));
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
                His_database his_database = His_database.getDbInstance(getContext());
                his_dao hisDao = his_database.dao();
                SharedPreferences sp = requireContext().getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
                String title = sp.getString("PAPER_NAME","");
                String cat_name = sp.getString("CATEGORY_NAME","");
                String paper_num = "Paper 1";
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



}