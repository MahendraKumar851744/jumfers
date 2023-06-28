package com.androidai.jumfers.Models;

public class SubCategoryItem {
    private int id;
    private String title;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    private int category;

    public SubCategoryItem() {
    }

    public SubCategoryItem(int id, String title,int category) {
        this.id = id;
        this.title = title;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
