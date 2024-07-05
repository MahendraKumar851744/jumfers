package com.jumfers.mocktestseries;

import com.jumfers.mocktestseries.databases.Categories.CategoryModel;

public interface CatItemClickListener {
    void onClick(int position, CategoryModel cat_model);
}