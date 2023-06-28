package com.androidai.jumfers.database.questions_db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface q_dao {
    @Query("SELECT * FROM questions")
    List<Question_model> getAllQuestions();

    @Insert
    void insert(Question_model news_item);

    @Delete
    void delete(Question_model news_item);

    @Update
    void update(Question_model news_item);
}
