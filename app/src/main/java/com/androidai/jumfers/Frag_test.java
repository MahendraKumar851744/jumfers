package com.androidai.jumfers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidai.jumfers.Adapters.Ad_Confirm_Answers;
import com.androidai.jumfers.Adapters.Ad_Full_Paper;
import com.androidai.jumfers.Exam.Exam_database;
import com.androidai.jumfers.Exam.Model;
import com.androidai.jumfers.Exam.exam_dao;
import com.androidai.jumfers.database.questions_db.Options;
import com.androidai.jumfers.database.questions_db.Question_database;
import com.androidai.jumfers.database.questions_db.Question_model;
import com.androidai.utils.NetworkConnection;
import com.google.android.recaptcha.Recaptcha;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Frag_test extends Fragment
implements View.OnClickListener{
    public Frag_test() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private TextView tv_title;
    private TextView question_num;
    private TextView tv_timer;
    private TextView tv_question;
    private TextView opt1,opt2,opt3,opt4;
    private LinearLayout ll_opt1,ll_opt2,ll_opt3,ll_opt4;
    private ImageView img_opt1,img_opt2,img_opt3,img_opt4;

    private CardView skip,next;

    private int db_size;
    public static  int current_pos = 0;
 
    private static CountDownTimer countDownTimer;

    private long startTime;
    private long totalTime;

    private static long timeLeftInMillis;

    ImageView iv_back;
    boolean firsttime = true;
    Dialog dialog;

    LinearLayout progress_circular;
    @SuppressLint("WrongThread")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.black));
        View v =  inflater.inflate(R.layout.lay_test, container, false);
        progress_circular = v.findViewById(R.id.progress_circular);

        tv_title = v.findViewById(R.id.tv_title);
        question_num = v.findViewById(R.id.question_num);
        tv_question = v.findViewById(R.id.tv_question);
        tv_timer = v.findViewById(R.id.tv_timer);
        opt1 = v.findViewById(R.id.option1);
        opt2 = v.findViewById(R.id.option2);
        opt3 = v.findViewById(R.id.option3);
        opt4 = v.findViewById(R.id.option4);
        skip = v.findViewById(R.id.skip);
        next = v.findViewById(R.id.next);
        ll_opt1 = v.findViewById(R.id.ll_opt1);
        ll_opt2 = v.findViewById(R.id.ll_opt2);
        ll_opt3 = v.findViewById(R.id.ll_opt3);
        ll_opt4 = v.findViewById(R.id.ll_opt4);
        img_opt1 = v.findViewById(R.id.img_opt1);
        img_opt2 = v.findViewById(R.id.img_opt2);
        img_opt3 = v.findViewById(R.id.img_opt3);
        img_opt4 = v.findViewById(R.id.img_opt4);
        iv_back = v.findViewById(R.id.iv_back);
        dialog=new Dialog(getContext(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        SharedPreferences sp = getContext().getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
        String title = sp.getString("PAPER_NAME","");
        String paper_id = sp.getString("PAPER_ID","0");
        Log.d("EWDWECEC",paper_id);
        tv_title.setText(title);
        afterFetch();
        showTCDialog();

        return v;
    }

    private void afterFetch(){
        Question_database qdb = Question_database.getDbInstance(getContext());
        Exam_database ex_db = Exam_database.getDbInstance(getContext());
        ex_db.clearAllTables();
        ArrayList<Question_model> arrayList = (ArrayList<Question_model>) qdb.dao().getAllQuestions();
        for(Question_model model:arrayList){

            Model modell = new Model(model.getId(),model.getQuestion(),model.getOptions(),model.getOptions().getIsRight(),model.getDetail(),"0","","2");
            ex_db.dao().insert(modell);
        }
        db_size = ex_db.dao().getAllQuestions().size();
        ll_opt1.setOnClickListener(this);
        ll_opt2.setOnClickListener(this);
        ll_opt3.setOnClickListener(this);
        ll_opt4.setOnClickListener(this);

        try{
            update_ui();
        }catch (Exception e){

        }

        next.setOnClickListener(this);
        skip.setOnClickListener(this);

        startTime = System.currentTimeMillis();
    }

    private void startTimer(int time) {

        timeLeftInMillis = time * 60 * 1000; // 2 minutes in milliseconds
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updateTimer();
                }

                @Override
                public void onFinish() {
                    if(current_pos<db_size-1) {
                        countDownTimer.cancel();
                        current_pos += 1;
                        update_ui();
                    }else{
                        current_pos=0;

                        replaceFragment(new result_fragment(getTotalTime()));
                    }
                }
            }.start();

    }


    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tv_timer.setText(timeLeftFormatted);
    }

    private Model get_model(int id){
        Exam_database ex_db = Exam_database.getDbInstance(getContext());
        Model model = ex_db.dao().getAllQuestions().get(id);
        return model;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void update_ui(){
        Model model = get_model(current_pos);
        if(firsttime) {
            startTimer(Integer.parseInt(model.getTime()));
            firsttime = false;
        }
        question_num.setText("Question "+String.valueOf(current_pos+1));
        tv_question.setText(model.getQuestion());
        opt1.setText(model.getOptions().getOption1());
        opt2.setText(model.getOptions().getOption2());
        opt3.setText(model.getOptions().getOption3());
        opt4.setText(model.getOptions().getOption4());
        String sel = model.getSelected();
        switch (Integer.parseInt(sel)){
            case 1:
                defualt();
                if(checkActivity(getActivity())){
                    ll_opt1.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corner_primary));
                    img_opt1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.baseline_check_circle_24));
                }
                break;
            case 2:
                defualt();
                if(checkActivity(getActivity())) {
                    ll_opt2.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corner_primary));
                    img_opt2.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.baseline_check_circle_24));
                }
                break;
            case 3:
                defualt();
                if(checkActivity(getActivity())) {
                    ll_opt3.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corner_primary));
                    img_opt3.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.baseline_check_circle_24));
                }
                break;
            case 4:
                defualt();
                if(checkActivity(getActivity())) {
                    ll_opt4.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corner_primary));
                    img_opt4.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.baseline_check_circle_24));
                }
                break;
            default:
                defualt();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void defualt(){
        if(checkActivity(getActivity())) {
            ll_opt1.setBackground(requireActivity().getResources().getDrawable(R.drawable.rounded_corner));
            ll_opt2.setBackground(requireActivity().getResources().getDrawable(R.drawable.rounded_corner));
            ll_opt3.setBackground(requireActivity().getResources().getDrawable(R.drawable.rounded_corner));
            ll_opt4.setBackground(requireActivity().getResources().getDrawable(R.drawable.rounded_corner));
            img_opt1.setImageDrawable(requireActivity().getResources().getDrawable(R.drawable.tick_grey));
            img_opt2.setImageDrawable(requireActivity().getResources().getDrawable(R.drawable.tick_grey));
            img_opt3.setImageDrawable(requireActivity().getResources().getDrawable(R.drawable.tick_grey));
            img_opt4.setImageDrawable(requireActivity().getResources().getDrawable(R.drawable.tick_grey));
        }

    }

    private boolean checkActivity(Activity activity){
        if(activity != null){
            return true;
        }else{
            return false;
        }
    }
    @Override
    public void onClick(View view) {
        Exam_database ex_db = Exam_database.getDbInstance(getContext());
        exam_dao dao = ex_db.dao();
        Model model = dao.getAllQuestions().get(current_pos);
        switch (view.getId()){
            case R.id.next:
            case R.id.skip:
                if(current_pos<db_size-1) {
                    countDownTimer.cancel();
                    current_pos += 1;
                    firsttime = true;
                    update_ui();
                }else{
                    showDialog();
                }
                break;
            case R.id.ll_opt1:
                model.setSelected("1");
                model.setSelected_answer("a "+opt1.getText().toString());
                dao.update(model);
                update_ui();
                break;
            case R.id.ll_opt2:
                model.setSelected("2");
                model.setSelected_answer("b "+opt2.getText().toString());
                dao.update(model);
                update_ui();
                break;
            case R.id.ll_opt3:
                model.setSelected("3");
                model.setSelected_answer("c "+opt3.getText().toString());
                dao.update(model);
                update_ui();
                break;
            case R.id.ll_opt4:
                model.setSelected("4");
                model.setSelected_answer("d "+opt4.getText().toString());
                dao.update(model);
                update_ui();
                break;
        }
    }

    public void replaceFragment(Fragment fragment){
        long endTime = System.currentTimeMillis();
        totalTime = endTime - startTime;
        SharedPreferences sp = getContext().getSharedPreferences("BASEAPP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("totaltime",String.valueOf(totalTime));
        editor.apply();
        Log.d("timmme",String.valueOf(totalTime));
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment);
        fragmentTransaction.commit();
    }

    public long getTotalTime() {
        return totalTime;
    }
    private void showDialog() {
        Exam_database ex_db = Exam_database.getDbInstance(getContext());
        int db_size = ex_db.dao().getAllQuestions().size();
        int answered = 0;
        ArrayList<Model> arrayList = new ArrayList<>();
        for(int i=0;i<db_size;i++){
            arrayList.add(ex_db.dao().getAllQuestions().get(i));
            if(!ex_db.dao().getAllQuestions().get(i).getSelected_answer().isEmpty()){
                answered+=1;
            }
        }
        dialog.setContentView(R.layout.activity_act_confirm_answers);
        dialog.setCancelable(true);
        TextView tv_score = dialog.findViewById(R.id.tv_score);
        tv_score.setText(answered+"/"+db_size);
        TextView total_answered = dialog.findViewById(R.id.total_answered);
        total_answered.setText("You answered "+answered+" out of "+db_size+" questions");
        RecyclerView rv = dialog.findViewById(R.id.rv_confirm_answers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);
        Ad_Confirm_Answers ad_confirm_answers = new Ad_Confirm_Answers(getContext(),arrayList);
        rv.setAdapter(ad_confirm_answers);

        TextView title = dialog.findViewById(R.id.paper_name);
        title.setText(tv_title.getText());

        LinearLayout edit_answer = dialog.findViewById(R.id.edit_answer);
        edit_answer.setOnClickListener(v->{
            current_pos=0;
            update_ui();
            dismissDialog(dialog);
        });

        LinearLayout submit = dialog.findViewById(R.id.submit);
        submit.setOnClickListener(v->{
            current_pos=0;
            replaceFragment(new result_fragment(getTotalTime()));
            dismissDialog(dialog);

        });
        dialog.show();
    }

    private void dismissDialog(Dialog dialog){
        Handler handler = new Handler();
        long delayMillis = 300;
        handler.postDelayed(dialog::dismiss, delayMillis);
    }
    private void showTCDialog() {
        Handler handler = new Handler();
        long delayMillis = 300;
        handler.postDelayed(() -> {
            dialog.setContentView(R.layout.frag_tandc);
            dialog.setCancelable(false);
            CardView btn_proceed = dialog.findViewById(R.id.btn_proceed);
            btn_proceed.setOnClickListener(v -> {
                dialog.dismiss();
            });

            dialog.show();
        }, delayMillis);
    }


}