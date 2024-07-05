package com.jumfers.mocktestseries.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jumfers.mocktestseries.CatItemClickListener;

import com.jumfers.mocktestseries.R;
import com.jumfers.mocktestseries.databases.Categories.CategoryModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Ad_Category extends RecyclerView.Adapter<Ad_Category.CategoryViewHolder> {

    Context context;
    ArrayList<CategoryModel> items;
    CatItemClickListener itemClickListener;


    public Ad_Category(Context context, ArrayList<CategoryModel> items,CatItemClickListener itemClickListener) {
        this.context = context;
        this.items = items;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.cat_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_title.setText(items.get(position).getCategory_title());
        Glide.with(context).load("https://test.jenacademy.in/storage/category/"+items.get(position).getCategory_img()).into(holder.img);
        holder.itemView.setOnClickListener(view -> {
            itemClickListener.onClick(position,items.get(position));
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        ImageView img;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }

}
