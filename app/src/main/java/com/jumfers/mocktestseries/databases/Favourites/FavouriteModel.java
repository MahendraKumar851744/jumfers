package com.jumfers.mocktestseries.databases.Favourites;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_model")
public class FavouriteModel {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "category_id")
    private String category_id;

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    @ColumnInfo(name = "category_name")
    private String category_name;

    public String getItem_position() {
        return item_position;
    }

    public void setItem_position(String item_position) {
        this.item_position = item_position;
    }

    @ColumnInfo(name = "item_position")
    private String item_position;
    @ColumnInfo(name = "title")
    private String title;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }



    public FavouriteModel(String title,String category_id,String category_name,  String item_position) {
        this.category_id = category_id;
        this.item_position = item_position;
        this.title = title;
        this.category_name = category_name;
    }

    public FavouriteModel(){

    }
}
