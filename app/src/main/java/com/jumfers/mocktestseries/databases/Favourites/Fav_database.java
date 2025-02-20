package com.jumfers.mocktestseries.databases.Favourites;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {FavouriteModel.class}, version  = 2)
public abstract class Fav_database extends RoomDatabase {

    public abstract fav_dao dao();

    private static Fav_database INSTANCE;

    public static Fav_database getDbInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Fav_database.class, "FAVOURITE_DATABASE")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}