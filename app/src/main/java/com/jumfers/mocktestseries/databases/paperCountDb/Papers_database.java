package com.jumfers.mocktestseries.databases.paperCountDb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jumfers.mocktestseries.databases.SubCategories.SubCat_dao;
import com.jumfers.mocktestseries.databases.SubCategories.SubCategoryItem;


@Database(entities = {PapersCount.class}, version  = 1)
public abstract class Papers_database extends RoomDatabase {

    public abstract Papers_dao dao();

    private static Papers_database INSTANCE;

    public static Papers_database getDbInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Papers_database.class, "PAPERS_COUNT_DATABASE")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}