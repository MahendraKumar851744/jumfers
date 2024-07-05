package com.jumfers.mocktestseries.databases.papers;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "papers")
public class Paper {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ColumnInfo(name = "cat_id")
    private int category;

    @ColumnInfo(name = "subcat_id")
    private int subcategory;
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

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "title")
    private String title;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTotal_papers() {
        return total_papers;
    }

    public void setTotal_papers(String total_papers) {
        this.total_papers = total_papers;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @ColumnInfo(name = "desc")
    private String description;
    @ColumnInfo(name = "total_papers")
    private String total_papers;

    @ColumnInfo(name = "duration")
    private String duration;

    public Paper(String id,int category,int subcategory, String title,String description,String total_papers,String duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.total_papers = total_papers;
        this.duration = duration;
        this.category = category;
        this.subcategory = subcategory;
    }

    public Paper(){

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
