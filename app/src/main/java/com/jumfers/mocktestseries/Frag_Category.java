package com.jumfers.mocktestseries;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jumfers.mocktestseries.Adapters.Ad_Category;

import com.jumfers.mocktestseries.databases.Categories.CategoryModel;
import com.jumfers.mocktestseries.databases.Categories.cat_dao;
import com.jumfers.mocktestseries.databases.Categories.cat_database;

import java.util.ArrayList;

public class Frag_Category extends Fragment {
    Ad_Category ad_category;
    RecyclerView rv;
    ProgressBar progress_circular;
    CatItemClickListener itemClickListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        assert container != null;
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.frag_favourites,container,false);
        TextView heading = v.findViewById(R.id.heading);
        ImageView iv_back = v.findViewById(R.id.iv_back);
        progress_circular = v.findViewById(R.id.progress_circular);
        iv_back.setVisibility(View.INVISIBLE);
        heading.setText("Categories");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        rv = v.findViewById(R.id.rv_fav);
        cat_database catDb = cat_database.getDbInstance(getContext());
        cat_dao catDao = catDb.dao();
        itemClickListener = new CatItemClickListener() {
            @Override
            public void onClick(int position, CategoryModel cat_model) {
                replaceFragment(new Frag_Sub_Category(), Integer.parseInt(cat_model.getCategory_id()),cat_model.getCategory_title());
            }
        };
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);
        ad_category = new Ad_Category(getContext(), (ArrayList<CategoryModel>) catDao.getAllQuestions(),itemClickListener);
        rv.setAdapter(ad_category);
        return v;
    }
    private void replaceFragment(Fragment fragment,int category_id,String cat_name){
        SharedPreferences sp = getContext().getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("CATEGORY_ID",category_id);
        editor.putString("CATEGORY_NAME",cat_name);
        editor.apply();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
