package com.jumfers.mocktestseries.viewModels;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PapersListViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private int catId;
    private int subCat;
    private int type;

    public PapersListViewModelFactory(Application application, int catId,int subCat,int type) {
        this.application = application;
        this.catId = catId;
        this.subCat = subCat;
        this.type = type;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PapersListViewModel.class)) {
            return (T) new PapersListViewModel(application, catId,subCat,type);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
