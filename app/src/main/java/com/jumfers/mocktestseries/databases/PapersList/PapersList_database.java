package com.jumfers.mocktestseries.databases.PapersList;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jumfers.mocktestseries.databases.papers.Paper;
import com.jumfers.mocktestseries.databases.papers.Paper_dao;


@Database(entities = {Paper_Count.class}, version  = 2)
public abstract class PapersList_database extends RoomDatabase {

    public abstract PapersList_dao dao();

    private static PapersList_database INSTANCE;

    public static PapersList_database getDbInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PapersList_database.class, "PAPERS_LIST_DATABASE")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}