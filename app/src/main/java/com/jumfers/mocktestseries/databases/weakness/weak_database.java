package com.jumfers.mocktestseries.databases.weakness;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {WeaknessModel.class}, version  = 1)
public abstract class weak_database extends RoomDatabase {

    public abstract weak_dao dao();

    private static weak_database INSTANCE;

    public static weak_database getDbInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), weak_database.class, "WEAKNESS_DATABASE")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}