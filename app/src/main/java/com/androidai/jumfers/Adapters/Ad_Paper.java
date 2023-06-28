package com.androidai.jumfers.Adapters;

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

import com.androidai.jumfers.Frag_test;
import com.androidai.jumfers.ItemClickListener;
import com.androidai.jumfers.Models.Paper;
import com.androidai.jumfers.Models.Paper_Count;
import com.androidai.jumfers.R;

import java.util.ArrayList;

public class Ad_Paper extends RecyclerView.Adapter<Ad_Paper.CategoryViewHolder> {

    Context context;
    ArrayList<Paper> items;

    FragmentManager fragmentManager;
    ItemClickListener itemClickListener;

    public Ad_Paper(Context context, ArrayList<Paper> items, ItemClickListener itemClickListener) {
        this.context = context;
        this.items = items;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.paper_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.tv_title.setText(items.get(position).getTitle());
        holder.tv_num_of_qn.setText("Number of Questions: "+items.size());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = context.getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("PAPER_NAME",items.get(position).getTitle());
                editor.putString("PAPER_ID", items.get(position).getId());
                editor.apply();
                itemClickListener.onClick(position,Integer.parseInt(items.get(position).getId()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_num_of_qn;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_num_of_qn = itemView.findViewById(R.id.tv_num_of_qn);
        }
    }



}
