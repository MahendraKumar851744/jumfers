package com.jumfers.mocktestseries.databases.paperCountDb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "papersCount")
public class PapersCount {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "cat_id")
    private int category;

    @ColumnInfo(name = "subcat_id")
    private int subcategory;
    @ColumnInfo(name = "full")
    private int full;
    @ColumnInfo(name = "sub")
    private int sub;
    @ColumnInfo(name = "topic")
    private int topic;
    @ColumnInfo(name = "total")
    private int total;

    public int getFull(){
        return  full;
    }
    public void setFull(int x){
        this.full = x;
    }


    public int getSub(){
        return  sub;
    }
    public void setSub(int x){
        this.sub = x;
    }

    public int getTopic(){
        return  topic;
    }
    public void setTopic(int x){
        this.topic = x;
    }

    public  int getTotal(){
        return total;
    }
    public void setTotal(int x){
        this.total = x;
    }



    public int getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(int scategory) {
        this.subcategory = scategory;
    }


    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }



    public PapersCount() {
    }

    public PapersCount(int category,int subcategory,int full,int sub,int topic,int total) {
        this.category = category;
        this.subcategory = subcategory;
        this.topic = topic;
        this.full = full;
        this.sub = sub;
        this.total = total;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
