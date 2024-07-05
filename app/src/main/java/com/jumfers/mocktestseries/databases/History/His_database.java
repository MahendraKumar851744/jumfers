package com.jumfers.mocktestseries.databases.History;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {HistoryModel.class}, version  = 1)
public abstract class His_database extends RoomDatabase {

    public abstract his_dao dao();
    private static His_database INSTANCE;

    public static His_database getDbInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), His_database.class, "HISTORY_DATABASE")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}