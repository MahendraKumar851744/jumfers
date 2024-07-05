package com.jumfers.mocktestseries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jumfers.mocktestseries.Adapters.Act_result;
import com.jumfers.mocktestseries.Adapters.Ad_Confirm_Answers;
import com.jumfers.mocktestseries.databases.Exam.Exam_database;
import com.jumfers.mocktestseries.databases.Exam.Model;
import com.jumfers.mocktestseries.databases.Exam.exam_dao;

import com.jumfers.mocktestseries.databases.questions_db.Question_database;
import com.jumfers.mocktestseries.databases.questions_db.Question_model;

import java.util.ArrayList;
import java.util.Locale;


public class Act_test extends AppCompatActivity implements View.OnClickListener {

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

    String duration="";
    LinearLayout progress_circular;

    TextView tv_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_test);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
        progress_circular = findViewById(R.id.progress_circular);

        tv_title = findViewById(R.id.tv_title);
        question_num = findViewById(R.id.question_num);
        tv_question = findViewById(R.id.tv_question);
        tv_timer = findViewById(R.id.tv_timer);
        opt1 = findViewById(R.id.option1);
        opt2 = findViewById(R.id.option2);
        opt3 = findViewById(R.id.option3);
        opt4 = findViewById(R.id.option4);
        skip = findViewById(R.id.skip);
        next = findViewById(R.id.next);
        ll_opt1 = findViewById(R.id.ll_opt1);
        ll_opt2 = findViewById(R.id.ll_opt2);
        ll_opt3 = findViewById(R.id.ll_opt3);
        ll_opt4 = findViewById(R.id.ll_opt4);
        img_opt1 = findViewById(R.id.img_opt1);
        img_opt2 = findViewById(R.id.img_opt2);
        img_opt3 = findViewById(R.id.img_opt3);
        img_opt4 = findViewById(R.id.img_opt4);
        iv_back = findViewById(R.id.iv_back);
        tv_next = findViewById(R.id.tv_next);
        dialog=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        SharedPreferences sp = getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
        String title = sp.getString("PAPER_NAME","");
        String paper_id = sp.getString("PAPER_ID","0");
        duration = sp.getString("PAPER_DURATION","0");
        Log.d("EWDWECEC",paper_id);
        tv_title.setText(title);


        afterFetch();
        showTCDialog();

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(view -> {
            onBackPressed();
        });


    }
    private void afterFetch(){
        Question_database qdb = Question_database.getDbInstance(Act_test.this);
        Exam_database ex_db = Exam_database.getDbInstance(Act_test.this);
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
                replaceFragment();

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
        Exam_database ex_db = Exam_database.getDbInstance(Act_test.this);
        Model model = ex_db.dao().getAllQuestions().get(id);
        return model;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void update_ui(){
        Model model = get_model(current_pos);

        if(current_pos==0){
            skip.setVisibility(View.GONE);
        }else {
            skip.setVisibility(View.VISIBLE);
        }

        if(current_pos == db_size-1){
            tv_next.setText("Submit");
        }else {
            tv_next.setText("Next");
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
                    ll_opt1.setBackground(Act_test.this.getResources().getDrawable(R.drawable.rounded_corner_primary));
                    img_opt1.setImageDrawable(Act_test.this.getResources().getDrawable(R.drawable.baseline_check_circle_24));
                break;
            case 2:
                defualt();
                    ll_opt2.setBackground(Act_test.this.getResources().getDrawable(R.drawable.rounded_corner_primary));
                    img_opt2.setImageDrawable(Act_test.this.getResources().getDrawable(R.drawable.baseline_check_circle_24));
//                }
                break;
            case 3:
                defualt();

                    ll_opt3.setBackground(Act_test.this.getResources().getDrawable(R.drawable.rounded_corner_primary));
                    img_opt3.setImageDrawable(Act_test.this.getResources().getDrawable(R.drawable.baseline_check_circle_24));
//                }
                break;
            case 4:
                defualt();

                    ll_opt4.setBackground(Act_test.this.getResources().getDrawable(R.drawable.rounded_corner_primary));
                    img_opt4.setImageDrawable(Act_test.this.getResources().getDrawable(R.drawable.baseline_check_circle_24));
//                }
                break;
            default:
                defualt();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void defualt(){
            ll_opt1.setBackground(getResources().getDrawable(R.drawable.rounded_corner));
            ll_opt2.setBackground(getResources().getDrawable(R.drawable.rounded_corner));
            ll_opt3.setBackground(getResources().getDrawable(R.drawable.rounded_corner));
            ll_opt4.setBackground(getResources().getDrawable(R.drawable.rounded_corner));
            img_opt1.setImageDrawable(getResources().getDrawable(R.drawable.tick_grey));
            img_opt2.setImageDrawable(getResources().getDrawable(R.drawable.tick_grey));
            img_opt3.setImageDrawable(getResources().getDrawable(R.drawable.tick_grey));
            img_opt4.setImageDrawable(getResources().getDrawable(R.drawable.tick_grey));

    }

    @Override
    public void onClick(View view) {
        Exam_database ex_db = Exam_database.getDbInstance(Act_test.this);
        exam_dao dao = ex_db.dao();
        Model model = dao.getAllQuestions().get(current_pos);
        switch (view.getId()){
            case R.id.next:
                if(current_pos<db_size-1) {
                    current_pos += 1;
                    update_ui();
                }else{
                    showDialog();
                }
                break;
            case R.id.skip:
                if(current_pos>=1) {
                    current_pos -= 1;
                    update_ui();
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

    public void replaceFragment(){
        long endTime = System.currentTimeMillis();
        totalTime = endTime - startTime;
        SharedPreferences sp = Act_test.this.getSharedPreferences("BASEAPP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("totaltime",String.valueOf(totalTime));
        editor.apply();
        Intent i = new Intent(Act_test.this, Act_result.class);
        startActivity(i);
        onBackPressed();
    }

    public long getTotalTime() {
        return totalTime;
    }
    private void showDialog() {
        Exam_database ex_db = Exam_database.getDbInstance(Act_test.this);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Act_test.this,LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);
        Ad_Confirm_Answers ad_confirm_answers = new Ad_Confirm_Answers(Act_test.this,arrayList);
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
            replaceFragment();
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
                startTimer(Integer.parseInt(duration));
                dialog.dismiss();
            });

            dialog.show();
        }, delayMillis);
    }

    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        finish();
    }
}