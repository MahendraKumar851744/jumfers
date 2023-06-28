package com.androidai.jumfers;

import com.androidai.jumfers.Favourites.FavouriteModel;

public interface FavItemClickListener {
    void onClick(int position, FavouriteModel fav_model);
}