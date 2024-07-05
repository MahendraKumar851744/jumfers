package com.jumfers.mocktestseries.databases.Categories;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {CategoryModel.class}, version  = 1)
public abstract class cat_database extends RoomDatabase {

    public abstract cat_dao dao();

    private static cat_database INSTANCE;

    public static cat_database getDbInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), cat_database.class, "CATEGORY_DATABASE")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}