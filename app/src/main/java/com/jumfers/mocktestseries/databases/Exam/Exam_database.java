package com.jumfers.mocktestseries.databases.Exam;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


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