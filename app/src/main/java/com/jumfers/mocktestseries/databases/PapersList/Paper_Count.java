package com.jumfers.mocktestseries.databases.PapersList;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "papersList")
public class Paper_Count {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int pid;


    public int getPid() {
        return pid;
    }

    public void setPid(int id) {
        this.pid = id;
    }

    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "title")
    private String title;


    @ColumnInfo(name = "papers_count")
    private String paper_count;


    @ColumnInfo(name = "cat_id")
    private int category;

    @ColumnInfo(name = "subcat_id")
    private int subcategory;

    @ColumnInfo(name = "type")
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int scategory) {
        this.type = scategory;
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



    public Paper_Count(String id,int category,int subcategory,String title, String paper_count,int type) {
        this.id = id;
        this.title = title;
        this.paper_count = paper_count;
        this.category = category;
        this.subcategory = subcategory;
        this.type = type;
    }

    public Paper_Count(){

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPaper_count() {
        return paper_count;
    }

    public void setPaper_count(String paper_count) {
        this.paper_count = paper_count;
    }
}
