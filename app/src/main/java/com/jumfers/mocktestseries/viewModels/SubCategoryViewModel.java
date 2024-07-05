package com.jumfers.mocktestseries.viewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jumfers.mocktestseries.databases.SubCategories.SubCategoryItem;
import com.jumfers.mocktestseries.databases.SubCategories.Subcat_database;

import java.util.List;

public class SubCategoryViewModel extends AndroidViewModel {
    private final LiveData<List<SubCategoryItem>> subcats;

    public SubCategoryViewModel(Application application, int cat_id) {
        super(application);

        Subcat_database p_db = Subcat_database.getDbInstance(application);

        subcats = p_db.dao().getAllItems(cat_id);
    }
    public LiveData<List<SubCategoryItem>> getSubCatItems() {
        return subcats;
    }
}
