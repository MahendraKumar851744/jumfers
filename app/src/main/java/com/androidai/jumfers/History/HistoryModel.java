package com.androidai.jumfers.History;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history_model")
public class HistoryModel {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "paper_num")
    private String paper_num;
    @ColumnInfo(name = "category_name")
    private String category_name;
    @ColumnInfo(name = "time")
    private String time;

    public HistoryModel(String title, String paper_num, String category_name, String time) {
        this.title = title;
        this.paper_num = paper_num;
        this.category_name = category_name;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPaper_num() {
        return paper_num;
    }

    public void setPaper_num(String paper_num) {
        this.paper_num = paper_num;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public HistoryModel(){

    }
}
