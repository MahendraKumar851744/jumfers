package com.jumfers.mocktestseries.viewModels;

import static java.security.AccessController.getContext;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jumfers.mocktestseries.databases.PapersList.Paper_Count;
import com.jumfers.mocktestseries.databases.PapersList.PapersList_database;
import com.jumfers.mocktestseries.databases.SubCategories.SubCategoryItem;
import com.jumfers.mocktestseries.databases.SubCategories.Subcat_database;

import java.util.List;

public class PapersListViewModel extends AndroidViewModel {
    private final LiveData<List<Paper_Count>> papers;

    public PapersListViewModel(Application application, int cat_id,int subcat,int type) {
        super(application);

        PapersList_database pdb = PapersList_database.getDbInstance(application);

        papers = pdb.dao().getAllPapers(cat_id,subcat,type);
    }
    public LiveData<List<Paper_Count>> getPapers() {
        return papers;
    }
}
