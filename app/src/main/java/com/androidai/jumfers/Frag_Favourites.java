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
import com.androidai.jumfers.Adapters.Ad_Sub_Category;
import com.androidai.jumfers.Favourites.Fav_database;
import com.androidai.jumfers.Favourites.FavouriteModel;
import com.androidai.jumfers.Favourites.fav_dao;

import java.util.ArrayList;

public class Frag_Favourites extends Fragment {
    Ad_Favourites ad_favourites;
    RecyclerView rv;
    FavItemClickListener itemClickListener;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        assert container != null;
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.frag_favourites,container,false);
        TextView heading = v.findViewById(R.id.heading);
        ImageView iv_back = v.findViewById(R.id.iv_back);
        heading.setText("Favourites");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        rv = v.findViewById(R.id.rv_fav);
        itemClickListener = new FavItemClickListener() {
            @Override
            public void onClick(int position, FavouriteModel fav_model) {
                SharedPreferences sp = getContext().getSharedPreferences("BASEAPP", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("CATEGORY_ID",Integer.parseInt(fav_model.getCategory_id()));
                editor.putString("CATEGORY_NAME",fav_model.getCategory_name());
                editor.apply();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFragment, new Frag_Sub_Category());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        };
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);
        Fav_database fav_database = Fav_database.getDbInstance(getContext());
        fav_dao favDao = fav_database.dao();

        ad_favourites = new Ad_Favourites(getContext(), (ArrayList<FavouriteModel>) favDao.getAllQuestions(),itemClickListener);
        rv.setAdapter(ad_favourites);
        return v;
    }



}
