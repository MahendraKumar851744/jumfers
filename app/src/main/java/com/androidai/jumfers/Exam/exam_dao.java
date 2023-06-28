package com.androidai.jumfers.Exam;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.androidai.jumfers.database.questions_db.Question_model;

import java.util.List;

@Dao
public interface exam_dao {
    @Query("SELECT * FROM exam_model")
    List<Model> getAllQuestions();

    @Insert
    void insert(Model news_item);

    @Delete
    void delete(Model news_item);

    @Update
    void update(Model news_item);
}
