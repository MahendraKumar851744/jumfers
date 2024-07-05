package com.jumfers.mocktestseries;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jumfers.mocktestseries.Adapters.Ad_Weakness;

import com.jumfers.mocktestseries.databases.weakness.WeaknessModel;
import com.jumfers.mocktestseries.databases.weakness.weak_dao;
import com.jumfers.mocktestseries.databases.weakness.weak_database;

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
