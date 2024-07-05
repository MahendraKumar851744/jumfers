package com.jumfers.mocktestseries.databases.SubCategories;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.jumfers.mocktestseries.databases.Categories.CategoryModel;

import java.util.List;

@Dao
public interface SubCat_dao {

    @Query("SELECT * FROM subcategories WHERE cat_id = :categoryId")
    LiveData<List<SubCategoryItem>> getAllItems(int categoryId);

    @Query("SELECT EXISTS (SELECT 1 FROM subcategories WHERE cat_id = :categoryId LIMIT 1)")
    LiveData<Boolean> doesItemExist(int categoryId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SubCategoryItem item);

    @Delete
    void delete(SubCategoryItem item);

    @Update
    void update(SubCategoryItem item);
}
