package com.jumfers.mocktestseries.databases.PapersList;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.jumfers.mocktestseries.databases.papers.Paper;

import java.util.List;

@Dao
public interface PapersList_dao {

    @Query("SELECT * FROM papersList WHERE cat_id = :categoryId AND subcat_id = :subcategoryId AND type= :type")
    LiveData<List<Paper_Count>> getAllPapers(int categoryId, int subcategoryId,int type);

    @Query("SELECT EXISTS (SELECT 1 FROM papersList WHERE cat_id = :categoryId AND subcat_id = :subcategoryId AND type= :type LIMIT 1)")
    LiveData<Boolean> doesItemExist(int categoryId,int subcategoryId,int type);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Paper_Count item);

    @Delete
    void delete(Paper_Count item);

    @Update
    void update(Paper_Count item);
}
