package com.androidai.jumfers.Exam;

import android.view.Display;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.androidai.jumfers.database.questions_db.Options;

@Entity(tableName = "exam_model")
public class Model {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "question_id")
    private int question_id;

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    @ColumnInfo(name = "question")
    private String question;

    @Embedded(prefix = "options_")
    private Options options;

    @ColumnInfo(name = "isright")
    private String is_right;

    public String getIs_right() {
        return is_right;
    }

    public void setIs_right(String is_right) {
        this.is_right = is_right;
    }

    @ColumnInfo(name = "detail")
    private String detail;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @ColumnInfo(name = "selected")
    private String selected;

    public String getSelected_answer() {
        return selected_answer;
    }

    public void setSelected_answer(String selected_answer) {
        this.selected_answer = selected_answer;
    }

    @ColumnInfo(name = "selected_answer")
    private String selected_answer;

    @ColumnInfo(name = "time")
    private String time;

    public Model(){

    }

    public Model(int question_id, String question, Options options,String is_right,String detail,String selected,String selected_answer, String time) {
        this.question_id = question_id;
        this.question = question;
        this.options = options;
        this.selected = selected;
        this.selected_answer = selected_answer;
        this.detail = detail;
        this.is_right = is_right;
        this.time = time;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
