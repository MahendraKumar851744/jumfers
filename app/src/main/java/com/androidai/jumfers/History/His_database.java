package com.androidai.jumfers.History;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.androidai.jumfers.Favourites.FavouriteModel;
import com.androidai.jumfers.Favourites.fav_dao;


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