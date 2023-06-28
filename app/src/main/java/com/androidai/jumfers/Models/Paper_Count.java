package com.androidai.jumfers.Models;

public class Paper_Count {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String title;
    private String paper_count;

    public Paper_Count(String id,String title, String paper_count) {
        this.id = id;
        this.title = title;
        this.paper_count = paper_count;
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
