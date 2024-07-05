package com.jumfers.mocktestseries;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jumfers.mocktestseries.databases.Exam.Exam_database;
import com.jumfers.mocktestseries.databases.Exam.Model;

import java.util.Objects;

public class Frag_review extends Fragment implements View.OnClickListener {

    public Frag_review() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ImageView iv_back;
    TextView tv_question_number,tv_question;
    LinearLayout next,prev;
    private int db_size;

    TextView option,option_text,option_text_sel,option_sel,detail;
    public static  int current_pos = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.lay_test_check, container, false);
        iv_back = v.findViewById(R.id.iv_back);
        tv_question_number = v.findViewById(R.id.tv_question_number);
        tv_question = v.findViewById(R.id.tv_question);
        next = v.findViewById(R.id.next);
        prev = v.findViewById(R.id.prev);
        option = v.findViewById(R.id.option);
        option_text = v.findViewById(R.id.option_text);
        option_sel = v.findViewById(R.id.option_sel);
        option_text_sel = v.findViewById(R.id.option_text_sel);
        detail = v.findViewById(R.id.detail);

        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        iv_back.setOnClickListener(V->{
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        });
        update_ui();
        return v;
    }

    private Model get_model(int id){
        Exam_database ex_db = Exam_database.getDbInstance(getContext());
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



}