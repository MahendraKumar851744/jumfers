package com.androidai.jumfers.Exam;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.androidai.jumfers.database.questions_db.Question_model;
import com.androidai.jumfers.database.questions_db.q_dao;


@Database(entities = {Model.class}, version  = 7)
public abstract class Exam_database extends RoomDatabase {

    public abstract exam_dao dao();

    private static Exam_database INSTANCE;

    public static Exam_database getDbInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Exam_database.class, "EXAM_DATABASE")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}