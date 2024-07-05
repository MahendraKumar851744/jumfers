package com.jumfers.mocktestseries.databases.SubCategories;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jumfers.mocktestseries.databases.Categories.CategoryModel;
import com.jumfers.mocktestseries.databases.Categories.cat_dao;


@Database(entities = {SubCategoryItem.class}, version  = 2)
public abstract class Subcat_database extends RoomDatabase {

    public abstract SubCat_dao dao();

    private static Subcat_database INSTANCE;

    public static Subcat_database getDbInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Subcat_database.class, "SUBCATEGORY_DATABASE")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}