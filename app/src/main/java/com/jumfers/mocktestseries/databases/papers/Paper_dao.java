package com.jumfers.mocktestseries.databases.papers;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.jumfers.mocktestseries.databases.paperCountDb.PapersCount;

import java.util.List;

@Dao
public interface Paper_dao {

    @Query("SELECT * FROM papers WHERE cat_id = :categoryId AND subcat_id = :subcategoryId ")
    LiveData<List<Paper>> getAllPapers(int categoryId, int subcategoryId);

    @Query("SELECT EXISTS (SELECT 1 FROM papers WHERE cat_id = :categoryId AND subcat_id = :subcategoryId LIMIT 1)")
    LiveData<Boolean> doesItemExist(int categoryId,int subcategoryId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Paper item);

    @Delete
    void delete(Paper item);

    @Update
    void update(Paper item);
}
