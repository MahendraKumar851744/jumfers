package com.androidai.jumfers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidai.jumfers.Adapters.Ad_Favourites;
import com.androidai.jumfers.Adapters.Ad_History;
import com.androidai.jumfers.Favourites.Fav_database;
import com.androidai.jumfers.Favourites.FavouriteModel;
import com.androidai.jumfers.Favourites.fav_dao;
import com.androidai.jumfers.History.His_database;
import com.androidai.jumfers.History.HistoryModel;
import com.androidai.jumfers.History.his_dao;

import java.util.ArrayList;

public class Frag_History extends Fragment {
    Ad_History ad_history;
    RecyclerView rv;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        assert container != null;
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.frag_favourites,container,false);
        TextView heading = v.findViewById(R.id.heading);
        ImageView iv_back = v.findViewById(R.id.iv_back);
        heading.setText("History");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        iv_back.setVisibility(View.INVISIBLE);
        rv = v.findViewById(R.id.rv_fav);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);
        His_database his_database = His_database.getDbInstance(getContext());
        his_dao hisDao = his_database.dao();
        ad_history = new Ad_History(getContext(), (ArrayList<HistoryModel>) hisDao.getAllItems());
        rv.setAdapter(ad_history);

        return v;
    }



}
