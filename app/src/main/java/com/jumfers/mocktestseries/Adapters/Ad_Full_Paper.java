package com.jumfers.mocktestseries.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.jumfers.mocktestseries.Frag_paper;
import com.jumfers.mocktestseries.databases.PapersList.Paper_Count;
import com.jumfers.mocktestseries.R;

import java.util.ArrayList;

public class Ad_Full_Paper extends RecyclerView.Adapter<Ad_Full_Paper.CategoryViewHolder> {

    Context context;
    ArrayList<Paper_Count> items;

    FragmentManager fragmentManager;
    int type;

    public Ad_Full_Paper(Context context, ArrayList<Paper_Count> items,FragmentManager fragmentManager,int type) {
        this.context = context;
        this.items = items;
        this.fragmentManager = fragmentManager;
        this.type = type;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.lay_sub_wise_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.tv_title.setText(items.get(position).getTitle());
        holder.tv_papers.setText(items.get(position).getPaper_count());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = context.getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("SUBJECT_ID",Integer.parseInt(items.get(position).getId()));
                editor.putString("SUBJECT_TITLE",items.get(position).getTitle());
                editor.apply();
                replaceFragment(new Frag_paper());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_papers;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_papers = itemView.findViewById(R.id.tv_papers);
        }
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


}
