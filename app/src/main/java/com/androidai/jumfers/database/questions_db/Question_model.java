package com.androidai.jumfers.database.questions_db;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "questions")
public class Question_model {

    @PrimaryKey(autoGenerate = true)
    private int id;


    @ColumnInfo(name = "paper__id")
    private String paper_id;

    public String getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(String paper_id) {
        this.paper_id = paper_id;
    }

    @ColumnInfo(name = "user_id")
    private String userId;

    @ColumnInfo(name = "ques")

    private String question;

    @ColumnInfo(name = "detail")

    private String detail;

    @ColumnInfo(name = "subject_id")
    private String subjectId;

    @ColumnInfo(name = "topic_id")
    private String topicId;

    private String label;

    private String remark;

    private String status;

    @ColumnInfo(name = "created_at")
    private String createdAt;

    @ColumnInfo(name = "updated_at")
    private String updatedAt;

    @Embedded(prefix = "options_")
    private Options options;

    // Constructor, getters and setters

    public Question_model(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public Question_model(String paper_id, String question, String detail, String subjectId, String topicId, Options options) {
        this.paper_id = paper_id;
        this.question = question;
        this.detail = detail;
        this.subjectId = subjectId;
        this.topicId = topicId;
        this.options = options;
    }
}
