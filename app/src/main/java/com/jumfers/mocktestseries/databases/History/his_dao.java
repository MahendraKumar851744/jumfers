package com.jumfers.mocktestseries.databases.History;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface his_dao {
    @Query("SELECT * FROM history_model")
    List<HistoryModel> getAllItems();

    @Insert
    void insert(HistoryModel his_item);

    @Delete
    void delete(HistoryModel his_item);

    @Update
    void update(HistoryModel his_item);
}
