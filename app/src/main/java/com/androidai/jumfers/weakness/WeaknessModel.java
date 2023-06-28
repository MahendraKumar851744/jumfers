package com.androidai.jumfers.weakness;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weakness_model")
public class WeaknessModel {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "qp_name")
    private String qp_name;
    @ColumnInfo(name = "paper_name")
    private String paper_name;
    @ColumnInfo(name = "question")
    private String question;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "answer")
    private String answer;

    public WeaknessModel(String qp_name, String paper_name, String question, String status, String answer) {
        this.qp_name = qp_name;
        this.paper_name = paper_name;
        this.question = question;
        this.status = status;
        this.answer = answer;
    }

    public String getQp_name() {
        return qp_name;
    }

    public void setQp_name(String qp_name) {
        this.qp_name = qp_name;
    }

    public String getPaper_name() {
        return paper_name;
    }

    public void setPaper_name(String paper_name) {
        this.paper_name = paper_name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public WeaknessModel(){

    }
}
