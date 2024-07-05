package com.jumfers.mocktestseries;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.jumfers.mocktestseries.Adapters.Ad_Full_Paper;

public class Frag_sub_topics extends Fragment {


    public Frag_sub_topics() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Ad_Full_Paper ad_full_paper;
    RecyclerView rv;

    Context context;

    ProgressBar progress_circular;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_frag_sub_topics, container, false);


        return v;
    }


}