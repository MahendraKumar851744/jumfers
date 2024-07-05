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

public class Frag_AboutUs extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        assert container != null;
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.frag_aboutus,container,false);
        TextView heading = v.findViewById(R.id.heading);
        ImageView iv_back = v.findViewById(R.id.iv_back);
        heading.setText("About Us");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        return v;
    }



}
