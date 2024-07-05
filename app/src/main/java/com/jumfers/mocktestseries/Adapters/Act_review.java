package com.jumfers.mocktestseries.Adapters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jumfers.mocktestseries.databases.Exam.Exam_database;
import com.jumfers.mocktestseries.databases.Exam.Model;
import com.jumfers.mocktestseries.R;

import java.util.Objects;

public class Act_review extends AppCompatActivity implements View.OnClickListener {
    ImageView iv_back;
    TextView tv_question_number,tv_question;
    LinearLayout next,prev;
    private int db_size;

    TextView option,option_text,option_text_sel,option_sel,detail;
    public static  int current_pos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_test_check);
        iv_back = findViewById(R.id.iv_back);
        tv_question_number = findViewById(R.id.tv_question_number);
        tv_question = findViewById(R.id.tv_question);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        option = findViewById(R.id.option);
        option_text = findViewById(R.id.option_text);
        option_sel = findViewById(R.id.option_sel);
        option_text_sel = findViewById(R.id.option_text_sel);
        detail = findViewById(R.id.detail);

        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        iv_back.setOnClickListener(V->{
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
        });
        update_ui();
    }
    private Model get_model(int id){
        Exam_database ex_db = Exam_database.getDbInstance(this);
        db_size = ex_db.dao().getAllQuestions().size();

        Model model = ex_db.dao().getAllQuestions().get(id);
        return model;
    }

    private void update_ui(){
        Model model = get_model(current_pos);
        tv_question_number.setText("Question "+String.valueOf(current_pos+1));
        tv_question.setText(model.getQuestion());
        String op = model.getIs_right();
        if(Objects.equals(op, "1")){
            option.setText("a");
            option_text.setText(model.getOptions().getOption1());
        } else if (Objects.equals(op, "2")) {
            option_text.setText(model.getOptions().getOption2());
            option.setText("b");
        } else if (Objects.equals(op, "3")) {
            option_text.setText(model.getOptions().getOption3());
            option.setText("c");
        }else{
            option_text.setText(model.getOptions().getOption4());
            option.setText("d");
        }
        String op1 = model.getSelected();
        if(Objects.equals(op1, "1")){
            option_sel.setText("a");
            option_text_sel.setText(model.getOptions().getOption1());
        } else if (Objects.equals(op1, "2")) {
            option_text_sel.setText(model.getOptions().getOption2());
            option_sel.setText("b");
        } else if (Objects.equals(op1, "3")) {
            option_text_sel.setText(model.getOptions().getOption3());
            option_sel.setText("c");
        }else if(Objects.equals(op1, "4")){
            option_text_sel.setText(model.getOptions().getOption4());
            option_sel.setText("d");
        }else{
            option_text_sel.setText("skipped");
            option_sel.setText("");
        }

        detail.setText(model.getDetail());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.next:
                if(current_pos<db_size-1) {
                    current_pos += 1;
                    update_ui();
                }
                break;
            case R.id.prev:
                if(current_pos>0) {
                    current_pos -= 1;
                    update_ui();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}