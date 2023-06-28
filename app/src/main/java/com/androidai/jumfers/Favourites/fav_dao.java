package com.androidai.jumfers.Favourites;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.androidai.jumfers.Exam.Model;

import java.util.List;

@Dao
public interface fav_dao {
    @Query("SELECT * FROM favourite_model")
    List<FavouriteModel> getAllQuestions();

    @Insert
    void insert(FavouriteModel fav_item);

    @Delete
    void delete(FavouriteModel fav_item);

    @Update
    void update(FavouriteModel fav_item);
}
