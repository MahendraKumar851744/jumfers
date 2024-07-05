package com.jumfers.mocktestseries.databases.paperCountDb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.jumfers.mocktestseries.databases.SubCategories.SubCategoryItem;

import java.util.List;

@Dao
public interface Papers_dao {

    @Query("SELECT * FROM papersCount WHERE cat_id = :categoryId AND subcat_id = :subcategoryId LIMIT 1 ")
    PapersCount getAllItems(int categoryId,int subcategoryId);

    @Query("SELECT EXISTS (SELECT 1 FROM papersCount WHERE cat_id = :categoryId AND subcat_id = :subcategoryId LIMIT 1)")
    LiveData<Boolean> doesItemExist(int categoryId,int subcategoryId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PapersCount item);

    @Delete
    void delete(PapersCount item);

    @Update
    void update(PapersCount item);
}
