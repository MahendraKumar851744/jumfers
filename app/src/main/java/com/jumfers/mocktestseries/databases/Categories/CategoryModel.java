package com.jumfers.mocktestseries.databases.Categories;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_model")
public class CategoryModel {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "category_id")
    private String category_id;

    @ColumnInfo(name = "category_title")
    private String category_title;

    @ColumnInfo(name = "category_img")
    private String category_img;

    public CategoryModel(String category_id, String category_title, String category_img) {
        this.category_id = category_id;
        this.category_title = category_title;
        this.category_img = category_img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_title() {
        return category_title;
    }

    public void setCategory_title(String category_title) {
        this.category_title = category_title;
    }

    public String getCategory_img() {
        return category_img;
    }

    public void setCategory_img(String category_img) {
        this.category_img = category_img;
    }

    public CategoryModel() {
    }
}

