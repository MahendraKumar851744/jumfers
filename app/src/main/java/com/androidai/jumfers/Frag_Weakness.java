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
import com.androidai.jumfers.Adapters.Ad_Weakness;
import com.androidai.jumfers.Favourites.Fav_database;
import com.androidai.jumfers.Favourites.FavouriteModel;
import com.androidai.jumfers.Favourites.fav_dao;
import com.androidai.jumfers.weakness.WeaknessModel;
import com.androidai.jumfers.weakness.weak_dao;
import com.androidai.jumfers.weakness.weak_database;

import java.util.ArrayList;

public class Frag_Weakness extends Fragment {
    Ad_Weakness ad_weakness;
    RecyclerView rv;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        assert container != null;
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.frag_favourites,container,false);
        TextView heading = v.findViewById(R.id.heading);
        ImageView iv_back = v.findViewById(R.id.iv_back);
        heading.setText("Weakness");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        rv = v.findViewById(R.id.rv_fav);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);
        weak_database weakDatabase = weak_database.getDbInstance(getContext());
        weak_dao weakDao = weakDatabase.dao();
        ad_weakness = new Ad_Weakness(getContext(), (ArrayList<WeaknessModel>) weakDao.getAllQuestions());
        rv.setAdapter(ad_weakness);
        return v;
    }



}
