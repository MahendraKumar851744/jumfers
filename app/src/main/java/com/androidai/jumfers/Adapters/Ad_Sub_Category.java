package com.androidai.jumfers.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.androidai.jumfers.Exam.Model;
import com.androidai.jumfers.Favourites.Fav_database;
import com.androidai.jumfers.Favourites.FavouriteModel;
import com.androidai.jumfers.Favourites.fav_dao;
import com.androidai.jumfers.ItemClickListener;
import com.androidai.jumfers.Models.SubCategoryItem;
import com.androidai.jumfers.R;

import java.util.ArrayList;

public class Ad_Sub_Category extends RecyclerView.Adapter<Ad_Sub_Category.CategoryViewHolder> {

    Context context;
    ArrayList<SubCategoryItem> items;

    ItemClickListener itemClickListener;

    public Ad_Sub_Category(Context context, ArrayList<SubCategoryItem> items, ItemClickListener itemClickListener ) {
        this.context = context;
        this.items = items;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.lay_sub_category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int category_id = items.get(position).getCategory();
        int item_position = position;
        Fav_database fav_database = Fav_database.getDbInstance(context);
        fav_dao favDao = fav_database.dao();
        for(FavouriteModel model:favDao.getAllQuestions()){
            if(model.getCategory_id().matches(String.valueOf(category_id)) && model.getItem_position().matches(String.valueOf(item_position))){
                holder.iv_fav.setImageResource(R.drawable.fav_colored);
            }
        }


        holder.tv_title.setText(items.get(position).getTitle());
        if(items.get(position).getCategory() == 1){
            holder.iv_icon.setImageResource(R.drawable.railway);
        }else if(items.get(position).getCategory() == 2){
            holder.iv_icon.setImageResource(R.drawable.ssc);
        }else if(items.get(position).getCategory() == 3){
            holder.iv_icon.setImageResource(R.drawable.law);
        }else{
            holder.iv_icon.setImageResource(R.drawable.civil);
        }

        holder.itemView.setOnClickListener(v->{
            itemClickListener.onClick(position,items.get(position).getId());
        });

        holder.iv_fav.setOnClickListener(v->{
            int flag = 0;
            for(FavouriteModel model:favDao.getAllQuestions()){
                if(model.getCategory_id().matches(String.valueOf(category_id)) && model.getItem_position().matches(String.valueOf(item_position))){
                    flag = 1;
                    favDao.delete(model);
                    holder.iv_fav.setImageResource(R.drawable.favourite);
                    break;
                }
            }
            if(flag == 0){
                holder.iv_fav.setImageResource(R.drawable.fav_colored);
                SharedPreferences sp = context.getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
                String title = sp.getString("CATEGORY_NAME","");
                favDao.insert(new FavouriteModel(items.get(position).getTitle(),String.valueOf(category_id),title,String.valueOf(item_position)));
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        ImageView iv_icon,iv_fav;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_title = itemView.findViewById(R.id.tv_title);
            iv_fav = itemView.findViewById(R.id.iv_fav);
        }
    }

}
