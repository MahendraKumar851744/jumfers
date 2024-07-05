package com.jumfers.mocktestseries;

import com.jumfers.mocktestseries.databases.Favourites.FavouriteModel;

public interface FavItemClickListener {
    void onClick(int position, FavouriteModel fav_model);
}