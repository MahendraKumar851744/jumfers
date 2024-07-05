package com.jumfers.mocktestseries.databases.questions_db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Question_model.class}, version  = 4)
public abstract class Question_database extends RoomDatabase {

    public abstract q_dao dao();

    private static Question_database INSTANCE;

    public static Question_database getDbInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Question_database.class, "QUESTION_DATABASE")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}