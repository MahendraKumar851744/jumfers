package com.jumfers.mocktestseries.databases.papers;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jumfers.mocktestseries.databases.paperCountDb.PapersCount;
import com.jumfers.mocktestseries.databases.paperCountDb.Papers_dao;


@Database(entities = {Paper.class}, version  = 1)
public abstract class Paper_database extends RoomDatabase {

    public abstract Paper_dao dao();

    private static Paper_database INSTANCE;

    public static Paper_database getDbInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Paper_database.class, "PAPERS_DATABASE")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}