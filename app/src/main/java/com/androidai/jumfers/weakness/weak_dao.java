package com.androidai.jumfers.weakness;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.androidai.jumfers.Favourites.FavouriteModel;

import java.util.List;

@Dao
public interface weak_dao {
    @Query("SELECT * FROM weakness_model")
    List<WeaknessModel> getAllQuestions();

    @Insert
    void insert(WeaknessModel weak_item);

    @Delete
    void delete(WeaknessModel weak_item);

    @Update
    void update(WeaknessModel weak_item);
}
