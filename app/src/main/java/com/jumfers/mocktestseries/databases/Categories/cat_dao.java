package com.jumfers.mocktestseries.databases.Categories;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface cat_dao {
    @Query("SELECT * FROM category_model")
    List<CategoryModel> getAllQuestions();

    @Insert
    void insert(CategoryModel cat_item);

    @Delete
    void delete(CategoryModel cat_item);

    @Update
    void update(CategoryModel cat_item);
}
