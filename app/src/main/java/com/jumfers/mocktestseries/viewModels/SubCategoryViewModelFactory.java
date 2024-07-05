package com.jumfers.mocktestseries.viewModels;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SubCategoryViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private int catId;

    public SubCategoryViewModelFactory(Application application, int catId) {
        this.application = application;
        this.catId = catId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SubCategoryViewModel.class)) {
            return (T) new SubCategoryViewModel(application, catId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
